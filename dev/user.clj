(ns user
  (:require
   [system.dev :as dev]
   [system]
   [gitbok.core :as gitbok]
   [clj-reload.core :as reload]
   [gitbok.examples.updater :as examples-updater]
   [klog.core :as log]
   [gitbok.reload :as gitbok-reload]))

(comment

  ;; run server
  (do (reload/init {:dirs ["src"]})
      (log/stdout-pretty-appender :debug)
      (def context (system/start-system gitbok/default-config))
      ;; Start reload watcher if DOCS_VOLUME_PATH is set
      #_(when (and context (System/getenv "DOCS_VOLUME_PATH"))
          (gitbok-reload/start-reload-watcher context
                                              gitbok/init-product-indices
                                              gitbok/init-products))
      (examples-updater/start-scheduler context))

  ;; reload server
  (do
    (system/stop-system context)
    (reload/reload)
    (def context (system/start-system gitbok/default-config)))

  (dev/update-libs))
