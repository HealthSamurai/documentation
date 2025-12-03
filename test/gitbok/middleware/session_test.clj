(ns gitbok.middleware.session-test
  (:require
   [clojure.test :refer [deftest is testing]]
   [gitbok.middleware.session :as session]))

(def test-api-key "phc_test123")

(deftest test-url-decode
  (testing "decodes URL-encoded string"
    (is (= "{\"distinct_id\":\"abc\"}"
           (#'session/url-decode "%7B%22distinct_id%22%3A%22abc%22%7D"))))

  (testing "returns nil on invalid input"
    (is (nil? (#'session/url-decode "%invalid")))))

(deftest test-parse-posthog-cookie
  (testing "parses valid PostHog cookie JSON"
    (let [cookie-value "%7B%22distinct_id%22%3A%22019ae471-9dc6-7aa8-afb6-63c7045cd8a1%22%7D"]
      (is (= "019ae471-9dc6-7aa8-afb6-63c7045cd8a1"
             (#'session/parse-posthog-cookie cookie-value)))))

  (testing "parses cookie with additional fields"
    (let [cookie-value "%7B%22distinct_id%22%3A%22user123%22%2C%22%24device_id%22%3A%22device456%22%7D"]
      (is (= "user123"
             (#'session/parse-posthog-cookie cookie-value)))))

  (testing "returns nil for invalid JSON"
    (is (nil? (#'session/parse-posthog-cookie "not-json"))))

  (testing "returns nil for JSON without distinct_id"
    (let [cookie-value "%7B%22other_field%22%3A%22value%22%7D"]
      (is (nil? (#'session/parse-posthog-cookie cookie-value))))))

(deftest test-get-posthog-cookie-name
  (testing "builds correct cookie name"
    (is (= "ph_phc_test123_posthog"
           (#'session/get-posthog-cookie-name "phc_test123")))))

(deftest test-get-posthog-distinct-id
  (testing "extracts distinct_id from PostHog cookie"
    (let [cookie-name (str "ph_" test-api-key "_posthog")
          cookie-value "%7B%22distinct_id%22%3A%22user-abc-123%22%7D"
          request {:headers {"cookie" (str cookie-name "=" cookie-value)}}]
      (is (= "user-abc-123"
             (#'session/get-posthog-distinct-id request test-api-key)))))

  (testing "handles multiple cookies"
    (let [cookie-name (str "ph_" test-api-key "_posthog")
          cookie-value "%7B%22distinct_id%22%3A%22user-xyz%22%7D"
          request {:headers {"cookie" (str "other=value; " cookie-name "=" cookie-value "; another=test")}}]
      (is (= "user-xyz"
             (#'session/get-posthog-distinct-id request test-api-key)))))

  (testing "returns nil when no cookie header"
    (let [request {:headers {}}]
      (is (nil? (#'session/get-posthog-distinct-id request test-api-key)))))

  (testing "returns nil when PostHog cookie not present"
    (let [request {:headers {"cookie" "other=value; another=test"}}]
      (is (nil? (#'session/get-posthog-distinct-id request test-api-key)))))

  (testing "returns nil when api-key is nil"
    (let [request {:headers {"cookie" "ph_test_posthog=%7B%22distinct_id%22%3A%22user%22%7D"}}]
      (is (nil? (#'session/get-posthog-distinct-id request nil))))))

(deftest test-generate-anonymous-id
  (testing "generates ID with anon_ prefix"
    (let [id (#'session/generate-anonymous-id)]
      (is (clojure.string/starts-with? id "anon_"))))

  (testing "generates unique IDs"
    (let [id1 (#'session/generate-anonymous-id)
          id2 (#'session/generate-anonymous-id)]
      (is (not= id1 id2)))))
