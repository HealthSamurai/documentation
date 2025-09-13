(ns gitbok.init
  "Initialization functions for products and indices"
  (:require [gitbok.products :as products]
            [gitbok.state :as state]
            [gitbok.handlers :as handlers]
            [gitbok.indexing.core :as indexing]
            [gitbok.indexing.impl.file-to-uri :as file-to-uri]
            [gitbok.indexing.impl.sitemap :as sitemap]
            [gitbok.indexing.impl.summary :as summary]
            [gitbok.indexing.impl.uri-to-file :as uri-to-file]
            [gitbok.markdown.core :as markdown]
            [clojure.string :as str]
            [clojure.tools.logging :as log]))

(defn init-product-indices
  "Initializes indexes for a specific product"
  [context product]
  (let [;; Temporarily set current product for initialization
        ctx (products/set-current-product-id context (:id product))]
    ;; order is important
    (log/info "init-product" {:msg "1. read summary. create toc htmx."
                              :product (:id product)})
    (let [parsed-summary (summary/parse-summary ctx)]
      (state/set-summary! ctx parsed-summary))
    (log/info "init-product" {:msg "2. get uris from summary (using slugging), merge with redirects"
                              :product (:id product)})
    (let [indexes (uri-to-file/uri->file-idx ctx)]
      ;; Store files index
      (state/set-uri-to-file-idx! ctx (:files indexes))
      ;; Store redirects index
      (state/set-redirects-idx! ctx (:redirects indexes)))
    (log/info "init-product" {:msg "3. reverse file to uri idx"
                              :product (:id product)})
    (let [file-to-uri-idx (file-to-uri/file->uri-idx ctx)]
      (state/set-file-to-uri-idx! ctx file-to-uri-idx))
    (log/info "init-product" {:msg "4. using files from summary (step 3), read all files into memory"
                              :product (:id product)})
    (indexing/set-md-files-idx ctx (state/get-file-to-uri-idx ctx))
    (log/info "init-product" {:msg "5. parse all files into memory, some things are already rendered as plain html"
                              :product (:id product)})
    (markdown/set-parsed-markdown-index ctx (indexing/get-md-files-idx ctx))
    (log/info "init-product" {:msg "6. render it on start"
                              :product (:id product)})
    (let [dev-mode (state/get-config ctx :dev-mode)]
      (if dev-mode
        (log/info "init-product" {:msg "DEV mode, skip pre-rendering"
                                  :product (:id product)})
        (do
          (log/info "init-product" {:msg "Production mode, render all pages into memory"
                                    :product (:id product)})
          (markdown/render-all! ctx
                                (markdown/get-parsed-markdown-index ctx)
                                handlers/read-markdown-file)
          (log/info "init-product" {:msg "render done"
                                    :product (:id product)}))))
    (log/info "init-product" {:msg "7. set lastmod data in context for Last Modified metadata"
                              :product (:id product)})
    (indexing/set-lastmod ctx)
    (log/info "init-product" {:msg "8. generate sitemap.xml for product"
                              :product (:id product)})
    (let [lastmod-data (products/get-product-state ctx [::lastmod])
          ;; Get primary navigation links (excluding cross-section references)
          primary-links (summary/get-primary-navigation-links ctx)
          ;; Extract just the hrefs, removing leading slashes
          primary-urls (mapv (fn [link]
                               (let [href (:href link)]
                                 (if (str/starts-with? href "/")
                                   (subs href 1)
                                   href)))
                             primary-links)
          sitemap-xml (sitemap/generate-sitemap ctx primary-urls lastmod-data)]
      (state/set-sitemap! ctx sitemap-xml))

    ;; 9. Pre-calculate favicon path for product
    (let [favicon-path (or (:favicon product) "public/favicon.ico")
          favicon-path (if (str/starts-with? favicon-path ".gitbook/")
                         (str/replace-first favicon-path #".*.gitbook/" "")
                         favicon-path)]
      (state/set-product-state! ctx [:favicon-path] favicon-path))))

(defn init-products
  "Initializes all products from configuration"
  [context]
  (let [full-config (products/load-products-config context)
        products-config (:products full-config)]
    (products/set-products-config context products-config)
    (products/set-full-config context full-config)
    products-config))
