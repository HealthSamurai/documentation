(ns gitbok.ui.layout
  (:require
   [gitbok.indexing.impl.summary :as summary]
   [gitbok.ui.main-content :as main-content]
   [gitbok.ui.main-navigation :as main-navigation]
   [gitbok.ui.left-navigation :as left-navigation]
   [gitbok.indexing.core :as indexing]
   [gitbok.http]
   [gitbok.products :as products]
   [cheshire.core :as json]
   [uui]
   [system]
   [clojure.string :as str]
   [gitbok.utils :as utils]))

(defn layout-view [context body uri filepath hide-breadcrumb]
  [:div
   (main-navigation/nav context)
   [:div.mobile-menu-overlay]
   [:div
    {:class "flex max-w-screen-2xl mx-auto site-full-width:max-w-full
     items-start overflow-visible md:px-8"}
    (left-navigation/left-navigation
     (summary/get-summary context)
     uri)
    (main-content/content-div context uri body filepath false hide-breadcrumb)]])

(defn document [context body {:keys [title description canonical-url og-preview lastmod favicon-url section]}]
  (let [version (gitbok.http/get-version context)
        version-param (when version (str "?v=" version))]
    [:html {:lang "en"}
     [:head
      (uui/raw "<!-- Google Tag Manager -->")
      [:script
       (uui/raw
        "(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
})(window,document,'script','dataLayer','GTM-PMS5LG2');")]
      (uui/raw "<!-- End Google Tag Manager -->")
      [:script
       (uui/raw "
(function(w, d) { w.PushEngage = w.PushEngage || []; w._peq = w._peq || []; PushEngage.push(['init', { appId: '795c4eea-7a69-42d7-bff3-882774303fcf' }]); var e = d.createElement('script'); e.src = 'https://clientcdn.pushengage.com/sdks/pushengage-web-sdk.js'; e.async = true; e.type = 'text/javascript'; d.head.appendChild(e); })(window, document);")]
      [:meta {:charset "utf-8"}]
      [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
      [:meta {:name "description" :content description}]
      [:meta {:property "og:title" :content title}]
      [:meta {:property "og:site_name" :content (:name (products/get-current-product context))}]
      [:meta {:property "article:author" :content "Health Samurai"}]
      [:meta {:property "article:modified_time" :content lastmod}]
      [:meta {:property "article:published_time" :content ""}]
      [:meta {:property "og:description" :content description}]
      [:meta {:property "og:url" :content canonical-url}]
      [:meta {:property "og:type" :content "article"}]
      [:meta {:property "og:locale" :content "en_US"}]
      [:meta {:property "og:image" :content og-preview}]
      [:meta {:property "telegram:channel" :content ""}]
      [:meta {:property "tg:site_verification" :content "g7j8/rPFXfhyrq5q0QQV7EsYWv4="}]

      [:meta {:name "twitter:card" :content "summary_large_image"}]
      [:meta {:name "twitter:title" :content title}]
      [:meta {:name "twitter:description" :content description}]
      [:meta {:name "twitter:image" :content og-preview}]
      [:meta {:name "twitter:site" :content "@health_samurai"}]
      [:meta {:name "robots" :content "index, follow"}]

      [:meta {:name "htmx-config",
              :content "{\"scrollIntoViewOnBoost\":false,\"scrollBehavior\":\"smooth\",\"allowEval\":false,\"historyCacheSize\":0,\"historyEnabled\":true,\"refreshOnHistoryMiss\":false}"}]
      (when section
        [:meta {:name "scroll-to-id" :content section}])
      [:link {:rel "icon" :type "image/x-icon" :href favicon-url}]
      [:link {:rel "shortcut icon" :type "image/x-icon" :href favicon-url}]
      [:link {:rel "apple-touch-icon" :href favicon-url}]
      [:link {:rel "canonical" :href canonical-url}]

      [:script {:type "application/ld+json"}
       (uui/raw
        (json/generate-string
         {"@context" "https://schema.org"

          "@type" "TechArticle"
          "headline" title
          "description" description
          "author" {"@type" "Organization", "name" "HealthSamurai"}}))]
      [:title (str title " | " (:name (products/get-current-product context)))]

      [:link {:rel "stylesheet", :href (str (gitbok.http/get-prefixed-url context "/static/app.min.css") version-param)}]

      ;; Critical scripts - load first
      [:script {:src (gitbok.http/get-prefixed-url context "/static/htmx.min.js")}]

      ;; Syntax highlighting - all deferred
      [:link {:rel "stylesheet" :href (gitbok.http/get-prefixed-url context "/static/github.min.css")}]
      [:link {:rel "stylesheet" :href (gitbok.http/get-prefixed-url context "/static/github-dark.min.css")
              :disabled true}]
      [:script {:src (gitbok.http/get-prefixed-url context "/static/highlight.min.js") :defer true}]
      [:script {:src (gitbok.http/get-prefixed-url context "/static/json.min.js") :defer true}]
      [:script {:src (gitbok.http/get-prefixed-url context "/static/bash.min.js") :defer true}]
      [:script {:src (gitbok.http/get-prefixed-url context "/static/yaml.min.js") :defer true}]
      [:script {:src (gitbok.http/get-prefixed-url context "/static/xml.min.js") :defer true}]
      [:script {:src (gitbok.http/get-prefixed-url context "/static/http.min.js") :defer true}]
      [:script {:src (gitbok.http/get-prefixed-url context "/static/graphql.min.js") :defer true}]
      [:script {:src (gitbok.http/get-prefixed-url context "/static/sql.min.js") :defer true}]
      [:script {:src (gitbok.http/get-prefixed-url context "/static/javascript.min.js") :defer true}]

      ;; Mermaid config (needed when Mermaid loads)
      [:script {:src (str (gitbok.http/get-prefixed-url context "/static/mermaid-config.js") version-param) :defer true}]

      ;; Combined UI bundle (includes tabs, toc, scroll-to-id, heading-links, mobile-menu, mobile-search, lastupdated, copy-code)
      ;; Load without defer to ensure it's available for HTMX events
      [:script {:src (str (gitbok.http/get-prefixed-url context "/static/ui-bundle.js") version-param)}]

      ;; Other UI scripts
      [:script {:src (str (gitbok.http/get-prefixed-url context "/static/meilisearch-htmx-nav.js") version-param) :defer true}]
      [:script {:defer true
                :src (str (gitbok.http/get-prefixed-url context "/static/keyboard-navigation.js") version-param)}]
      [:script {:defer true
                :src (str (gitbok.http/get-prefixed-url context "/static/posthog.js") version-param)}]

      ;; Theme toggle functionality
      ;; TODO
      #_[:script {:defer true
                :src (str (gitbok.http/get-prefixed-url context "/static/theme-toggle.js") version-param)}]

      ]

     [:body {:hx-on "htmx:afterSwap: window.scrollTo(0, 0);"}
      (uui/raw "<!-- Google Tag Manager (noscript) -->")
      [:noscript
       [:iframe
        {:src "https://www.googletagmanager.com/ns.html?id=GTM-PMS5LG2" :height "0" :width "0"
         :style
         "display:none;visibility:hidden"}]]
      (uui/raw "<!-- End Google Tag Manager (noscript) -->")
      body]]))

(defn layout [context request
              {:keys [content
                      title
                      description
                      filepath
                      lastmod
                      section
                      status
                      hide-breadcrumb]}]
  (let [status (or status 200)
        uri (:uri request)
        is-hx-target (uui/hx-target request)
        is-search-page (str/includes? uri "/search")
        body
        (cond
          is-hx-target
          (main-content/content-div context uri content filepath true hide-breadcrumb)

          :else
          (document
           context
           (layout-view context content uri filepath hide-breadcrumb)
           {:title title
            :description description
            :section section
            :canonical-url
            ;; / and /readme is same
            (if (get request :/)
              (gitbok.http/get-url context)
              (gitbok.http/get-absolute-url context uri))
            :og-preview
            (let [product-id (:id (products/get-current-product context))
                  png-filename (when filepath (str/replace filepath #"\.md" ".png"))]
              (gitbok.http/get-absolute-url
               context
               (utils/concat-urls "/public/og-preview" product-id png-filename)))
            :lastmod lastmod
            :favicon-url (gitbok.http/get-product-prefixed-url context "/favicon.ico")}))

        lastmod (if is-search-page
                  nil
                  (when filepath
                    (or lastmod
                        (indexing/get-lastmod context filepath))))]
    (gitbok.http/response1 body status lastmod section)))
