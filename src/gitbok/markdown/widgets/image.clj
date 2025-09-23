(ns gitbok.markdown.widgets.image
  (:require
   [clojure.string :as str]
   [gitbok.indexing.core :as indexing]
   [gitbok.http :as http]
   [nextjournal.markdown.utils :as u]))

(defn image-renderer [context filepath _ctx node]
  (let [src (some-> node :attrs :src)
        ;; Normalize .gitbook/assets paths - remove ../ prefixes
        normalized-src (when src
                         (if (str/includes? src ".gitbook/assets")
                           (str ".gitbook/assets" (last (str/split src #"\.gitbook/assets")))
                           src))
        ;; Handle different types of image paths
        processed-src (cond
                        ;; Empty or nil src
                        (str/blank? normalized-src) ""

                        ;; External URLs - keep as is
                        (str/starts-with? normalized-src "http") normalized-src

                        ;; .gitbook/assets - return with simple prefix
                        (str/starts-with? normalized-src ".gitbook/assets")
                        (http/get-prefixed-url context (str "/" normalized-src))

                        ;; Other relative paths - use the standard link resolution
                        :else (let [href (indexing/filepath->href context filepath normalized-src)]
                                (http/get-prefixed-url context href)))

        alt (or (:alt node)
                (:title (:attrs node))
                (:text (first (:content node))) "")

        ;; Generate WebP path and check if we should include it
        ;; For simplicity, we'll only generate WebP source for files we know exist
        ;; or for external URLs (assuming they have WebP versions)
        webp-src (when (and normalized-src
                            (not (str/blank? normalized-src))
                            (not (str/ends-with? normalized-src ".svg"))
                            (not (str/ends-with? normalized-src ".gif")))
                   (let [last-dot-idx (clojure.string/last-index-of normalized-src ".")
                         base-src (if last-dot-idx
                                    (str (subs normalized-src 0 last-dot-idx) ".webp")
                                    nil)]
                     (when base-src
                       (cond
                         ;; External URLs - assume WebP exists
                         (str/starts-with? base-src "http") base-src

                         ;; .gitbook/assets - check if WebP file exists
                         (str/starts-with? base-src ".gitbook/assets")
                         (let [webp-path (str "/" base-src)
                               ;; Check if WebP exists in classpath
                               webp-exists? (or
                                            ;; Try to find in classpath
                                             (not (nil? (clojure.java.io/resource base-src)))
                                             (not (nil? (clojure.java.io/resource (str "assets/" (last (str/split base-src #"\.gitbook/assets/"))))))
                                            ;; For known files that have WebP versions
                                             (str/includes? base-src "rest-console")
                                             (str/includes? base-src "rest-console-get-patient"))]
                           (when webp-exists?
                             (http/get-prefixed-url context webp-path)))

                         ;; Other paths - check resource existence
                         :else
                         (let [href (indexing/filepath->href context filepath base-src)
                               ;; For other files, assume WebP exists if it's in certain directories
                               ;; This is a heuristic - you might want to make this more sophisticated
                               webp-likely? (or (str/includes? href "assets/")
                                                (str/includes? href "public/"))]
                           (when webp-likely?
                             (http/get-prefixed-url context href)))))))

        fallback-type
        (cond
          (and normalized-src (str/ends-with? normalized-src ".png")) "image/png"
          (and normalized-src (or (str/ends-with? normalized-src ".jpg")
                                  (str/ends-with? normalized-src ".jpeg"))) "image/jpeg"
          (and normalized-src (str/ends-with? normalized-src ".gif")) "image/gif"
          (and normalized-src (str/ends-with? normalized-src ".svg")) "image/svg+xml"
          :else "image/jpeg")]
    ;; Generate picture tag with WebP source only if available
    (if webp-src
      [:picture
       [:source {:srcset webp-src :type "image/webp"}]
       [:source {:srcset processed-src :type fallback-type}]
       [:img {:src processed-src
              :alt alt
              :loading "lazy"
              :class "max-w-full h-auto mx-auto rounded-lg my-6"}]]
      ;; No WebP available - use simple img tag
      [:img {:src processed-src
             :alt alt
             :loading "lazy"
             :class "max-w-full h-auto mx-auto rounded-lg my-6"}])))

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
