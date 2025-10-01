(ns gitbok.ui.layout
  (:require
   [gitbok.http :as http]
   [gitbok.state :as state]
   [gitbok.ui.main-content :as main-content]
   [gitbok.ui.main-navigation :as main-navigation]
   [gitbok.ui.left-navigation :as left-navigation]
   [gitbok.indexing.core :as indexing]
   [gitbok.reload :as reload]
   [gitbok.products :as products]
   [cheshire.core :as json]
   [hiccup2.core]
   [clojure.string :as str]
   [gitbok.utils :as utils]))

(defn site-footer
  "Site-wide footer component"
  [context]
  (let [reload-state (reload/get-reload-state context)
        git-head (:git-head reload-state)
        version-text (when (and git-head (not= git-head ""))
                       (str " (" (subs git-head 0 (min 7 (count git-head))) ")"))]
    [:footer {:class "mt-auto border-t border-tint-6 bg-tint-1"}
     [:div {:class "max-w-screen-2xl mx-auto px-5 md:px-8 py-6"}
      ;; Mobile: stack vertically, Desktop: single row
      [:div {:class "flex flex-col md:flex-row md:justify-center items-center gap-4"}
       ;; Copyright - always first
       [:div {:class "flex items-center gap-2"}
        [:a {:class "text-[#353B50] hover:text-primary-9 transition-colors no-underline text-sm leading-5"
             :href "https://www.health-samurai.io/"
             :target "_blank"
             :rel "noopener noreferrer"}
         "© Health Samurai"]]

       ;; Social icons group
       [:div {:class "flex items-center gap-4"}
        ;; LinkedIn icon
        [:a {:class "text-[#353B50] hover:text-primary-9 transition-colors"
             :href "https://www.linkedin.com/company/health-samurai"
             :target "_blank"
             :rel "noopener noreferrer"
             :title "Health Samurai on LinkedIn"}
         [:svg {:class "w-5 h-5"
                :viewBox "0 0 24 24"
                :fill "currentColor"}
          [:path {:d "M20.447 20.452h-3.554v-5.569c0-1.328-.027-3.037-1.852-3.037-1.853 0-2.136 1.445-2.136 2.939v5.667H9.351V9h3.414v1.561h.046c.477-.9 1.637-1.85 3.37-1.85 3.601 0 4.267 2.37 4.267 5.455v6.286zM5.337 7.433c-1.144 0-2.063-.926-2.063-2.065 0-1.138.92-2.063 2.063-2.063 1.14 0 2.064.925 2.064 2.063 0 1.139-.925 2.065-2.064 2.065zm1.782 13.019H3.555V9h3.564v11.452zM22.225 0H1.771C.792 0 0 .774 0 1.729v20.542C0 23.227.792 24 1.771 24h20.451C23.2 24 24 23.227 24 22.271V1.729C24 .774 23.2 0 22.222 0h.003z"}]]]

        ;; YouTube icon
        [:a {:class "text-[#353B50] hover:text-primary-9 transition-colors"
             :href "https://www.youtube.com/@HealthSamurai"
             :target "_blank"
             :rel "noopener noreferrer"
             :title "Health Samurai on YouTube"}
         [:svg {:class "w-5 h-5"
                :viewBox "0 0 24 24"
                :fill "currentColor"}
          [:path {:d "M23.498 6.186a3.016 3.016 0 0 0-2.122-2.136C19.505 3.545 12 3.545 12 3.545s-7.505 0-9.377.505A3.017 3.017 0 0 0 .502 6.186C0 8.07 0 12 0 12s0 3.93.502 5.814a3.016 3.016 0 0 0 2.122 2.136c1.871.505 9.376.505 9.376.505s7.505 0 9.377-.505a3.015 3.015 0 0 0 2.122-2.136C24 15.93 24 12 24 12s0-3.93-.502-5.814zM9.545 15.568V8.432L15.818 12l-6.273 3.568z"}]]]]

       ;; Separator on desktop only
       [:div {:class "hidden md:block text-tint-6"} "•"]

       ;; Links group
       [:div {:class "flex flex-col sm:flex-row items-center gap-3 text-center"}
        [:a {:class "text-[#353B50] hover:text-primary-9 transition-colors no-underline text-sm leading-5"
             :href "https://www.health-samurai.io/contacts"
             :target "_blank"
             :rel "noopener noreferrer"}
         "Contact us"]

        [:span {:class "hidden sm:block text-tint-6"} "•"]

        [:a {:class "text-[#353B50] hover:text-primary-9 transition-colors no-underline text-sm leading-5"
             :href "https://github.com/HealthSamurai/documentation"
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
    (left-navigation/left-navigation (state/get-summary context) uri)
    (main-content/content-div context uri body filepath false hide-breadcrumb)]
   (site-footer context)])

(defn document [context body {:keys [title description canonical-url og-preview lastmod favicon-url section]}]
  (let [version (state/get-config context :version)
        version-param (when version (str "?v=" version))]
    [:html {:lang "en"
            :class "antialiased"}
     [:head
      [:meta {:charset "utf-8"}]
      [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
      [:meta {:name "description" :content description}]
      [:meta {:property "og:title" :content title}]
      [:meta {:property "og:site_name" :content (or (:name (products/get-current-product context)) "Documentation")}]
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

      [:meta {:name "htmx-config"
              :content "{\"scrollIntoViewOnBoost\":false,\"scrollBehavior\":\"smooth\",\"allowEval\":false,\"historyCacheSize\":0,\"historyEnabled\":true,\"refreshOnHistoryMiss\":false}"}]
      (when section
        [:meta {:name "scroll-to-id" :content section}])
      [:link {:rel "icon" :type "image/x-icon" :href favicon-url}]
      [:link {:rel "shortcut icon" :type "image/x-icon" :href favicon-url}]
      [:link {:rel "apple-touch-icon" :href favicon-url}]
      [:link {:rel "canonical" :href canonical-url}]

      [:script {:type "application/ld+json"}
       (hiccup2.core/raw
        (json/generate-string
         {"@context" "https://schema.org"

          "@type" "TechArticle"
          "headline" title
          "description" description
          "author" {"@type" "Organization", "name" "HealthSamurai"}}))]
      [:title (str title " | " (or (:name (products/get-current-product context)) "Documentation"))]

      [:link {:rel "stylesheet", :href (str (http/get-prefixed-url context "/static/app.min.css") version-param)}]

      ;; Critical scripts - load with defer to avoid render blocking
      [:script {:src (http/get-prefixed-url context "/static/htmx.min.js")
                :defer true}]

      ;; Prism.js for syntax highlighting - lazy load with media trick
      [:link {:rel "stylesheet"
              :href (http/get-prefixed-url context "/static/prism.css")
              :media "print"
              :onload "this.media='all'"}]
      [:script {:src (str (http/get-prefixed-url context "/static/prism.js") version-param)
                :defer true}]

;; Combined UI bundle (includes tabs, toc, scroll-to-id, heading-links, mobile-menu, mobile-search, lastupdated, copy-code)
      [:script {:src (str (http/get-prefixed-url context "/static/ui-bundle.js") version-param)
                :defer true}]

      ;; Other UI scripts
      [:script {:src (str (http/get-prefixed-url context "/static/meilisearch-htmx-nav.js") version-param)
                :defer true}]

      ;; Mermaid config (needed when Mermaid loads)
      [:script {:src (str (http/get-prefixed-url context "/static/mermaid-config.js") version-param) :defer true}]

      [:script {:defer true
                :src (str (http/get-prefixed-url context "/static/posthog.js") version-param)}]
      ;; Theme toggle functionality
      ;; TODO
      #_[:script {:defer true
                  :src (str (http/get-prefixed-url context "/static/theme-toggle.js") version-param)}]

      ;; PushEngage - moved to end of head
      [:script {:defer true}
       (hiccup2.core/raw "
(function(w, d) { w.PushEngage = w.PushEngage || []; w._peq = w._peq || []; PushEngage.push(['init', { appId: '795c4eea-7a69-42d7-bff3-882774303fcf' }]); var e = d.createElement('script'); e.src = 'https://clientcdn.pushengage.com/sdks/pushengage-web-sdk.js'; e.async = true; e.type = 'text/javascript'; d.head.appendChild(e); })(window, document);")]

      ;; Google Tag Manager - moved to end of head with defer
      (hiccup2.core/raw "<!-- Google Tag Manager -->")
      [:script {:defer true}
       (hiccup2.core/raw
        "(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0],
j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
'https://www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
})(window,document,'script','dataLayer','GTM-PMS5LG2');")]
      (hiccup2.core/raw "<!-- End Google Tag Manager -->")]

     [:body {:hx-on "htmx:afterSwap: window.scrollTo(0, 0);"}
      (hiccup2.core/raw "<!-- Google Tag Manager (noscript) -->")
      [:noscript
       [:iframe
        {:src "https://www.googletagmanager.com/ns.html?id=GTM-PMS5LG2" :height "0" :width "0"
         :style
         "display:none;visibility:hidden"}]]
      (hiccup2.core/raw "<!-- End Google Tag Manager (noscript) -->")
      body]]))

(defn hx-target [request]
  (get-in request [:headers "hx-target"]))

(defn layout [ctx
              {:keys [content
                      title
                      description
                      filepath
                      lastmod
                      section
                      status
                      hide-breadcrumb]}]
  (let [request (:request ctx)
        status (or status 200)
        uri (:uri request)
        is-hx-target (hx-target request)
        is-search-page (str/includes? uri "/search")
        body
        (cond
          is-hx-target
          (main-content/content-div ctx uri content filepath true hide-breadcrumb)

          :else
          (document
           ctx
           (layout-view ctx content uri filepath hide-breadcrumb)
           {:title title
            :description description
            :section section
            :canonical-url
            ;; / and /readme is same
            (if (get request :/)
              (http/get-url ctx)
              (http/get-absolute-url ctx uri))
            :og-preview
            (let [current-product (products/get-current-product ctx)
                  product-id (when current-product (:id current-product))
                  png-filename (when filepath (str/replace filepath #"\.md" ".png"))]
              (when (and product-id png-filename)
                (http/get-absolute-url
                 ctx
                 (utils/concat-urls "/public/og-preview" product-id png-filename))))
            :lastmod lastmod
            :favicon-url (http/get-product-prefixed-url ctx "/favicon.ico")}))

        lastmod (if is-search-page
                  nil
                  (when filepath
                    (or lastmod
                        (indexing/get-lastmod ctx filepath))))]
    (http/response1 ctx body status lastmod section)))
