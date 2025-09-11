(ns gitbok.lastmod-generator-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [clojure.java.io :as io]
   [clojure.java.shell]
   [gitbok.lastmod.generator :as gen]))

(deftest test-generate-lastmod-data
  (testing "Generate lastmod data for non-existent directory returns empty map"
    (let [test-dir "/non/existent/directory"
          data (gen/generate-lastmod-data test-dir)]
      (is (map? data))
      (is (empty? data))))

  (testing "Generate lastmod data for temporary directory with markdown files"
    (let [temp-dir (io/file (System/getProperty "java.io.tmpdir")
                            (str "test-lastmod-" (System/currentTimeMillis)))
          test-file (io/file temp-dir "test.md")]
      (try
        (.mkdirs temp-dir)
        (spit test-file "# Test Content")
        ;; Without git history, files won't have lastmod dates
        ;; So we expect an empty map even with files present
        (let [data (gen/generate-lastmod-data (.getPath temp-dir))]
          (is (map? data))
          ;; Files exist but have no git history
          (is (empty? data)))
        (finally
          (.delete test-file)
          (.delete temp-dir)))))

  (testing "Generate lastmod data when git is available"
    ;; Only run this test if we're in a git repository
    (when (.exists (io/file ".git"))
      (let [test-dir "docs"
            data (gen/generate-lastmod-data test-dir)]
        (is (map? data))
        ;; Only check for non-empty if docs directory exists
        (when (.exists (io/file test-dir))
          (is (pos? (count data)))
          (is (contains? data "SUMMARY.md")))))))

(deftest test-caching
  (testing "Caching functionality with mock data"
    (let [context {:system (atom {})}
          product-id "test-product"
          test-dir "/non/existent/test"
          ;; First call will generate (empty map for non-existent dir)
          data1 (gen/generate-or-get-cached-lastmod context product-id test-dir)
          ;; Get the cached entry
          cached-entry (get (gen/get-lastmod-cache context) product-id)
          ;; Second call should return from cache
          data2 (gen/generate-or-get-cached-lastmod context product-id test-dir)]
      (is (map? data1))
      (is (= data1 data2))
      (is (some? cached-entry))
      (is (= (:data cached-entry) data1))))

  (testing "Cache invalidation when HEAD changes"
    (let [context {:system (atom {})}
          product-id "test-product"
          test-dir "/non/existent/test"
          ;; Generate initial cache
          data1 (gen/generate-or-get-cached-lastmod context product-id test-dir)
          ;; Manually update cache with different HEAD
          current-cache (gen/get-lastmod-cache context)
          updated-cache (assoc-in current-cache [product-id :head] "different-head")
          _ (gen/set-lastmod-cache context updated-cache)
          ;; This should regenerate because HEAD is different
          data2 (gen/generate-or-get-cached-lastmod context product-id test-dir)]
      ;; Both should be empty maps for non-existent directory
      (is (= data1 data2))
      (is (empty? data1)))))

(deftest test-get-repo-head
  (testing "get-repo-head with DOCS_REPO_PATH environment variable"
    ;; This test will return nil if not in a git repo or if git command fails
    ;; That's expected behavior on CI
    (let [head (gen/get-repo-head)]
      (is (or (nil? head) (string? head)))
      (when (string? head)
        ;; If we get a head, it should be a 40-char SHA1 hash
        (is (= 40 (count head))))))

  (testing "get-repo-head returns nil when git not available"
    ;; We can't easily test this without mocking shell/sh
    ;; but we can verify the function handles exceptions gracefully
    (with-redefs [clojure.java.shell/sh
                  (fn [& _] (throw (Exception. "Git not found")))]
      (is (nil? (gen/get-repo-head))))))

(deftest test-lastmod-for-file
  (testing "lastmod-for-file returns nil for non-existent file"
    (let [file (io/file "/non/existent/file.md")
          git-dir "."]
      (is (nil? (gen/lastmod-for-file file git-dir)))))

  (testing "lastmod-for-file returns nil when git command fails"
    (with-redefs [clojure.java.shell/sh
                  (fn [& _] {:exit 1 :out "" :err "error"})]
      (let [file (io/file "test.md")
            git-dir "."]
        (is (nil? (gen/lastmod-for-file file git-dir))))))

  (testing "lastmod-for-file parses valid timestamp"
    (with-redefs [clojure.java.shell/sh
                  (fn [& _] {:exit 0 :out "1609459200" :err ""})]
      (let [file (io/file "test.md")
            git-dir "."
            result (gen/lastmod-for-file file git-dir)]
        (is (string? result))
        ;; Should be ISO format: 2021-01-01T00:00:00Z
        (is (.contains result "2021-01-01"))))))
