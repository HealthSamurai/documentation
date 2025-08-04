(ns gitbok.markdown.core
  (:require
   [clojure.string :as str]
   [clojure.walk :as walk]
   [gitbok.markdown.widgets.big-links :as big-links]
   [gitbok.markdown.widgets.link :as link]
   [gitbok.markdown.widgets.headers :as headers]
   [gitbok.markdown.widgets.image :as image]
   [gitbok.markdown.widgets.github-hint :as github-hint]
   [gitbok.markdown.widgets.cards :as cards]
   [gitbok.markdown.widgets.tabs :as tabs]
   [gitbok.markdown.widgets.description :as description]
   [gitbok.markdown.widgets.gitbook-code :as gitbook-code]
   [nextjournal.markdown :as md]
   [nextjournal.markdown.transform :as transform]
   [hickory.core]
   [system]
   [gitbok.constants :as const]
   [gitbok.products :as products]
   [hiccup2.core]
   [nextjournal.markdown.utils :as u]
   [uui]))

(declare hack-md)

(def custom-doc
  (update u/empty-doc
          :text-tokenizers
          conj
          big-links/big-link-tokenizer
          image/youtube-tokenizer))

(defn parse-markdown-content
  [context [filepath content]]
  {:filepath filepath
   :description
   (description/parse-description content)
   :title (description/parse-title content)
   :parsed
   (md/parse*
    custom-doc
    (hack-md context filepath content))})

(defn parse-html [html]
  (map hickory.core/as-hiccup
       (hickory.core/parse-fragment html)))

(defn get-meta-from-url [url]
  (let [hic (->> (slurp url)
                 (parse-html)
                 (filter vector?))]
    {:image (->>
             hic
             (filter
              (fn [el]
                (= "icon" (:rel (second el)))))
             (first)
             (last) :href)
     :title (->>
             hic
             (filter
              (fn [el]
                (= :title (first el))))
             (first)
             (last))}))

(defn remove-selects [form]
  (walk/postwalk
   (fn [node]
     (if (and (vector? node)
              (= (first node) :select))
       nil
       node))
   form))

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
             {:class "min-w-0 w-full decoration-primary/6 page-full-width:ml-0 max-w-3xl page-api-block:ml-0 space-y-2"}]
            (map-indexed
             (fn [idx child]
               (transform/->hiccup
                ctx
                (assoc child :numbered-list-idx idx)))
             (:content node))))

         :list-item
         (fn [ctx {:keys [numbered-list-idx] :as node}]
           (if numbered-list-idx
             [:li {:class "leading-normal flex items-start"}
              [:div {:class "text-base leading-normal mr-1 flex min-h-[1lh] min-w-6 items-center justify-center text-tint"}
               [:div (str (inc numbered-list-idx) ".")]]
              [:div {:class "flex min-w-0 flex-1 flex-col"}
               (into [:div {:class "w-full decoration-primary/6 page-full-width:ml-0 max-w-3xl min-h-[1lh] flip-heading-hash [&:is(h2)>div]:mt-0 [&:is(h3)>div]:mt-0 [&:is(h4)>div]:mt-0 mx-0"}]
                     (mapv (fn [c] (transform/->hiccup ctx c))
                           (:content node)))]]
             ((:list-item transform/default-hiccup-renderers) ctx node)))

         :embed
         (fn [_ctx node]
           (let [url (second (re-find #"url=\"(.*?)\"" (:body node)))
                 title-user (second (re-find #"title=\"(.*?)\"" (:body node)))
                 is-youtube (str/starts-with? url "youtube")
                 video-id
                 (when is-youtube
                   (or
                    (second (re-find #"v=([^&]+)" url))
                    (str/replace-first url #"^youtube " "")))
                 youtube-embed-url
                 (when video-id
                   (str "https://www.youtube.com/embed/" video-id))
                 url (str "https://" url)]
             [:div {:class "my-6"}
              (if youtube-embed-url
                [:div {:class "relative w-full h-0 pb-[56.25%] rounded-lg overflow-hidden shadow-lg"}
                 [:iframe {:src youtube-embed-url
                           :class "absolute top-0 left-0 w-full h-full border-0"
                           :allowfullscreen true
                           :allow "accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"}]]
                (let [{:keys [title image]} (get-meta-from-url url)]
                  (big-links/big-link-view url (or title-user title url) image)))]))
         :blockquote github-hint/github-hint-renderer
         :heading
         headers/render-heading

         :paragraph
         (fn [ctx node]
           (into [:p {:class "text-base text-tint-12 leading-relaxed"}]
                 (mapv #(transform/->hiccup ctx %) (:content node))))

         :bullet-list
         (fn [ctx node]
           (into [:ul {:class "ml-8 text-tint-12 text-base space-y-2"}]
                 (mapv #(transform/->hiccup ctx %)
                       (:content node))))

         :code
         (fn [_ctx node]
           (if (and (:info node) (str/starts-with? (:info node) "mermaid"))
             [:pre.mermaid (uui/raw (-> node :content first :text))]
             [:pre {:class "text-base"}
              [:code
               ;; protect from xss, do not use raw
               (-> node :content first :text)]]))

         :monospace
         (fn [_ctx node]
           [:code {:class "py-[1px] px-1.5 min-w-[1.625rem] ring-1 ring-inset ring-tint-4 bg-tint-2 rounded text-[.875em] inline break-words"
                   :style "font-family: var(--font-code); line-height: calc(max(1.20em, 1.25rem));"}
            (-> node :content first :text)])

         :table
         (fn [ctx node]
           (into [:table]
                 (mapv #(transform/->hiccup ctx %) (:content node))))

         :table-head
         (fn [ctx node]
           (into [:thead]
                 (mapv #(transform/->hiccup ctx %) (:content node))))

         :table-header
         (fn [ctx node]
           (into [:th]
                 (mapv #(transform/->hiccup ctx %) (:content node))))

         :table-body
         (fn [ctx node]
           (into [:tbody]
                 (mapv #(transform/->hiccup ctx %) (:content node))))

         :table-row
         (fn [ctx node]
           (into [:tr]
                 (mapv #(transform/->hiccup ctx %) (:content node))))

         :table-data
         (fn [ctx node]
           (into [:td]
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

               (and c
                    (= :table (first c))
                    (= :thead (first (nth c 2))))
               (update c 2 remove-selects)

               :else
               (uui/raw (-> node :content first :text)))))))

(defn render-md [context filepath parsed]
  (transform/->hiccup (renderers context filepath) parsed))

(defn hack-md [context filepath md-file]
  (let [context-hack
        (assoc context :parsing-in-hack-phase true)]
    (->> md-file
         big-links/hack-content-ref
         github-hint/hack-info
         image/hack-youtube
         image/hack-other-websites
         (tabs/hack-tabs
          context-hack
          filepath
          parse-markdown-content
          render-md)
         (gitbook-code/hack-gitbook-code
          context-hack
          filepath
          parse-markdown-content
          render-md)
         (description/hack-h1-and-description
          context
          filepath
          md/parse
          render-md))))

(defn set-parsed-markdown-index [context md-files-idx]
  (let [parsed-files
        (mapv #(parse-markdown-content context %) md-files-idx)]
    (println "Parsed " (count parsed-files) " files")
    (products/set-product-state
     context
     [const/PARSED_MARKDOWN_IDX]
     parsed-files)))

(defn get-parsed-markdown-index [context]
  (products/get-product-state
   context
   [const/PARSED_MARKDOWN_IDX]))

(defn get-rendered [context filepath]
  (get (products/get-product-state context [const/RENDERED])
       filepath))

(defn render-all! [context parsed-md-index read-markdown-file]
  (products/set-product-state
   context
   [const/RENDERED]
   (->> parsed-md-index
        (mapv
         (fn [{:keys [filepath _parsed]}]
           (println "render filepath " filepath)
           [filepath (read-markdown-file context filepath)]))
        (into {}))))
