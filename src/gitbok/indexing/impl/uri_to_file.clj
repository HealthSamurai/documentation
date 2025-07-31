(ns gitbok.indexing.impl.uri-to-file
  (:require
   [clojure.string :as str]
   [gitbok.constants :as const]
   [gitbok.indexing.impl.summary]
   [gitbok.indexing.impl.redirects :as redirects]
   [gitbok.utils :as utils]
   [gitbok.products :as products]
   [clojure.data]
   [system]))

(defn uri->filepath [uri->file-idx ^String uri]
  (when (and uri (> (count uri) 1))
    (let [uri (if (= "/" (subs uri 0 1)) (subs uri 1) uri)
          fixed-url (if (= "/" (subs uri (dec (count uri))))
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
        readme-count (count "readme.md")
        md-count (count ".md")
        redirects (->> (redirects/redirects context)
                       (mapv (fn [[k v]] [(subs (str k) 1) v]))
                       (into {}))
        index
        (loop [lines lines
               section nil
               stack []
               acc {}]
          (if (empty? lines)
            acc
            (let [line (first lines)
                  trimmed (str/trim line)]
              (cond
                (str/starts-with? trimmed "# ")
                (recur (rest lines) section stack acc)

                (str/starts-with? trimmed "## ")
                (let [section-title (str/trim (subs trimmed 3))]
                  (recur (rest lines)
                         (utils/s->url-slug section-title)
                         [] acc))

                (re-matches #"\s*\*\s+\[([^\]]+)\]\(([^)]+)\)" line)
                (let [indent (->> line (re-find #"^(\s*)\*") second count)
                      level (quot indent 2)
                      [_ title path]
                      (re-matches #"\s*\*\s+\[([^\]]+)\]\(([^)]+)\)" line)
                      readme? (str/ends-with? (str/lower-case path) "readme.md")
                      new-stack (-> stack
                                    (subvec 0 level)
                                    (conj title))
                      prefix (if section (str section "/") "")
                      full-path
                      (str
                       prefix
                       (str/join "/"
                                 (mapv
                                  utils/s->url-slug
                                  new-stack)))
                      url2 (subs path 0
                                 (- (count path)
                                    (if readme?
                                      readme-count
                                      md-count)))
                      url2 (if (str/ends-with? url2 "/")
                             (subs url2 0 (dec (count url2)))
                             url2)]
                  (recur (rest lines) section new-stack
                         (assoc acc
                                full-path path
                            ;; todo migrate all urls to [title]s
                                url2 path)))

                :else
                (recur (rest lines) section stack acc)))))
        diff
        (nth (clojure.data/diff (set (keys redirects))
                                (set (keys index))) 2)

        result (merge index redirects)]

    (when diff
      (println "diff between redirects and summary urls")
      (doseq [p diff]
        (println "-------")
        (printf "record in summary: {%s %s}\n" p (get index p))
        (printf "file in redirect: {%s %s}\n" p (get redirects p))
        (println "-------")))
    (println "uri->file idx is ready with " (count result) " entries")
    result))

(defn get-idx [context]
  (products/get-product-state context [const/URI->FILE_IDX]))

(defn set-idx [context]
  (products/set-product-state
   context [const/URI->FILE_IDX]
   (uri->file-idx context)))

(defn all-urls [context]
  (keys (get-idx context)))
