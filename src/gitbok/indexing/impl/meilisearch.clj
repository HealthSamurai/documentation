(ns gitbok.indexing.impl.meilisearch
  (:require
   [cheshire.core :as json]
   [org.httpkit.client :as http-client]
   [gitbok.state :as state]))

(defn- get-config [context]
  {:url (or (state/get-config context :meilisearch-url) "http://localhost:7700")
   :api-key (state/get-config context :meilisearch-api-key)
   :index-name "docs"
   :timeout-ms 3000})

(defn- meilisearch-request
  "Make a request to Meilisearch API"
  [context method path & [opts]]
  (let [config (get-config context)
        url (str (:url config) path)
        headers (cond-> {"content-type" "application/json"}
                  (:api-key config) (assoc "authorization" (str "Bearer " (:api-key config))))
        options (merge {:headers headers
                        :timeout (:timeout-ms config)}
                       opts)
        response (case method
                   :get @(http-client/get url options)
                   :post @(http-client/post url options)
                   :put @(http-client/put url options)
                   :patch @(http-client/patch url options)
                   :delete @(http-client/delete url options))]
    response))

(defn- determine-hit-field
  "Determine which field was hit based on hierarchy levels"
  [hit]
  (cond
    (:hierarchy_lvl4 hit) :h4
    (:hierarchy_lvl3 hit) :h3
    (:hierarchy_lvl2 hit) :h2
    (:hierarchy_lvl1 hit) :h1
    (:hierarchy_lvl0 hit) :title
    :else :text))

(defn- extract-title
  "Extract the most relevant title from hit"
  [hit]
  (or (:hierarchy_lvl1 hit)
      (:hierarchy_lvl0 hit)
      "Documentation"))

(defn- extract-text-content
  "Extract and format text content from hit"
  [hit field]
  (case field
    :title (or (:hierarchy_lvl0 hit) "-")
    :h1 (or (:hierarchy_lvl1 hit) "-")
    :h2 (or (:hierarchy_lvl2 hit) "-")
    :h3 (or (:hierarchy_lvl3 hit) "-")
    :h4 (or (:hierarchy_lvl4 hit) "-")
    :text (or (:content hit)
              (:hierarchy_lvl6 hit)
              (:hierarchy_lvl5 hit)
              "-")))

(defn search
  "Search using Meilisearch API"
  [context query & {:keys [limit] :or {limit 100}}]
  (let [config (get-config context)
        response (meilisearch-request
                  context
                  :post
                  (str "/indexes/" (:index-name config) "/search")
                  {:body (json/generate-string
                          {:q query
                           :limit limit
                           :showMatchesPosition true})})]

    (if (= 200 (:status response))
      (let [body (json/parse-string (:body response) true)
            hits (:hits body)]
        (mapv (fn [hit]
                (let [hit-field (determine-hit-field hit)
                      title (extract-title hit)
                      url (:url hit)]
                  {:doc-id (or (:objectID hit) (str (java.util.UUID/randomUUID)))
                   :score (or (:_rankingScore hit) 0.0)
                   :hit-by hit-field
                   :uri (or url "")
                   :hit {:title (or title "-")
                         :h1 (extract-text-content hit :h1)
                         :h2 (extract-text-content hit :h2)
                         :h3 (extract-text-content hit :h3)
                         :h4 (extract-text-content hit :h4)
                         :text (extract-text-content hit :text)
                         :url url}}))
              hits))
      (throw (ex-info "Meilisearch search failed"
                      {:status (:status response)
                       :body (:body response)
                       :type :meilisearch-search-error})))))
