---
description: >-
  Translate code from one value set to another, based on the existing value set
  and concept maps resources, and/or other additional knowledge available to the
  server.
---

# $translate on ConceptMap (deprecated)

{% hint style="info" %}
All the examples can be found at the `"FHIR ConceptMap Resource and translation"`community notebook which is accessible via your Aidbox Notebooks UI Tab
{% endhint %}

### Parameters and usage

<table><thead><tr><th width="174.33333333333331">Parameter</th><th>Type</th><th>Description</th></tr></thead><tbody><tr><td><code>code</code></td><td>required</td><td>The code that is to be translated</td></tr><tr><td><code>system</code></td><td>required</td><td>The system for the code that is to be translated</td></tr><tr><td><code>url</code></td><td>optional</td><td><p>A canonical URL for a concept map.</p><p>Ignored when ConceptMap id is provided</p></td></tr><tr><td><code>reverse</code></td><td>optional</td><td>If true, the <code>translate</code> should return all the codes that might be mapped to the given code</td></tr></tbody></table>

#### Example request

{% tabs %}
{% tab title="Request" %}
```http
GET /fhir/ConceptMap/cm1/$translate?code=fetus-speciment&system=http://snomed&reverse=true
```
{% endtab %}

{% tab title="Response" %}
```yaml
status: 200
body:
  resourceType: Parameters
  parameter:
  - name: result
    valueBoolean: true
  - name: match
    part:
    - name: equivalence
      valueCode: equivalent
    - name: concept
      valueCoding:
        system: http://hl7
        code: fetus-scalp
        userSelected: false
    - name: source
      valueUri: http://cm1
    - name: product
      part:
      - name: element
        valueUri: http://snomed/scalp-structure
      - name: concept
        valueCoding:
          system: http://snomed
          code: scalp-structure
          userSelected: false
```
{% endtab %}
{% endtabs %}

### Ungrouping ConceptMaps on create

ConceptMaps are ungrouped on create to have an opportunity to use Search API instead of $tranalate operation. ConceptMaps are transformed into ConceptMapRule resources.

#### ConceptMapRule resource schema:

* `element` as in ConceptMap
* `source`: source system where concepts to be mapped are defined
* `target`: target system that the concepts are to be mapped to
* `sourceValueSet`: the source value set that contains the concepts that are being mapped
* `targetValueSet`: the target value set which provides context for the mappings
* `conceptMapId`: id of the original ConceptMap
* `conceptmapUrl`: url of the original ConceptMap
* `unmapped`: as in ConceptMap

#### Aidbox Search API for ConceptMapRule

{% tabs %}
{% tab title="Request" %}
```http
GET /fhir/ConceptMapRule?.source=http://hl7.org/fhir/address-use&.target=http://terminology.hl7.org/CodeSystem/v3-AddressUse&.element.code=home&_elements=.element.target
```
{% endtab %}

{% tab title="Response" %}
```yaml
status: 200
body:
  resourceType: Bundle
  type: searchset
  meta:
    versionId: '70'
  total: 1
  link:
  - relation: first
    url: "/fhir/ConceptMapRule?.source=http://hl7.org/fhir/address-use&.target=http://terminology.hl7.org/CodeSystem/v3-AddressUse&.element.code=home&_elements=.element.target&page=1"
  - relation: self
    url: "/fhir/ConceptMapRule?.source=http://hl7.org/fhir/address-use&.target=http://terminology.hl7.org/CodeSystem/v3-AddressUse&.element.code=home&_elements=.element.target&page=1"
  entry:
  - resource:
      element:
        target:
        - code: H
          display: home
          equivalence: equivalent
      id: 7378326f-d142-43d5-8ef4-97cdad65d159
      resourceType: ConceptMapRule
```
{% endtab %}
{% endtabs %}

Created ConceptMap does not have `group` property as it is just a meta header resource.

### Convert ConceptMap.json to ndjson bundle

In order to convert a huge ConceptMap to ndjson bundle use this [FHIR converter](https://github.com/zen-lang/fhir).

```bash
java -jar [JAR_PATH] cmndj -i PATH/TO/CONCEPT_MAP.json -o PATH/TO/OUTPUT_BUNDLE.ndjson
```

The output bundle consists of ConceptMap meta resource and the set of ConceptMapRule resources.

Use [Bulk API ](../../../../api/bulk-api/)to upload output bundle.

### How to get full ConceptMap resource

Aidbox doesn't store ConceptMap groups in ConceptMap resource as mentioned above.

To get full ConceptMap resource make a request using `/fhir` prefix.

Example:

```http
GET /fhir/ConceptMap/my-concept-map
```
