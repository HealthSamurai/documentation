;; (ns gitbok.summary-test
;;   (:require
;;    [clojure.test :refer [deftest is]]
;;    [gitbok.summary :as sut]))
;;
;; (deftest path-to-uri-test
;;   (is (= "hello" (sut/path->uri "docs/hello.md")))
;;   (is (= "hello" (sut/path->uri "hello.md")))
;;   (is (= "hello" (sut/path->uri "docs/hello")))
;;   (is (= "/docs/hello" (sut/path->uri "/docs/hello"))))
;;
;; (deftest uri->path-test
;;   (is (= "docs/README.md" (sut/uri->path "/")))
;;   (is (= "/upper-section/section/README.md"
;;          (sut/uri->path "/upper-section/section")))
;;   )
