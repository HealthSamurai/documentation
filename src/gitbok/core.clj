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
   [gitbok.utils :as utils]
   [uui]))

(set! *warn-on-reflection* true)

(defn read-content [_context filepath]
  (let [content (slurp filepath)]
    (if (str/starts-with? content "---")
      (last (str/split content #"---\n" 3))
      content)))

(defn not-found-view [context uri]
  (let [search-term (last (str/split uri #"/"))
        search-results (gitbok.search/search context search-term)]
    [:div.min-h-screen.flex.items-center.justify-center
     [:div.max-w-2xl.w-full.px-4
      [:div
       [:h2.mt-4.text-3xl.font-semibold.text-gray-700.text-center "Page not found"]
       (when (seq search-results)
         [:div.mt-8
          [:h3.text-lg.font-medium.text-gray-900 "You might be looking for:"]
          [:ul.mt-4.space-y-2.text-left
           (for [search-res (take 5 (utils/distinct-by #(-> % :hit :title) search-results))]
             [:li
              [:a.text-blue-600.hover:text-blue-800.text-lg.flex.items-start
               {:href (file-to-uri/filepath->uri
                        context (:filepath (:hit search-res)))}
               (:title (:hit search-res))]])]])]]]))

(defn read-and-render-file* [context uri]
  (let [filepath (indexing/uri->filepath context uri)]
    (if filepath
      (let [filepath (str "." (indexing/uri->filepath context uri))
            content* (read-content context filepath)
            {:keys [parsed]}
            (markdown/parse-markdown-content [filepath content*])]
        (try
          (markdown/render-gitbook context filepath parsed)
          (catch Exception e
            [:div {:role "alert"}
             (.getMessage e)
             [:pre (pr-str e)]
             [:pre content*]])))
      (not-found-view context uri))))

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
