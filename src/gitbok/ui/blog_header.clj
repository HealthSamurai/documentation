(ns gitbok.ui.blog-header
  (:require [hiccup2.core :as hiccup]
            [gitbok.blog.core :as blog]
            [clojure.string :as str]))

;; Font family from production
(def font-family "'Gotham Pro', Arial, sans-serif")

;; Arrow icon for dropdown banners
(def banner-arrow-url
  "https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/634d407529d4e111967296f9_rightArrow.svg")

;; Dropdown arrow icon (rotates on open)
(defn dropdown-arrow [dropdown-id]
  [:svg {:class "dropdown-arrow w-4 h-4 transition-transform duration-200"
         :data-for dropdown-id
         :fill "none"
         :stroke "currentColor"
         :viewBox "0 0 24 24"
         :style {:margin-left "8px"}}
   [:path {:stroke-linecap "round"
           :stroke-linejoin "round"
           :stroke-width "2.5"
           :d "M19 9l-7 7-7-7"}]])

;; Banner card component (vertical layout with arrow at bottom right)
(defn dropdown-banner-card [{:keys [title desc href icon]}]
  [:a {:href href
       :target "_blank"
       :rel "nofollow"
       :class "group relative flex flex-col p-8 no-underline transition-shadow bg-surface-alt border border-outline rounded"
       :style {:font-family font-family
               :min-height "180px"
               :box-shadow "0 1px 2px rgba(0,0,0,0.05)"}
       :onmouseover "this.style.boxShadow='0 4px 12px rgba(0,0,0,0.15)'"
       :onmouseout "this.style.boxShadow='0 1px 2px rgba(0,0,0,0.05)'"}
   ;; Icon at top
   (when icon
     [:img {:src icon
            :alt ""
            :class "mb-4"
            :style {:height "48px" :width "48px"}}])
   ;; Title (bold, no hover effect)
   [:div {:class "mb-1 text-on-surface-strong text-lg font-bold leading-6"}
    title]
   ;; Description
   [:div {:class "text-on-surface-muted text-sm flex-grow"}
    desc]
   ;; Arrow at bottom right
   [:img {:src banner-arrow-url
          :class "absolute"
          :style {:width "19px"
                  :right "24px"
                  :bottom "24px"}
          :alt ""}]])

;; Simple links column (title only)
(defn dropdown-simple-column [{:keys [title items]}]
  [:div
   (when title
     [:div {:class "uppercase mb-3 tracking-wide text-on-surface-muted text-xs"
            :style {:font-family font-family}}
      title])
   (for [item items]
     ^{:key (:title item)}
     [:a {:href (:href item)
          :target (when-not (:internal item) "_blank")
          :class "block py-2 no-underline hover:underline text-on-surface-strong text-sm"
          :style {:font-family font-family}}
      (:title item)])])

;; Extended links column (title + description)
(defn dropdown-extended-column [{:keys [title items]}]
  [:div
   (when title
     [:div {:class "uppercase mb-3 tracking-wide text-on-surface-muted text-xs"
            :style {:font-family font-family}}
      title])
   (for [item items]
     ^{:key (:title item)}
     [:a {:href (:href item)
          :target "_blank"
          :class "group block mb-3 no-underline"}
      [:div {:class "group-hover:underline text-on-surface-strong text-sm leading-[21px]"
             :style {:font-family font-family}}
       (:title item)]
      [:div {:class "text-on-surface-muted text-sm"
             :style {:font-family font-family}}
       (:desc item)]])])

;; Render a single column based on type
(defn render-column [col]
  (if (= (:type col) "extended")
    (dropdown-extended-column col)
    (dropdown-simple-column col)))

;; Universal dropdown component
(defn render-dropdown [dropdown-id {:keys [columns banner banners]}]
  [:div {:id dropdown-id
         :class "fixed left-0 right-0 top-[65px] shadow-lg hidden z-50 bg-surface-alt border-b border-outline"}
   [:div {:class "max-w-[1200px] mx-auto p-6 grid grid-cols-4 gap-6"}
    ;; Columns
    (for [[idx col] (map-indexed vector columns)]
      ^{:key idx}
      [:div (render-column col)])
    ;; Banner(s)
    (if banners
      (for [[idx b] (map-indexed vector banners)]
        ^{:key (str "banner-" idx)}
        [:div (dropdown-banner-card b)])
      (when banner
        [:div (dropdown-banner-card banner)]))]])

;; Render a single nav item based on type
(defn render-nav-item [item]
  (let [dropdown-id (str "dropdown-" (:id item))]
    (case (:type item)
      "dropdown"
      [:div {:class "relative"}
       [:button {:class "flex items-center cursor-pointer no-underline dropdown-toggle text-on-surface-strong text-sm px-6 py-[21px]"
                 :data-dropdown dropdown-id
                 :style {:font-family font-family}}
        (:label item) (dropdown-arrow dropdown-id)]
       (render-dropdown dropdown-id item)]

      "link"
      [:a {:href (:href item)
           :target "_blank"
           :class "flex items-center no-underline hover:text-brand transition-colors text-on-surface-strong text-sm px-5 py-[21px]"
           :style {:font-family font-family}}
       (:label item)]

      "divider"
      [:div {:class "border-l border-brand h-8 mx-2 my-4"}]

      nil)))

;; Get all dropdown IDs for JavaScript
(defn get-dropdown-ids [nav-items]
  (->> nav-items
       (filter #(= (:type %) "dropdown"))
       (map #(str "dropdown-" (:id %)))
       vec))

;; Hamburger button for mobile menu (right side)
(defn hamburger-button []
  [:button {:id "hamburger-btn"
            :type "button"
            :class "lg:hidden flex items-center justify-center w-10 h-10 cursor-pointer"
            :aria-label "Open menu"}
   [:div {:class "flex flex-col gap-1.5 pointer-events-none"}
    [:span {:class "block w-6 h-0.5 bg-on-surface-strong"}]
    [:span {:class "block w-6 h-0.5 bg-on-surface-strong"}]
    [:span {:class "block w-6 h-0.5 bg-on-surface-strong"}]]])

;; Mobile nav item (simplified version for mobile menu)
(defn render-mobile-nav-item [item]
  (case (:type item)
    "dropdown"
    [:div {:class "border-b border-outline"}
     [:div {:class "py-3 font-medium text-on-surface-strong"
            :style {:font-family font-family}}
      (:label item)]
     ;; Show first-level links from columns
     [:div {:class "pl-4 pb-3"}
      (for [col (:columns item)]
        (for [link-item (:items col)]
          ^{:key (:title link-item)}
          [:a {:href (:href link-item)
               :target (when-not (:internal link-item) "_blank")
               :class "block py-2 text-sm no-underline hover:text-brand text-on-surface-muted"
               :style {:font-family font-family}}
           (:title link-item)]))]]

    "link"
    [:a {:href (:href item)
         :target "_blank"
         :class "block py-3 border-b border-outline no-underline hover:text-brand text-on-surface-strong"
         :style {:font-family font-family}}
     (:label item)]

    "divider"
    [:div {:class "my-2"}]

    nil))

;; Mobile menu drawer
(defn mobile-menu [nav-items]
  [:div {:id "mobile-menu"
         :class "fixed inset-0 bg-surface z-[100] hidden"
         :style {:font-family font-family
                 :display "none"}}
   ;; Header with close button
   [:div {:class "flex items-center justify-between h-[65px] px-4 border-b border-outline"}
    [:a {:href "https://www.health-samurai.io/"
         :target "_blank"
         :rel "noopener noreferrer"
         :class "inline-block bg-brand rounded-b"}
     [:img {:src "https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/5a2ff50e669ec50001a59b5d_health-samurai.webp"
            :alt "Health Samurai Logo"
            :width "48"
            :height "48"
            :class "block"
            :style {:width "48px" :height "48px"}}]]
    [:button {:id "mobile-menu-close"
              :type "button"
              :class "w-10 h-10 flex items-center justify-center text-2xl cursor-pointer"
              :aria-label "Close menu"}
     "×"]]
   ;; Nav links (vertical list)
   [:nav {:class "flex-1 overflow-y-auto py-4 px-4"}
    (for [[idx item] (map-indexed vector nav-items)]
      ^{:key idx}
      (render-mobile-nav-item item))]
   ;; CTA at bottom
   [:div {:class "p-4 border-t border-outline"}
    [:a {:href "https://aidbox.app/ui/portal#/signup"
         :target "_blank"
         :rel "noopener noreferrer"
         :class "block w-full text-center py-3 text-white no-underline rounded bg-brand"
         :style {:font-family font-family}}
     "Sign up for free"]]])

(defn main-navbar
  "Main site navigation bar - generated from YAML config"
  [context]
  (let [nav-items (blog/get-nav-items context)]
    [:nav {:class "w-full bg-surface h-[65px] px-4 md:px-8 border-b border-outline"
           :style {:font-family font-family}
           :aria-label "Main navigation"}
     [:div {:class "flex items-center justify-between h-full mx-auto max-w-[1200px]"}

      ;; Left section: Logo + nav items
      [:div {:class "flex items-center"}
       ;; Logo (64x64, red background, square top, slightly rounded bottom)
       [:a {:href "https://www.health-samurai.io/"
            :target "_blank"
            :rel "noopener noreferrer"
            :class "inline-block bg-brand rounded-b mr-4"}
        [:img {:src "https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/5a2ff50e669ec50001a59b5d_health-samurai.webp"
               :alt "Health Samurai Logo"
               :loading "eager"
               :width "64"
               :height "64"
               :class "block w-16 h-16"}]]

       ;; Nav items (desktop) - generated from YAML
       [:div {:class "hidden lg:flex items-center"}
        (for [[idx item] (map-indexed vector nav-items)]
          ^{:key idx}
          (render-nav-item item))]]

      ;; Right section: CTA button + Hamburger
      [:div {:class "flex items-center gap-4"}
       [:a {:href "https://aidbox.app/ui/portal#/signup"
            :target "_blank"
            :rel "noopener noreferrer"
            :class "hidden sm:inline-flex items-center justify-center text-white no-underline transition-opacity hover:opacity-90 bg-brand px-6 py-3.5 rounded text-sm font-semibold leading-none"
            :style {:font-family font-family}}
        "Sign up for free"]
       ;; Hamburger button (mobile/tablet only, on the right)
       (hamburger-button)]]]))

;; JavaScript for dropdown toggle with arrow rotation and mobile menu
(defn dropdown-script [context]
  (let [nav-items (blog/get-nav-items context)
        dropdown-ids (get-dropdown-ids nav-items)
        ids-js (str "[" (str/join ", " (map #(str "'" % "'") dropdown-ids)) "]")]
    (str "(function() {
    if (window._dropdownInitialized) return;
    window._dropdownInitialized = true;
    var dropdowns = " ids-js ";

    function closeAllDropdowns() {
      dropdowns.forEach(function(id) {
        var el = document.getElementById(id);
        if (el) el.classList.add('hidden');
        var arrow = document.querySelector('.dropdown-arrow[data-for=\"' + id + '\"]');
        if (arrow) arrow.style.transform = 'rotate(0deg)';
      });
    }

    function toggleMobileMenu() {
      var menu = document.getElementById('mobile-menu');
      if (menu) {
        var isHidden = menu.style.display === 'none' || menu.style.display === '';
        menu.style.display = isHidden ? 'flex' : 'none';
        menu.style.flexDirection = 'column';
        document.body.style.overflow = isHidden ? 'hidden' : '';
      }
    }

    // Use event delegation - works after HTMX swaps
    document.addEventListener('click', function(e) {
      // Hamburger button click
      var hamburger = e.target.closest('#hamburger-btn');
      if (hamburger) {
        e.preventDefault();
        e.stopPropagation();
        toggleMobileMenu();
        return;
      }

      // Mobile menu close button
      var closeBtn = e.target.closest('#mobile-menu-close');
      if (closeBtn) {
        e.preventDefault();
        toggleMobileMenu();
        return;
      }

      // Dropdown toggle
      var btn = e.target.closest('.dropdown-toggle');
      if (btn) {
        e.stopPropagation();
        var targetId = btn.getAttribute('data-dropdown');
        var target = document.getElementById(targetId);
        var arrow = document.querySelector('.dropdown-arrow[data-for=\"' + targetId + '\"]');
        var isOpen = target && !target.classList.contains('hidden');

        closeAllDropdowns();

        if (!isOpen && target) {
          target.classList.remove('hidden');
          if (arrow) arrow.style.transform = 'rotate(180deg)';
        }
      } else if (!e.target.closest('[id^=\"dropdown-\"]') && !e.target.closest('#mobile-menu')) {
        closeAllDropdowns();
      }
    });
  })();")))

;; Gotham Pro font-face CSS (extracted from health-samurai.io)
;; Note: fonts load from CDN which may have CORS restrictions
(def gotham-pro-font-css
  "@font-face{font-family:'Gotham Pro';src:url(https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/5a2ff11b47c38400019e7bb2_GothaProReg.otf)format('opentype');font-weight:400;font-style:normal;font-display:swap}
@font-face{font-family:'Gotham Pro';src:url(https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/5a2ff11a5700e10001e0d55e_GothaProMed.otf)format('opentype');font-weight:500;font-style:normal;font-display:swap}
@font-face{font-family:'Gotham Pro';src:url(https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/5a2ff11ac197b800013c065d_GothaProBol.otf)format('opentype');font-weight:700;font-style:normal;font-display:swap}")

(defn blog-nav
  "Blog header - main navbar + mobile menu"
  [context]
  (let [nav-items (blog/get-nav-items context)]
    [:header {:class "sticky top-0 z-50"}
     [:style (hiccup/raw gotham-pro-font-css)]
     (main-navbar context)
     ;; Mobile menu (outside nav for proper z-index stacking)
     (mobile-menu nav-items)
     [:script (hiccup/raw (dropdown-script context))]]))
