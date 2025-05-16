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
   [gitbok.ui]
   [gitbok.static]
   [http]
   [ring.util.response :as resp]
   [system]
   [uui]))

(set! *warn-on-reflection* true)

(defn read-content [context uri]
  (let [filepath (indexing/uri->filepath (uri-to-file/get-idx context) uri)
        content (slurp (str "." filepath)) ]
    (if (str/starts-with? content "---")
      (last (str/split content #"---\n" 3))
      content)))

(defn read-and-render-file* [context uri]
  (let [content* (read-content context uri)]
    (try
      (markdown/render-gitbook content*)
      (catch Exception e
        [:div {:role "alert"}
         (.getMessage e)
         [:pre (pr-str e)]
         [:pre content*]]))))

(defn picture? [request]
  (str/includes? (:uri request) ".gitbook"))

(defn render-picture [request]
  (->> #"\.gitbook"
       (str/split (:uri request))
       second
       http/url-decode
       (str "./docs/.gitbook")
       resp/file-response))

(defn read-and-render-file
  [context request]
  [:div.gitbook
   (try
     (read-and-render-file* context (:uri request))
     (catch Exception e
       [:div {:role "alert"}
        (.getMessage e)
        [:pre (pr-str e)]]))])

(defn
  ^{:http {:path "/:path*"}}
  render-file-view
  [context request]
  (cond
    (picture? request)
    (render-picture request)

    :else
    (gitbok.ui/layout context request (read-and-render-file context request))))

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
    {:path "/" :method :get :fn #'render-file-view})
  (http/register-endpoint context
                          {:path "/admin/broken" :method :get :fn #'gitbok.broken-links/broken-links-view})

  ;; Register static file handlers
  (gitbok.static/register-endpoints context)

  ;; Set up indexing
  (uri-to-file/set-idx context)
  (file-to-uri/set-idx context)
  (summary/set-summary context)
  {})

(def default-config
  {:services ["http" "uui" "gitbok.core"]
   :http {:port 8081}})
