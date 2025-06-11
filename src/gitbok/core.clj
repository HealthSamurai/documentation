(ns gitbok.core
  (:gen-class)
  (:require
   [cheshire.core]
   [gitbok.constants :as const]
   [clojure.string :as str]
   [gitbok.broken-links]
   [gitbok.markdown.core :as markdown]
   [gitbok.indexing.core :as indexing]
   [gitbok.indexing.impl.file-to-uri :as file-to-uri]
   [gitbok.markdown.widgets.big-links :as big-links]
   [gitbok.indexing.impl.summary :as summary]
   [gitbok.indexing.impl.uri-to-file :as uri-to-file]
   [gitbok.search]
   [gitbok.ui]
   [http]
   [clojure.java.io :as io]
   [ring.util.response :as resp]
   [system]
   [gitbok.utils :as utils]
   [uui]))

(set! *warn-on-reflection* true)

(def dev? (= "true" (System/getenv "DEV")))

(defn read-content [_context filepath]
  (let [content (slurp (io/resource filepath))]
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

(defn find-children-files [context filepath]
  (when
   (and filepath
        (str/ends-with? (str/lower-case filepath) "readme.md"))
    (let [index (file-to-uri/get-idx context)
          filepath (if (str/ends-with? filepath "/")
                     (subs filepath 0 (dec (count filepath)))
                     filepath)
          filepath
          (if (str/starts-with? filepath "./docs/")
            (subs filepath 7)
            filepath)
          dir (.getParent (io/file filepath))]
      (filterv
       (fn [[file _info]]
         (and
          (str/starts-with? file dir)
          (not= file filepath)
          (or
           (= dir (.getParent (io/file file)))
           (and
            (= dir (.getParent (io/file (.getParent (io/file file)))))
            (str/ends-with? (str/lower-case file) "readme.md")))))
       index))))

(defn render-empty-page [context filepath parsed-heading]
  [:div [:h1 (-> parsed-heading :content first :text)]
   (for [[_path {:keys [title uri]}]
         (find-children-files context filepath)]
     (big-links/big-link-view (str "/" uri) title))])

(defn render-file* [context filepath parsed]
  (if (and (= 1 (count (:content parsed)))
           (= :heading (:type (first (:content parsed)))))
    (render-empty-page context filepath (first (:content parsed)))
    (markdown/render-gitbook context filepath parsed)))

(defn read-markdown-file [context filepath]
  (let [content* (read-content context filepath)
        {:keys [parsed]}
        (markdown/parse-markdown-content context [filepath content*])]
    (try
      (render-file* context filepath parsed)
      (catch Exception e
        [:div {:role "alert"}
         (.getMessage e)
         [:pre (pr-str e)]
         [:pre content*]]))))

(defn picture-url? [url]
  (when url
    (str/includes? url ".gitbook/assets")))

(defn render-all! [context parsed-md-index]
  (system/set-system-state
   context
   [const/RENDERED]
   (->> parsed-md-index
        (mapv
         (fn [{:keys [filepath parsed]}]
           (println "render filepath " filepath)
           [filepath (render-file* context filepath parsed)]))
        (into {}))))

(defn get-rendered [context filepath]
  (get (system/get-system-state context [const/RENDERED]) filepath))

(defn render-file
  [context uri]
  [:div.gitbook
   (try
     (let [uri (:uri uri)
           filepath (indexing/uri->filepath context uri)]
       (if dev?
         (read-markdown-file context filepath)
         (get-rendered context filepath)))
     (catch Exception e
       [:div {:role "alert"}
        (.getMessage e)
        [:pre (pr-str e)]]))])

(defn
  ^{:http {:path "/:path*"}}
  render-file-view
  [context request]
  (cond
    (picture-url? (:uri request))
    (resp/resource-response
     (subs (str/replace (:uri request)
                        #"%20" " ")
           10))

    ;; todo
    (= (:uri request) "/favicon.ico")
    {:status 404}

    :else
    (let [filepath (indexing/uri->filepath context (:uri request))]
      (if filepath
        (gitbok.ui/layout
         context request
         (render-file context request))

        (gitbok.ui/layout
         context request
         {:status 404
          :body (not-found-view context (:uri request))})))))

(def readme-path "readme")

(defn
  ^{:http {:path "/"}}
  redirect-to-readme
  [context request]
  (let [request
        (update request :uri
                #(if (= "/" %) readme-path %))]
    (render-file-view context request)))

(defn healthcheck
  [context request]
  {:status 200 :body {:status "ok"}})

(system/defmanifest
  {:description "gitbok"
   :deps ["http"]
   :config {:history {:type "boolean"}}})

#_{:clj-kondo/ignore [:unresolved-symbol]}
(system/defstart
  [context config]
  ;; (http/register-ns-endpoints context *ns*)

  ;; order is important
  ; 1. read summary. create toc htmx.
  (summary/set-summary context)
  ; 2. get uris from summary (using slugging), merge with redirects
  (uri-to-file/set-idx context)
  ; 3. reverse file to uri idx
  (file-to-uri/set-idx context)
  (def ftu (file-to-uri/get-idx context))
  (take 10 ftu)
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
  (render-all!
   context
   (markdown/get-parsed-markdown-index context))

  (http/register-endpoint
   context
   {:path "/search" :method
    :get :fn #'gitbok.search/search-view})

  (http/register-endpoint
   context
   {:path "/search/results"
    :method :get
    :fn #'gitbok.search/search-results-view})

  (http/register-endpoint
   context
   {:path "/healthcheck" :method
    :get :fn #'healthcheck})

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

  (println "setup done!")
  {})

(def default-port 8081)
(def default-config
  {:services ["http" "uui" "gitbok.core"]
   :http {:port default-port}})

(defn -main [& args]
  (let [p (System/getenv "PORT")
        port (or
              (when p
                (try (Integer/parseInt p)
                     (catch Exception _ nil)))
              default-port)]
    (println "Server started")
    (println "port " port)
    (system/start-system default-config)))
