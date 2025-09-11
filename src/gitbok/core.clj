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

(defn init-products!
  "Initialize products configuration from products.yaml"
  [context]
  (let [products-config (init/init-products context)]
    (state/set-products! context products-config)
    (products/set-products-config context products-config) ; For legacy compatibility
    (log/info "Products initialized" {:count (count products-config)
                                      :products (mapv :name products-config)})
    products-config))

(defn init-product-indices!
  "Initialize indices for a specific product"
  [context product]
  (try
    (state/set-current-product! context product)
    ;; Add product info to context
    (let [context (assoc context
                         :current-product-id (:id product)
                         ::products/current-product product)]
      (init/init-product-indices context product))
    (log/info "Product indices initialized" {:product (:name product)})
    (catch Exception e
      (log/error e "Failed to initialize product indices" {:product (:name product)}))))

(defn init-all-product-indices!
  "Initialize indices for all products"
  [context]
  (let [products-config (state/get-products context)]
    (doseq [product products-config]
      (init-product-indices! context product))
    (log/info "All product indices initialized" {:count (count products-config)})))

(defn start!
  "Start the server with optional config overrides"
  ([] (start! {}))
  ([config-overrides]
   (let [context (state/init-state! config-overrides)
         port (state/get-config context :port)
         prefix (state/get-config context :prefix "")
         base-url (state/get-config context :base-url)
         version (state/get-config context :version)]

     ;; Initialize products
     (init-products! context)

     ;; Initialize all product indices
     (init-all-product-indices! context)

     ;; Create and start HTTP server
     (let [handler (routes/create-app context)
           server-instance (http-kit/run-server handler {:port port})]
       (state/set-server! context server-instance)
       (log/info "Server started" {:port port
                                   :prefix prefix
                                   :base-url base-url
                                   :version version}))

     ;; Start schedulers with context
     (when-let [_reload-schedule (scheduler/start-reload-watcher!
                                  context
                                  (partial init-product-indices! context)
                                  (partial init-products! context))]
       (log/info "Reload watcher started"))

     (when-let [_examples-schedule (scheduler/start-examples-updater! context)]
       (log/info "Examples updater started"))

     {:status :started
      :port port
      :context context})))

(defn stop!
  "Stop the server and all schedulers"
  [context]
  ;; Stop schedulers
  (scheduler/stop-all-schedulers! context)

  ;; Stop HTTP server
  (when-let [stop-fn (state/get-server context)]
    (stop-fn)
    (state/clear-server! context)
    (log/info "Server stopped"))

  {:status :stopped})

(defn restart!
  "Restart the server"
  [context]
  (stop! context)
  (Thread/sleep 1000) ; Give it a moment to clean up
  (start!))

(defn status
  "Get server status"
  [context]
  {:server (if (state/get-server context) :running :stopped)
   :schedulers (scheduler/scheduler-status context)
   :config (state/get-config context :prefix "")
   :products (count (state/get-products context))})

;; Development helpers
(defn reload-products!
  "Reload products configuration (for development)"
  [context]
  (init-products! context)
  (init-all-product-indices! context)
  (log/info "Products reloaded"))

(defn clear-caches!
  "Clear all caches (for development)"
  [context]
  (indexing/clear-all-caches context)
  (state/set-cache! context :lastmod {})
  (state/set-cache! context :reload-state {:git-head nil
                                   :last-reload-time nil
                                   :app-version (state/get-config context :version)
                                   :in-progress false})
  (log/info "All caches cleared"))

(defn -main [& _args]
  (log/info "Starting server")
  (let [{:keys [context]} (start!)]
    ;; Setup shutdown hook
    (.addShutdownHook (Runtime/getRuntime)
                      (Thread. (fn []
                                 (log/info "shutdown" {:msg "Got SIGTERM."})
                                 (stop! context))))))
