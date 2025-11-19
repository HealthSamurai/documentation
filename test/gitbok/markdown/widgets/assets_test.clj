(ns gitbok.markdown.widgets.assets-test
  (:require
   [clojure.test :refer [deftest testing is]]
   [clojure.string :as str]
   [gitbok.markdown.widgets.assets :as assets]))

(deftest test-hack-assets-basic
  (testing "Basic asset-path tag replacement with double quotes"
    (let [content "{% asset-path \"image.png\" %}"
          filepath "api.md"
          result (assets/hack-assets filepath content)]
      (is (= "./.gitbook/assets/image.png" result))))

  (testing "Basic asset-path tag replacement with single quotes"
    (let [content "{% asset-path 'image.png' %}"
          filepath "api.md"
          result (assets/hack-assets filepath content)]
      (is (= "./.gitbook/assets/image.png" result))))

  (testing "Asset-path tag in HTML img tag"
    (let [content "<img src=\"{% asset-path \"docker.png\" %}\" alt=\"docker\">"
          filepath "api.md"
          result (assets/hack-assets filepath content)]
      (is (= "<img src=\"./.gitbook/assets/docker.png\" alt=\"docker\">" result))))

  (testing "Asset-path tag in markdown image"
    (let [content "![alt]({% asset-path \"image.png\" %})"
          filepath "api.md"
          result (assets/hack-assets filepath content)]
      (is (= "![alt](./.gitbook/assets/image.png)" result))))

  (testing "Multiple asset-path tags"
    (let [content "First: {% asset-path \"img1.png\" %} and second: {% asset-path \"img2.png\" %}"
          filepath "api.md"
          result (assets/hack-assets filepath content)]
      (is (= "First: ./.gitbook/assets/img1.png and second: ./.gitbook/assets/img2.png" result)))))

(deftest test-hack-assets-different-depths
  (testing "File at root level (api.md)"
    (let [content "{% asset-path \"image.png\" %}"
          filepath "api.md"
          result (assets/hack-assets filepath content)]
      (is (= "./.gitbook/assets/image.png" result))))

  (testing "File one level deep (getting-started/run.md)"
    (let [content "{% asset-path \"image.png\" %}"
          filepath "getting-started/run.md"
          result (assets/hack-assets filepath content)]
      (is (= "../.gitbook/assets/image.png" result))))

  (testing "File two levels deep (getting-started/installation/guide.md)"
    (let [content "{% asset-path \"image.png\" %}"
          filepath "getting-started/installation/guide.md"
          result (assets/hack-assets filepath content)]
      (is (= "../../.gitbook/assets/image.png" result))))

  (testing "File three levels deep (docs/api/reference/endpoints.md normalizes to api/reference/endpoints.md)"
    (let [content "{% asset-path \"image.png\" %}"
          filepath "docs/api/reference/endpoints.md"
          result (assets/hack-assets filepath content)]
      (is (= "../../.gitbook/assets/image.png" result))))

  (testing "File with docs/ prefix (docs/getting-started/run.md normalizes to getting-started/run.md)"
    (let [content "{% asset-path \"image.png\" %}"
          filepath "docs/getting-started/run.md"
          result (assets/hack-assets filepath content)]
      (is (= "../.gitbook/assets/image.png" result))))

  (testing "File with ./docs/ prefix"
    (let [content "{% asset-path \"image.png\" %}"
          filepath "./docs/getting-started/run.md"
          result (assets/hack-assets filepath content)]
      (is (= "../.gitbook/assets/image.png" result))))

  (testing "File with docs-new/auditbox/docs/ prefix (normalizes to getting-started/run-with-oneliner.md)"
    (let [content "{% asset-path \"docker.png\" %}"
          filepath "docs-new/auditbox/docs/getting-started/run-with-oneliner.md"
          result (assets/hack-assets filepath content)]
      (is (= "../.gitbook/assets/docker.png" result)))))

(deftest test-hack-assets-edge-cases
  (testing "Empty filepath returns content unchanged"
    (let [content "{% asset-path \"image.png\" %}"
          filepath ""
          result (assets/hack-assets filepath content)]
      (is (= content result))))

  (testing "Blank filepath returns content unchanged"
    (let [content "{% asset-path \"image.png\" %}"
          filepath "   "
          result (assets/hack-assets filepath content)]
      (is (= content result))))

  (testing "Content without asset-path tags remains unchanged"
    (let [content "This is just regular text with no widgets."
          filepath "api.md"
          result (assets/hack-assets filepath content)]
      (is (= content result))))

  (testing "Asset-path tag with spaces around it"
    (let [content "Text {% asset-path \"image.png\" %} more text"
          filepath "api.md"
          result (assets/hack-assets filepath content)]
      (is (= "Text ./.gitbook/assets/image.png more text" result))))

  (testing "Asset-path tag with extra whitespace"
    (let [content "{% asset-path   \"image.png\"   %}"
          filepath "api.md"
          result (assets/hack-assets filepath content)]
      (is (= "./.gitbook/assets/image.png" result))))

  (testing "Asset-path tag with filename containing spaces"
    (let [content "{% asset-path \"my image.png\" %}"
          filepath "api.md"
          result (assets/hack-assets filepath content)]
      (is (= "./.gitbook/assets/my image.png" result))))

  (testing "Asset-path tag with subdirectory in filename"
    (let [content "{% asset-path \"icons/docker.png\" %}"
          filepath "api.md"
          result (assets/hack-assets filepath content)]
      (is (= "./.gitbook/assets/icons/docker.png" result)))))

(deftest test-hack-assets-real-world-scenarios
  (testing "Complete HTML block with asset-path"
    (let [content "{% hint style=\"warning\" %}
<img src=\"{% asset-path \"docker.png\" %}\" 
     alt=\"docker whale image\" 
     data-size=\"original\">
{% endhint %}"
          filepath "docs-new/auditbox/docs/getting-started/run-with-oneliner.md"
          result (assets/hack-assets filepath content)]
      (is (str/includes? result "../.gitbook/assets/docker.png"))
      (is (str/includes? result "alt=\"docker whale image\""))
      (is (not (str/includes? result "{% asset-path")))))

  (testing "Multiple images in different formats"
    (let [content "<img src=\"{% asset-path \"img1.png\" %}\">
![alt2]({% asset-path \"img2.png\" %})
<img src=\"{% asset-path 'img3.png' %}\">"
          filepath "getting-started/guide.md"
          result (assets/hack-assets filepath content)]
      (is (str/includes? result "../.gitbook/assets/img1.png"))
      (is (str/includes? result "../.gitbook/assets/img2.png"))
      (is (str/includes? result "../.gitbook/assets/img3.png"))
      (is (not (str/includes? result "{% asset-path")))))

  (testing "Asset-path in markdown link"
    (let [content "[Download]({% asset-path \"file.pdf\" %})"
          filepath "api.md"
          result (assets/hack-assets filepath content)]
      (is (= "[Download](./.gitbook/assets/file.pdf)" result)))))

