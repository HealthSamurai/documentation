(ns gitbok.markdown.widgets.link
  (:require
   [clojure.string :as str]
   [gitbok.indexing.core :as indexing]
   [uui.heroicons :as ico]))

(defn href [context node filepath]
  (let [href (-> node :attrs :href)]
    (def hh href)
    (if (str/starts-with? href "http")
      href
      (-> (indexing/page-link->uri
           context
           filepath
           href)))))

(defn link-renderer [context filepath _ctx node]
  (def node node)
  (let [href (href context node filepath)
        is-external (str/starts-with? href "http")]
    (def href1 href)
    [:a {:href href
         :class "text-primary-9 underline underline-offset-2 decoration-1 hover:text-primary-10 hover:decoration-2 transition-all duration-200 inline-flex items-center gap-1"}
     (or (-> node :content (get 0) :text)
         (-> node :content (get 0)
             :content (get 0) :text))
     (when is-external
       (ico/arrow-up-right "size-3 text-primary-9/60"))]))
