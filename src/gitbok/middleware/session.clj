(ns gitbok.middleware.session
  (:require
   [clojure.string :as str]))

(defn generate-session-id
  "Generate a random session ID."
  []
  (str (java.util.UUID/randomUUID)))

(defn get-session-id-from-cookie
  "Extract session ID from cookies."
  [request]
  (when-let [cookie-header (get-in request [:headers "cookie"])]
    (let [cookies (str/split cookie-header #";\s*")
          session-cookie (first (filter #(str/starts-with? % "session_id=") cookies))]
      (when session-cookie
        (second (str/split session-cookie #"="))))))

(defn wrap-session
  "Middleware to ensure every request has a session ID.
   Adds session ID to request and sets cookie if needed."
  [handler]
  (fn [request]
    (let [existing-session-id (get-session-id-from-cookie request)
          session-id (or existing-session-id (generate-session-id))
          request-with-session (assoc request :session-id session-id)
          response (handler request-with-session)]
      (if existing-session-id
        response
        ;; Set new session cookie with 30 day expiration
        (assoc-in response
                  [:headers "Set-Cookie"]
                  (str "session_id=" session-id
                       "; Max-Age=" (* 30 24 60 60)
                       "; Path=/"
                       "; SameSite=Lax"
                       "; HttpOnly"))))))
