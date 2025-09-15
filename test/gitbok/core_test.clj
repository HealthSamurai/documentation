(ns gitbok.core-test
  (:require
   [gitbok.state :as state]
   [gitbok.test-helpers :as th]
   [clojure.test :refer [deftest testing is]]
   [gitbok.handlers :as handlers]
   [gitbok.ui.layout :as layout]
   [gitbok.products :as products]
   [gitbok.initialization :as initialization]))

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

