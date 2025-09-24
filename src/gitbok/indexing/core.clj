(ns gitbok.indexing.core
  (:require
   [gitbok.http :as http]
   [clojure.tools.logging :as log]
   [clojure.java.io :as io]
   [gitbok.indexing.impl.summary]
   [gitbok.indexing.impl.uri-to-file :as uri-to-file]
   [gitbok.products :as products]
   [gitbok.lastmod.generator :as lastmod-gen]
   [gitbok.state :as state]
   [clojure.string :as str]
   [gitbok.utils :as utils]))

(set! *warn-on-reflection* true)

(defn get-filepath [filepath-a relative-filepath]
  (.getCanonicalPath
   (io/file (.getParent (io/file filepath-a)) relative-filepath)))

(def lastmod-key :gitbok.indexing.core/lastmod)

(defn uri->filepath [context ^String uri]
  (let [idx (state/get-uri-to-file-idx context)
        filepath (gitbok.indexing.impl.uri-to-file/uri->filepath idx uri)]
    filepath))

(defn get-redirect [context ^String uri]
  (let [redirects-idx (state/get-redirects-idx context)
        redirect (get redirects-idx uri)]
    redirect))

(defn absolute-filepath->relative
  "Converts an absolute filepath to a relative filepath by removing the product root prefix.
  This ensures the filepath matches the keys used in the file->uri index."
  [context absolute-filepath]
  (let [;config (products/get-current-product context)
        product-root (products/filepath context "")
        ;; Remove trailing slash from product root if present
        product-root (if (str/ends-with? product-root "/")
                       (subs product-root 0 (dec (count product-root)))
                       product-root)]
    (if (str/starts-with? absolute-filepath product-root)
      (utils/safe-subs absolute-filepath (inc (count product-root)))
      absolute-filepath)))

(defn page-link->uri [context
                      ^String current-page-filepath
                      ^String relative-page-link]
  (let [current-page-filepath (str/replace
                               current-page-filepath
                               #"#.*$" "")

        section
        (last (re-matches #".*#(.*)" relative-page-link))

        relative-page-link
        (str/replace relative-page-link #"#.*$" "")

        relative-page-link
        (if (str/ends-with? (str/lower-case relative-page-link) "/")
          (str relative-page-link "README.md")
          relative-page-link)

        current-page-filename
        (last (str/split current-page-filepath #"/"))

        same-page? (= current-page-filename relative-page-link)]
    (if same-page?
      (str "#" section)
      (let [file->uri-idx (state/get-file-to-uri-idx context)
            _ (when-not file->uri-idx (throw (Exception. "no idx")))
            ;; Convert absolute filepath to relative filepath that matches the index
            current-page-filepath (absolute-filepath->relative context current-page-filepath)
            path
            (utils/safe-subs
             (get-filepath
              current-page-filepath
              relative-page-link)
             (count
              (System/getProperty
               "user.dir")))

            path (if (str/starts-with? path "/")
                   (utils/safe-subs path 1)
                   path)
            path (http/get-product-prefixed-url
                  context
                  (str "/" (:uri (get file->uri-idx path))))]

        (if section
          (str path "#" section)
          path)))))

(defn read-content [context filepath]
  (state/slurp-resource context filepath))

(defn slurp-md-files! [context filepaths-from-summary]
  (let [;; Filter out http URLs
        start (System/currentTimeMillis)
        local-files (filter #(not (str/starts-with? % "http")) filepaths-from-summary)
        ;; Chunk files for better parallel processing
        ;; Process in batches to reduce future overhead
        batch-size 50
        batches (partition-all batch-size local-files)
        ;; Process batches in parallel, files within batch sequentially
        files (->> batches
                   (pmap (fn [batch]
                           ;; Process files within batch sequentially
                           (map (fn [filename]
                                  [filename
                                   (read-content context (products/filepath context filename))])
                                batch)))
                   doall
                   (mapcat identity)
                   (into {}))]
    (log/info "files loaded" {:count (count files)
                              :duration-ms (- (System/currentTimeMillis) start)})
    files))

(defn set-md-files-idx [context file-to-uri-idx]
  (state/set-md-files-idx! context (slurp-md-files! context (keys file-to-uri-idx))))

(defn get-md-files-idx [context]
  (state/get-md-files-idx context))

(defn filepath->href [context filepath href]
  (if (str/starts-with? href "http")
    href
    (-> (page-link->uri
         context
         filepath
         href))))

(defn get-lastmod [context filepath]
  (when filepath
    (let [lastmod-map (products/get-product-state context [lastmod-key])
          ;; Normalize the filepath to match lastmod keys
          ;; Lastmod keys are relative paths from docs directory
          ;; filepath might be "../docs/path/to/file.md" or "docs/path/to/file.md"
          normalized-path (cond
                           ;; If path contains /docs/, take everything after it
                            (str/includes? filepath "/docs/")
                            (second (str/split filepath #"/docs/" 2))

                           ;; If path starts with docs/, remove it
                            (str/starts-with? filepath "docs/")
                            (subs filepath 5)

                           ;; Otherwise use as is
                            :else filepath)]
      (get lastmod-map normalized-path))))

(defn set-lastmod
  [context]
   ;; New API - works with state directly
  (let [product-config (or (:product context)
                           (products/get-current-product context))
        product-id (:id product-config)
        volume-path (or (gitbok.state/get-config context :docs-volume-path) ".")

         ;; Build docs path from product config
        config-path (:config product-config)
        config-dir (utils/parent config-path)
        root (or (-> product-config :structure :root)
                 (:root product-config)
                 "./docs")
         ;; Remove leading "./" from root if present
        root (if (str/starts-with? root "./")
               (subs root 2)
               root)

         ;; Construct full docs path
        docs-path (.getPath (io/file volume-path config-dir root))

         ;; Generate lastmod data in memory with caching
        lastmod-data (if (.exists (io/file docs-path))
                       (lastmod-gen/generate-or-get-cached-lastmod
                        context
                        product-id
                        docs-path)
                       {})]

    (log/info "✅lastmod set"
              {:product product-id
               :docs-path docs-path
               :entries (count lastmod-data)})
    (gitbok.state/set-product-state! context [lastmod-key] lastmod-data)))
