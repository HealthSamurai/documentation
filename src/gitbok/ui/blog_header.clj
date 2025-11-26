(ns gitbok.ui.blog-header
  (:require [gitbok.http :as http]))

(def search-icon
  "Search icon SVG component"
  [:svg {:class "size-4 text-on-surface-placeholder shrink-0"
         :fill "none"
         :stroke "currentColor"
         :viewBox "0 0 24 24"}
   [:path {:stroke-linecap "round"
           :stroke-linejoin "round"
           :stroke-width "2"
           :d "m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607Z"}]])

(defn blog-nav [context]
  [:nav {:class "w-full bg-header-bg backdrop-blur-xl border-b border-header-border flex-shrink-0 sticky top-0 z-50"
         :aria-label "Blog navigation"}
   [:div {:class "flex items-center justify-between py-3 min-h-16 px-4 sm:px-6 md:px-8 max-w-screen-2xl mx-auto"}
    ;; Left section: Logo and brand
    [:div {:class "flex items-center"}
     [:a {:href "https://www.health-samurai.io/"
          :class "group/headerlogo flex items-center"
          :target "_blank"
          :rel "noopener noreferrer"}
      [:img {:alt "Health Samurai Logo"
             :class "block object-contain size-8"
             :fetchpriority "high"
             :src "/.gitbook/assets/aidbox_logo.jpg"}]
      [:div {:class "text-pretty tracking-tight font-semibold ms-3 text-lg/tight lg:text-xl/tight text-on-surface-strong"}
       "Health Samurai"]]]

    ;; Center: Navigation links
    [:nav {:class "hidden lg:flex items-center gap-8"}
     [:a {:href "https://www.health-samurai.io/"
          :target "_blank"
          :rel "noopener noreferrer"
          :class "text-sm leading-5 text-on-surface-strong hover:text-brand transition-colors duration-200 font-normal no-underline"}
      "Home"]
     [:a {:href "/blog"
          :class "text-sm leading-5 text-on-surface-strong hover:text-brand transition-colors duration-200 font-normal no-underline"}
      "Blog"]
     [:a {:href "https://www.health-samurai.io/aidbox"
          :target "_blank"
          :rel "noopener noreferrer"
          :class "text-sm leading-5 text-on-surface-strong hover:text-brand transition-colors duration-200 font-normal no-underline"}
      "Products"]
     [:a {:href "/aidbox"
          :class "text-sm leading-5 text-on-surface-strong hover:text-brand transition-colors duration-200 font-normal no-underline"}
      "Documentation"]]

    ;; Right section: Search and CTA
    [:div {:class "flex items-center gap-4"}
     ;; Desktop search
     [:div {:class "hidden md:block relative"}
      [:div {:class "relative"}
       [:input {:type "search"
                :placeholder "Search blog..."
                :class "w-64 pl-10 pr-4 py-2 text-sm bg-surface-subtle border border-outline rounded-lg focus:outline-none focus:ring-2 focus:ring-brand transition-all"
                :hx-get "/blog/search"
                :hx-trigger "keyup changed delay:300ms"
                :hx-target "#blog-search-results"
                :hx-indicator "#search-spinner"
                :autocomplete "off"
                :name "q"}]
       [:div {:class "absolute left-3 top-1/2 -translate-y-1/2"}
        search-icon]
       [:div {:id "search-spinner"
              :class "htmx-indicator absolute right-3 top-1/2 -translate-y-1/2"}
        [:div {:class "w-4 h-4 border-2 border-brand border-t-transparent rounded-full animate-spin"}]]]
      ;; Search results dropdown
      [:div {:id "blog-search-results"
             :class "absolute top-full mt-2 w-full bg-surface rounded-lg shadow-lg border border-outline max-h-96 overflow-y-auto hidden"}]]

     ;; Mobile search toggle
     [:button {:class "md:hidden p-2 text-on-surface-strong hover:text-brand transition-colors"
               :id "mobile-search-toggle"
               :aria-label "Toggle search"}
      search-icon]

     ;; CTA Button
     [:a {:href "https://www.health-samurai.io/aidbox#get-started"
          :target "_blank"
          :rel "noopener noreferrer"
          :class "hidden sm:inline-flex px-4 py-2 bg-brand text-white rounded-lg hover:bg-brand/90 transition-colors font-medium text-sm no-underline"}
      "Get Started"]]]

   ;; Mobile search (hidden by default)
   [:div {:id "mobile-search"
          :class "hidden px-4 pb-4 md:hidden"}
    [:div {:class "relative"}
     [:input {:type "search"
              :placeholder "Search blog..."
              :class "w-full pl-10 pr-4 py-2 text-sm bg-surface-subtle border border-outline rounded-lg focus:outline-none focus:ring-2 focus:ring-brand"
              :hx-get "/blog/search"
              :hx-trigger "keyup changed delay:300ms"
              :hx-target "#mobile-search-results"
              :hx-indicator "#mobile-search-spinner"
              :autocomplete "off"
              :name "q"}]
     [:div {:class "absolute left-3 top-1/2 -translate-y-1/2"}
      search-icon]
     [:div {:id "mobile-search-spinner"
            :class "htmx-indicator absolute right-3 top-1/2 -translate-y-1/2"}
      [:div {:class "w-4 h-4 border-2 border-brand border-t-transparent rounded-full animate-spin"}]]]
    [:div {:id "mobile-search-results"
           :class "mt-2 bg-surface rounded-lg shadow-lg border border-outline max-h-96 overflow-y-auto hidden"}]]

   ;; Mobile search toggle script
   [:script
    "
    document.getElementById('mobile-search-toggle')?.addEventListener('click', function() {
      const mobileSearch = document.getElementById('mobile-search');
      mobileSearch.classList.toggle('hidden');
    });
    "]])
