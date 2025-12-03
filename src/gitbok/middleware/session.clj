(ns gitbok.middleware.session
  (:require
   [clojure.string :as str]
   [clojure.data.json :as json]
   [gitbok.state :as state]))

(defn- url-decode
  "URL decode a string."
  [s]
  (try
    (java.net.URLDecoder/decode s "UTF-8")
    (catch Exception _ nil)))

(defn- parse-posthog-cookie
  "Parse PostHog cookie value and extract distinct_id.
   Cookie value is URL-encoded JSON with distinct_id field."
  [cookie-value]
  (try
    (when-let [decoded (url-decode cookie-value)]
      (let [parsed (json/read-str decoded)]
        (get parsed "distinct_id")))
    (catch Exception _ nil)))

(defn- get-posthog-cookie-name
  "Build PostHog cookie name from API key."
  [api-key]
  (str "ph_" api-key "_posthog"))

(defn- get-posthog-distinct-id
  "Extract distinct_id from PostHog cookie if present."
  [request api-key]
  (when (and api-key (get-in request [:headers "cookie"]))
    (let [cookie-header (get-in request [:headers "cookie"])
          cookie-name (get-posthog-cookie-name api-key)
          cookies (str/split cookie-header #";\s*")
          posthog-cookie (first (filter #(str/starts-with? % (str cookie-name "=")) cookies))]
      (when posthog-cookie
        (let [cookie-value (subs posthog-cookie (inc (str/index-of posthog-cookie "=")))]
          (parse-posthog-cookie cookie-value))))))

(defn- generate-anonymous-id
  "Generate a random anonymous ID for a single request."
  []
  (str "anon_" (java.util.UUID/randomUUID)))

(defn wrap-session
  "Middleware to provide session ID for analytics.
   Uses PostHog cookie distinct_id if available (user accepted cookies),
   otherwise generates anonymous ID per request (no cookie set)."
  [handler context]
  (let [api-key (state/get-config context :posthog-api-key)]
    (fn [request]
      (let [distinct-id (or (get-posthog-distinct-id request api-key)
                            (generate-anonymous-id))
            request-with-session (assoc request :session-id distinct-id)]
        (handler request-with-session)))))
