(ns user
  (:require
   [system.dev :as dev]
   [system]
   [clojure.string :as str]
   [gitbok.indexing.impl.uri-to-file :as uri-to-file]
   [gitbok.core :as gitbok]
   [clj-reload.core :as reload]
   [klog.core :as log]
   [gitbok.reload :as gitbok-reload]
   [gitbok.markdown.core :as markdown]))

(comment

  ;; run server
  (do (reload/init {:dirs ["src"]})
      (log/stdout-pretty-appender :debug)
      (def context (system/start-system gitbok/default-config))
      ;; Start reload watcher if DOCS_VOLUME_PATH is set
      (when (and context (System/getenv "DOCS_VOLUME_PATH"))
        (gitbok-reload/start-reload-watcher context
                                            gitbok/init-product-indices
                                            gitbok/init-products)))

  ;; reload server
  (do (reload/reload)
      (system/stop-system context)
      (def context (system/start-system gitbok/default-config)))

  (dev/update-libs)

  )
