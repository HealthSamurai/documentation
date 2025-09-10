(ns gitbok.ui.layout
  (:require
   [gitbok.indexing.impl.summary :as summary]
   [gitbok.ui.main-content :as main-content]
   [gitbok.ui.main-navigation :as main-navigation]
   [gitbok.ui.left-navigation :as left-navigation]
   [gitbok.indexing.core :as indexing]
   [gitbok.http]
   [gitbok.reload :as reload]
   [gitbok.products :as products]
   [cheshire.core :as json]
   [uui]
   [system]
   [clojure.string :as str]
   [gitbok.utils :as utils]))
(defn site-footer
  "Site-wide footer component"
  [context]
  (let [git-head (:git-head (reload/get-reload-state context))
        version-text (when git-head (str " (" (subs git-head 0 (min 7 (count git-head))) ")"))]
    [:footer.mt-auto.border-t.border-tint-6.bg-tint-1
     [:div.max-w-screen-2xl.mx-auto.px-5.md:px-8.py-6
      ;; Mobile: stack vertically, Desktop: single row
      [:div.flex.flex-col.md:flex-row.md:justify-center.items-center.gap-4
       ;; Copyright - always first
       [:div.flex.items-center.gap-2
        [:a.text-tint-10.hover:text-primary-9.transition-colors.no-underline.text-sm
         {:href "https://www.health-samurai.io/"
          :target "_blank"
          :rel "noopener noreferrer"}
         "© Health Samurai"]]
       
       ;; Social icons group
       [:div.flex.items-center.gap-4
        ;; LinkedIn icon
        [:a.text-tint-10.hover:text-primary-9.transition-colors
         {:href "https://www.linkedin.com/company/health-samurai"
          :target "_blank"
          :rel "noopener noreferrer"
          :title "Health Samurai on LinkedIn"}
         [:svg.w-5.h-5
          {:viewBox "0 0 24 24"
           :fill "currentColor"}
          [:path {:d "M20.447 20.452h-3.554v-5.569c0-1.328-.027-3.037-1.852-3.037-1.853 0-2.136 1.445-2.136 2.939v5.667H9.351V9h3.414v1.561h.046c.477-.9 1.637-1.85 3.37-1.85 3.601 0 4.267 2.37 4.267 5.455v6.286zM5.337 7.433c-1.144 0-2.063-.926-2.063-2.065 0-1.138.92-2.063 2.063-2.063 1.14 0 2.064.925 2.064 2.063 0 1.139-.925 2.065-2.064 2.065zm1.782 13.019H3.555V9h3.564v11.452zM22.225 0H1.771C.792 0 0 .774 0 1.729v20.542C0 23.227.792 24 1.771 24h20.451C23.2 24 24 23.227 24 22.271V1.729C24 .774 23.2 0 22.222 0h.003z"}]]]
        
        ;; YouTube icon
        [:a.text-tint-10.hover:text-primary-9.transition-colors
         {:href "https://www.youtube.com/@HealthSamurai"
          :target "_blank"
          :rel "noopener noreferrer"
          :title "Health Samurai on YouTube"}
         [:svg.w-5.h-5
          {:viewBox "0 0 24 24"
           :fill "currentColor"}
          [:path {:d "M23.498 6.186a3.016 3.016 0 0 0-2.122-2.136C19.505 3.545 12 3.545 12 3.545s-7.505 0-9.377.505A3.017 3.017 0 0 0 .502 6.186C0 8.07 0 12 0 12s0 3.93.502 5.814a3.016 3.016 0 0 0 2.122 2.136c1.871.505 9.376.505 9.376.505s7.505 0 9.377-.505a3.015 3.015 0 0 0 2.122-2.136C24 15.93 24 12 24 12s0-3.93-.502-5.814zM9.545 15.568V8.432L15.818 12l-6.273 3.568z"}]]]]
       
       ;; Separator on desktop only
       [:div.hidden.md:block.text-tint-6 "•"]
       
       ;; Links group
       [:div.flex.flex-col.sm:flex-row.items-center.gap-3.text-center
        [:a.text-tint-10.hover:text-primary-9.transition-colors.no-underline.text-sm
         {:href "https://connect.health-samurai.io"
          :target "_blank"
          :rel "noopener noreferrer"}
         "Contact us"]
        
        [:span.hidden.sm:block.text-tint-6 "•"]
        
        [:a.text-tint-10.hover:text-primary-9.transition-colors.no-underline.text-sm
         {:href "https://github.com/HealthSamurai/documentation"
          :target "_blank"
          :rel "noopener noreferrer"}
         (str "Docs source" version-text)]]]]]))

(defn page-wrapper
  "Wrapper for pages with header and footer but no left navigation"
  [context content]
  [:div.min-h-screen.flex.flex-col
   (main-navigation/nav context)
   [:div.mobile-menu-overlay]
   [:div.flex-1
    {:class "flex max-w-screen-2xl mx-auto site-full-width:max-w-full
     items-start overflow-visible md:px-8 py-8"}
    [:main#content {:class "flex-1 items-start"}
     [:div {:class "flex items-start"}
      [:article {:class "article__content min-w-0 flex-1 transform-3d"}
       [:div {:class "mx-auto max-w-full"}
        content]]]]]
   (site-footer context)])

(defn layout-view [context body uri filepath hide-breadcrumb]
  [:div
   (main-navigation/nav context)
   [:div.mobile-menu-overlay]
   [:div.flex-1
    {:class "flex max-w-screen-2xl mx-auto site-full-width:max-w-full
     items-start overflow-visible md:px-8"}
    (left-navigation/left-navigation
     (summary/get-summary context)
     uri)
    (main-content/content-div context uri body filepath false hide-breadcrumb)]
   (site-footer context)])

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
      [:script {:src (gitbok.http/get-prefixed-url context "/static/htmx.min.js")
                :deref true}]

      [:link {:rel "stylesheet" :href (gitbok.http/get-prefixed-url context "/static/github.min.css") :defer true}]

      ;; todo dark theme later
      #_[:link {:rel "stylesheet" :href (gitbok.http/get-prefixed-url context "/static/github-dark.min.css")
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
      [:script {:src (str (gitbok.http/get-prefixed-url context "/static/ui-bundle.js") version-param)
                :defer true}]

      ;; Other UI scripts
      [:script {:src (str (gitbok.http/get-prefixed-url context "/static/meilisearch-htmx-nav.js") version-param)
                :defer true}]
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
