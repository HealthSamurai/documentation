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
                     :target-dir class-dir})
        ;; Handle aidbox specially - it uses symlinks to the main docs
        (let [aidbox-dir (clojure.java.io/file workdir "aidbox")]
          (when (.exists aidbox-dir)
            (println "Copying docs to aidbox/docs")
            (.mkdirs (clojure.java.io/file class-dir "aidbox/docs"))
            (b/copy-dir {:src-dirs ["docs"]
                         :target-dir (str class-dir "/aidbox/docs")})))
        ;; Copy nested docs directories from other products
        (doseq [product-dir (.listFiles (clojure.java.io/file workdir))
                :when (and (.isDirectory product-dir)
                           (not= (.getName product-dir) "aidbox"))]
          (let [docs-dir (clojure.java.io/file product-dir "docs")]
            (when (.exists docs-dir)
              (println (str "Copying " (.getName product-dir) "/docs"))
              (b/copy-dir {:src-dirs [(.getPath docs-dir)]
                           :target-dir (str class-dir "/" (.getName product-dir) "/docs")})))))
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
