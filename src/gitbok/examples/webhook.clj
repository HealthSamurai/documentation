(ns gitbok.examples.webhook
  (:require [clojure.string :as str]
            [clojure.data.json :as json]
            [klog.core :as log]
            [gitbok.examples.indexer :as indexer]
            [gitbok.products :as products]))

;; GitHub webhook IP ranges from https://api.github.com/meta
;; These are the IP ranges GitHub uses for webhooks and actions
;; Last updated: 2024-01-15
(def github-webhook-ips
  #{"140.82.112.0/20"
    "143.55.64.0/20"
    "185.199.108.0/22"
    "192.30.252.0/22"
    "20.201.28.148/32"
    "20.201.28.151/32"
    "20.201.28.152/32"
    "20.205.243.160/32"
    "20.205.243.164/32"
    "20.205.243.165/32"
    "20.205.243.166/32"
    "20.205.243.168/32"
    "20.87.225.212/32"
    "20.87.225.213/32"
    "20.87.225.214/32"
    "20.87.225.216/32"
    "20.87.225.217/32"
    "20.87.225.218/32"
    "20.87.225.219/32"
    "20.87.225.220/32"})

(defn ip-to-long
  "Convert IP address string to long"
  [ip]
  (let [parts (str/split ip #"\.")]
    (reduce (fn [acc part]
              (+ (* acc 256) (Long/parseLong part)))
            0
            parts)))

(defn cidr-to-range
  "Convert CIDR notation to IP range [start end]"
  [cidr]
  (let [[ip mask-str] (str/split cidr #"/")
        mask (Long/parseLong mask-str)
        ip-long (ip-to-long ip)
        mask-bits (- 32 mask)
        start (bit-and ip-long (bit-not (dec (bit-shift-left 1 mask-bits))))
        end (bit-or start (dec (bit-shift-left 1 mask-bits)))]
    [start end]))

(defn ip-in-cidr?
  "Check if IP is within CIDR range"
  [ip cidr]
  (try
    (let [ip-long (ip-to-long ip)
          [start end] (cidr-to-range cidr)]
      (and (>= ip-long start) (<= ip-long end)))
    (catch Exception e
      (log/error ::ip-check-failed {:ip ip :cidr cidr :error (.getMessage e)})
      false)))

(defn validate-github-ip
  "Validate if the client IP is from GitHub"
  [client-ip]
  (some #(ip-in-cidr? client-ip %) github-webhook-ips))

(defn get-client-ip
  "Extract client IP from request headers"
  [request]
  (let [forwarded-for (get-in request [:headers "x-forwarded-for"])
        real-ip (get-in request [:headers "x-real-ip"])
        remote-addr (:remote-addr request)]
    ;; If x-forwarded-for exists, take the first IP (original client)
    (if forwarded-for
      (first (str/split forwarded-for #",\s*"))
      (or real-ip remote-addr))))

(defn webhook-handler
  "Handle incoming webhook from GitHub Actions"
  [context request]
  (let [client-ip (get-client-ip request)
        dev-mode? (= "true" (System/getenv "DEV"))]

    (log/info ::webhook-received {:client-ip client-ip :dev-mode dev-mode?})

    ;; Check IP whitelist (skip in dev mode)
    (if (and (not dev-mode?)
             (not (validate-github-ip client-ip)))
      (do
        (log/warn ::invalid-webhook-ip {:client-ip client-ip})
        {:status 403
         :headers {"content-type" "text/plain"}
         :body "Forbidden: Invalid source IP"})

      ;; Process webhook data
      (try
        (let [body (slurp (:body request))
              examples-data (json/read-str body :key-fn keyword)]

          ;; Validate structure
          (when-not (and (:examples examples-data)
                         (:timestamp examples-data))
            (throw (ex-info "Invalid webhook data structure"
                            {:data (keys examples-data)})))

          ;; Store in context using the indexer
          ;; Set product to aidbox since examples are for aidbox
          (let [ctx-with-product (products/set-current-product-id context "aidbox")]
            (indexer/update-examples! ctx-with-product examples-data))

          (log/info ::examples-updated
                    {:count (count (:examples examples-data))
                     :features-count (count (:features_list examples-data))
                     :languages-count (count (:languages_list examples-data))
                     :timestamp (:timestamp examples-data)})

          {:status 200
           :headers {"content-type" "text/plain"}
           :body "OK"})

        (catch Exception e
          (log/error ::webhook-processing-failed
                     {:error (.getMessage e)
                      :type (type e)})
          {:status 500
           :headers {"content-type" "text/plain"}
           :body "Internal error"})))))
