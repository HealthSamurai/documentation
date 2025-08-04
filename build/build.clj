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
    ;; Always copy resources and .gitbook
    (b/copy-dir {:src-dirs ["resources" ".gitbook"]
                 :target-dir class-dir})
    
    ;; Copy docs directory - need to maintain the docs/ prefix in the JAR
    (if workdir
      (do
        (println (str "Using WORKDIR: " workdir))
        ;; Create docs directory in target
        (.mkdirs (clojure.java.io/file class-dir "docs"))
        ;; Copy contents of docs to target-dir/docs
        (b/copy-dir {:src-dirs ["docs"]
                     :target-dir (str class-dir "/docs")})
        ;; Copy workdir for products.yaml and other configs
        (b/copy-dir {:src-dirs [workdir]
                     :target-dir class-dir}))
      ;; Legacy mode - create docs directory and copy contents
      (do
        (.mkdirs (clojure.java.io/file class-dir "docs"))
        (b/copy-dir {:src-dirs ["docs"]
                     :target-dir (str class-dir "/docs")})))

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
