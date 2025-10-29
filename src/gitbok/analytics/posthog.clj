(ns gitbok.analytics.posthog
  (:require
   [clojure.tools.logging :as log])
  (:import
   [com.posthog.java PostHog]
   [com.posthog.java PostHog$Builder]))

(defn create-client
  "Create PostHog client with API key and host.
   Returns nil if API key is not provided."
  [api-key host]
  (try
    (when api-key
      (let [^PostHog$Builder builder (PostHog$Builder. api-key)
            builder-with-host (if host
                                (.host builder host)
                                builder)
            client (.build builder-with-host)]
        (log/info "PostHog client initialized" {:host (or host "default")})
        client))
    (catch Exception e
      (log/error e "Failed to create PostHog client")
      nil)))

(defn get-client
  "Get the PostHog client instance from runtime state."
  [context]
  (get-in context [:runtime :posthog-client]))

(defn capture-event!
  "Capture an event in PostHog.

   Parameters:
   - context: Application context
   - distinct-id: User identifier (session ID)
   - event-name: Name of the event (should start with 'docs_')
   - properties: Map of event properties"
  [context distinct-id event-name properties]
  (try
    (when-let [client (get-client context)]
      (let [props (java.util.HashMap. properties)]
        (.capture client distinct-id event-name props)
        (log/debug "PostHog event captured" {:event event-name
                                              :distinct-id distinct-id
                                              :properties properties})))
    (catch Exception e
      (log/error e "Failed to capture PostHog event" {:event event-name
                                                       :distinct-id distinct-id}))))

(defn shutdown!
  "Gracefully shutdown PostHog client, flushing pending events."
  [context]
  (try
    (when-let [client (get-client context)]
      (.shutdown client)
      (log/info "PostHog client shutdown"))
    (catch Exception e
      (log/error e "Failed to shutdown PostHog client"))))
