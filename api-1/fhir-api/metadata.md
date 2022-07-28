---
description: >-
  The CapabilityStatement resource is a statement of the kinds of resources and
  operations provided and/or consumed by an application.
---

# Capability Statement

## Overview

Using `GET /metadata` you can access [FHIR capabilities](https://www.hl7.org/fhir/http.html#capabilities) interaction.

Response is a `CapabilityStatement` generated from _meta resources_ created in an Aidbox instance. The list of this _meta resource_ types used for the response generation:

* `Operation`
* `SearchParameter`
* `Entity`

{% hint style="info" %}
Aidbox `CapabilityStatement` updates automatically after _meta resources_ change. No interaction needed to apply changes to `CapabilityStatement`.
{% endhint %}

Read more about [conformance rules](http://build.fhir.org/conformance-rules.html) and the [CapabilityStatement resource](http://build.fhir.org/capabilitystatement.html) itself.

Base URL for FHIR compatible API is `<server-domain>/fhir/`.

The `capabilities` interaction returns a capability statement describing the server's current operational functionality. The interaction is performed by the HTTP `GET` command as shown:

```javascript
GET [base]/metadata{?mode=[mode]}{&_format=[mime-type]}
```

You can get [Capability Statement](https://www.hl7.org/fhir/capabilitystatement.html) of your Aidbox with the command:

```javascript
GET <server-domain>/fhir/metadata
```

{% swagger baseUrl="<your-domain>/fhir/metadata" path="" method="get" summary=" Get metadata" %}
{% swagger-description %}

{% endswagger-description %}

{% swagger-parameter in="path" name="mode" type="string" %}
full | normative | terminology
{% endswagger-parameter %}

{% swagger-parameter in="path" name="_format" type="string" %}
json | yaml | edn | xml
{% endswagger-parameter %}

{% swagger-response status="200" description="" %}
```yaml
patchFormat: [json-patch, merge-patch]
rest:
- mode: server
  interaction:
  - {code: batch}
  - {code: transaction}
  operation:
  - {name: patient_list, deinition: Operation/patient_list}
  ....
  resource:
  - interaction:
    - {code: read}
    - {code: vread}
    - {code: update}
    - {code: patch}
    - {code: delete}
    - {code: history-instance}
    - {code: history-type}
    - {code: create}
    - {code: search-type}
    readHistory: true
    updateCreate: true
    versioning: versioned-update
    searchRevInclude: [all]
    referencePolicy: local
    conditionalRead: full-support
    searchInclude: [all]
    type: Patient
    conditionalDelete: single
    conditionalUpdate: true
    conditionalCreate: true
    searchParam:
    - {name: address-city, definition: /SearchParameter/Patient.address-city, type: string}
    - {name: active, definition: /SearchParameter/Patient.active, type: token}
    - {name: given, definition: /SearchParameter/Patient.given, type: string}
    - {name: address-state, definition: /SearchParameter/Patient.address-state, type: string}
    - {name: general-practitioner, definition: /SearchParameter/Patient.general-practitioner,
      type: reference}
    - {name: gender, definition: /SearchParameter/Patient.gender, type: token}
    - {name: name, definition: /SearchParameter/Patient.name, type: string}
    - {name: family, definition: /SearchParameter/Patient.family, type: string}
    - {name: telecom, definition: /SearchParameter/Patient.telecom, type: token}
    - {name: animal-species, definition: /SearchParameter/Patient.animal-species,
      type: token}
    - {name: identifier, definition: /SearchParameter/Patient.identifier, type: token}
    - {name: link, definition: /SearchParameter/Patient.link, type: reference}
    - {name: address-use, definition: /SearchParameter/Patient.address-use, type: token}
    - {name: language, definition: /SearchParameter/Patient.language, type: token}
    - {name: phonetic, definition: /SearchParameter/Patient.phonetic, type: string}
    - {name: death-date, definition: /SearchParameter/Patient.death-date, type: date}
    - {name: animal-breed, definition: /SearchParameter/Patient.animal-breed, type: token}
    - {name: deceased, definition: /SearchParameter/Patient.deceased, type: token}
    - {name: address, definition: /SearchParameter/Patient.address, type: string}
    - {name: email, definition: /SearchParameter/Patient.email, type: token}
    - {name: organization, definition: /SearchParameter/Patient.organization, type: reference}
    - {name: birthdate, definition: /SearchParameter/Patient.birthdate, type: date}
    - {name: address-country, definition: /SearchParameter/Patient.address-country,
      type: string}
    - {name: phone, definition: /SearchParameter/Patient.phone, type: token}
    - {name: address-postalcode, definition: /SearchParameter/Patient.address-postalcode,
      type: string}
```
{% endswagger-response %}
{% endswagger %}

The information returned depends on the value of the `mode` parameter:

| `Value`                      | Description                                                                                                                                                                      |
| ---------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `full` (or mode not present) | A [Capability Statement](http://hl7.org/fhir/2018Sep/capabilitystatement.html) that specifies which resource types and interactions are supported                                |
| `normative`                  | As above, but only the normative portions of the Capability Statement                                                                                                            |
| `terminology`                | A [TerminologyCapabilities](http://hl7.org/fhir/2018Sep/terminologycapabilities.html) resource that provides further information about terminologies are supported by the server |

Servers MAY ignore the mode parameter and return a CapabilityStatement resource. &#x20;

#### Configure CapabilityStatement

You can reconfigure specific parts of CapabilityStatement by creating `AidboxConfig/box` resource:

```yaml
PUT /AidboxConfig/box

metadata:
  # override name and title of CapabilityStatement
  name: MyFHIRServer
  title: My FHIR server
  description: Description of my FHIR server
  # override CapabilityStatement.rest.service
  service:
  - coding:
    - {system: 'http://hl7.org/fhir/restful-security-service', code: SMART-on-FHIR}
    text: Very smart!!!
  # override CapabilityStatement.rest.security
  security:
    extension:
    - url: http://fhir-registry.smarthealthit.org/StructureDefinition/oauth-uris
      extension:
      - {url: token, valueUri: 'https://myserver.com/connect/token'}
      - {url: authorize, valueUri: 'https://myserver.com/connect/token'}
```

{% hint style="info" %}
If you want more control over CapabilityStatement, please contact us in the community chat!
{% endhint %}

### Notes

#### rest\[].resource\[].profile

The profile property is set when zen profiling is enabled and the following conditions are met:&#x20;

* the corresponding resource is defined in zen profile&#x20;
* there is exactly one zen schema tagged with `base-profile` for the corresponding resource

## Aidbox native metadata

To get metadata in the internal Aidbox format, use [`/$metadata?_format=yaml`](http://localhost:7777/$metadata?\_format=yaml)

{% swagger baseUrl="<your-domain>/$metadata" path="" method="get" summary="Get Aidbox native metadata" %}
{% swagger-description %}

{% endswagger-description %}

{% swagger-parameter in="path" name="_format" type="string" %}
yaml | json | edn
{% endswagger-parameter %}

{% swagger-parameter in="query" name="path" type="string" %}
Path to specific part of metadata (for example Entity.Patient)
{% endswagger-parameter %}

{% swagger-response status="200" description="" %}
```
```
{% endswagger-response %}
{% endswagger %}

&#x20;Capability statements can become quite large; servers are encouraged to support the [`_summary`](http://hl7.org/fhir/2018Sep/search.html#summary) and [`_elements`](http://hl7.org/fhir/2018Sep/search.html#elements) parameters on the capabilities interaction, though this is not required. In addition, servers are encouraged to implement the [$subset](http://hl7.org/fhir/2018Sep/capabilitystatement-operation-subset.html) and [$implements](http://hl7.org/fhir/2018Sep/capabilitystatement-operation-implements.html) operations to make it easy for a client to check conformance.
