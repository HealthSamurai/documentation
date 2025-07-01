(ns gitbok.markdown.widgets.image
  (:require
    [clojure.string :as str]
   [nextjournal.markdown.utils :as u]))

(def github-image-regex #"\!\[([^\]]*)\]\(([^)]+)\)")

(def image-tokenizer
  (u/normalize-tokenizer
   {:regex github-image-regex
    :handler (fn [match] {:type :image
                          :text (match 2)
                          :alt (match 1)})}))

(defn image-renderer [_ctx node]
  (let [src (some-> node :attrs :src)
        alt (or (:alt node) (:title (:attrs node)) "")
        webp-src (when src (str (subs src 0 (clojure.string/last-index-of src ".")) ".webp"))
        fallback-type (cond
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
            :class "max-w-full h-auto mx-auto rounded-lg shadow-lg my-6"}]]))

(def youtube-tokenizer
  (u/normalize-tokenizer
    {:regex
     #"\{\%.*embed.*url=\"(.*?)\".*\%\}"
     :handler
     (fn [match]
       {:type :embed
        :url (match 1)})}))

(defn hack-youtube
 [md]
 (str/replace md #"url=\"https://www.youtube.com(.*?)\"" "url=\"$1\""))
