(ns gitbok.markdown.core
  (:require
   [clojure.string :as str]
   [clojure.tools.logging :as log]
   [clojure.walk :as walk]
   [gitbok.state :as state]
   [gitbok.markdown.widgets.big-links :as big-links]
   [gitbok.markdown.widgets.link :as link]
   [gitbok.markdown.widgets.headers :as headers]
   [gitbok.markdown.widgets.image :as image]
   [gitbok.markdown.widgets.github-hint :as github-hint]
   [gitbok.markdown.widgets.cards :as cards]
   [gitbok.markdown.widgets.tabs :as tabs]
   [gitbok.markdown.widgets.stepper :as stepper]
   [gitbok.markdown.widgets.description :as description]
   [gitbok.markdown.widgets.gitbook-code :as gitbook-code]
   [gitbok.ui.fhir-structure-table :as fhir-table]
   [cheshire.core :as json]
   [nextjournal.markdown :as md]
   [nextjournal.markdown.transform :as transform]
   [hickory.core]
   [gitbok.products :as products]
   [hiccup2.core]
   [nextjournal.markdown.utils :as u]))

(def rendered-key ::rendered)

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
   :title (description/parse-title content filepath)
   :parsed
   (md/parse*
    custom-doc
    (hack-md context filepath content))})

(defn parse-html [html]
  (map hickory.core/as-hiccup
       (hickory.core/parse-fragment html)))

(defn get-meta-from-url [url]
  (try
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
               (last))})
    (catch Exception e
      ;; Log warning but don't crash - return defaults for any fetch error
      (println (str "Warning: Could not fetch metadata from URL: " url
                    " (" (.getMessage e) ")"))
      {:image nil
       :title nil})))

(defn remove-selects [form]
  (walk/postwalk
   (fn [node]
     (if (and (vector? node)
              (= (first node) :select))
       nil
       node))
   form))

(defn parse-inline-markdown
  "Parse inline markdown for FHIR table descriptions"
  [text]
  (when text
    (try
      (let [parsed (md/parse text)
            hiccup (transform/->hiccup transform/default-hiccup-renderers parsed)]
        ;; Extract content from :doc > :paragraph wrapper
        (if (and (vector? hiccup) (= :doc (first hiccup)))
          (let [content (get hiccup 2)]
            (if (and (vector? content) (= :paragraph (first content)))
              (into [:span] (drop 2 content))
              content))
          hiccup))
      (catch Exception e
        ;; Fallback to plain text if parsing fails
        (log/warn e "Failed to parse inline markdown, falling back to plain text" {:text text})
        [:span text]))))

(defn renderers [context filepath]
  (assoc transform/default-hiccup-renderers
         :big-link (partial big-links/render-big-link context filepath)
         :big-link1 (partial big-links/render-big-link context filepath)
         :image (partial image/image-renderer context filepath)
         :link (partial link/link-renderer context filepath)
         :internal-link link/link-renderer

         :numbered-list
         (fn [ctx node]
           (let [start-num (:start node 1)]  ; Parser provides :start directly
             (into
              [:ol
               (merge
                {:class "min-w-0 w-full decoration-primary/6 page-full-width:ml-0 max-w-3xl page-api-block:ml-0 space-y-2 text-on-surface-secondary"}
                (when (not= start-num 1)
                  {:start start-num}))]
              (map-indexed
               (fn [idx child]
                 (let [actual-num (+ idx start-num)]
                   (transform/->hiccup
                    ctx
                    (assoc child :numbered-list-idx (dec actual-num)))))
               (:content node)))))

         :list-item
         (fn [ctx {:keys [numbered-list-idx] :as node}]
           (if numbered-list-idx
             [:li {:class "leading-normal flex items-start"}
              [:div {:class "text-base leading-normal mr-1 flex min-h-[1lh] min-w-6 items-center justify-center text-on-surface-secondary"}
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
           (into [:p {:class "text-base text-on-surface-secondary leading-relaxed"}]
                 (mapv #(transform/->hiccup ctx %) (:content node))))

         :bullet-list
         (fn [ctx node]
           (into [:ul {:class "text-base text-on-surface-secondary space-y-2"}]
                 (mapv #(transform/->hiccup ctx %)
                       (:content node))))

         ;; Handle LaTeX formulas from nextjournal/markdown
         :formula
         (fn [_ctx node]
           (let [tex (:text node)]
             (if (or (str/includes? tex "\\supset")
                     (str/includes? tex "\\le")
                     (str/includes? tex "\\ge"))
               [:span.katex-inline tex]
               tex)))
         ;;
         ;; ;; Handle block formulas (when formula is on separate lines)
         ;; :block-formula
         ;; (fn [_ctx node]
         ;;   (def nn node)
         ;;   (let [tex (:text node)]
         ;;     [:div.katex-display tex]))

         :code
         (fn [_ctx node]
           (cond
             ;; Mermaid diagrams
             (and (:info node) (str/starts-with? (:info node) "mermaid"))
             [:pre.mermaid (-> node :content first :text)]

             ;; FHIR Structure table
             (and (:info node) (= (:info node) "fhir-structure"))
             (try
               (let [json-text (-> node :content first :text)
                     elements (json/parse-string json-text true)
                     ;; Get resource type from annotation (added by annotate-fhir-structures-with-headings)
                     resource-type (:fhir-resource-type node)]
                 (fhir-table/render-table elements parse-inline-markdown
                                          (when resource-type {:resource-type resource-type})))
               (catch Exception e
                 (log/error e "Failed to parse FHIR structure table")
                 [:div {:class "text-red-600 p-4 border border-red-300 rounded"}
                  [:p [:strong "Error parsing FHIR structure table"]]
                  [:p {:class "text-sm"} (str "Error: " (.getMessage e))]]))

             ;; Regular code blocks
             :else
             (let [code-text (-> node :content first :text)
                   info-lang (when (and (:info node) (not= (:info node) ""))
                               (:info node))

                   detected-lang (when-not info-lang
                                   ;; Auto-detect only bash and yaml if not specified
                                   (cond
                                     ;; commented - bash envs are ugly yellow
                                     ;; Bash - environment variables with = (multiline mode)
                                     ;; (re-find #"(?m)^\s*[A-Z_]+[A-Z0-9_]*\s*=" code-text)
                                     ;; "bash"

                                     ;; YAML - key: value structure (multiline mode)
                                     (re-find #"(?m)^\s*\w+:\s" code-text)
                                     "yaml"

                                     ;; Default: no language
                                     :else nil))
                   lang (or info-lang detected-lang)]
               [:pre {:class "markdown-code text-base"}
                [:code (when lang
                         {:class (str "language-" lang)})
                 ;; protect from xss, do not use raw
                 code-text]])))

         :monospace
         (fn [_ctx node]
           [:code (-> node :content first :text)])

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
               (hiccup2.core/raw (-> node :content first :text)))))

         :html-block
         (fn [_ctx node]
           (let [html-text (-> node :content first :text)
                 ;; Replace our markers in the HTML text
                 fixed-html (-> html-text
                                (str/replace #"%%%NL%%%" "\n")
                                (str/replace #"%%%TAB_NL%%%" "\n")
                                (str/replace #"%%%STEPPER_NL%%%" "\n")
                                (str/replace #"%%GITBOOK_EMPTY_LINE%%" "\n")
                                (str/replace #"<!-- GITBOOK_NL -->" ""))
                 c (first (parse-html fixed-html))]
             (cond
               (and c
                    (= :table (first c))
                    (= {:data-view "cards"} (second c)))
               (cards/render-cards-from-table context filepath c)

               (and c
                    (= :table (first c))
                    (= {:data-header-hidden ""} (second c)))
               (hiccup2.core/raw (-> fixed-html
                                     (str/replace #"\<thead.*/thead>" "")))

               (and c
                    (= :table (first c))
                    (= :thead (first (nth c 2))))
               (update c 2 remove-selects)

               :else
               (hiccup2.core/raw fixed-html))))))

(defn annotate-fhir-structures-with-headings
  "Walk through parsed markdown and annotate fhir-structure blocks with preceding heading text"
  [parsed]
  (let [process-nodes (fn process-nodes [nodes last-heading]
                        (loop [result []
                               current-heading last-heading
                               [node & remaining] nodes]
                          (if-not node
                            [result current-heading]
                            (let [is-heading? (= :heading (:type node))
                                  is-fhir-structure? (and (= :code (:type node))
                                                          (= "fhir-structure" (:info node)))
                                  heading-text (when is-heading?
                                                 (-> node :content first :text))
                                  ;; Update heading BEFORE processing children
                                  new-heading (if is-heading? heading-text current-heading)
                                  ;; Recursively process children with updated heading
                                  processed-node (if (:content node)
                                                   (let [[processed-children _] (process-nodes (:content node) new-heading)]
                                                     (assoc node :content processed-children))
                                                   node)
                                  ;; Annotate fhir-structure nodes
                                  annotated-node (if (and is-fhir-structure? current-heading)
                                                   (assoc processed-node :fhir-resource-type current-heading)
                                                   processed-node)]
                              (recur (conj result annotated-node)
                                     new-heading
                                     remaining)))))]
    (if-let [children (:content parsed)]
      (let [[annotated-children _] (process-nodes children nil)]
        (assoc parsed :content annotated-children))
      parsed)))

(defn render-md [context filepath parsed]
  (let [;; First pass: annotate fhir-structure blocks with preceding headings
        annotated-parsed (annotate-fhir-structures-with-headings parsed)
        hiccup (transform/->hiccup (renderers context filepath) annotated-parsed)]
    ;; Clean up any temporary markers from the final output
    (walk/postwalk
     (fn [node]
       (if (string? node)
         ;; Replace markers with actual newlines
         (-> node
             (str/replace #"%%%NL%%%" "\n")
             (str/replace #"%%%TAB_NL%%%" "\n")
             (str/replace #"%%%STEPPER_NL%%%" "\n")
             (str/replace #"%%GITBOOK_EMPTY_LINE%%" "\n")
             (str/replace #"<!-- GITBOOK_NL -->" ""))
         node))
     hiccup)))

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
         (stepper/hack-stepper
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

(defn parse-all-files
  "Parse markdown files and return the parsed result without saving to state.
   Takes md-files-idx (map of filepath -> content) and returns vector of parsed files."
  [context md-files-idx]
  (log/info "Parsing markdown files" {:count (count md-files-idx)})
  (let [start-time (System/currentTimeMillis)
        ;; Process in batches to reduce future overhead
        batch-size 50
        batches (partition-all batch-size md-files-idx)
        ;; Process batches in parallel, files within batch sequentially
        parsed-files (->> batches
                          (pmap (fn [batch]
                                       ;; Process files within batch sequentially
                                  (map (fn [[filepath content]]
                                         (try
                                           (parse-markdown-content context [filepath content])
                                           (catch Exception e
                                             (log/error e "Failed to parse" {:filepath filepath})
                                             {:filepath filepath
                                              :description ""
                                              :title "Parse Error"
                                              :parsed [:div "Parse error"]})))
                                       batch)))
                          doall
                          (mapcat identity)
                          vec)
        duration (- (System/currentTimeMillis) start-time)]
    (log/info "files parsed" {:count (count parsed-files)
                              :duration-ms duration})
    parsed-files))

(defn set-parsed-markdown-index [context md-files-idx]
  (let [parsed-files (parse-all-files context md-files-idx)]
    (state/set-parsed-markdown-idx! context parsed-files)))

(defn get-parsed-markdown-index [context]
  (state/get-parsed-markdown-idx context))

(defn get-rendered [context filepath]
  (get (products/get-product-state context [rendered-key])
       filepath))

(defn render-all! [context parsed-md-index read-markdown-file]
  (log/info "Pre-rendering all pages" {:count (count parsed-md-index)})
  (let [start-time (System/currentTimeMillis)
        ;; Process in batches to reduce future overhead
        batch-size 50
        batches (partition-all batch-size parsed-md-index)
        ;; Process batches in parallel, files within batch sequentially
        rendered (into {}
                       (mapcat identity
                               (doall
                                (pmap (fn [batch]
                                       ;; Process files within batch sequentially
                                        (map (fn [{:keys [filepath _parsed]}]
                                               (try
                                                 (log/debug "render file" {:filepath filepath})
                                                 [filepath (read-markdown-file context filepath)]
                                                 (catch Exception e
                                                   (log/error e "Failed to render" {:filepath filepath})
                                                   [filepath {:content [:div "Render error"]
                                                              :title "Error"
                                                              :description ""}])))
                                             batch))
                                      batches))))
        duration (- (System/currentTimeMillis) start-time)]
    (log/info "Rendering complete" {:count (count rendered)
                                    :duration-ms duration})
    (products/set-product-state
     context
     [rendered-key]
     rendered)))
