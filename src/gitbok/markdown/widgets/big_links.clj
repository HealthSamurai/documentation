(ns gitbok.markdown.widgets.big-links
  (:require
   [gitbok.http]
   [clojure.string :as str]
   [gitbok.indexing.core :as indexing]
   [gitbok.indexing.impl.file-to-uri :as file-to-uri]
   [nextjournal.markdown.utils :as u]
   [gitbok.utils :as utils]
   [gitbok.products :as products]
   [uui.heroicons :as ico]))

(def big-link-tokenizer
  (u/normalize-tokenizer
   {:regex
    #"\[\[(.+)\]\]"
    :handler (fn [match]
               {:type :big-link
                :text (match 1)})}))

(defn href [context url filepath]
  (if (and url (str/starts-with? url "http"))
    url
    (when (and url filepath)
      (let [uri (-> (indexing/page-link->uri
                      context
                      filepath
                      url))]
        (when uri
          (if (str/starts-with? uri "/")
            uri
            (str "/" uri)))))))

(defn big-link-view [href title & [image]]
  [:div {:class "my-6 p-4 border
         border-tint-8
         rounded-lg bg-tint-base hover:border-primary-7
         transition-all duration-200 flex
         items-center cursor-pointer group"}
   (when image
     [:div {:class "w-10 h-10 rounded-full flex items-center justify-center mr-3 flex-shrink-0"}
      [:img {:src image :alt title :class "w-6 h-6 object-contain"}]])
   [:div {:class "flex-1"}
    [:a {:href href
         :class "group-hover:text-primary-9 text-base transition-colors duration-200
         no-underline block w-full h-full"}
     title]]
   (ico/chevron-right "chevron size-5 text-tint-strong/40")])

(defn render-big-link
  ([context filepath url]
   (render-big-link context filepath nil {:text url}))
  ([context filepath _ctx node]
   (let [uri (href context (:text node) filepath)
         uri
         (utils/uri-to-relative
           uri
           (gitbok.http/get-prefix context)
           (products/path context))

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

(defn hack-content-ref [md-file]
  (str/replace md-file
               #"\{% content-ref.*%\}\s*\n\[[^\]]*\]\(([^)]*)\)\s*\n\{% endcontent-ref %\}"
               "[[$1]]"))
