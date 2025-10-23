(ns gitbok.ui.main-content
  (:require
   [clojure.java.io :as io]
   [gitbok.indexing.core :as indexing]
   [gitbok.state :as state]
   [clojure.string :as str]
   [gitbok.markdown.core :as markdown]
   [gitbok.indexing.impl.summary :as summary]
   [gitbok.markdown.widgets.big-links :as big-links]
   [gitbok.markdown.widgets.headers :as headers]
   [gitbok.ui.right-toc :as right-toc]
   [gitbok.ui.breadcrumb :as breadcrumb]
   [gitbok.utils :as utils]
   [gitbok.products :as products]
   [gitbok.http :as http]
   [hiccup2.core]))

(defn empty-content-after-h1?
  "Checks if the markdown content only has an H1 header with no meaningful content after it"
  [raw-content]
  (when raw-content
    (let [lines (str/split-lines raw-content)
          ;; Find first line starting with "# " (one hashtag and at least one space)
          h1-idx (some #(when (re-matches #"#\s+.*" (nth lines %))
                          %)
                       (range (count lines)))]
      (if h1-idx
        ;; Get all lines after H1, remove all whitespace and check if empty
        (let [after-h1 (subvec (vec lines) (inc h1-idx))
              ;; Join all lines and remove ALL whitespace
              all-content (str/join "" after-h1)
              clean-content (str/replace all-content #"\s" "")]
          (empty? clean-content))
        ;; No H1 found, not an empty page
        false))))

(defn find-children-files [context filepath]
  (when
   (and filepath
        (str/ends-with? (str/lower-case filepath) "readme.md"))
    (let [index (state/get-file-to-uri-idx context)
          ;; Get DOCS_PREFIX and product path dynamically
          docs-prefix (gitbok.state/get-config context)
          product-path (or (gitbok.products/path context) "")
          ;; Remove trailing slash if present
          filepath (if (str/ends-with? filepath "/")
                     (subs filepath 0 (dec (count filepath)))
                     filepath)
          ;; Construct the full prefix pattern: <product-path-without-slash>/<docs-prefix-without-slash>/
          ;; For example: "aidbox/docs/" when product-path="/aidbox" and docs-prefix="/docs"
          product-path-clean (str/replace product-path #"^/" "")
          docs-prefix-clean (str/replace docs-prefix #"^/" "")
          ;; Build possible prefix patterns
          full-prefix (if (and (not (str/blank? product-path-clean))
                               (not (str/blank? docs-prefix-clean)))
                        (str product-path-clean "/" docs-prefix-clean "/")
                        nil)
          dot-prefix (if (not (str/blank? docs-prefix-clean))
                       (str "./" docs-prefix-clean "/")
                       nil)
          plain-prefix (if (not (str/blank? docs-prefix-clean))
                         (str docs-prefix-clean "/")
                         nil)
          ;; Process filepath to match index format
          ;; First remove ../docs/ prefix if present
          filepath-clean (if (str/starts-with? filepath "../docs/")
                           (subs filepath 8)
                           filepath)
          filepath-normalized
          (cond
            ;; Remove <product>/<docs-prefix>/ prefix if present
            (and full-prefix (str/starts-with? filepath-clean full-prefix))
            (subs filepath-clean (count full-prefix))
            ;; Remove ./<docs-prefix>/ prefix if present
            (and dot-prefix (str/starts-with? filepath-clean dot-prefix))
            (subs filepath-clean (count dot-prefix))
            ;; Remove <docs-prefix>/ prefix if present
            (and plain-prefix (str/starts-with? filepath-clean plain-prefix))
            (subs filepath-clean (count plain-prefix))
            ;; Otherwise use as is
            :else filepath-clean)
          dir (.getParent (io/file filepath-normalized))
          result (filterv
                  (fn [[file _info]]
                    (let [file-obj (io/file file)
                          file-parent (.getParent file-obj)
                          file-parent-obj (when file-parent (io/file file-parent))
                          file-grandparent (when file-parent-obj (.getParent file-parent-obj))
                          starts-with-dir (and dir (str/starts-with? file dir))
                          not-same-file (not= file filepath-normalized)
                          same-parent (= dir file-parent)
                          readme-in-subdir (and
                                            dir
                                            file-grandparent
                                            (= dir file-grandparent)
                                            (str/ends-with? (str/lower-case file) "readme.md"))
                          matches (and starts-with-dir
                                       not-same-file
                                       (or same-parent readme-in-subdir))]
                      matches))
                  index)]
      result)))

(defn find-children-from-summary
  "Get children sorted by SUMMARY.md order"
  [context filepath]
  (let [children-from-files (find-children-files context filepath)]
    (when (seq children-from-files)
      (let [nav-links (summary/get-navigation-links context)
            product-prefix (http/get-product-prefix context)
            ;; Create map: full-href (normalized) -> index for quick lookup
            ;; Normalize by removing trailing slash
            href-to-index (into {} 
                                (map-indexed 
                                  (fn [idx item] 
                                    (let [href (str/replace (:href item) #"/$" "")]
                                      [href idx])) 
                                  nav-links))]
        ;; Sort children by their index in summary navigation
        (sort-by 
          (fn [[_path info]]
            (let [full-href (str product-prefix "/" (:uri info))
                  ;; Normalize by removing trailing slash
                  normalized-href (str/replace full-href #"/$" "")]
              (or (get href-to-index normalized-href) 999999)))
          children-from-files)))))

(defn render-empty-page [context filepath title]
  [:div
   (headers/render-h1
    (markdown/renderers context filepath) title)
   (for [[_path {:keys [title uri]}]
         (find-children-from-summary context filepath)]
     (let [prefix (gitbok.state/get-config context :prefix "")
           product-path (or (products/path context) "")
           full-href (str prefix product-path "/" uri)]
       (big-links/big-link-view full-href title)))])

(def nav-button-classes
  "group text-sm
  flex gap-4 flex-1 items-center
  p-2.5
  border border-outline-subtle rounded
  hover:border-brand
  text-pretty
  md:text-base
  md:h-[80px]")

(defn navigation-buttons [context uri]
  (let [[[prev-page-url prev-page-title] [next-page-url next-page-title]]
        (summary/get-prev-next-pages context uri)]
    [:div {:class "flex flex-col sm:flex-row justify-between items-start mt-8 pt-4 gap-4"}
     (when prev-page-url
       [:div {:class "flex-1 w-full sm:w-auto"}
        [:a {:href prev-page-url
             :hx-target "#content"
             :hx-push-url prev-page-url
             :hx-get (str (http/get-partial-product-prefixed-url context
                           (subs prev-page-url (count (http/get-product-prefix context)))))
             :hx-swap "outerHTML"
             :class (str nav-button-classes " flex-row-reverse")}
         [:span {:class "flex flex-col flex-1 text-right justify-center"}
          [:span {:class "text-xs text-on-surface-placeholder"} "Previous"]
          [:span {:class "text-on-surface-muted group-hover:text-brand line-clamp-2"} prev-page-title]]
         [:svg {:class "size-4 text-on-surface-muted group-hover:text-brand flex-shrink-0"
                :fill "none"
                :stroke "currentColor"
                :viewBox "0 0 24 24"
                :stroke-width "2"}
          [:path {:stroke-linecap "round"
                  :stroke-linejoin "round"
                  :d "M15 19l-7-7 7-7"}]]]])
     (when next-page-url
       [:div {:class "flex-1 w-full sm:w-auto"}
        [:a {:href next-page-url
             :hx-target "#content"
             :hx-push-url next-page-url
             :hx-get (str (http/get-partial-product-prefixed-url context
                           (subs next-page-url (count (http/get-product-prefix context)))))
             :hx-swap "outerHTML"
             :class nav-button-classes}
         [:span {:class "flex flex-col flex-1 justify-center"}
          [:span {:class "text-xs text-on-surface-placeholder"} "Next"]
          [:span {:class "text-on-surface-muted group-hover:text-brand line-clamp-2"} next-page-title]]
         [:svg {:class "size-4 text-on-surface-muted group-hover:text-brand flex-shrink-0"
                :fill "none"
                :stroke "currentColor"
                :viewBox "0 0 24 24"
                :stroke-width "2"}
          [:path {:stroke-linecap "round"
                  :stroke-linejoin "round"
                  :d "M9 5l7 7-7 7"}]]]])]))

(defn render-file* [context filepath parsed title raw-content]
  (let [is-empty-page (empty-content-after-h1? raw-content)]
    {:content [:div {:class "real-content flex-1 min-w-0 max-w-full"}
               (if is-empty-page
                 (render-empty-page context filepath title)
                 (markdown/render-md context filepath parsed))]
     :parsed parsed}))

(defn content-div [context uri content filepath & [htmx? hide-breadcrumb]]
  (let [parsed (when (map? content) (:parsed content))
        body (if (map? content) (:content content) content)
        ;; Extract relative URI for breadcrumb
        uri-relative (utils/uri-to-relative
                      uri
                      (state/get-config context :prefix)
                      (:path (gitbok.products/get-current-product context)))
        ;; Generate breadcrumb (skip if hide-breadcrumb is true)
        breadcrumb-elem (when-not hide-breadcrumb
                          (breadcrumb/breadcrumb context uri-relative filepath))
        ;; Handle breadcrumb insertion based on body type
        body-with-breadcrumb (cond
                               ;; No breadcrumb to add
                               (nil? breadcrumb-elem) body

                               ;; Body is a string (HTML)
                               (string? body)
                               (if (str/includes? body "id=\"page-header\"")
                                 (str/replace body
                                              #"<header[^>]*id=\"page-header\"[^>]*>"
                                              (str "$0" (hiccup2.core/html breadcrumb-elem)))
                                 (str (hiccup2.core/html breadcrumb-elem) body))

                               ;; Body is Hiccup (vector)
                               (vector? body)
                               [:div
                                breadcrumb-elem
                                body]

                               ;; Fallback
                               :else body)
        toc (when filepath
              (if parsed
                (let [toc-result (right-toc/render-right-toc parsed)]
                  toc-result)
                (try
                  (let [content* (state/slurp-resource context filepath)
                        {:keys [parsed]} (markdown/parse-markdown-content context [filepath content*])
                        toc-result (right-toc/render-right-toc parsed)]
                    toc-result)
                  (catch Exception _
                    nil))))]
    [:main#content {:class "flex-1 items-start"}
     [:div {:class "flex items-start"}
      [:article {:class "article__content py-8 min-w-0 flex-1
                 max-w-5xl transform-3d"}
       (when htmx?
         [:script (hiccup2.core/raw "
           window.scrollTo(0, 0);
           // Defer execution to ensure scripts are loaded
           setTimeout(function() {
             if (typeof updateActiveNavItem === 'function') {
               updateActiveNavItem(window.location.pathname);
             }
             if (typeof updatePageTitle === 'function') {
               updatePageTitle();
             }
           }, 10);
         ")])
       [:div {:class "mx-auto max-w-full"} body-with-breadcrumb]
       (navigation-buttons context uri)
       (let [lastupdated (indexing/get-lastmod context filepath)]
         (when lastupdated
           [:p {:class "mt-4 text-sm text-on-surface-muted"
                :id "lastupdated"
                :data-updated-at lastupdated}
            ;; Text will be updated by JavaScript
            ""]))]
      toc]]))
