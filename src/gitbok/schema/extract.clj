(ns gitbok.schema.extract
  "Extract structured data from markdown for Schema.org"
  (:require [clojure.string :as str]
            [gitbok.indexing.core :as indexing]
            [gitbok.state :as state]))

(defn extract-h2-headings
  "Extracts H2 headings from markdown for HowTo steps"
  [content]
  (when content
    (->> (re-seq #"(?m)^##\s+(.+)$" content)
         (map second)
         (map str/trim)
         (remove str/blank?)
         vec)))

(defn- extract-step-content
  "Extracts step name and text from section"
  [section-content]
  (let [lines (str/split-lines section-content)
        name (str/trim (first lines))
        text-lines (rest lines)]
    (when (not (str/blank? name))
      {:name name
       :text (-> (str/join " " text-lines)
                 (str/replace #"\s+" " ")
                 str/trim)})))

(defn extract-howto-steps
  "Extracts HowTo steps with name and text from markdown.
   Any heading (##, ###, ####) = step name, text until next heading = step description"
  [content]
  (when content
    (let [;; Split by any heading level (##, ###, ####, etc.)
          sections (str/split content #"(?m)^#{2,}\s+")
          step-sections (rest sections)]
      (->> step-sections
           (map extract-step-content)
           (remove nil?)
           (remove #(str/blank? (:name %)))
           vec))))

(defn- resolve-link
  "Resolves markdown link to absolute URL using indexing"
  [context filepath href]
  (let [base-url (state/get-config context :base-url)
        resolved (indexing/filepath->href context filepath href)]
    (if (and resolved (not (str/starts-with? resolved "http")))
      (str base-url resolved)
      resolved)))

(defn- clean-markdown-for-schema
  "Cleans markdown formatting for schema.org text fields.
   - Removes code blocks
   - Converts relative links to absolute using indexing
   - Removes bold/italic markers
   - Converts lists to plain text"
  [text context filepath]
  (-> text
      ;; Remove code blocks entirely
      (str/replace #"```[\s\S]*?```" "")
      ;; Convert markdown links [text](url) to absolute URLs
      (str/replace #"\[([^\]]+)\]\(([^\)]+)\)"
                   (fn [[_ link-text url]]
                     (if (str/starts-with? url "http")
                       (str link-text " (" url ")")
                       (let [resolved (resolve-link context filepath url)]
                         (str link-text " (" resolved ")")))))
      ;; Remove bold markers
      (str/replace #"\*\*([^*]+)\*\*" "$1")
      ;; Remove italic markers
      (str/replace #"\*([^*]+)\*" "$1")
      ;; Convert list items to bullet points
      (str/replace #"(?m)^-\s+" "• ")
      ;; Collapse multiple spaces/newlines
      (str/replace #"\s+" " ")
      str/trim))

(defn- extract-qa-from-section
  "Extracts question and answer from H3 section content"
  [section-content context filepath]
  (let [lines (str/split-lines section-content)
        question (str/trim (first lines))
        answer-lines (rest lines)]
    (when (not (str/blank? question))
      {:question question
       :answer (-> (str/join " " answer-lines)
                   (clean-markdown-for-schema context filepath))})))

(defn extract-faq-pairs
  "Extracts question-answer pairs from first H2 section of FAQ.
   Only H3 headings from first section are included (max 15).
   Uses indexing to resolve relative links to absolute URLs."
  [content context filepath]
  (when content
    (let [;; Find first H2 section (between first ## and second ##)
          first-section-match (re-find #"(?s)##\s+[^\n]+\n(.*?)(?=\n##\s|\z)" content)
          section-content (second first-section-match)]
      (when section-content
        (->> (str/split section-content #"(?m)^###\s+")
             rest
             (take 15)
             (map #(extract-qa-from-section % context filepath))
             (remove nil?)
             (remove #(str/blank? (:answer %)))
             vec)))))
