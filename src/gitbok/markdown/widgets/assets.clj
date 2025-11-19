(ns gitbok.markdown.widgets.assets
  "Widget for automatically resolving paths to .gitbook/assets/ directory.
   
   Usage:
   {% asset-path \"image.png\" %}
   
   Returns just the path, e.g.:
   {% asset-path \"docker.png\" %} -> ../../.gitbook/assets/docker.png
   
   Then you can use it in img tag or markdown:
   <img src=\"{% asset-path \"docker.png\" %}\" alt=\"docker\" width=\"50%\">
   ![alt]({% asset-path \"image.png\" %})
   
   Automatically calculates the relative path from current file to .gitbook/assets/"
  (:require
   [clojure.string :as str]
   [gitbok.utils :as utils]))

(defn- calculate-assets-path
  "Calculate relative path from current file to .gitbook/assets/
   
   Example:
   - File: getting-started/run-with-oneliner.md -> ../../.gitbook/assets/
   - File: api.md -> ./.gitbook/assets/
   - File: configuration/envs.md -> ../.gitbook/assets/"
  [filepath]
  (if (str/blank? filepath)
    ".gitbook/assets/"
    (let [;; Normalize filepath - extract relative path from docs/ directory
          ;; Handle different formats:
          ;; - docs-new/auditbox/docs/getting-started/file.md -> getting-started/file.md
          ;; - getting-started/file.md -> getting-started/file.md
          ;; - docs/getting-started/file.md -> getting-started/file.md
          normalized-path (cond
                           ;; Full path with docs/ somewhere - take everything after last docs/
                           (str/includes? filepath "/docs/")
                           (last (str/split filepath #"/docs/" 2))
                           
                           ;; Starts with docs/ - remove it
                           (str/starts-with? filepath "docs/")
                           (subs filepath 5)
                           
                           ;; Starts with ./docs/ - remove it
                           (str/starts-with? filepath "./docs/")
                           (subs filepath 7)
                           
                           ;; Already relative - use as is
                           :else filepath)
          ;; Remove filename, keep only directory path
          dir-path (if (str/includes? normalized-path "/")
                    (subs normalized-path 0 (str/last-index-of normalized-path "/"))
                    "")
          ;; Count directory depth (number of path segments)
          depth (if (str/blank? dir-path)
                 0
                 (count (filter #(not (str/blank? %))
                               (str/split dir-path #"/"))))
          ;; Build relative path with ../ for each level
          relative-prefix (if (> depth 0)
                           (str/join (repeat depth "../"))
                           "./")]
      (str relative-prefix ".gitbook/assets/"))))

(defn- replace-assets-tag
  "Replace {% asset-path \"filename.png\" %} with just the path"
  [content filepath]
  (let [;; Pattern: {% asset-path "filename.png" %} or {% asset-path 'filename.png' %}
        ;; In Clojure regex literals #"..." we need to escape quotes because the string itself is in quotes
        pattern #"\{\%\s*asset-path\s+[\"']([^\"']+)[\"']\s*%\}"
        assets-path (calculate-assets-path filepath)]
    (str/replace content
                pattern
                (fn [[_ filename]]
                  (str assets-path filename)))))

(defn hack-assets
  "Process {% asset-path %} tags and replace them with just the path to .gitbook/assets/
   
   Usage:
   {% asset-path \"image.png\" %} -> ../../.gitbook/assets/image.png
   
   Then use in img tag:
   <img src=\"{% asset-path \"docker.png\" %}\" alt=\"docker\" width=\"50%\">
   
   Or in markdown:
   ![alt]({% asset-path \"image.png\" %})"
  [filepath content]
  (if (str/blank? filepath)
    content
    (replace-assets-tag content filepath)))

