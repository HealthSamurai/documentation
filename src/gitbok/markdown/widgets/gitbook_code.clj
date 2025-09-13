(ns gitbok.markdown.widgets.gitbook-code
  (:require
   [clojure.string :as str]
   [gitbok.utils :as utils]
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

(defn hack-gitbook-code
  [_context _filepath
   _parse-markdown-content-fn
   _render-md-fn
   content]
  (let [code-data (parse-code-blocks content)
        sorted-code (sort-by :start > code-data)]
    (reduce
     (fn [content code-block]
       (let [;; Get the original code content
             block-content (:content code-block)
             ;; Extract language from the content if it starts with ```
             ;; Remove the opening and closing ``` if present
             clean-content (-> block-content
                               (str/replace #"^```\w*\n?" "")
                               (str/replace #"\n?```$" ""))
             ;; Get the title
             title (get (:attributes code-block) :title)
             ;; Escape HTML entities for the content
             escaped-content (-> clean-content
                                 (str/replace "&" "&amp;")
                                 (str/replace "<" "&lt;")
                                 (str/replace ">" "&gt;")
                                 (str/replace "\"" "&quot;"))
             ;; Create HTML in a single line to prevent markdown splitting
             ;; Replace newlines with a placeholder that we'll restore later
             single-line-content (str/replace escaped-content #"\n" "%%%NL%%%")
             ;; Create the HTML all in one line
             html (str "<div class=\"code-gitbook group/codeblock grid grid-flow-col w-full decoration-primary/6 page-full-width:ml-0 max-w-3xl page-api-block:ml-0\">"
                       (when title
                         (str "<div class=\"flex items-center justify-start gap-2 text-sm [grid-area:1/1] -mb-px\">"
                              "<div class=\"relative top-px z-20 inline-flex items-center justify-center rounded-t straight-corners:rounded-t-s border border-tint-6 border-b-0 bg-tint-subtle theme-muted:bg-tint-base px-3 py-2 text-tint-11 text-xs leading-none tracking-wide [html.theme-bold.sidebar-filled_&]:bg-tint-base\">"
                              title
                              "</div></div>"))
                       "<div class=\"relative overflow-auto border border-tint-6 bg-tint-subtle theme-muted:bg-tint-base [grid-area:2/1] contrast-more:border-tint contrast-more:bg-tint-base [html.theme-bold.sidebar-filled_&]:bg-tint-base rounded-md straight-corners:rounded-sm rounded-ss-none\">"
                       "<pre class=\"text-base\"><code>"
                       single-line-content
                       "</code></pre>"
                       "</div></div>")]
         (str
          (utils/safe-subs content 0 (:start code-block))
          html
          (utils/safe-subs content (:end code-block)))))
     content
     sorted-code)))
