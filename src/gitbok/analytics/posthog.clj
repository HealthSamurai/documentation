(ns gitbok.analytics.posthog
  (:require
   [clojure.tools.logging :as log]
   [gitbok.state :as state])
  (:import
   [com.posthog.server PostHog PostHogConfig PostHogInterface PostHogCaptureOptions]))

(defn create-client
  "Create PostHog client with API key and host.
   Returns nil if API key is not provided."
  [api-key host]
  (try
    (when api-key
      (let [builder (PostHogConfig/builder api-key)
            builder-with-config (-> builder
                                    (.flushAt 10)
                                    (.flushIntervalSeconds 10))
            builder-with-host (if host
                                (.host builder-with-config host)
                                builder-with-config)
            ^PostHogConfig config (.build builder-with-host)
            ^PostHogInterface client (PostHog/with config)]
        (log/info "PostHog client created successfully" {:host (or host "default")
                                                           :flush-at 10
                                                           :flush-interval-sec 10})
        client))
    (catch Exception e
      (log/error e "Failed to create PostHog client")
      nil)))

(defn get-client
  "Get the PostHog client instance from runtime state."
  [context]
  (state/get-runtime context :posthog-client))

(defn capture-event!
  "Capture an event in PostHog.

   Parameters:
   - context: Application context
   - distinct-id: User identifier (session ID)
   - event-name: Name of the event (should start with 'docs_')
   - properties: Map of event properties"
  [context distinct-id event-name properties]
  (try
    (if-let [^PostHogInterface client (get-client context)]
      (let [builder (PostHogCaptureOptions/builder)]
        (doseq [[k v] properties]
          (.property builder (name k) v))
        (let [options (.build builder)]
          (.capture client distinct-id event-name options)))
      (log/warn "PostHog client not available, event not sent" {:event event-name}))
    (catch Exception e
      (log/error e "Failed to capture PostHog event" {:event event-name
                                                       :distinct-id distinct-id}))))

(defn shutdown!
  "Gracefully shutdown PostHog client, flushing pending events."
  [context]
  (try
    (when-let [^PostHogInterface client (get-client context)]
      (.flush client)
      (.close client)
      (log/info "PostHog client shutdown successfully"))
    (catch Exception e
      (log/error e "Failed to shutdown PostHog client"))))
