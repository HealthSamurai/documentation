(ns gitbok.initialization
  "Unified initialization logic for both startup and reload scenarios"
  (:require
   [clojure.string :as str]
   [clojure.tools.logging :as log]
   [gitbok.indexing.core :as indexing]
   [gitbok.indexing.impl.file-to-uri :as file-to-uri]
   [gitbok.indexing.impl.sitemap :as sitemap]
   [gitbok.indexing.impl.sitemap-index :as sitemap-index]
   [gitbok.indexing.impl.summary :as summary]
   [gitbok.indexing.impl.uri-to-file :as uri-to-file]
   [gitbok.markdown.core :as markdown]
   [gitbok.products :as products]
   [gitbok.state :as state]))

(defn init-product-indices
  "Initializes indexes for a specific product"
  [context product & {:keys [read-markdown-fn]}]
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
          (when read-markdown-fn
            (markdown/render-all! ctx
                                  (markdown/get-parsed-markdown-index ctx)
                                  read-markdown-fn))
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

(defn init-single-product!
  "Initialize a single product with error handling and logging"
  [context product & {:keys [read-markdown-fn]}]
  (try
    (init-product-indices context product :read-markdown-fn read-markdown-fn)
    (log/info "Product indices initialized" {:product (:name product)})
    (catch Exception e
      (log/error e "Failed to initialize product indices" {:product (:name product)}))))

(defn init-all-products!
  "Initialize all products - used during startup and reload"
  [context & {:keys [read-markdown-fn]}]
  (log/info "Initializing products configuration")
  (let [products-config (init-products context)]
    (state/set-products! context products-config)
    (log/info "Products initialized" {:count (count products-config)
                                      :products (mapv :name products-config)})
    
    ;; Initialize each product's indices
    (doseq [[idx product] (map-indexed vector products-config)]
      (log/info "Initializing product" {:product-id (:id product)
                                        :product-name (:name product)
                                        :index (inc idx)
                                        :total (count products-config)})
      (let [start-time (System/currentTimeMillis)]
        (init-single-product! context product :read-markdown-fn read-markdown-fn)
        (let [duration (- (System/currentTimeMillis) start-time)]
          (log/info "Product initialized" {:product-id (:id product)
                                           :product-name (:name product)
                                           :duration-ms duration}))))
    
    ;; Update lastmod for all products
    (log/info "Updating lastmod data for all products")
    (doseq [product products-config]
      (let [context-with-product (assoc context
                                        :product product
                                        :current-product-id (:id product))]
        (indexing/set-lastmod context-with-product)))
    
    ;; Generate and cache sitemap index
    (log/info "Generating sitemap index")
    (let [sitemap-index-xml (sitemap-index/generate-and-cache-sitemap-index! context)]
      (state/set-cache! context :sitemap-index-xml sitemap-index-xml))
    
    products-config))