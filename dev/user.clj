(ns user
  (:require
   [system.dev :as dev]
   [system]
   [clojure.string :as str]
   [gitbok.indexing.impl.uri-to-file :as uri-to-file]
   [gitbok.core :as gitbok]
   [gitbok.markdown.core]))

(comment
  (dev/update-libs)

  (def context (system/start-system gitbok/default-config))
  (system/stop-system context)

  (def uri->file-idx
    (uri-to-file/get-idx context))

  (take 10 uri->file-idx)
  (filter
   (fn [[url file]]
     (str/starts-with? url "readme"))
   uri->file-idx))
