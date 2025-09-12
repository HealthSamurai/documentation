(ns gitbok.products
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
   [clj-yaml.core :as yaml]
   [gitbok.utils :as utils]
   [gitbok.state :as state]
   [clojure.tools.logging :as log]))

(def default-aidbox
  [{:id "aidbox"
    :name "Aidbox Documentation"
    :path "/aidbox"
    :config "aidbox/.gitbook.yaml"}])

(defn read-product-config-file [config-file]
  (yaml/parse-string (utils/slurp-resource config-file)))

(defn volume-path [context]
  (state/get-config context :docs-volume-path))

(defn load-products-config
  "Loads products configuration from products.yaml in classpath or volume"
  [context]
  (try
    (let [config-str (if-let [vol-path (volume-path context)]
                       ;; Try volume first, then fallback to classpath
                       (let [file (io/file vol-path "products.yaml")]
                         (log/info "load config" {:source "volume" :path (.getPath file)})
                         (if (.exists file)
                           (slurp file)
                           ;; Volume path set but file not found - try classpath
                           (do
                             (log/warn "fallback classpath" {:reason "file-not-in-volume"})
                             (utils/slurp-resource "products.yaml"))))
                       ;; Read from classpath
                       (utils/slurp-resource "products.yaml"))
          config (yaml/parse-string config-str)
          products (mapv
                    (fn [product]
                      (let [config-data (read-product-config-file (:config product))
                            merged (merge product config-data)
                            ;; Calculate docs-relative-path for this product
                            config-path (:config merged)
                            config-dir (utils/parent config-path)
                            root (or (-> merged :structure :root)
                                     (:root merged)
                                     "./docs")
                            ;; Remove leading "./" from root if present
                            root (if (str/starts-with? root "./")
                                   (subs root 2)
                                   root)
                            ;; Build relative docs path
                            docs-relative-path (if (str/blank? config-dir)
                                                  root
                                                  (.getPath (io/file config-dir root)))]
                        (assoc merged :docs-relative-path docs-relative-path)))
                    (:products config))]
      {:products products
       :root-redirect (:root-redirect config)})
    (catch Exception e
      (log/error "load config error" {:error (.getMessage e)
                                      :fallback "default-aidbox"})
      {:products default-aidbox})))

(defn get-current-product-id
  "Gets current product ID - for compatibility"
  [context]
  (or (:current-product-id context)
      (:id (:product context))
      (:id (:gitbok.products/current-product context))
      "default"))

(defn set-current-product-id
  "Sets current product ID - returns updated context"
  [context product-id]
  ;; Just return updated context with product-id
  (assoc context :current-product-id product-id))

(defn get-product-state
  "Gets state for a specific product"
  [context path & [default]]
  (state/get-product-state context path default))

(defn set-product-state
  "Sets state for a specific product"
  [context path value]
  (state/set-product-state! context path value))

(defn determine-product-by-uri
  "Determines product from request URI"
  [products uri]
  (let [by-path (first (filter #(str/starts-with? uri (:path %))
                               (sort-by #(- (count (:path %))) products)))
        default-product (first (filter #(= (:id %) "default") products))
        fallback (first products)
        result (or by-path default-product fallback)]
    (log/info "determine product by uri" {:uri uri
                                           :products-count (count products)
                                           :matched-by-path (when by-path (:id by-path))
                                           :default-product (when default-product (:id default-product))
                                           :fallback-product (when fallback (:id fallback))
                                           :result-id (when result (:id result))})
    result))

(defn get-products-config
  "Gets all products configuration"
  [context]
  (state/get-products context))

(defn get-full-config
  "Gets full configuration including products and root-redirect"
  [context]
  (state/get-state context [:products :full-config] {}))

(defn set-products-config
  "Saves products configuration"
  [context products]
  (state/set-products! context products))

(defn set-full-config
  "Saves full configuration"
  [context config]
  (state/set-state! context [:products :full-config] config))

(defn get-product-by-id
  "Gets product configuration by its ID"
  [context product-id]
  (let [products (get-products-config context)]
    (first (filter #(= (:id %) product-id) products))))

(defn get-current-product
  "Gets current product configuration"
  [context]
  (let [product-id (get-current-product-id context)
        products (get-products-config context)
        product (or (get-product-by-id context product-id)
                    (first products))]
    product))


(defn summary-path
  [product-config]
  (let [root (:root product-config "./docs")
        ;; Remove leading "./" from root if present
        root (if (str/starts-with? root "./")
               (subs root 2)
               root)]
    (utils/concat-filenames
     (utils/parent (:config product-config ".gitbook.yaml"))
     root
     (or (-> product-config :structure :summary) "SUMMARY.md"))))

(defn filepath [context filepath]
  (let [config
        (get-current-product context)
        root (:root config "./docs")
        ;; Remove leading "./" from root if present
        root (if (str/starts-with? root "./")
               (subs root 2)
               root)]
    (utils/concat-filenames
     (utils/parent (:config config ".gitbook.yaml"))
     root
     filepath)))

(defn uri [context docs-prefix uri]
  (utils/concat-urls
   docs-prefix
   (:path (get-current-product context))
   uri))

(defn readme-relative-path [context]
  (-> context
      (get-current-product)
      :structure
      :readme))

(defn readme-url [context]
  (utils/s->url-slug
   (str/replace
    (readme-relative-path context)
    #"README.md$"
    "")))

(defn path [context]
  (let [product (get-current-product context)
        product-path (when product (:path product))]
    (log/info "path lookup" {:product-id (when product (:id product))
                             :product-exists (boolean product)
                             :path product-path
                             :context-product-id (:current-product-id context)})
    product-path))
