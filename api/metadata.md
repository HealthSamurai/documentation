---
description: FHIR Capability Statement Endpoint
---

# Capabilities Statement

**Base URL** for FHIR compatible API is   `<server-domain>/fhir/`

The `capabilities` interaction retrieves the server's Capability Statement that defines how it supports resources. The interaction is performed by an HTTP `GET` command as shown:

```text
  GET [base]/metadata {?_format=[mime-type]}
```

You can get [Capability Statement](https://www.hl7.org/fhir/capabilitystatement.html)  of your aidbox with `GET <server-domain>/fhir/metadata`

{% api-method method="get" host="<your-domain>/fhir/metadata" path="" %}
{% api-method-summary %}
 get metadata
{% endapi-method-summary %}

{% api-method-description %}

{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-path-parameters %}
{% api-method-parameter name="\_format" type="string" required=false %}
json \| yaml \| edn
{% endapi-method-parameter %}
{% endapi-method-path-parameters %}
{% endapi-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=200 %}
{% api-method-response-example-description %}

{% endapi-method-response-example-description %}

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
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

To get metadata in internal aidbox format use [`/$metadata?_format=yaml`](http://localhost:7777/$metadata?_format=yaml)

{% api-method method="get" host="<your-domain>/$metadata" path="" %}
{% api-method-summary %}
Get aidbox native metadata
{% endapi-method-summary %}

{% api-method-description %}

{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-path-parameters %}
{% api-method-parameter name="\_format" type="string" required=false %}
yaml \| json \| edn
{% endapi-method-parameter %}
{% endapi-method-path-parameters %}

{% api-method-query-parameters %}
{% api-method-parameter name="path" type="string" required=false %}
Path to specific part of metadata \(for example Entity.Patient\)
{% endapi-method-parameter %}
{% endapi-method-query-parameters %}
{% endapi-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=200 %}
{% api-method-response-example-description %}

{% endapi-method-response-example-description %}

```

```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

