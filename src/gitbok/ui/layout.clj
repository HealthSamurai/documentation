(ns gitbok.ui.layout
  (:require
   [gitbok.ui.right-toc :as right-toc]
   [gitbok.indexing.impl.summary :as summary]
   [gitbok.ui.main-content :as main-content]
   [gitbok.ui.main-navigation :as main-navigation]
   [gitbok.ui.left-navigation :as left-navigation]
   [gitbok.indexing.core :as indexing]
   [gitbok.http]
   [cheshire.core :as json]
   [uui]
   [system]
   [clojure.string :as str]))

(defn layout-view [context body uri filepath toc]
  [:div
   (main-navigation/nav context)
   [:div.mobile-menu-overlay]
   [:div
    {:class "flex px-4 sm:px-6 md:px-8 max-w-screen-2xl mx-auto site-full-width:max-w-full gap-20"}
    (left-navigation/left-navigation
     (summary/get-summary context)
     uri)
    [:div {:class "flex-1"}
     (main-content/content-div context uri body filepath)]
    (when filepath
      (or (right-toc/get-right-toc context filepath) toc))]])

(defn document [body {:keys [title description canonical-url og-preview lastmod favicon-url]}]
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
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
    [:meta {:name "description" :content description}]
    [:meta {:property "og:title" :content title}]
    [:meta {:property "og:site_name" :content "Aidbox User Docs"}]
    [:meta {:property "article:modified_time" :content lastmod}]
    [:meta {:property "og:description" :content description}]
    [:meta {:property "og:url" :content canonical-url}]
    [:meta {:property "og:type" :content "article"}]
    [:meta {:property "og:locale" :content "en_US"}]
    [:meta {:property "og:image" :content og-preview}]

    [:meta {:name "twitter:card" :content "summary_large_image"}]
    [:meta {:name "twitter:title" :content title}]
    [:meta {:name "twitter:description" :content description}]
    [:meta {:name "twitter:image" :content og-preview}]
    [:meta {:name "twitter:site" :content "@health_samurai"}]
    [:meta {:name "robots" :content "index, follow"}]

    [:meta {:name "htmx-config",
            :content "{\"scrollIntoViewOnBoost\":false,\"scrollBehavior\":\"smooth\",\"allowEval\":false}"}]
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
    [:title (str title " | Aidbox User Docs")]

    [:link {:rel "stylesheet", :href "/static/app.min.css"}]
    [:script {:src "/static/htmx.min.js"}]
    [:script {:src "/static/tabs.js"}]
    [:script {:src "/static/toc.js"}]
    [:script {:src "/static/mobile-menu.js"}]
    [:link {:rel "stylesheet" :href "/static/github.min.css"}]
    [:script {:src "/static/highlight.min.js"}]
    [:script {:src "/static/json.min.js"}]
    [:script {:src "/static/bash.min.js"}]
    [:script {:src "/static/yaml.min.js"}]
    [:script {:src "/static/json.min.js"}]
    [:script {:src "/static/xml.min.js"}]
    [:script {:src "/static/http.min.js"}]
    [:script {:src "/static/graphql.min.js"}]
    [:script {:src "/static/sql.min.js"}]
    [:script {:src "/static/javascript.min.js"}]
    [:script {:src "/static/mermaid.min.js"}]
    [:script "hljs.highlightAll();"]
    [:script {:src "/static/copy-code.js"}]
    [:script {:defer true
              :src "/static/keyboard-navigation.js"}]
    [:script {:defer true
              :src "/static/toc-scroll.js"}]
    [:script {:defer true
              :src "/static/lastupdated.js"}]
    [:script {:defer true
              :src "/static/posthog.js"}]
    #_[:script {:defer true
                :src "/static/gtm.js"}]]
   [:body {:hx-boost "true"
           :hx-on "htmx:afterSwap: window.scrollTo(0, 0); updateLastUpdated();"}
    (uui/raw "<!-- Google Tag Manager (noscript) -->")
    [:noscript
     [:iframe
      {:src "https://www.googletagmanager.com/ns.html?id=GTM-PMS5LG2" :height "0" :width "0"
       :style
       "display:none;visibility:hidden"}]]
    (uui/raw "<!-- End Google Tag Manager (noscript) -->")
    body]])

(defn layout [context request
              {:keys [content
                      title
                      description
                      filepath
                      toc
                      lastmod]}]
  (let [body (if (map? content) (:body content) content)
        status (if (map? content) (:status content 200) 200)
        uri (:uri request)
        is-hx-target (uui/hx-target request)
        is-search-page (str/includes? uri "/search")
        body
        (cond
          is-hx-target
          (main-content/content-div context uri body filepath true)

          :else
          (document
           (layout-view context body uri filepath toc)
           {:title title :description description
            :canonical-url
            ;; / and /readme is same
            (if (get request :/)
              (gitbok.http/get-url context)
              (gitbok.http/get-absolute-url context uri))
            :og-preview
            (gitbok.http/get-absolute-url
             context
             (str "public/og-preview/"
                  (when filepath (str/replace filepath #".md" ".png"))))
            :lastmod lastmod
            :favicon-url (gitbok.http/get-prefixed-url context "/favicon.ico")}))

        lastmod (if is-search-page
                  nil
                  (when filepath
                    (or lastmod
                        (indexing/get-lastmod context filepath))))]
    (gitbok.http/response1 body status lastmod)))
