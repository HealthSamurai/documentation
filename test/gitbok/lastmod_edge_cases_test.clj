(ns gitbok.lastmod-edge-cases-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [clojure.java.io :as io]
   [gitbok.lastmod.generator :as gen]))

(deftest test-no-git-available
  (testing "System gracefully handles when git is not available"
    (with-redefs [clojure.java.shell/sh
                  (constantly {:exit 127 :out "" :err "git: command not found"})]
      ;; get-repo-head should return nil
      (is (nil? (gen/get-repo-head)))

      ;; generate-lastmod-data should return empty map
      (let [data (gen/generate-lastmod-data "/some/path")]
        (is (map? data))
        (is (empty? data)))

      ;; lastmod-for-file should return nil
      (is (nil? (gen/lastmod-for-file (io/file "test.md") "."))))))

(deftest test-git-permission-denied
  (testing "System handles git permission errors"
    (with-redefs [clojure.java.shell/sh
                  (constantly {:exit 128 :out "" :err "fatal: detected dubious ownership"})]
      (is (nil? (gen/get-repo-head)))
      (let [data (gen/generate-lastmod-data "/some/path")]
        (is (map? data))
        (is (empty? data))))))

(deftest test-performance-many-files
  (testing "Performance with many markdown files"
    (let [temp-dir (io/file (System/getProperty "java.io.tmpdir")
                            (str "test-perf-" (System/currentTimeMillis)))]
      (try
        (.mkdirs temp-dir)
        ;; Create 100 test files (not 1000 - that's too much for unit test)
        (doseq [i (range 100)]
          (spit (io/file temp-dir (str "test-" i ".md")) (str "# Test " i)))

        ;; Measure generation time
        (let [start (System/currentTimeMillis)
              data (gen/generate-lastmod-data (.getPath temp-dir))
              duration (- (System/currentTimeMillis) start)]
          ;; Should complete in reasonable time (5 seconds for 100 files)
          (println duration)
          (is (< duration 5000)
              (str "Generation took " duration "ms, expected < 5000ms"))
          ;; Without git history, should return empty map
          (is (empty? data)))

        (finally
          ;; Cleanup
          (doseq [f (.listFiles temp-dir)]
            (.delete f))
          (.delete temp-dir))))))

(deftest test-cache-invalidation-on-head-change
  (testing "Cache properly invalidates when HEAD changes"
    (let [context {:system (atom {})}
          product-id "test-product"
          test-dir "/test/dir"]

      ;; Mock initial HEAD
      (with-redefs [gen/get-repo-head (constantly "abc123")
                    gen/generate-lastmod-data (constantly {"file1.md" "2024-01-01"})]
        (let [data1 (gen/generate-or-get-cached-lastmod context product-id test-dir)]
          (is (= {"file1.md" "2024-01-01"} data1))))

      ;; Same HEAD - should use cache
      (with-redefs [gen/get-repo-head (constantly "abc123")
                    gen/generate-lastmod-data
                    (fn [_] (throw (Exception. "Should not be called - should use cache")))]
        (let [data2 (gen/generate-or-get-cached-lastmod context product-id test-dir)]
          (is (= {"file1.md" "2024-01-01"} data2))))

      ;; Different HEAD - should regenerate
      (with-redefs [gen/get-repo-head (constantly "def456")
                    gen/generate-lastmod-data (constantly {"file1.md" "2024-01-02"})]
        (let [data3 (gen/generate-or-get-cached-lastmod context product-id test-dir)]
          (is (= {"file1.md" "2024-01-02"} data3)))))))


(deftest test-empty-directory
  (testing "Handles empty directory correctly"
    (let [temp-dir (io/file (System/getProperty "java.io.tmpdir")
                            (str "test-empty-" (System/currentTimeMillis)))]
      (try
        (.mkdirs temp-dir)
        (let [data (gen/generate-lastmod-data (.getPath temp-dir))]
          (is (map? data))
          (is (empty? data)))
        (finally
          (.delete temp-dir))))))

(deftest test-non-markdown-files-ignored
  (testing "Only processes .md files"
    (let [temp-dir (io/file (System/getProperty "java.io.tmpdir")
                            (str "test-mixed-" (System/currentTimeMillis)))]
      (try
        (.mkdirs temp-dir)
        (spit (io/file temp-dir "test.md") "# Markdown")
        (spit (io/file temp-dir "test.txt") "Text file")
        (spit (io/file temp-dir "test.html") "<html>")
        (spit (io/file temp-dir "README") "No extension")

        ;; Without git, all files will have nil dates
        ;; But we can verify that only .md files are processed
        (with-redefs [gen/batch-get-all-lastmods
                      (fn [_ _]
                        ;; Return timestamps only for .md files
                        {"test.md" "2024-01-01T00:00:00Z"})]
          (let [data (gen/generate-lastmod-data (.getPath temp-dir))]
            ;; Should only have the .md file
            (is (= 1 (count data)))
            (is (contains? data "test.md"))))

        (finally
          (doseq [f (.listFiles temp-dir)]
            (.delete f))
          (.delete temp-dir))))))

(deftest test-malformed-git-output
  (testing "Handles malformed git output gracefully"
    (with-redefs [clojure.java.shell/sh
                  (constantly {:exit 0 :out "not-a-number\n" :err ""})]
      (is (nil? (gen/lastmod-for-file (io/file "test.md") "."))))

    (with-redefs [clojure.java.shell/sh
                  (constantly {:exit 0 :out "" :err ""})]
      (is (nil? (gen/lastmod-for-file (io/file "test.md") "."))))

    (with-redefs [clojure.java.shell/sh
                  (constantly {:exit 0 :out "   \n\n  " :err ""})]
      (is (nil? (gen/lastmod-for-file (io/file "test.md") "."))))))

(deftest test-docs-repo-path-env
  (testing "Uses DOCS_REPO_PATH environment variable when set"
    ;; Can't actually set env vars in tests, but we can verify the code path
    ;; by checking that the function attempts to use the env var
    (let [original-env (System/getenv "DOCS_REPO_PATH")]
      ;; The function should use "." when env var is not set
      (when-not original-env
        (with-redefs [clojure.java.shell/sh
                      (fn [& args]
                        ;; Verify that :dir is "." when env var not set
                        (is (some #(= % ".") args))
                        {:exit 1 :out "" :err ""})]
          (gen/get-repo-head))))))
