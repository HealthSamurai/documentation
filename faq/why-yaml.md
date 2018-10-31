---
description: 'compact, human-readable, good'
---

# Why YAML?

Aidbox supports following content-types, which not presented in fhir spec yet:

* application/fhir+yaml
* appliaction/yaml
* text/yaml

yaml subset isomorphic to json \(can be converted from one format to another and vice versa without any additional meta-information\) and adding yaml support was very cheap.

Also, yaml less verbose and therefore more human-readable. In additional to compactness and readability yaml is used in many huge opensource projects like kubernetes and also, pretty popular among web developers.

Here example of the same resource in both formats:

application/json:

```javascript
{
  "resource": {
    "name": [
      {
        "given": [
          "Peter"
        ]
      }
    ],
    "id": "1882cce7-dd00-4c9d-a1f5-adc5f6747955",
    "resourceType": "Patient",
    "meta": {
      "lastUpdated": "2018-09-19T17:44:01.252Z",
      "versionId": "167",
      "tag": [
        {
          "system": "https://aidbox.io",
          "code": "created"
        }
      ]
    }
  }
}
```

application/yaml:

```yaml
resource:
  name:
  - given:
    - Peter
  id: 1882cce7-dd00-4c9d-a1f5-adc5f6747955
  resourceType: Patient
  meta:
    lastUpdated: '2018-09-19T17:44:01.252Z'
    versionId: '167'
    tag:
    - system: https://aidbox.io
      code: created

```

To play with both formats visit [https://www.json2yaml.com/](https://www.json2yaml.com/)

