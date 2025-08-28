(ns gitbok.indexing.impl.uri-to-file
  (:require
   [klog.core :as log]
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
        redirects (->> (redirects/redirects context)
                       (mapv (fn [[k v]] [(subs (str k) 1) v]))
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
                (let [[_ title path]
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
                (recur (rest lines) acc)))))

        diff
        (nth (clojure.data/diff (set (keys redirects))
                                (set (keys index))) 2)

        result (merge index redirects)]

    (when diff
      (log/debug ::urls-diff {:action "comparing redirects and summary urls"})
      (doseq [p diff]
        (log/debug ::separator {})
        (log/debug ::record-in-summary {:url p :file (get index p)})
        (log/debug ::file-in-redirect {:url p :redirect (get redirects p)})
        (log/debug ::separator {})))
    (log/info ::index-ready {:type "uri->file" :entries (count result)})
    result))

(defn get-idx [context]
  (products/get-product-state context [const/URI->FILE_IDX]))

(defn set-idx [context]
  (products/set-product-state
   context [const/URI->FILE_IDX]
   (uri->file-idx context)))

(defn all-urls [context]
  ;; redirect are bad urls.
  (let [idx (get-idx context)
        redirect-keys (set (keys (redirects/redirects context)))]
    (filter #(not (contains? redirect-keys (keyword %))) (keys idx))))
