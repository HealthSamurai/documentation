(ns gitbok.reload-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [clojure.java.io :as io]
   [gitbok.reload :as reload]
   ))

(deftest test-last-update-time
  (testing "Getting last update time of .git directory"
    (when (.exists (io/file ".git"))
      (testing "get-last-update-time returns a Date when .git exists"
        (with-redefs [reload/volume-path "."]
          (let [update-time (reload/get-last-update-time)]
            (when update-time
              (is (instance? java.util.Date update-time))
              (println "Last update time:" update-time))))))))

(deftest test-reload-state-structure
  (testing "Reload state has all expected keys"
    (let [context {:system (atom {})}
          ;; get-reload-state returns default state if none exists
          state (reload/get-reload-state context)]
      (is (map? state))
      ;; Check default values are set
      (is (contains? state :checksum))
      (is (contains? state :last-update-time))
      (is (contains? state :last-reload-time))
      (is (contains? state :app-version))
      (is (contains? state :in-progress)))))

(deftest test-state-management
  (testing "State management functions"
    (let [context {:system (atom {})}]
      (testing "Checksum getter and setter for backwards compatibility"
        (let [test-checksum "test123"]
          (reload/set-current-checksum context test-checksum)
          (is (= test-checksum (reload/get-current-checksum context)))))

      (testing "Reloading flag"
        (reload/set-reloading context true)
        (is (reload/is-reloading? context))
        (reload/set-reloading context false)
        (is (not (reload/is-reloading? context)))))))
