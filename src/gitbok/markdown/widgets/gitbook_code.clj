(ns gitbok.markdown.widgets.gitbook-code
  (:require
   [clojure.string :as str]
   [gitbok.utils :as utils]
   [uui]
   [hiccup2.core]))

;; {% code title="My cool code" lineNumbers="true" %}
;; ```
;; somecode
;; ```
;; {% endcode %}

(defn- parse-attributes [attr-string]
  (when attr-string
    (let [pattern #"(\w+)=\"([^\"]+)\""
          matches (re-seq pattern attr-string)
          attrs (reduce (fn [acc [_ key value]]
                          (assoc acc (keyword key) value))
                        {}
                        matches)]
      attrs)))

(defn- parse-code-blocks [content]
  (let [code-start "{% code"
        code-end "{% endcode %}"

        find-code-blocks
        (fn [^String content]
          (loop [remaining content
                 blocks []
                 offset 0]
            (if remaining
              (if-let [start-pos (.indexOf ^String remaining code-start)]
                (if-let [end-pos (.indexOf ^String remaining code-end (+ start-pos (count code-start)))]
                  (let [block-start (+ offset start-pos)
                        block-end (+ offset end-pos (count code-end))
                        block-content (utils/safe-subs remaining (+ start-pos (count code-start)) end-pos)]
                    (if block-content
                      (recur (utils/safe-subs remaining (+ end-pos (count code-end)))
                             (conj blocks {:start block-start
                                           :end block-end
                                           :content block-content})
                             (+ offset end-pos (count code-end)))
                      blocks))
                  blocks)
                blocks)
              blocks)))

        parse-code-in-block
        (fn [block-content block-start]
          (let [lines (str/split-lines block-content)
                first-line (first lines)
                rest-lines (rest lines)]
            (if (and first-line (str/includes? first-line "%}"))
              (let [attr-end (.indexOf ^String first-line "%}")
                    attr-string (utils/safe-subs first-line 0 attr-end)
                    attributes (parse-attributes attr-string)
                    code-content (str/join "\n" rest-lines)]
                {:attributes attributes
                 :content (str/trim code-content)
                 :start block-start
                 :end (+ block-start (count block-content))})
              nil)))

        blocks (find-code-blocks content)]

    (for [block blocks
          :let [parsed (parse-code-in-block (:content block) (:start block))]
          :when parsed]
      {:text (utils/safe-subs content (:start block) (:end block))
       :start (:start block)
       :end (:end block)
       :attributes (:attributes parsed)
       :content (:content parsed)})))

(defn- render-code-hiccup [context filepath code-data
                           parse-markdown-content-fn
                           render-md-fn]
  (let [title (get (:attributes code-data) :title)
        ;; line-numbers (get (:attributes code-data) :lineNumbers)
        content (:content code-data)
        parsed-content (:parsed (parse-markdown-content-fn context [filepath content]))
        raw-html (render-md-fn context filepath parsed-content)
        #_(if line-numbers
            (str/replace raw-html #"nohljsln" "")
            raw-html)]
    [:div {:class "bg-white border border-gray-200 rounded-lg overflow-hidden mb-4"}
     (when title
       [:div {:class "bg-gray-50 px-4 py-3 border-b border-gray-200"}
        [:h3 {:class "text-lg font-medium text-gray-900"} title]])
     raw-html]))

(defn hack-gitbook-code
  [context filepath
   parse-markdown-content-fn
   render-md-fn
   content]
  (let [code-data (parse-code-blocks content)
        sorted-code (sort-by :start > code-data)]
    (reduce
     (fn [content code-block]
       (let [hiccup
             (render-code-hiccup context filepath code-block
                                 parse-markdown-content-fn
                                 render-md-fn)
             html
             (hiccup2.core/html hiccup)

             processed-html
             (->
              html
              (str/replace #"\n\n" "\n\u00A0\n")
              ;; (str/replace #"\n" "\n\u00A0")
              (str/replace #"\n\<" "<")
              (str/replace #"\n\s*\<" "<")
              (str/replace #"\n \<" "<")
               ;; (str/replace #"}\<" "}\n<")
               )]
         (str
          (utils/safe-subs content 0 (:start code-block))
          processed-html
          (utils/safe-subs content (:end code-block)))))
     content
     sorted-code)))
