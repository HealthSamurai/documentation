(ns gitbok.markdown.widgets.description
  (:require [clj-yaml.core :as yaml]
            [hiccup2.core :as hiccup]
            [gitbok.ui.breadcrumb :as breadcrumb]
            [clojure.string :as str]))

(defn parse-title [content]
  (let [lines (str/split-lines content)
        h1-idx (some #(when (and (>= % 0)
                                 (str/starts-with? (str/trim (nth lines %)) "# "))
                        %)
                     (range (count lines)))
        h1-line (when h1-idx (nth lines h1-idx))
        title (when h1-line (str/trim (subs h1-line 2)))
        h2-idx (when-not title
                 (some #(when (and (>= % 0)
                                   (str/starts-with? (str/trim (nth lines %)) "## "))
                          %)
                       (range (count lines))))
        h2-line (when h2-idx (nth lines h2-idx))
        h2-title (when h2-line (str/trim (subs h2-line 3)))]
    (or title h2-title)))

(defn parse-description [content]
  (let [lines (str/split-lines content)
        first-line (str/trim (first lines))
        start-idx (when (= "---" first-line) 0)
        end-idx (when start-idx
                  (some #(when (and (> % start-idx)
                                    (= "---" (str/trim (nth lines %))))
                           %)
                        (range (inc start-idx) (count lines))))
        yaml-lines (when (and start-idx end-idx)
                     (subvec (vec lines)
                             (inc start-idx) end-idx))
        frontmatter (when yaml-lines (yaml/parse-string (str/join "\n" yaml-lines)))
        description (:description frontmatter)]
    description))

(defn hack-h1-and-description [context
                               filepath
                               parse-markdown-content
                               render-md
                               content]
  (if (:parsing-in-hack-phase context)
    content
    (let [description (parse-description content)
          new-desc (when description [:p {:class "text-lg text-tint-11"} description])
          title (parse-title content)
          lines (str/split-lines content)
          start-idx (.indexOf ^java.util.List lines "---")
          end-idx (when (>= start-idx 0)
                    (+ start-idx 1 (.indexOf ^java.util.List (subvec (vec lines) (inc start-idx)) "---")))
          after-frontmatter (if (and end-idx (> end-idx start-idx))
                              (subvec (vec lines) (inc end-idx))
                              lines)
          h1-idx (some #(when
                         (and (>= % 0)
                              (str/starts-with? (nth after-frontmatter %) "# "))
                          %)
                       (range (count after-frontmatter)))]
      (if (and title h1-idx)
        (let [h1-line (nth after-frontmatter h1-idx)
              parsed-h1 (parse-markdown-content h1-line)
              rendered-h1 (render-md context filepath parsed-h1)
              after-h1 (subvec after-frontmatter (inc h1-idx))
              header-content (if new-desc [rendered-h1 new-desc] [rendered-h1])
              ;; Add a placeholder for breadcrumb
              header-html (str
                           (hiccup/html
                            (into [:header {:class "mb-6" :id "page-header"}] header-content)))]
          (str
           header-html
           "\n"
           (str/join "\n" after-h1)))
        content))))
