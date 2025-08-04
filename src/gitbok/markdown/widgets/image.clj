(ns gitbok.markdown.widgets.image
  (:require
   [clojure.string :as str]
   [gitbok.indexing.core :as indexing]
   [gitbok.http]
   [nextjournal.markdown.utils :as u]))

(defn image-renderer [context filepath _ctx node]
  (let [src (some-> node :attrs :src)
        _ (when src (println "Original image src:" src))
        ;; Normalize .gitbook/assets paths - remove ../ prefixes
        normalized-src (when src
                         (if (str/includes? src ".gitbook/assets")
                           (let [result (str ".gitbook/assets" (last (str/split src #"\.gitbook/assets")))]
                             (println "Normalized src:" result)
                             result)
                           src))
        ;; Handle different types of image paths
        processed-src (cond
                        ;; Empty or nil src
                        (str/blank? normalized-src) ""

                        ;; External URLs - keep as is
                        (str/starts-with? normalized-src "http") normalized-src

                        ;; .gitbook/assets paths - already normalized, just return
                        (str/starts-with? normalized-src ".gitbook/assets") normalized-src

                        ;; Other relative paths - use the standard link resolution
                        :else (indexing/filepath->href context filepath normalized-src))
        _ (when processed-src (println "Processed src:" processed-src))

        alt (or (:alt node)
                (:title (:attrs node))
                (:text (first (:content node))) "")

        webp-src (when (and normalized-src (not (str/blank? normalized-src)))
                   (let [base-src (str (subs normalized-src 0 (clojure.string/last-index-of normalized-src ".")) ".webp")]
                     (cond
                       ;; External URLs - keep as is
                       (str/starts-with? base-src "http") base-src

                       ;; .gitbook/assets paths - already normalized
                       (str/starts-with? base-src ".gitbook/assets") base-src

                       ;; Other relative paths - use the standard link resolution
                       :else (indexing/filepath->href context filepath base-src))))

        fallback-type
        (cond
          (and normalized-src (clojure.string/ends-with? normalized-src ".png")) "image/png"
          (and normalized-src (or (clojure.string/ends-with? normalized-src ".jpg")
                                  (clojure.string/ends-with? normalized-src ".jpeg"))) "image/jpeg"
          :else "image/jpeg")]
    [:picture
     [:source {:srcset webp-src :type "image/webp"}]
     [:source {:srcset processed-src :type fallback-type}]
     [:img {:src processed-src
            :alt alt
            :loading "lazy"
            :class "max-w-full h-auto mx-auto rounded-lg my-6"}]]))

(def youtube-tokenizer
  (u/normalize-tokenizer
   {:regex
    #"\{\%.*embed(.*?)\%\}"
    :handler
    (fn [match]
      {:type :embed
       :body
       (match 1)})}))

(defn hack-youtube
  [md]
  (-> md
      (str/replace #"url=\"https://www.youtube.com(.*?)\"" "url=\"youtube $1\"")
      (str/replace #"url=\"https://youtu.be(.*?)\"" "url=\"youtube $1\"")))

(defn hack-other-websites [md]
  (-> md
      (clojure.string/replace
       #"\{\% embed url=\"([^\"]+)\" %\}\s*(.*?)\s*\{\% endembed %\}"
       "{% embed url=\"$1\" title=\"$2\" %}")
      (str/replace #"url=\"https://(.*?)\"" "url=\"$1\"")))
