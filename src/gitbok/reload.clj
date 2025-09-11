(ns gitbok.reload
  (:require
   [clojure.java.io :as io]
   [clojure.java.shell :as shell]
   [clojure.string :as str]
   [gitbok.state :as state]
   [gitbok.products :as products]
   [gitbok.indexing.core :as indexing]
   [clojure.tools.logging :as log]))

(defn get-volume-path
  "Get current volume path, with fallback to local docs/ directory for development"
  []
  (or (state/get-env :docs-volume-path)
      (when (.exists (io/file "docs"))
        (log/info "using local docs" {:path "docs"})
        "docs")
      (when (.exists (io/file "../docs"))
        (log/info "using local docs" {:path "../docs"})
        "../docs")))

(defn get-reload-check-interval-ms []
  (* 1000 (Integer/parseInt (state/get-env :reload-check-interval "60"))))

;; Removed checksum functions - now using git HEAD for change detection

;; State management functions
(defn get-reload-state []
  (state/get-cache :reload-state
                   {:git-head nil
                    :last-reload-time nil
                    :app-version nil
                    :in-progress false}))

(defn set-reload-state! [reload-state]
  (state/set-cache! :reload-state reload-state))

(defn update-reload-state! [f & args]
  (state/update-cache! :reload-state
                       (fn [state]
                         (apply f (or state {}) args))))

;; Removed checksum getters/setters - using git HEAD instead

(defn is-reloading? []
  (:in-progress (get-reload-state)))

(defn set-reloading! [value]
  (update-reload-state! assoc :in-progress value))

(defn rebuild-all-caches
  "Build complete new cache for all products.
   This reuses existing initialization logic."
  [init-product-indices-fn init-products-fn]
  (log/info "📦rebuild start" {:action "re-initializing-products"})
  ;; Simply re-initialize all products
  ;; This will reload products.yaml and all documentation
  (let [products-config (init-products-fn)
        product-count (count products-config)]
    (log/info "📚products found" {:count product-count})
    ;; Rebuild cache for each product
    (doseq [[idx product] (map-indexed vector products-config)]
      (let [product-name (:name product)
            product-id (:id product)]
        (log/info "rebuilding product" {:product-id product-id
                                        :product-name product-name
                                        :index (inc idx)
                                        :total product-count})
        (let [start-time (System/currentTimeMillis)]
          ;; This will:
          ;; 1. Read SUMMARY.md
          ;; 2. Build URI mappings
          ;; 3. Load all markdown files
          ;; 4. Parse markdown
          ;; 5. Pre-render pages
          ;; 6. Generate sitemap
          (init-product-indices-fn product)
          (let [duration (- (System/currentTimeMillis) start-time)]
            (log/info "product rebuilt" {:product-id product-id
                                         :product-name product-name
                                         :duration-ms duration})))))
    (log/info "rebuild complete" {:product-count product-count})
    true))

(defn check-and-reload!
  "Check if git HEAD changed and reload if needed.
   This ensures only one reload happens at a time."
  [init-product-indices-fn init-products-fn]
  (when (and (get-volume-path)
             (not (is-reloading?)))
    (let [;; Get current git HEAD
          repo-path (state/get-env :docs-repo-path ".")
          new-head (try
                     (let [{:keys [out exit]} (shell/sh "git"
                                                        "-c" (str "safe.directory=" repo-path)
                                                        "rev-parse" "HEAD"
                                                        :dir repo-path)]
                       (when (zero? exit)
                         (str/trim out)))
                     (catch Exception e
                       (log/warn "⚠️get head failed" {:error (.getMessage e)})
                       nil))
          reload-state (get-reload-state)
          current-head (:git-head reload-state)
          app-version (state/get-config :version)]

      (log/info "check status" {:🏷️app-version app-version
                                :📌current-head (or current-head "none")
                                :🆕new-head (or new-head "none")})

      (cond
        (not new-head)
        (log/warn "check skipped" {:reason "cannot-get-git-head"})

        (and new-head (not= new-head current-head))
        (do
          (log/info "📊repo changed" {:old-head (or current-head "none")
                                      :new-head new-head})

          (set-reloading! true)
          (try
            (let [start-time (System/currentTimeMillis)]
              (log/info "🚀reload start" {:action "starting-reload"})

              ;; Build new caches
              (rebuild-all-caches init-product-indices-fn init-products-fn)

              ;; Update lastmod for all products
              (log/info "🔄updating lastmod data")
              (doseq [product (state/get-products)]
                (state/set-current-product! product)
                (indexing/set-lastmod))

              ;; Update state only after successful reload
              (update-reload-state! assoc
                                   :git-head new-head
                                   :last-reload-time (java.util.Date.)
                                   :app-version app-version)

              (let [duration (- (System/currentTimeMillis) start-time)]
                (log/info "reload success" {:duration-ms duration
                                            :new-head new-head})))
            (catch Exception e
              (log/error "reload failed" {:error (.getMessage e)
                                          :exception e}))
            (finally
              (set-reloading! false))))

        :else
        (log/info "📚docs unchanged")))))
;; Removed separate lastmod updater - now integrated into check-and-reload!

(defn init-reload-state!
  "Initialize reload state at startup"
  []
  (when-let [docs-path (get-volume-path)]
    (let [app-version (state/get-config :version)
          repo-path (state/get-env :docs-repo-path ".")]

      (log/info "reload state init" {:app-version app-version
                                     :volume-path docs-path
                                     :repo-path repo-path})

      ;; Get initial git HEAD
      (let [initial-head (try
                           (let [{:keys [out exit]} (shell/sh "git"
                                                              "-c" (str "safe.directory=" repo-path)
                                                              "rev-parse" "HEAD"
                                                              :dir repo-path)]
                             (when (zero? exit)
                               (str/trim out)))
                           (catch Exception e
                             (log/warn "⚠️get initial head failed" {:error (.getMessage e)})
                             nil))]
        (set-reload-state!
         {:git-head initial-head
          :last-reload-time (java.util.Date.)
          :app-version app-version
          :in-progress false})
        (log/info "initial git head" {:head initial-head
                                      :path repo-path})))))

