(ns gitbok.format-relative-time-test
  (:require [clojure.test :refer :all]
            [gitbok.utils :as utils])
  (:import [java.time Instant Duration]))

(defn- instant-ago
  "Create an Instant that is the specified duration ago from now"
  [& args]
  (let [duration (apply #(Duration/of %1 %2) args)
        now (Instant/now)]
    (.minus now duration)))

(defn- instant-str-ago
  "Create an ISO string for an instant that is the specified duration ago"
  [& args]
  (.toString (apply instant-ago args)))

(deftest format-relative-time-test
  (testing "Recent times (seconds)"
    (is (= "just now" (utils/format-relative-time (instant-str-ago 0 java.time.temporal.ChronoUnit/SECONDS))))
    (is (= "just now" (utils/format-relative-time (instant-str-ago 30 java.time.temporal.ChronoUnit/SECONDS))))
    (is (= "just now" (utils/format-relative-time (instant-str-ago 59 java.time.temporal.ChronoUnit/SECONDS)))))
  
  (testing "Minutes"
    (is (= "a minute ago" (utils/format-relative-time (instant-str-ago 60 java.time.temporal.ChronoUnit/SECONDS))))
    (is (= "a minute ago" (utils/format-relative-time (instant-str-ago 90 java.time.temporal.ChronoUnit/SECONDS))))
    (is (= "2 minutes ago" (utils/format-relative-time (instant-str-ago 2 java.time.temporal.ChronoUnit/MINUTES))))
    (is (= "30 minutes ago" (utils/format-relative-time (instant-str-ago 30 java.time.temporal.ChronoUnit/MINUTES))))
    (is (= "59 minutes ago" (utils/format-relative-time (instant-str-ago 59 java.time.temporal.ChronoUnit/MINUTES)))))
  
  (testing "Hours"
    (is (= "an hour ago" (utils/format-relative-time (instant-str-ago 60 java.time.temporal.ChronoUnit/MINUTES))))
    (is (= "an hour ago" (utils/format-relative-time (instant-str-ago 90 java.time.temporal.ChronoUnit/MINUTES))))
    (is (= "2 hours ago" (utils/format-relative-time (instant-str-ago 2 java.time.temporal.ChronoUnit/HOURS))))
    (is (= "12 hours ago" (utils/format-relative-time (instant-str-ago 12 java.time.temporal.ChronoUnit/HOURS))))
    (is (= "23 hours ago" (utils/format-relative-time (instant-str-ago 23 java.time.temporal.ChronoUnit/HOURS)))))
  
  (testing "Days"
    (is (= "yesterday" (utils/format-relative-time (instant-str-ago 24 java.time.temporal.ChronoUnit/HOURS))))
    (is (= "yesterday" (utils/format-relative-time (instant-str-ago 36 java.time.temporal.ChronoUnit/HOURS))))
    (is (= "2 days ago" (utils/format-relative-time (instant-str-ago 2 java.time.temporal.ChronoUnit/DAYS))))
    (is (= "6 days ago" (utils/format-relative-time (instant-str-ago 6 java.time.temporal.ChronoUnit/DAYS)))))
  
  (testing "Weeks"
    (is (= "a week ago" (utils/format-relative-time (instant-str-ago 7 java.time.temporal.ChronoUnit/DAYS))))
    (is (= "a week ago" (utils/format-relative-time (instant-str-ago 10 java.time.temporal.ChronoUnit/DAYS))))
    (is (= "2 weeks ago" (utils/format-relative-time (instant-str-ago 14 java.time.temporal.ChronoUnit/DAYS))))
    (is (= "3 weeks ago" (utils/format-relative-time (instant-str-ago 21 java.time.temporal.ChronoUnit/DAYS))))
    (is (= "4 weeks ago" (utils/format-relative-time (instant-str-ago 29 java.time.temporal.ChronoUnit/DAYS)))))
  
  (testing "Months"
    (is (= "a month ago" (utils/format-relative-time (instant-str-ago 30 java.time.temporal.ChronoUnit/DAYS))))
    (is (= "a month ago" (utils/format-relative-time (instant-str-ago 45 java.time.temporal.ChronoUnit/DAYS))))
    (is (= "2 months ago" (utils/format-relative-time (instant-str-ago 60 java.time.temporal.ChronoUnit/DAYS))))
    (is (= "6 months ago" (utils/format-relative-time (instant-str-ago 180 java.time.temporal.ChronoUnit/DAYS))))
    ;; 364 days = 12 months (364/30 = 12.13)
    (is (= "12 months ago" (utils/format-relative-time (instant-str-ago 364 java.time.temporal.ChronoUnit/DAYS)))))
  
  (testing "Years"
    (is (= "a year ago" (utils/format-relative-time (instant-str-ago 365 java.time.temporal.ChronoUnit/DAYS))))
    (is (= "a year ago" (utils/format-relative-time (instant-str-ago 500 java.time.temporal.ChronoUnit/DAYS))))
    (is (= "2 years ago" (utils/format-relative-time (instant-str-ago 730 java.time.temporal.ChronoUnit/DAYS))))
    (is (= "5 years ago" (utils/format-relative-time (instant-str-ago 1825 java.time.temporal.ChronoUnit/DAYS))))
    (is (= "10 years ago" (utils/format-relative-time (instant-str-ago 3650 java.time.temporal.ChronoUnit/DAYS)))))
  
  (testing "Edge cases - nil and invalid input"
    (is (= nil (utils/format-relative-time nil)))
    ;; Empty string returns itself when parse fails
    (is (= "" (utils/format-relative-time "")))
    ;; Invalid dates return the original string
    (is (= "not-a-date" (utils/format-relative-time "not-a-date")))
    (is (= "2024-13-45T25:61:70Z" (utils/format-relative-time "2024-13-45T25:61:70Z")))) ; Invalid date
  
  (testing "Edge cases - future dates"
    ;; Future dates should still work (showing negative time ago conceptually)
    (let [future-instant (.toString (.plus (Instant/now) (Duration/ofHours 2)))]
      ;; This will show "just now" because the diff will be negative and likely default to "just now"
      (is (string? (utils/format-relative-time future-instant)))))
  
  (testing "Edge cases - exact boundaries"
    ;; Test exact boundaries between units
    (is (= "just now" (utils/format-relative-time (instant-str-ago 59999 java.time.temporal.ChronoUnit/MILLIS))))
    (is (= "a minute ago" (utils/format-relative-time (instant-str-ago 60000 java.time.temporal.ChronoUnit/MILLIS))))
    (is (= "59 minutes ago" (utils/format-relative-time (instant-str-ago 3599 java.time.temporal.ChronoUnit/SECONDS))))
    (is (= "an hour ago" (utils/format-relative-time (instant-str-ago 3600 java.time.temporal.ChronoUnit/SECONDS))))
    (is (= "23 hours ago" (utils/format-relative-time (instant-str-ago 86399 java.time.temporal.ChronoUnit/SECONDS))))
    (is (= "yesterday" (utils/format-relative-time (instant-str-ago 86400 java.time.temporal.ChronoUnit/SECONDS)))))
  
  (testing "Edge cases - very old dates"
    (is (= "100 years ago" (utils/format-relative-time (instant-str-ago 36500 java.time.temporal.ChronoUnit/DAYS))))
    (is (= "1000 years ago" (utils/format-relative-time (instant-str-ago 365000 java.time.temporal.ChronoUnit/DAYS)))))
  
  (testing "Edge cases - different ISO formats"
    ;; Test various valid ISO 8601 formats
    (is (string? (utils/format-relative-time "2024-01-15T10:30:00Z")))
    (is (string? (utils/format-relative-time "2024-01-15T10:30:00.123Z")))
    (is (string? (utils/format-relative-time "2024-01-15T10:30:00+00:00")))
    (is (string? (utils/format-relative-time "2024-01-15T10:30:00.123456789Z"))))
  
  (testing "Edge cases - malformed but parseable dates"
    ;; These return the original string due to parse errors
    (is (= "2024-01-15" (utils/format-relative-time "2024-01-15"))) ; Missing time
    (is (= "10:30:00Z" (utils/format-relative-time "10:30:00Z"))) ; Missing date
    (is (= "January 15, 2024" (utils/format-relative-time "January 15, 2024"))) ; Wrong format
    (is (= "2024/01/15T10:30:00Z" (utils/format-relative-time "2024/01/15T10:30:00Z"))))) ; Wrong separator