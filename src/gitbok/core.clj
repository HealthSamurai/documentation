(ns gitbok.core
  (:require
   [cheshire.core]
   [clojure.string :as str]
   [gitbok.broken-links]
   [gitbok.markdown.core :as markdown]
   [gitbok.indexing.core :as indexing]
   [gitbok.indexing.impl.file-to-uri :as file-to-uri]
   [gitbok.indexing.impl.summary :as summary]
   [gitbok.indexing.impl.uri-to-file :as uri-to-file]
   [gitbok.search]
   [gitbok.ui]
   [gitbok.static]
   [http]
   [ring.util.response :as resp]
   [system]
   [uui]))

(set! *warn-on-reflection* true)

(defn read-content [_context filepath]
  (let [content (slurp filepath)]
    (if (str/starts-with? content "---")
      (last (str/split content #"---\n" 3))
      content)))

(defn read-and-render-file* [context uri]
  (let [filepath (str "." (indexing/uri->filepath (uri-to-file/get-idx context) uri))
        content* (read-content context filepath)
        {:keys [parsed]}
        (markdown/parse-markdown-content [filepath content*])]
    (try
      (markdown/render-gitbook context filepath parsed)
      (catch Exception e
        [:div {:role "alert"}
         (.getMessage e)
         [:pre (pr-str e)]
         [:pre content*]]))))

(defn read-and-render-file
  [context request]
  ;; todo do not read, get from idx
  [:div.gitbook
   (try
     (read-and-render-file* context
                            (:uri request))
     (catch Exception e
       [:div {:role "alert"}
        (.getMessage e)
        [:pre (pr-str e)]]))])

(defn picture-url? [url]
  (when url
    (str/includes? url ".gitbook/assets")))

(defn todo
  [context request]
  (gitbok.ui/layout
   context request
   [:div.gitbook
    (let [content* (slurp "./todo.md")
         parsed-md (:parsed (markdown/parse-markdown-content [nil content*]))]
     (try
       (markdown/render-gitbook context "todo.md" parsed-md)
       (catch Exception e
         [:div {:role "alert"}
          (.getMessage e)
          [:pre (pr-str e)]
          [:pre content*]])))]))

(defn
  ^{:http {:path "/:path*"}}
  render-file-view
  [context request]
  (cond
    (picture-url? (:uri request))
    (resp/file-response (subs (:uri request) 1))

    ;; todo
    (= (:uri request) "/favicon.ico")
    {:status 404}

    :else
    (gitbok.ui/layout
     context request
     (read-and-render-file context request))))

(def readme-path "readme")

(defn
  ^{:http {:path "/"}}
  redirect-to-readme
  [context request]
  (let [request
        (update request :uri
                #(if (= "/" %) readme-path %))]
    (render-file-view context request)))

(defn
  handle-gitbook-assets
  [_context request]
  (resp/file-response
   (str/replace (:uri request)
                #"^/pictures/"
                ".gitbook/assets/")))

(system/defmanifest
  {:description "gitbok"
   :deps ["http"]
   :config {:history {:type "boolean"}}})

#_{:clj-kondo/ignore [:unresolved-symbol]}
(system/defstart
  [context config]
  (http/register-ns-endpoints context *ns*)
  (http/register-endpoint
   context
   {:path "/:path*"
    :method :get
    :fn #'render-file-view})

  (http/register-endpoint
   context
   {:path "/"
    :method :get
    :fn #'redirect-to-readme})

  (http/register-endpoint
   context
   {:path "/todo"
    :method :get
    :fn #'todo})

  ;; (http/register-endpoint
  ;;  context
  ;;  {:path "/pictures/:path"
  ;;   :method :get
  ;;   :fn #'handle-gitbook-assets})

  ;; todo
  ;; (http/register-endpoint
  ;;   context
  ;;   {:path "/admin/broken" :method :get :fn #'gitbok.broken-links/broken-links-view})

  (indexing/set-md-files-idx context)

  (markdown/set-parsed-markdown-index
   context
   (indexing/get-md-files-idx context))

  (indexing/set-search-idx
   context
   (markdown/get-parsed-markdown-index context))

  ;; todo reuse md-files-idx
  (uri-to-file/set-idx context)
  (file-to-uri/set-idx context)
  (def file1 (file-to-uri/get-idx context))
  (take 10 file1)

  (summary/set-summary context)
  (http/register-endpoint
   context
   {:path "/search" :method
    :get :fn #'gitbok.search/search-view})

  (http/register-endpoint
   context
   {:path "/search/results"
    :method :get
    :fn #'gitbok.search/search-results-view})

  (println "setup done!")
  {})

(def default-config
  {:services ["http" "uui" "gitbok.core"]
   :http {:port 8081}})
