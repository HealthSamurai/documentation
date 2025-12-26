(ns gitbok.blog.handlers
  (:require [gitbok.blog.core :as blog]
            [gitbok.blog.sitemap :as blog-sitemap]
            [gitbok.blog.ui :as blog-ui]
            [gitbok.http :as http]
            [gitbok.ui.blog-header :as blog-header]
            [gitbok.ui.blog-footer :as blog-footer]
            [gitbok.ui.blog-subscribe :as blog-subscribe]
            [gitbok.ui.layout :as layout]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [hiccup2.core :as h]))

(def gotham-pro-font-css
  "@font-face{font-family:'Gotham Pro';src:url(https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/5a2ff11b47c38400019e7bb2_GothaProReg.otf)format('opentype');font-weight:400;font-style:normal;font-display:swap}
@font-face{font-family:'Gotham Pro';src:url(https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/5a2ff11a5700e10001e0d55e_GothaProMed.otf)format('opentype');font-weight:500;font-style:normal;font-display:swap}
@font-face{font-family:'Gotham Pro';src:url(https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/5a2ff11ac197b800013c065d_GothaProBol.otf)format('opentype');font-weight:700;font-style:normal;font-display:swap}")

(defn- gotham-font-style []
  [:style (h/raw gotham-pro-font-css)])

(defn- htmx-request?
  "Check if this is an HTMX request."
  [context]
  (get-in context [:request :headers "hx-request"]))

(defn- render-body
  "Render body hiccup to HTML string."
  [body]
  (str (h/html body)))

(defn list-handler
  "Handler for /blog listing page."
  [context]
  (let [request (:request context)
        params (:params request)
        tag (get params "tag")
        page (try
               (Integer/parseInt (or (get params "page") "1"))
               (catch Exception _ 1))
        listing-data (blog/get-articles-for-listing context {:tag tag :page page})
        current-url (http/get-absolute-url context (:uri request))
        body [:div {:id "blog-body"
                    :class "min-h-screen flex flex-col"
                    :hx-boost "true"
                    :hx-target "#blog-body"
                    :hx-swap "outerHTML"
                    :hx-push-url "true"}
              (blog-header/blog-nav context)
              (blog-ui/blog-listing-page context listing-data)
              ;; Subscribe section before footer (second one on /blog page)
              (blog-subscribe/subscribe-section {:page-url current-url
                                                  :email-id "EMAIL"
                                                  :standalone? true
                                                  :include-script? true})
              (blog-footer/blog-footer context)]]
    (if (htmx-request? context)
      ;; HTMX request - return only body
      {:status 200
       :headers {"content-type" "text/html; charset=utf-8"}
       :body (render-body body)}
      ;; Regular request - return full page
      (http/response1
       (layout/document
        context
        body
        {:title (if tag
                  (str tag " - Health Samurai Blog")
                  "Health Samurai Blog")
         :description "Health Samurai Blog - FHIR, Healthcare IT, and Aidbox insights"
         :canonical-url (http/get-absolute-url context (:uri request))
         :og-preview nil
         :lastmod nil
         :favicon-url "https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/5a2ff62247c38400019e81f3_32.png"
         :sharethis true
         :mailchimp true
         :link-previews false
         :extra-head (gotham-font-style)})))))

(defn article-handler
  "Handler for /articles/:slug page."
  [context]
  (let [request (:request context)
        slug (get-in context [:request :path-params :slug])
        article (blog/get-article context slug)]
    (if article
      (let [metadata (:metadata article)
            article-url (http/get-absolute-url context (:uri request))
            json-ld (blog-ui/article-json-ld metadata article-url)
            body [:div {:id "blog-body"
                        :class "min-h-screen flex flex-col"
                        :hx-boost "true"
                        :hx-target "#blog-body"
                        :hx-swap "outerHTML"
                        :hx-push-url "true"}
                  (blog-header/blog-nav context)
                  (blog-ui/article-page context article)
                  ;; Subscribe section before footer
                  (blog-subscribe/subscribe-section {:page-url article-url
                                                      :email-id "EMAIL"
                                                      :standalone? true
                                                      :include-script? true})
                  (blog-footer/blog-footer context)]]
        (if (htmx-request? context)
          ;; HTMX request - return only body
          {:status 200
           :headers {"content-type" "text/html; charset=utf-8"}
           :body (render-body body)}
          ;; Regular request - return full page
          (http/response1
           (layout/document
            context
            body
            {:title (str "🔥 " (:title metadata) " - Health Samurai Blog")
             :description (or (:teaser metadata) "")
             :canonical-url article-url
             :og-preview (:image metadata)
             :lastmod (:published metadata)
             :json-ld json-ld
             :favicon-url "https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/5a2ff62247c38400019e81f3_32.png"
             :sharethis true
             :mailchimp true
             :link-previews false
             :extra-head (gotham-font-style)}))))
      ;; Article not found
      {:status 404
       :headers {"content-type" "text/html; charset=utf-8"}
       :body "Article not found"})))

(defn category-handler
  "Handler for /article-categories/:slug page."
  [context]
  (let [request (:request context)
        slug (get-in context [:request :path-params :tag])
        ;; Convert slug to category name
        tag (blog/get-category-by-slug context slug)]
    (if tag
      (let [listing-data (-> (blog/get-articles-for-listing context {:tag tag})
                             (assoc :category-slug slug))
            current-url (http/get-absolute-url context (:uri request))
            body [:div {:id "blog-body"
                        :class "min-h-screen flex flex-col"
                        :hx-boost "true"
                        :hx-target "#blog-body"
                        :hx-swap "outerHTML"
                        :hx-push-url "true"}
                  (blog-header/blog-nav context)
                  (blog-ui/category-listing-page context listing-data)
                  ;; Subscribe section before footer
                  (blog-subscribe/subscribe-section {:page-url current-url
                                                      :email-id "EMAIL"
                                                      :standalone? true
                                                      :include-script? true})
                  (blog-footer/blog-footer context)]]
        (if (htmx-request? context)
          {:status 200
           :headers {"content-type" "text/html; charset=utf-8"}
           :body (render-body body)}
          (http/response1
           (layout/document
            context
            body
            {:title (str tag " - Health Samurai Blog")
             :description (str "Articles about " tag " - Health Samurai Blog")
             :canonical-url (http/get-absolute-url context (:uri request))
             :og-preview nil
             :lastmod nil
             :favicon-url "https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/5a2ff62247c38400019e81f3_32.png"
             :sharethis true
             :mailchimp true
             :link-previews false
             :extra-head (gotham-font-style)}))))
      ;; Category not found - return 404
      {:status 404
       :headers {"content-type" "text/html; charset=utf-8"}
       :body "Category not found"})))

(defn author-handler
  "Handler for /authors/:slug page."
  [context]
  (let [request (:request context)
        slug (get-in context [:request :path-params :slug])
        author (blog/get-author-by-slug context slug)]
    (if author
      (let [listing-data (-> (blog/get-articles-by-author context (:name author))
                             (assoc :author author))
            current-url (http/get-absolute-url context (:uri request))
            body [:div {:id "blog-body"
                        :class "min-h-screen flex flex-col"
                        :hx-boost "true"
                        :hx-target "#blog-body"
                        :hx-swap "outerHTML"
                        :hx-push-url "true"}
                  (blog-header/blog-nav context)
                  (blog-ui/author-listing-page context listing-data)
                  (blog-subscribe/subscribe-section {:page-url current-url
                                                      :email-id "EMAIL"
                                                      :standalone? true
                                                      :include-script? true})
                  (blog-footer/blog-footer context)]]
        (if (htmx-request? context)
          {:status 200
           :headers {"content-type" "text/html; charset=utf-8"}
           :body (render-body body)}
          (http/response1
           (layout/document
            context
            body
            {:title (str (:name author) " - Health Samurai Blog")
             :description (str "Articles by " (:name author) " - Health Samurai Blog")
             :canonical-url (http/get-absolute-url context (:uri request))
             :og-preview (:image author)
             :lastmod nil
             :favicon-url "https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/5a2ff62247c38400019e81f3_32.png"
             :sharethis true
             :mailchimp true
             :link-previews false
             :extra-head (gotham-font-style)}))))
      ;; Author not found - return 404
      {:status 404
       :headers {"content-type" "text/html; charset=utf-8"}
       :body "Author not found"})))

(defn search-handler
  "Handler for /blog/search endpoint."
  [context]
  (let [query (get-in context [:request :params "q"])]
    {:status 200
     :headers {"content-type" "text/html; charset=utf-8"}
     :body (if (and query (> (count query) 2))
             ;; TODO: Implement actual Meilisearch integration
             "<div class=\"p-4\"><p class=\"text-sm text-on-surface-muted\">Search functionality will be integrated with Meilisearch.</p></div>"
             "<div class=\"p-4\"><p class=\"text-sm text-on-surface-muted\">Enter at least 3 characters to search.</p></div>")}))

(defn- get-content-type
  "Get content type based on file extension."
  [filename]
  (cond
    (str/ends-with? filename ".png") "image/png"
    (str/ends-with? filename ".jpg") "image/jpeg"
    (str/ends-with? filename ".jpeg") "image/jpeg"
    (str/ends-with? filename ".gif") "image/gif"
    (str/ends-with? filename ".webp") "image/webp"
    (str/ends-with? filename ".svg") "image/svg+xml"
    :else "application/octet-stream"))

(defn blog-image-handler
  "Handler for serving images from blog article folders via /blog/static/img/:folder/:file"
  [context]
  (let [blog-dir (blog/get-blog-dir context)
        folder (get-in context [:request :path-params :folder])
        filename (get-in context [:request :path-params :file])
        file (io/file blog-dir folder filename)]
    (if (and (.exists file) (.isFile file))
      {:status 200
       :headers {"Content-Type" (get-content-type filename)
                 "Cache-Control" "public, max-age=31536000"}
       :body file}
      {:status 404
       :headers {"Content-Type" "text/plain"}
       :body "Not found"})))

(defn sitemap-handler
  "Handler for /docs/futureblog/blog/sitemap.xml"
  [context]
  {:status 200
   :headers {"Content-Type" "application/xml; charset=utf-8"
             "Cache-Control" "public, max-age=3600"}
   :body (blog-sitemap/generate-blog-sitemap context)})

