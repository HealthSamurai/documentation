(ns gitbok.handlers
  "HTTP request handlers for the application"
  (:require
   [clojure.stacktrace]
   [clojure.string :as str]
   [clojure.tools.logging :as log]
   [gitbok.http :as http]
   [gitbok.indexing.core :as indexing]
   [gitbok.state :as state]
   [gitbok.indexing.impl.sitemap-index :as sitemap-index]
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
                                            nil))
                           from-index (get (indexing/get-md-files-idx context) filepath)
                           content (or from-disk from-index)]
                       (log/info "DEV: content loaded" {:path full-filepath
                                                        :from-disk? (some? from-disk)
                                                        :from-index? (some? from-index)
                                                        :size (if content (count content) 0)
                                                        :nil? (nil? content)})
                       content))
                   ;; In production, use cached content from memory
                   (or (get (indexing/get-md-files-idx context) filepath)
                       (state/slurp-resource context full-filepath)))
        _ (when-not content*
            (log/error "No content found" {:filepath filepath :full-filepath full-filepath}))
        {:keys [parsed description title]}
        (if content*
          (markdown/parse-markdown-content
           context
           [full-filepath content*])
          {:parsed [:div "No content"] :description "" :title "Error"})]
    (try
      {:content
       (main-content/render-file* context full-filepath parsed title (or content* ""))
       :title title
       :description description
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
     :title (:title result)}))

(defn check-cache-lastmod [request last-mod]
  (let [if-modified-since
        (get-in request [:headers "if-modified-since"])
        lastmod (utils/iso-to-http-date last-mod)]
    (and if-modified-since
         lastmod
         (= if-modified-since lastmod))))

(defn check-cache-etag [request etag]
  (let [if-none-match (get-in request [:headers "if-none-match"])]
    (and if-none-match (= if-none-match etag))))

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
                   (str/replace-first #".*.gitbook/" ""))
        dev-mode? (state/get-config ctx :dev-mode)
        volume-path (state/get-config ctx :docs-volume-path)
        ;; Try to find the image in multiple locations
        response (if volume-path
                   ;; When volume path is available: try filesystem first
                   (or
                    ;; Try at root .gitbook/assets/
                    (resp/file-response (str volume-path "/.gitbook/assets/" file-path))
                    ;; Try at .gitbook/ (for compatibility)
                    (resp/file-response (str volume-path "/.gitbook/" file-path))
                    ;; Try docs/.gitbook/assets/
                    (resp/file-response (str volume-path "/docs/.gitbook/assets/" file-path))
                    ;; Try docs/.gitbook/ (for compatibility)
                    (resp/file-response (str volume-path "/docs/.gitbook/" file-path))
                    ;; Fallback to classpath resources
                    (resp/resource-response file-path)
                    (resp/resource-response (str ".gitbook/" file-path)))
                   ;; No volume path: use classpath only
                   (or
                    (resp/resource-response file-path)
                    (resp/resource-response (str ".gitbook/" file-path))))]
    (when response
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
        (assoc response-with-type :headers headers)))))

(defn render-favicon [ctx]
  (resp/resource-response
    (state/get-product-state ctx [:favicon-path])))

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

      :else
      ;; Check for redirects first
      (if-let [redirect-target (indexing/get-redirect ctx uri-relative)]
        ;; Return HTTP 301 redirect
        (let [target-url (http/get-product-prefixed-url ctx redirect-target)]
          {:status 301
           :headers {"Location" target-url}})
        ;; Otherwise proceed with normal file handling
        (let [filepath (indexing/uri->filepath ctx uri-relative)]
          (if filepath
            (let [lastmod (indexing/get-lastmod ctx filepath)
                  lastmod-iso (utils/iso-to-http-date lastmod)
                  etag (utils/etag lastmod-iso)]
              (if (or (check-cache-etag request etag)
                      (and lastmod
                           (check-cache-lastmod request lastmod)))
                {:status 304
                 :headers {"Cache-Control" "public, max-age=300"
                           "Last-Modified" lastmod-iso
                           "ETag" etag}}
                (let [{:keys [title description content section]}
                      (render-file ctx filepath)]
                  (layout/layout
                   ctx
                   {:content content
                    :lastmod lastmod
                    :title title
                    :description description
                    :section section
                    :filepath filepath}))))

            (layout/layout
             ctx
             {:content (not-found/not-found-view ctx uri-relative)
              :status 404
              :title "Not found"
              :description "Page not found"
              :hide-breadcrumb true})))))))

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
  (let [request (:request ctx)
        prefix (state/get-config ctx :prefix "")
        uri (products/uri
             ctx prefix
             (or (products/readme-url ctx) "readme"))
        new-ctx (assoc ctx :request (assoc request :uri uri))]
    (render-file-view new-ctx)))

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
        uri (http/url-without-prefix ctx (:uri request))
        path (str/replace uri #"^/static/" "")
        response (resp/resource-response (utils/concat-urls "public" path))]
    (when response
      (let [;; Cache for 1 year (files are versioned)
            cache-control "public, max-age=31536000, immutable"
            ;; First add content-type
            response-with-type (content-type-response response request)
            ;; Then add cache-control to the headers map
            headers (assoc (or (:headers response-with-type) {})
                           "Cache-Control" cache-control)]
        (assoc response-with-type :headers headers)))))

(defn serve-og-preview
  "Serves OG preview images from resources"
  [ctx]
  (let [request (:request ctx)
        uri (http/url-without-prefix ctx (:uri request))
        path (str/replace uri #"^/public/og-preview/" "")
        resource-path (utils/concat-urls "public/og-preview" path)]
    (if-let [response (resp/resource-response resource-path)]
      (resp/content-type response "image/png")
      {:status 404
       :body "OG preview not found"})))
