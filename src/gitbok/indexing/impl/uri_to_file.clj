(ns gitbok.indexing.impl.uri-to-file
  (:require
   [clojure.tools.logging :as log]
   [clojure.string :as str]
   [gitbok.constants :as const]
   [gitbok.indexing.impl.summary]
   [gitbok.indexing.impl.redirects :as redirects]
   [gitbok.products :as products]
   [clojure.data]
   [system]))

(defn uri->filepath [uri->file-idx ^String uri]
  (when uri
    (let [;; Handle root path "/" specially - map it to empty string
          uri (cond
                (= uri "/") ""
                (and (> (count uri) 0) (= "/" (subs uri 0 1))) (subs uri 1)
                :else uri)
          ;; Remove trailing slash if present
          fixed-url (if (and (> (count uri) 0)
                             (= "/" (subs uri (dec (count uri)))))
                      (subs uri 0 (dec (count uri)))
                      uri)
          ;; Handle anchors in redirects
          filepath (get uri->file-idx fixed-url)]
      (if (and filepath (str/includes? filepath "#"))
        (str/replace filepath #"#.*$" "")
        filepath))))

(defn uri->file-idx
  [context]
  (let [summary-text (gitbok.indexing.impl.summary/read-summary context)
        lines (str/split-lines summary-text)
        redirects-raw (redirects/redirects context)
        ;; Process redirects: remove leading slash and clean up README.md paths
        redirects (->> redirects-raw
                       (mapv (fn [[k v]] 
                               (let [from-url (subs (str k) 1)
                                     ;; Clean up the target path similar to how files are processed
                                     to-path-clean (str/replace v #"\.md$" "")
                                     to-url (cond
                                              ;; Root README
                                              (= to-path-clean "README")
                                              ""
                                              
                                              ;; Directory README files
                                              (str/ends-with? to-path-clean "/README")
                                              (subs to-path-clean 0 (- (count to-path-clean) 7))
                                              
                                              ;; Regular files
                                              :else
                                              to-path-clean)]
                                 [from-url to-url])))
                       (into {}))
        index
        (loop [lines lines
               acc {}]
          (if (empty? lines)
            acc
            (let [line (first lines)
                  trimmed (str/trim line)]
              (cond
                ;; Skip headers
                (or (str/starts-with? trimmed "# ")
                    (str/starts-with? trimmed "## "))
                (recur (rest lines) acc)

                ;; Process list items with links
                (re-matches #"\s*\*\s+\[([^\]]+)\]\(([^)]+)\)" line)
                (let [[_ _title path]
                      (re-matches #"\s*\*\s+\[([^\]]+)\]\(([^)]+)\)" line)]
                  ;; Skip external links
                  (if (str/starts-with? path "http")
                    (recur (rest lines) acc)
                    (let [;; Clean up the path - remove .md extension
                          clean-path (str/replace path #"\.md$" "")
                          ;; Create URL from file path
                          url (cond
                                ;; Root README
                                (= clean-path "README")
                                ""

                                ;; Directory README files
                                (str/ends-with? clean-path "/README")
                                (subs clean-path 0 (- (count clean-path) 7))

                                ;; Regular files
                                :else
                                clean-path)]
                      (recur (rest lines)
                             (assoc acc url path)))))

                :else
                (recur (rest lines) acc)))))]
    
    (log/info "index ready" {:type "uri->file" 
                             :files (count index)
                             :redirects (count redirects)})
    ;; Return separate indexes for files and redirects
    {:files index
     :redirects redirects}))

(defn get-idx [context]
  (products/get-product-state context [const/URI->FILE_IDX]))

(defn set-idx [context]
  (let [indexes (uri->file-idx context)]
    ;; Store files index
    (products/set-product-state
     context [const/URI->FILE_IDX]
     (:files indexes))
    ;; Store redirects index
    (products/set-product-state
     context [const/URI->REDIRECTS_IDX]
     (:redirects indexes))))

(defn get-redirects-idx [context]
  (products/get-product-state context [const/URI->REDIRECTS_IDX]))

(defn set-redirects-idx [context]
  (let [indexes (uri->file-idx context)]
    (products/set-product-state
     context [const/URI->REDIRECTS_IDX]
     (:redirects indexes))))
