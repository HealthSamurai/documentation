(ns gitbok.markdown.widgets.link
  (:require
   [clojure.string :as str]
   [gitbok.indexing.core :as indexing]
   [gitbok.ui.heroicons :as ico]))

(defn href [context node filepath]
  (let [href (-> node :attrs :href)]
    (indexing/filepath->href context filepath href)))

(defn link-renderer [context filepath _ctx node]
  (let [href (href context node filepath)
        is-external (str/starts-with? href "http")]
    [:a {:href href
         :class (str "text-primary-9 underline underline-offset-2
         decoration-[max(0.07em,1px)] hover:text-primary-12 hover:decoration-primary-9
         transition-all duration-200"
                    (when is-external " inline-flex items-center gap-1"))}
     (or (-> node :content (get 0) :text)
         (-> node :content (get 0)
             :content (get 0) :text))
     (when is-external
       (ico/arrow-up-right "size-3 text-primary-9/60"))]))
