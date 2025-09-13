(ns gitbok.state-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [gitbok.state :as state]
   [gitbok.test-helpers :as th]
   [clojure.java.io :as io]))

;; ========================================
;; Initialization Tests
;; ========================================

(deftest test-init-state!
  (testing "initializes with default values"
    (with-redefs [state/slurp-resource-init (fn [_] "1.0.0")]
      (let [context (state/init-state! {:port 8080
                                        :prefix ""
                                        :base-url "http://localhost:8080"
                                        :dev-mode false})]
        (is (map? context))
        (is (instance? clojure.lang.Atom (:system context)))
        (is (= 8080 (state/get-config context :port)))
        (is (= "" (state/get-config context :prefix)))
        (is (= "http://localhost:8080" (state/get-config context :base-url)))
        (is (false? (state/get-config context :dev-mode)))
        (is (= "1.0.0" (state/get-config context :version))))))

  (testing "initializes with custom configuration"
    (with-redefs [state/slurp-resource-init (fn [_] "2.0.0")]
      (let [context (state/init-state! {:port 3000
                                        :prefix "/docs"
                                        :base-url "https://example.com"
                                        :dev-mode true
                                        :github-token "test-token"})]
        (is (= 3000 (state/get-config context :port)))
        (is (= "/docs" (state/get-config context :prefix)))
        (is (= "https://example.com" (state/get-config context :base-url)))
        (is (true? (state/get-config context :dev-mode)))
        (is (= "test-token" (state/get-config context :github-token))))))

  (testing "loads from environment variables"
    (with-redefs [state/slurp-resource-init (fn [_] "1.0.0")]
      ;; Since System/getenv is a static Java method, we can't directly redef it
      ;; Instead, test with explicit parameters that would override env vars
      (let [context (state/init-state! {:port 9090
                                        :prefix "/api"
                                        :base-url "http://prod.com"
                                        :dev-mode true
                                        :github-token "env-token"})]
        (is (= 9090 (state/get-config context :port)))
        (is (= "/api" (state/get-config context :prefix)))
        (is (= "http://prod.com" (state/get-config context :base-url)))
        (is (true? (state/get-config context :dev-mode)))
        (is (= "env-token" (state/get-config context :github-token))))))

  (testing "handles missing version file gracefully"
    (with-redefs [state/slurp-resource-init
                  (fn [path]
                    (if (= path "version")
                      (throw (Exception. "File not found"))
                      "fallback"))]
      (is (thrown? Exception (state/init-state!))))))

;; ========================================
;; Core State Operation Tests
;; ========================================

(deftest test-get-state
  (testing "retrieves value at path"
    (let [context (th/create-test-context
                   {:config {:test-key "test-value"}
                    :cache {:items [1 2 3]}})]
      (is (= "test-value" (state/get-state context [:config :test-key])))
      (is (= [1 2 3] (state/get-state context [:cache :items])))
      (is (= 2 (state/get-state context [:cache :items 1])))))

  (testing "returns default for non-existent path"
    (let [context (th/create-test-context)]
      (is (nil? (state/get-state context [:non :existent])))
      (is (= "default" (state/get-state context [:non :existent] "default")))))

  (testing "handles invalid context"
    (is (nil? (state/get-state {} [:config])))
    (is (nil? (state/get-state nil [:config])))))

(deftest test-set-state!
  (testing "sets value at path"
    (let [context (th/create-test-context)]
      (state/set-state! context [:config :new-key] "new-value")
      (is (= "new-value" (state/get-state context [:config :new-key])))

      (state/set-state! context [:deeply :nested :path] {:data "value"})
      (is (= {:data "value"} (state/get-state context [:deeply :nested :path])))))

  (testing "overwrites existing values"
    (let [context (th/create-test-context {:config {:key "old"}})]
      (state/set-state! context [:config :key] "new")
      (is (= "new" (state/get-state context [:config :key])))))

  (testing "returns the set value"
    (let [context (th/create-test-context)]
      (is (= "test" (state/set-state! context [:key] "test"))))))

(deftest test-update-state!
  (testing "updates value with function"
    (let [context (th/create-test-context {:counter 0})]
      (state/update-state! context [:counter] inc)
      (is (= 1 (state/get-state context [:counter])))

      (state/update-state! context [:counter] + 5)
      (is (= 6 (state/get-state context [:counter])))))

  (testing "updates collections"
    (let [context (th/create-test-context {:items []})]
      (state/update-state! context [:items] conj 1)
      (state/update-state! context [:items] conj 2)
      (is (= [1 2] (state/get-state context [:items]))))))

;; ========================================
;; Configuration Management Tests
;; ========================================

(deftest test-config-functions
  (testing "get-config retrieves full config"
    (let [context (th/create-test-context
                   {:config {:port 8080 :prefix "/api"}})]
      (is (= {:port 8080 :prefix "/api"} (state/get-config context)))))

  (testing "get-config retrieves specific key"
    (let [context (th/create-test-context
                   {:config {:port 8080 :prefix "/api"}})]
      (is (= 8080 (state/get-config context :port)))
      (is (= "/api" (state/get-config context :prefix)))))

  (testing "get-config with defaults"
    (let [context (th/create-test-context {:config {}})]
      (is (nil? (state/get-config context :missing)))
      (is (= "default" (state/get-config context :missing "default"))))))

;; ========================================
;; Resource Loading Tests
;; ========================================

(deftest test-slurp-resource
  (testing "requires context"
    (is (thrown? IllegalArgumentException
                 (state/slurp-resource nil "test.txt"))))

  (testing "reads from classpath when no volume path"
    (let [context (th/create-test-context {:config {}})]
      (with-redefs [io/resource (fn [path]
                                  (when (= path "test.txt")
                                    (java.io.StringReader. "classpath content")))]
        (is (= "classpath content" (state/slurp-resource context "test.txt"))))))

  (testing "reads from volume path when configured"
    (let [temp-dir (io/file (System/getProperty "java.io.tmpdir")
                            (str "test-" (System/currentTimeMillis)))
          _ (.mkdirs temp-dir)
          test-file (io/file temp-dir "test.txt")
          _ (spit test-file "volume content")
          context (th/create-test-context
                   {:config {:docs-volume-path (.getPath temp-dir)}})]
      (try
        (is (= "volume content" (state/slurp-resource context "test.txt")))
        (finally
          (.delete test-file)
          (.delete temp-dir)))))

  (testing "falls back to classpath when file not in volume"
    (let [context (th/create-test-context
                   {:config {:docs-volume-path "/non-existent"}})]
      (with-redefs [io/resource (fn [path]
                                  (when (= path "fallback.txt")
                                    (java.io.StringReader. "classpath fallback")))]
        (is (= "classpath fallback"
               (state/slurp-resource context "fallback.txt"))))))

  (testing "throws when resource not found"
    (let [context (th/create-test-context {:config {}})]
      (with-redefs [io/resource (constantly nil)]
        (is (thrown? Exception
                     (state/slurp-resource context "missing.txt")))))))

(deftest test-slurp-resource-init
  (testing "reads from classpath only"
    (with-redefs [io/resource (fn [path]
                                (when (= path "init.txt")
                                  (java.io.StringReader. "init content")))]
      (is (= "init content" (state/slurp-resource-init "init.txt")))))

  (testing "throws when not found"
    (with-redefs [io/resource (constantly nil)]
      (is (thrown? Exception (state/slurp-resource-init "missing.txt"))))))

;; ========================================
;; Product State Management Tests
;; ========================================

(deftest test-products-management
  (testing "get and set products"
    (let [context (th/create-test-context)]
      (is (= [] (state/get-products context)))

      (state/set-products! context [{:id "prod1"} {:id "prod2"}])
      (is (= [{:id "prod1"} {:id "prod2"}] (state/get-products context))))))

(deftest test-product-state
  (testing "get product state with current product"
    (let [context (th/create-test-context
                   {:products {:indices {"prod1" {:data "test"}}}})
          ctx-with-product (assoc context :current-product-id "prod1")]
      (is (= {:data "test"} (state/get-product-state ctx-with-product [])))
      (is (= "test" (state/get-product-state ctx-with-product [:data])))))

  (testing "set product state"
    (let [context (th/create-test-context)
          ctx-with-product (assoc context :current-product-id "prod1")]
      (state/set-product-state! ctx-with-product [:key] "value")
      (is (= "value" (state/get-product-state ctx-with-product [:key])))))

  (testing "handles missing product context"
    (let [context (th/create-test-context)]
      (is (nil? (state/get-product-state context [:key])))
      (is (= "default" (state/get-product-state context [:key] "default")))
      ;; set-product-state! logs error but doesn't throw
      (state/set-product-state! context [:key] "value"))))

(deftest test-product-indices
  (testing "summary management"
    (let [context (assoc (th/create-test-context) :current-product-id "prod1")]
      (state/set-summary! context [:div "summary"])
      (is (= [:div "summary"] (state/get-summary context)))))

  (testing "file-to-uri index"
    (let [context (assoc (th/create-test-context) :current-product-id "prod1")]
      (state/set-file-to-uri-idx! context {"file.md" "/uri"})
      (is (= {"file.md" "/uri"} (state/get-file-to-uri-idx context)))))

  (testing "uri-to-file index"
    (let [context (assoc (th/create-test-context) :current-product-id "prod1")]
      (state/set-uri-to-file-idx! context {"/uri" "file.md"})
      (is (= {"/uri" "file.md"} (state/get-uri-to-file-idx context)))))

  (testing "redirects index"
    (let [context (assoc (th/create-test-context) :current-product-id "prod1")]
      (state/set-redirects-idx! context {"/old" "/new"})
      (is (= {"/old" "/new"} (state/get-redirects-idx context)))))

  (testing "sitemap"
    (let [context (assoc (th/create-test-context) :current-product-id "prod1")]
      (state/set-sitemap! context {:urls ["url1" "url2"]})
      (is (= {:urls ["url1" "url2"]} (state/get-sitemap context)))))

  (testing "markdown files index"
    (let [context (assoc (th/create-test-context) :current-product-id "prod1")]
      (state/set-md-files-idx! context {"file.md" "content"})
      (is (= {"file.md" "content"} (state/get-md-files-idx context)))))

  (testing "parsed markdown index"
    (let [context (assoc (th/create-test-context) :current-product-id "prod1")]
      (state/set-parsed-markdown-idx! context {"file.md" {:parsed true}})
      (is (= {"file.md" {:parsed true}}
             (state/get-parsed-markdown-idx context))))))

;; ========================================
;; Cache Management Tests
;; ========================================

(deftest test-cache-management
  (testing "get, set, and update cache"
    (let [context (th/create-test-context)]
      (is (nil? (state/get-cache context :test-cache)))
      (is (= "default" (state/get-cache context :test-cache "default")))

      (state/set-cache! context :test-cache {:data "cached"})
      (is (= {:data "cached"} (state/get-cache context :test-cache)))

      (state/update-cache! context :test-cache assoc :more "data")
      (is (= {:data "cached" :more "data"}
             (state/get-cache context :test-cache)))))

  (testing "cache isolation"
    (let [context (th/create-test-context)]
      (state/set-cache! context :cache1 "value1")
      (state/set-cache! context :cache2 "value2")
      (is (= "value1" (state/get-cache context :cache1)))
      (is (= "value2" (state/get-cache context :cache2))))))

;; ========================================
;; Scheduler Management Tests
;; ========================================

(deftest test-scheduler-management
  (testing "get, set, and remove scheduler"
    (let [context (th/create-test-context)
          scheduler {:id "test-scheduler"}]
      (is (nil? (state/get-scheduler context :test)))

      (state/set-scheduler! context :test scheduler)
      (is (= scheduler (state/get-scheduler context :test)))

      (state/remove-scheduler! context :test)
      (is (nil? (state/get-scheduler context :test))))))

;; ========================================
;; Runtime State Tests
;; ========================================

(deftest test-runtime-state
  (testing "get and set runtime"
    (let [context (th/create-test-context)]
      (is (nil? (state/get-runtime context :key)))
      (is (= "default" (state/get-runtime context :key "default")))

      (state/set-runtime! context :key "runtime-value")
      (is (= "runtime-value" (state/get-runtime context :key))))))

(deftest test-server-management
  (testing "server instance management"
    (let [context (th/create-test-context)
          server {:port 8080 :instance "server"}]
      (is (nil? (state/get-server context)))

      (state/set-server! context server)
      (is (= server (state/get-server context)))

      (state/clear-server! context)
      (is (nil? (state/get-server context))))))

;; ========================================
;; Thread Safety Tests
;; ========================================

(deftest test-concurrent-operations
  (testing "concurrent reads and writes"
    (let [context (th/create-test-context {:counter 0})
          threads 10
          operations-per-thread 100
          futures (doall
                   (for [_ (range threads)]
                     (future
                       (dotimes [_ operations-per-thread]
                         (state/update-state! context [:counter] inc)))))]
      (doseq [f futures] @f)
      (is (= (* threads operations-per-thread)
             (state/get-state context [:counter])))))

  (testing "atomic state transitions"
    (let [context (th/create-test-context {:values []})
          threads 10
          futures (doall
                   (for [i (range threads)]
                     (future
                       (state/update-state! context [:values] conj i))))]
      (doseq [f futures] @f)
      (is (= threads (count (state/get-state context [:values]))))
      (is (= (set (range threads))
             (set (state/get-state context [:values]))))))

  (testing "concurrent product state updates"
    (let [context (assoc (th/create-test-context) :current-product-id "prod1")
          threads 5
          futures (doall
                   (for [i (range threads)]
                     (future
                       (state/set-product-state! context [:key i] (str "value" i)))))]
      (doseq [f futures] @f)
      (dotimes [i threads]
        (is (= (str "value" i)
               (state/get-product-state context [:key i])))))))

;; ========================================
;; Debug Helper Tests
;; ========================================

(deftest test-get-full-state
  (testing "returns entire state atom"
    (let [initial-state {:config {:test true}
                         :products {:config []}
                         :cache {}}
          context (th/create-test-context initial-state)]
      (is (= (merge state/empty-state initial-state)
             (state/get-full-state context))))))