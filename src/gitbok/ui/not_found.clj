(ns gitbok.ui.not-found
  (:require
   [gitbok.indexing.impl.file-to-uri :as file-to-uri]
   [gitbok.utils :as utils]
   [clojure.string :as str]
   [uui]
   [gitbok.search]))

(defn not-found-view [context uri]
  (let [search-term (last (str/split uri #"/"))
        search-results (gitbok.search/search context search-term)]
    [:div.min-h-screen.flex.items-center.justify-center
     [:script
      (uui/raw "
document.addEventListener('DOMContentLoaded', () => {
  if (window.posthog && posthog.capture) {
    posthog.capture('Page Not Found', {
      path: window.location.pathname,
      full_url: window.location.href,
      referrer: document.referrer,
    });
  }
});")]
     [:div.max-w-2xl.w-full.px-4
      [:div
       [:h2.mt-4.text-3xl.font-semibold.text-tint-11.text-center "Page not found"]
       (when (seq search-results)
         [:div.mt-8
          [:h3.text-lg.font-medium.text-tint-12 "You might be looking for:"]
          [:ul.mt-4.space-y-2.text-left
           (for [search-res (take 8 (utils/distinct-by #(-> % :hit :title) search-results))]
             [:li
              [:a.text-primary-9.hover:text-primary-10.text-lg.flex.items-start
               {:href (file-to-uri/filepath->uri context (:filepath (:hit search-res)))}
               (:title (:hit search-res))]])]])]]]))
