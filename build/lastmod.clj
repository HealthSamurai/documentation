(ns lastmod
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
   [clojure.java.shell :as shell]))

(defn lastmod-for-file [file]
  (let [{:keys [out exit]}
        (shell/sh "git" "log" "-1" "--format=%ct" (.getPath ^java.io.File file))]
    (when (and exit (zero? exit))
      (let [timestamp (Long/parseLong (str/trim out))
            instant (java.time.Instant/ofEpochSecond timestamp)]
        (.toString instant)))))

(defn generate-lastmod-file! [docs-dir save-path]
  (println "Generating lastmod.edn...")
  (let [md-files (->> (file-seq (io/file docs-dir))
                      (filter #(and (.isFile %)
                                    (.endsWith (.getName %) ".md"))))
        data (->> md-files
                  (map (fn [f]
                         (let [rel (.relativize (.toPath (io/file docs-dir))
                                                (.toPath f))
                               date (lastmod-for-file f)]
                           (when date
                             [(str rel) date]))))
                  (remove nil?)
                  (into (sorted-map)))]
    (spit save-path (pr-str data))
    (println "Saved:" save-path)))

(defn -main [& _]
  (generate-lastmod-file!
   "docs"
   "resources/lastmod.edn")
  (System/exit 0))
