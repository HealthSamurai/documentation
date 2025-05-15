(ns user
  (:require
   [system.dev :as dev]
   [system]
   [nextjournal.markdown :as md]
   [nextjournal.markdown.transform :as md.transform]
   [gitbok.core :as gitbok]))

(comment
  (dev/update-libs)

  (def context (system/start-system gitbok/default-config))
  (system/stop-system context)


  ) 