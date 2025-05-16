(ns gitbok.markdown.widgets.hint
  "Widget for handling GitBook-style hint blocks.
   Supports warning and info styles with appropriate styling."
  (:require
   [nextjournal.markdown :as md]
   [nextjournal.markdown.transform :as transform]
   [clojure.string :as str]))


(def hint-styles
  {"info" {:bg-class "bg-blue-50"
           :border-class "border-blue-400"
           :text-class "text-blue-700"
           :icon-html "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" width=\"24\" height=\"24\" fill=\"currentColor\"><path d=\"M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-6h2v6zm0-8h-2V7h2v2z\"/></svg>"}
   "warning" {:bg-class "bg-yellow-50"
              :border-class "border-yellow-400"
              :text-class "text-yellow-700"
              :icon-html "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" width=\"24\" height=\"24\" fill=\"currentColor\"><path d=\"M1 21h22L12 2 1 21zm12-3h-2v-2h2v2zm0-4h-2v-4h2v4z\"/></svg>"}
   "success" {:bg-class "bg-green-50"
              :border-class "border-green-400"
              :text-class "text-green-700"
              :icon-html "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" width=\"24\" height=\"24\" fill=\"currentColor\"><path d=\"M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z\"/></svg>"}
   "danger" {:bg-class "bg-red-50"
             :border-class "border-red-400"
             :text-class "text-red-700"
             :icon-html "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" width=\"24\" height=\"24\" fill=\"currentColor\"><path d=\"M12 2C6.47 2 2 6.47 2 12s4.47 10 10 10 10-4.47 10-10S17.53 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm3.59-13L12 10.59 8.41 7 7 8.41 10.59 12 7 15.59 8.41 17 12 13.41 15.59 17 17 15.59 13.41 12 17 8.41z\"/></svg>"}})

;; (def hint-pattern
;;   "Pattern to match GitBook hint blocks.
;;    Format: {% hint style=\"style\" %}content{% endhint %}
;;    Also handles escaped quotes, spaces, and line breaks"
;;   #"(?s)\{\%\s*hint\s+style=(\\\\)?\"([^\"]+)(\\\\)?\"\s*\%\}(.*?)\{\%\s*endhint\s*\%\}")

;; (defn parse-hint-block
;;   "Parse a GitBook hint block into a structured map.
;;    Returns nil if the text doesn't match the hint pattern."
;;   [text]
;;   (try
;;     (when-let [[_ _escaped1 style _escaped2 content] (re-matches hint-pattern text)]
;;       (when (and style content)
;;         {:type :hint
;;          :style style
;;          :content (md/parse (str/trim content))}))
;;     (catch Exception e
;;       (println "Error parsing hint block: " e)
;;       nil)))


(defn svg-icon [icon-html text-class]
  [:div.hint-icon
   {:class (str "flex-shrink-0 " text-class)}
   [:span {:dangerouslySetInnerHTML {:__html icon-html}}]])

(defn transform-hint
  "Transform a parsed hint block into styled hiccup markup"
  [_ctx {:keys [style content]}]
  (let [style-data (get hint-styles style (get hint-styles "info"))
        {:keys [bg-class border-class text-class icon-html]} style-data]
    [:div.gitbook-hint
     {:class (str bg-class " " border-class)}
     [:div.gitbook-hint-header
      {:class text-class}
      (str (str/capitalize style))]
     [:div.hint-content
      {:class "flex gap-3"}
      (svg-icon icon-html text-class)
      [:div.hint-text
       (transform/->hiccup content)]]]))

;; Register hint block renderer
(def renderers
  {:hint transform-hint})
