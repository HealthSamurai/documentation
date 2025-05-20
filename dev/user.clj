(ns user
  (:require
   [system.dev :as dev]
   [system]
   [gitbok.core :as gitbok]
   ;; [nextjournal.clerk :as clerk]
   [nextjournal.markdown :as md]
   [nextjournal.markdown.transform :as md.transform]
   [nextjournal.markdown.utils :as u]
   [edamame.core :as edamame]
   [clojure.zip :as z]))

(comment
  (dev/update-libs)

  (def context (system/start-system gitbok/default-config))
  (system/stop-system context))
