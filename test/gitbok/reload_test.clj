(ns gitbok.reload-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [clojure.java.io :as io]
   [gitbok.reload :as reload]))

(deftest test-reload-state-structure
  (testing "Reload state has all expected keys"
    (let [context {:system (atom {})}
          ;; get-reload-state returns default state if none exists
          state (reload/get-reload-state context)]
      (is (map? state))
      ;; Check default values are set with new structure
      (is (contains? state :git-head))
      (is (contains? state :last-reload-time))
      (is (contains? state :app-version))
      (is (contains? state :in-progress)))))

(deftest test-state-management
  (testing "State management functions"
    (let [context {:system (atom {})}]
      (testing "Git HEAD state management"
        (let [test-head "abc123def456"]
          (reload/set-reload-state context {:git-head test-head
                                           :last-reload-time nil
                                           :app-version nil
                                           :in-progress false})
          (is (= test-head (:git-head (reload/get-reload-state context))))))

      (testing "Reloading flag"
        (reload/set-reloading context true)
        (is (reload/is-reloading? context))
        (reload/set-reloading context false)
        (is (not (reload/is-reloading? context)))))))