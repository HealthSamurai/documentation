(ns gitbok.indexing.impl.summary
  (:require
   [clojure.tools.logging :as log]
   [clojure.string :as str]
   [gitbok.http :as http]
   [gitbok.products :as products]
   [gitbok.state :as state]
   [gitbok.ui.heroicons :as ico]))

(defn count-whitespace [s] (count (re-find #"^\s*" s)))

(defn file->href [href]
  (cond
    (str/starts-with? href "http")
    href

    :else
    (-> href
        (str/replace #"\.md$" "")
        (str/replace #"README$" "")
        (str/replace #"/$" "")
        (str/replace #"^/" ""))))

(def summary-classes
  "block py-1.5 transition-colors duration-200 ease-in-out
   mr-2 my-0.5 clickable-summary text-sm leading-5 tracking-tight text-on-surface-secondary")

(def leaf-classes summary-classes)

(defn render-markdown-link-in-toc [context title href & {:keys [is-cross-section] :or {is-cross-section false}}]
  (let [is-external (str/starts-with? href "http")
        link-attrs
        {:class leaf-classes
         :href href}]
    [:a (cond-> link-attrs
          is-external
          (assoc :target "_blank"
                 :rel "noopener noreferrer")
          ;; Mark cross-section links with a data attribute
          is-cross-section
          (assoc :data-cross-section "true")
          ;; Only add HTMX attributes if not external and not cross-section
          (and (not is-external) (not is-cross-section))
          (assoc :hx-get (str (http/get-partial-product-prefixed-url context
                                                                     (subs href (count (http/get-product-prefix context)))))
                 :hx-target "#content"
                 :hx-push-url href
                 :hx-swap "outerHTML"))
     [:span {:class "flex items-center gap-2 ml-4"}
      title
      (when (or is-external is-cross-section)
        (ico/arrow-top-right-on-square "size-4"))]]))

(defn parse-md-link [line]
  (when-let [match (re-find #"\[(.*?)\]\((.*?)\)"
                            (str/replace (str/trim line) #"\s*\*\s*" ""))]
    (let [href (nth match 2)
          href (file->href href)
          title (nth match 1)]
      {:title title :href href})))

(defn collect-children [x ls]
  (loop [[{i :i :as l} & ls :as pls] ls acc []]
    (if (nil? l)
      [acc nil]
      (if (> i x)
        (recur ls (conj acc l))
        [acc pls]))))

(defn treefy [lines]
  (loop [[l & ls] lines
         acc []]
    (if (nil? l)
      acc
      (let [[chld ls] (collect-children (:i l) ls)
            l (if (seq chld) (assoc l :children (treefy chld)) l)]
        (recur ls (conj acc l))))))

(defn read-summary [context]
  (let [config (products/get-current-product context)
        summary-path (products/summary-path config)]
    (log/debug "read summary" {:path summary-path})
    (state/slurp-resource context summary-path)))

(defn title [s]
  (let [t (str/trim (str/replace (str/replace s #"\<.*\>" "") #"#" ""))]
    (if (= "Table of contents" t) "" t)))

(defn get-section-from-path
  "Extract the top-level section from a file path.
   e.g. 'api/rest-api/fhir-search/searchparameter.md' -> 'api'"
  [path]
  (when path
    (let [clean-path (str/replace path #"^/" "")
          parts (str/split clean-path #"/")]
      (first parts))))

(defn parse-summary
  "Read SUMMARY.md and parse and render."
  [context]
  (let [sum (read-summary context)
        ;; First pass: collect all file occurrences and their sections
        file-sections
        (loop [current-section nil
               file-map {}
               [l & ls] (str/split sum #"\n")]
          (if (nil? l)
            file-map
            (cond
              ;; New section header
              (str/starts-with? (str/trim l) "#")
              (recur (title l) file-map ls)

              ;; Link line
              (and (not (str/blank? l)) current-section)
              (if-let [parsed (parse-md-link l)]
                (let [filepath (:href parsed)]
                  (if (and filepath
                           (not (str/starts-with? filepath "http")))
                    (recur current-section
                           (update file-map filepath
                                   (fn [sections]
                                     (conj (or sections #{}) current-section)))
                           ls)
                    (recur current-section file-map ls)))
                (recur current-section file-map ls))

              ;; Empty line or other
              :else
              (recur current-section file-map ls))))

        summary
        (->>
         (loop [acc []
                cur nil
                current-section nil
                [l & ls] (str/split sum #"\n")
                j 0]
           (if (nil? l)
             (if cur (conj acc cur) acc)
             (if (str/starts-with? (str/trim l) "#")
               (let [section-title (title l)]
                 (recur
                  (if cur (conj acc cur) acc)
                  {:title section-title
                   :section-title section-title
                   :j j
                   :children []}
                  section-title ;; Track current section
                  ls
                  (inc j)))
               (recur acc
                      (if (str/blank? l)
                        cur
                        (update cur :children conj {:md-link l
                                                    :j j
                                                    :current-section current-section}))
                      current-section
                      ls
                      (inc j)))))

         (mapv (fn [x]
                 (update x :children
                         (fn [chld]
                           (->> chld
                                (mapv (fn [x]
                                        (let [md-link (:md-link x)
                                              current-section (:current-section x)
                                              parsed (parse-md-link md-link)
                                              filepath (:href parsed)

                                              ;; Check if this is a cross-section reference
                                              is-cross-section
                                              (when (and filepath
                                                         (not (str/starts-with? filepath "http"))
                                                         current-section)
                                                (let [file-sections-set (get file-sections filepath)
                                                      path-section (get-section-from-path filepath)]
                                                  (and
                                                   ;; Condition 1: File appears in multiple sections
                                                   (> (count file-sections-set) 1)
                                                   ;; Condition 2: Path goes outside current section
                                                   path-section
                                                   (not= (str/lower-case path-section)
                                                         (str/lower-case (first (str/split current-section #" ")))))))

                                              href (:href parsed)
                                              ;; Special handling for readme - should map to root
                                              href (if (= href "readme") "" href)
                                              href
                                              (when href (if (str/starts-with? href "http")
                                                           href
                                                           (let [h (http/get-product-prefixed-url context href)]
                                                             (if (str/starts-with? h "/") h
                                                                 (str "/" h)))))]

                                          (when href
                                            {:i (count-whitespace md-link)
                                             :j (:j x)
                                             :parsed parsed
                                             :href href
                                             :is-cross-section is-cross-section ;; Store the flag in the data structure
                                             :section-title current-section
                                             :title (when href (render-markdown-link-in-toc context (:title parsed) href
                                                                                            :is-cross-section is-cross-section))}))))
                                (remove nil?)
                                (treefy)))))))]
    (log/info "summary parsed" {:entries (count summary)})
    summary))

(defn flatten-navigation [items]
  (reduce (fn [acc item]
            (let [acc-with-item (conj acc (dissoc item :children))]
              (if (:children item)
                (vec (concat acc-with-item
                             (vec (mapv (fn [x] (dissoc x :children))
                                        (flatten-navigation (:children item))))))
                acc-with-item)))
          []
          items))

(defn get-navigation-links [context]
  (let [summary (state/get-summary context)
        flattened (gitbok.indexing.impl.summary/flatten-navigation summary)]
    (filterv (fn [item]
               (and (:parsed item)
                    (:href item)
                    (not= (:href item) "")
                    (not (str/starts-with? (:href item) "http"))))
             flattened)))

(defn get-primary-navigation-links
  "Returns only primary navigation links, excluding cross-section references."
  [context]
  (let [summary (state/get-summary context)
        flattened (gitbok.indexing.impl.summary/flatten-navigation summary)]
    (filterv (fn [item]
               (and (:parsed item)
                    (:href item)
                    (not= (:href item) "")
                    (not (str/starts-with? (:href item) "http"))
                    ;; Exclude cross-section references
                    (not (:is-cross-section item))))
             flattened)))

(defn first-matching-index [pred coll]
  (some (fn [[idx val]]
          (when (pred val) idx))
        (map-indexed vector coll)))

(defn normalize-uri [uri]
  (-> uri
      (str/replace #"^https?://[^/]+" "")
      (str/replace #"^/" "")
      (str/replace #"\.md$" "")
      (str/replace #"/$" "")))

(defn find-page-by-uri [all-pages uri]
  (let [normalized-uri (normalize-uri uri)]
    (first-matching-index
     (fn [item]
       (and (:parsed item)
            (:href item)
            (= (normalize-uri (:href item)) normalized-uri)))
     all-pages)))

(defn get-prev-next-pages [context uri]
  (let [all-pages (get-navigation-links context)
        c (count all-pages)
        current-page-idx (find-page-by-uri all-pages uri)
        prev-page
        (when (and current-page-idx (<= 0 (dec current-page-idx)))
          (nth
           all-pages
           (dec current-page-idx)))

        next-page
        (when (and current-page-idx (< (inc current-page-idx) c))
          (nth
           all-pages
           (inc current-page-idx)))]

    (when current-page-idx
      [[(:href prev-page) (-> prev-page :parsed :title)]
       [(:href next-page) (-> next-page :parsed :title)]])))

(comment

  (treefy
   (->> ["a" "  b" "  c" "x" " x1" " x2"]
        (mapv (fn [x] {:i (count-whitespace x) :l (str/trim x)})))))
