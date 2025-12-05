(ns gitbok.indexing.previews
  "Link preview functionality - returns title, description, and breadcrumbs for hrefs"
  (:require
   [gitbok.state :as state]
   [gitbok.ui.breadcrumb :as breadcrumb]
   [gitbok.markdown.widgets.description :as desc]
   [gitbok.http :as http]
   [clojure.string :as str]
   [clojure.tools.logging :as log]))

(defn get-breadcrumb-titles
  "Returns breadcrumb titles for a given href (excluding the page itself).
   Always includes section-title from current page if available."
  [context href]
  (let [summary (state/get-summary context)
        path (breadcrumb/find-breadcrumb-path summary href)]
    (when (seq path)
      (let [current-page (last path)
            section-title (:section-title current-page)
            parent-titles (->> path
                               drop-last ; Remove current page
                               (keep (fn [item]
                                       (get-in item [:parsed :title])))
                               (remove str/blank?))]
        (->> (concat (when (and section-title (not (str/blank? section-title)))
                       [section-title])
                     parent-titles)
             vec)))))

(defn- build-preview-for-file
  "Build preview data for a single file. Returns [href preview] or nil."
  [context filepath content]
  (try
    (let [file-to-uri-idx (state/get-file-to-uri-idx context)
          uri (:uri (get file-to-uri-idx filepath))
          product-prefix (http/get-product-prefix context)
          ;; Build href with product prefix (e.g., /docs/aidbox/getting-started)
          href (if (and product-prefix (not (str/blank? product-prefix)))
                 (str product-prefix "/" uri)
                 (str "/" uri))]
      (when uri
        (let [title (desc/parse-title content filepath)
              description (desc/parse-description content)
              breadcrumbs (get-breadcrumb-titles context href)]
          [href {:t title
                 :d description
                 :b (or breadcrumbs [])}])))
    (catch Exception e
      (log/debug "Failed to build preview" {:filepath filepath :error (.getMessage e)})
      nil)))

(defn build-previews-idx!
  "Builds previews index for all markdown files. Called during initialization.
   Returns map of href -> {:t title :d description :b breadcrumbs}
   Keys are full hrefs like /docs/aidbox/getting-started"
  [context]
  (let [md-files-idx (state/get-md-files-idx context)]
    (if (empty? md-files-idx)
      (do
        (log/info "Skipping previews index (dev mode or no files cached)")
        {})
      (let [previews (->> md-files-idx
                          (keep (fn [[filepath content]]
                                  (build-preview-for-file context filepath content)))
                          (into {}))]
        (log/info "Built previews index" {:count (count previews)})
        previews))))

(defn get-previews-for-hrefs
  "Returns cached preview data for requested hrefs.
   Only returns hrefs that exist in the previews index.
   Handles both /path and /path/ formats."
  [context hrefs]
  (let [previews-idx (state/get-previews-idx context)]
    (when (and (seq hrefs) previews-idx)
      (->> hrefs
           (keep (fn [href]
                   (let [;; Try original, without trailing slash, and with trailing slash
                         without-slash (if (and href (str/ends-with? href "/") (> (count href) 1))
                                         (subs href 0 (dec (count href)))
                                         href)
                         with-slash (if (and href (not (str/ends-with? href "/")))
                                      (str href "/")
                                      href)
                         preview (or (get previews-idx href)
                                     (get previews-idx without-slash)
                                     (get previews-idx with-slash))]
                     (when preview
                       [href preview]))))
           (into {})))))
