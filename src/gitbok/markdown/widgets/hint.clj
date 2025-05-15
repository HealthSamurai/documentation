(ns gitbok.markdown.widgets.hint
  "Widget for handling GitBook-style hint blocks.
   Supports warning and info styles with appropriate styling."
  (:require
   [nextjournal.markdown :as md]
   [nextjournal.markdown.transform :as transform]
   [clojure.string :as str]))

(def hint-pattern
  "Pattern to match GitBook hint blocks.
   Format: {% hint style=\"style\" %}content{% endhint %}
   Also handles escaped quotes: {% hint style=\\\"style\\\" %}content{% endhint %}"
  #"(?s)\{\% hint style=(\\\\)?\"([^\"]+)(\\\\)?\" \%\}(.*?)\{\% endhint \%\}")

(defn parse-hint-block
  "Parse a GitBook hint block into a structured map.
   Returns nil if the text doesn't match the hint pattern.
   
   Parameters:
   - text: The text content containing the hint block
   
   Returns:
   {:type :hint
    :style string   ; The hint style (warning, info)
    :content ast}   ; Parsed markdown content"
  [text]
  (when-let [[_ _escaped1 style _escaped2 content] (re-matches hint-pattern text)]
    (when (and style content)
      {:type :hint
       :style style
       :content (md/parse (str/trim content))})))

(defn transform-hint
  "Transform a parsed hint block into hiccup markup.
   Applies appropriate styling based on hint style.
   
   Parameters:
   - ctx: Context parameter (may contain theme settings)
   - block: The parsed hint block map
   
   Returns hiccup vector with styled div structure."
  [ctx {:keys [style content] :as block}]
  (let [style-class (case style
                      "warning" "bg-amber-50 border-amber-200"
                      "info" "bg-sky-50 border-sky-200"
                      "bg-slate-50 border-slate-200")]
    [:div.hint.w-full.my-4.p-4.border.rounded-md
     {:class style-class}
     (transform/->hiccup content)]))

;; Register hint block renderer
(def renderers
  {:hint transform-hint})
