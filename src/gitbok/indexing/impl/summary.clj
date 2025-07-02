(ns gitbok.indexing.impl.summary
  (:require
   [clojure.string :as str]
   [gitbok.constants :as const]
   [gitbok.utils :as utils]
   [gitbok.http]
   [system]
   [uui]
   [uui.heroicons :as ico]))

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

(defn render-markdown-link-in-toc [title href]
  (let [is-external (str/starts-with? href "http")
        link-attrs {:class "block py-1.5 text-tint-strong/70 hover:bg-tint-hover hover:text-tint-strong transition-colors duration-200 rounded-md mx-2 my-0.5 clickable-summary"
                    :href href}]
    [:a (cond-> link-attrs
          is-external (assoc :target "_blank" :rel "noopener noreferrer")
          (not is-external) (assoc :data-hx-nav true
                                   :hx-get (str href "?partial=true")
                                   :hx-target "#content"
                                   :hx-push-url href
                                   :hx-swap "outerHTML"
                                   :hx-boost "false"
                                   :data-hx-boost "false"))
     [:span {:class "flex items-center gap-2 mx-2"}
      title
      (when is-external
        (ico/arrow-top-right-on-square "size-4 text-tint-strong/40"))]]))

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
        (recur ls  (conj acc l))
        [acc pls]))))

(defn treefy [lines]
  (loop [[l & ls] lines
         acc []]
    (if (nil? l)
      acc
      (let [[chld ls] (collect-children (:i l) ls)
            l (if (seq chld) (assoc l :children (treefy chld)) l)]
        (recur ls (conj acc l))))))

(defn read-summary []
  (utils/slurp-resource const/SUMMARY_PATH))

(defn title [s]
  (let [t (str/trim (str/replace (str/replace s #"\<.*\>" "") #"#" ""))]
    (if (= "Table of contents" t) "" t)))

(defn parse-summary
  "Read SUMMARY.md and parse and render."
  [context]
  (let [sum (read-summary)
        summary
        (->>
         (loop [acc []
                cur nil
                [l & ls] (str/split sum #"\n")
                j 0]
           (if (nil? l)
             (if cur (conj acc cur) acc)
             (if (str/starts-with? (str/trim l) "#")
               (recur
                (if cur (conj acc cur) acc)
                {:title (title l)
                 :j j
                 :children []}
                ls
                (inc j))
               (recur acc
                      (if (str/blank? l)
                        cur
                        (update cur :children conj {:md-link l
                                                    :j j}))
                      ls
                      (inc j)))))

         (mapv (fn [x]
                 (update x :children
                         (fn [chld]
                           (->> chld
                                (mapv (fn [x]
                                        (let [md-link (:md-link x)
                                              parsed (parse-md-link md-link)
                                              href (:href parsed)
                                              href
                                              (if (str/starts-with? href "http")
                                                href
                                                (let [h (gitbok.http/get-prefixed-url context href)]
                                                  (if (str/starts-with? h "/") h
                                                      (str "/" h))))]

                                          {:i (count-whitespace md-link)
                                           :j (:j x)
                                           :parsed parsed
                                           :href href
                                           :title (render-markdown-link-in-toc (:title parsed) href)})))
                                (treefy)))))))]
    summary))

(defn set-summary [context]
  (system/set-system-state
   context
   [const/SUMMARY_STATE]
   (parse-summary context)))

(defn get-summary [context]
  (system/get-system-state context [const/SUMMARY_STATE]))

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
  (let [summary (gitbok.indexing.impl.summary/get-summary context)
        flattened (gitbok.indexing.impl.summary/flatten-navigation summary)]
    (filterv (fn [item]
               (and (:parsed item)
                    (:href item)
                    (not= (:href item) "")
                    (not (str/starts-with? (:href item) "http"))))
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
