(ns fhir-schema-doc
  (:require cheshire.core))

(def supported-implementation-guide-destination
  "docs/modules/profiling-and-validation/fhir-schema-validator/aidbox-fhir-igs-registry.md")

(def supported-implementation-guide-template "---
description: >-
  List of supported Implementation Guides, which synchronise automatically every
  day at 00:00 UTC
---

# Supported FHIR Implementation Guides (IGs)

## Configure Aidbox

To begin using FHIR IGs, enable the FHIR Schema validator engine in Aidbox.

{% content-ref url=\"setup.md\" %}
[setup.md](setup.md)
{% endcontent-ref %}

**Example configuration**

{% code title=\".env\" %}
```bash
AIDBOX_FHIR_SCHEMA_VALIDATION=true
AIDBOX_FHIR_PACKAGES=hl7.fhir.us.core#5.0.1:hl7.fhir.us.mcode#3.0.0
AIDBOX_TERMINOLOGY_SERVICE_BASE_URL=https://tx.fhir.org/r4
```
{% endcode %}

## Supported FHIR Implementation Guides (IGs) Packages

Here is the complete list of supported and ready-to-use Implementation Guides (IGs) for the new validator engine. We use [packages2.fhir.org](http://packages2.fhir.org/) as the source of truth for implementation guides and synchronise them daily at 00:00 UTC. The package includes the following resources: FHIR NPM Package manifest, FHIR Schemas, StructureDefinitions (for introspection purposes only), SearchParameters, CompartmentDefinitions, and ValueSet resources (for introspection purposes only). This documentation page is also updated automatically.

{{list-of-packages}}
")

(def gcp-bucket-url "https://storage.googleapis.com/fhir-schema-registry")
(def gcp-bucket-version "1.0.0")
(def gcp-buckit-api-url (str "https://storage.googleapis.com/storage/v1/b/fhir-schema-registry/o?maxResults=10000&delimiter=/" gcp-bucket-version))
(def package-manifest-file-name "package.ndjson.gz")

(defn get-package-meta!
  [^String url]
  (try
    (with-open [input-stream (clojure.java.io/input-stream url)]
      (->
       (java.util.zip.GZIPInputStream. input-stream)
       (clojure.java.io/reader)
       (.readLine)
       (cheshire.core/parse-string keyword)))
    (catch Exception e (println (format "Unexist package: %s" url)))))

(defn get-registry-main-packages
  []
  (let [logging  (agent nil)
        response (-> gcp-buckit-api-url slurp cheshire.core/parse-string)]
    (->>
     (get response "items")
     (filter #(clojure.string/ends-with? (get % "name") package-manifest-file-name))
     (pmap #(do
              (send logging (partial println (get % "name")))
              (get-package-meta! (get % "mediaLink"))))
     (doall))))

(defn build-package-section
  [manifests]
  (let [manifests (reverse (sort-by (fn [x]
                                      (mapv parse-long
                                            (clojure.string/split (:version x)
                                                                  (re-pattern "\\."))))
                                    manifests))]
    (format "---

### %s

**Versions**

%s
"
            (or (some :title manifests)
                (some :name manifests))
            (->>
             manifests
             (mapv #(str "* `" (:name %) "#" (:version %) "`\n"))
             (apply str)))))

(defn build-template
  [manifests]
  (let [list-of-packages
        (->>
         (group-by :name manifests)
         (vals)
         (mapv build-package-section)
         (clojure.string/join "\n"))]
    (spit supported-implementation-guide-destination
          (clojure.string/replace
           supported-implementation-guide-template
           "{{list-of-packages}}"
           list-of-packages))))

(defn -main
  []
  (-> (get-registry-main-packages)
      (build-template)))

(comment
  (-main))
