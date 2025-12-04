(ns gitbok.ui.blog-subscribe)

(def font-family "'Gotham Pro', Arial, sans-serif")

;; Mailchimp configuration (same as health-samurai.io)
(def mailchimp-action
  "https://health-samurai.us19.list-manage.com/subscribe/post?u=1c57d4d1b1aaffde230e81f34&id=0197cbafab")

(def mailchimp-tags "6237144")

(def samurai-image-url
  "https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/632207aa3b981e44e5fead6a_subs.webp")

(defn- subscribe-box
  "Inner subscribe box component"
  [{:keys [email-id page-url box-class]}]
  (let [email-field-id (or email-id "EMAIL")
        form-id (str "subscribe-form-" email-field-id)]
    [:div {:id "bg-subscribe-section"
           :class (str "bg-[#1f2a44] rounded-2xl" (when box-class (str " " box-class)))}
     [:div {:class "flex flex-col md:flex-row items-center"
            :style {:font-family font-family}}
      ;; Left column - form
      [:div {:class "flex-1 p-8 md:p-12"}
       [:form {:id form-id
               :name "subscribe-form"
               :method "post"
               :action mailchimp-action
               :class "space-y-4"}
        ;; Hidden fields
        [:input {:type "hidden"
                 :name "tags"
                 :value mailchimp-tags}]
        (when page-url
          [:input {:type "hidden"
                   :id "current-page"
                   :data-name "WPAGE"
                   :value page-url}])
        ;; HubSpot tracking fields (empty, populated by scripts)
        [:input {:type "hidden" :name "hutk" :value ""}]
        [:input {:type "hidden" :name "ipAddress" :value ""}]
        [:input {:type "hidden" :name "pageUri" :value ""}]
        [:input {:type "hidden" :name "pageId" :value ""}]
        [:input {:type "hidden" :name "pageName" :value ""}]

        ;; Title
        [:h3 {:class "text-white text-2xl md:text-3xl leading-tight mb-6"}
         "Never miss a thing"
         [:br]
         [:span {:class "font-bold"} "Subscribe for more content!"]]

        ;; Email input
        [:div {:class "max-w-md"}
         [:input {:type "email"
                  :name "EMAIL"
                  :id email-field-id
                  :placeholder "Your email"
                  :required true
                  :class "w-full px-4 py-3 rounded-lg bg-white text-gray-900 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-[#ea4a35]"}]]

        ;; Submit button
        [:input {:type "submit"
                 :value "Subscribe"
                 :data-wait "Please wait..."
                 :class "px-8 py-3 bg-[#2d3a54] hover:bg-[#3d4a64] text-white font-medium rounded-lg cursor-pointer transition-colors"}]]

       ;; Success message
       [:div {:class "hidden mt-4 p-4 bg-green-100 text-green-800 rounded-lg"
              :id (str form-id "-success")}
        "Thank you! Your submission has been received!"]

       ;; Error message
       [:div {:class "hidden mt-4 p-4 bg-red-100 text-red-800 rounded-lg"
              :id (str form-id "-error")}
        "Oops! Something went wrong while submitting the form."]

       ;; Privacy notice
       [:p {:class "mt-4 text-xs text-gray-400 max-w-md leading-relaxed"}
        "By clicking \"Subscribe\" you agree to Health Samurai "
        [:a {:href "https://www.health-samurai.io/legal/privacy-policy"
             :target "_blank"
             :rel "noopener noreferrer"
             :class "text-[#ea4a35] hover:underline"}
         "Privacy Policy"]
        " and consent to Health Samurai using your contact data for newsletter purposes"]]

      ;; Right column - illustration
      [:div {:class "hidden md:block flex-shrink-0 p-8"}
       [:img {:src samurai-image-url
              :alt "Subscribe illustration"
              :loading "lazy"
              :class "w-[280px] h-auto"}]]]]))

(defn subscribe-section
  "Newsletter subscription section - exact copy of health-samurai.io/blog.
   Options:
   - :with-margin? - adds top margin (for placement after 3 articles)
   - :page-url - current page URL for tracking
   - :email-id - unique ID for email input (to avoid duplicate IDs on page)
   - :standalone? - true when used outside of article list (before footer), wraps in container"
  [{:keys [with-margin? page-url email-id standalone?]}]
  (if standalone?
    ;; Standalone: wrap in container with max-width (like before footer)
    [:div {:class "w-full px-4 md:px-8 my-8"}
     [:div {:class "mx-auto"
            :style {:max-width "1280px"}}
      (subscribe-box {:email-id email-id
                      :page-url page-url})]]
    ;; Inline: full width within article container
    [:div {:class (str "my-8" (when with-margin? " mt-10"))}
     (subscribe-box {:email-id email-id
                     :page-url page-url})]))
