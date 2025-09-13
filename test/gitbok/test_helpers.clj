(ns gitbok.test-helpers
  "Helper functions for tests"
  (:require [gitbok.state :as state]))

(defn create-test-context
  "Create a test context with a fresh atom for state.
   Optionally accepts initial state to merge with empty-state."
  ([]
   {:system (atom state/empty-state)})
  ([initial-state]
   {:system (atom (merge state/empty-state initial-state))}))

(defmacro with-test-context
  "Execute body with a fresh test context. 
   Optionally accepts initial state to merge with empty-state."
  [[context-binding & [initial-state]] & body]
  `(let [~context-binding (create-test-context ~(or initial-state {}))]
     ~@body))

(defmacro with-temp-file
  "Execute body with a temporary file that is cleaned up afterwards.
   Binds the file path to the given symbol."
  [[file-binding content] & body]
  `(let [temp-dir# (clojure.java.io/file
                    (System/getProperty "java.io.tmpdir")
                    (str "test-" (System/currentTimeMillis)))
         _# (.mkdirs temp-dir#)
         temp-file# (clojure.java.io/file temp-dir# "test-file.txt")
         ~file-binding (.getPath temp-file#)]
     (try
       (spit temp-file# ~content)
       ~@body
       (finally
         (.delete temp-file#)
         (.delete temp-dir#)))))

;; Note: with-env-vars is removed because System/getenv is a static Java method
;; that cannot be mocked with with-redefs. Instead, test functions that accept
;; parameters to override environment variables. 

(defn create-test-product
  "Create a test product configuration"
  [& [{:keys [id path name]
       :or {id "test-product"
            path "/test"
            name "Test Product"}}]]
  {:id id
   :path path
   :name name
   :summary "docs/README.md"
   :favicon "public/favicon.ico"})

(defn assert-state-path
  "Assert that a value exists at the given state path"
  [context path expected]
  (let [actual (state/get-state context path)]
    (assert (= expected actual)
            (str "Expected " expected " at path " path " but got " actual))))

(defn with-product-context
  "Add product context to a test context"
  [context product-id]
  (assoc context :current-product-id product-id))

(defn mock-resource
  "Helper to create a mock resource for io/resource"
  [path content]
  (when (= path path)
    (java.io.StringReader. content)))

(defn create-test-state
  "Create a test state with common defaults"
  []
  {:config {:port 8080
            :prefix ""
            :base-url "http://localhost:8080"
            :dev-mode false}
   :products {:config []
              :indices {}}
   :cache {}
   :schedulers {}
   :runtime {}})