(ns gitbok.middleware.posthog
  (:require
   [clojure.string :as str]
   [clojure.tools.logging :as log]
   [gitbok.analytics.posthog :as posthog]
   [gitbok.products :as products]
   [gitbok.state :as state]))

(defn- build-excluded-paths-regex
  "Build regex pattern for paths that should not be tracked in PostHog.
   Takes into account the DOCS_PREFIX."
  [prefix]
  (let [prefix-path (if (str/blank? prefix) "" prefix)
        excluded-paths ["static/"
                        "\\.gitbook/assets/"
                        "public/og-preview/"
                        "partial/"
                        "api/"]
        file-extensions ["js" "css" "svg" "jpg" "png" "ico" "woff" "woff2" "ttf" "eot"]
        system-endpoints ["metrics" "healthcheck" "version" "debug" "service-worker\\.js"]

        prefix-pattern (when-not (str/blank? prefix-path)
                         (str "^" prefix-path "/(" (str/join "|" excluded-paths) "|.*\\.(" (str/join "|" file-extensions) ")$)"))
        system-pattern (str "^/(" (str/join "|" system-endpoints) ")")

        full-pattern (if prefix-pattern
                       (str prefix-pattern "|" system-pattern)
                       system-pattern)]
    (re-pattern full-pattern)))

(defn- should-track-path?
  "Check if the path should be tracked in PostHog.
   Filters out system endpoints, static assets, and partials."
  [uri excluded-paths-regex]
  (not (re-find excluded-paths-regex uri)))

(defn wrap-posthog-tracking
  "Middleware to track page views in PostHog.
   Sends docs_page_view event for each request."
  [handler context]
  (let [prefix (state/get-config context :prefix "/docs")
        excluded-paths-regex (build-excluded-paths-regex prefix)]
    (fn [request]
      (let [response (handler request)
            uri (:uri request)
            session-id (:session-id request)]
        (try
          (when-let [session-id session-id]
            (let [uri uri]
              (when (should-track-path? uri excluded-paths-regex)
                (let [current-product (products/get-current-product
                                     (assoc context :request request))
                    product-id (or (:id current-product) "unknown")
                    user-agent (get-in request [:headers "user-agent"])
                    base-url (state/get-config context :base-url)
                    properties {"url" (str (or (:scheme request) "http") "://"
                                           (or (get-in request [:headers "host"]) "")
                                           (or uri ""))
                                "base_url" (or base-url "")
                                "path" (or uri "")
                                "user_agent" (or user-agent "")
                                "product" product-id
                                "method" (if-let [method (:request-method request)]
                                           (name method)
                                           "unknown")
                                "status" (or (:status response) 0)}]
                ;; SDK uses internal queue - non-blocking
                (posthog/capture-event! context
                                        session-id
                                        "docs_page_view"
                                        properties)))))
          (catch Exception e
            (log/error e "Failed to track page view in PostHog")))
        response))))
