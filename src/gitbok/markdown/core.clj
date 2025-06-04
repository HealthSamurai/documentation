(ns gitbok.markdown.core
  (:require
   [clojure.string :as str]
   [gitbok.markdown.widgets.big-links :as big-links]
   [gitbok.markdown.widgets.link :as link]
   [gitbok.markdown.widgets.image :as image]
   [nextjournal.markdown :as md]
   [nextjournal.markdown.transform :as transform]
   [system]
   [gitbok.constants :as const]
   [gitbok.utils :as utils]
   [hiccup2.core]
   [nextjournal.markdown.utils :as u]
   [uui]))

(def custom-doc
  (update u/empty-doc
          :text-tokenizers
          conj
          big-links/big-link-tokenizer
          image/image-tokenizer))

(defn parse-markdown-content
  [[filepath content]]
  {:filepath filepath
   :parsed (md/parse* custom-doc content)})

(defn renderers [context filepath]
  (assoc transform/default-hiccup-renderers
         :big-link big-links/big-link-renderer
         :image image/image-renderer
         :link (partial link/link-renderer context filepath)
         :internal-link link/link-renderer
         :heading
         (comp
           (fn [header-hiccup]
             (update-in header-hiccup [1 :id]
                        (fn [id] (str/replace id #"^-|-$" ""))))
           (:heading transform/default-hiccup-renderers))
         :html-inline
         (fn [_ctx node]
           (uui/raw (-> node :content first :text)))
         :html-block
         (fn [_ctx node]
           (uui/raw (-> node :content first :text)))))

(defn render-toc-item [item]
  (let [content (->> (:content item)
                     (remove (fn [node] (= :html-inline (:type node))))
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

(defn set-parsed-markdown-index [context md-files-idx]
  (system/set-system-state
   context
   [const/PARSED_MARKDOWN_IDX]
   (mapv parse-markdown-content
         md-files-idx)))

(defn get-parsed-markdown-index [context]
  (system/get-system-state
   context
   [const/PARSED_MARKDOWN_IDX]))
