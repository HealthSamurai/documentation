(ns gitbok.core
  (:require
   [gitbok.indexing.impl.sitemap :as sitemap]
   [gitbok.indexing.impl.sitemap-index :as sitemap-index]
   [clojure.string :as str]
   [hiccup2.core]
   [gitbok.markdown.core :as markdown]
   [gitbok.indexing.core :as indexing]
   [gitbok.indexing.impl.file-to-uri :as file-to-uri]
   [gitbok.indexing.impl.summary :as summary]
   [gitbok.indexing.impl.uri-to-file :as uri-to-file]
   [gitbok.ui.main-content :as main-content]
   [gitbok.ui.layout :as layout]
   [gitbok.ui.not-found :as not-found]
   [gitbok.ui.main-navigation :as main-navigation]
   [gitbok.ui.left-navigation :as left-navigation]
   [gitbok.ui.search]
   [gitbok.ui.meilisearch]
   [ring.middleware.gzip :refer [wrap-gzip]]
   [ring.middleware.content-type :refer [content-type-response]]
   [gitbok.http]
   [gitbok.products :as products]
   [gitbok.constants :as const]
   [http]
   [ring.util.response :as resp]
   [system]
   [gitbok.utils :as utils]
   [uui])
  (:gen-class))

(set! *warn-on-reflection* true)

(def dev? (= "true" (System/getenv "DEV")))
(def prefix (or (System/getenv "DOCS_PREFIX") "/"))
(def base-url (or (System/getenv "BASE_URL")
                  "http://localhost:8081"))

(defn read-markdown-file [context filepath]
  (let [[filepath section] (str/split filepath #"#")
        filepath (products/filepath context filepath)
        content* (utils/slurp-resource filepath)
        {:keys [parsed description title]}
        (markdown/parse-markdown-content
         context
         [filepath content*])]
    (try
      {:content
       (main-content/render-file* context filepath parsed title content*)
       :title title
       :description description
       :section section}
      (catch Exception e
        (println "cannot render file " filepath)
        {:content [:div {:role "alert"}
                   (.getMessage e)
                   [:pre (pr-str e)]
                   [:pre content*]]}))))

(defn picture-url? [url]
  (when url
    (str/starts-with? url "/.gitbook/assets")))

(defn render-file
  [context filepath]
  (let [result
        (try
          (if dev?
            (read-markdown-file context filepath)
            (markdown/get-rendered context filepath))
          (catch Exception e
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

(defn render-pictures [context request]
  (let [uri (:uri request)
        uri-relative
        (utils/uri-to-relative uri prefix
                               (products/path context))]
    (resp/resource-response
     (->
      uri-relative
      (str/replace #"%20" " ")
      (str/replace-first #".*.gitbook/" "")))))

(defn render-favicon [context _]
  (let [product (products/get-current-product context)
        favicon-path (or (:favicon product) "public/favicon.ico")]
    (if (str/starts-with? favicon-path ".gitbook/")
      ;; Handle .gitbook/assets paths like render-pictures does
      (resp/resource-response
       (str/replace-first favicon-path #".*.gitbook/" ""))
      ;; Regular resource path
      (resp/resource-response favicon-path))))

(defn render-robots-txt [context _]
  (let [product (products/get-current-product context)
        robots-path (:robots product)]
    (if robots-path
      ;; Use custom robots.txt if specified
      (if (str/starts-with? robots-path ".gitbook/")
        ;; Handle .gitbook/assets paths like render-pictures does
        (resp/resource-response
         (str/replace-first robots-path #".*.gitbook/" ""))
        ;; Regular resource path
        (resp/resource-response robots-path))
      ;; Generate default robots.txt dynamically
      (let [sitemap-url (gitbok.http/get-product-absolute-url
                         context
                         "/sitemap.xml")]
        {:status 200
         :headers {"content-type" "text/plain"}
         :body (str "User-agent: *\n"
                    "Allow: /\n"
                    "Sitemap: " sitemap-url "\n")}))))

(defn render-landing
  "Render the landing page for a product"
  [context request]
  (let [content
        [:div.px-4.py-12.mx-auto
         [:div.text-center.mb-12
          [:h1.text-4xl.font-bold.mb-4.text-tint-12.font-sans "What is Aidbox"]]

         ;; Bento grid layout - 4 columns
         [:div.grid.grid-cols-1.md:grid-cols-2.lg:grid-cols-4.gap-6

          ;; FHIR Database Card - spans 2 columns
          [:a.block.p-4.rounded-lg.bg-tint-base.border.border-tint-6.hover:shadow-lg.transition-shadow.duration-300.lg:col-span-2
           {:href "/database/overview"}
           [:img.w-20.h-20.mb-4
            {:src "https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/685e9a442bb239cfcd007e5c_Database%20%2B%20FHIR.svg"
             :alt "FHIR Database"}]
           [:h3.text-xl.mb-3.text-tint-12.font-semibold.font-sans "FHIR Database"]
           [:div.flex.flex-wrap.gap-2.mb-3
            [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded "PostgreSQL"]
            [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded "JSONB"]
            [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded "Indexes"]
            [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded "Custom resources"]
            [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded "SQL on FHIR"]]
           [:p.text-sm.leading-relaxed.text-tint-10.font-content
            "Manage FHIR data with the power of PostgreSQL — fully under your control. Aidbox stores resources transparently as JSONB, enabling you to query, join, and aggregate by any element, with full support for transactional operations, reporting, and seamless migrations."]]

          ;; API Card
          [:a.block.p-4.rounded-lg.bg-tint-base.border.border-tint-6.hover:shadow-lg.transition-shadow.duration-300
           {:href "/api/api-overview"}
           [:img.w-20.h-20.mb-4
            {:src "https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/685e9a444fc720f2ad877e7d_API.svg"
             :alt "API"}]
           [:h3.text-xl.mb-3.text-tint-12.font-semibold.font-sans "API"]
           [:div.flex.flex-wrap.gap-2.mb-3
            [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded "FHIR"]
            [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded "SQL"]
            [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded "GraphQL"]]
           [:p.text-sm.leading-relaxed.text-tint-10.font-content
            "Multiple APIs — FHIR, SQL, GraphQL, Bulk, and Subscription — to work efficiently with FHIR data for maximum flexibility and performance."]]

          ;; Artifact Registry Card
          [:a.block.p-4.rounded-lg.bg-tint-base.border.border-tint-6.hover:shadow-lg.transition-shadow.duration-300
           {:href "/artifact-registry/artifact-registry-overview"}
           [:img.w-20.h-20.mb-4
            {:src "https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/685e9a44bf8f6440a9a0bcc2_FHIR%20Artefact%20Registry.svg"
             :alt "Artifact Registry"}]
           [:h3.text-xl.mb-3.text-tint-12.font-semibold.font-sans "Artifact Registry"]
           [:div.flex.flex-wrap.gap-2.mb-3
            [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded "IGs"]
            [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded "Profiles"]
            [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded "Search params"]]
           [:p.text-sm.leading-relaxed.text-tint-10.font-content
            "Multiple FHIR versions: STU3, R4, R5, and R6. 500+ ready-to-use IGs: IPS, national (US, DE, CA, etc.), domain (mCode, Da Vinci, etc.), custom IGs."]]

          ;; Access Control Card
          [:a.block.p-4.rounded-lg.bg-tint-base.border.border-tint-6.hover:shadow-lg.transition-shadow.duration-300
           {:href "/access-control/access-control"}
           [:img.w-20.h-20.mb-4
            {:src "https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/685e9a441cfd9ebadf77b357_AUTH.svg"
             :alt "Access Control"}]
           [:h3.text-xl.mb-3.text-tint-12.font-semibold.font-sans "Access Control"]
           [:div.flex.flex-wrap.gap-2.mb-3
            [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded "OAuth 2.0"]
            [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded "SMART"]
            [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded "RBAC/ABAC"]]
           [:p.text-sm.leading-relaxed.text-tint-10.font-content
            "Enterprise-grade security with OAuth 2.0, multitenancy, flexible user management, granular access policies, and complete audit trails."]]

          ;; Terminology Card
          [:a.block.p-4.rounded-lg.bg-tint-base.border.border-tint-6.hover:shadow-lg.transition-shadow.duration-300
           {:href "/terminology-module/overview"}
           [:img.w-20.h-20.mb-4
            {:src "https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/685e9a4419fe3f4c5c0e24b5_Translation%20Book.svg"
             :alt "Terminology"}]
           [:h3.text-xl.mb-3.text-tint-12.font-semibold.font-sans "Terminology"]
           [:div.flex.flex-wrap.gap-2.mb-3
            [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded "CodeSystems"]
            [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded "ValueSets"]]
           [:p.text-sm.leading-relaxed.text-tint-10.font-content
            "Validate codes and perform fast lookups in ICD-10, SNOMED, LOINC. Use custom code systems and value sets."]]

          ;; Developer Experience Card
          [:a.block.p-4.rounded-lg.bg-tint-base.border.border-tint-6.hover:shadow-lg.transition-shadow.duration-300
           {:href "/developer-experience/developer-experience-overview"}
           [:img.w-20.h-20.mb-4
            {:src "https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/685e9a4478a178659dd16f36_SDK.svg"
             :alt "Developer Experience"}]
           [:h3.text-xl.mb-3.text-tint-12.font-semibold.font-sans "Developer Experience"]
           [:div.flex.flex-wrap.gap-2.mb-3
            [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded "Python"]
            [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded "C#"]
            [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded "JS"]
            [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded "Codegen"]]
           [:p.text-sm.leading-relaxed.text-tint-10.font-content
            "TypeScript, C#, and Python SDKs for easy Aidbox integration and rapid app development. SDK generator for custom toolkits tailored to your stack."]]

          ;; UI Card
          [:a.block.p-4.rounded-lg.bg-tint-base.border.border-tint-6.hover:shadow-lg.transition-shadow.duration-300
           {:href "/overview/aidbox-ui"}
           [:img.w-20.h-20.mb-4
            {:src "https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/685e9a44f6b12fad351dc0d6_UI.svg"
             :alt "UI"}]
           [:h3.text-xl.mb-3.text-tint-12.font-semibold.font-sans "UI"]
           [:div.flex.flex-wrap.gap-2.mb-3
            [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded "FHIR Viewer"]
            [:span.px-2.py-1.text-xs.bg-tint-3.text-tint-11.rounded "Search params"]]
           [:p.text-sm.leading-relaxed.text-tint-10.font-content
            "Intuitive UI to work with FHIR data, manage users, clients, access policies, and configure system settings."]]]

         ;; Bottom CTA button
         [:div.text-center.mt-12
          [:a.inline-flex.items-center.px-6.py-3.bg-primary-9.text-white.font-medium.font-sans.rounded-lg.hover:bg-primary-10.transition-colors
           {:href "/readme/features"}
           "Technical Features →"]]]

        title "What is Aidbox"
        description "Aidbox - FHIR-first healthcare application platform"

        ;; Custom layout for landing page without breadcrumb and max-width constraint
        full-page [:div
                   (main-navigation/nav context)
                   [:div.mobile-menu-overlay]
                   [:div
                    {:class "flex max-w-screen-2xl mx-auto site-full-width:max-w-full
                     items-start overflow-visible md:px-8"}
                    (left-navigation/left-navigation
                     (summary/get-summary context)
                     (:uri request))
                    ;; Custom content area without max-w-5xl and breadcrumb
                    [:main#content {:class "flex-1 items-start"}
                     [:script (uui/raw "hljs.highlightAll(); if (typeof initializeMermaid !== 'undefined') { initializeMermaid(); }")]
                     [:div {:class "flex items-start"}
                      [:article {:class "article__content py-8 min-w-0 flex-1 transform-3d"}
                       [:div {:class "mx-auto max-w-full"}
                        content]]]]]]]

    ;; Return custom response bypassing standard layout
    (gitbok.http/response1
     (layout/document
      context
      full-page
      {:title title
       :description description
       :canonical-url (gitbok.http/get-absolute-url context (:uri request))
       :og-preview nil
       :lastmod nil
       :favicon-url (gitbok.http/get-product-prefixed-url context "/favicon.ico")}))))

(defn render-file-view
  [context request]
  (let [uri (:uri request)
        uri-relative
        (utils/uri-to-relative
         uri
         prefix
         (products/path context))]
    (cond

      (or (picture-url? uri-relative)
          (picture-url? uri))
      (render-pictures context request)

      (str/starts-with? uri-relative "public/og-preview")
      (resp/resource-response uri-relative)

      :else
      (let [filepath
            (indexing/uri->filepath context uri-relative)]
        (if filepath
          (let [lastmod (indexing/get-lastmod context filepath)
                etag (utils/etag lastmod)]
            (if (or (check-cache-etag request etag)
                    (and lastmod
                         (check-cache-lastmod request lastmod)))
              {:status 304
               :headers {"Cache-Control" "public, max-age=86400"
                         "Last-Modified" lastmod
                         "ETag" etag}}
              (let [{:keys [title description content section]}
                    (render-file (assoc context :current-uri uri-relative) filepath)]
                (layout/layout
                 context request
                 {:content content
                  :lastmod lastmod
                  :title title
                  :description description
                  :section section
                  :filepath filepath}))))

          (layout/layout
           context request
           {:content (not-found/not-found-view context uri-relative)
            :status 404
            :title "Not found"
            :description "Page not found"}))))))

(defn sitemap-xml
  [context _]
  {:status 200
   :headers {"content-type" "application/xml"}
   :body (sitemap/get-sitemap context)})

(defn sitemap-index-xml
  "Generates sitemap index that references all product sitemaps"
  [context _]
  {:status 200
   :headers {"content-type" "application/xml"}
   :body (gitbok.indexing.impl.sitemap-index/generate-sitemap-index context)})

(defn redirect-to-readme
  [context request]
  (let [uri (products/uri
             context prefix
             (or (products/readme-url context) "readme"))
        request
        (assoc request
               :uri uri
               :/ true)]
    (render-file-view context request)))

(defn root-redirect-handler
  "Handles root path redirect based on configuration"
  [context request]
  (let [full-config (products/get-full-config context)
        root-redirect (:root-redirect full-config)]
    (if root-redirect
      (let [redirect-uri (utils/concat-urls prefix root-redirect)]
        (resp/redirect redirect-uri))
      ;; If no root-redirect configured, show 404 or default page
      (layout/layout
       context request
       {:content (not-found/not-found-view context "/")
        :status 404
        :title "Not found"
        :description "Page not found"}))))

(defn healthcheck
  [_ _]
  {:status 200 :body {:status "ok"}})

(defn version-endpoint
  [context _]
  {:status 200 :body {:version (gitbok.http/get-version context)}})

(defn debug-endpoint
  [context request]
  (let [products (products/get-products-config context)
        full-config (products/get-full-config context)
        current-product (products/get-current-product context)]
    {:status 200
     :headers {"content-type" "application/json"}
     :body {:products products
            :full-config full-config
            :current-product current-product
            :environment {:prefix prefix
                          :base-url base-url
                          :port (gitbok.http/get-port context)}
            :request-info {:uri (:uri request)
                           :headers (select-keys (:headers request)
                                                 ["host" "x-forwarded-for" "x-real-ip"
                                                  "x-forwarded-proto" "x-forwarded-host"])}}}))

(defn serve-static-file
  "Serves static files with proper content type headers"
  [context request]
  (let [uri (gitbok.http/url-without-prefix context (:uri request))
        path (str/replace uri #"^/static/" "")
        response (resp/resource-response (utils/concat-urls "public" path))]
    (when response
      (content-type-response response request))))

(system/defmanifest
  {:description "gitbok"
   :deps ["http"]
   :config {:history {:type "boolean"}}})

(def default-port 8081)

(def port
  (let [p (System/getenv "PORT")]
    (or
     (when (string? p)
       (try (Integer/parseInt p)
            (catch Exception _ nil)))
     default-port)))

(def default-config
  {:services ["http" "uui" "gitbok.core"]
   :http {:port port}})

(defn product-middleware
  "Middleware to determine product from request URI"
  [handler]
  (fn [context request]
    (let [uri (:uri request)
          uri-without-prefix (if (str/starts-with? uri prefix)
                               (subs uri (count prefix))
                               uri)
          products-config (products/get-products-config context)
          product (products/determine-product-by-uri products-config uri-without-prefix)
          context (products/set-current-product-id context (:id product))]
      (handler context request))))

(defn gzip-middleware [f]
  (fn [ctx req]
    (let [ring-handler (fn [req] (f ctx req))
          gzip-handler (wrap-gzip ring-handler)]
      (gzip-handler req))))

(defn init-product-indices
  "Initializes indexes for a specific product"
  [context product]
  (let [;; Temporarily set current product for initialization
        ctx (products/set-current-product-id context (:id product))]
    ;; order is important
    (println "1. read summary. create toc htmx.")
    (summary/set-summary ctx)
    (println "2. get uris from summary (using slugging), merge with redirects")
    (uri-to-file/set-idx ctx)
    (println "3. reverse file to uri idx")
    (file-to-uri/set-idx ctx)
    (println "4. using files from summary (step 3), read all files into memory")
    (indexing/set-md-files-idx ctx (file-to-uri/get-idx ctx))
    (println "5. parse all files into memory, some things are already rendered as plain html")
    (markdown/set-parsed-markdown-index ctx (indexing/get-md-files-idx ctx))
    (println "6. using parsed markdown, set search index")
    (indexing/set-search-idx ctx (markdown/get-parsed-markdown-index ctx))
    (println "7. render it on start")
    (when-not dev?
      (println "Not DEV, render all pages into memory")
      (markdown/render-all! ctx
                            (markdown/get-parsed-markdown-index ctx)
                            read-markdown-file)
      (println "Render all pages done."))
    (println "8. set lastmod data in context for Last Modified metadata")
    (indexing/set-lastmod ctx)
    (println "9. generate sitemap.xml for product")
    (when-not false
      (println (str "generating sitemap.xml for " (:name product)))
      ;; Now sitemap can use the lastmod data from context
      (let [lastmod-data (products/get-product-state ctx [const/LASTMOD])]
        (sitemap/set-sitemap ctx lastmod-data)))))

(defn init-products
  "Initializes all products from configuration"
  [context]
  (let [full-config (products/load-products-config)
        products-config (:products full-config)]
    (products/set-products-config context products-config)
    (products/set-full-config context full-config)
    products-config))

#_{:clj-kondo/ignore [:unresolved-symbol]}
(system/defstart
  [context config]
  (gitbok.http/set-port context port)

  (gitbok.http/set-prefix context prefix)

  (gitbok.http/set-base-url context base-url)

  (gitbok.http/set-version
   context
   (str/trim (utils/slurp-resource "version")))

  (gitbok.http/set-dev-mode context dev?)

  (http/register-endpoint
   context
   {:path (utils/concat-urls prefix "/static/:path*")
    :method :get
    :fn #'serve-static-file})

  (http/register-endpoint
   context
   {:path (utils/concat-urls prefix "/.gitbook/assets/:path*")
    :method :get
    :middleware [gzip-middleware]
    :fn #'render-pictures})

  ;; Initialize all products
  (let [products-config
        (init-products context)]
    ;; Register endpoints for each product
    (doseq [product products-config]
      (let [product-path (utils/concat-urls prefix (:path product))]
        (println "base path! " product-path)

        ;; Product main page
        (http/register-endpoint
         context
         {:path product-path
          :method :get
          :middleware [product-middleware gzip-middleware]
          :fn #'redirect-to-readme})

        ;; Product search
        (http/register-endpoint
         context
         {:path (str product-path "/search/dropdown")
          :method :get
          :middleware [product-middleware gzip-middleware]
          :fn
          gitbok.ui.search/search-endpoint})

        ;; Meilisearch HTMX endpoint
        (http/register-endpoint
         context
         {:path (str product-path "/meilisearch/dropdown")
          :method :get
          :middleware [product-middleware gzip-middleware]
          :fn
          gitbok.ui.meilisearch/meilisearch-endpoint})

        ;; Product sitemap
        (http/register-endpoint
         context
         {:path (str product-path "/sitemap.xml")
          :method :get
          :middleware [product-middleware]
          :fn #'sitemap-xml})

        ;; Product favicon
        (http/register-endpoint
         context
         {:path (str product-path "/favicon.ico")
          :method :get
          :middleware [product-middleware gzip-middleware]
          :fn #'render-favicon})

        ;; Product robots.txt
        (http/register-endpoint
         context
         {:path (str product-path "/robots.txt")
          :method :get
          :middleware [product-middleware]
          :fn #'render-robots-txt})

        ;; Product landing page
        (http/register-endpoint
         context
         {:path (str product-path "/landing")
          :method :get
          :middleware [product-middleware gzip-middleware]
          :fn #'render-landing})

        ;; Product OG preview images
        (http/register-endpoint
         context
         {:path (str product-path "/public/og-preview/:product-id/:path*")
          :method :get
          :middleware [product-middleware gzip-middleware]
          :fn #'render-pictures})

        ;; All product pages
        (http/register-endpoint
         context
         {:path (str product-path "/:path*")
          :method :get
          :middleware [product-middleware gzip-middleware]
          :fn #'render-file-view}))))

  ;; Root path handler
  (http/register-endpoint
   context
   {:path prefix
    :method :get
    :middleware [gzip-middleware]
    :fn #'root-redirect-handler})

  ;; Root path with trailing slash handler
  (http/register-endpoint
   context
   {:path (str prefix "/")
    :method :get
    :middleware [gzip-middleware]
    :fn #'root-redirect-handler})

  ;; Sitemap index at root
  (http/register-endpoint
   context
   {:path (utils/concat-urls prefix "/sitemap.xml")
    :method :get
    :fn #'sitemap-index-xml})

  ;; Root robots.txt
  (http/register-endpoint
   context
   {:path (utils/concat-urls prefix "/robots.txt")
    :method :get
    :fn (fn [ctx _]
          (let [sitemap-url (str base-url
                                 (utils/concat-urls prefix "/sitemap.xml"))]
            {:status 200
             :headers {"content-type" "text/plain"}
             :body (str "User-agent: *\n"
                        "Allow: /\n"
                        "Sitemap: " sitemap-url "\n")}))})

  (http/register-endpoint
   context
   {:path (utils/concat-urls prefix "/version")
    :method :get
    :fn #'version-endpoint})

  (http/register-endpoint
   context
   {:path (utils/concat-urls prefix "/debug")
    :method :get
    :fn #'debug-endpoint})

  (let [products-config (products/get-products-config context)]
    (doseq [product products-config]
      (println)
      (println)
      (println (str "Initializing product indices: "
                    (:name product)))
      (init-product-indices context product))

    (println "setup done!")
    (println "version " (utils/slurp-resource "version"))
    (println "PORT env " (System/getenv "PORT"))
    (println "port " port)
    (println "BASE_URL " base-url)
    (println "PREFIX " prefix)
    (println "Products:\n"
             (str/join "\n" (mapv #(str (:name %) " - " (:path %))
                                  products-config))
             "\n"))

  (http/register-endpoint
   context
   {:path "/healthcheck"
    :method :get
    :fn #'healthcheck})

  {})

(defn -main [& _args]
  (.addShutdownHook (Runtime/getRuntime)
                    (Thread. #(println "Got SIGTERM.")))
  (println "Server started")
  (println "port " port)
  (system/start-system default-config))
