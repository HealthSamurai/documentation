(ns gitbok.schema.extract
  "Extract structured data from markdown for Schema.org"
  (:require [clojure.string :as str]))

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

(defn- extract-text-until-next-h2
  "Extracts text from section until next ## heading"
  [section-content]
  (let [lines (str/split-lines section-content)
        question (str/trim (first lines))
        answer-lines (rest lines)]
    (when (not (str/blank? question))
      {:question question
       :answer (-> (str/join " " answer-lines)
                   (str/replace #"\s+" " ")
                   str/trim)})))

(defn extract-faq-pairs
  "Extracts question-answer pairs from markdown.
   H2 = question, text until next H2 = answer"
  [content]
  (when content
    (let [;; Split by ## but keep the heading text
          sections (str/split content #"(?m)^##\s+")
          ;; Skip content before first H2
          qa-sections (rest sections)]
      (->> qa-sections
           (map extract-text-until-next-h2)
           (remove nil?)
           (remove #(str/blank? (:answer %)))
           vec))))
