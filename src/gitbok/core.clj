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
   [ring.middleware.gzip :refer [wrap-gzip]]
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
       :section section
       :toc (right-toc/render-right-toc parsed)}
      (catch Exception e
        (println "cannot render file " filepath)
        {:content [:div {:role "alert"}
                   (.getMessage e)
                   [:pre (pr-str e)]
                   [:pre content*]]
         :toc nil}))))

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
     [:div
      (if (map? result)
        (:content result)
        result)]
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
           {:content
            {:status 404
             :body (not-found/not-found-view context uri-without-prefix)}
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

#_{:clj-kondo/ignore [:unresolved-symbol]}
(system/defstart
  [context config]
  (gitbok.http/set-port context port)
  (gitbok.http/set-prefix context prefix)
  (gitbok.http/set-base-url context base-url)

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
  (markdown/render-all!
   context
   (markdown/get-parsed-markdown-index context) read-markdown-file)
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
   {:path (utils/concat-urls prefix "/search")
    :method :get
    :middleware [gzip-middleware]
    :fn #'gitbok.ui.search/search-view})

  (http/register-endpoint
   context
   {:path (utils/concat-urls prefix "/search/results-only")
    :method :get
    :middleware [gzip-middleware]
    :fn (fn [context request]
          (let [result (gitbok.ui.search/search-results-only context request)]
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
   {:path (utils/concat-urls prefix "/toc/:path*")
    :method :get
    :middleware [gzip-middleware]
    :fn #'right-toc/get-toc-view})

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
  {})

(defn -main [& _args]
  (.addShutdownHook (Runtime/getRuntime)
                    (Thread. #(println "Got SIGTERM.")))
  (println "Server started")
  (println "port " port)
  (system/start-system default-config))
