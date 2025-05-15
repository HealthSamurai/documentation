(ns gitbok.markdown.widgets.github-hint
  "Widget for handling GitHub-style hint/admonition blocks.
   Supports NOTE, TIP, IMPORTANT, WARNING, and CAUTION styles with appropriate styling."
  (:require
   [nextjournal.markdown :as md]
   [nextjournal.markdown.transform :as transform]
   [clojure.string :as str]))

(def github-hint-pattern
  "Pattern to match GitHub-style admonition blocks in blockquotes.
   Format: > [!TYPE]\n> Content"
  #"(?m)^>\s*\[\!([A-Z]+)\]\s*\n((?:>\s*.*(?:\n|$))+)")

(def supported-types
  "Map of supported hint types to their styling"
  {"NOTE" {:class "bg-blue-50 border-blue-200"
           :icon [:svg.w-5.h-5.text-blue-500
                  {:xmlns "http://www.w3.org/2000/svg"
                   :viewBox "0 0 24 24"
                   :fill "currentColor"}
                  [:path
                   {:fill-rule "evenodd"
                    :clip-rule "evenodd"
                    :d "M2.25 12c0-5.385 4.365-9.75 9.75-9.75s9.75 4.365 9.75 9.75-4.365 9.75-9.75 9.75S2.25 17.385 2.25 12zm8.706-1.442c1.146-.573 2.437.463 2.126 1.706l-.709 2.836.042-.02a.75.75 0 01.67 1.34l-.04.022c-1.147.573-2.438-.463-2.127-1.706l.71-2.836-.042.02a.75.75 0 11-.671-1.34l.041-.022zM12 9a.75.75 0 100-1.5.75.75 0 000 1.5z"}]]}
   "TIP" {:class "bg-green-50 border-green-200"
          :icon [:svg.w-5.h-5.text-green-500
                 {:xmlns "http://www.w3.org/2000/svg"
                  :viewBox "0 0 24 24"
                  :fill "currentColor"}
                 [:path
                  {:fill-rule "evenodd"
                   :clip-rule "evenodd"
                   :d "M12 2.25c-5.385 0-9.75 4.365-9.75 9.75s4.365 9.75 9.75 9.75 9.75-4.365 9.75-9.75S17.385 2.25 12 2.25zm.53 5.47a.75.75 0 00-1.06 0l-3 3a.75.75 0 101.06 1.06l1.72-1.72v5.69a.75.75 0 001.5 0v-5.69l1.72 1.72a.75.75 0 101.06-1.06l-3-3z"}]]}
   "IMPORTANT" {:class "bg-purple-50 border-purple-200"
                :icon [:svg.w-5.h-5.text-purple-500
                       {:xmlns "http://www.w3.org/2000/svg"
                        :viewBox "0 0 24 24"
                        :fill "currentColor"}
                       [:path
                        {:fill-rule "evenodd"
                         :clip-rule "evenodd"
                         :d "M2.25 12c0-5.385 4.365-9.75 9.75-9.75s9.75 4.365 9.75 9.75-4.365 9.75-9.75 9.75S2.25 17.385 2.25 12zM12 8.25a.75.75 0 01.75.75v3.75a.75.75 0 01-1.5 0V9a.75.75 0 01.75-.75zm0 8.25a.75.75 0 100-1.5.75.75 0 000 1.5z"}]]}
   "WARNING" {:class "bg-amber-50 border-amber-200"
              :icon [:svg.w-5.h-5.text-amber-500
                     {:xmlns "http://www.w3.org/2000/svg"
                      :viewBox "0 0 24 24"
                      :fill "currentColor"}
                     [:path
                      {:fill-rule "evenodd"
                       :clip-rule "evenodd"
                       :d "M9.401 3.003c1.155-2 4.043-2 5.197 0l7.355 12.748c1.154 2-.29 4.5-2.599 4.5H4.645c-2.309 0-3.752-2.5-2.598-4.5L9.4 3.003zM12 8.25a.75.75 0 01.75.75v3.75a.75.75 0 01-1.5 0V9a.75.75 0 01.75-.75zm0 8.25a.75.75 0 100-1.5.75.75 0 000 1.5z"}]]}
   "CAUTION" {:class "bg-red-50 border-red-200"
              :icon [:svg.w-5.h-5.text-red-500
                     {:xmlns "http://www.w3.org/2000/svg"
                      :viewBox "0 0 24 24"
                      :fill "currentColor"}
                     [:path
                      {:fill-rule "evenodd"
                       :clip-rule "evenodd"
                       :d "M12 2.25c-5.385 0-9.75 4.365-9.75 9.75s4.365 9.75 9.75 9.75 9.75-4.365 9.75-9.75S17.385 2.25 12 2.25zm-1.72 6.97a.75.75 0 10-1.06 1.06L10.94 12l-1.72 1.72a.75.75 0 101.06 1.06L12 13.06l1.72 1.72a.75.75 0 101.06-1.06L13.06 12l1.72-1.72a.75.75 0 10-1.06-1.06L12 10.94l-1.72-1.72z"}]]}})

(defn extract-content
  "Extract content from blockquote format by removing '> ' prefixes"
  [content]
  (->> (str/split-lines content)
       (map #(if (str/starts-with? % "> ")
               (subs % 2)
               (if (= % ">") "" %)))
       (str/join "\n")))

(defn parse-github-hint
  "Parse a GitHub-style hint block into a structured map.
   Returns nil if the text doesn't match the GitHub hint pattern.
   
   Parameters:
   - text: The text content containing the GitHub hint block
   
   Returns:
   {:type :github-hint
    :hint-type string    ; The hint type (NOTE, TIP, etc.)
    :content ast}        ; Parsed markdown content"
  [text]
  (when-let [[_ hint-type content] (re-matches github-hint-pattern text)]
    (when (contains? supported-types hint-type)
      {:type :github-hint
       :hint-type hint-type
       :content (md/parse (extract-content content))})))

(defn transform-github-hint
  "Transform a parsed GitHub hint block into hiccup markup.
   Applies appropriate styling based on hint type.
   
   Parameters:
   - ctx: Context parameter (may contain theme settings)
   - block: The parsed GitHub hint block map
   
   Returns hiccup vector with styled div structure."
  [ctx {:keys [hint-type content] :as block}]
  (let [{:keys [class icon]} (get supported-types hint-type)]
    [:div.github-hint.w-full.my-4.p-4.border.rounded-md
     {:class class}
     [:div.github-hint-header.flex.items-center.gap-2.font-medium.mb-2
      icon
      hint-type]
     [:div.github-hint-content
      (transform/->hiccup content)]]))

;; Register GitHub hint block renderer
(def renderers
  {:github-hint transform-github-hint}) 