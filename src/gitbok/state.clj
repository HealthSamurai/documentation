(ns gitbok.state
  (:require [clojure.tools.logging :as log]
            [clojure.java.io]))

(def empty-state
  {:config {}
   :products {}
   :cache {}
   :schedulers {}
   :runtime {}})

;; Resource loading functions - defined before init-state! which uses them
(declare get-config) ;; Forward declaration since get-config is defined later

(defn slurp-resource-init
  "Reads a resource for initialization when context is not yet available.
   Uses classpath only - no volume path support."
  [path]
  (if-let [r (clojure.java.io/resource path)]
    (do
      (log/debug "read classpath for init" {:path path})
      (slurp r))
    (do
      (log/error "file not found during init" {:path path})
      (throw (Exception. (str "Cannot find " path " in classpath"))))))

(defn init-state!
  "Initialize application state with config from environment.
   All System/getenv calls happen here at startup.
   Returns a system context that should be used for all subsequent operations."
  [& [{:keys [port prefix base-url dev-mode version
              github-token docs-volume-path docs-repo-path
              examples-update-interval reload-check-interval
              meilisearch-url meilisearch-api-key]}]]
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
                              (= "true" (System/getenv "DEV")))
                :version (or version
                             (slurp-resource-init "version"))
                ;; All environment variables are now at the same level
                :github-token (or github-token (System/getenv "GITHUB_TOKEN"))
                :docs-volume-path (or docs-volume-path (System/getenv "DOCS_VOLUME_PATH"))
                :docs-repo-path (or docs-repo-path (System/getenv "DOCS_REPO_PATH") ".")
                :examples-update-interval (or examples-update-interval
                                              (System/getenv "EXAMPLES_UPDATE_INTERVAL")
                                              "60")
                :reload-check-interval (or reload-check-interval
                                           (System/getenv "RELOAD_CHECK_INTERVAL")
                                           "30")
                :meilisearch-url (or meilisearch-url (System/getenv "MEILISEARCH_URL"))
                :meilisearch-api-key (or meilisearch-api-key (System/getenv "MEILISEARCH_API_KEY"))}
        initial-state {:config config
                       :products {:config []
                                  :indices {}}
                       :cache {:lastmod {}
                               :reload-state {:git-head nil
                                              :last-reload-time nil
                                              :app-version (:version config)
                                              :in-progress false}}
                       :schedulers {}
                       :runtime {}}]
    (log/info "State initialized" {:port (:port config)
                                   :prefix (:prefix config)
                                   :base-url (:base-url config)
                                   :dev-mode (:dev-mode config)
                                   :version (:version config)})
    ;; Return a context with the atom for all subsequent operations
    {:system (atom initial-state)}))

;; Core API functions
(defn get-state
  "Get value from state by path"
  ([context path] (get-state context path nil))
  ([context path default]
   (if-let [system (:system context)]
     (get-in @system path default)
     (do
       (log/warn "Get state: no :system in context!")
       nil))))

(defn set-state!
  "Set value in state by path"
  [context path value]
  (swap! (:system context) assoc-in path value)
  value)

(defn update-state!
  "Update value in state by path with function"
  [context path f & args]
  (apply swap! (:system context) update-in path f args))

;; Convenience getters for common paths
(defn get-config
  ([context] (get-state context [:config]))
  ([context k] (get-state context [:config k]))
  ([context k default] (get-state context [:config k] default)))

(defn slurp-resource
  "Reads a resource from volume path or classpath.
   Requires context to get volume-path from state."
  [context path]
  (when-not context
    (throw (IllegalArgumentException. "Context is required for slurp-resource")))
  (let [volume-path (get-config context :docs-volume-path)]
    (if volume-path
      (let [file-path (str volume-path "/" path)
            file (clojure.java.io/file file-path)]
        (if (.exists file)
          (do
            (log/debug "read volume" {:path path})
            (slurp file))
          ;; Fallback to classpath for non-documentation resources
          (if-let [r (clojure.java.io/resource path)]
            (do
              (log/debug "read classpath" {:path path :reason "not-in-volume"})
              (slurp r))
            (do
              (log/error "file not found" {:path path :locations ["volume" "classpath"]})
              (throw (Exception. (str "Cannot find " path " in volume or classpath")))))))
      ;; Original classpath logic when no volume-path configured
      (if-let [r (clojure.java.io/resource path)]
        (do
          (log/debug "read classpath" {:path path :reason "no-volume"})
          (slurp r))
        (do
          (log/error "file not found" {:path path :location "classpath"})
          (throw (Exception. (str "Cannot find " path))))))))

(defn get-products [context]
  (get-state context [:products :config] []))

(defn set-products! [context products]
  (set-state! context [:products :config] products))

(defn get-product-state
  "Get state for current product or specific product"
  ([context path] (get-product-state context path nil))
  ([context path default]
   ;; Product should now be in context from middleware/request
   (if-let [product-id (or (:current-product-id context)
                           (:id (:product context))
                           (:id (:gitbok.products/current-product context)))]
     (get-state context (concat [:products :indices product-id] path) default)
     (do
       (log/warn "No current product set when getting product state" {:path path})
       default))))

(defn set-product-state!
  "Set state for current product"
  [context path value]
  ;; Product should now be in context from middleware/request
  (if-let [product-id (or (:current-product-id context)
                          (:id (:product context))
                          (:id (:gitbok.products/current-product context)))]
    (set-state! context (concat [:products :indices product-id] path) value)
    (log/error "No current product set when setting product state" {:path path})))

;; Cache management
(defn get-cache
  ([context k] (get-state context [:cache k]))
  ([context k default] (get-state context [:cache k] default)))

(defn set-cache! [context k v]
  (set-state! context [:cache k] v))

(defn update-cache! [context k f & args]
  (apply update-state! context [:cache k] f args))

;; Scheduler management
(defn get-scheduler [context k]
  (get-state context [:schedulers k]))

(defn set-scheduler! [context k scheduler]
  (set-state! context [:schedulers k] scheduler))

(defn remove-scheduler! [context k]
  (swap! (:system context) update :schedulers dissoc k)
  nil)

;; Runtime state
(defn get-runtime
  ([context k] (get-state context [:runtime k]))
  ([context k default] (get-state context [:runtime k] default)))

(defn set-runtime! [context k v]
  (set-state! context [:runtime k] v))

;; Server management
(defn get-server [context]
  (get-runtime context :server))

(defn set-server! [context server-instance]
  (set-runtime! context :server server-instance))

(defn clear-server! [context]
  (set-runtime! context :server nil))

;; Product indices and data management
(defn set-summary! [context summary-data]
  (set-product-state! context [:summary-hiccup] summary-data))

(defn get-summary [context]
  (get-product-state context [:summary-hiccup]))

(defn set-file-to-uri-idx! [context idx]
  (set-product-state! context [:file-to-uri-idx] idx))

(defn get-file-to-uri-idx [context]
  (get-product-state context [:file-to-uri-idx]))

(defn set-uri-to-file-idx! [context idx]
  (set-product-state! context [:uri-to-file-idx] idx))

(defn get-uri-to-file-idx [context]
  (get-product-state context [:uri-to-file-idx]))

(defn set-redirects-idx! [context idx]
  (set-product-state! context [:redirects-idx] idx))

(defn get-redirects-idx [context]
  (get-product-state context [:redirects-idx]))

(defn set-sitemap! [context sitemap-data]
  (set-product-state! context [:sitemap] sitemap-data))

(defn get-sitemap [context]
  (get-product-state context [:sitemap]))

(defn set-md-files-idx! [context idx]
  (set-product-state! context [:md-files-idx] idx))

(defn get-md-files-idx [context]
  (get-product-state context [:md-files-idx]))

(defn set-parsed-markdown-idx! [context idx]
  (set-product-state! context [:parsed-markdown-idx] idx))

(defn get-parsed-markdown-idx [context]
  (get-product-state context [:parsed-markdown-idx]))

(defn set-navigation-links! [context links]
  (set-product-state! context [:navigation-links] links))

(defn get-navigation-links [context]
  (get-product-state context [:navigation-links]))

(defn set-examples-data! [context data]
  (set-product-state! context [:examples-data] data))

(defn get-examples-data [context]
  (get-product-state context [:examples-data]))

;; Debug helper
(defn get-full-state [context]
  @(:system context))

(defn reset-state! [context]
  (reset! (:system context) empty-state))
