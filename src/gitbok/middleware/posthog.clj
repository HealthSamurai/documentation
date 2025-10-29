(ns gitbok.middleware.posthog
  (:require
   [clojure.string :as str]
   [clojure.tools.logging :as log]
   [gitbok.analytics.posthog :as posthog]
   [gitbok.products :as products]))

(def ^:private excluded-paths-regex
  "Regex pattern for paths that should not be tracked in PostHog."
  #"^/(metrics|healthcheck|version|debug|static/|\.gitbook/assets/|public/og-preview/|partial/|api/|service-worker\.js|favicon\.ico)")

(defn- should-track-path?
  "Check if the path should be tracked in PostHog.
   Filters out system endpoints, static assets, and partials."
  [uri]
  (not (re-find excluded-paths-regex uri)))

(defn wrap-posthog-tracking
  "Middleware to track page views in PostHog.
   Sends docs_page_view event for each request."
  [handler context]
  (fn [request]
    (let [response (handler request)]
      (try
        (when-let [session-id (:session-id request)]
          (let [uri (:uri request)]
            (when (should-track-path? uri)
              (let [current-product (products/get-current-product
                                     (assoc context :request request))
                    product-id (or (:id current-product) "unknown")
                    user-agent (get-in request [:headers "user-agent"])
                    base-url (get-in context [:state :config :base-url])
                    properties {"url" (str (:scheme request) "://"
                                           (get-in request [:headers "host"])
                                           uri)
                                "base_url" base-url
                                "path" uri
                                "user_agent" user-agent
                                "product" product-id
                                "method" (name (:request-method request))
                                "status" (:status response)}]
                ;; Capture event asynchronously (doesn't block response)
                (future
                  (posthog/capture-event! context
                                          session-id
                                          "docs_page_view"
                                          properties))))))
        (catch Exception e
          (log/error e "Failed to track page view in PostHog")))
      response)))
