;; (ns gitbok.notebooks
;;   (:require [org.httpkit.client :as http-client]
;;             [cheshire.core :as json]))
;;
;; (defn get-notebooks [context]
;;   (-> @(http-client/post (str "https://aidbox.app" "/rpc")
;;                          {:body (json/generate-string {:method 'aidbox.portal/published-notebooks})
;;                           :headers {"content-type" "application/json"}})
;;       :body
;;       (json/parse-string keyword)))
;;
;; (defn get-notebook [context id]
;;   (-> @(http-client/get (str "https://aidbox.app/PublishedNotebook/" id) {:headers {"content-type" "application/json"}})
;;       :body
;;       (json/parse-string keyword)))
;;
;; (comment
;;
;;   (def context gb/context)
;;
;;   (get-notebooks context)
;;   (get-notebook context "c657dbba-2f64-4477-b274-4e3bb5da47eb")
;;
;;   )

#_(defn show-notebook [context {{id :id} :route-params :as request}]
    (let [nb (gitbok.notebooks/get-notebook context id)]
      (layout
       context request
       [:div
        (uui/breadcramp
         ["/notebooks" "notebooks"]
         ["#" (:name nb)])

        [:h1 {:class "text-3xl font-bold py-3 border-b"} (:name nb)]
        (for [cell (:cells nb)]
          (cond
            (= "markdown" (:type cell))
            [:div.gitbook {:class "my-6"}
             (if-let [v (:result cell)] (uui/raw v) (markdown/parse-md context (:value cell)))]

            (= "rest" (:type cell))
            [:div {:class "my-6"}
             [:pre {:class cell-code-c} (uui/raw (hl-http (:value cell)))]
             (when-let [res (:result cell)]
               [:details
                [:summary {:class cell-res-c}
                 [:b (:status res)]
                 [:span {:class "mx-4"} (:content-type (:headers res))]]
                [:pre {:class "-mt-px text-xs bg-gray-50 p-4 border border-gray-300"} (:body res)]])]

            (= "sql" (:type cell))
            [:div {:class "my-6"}
             [:pre {:class cell-code-c} (uui/raw (hl-sql (:value cell)))]
             (when-let [res (:result cell)]
               [:details
                [:summary {:class cell-res-c} "Result " [:b (:duration res) "ms"]]
                [:pre {:class "-mt-px text-xs bg-gray-50 p-4 border border-gray-300"}
                 (for [res (:result res)]
                   [:div
                    (if-not (sequential? (:data res))
                      [:pre (pr-str (:data res))]
                      (let [rows (:data res)
                            cols (keys (first rows))]
                        [:div.gitbook
                         [:table.uui {:class "text-xs"}
                          [:thead
                           [:tr (for [c cols] [:th (name c)])]]
                          [:tbody
                           (for [row (:data res)]
                             [:tr
                              (for [c cols] [:td (str (get row c))])])]]]))])]])]

            :else
            [:div {:class "my-6"}
             [:b (:type cell)]
             [:pre {:class "text-xs mt-4 bg-gray-100 p-4 rounded-md"} (:value cell)]
             [:pre {:class "text-xs mt-4 bg-gray-100 p-4 rounded-md"} (:result cell)]]))])))


#_(defn notebooks [context request]
    (let [notebooks (->> (gitbok.notebooks/get-notebooks context)
                         :result
                         (sort-by :name))]
      (layout
       context request
       [:div.uui-
        [:h1 {:class "text-2xl py-3 border-b font-bold"} "Notebooks"]
        (for [n notebooks]
          [:div {:class "flex space-x-4 items-top py-2"}
           [:div {:class "flex-1"}
            [:a   {:class "text-sky-600" :href (str "/notebooks/" (:id n))} (:name n)]
            [:div {:class "text-sm text-gray-500"} (:description n)]]
           [:div {:class "flex space-x-2 items-top"}
            (for [tg  (:value (:tags n))]
              [:span {:class "py-1 px-2 text-xs border border-gray-200 bg-gray-50 rounded-md"} tg])]])])))


