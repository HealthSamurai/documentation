(ns gitbok.dev.page-relocator
  (:require
   [clojure.string :as str]
   [clojure.java.io :as io]
   [gitbok.indexing.core :as indexing]
   [gitbok.indexing.impl.summary :as summary]
   [gitbok.indexing.impl.uri-to-file :as uri-to-file]
   [gitbok.indexing.impl.file-to-uri :as file-to-uri]
   [gitbok.markdown.core :as markdown]
   [gitbok.utils :as utils]
   [edamame.core :as edamame]))

(set! *warn-on-reflection* true)

(defn normalize-file-path
  "Normalize file path relative to docs directory"
  [file-path]
  (-> file-path
      (str/replace #"^\.?/?docs/" "")
      (str/replace #"^/" "")))

(defn get-current-page-info
  "Get current page info from URI"
  [context uri]
  (println "DEBUG: get-current-page-info called with URI:" uri)
  (let [uri-without-prefix (if (str/starts-with? uri "/")
                             (subs uri 1)
                             uri)
        _ (println "DEBUG: uri-without-prefix:" uri-without-prefix)
        filepath (indexing/uri->filepath context uri-without-prefix)
        _ (println "DEBUG: filepath from indexing:" filepath)]
    (if filepath
      (do
        (println "DEBUG: Found filepath, returning page info")
        {:filepath (normalize-file-path filepath)
         :uri uri-without-prefix
         :current-url uri})
      (do
        (println "DEBUG: No filepath found, returning nil")
        ; Return nil if no filepath found (like for root or generated pages)
        nil))))

(defn preview-new-url
  "Preview new URL based on new file path"
  [context new-file-path]
  (let [normalized-path (normalize-file-path new-file-path)
        ; Remove .md extension and convert to URI-friendly format
        uri-path (-> normalized-path
                     (str/replace #"\.md$" "")
                     (str/replace #"/README$" "")
                     (str/replace #"^README$" "")
                     (str/replace #"/" "/"))]
    (if (str/blank? uri-path)
      "/"
      (str "/" uri-path))))

(defn validate-file-path
  "Validate new file path"
  [new-file-path]
  (let [normalized (normalize-file-path new-file-path)]
    (cond
      (str/blank? normalized)
      {:valid false :error "File path cannot be empty"}

      (not (str/ends-with? normalized ".md"))
      {:valid false :error "File path must end with .md"}

      (str/includes? normalized "..")
      {:valid false :error "File path cannot contain .. (parent directory references)"}

      (not (re-matches #"^[a-zA-Z0-9/_\-\.]+$" normalized))
      {:valid false :error "File path contains invalid characters"}

      :else
      {:valid true})))

(defn move-file
  "Move file from old location to new location"
  [old-filepath new-filepath]
  (let [old-file (io/file (str "docs/" old-filepath))
        new-file (io/file (str "docs/" new-filepath))
        new-dir (.getParentFile new-file)]

    (when-not (.exists old-file)
      (throw (ex-info "Source file does not exist" {:old-filepath old-filepath})))

    (when (.exists new-file)
      (throw (ex-info "Target file already exists" {:new-filepath new-filepath})))

    ; Create parent directories if they don't exist
    (when new-dir
      (.mkdirs new-dir))

    ; Move the file
    (when-not (.renameTo old-file new-file)
      (throw (ex-info "Failed to move file" {:old-filepath old-filepath :new-filepath new-filepath})))

    true))

(defn update-gitbook-yaml
  "Update .gitbook.yaml with redirect from old URL to new URL"
  [old-url new-url]
  (let [yaml-file (io/file ".gitbook.yaml")
        content (slurp yaml-file)
        ; Create redirect entry
        redirect-entry (str "    " old-url ": " new-url)
        ; Find redirects section and add new entry
        updated-content (if (str/includes? content "redirects:")
                          ; Add to existing redirects section
                          (str/replace content
                                       #"(redirects:\s*\n)"
                                       (str "$1" redirect-entry "\n"))
                          ; Add redirects section if it doesn't exist
                          (str content "\n\nredirects:\n" redirect-entry "\n"))]

    (spit yaml-file updated-content)))

(defn update-summary-md
  "Update docs/SUMMARY.md with new file location"
  [old-filepath new-filepath]
  (let [summary-file (io/file "docs/SUMMARY.md")
        content (slurp summary-file)
        ; Replace old filepath with new filepath
        updated-content (str/replace content old-filepath new-filepath)]

    (when (= content updated-content)
      (throw (ex-info "File path not found in SUMMARY.md" {:old-filepath old-filepath})))

    (spit summary-file updated-content)))

(defn regenerate-system-indices
  "Regenerate all system indices after file relocation"
  [context]
  (println "Regenerating system indices...")
  ; 1. read summary. create toc htmx.
  (summary/set-summary context)
  ; 2. get uris from summary (using slugging), merge with redirects
  (uri-to-file/set-idx context)
  ; 3. reverse file to uri idx
  (file-to-uri/set-idx context)
  ; 4. using files from summary (step 3), read all files into memory
  (indexing/set-md-files-idx
   context
   (file-to-uri/get-idx context))
  ; 5. parse all files into memory, some things are already rendered as plain html
  (markdown/set-parsed-markdown-index
   context
   (indexing/get-md-files-idx context))
  ; 6. using parsed markdown, set search index
  (indexing/set-search-idx
   context
   (markdown/get-parsed-markdown-index context))
  (println "System indices regenerated successfully"))

(defn find-reference-files
  "Find all markdown files that reference the old filepath"
  [old-filepath]
  (let [docs-dir (io/file "docs")]
    (when (.exists docs-dir)
      (filter
       #(and (.isFile %)
             (.endsWith (.getName %) ".md")
             (not= (.getName %) "SUMMARY.md"))
       (file-seq docs-dir)))))

(defn scan-and-update-references
  "Scan and update internal references to moved page"
  [old-filepath new-filepath]
  (let [reference-files (find-reference-files old-filepath)
        old-relative-path old-filepath
        new-relative-path new-filepath
        updates-made (atom [])]

    (println (str "Scanning " (count reference-files) " files for references to " old-filepath))

    (doseq [file reference-files]
      (let [content (slurp file)
            ; Look for various reference patterns
            patterns [; Markdown links: [text](old-path)
                      (re-pattern (str "\\]\\(" (java.util.regex.Pattern/quote old-relative-path) "\\)"))
                     ; Markdown links without .md: [text](old-path-without-md)
                      (re-pattern (str "\\]\\(" (java.util.regex.Pattern/quote (str/replace old-relative-path #"\.md$" "")) "\\)"))
                     ; Raw filepath references
                      (re-pattern (java.util.regex.Pattern/quote old-relative-path))]
            replacements [(str "](" new-relative-path ")")
                          (str "](" (str/replace new-relative-path #"\.md$" "") ")")
                          new-relative-path]

            updated-content (reduce-kv
                             (fn [content idx pattern]
                               (let [replacement (nth replacements idx)]
                                 (str/replace content pattern replacement)))
                             content
                             (vec patterns))]

        (when-not (= content updated-content)
          (spit file updated-content)
          (swap! updates-made conj (.getPath file))
          (println (str "Updated references in: " (.getPath file))))))

    @updates-made))

(defn relocate-page
  "Execute complete page relocation with reference scanning"
  [context current-filepath new-filepath]
  (println "🚀 === PAGE RELOCATION STARTED ===")
  (println "📂 From:" current-filepath)
  (println "📂 To:" new-filepath)

  (let [validation (validate-file-path new-filepath)]
    (println "✅ Path validation:" validation)

    (if-not (:valid validation)
      {:success false :error (:error validation)}

      (try
        (let [normalized-old (normalize-file-path current-filepath)
              normalized-new (normalize-file-path new-filepath)
              old-url (indexing/filepath->uri context normalized-old)
              new-url (preview-new-url context normalized-new)]

          (println "📋 Normalized paths:")
          (println "  Old:" normalized-old)
          (println "  New:" normalized-new)
          (println "📋 URLs:")
          (println "  Old URL:" old-url)
          (println "  New URL:" new-url)

          ; Perform all operations
          (println "📁 Step 1: Moving file...")
          (move-file normalized-old normalized-new)
          (println "✅ File moved successfully")

          (println "🔗 Step 2: Updating .gitbook.yaml redirects...")
          (update-gitbook-yaml old-url new-url)
          (println "✅ Redirects updated")

          (println "📋 Step 3: Updating SUMMARY.md...")
          (update-summary-md normalized-old normalized-new)
          (println "✅ SUMMARY.md updated")

          ; Scan and update internal references
          (println "🔍 Step 4: Scanning and updating references...")
          (let [updated-files (scan-and-update-references normalized-old normalized-new)]
            (println (str "✅ Updated references in " (count updated-files) " files"))

            ; Regenerate system indices
            (println "🔄 Step 5: Regenerating system indices...")
            (regenerate-system-indices context)
            (println "✅ System indices regenerated")

            (println "🎉 === PAGE RELOCATION COMPLETED SUCCESSFULLY ===")
            {:success true
             :old-filepath normalized-old
             :new-filepath normalized-new
             :old-url old-url
             :new-url new-url
             :updated-reference-files (count updated-files)
             :reference-files updated-files}))

        (catch Exception e
          (println "❌ === PAGE RELOCATION FAILED ===")
          (println "❌ Error:" (.getMessage e))
          (println "📋 Error type:" (.getClass e))
          {:success false :error (.getMessage e) :error-type (.getName (.getClass e))})))))