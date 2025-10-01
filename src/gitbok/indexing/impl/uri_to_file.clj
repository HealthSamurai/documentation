(ns gitbok.indexing.impl.uri-to-file
  (:require
   [clojure.tools.logging :as log]
   [clojure.string :as str]
   [gitbok.indexing.impl.summary]
   [gitbok.indexing.impl.redirects :as redirects]
   [gitbok.products :as products]
   [clojure.data]))

(defn uri->filepath [uri->file-idx ^String uri]
  (when uri
    (let [;; First remove any fragment from the URI
          uri-without-fragment (if (str/includes? uri "#")
                                 (first (str/split uri #"#"))
                                 uri)
          ;; Handle root path "/" specially - map it to empty string
          uri (cond
                (= uri-without-fragment "/") ""
                (and (> (count uri-without-fragment) 0)
                     (= "/" (subs uri-without-fragment 0 1)))
                (subs uri-without-fragment 1)
                :else uri-without-fragment)
          ;; Remove trailing slash if present
          fixed-url (if (and (> (count uri) 0)
                             (= "/" (subs uri (dec (count uri)))))
                      (subs uri 0 (dec (count uri)))
                      uri)
          ;; Look up filepath in index
          filepath (get uri->file-idx fixed-url)]
      ;; Remove any fragment from the filepath if present
      (if (and filepath (str/includes? filepath "#"))
        (str/replace filepath #"#.*$" "")
        filepath))))

(defn uri->file-idx
  [context]
  (let [summary-text (gitbok.indexing.impl.summary/read-summary context)
        lines (str/split-lines summary-text)
        redirects-raw (redirects/redirects context)
        readme-path (products/readme-relative-path context)
        ;; Get readme-mode from product config
        product (products/get-current-product context)
        readme-mode (get product :readme-mode "root-only")
        preserve-original? (= readme-mode "preserve-original-path")
        ;; Process redirects: remove leading slash and clean up README.md paths
        redirects (->> redirects-raw
                       (mapv (fn [[k v]]
                               (let [from-url (subs (str k) 1)
                                     ;; Clean up the target path similar to how files are processed
                                     to-path-clean (str/replace v #"\.md$" "")
                                     to-url (cond
                                              ;; Root README - check against configured readme path
                                              (and readme-path
                                                   (= v readme-path))
                                              ""

                                              ;; Also check clean path
                                              (and readme-path
                                                   (= to-path-clean (str/replace readme-path #"\.md$" "")))
                                              ""

                                              ;; Legacy fallback
                                              (= to-path-clean "README")
                                              ""

                                              ;; Directory README files
                                              (str/ends-with? to-path-clean "/README")
                                              (subs to-path-clean 0 (- (count to-path-clean) 7))

                                              ;; Regular files
                                              :else
                                              to-path-clean)]
                                 ;; Don't create redirect if from and to are the same
                                 (when (not= from-url to-url)
                                   [from-url to-url]))))
                       (remove nil?)
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
                                ;; If preserve-original? is true, keep the original path
                                (and readme-path
                                     (= path readme-path)
                                     preserve-original?)
                                (-> path
                                    (str/replace #"\.md$" "")
                                    (str/replace #"/README$" ""))

                                ;; Root README - check against configured readme path (root-only mode)
                                (and readme-path
                                     (= path readme-path))
                                ""

                                ;; Also check clean path (without .md)
                                (and readme-path
                                     (= clean-path (str/replace readme-path #"\.md$" "")))
                                ""

                                ;; Legacy fallback
                                (= clean-path "README")
                                ""

                                ;; Directory README files
                                (str/ends-with? clean-path "/README")
                                (subs clean-path 0 (- (count clean-path) 7))

                                ;; Regular files
                                :else
                                clean-path)]
                      (let [;; Map the URL to the original path
                            acc-with-url (assoc acc url path)
                            ;; If preserve-original? and this is the readme path, also add root mapping
                            acc-with-root (if (and readme-path
                                                   (= path readme-path)
                                                   preserve-original?)
                                            (assoc acc-with-url "" path)
                                            acc-with-url)]
                        ;; For root page, also add "readme" mapping if it matches the pattern
                        (if (and (= url "")
                                 readme-path
                                 (str/starts-with? readme-path "readme/"))
                          (recur (rest lines)
                                 (assoc acc-with-root "readme" path))
                          (recur (rest lines)
                                 acc-with-root))))))

                :else
                (recur (rest lines) acc)))))]

    (log/info "index ready" {:type "uri->file"
                             :files (count index)
                             :redirects (count redirects)})
    ;; Return separate indexes for files and redirects
    {:files index
     :redirects redirects}))

