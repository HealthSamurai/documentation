(ns gitbok.core
  (:require
   [clojure.tools.logging :as log]
   [org.httpkit.server :as http-kit]
   [gitbok.routes :as routes]
   [gitbok.state :as state]
   [gitbok.scheduler :as scheduler]
   [gitbok.init :as init])
  (:gen-class))

(set! *warn-on-reflection* true)

(defn init-products!
  "Initialize products configuration from products.yaml"
  [context]
  (let [products-config (init/init-products context)]
    (state/set-products! context products-config)
    (log/info "Products initialized" {:count (count products-config)
                                      :products (mapv :name products-config)})
    products-config))

(defn init-product-indices!
  "Initialize indices for a specific product"
  [context product]
  (try
    (init/init-product-indices context product)
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
      :port port})))

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

(defn -main [& _args]
  (log/info "Starting server")
  (let [{:keys [context]} (start!)]
    ;; Setup shutdown hook
    (.addShutdownHook (Runtime/getRuntime)
                      (Thread. (fn []
                                 (log/info "shutdown" {:msg "Got SIGTERM."})
                                 (stop! context))))))
