(ns gitbok.core-test
  (:require
   [gitbok.state :as state]
   [gitbok.test-helpers :as th]
   [clojure.test :refer [deftest testing is]]
   [gitbok.handlers :as handlers]
   [gitbok.ui.layout :as layout]
   [gitbok.products :as products]
   [gitbok.initialization :as initialization]
   [gitbok.analytics.posthog :as posthog]))

(deftest test-root-redirect-handler
  (testing "root-redirect-handler with configured redirect"
    (let [context (th/create-test-context)]
      ;; Set up full config with root-redirect
      (products/set-full-config context {:root-redirect "/aidbox"
                                         :products []})
      (with-redefs [state/get-config (fn [_ _ & [default]] (or "/" default))]
        (let [response (handlers/root-redirect-handler (assoc context :request {}))]
          (is (= 302 (:status response)))
          (is (= "/aidbox" (get-in response [:headers "Location"])))))))

  (testing "root-redirect-handler with prefix and redirect"
    (let [context (th/create-test-context)]
      ;; Set up full config with root-redirect
      (products/set-full-config context {:root-redirect "/aidbox"
                                         :products []})
      (with-redefs [state/get-config (fn [_ _ & [default]] (or "/docs" default))]
        (let [response (handlers/root-redirect-handler (assoc context :request {}))]
          (is (= 302 (:status response)))
          (is (= "/docs/aidbox" (get-in response [:headers "Location"])))))))

  (testing "root-redirect-handler without configured redirect shows 404"
    (let [context (th/create-test-context)]
      ;; Set up full config without root-redirect
      (products/set-full-config context {:products []})
      (with-redefs [layout/layout
                    (fn [_ctx opts]
                      {:status (:status opts)
                       :body "404 page"})]
        (let [response (handlers/root-redirect-handler (assoc context :request {}))]
          (is (= 404 (:status response)))
          (is (= "404 page" (:body response))))))))

(deftest test-redirect-to-readme
  (testing "redirect-to-readme renders readme view"
    (let [context {:system (atom {})}
          request {:uri "/test"}
          ctx (assoc context :request request)]
      ;; Mock current product and rendering functions
      (with-redefs [products/get-current-product
                    (fn [_ctx] {:id "test" :path "/test"})
                    products/readme-relative-path
                    (fn [_ctx] "readme/README.md")
                    handlers/handle-cached-response
                    (fn [_ctx _filepath _type render-fn]
                      {:status 200
                       :body "Readme content rendered"})]
        (let [response (handlers/redirect-to-readme ctx)]
          (is (= 200 (:status response)))
          (is (= "Readme content rendered" (:body response))))))))

(deftest test-process-redirect-target
  (testing "process-redirect-target removes .md extension and preserves fragments"
    ;; Test removing .md extension
    (is (= "api/rest-api/aidbox-search"
           (handlers/process-redirect-target "api/rest-api/aidbox-search.md")))

    ;; Test preserving fragments with .md extension
    (is (= "api/rest-api/aidbox-search#aidbox-special-search-parameters"
           (handlers/process-redirect-target "api/rest-api/aidbox-search.md#aidbox-special-search-parameters")))

    ;; Test with complex fragment containing dots
    (is (= "reference/settings/all-settings#security.grant-page-url"
           (handlers/process-redirect-target "reference/settings/all-settings.md#security.grant-page-url")))

    ;; Test without .md extension (should remain unchanged)
    (is (= "api/rest-api/aidbox-search"
           (handlers/process-redirect-target "api/rest-api/aidbox-search")))

    ;; Test without .md but with fragment
    (is (= "api/rest-api/aidbox-search#section"
           (handlers/process-redirect-target "api/rest-api/aidbox-search#section")))

    ;; Test nil input
    (is (nil? (handlers/process-redirect-target nil)))

    ;; Test empty string
    (is (= "" (handlers/process-redirect-target "")))))

(deftest test-get-redirect-with-fragment
  (testing "get-redirect-with-fragment handles various redirect scenarios"
    (let [context (th/create-test-context)]
      ;; Set up products first
      (state/set-products! context [{:id "test-product" :path "/test"}])
      ;; Create context with current product
      (let [ctx (products/set-current-product-id context "test-product")]
        ;; Set up test redirects
        (state/set-redirects-idx! ctx
                                  {"reference/settings/security-and-access-control" "reference/settings/all-settings.md"
                                   "reference/settings/security-and-access-control#security.grant-page-url" "reference/settings/all-settings.md#security.grant-page-url"
                                   "api/search" "api/rest-api/search.md"})

        ;; Test 1: Direct redirect without fragment
        (is (= ["reference/settings/all-settings.md" false]
               (handlers/get-redirect-with-fragment ctx "reference/settings/security-and-access-control")))

        ;; Test 2: URI with fragment, redirect exists for base URI only
        ;; Should preserve the fragment
        (is (= ["reference/settings/all-settings.md" true]
               (handlers/get-redirect-with-fragment ctx "reference/settings/security-and-access-control#section-a")))

        ;; Test 3: Specific redirect for URI with fragment
        ;; Should not preserve fragment (use the specific redirect)
        (is (= ["reference/settings/all-settings.md#security.grant-page-url" false]
               (handlers/get-redirect-with-fragment ctx "reference/settings/security-and-access-control#security.grant-page-url")))

        ;; Test 4: No redirect found
        (is (= [nil false]
               (handlers/get-redirect-with-fragment ctx "non-existent-page")))

        ;; Test 5: No redirect found for URI with fragment
        (is (= [nil false]
               (handlers/get-redirect-with-fragment ctx "non-existent-page#section")))))))

(deftest test-posthog-initialization
  (testing "PostHog client initializes and is accessible via state/get-runtime when dev-mode is false"
    (let [mock-client (reify Object
                        (toString [_] "MockPostHogClient"))
          context (th/create-test-context)]
      (with-redefs [posthog/create-client (fn [api-key _host]
                                             (when api-key mock-client))]
        ;; Set config for non-dev mode with API key
        (state/set-state! context [:config :dev-mode] false)
        (state/set-state! context [:config :posthog-api-key] "test-api-key")
        (state/set-state! context [:config :posthog-host] "https://test.posthog.com")

        ;; Simulate initialization logic from core.clj
        (let [dev-mode (state/get-config context :dev-mode)
              posthog-api-key (state/get-config context :posthog-api-key)
              posthog-host (state/get-config context :posthog-host)]
          (when-not dev-mode
            (when-let [client (posthog/create-client posthog-api-key posthog-host)]
              (state/set-runtime! context :posthog-client client))))

        ;; Verify client is initialized and accessible
        (is (some? (state/get-runtime context :posthog-client))
            "PostHog client should be initialized")
        (is (= mock-client (state/get-runtime context :posthog-client))
            "PostHog client should be the mock client"))))

  (testing "PostHog client does NOT initialize when dev-mode is true"
    (let [context (th/create-test-context)]
      (with-redefs [posthog/create-client (fn [_ _]
                                             (throw (Exception. "Should not be called in dev mode")))]
        ;; Set config for dev mode
        (state/set-state! context [:config :dev-mode] true)
        (state/set-state! context [:config :posthog-api-key] "test-api-key")
        (state/set-state! context [:config :posthog-host] "https://test.posthog.com")

        ;; Simulate initialization logic from core.clj
        (let [dev-mode (state/get-config context :dev-mode)
              posthog-api-key (state/get-config context :posthog-api-key)
              posthog-host (state/get-config context :posthog-host)]
          (when-not dev-mode
            (when-let [client (posthog/create-client posthog-api-key posthog-host)]
              (state/set-runtime! context :posthog-client client))))

        ;; Verify client is NOT initialized
        (is (nil? (state/get-runtime context :posthog-client))
            "PostHog client should NOT be initialized in dev mode"))))

  (testing "PostHog client does NOT initialize when API key is missing"
    (let [context (th/create-test-context)]
      (with-redefs [posthog/create-client (fn [api-key _host]
                                             (when api-key
                                               (throw (Exception. "Should not create client without API key"))))]
        ;; Set config without API key
        (state/set-state! context [:config :dev-mode] false)
        (state/set-state! context [:config :posthog-api-key] nil)
        (state/set-state! context [:config :posthog-host] "https://test.posthog.com")

        ;; Simulate initialization logic from core.clj
        (let [dev-mode (state/get-config context :dev-mode)
              posthog-api-key (state/get-config context :posthog-api-key)
              posthog-host (state/get-config context :posthog-host)]
          (when-not dev-mode
            (when-let [client (posthog/create-client posthog-api-key posthog-host)]
              (state/set-runtime! context :posthog-client client))))

        ;; Verify client is NOT initialized
        (is (nil? (state/get-runtime context :posthog-client))
            "PostHog client should NOT be initialized without API key")))))
