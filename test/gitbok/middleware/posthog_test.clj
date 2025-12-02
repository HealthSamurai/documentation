(ns gitbok.middleware.posthog-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [gitbok.middleware.posthog :as posthog]))

(deftest special-path-test
  (testing "SEO files are tracked"
    (is (true? (#'posthog/special-path? "/sitemap.xml")))
    (is (true? (#'posthog/special-path? "/docs/sitemap.xml")))
    (is (true? (#'posthog/special-path? "/robots.txt")))
    (is (true? (#'posthog/special-path? "/docs/robots.txt"))))

  (testing "AI/LLM files are tracked"
    (is (true? (#'posthog/special-path? "/llms.txt")))
    (is (true? (#'posthog/special-path? "/docs/llms.txt")))
    (is (true? (#'posthog/special-path? "/llms-full.txt")))
    (is (true? (#'posthog/special-path? "/docs/llms-full.txt")))
    (is (true? (#'posthog/special-path? "/openapi.json")))
    (is (true? (#'posthog/special-path? "/docs/openapi.json")))
    (is (true? (#'posthog/special-path? "/openapi.yaml")))
    (is (true? (#'posthog/special-path? "/ai-plugin.json")))
    (is (true? (#'posthog/special-path? "/.well-known/ai-plugin.json")))
    (is (true? (#'posthog/special-path? "/docs/.well-known/ai-plugin.json"))))

  (testing "RSS feed files are tracked"
    (is (true? (#'posthog/special-path? "/feed.xml")))
    (is (true? (#'posthog/special-path? "/rss.xml")))
    (is (true? (#'posthog/special-path? "/atom.xml")))
    (is (true? (#'posthog/special-path? "/docs/feed.xml"))))

  (testing "Security files are tracked"
    (is (true? (#'posthog/special-path? "/security.txt")))
    (is (true? (#'posthog/special-path? "/.well-known/security.txt")))
    (is (true? (#'posthog/special-path? "/docs/.well-known/security.txt"))))

  (testing "Regular pages are NOT tracked"
    (is (not (#'posthog/special-path? "/")))
    (is (not (#'posthog/special-path? "/docs")))
    (is (not (#'posthog/special-path? "/docs/getting-started")))
    (is (not (#'posthog/special-path? "/docs/aidbox/overview")))
    (is (not (#'posthog/special-path? "/docs/aidbox/database/overview")))
    (is (not (#'posthog/special-path? "/docs/some/deep/page/path"))))

  (testing "Static assets are NOT tracked"
    (is (not (#'posthog/special-path? "/docs/static/css/app.css")))
    (is (not (#'posthog/special-path? "/docs/static/js/main.js")))
    (is (not (#'posthog/special-path? "/docs/.gitbook/assets/image.png"))))

  (testing "System endpoints are NOT tracked"
    (is (not (#'posthog/special-path? "/metrics")))
    (is (not (#'posthog/special-path? "/healthcheck")))
    (is (not (#'posthog/special-path? "/version"))))

  (testing "nil URI returns nil"
    (is (nil? (#'posthog/special-path? nil)))))
