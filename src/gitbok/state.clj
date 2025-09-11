(ns gitbok.state
  (:require [gitbok.utils :as utils]
            [clojure.tools.logging :as log]))

(defonce ^:private app-state
  (atom {:config {}
         :products {}
         :cache {}
         :schedulers {}
         :runtime {}}))

(defn init-state!
  "Initialize application state with config from environment.
   All System/getenv calls happen here at startup."
  [& [{:keys [port prefix base-url dev-mode version]}]]
  (let [config {:port (or port
                          (when-let [env-port (System/getenv "PORT")]
                            (Integer/parseInt env-port))
                          8080)
                :prefix (or prefix
                            (System/getenv "DOCS_PREFIX")
                            "")
                :base-url (or base-url
                              (System/getenv "BASE_URL")
                              "http://localhost:8080")
                :dev-mode (or dev-mode
                              (= "true" (System/getenv "DEV_MODE")))
                :version (or version
                             (utils/slurp-resource "version"))
                ;; Store environment variables that will be used later
                :env {:github-token (System/getenv "GITHUB_TOKEN")
                      :docs-volume-path (System/getenv "DOCS_VOLUME_PATH")
                      :docs-repo-path (or (System/getenv "DOCS_REPO_PATH") ".")
                      :examples-update-interval (or (System/getenv "EXAMPLES_UPDATE_INTERVAL") "60")
                      :reload-check-interval (or (System/getenv "RELOAD_CHECK_INTERVAL") "30")
                      :meilisearch-url (System/getenv "MEILISEARCH_URL")
                      :meilisearch-api-key (System/getenv "MEILISEARCH_API_KEY")}}]
    (reset! app-state
            {:config config
             :products {:config []
                        :current nil
                        :indices {}}
             :cache {:lastmod {}
                     :reload-state {:git-head nil
                                    :last-reload-time nil
                                    :app-version (:version config)
                                    :in-progress false}}
             :schedulers {}
             :runtime {}})
    (log/info "State initialized" {:port (:port config)
                                   :prefix (:prefix config)
                                   :base-url (:base-url config)
                                   :dev-mode (:dev-mode config)
                                   :version (:version config)})
    config))

;; Core API functions
(defn get-state
  "Get value from state by path"
  ([path] (get-state path nil))
  ([path default]
   (get-in @app-state path default)))

(defn set-state!
  "Set value in state by path"
  [path value]
  (swap! app-state assoc-in path value)
  value)

(defn update-state!
  "Update value in state by path with function"
  [path f & args]
  (apply swap! app-state update-in path f args))

(defn merge-state!
  "Merge map into state at path"
  [path m]
  (swap! app-state update-in path merge m))

;; Convenience getters for common paths
(defn get-config
  ([] (get-state [:config]))
  ([k] (get-state [:config k]))
  ([k default] (get-state [:config k] default)))

(defn get-env
  "Get environment variable stored at startup"
  ([k] (get-state [:config :env k]))
  ([k default] (get-state [:config :env k] default)))

(defn get-products []
  (get-state [:products :config] []))

(defn set-products! [products]
  (set-state! [:products :config] products))

(defn get-current-product []
  (get-state [:products :current]))

(defn set-current-product! [product]
  (set-state! [:products :current] product))

(defn get-product-state
  "Get state for current product or specific product"
  ([path] (get-product-state path nil))
  ([path default]
   (if-let [product-id (:id (get-current-product))]
     (get-state (concat [:products :indices product-id] path) default)
     (do
       (log/warn "No current product set when getting product state" {:path path})
       default))))

(defn set-product-state!
  "Set state for current product"
  [path value]
  (if-let [product-id (:id (get-current-product))]
    (set-state! (concat [:products :indices product-id] path) value)
    (log/error "No current product set when setting product state" {:path path})))

;; Cache management
(defn get-cache
  ([k] (get-state [:cache k]))
  ([k default] (get-state [:cache k] default)))

(defn set-cache! [k v]
  (set-state! [:cache k] v))

(defn update-cache! [k f & args]
  (apply update-state! [:cache k] f args))

;; Scheduler management
(defn get-scheduler [k]
  (get-state [:schedulers k]))

(defn set-scheduler! [k scheduler]
  (set-state! [:schedulers k] scheduler))

(defn remove-scheduler! [k]
  (swap! app-state update :schedulers dissoc k)
  nil)

;; Runtime state
(defn get-runtime
  ([k] (get-state [:runtime k]))
  ([k default] (get-state [:runtime k] default)))

(defn set-runtime! [k v]
  (set-state! [:runtime k] v))

;; Server management
(defn get-server []
  (get-runtime :server))

(defn set-server! [server-instance]
  (set-runtime! :server server-instance))

(defn clear-server! []
  (set-runtime! :server nil))

;; Debug helper
(defn get-full-state []
  @app-state)

(defn reset-state! []
  (reset! app-state {:config {}
                     :products {}
                     :cache {}
                     :schedulers {}
                     :runtime {}}))
