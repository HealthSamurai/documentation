(ns gitbok.blog.frontmatter
  (:require [clj-yaml.core :as yaml]
            [clojure.string :as str]))

(defn- split-frontmatter
  "Split markdown content into frontmatter and body.
   Returns {:frontmatter '...' :body '...'} or nil if no frontmatter found."
  [content]
  (when (str/starts-with? content "---")
    (let [rest-content (subs content 3)
          end-index (str/index-of rest-content "---")]
      (when end-index
        {:frontmatter (subs rest-content 0 end-index)
         :body (str/trim (subs rest-content (+ end-index 3)))}))))

(defn parse-frontmatter
  "Parse YAML frontmatter from markdown content.
   Returns map with :metadata (parsed YAML) and :content (markdown body)."
  [content]
  (if-let [{:keys [frontmatter body]} (split-frontmatter content)]
    {:metadata (yaml/parse-string frontmatter)
     :content body}
    {:metadata {}
     :content content}))

(defn extract-metadata
  "Extract just the metadata from markdown content."
  [content]
  (:metadata (parse-frontmatter content)))

(defn extract-content
  "Extract just the markdown body from content, stripping frontmatter."
  [content]
  (:content (parse-frontmatter content)))
