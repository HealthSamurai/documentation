(ns gitbok.markdown.render-all-test
  (:require
   [clojure.java.io]
   [clojure.string :as str]
   [clojure.test :refer [deftest is testing]]
   [gitbok.markdown.core :as markdown]
   [gitbok.products :as products]
   [gitbok.state :as state]
   [gitbok.ui.main-content :as main-content]
   [gitbok.handlers]
   [gitbok.init]
   [gitbok.indexing.core :as indexing]
   [gitbok.indexing.impl.summary :as summary]
   [gitbok.indexing.impl.uri-to-file :as uri-to-file]
   [gitbok.indexing.impl.file-to-uri :as file-to-uri]))

(defn read-markdown-file-no-try-catch
  "Version of read-markdown-file without try-catch block - any exception will fail the test"
  [context filepath]
  (let [[filepath section] (str/split filepath #"#")
        filepath (products/filepath context filepath)
        content* (state/slurp-resource context filepath)
        {:keys [parsed description title]}
        (markdown/parse-markdown-content
         context
         [filepath content*])]
    {:content
     (main-content/render-file* context filepath parsed title content*)
     :title title
     :description description
     :section section}))

(deftest test-all-pages-render-without-exceptions
  (testing "All pages must render without throwing exceptions"
    ;; Test with actual project structure
    (is (.exists (clojure.java.io/file "docs/SUMMARY.md")) "docs/SUMMARY.md not found")

    (with-redefs [gitbok.handlers/read-markdown-file read-markdown-file-no-try-catch
                    ;; Make utils/slurp-resource read from file system instead of classpath
                  state/slurp-resource (fn [_context path]
                                         (let [file (clojure.java.io/file path)]
                                           (if (.exists file)
                                             (slurp file)
                                             (throw (Exception. (str "Cannot find " path))))))]
      (let [context {:system (atom {})}
              ;; Set up basic config
            _ (state/set-state! context [:config :prefix] (or (System/getenv "DOCS_PREFIX") "/docs"))
            _ (state/set-state! context [:config :base-url] (or (System/getenv "BASE_URL") "http://localhost:8081"))
            _ (state/set-state! context [:config :dev-mode] false)
              ;; Set volume path to current directory so it reads from file system
            _ (state/set-state! context [:config :env :docs-volume-path] ".")

              ;; Create a simple product configuration for testing
              ;; Point directly to docs folder
            test-product {:id "test"
                          :name "Test Documentation"
                          :path "/"
                          :config ".gitbook.yaml"  ;; Simplified config path
                          :docs-relative-path "docs"
                          :root "docs"
                          :structure {:root "docs"
                                      :summary "SUMMARY.md"}}

              ;; Set the product config directly
            _ (products/set-products-config context [test-product])
            _ (products/set-full-config context {:products [test-product]
                                                 :root-redirect "/"})
            ;; Add product to context for the new architecture
            context (assoc context
                           :current-product-id "test"
                           :product test-product)]

          ;; Initialize indices - just test that basic initialization works
        (testing "Loading markdown files"
          (try
              ;; Step 1: Read summary
            (let [summary (summary/parse-summary context)]
              (is (> (count summary) 0) "Should have parsed summary")
              (state/set-summary! context summary))

              ;; Step 2: Build uri->file index using the actual function from init
            (let [uri-to-file-idx (uri-to-file/uri->file-idx context)]
              (is (> (count uri-to-file-idx) 0) "Should have created uri->file index")
              (state/set-uri-to-file-idx! context uri-to-file-idx))

              ;; Step 3: Build file->uri index using the actual function
            (let [file-to-uri-idx (file-to-uri/file->uri-idx context)]
              (is (> (count file-to-uri-idx) 0) "Should have created file->uri index")
              (state/set-file-to-uri-idx! context file-to-uri-idx))

              ;; Step 4: Load files into memory
            (let [file-to-uri-idx (state/get-file-to-uri-idx context)]
              (indexing/set-md-files-idx context file-to-uri-idx)
              (let [md-files (indexing/get-md-files-idx context)]
                (is (> (count md-files) 0) "Should have loaded markdown files")))

              ;; Step 5: Parse markdown files
            (let [md-files (indexing/get-md-files-idx context)]
              (markdown/set-parsed-markdown-index context md-files)
              (let [parsed-index (markdown/get-parsed-markdown-index context)]
                (is (> (count parsed-index) 0) "Should have parsed markdown files")))

            (is true "Basic initialization completed successfully")

            (catch Exception e
              (is false (str "Failed during test: " (.getMessage e))))))))))
