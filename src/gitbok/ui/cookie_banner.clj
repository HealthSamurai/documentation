(ns gitbok.ui.cookie-banner
  "Cookie consent banner component for GDPR compliance")

(defn cookie-banner
  "Renders a fixed cookie consent banner at the bottom of the page.
   Banner visibility is controlled by JavaScript based on PostHog consent status."
  []
  [:div {:id "cookie-consent-banner"
         :class "fixed bottom-0 left-0 right-0 bg-surface border-t border-outline shadow-lg z-50 hidden"
         :style "display: none;"}
   [:div {:class "max-w-screen-xl mx-auto px-5 py-4 md:py-6"}
    [:div {:class "flex flex-col md:flex-row md:items-center md:justify-between gap-4"}
     ;; Text content with cookie policy link
     [:div {:class "flex-1 text-sm text-on-surface"}
      [:p
       "We use cookies for analytics and marketing to improve your experience and communicate with you. You can accept or reject cookies below. View our "
       [:a {:href "https://www.health-samurai.io/legal/cookie-policy"
            :target "_blank"
            :rel "noopener noreferrer"
            :class "text-brand hover:underline"}
        "Cookie Policy"]
       " for more information."]]
     ;; Action buttons
     [:div {:class "flex gap-3 justify-end"}
      [:button {:onclick "handleRejectCookies()"
                :class "px-4 py-2 rounded border border-outline text-on-surface hover:bg-surface-hover transition-colors cursor-pointer"}
       "Reject"]
      [:button {:onclick "handleAcceptCookies()"
                :class "px-4 py-2 rounded bg-brand text-white hover:bg-brand-hover transition-colors cursor-pointer"}
       "Accept All"]]]]])
