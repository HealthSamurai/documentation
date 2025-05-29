(ns user
  (:require
   [system.dev :as dev]
   [system]
   [gitbok.core :as gitbok]
   [gitbok.markdown.core]))

(comment
  (dev/update-libs)

  (def context (system/start-system gitbok/default-config))
  (system/stop-system context))
