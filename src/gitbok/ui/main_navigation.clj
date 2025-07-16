(ns gitbok.ui.main-navigation
  (:require [gitbok.http]))

(defn nav [context]
  [:nav {:class "w-full bg-header-bg backdrop-blur-header border-b border-header-border flex-shrink-0 sticky top-0 z-50 shadow-header"
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
      [:div {:class "flex items-center gap-3 px-4 py-2.5 bg-tint-1 border
             border-tint-6 rounded-aidbox-lg text-tint-11 text-sm transition-all duration-200
             hover:bg-tint-1 hover:border-tint-7 sm:w-[35rem] focus-within:border-primary-7"}
       [:svg {:class "size-4 text-tint-9" :fill "none" :stroke "currentColor" :viewBox "0 0 24 24"}
        [:path {:stroke-linecap "round" :stroke-linejoin "round" :stroke-width "2" :d "m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607Z"}]]
       [:input {:type "text"
                :id "search-input"
                :name "q"
                :placeholder "Ask or search..."
                :class "flex-1 bg-transparent outline-none placeholder-tint-9 text-tint-strong font-normal"
                :autocomplete "off"
                :hx-get "/search/dropdown"
                :hx-trigger "keyup[!event.key.startsWith('Arrow') && event.key !== 'Enter' && event.key !== 'Escape'] changed delay:300ms, focus"
                :hx-target "#search-dropdown"
                :hx-swap "innerHTML"
                :hx-indicator "#search-indicator"}]
       [:span {:id "search-shortcut" :class "hidden sm:block text-xs text-tint-9 font-normal transition-opacity duration-200"} "Ctrl + K"]
       [:div {:id "search-indicator" :class "htmx-indicator"}
        [:svg {:class "animate-spin size-4 text-tint-9" :fill "none" :viewBox "0 0 24 24"}
         [:circle {:class "opacity-25" :cx "12" :cy "12" :r "10" :stroke "currentColor" :stroke-width "4"}]
         [:path {:class "opacity-75" :fill "currentColor" :d "M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"}]]]]
      [:div {:id "search-dropdown"
             :class "absolute top-full mt-2 left-0 right-0 z-50"}]]]]])
