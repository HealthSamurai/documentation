(ns user
  (:require
   [system.dev :as dev]
   [system]
   [clojure.string :as str]
   [gitbok.indexing.impl.uri-to-file :as uri-to-file]
   [gitbok.core :as gitbok]
   [gitbok.markdown.core :as markdown]))

(comment
  (dev/update-libs)

  (def context (system/start-system gitbok/default-config))
  (system/stop-system context)

  (def uri->file-idx
    (uri-to-file/get-idx context))

  (take 10 uri->file-idx)
  (filter
    (fn [[url file]]
      (str/starts-with? url "modules/aidbox-forms/aidbox-ui-builder-alpha"))
    uri->file-idx)

  (def r "### Load dataset")
  (def parsed (:parsed (markdown/parse-markdown-content [nil r])))
  (markdown/render-md context nil parsed)

  )
