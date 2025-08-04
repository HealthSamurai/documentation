(ns gitbok.core-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [gitbok.core :as core]
   [gitbok.products :as products]
   [gitbok.utils :as utils]
   [ring.util.response :as resp]
   [system]))

(deftest test-root-redirect-handler
  (testing "root-redirect-handler with configured redirect"
    (let [context {:system (atom {})}]
      ;; Set up full config with root-redirect
      (products/set-full-config context {:root-redirect "/aidbox"
                                         :products []})
      (with-redefs [core/prefix "/"]
        (let [response (core/root-redirect-handler context {})]
          (is (= 302 (:status response)))
          (is (= "/aidbox" (get-in response [:headers "Location"])))))))

  (testing "root-redirect-handler with prefix and redirect"
    (let [context {:system (atom {})}]
      ;; Set up full config with root-redirect
      (products/set-full-config context {:root-redirect "/aidbox"
                                         :products []})
      (with-redefs [core/prefix "/docs"]
        (let [response (core/root-redirect-handler context {})]
          (is (= 302 (:status response)))
          (is (= "/docs/aidbox" (get-in response [:headers "Location"])))))))

  (testing "root-redirect-handler without configured redirect shows 404"
    (let [context {:system (atom {})}]
      ;; Set up full config without root-redirect
      (products/set-full-config context {:products []})
      (with-redefs [gitbok.ui.layout/layout
                    (fn [ctx req opts]
                      {:status (:status opts)
                       :body "404 page"})]
        (let [response (core/root-redirect-handler context {})]
          (is (= 404 (:status response)))
          (is (= "404 page" (:body response))))))))

(deftest test-redirect-to-readme
  (testing "redirect-to-readme renders readme view"
    (let [context {:system (atom {})}
          request {:uri "/test"}]
      ;; Mock current product
      (with-redefs [products/get-current-product
                    (fn [ctx] {:id "test" :path "/test"})
                    products/readme-url
                    (fn [ctx] "readme")
                    products/uri
                    (fn [ctx prefix uri] (str prefix uri))
                    core/prefix "/"
                    core/render-file-view
                    (fn [ctx req]
                      {:status 200
                       :body (str "Rendered: " (:uri req))})]
        (let [response (core/redirect-to-readme context request)]
          (is (= 200 (:status response)))
          (is (= "Rendered: /readme" (:body response)))))))

  (deftest test-init-products
    (testing "init-products stores full config"
      (let [context {:system (atom {})}]
        (with-redefs [products/load-products-config
                      (fn []
                        {:root-redirect "/aidbox"
                         :products [{:id "test" :path "/test"}]})]
          (let [result (core/init-products context)]
          ;; Check that products were returned
            (is (= 1 (count result)))
            (is (= "test" (-> result first :id)))
          ;; Check that configs were stored
            (is (= [{:id "test" :path "/test"}]
                   (products/get-products-config context)))
            (is (= {:root-redirect "/aidbox"
                    :products [{:id "test" :path "/test"}]}
                   (products/get-full-config context)))))))))

