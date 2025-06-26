(ns gitbok.core
  (:require
   [gitbok.constants :as const]
   [gitbok.indexing.impl.sitemap :as sitemap]
   [edamame.core :as edamame]
   [clojure.string :as str]
   [cheshire.core :as json]
   [hiccup2.core]
   [gitbok.markdown.core :as markdown]
   [gitbok.indexing.core :as indexing]
   [gitbok.indexing.impl.file-to-uri :as file-to-uri]
   [gitbok.markdown.widgets.big-links :as big-links]
   [gitbok.markdown.widgets.headers :as headers]
   [gitbok.indexing.impl.summary :as summary]
   [gitbok.indexing.impl.uri-to-file :as uri-to-file]
   [gitbok.search]
   [uui.heroicons :as ico]
   [http]
   [clojure.java.io :as io]
   [ring.util.response :as resp]
   [system]
   [gitbok.utils :as utils]
   [uui])
  (:gen-class))

(set! *warn-on-reflection* true)
(def base-url
  (or (System/getenv "BASE_URL")
      "https://gitbok.cs.aidbox.dev"))

(def dev? (= "true" (System/getenv "DEV")))

(defn read-content [_context filepath]
  (utils/slurp-resource filepath))

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

(defn render-empty-page [context filepath title]
  [:div
   (headers/render-h1
     (markdown/renderers context filepath) title)
   (for [[_path {:keys [title uri]}]
         (find-children-files context filepath)]
     (big-links/big-link-view (str "/" uri) title))])

(defn render-file* [context filepath parsed title]
  [:div {:class "flex-1 min-w-0 max-w-4xl"}
   (if (= 1 (count (:content parsed)))
     (render-empty-page context filepath title)
     (markdown/render-md context filepath parsed))])

(defn render-toc [parsed]
  (when (:toc parsed)
    [:div {:class "toc-container sticky top-16 h-[calc(100vh-4rem)] overflow-y-auto p-6 bg-white w-60 rounded-lg z-50"}
     [:div {:class "toc w-full max-w-full"}
      (for [item (-> parsed :toc :children first :children)]
        (markdown/render-toc-item item))]]))

(defn read-markdown-file [context filepath]
  (let [content* (read-content context filepath)
        {:keys [parsed description title]}
        (markdown/parse-markdown-content context [filepath content*])]
    (try
      {:content (render-file* context filepath parsed title)
       :description (or description
                        (if (>= (count content*) 150)
                          (subs content* 0 150)
                          content*))
       :toc (render-toc parsed)}
      (catch Exception e
        {:content [:div {:role "alert"}
                   (.getMessage e)
                   [:pre (pr-str e)]
                   [:pre content*]]
         :toc nil}))))

(defn picture-url? [url]
  (when url
    (str/starts-with? url "/.gitbook/assets")))

(defn render-all! [context parsed-md-index]
  (system/set-system-state
   context
   [const/RENDERED]
   (->> parsed-md-index
        (mapv
         (fn [{:keys [filepath _parsed]}]
           (println "render filepath " filepath)
           [filepath (read-markdown-file context filepath)]))
        (into {}))))

(defn get-rendered [context filepath]
  (get (system/get-system-state context [const/RENDERED]) filepath))

(defn render-file
  [context filepath]
  (let [result
        (try
          (if dev?
            (read-markdown-file context filepath)
            (get-rendered context filepath))
          (catch Exception e
            [:div {:role "alert"}
             (.getMessage e)
             [:pre (pr-str e)]]))]
    {:content
     [:div
      (if (map? result)
        (:content result)
        result)]
     :description (:description result)}))

(defn add-active-class [item add?]
  (let [link-element (:title item)
        current-class (get-in link-element [1 :class] "")
        active-class (if add? " active" "")
        updated-class (str current-class active-class)]
    (assoc-in link-element [1 :class] updated-class)))

(defn render-menu [url item]
  (let [open? (str/starts-with? url (:href item))]
    (if (:children item)
      [:details (when open? {:open ""})
       [:summary {:class "flex items-center justify-between font-medium text-gray-900 hover:bg-gray-100 transition-colors duration-200 cursor-pointer group"}
        [:div {:class "flex-1 clickable-summary"}
         (add-active-class item (= url (:href item)))]
        (ico/chevron-right "chevron size-5 text-gray-400 group-hover:text-primary-9 transition-colors duration-200")]
       [:div {:class "border-l border-gray-200 ml-4"}
        (for [c (:children item)]
          (render-menu url c))]]
      (add-active-class item open?))))

(defn menu [summary url]
  [:div#navigation {:class "w-[17.5rem] flex-shrink-0 sticky top-16 h-[calc(100vh-4rem)] overflow-y-auto py-4 bg-white"}
   (for [item summary]
     [:div {:class "break-words"}
      (when-not
       (str/blank? (:title item))
        [:div {:class "mt-4 mb-2 mx-4"}
         [:b (:title item)]])
      (for [ch (:children item)]
        (render-menu url ch))])
   [:div "version " (utils/slurp-resource "version")]])

(defn nav []
  [:div {:class "w-full bg-white border-b border-gray-200 flex-shrink-0 sticky top-0 z-50"}
   [:div {:class "flex items-center justify-between py-3 min-h-16 px-4 sm:px-6 md:px-8 max-w-screen-2xl mx-auto"}
    [:div {:class "flex max-w-full lg:basis-72 min-w-0 shrink items-center justify-start gap-2 lg:gap-4"}
     [:button {:class "mobile-menu-button md:hidden"
               :onclick "toggleMobileMenu()"
               :type "button"
               :aria-label "Toggle mobile menu"}
      [:svg {:class "size-6" :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
       [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5"}]]]

     [:a {:href "/" :class "group/headerlogo min-w-0 shrink flex items-center"}
      [:img {:alt "Aidbox Logo"
             :class "block object-contain size-8"
             :src "/.gitbook/assets/aidbox_logo.jpg"}]
      [:div {:class "text-pretty line-clamp-2 tracking-tight max-w-[18ch] lg:max-w-[24ch] font-semibold ms-3 text-base/tight lg:text-lg/tight text-gray-900"}
       "Aidbox User Docs"]]]

    [:div {:class "flex items-center gap-4"}
     [:div {:class "hidden md:flex items-center gap-4"}
      [:a {:href "/getting-started/run-aidbox-locally"
           :class "text-gray-700 hover:text-primary-9 transition-colors duration-200 no-underline"}
       "Run Aidbox locally"]
      [:a {:href "/getting-started/run-aidbox-in-sandbox"
           :class "text-gray-700 hover:text-primary-9 transition-colors duration-200 no-underline"}
       "Run Aidbox in Sandbox"]
      [:a {:href "https://bit.ly/3R7eLke"
           :target "_blank"
           :class "text-gray-700 hover:text-primary-9 transition-colors duration-200 no-underline"}
       "Talk to us"]
      [:a {:href "https://connect.health-samurai.io/"
           :target "_blank"
           :class "text-gray-700 hover:text-primary-9 transition-colors duration-200 no-underline"}
       "Ask community"]]

     [:a {:href "/search"
          :class "flex items-center gap-2 px-3 py-2 bg-gray-100 border border-gray-300 rounded-md text-gray-700 text-sm transition-all duration-200 hover:bg-gray-200 hover:border-gray-400"
          :id "search-link"
          :hx-get "/search"
          :hx-target "#content"
          :hx-swap "innerHTML"
          :hx-push-url "true"
          :hx-on ":after-request \"document.querySelector('#search-input')?.focus()\""}
      [:svg {:class "size-4" :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
       [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607Z"}]]

      [:span {:class "text-xs text-gray-400"} "⌘K"]]]]])

(defn navigation-buttons [context uri]
  (let [[[prev-page-url prev-page-title] [next-page-url next-page-title]]
        (summary/get-prev-next-pages context uri)]
    [:div {:class "flex flex-col sm:flex-row justify-between items-center mt-8 pt-4 gap-4"}
     (when prev-page-url
       [:div {:class "flex-1 w-full sm:w-auto"}
        [:a {:href prev-page-url
             :hx-target "#content"
             :hx-push-url "true"
             :hx-swap "outerHTML"
             :class "group text-sm p-2.5 flex gap-4 flex-1 flex-row-reverse items-center pl-4 border border-gray-300 rounded hover:border-orange-500 text-pretty md:p-4 md:text-base"}
         [:span {:class "flex flex-col flex-1 text-right"}
          [:span {:class "text-xs text-gray-500"} "Previous"]
          [:span {:class "text-gray-700 group-hover:text-orange-600 line-clamp-2"} prev-page-title]]
         [:svg {:class "size-4 text-gray-400 group-hover:text-orange-600"
                :fill "none"
                :stroke "currentColor"
                :viewBox "0 0 24 24"
                :stroke-width "2"}
          [:path {:stroke-linecap "round"
                  :stroke-linejoin "round"
                  :d "M15 19l-7-7 7-7"}]]]])
     (when next-page-url
       [:div {:class "flex-1 w-full sm:w-auto text-left"}
        [:a {:href next-page-url
             :hx-target "#content"
             :hx-push-url "true"
             :hx-swap "outerHTML"
             :class "group text-sm p-2.5 flex gap-4 flex-1 items-center pr-4 border border-gray-300 rounded hover:border-orange-500 text-pretty md:p-4 md:text-base"}
         [:span {:class "flex flex-col flex-1"}
          [:span {:class "text-xs text-gray-500"} "Next"]
          [:span {:class "text-gray-700 group-hover:text-orange-600 line-clamp-2"} next-page-title]]
         [:svg {:class "size-4 text-gray-400 group-hover:text-orange-600"
                :fill "none"
                :stroke "currentColor"
                :viewBox "0 0 24 24"
                :stroke-width "2"}
          [:path {:stroke-linecap "round"
                  :stroke-linejoin "round"
                  :d "M9 5l7 7-7 7"}]]]])]))

(defn content-div [context uri content]
  [:main#content {:class "flex-1 py-6 max-w-6xl min-w-0 overflow-x-hidden"}
   [:script "hljs.highlightAll();"]
   [:script "window.scrollTo(0, 0);"]
   [:div {:class "mx-auto px-2 max-w-full"} content]
   (navigation-buttons context uri)])

(defn get-toc [context filepath]
  (let [rendered (get-rendered context filepath)]
    (if (map? rendered)
      (:toc rendered)
      nil)))

(defn layout-view [context content uri]
  [:div
   (nav)
   [:div
    {:class "flex px-4 sm:px-6 md:px-8 max-w-screen-2xl mx-auto site-full-width:max-w-full gap-20"}
    (menu (summary/get-summary context) uri)
    [:div {:class "flex-1"}
     (content-div context uri content)]
    (when-let [filepath (indexing/uri->filepath context uri)]
      (get-toc context filepath))]])

(defn response1 [body status]
  {:status (or status 200)
   :headers {"content-type" "text/html; ; charset=utf-8"}
   :body (str "<!DOCTYPE html>\n"
              (hiccup2.core/html body))})

(defn document [body title description page-url open-graph-image]
  [:html {:lang "en"}
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
    [:meta {:name "description" :content description}]
    [:meta {:property "og:title" :content title}]
    [:meta {:property "og:description" :content description}]
    [:meta {:property "og:url" :content page-url}]
    [:meta {:property "og:type" :content "article"}]
    [:meta {:property "og:image" :content open-graph-image}]
    [:script {:type "application/ld+json" }
     (uui/raw
       (json/generate-string
         {"@context" "https://schema.org"

          "@type" "TechArticle"
          "headline" title
          "description" description
          "author" { "@type" "Organization", "name" "HealthSamurai"}}))]
    [:title (str title " | Aidbox User Docs")]
    [:link {:rel "canonical" :href page-url}]
    [:link {:rel "stylesheet", :href "/static/app.min.css"}]
    [:script {:src "/static/htmx.min.js"}]
    [:meta {:name "htmx-config",
            :content "{\"scrollIntoViewOnBoost\":false,\"scrollBehavior\":\"smooth\"}"}]
    [:script {:src "/static/app.js"}]
    [:script {:src "/static/toc-scroll.js"}]
    [:script {:src "/static/tabs.js"}]
    [:script {:src "/static/toc.js"}]
    [:script {:src "/static/mobile-menu.js"}]
    [:script {:src "/static/keyboard-navigation.js"}]
    [:link {:rel "stylesheet" :href "/static/github.min.css"}]
    [:script {:src "/static/highlight.min.js"}]
    [:script {:src "/static/highlightjs-line-numbers.min.js"}]
    [:script {:src "/static/json.min.js"}]
    [:script {:src "/static/bash.min.js"}]
    [:script {:src "/static/yaml.min.js"}]
    [:script {:src "/static/json.min.js"}]
    [:script {:src "/static/http.min.js"}]
    [:script {:src "/static/graphql.min.js"}]
    [:script "hljs.highlightAll();"]
    [:script
     (uui/raw
       "!function(t,e){var o,n,p,r;e.__SV||(window.posthog=e,e._i=[],e.init=function(i,s,a){function g(t,e){var o=e.split(\".\");2==o.length&&(t=t[o[0]],e=o[1]),t[e]=function(){t.push([e].concat(Array.prototype.slice.call(arguments,0)))}}(p=t.createElement(\"script\")).type=\"text/javascript\",p.crossOrigin=\"anonymous\",p.async=!0,p.src=s.api_host.replace(\".i.posthog.com\",\"-assets.i.posthog.com\")+\"/static/array.js\",(r=t.getElementsByTagName(\"script\")[0]).parentNode.insertBefore(p,r);var u=e;for(void 0!==a?u=e[a]=[]:a=\"posthog\",u.people=u.people||[],u.toString=function(t){var e=\"posthog\";return\"posthog\"!==a&&(e+=\".\"+a),t||(e+=\" (stub)\"),e},u.people.toString=function(){return u.toString(1)+\".people (stub)\"},o=\"init Ie Ts Ms Ee Es Rs capture Ge calculateEventProperties Os register register_once register_for_session unregister unregister_for_session js getFeatureFlag getFeatureFlagPayload isFeatureEnabled reloadFeatureFlags updateEarlyAccessFeatureEnrollment getEarlyAccessFeatures on onFeatureFlags onSurveysLoaded onSessionId getSurveys getActiveMatchingSurveys renderSurvey canRenderSurvey canRenderSurveyAsync identify setPersonProperties group resetGroups setPersonPropertiesForFlags resetPersonPropertiesForFlags setGroupPropertiesForFlags resetGroupPropertiesForFlags reset get_distinct_id getGroups get_session_id get_session_replay_url alias set_config startSessionRecording stopSessionRecording sessionRecordingStarted captureException loadToolbar get_property getSessionProperty Ds Fs createPersonProfile Ls Ps opt_in_capturing opt_out_capturing has_opted_in_capturing has_opted_out_capturing clear_opt_in_out_capturing Cs debug I As getPageViewId captureTraceFeedback captureTraceMetric\".split(\" \"),n=0;n<o.length;n++)g(u,o[n]);e._i.push([i,s,a])},e.__SV=1)}(document,window.posthog||[]);
       posthog.init('phc_uO4ImMUxOljaWPDRr7lWu9TYpBrpIs4R1RwLu8uLRmx', {
                                                                        api_host: 'https://ph.aidbox.app',
                                                                        defaults: '2025-05-24',
                                                                        person_profiles: 'identified_only', // or 'always' to create profiles for anonymous users as well
                                                                        })")]]
   [:body {:hx-boost "true"
           :hx-on "htmx:afterSwap: window.scrollTo(0, 0); hljs.highlightAll();"}
    body]])

(defn layout [context request content title description]
  (let [body (if (map? content) (:body content) content)
        status (if (map? content) (:status content 200) 200)
        hx-current-url (get-in request [:headers "hx-current-url"])
        uri (or (when hx-current-url
                  (if (str/includes? hx-current-url "://")
                    (second (str/split hx-current-url #"://[^/]+"))
                    hx-current-url))
                (:uri request))
        is-hx-target (uui/hx-target request)]
    (response1
     (cond
       is-hx-target
       (content-div context uri body)

       :else
       (document
        (layout-view context body uri)
        title
        description
        (str base-url uri)
        (str base-url "/.gitbook/assets/aidbox_logo.png")))
     status)))

(defn
  ^{:http {:path "/:path*"}}
  render-file-view
  [context request]
  (let [uri (:uri request)]

    (cond
      (= uri "/favicon.ico")
      (resp/resource-response
       "public/favicon.ico")

      (picture-url? uri)
      (resp/resource-response
       (subs (str/replace uri #"%20" " ")
             10))

      :else
      (let [filepath (indexing/uri->filepath context uri)
            title (:title (get (indexing/file->uri-idx context) filepath))
            {:keys [description content]} (render-file context filepath)]
        (if filepath
          (layout
           context request
           content
           title description)

          (layout
           context request
           {:status 404
            :body (not-found-view context uri)}
           "Not found"
           "Page not found"))))))

(defn sitemap-xml
  [context _]
  {:status 200
   :headers {"content-type" "application/xml"}
   :body (sitemap/get-sitemap context)})

(defn sitemap-pages-xml
  [context _]
  {:status 200
   :headers {"content-type" "application/xml"}
   :body (sitemap/get-sitemap-pages context)})

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

(defn
  ^{:http {:path "/search"}}
  search-view
  [context request]
  (layout
   context request
   [:div.flex.flex-col.items-center.min-h-screen.bg-gray-50.p-4
    [:div.w-full.max-w-2xl.mt-8
     [:div.relative
      [:input#search-input.w-full.px-4.py-3.text-lg.rounded-lg.border.border-gray-300.shadow-sm.focus:outline-none.focus:ring-2.focus:ring-blue-500.focus:border-transparent
       {:type "text"
        :name "q"
        :placeholder "Search documentation..."
        :hx-get "/search/results"
        :hx-trigger "keyup changed delay:500ms, search"
        :hx-target "#search-results"
        :hx-indicator ".htmx-indicator"}]
      [:div.htmx-indicator.absolute.right-3.top-3
       [:div.animate-spin.rounded-full.h-6.w-6.border-b-2.border-blue-500]]]
     [:div#search-results.mt-4.space-y-4]]] "Search" "Search results"))

(defn
  ^{:http {:path "/search/results"}}
  search-results-view
  [context request]
  (let [query (get-in request [:query-params :q])
        results (gitbok.search/search context query)
        results
        (mapv
         (fn [res]
           (assoc res :uri
                  (indexing/filepath->uri context (-> res :hit :filepath))))
         results)]
    (layout
     context request
     [:div.space-y-4
      (if (empty? results)
        [:div.text-gray-500.text-center.py-4 "No results found"]
        (for [result results]
          (gitbok.search/page-view result)))] "Search results" "Search results")))

#_{:clj-kondo/ignore [:unresolved-symbol]}
(system/defstart
  [context config]

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
  (render-all!
   context
   (markdown/get-parsed-markdown-index context))
  (println "generating sitemap.xml")
  ;; 8. generate sitemap.xml
  (sitemap/set-sitemap context base-url)
  (sitemap/set-sitemap-pages
   context
   base-url
   (edamame/parse-string (utils/slurp-resource "lastmod.edn")))

  (http/register-endpoint
   context
   {:path "/search" :method
    :get :fn #'search-view})

  (http/register-endpoint
   context
   {:path "/search/results"
    :method :get
    :fn #'search-results-view})

  (http/register-endpoint
   context
   {:path "/healthcheck" :method
    :get :fn #'healthcheck})

  (http/register-endpoint
   context
   {:path "/sitemap.xml"
    :method :get
    :fn #'sitemap-xml})

  (http/register-endpoint
   context
   {:path "/sitemap-pages.xml"
    :method :get
    :fn #'sitemap-pages-xml})

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

  (println "PORT env " (System/getenv "PORT"))
  (println "port " port)
  (println "version " (utils/slurp-resource "version"))
  {})

(defn -main [& _args]
  (println "Server started")
  (println "port " port)
  (system/start-system default-config))
