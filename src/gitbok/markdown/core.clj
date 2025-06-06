(ns gitbok.markdown.core
  (:require
   [clojure.string :as str]
   [gitbok.markdown.widgets.big-links :as big-links]
   [gitbok.markdown.widgets.link :as link]
   [gitbok.markdown.widgets.image :as image]
   [nextjournal.markdown :as md]
   [nextjournal.markdown.transform :as transform]
   [hickory.core]
   [system]
   [gitbok.constants :as const]
   [gitbok.utils :as utils]
   [hiccup2.core]
   [nextjournal.markdown.utils :as u]
   [uui]))

(defn parse-html [html]
  (map hickory.core/as-hiccup
       (hickory.core/parse-fragment html)))

(def custom-doc
  (update u/empty-doc
          :text-tokenizers
          conj
          big-links/big-link-tokenizer ;; [[]]
          ;; big-links/content-ref-tokenizer
          ;; big-links/end-content-ref-tokenizer
          image/image-tokenizer))

(defn parse-markdown-content
  [[filepath content]]
  {:filepath filepath
   :parsed (md/parse* custom-doc content)})

(defn render-cards-from-table
  [[_ _ _ tbody]]
  (let [rows (->> tbody
                  (filter vector?)
                  (filter #(= :tr (first %))))]
    [:div
     {:class "grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6"}
     (for [row rows
           :let [[title desc footer pic1 pic2]
                 (->> row (filter vector?)
                      (mapv (fn [a] (into [:div] (next (next a))))))
                 pic1 (first (filterv
                              #(and (sequential? %) (= (first %) :a))
                              pic1))
                 pic2 (first (filterv
                              #(and (sequential? %) (= (first %) :a))
                              pic2))
                 pic-href1 (get-in pic1 [1 :href])
                 pic-href2 (get-in pic2 [1 :href])
                 pic-footer
                 (get-in
                  (first (filterv
                          #(and (sequential? %) (= (first %) :a))
                          footer))

                  [1 :href])
                 img-href
                 (first (filter (fn [s]
                                  (when s (re-matches #".*(png|jpg|jpeg|svg)$" s)))
                                [pic-footer pic-href1 pic-href2]))]]

       [:div {:class "flex flex-col bg-white rounded-2xl shadow overflow-hidden h-full min-h-[300px]"}
        (when img-href [:img {:src img-href}])
        [:div
         {:class
          (str "flex flex-col gap-2 p-4 flex-1 "
               (when-not img-href "justify-start"))}
         [:a {:href pic-href1
              :class "text-lg hover:underline"} title]
         [:p {:class "text-gray-600 text-sm"} desc]
         (when-not pic-footer footer)]])]))

(defn renderers [context filepath]
  (assoc transform/default-hiccup-renderers
         :big-link (partial big-links/render-big-link context filepath)
         :big-link1 (partial big-links/render-big-link context filepath)
         :image image/image-renderer
         :link (partial link/link-renderer context filepath)
         :internal-link link/link-renderer
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
             (if (and c (= :table (first c)))
               (render-cards-from-table c)
               (uui/raw (-> node :content first :text)))))
         :html-block
         (fn [_ctx node]
           (let [c (first (parse-html (-> node :content first :text)))]
             (if (and c (= :table (first c)))
               (render-cards-from-table c)
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
