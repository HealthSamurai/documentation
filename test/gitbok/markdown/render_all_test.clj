(ns gitbok.markdown.render-all-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [clojure.string :as str]
   [gitbok.core :as core]
   [gitbok.products :as products]
   [gitbok.markdown.core :as markdown]
   [gitbok.ui.main-content :as main-content]
   [gitbok.utils :as utils]
   [gitbok.http :as http]
   [system]))

(defn read-markdown-file-no-try-catch
  "Version of read-markdown-file without try-catch block - any exception will fail the test"
  [context filepath]
  (println "test render " filepath)
  (let [[filepath section] (str/split filepath #"#")
        filepath (products/filepath context filepath)
        content* (utils/slurp-resource filepath)
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
    ;; Skip test if required directory structure doesn't exist
    ;; The test expects docs-new/aidbox/docs/SUMMARY.md based on products.yaml
    (if (or (not (.exists (clojure.java.io/file "docs-new/products.yaml")))
            (not (.exists (clojure.java.io/file "docs-new/aidbox/docs/SUMMARY.md"))))
      (is true "Skipping test - required directory structure not found")
      (with-redefs [core/read-markdown-file read-markdown-file-no-try-catch
                    core/dev? false]
        (let [context {:system (atom {})}
              _ (http/set-prefix context (or (System/getenv "DOCS_PREFIX") "/docs"))
              _ (http/set-base-url context (or (System/getenv "BASE_URL") "http://localhost:8081"))
              _ (http/set-dev-mode context false)
              products (core/init-products context)]
          (doseq [product products]
            (testing (str "Testing product: " (:id product))
              (core/init-product-indices context product)))
          (is true "All pages rendered successfully"))))))
