(ns gitbok.markdown.core
  (:require
   [clojure.string :as str]
   [gitbok.markdown.widgets.big-links :as big-links]
   [gitbok.markdown.widgets.link :as link]
   [gitbok.markdown.widgets.image :as image]
   [gitbok.markdown.widgets.github-hint :as github-hint]
   [gitbok.markdown.widgets.cards :as cards]
   [gitbok.markdown.widgets.tabs :as tabs]
   [gitbok.markdown.widgets.gitbook-code :as gitbook-code]
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
          image/image-tokenizer
          image/youtube-tokenizer))

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

         :numbered-list
         (fn [ctx node]
           (into
            [:ol
             {:class "min-w-0 space-y-2 w-full decoration-primary/6 page-full-width:ml-0 max-w-3xl page-api-block:ml-0"}]
            (map-indexed
             (fn [idx child]
               (transform/->hiccup ctx (assoc child :numbered-list-idx idx)))
             (:content node))))

         :list-item
         (fn [ctx {:keys [numbered-list-idx] :as node}]
           (if numbered-list-idx
             [:li {:class "leading-normal flex items-start"}
              [:div {:class "text-base leading-normal mr-1 flex min-h-[1lh] min-w-6 items-center justify-center text-tint"}
               [:div
                (when numbered-list-idx (str (inc numbered-list-idx) "."))]]
              [:div {:class "flex min-w-0 flex-1 flex-col space-y-2"}
               (into [:p {:class "w-full decoration-primary/6 page-full-width:ml-0 max-w-3xl min-h-[1lh] flip-heading-hash [&:is(h2)>div]:mt-0 [&:is(h3)>div]:mt-0 [&:is(h4)>div]:mt-0 mx-0"}]
                     (mapv #(transform/->hiccup ctx %)
                           (:content node)))]]
             ((:list-item transform/default-hiccup-renderers) ctx node)))

         :embed
         (fn [_ctx node]
           (let [url (:url node)
                 content (:content node)
                 is-youtube (str/includes? url "/watch")
                 video-id
                 (when is-youtube
                   (second (re-find #"v=([^&]+)" url)))
                 embed-url
                 (when video-id
                   (str "https://www.youtube.com/embed/" video-id))]
             [:div {:class "my-6"}
              (when embed-url
                [:div {:class "relative w-full h-0 pb-[56.25%] rounded-lg overflow-hidden shadow-lg"}
                 [:iframe {:src embed-url
                           :class "absolute top-0 left-0 w-full h-full border-0"
                           :allowfullscreen true
                           :allow "accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"}]])
              (when (and content (not (str/blank? (str/trim content))))
                [:div {:class "mt-4 text-sm text-gray-600 italic"}
                 (str/trim content)])]))
         :blockquote github-hint/github-hint-renderer
         :heading
         (comp
          (fn [header-hiccup]
            (let [tag (first header-hiccup)
                  classes (case tag
                            :h1 "mt-6 text-4xl font-bold text-gray-900 pb-4 mb-8"
                            :h2 "mt-8 text-3xl font-semibold text-gray-900 pb-2 mb-6"
                            :h3 "mt-6 text-2xl font-semibold text-gray-900 mb-4"
                            :h4 "mt-4 text-lg font-semibold text-gray-900 mb-3"
                            :h5 "mt-3 text-base font-semibold text-gray-900 mb-2"
                            :h6 "mt-2 text-sm font-semibold text-gray-900 mb-1"
                            "text-gray-900")]
              (cond-> header-hiccup
                (-> header-hiccup (get 1) :id)
                (update-in
                 [1 :id]
                 (fn [id]
                   (-> id
                       (str/replace #"^-|-$" "")
                       (utils/s->url-slug))))
                :always
                (update-in
                 [1 :class]
                 (fnil
                  (fn [existing-class]
                    (str/trim
                     (str existing-class " " classes))) "")))))
          (:heading transform/default-hiccup-renderers))

         :paragraph
         (fn [ctx node]
           (into [:p {:class "my-4 text-base text-gray-900 leading-relaxed"}]
                 (mapv #(transform/->hiccup ctx %) (:content node))))

         :bullet-list
         (fn [ctx node]
           (into [:ul {:class "mt-4 ml-8 list-disc text-gray-900 text-base"}]
                 (mapv #(transform/->hiccup ctx %)
                       (:content node))))

         :code
         (fn [_ctx node]
           (if (str/starts-with? (:info node) "mermaid")
             [:div
              [:pre.mermaid (-> node :content first :text)]
              [:script
               (uui/raw "if (typeof mermaid === 'undefined') {
                  var script = document.createElement('script');
                  script.src = '/static/mermaid.min.js';
                  script.onload = function() {
                    mermaid.initialize({ startOnLoad: true });
                  };
                  document.head.appendChild(script);
                } else {
                  mermaid.initialize({ startOnLoad: true });
                }")]]

             [:pre
              [:code.nohljsln
               (-> node :content first :text)]]))

         :monospace
         (fn [_ctx node]
           [:code {:class "px-1 border border-gray-200 rounded"
                   :style "background-color: #fbf9f9;"}
            (-> node :content first :text)])

         :table
         (fn [ctx node]
           (into [:table {:class "min-w-full border border-tint-subtle rounded-lg bg-white shadow-sm my-6 text-base"}]
                 (mapv #(transform/->hiccup ctx %) (:content node))))

         :table-head
         (fn [ctx node]
           (into [:thead {:class "border-b border-tint-subtle text-base"}]
                 (mapv #(transform/->hiccup ctx %) (:content node))))

         :table-body
         (fn [ctx node]
           (into [:tbody {:class "divide-y divide-tint-subtle text-base"}]
                 (mapv #(transform/->hiccup ctx %) (:content node))))

         :table-row
         (fn [ctx node]
           (into [:tr {:class "hover:bg-tint-hover transition-colors duration-200 text-base"}]
                 (mapv #(transform/->hiccup ctx %) (:content node))))

         :table-data
         (fn [ctx node]
           (into [:td {:class "px-4 py-3 border-r border-tint-subtle/50 last:border-r-0 text-tint-strong/80 text-left text-base"}]
                 (mapv #(transform/->hiccup ctx %) (:content node))))

         :table-header
         (fn [ctx node]
           (into [:th {:class "px-4 py-4 text-base border-r border-tint-subtle/50 last:border-r-0 text-tint-strong font-semibold text-left"}]
                 (mapv #(transform/->hiccup ctx %) (:content node))))

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
             (cond
               (and c
                    (= :table (first c))
                    (= {:data-view "cards"} (second c)))
               (cards/render-cards-from-table context filepath c)

               (and c
                    (= :table (first c))
                    (= {:data-header-hidden ""} (second c)))
               (uui/raw (-> node :content first :text
                            (str/replace #"\<thead.*/thead>" "")))

               :else
               (uui/raw (-> node :content first :text)))))))

(defn render-toc-item [item]
  [:div {:class "w-full"}
   (when (:content item)
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
                   (:heading-level item))
           padding-class (case (long level)
                           1 "pl-0"
                           2 "pl-4"
                           3 "pl-8"
                           4 "pl-12"
                           5 "pl-16"
                           6 "pl-20"
                           "pl-0")]
       [:a {:href href
            :class (str padding-class)}
        content]))

   (when (:children item)
     (for [child (:children item)]
       (render-toc-item child)))])

(defn render-md [context filepath parsed]
  (transform/->hiccup (renderers context filepath) parsed))

(defn hack-md [context filepath md-file]
  (->> md-file
       big-links/hack-content-ref
       github-hint/hack-info
       image/hack-youtube
       (tabs/hack-tabs context filepath
                       parse-markdown-content
                       render-md)
       (gitbook-code/hack-gitbook-code context filepath
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
