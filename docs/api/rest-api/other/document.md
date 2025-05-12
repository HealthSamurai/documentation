---
description: Generates a Bundle document from Composition resource
---

# $document

Since the 2503 version, Aidbox supports FHIR [$document operation](https://www.hl7.org/fhir/composition-operation-document.html). $document is used to generate a Bundle document from existing Composition resource. It includes the Composition resource itself and some resources referenced in the Composition according to the [FHIR specification](https://www.hl7.org/fhir/documents.html#content).&#x20;

{% hint style="info" %}
$document operation uses several Search requests under the hood. These requests should be allowed by Access Policies. See [#setting-up-access-policies-for-usddocument-operation]()
{% endhint %}

### Example request

```
GET /fhir/Composition/<composition-id>/$document
```

### Example response

```
{
  "resourceType" : "Bundle",
  "type" : "document",
  "identifier" : {
    "system" : "urn:ietf:rfc:3986",
    "value" : "a9080314-eb76-4968-bbb6-425c96becf6c"
  },
  "entry" : [ {
    "fullUrl" : "https://someaidbox.com/fhir/Composition/c2",
    "resource" : "composition"
  }, {
    "fullUrl" : "https://someaidbox.com/fhir/Patient/pt1",
    "resource" : {
      "id" : "pt1",
      "resourceType" : "Patient"
      ...
    }
  }, {
    "fullUrl" : "https://someaidbox.com/fhir/Encounter/enc1",
    "resource" : {
      "id" : "enc1",
      "resourceType" : "Encounter"
      ...
    }
  }, {
    "fullUrl" : "https://someaidbox.com/fhir/Practitioner/pr1",
    "resource" : {
      "id" : "pr1",
      "resourceType" : "Practitioner"
      ...
    }
    // other resources
  }]
}
```

### Saving Bundle after $document operation

When `persist` parameter is provided, the generated bundle is saved in the database. The request

```
GET /fhir/Composition/<composition-id>/$document?persist=true
```

will return the Bundle containing `id` of the saved Bundle. The following request can retrieve it:

```
GET /fhir/Bundle/<bundle-id>
```

### Setting up Access Policies for $document operation

Every $document operation consists of several Search requests, that should be permitted by Access Policies. Aidbox takes all the required references (according to the [FHIR specification](https://www.hl7.org/fhir/documents.html#content), depending on FHIR version) from the Composition resource and performs Search requests for each found resource type.&#x20;

For example, the following Access Policy will permit the client with id=`my-client-id` to access all resources in R4:

```json
{
  "resourceType": "AccessPolicy",
  "id": "client-has-access-to-all-nested-resources",
  "engine": "matcho",
  "matcho": {
    "client": {
      "id": "my-client-id"
    },
    "uri": {
      "$one-of": [
        "/fhir/Practitioner",
        "/fhir/Encounter",
        "/fhir/Observation",
        "/fhir/Condition",
        "/fhir/Patient",
        "/fhir/Organization",
        "#/fhir/Composition.*"
      ]
    }
  }
}
```
