---
description: The article explains, how Aidbox stores authorization details
---

# Storing authorization details

{% hint style="info" %}
This article explains details about storing authorization information related to [SMART of FHIR app launch](../../../security-and-access-control-1/how-to-guides/smart-on-fhir/smart-on-fhir-app-launch.md) only
{% endhint %}

### `session` rsource

During the SoF authorization process Aidbox creates `session` resource. All details related to the authorization are stored to the `session`.

For example, there could be found following keys in the session:

* `access_token` is an opaque string. It is used to 'sign' requests to Aidbox
* `refresh_token` is an opaque string. It is used by `Client` (application) to get new acess tokens when it is staled
* `client` reference to the `Client` is granted access to
* `scope` is the array of scopes authorized by the resource owner
