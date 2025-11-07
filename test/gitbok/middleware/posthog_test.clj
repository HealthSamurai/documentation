(ns gitbok.middleware.posthog-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [gitbok.middleware.posthog :as posthog]))

(deftest build-excluded-paths-regex-test
  (testing "builds correct regex with /docs prefix"
    (let [regex (#'posthog/build-excluded-paths-regex "/docs")]
      (testing "should NOT track system endpoints"
        (is (false? (#'posthog/should-track-path? "/metrics" regex)))
        (is (false? (#'posthog/should-track-path? "/healthcheck" regex)))
        (is (false? (#'posthog/should-track-path? "/version" regex)))
        (is (false? (#'posthog/should-track-path? "/debug" regex))))

      (testing "should NOT track static assets with /docs prefix"
        (is (false? (#'posthog/should-track-path? "/docs/static/css/app.css" regex)))
        (is (false? (#'posthog/should-track-path? "/docs/static/js/main.js" regex)))
        (is (false? (#'posthog/should-track-path? "/docs/.gitbook/assets/image.png" regex)))
        (is (false? (#'posthog/should-track-path? "/docs/public/og-preview/some-page.png" regex))))

      (testing "should NOT track files by extension"
        (is (false? (#'posthog/should-track-path? "/docs/aidbox/favicon.ico" regex)))
        (is (false? (#'posthog/should-track-path? "/docs/static/app.js" regex)))
        (is (false? (#'posthog/should-track-path? "/docs/static/app.css" regex)))
        (is (false? (#'posthog/should-track-path? "/docs/.gitbook/assets/logo.svg" regex)))
        (is (false? (#'posthog/should-track-path? "/docs/.gitbook/assets/image.jpg" regex)))
        (is (false? (#'posthog/should-track-path? "/docs/.gitbook/assets/image.png" regex))))

      (testing "should NOT track HTMX partials with /docs prefix"
        (is (false? (#'posthog/should-track-path? "/docs/partial/navigation" regex)))
        (is (false? (#'posthog/should-track-path? "/docs/partial/sidebar" regex))))

      (testing "should NOT track API endpoints with /docs prefix"
        (is (false? (#'posthog/should-track-path? "/docs/api/search" regex)))
        (is (false? (#'posthog/should-track-path? "/docs/api/v1/products" regex))))

      (testing "should NOT track service worker"
        (is (false? (#'posthog/should-track-path? "/service-worker.js" regex))))

      (testing "SHOULD track documentation pages"
        (is (true? (#'posthog/should-track-path? "/docs/getting-started" regex)))
        (is (true? (#'posthog/should-track-path? "/docs/aidbox/overview" regex)))
        (is (true? (#'posthog/should-track-path? "/docs/aidbox/database/overview" regex)))
        (is (true? (#'posthog/should-track-path? "/docs/some/deep/page/path" regex))))

      (testing "SHOULD track special files"
        (is (true? (#'posthog/should-track-path? "/docs/sitemap.xml" regex)))
        (is (true? (#'posthog/should-track-path? "/docs/robots.txt" regex)))
        (is (true? (#'posthog/should-track-path? "/docs/llms.txt" regex))))))

  (testing "builds correct regex with empty prefix"
    (let [regex (#'posthog/build-excluded-paths-regex "")]
      (testing "should track root paths when no prefix"
        (is (true? (#'posthog/should-track-path? "/" regex)))
        (is (true? (#'posthog/should-track-path? "/getting-started" regex)))
        (is (true? (#'posthog/should-track-path? "/aidbox/overview" regex))))

      (testing "should NOT track system endpoints"
        (is (false? (#'posthog/should-track-path? "/metrics" regex)))
        (is (false? (#'posthog/should-track-path? "/healthcheck" regex)))))))
