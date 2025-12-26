(ns gitbok.webhooks.remark42
  (:require
   [cheshire.core :as json]
   [clojure.string :as str]
   [org.httpkit.client :as http-client]
   [clojure.tools.logging :as log]))

(defn format-comment-message
  "Format remark42 comment data into Zulip message"
  [comment]
  (let [{:keys [id text user locator time title]} comment
        author-name (get user :name "Anonymous")
        user-id (get user :id "")
        url (get locator :url "")
        page-title (or title "")
        is-github (str/starts-with? user-id "github_")
        is-google (str/starts-with? user-id "google_")
        author-link (cond
                      is-github (str "https://github.com/" author-name)
                      :else author-name)
        provider (cond
                   is-github "GitHub"
                   is-google "Google"
                   :else "Unknown")
        comment-link (str url "#remark42__comment-" id)]
    (str "**New blog comment**\n\n"
         "**Page**: " page-title "\n"
         "**Author**: " (if is-github
                          (str "[" author-name "](https://github.com/" author-name ")")
                          (str author-name " (" provider ")")) "\n"
         "**Comment link**: " comment-link "\n"
         "**Time**: " time "\n\n"
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
          comment (json/parse-string body true)]
      (log/info "Received remark42 webhook" {:comment comment})
      (let [message (format-comment-message comment)
            result (send-to-zulip message zulip-config)]
        {:status 200
         :headers {"Content-Type" "application/json"}
         :body (json/generate-string result)}))
    (catch Exception e
      (log/error e "Failed to process remark42 webhook")
      {:status 500
       :headers {"Content-Type" "application/json"}
       :body (json/generate-string {:status :error :message (.getMessage e)})})))
