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
       [:button {:class "p-2 text-tint-strong hover:text-primary-9 transition-colors duration-200 lg:hidden"
                 :id "mobile-menu-toggle"
                 :type "button"
                 :aria-label "Toggle mobile menu"}
        [:svg {:class "size-6" :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
         [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5"}]]]

       ;; Logo and product name - conditionally aligned
       [:div {:class (if nav-links-right?
                       "flex items-center"
                       "flex items-center lg:w-80 flex-shrink-0")}
        [:a {:href (http/get-product-prefixed-url context "/")
             :class "group/headerlogo flex items-center"}
         [:img {:alt (str (:name product) " Logo")
                :class "block object-contain size-8"
                :fetchpriority "high"
                :src (http/get-absolute-url
                      context
                      (or (:logo product) "/.gitbook/assets/aidbox_logo.jpg"))}]
         [:div {:class "text-pretty tracking-tight font-semibold ms-3 text-lg/tight lg:text-xl/tight text-tint-strong"}
          (:name product)]]]

       ;; Navigation container - shown on left when nav-links-right is false
       (when-not nav-links-right?
         [:nav {:id "top-navigation"
                :class "hidden lg:flex items-center gap-4"}
          (let [links (or (:links product) [])]
            (for [link links]
              (if (:title link)
                ;; Dropdown menu item
                [:div {:class "relative group" :key (:title link)}
                 [:button {:class "flex items-center gap-1 text-small text-tint-10 hover:text-primary-9 transition-colors duration-200 font-normal"
                           :type "button"
                           :aria-label (str (:title link) " menu")
                           :aria-expanded "false"
                           :aria-haspopup "true"}
                  [:span (:title link)]
                  [:svg {:class "size-4 transition-transform duration-200 group-hover:rotate-180" :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
                   [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M19 9l-7 7-7-7"}]]]
                 ;; Dropdown menu (shows on hover)
                 [:div {:class "invisible opacity-0 group-hover:visible group-hover:opacity-100 transition-all duration-200 absolute top-full mt-2 w-48 rounded-md shadow-lg bg-tint-base z-50"}
                  [:div {:class "py-1" :role "menu" :aria-orientation "vertical"}
                   (for [entry (:entries link)]
                     (let [external? (when (:href entry)
                                       (str/starts-with? (:href entry) "http"))
                           href (if external?
                                  (:href entry)
                                  (http/get-product-prefixed-url context (:href entry)))]
                       [:a (cond->
                            {:href href
                             :class "block px-4 py-2 text-sm text-tint-11 hover:bg-tint-hover no-underline"
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
                               :class "text-small text-tint-10 hover:text-primary-9 transition-colors duration-200 no-underline font-normal"
                               :key (:text link)}
                        (:target link)
                        (assoc :target (:target link))))
                 (:text link)])))])]

      ;; Right section: Navigation (if nav-links-right), Search, Sign up button
      [:div {:class "flex items-center gap-3"}
       ;; Navigation links when nav-links-right is true
       (when nav-links-right?
         [:nav {:id "top-navigation"
                :class "hidden lg:flex items-center gap-4"}
          (let [links (or (:links product) [])]
            (for [link links]
              (if (:title link)
                ;; Dropdown menu item
                [:div {:class "relative group" :key (:title link)}
                 [:button {:class "flex items-center gap-1 text-small text-tint-10 hover:text-primary-9 transition-colors duration-200 font-normal"
                           :type "button"
                           :aria-label (str (:title link) " menu")
                           :aria-expanded "false"
                           :aria-haspopup "true"}
                  [:span (:title link)]
                  [:svg {:class "size-4 transition-transform duration-200 group-hover:rotate-180" :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
                   [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M19 9l-7 7-7-7"}]]]
                 ;; Dropdown menu (shows on hover)
                 [:div {:class "invisible opacity-0 group-hover:visible group-hover:opacity-100 transition-all duration-200 absolute top-full mt-2 w-48 rounded-md shadow-lg bg-tint-base z-50"}
                  [:div {:class "py-1" :role "menu" :aria-orientation "vertical"}
                   (for [entry (:entries link)]
                     (let [external? (when (:href entry)
                                       (str/starts-with? (:href entry) "http"))
                           href (if external?
                                  (:href entry)
                                  (http/get-product-prefixed-url context (:href entry)))]
                       [:a (cond->
                            {:href href
                             :class "block px-4 py-2 text-sm text-tint-11 hover:bg-tint-hover no-underline"
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
                               :class "text-small text-tint-10 hover:text-primary-9 transition-colors duration-200 no-underline font-normal"
                               :key (:text link)}
                        (:target link)
                        (assoc :target (:target link))))
                 (:text link)])))])

       ;; Ellipsis menu for tablet/mobile (includes Product dropdown items)
       [:div {:class "relative lg:hidden"}
        [:button {:class "p-2 text-tint-strong hover:text-primary-9 transition-colors duration-200"
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
               :class "hidden absolute right-0 mt-2 w-64 rounded-md shadow-lg outline-none ring-1 ring-tint-subtle bg-tint-base z-50"}
         [:div {:class "py-1" :role "menu" :aria-orientation "vertical"}
          ;; Product navigation links
          (let [links (or (:links product) [])]
            (concat
             ;; Regular links and dropdown sections
             (mapcat (fn [link]
                       (if (:title link)
                         ;; Dropdown section in mobile menu
                         (concat
                          [[:div {:class "px-4 py-2 text-xs font-semibold text-tint-9 uppercase tracking-wide"}
                            (:title link)]]
                          (for [entry (:entries link)]
                            [:a (merge
                                 {:href (:href entry)
                                  :class "block px-4 py-2 text-sm text-tint-11 hover:bg-tint-hover hover:text-primary-9 no-underline"
                                  :role "menuitem"}
                                 (when (:target entry)
                                   {:target (:target entry)}))
                             (:text entry)]))
                         ;; Regular link
                         [[:a (merge
                               {:href (if (clojure.string/starts-with? (:href link) "http")
                                        (:href link)
                                        (http/get-product-prefixed-url context (:href link)))
                                :class "block px-4 py-2 text-sm text-tint-11 hover:bg-tint-hover hover:text-primary-9 no-underline"
                                :role "menuitem"}
                               (when (:target link)
                                 {:target (:target link)}))
                           (:text link)]]))
                     links)
             ;; Sign up link for mobile - only for Aidbox
             #_(when (= (:id product) "aidbox")
                 [[:div {:class "border-t border-tint-subtle my-1"}]
                  [:a {:href "https://aidbox.app/ui/portal#/signup"
                       :class "block px-4 py-2 text-sm text-primary-9 hover:bg-tint-hover no-underline font-medium"
                       :role "menuitem"}
                   "Sign up"]])))]]]

       ;; Search section
       [:div {:class "relative"}
        ;; Mobile search button
        [:button {:class "md:hidden p-2 text-tint-strong hover:text-primary-9 transition-colors duration-200 cursor-pointer"
                  :id "mobile-search-toggle"
                  :type "button"
                  :aria-label "Toggle search"}
         [:div {:class "border border-tint-6 rounded p-2 bg-tint-1 hover:border-tint-7 transition-colors"}
          [:svg {:class "size-5" :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
           [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2"
                   :d "m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607Z"}]]]]

        ;; Meilisearch custom search with HTMX (desktop)
        [:div {:class "hidden md:block"}
         [:div {:class "relative h-9 w-60 max-w-64"
                :id "meilisearch-wrapper"}
          [:div {:class "flex h-9 w-full items-center overflow-hidden
                         rounded-md border border-tint-subtle bg-tint-base
                         text-tint shadow-sm transition-all
                         hover:border-tint-hover
                         hover:shadow-md
                         has-[input:focus-visible]:ring-2
                         has-[input:focus-visible]:ring-primary-7
                         focus-within:[&_#meilisearch-shortcut]:hidden"}
           [:div {:class "flex min-w-0 flex-1 items-center gap-2"}
            [:div {:class "flex items-center justify-center pl-3"}
             [:svg {:class "size-4 text-tint-9 shrink-0"
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
                            placeholder:text-tint-9
                            text-tint-strong
                            font-normal"
                     :autocomplete "off"
                     :hx-get (http/get-product-prefixed-url context "/meilisearch/dropdown")
                     :hx-trigger "keyup[!event.key.startsWith('Arrow') && event.key !== 'Enter' && event.key !== 'Escape'] changed delay:300ms, focus"
                     :hx-target "#meilisearch-dropdown"
                     :hx-swap "innerHTML"
                     :hx-indicator "#meilisearch-indicator"
                     :onfocus "document.getElementById('meilisearch-shortcut').style.display='none'"
                     :onblur "document.getElementById('meilisearch-shortcut').style.display='flex'"}]]
           [:div {:class "flex shrink-0 items-center pr-3"}
            [:div {:id "meilisearch-shortcut"
                   :class "hidden sm:flex justify-end gap-0.5 whitespace-nowrap text-xs text-tint-9 font-normal transition-opacity duration-200"}
             [:kbd {:class "inline-flex h-5 min-w-5 items-center justify-center rounded border border-tint-subtle bg-tint-base px-1 text-xs"} "Ctrl"]
             [:kbd {:class "inline-flex h-5 min-w-5 items-center justify-center rounded border border-tint-subtle bg-tint-base px-1 text-xs"} "K"]]
            [:div {:id "meilisearch-indicator"
                   :class "htmx-indicator"}
             [:svg {:class "animate-spin size-4 text-tint-9"
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
                      :d "M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 714 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"}]]]]]
          [:div {:id "meilisearch-dropdown"
                 :class "absolute top-full mt-2 right-0 left-0 md:left-auto md:min-w-[32rem] z-50"}]]]

        ;; Desktop search input (existing HTMX search) - hidden when Meilisearch is active
        [:div {:class "hidden relative h-9 w-60 max-w-64"
               :id "desktop-search-container"}
         [:div {:class "flex h-9 w-full items-center overflow-hidden
                rounded-md border border-tint-subtle bg-tint-base
                text-tint shadow-sm transition-all
                hover:border-tint-hover
                hover:shadow-md
                has-[input:focus-visible]:ring-2
                has-[input:focus-visible]:ring-primary-7
                focus-within:[&_#search-shortcut]:hidden"}
          [:div {:class "flex min-w-0 flex-1 items-center gap-2"}
           [:div {:class "flex items-center justify-center pl-3"}
            [:svg {:class "size-4 text-tint-9 shrink-0" :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
             [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607Z"}]]]
           [:input {:type "text"
                    :id "search-input"
                    :name "q"
                    :placeholder "Ask or search..."
                    :class
                    "min-w-0 flex-1 bg-transparent py-1.5 pr-3
                    text-sm outline-none placeholder:text-sm
                    placeholder:text-tint-9
                    text-tint-strong
                    font-normal"
                    :autocomplete "off"
                    :hx-get (http/get-product-prefixed-url context "/search/dropdown")
                    :hx-trigger "keyup[!event.key.startsWith('Arrow') && event.key !== 'Enter' && event.key !== 'Escape'] changed delay:300ms, focus"
                    :hx-target "#search-dropdown"
                    :hx-swap "innerHTML"
                    :hx-indicator "#search-indicator"
                    :onfocus "document.getElementById('search-shortcut').style.display='none'"
                    :onblur "document.getElementById('search-shortcut').style.display='flex'"}]]
          [:div {:class "flex shrink-0 items-center pr-3"}
           [:div {:id "search-shortcut"
                  :class "hidden sm:flex justify-end gap-0.5 whitespace-nowrap text-xs text-tint-9 font-normal transition-opacity duration-200"}
            [:kbd {:class "inline-flex h-5 min-w-5 items-center justify-center rounded border border-tint-subtle bg-tint-base px-1 text-xs"} "Ctrl"]
            [:kbd {:class "inline-flex h-5 min-w-5 items-center justify-center rounded border border-tint-subtle bg-tint-base px-1 text-xs"} "K"]]
           [:div {:id "search-indicator" :class "htmx-indicator"}
            [:svg {:class "animate-spin size-4 text-tint-9" :fill "none" :viewBox "0 0 24 24"}
             [:circle {:class "opacity-25" :cx "12" :cy "12" :r "10" :stroke "currentColor" :stroke-width "4"}]
             [:path {:class "opacity-75" :fill "currentColor"
                     :d "M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 714 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"}]]]]]]

        ;; Mobile search container with Meilisearch (hidden by default)
        [:div {:class "hidden fixed inset-x-0 top-16 z-50 bg-white border-b border-tint-6 md:hidden"
               :id "mobile-search-container"}
         [:div {:class "p-4"}
          [:div {:class "flex items-center gap-3 px-4 py-2.5 bg-white border
                 border-tint-6 rounded-aidbox-lg text-tint-11 text-sm transition-all duration-200
                 hover:bg-white hover:border-tint-7 focus-within:border-primary-7"}
           [:svg {:class "size-4 text-tint-9" :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
            [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607Z"}]]
           [:input {:type "text"
                    :id "mobile-meilisearch-input"
                    :name "q"
                    :placeholder "Ask or search..."
                    :class "flex-1 bg-transparent outline-none placeholder-tint-9 text-tint-strong font-normal"
                    :autocomplete "off"
                    :hx-get (http/get-product-prefixed-url context "/meilisearch/dropdown?mobile=true")
                    :hx-trigger "keyup[!event.key.startsWith('Arrow') && event.key !== 'Enter' && event.key !== 'Escape'] changed delay:300ms, focus"
                    :hx-target "#mobile-meilisearch-dropdown"
                    :hx-swap "innerHTML"
                    :hx-indicator "#mobile-meilisearch-indicator"}]
           [:div {:id "mobile-meilisearch-indicator" :class "htmx-indicator"}
            [:svg {:class "animate-spin size-4 text-tint-9" :fill "none" :viewBox "0 0 24 24"}
             [:circle {:class "opacity-25" :cx "12" :cy "12" :r "10" :stroke "currentColor" :stroke-width "4"}]
             [:path {:class "opacity-75" :fill "currentColor" :d "M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 714 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"}]]]
           [:button {:class "p-1 text-tint-9 hover:text-tint-strong transition-colors"
                     :id "mobile-search-close"
                     :type "button"
                     :aria-label "Close search"}
            [:svg {:class "size-5" :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
             [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M6 18L18 6M6 6l12 12"}]]]]]
         [:div {:id "mobile-meilisearch-dropdown"
                :class "px-4 pb-4 z-50 max-h-[60vh] overflow-y-auto"}]]

        ;; Dropdown container (for both desktop and mobile)
        [:div {:id "search-dropdown"
               :class "absolute top-full mt-2 right-0 left-0 md:left-auto z-50"}]]

       ;; Sign up button (desktop only) - only for Aidbox
       #_(when (= (:id product) "aidbox")
           [:a {:href "https://aidbox.app/ui/portal#/signup"
                :class "hidden lg:inline-flex items-center justify-center px-4 py-2 text-sm font-medium text-white bg-primary-9 hover:bg-primary-10 rounded-md transition-colors duration-200 no-underline"}
            "Sign up"])]])])
