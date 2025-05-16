(ns gitbok.markdown.widgets.github-hint
  "Widget for handling GitHub-style hint/admonition blocks."
  (:require
   [nextjournal.markdown :as md]
   [nextjournal.markdown.transform :as transform]
   [clojure.string :as str]))

(def hint-styles
  {"NOTE" {:bg-class "bg-blue-50"
           :border-class "border-blue-400"
           :text-class "text-blue-700"
           :icon "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" width=\"20\" height=\"20\" fill=\"currentColor\"><path fill-rule=\"evenodd\" d=\"M2.25 12c0-5.385 4.365-9.75 9.75-9.75s9.75 4.365 9.75 9.75-4.365 9.75-9.75 9.75S2.25 17.385 2.25 12zm8.706-1.442c1.146-.573 2.437.463 2.126 1.706l-.709 2.836.042-.02a.75.75 0 01.67 1.34l-.04.022c-1.147.573-2.438-.463-2.127-1.706l.71-2.836-.042.02a.75.75 0 11-.671-1.34l.041-.022zM12 9a.75.75 0 100-1.5.75.75 0 000 1.5z\"></path></svg>"}

   "TIP" {:bg-class "bg-green-50"
          :border-class "border-green-400"
          :text-class "text-green-700"
          :icon "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" width=\"20\" height=\"20\" fill=\"currentColor\"><path fill-rule=\"evenodd\" d=\"M12 2.25c-5.385 0-9.75 4.365-9.75 9.75s4.365 9.75 9.75 9.75 9.75-4.365 9.75-9.75S17.385 2.25 12 2.25zm.53 5.47a.75.75 0 00-1.06 0l-3 3a.75.75 0 101.06 1.06l1.72-1.72v5.69a.75.75 0 001.5 0v-5.69l1.72 1.72a.75.75 0 101.06-1.06l-3-3z\"></path></svg>"}

   "IMPORTANT" {:bg-class "bg-purple-50"
                :border-class "border-purple-400"
                :text-class "text-purple-700"
                :icon "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" width=\"20\" height=\"20\" fill=\"currentColor\"><path fill-rule=\"evenodd\" d=\"M2.25 12c0-5.385 4.365-9.75 9.75-9.75s9.75 4.365 9.75 9.75-4.365 9.75-9.75 9.75S2.25 17.385 2.25 12zM12 8.25a.75.75 0 01.75.75v3.75a.75.75 0 01-1.5 0V9a.75.75 0 01.75-.75zm0 8.25a.75.75 0 100-1.5.75.75 0 000 1.5z\"></path></svg>"}

   "WARNING" {:bg-class "bg-yellow-50"
              :border-class "border-yellow-400"
              :text-class "text-yellow-700"
              :icon "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" width=\"20\" height=\"20\" fill=\"currentColor\"><path fill-rule=\"evenodd\" d=\"M9.401 3.003c1.155-2 4.043-2 5.197 0l7.355 12.748c1.154 2-.29 4.5-2.599 4.5H4.645c-2.309 0-3.752-2.5-2.598-4.5L9.4 3.003zM12 8.25a.75.75 0 01.75.75v3.75a.75.75 0 01-1.5 0V9a.75.75 0 01.75-.75zm0 8.25a.75.75 0 100-1.5.75.75 0 000 1.5z\"></path></svg>"}

   "CAUTION" {:bg-class "bg-red-50"
              :border-class "border-red-400"
              :text-class "text-red-700"
              :icon "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" width=\"20\" height=\"20\" fill=\"currentColor\"><path fill-rule=\"evenodd\" d=\"M2.25 12c0-5.385 4.365-9.75 9.75-9.75s9.75 4.365 9.75 9.75-4.365 9.75-9.75 9.75S2.25 17.385 2.25 12zM12 8.25a.75.75 0 01.75.75v3.75a.75.75 0 01-1.5 0V9a.75.75 0 01.75-.75zm0 8.25a.75.75 0 100-1.5.75.75 0 000 1.5z\"></path></svg>"}})

;; Pattern to match GitHub-style admonition blocks
(def github-hint-pattern
  #"(?s)\[!(NOTE|TIP|IMPORTANT|WARNING|CAUTION)\](.*?)(\n\n|\n(?=\[!|\{% |\Z)|$)")

(defn extract-content
  "Extract content from a GitHub-style hint block."
  [text]
  (str/trim text))

(defn parse-github-hint
  "Parse a GitHub-style hint block into a structured map."
  [text]
  (try
    (when-let [match (re-find #"\[!(NOTE|TIP|IMPORTANT|WARNING|CAUTION)\](.*?)(\n\n|\n(?=\[!|\{% |\Z)|$)" text)]
      (println "Parsing GitHub hint: " (subs text 0 (min 30 (count text))) "...")
      (let [hint-type (nth match 1)  ;; Extracted type
            content (str/trim (nth match 2))] ;; Extracted content
        (println "Extracted hint-type:" hint-type ", content length:" (count content))
        {:type :github-hint
         :hint-type hint-type
         :title ""
         :content (md/parse content)}))
    (catch Exception e
      (println "Error parsing GitHub hint: " e)
      nil)))

;; Helper function to create an SVG icon element with dangerouslySetInnerHTML
(defn svg-icon [icon-html text-class]
  [:span
   {:class text-class}
   [:span {:dangerouslySetInnerHTML {:__html icon-html}}]])

(defn transform-github-hint
  "Transform a parsed GitHub hint block into HTML directly."
  [ctx {:keys [hint-type title content]}]
  (let [{:keys [bg-class border-class text-class icon]} (get hint-styles hint-type)]
    [:div.github-hint
     {:class (str bg-class " " border-class)}
     [:div.github-hint-header
      {:class text-class}
      (svg-icon icon text-class)
      [:span
       {:class (str "ml-2")}
       (if (not (str/blank? title))
         (str hint-type ": " title)
         hint-type)]]
     [:div.github-hint-content
      (into [:div]
            (map #(transform/->hiccup ctx %) content))]]))

;; Register GitHub hint block renderer
(def renderers
  {:github-hint transform-github-hint})
