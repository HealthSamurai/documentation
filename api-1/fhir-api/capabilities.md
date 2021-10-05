---
description: CapabilityStatement of an Aidbox instance
---

# capabilities

## `GET /metadata`

Using `GET /metadata` you can access [FHIR capabilities](https://www.hl7.org/fhir/http.html#capabilities) interaction.

Response is a `CapabilityStatement` generated from _meta resources_ created in an Aidbox instance. The list of this _meta resource_ types used for the response generation:

* `Operation`
* `SearchParmeter`
* `Entity`

{% hint style="info" %}
Aidbox `CapabilityStatement` updates automatically after _meta resources_ change. No interaction needed to apply changes to `CapabilityStatement`.
{% endhint %}

