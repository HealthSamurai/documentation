(ns gitbok.schema.core
  "Schema.org JSON-LD generation for documentation pages"
  (:require [cheshire.core :as json]
            [clojure.string :as str]))

(defn detect-schema-type
  "Detects schema type by file path.
   getting-started/* -> :howto
   overview/faq.md -> :faq
   everything else -> :techarticle"
  [filepath]
  (cond
    ;; Match getting-started/ anywhere in path
    (re-find #"(^|/)getting-started/" filepath) :howto
    ;; Match overview/faq.md at end of path
    (re-find #"(^|/)overview/faq\.md$" filepath) :faq
    :else :techarticle))

(defn resolve-schema-type
  "Returns schema type: from frontmatter or auto-detect by path"
  [filepath frontmatter-schema]
  (if frontmatter-schema
    (keyword frontmatter-schema)
    (detect-schema-type (or filepath ""))))

(defn make-organization
  "Creates Organization schema with optional logo"
  [logo-url]
  (cond-> {"@type" "Organization"
           "name" "Health Samurai"
           "url" "https://www.health-samurai.io"}
    logo-url (assoc "logo" {"@type" "ImageObject"
                            "url" logo-url})))

(defn generate-howto-schema
  "Generates HowTo schema with steps from H2 headings"
  [{:keys [title description steps url lastmod image logo-url]}]
  (let [org (make-organization logo-url)]
    (cond-> {"@context" "https://schema.org"
             "@type" "HowTo"
             "name" title
             "author" org
             "publisher" org
             "inLanguage" "en"}
      description (assoc "description" description)
      url (assoc "url" url)
      lastmod (assoc "dateModified" lastmod)
      image (assoc "image" image)
      (seq steps) (assoc "step"
                         (map-indexed
                          (fn [idx {:keys [name text]}]
                            (cond-> {"@type" "HowToStep"
                                     "position" (inc idx)
                                     "name" name}
                              (not (str/blank? text)) (assoc "text" text)))
                          steps)))))

(defn generate-faq-schema
  "Generates FAQPage schema from Q&A pairs"
  [{:keys [title description qa-pairs url lastmod logo-url]}]
  (let [org (make-organization logo-url)]
    (cond-> {"@context" "https://schema.org"
             "@type" "FAQPage"
             "name" title
             "publisher" org
             "inLanguage" "en"}
      description (assoc "description" description)
      url (assoc "url" url)
      lastmod (assoc "dateModified" lastmod)
      (seq qa-pairs) (assoc "mainEntity"
                            (map (fn [{:keys [question answer]}]
                                   {"@type" "Question"
                                    "name" question
                                    "acceptedAnswer" {"@type" "Answer"
                                                      "text" answer}})
                                 qa-pairs)))))

(defn generate-techarticle-schema
  "Generates TechArticle schema (default behavior)"
  [{:keys [title description url lastmod image logo-url]}]
  (let [org (make-organization logo-url)]
    (cond-> {"@context" "https://schema.org"
             "@type" "TechArticle"
             "headline" title
             "author" org
             "publisher" org
             "inLanguage" "en"}
      description (assoc "description" description)
      url (assoc "url" url
                 "mainEntityOfPage" {"@type" "WebPage" "@id" url})
      lastmod (assoc "dateModified" lastmod)
      image (assoc "image" image))))

(defn generate-json-ld
  "Generates JSON-LD string for the specified schema type"
  [schema-type data]
  (json/generate-string
   (case schema-type
     :howto (generate-howto-schema data)
     :faq (generate-faq-schema data)
     (generate-techarticle-schema data))))
