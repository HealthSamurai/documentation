(ns gitbok.ui.main-navigation
  (:require [gitbok.http]))

(defn nav [context]
  [:nav {:class "w-full bg-header-bg backdrop-blur-header border-b border-header-border flex-shrink-0 sticky top-0 z-50"
         :aria-label "Main site menu"}
   [:div {:class "flex items-center justify-between py-3 min-h-16 px-4 sm:px-6 md:px-8 max-w-screen-2xl mx-auto"}
    [:div {:class "flex max-w-full lg:basis-72 min-w-0 shrink items-center justify-start gap-2 lg:gap-4"}
     [:button {:class "p-2 text-tint-strong hover:text-primary-9 transition-colors duration-200 md:hidden"
               :id "mobile-menu-toggle"
               :type "button"
               :aria-label "Toggle mobile menu"}
      [:svg {:class "size-6" :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
       [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5"}]]]

     [:a {:href
          (gitbok.http/get-url context)
          :class "group/headerlogo min-w-0 shrink flex items-center"}
      [:img {:alt "Aidbox Logo"
             :class "block object-contain size-8"
             :fetchpriority "high"
             :src
             (gitbok.http/get-absolute-url
              context
              "/.gitbook/assets/aidbox_logo.jpg")}]
      [:div {:class "text-pretty line-clamp-2 tracking-tight max-w-[18ch] lg:max-w-[24ch] font-semibold ms-3 text-lg/tight lg:text-xl/tight text-tint-strong"}
       "Aidbox User Docs"]]]

    [:div {:class "flex items-center gap-4"}
     [:div {:class "hidden md:flex items-center gap-4"}
      [:a {:href "/getting-started/run-aidbox-locally"
           :class "text-small text-tint-10 hover:text-primary-9 transition-colors duration-200 no-underline font-normal"}
       "Run Aidbox locally"]
      [:a {:href "/getting-started/run-aidbox-in-sandbox"
           :class "text-small text-tint-10 hover:text-primary-9 transition-colors duration-200 no-underline font-normal"}
       "Run Aidbox in Sandbox"]
      [:a {:href "https://bit.ly/3R7eLke"
           :target "_blank"
           :class "text-small text-tint-10 hover:text-primary-9 transition-colors duration-200 no-underline font-normal"}
       "Talk to us"]
      [:a {:href "https://connect.health-samurai.io/"
           :target "_blank"
           :class "text-small text-tint-10 hover:text-primary-9 transition-colors duration-200 no-underline font-normal"}
       "Ask community"]]

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

      ;; Desktop search input
      [:div {:class "hidden md:flex relative h-9 w-60 max-w-64"
             :id "desktop-search-container"}
       [:div {:class "flex h-9 w-full items-center overflow-hidden
              rounded-md border border-tint-subtle bg-tint-base
              text-tint shadow-sm transition-all
              hover:border-tint-hover
              hover:shadow-md
              has-[input:focus-visible]:ring-2
              has-[input:focus-visible]:ring-primary-7"}
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
                  :hx-get "/search/dropdown"
                  :hx-trigger "keyup[!event.key.startsWith('Arrow') && event.key !== 'Enter' && event.key !== 'Escape'] changed delay:300ms, focus"
                  :hx-target "#search-dropdown"
                  :hx-swap "innerHTML"
                  :hx-indicator "#search-indicator"}]]
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

      ;; Mobile search container (hidden by default)
      [:div {:class "hidden fixed inset-x-0 top-16 z-50 bg-header-bg border-b border-header-border p-4 md:hidden"
             :id "mobile-search-container"}
       [:div {:class "flex items-center gap-3 px-4 py-2.5 bg-tint-1 border
              border-tint-6 rounded-aidbox-lg text-tint-11 text-sm transition-all duration-200
              hover:bg-tint-1 hover:border-tint-7 focus-within:border-primary-7"}
        [:svg {:class "size-4 text-tint-9" :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
         [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607Z"}]]
        [:input {:type "text"
                 :id "mobile-search-input"
                 :name "q"
                 :placeholder "Ask or search..."
                 :class "flex-1 bg-transparent outline-none placeholder-tint-9 text-tint-strong font-normal"
                 :autocomplete "off"
                 :hx-get "/search/dropdown"
                 :hx-trigger "keyup[!event.key.startsWith('Arrow') && event.key !== 'Enter' && event.key !== 'Escape'] changed delay:300ms, focus"
                 :hx-target "#mobile-search-dropdown"
                 :hx-swap "innerHTML"
                 :hx-indicator "#mobile-search-indicator"}]
        [:div {:id "mobile-search-indicator" :class "htmx-indicator"}
         [:svg {:class "animate-spin size-4 text-tint-9" :fill "none" :viewBox "0 0 24 24"}
          [:circle {:class "opacity-25" :cx "12" :cy "12" :r "10" :stroke "currentColor" :stroke-width "4"}]
          [:path {:class "opacity-75" :fill "currentColor" :d "M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 714 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"}]]]
        [:button {:class "p-1 text-tint-9 hover:text-tint-strong transition-colors"
                  :id "mobile-search-close"
                  :type "button"
                  :aria-label "Close search"}
         [:svg {:class "size-5" :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
          [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "M6 18L18 6M6 6l12 12"}]]]]
       [:div {:id "mobile-search-dropdown"
              :class "mt-2 z-50"}]]

      ;; Dropdown container (for both desktop and mobile)
      [:div {:id "search-dropdown"
             :class "absolute top-full mt-2 right-0 left-0 md:left-auto z-50"}]]]]])
