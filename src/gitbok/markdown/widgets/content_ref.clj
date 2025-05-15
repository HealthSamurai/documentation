(ns gitbok.markdown.widgets.content-ref
  "Widget for handling GitBook-style content reference blocks.
   Provides navigation links to other content with consistent styling."
  (:require
   [nextjournal.markdown :as md]
   [nextjournal.markdown.transform :as transform]
   [clojure.string :as str]))

(def content-ref-pattern
  "Pattern to match GitBook content-ref blocks.
   Handles various formats and spacing variations of content-ref blocks."
  #"(?s)\{\%\s*content-ref\s+url\s*=\s*(\\\\)?\"([^\"]+)(\\\\)?\"(?:\s+title\s*=\s*(\\\\)?\"([^\"]*)(\\\\)?\")?(?:\s+description\s*=\s*(\\\\)?\"([^\"]*)(\\\\)?\")?(?:\s+\%\}|\s*\%\})(.*?)(?:\{\%\s*endcontent-ref\s*\%\})")

(defn extract-link-details
  "Extract link text and URL from markdown-style link.

   Parameters:
   - content: String containing markdown link like [text](url)

   Returns:
   {:text string  ; The link text
    :url string}  ; The link URL (falls back to the one from the block)"
  [content]
  (let [link-match (re-find #"\[([^\]]+)\]\(([^\)]+)\)" content)]
    (when link-match
      {:text (second link-match)
       :url (nth link-match 2 nil)})))

(defn parse-content-ref-block
  "Parse a GitBook content-ref block into a structured map.
   Returns nil if the text doesn't match the content-ref pattern.
   
   Parameters:
   - text: The text content containing the content-ref block
   
   Returns:
   {:type :content-ref
    :url string    ; The target URL
    :text string   ; The link text (from [text] or title attribute)
    :content ast}  ; Parsed markdown content (if any)"
  [text]
  (when-let [[_ _escaped1 url _escaped2 _escaped3 title _escaped4 _escaped5 description _escaped6 content] 
             (re-matches content-ref-pattern text)]
    (let [link-details (extract-link-details content)
          ;; Extract any additional content that's not part of the link
          additional-content (when content
                               (-> content
                                   (str/replace #"\[([^\]]+)\]\([^\)]+\)" "")
                                   str/trim))
          ;; Parse additional content as markdown if it exists
          parsed-content (when-not (str/blank? additional-content)
                           (md/parse additional-content))]
      (merge
       {:type :content-ref
        :url url
        :text (or (:text link-details) 
                  title
                  "View content")}
       (when description
         {:description description})
       (when parsed-content
         {:content parsed-content})))))

(defn transform-content-ref
  "Transform a parsed content-ref block into hiccup markup.
   Creates a styled link block with title and arrow icon.
   
   Parameters:
   - ctx: Context parameter (may contain theme settings)
   - block: The parsed content-ref block map
   
   Returns hiccup vector with styled link structure."
  [ctx {:keys [url text description content] :as block}]
  (cond-> [:div.content-ref.w-full.my-4.p-4.border.border-slate-200.rounded-md.bg-slate-50
          ;; Link element with icon
          [:div.content-ref-link.flex.items-center.gap-2
           ;; Right arrow icon
           [:svg.icon.w-4.h-4.text-slate-500
            {:xmlns "http://www.w3.org/2000/svg"
             :viewBox "0 0 24 24"
             :fill "none"
             :stroke "currentColor"
             :stroke-width "2"}
            [:path 
             {:stroke-linecap "round"
              :stroke-linejoin "round"
              :d "M9 5l7 7-7 7"}]]
           ;; Link with consistent styling
           [:a.text-slate-900.no-underline.font-medium
            {:href url}
            text]]]
    
    ;; Add description if provided
    description
    (conj [:div.content-ref-description.mt-2.text-sm.text-slate-600
           description])
    
    ;; Conditionally add content if it exists
    (and content (not-empty (:content content)))
    (conj [:div.content-ref-content.mt-2.text-slate-700
           (transform/->hiccup content)])))

;; Register content-ref block renderer
(def renderers
  {:content-ref transform-content-ref})
