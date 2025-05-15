(ns gitbok.markdown.widgets.github-hint
  "Widget for handling GitHub-style hint/admonition blocks."
  (:require
   [nextjournal.markdown :as md]
   [nextjournal.markdown.transform :as transform]
   [clojure.string :as str]
   [hiccup2.core :as hiccup2]))

;; Direct mapping of hint types to their HTML representation
(def hint-type-styles
  {"NOTE" {:html "<div style=\"margin: 16px 0; padding: 16px; border-left: 4px solid #54aeff; background-color: #ddf4ff; border-radius: 6px;\">
                   <div style=\"display: flex; align-items: center; margin-bottom: 8px; color: #0969da; font-weight: 600;\">
                     <svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" width=\"20\" height=\"20\" fill=\"currentColor\" style=\"color: #0969da;\">
                       <path fill-rule=\"evenodd\" d=\"M2.25 12c0-5.385 4.365-9.75 9.75-9.75s9.75 4.365 9.75 9.75-4.365 9.75-9.75 9.75S2.25 17.385 2.25 12zm8.706-1.442c1.146-.573 2.437.463 2.126 1.706l-.709 2.836.042-.02a.75.75 0 01.67 1.34l-.04.022c-1.147.573-2.438-.463-2.127-1.706l.71-2.836-.042.02a.75.75 0 11-.671-1.34l.041-.022zM12 9a.75.75 0 100-1.5.75.75 0 000 1.5z\"></path>
                     </svg>
                     <span style=\"margin-left: 8px; color: #0969da;\">NOTE</span>
                   </div>
                   <div style=\"margin-top: 8px;\">"}
   "TIP" {:html "<div style=\"margin: 16px 0; padding: 16px; border-left: 4px solid #56d364; background-color: #dafbe1; border-radius: 6px;\">
                  <div style=\"display: flex; align-items: center; margin-bottom: 8px; color: #1a7f37; font-weight: 600;\">
                    <svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" width=\"20\" height=\"20\" fill=\"currentColor\" style=\"color: #1a7f37;\">
                      <path fill-rule=\"evenodd\" d=\"M12 2.25c-5.385 0-9.75 4.365-9.75 9.75s4.365 9.75 9.75 9.75 9.75-4.365 9.75-9.75S17.385 2.25 12 2.25zm.53 5.47a.75.75 0 00-1.06 0l-3 3a.75.75 0 101.06 1.06l1.72-1.72v5.69a.75.75 0 001.5 0v-5.69l1.72 1.72a.75.75 0 101.06-1.06l-3-3z\"></path>
                    </svg>
                    <span style=\"margin-left: 8px; color: #1a7f37;\">TIP</span>
                  </div>
                  <div style=\"margin-top: 8px;\">"}
   "IMPORTANT" {:html "<div style=\"margin: 16px 0; padding: 16px; border-left: 4px solid #d2a8ff; background-color: #fbefff; border-radius: 6px;\">
                        <div style=\"display: flex; align-items: center; margin-bottom: 8px; color: #8250df; font-weight: 600;\">
                          <svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" width=\"20\" height=\"20\" fill=\"currentColor\" style=\"color: #8250df;\">
                            <path fill-rule=\"evenodd\" d=\"M2.25 12c0-5.385 4.365-9.75 9.75-9.75s9.75 4.365 9.75 9.75-4.365 9.75-9.75 9.75S2.25 17.385 2.25 12zM12 8.25a.75.75 0 01.75.75v3.75a.75.75 0 01-1.5 0V9a.75.75 0 01.75-.75zm0 8.25a.75.75 0 100-1.5.75.75 0 000 1.5z\"></path>
                          </svg>
                          <span style=\"margin-left: 8px; color: #8250df;\">IMPORTANT</span>
                        </div>
                        <div style=\"margin-top: 8px;\">"}
   "WARNING" {:html "<div style=\"margin: 16px 0; padding: 16px; border-left: 4px solid #f2cc60; background-color: #fff8c5; border-radius: 6px;\">
                      <div style=\"display: flex; align-items: center; margin-bottom: 8px; color: #9a6700; font-weight: 600;\">
                        <svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" width=\"20\" height=\"20\" fill=\"currentColor\" style=\"color: #9a6700;\">
                          <path fill-rule=\"evenodd\" d=\"M9.401 3.003c1.155-2 4.043-2 5.197 0l7.355 12.748c1.154 2-.29 4.5-2.599 4.5H4.645c-2.309 0-3.752-2.5-2.598-4.5L9.4 3.003zM12 8.25a.75.75 0 01.75.75v3.75a.75.75 0 01-1.5 0V9a.75.75 0 01.75-.75zm0 8.25a.75.75 0 100-1.5.75.75 0 000 1.5z\"></path>
                        </svg>
                        <span style=\"margin-left: 8px; color: #9a6700;\">WARNING</span>
                      </div>
                      <div style=\"margin-top: 8px;\">"}
   "CAUTION" {:html "<div style=\"margin: 16px 0; padding: 16px; border-left: 4px solid #ffb77c; background-color: #fff1e5; border-radius: 6px;\">
                      <div style=\"display: flex; align-items: center; margin-bottom: 8px; color: #bc4c00; font-weight: 600;\">
                        <svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" width=\"20\" height=\"20\" fill=\"currentColor\" style=\"color: #bc4c00;\">
                          <path fill-rule=\"evenodd\" d=\"M12 2.25c-5.385 0-9.75 4.365-9.75 9.75s4.365 9.75 9.75 9.75 9.75-4.365 9.75-9.75S17.385 2.25 12 2.25zm-1.72 6.97a.75.75 0 10-1.06 1.06L10.94 12l-1.72 1.72a.75.75 0 101.06 1.06L12 13.06l1.72 1.72a.75.75 0 101.06-1.06L13.06 12l1.72-1.72a.75.75 0 10-1.06-1.06L12 10.94l-1.72-1.72z\"></path>
                        </svg>
                        <span style=\"margin-left: 8px; color: #bc4c00;\">CAUTION</span>
                      </div>
                      <div style=\"margin-top: 8px;\">"}})

(defn extract-content
  "Extract content from admonition, removing blockquote prefixes if present"
  [content]
  (println "Extracting GitHub hint content, length:" (count content))
  (->> (str/split-lines content)
       (map #(cond
               (str/starts-with? % "> ") (subs % 2)
               (= % ">") ""
               :else %))
       (str/join "\n")))

;; A separate function to detect GitHub hint blocks from a blockquote
(defn parse-github-blockquote-hint
  "Try to detect GitHub-style hint blocks from a blockquote node"
  [blockquote-node]
  (println "Checking blockquote for GitHub hint pattern")
  (when-let [content (:content blockquote-node)]
    (when (seq content)
      (let [first-paragraph (first content)]
        (when (and (map? first-paragraph) 
                   (= (:type first-paragraph) :paragraph)
                   (seq (:content first-paragraph)))
          (let [first-line (first (:content first-paragraph))]
            (when (and (map? first-line)
                      (= (:type first-line) :text))
              (let [text (:text first-line)
                    hint-match (re-find #"^\s*\[\!([A-Z]+)\]\s*$" text)]
                (when hint-match
                  (let [hint-type (second hint-match)
                        rest-content (rest content)]
                    (when (contains? hint-type-styles hint-type)
                      (println "Found GitHub hint in blockquote:" hint-type)
                      {:type :github-hint
                       :hint-type hint-type
                       :content rest-content})))))))))))

(def github-hint-pattern
  "Pattern to match GitHub-style admonition blocks - simplest possible form"
  #"(?s)>\s*\[\!([A-Z]+)\]((?:\n>.*)*)")

(defn parse-github-hint
  "Parse a GitHub-style hint block into a structured map."
  [text]
  (println "Attempting to parse GitHub hint, text length:" (count text))
  (try
    (when-let [[_ hint-type content] (re-matches github-hint-pattern text)]
      (println "Found GitHub hint type:" hint-type)
      (when (contains? hint-type-styles hint-type)
        (println "Valid GitHub hint type:" hint-type)
        {:type :github-hint
         :hint-type hint-type
         :content (md/parse (extract-content content))}))
    (catch Exception e
      (println "Error parsing GitHub hint:" (.getMessage e))
      (.printStackTrace e)
      nil)))

(defn transform-github-hint
  "Transform a parsed GitHub hint block into HTML directly."
  [ctx {:keys [hint-type content] :as block}]
  (println "Rendering GitHub hint of type:" hint-type)
  (let [html-start (get-in hint-type-styles [hint-type :html])
        content-html (with-out-str (transform/->hiccup content))
        html-end "</div></div>"]
    (hiccup2/raw (str html-start content-html html-end))))

;; Special handler for GitHub blockquote style hints
(defn transform-blockquote
  "Transform a blockquote, checking if it might be a GitHub hint first"
  [ctx node]
  (if-let [github-hint (parse-github-blockquote-hint node)]
    (transform-github-hint ctx github-hint)
    (transform/default-hiccup-renderers :blockquote ctx node)))

;; Register GitHub hint block renderer with additional blockquote handler
(def renderers
  {:github-hint transform-github-hint
   :blockquote transform-blockquote}) 