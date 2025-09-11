(ns gitbok.core
  (:require
   [clojure.tools.logging :as log]
   [org.httpkit.server :as http-kit]
   [gitbok.routes :as routes]
   [gitbok.state :as state]
   [gitbok.scheduler :as scheduler]
   [gitbok.init :as init]
   [gitbok.products :as products]
   [gitbok.indexing.core :as indexing])
  (:gen-class))

(set! *warn-on-reflection* true)

;; Server management functions (moved from gitbok.server)

;; Forward declaration
(declare stop!)

(defn init-products!
  "Initialize products configuration from products.yaml"
  []
  ;; Create a minimal context for compatibility with old code
  (let [context {:prefix (state/get-config :prefix "")
                 :base-url (state/get-config :base-url)
                 :dev-mode (state/get-config :dev-mode)}
        products-config (init/init-products context)]
    (state/set-products! products-config)
    (products/set-products-config context products-config) ; For legacy compatibility
    (log/info "Products initialized" {:count (count products-config)
                                      :products (mapv :name products-config)})
    products-config))

(defn init-product-indices!
  "Initialize indices for a specific product"
  [product]
  (try
    (state/set-current-product! product)
    ;; Create context with current product for legacy code
    (let [context {:prefix (state/get-config :prefix "")
                   :base-url (state/get-config :base-url)
                   :dev-mode (state/get-config :dev-mode)
                   :current-product-id (:id product)
                   ::products/current-product product}]
      (init/init-product-indices context product))
    (log/info "Product indices initialized" {:product (:name product)})
    (catch Exception e
      (log/error e "Failed to initialize product indices" {:product (:name product)}))))

(defn init-all-product-indices!
  "Initialize indices for all products"
  []
  (let [products-config (state/get-products)]
    (doseq [product products-config]
      (init-product-indices! product))
    (log/info "All product indices initialized" {:count (count products-config)})))

(defn start!
  "Start the server with optional config overrides"
  ([] (start! {}))
  ([config-overrides]
   (when (state/get-server)
     (log/warn "Server already running, stopping first")
     (stop!))

   ;; Initialize state with environment variables
   (let [config (state/init-state! config-overrides)
         port (:port config)
         prefix (:prefix config)
         base-url (:base-url config)
         version (:version config)]

     ;; ;; Set legacy http module values for compatibility
     ;; (gitbok.http/set-port nil port)
     ;; (gitbok.http/set-prefix nil prefix)
     ;; (gitbok.http/set-base-url nil base-url)
     ;; (gitbok.http/set-version nil version)
     ;; (gitbok.http/set-dev-mode nil (:dev-mode config))

     ;; Initialize products
     (init-products!)

     ;; Initialize all product indices
     (init-all-product-indices!)

     ;; Create and start HTTP server
     (let [handler (routes/create-app)
           server-instance (http-kit/run-server handler {:port port})]
       (state/set-server! server-instance)
       (log/info "Server started" {:port port
                                   :prefix prefix
                                   :base-url base-url
                                   :version version}))

     ;; Start schedulers
     (when-let [reload-schedule (scheduler/start-reload-watcher!
                                 init-product-indices!
                                 init-products!)]
       (log/info "Reload watcher started"))

     (when-let [examples-schedule (scheduler/start-examples-updater!)]
       (log/info "Examples updater started"))

     {:status :started
      :port port
      :config config})))

(defn stop!
  "Stop the server and all schedulers"
  []
  ;; Stop schedulers
  (scheduler/stop-all-schedulers!)

  ;; Stop HTTP server
  (when-let [stop-fn (state/get-server)]
    (stop-fn)
    (state/clear-server!)
    (log/info "Server stopped"))

  {:status :stopped})

(defn restart!
  "Restart the server"
  []
  (stop!)
  (Thread/sleep 1000) ; Give it a moment to clean up
  (start!))

(defn status
  "Get server status"
  []
  {:server (if (state/get-server) :running :stopped)
   :schedulers (scheduler/scheduler-status)
   :config (state/get-config)
   :products (count (state/get-products))})

;; Development helpers
(defn reload-products!
  "Reload products configuration (for development)"
  []
  (init-products!)
  (init-all-product-indices!)
  (log/info "Products reloaded"))

(defn clear-caches!
  "Clear all caches (for development)"
  []
  (indexing/clear-all-caches)
  (state/set-cache! :lastmod {})
  (state/set-cache! :reload-state {:git-head nil
                                   :last-reload-time nil
                                   :app-version (state/get-config :version)
                                   :in-progress false})
  (log/info "All caches cleared"))

(defn -main [& _args]
  (log/info "Starting server")
  (start!)

  ;; Setup shutdown hook
  (.addShutdownHook (Runtime/getRuntime)
                    (Thread. (fn []
                               (log/info "shutdown" {:msg "Got SIGTERM."})
                               (stop!)))))