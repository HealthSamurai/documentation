(ns gitbok.analytics.posthog
  (:require
   [clojure.tools.logging :as log]
   [gitbok.state :as state])
  (:import
   [com.posthog.java PostHog]
   [com.posthog.java PostHog$Builder]))

(defonce ^:private posthog-client (atom nil))

(defn init-client!
  "Initialize PostHog client with API key and host from context."
  [context]
  (try
    (let [api-key (state/get-config context :posthog-api-key)
          host (state/get-config context :posthog-host)]
      (when api-key
        (let [^PostHog$Builder builder (PostHog$Builder. api-key)
              builder-with-host (if host
                                  (.host builder host)
                                  builder)]
          (reset! posthog-client (.build builder-with-host))
          (log/info "PostHog client initialized" {:host (or host "default")}))
        true))
    (catch Exception e
      (log/error e "Failed to initialize PostHog client")
      false)))

(defn get-client
  "Get the PostHog client instance, initialize if needed."
  [context]
  (when-not @posthog-client
    (init-client! context))
  @posthog-client)

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
  []
  (try
    (when-let [client @posthog-client]
      (.shutdown client)
      (reset! posthog-client nil)
      (log/info "PostHog client shutdown"))
    (catch Exception e
      (log/error e "Failed to shutdown PostHog client"))))
