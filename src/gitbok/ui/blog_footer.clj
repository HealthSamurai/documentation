(ns gitbok.ui.blog-footer
  (:require [gitbok.blog.core :as blog]))

(def link-class "font-['Gotham_Pro',Arial,sans-serif] block mb-5 text-[15px] font-normal leading-[21px] text-[#5e5e5e] dark:text-on-surface-muted")
(def contact-class "font-['Gotham_Pro',Arial,sans-serif] text-[15px] font-normal leading-[30px] text-[#333333] dark:text-on-surface")

(defn- certification-item [{:keys [image alt href]}]
  (let [img-el [:img {:src image
                      :alt (or alt "")
                      :loading "lazy"
                      :class "h-[82px] w-auto"}]]
    (if href
      [:a {:href href
           :target "_blank"
           :rel "noopener noreferrer"}
       img-el]
      img-el)))

(defn- certifications-row [certifications]
  [:div {:class "flex items-center gap-8 py-5"}
   (for [[idx cert] (map-indexed vector certifications)]
     ^{:key idx}
     (certification-item cert))])

(defn- footer-link [{:keys [title href active type]}]
  (if (= type "button")
    [:button {:onclick "showCookieSettings()"
              :class (str link-class " hover:underline cursor-pointer bg-transparent border-0 p-0 text-left")}
     title]
    [:a {:href href
         :target "_blank"
         :rel "noopener noreferrer"
         :class (str link-class " no-underline hover:underline"
                     (if active " font-bold! border-b-2 border-[#ea4a35] inline-block pb-0.5" ""))}
     title]))

(defn- footer-column [{:keys [title items]}]
  [:div
   (when title
     [:a {:href "https://www.health-samurai.io/"
          :target "_blank"
          :rel "noopener noreferrer"
          :class (str link-class " no-underline inline-block")}
      title])
   (for [[idx item] (map-indexed vector items)]
     ^{:key idx}
     (footer-link item))])

(defn- contact-column [{:keys [address phone email]}]
  [:div {:class "text-center"}
   [:div {:class contact-class} address]
   [:div {:class contact-class} phone]
   [:div {:class contact-class} email]])

(defn- footer-grid [columns contact]
  [:div {:class "grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6 lg:gap-8"}
   (for [[idx col] (map-indexed vector columns)]
     ^{:key idx}
     [:div (footer-column col)])
   [:div (contact-column contact)]])

(defn- footer-logo [logo-url]
  [:div {:class "flex justify-center py-4"}
   [:img {:src logo-url
          :alt "Health Samurai Company Logo"
          :class "h-[32px] w-auto"}]])

(defn blog-footer
  "Blog footer component - matches health-samurai.io footer"
  [context]
  (let [footer-config (blog/get-footer-config context)
        {:keys [certifications columns contact logo]} footer-config]
    [:footer {:class "mt-auto subpixel-antialiased"}
     ;; Pre-footer section (light blue background)
     [:div {:class "bg-[#f4f8fb] dark:bg-[#1e2330] px-4 md:px-8 pt-5 pb-5"}
      [:div {:class "max-w-[1200px] mx-auto"}
       (certifications-row certifications)
       (footer-grid columns contact)]]
     ;; Footer section (logo)
     [:div {:class "bg-surface"}
      (footer-logo logo)]]))
