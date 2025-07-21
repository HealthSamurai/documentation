(ns gitbok.core
  (:require
   [gitbok.indexing.impl.sitemap :as sitemap]
   [edamame.core :as edamame]
   [clojure.string :as str]
   [hiccup2.core]
   [gitbok.markdown.core :as markdown]
   [gitbok.markdown.widgets]
   [gitbok.indexing.core :as indexing]
   [gitbok.indexing.impl.file-to-uri :as file-to-uri]
   [gitbok.indexing.impl.summary :as summary]
   [gitbok.indexing.impl.uri-to-file :as uri-to-file]
   [gitbok.ui.main-content :as main-content]
   [gitbok.ui.right-toc :as right-toc]
   [gitbok.ui.layout :as layout]
   [gitbok.ui.not-found :as not-found]
   [gitbok.ui.search]
   [gitbok.dev.page-relocator :as page-relocator]
   [ring.middleware.gzip :refer [wrap-gzip]]
   [ring.middleware.params :refer [wrap-params]]
   [ring.middleware.keyword-params :refer [wrap-keyword-params]]
   [gitbok.http]
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
        content* (utils/slurp-resource filepath)
        {:keys [parsed description title]}
        (markdown/parse-markdown-content
         context
         [filepath content*])]
    (try
      {:content
       (main-content/render-file* context filepath parsed title content*)
       :description
       (or
        description
        (let [stripped (utils/strip-markdown content*)]
          (if (>= (count stripped) 150)
            (subs stripped 0 150)
            stripped)))
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
       (:content result) ; This preserves the {:content ... :parsed ...} structure from render-file*
       [:div result])
     :section (:section result)
     :description (:description result)}))

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

(defn render-pictures [_ request]
  (let [uri (:uri request)
        uri-without-prefix
        (if (str/starts-with? uri prefix)
          (subs uri (count prefix))
          uri)]
    (resp/resource-response
     (->
      uri-without-prefix
      (str/replace #"%20" " ")
      (str/replace-first #".*.gitbook/" "")))))

(defn render-favicon [_ _]
  (resp/resource-response "public/favicon.ico"))

(defn render-file-view
  [context request]
  (let [uri (:uri request)
        uri-without-prefix (if (str/starts-with? uri prefix)
                             (subs uri (count prefix))
                             uri)]
    (cond

      (or (= uri-without-prefix "/robots.txt")
          (= uri "/robots.txt"))
      (resp/resource-response "public/robots.txt")

      (or (picture-url? uri-without-prefix)
          (picture-url? uri))
      (render-pictures context request)

      (str/starts-with? uri-without-prefix "/public/og-preview")
      (resp/resource-response uri-without-prefix)

      :else
      (let [filepath (indexing/uri->filepath context uri-without-prefix)]
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
              (let [title (:title (get (indexing/file->uri-idx context) filepath))
                    {:keys [description content section]}
                    (render-file context filepath)]
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
           {:content (not-found/not-found-view context uri-without-prefix)
            :status 404
            :title "Not found"
            :description "Page not found"}))))))

(defn sitemap-xml
  [context _]
  {:status 200
   :headers {"content-type" "application/xml"}
   :body (sitemap/get-sitemap context)})

(defn redirect-to-readme
  [context request]
  (let [request
        (assoc request
               :uri (utils/concat-urls prefix "readme")
               :/ true)]
    (render-file-view context request)))

(defn healthcheck
  [_ _]
  {:status 200 :body {:status "ok"}})

(defn version-endpoint
  [context _]
  {:status 200 :body {:version (gitbok.http/get-version context)}})

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

(defn gzip-middleware [f]
  (fn [ctx req]
    (let [ring-handler (fn [req] (f ctx req))
          gzip-handler (wrap-gzip ring-handler)]
      (gzip-handler req))))

(defn form-parsing-middleware [f]
  "Middleware to parse form-urlencoded data into params and form-params"
  (fn [ctx req]
    (let [ring-handler (fn [req] (f ctx req))
          form-handler (-> ring-handler
                           wrap-keyword-params
                           wrap-params)]
      (form-handler req))))

#_{:clj-kondo/ignore [:unresolved-symbol]}
(system/defstart
  [context config]
  (gitbok.http/set-port context port)
  (gitbok.http/set-prefix context prefix)
  (gitbok.http/set-base-url context base-url)
  (gitbok.http/set-version context (str/trim (utils/slurp-resource "version")))
  (gitbok.http/set-dev-mode context dev?)

  ;; order is important
  ; 1. read summary. create toc htmx.
  (summary/set-summary context)
  ; 2. get uris from summary (using slugging), merge with redirects
  (uri-to-file/set-idx context)
  ; 3. reverse file to uri idx
  (file-to-uri/set-idx context)
  ; 4. using files from summary (step 3), read all files into memory
  (indexing/set-md-files-idx
   context
   (file-to-uri/get-idx context))
  ; 5. parse all files into memory, some things are already rendered as plain html
  (markdown/set-parsed-markdown-index
   context
   (indexing/get-md-files-idx context))
  ; 6. using parsed markdown, set search index
  (indexing/set-search-idx
   context
   (markdown/get-parsed-markdown-index context))
  ;; 7. render it on start
  (when-not dev?
    (markdown/render-all!
     context
     (markdown/get-parsed-markdown-index context) read-markdown-file))
  ;; 8. generate sitemap.xml
  (when-not
   dev?
    (println "generating sitemap.xml")
    (sitemap/set-sitemap
     context
     (edamame/parse-string (utils/slurp-resource "lastmod.edn"))))
  ;; 9. set lastmod.edn in context for Last Modified metadata
  (indexing/set-lastmod context)

  (http/register-endpoint
   context
   {:path (utils/concat-urls prefix "/search/dropdown")
    :method :get
    :middleware [gzip-middleware]
    :fn (fn [context request]
          (let [result (gitbok.ui.search/search-dropdown-results context request)]
            {:status 200
             :headers {"content-type" "text/html; charset=utf-8"
                       "Cache-Control" "no-cache, no-store, must-revalidate"}
             :body (gitbok.utils/->html result)}))})

  (http/register-endpoint
   context
   {:path (utils/concat-urls prefix "/sitemap.xml")
    :method :get
    :fn #'sitemap-xml})

  (http/register-endpoint
   context
   {:path (utils/concat-urls prefix "/:path*")
    :method :get
    :middleware [gzip-middleware]
    :fn #'render-file-view})

  (http/register-endpoint
   context
   {:path "/.gitbook/assets/:path*"
    :method :get
    :middleware [gzip-middleware]
    :fn #'render-pictures})

  (http/register-endpoint
   context
   {:path (utils/concat-urls prefix "favicon.ico")
    :method :get
    :middleware [gzip-middleware]
    :fn #'render-favicon})

  (http/register-endpoint
   context
   {:path prefix
    :method :get
    :middleware [gzip-middleware]
    :fn #'redirect-to-readme})

  (http/register-endpoint
   context
   {:path (utils/concat-urls prefix "/widgets")
    :method :get
    :middleware [gzip-middleware]
    :fn #'gitbok.markdown.widgets/widgets})

  (println "setup done!")
  (println "PORT env " (System/getenv "PORT"))
  (println "port " port)
  (println "version " (utils/slurp-resource "version"))

  (http/register-endpoint
   context
   {:path "/healthcheck"
    :method :get
    :fn #'healthcheck})

  (http/register-endpoint
   context
   {:path "/version"
    :method :get
    :fn #'version-endpoint})

  ;; Dev endpoints (only available when DEV=true)
  (when dev?
    (http/register-endpoint
     context
     {:path "/dev/page-info"
      :method :get
      :fn (fn [context request]
            (println "DEBUG: Full request object:" request)
            (println "DEBUG: Query params:" (:query-params request))
            (println "DEBUG: Params:" (:params request))
            (println "DEBUG: Query string:" (:query-string request))
            (let [uri (or (get-in request [:query-params :uri])
                          (get-in request [:params :uri])
                          (:uri request))]
              (println "DEBUG: /dev/page-info endpoint called with URI:" uri)
              (let [result (page-relocator/get-current-page-info context uri)]
                (println "DEBUG: get-current-page-info result:" result)
                {:status 200
                 :headers {"content-type" "application/json"}
                 :body (utils/->json result)})))})

    (http/register-endpoint
     context
     {:path "/dev/preview-url"
      :method :post
      :fn (fn [context request]
            (println "DEBUG: /dev/preview-url request body:" (:body request))
            (try
              (let [body-str (if (instance? java.io.InputStream (:body request))
                               (slurp (:body request))
                               (:body request))
                    _ (println "DEBUG: body string:" body-str)
                    body (utils/json->clj body-str)
                    _ (println "DEBUG: parsed body:" body)
                    new-file-path (:new-file-path body)
                    _ (println "DEBUG: new-file-path:" new-file-path)]
                {:status 200
                 :headers {"content-type" "application/json"}
                 :body (utils/->json {:new-url (page-relocator/preview-new-url context new-file-path)})})
              (catch Exception e
                (println "DEBUG: Error in /dev/preview-url:" (.getMessage e))
                {:status 500
                 :headers {"content-type" "application/json"}
                 :body (utils/->json {:error (.getMessage e)})})))})

    (http/register-endpoint
     context
     {:path "/dev/relocate-page"
      :method :post
      :fn (fn [context request]
            (println "DEBUG: /dev/relocate-page request body:" (:body request))
            (try
              (let [body-str (if (instance? java.io.InputStream (:body request))
                               (slurp (:body request))
                               (:body request))
                    _ (println "DEBUG: body string:" body-str)
                    body (utils/json->clj body-str)
                    _ (println "DEBUG: parsed body:" body)
                    current-filepath (:current-filepath body)
                    new-filepath (:new-filepath body)
                    _ (println "DEBUG: current-filepath:" current-filepath "new-filepath:" new-filepath)
                    result (page-relocator/relocate-page context current-filepath new-filepath)]
                {:status (if (:success result) 200 400)
                 :headers {"content-type" "application/json"}
                 :body (utils/->json result)})
              (catch Exception e
                (println "DEBUG: Error in /dev/relocate-page:" (.getMessage e))
                {:status 500
                 :headers {"content-type" "application/json"}
                 :body (utils/->json {:error (.getMessage e)})})))})

    ;; Enhanced API endpoints for F2 documentation reorganization
    (http/register-endpoint
     context
     {:path "/api/reorganize-docs"
      :method :post
      :middleware [form-parsing-middleware]
      :fn (fn [context request]
            (println "🎯 === API /api/reorganize-docs called ===")
            (println "🔍 === COMPREHENSIVE REQUEST DEBUG ===")
            (println "📋 Request keys:" (keys request))
            (println "📋 Method:" (:method request))
            (println "📋 URI:" (:uri request))
            (println "📋 Headers:" (:headers request))
            (println "📋 Content-Type:" (get-in request [:headers "content-type"]))
            (println "📋 Body type:" (type (:body request)))
            (println "📋 Body:" (:body request))

            ;; Try to read the body if it's an InputStream
            (let [body-content (when (:body request)
                                 (try
                                   (if (instance? java.io.InputStream (:body request))
                                     (slurp (:body request))
                                     (:body request))
                                   (catch Exception e
                                     (str "Error reading body: " (.getMessage e)))))]
              (println "📋 Body content:" body-content))

            (println "📋 Params:" (:params request))
            (println "📋 Form-params:" (:form-params request))
            (println "📋 Query-params:" (:query-params request))
            (println "📋 Route-params:" (:route-params request))
            (println "🔍 === END REQUEST DEBUG ===")

            (try
              (let [params (:params request)
                    form-params (:form-params request)
                    query-params (:query-params request)
                    action (or (:action params) (:action form-params) (:action query-params))
                    changes-str (or (:changes params) (:changes form-params) (:changes query-params))
                    timestamp (or (:timestamp params) (:timestamp form-params) (:timestamp query-params))
                    changes (when changes-str
                              (try
                                (utils/json->clj changes-str)
                                (catch Exception e
                                  (println "❌ Error parsing JSON changes:" (.getMessage e))
                                  nil)))]

                (println "DEBUG: reorganize action:" action)
                (println "DEBUG: changes:" changes)
                (println "DEBUG: timestamp:" timestamp)

                (cond
                  (nil? action)
                  {:status 400
                   :headers {"content-type" "application/json"}
                   :body (utils/->json {:error "Missing action parameter"
                                        :debug {:params params :form-params form-params}})}

                  (= action "move_document")
                  (if-let [move-info (:moveFile changes)]
                    (do
                      (println "🚀 Attempting to relocate page from" (:from move-info) "to" (:to move-info))
                      (let [result (page-relocator/relocate-page
                                    context
                                    (:from move-info)
                                    (:to move-info))]
                        (println "📋 Relocation result:" result)
                        {:status (if (:success result) 200 400)
                         :headers {"content-type" "application/json"}
                         :body (utils/->json (assoc result :timestamp timestamp))}))
                    {:status 400
                     :headers {"content-type" "application/json"}
                     :body (utils/->json {:error "No file move information provided"
                                          :debug {:changes changes}})})

                  (= action "rename_document")
                  (if-let [rename-info (:renameFile changes)]
                    (do
                      (println "🚀 Attempting to rename page from" (:from rename-info) "to" (:to rename-info))
                      (let [result (page-relocator/relocate-page
                                    context
                                    (:from rename-info)
                                    (:to rename-info))]
                        (println "📋 Rename result:" result)
                        {:status (if (:success result) 200 400)
                         :headers {"content-type" "application/json"}
                         :body (utils/->json (assoc result :timestamp timestamp))}))
                    {:status 400
                     :headers {"content-type" "application/json"}
                     :body (utils/->json {:error "No file rename information provided"
                                          :debug {:changes changes}})})

                  :else
                  {:status 400
                   :headers {"content-type" "application/json"}
                   :body (utils/->json {:error (str "Unknown action: " action)})}))

              (catch Exception e
                (println "❌ Error in /api/reorganize-docs:" (.getMessage e))
                (println "📋 Error stack trace:" (clojure.string/join "\n" (.getStackTrace e)))
                {:status 500
                 :headers {"content-type" "application/json"}
                 :body (utils/->json {:error (.getMessage e)
                                      :type "internal_server_error"})})))})

    (http/register-endpoint
     context
     {:path "/api/validate-move"
      :method :post
      :fn (fn [context request]
            (try
              (let [params (:params request)
                    form-params (:form-params request)
                    from-path (or (:from params) (:from form-params))
                    to-path (or (:to params) (:to form-params))]

                (println "DEBUG: validate-move from:" from-path "to:" to-path)

                (let [validation (page-relocator/validate-file-path to-path)]
                  {:status 200
                   :headers {"content-type" "application/json"}
                   :body (utils/->json validation)}))

              (catch Exception e
                (println "DEBUG: Error in /api/validate-move:" (.getMessage e))
                {:status 500
                 :headers {"content-type" "application/json"}
                 :body (utils/->json {:error (.getMessage e)})})))})

    (http/register-endpoint
     context
     {:path "/api/preview-structure"
      :method :post
      :fn (fn [context request]
            (try
              (let [params (:params request)
                    form-params (:form-params request)
                    changes-str (or (:changes params) (:changes form-params))
                    changes (when changes-str (utils/json->clj changes-str))]

                (println "DEBUG: preview-structure changes:" changes)

                ;; For now, just return a preview of what would happen
                (let [preview {:success true
                               :changes changes
                               :preview true
                               :message "This shows what would happen (actual implementation pending)"}]
                  {:status 200
                   :headers {"content-type" "application/json"}
                   :body (utils/->json preview)}))

              (catch Exception e
                (println "DEBUG: Error in /api/preview-structure:" (.getMessage e))
                {:status 500
                 :headers {"content-type" "application/json"}
                 :body (utils/->json {:error (.getMessage e)})})))}))

  {})

(defn -main [& _args]
  (.addShutdownHook (Runtime/getRuntime)
                    (Thread. #(println "Got SIGTERM.")))
  (println "Server started")
  (println "port " port)
  (system/start-system default-config))
