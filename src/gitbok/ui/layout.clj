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
   [clojure.string :as str]
   [gitbok.utils :as utils]))

(defn layout-view [context body uri filepath]
  [:div
   (main-navigation/nav)
   [:div.mobile-menu-overlay]
   [:div
    {:class "flex px-4 sm:px-6 md:px-8 max-w-screen-2xl mx-auto site-full-width:max-w-full gap-20"}
    (left-navigation/left-navigation
     (summary/get-summary context)
     uri)
    [:div {:class "flex-1"}
     (main-content/content-div context uri body filepath)]
    (when filepath
      (right-toc/get-right-toc context filepath))]])

(defn document [body {:keys [title description canonical-url open-graph-image]}]
  [:html {:lang "en"}
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1.0"}]
    [:meta {:name "description" :content description}]
    [:meta {:property "og:title" :content title}]
    [:meta {:property "og:description" :content description}]
    [:meta {:property "og:url" :content canonical-url}]
    [:meta {:property "og:type" :content "article"}]
    [:meta {:property "og:image" :content open-graph-image}]
    [:meta {:name "htmx-config",
            :content "{\"scrollIntoViewOnBoost\":false,\"scrollBehavior\":\"smooth\"}"}]
    [:link {:rel "icon" :type "image/x-icon" :href "/favicon.ico"}]
    [:link {:rel "shortcut icon" :type "image/x-icon" :href "/favicon.ico"}]
    [:link {:rel "apple-touch-icon" :href "/favicon.ico"}]
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
    [:script {:defer true
              :src "/static/keyboard-navigation.js"}]
    [:script {:defer true
              :src "/static/toc-scroll.js"}]
    [:script {:defer true
              :src "/static/lastupdated.js"}]
    [:script {:defer true
              :src "/static/posthog.js"}]]
   [:body {:hx-boost "true"
           :hx-on "htmx:afterSwap: window.scrollTo(0, 0); updateLastUpdated();"}
    body]])

(defn layout [context request
              {:keys [content
                      title
                      description
                      filepath
                      base-url
                      lastmod]}]
  (let [body (if (map? content) (:body content) content)
        status (if (map? content) (:status content 200) 200)
        uri (:uri request)
        is-hx-target (uui/hx-target request)
        body
        (cond
          is-hx-target
          (main-content/content-div context uri body filepath true)

          :else
          (document
           (layout-view context body uri filepath)
           {:title title :description description
            :canonical-url
            (if (get request :/) base-url (when base-url (utils/concat-urls base-url uri)))
            :og-preview
            (when base-url
              (utils/concat-urls
               base-url
               (str "public/og-preview/"
                    (when filepath (str/replace filepath #".md" ".png")))))}))

        lastmod (when filepath
                  (or lastmod
                      (indexing/get-lastmod context filepath)))]
    (gitbok.http/response1
     body status lastmod is-hx-target)))
