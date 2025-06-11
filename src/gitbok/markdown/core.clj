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
         :blockquote
         github-hint/github-hint-renderer
         :nothing
         (fn [_ctx _node] "")
         :heading
         (comp
          (fn [header-hiccup]
            (cond-> header-hiccup
              (-> header-hiccup (get 1) :id)
              (update-in
               [1 :id]
               (fn [id]
                 (str/replace id #"^-|-$" "")))))
          (:heading transform/default-hiccup-renderers))
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
          :class "block py-1 text-sm text-gray-600 hover:text-gray-900"}
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
     [:div {:class "w-64 flex-shrink-0 sticky top-0 h-screen overflow-auto p-6 bg-gray-50 border-l border-gray-200"}
      [:div {:class "text-sm font-medium text-gray-900 mb-4"}
       "On this page"]
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
