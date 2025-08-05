(ns lastmod
  (:require
   [clojure.java.io :as io]
   [clojure.string :as str]
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

(defn -main [& args]
  (cond
    ;; If no args, generate for default docs directory (backward compatibility)
    (empty? args)
    (do
      (generate-lastmod-file! "docs" "resources/lastmod.edn")
      (System/exit 0))

    ;; If one arg "all", generate for all products
    (= (first args) "all")
    (do
      ;; Generate for aidbox product
      (generate-lastmod-file!
       "docs-new/aidbox/docs"
       "resources/lastmod/lastmod-aidbox.edn")

      ;; Generate for forms product
      (generate-lastmod-file!
       "docs-new/forms/docs"
       "resources/lastmod/lastmod-forms.edn")

      ;; Generate for default (backward compatibility)
      (when (.exists (io/file "docs"))
        (generate-lastmod-file!
         "docs"
         "resources/lastmod/lastmod-default.edn"))

      (System/exit 0))

    ;; If two args provided: docs-dir and save-path
    (= (count args) 2)
    (do
      (generate-lastmod-file! (first args) (second args))
      (System/exit 0))

    ;; Otherwise show usage
    :else
    (do
      (println "Usage:")
      (println "  bb lastmod.clj                    # Generate for default docs/ directory")
      (println "  bb lastmod.clj all                # Generate for all products")
      (println "  bb lastmod.clj <docs-dir> <save-path>  # Generate for specific directory")
      (System/exit 1))))
