(ns build
  (:require
   [clojure.tools.build.api :as b]))

(def lib 'gitbok)
(def main 'gitbok.core)
(def class-dir "target/classes")
(def version
  (or (System/getenv "VERSION")
      (b/git-process {:git-args ["rev-parse" "--short" "HEAD"]})))

(defn- uber-opts [opts]
  (merge opts
         {:main main
          :uber-file (format "target/%s.jar" lib)
          :basis (b/create-basis {:project "deps.edn"})
          :class-dir class-dir
          :src-dirs ["src"]
          :ns-compile [main]}))

(defn uber [opts]
  (println "Cleaning...")
  (b/delete {:path "target"})

  (let [opts (uber-opts opts)
        workdir (System/getenv "WORKDIR")]
    (println "Copying files...")
    (b/copy-dir {:src-dirs (if workdir
                             ["resources" workdir ".gitbook"]
                             ["resources" "docs" ".gitbook"])
                 :target-dir class-dir})

    (b/copy-file {:src ".gitbook.yaml"
                  :target (str class-dir "/" ".gitbook.yaml")})

    (b/copy-file {:src (if workdir
                         (str workdir "/products.yaml")
                         "products.yaml")
                  :target (str class-dir "/" "products.yaml")})

    (println "VERSION " version)
    (b/write-file {:path (str class-dir "/version") :string version})

    (println "Compiling files...")
    (b/compile-clj opts)

    (println "Creating uberjar...")
    (b/uber opts)))

(defn -main []
  (uber {}))
