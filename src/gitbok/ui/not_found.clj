(ns gitbok.ui.not-found
  (:require
   [gitbok.indexing.impl.file-to-uri :as file-to-uri]
   [gitbok.utils :as utils]
   [clojure.string :as str]
   [gitbok.search]))

(defn not-found-view [context uri]
  (let [search-term (last (str/split uri #"/"))
        search-results (gitbok.search/search context search-term)]
    [:div.min-h-screen.flex.items-center.justify-center
     [:div.max-w-2xl.w-full.px-4
      [:div
       [:h2.mt-4.text-3xl.font-semibold.text-gray-700.text-center "Page not found"]
       (when (seq search-results)
         [:div.mt-8
          [:h3.text-lg.font-medium.text-gray-900 "You might be looking for:"]
          [:ul.mt-4.space-y-2.text-left
           (for [search-res (take 5 (utils/distinct-by #(-> % :hit :title) search-results))]
             [:li
              [:a.text-blue-600.hover:text-blue-800.text-lg.flex.items-start
               {:href (file-to-uri/filepath->uri
                       context (:filepath (:hit search-res)))}
               (:title (:hit search-res))]])]])]]]))
