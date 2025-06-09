(ns gitbok.markdown.widgets.big-links
  (:require
   [clojure.string :as str]
   [gitbok.indexing.core :as indexing]
   [gitbok.indexing.impl.file-to-uri :as file-to-uri]
   [nextjournal.markdown.utils :as u]))

(def big-link-tokenizer
  (u/normalize-tokenizer
   {:regex
    #"\[\[(.+)\]\]"
    ;; [[[ ]]]
    ;; #"\[\[\[([^\]]+)\]\]\]"
    :handler (fn [match]
               {:type :big-link
                :text (match 1)})}))

(def icon
  {:dangerouslySetInnerHTML
   {:__html "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"24\" height=\"24\" viewBox=\"0 0 24 24\" fill=\"none\" stroke=\"currentColor\" stroke-width=\"2\" stroke-linecap=\"round\" stroke-linejoin=\"round\"><path d=\"M18 13v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h6\"></path><polyline points=\"15 3 21 3 21 9\"></polyline><line x1=\"10\" y1=\"14\" x2=\"21\" y2=\"3\"></line></svg>"}})

(defn href [context url filepath]
  (if (and url (str/starts-with? url "http"))
    url
    (let [uri (-> (indexing/page-link->uri
                   context
                   filepath
                   url))]
      (if (str/starts-with? uri "/")
        uri
        (str "/" uri)))))

(defn big-link-view [href title]
  [:div.content-ref
   {:class "my-4 p-4 border border-gray-200 rounded-md bg-gray-50 hover:bg-gray-100 transition-colors flex items-center"}
   [:div.content-ref-icon
    {:class "mr-3 text-blue-500"}
    [:span icon]]
   [:div.content-ref-content
    {:class "flex-1"}
    [:a.content-ref-link
     {:href href
      :class "text-blue-600 hover:text-blue-800 font-medium"}
     title]]])

(defn render-big-link
  ([context filepath url]
   (render-big-link context filepath nil {:text url}))
  ([context filepath _ctx node]
   (let [uri (href context (:text node) filepath)
         uri (if (and uri (str/starts-with? uri "/"))
               (subs uri 1)
               uri)
         file (indexing/uri->filepath context uri)
         file (if (and file (str/starts-with? file "/docs"))
                (subs file 6)
                file)
         title
         (or (:title (get (file-to-uri/get-idx context) file))
             (:text node))]
     (big-link-view (str "/" uri) title))))
