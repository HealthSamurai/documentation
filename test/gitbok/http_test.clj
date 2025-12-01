(ns gitbok.http-test
  (:require [clojure.test :refer [deftest testing is]]
            [gitbok.http :as http]
            [gitbok.utils :as utils]
            [gitbok.test-helpers :as h]))

(def ctx
  (h/create-test-context {:config {:base-url "https://www.health-samurai.io"
                                   :prefix "/docs"}}))

(def ctx-empty-prefix
  (h/create-test-context {:config {:base-url "https://www.health-samurai.io"
                                   :prefix ""}}))

(def ctx-no-prefix
  (h/create-test-context {:config {:base-url "https://www.health-samurai.io"}}))

(deftest get-absolute-url-test
  (testing "Basic URL construction"
    (is (= "https://www.health-samurai.io/docs/page"
           (http/get-absolute-url ctx "/page"))))

  (testing "Prevents prefix duplication"
    (is (= "https://www.health-samurai.io/docs/page"
           (http/get-absolute-url ctx "/docs/page")))
    (is (= "https://www.health-samurai.io/docs/nested/page"
           (http/get-absolute-url ctx "/docs/nested/page"))))

  (testing "Does not strip similar but different prefixes"
    (is (= "https://www.health-samurai.io/docs/docs-api/endpoint"
           (http/get-absolute-url ctx "/docs-api/endpoint")))
    (is (= "https://www.health-samurai.io/docs/documentation/page"
           (http/get-absolute-url ctx "/documentation/page"))))

  (testing "Empty prefix results in double slash"
    (is (= "https://www.health-samurai.io//page"
           (http/get-absolute-url ctx-empty-prefix "/page"))))

  (testing "Nil prefix uses default empty string"
    (is (= "https://www.health-samurai.io//page"
           (http/get-absolute-url ctx-no-prefix "/page"))))

  (testing "URL equals prefix exactly - trailing slash added"
    (is (= "https://www.health-samurai.io/docs/"
           (http/get-absolute-url ctx "/docs"))))

  (testing "Root URL - no trailing slash"
    (is (= "https://www.health-samurai.io/docs"
           (http/get-absolute-url ctx "/")))))

(deftest og-preview-url-test
  (testing "OG preview URL without prefix duplication"
    (let [product-id "aidbox"
          filepath "getting-started/overview.md"
          png-filename (clojure.string/replace filepath #"\.md" ".png")
          og-url (http/get-absolute-url
                  ctx
                  (utils/concat-urls "/public/og-preview" product-id png-filename))]
      (is (= "https://www.health-samurai.io/docs/public/og-preview/aidbox/getting-started/overview.png"
             og-url))))

  (testing "OG preview URL for nested path"
    (let [product-id "aidbox"
          filepath "api/rest/crud/create.md"
          png-filename (clojure.string/replace filepath #"\.md" ".png")
          og-url (http/get-absolute-url
                  ctx
                  (utils/concat-urls "/public/og-preview" product-id png-filename))]
      (is (= "https://www.health-samurai.io/docs/public/og-preview/aidbox/api/rest/crud/create.png"
             og-url))))

  (testing "OG preview URL for fhirbase product"
    (let [product-id "fhirbase"
          filepath "installation.md"
          png-filename (clojure.string/replace filepath #"\.md" ".png")
          og-url (http/get-absolute-url
                  ctx
                  (utils/concat-urls "/public/og-preview" product-id png-filename))]
      (is (= "https://www.health-samurai.io/docs/public/og-preview/fhirbase/installation.png"
             og-url)))))
