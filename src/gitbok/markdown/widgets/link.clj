(ns gitbok.markdown.widgets.link
  (:require
   [clojure.string :as str]
   [gitbok.indexing.core :as indexing]
   [gitbok.ui.heroicons :as ico]))

(defn- special-protocol?
  "Check if href is a special protocol that shouldn't be resolved"
  [href]
  (or (str/starts-with? href "mailto:")
      (str/starts-with? href "tel:")
      (str/starts-with? href "javascript:")
      (str/starts-with? href "#")))

(defn href [context node filepath]
  (let [href (-> node :attrs :href)]
    (cond
      ;; Nil or empty
      (nil? href) nil
      (str/blank? href) href
      ;; Special protocols - return as is
      (special-protocol? href) href
      ;; External URLs - return as is
      (str/starts-with? href "http") href
      ;; Skip link resolution flag - return href as is
      (:skip-link-resolution context) href
      ;; Normal resolution via indexing
      :else (indexing/filepath->href context filepath href))))

(defn link-renderer [context filepath _ctx node]
  (let [href (href context node filepath)
        is-external (and href (str/starts-with? href "http"))]
    [:a {:href href
         :class (str "text-brand underline underline-offset-2
         decoration-[max(0.07em,1px)] hover:text-on-surface-strong hover:decoration-brand
         transition-all duration-200"
                    (when is-external " inline-flex items-center gap-1"))}
     (or (-> node :content (get 0) :text)
         (-> node :content (get 0)
             :content (get 0) :text))
     (when is-external
       (ico/arrow-up-right "size-3 text-brand/60"))]))
