(ns gitbok.ui.blog-header
  (:require [hiccup2.core :as hiccup]
            [gitbok.blog.core :as blog]
            [clojure.string :as str]))

;; Font family from production
(def font-family "'Gotham Pro', Arial, sans-serif")

;; Colors extracted from health-samurai.io
(def primary-red "#EA4A35")
(def text-dark "#222222")
(def text-dark-alt "#333333")
(def text-muted "rgba(51, 51, 51, 0.6)")
(def border-color "rgba(53, 59, 80, 0.1)")
(def dropdown-bg "#f3f8fb")

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
       :class "group relative flex flex-col p-8 no-underline transition-shadow"
       :style {:border (str "1px solid " border-color)
               :border-radius "4px"
               :font-family font-family
               :min-height "180px"
               :background-color dropdown-bg
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
   [:div {:class "mb-1"
          :style {:font-size "18px"
                  :font-weight "700"
                  :color text-dark-alt
                  :line-height "24px"}}
    title]
   ;; Description
   [:div {:style {:font-size "14px"
                  :color text-muted
                  :flex-grow "1"}}
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
     [:div {:class "uppercase mb-3 tracking-wide"
            :style {:font-size "12px"
                    :color text-muted
                    :font-family font-family}}
      title])
   (for [item items]
     ^{:key (:title item)}
     [:a {:href (:href item)
          :target (when-not (:internal item) "_blank")
          :class "block py-2 no-underline hover:underline"
          :style {:font-size "14px"
                  :color text-dark
                  :font-family font-family}}
      (:title item)])])

;; Extended links column (title + description)
(defn dropdown-extended-column [{:keys [title items]}]
  [:div
   (when title
     [:div {:class "uppercase mb-3 tracking-wide"
            :style {:font-size "12px"
                    :color text-muted
                    :font-family font-family}}
      title])
   (for [item items]
     ^{:key (:title item)}
     [:a {:href (:href item)
          :target "_blank"
          :class "group block mb-3 no-underline"}
      [:div {:class "group-hover:underline"
             :style {:font-size "14px"
                     :color text-dark-alt
                     :font-family font-family
                     :line-height "21px"}}
       (:title item)]
      [:div {:style {:font-size "14px"
                     :color text-muted
                     :font-family font-family}}
       (:desc item)]])])

;; Render a single column based on type
(defn render-column [col]
  (if (= (:type col) "extended")
    (dropdown-extended-column col)
    (dropdown-simple-column col)))

;; Universal dropdown component
(defn render-dropdown [dropdown-id {:keys [columns banner banners]}]
  [:div {:id dropdown-id
         :class "fixed left-0 right-0 top-[65px] shadow-lg hidden z-50"
         :style {:background-color dropdown-bg
                 :box-shadow "0 0 2px 0 rgba(53, 59, 80, 0.1)"}}
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
       [:button {:class "flex items-center cursor-pointer no-underline dropdown-toggle"
                 :data-dropdown dropdown-id
                 :style {:padding "21px 24px"
                         :color text-dark
                         :font-size "14px"
                         :font-weight "400"
                         :font-family font-family}}
        (:label item) (dropdown-arrow dropdown-id)]
       (render-dropdown dropdown-id item)]

      "link"
      [:a {:href (:href item)
           :target "_blank"
           :class "flex items-center no-underline hover:text-[#EA4A35] transition-colors"
           :style {:padding "21px 20px"
                   :color text-dark-alt
                   :font-size "14px"
                   :font-weight "400"
                   :font-family font-family}}
       (:label item)]

      "divider"
      [:div {:style {:width "1px"
                     :height "32px"
                     :border-left "1px solid #EB4A35"
                     :margin "16px 8px"}}]

      nil)))

;; Get all dropdown IDs for JavaScript
(defn get-dropdown-ids [nav-items]
  (->> nav-items
       (filter #(= (:type %) "dropdown"))
       (map #(str "dropdown-" (:id %)))
       vec))

(defn main-navbar
  "Main site navigation bar - generated from YAML config"
  [context]
  (let [nav-items (blog/get-nav-items context)]
    [:nav {:class "w-full bg-white sticky top-0 z-50"
           :style {:height "65px"
                   :padding "0 32px"
                   :border-bottom "1px solid rgba(51, 51, 51, 0.05)"
                   :font-family font-family}
           :aria-label "Main navigation"}
     [:div {:class "flex items-center justify-between h-full mx-auto"
            :style {:max-width "1200px"}}

      ;; Left section: Logo and nav items
      [:div {:class "flex items-center"}
       ;; Logo (64x64, red background, square top, slightly rounded bottom)
       [:a {:href "https://www.health-samurai.io/"
            :target "_blank"
            :rel "noopener noreferrer"
            :class "inline-block"
            :style {:background-color primary-red
                    :border-radius "0px 0px 4px 4px"
                    :margin-right "16px"}}
        [:img {:src "https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/5a2ff50e669ec50001a59b5d_health-samurai.webp"
               :alt "Health Samurai Logo"
               :loading "eager"
               :width "64"
               :height "64"
               :class "block"
               :style {:width "64px"
                       :height "64px"}}]]

       ;; Nav items (desktop) - generated from YAML
       [:div {:class "hidden lg:flex items-center"}
        (for [[idx item] (map-indexed vector nav-items)]
          ^{:key idx}
          (render-nav-item item))]]

      ;; Right section: CTA button
      [:div {:class "flex items-center"}
       [:a {:href "https://aidbox.app/ui/portal#/signup"
            :target "_blank"
            :rel "noopener noreferrer"
            :class "hidden sm:inline-flex items-center justify-center text-white no-underline transition-opacity hover:opacity-90"
            :style {:background-color primary-red
                    :padding "14px 24px"
                    :border-radius "4px"
                    :font-size "14px"
                    :font-weight "600"
                    :line-height "14px"
                    :font-family font-family}}
        "Sign up for free"]]]]))

;; JavaScript for dropdown toggle with arrow rotation
(defn dropdown-script [context]
  (let [nav-items (blog/get-nav-items context)
        dropdown-ids (get-dropdown-ids nav-items)
        ids-js (str "[" (str/join ", " (map #(str "'" % "'") dropdown-ids)) "]")]
    (str "document.addEventListener('DOMContentLoaded', function() {
    var dropdowns = " ids-js ";

    function closeAllDropdowns() {
      dropdowns.forEach(function(id) {
        document.getElementById(id).classList.add('hidden');
        var arrow = document.querySelector('.dropdown-arrow[data-for=\"' + id + '\"]');
        if (arrow) arrow.style.transform = 'rotate(0deg)';
      });
    }

    document.querySelectorAll('.dropdown-toggle').forEach(function(btn) {
      btn.addEventListener('click', function(e) {
        e.stopPropagation();
        var targetId = this.getAttribute('data-dropdown');
        var target = document.getElementById(targetId);
        var arrow = document.querySelector('.dropdown-arrow[data-for=\"' + targetId + '\"]');
        var isOpen = !target.classList.contains('hidden');

        // Close all dropdowns first
        closeAllDropdowns();

        // Toggle this dropdown
        if (!isOpen) {
          target.classList.remove('hidden');
          if (arrow) arrow.style.transform = 'rotate(180deg)';
        }
      });
    });

    document.addEventListener('click', function(e) {
      if (!e.target.closest('.dropdown-toggle') && !e.target.closest('[id^=\"dropdown-\"]')) {
        closeAllDropdowns();
      }
    });
  });")))

;; Gotham Pro font-face CSS (extracted from health-samurai.io)
;; Note: fonts load from CDN which may have CORS restrictions
(def gotham-pro-font-css
  "@font-face{font-family:'Gotham Pro';src:url(https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/5a2ff11b47c38400019e7bb2_GothaProReg.otf)format('opentype');font-weight:400;font-style:normal;font-display:swap}
@font-face{font-family:'Gotham Pro';src:url(https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/5a2ff11a5700e10001e0d55e_GothaProMed.otf)format('opentype');font-weight:500;font-style:normal;font-display:swap}
@font-face{font-family:'Gotham Pro';src:url(https://cdn.prod.website-files.com/57441aa5da71fdf07a0a2e19/5a2ff11ac197b800013c065d_GothaProBol.otf)format('opentype');font-weight:700;font-style:normal;font-display:swap}")

(defn blog-nav
  "Blog header - main navbar only"
  [context]
  [:header
   [:style (hiccup/raw gotham-pro-font-css)]
   (main-navbar context)
   [:script (hiccup/raw (dropdown-script context))]])
