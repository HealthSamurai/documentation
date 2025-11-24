(ns gitbok.ui.main-navigation
  (:require
   [gitbok.http :as http]
   [clojure.string :as str]
   [gitbok.products :as products]))

(defn nav [context]
  [:nav {:class "w-full bg-header-bg backdrop-blur-xl border-b border-header-border flex-shrink-0 sticky top-0 z-50"
         :aria-label "Main site menu"}
   (let [product (products/get-current-product context)
         nav-links-right? (:nav-links-right product)]
     [:div {:class "flex items-center justify-between py-3 min-h-16 px-4 sm:px-6 md:px-8 max-w-screen-2xl mx-auto"}
      ;; Left section: Mobile menu toggle and logo with product name
      [:div {:class "flex items-center"}
       ;; Mobile menu toggle
       [:button {:class "p-2 text-on-surface-strong hover:text-brand transition-colors duration-200 lg:hidden"
                 :id "mobile-menu-toggle"
                 :type "button"
                 :aria-label "Toggle mobile menu"}
        [:svg {:class "size-6" :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
         [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5"}]]]

       ;; Logo and product name - conditionally aligned
       [:div {:class (if nav-links-right?
                       "flex items-center"
                       "flex items-center flex-shrink-0")}
        [:a {:href (http/get-product-prefixed-url context "/")
             :class "group/headerlogo flex items-center"}
         [:img {:alt (str (:name product) " Logo")
                :class "block object-contain size-8"
                :fetchpriority "high"
                :src (http/get-absolute-url
                      context
                      (or (:logo product) "/.gitbook/assets/aidbox_logo.jpg"))}]
         [:div {:class "text-pretty tracking-tight font-semibold ms-3 text-lg/tight lg:text-xl/tight text-on-surface-strong"}
          (:name product)]]]

       ;; Navigation container - shown on left when nav-links-right is false
       (when-not nav-links-right?
         [:nav {:id "top-navigation"
                :class "hidden lg:flex items-center gap-8 ml-16"}
          (let [links (or (:links product) [])]
            (for [link links]
              (if (:title link)
                ;; Dropdown menu item
                [:div {:class "relative group" :key (:title link)}
                 [:button {:class "flex items-center gap-1 text-sm leading-5 text-on-surface-strong hover:text-brand transition-colors duration-200 font-normal"
                           :type "button"
                           :aria-label (str (:title link) " menu")
                           :aria-expanded "false"
                           :aria-haspopup "true"}
                  [:span (:title link)]
                  [:svg {:class "size-4 transition-transform duration-200 group-hover:rotate-180" :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
                   [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M19 9l-7 7-7-7"}]]]
                 ;; Dropdown menu (shows on hover)
                 [:div {:class "invisible opacity-0 group-hover:visible group-hover:opacity-100 transition-all duration-200 absolute top-full mt-2 w-48 rounded-md shadow-lg bg-surface z-50"}
                  [:div {:class "py-1" :role "menu" :aria-orientation "vertical"}
                   (for [entry (:entries link)]
                     (let [external? (when (:href entry)
                                       (str/starts-with? (:href entry) "http"))
                           href (if external?
                                  (:href entry)
                                  (http/get-product-prefixed-url context (:href entry)))]
                       [:a (cond->
                            {:href href
                             :class "block px-4 py-2 text-sm text-on-surface-strong hover:text-brand hover:bg-surface-hover no-underline transition-colors duration-200 font-normal"
                             :role "menuitem"
                             :key (:text entry)}
                             (:target entry)
                             (assoc :target (:target entry)))
                        (:text entry)]))]]]
                ;; Regular link
                [:a (let [external? (str/starts-with? (:href link) "http")
                          href (if external?
                                 (:href link)
                                 (http/get-product-prefixed-url context (:href link)))]
                      (cond-> {:href href
                               :class "text-sm leading-5 text-on-surface-strong hover:text-brand transition-colors duration-200 no-underline font-normal"
                               :key (:text link)}
                        (:target link)
                        (assoc :target (:target link))))
                 (:text link)])))])]

      ;; Right section: Navigation (if nav-links-right), Search, Login button
      [:div {:class "flex items-center gap-6"}
       ;; Navigation links when nav-links-right is true
       (when nav-links-right?
         [:nav {:id "top-navigation"
                :class "hidden lg:flex items-center gap-4"}
          (let [links (or (:links product) [])]
            (for [link links]
              (if (:title link)
                ;; Dropdown menu item
                [:div {:class "relative group" :key (:title link)}
                 [:button {:class "flex items-center gap-1 text-sm leading-5 text-on-surface-strong hover:text-brand transition-colors duration-200 font-normal"
                           :type "button"
                           :aria-label (str (:title link) " menu")
                           :aria-expanded "false"
                           :aria-haspopup "true"}
                  [:span (:title link)]
                  [:svg {:class "size-4 transition-transform duration-200 group-hover:rotate-180" :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
                   [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M19 9l-7 7-7-7"}]]]
                 ;; Dropdown menu (shows on hover)
                 [:div {:class "invisible opacity-0 group-hover:visible group-hover:opacity-100 transition-all duration-200 absolute top-full mt-2 w-48 rounded-md shadow-lg bg-surface z-50"}
                  [:div {:class "py-1" :role "menu" :aria-orientation "vertical"}
                   (for [entry (:entries link)]
                     (let [external? (when (:href entry)
                                       (str/starts-with? (:href entry) "http"))
                           href (if external?
                                  (:href entry)
                                  (http/get-product-prefixed-url context (:href entry)))]
                       [:a (cond->
                            {:href href
                             :class "block px-4 py-2 text-sm text-on-surface-strong hover:text-brand hover:bg-surface-hover no-underline transition-colors duration-200 font-normal"
                             :role "menuitem"
                             :key (:text entry)}
                             (:target entry)
                             (assoc :target (:target entry)))
                        (:text entry)]))]]]
                ;; Regular link
                [:a (let [external? (str/starts-with? (:href link) "http")
                          href (if external?
                                 (:href link)
                                 (http/get-product-prefixed-url context (:href link)))]
                      (cond-> {:href href
                               :class "text-sm leading-5 text-on-surface-strong hover:text-brand transition-colors duration-200 no-underline font-normal"
                               :key (:text link)}
                        (:target link)
                        (assoc :target (:target link))))
                 (:text link)])))])

       ;; Ellipsis menu for tablet/mobile (includes Product dropdown items)
       [:div {:class "relative lg:hidden"}
        [:button {:class "p-2 text-on-surface-strong hover:text-brand transition-colors duration-200"
                  :id "ellipsis-menu-toggle"
                  :type "button"
                  :aria-label "More options"
                  :aria-expanded "false"
                  :aria-haspopup "true"}
         [:svg {:class "size-5" :fill "currentColor" :viewBox "0 0 24 24"}
          [:circle {:cx "5" :cy "12" :r "2"}]
          [:circle {:cx "12" :cy "12" :r "2"}]
          [:circle {:cx "19" :cy "12" :r "2"}]]]

        ;; Dropdown menu
        [:div {:id "ellipsis-dropdown"
               :class "hidden absolute right-0 mt-2 w-64 rounded-md shadow-lg outline-none ring-1 ring-outline-subtle bg-surface z-50"}
         [:div {:class "py-1" :role "menu" :aria-orientation "vertical"}
          ;; Product navigation links
          (let [links (or (:links product) [])]
            (concat
             ;; Regular links and dropdown sections
             (mapcat (fn [link]
                       (if (:title link)
                         ;; Dropdown section in mobile menu
                         (concat
                          (when (not= "PRODUCT" (:title link))
                            [[:div {:class "px-4 py-2 text-xs font-semibold text-on-surface-placeholder uppercase tracking-wide"}
                              (:title link)]])
                          (for [entry (:entries link)]
                            [:a (merge
                                 {:href (:href entry)
                                  :class "block px-4 py-2 text-sm text-on-surface-strong hover:text-brand hover:bg-surface-hover no-underline transition-colors duration-200 font-normal"
                                  :role "menuitem"}
                                 (when (:target entry)
                                   {:target (:target entry)}))
                             (:text entry)]))
                         ;; Regular link
                         [[:a (merge
                               {:href (if (clojure.string/starts-with? (:href link) "http")
                                        (:href link)
                                        (http/get-product-prefixed-url context (:href link)))
                                :class "block px-4 py-2 text-sm text-on-surface-strong hover:text-brand hover:bg-surface-hover no-underline transition-colors duration-200 font-normal"
                                :role "menuitem"}
                               (when (:target link)
                                 {:target (:target link)}))
                           (:text link)]]))
                     links)
             ;; Sign up link for mobile - only for Aidbox
             #_(when (= (:id product) "aidbox")
                 [[:div {:class "border-t border-outline-subtle my-1"}]
                  [:a {:href "https://aidbox.app/ui/portal#/signup"
                       :class "block px-4 py-2 text-sm text-brand hover:bg-surface-hover no-underline font-medium"
                       :role "menuitem"}
                   "Sign up"]])))]]]

       ;; Search section
       [:div {:class "relative"}
        ;; Mobile search button
        [:button {:class "md:hidden p-2 text-on-surface-strong hover:text-brand transition-colors duration-200 cursor-pointer"
                  :id "mobile-search-toggle"
                  :type "button"
                  :aria-label "Toggle search"}
         [:div {:class "border border-outline rounded p-2 bg-surface-subtle hover:border-outline-hover transition-colors"}
          [:svg {:class "size-5" :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
           [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2"
                   :d "m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607Z"}]]]]

        ;; Meilisearch custom search with HTMX (desktop)
        [:div {:class "hidden md:block"}
         [:div {:class "relative h-9 w-64 max-w-64"
                :id "meilisearch-wrapper"}
          [:div {:class "flex h-9 w-full items-center overflow-hidden
                         rounded-md border border-outline bg-surface
                         text-on-surface-secondary transition-all
                         hover:border-outline-input-focus
                         focus-within:border-outline-input-focus
                         focus-within:[&_#meilisearch-shortcut]:hidden"}
           [:div {:class "flex min-w-0 flex-1 items-center gap-2"}
            [:div {:class "flex items-center justify-center pl-3"}
             [:svg {:class "size-4 text-on-surface-placeholder shrink-0"
                    :fill "none"
                    :stroke "currentColor"
                    :viewBox "0 0 24 24"}
              [:path {:stroke-linecap "round"
                      :stroke-linejoin "round"
                      :stroke-width "2"
                      :d "m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607Z"}]]]
            [:input {:type "text"
                     :id "meilisearch-input"
                     :name "q"
                     :placeholder "Ask or search..."
                     :class "min-w-0 flex-1 bg-transparent py-1.5 pr-3
                            text-sm outline-none placeholder:text-sm
                            placeholder:text-on-surface-placeholder
                            text-on-surface-strong
                            font-normal"
                     :autocomplete "off"
                     :hx-get (http/get-product-prefixed-url context "/meilisearch/dropdown")
                     :hx-trigger "keyup[!event.key.startsWith('Arrow') && event.key !== 'Enter' && event.key !== 'Escape'] changed delay:300ms"
                     :hx-target "#meilisearch-dropdown"
                     :hx-swap "innerHTML"
                     :hx-indicator "#meilisearch-indicator"
                     :onfocus "document.getElementById('meilisearch-shortcut').style.display='none'"
                     :onblur "document.getElementById('meilisearch-shortcut').style.display='flex'"}]]
           [:div {:class "flex shrink-0 items-center"}
            [:div {:id "meilisearch-shortcut"
                   :class "hidden sm:flex justify-end gap-0.5 whitespace-nowrap text-xs text-on-surface-placeholder font-normal transition-opacity duration-200"}
             [:kbd {:class "inline-flex h-5 w-[39px] items-center justify-center rounded border border-outline bg-surface-hover px-[5px] py-[1px] text-xs leading-4 text-on-surface-placeholder font-normal text-center"} "Ctrl"]
             [:kbd {:class "inline-flex h-5 w-5 min-w-[20px] items-center justify-center rounded border border-outline bg-surface-hover px-[5px] py-[1px] text-xs leading-4 text-on-surface-placeholder font-normal text-center"} "K"]]
            [:div {:id "meilisearch-indicator"
                   :class "htmx-indicator"}
             [:svg {:class "animate-spin size-4 text-on-surface-placeholder"
                    :fill "none"
                    :viewBox "0 0 24 24"}
              [:circle {:class "opacity-25"
                        :cx "12"
                        :cy "12"
                        :r "10"
                        :stroke "currentColor"
                        :stroke-width "4"}]
              [:path {:class "opacity-75"
                      :fill "currentColor"
                      :d "M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 0 1 4 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"}]]]]]
          [:div {:id "meilisearch-dropdown"
                 :class "absolute top-full mt-2 right-0 left-0 md:left-auto md:min-w-[32rem] z-50"}]]]

        ;; Mobile search container with Meilisearch (hidden by default)
        [:div {:class "hidden fixed inset-x-0 top-16 z-50 bg-surface border-b border-outline md:hidden"
               :id "mobile-search-container"}
         [:div {:class "p-4"}
          [:div {:class "flex items-center gap-3 px-4 py-2.5 bg-surface border
                 border-outline rounded-aidbox-lg text-on-surface-muted text-sm transition-all duration-200
                 hover:bg-surface hover:border-outline-hover focus-within:border-brand"}
           [:svg {:class "size-4 text-on-surface-placeholder" :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
            [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607Z"}]]
           [:input {:type "text"
                    :id "mobile-meilisearch-input"
                    :name "q"
                    :placeholder "Ask or search..."
                    :class "flex-1 bg-transparent outline-none placeholder:text-on-surface-placeholder text-on-surface-strong font-normal"
                    :autocomplete "off"
                    :hx-get (http/get-product-prefixed-url context "/meilisearch/dropdown?mobile=true")
                    :hx-trigger "keyup[!event.key.startsWith('Arrow') && event.key !== 'Enter' && event.key !== 'Escape'] changed delay:300ms"
                    :hx-target "#mobile-meilisearch-dropdown"
                    :hx-swap "innerHTML"
                    :hx-indicator "#mobile-meilisearch-indicator"}]
           [:div {:id "mobile-meilisearch-indicator" :class "htmx-indicator"}
            [:svg {:class "animate-spin size-4 text-on-surface-placeholder" :fill "none" :viewBox "0 0 24 24"}
             [:circle {:class "opacity-25" :cx "12" :cy "12" :r "10" :stroke "currentColor" :stroke-width "4"}]
             [:path {:class "opacity-75" :fill "currentColor" :d "M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 0 1 4 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"}]]]
           [:button {:class "p-1 text-on-surface-placeholder hover:text-on-surface-strong transition-colors"
                     :id "mobile-search-close"
                     :type "button"
                     :aria-label "Close search"}
            [:svg {:class "size-5" :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
             [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M6 18L18 6M6 6l12 12"}]]]]]
         [:div {:id "mobile-meilisearch-dropdown"
                :class "px-4 pb-4 z-50 max-h-[60vh] overflow-y-auto"}]]]

       ;; Login button (desktop only) - far right with borders
       [:a {:href "https://aidbox.app/ui/portal#/signin"
            :class "hidden lg:inline-flex items-center justify-center px-4 py-2 text-sm font-semibold leading-5 bg-surface text-on-surface hover:text-button-hover-text hover:bg-button-hover-bg border border-outline rounded-md transition-colors cursor-pointer"
            :target "_blank"
            :rel "noopener noreferrer"}
        "Login"]]])])
