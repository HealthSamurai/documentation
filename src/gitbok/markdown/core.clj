(ns gitbok.markdown.core
  (:require
   [clojure.string :as str]
   [gitbok.markdown.widgets.big-links :as big-links]
   [gitbok.markdown.widgets.link :as link]
   [gitbok.markdown.widgets.image :as image]
   [gitbok.markdown.widgets.github-hint :as github-hint]
   [gitbok.markdown.widgets.cards :as cards]
   [gitbok.markdown.widgets.tabs :as tabs]
   [nextjournal.markdown :as md]
   [nextjournal.markdown.transform :as transform]
   [hickory.core]
   [system]
   [gitbok.constants :as const]
   [gitbok.utils :as utils]
   [hiccup2.core]
   [nextjournal.markdown.utils :as u]
   [uui]))

(declare hack-md)

(def custom-doc
  (update u/empty-doc
          :text-tokenizers
          conj
          big-links/big-link-tokenizer
          image/image-tokenizer))

(defn parse-markdown-content
  [context [filepath content]]
  {:filepath filepath
   :parsed (md/parse* custom-doc (hack-md context filepath content))})

(defn parse-html [html]
  (map hickory.core/as-hiccup
       (hickory.core/parse-fragment html)))

(defn renderers [context filepath]
  (assoc transform/default-hiccup-renderers
         :big-link (partial big-links/render-big-link context filepath)
         :big-link1 (partial big-links/render-big-link context filepath)
         :image image/image-renderer
         :link (partial link/link-renderer context filepath)
         :internal-link link/link-renderer

         :blockquote github-hint/github-hint-renderer

         :heading
         (comp
          (fn [header-hiccup]
            (let [tag (first header-hiccup)
                  ;; attrs (second header-hiccup)
                  ;; content (nthrest header-hiccup 2)
                  level (case tag
                          :h1 1
                          :h2 2
                          :h3 3
                          :h4 4
                          :h5 5
                          :h6 6
                          4)
                  classes (case level
                           1 "mt-6 text-4xl font-bold text-gray-900 pb-4 mb-8"
                           2 "mt-8 text-2xl font-semibold text-gray-900 pb-2 mb-6"
                           3 "mt-6 text-xl font-semibold text-gray-900 mb-4"
                           4 "mt-4 text-lg font-medium text-gray-900 mb-3"
                           5 "mt-3 text-base font-medium text-gray-900 mb-2"
                           6 "mt-2 text-sm font-medium text-gray-900 mb-1"
                           "text-gray-900")]
              (cond-> header-hiccup
                (-> header-hiccup (get 1) :id)
                (update-in
                 [1 :id]
                 (fn [id]
                   (str/replace id #"^-|-$" "")))
                :always
                (update-in
                  [1 :class]
                  (fnil
                    (fn [existing-class]
                      (str/trim
                        (str existing-class " " classes))) "")))))
          (:heading transform/default-hiccup-renderers))

         ;; :paragraph
         ;; (comp
         ;;  (fn [p-hiccup]
         ;;    (update-in p-hiccup [1 :class] (fn [existing-class]
         ;;                                    (str/trim (str existing-class " my-4 text-gray-900 leading-relaxed")))))
         ;;  (:paragraph transform/default-hiccup-renderers))

         ;; :bullet-list
         ;; (comp
         ;;  (fn [ul-hiccup]
         ;;    (update-in ul-hiccup [1 :class] (fn [existing-class]
         ;;                                     (str/trim (str existing-class " mt-4 ml-8 list-disc text-gray-900")))))
         ;;  (:bullet-list transform/default-hiccup-renderers))
         ;;
         ;; :ordered-list
         ;; (comp
         ;;  (fn [ol-hiccup]
         ;;    (update-in ol-hiccup [1 :class] (fn [existing-class]
         ;;                                     (str/trim (str existing-class " mt-4 ml-8 list-decimal text-gray-900")))))
         ;;  (:ordered-list transform/default-hiccup-renderers))
         ;;
         ;; :list-item
         ;; (comp
         ;;  (fn [li-hiccup]
         ;;    (update-in li-hiccup [1 :class] (fn [existing-class]
         ;;                                     (str/trim (str existing-class " mb-2")))))
         ;;  (:list-item transform/default-hiccup-renderers))

         ;; :code
         ;; (comp
         ;;  (fn [code-hiccup]
         ;;    (update-in code-hiccup [1 :class] (fn [existing-class]
         ;;                                       (str/trim (str existing-class " bg-gray-100 text-sm px-2 py-1 rounded font-mono text-gray-900 border border-gray-200")))))
         ;;  (:code transform/default-hiccup-renderers))
         ;;
         ;; :code-block
         ;; (comp
         ;;  (fn [pre-hiccup]
         ;;    (update-in pre-hiccup [1 :class] (fn [existing-class]
         ;;                                      (str/trim (str existing-class " my-6 p-4 text-sm rounded-lg overflow-x-auto bg-gray-900 text-gray-100 border border-gray-700")))))
         ;;  (:code-block transform/default-hiccup-renderers))

         ;; :table
         ;; (comp
         ;;  (fn [table-hiccup]
         ;;    (update-in table-hiccup [1 :class] (fn [existing-class]
         ;;                                        (str/trim (str existing-class " mt-6 text-sm w-full border-collapse rounded-lg overflow-hidden shadow-sm border border-gray-200")))))
         ;;  (:table transform/default-hiccup-renderers))
         ;;
         ;; :table-head
         ;; (comp
         ;;  (fn [thead-hiccup]
         ;;    (update-in thead-hiccup [1 :class] (fn [existing-class]
         ;;                                        (str/trim (str existing-class " bg-gray-50")))))
         ;;  (:table-head transform/default-hiccup-renderers))
         ;;
         ;; :table-row
         ;; (comp
         ;;  (fn [tr-hiccup]
         ;;    (update-in tr-hiccup [1 :class] (fn [existing-class]
         ;;                                     (str/trim (str existing-class " border-b border-gray-200 hover:bg-gray-50 transition-colors duration-200")))))
         ;;  (:table-row transform/default-hiccup-renderers))
         ;;
         ;; :table-head-cell
         ;; (comp
         ;;  (fn [th-hiccup]
         ;;    (update-in th-hiccup [1 :class] (fn [existing-class]
         ;;                                     (str/trim (str existing-class " px-4 py-3 text-left font-semibold text-gray-900 bg-gray-50")))))
         ;;  (:table-head-cell transform/default-hiccup-renderers))
         ;;
         ;; :table-cell
         ;; (comp
         ;;  (fn [td-hiccup]
         ;;    (update-in td-hiccup [1 :class] (fn [existing-class]
         ;;                                     (str/trim (str existing-class " px-4 py-3 text-gray-900")))))
         ;;  (:table-cell transform/default-hiccup-renderers))

         :html-inline
         (fn [_ctx node]
           (let [c (first (parse-html (-> node :content first :text)))]
             (if (and c (= :table (first c))
                      (= {:data-view "cards"} (second c)))
               (cards/render-cards-from-table context filepath c)
               (uui/raw (-> node :content first :text)))))
         :html-block
         (fn [_ctx node]
           (let [c (first (parse-html (-> node :content first :text)))]
             (if (and c
                      (= :table (first c))
                      (= {:data-view "cards"} (second c)))
               (cards/render-cards-from-table context filepath c)
               (uui/raw (-> node :content first :text)))))))

(defn render-toc-item [item]
  (let [content
        (->> (:content item)
             (remove (fn [node]
                       (= :html-inline (:type node))))
             (map #(if (= :text (:type %))
                     (:text %)
                     (->> (:content %)
                          (map :text)
                          (str/join " "))))
             (str/join " "))
        href (str "#" (utils/s->url-slug (:id (:attrs item))))
        level (when (= :toc (:type item))
                (:heading-level item))]
    [:div {:class (when (not= 1 level) "pl-4")}
     [:a {:href href
          :class "block py-1 text-sm text-gray-700 hover:text-red-600 transition-colors duration-200 no-underline"}
      content]
     (when (:children item)
       (for [child (:children item)]
         (render-toc-item child)))]))

(defn render-md [context filepath parsed]
  (transform/->hiccup (renderers context filepath) parsed))

(defn render-gitbook [context filepath {:keys [toc] :as parsed}]
  [:div {:class "flex gap-8"}
   [:div {:class "flex-1 min-w-0"}
    (render-md context filepath parsed)]
   (when toc
     [:div {:class "w-70 flex-shrink-0 sticky top-0 h-screen overflow-auto p-6 border-l border-gray-200 bg-white"}
      (for [item (:children toc)]
        (render-toc-item item))])])

(defn hack-md [context filepath md-file]
  (->> md-file
       big-links/hack-content-ref
       github-hint/hack-info
       (tabs/hack-tabs context filepath
                       parse-markdown-content
                       render-md)))

(defn set-parsed-markdown-index [context md-files-idx]
  (system/set-system-state
   context
   [const/PARSED_MARKDOWN_IDX]
   (mapv #(parse-markdown-content context %) md-files-idx)))

(defn get-parsed-markdown-index [context]
  (system/get-system-state
   context
   [const/PARSED_MARKDOWN_IDX]))
