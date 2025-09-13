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
      ;; Mock current product
      (with-redefs [products/get-current-product
                    (fn [_ctx] {:id "test" :path "/test"})
                    products/readme-url
                    (fn [_ctx] "readme")
                    products/uri
                    (fn [_ctx prefix uri] (str prefix uri))
                    state/get-config (fn [_ _ & [default]] (or "/" default))
                    handlers/render-file-view
                    (fn [ctx]
                      {:status 200
                       :body (str "Rendered: " (get-in ctx [:request :uri]))})]
        (let [response (handlers/redirect-to-readme ctx)]
          (is (= 200 (:status response)))
          (is (= "Rendered: /readme" (:body response)))))))

  (deftest test-init-products
    (testing "init-products stores full config"
      (let [context (th/create-test-context)]
        (with-redefs [products/load-products-config
                      (fn [_ctx]
                        {:root-redirect "/aidbox"
                         :products [{:id "test" :path "/test"}]})]
          (let [result (initialization/init-products context)]
          ;; Check that products were returned
            (is (= 1 (count result)))
            (is (= "test" (-> result first :id)))
          ;; Check that configs were stored
            (is (= [{:id "test" :path "/test"}]
                   (products/get-products-config context)))
            (is (= {:root-redirect "/aidbox"
                    :products [{:id "test" :path "/test"}]}
                   (products/get-full-config context)))))))))

