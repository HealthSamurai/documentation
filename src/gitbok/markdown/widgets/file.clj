(ns gitbok.markdown.widgets.file
  "Widget for handling GitBook file download blocks.
   Syntax: {% file src=\"path/to/file\" %}label{% endfile %}"
  (:require
   [clojure.string :as str]
   [gitbok.utils :as utils]
   [gitbok.http :as http]
   [hiccup2.core]))

(defn- parse-file-blocks
  "Parse {% file src=\"...\" %}...{% endfile %} blocks from content"
  [^String content]
  (let [pattern #"(?s)\{% ?file src=\"([^\"]+)\" ?%\}\s*\n?(.*?)\n?\{% ?endfile ?%\}"]
    (loop [matcher (re-matcher pattern content)
           blocks []]
      (if (.find matcher)
        (let [full-match (.group matcher 0)
              src (.group matcher 1)
              label (str/trim (.group matcher 2))
              start (.start matcher)
              end (.end matcher)]
          (recur matcher
                 (conj blocks {:src src
                               :label label
                               :start start
                               :end end
                               :text full-match})))
        blocks))))

(defn- get-filename [src]
  (last (str/split src #"/")))

(defn- resolve-file-url [context src]
  ;; Normalize .gitbook/assets paths - remove ../ prefixes
  (let [normalized-src (if (str/includes? src ".gitbook/assets")
                         (str ".gitbook/assets" (last (str/split src #"\.gitbook/assets")))
                         src)]
    (cond
      ;; External URLs - keep as is
      (str/starts-with? normalized-src "http")
      normalized-src

      ;; .gitbook/assets - return with product prefix
      (str/starts-with? normalized-src ".gitbook/assets")
      (http/get-product-prefixed-url context (str "/" normalized-src))

      ;; Other paths
      :else
      (http/get-product-prefixed-url context (str "/" normalized-src)))))

(def ^:private download-icon-svg
  "M12 2.25a.75.75 0 0 1 .75.75v11.69l3.22-3.22a.75.75 0 1 1 1.06 1.06l-4.5 4.5a.75.75 0 0 1-1.06 0l-4.5-4.5a.75.75 0 1 1 1.06-1.06l3.22 3.22V3a.75.75 0 0 1 .75-.75Zm-9 13.5a.75.75 0 0 1 .75.75v2.25a1.5 1.5 0 0 0 1.5 1.5h13.5a1.5 1.5 0 0 0 1.5-1.5V16.5a.75.75 0 0 1 1.5 0v2.25a3 3 0 0 1-3 3H5.25a3 3 0 0 1-3-3V16.5a.75.75 0 0 1 .75-.75Z")

(def ^:private chevron-icon-svg
  "M16.28 11.47a.75.75 0 0 1 0 1.06l-7.5 7.5a.75.75 0 0 1-1.06-1.06L14.69 12 7.72 5.03a.75.75 0 0 1 1.06-1.06l7.5 7.5Z")

(defn- render-file-widget [context _filepath {:keys [src label]}]
  (let [filename (get-filename src)
        display-label (if (str/blank? label) filename label)
        resolved-url (resolve-file-url context src)]
    [:div {:class "file-widget-wrapper"}
     [:a {:href resolved-url
          :download filename
          :class "file-widget mt-2 mb-4 p-4 border border-outline rounded-lg bg-surface hover:border-outline-input-focus transition-all duration-200 flex items-center cursor-pointer group no-underline"}
      [:div {:class "flex-shrink-0 p-2 bg-brand/10 rounded-md mr-3"}
       [:svg {:class "size-5 text-brand" :xmlns "http://www.w3.org/2000/svg" :viewBox "0 0 24 24" :fill "currentColor"}
        [:path {:fill-rule "evenodd" :clip-rule "evenodd" :d download-icon-svg}]]]
      [:div {:class "flex-1 min-w-0"}
       [:span {:class "text-base font-normal text-on-surface-strong block truncate"} display-label]]
      [:svg {:class "size-5 text-on-surface-strong/40" :xmlns "http://www.w3.org/2000/svg" :viewBox "0 0 24 24" :fill "currentColor"}
       [:path {:fill-rule "evenodd" :clip-rule "evenodd" :d chevron-icon-svg}]]]]))

(defn hack-file
  "Transform {% file %} blocks into rendered HTML widgets"
  [context filepath content]
  (let [file-blocks (parse-file-blocks content)
        sorted-blocks (sort-by :start > file-blocks)]
    (reduce
     (fn [content block]
       (let [hiccup (render-file-widget context filepath block)
             html (str (hiccup2.core/html hiccup))]
         (str
          (utils/safe-subs content 0 (:start block))
          html
          (utils/safe-subs content (:end block)))))
     content
     sorted-blocks)))
