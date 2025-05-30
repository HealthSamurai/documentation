(ns gitbok.markdown.widgets.link
  (:require
   [clojure.string :as str]
   [gitbok.indexing.core :as indexing]))

(defn href [context node filepath]
  (let [href (-> node :attrs :href)]
    (if (str/starts-with? href "http")
      href
      (-> (indexing/page-link->uri
            context
            filepath
            href)
          ;; (str/replace #"README\.md|readme\.md" "")
          ;; (str/replace #"\.md|\.MD" "")

          ))))

(defn link-renderer [context filepath ctx node]
  [:a {:href (href context node filepath)}
   (-> node :content (get 0) :text)])
