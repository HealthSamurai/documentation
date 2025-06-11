(ns gitbok.indexing.impl.summary
  (:require
   [clojure.string :as str]
   [gitbok.constants :as const]
   [clojure.java.io :as io]
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
  [:a {:class "block px-4 py-1.5 text-gray-700 hover:bg-gray-100 hover:text-red-600 transition-colors duration-200 rounded-md mx-2 my-0.5 clickable-summary no-underline"
       :href (if (str/starts-with? href "http")
               href (str "/" href))
       :hx-target "#content"
       :hx-swap "outerHTML"}
   [:span {:class "flex items-center gap-2"}
    title
    (when (str/starts-with? href "http")
      (ico/arrow-top-right-on-square "size-4 text-gray-400"))]])

(defn parse-md-link [line]
  (when-let [match (re-find #"\[(.*?)\]\((.*?)\)"
                            (str/replace (str/trim line) #"\s*\*\s*" ""))]
    (let [href (nth match 2)
          href (file->href href)
          title (nth match 1)]
      {:title title :href href})))

(defn render-md-link [line]
  (if-let [parsed (parse-md-link line)]
    (render-markdown-link-in-toc (:title parsed) (:href parsed))
    line))

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
  (slurp (io/resource const/SUMMARY_PATH)))

(defn title [s]
  (let [t (str/trim (str/replace (str/replace s #"\<.*\>" "") #"#" ""))]
    (if (= "Table of contents" t) "" t)))

(defn parse-summary
  "Read SUMMARY.md and parse and render."
  []
  (let [sum (read-summary)
        summary
        (->>
         (loop [acc []
                cur nil
                [l & ls] (str/split sum #"\n")]
           (if (nil? l)
             (if cur (conj acc cur) acc)
             (if (str/starts-with? (str/trim l) "#")
               (recur
                (if cur (conj acc cur) acc)
                {:title (title l)
                 :children []}
                ls)
               (recur acc
                      (if (str/blank? l)
                        cur
                        (update cur :children conj l))
                      ls))))

         (mapv (fn [x]
                 (update x :children
                         (fn [chld]
                           (->> chld
                                (mapv (fn [x]
                                        {:i (count-whitespace x)
                                         :title (render-md-link x)}))
                                (treefy)))))))]
    summary
    #_(into (get-in summary [0 :children])
            (drop 1 summary))))

(defn set-summary [context]
  (system/set-system-state
   context
   [const/SUMMARY_STATE]
   (parse-summary)))

(defn get-summary [context]
  (system/get-system-state context [const/SUMMARY_STATE]))

(comment
  (parse-summary)

  (treefy
   (->> ["a" "  b" "  c" "x" " x1" " x2"]
        (mapv (fn [x] {:i (count-whitespace x) :l (str/trim x)})))))
