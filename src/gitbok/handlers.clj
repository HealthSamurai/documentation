(ns gitbok.handlers
  "HTTP request handlers for the application"
  (:require
   [cheshire.core :as json]
   [clojure.java.io :as io]
   [clojure.stacktrace]
   [clojure.string :as str]
   [clojure.tools.logging :as log]
   [gitbok.analytics.posthog :as posthog]
   [gitbok.http :as http]
   [gitbok.indexing.core :as indexing]
   [gitbok.indexing.previews :as previews]
   [gitbok.state :as state]
   [gitbok.indexing.impl.sitemap-index :as sitemap-index]
   [gitbok.llms-txt :as llms-txt]
   [gitbok.markdown.core :as markdown]
   [gitbok.products :as products]
   [gitbok.ui.examples]
   [gitbok.ui.layout :as layout]
   [gitbok.ui.main-content :as main-content]
   [gitbok.ui.meilisearch]
   [gitbok.ui.not-found :as not-found]
   [gitbok.utils :as utils]
   [hiccup2.core]
   [ring.middleware.content-type :refer [content-type-response]]
   [ring.util.response :as resp]))

(defn read-markdown-file [context filepath]
  (let [[filepath section] (str/split filepath #"#")
        full-filepath (products/filepath context filepath)
        dev-mode (state/get-config context :dev-mode)
        ;; In DEV mode, always read fresh content from disk/volume
        _ (when dev-mode (log/debug "reading-file" {:filepath filepath :full-path full-filepath}))
        content* (if dev-mode
                   ;; Force re-read from disk in DEV mode
                   (do
                     (log/info "DEV: reading-fresh" {:path full-filepath :filepath filepath})
                     (let [from-disk (try (state/slurp-resource context full-filepath)
                                          (catch Exception e
                                            (log/warn "Failed to read from disk" {:path full-filepath :error (.getMessage e)})
                                            nil))]
                       (log/info "DEV: content loaded" {:path full-filepath
                                                        :from-disk? (some? from-disk)
                                                        :size (if from-disk (count from-disk) 0)
                                                        :nil? (nil? from-disk)})
                       from-disk))
                   ;; In production, this should never be called - rendered pages are pre-cached
                   ;; But as a fallback, try to read from disk
                   (do
                     (log/warn "PRODUCTION: reading file on-demand (should be pre-rendered)" {:filepath filepath})
                     (state/slurp-resource context full-filepath)))
        _ (when-not content*
            (log/error "No content found" {:filepath filepath :full-filepath full-filepath}))
        {:keys [parsed description title schema-type]}
        (if content*
          (markdown/parse-markdown-content
           context
           [full-filepath content*])
          {:parsed [:div "No content"] :description "" :title "Error" :schema-type nil})]
    (try
      {:content
       (main-content/render-file* context full-filepath parsed title (or content* ""))
       :title title
       :description description
       :schema-type schema-type
       :raw-content content*
       :section section}
      (catch Exception e
        (log/info "cannot-render-file" {:filepath full-filepath})
        {:content [:div {:role "alert"}
                   (.getMessage e)
                   [:pre (pr-str e)]
                   [:pre content*]]}))))

(defn picture-url? [url]
  (when url
    (or (str/starts-with? url "/.gitbook/assets")
        (str/includes? url "/.gitbook/assets"))))

(defn canonical-file-response
  "Like ring.util.response/file-response but resolves symlinks to canonical paths.
   This ensures git-sync symlinks are followed correctly."
  [path]
  (try
    (let [file (io/file path)]
      (when (.exists file)
        (let [canonical-path (.getCanonicalPath file)]
          (resp/file-response canonical-path))))
    (catch Exception e
      ;; Return nil on any exception, just like file-response does
      nil)))

(defn render-file
  [context filepath]
  (let [dev-mode (state/get-config context :dev-mode)
        _ (when dev-mode (log/info "render-file in DEV mode" {:filepath filepath}))
        result
        (try
          (if dev-mode
            (read-markdown-file context filepath)
            (markdown/get-rendered context filepath))
          (catch Exception e
            (log/error e "Error rendering file" {:filepath filepath})
            [:div {:role "alert"}
             (.getMessage e)
             [:pre (pr-str e)]]))]
    {:content
     (if (map? result)
       (:content result)
       [:div result])
     :section (:section result)
     :description (:description result)
     :title (:title result)
     :schema-type (:schema-type result)
     :raw-content (:raw-content result)}))

(defn check-cache-lastmod [request last-mod]
  (let [if-modified-since (get-in request [:headers "if-modified-since"])
        lastmod (utils/iso-to-http-date last-mod)]
    (when (and if-modified-since lastmod)
      (try
        ;; Parse both dates and compare as timestamps
        (let [if-modified-instant (utils/parse-http-date if-modified-since)
              lastmod-instant (utils/parse-http-date lastmod)]
          (and if-modified-instant
               lastmod-instant
               ;; Use >= to handle minor time differences (within 1 second)
               (>= (.toEpochMilli if-modified-instant)
                   (.toEpochMilli lastmod-instant))))
        (catch Exception e
          ;; Fallback to string comparison if parsing fails
          (log/debug "Failed to parse dates for comparison"
                     {:error (.getMessage e)
                      :if-modified-since if-modified-since
                      :lastmod lastmod})
          (= if-modified-since lastmod))))))

(defn check-cache-etag [request etag]
  (let [if-none-match (get-in request [:headers "if-none-match"])]
    ;; Direct comparison - both should have quotes
    (= if-none-match etag)))

(defn handle-cached-response
  "Common function to handle cached responses with ETag and Last-Modified"
  [ctx filepath content-type render-fn]
  (let [request (:request ctx)
        lastmod (indexing/get-lastmod ctx filepath)
        lastmod-iso (utils/iso-to-http-date lastmod)
        etag (utils/generate-versioned-etag ctx content-type lastmod-iso)
        if-none-match (get-in request [:headers "if-none-match"])
        if-modified-since (get-in request [:headers "if-modified-since"])
        ;; Per RFC 7232: If-None-Match takes precedence over If-Modified-Since
        cache-hit? (if if-none-match
                     (check-cache-etag request etag)
                     (and if-modified-since lastmod
                          (check-cache-lastmod request lastmod)))]

    (if cache-hit?
      {:status 304
       :headers {"Cache-Control" "public, max-age=300"
                 "Last-Modified" lastmod-iso
                 "ETag" etag
                 "Vary" "Accept"}}
      (let [response (render-fn)]
        (assoc response
               :headers (merge (:headers response {})
                               {"Cache-Control" "public, max-age=300"
                                "Last-Modified" (or lastmod-iso "")
                                "ETag" etag
                                "Vary" "Accept"}))))))

(defn render-pictures [ctx]
  (let [request (:request ctx)
        uri (:uri request)
        prefix (state/get-config ctx :prefix "")
        uri-relative
        (utils/uri-to-relative uri prefix
                               (products/path ctx))
        file-path (->
                   uri-relative
                   (str/replace #"%20" " ")
                   (str/replace-first #".*.gitbook/assets/" ""))
        ;; Extract product id from URI for product-specific assets
        ;; URI (after prefix removal) looks like /auditbox/.gitbook/assets/auditbox/image.png
        uri-without-prefix (if (and (not (str/blank? prefix))
                                    (str/starts-with? uri prefix))
                             (subs uri (count prefix))
                             uri)
        product-id (second (re-find #"^/?([^/]+)/\.gitbook/assets/" uri-without-prefix))
        dev-mode? (state/get-config ctx :dev-mode)
        volume-path (state/get-config ctx :docs-volume-path)
        repo-path (state/get-config ctx :docs-repo-path)
        ;; Try to find the image in multiple locations
        response (if volume-path
                   ;; When volume path is available: try filesystem first
                   (or
                    ;; Try product-specific assets first (docs-new/{product}/.gitbook/assets/)
                    (when product-id
                      (canonical-file-response (str volume-path "/docs-new/" product-id "/.gitbook/assets/" file-path)))
                    ;; Try repo path (for assets in repo root)
                    (when repo-path
                      (or
                       (canonical-file-response (str repo-path "/.gitbook/assets/" file-path))
                       (canonical-file-response (str repo-path "/docs/.gitbook/assets/" file-path))))
                    ;; Try at root .gitbook/assets/
                    (canonical-file-response (str volume-path "/.gitbook/assets/" file-path))
                    ;; Try docs/.gitbook/assets/
                    (canonical-file-response (str volume-path "/docs/.gitbook/assets/" file-path))
                    ;; Try at .gitbook/ (for compatibility)
                    (canonical-file-response (str volume-path "/.gitbook/" file-path))
                    ;; Try docs/.gitbook/ (for compatibility)
                    (canonical-file-response (str volume-path "/docs/.gitbook/" file-path))
                    ;; Fallback to classpath resources
                    (when product-id
                      (resp/resource-response (str product-id "/.gitbook/assets/" file-path)))
                    (resp/resource-response file-path)
                    (resp/resource-response (str ".gitbook/" file-path))
                    (resp/resource-response (str ".gitbook/assets/" file-path))
                    (resp/resource-response (str "assets/" file-path))
                    (resp/resource-response (str "docs/.gitbook/" file-path))
                    (resp/resource-response (str "docs/.gitbook/assets/" file-path)))
                   ;; No volume path: use classpath only
                   (or
                    ;; Try product-specific assets first
                    (when product-id
                      (resp/resource-response (str product-id "/.gitbook/assets/" file-path)))
                    (resp/resource-response (str "assets/" file-path))
                    (resp/resource-response file-path)
                    (resp/resource-response (str ".gitbook/" file-path))
                    (resp/resource-response (str ".gitbook/assets/" file-path))
                    (resp/resource-response (str "docs/.gitbook/" file-path))
                    (resp/resource-response (str "docs/.gitbook/assets/" file-path))))]
    (if response
      (let [;; In dev mode: no cache
            ;; In production: cache for 1 year (images are typically versioned)
            cache-control (if dev-mode?
                            "no-cache, no-store, must-revalidate"
                            "public, max-age=31536000, immutable")
            ;; Add proper Content-Type for SVG files
            response-with-type (if (str/ends-with? file-path ".svg")
                                 (resp/content-type response "image/svg+xml")
                                 response)
            ;; Add cache-control to the headers map
            headers (assoc (or (:headers response-with-type) {})
                           "Cache-Control" cache-control)]
        ;; Return response with updated headers
        (assoc response-with-type :headers headers))
      (do
        (log/warn "Image not found" {:uri uri :file-path file-path})
        nil))))

(defn render-favicon [ctx]
  (let [favicon-path (or (state/get-product-state ctx [:favicon-path])
                         "public/favicon.ico")]
    (or (resp/resource-response favicon-path)
        {:status 404
         :headers {"Content-Type" "text/plain"}
         :body "Favicon not found"})))

(defn process-redirect-target
  "Process redirect target to remove .md extension while preserving fragments"
  [target]
  (when target
    (let [[path fragment] (str/split target #"#" 2)]
      (str (str/replace path #"\.md$" "")
           (when fragment (str "#" fragment))))))

(defn get-redirect-with-fragment
  "Get redirect for a URI, checking both with and without fragment.
   Returns [redirect-target preserve-fragment?] where preserve-fragment?
   indicates if we should append the original fragment to the redirect target."
  [ctx uri-with-possible-fragment]
  (let [redirects-idx (state/get-redirects-idx ctx)
        [base-uri fragment] (str/split uri-with-possible-fragment #"#" 2)]
    (cond
      ;; First check if there's a specific redirect for the URI with fragment
      (and fragment (get redirects-idx uri-with-possible-fragment))
      [(get redirects-idx uri-with-possible-fragment) false]

      ;; Then check for a redirect for the base URI
      (get redirects-idx base-uri)
      [(get redirects-idx base-uri) (boolean fragment)]

      ;; No redirect found
      :else
      [nil false])))

(defn render-file-view
  [ctx]
  (let [request (:request ctx)
        uri (:uri request)
        product-path (products/path ctx)
        prefix (state/get-config ctx :prefix "")
        uri-relative
        (utils/uri-to-relative
         uri
         prefix
         product-path)]
    (cond

      (or (picture-url? uri-relative)
          (picture-url? uri))
      (render-pictures ctx)

      ;; Special case: redirect "readme" to root
      (= uri-relative "readme")
      {:status 301
       :headers {"Location" (http/get-product-prefixed-url ctx "")}}

      :else
      (let [filepath (indexing/uri->filepath ctx uri-relative)]
        ;; Check if file exists first
        (if filepath
          (handle-cached-response ctx filepath "full"
                                  (fn []
                                    (let [{:keys [title description content section schema-type raw-content]}
                                          (render-file ctx filepath)
                                          lastmod (indexing/get-lastmod ctx filepath)]
                                      (layout/layout
                                       ctx
                                       {:content content
                                        :lastmod lastmod
                                        :title title
                                        :description description
                                        :section section
                                        :filepath filepath
                                        :schema-type schema-type
                                        :raw-content raw-content}))))
          ;; No file - check for redirect
          (let [[redirect-target preserve-fragment?] (get-redirect-with-fragment ctx uri-relative)]
            (if redirect-target
              ;; Return HTTP 301 redirect
              (let [processed-target (process-redirect-target redirect-target)
                    ;; Extract fragment from original URI if we need to preserve it
                    [_ original-fragment] (when preserve-fragment?
                                            (str/split uri-relative #"#" 2))
                    ;; Build final target URL with fragment handling
                    final-target (if (and preserve-fragment? original-fragment)
                                   (str processed-target "#" original-fragment)
                                   processed-target)
                    target-url (http/get-product-prefixed-url ctx final-target)
                    request (:request ctx)
                    session-id (:session-id request)
                    current-product (products/get-current-product ctx)
                    product-id (or (:id current-product) "unknown")]
                ;; Track redirect event in PostHog
                (when session-id
                  (future
                    (posthog/capture-event! ctx
                                            session-id
                                            "docs_redirect"
                                            {"old_url" uri-relative
                                             "new_url" final-target
                                             "product" product-id
                                             "preserve_fragment" preserve-fragment?})))
                {:status 301
                 :headers {"Location" target-url}})
              ;; No file and no redirect - show 404
              (layout/layout
               ctx
               {:content (not-found/not-found-view ctx uri-relative)
                :status 404
                :title "Not found"
                :description "Page not found"
                :hide-breadcrumb true}))))))))

(defn render-partial-view
  "Render partial content for HTMX requests (without full layout)"
  [ctx]
  (let [request (:request ctx)
        uri (:uri request)
        ;; Remove /partial prefix to get the actual resource path
        uri-without-partial (str/replace-first uri #"/partial" "")
        product-path (products/path ctx)
        prefix (state/get-config ctx :prefix "")
        uri-relative
        (utils/uri-to-relative
         uri-without-partial
         prefix
         product-path)]
    ;; Partial requests are only for HTML content, not images
    ;; Handle root path - redirect to readme
    (if (or (str/blank? uri-relative)
            (= "/" uri-relative))
      (let [readme-path (or (products/readme-relative-path ctx) "readme/README.md")]
        (handle-cached-response ctx readme-path "partial"
                                (fn []
                                  (let [{:keys [_title _description content _section]}
                                        (render-file ctx readme-path)]
                                    {:status 200
                                     :headers {"Content-Type" "text/html; charset=utf-8"}
                                     :body (str (hiccup2.core/html
                                                 (main-content/content-div ctx uri-without-partial content readme-path true false)))}))))
        ;; Special case: redirect "readme" to root
      (if (= uri-relative "readme")
        {:status 301
         :headers {"Location" (str (http/get-product-prefixed-url ctx "/partial")
                                   (http/get-product-prefixed-url ctx ""))}}
        ;; Check for files first
        (let [filepath (indexing/uri->filepath ctx uri-relative)]
          (if filepath
            (handle-cached-response ctx filepath "partial"
                                    (fn []
                                      (let [{:keys [_title _description content _section]}
                                            (render-file ctx filepath)]
                                        {:status 200
                                         :headers {"Content-Type" "text/html; charset=utf-8"}
                                         :body (str (hiccup2.core/html
                                                     (main-content/content-div ctx uri-without-partial content filepath true false)))})))
            ;; No file - check for redirect
            (let [[redirect-target preserve-fragment?] (get-redirect-with-fragment ctx uri-relative)]
              (if redirect-target
                ;; Return HTTP 301 redirect to partial URL
                (let [processed-target (process-redirect-target redirect-target)
                      ;; Extract fragment from original URI if we need to preserve it
                      [_ original-fragment] (when preserve-fragment?
                                              (str/split uri-relative #"#" 2))
                      ;; Build final target URL with fragment handling
                      final-target (if (and preserve-fragment? original-fragment)
                                     (str processed-target "#" original-fragment)
                                     processed-target)
                      target-url (str (http/get-product-prefixed-url ctx "/partial")
                                      (http/get-product-prefixed-url ctx final-target))
                      session-id (:session-id request)
                      current-product (products/get-current-product ctx)
                      product-id (or (:id current-product) "unknown")]
                  ;; Track redirect event in PostHog
                  (when session-id
                    (future
                      (posthog/capture-event! ctx
                                              session-id
                                              "docs_redirect"
                                              {"old_url" uri-relative
                                               "new_url" final-target
                                               "product" product-id
                                               "preserve_fragment" preserve-fragment?})))
                  {:status 301
                   :headers {"Location" target-url}})
                ;; No file and no redirect - show 404
                {:status 404
                 :headers {"Content-Type" "text/html; charset=utf-8"
                           "Cache-Control" "private, no-cache"}
                 :body (str (hiccup2.core/html
                             (not-found/not-found-view ctx uri-relative)))}))))))))

(defn sitemap-xml
  [ctx]
  {:status 200
   :headers {"content-type" "application/xml"}
   :body (state/get-sitemap ctx)})

(defn sitemap-index-xml
  "Returns cached sitemap index XML"
  [ctx]
  {:status 200
   :headers {"content-type" "application/xml"}
   :body (or (gitbok.state/get-cache ctx :sitemap-index-xml)
             ;; Fallback: generate on-the-fly if cache is empty
             (do
               (log/warn "sitemap cache was not available")
               (gitbok.indexing.impl.sitemap-index/generate-and-cache-sitemap-index! ctx)))})

(defn redirect-to-readme
  [ctx]
  (let [readme-path (products/readme-relative-path ctx)
        filepath (or readme-path "readme/README.md")
        ;; Convert file path to URL path (remove .md and README)
        url-path (when readme-path
                   (-> readme-path
                       (str/replace #"\.md$" "")
                       (str/replace #"README$" "")
                       (str/replace #"/$" "")))]
    ;; If url-path is empty (README.md at root), render content directly
    ;; Otherwise redirect to the readme's URL
    (if (str/blank? url-path)
      ;; Render readme content directly (for products like fhirbase with README.md at root)
      (handle-cached-response ctx filepath "full"
                              (fn []
                                (let [{:keys [title description content section schema-type raw-content]}
                                      (render-file ctx filepath)
                                      lastmod (indexing/get-lastmod ctx filepath)]
                                  (layout/layout
                                   ctx
                                   {:content content
                                    :lastmod lastmod
                                    :title title
                                    :description description
                                    :section section
                                    :filepath filepath
                                    :schema-type schema-type
                                    :raw-content raw-content}))))
      ;; Redirect to readme URL (for products like auditbox with getting-started/README.md)
      {:status 301
       :headers {"Location" (http/get-product-prefixed-url ctx url-path)}})))

(defn root-redirect-handler
  "Handles root path redirect based on configuration"
  [ctx]
  (let [full-config (products/get-full-config ctx)
        root-redirect (:root-redirect full-config)]
    (if root-redirect
      (let [redirect-uri (utils/concat-urls (state/get-config ctx :prefix "") root-redirect)]
        (resp/redirect redirect-uri))
      ;; If no root-redirect configured, show 404 or default page
      (layout/layout
       ctx
       {:content (not-found/not-found-view ctx "/")
        :status 404
        :title "Not found"
        :description "Page not found"
        :hide-breadcrumb true}))))

(defn serve-static-file
  "Serves static files with proper content type and cache headers"
  [ctx]
  (let [request (:request ctx)
        uri (:uri request)
        ;; Remove /static prefix to get actual file path
        file-path (str/replace-first uri #"^.*/static" "public")]
    (when-let [response (resp/resource-response file-path)]
      (let [;; Cache for 1 year (files are versioned)
            cache-control "public, max-age=31536000, immutable"
            ;; First add content-type
            response-with-type (content-type-response response request)
            ;; Then add cache-control to the headers map
            headers (assoc (or (:headers response-with-type) {})
                           "Cache-Control" cache-control)]
        (assoc response-with-type :headers headers)))))

(defn serve-og-preview
  "Serves OG preview images with proper headers"
  [ctx]
  (let [request (:request ctx)
        uri (:uri request)
        prefix (state/get-config ctx :prefix "")
        ;; Extract file path from /public/og-preview/<product>/<path>
        path-pattern (re-pattern (str "^" (java.util.regex.Pattern/quote prefix) "/public/og-preview/(.+)$"))
        file-path (when-let [[_ relative-path] (re-find path-pattern uri)]
                    (str "public/og-preview/" relative-path))]
    (when file-path
      (when-let [response (resp/resource-response file-path)]
        (let [;; Cache OG images for 1 hour
              cache-control "public, max-age=3600"
              ;; First add content-type
              response-with-type (content-type-response response request)
              ;; Then add cache-control to the headers map
              headers (assoc (or (:headers response-with-type) {})
                             "Cache-Control" cache-control)]
          (assoc response-with-type :headers headers))))))

(defn- parse-markdown-uri
  "Parses URI to extract filepath for markdown file"
  [ctx uri]
  (let [prefix (state/get-config ctx :prefix "")
        ;; Remove prefix and product path
        uri-without-prefix (if (and (not (str/blank? prefix))
                                    (str/starts-with? uri prefix))
                             (subs uri (count prefix))
                             uri)
        product-path (:path (gitbok.products/get-current-product ctx))
        uri-without-product (if (str/starts-with? uri-without-prefix product-path)
                              (subs uri-without-prefix (count product-path))
                              uri-without-prefix)
        ;; Remove .md extension
        uri-relative (if (str/ends-with? uri-without-product ".md")
                       (subs uri-without-product 0 (- (count uri-without-product) 3))
                       uri-without-product)]
    (indexing/uri->filepath ctx uri-relative)))

(defn- serve-raw-markdown-from-disk
  "DEV mode: read markdown from disk on each request"
  [ctx]
  (let [request (:request ctx)
        uri (:uri request)]
    ;; Only handle URIs ending with .md
    (if-not (str/ends-with? uri ".md")
      nil  ; Return nil to fall through to next handler
      (let [filepath (parse-markdown-uri ctx uri)]
        (if filepath
          (let [content (try
                          (let [full-filepath (products/filepath ctx filepath)]
                            (state/slurp-resource ctx full-filepath))
                          (catch Exception e
                            (log/error e "Failed to read raw markdown file" {:filepath filepath})
                            nil))]
            (if content
              {:status 200
               :headers {"Content-Type" "text/plain; charset=utf-8"
                         "Cache-Control" "no-cache"}
               :body content}
              {:status 404
               :headers {"Content-Type" "text/plain"}
               :body "Markdown file not found"}))
          {:status 404
           :headers {"Content-Type" "text/plain"}
           :body "Page not found"})))))

(defn- serve-raw-markdown-from-cache
  "Production mode: serve markdown from cache"
  [ctx]
  (let [request (:request ctx)
        uri (:uri request)]
    ;; Only handle URIs ending with .md
    (if-not (str/ends-with? uri ".md")
      nil  ; Return nil to fall through to next handler
      (let [filepath (parse-markdown-uri ctx uri)]
        (if filepath
          (let [md-files-idx (state/get-md-files-idx ctx)
                content (get md-files-idx filepath)]
            (if content
              {:status 200
               :headers {"Content-Type" "text/plain; charset=utf-8"
                         "Cache-Control" "public, max-age=3600"}
               :body content}
              {:status 404
               :headers {"Content-Type" "text/plain"}
               :body "Markdown file not found"}))
          {:status 404
           :headers {"Content-Type" "text/plain"}
           :body "Page not found"})))))

(defn make-serve-raw-markdown-handler
  "Creates markdown handler based on dev-mode"
  [context]
  (if (state/get-config context :dev-mode)
    #'serve-raw-markdown-from-disk
    #'serve-raw-markdown-from-cache))

;; llms.txt handlers

(defn serve-llms-txt
  "Serve llms.txt for a specific product"
  [ctx]
  (try
    (let [product (:product ctx)]
      (if product
        (let [content (llms-txt/generate-product-llms-txt ctx product)]
          {:status 200
           :headers {"Content-Type" "text/plain; charset=utf-8"
                     "Cache-Control" "public, max-age=3600"}
           :body content})
        {:status 404
         :headers {"Content-Type" "text/plain"}
         :body "Product not found"}))
    (catch Exception e
      (log/error e "Failed to serve llms.txt")
      {:status 500
       :headers {"Content-Type" "text/plain"}
       :body "Error generating llms.txt"})))

(defn serve-root-llms-txt
  "Serve root llms.txt with list of all products"
  [ctx]
  (try
    (let [content (llms-txt/generate-root-llms-txt ctx)]
      {:status 200
       :headers {"Content-Type" "text/plain; charset=utf-8"
                 "Cache-Control" "public, max-age=3600"}
       :body content})
    (catch Exception e
      (log/error e "Failed to serve root llms.txt")
      {:status 500
       :headers {"Content-Type" "text/plain"}
       :body "Error generating llms.txt"})))

;; Link preview handler

(defn batch-previews-handler
  "Returns preview data (title, description, breadcrumbs) for a batch of hrefs.
   POST body: {\"hrefs\": [\"/docs/foo\", \"/docs/bar\"]}
   Response: {\"/docs/foo\": {\"t\": \"Title\", \"d\": \"Desc\", \"b\": [\"Section\"]}}"
  [ctx]
  (try
    (let [request (:request ctx)
          body (:body-params request)
          hrefs (or (:hrefs body) (get body "hrefs") [])]
      (if (and (sequential? hrefs) (<= (count hrefs) 200))
        (let [result (previews/get-previews-for-hrefs ctx hrefs)]
          {:status 200
           :headers {"Content-Type" "application/json"
                     "Cache-Control" "public, max-age=3600"}
           :body (json/generate-string (or result {}))})
        {:status 400
         :headers {"Content-Type" "application/json"}
         :body (json/generate-string {:error "Invalid hrefs: must be array with max 200 items"})}))
    (catch Exception e
      (log/error e "Failed to get batch previews")
      {:status 500
       :headers {"Content-Type" "application/json"}
       :body (json/generate-string {:error "Internal server error"})})))
