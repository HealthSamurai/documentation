(ns lastmod
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
   [clojure.pprint :as pprint]
   [clojure.java.shell :as shell]))

(defn lastmod-for-file [file]
  (let [canonical-file (.getCanonicalFile file)
        {:keys [out exit]}
        (shell/sh "git" "log" "-1" "--format=%ct" (.getPath canonical-file))]
    (when (and exit (zero? exit) (not (str/blank? out)))
      (try
        (let [timestamp (Long/parseLong (str/trim out))
              instant (java.time.Instant/ofEpochSecond timestamp)]
          (.toString instant))
        (catch Exception _
          ;; If parsing fails, return nil
          nil)))))

(defn generate-lastmod-file! [docs-dir save-path]
  (println "Generating " save-path)
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
    (with-open [w (io/writer save-path)]
      (pprint/pprint data w))
    (println "Saved:" save-path)))

(defn -main [& args]
  (println "start lastmod generation script ")

    ;; Generate for aidbox product
  (generate-lastmod-file!
   "docs-new/aidbox/docs"
   "resources/lastmod/lastmod-aidbox.edn")

    ;; Generate for fhirbase product
  (generate-lastmod-file!
   "docs-new/fhirbase/docs"
   "resources/lastmod/lastmod-fhirbase.edn")

    ;; Generate for default (backward compatibility)
  (when (.exists (io/file "docs"))
    (generate-lastmod-file!
     "docs"
     "resources/lastmod/lastmod-default.edn"))
  (println "end lastmod generation script")

  (System/exit 0))


(when (= *file* (System/getProperty "babashka.file"))
  (apply -main *command-line-args*))
