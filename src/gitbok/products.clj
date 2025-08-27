(ns gitbok.products
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
   [clj-yaml.core :as yaml]
   [gitbok.utils :as utils]
   [system]))

(def default-aidbox
  [{:id "aidbox"
    :name "Aidbox Documentation"
    :path "/aidbox"
    :config "aidbox/.gitbook.yaml"}])

(defn read-product-config-file [config-file]
  (yaml/parse-string (utils/slurp-resource config-file)))

(def volume-path (System/getenv "DOCS_VOLUME_PATH"))

(defn load-products-config
  "Loads products configuration from products.yaml in classpath or volume"
  []
  (try
    (let [config-str (if volume-path
                       ;; Try volume first, then fallback to classpath
                       (let [file (io/file volume-path "products.yaml")]
                         (println "Loading products.yaml from volume:" (.getPath file))
                         (if (.exists file)
                           (slurp file)
                           ;; Volume path set but file not found - try classpath
                           (do
                             (println "  File not found in volume, trying classpath")
                             (utils/slurp-resource "products.yaml"))))
                       ;; Read from classpath
                       (utils/slurp-resource "products.yaml"))
          config (yaml/parse-string config-str)
          products (mapv
                    #(merge
                      %
                      (read-product-config-file (:config %)))
                    (:products config))]
      {:products products
       :root-redirect (:root-redirect config)})
    (catch Exception e
      (println "ERROR loading products.yaml:" (.getMessage e))
      (println "Falling back to default-aidbox")
      {:products default-aidbox})))

(defn get-current-product-id
  "Gets current product ID from request context"
  [context]
  (:current-product-id context "default"))

(defn get-product-state
  "Gets state for a specific product"
  [context path & [default]]
  (or (try
        (let [product-id (get-current-product-id context)]
          (system/get-system-state context
                                   (concat [::products product-id] path)
                                   default))
        (catch Exception _
          (try
            (system/get-system-state context path default)
            (catch Exception _
              default))))
      default))

(defn set-product-state
  "Sets state for a specific product"
  [context path value]
  (try
    (let [product-id (get-current-product-id context)]
      (system/set-system-state context
                               (concat [::products product-id] path)
                               value))
    (catch Exception _
      (try
        (system/set-system-state context path value)
        (catch Exception _ nil)))))

(defn determine-product-by-uri
  "Determines product from request URI"
  [products uri]
  (or (first (filter #(str/starts-with? uri (:path %))
                     (sort-by #(- (count (:path %))) products)))
      (first (filter #(= (:id %) "default") products))
      (first products)))

(defn get-products-config
  "Gets all products configuration from context"
  [context]
  (system/get-system-state context [::products-config] []))

(defn get-full-config
  "Gets full configuration including products and root-redirect"
  [context]
  (system/get-system-state context [::full-config] {}))

(defn set-products-config
  "Saves products configuration to context"
  [context products]
  (system/set-system-state context [::products-config] products))

(defn set-full-config
  "Saves full configuration to context"
  [context config]
  (system/set-system-state context [::full-config] config))

(defn get-product-by-id
  "Gets product configuration by its ID"
  [context product-id]
  (let [products (get-products-config context)]
    (first (filter #(= (:id %) product-id) products))))

(defn get-current-product
  "Gets current product configuration"
  [context]
  (let [product-id (get-current-product-id context)
        products (get-products-config context)]
    (or (get-product-by-id context product-id)
        (first products))))

(defn set-current-product-id
  "Sets current product ID in context"
  [context product-id]
  (assoc context :current-product-id product-id))

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
  (:path (get-current-product context)))
