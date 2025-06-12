(ns gitbok.markdown.widgets.tabs
  (:require
   [clojure.string :as str]
   [gitbok.utils :as utils]
   [hiccup2.core]))

(defn- parse-tabs [content]
  (let [tabs-start "{% tabs %}"
        tabs-end "{% endtabs %}"
        tab-start-prefix "{% tab title=\""
        tab-start-suffix "\" %}"
        tab-end "{% endtab %}"

        find-tabs-blocks
        (fn [^String content]
          (loop [remaining content
                 blocks []
                 offset 0]
            (if remaining
              (if-let [start-pos (.indexOf ^String remaining tabs-start)]
                (if-let [end-pos (.indexOf ^String remaining tabs-end (+ start-pos (count tabs-start)))]
                  (let [block-start (+ offset start-pos)
                        block-end (+ offset end-pos (count tabs-end))
                        block-content (utils/safe-subs remaining (+ start-pos (count tabs-start)) end-pos)]
                    (if block-content
                      (recur (utils/safe-subs remaining (+ end-pos (count tabs-end)))
                             (conj blocks {:start block-start
                                           :end block-end
                                           :content block-content})
                             (+ offset end-pos (count tabs-end)))
                      blocks))
                  blocks)
                blocks)
              blocks)))

        parse-tabs-in-block
        (fn [block-content block-start]
          (loop [remaining block-content
                 tabs []
                 offset 0]
            (if (and remaining (not= remaining "") (not= (count remaining) 0))
              (if-let [start-pos (.indexOf ^String remaining tab-start-prefix)]
                (if (>= start-pos 0)
                  (if-let [title-start (+ start-pos (count tab-start-prefix))]
                    (if-let [title-end (.indexOf ^String remaining "\"" title-start)]
                      (if (>= title-end 0)
                        (if-let [content-start (+ title-end (count tab-start-suffix))]
                          (if-let [content-end (.indexOf ^String remaining tab-end content-start)]
                            (if (>= content-end 0)
                              (let [title (utils/safe-subs remaining title-start title-end)
                                    content (utils/safe-subs remaining content-start content-end)
                                    tab-start (+ block-start offset start-pos)
                                    tab-end-pos (+ block-start offset content-end (count tab-end))
                                    next-remaining (utils/safe-subs remaining (+ content-end (count tab-end)))]
                                (if (and title content)
                                  (recur (or next-remaining "")
                                         (conj tabs {:title title
                                                     :text (str/trim content)
                                                     :start tab-start
                                                     :end tab-end-pos})
                                         (+ offset content-end (count tab-end)))
                                  tabs))
                              tabs)
                            tabs)
                          tabs)
                        tabs)
                      tabs)
                    tabs)
                  tabs)
                tabs)
              tabs)))

        blocks (find-tabs-blocks content)]

    (for [block blocks]
      {:text (utils/safe-subs content (:start block) (:end block))
       :start (:start block)
       :end (:end block)
       :tabs (parse-tabs-in-block (:content block) (:start block))})))

(defn- render-tabs-hiccup [context filepath tabs-data
                           parse-markdown-content-fn
                           render-md-fn]
  (let [tabs (for [{:keys [title text start end]} (:tabs tabs-data)
                   :let [parsed-content (:parsed (parse-markdown-content-fn context [filepath text]))
                         rendered-content (render-md-fn context filepath parsed-content)]]
               {:title title
                :content rendered-content
                :start start
                :end end})]
    [:div {:class "bg-white border border-gray-200 rounded-lg overflow-hidden"}
     [:div {:class "flex border-b border-gray-200"}
      (for [[idx {:keys [title]}] (map-indexed vector tabs)]
        [:button {:class (str "px-4 py-2 text-sm font-medium border-b-2 transition-colors "
                              (if (= idx 0) "border-blue-500 text-blue-600" "border-transparent text-gray-500 hover:text-gray-700"))
                  :data-tab idx
                  :onclick (str "switchTab(this, " idx ")")}
         title])]
     [:div {:class "p-4"}
      (for [[idx {:keys [content]}] (map-indexed vector tabs)]
        [:div {:class (str "tab-content " (if (= idx 0) "block" "hidden"))
               :data-tab idx}
         content])]]))

(defn hack-tabs [context filepath
                 parse-markdown-content-fn
                 render-md-fn
                 content]
  (let [tabs-data (parse-tabs content)
        sorted-tabs (sort-by :start > tabs-data)]
    (reduce (fn [content tab-data]
              (let [hiccup
                    (render-tabs-hiccup context filepath tab-data
                                        parse-markdown-content-fn
                                        render-md-fn)
                    html
                    (hiccup2.core/html hiccup)]
                (str (utils/safe-subs content 0 (:start tab-data))
                     html
                     (utils/safe-subs content (:end tab-data)))))
            content
            sorted-tabs)))

