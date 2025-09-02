(ns gitbok.indexing.core
  (:require
   [klog.core :as log]
   [gitbok.indexing.impl.summary]
   [gitbok.indexing.impl.uri-to-file :as uri-to-file]
   [gitbok.indexing.impl.common :as common]
   [gitbok.indexing.impl.search-index :as search-index]
   [gitbok.indexing.impl.meilisearch :as meilisearch]
   [gitbok.indexing.impl.file-to-uri :as file-to-uri]
   [gitbok.constants :as const]
   [gitbok.products :as products]
   [gitbok.lastmod.generator :as lastmod-gen]
   [edamame.core :as edamame]
   [system]
   [clojure.string :as str]
   [clojure.java.io :as io]
   [gitbok.utils :as utils]
   [gitbok.http]
   [uui]
   [http]))

(set! *warn-on-reflection* true)

(defn uri->file-idx
  "Creates index to get filepath by uri.

  We read SUMMARY.md and use page names to create urls.
  This is used to match uri from the request to actual file to render."
  [context]
  (uri-to-file/get-idx context))

(defn file->uri-idx
  "Creates index to get uri by filepath.

  We use it to add href attribute to links when we render markdown link."
  [context]
  (gitbok.indexing.impl.file-to-uri/file->uri-idx context))

(defn filepath->uri [context filepath]
  (gitbok.indexing.impl.file-to-uri/filepath->uri context filepath))

(defn uri->filepath [context ^String uri]
  (gitbok.indexing.impl.uri-to-file/uri->filepath
   (uri-to-file/get-idx context) uri))

(defn absolute-filepath->relative
  "Converts an absolute filepath to a relative filepath by removing the product root prefix.
  This ensures the filepath matches the keys used in the file->uri index."
  [context absolute-filepath]
  (let [config (products/get-current-product context)
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
      (let [file->uri-idx (file-to-uri/get-idx context)
            _ (when-not file->uri-idx (throw (Exception. "no idx")))
            _ (def file->uri-idx file->uri-idx)
            _ (def current-page-filepath current-page-filepath)
            ;; Convert absolute filepath to relative filepath that matches the index
            current-page-filepath (absolute-filepath->relative context current-page-filepath)
            path
            (utils/safe-subs
             (common/get-filepath
              current-page-filepath
              relative-page-link)
             (count
              (System/getProperty
               "user.dir")))

            path (if (str/starts-with? path "/")
                   (utils/safe-subs path 1)
                   path)
            _ (def path1 path)
            path (gitbok.http/get-product-prefixed-url
                  context
                  (str "/" (:uri (get file->uri-idx path))))]
        (def path path)

        (if section
          (str path "#" section)
          path)))))

(defn create-search-index
  [parsed-md-index]
  (let [si (search-index/parsed-md-idx->index parsed-md-index)]
    (log/info ::search-index-created {:entries (count si)})
    si))

(defn read-content [filepath]
  (utils/slurp-resource filepath))

(defn slurp-md-files! [context filepaths-from-summary]
  (let [files (reduce (fn [acc filename]
                        (if (str/starts-with? filename "http")
                          acc
                          (let [full-path (products/filepath context filename)]
                            ;; Use original filename as key, not the full path
                            (assoc acc filename
                                   (read-content full-path))))) {}
                      (filter #(not (str/starts-with? % "http"))
                              filepaths-from-summary))]
    (log/info ::files-loaded {:count (count files)})
    files))

(defn set-md-files-idx [context file-to-uri-idx]
  (products/set-product-state
   context
   [const/MD_FILES_IDX]
   (slurp-md-files! context (keys file-to-uri-idx))))

(defn get-md-files-idx [context]
  (products/get-product-state
   context
   [const/MD_FILES_IDX]))

(defn set-search-idx
  [context parsed-md-index]
  (products/set-product-state
   context
   [const/SEARCH_IDX]
   (create-search-index parsed-md-index)))

(defn get-search-idx [context]
  (products/get-product-state context
                              [const/SEARCH_IDX]))

(defn search [context q]
  (try
    (meilisearch/search q)
    (catch Exception e
      (log/warn ::meilisearch-fallback {:error (.getMessage e)})
      (search-index/search (get-search-idx context) q))))

(defn filepath->href [context filepath href]
  (if (str/starts-with? href "http")
    href
    (-> (page-link->uri
         context
         filepath
         href))))

(defn get-lastmod [context filepath]
  (when filepath
    (let [lastmod-map (products/get-product-state context [const/LASTMOD])
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

(defn set-lastmod [context]
  (let [product-id (products/get-current-product-id context)
        product-config (products/get-current-product context)
        
        ;; Get paths from product config and environment
        volume-path (or (System/getenv "DOCS_VOLUME_PATH") ".")
        
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
    
    (log/info ::✅lastmod-set {:product product-id 
                             :docs-path docs-path
                             :entries (count lastmod-data)})
    (products/set-product-state
     context
     [const/LASTMOD]
     lastmod-data)))
