(ns gitbok.markdown.widgets.image
  (:require
   [clojure.string :as str]
   [nextjournal.markdown.utils :as u]))

(defn image-renderer [_ctx node]
  (let [src (some-> node :attrs :src)
        alt (or (:alt node)
                (:title (:attrs node))
                (:text (first (:content node)))  "")
        webp-src (when src (str (subs src 0 (clojure.string/last-index-of src ".")) ".webp"))
        fallback-type
        (cond
          (clojure.string/ends-with? src ".png") "image/png"
          (or (clojure.string/ends-with? src ".jpg")
              (clojure.string/ends-with? src ".jpeg")) "image/jpeg"
          :else "image/jpeg")]
    [:picture
     [:source {:srcset webp-src :type "image/webp"}]
     [:source {:srcset src :type fallback-type}]
     [:img {:src src
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
