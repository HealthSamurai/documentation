(ns gitbok.markdown.widgets.big-links
  (:require
   [clojure.string :as str]
   [gitbok.indexing.core :as indexing]
   [gitbok.indexing.impl.file-to-uri :as file-to-uri]
   [nextjournal.markdown.utils :as u]
   [uui.heroicons :as ico]))

(def big-link-tokenizer
  (u/normalize-tokenizer
   {:regex
    #"\[\[(.+)\]\]"
    ;; [[[ ]]]
    ;; #"\[\[\[([^\]]+)\]\]\]"
    :handler (fn [match]
               {:type :big-link
                :text (match 1)})}))

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
  [:div {:class "my-6 p-4 border border-tint-subtle rounded-lg bg-tint-base hover:border-primary-9 transition-all duration-200 flex items-center shadow-sm hover:shadow-md cursor-pointer"}
   [:div {:class "flex-1"}
    [:a {:href href
         :class "text-tint-strong hover:text-primary-9 font-medium text-lg transition-colors duration-200 no-underline block w-full h-full"}
     title]]
   (ico/chevron-right "chevron size-5 text-tint-strong/40")])

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

(defn hack-content-ref [md-file]
  (str/replace md-file
               #"\{% content-ref.*%\}\s*\n\[[^\]]*\]\(([^)]*)\)\s*\n\{% endcontent-ref %\}"
               "[[$1]]"))
