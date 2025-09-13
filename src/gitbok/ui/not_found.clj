(ns gitbok.ui.not-found
  (:require
   [hiccup2.core]))

(defn not-found-view [_context _uri]
  [:div.min-h-screen.flex.items-center.justify-center
   [:script
    (hiccup2.core/raw "
document.addEventListener('DOMContentLoaded', () => {
  if (window.posthog && posthog.capture) {
    posthog.capture('Page Not Found', {
      path: window.location.pathname,
      full_url: window.location.href,
      referrer: document.referrer,
    });
  }
});")]
   [:div.max-w-2xl.w-full.px-4
    [:div
     [:h2.mt-4.text-3xl.font-semibold.text-tint-11.text-center "Page not found"]
     [:p.mt-2.text-base.text-tint-10.text-center "The page you are looking for doesn't exist."]]]])
