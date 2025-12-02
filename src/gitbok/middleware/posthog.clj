(ns gitbok.middleware.posthog
  (:require
   [clojure.string :as str]
   [clojure.tools.logging :as log]
   [gitbok.analytics.posthog :as posthog]
   [gitbok.products :as products]
   [gitbok.state :as state]))

(def ^:private special-path-filenames
  "Set of special filenames that should be tracked (SEO, AI/LLM, RSS, security)."
  #{"sitemap.xml" "llms.txt" "llms-full.txt" "robots.txt"
    "openapi.json" "openapi.yaml" "ai-plugin.json"
    "feed.xml" "rss.xml" "atom.xml" "security.txt"})

(defn- special-path?
  "Check if the URI is a special path that should be tracked.
   Tracks SEO files, AI/LLM files, RSS feeds, and security files."
  [uri]
  (when uri
    (let [filename (last (str/split uri #"/"))]
      (or (contains? special-path-filenames filename)
          (str/ends-with? uri "/.well-known/ai-plugin.json")
          (str/ends-with? uri "/.well-known/security.txt")))))

(defn wrap-posthog-tracking
  "Middleware to track special paths in PostHog.
   Sends docs_special_paths event for SEO, AI/LLM, RSS, and security files."
  [handler context]
  (fn [request]
    (let [response (handler request)
          uri (:uri request)
          session-id (:session-id request)]
      (try
        (when (and session-id (special-path? uri))
          (let [current-product (products/get-current-product
                                  (assoc context :request request))
                product-id (or (:id current-product) "unknown")
                user-agent (get-in request [:headers "user-agent"])
                base-url (state/get-config context :base-url)
                scheme (if-let [s (:scheme request)] (name s) "https")
                properties {"url" (str scheme "://"
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
            (posthog/capture-event! context
                                    session-id
                                    "docs_special_paths"
                                    properties)))
        (catch Exception e
          (log/error e "Failed to track special path in PostHog")))
      response)))
