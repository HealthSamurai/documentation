(ns gitbok.reload-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [clojure.java.io :as io]
   [clojure.string :as str]
   [gitbok.reload :as reload]
   [gitbok.constants :as const]))

(deftest test-git-commit-hash-reading
  (testing "Reading git commit hash from .git directory"
    (let [git-dir (io/file ".git")]
      (when (.exists git-dir)
        (testing "Git directory exists"
          (is (.exists git-dir)))
        
        (testing "Can read HEAD file"
          (let [head-file (io/file ".git/HEAD")]
            (is (.exists head-file))
            (let [head-content (slurp head-file)]
              (is (not (str/blank? head-content)))
              (println "HEAD content:" head-content))))
        
        (testing "get-git-commit-hash returns a short hash when .git exists"
          (with-redefs [reload/volume-path "."]
            (let [commit-hash (reload/get-git-commit-hash)]
              (when commit-hash
                (is (string? commit-hash))
                (is (= 8 (count commit-hash)))
                (is (re-matches #"[a-f0-9]{8}" commit-hash))
                (println "Detected commit hash:" commit-hash)))))))))

(deftest test-last-update-time
  (testing "Getting last update time of .git directory"
    (when (.exists (io/file ".git"))
      (testing "get-last-update-time returns a Date when .git exists"
        (with-redefs [reload/volume-path "."]
          (let [update-time (reload/get-last-update-time)]
            (when update-time
              (is (instance? java.util.Date update-time))
              (println "Last update time:" update-time))))))))

(deftest test-docs-identifier-fallback
  (testing "get-docs-identifier prefers git commit over checksum"
    (with-redefs [reload/volume-path "."]
      (let [identifier (reload/get-docs-identifier)]
        (is (not (nil? identifier)))
        (println "Docs identifier:" identifier)
        
        ;; If .git exists, should be a short hash
        (when (.exists (io/file ".git"))
          (is (= 8 (count identifier)))
          (is (re-matches #"[a-f0-9]{8}" identifier)))))))

(deftest test-reload-state-structure
  (testing "Reload state has all expected keys"
    (let [context {:system (atom {})}
          ;; get-reload-state returns default state if none exists
          state (reload/get-reload-state context)]
      (is (map? state))
      ;; Check default values are set
      (is (contains? state :checksum))
      (is (contains? state :commit-hash))
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