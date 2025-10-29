(ns gitbok.middleware.posthog-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [gitbok.middleware.posthog :as posthog]))

(deftest should-track-path-test
  (testing "should NOT track system endpoints"
    (is (false? (#'posthog/should-track-path? "/metrics")))
    (is (false? (#'posthog/should-track-path? "/healthcheck")))
    (is (false? (#'posthog/should-track-path? "/version")))
    (is (false? (#'posthog/should-track-path? "/debug"))))

  (testing "should NOT track static assets"
    (is (false? (#'posthog/should-track-path? "/static/css/app.css")))
    (is (false? (#'posthog/should-track-path? "/static/js/main.js")))
    (is (false? (#'posthog/should-track-path? "/.gitbook/assets/image.png")))
    (is (false? (#'posthog/should-track-path? "/public/og-preview/some-page.png")))
    (is (false? (#'posthog/should-track-path? "/favicon.ico"))))

  (testing "should NOT track HTMX partials"
    (is (false? (#'posthog/should-track-path? "/partial/navigation")))
    (is (false? (#'posthog/should-track-path? "/partial/sidebar"))))

  (testing "should NOT track API endpoints"
    (is (false? (#'posthog/should-track-path? "/api/search")))
    (is (false? (#'posthog/should-track-path? "/api/v1/products"))))

  (testing "should NOT track service worker"
    (is (false? (#'posthog/should-track-path? "/service-worker.js"))))

  (testing "SHOULD track documentation pages"
    (is (true? (#'posthog/should-track-path? "/")))
    (is (true? (#'posthog/should-track-path? "/docs/getting-started")))
    (is (true? (#'posthog/should-track-path? "/aidbox/overview")))
    (is (true? (#'posthog/should-track-path? "/some/deep/page/path"))))

  (testing "SHOULD track special files"
    (is (true? (#'posthog/should-track-path? "/sitemap.xml")))
    (is (true? (#'posthog/should-track-path? "/robots.txt")))
    (is (true? (#'posthog/should-track-path? "/llms.txt")))))
