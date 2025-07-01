(ns gitbok.ui.right-toc
  (:require
   [system]
   [gitbok.indexing.core :as indexing]
   [gitbok.http]
   [clojure.string :as str]
   [gitbok.markdown.core :as markdown]))

(defn render-right-toc [parsed]
  (when (:toc parsed)
    [:nav {:class "toc-container sticky top-16 h-[calc(100vh-4rem)] overflow-y-auto p-6 bg-white w-60 rounded-lg z-50"
           :aria-label "On-page navigation"}
     [:div {:class "toc w-full max-w-full"}
      (for [item (-> parsed :toc :children first :children)]
        (markdown/render-right-toc-item item))]]))

(defn get-right-toc [context filepath]
  (let [rendered (markdown/get-rendered context filepath)]
    (if (map? rendered)
      (:toc rendered)
      nil)))

(defn get-toc-view
  [context request]
  (let [uri (str/replace (:uri request) #"^/toc" "")
        filepath (indexing/uri->filepath context uri)
        lastmod
        (indexing/get-lastmod context filepath)]
    (if filepath
      (gitbok.http/response1
       (get-right-toc context filepath) 200 lastmod true)
      (gitbok.http/response1 [:div] 404 nil))))
