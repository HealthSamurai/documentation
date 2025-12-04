(ns gitbok.markdown.widgets.cards
  (:require
   [gitbok.state :as state]
   [clojure.string :as str]
   [hiccup2.core]
   [gitbok.http :as http]
   [gitbok.indexing.core :as indexing]))

(defn- parse-row
  "Parse a table row into card data map"
  [row]
  (let [cells (->> row (filter vector?) (mapv (fn [a] (next (next a)))))
        first-cell (first cells)
        first-cell-empty? (or (nil? first-cell)
                              (and (seq? first-cell) (empty? first-cell))
                              (and (seq? first-cell)
                                   (string? (last first-cell))
                                   (empty? (str/trim (last first-cell)))))
        has-badge? (and first-cell
                        (not first-cell-empty?)
                        (let [content (if (seq? first-cell) (last first-cell) first-cell)]
                          (or (and (vector? content) (= :mark (first content)))
                              (and (string? content)
                                   (< (count content) 10)
                                   (re-matches #"(?i)^(beta|alpha|new|preview)$" content)))))
        [badge one two three four five] (cond
                                          has-badge? cells
                                          first-cell-empty? (into [nil] (rest cells))
                                          :else (cons nil cells))
        pic1 (when four
               (first (filterv #(and (sequential? %) (= (first %) :a)) four)))
        pic2 (when five
               (first (filterv #(and (sequential? %) (= (first %) :a)) five)))
        pic-href1 (get-in pic1 [1 :href])
        pic-href2 (get-in pic2 [1 :href])
        nav-from-five (when five
                        (let [c (if (seq? five) (last five) five)]
                          (when (and (vector? c) (= :a (first c)))
                            (get-in c [1 :href]))))
        nav-from-four (when four
                        (let [c (if (seq? four) (last four) four)]
                          (when (and (vector? c) (= :a (first c))
                                     (not (re-matches #".*(png|jpg|jpeg|svg)$" (get-in c [1 :href]))))
                            (get-in c [1 :href]))))
        navigation-link (or nav-from-five nav-from-four)
        pic-footer (when three
                     (let [c (if (seq? three) (last three) three)]
                       (when (and (vector? c) (= :a (first c)))
                         (get-in c [1 :href]))))
        pic-footer? (when pic-footer (re-matches #".*(png|jpg|jpeg|svg)$" pic-footer))
        title-filepath (or navigation-link (when-not pic-footer? pic-footer))
        img-href (first (filter #(when % (re-matches #".*(png|jpg|jpeg|svg)$" %))
                                [pic-footer pic-href1 pic-href2]))]
    {:badge badge :one one :two two :three three
     :pic-footer pic-footer :pic-footer? pic-footer?
     :title-filepath title-filepath :img-href img-href}))

(defn- render-card
  "Render a single card from parsed row data"
  [context filepath {:keys [badge one two three pic-footer pic-footer? title-filepath img-href]}]
  (let [title-href (when title-filepath
                     (if (or (str/starts-with? title-filepath "http://")
                             (str/starts-with? title-filepath "https://"))
                       title-filepath
                       (indexing/page-link->uri context filepath title-filepath)))
        processed-footer (when (and three (not pic-footer?))
                           (let [c (if (seq? three) (last three) three)]
                             (when (and (vector? c) (= :a (first c)))
                               (let [opts (-> c second
                                              (assoc :class "text-brand hover:text-brand-hover hover:underline underline text-sm")
                                              (update :href #(indexing/filepath->href context filepath %)))]
                                 [:a opts (nth c 2)]))))
        href title-href
        clickable? (boolean href)
        external? (and href (or (str/starts-with? href "http://") (str/starts-with? href "https://")))]
    [:div {:class (str "card group flex hover:shadow-md transition-all duration-200 "
                       (when clickable? "cursor-pointer ")
                       "flex-col bg-surface-subtle border border-outline-subtle "
                       "hover:border-outline rounded-sm overflow-hidden h-full "
                       "min-h-[150px] md:max-h-[300px] transition-colors duration-200 relative")
           :hx-get (when (and clickable? (not external?))
                     (str (http/get-partial-product-prefixed-url context
                                                                 (subs href (count (http/get-product-prefix context))))))
           :hx-target (when (and clickable? (not external?)) "#content")
           :hx-push-url (when (and clickable? (not external?)) href)
           :hx-swap (when (and clickable? (not external?)) "outerHTML")}
     (when clickable?
       [:a {:href href :class "absolute inset-0 z-10" :style "font-size: 0; opacity: 0;"
            :onclick (if external? nil "event.preventDefault(); htmx.trigger(this.parentElement, 'click'); return false;")}
        "Link"])
     [:div {:class "relative z-20 flex flex-col h-full pointer-events-none"}
      (when img-href
        (let [processed-img-href (cond
                                   (str/starts-with? img-href "http") img-href
                                   (str/includes? img-href ".gitbook/assets")
                                   (str (state/get-config context :prefix "") "/.gitbook/assets"
                                        (last (str/split img-href #"\.gitbook/assets")))
                                   :else img-href)
              ;; Check if there's a -dark variant for dark mode
              dark-img-href (when-not (str/starts-with? img-href "http")
                              (str/replace processed-img-href #"\.([^.]+)$" "-dark.$1"))]
          [:div
           [:img {:src processed-img-href :alt "card" :class "pointer-events-none dark:hidden"}]
           [:img {:src dark-img-href :alt "card" :class "pointer-events-none hidden dark:block"}]]))
      [:div {:class (str "flex flex-col gap-0 p-4 flex-1 " (when-not img-href "justify-start"))}
       (when badge (into [:div {:class "mb-2"}] (if (seq? badge) badge [badge])))
       (when one
         (let [c (if (seq? one) (last one) one)]
           (when c (into [:div {:class "text-sm text-on-surface-muted"}] (if (seq? c) c [c])))))
       (when two
         (let [c (if (seq? two) (last two) two)]
           (when c (into [:p {:class "text-on-surface-muted text-sm group-hover:text-on-surface-strong"}] (if (seq? c) c [c])))))
       (when processed-footer
         [:div {:class "text-sm mt-2 pointer-events-auto relative z-30"} processed-footer])]]]))

(defn render-cards-from-table
  [context filepath [_ _ _ tbody]]
  (when-not (:skip-link-resolution context)
    (let [rows (->> tbody (filter vector?) (filter #(= :tr (first %))))]
      [:div {:class "grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mt-4"}
       (for [row rows]
         (render-card context filepath (parse-row row)))])))