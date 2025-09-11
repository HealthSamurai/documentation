(ns gitbok.examples.updater
  (:require [clojure.data.json :as json]
            [clojure.tools.logging :as log]
            [gitbok.examples.indexer :as indexer]
            [gitbok.products :as products]
            [gitbok.state :as state]
            [org.httpkit.client :as http-client])
  (:import [java.util.zip ZipInputStream]
           [java.io ByteArrayInputStream]))

(defn get-github-token
  "Get GitHub PAT from state (stored at startup)"
  [context]
  (state/get-env context :github-token))

(defn fetch-artifacts-list
  "Fetch list of artifacts from GitHub API"
  [context]
  (let [token (get-github-token context)
        headers (if token
                  {"Authorization" (str "Bearer " token)
                   "Accept" "application/vnd.github.v3+json"}
                  {"Accept" "application/vnd.github.v3+json"})
        url "https://api.github.com/repos/Aidbox/examples/actions/artifacts"
        {:keys [status body error]} @(http-client/get url {:headers headers
                                                           :timeout 10000})]
    (cond
      error (do (log/error "fetch artifacts failed" {:error error})
                nil)
      (not= status 200) (do (log/error "fetch artifacts failed" {:status status :body body})
                            nil)
      :else (try
              (json/read-str body :key-fn keyword)
              (catch Exception e
                (log/error "parse artifacts failed" {:error (.getMessage e)})
                nil)))))

(defn find-latest-artifact
  "Find the latest examples-metadata artifact"
  [artifacts-response]
  (when-let [artifacts (:artifacts artifacts-response)]
    (->> artifacts
         (filter #(= (:name %) "examples-metadata"))
         (filter #(not (:expired %)))
         (sort-by :created_at)
         last)))

(defn download-artifact
  "Download artifact ZIP file from GitHub"
  [context artifact-id]
  (let [token (get-github-token context)]
    (when token
      (let [url (str "https://api.github.com/repos/Aidbox/examples/actions/artifacts/"
                     artifact-id "/zip")
            headers {"Authorization" (str "Bearer " token)
                     "Accept" "application/vnd.github.v3+json"}
            ;; First request to GitHub API - don't follow redirects
            {:keys [status headers body error]} @(http-client/get url
                                                                  {:headers headers
                                                                   :follow-redirects false
                                                                   :timeout 30000})]
        (cond
          error
          (do (log/error "download artifact failed" {:error error})
              nil)

          ;; GitHub returns 302 redirect to Azure blob storage
          (= status 302)
          (let [redirect-url (get headers :location)]
            (log/info "following redirect" {:url redirect-url})
            ;; Follow redirect without Authorization header
            (let [{:keys [status body error]} @(http-client/get redirect-url
                                                               {:as :byte-array
                                                                :timeout 30000})]
              (cond
                error (do (log/error "redirect download failed" {:error error})
                          nil)
                (not= status 200) (do (log/error "redirect download failed"
                                                 {:status status
                                                  :artifact-id artifact-id})
                                      nil)
                :else body)))

          (not= status 200)
          (do (log/error "download artifact failed"
                        {:status status
                         :artifact-id artifact-id})
              nil)

          :else body)))))

(defn extract-json-from-zip
  "Extract examples-metadata.json from ZIP bytes"
  [zip-bytes]
  (try
    (with-open [zip-stream (ZipInputStream. (ByteArrayInputStream. zip-bytes))]
      (loop []
        (when-let [entry (.getNextEntry zip-stream)]
          (if (= (.getName entry) "examples-metadata.json")
            (let [content (slurp zip-stream)]
              (json/read-str content :key-fn keyword))
            (recur)))))
    (catch Exception e
      (log/error "extract json failed" {:error (.getMessage e)})
      nil)))

(defn update-examples-from-artifact
  "Fetch and update examples from the latest GitHub artifact"
  [context]
  (log/info "update examples start" {})
  (try
    (when-let [artifacts (fetch-artifacts-list context)]
      (when-let [latest-artifact (find-latest-artifact artifacts)]
        (let [artifact-id (:id latest-artifact)
              created-at (:created_at latest-artifact)]
          (log/info "found latest artifact" {:id artifact-id :created created-at})

          (when-let [zip-bytes (download-artifact context artifact-id)]
            (log/info "artifact downloaded" {:size (count zip-bytes)})

            (when-let [examples-data (extract-json-from-zip zip-bytes)]
              (log/info "extracted examples" {:count (count (:examples examples-data))})

              ;; Update for aidbox product
              (products/set-current-product-id context "aidbox")
              (indexer/update-examples! context examples-data)
              (log/info "examples updated"
                        {:count (count (:examples examples-data))
                         :timestamp (:timestamp examples-data)})
              true)))))
    (catch Exception e
      (log/error "update examples failed" {:error (.getMessage e)})
      false)))

;; These functions are now handled by scheduler.clj with chime
;; Keeping only the core update logic here

(defn manual-update
  "Manually trigger an update (for testing)"
  [context]
  (update-examples-from-artifact context))
