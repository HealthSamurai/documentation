(ns gitbok.routes-test
  (:require [clojure.test :refer [deftest testing is]]
            [gitbok.routes :as routes]
            [gitbok.state :as state]
            [gitbok.products :as products]
            [gitbok.test-helpers :as th]))

(deftest test-trailing-slash-handling
  (testing "Root paths with and without trailing slash"
    (let [context (th/create-test-context)]
      ;; Setup products and config
      (state/set-products! context [{:id "aidbox" :path "/aidbox" :name "Aidbox"}])
      (products/set-full-config context {:root-redirect "/aidbox"})
      (state/set-cache! context :app-initialized true)
      
      (with-redefs [state/get-config (fn [ctx key & [default]]
                                       (case key
                                         :prefix "/docs"
                                         :base-url "http://localhost:8081"
                                         default))]
        (let [app (routes/create-app context)]
          
          ;; Test /docs redirects to /docs/aidbox
          (let [response (app {:request-method :get :uri "/docs"})]
            (is (= 302 (:status response)))
            (is (= "/docs/aidbox" (get-in response [:headers "Location"]))))
          
          ;; Test /docs/ redirects to /docs/aidbox
          (let [response (app {:request-method :get :uri "/docs/"})]
            (is (= 302 (:status response)))
            (is (= "/docs/aidbox" (get-in response [:headers "Location"]))))))))
  
  (testing "Product paths with trailing slash"
    (let [context (th/create-test-context)]
      (state/set-products! context [{:id "aidbox" :path "/aidbox" :name "Aidbox"}])
      (state/set-cache! context :app-initialized true)
      
      (with-redefs [state/get-config (fn [ctx key & [default]]
                                       (case key
                                         :prefix "/docs"
                                         :base-url "http://localhost:8081"
                                         default))
                    ;; Mock the redirect-to-readme handler
                    gitbok.handlers/redirect-to-readme 
                    (fn [ctx]
                      {:status 200 :body "readme"})]
        
        (let [app (routes/create-app context)]
          
          ;; Test /docs/aidbox serves content
          (let [response (app {:request-method :get :uri "/docs/aidbox"})]
            (is (= 200 (:status response)))
            (is (= "readme" (:body response))))
          
          ;; Test /docs/aidbox/ redirects to /docs/aidbox
          (let [response (app {:request-method :get :uri "/docs/aidbox/"})]
            (is (= 301 (:status response)))
            (is (= "/docs/aidbox" (get-in response [:headers "Location"]))))))))
  
  (testing "Product paths without prefix"
    (let [context (th/create-test-context)]
      (state/set-products! context [{:id "aidbox" :path "/aidbox" :name "Aidbox"}])
      (products/set-full-config context {:root-redirect "/aidbox"})
      (state/set-cache! context :app-initialized true)
      
      (with-redefs [state/get-config (fn [ctx key & [default]]
                                       (case key
                                         :prefix ""
                                         :base-url "http://localhost:8081"
                                         default))
                    gitbok.handlers/redirect-to-readme 
                    (fn [ctx]
                      {:status 200 :body "readme"})]
        
        (let [app (routes/create-app context)]
          
          ;; Test / redirects to /aidbox
          (let [response (app {:request-method :get :uri "/"})]
            (is (= 302 (:status response)))
            (is (= "/aidbox" (get-in response [:headers "Location"]))))
          
          ;; Test /aidbox serves content
          (let [response (app {:request-method :get :uri "/aidbox"})]
            (is (= 200 (:status response)))
            (is (= "readme" (:body response))))
          
          ;; Test /aidbox/ redirects to /aidbox
          (let [response (app {:request-method :get :uri "/aidbox/"})]
            (is (= 301 (:status response)))
            (is (= "/aidbox" (get-in response [:headers "Location"])))))))))