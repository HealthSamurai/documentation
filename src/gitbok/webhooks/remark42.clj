(ns gitbok.webhooks.remark42
  (:require
   [cheshire.core :as json]
   [org.httpkit.client :as http-client]
   [clojure.tools.logging :as log]))

(defn format-comment-message
  "Format remark42 comment data into Zulip message"
  [comment]
  (let [{:keys [text user locator timestamp]} comment
        author (get user "name" "Anonymous")
        url (get locator "url" "")
        time (or timestamp "")]
    (str "**New blog comment** (" time ")\n\n"
         "**Author**: " author "\n\n"
         "**Page**: " url "\n\n"
         "**Comment**:\n" text)))

(defn send-to-zulip
  "Send formatted message to Zulip via API"
  [message zulip-config]
  (let [{:keys [url bot-email bot-token stream-id]} zulip-config
        auth (str bot-email ":" bot-token)
        encoded-auth (.encodeToString (java.util.Base64/getEncoder)
                                       (.getBytes auth "UTF-8"))
        form-data (str "type=stream&to=" stream-id "&topic=blog+comments&content=" (java.net.URLEncoder/encode message "UTF-8"))]
    (try
      @(http-client/post (str url "/api/v1/messages")
                         {:headers {"Authorization" (str "Basic " encoded-auth)
                                    "Content-Type" "application/x-www-form-urlencoded"}
                          :body form-data})
      (log/info "Sent remark42 comment notification to Zulip")
      {:status :success}
      (catch Exception e
        (log/error e "Failed to send notification to Zulip")
        {:status :error :message (.getMessage e)}))))

(defn handle-webhook
  "Handle incoming webhook from remark42"
  [request zulip-config]
  (try
    (let [body (slurp (:body request))
          comment (json/parse-string body true)
          message (format-comment-message comment)
          result (send-to-zulip message zulip-config)]
      {:status 200
       :headers {"Content-Type" "application/json"}
       :body (json/generate-string result)})
    (catch Exception e
      (log/error e "Failed to process remark42 webhook")
      {:status 500
       :headers {"Content-Type" "application/json"}
       :body (json/generate-string {:status :error :message (.getMessage e)})})))
