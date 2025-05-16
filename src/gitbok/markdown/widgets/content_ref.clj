(ns gitbok.markdown.widgets.content-ref
  "Widget for handling GitBook-style content reference blocks.
   Provides navigation links to other content with consistent styling."
  (:require
   [clojure.string :as str]))

(def content-ref-pattern
  "Pattern to match GitBook content-ref blocks.
   Handles various formats and spacing variations of content-ref blocks."
  #"(?s)\{\%\s*content-ref\s+url\s*=\s*(\\\\)?\"([^\"]+)(\\\\)?\"(?:\s+title\s*=\s*(\\\\)?\"([^\"]*)(\\\\)?\")?(?:\s+description\s*=\s*(\\\\)?\"([^\"]*)(\\\\)?\")?(?:\s+\%\}|\s*\%\})(.*?)(?:\{\%\s*endcontent-ref\s*\%\})")

(defn extract-link-text
  "Extract link text from markdown-style link if present"
  [content]
  (let [content-trimmed (str/trim content)
        link-match (re-find #"\[([^\]]+)\](?:\([^\)]+\))?" content-trimmed)]
    (if (and link-match (> (count link-match) 1))
      (second link-match)  ; Return just the text part from [text](url)
      content-trimmed)))   ; Return the whole content if not a markdown link

(defn parse-content-ref-block
  "Parse a GitBook content-ref block into a structured map."
  [text]
  (try
    (when-let [[_ _escaped1 url _escaped2 _escaped3 title _escaped4 _escaped5 description _escaped6 content]
               (re-matches content-ref-pattern text)]
      {:type :content-ref
       :url url
       :text (if (and title (not (str/blank? title)))
               title
               (extract-link-text content))
       :description description})
    (catch Exception e
      (println "Error parsing content-ref: " e)
      nil)))

(defn transform-content-ref
  "Transform a parsed content-ref block into hiccup markup.
   Creates a styled link block with title and arrow icon."
  [_ctx {:keys [url text description]}]
  [:div.content-ref
   {:class "my-4 p-4 border border-gray-200 rounded-md bg-gray-50 hover:bg-gray-100 transition-colors flex items-center"}
   [:div.content-ref-icon
    {:class "mr-3 text-blue-500"}
    [:span {:dangerouslySetInnerHTML {:__html "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"24\" height=\"24\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"currentColor\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\"><path d=\"M18 13v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h6\"></path><polyline points=\"15 3 21 3 21 9\"></polyline><line x1=\"10\" y1=\"14\" x2=\"21\" y2=\"3\"></line></svg>"}}]]
   [:div.content-ref-content
    {:class "flex-1"}
    [:a.content-ref-link
     {:href url
      :class "text-blue-600 hover:text-blue-800 font-medium"}
     text]
    (when (and description (not (str/blank? description)))
      [:div.content-ref-description
       {:class "mt-1 text-sm text-gray-600"}
       description])]])

;; Register content-ref block renderer
(def renderers
  {:content-ref transform-content-ref})
