(ns user
  (:require
   [system.dev :as dev]
   [system]
   [clojure.string :as str]
   [gitbok.indexing.impl.uri-to-file :as uri-to-file]
   [gitbok.indexing.impl.file-to-uri :as file-to-uri]
   [gitbok.core :as gitbok]
   [gitbok.markdown.core :as markdown]))

(comment
  (dev/update-libs)

  (def context (system/start-system gitbok/default-config))
  (system/stop-system context)


  (def r "### Load dataset")
  (def parsed (:parsed (markdown/parse-markdown-content [nil r])))
  (markdown/render-md context nil parsed)

  (def uri->file-idx
    (uri-to-file/get-idx context))

  (take 10 uri->file-idx)

  (filter
    (fn [[url file]]
      (and
        ;; (str/starts-with? url "modules/aidbox-forms/aidbox-ui-builder-alpha/")
           (str/starts-with? file "modules/aidbox-forms/aidbox-ui-builder-alpha/README.md")
           )

      )
    uri->file-idx)

  (def file-to-uri
    (file-to-uri/get-idx context))

  (take 10 file-to-uri)

  (filter
    (fn [[file uri]]
      (str/starts-with? file "modules/aidbox-forms/aidbox-ui-builder"))
    file-to-uri)

  ;; bad pages
  (filter
    (fn [[file uri]]
      (and (not= uri (subs file 0 (- (count file) (count ".md"))))
           (not (str/starts-with? file "https"))
           (not (str/ends-with? file "README.md"))))
    file-to-uri)

  )

(subs "hello.md" 0 (- (count "hello.md") 3) )
