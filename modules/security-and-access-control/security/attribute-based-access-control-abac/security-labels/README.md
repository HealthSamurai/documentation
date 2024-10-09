---
description: This pages explains how security labels access control works in Aidbox
---

# Label-based Access Control

## What are security labels?

A security label is a concept attached to a resource or bundle that provides specific security metadata about the information it is fixed to.

## What is Label-based Access Control?

Label-based Access Control engine provides a mechanism to restrict access to bundles, resources, or resource elements depending on permissions associated with a request. When security labels are included in the request context, they allow the requester to access information in accordance with those labels.

Two security label code systems are currently supported:

1. [http://terminology.hl7.org/CodeSystem/v3-Confidentiality](http://terminology.hl7.org/CodeSystem/v3-Confidentiality)
2. [http://terminology.hl7.org/CodeSystem/v3-ActCode](http://terminology.hl7.org/CodeSystem/v3-ActCode)

## Security Labels in the request context

There are two ways the security labels appear in the request context:

1. From the `scope` claim of a JWT.
2. From the Aidbox User’s property `securityLabel`.

### scope claim in JWT <a href="#docs-internal-guid-71b74bb2-7fff-d9f8-f70b-bfac58a2d392" id="docs-internal-guid-71b74bb2-7fff-d9f8-f70b-bfac58a2d392"></a>

Aidbox parses the `scope` claim and fetches security labels. There can be multiple security labels on the scope.

A security label must be defined using the pattern `system|code`. For example, `http://terminology.hl7.org/CodeSystem/v3-ActCode|PSY.`

### User.securityLabel

If the request context is associated with an Aidbox user, Aidbox tries to get security labels from the `User.securityLabel`.

For example, the user resource contains two security labels.

```yaml
resourceType: User
id: some-user-id
securityLabel:
  - system: http://terminology.hl7.org/CodeSystem/v3-ActCode
    code: PSY
  - system: https://terminology.hl7.org/CodeSystem/v3-Confidentiality
    code: M
```

### Expanding confidentiality security label

The security label for confidentiality is [hierarchical](https://terminology.hl7.org/ValueSet-v3-Confidentiality.html). The code may contain several others.

For example, the R code expands to R, N, M, L, and U.

## How access control works

Security Labels access control is done in two steps:

1. Resource-level access control. Decides whether a resource itself is accessible to a requester.
2. Resource-element level access (masking). Decides whether some elements of the resource should be hidden from the requester.\
   Masking is applied only if the resource-level access control permits access to the resource.

### Resource-level access control

If the security labels of the request context intersect with the security labels of the resource, the requester can access the resource. Otherwise, there is no access. Consider marking non-sensitive data with the security label U (unrestricted).&#x20;

{% hint style="warning" %}
If a resource has no security labels, no one can access the resource.
{% endhint %}

#### Resource accessibility matrix

| Resource security labels                      | Request security labels                       | Accessibility                                  |
| --------------------------------------------- | --------------------------------------------- | ---------------------------------------------- |
| Confidentiality: V                            | Confidentiality: R                            | <mark style="color:orange;">`no access`</mark> |
| Confidentiality: R                            | Confidentiality: R                            | <mark style="color:green;">`available`</mark>  |
| Confidentiality: L                            | Confidentiality: R                            | <mark style="color:green;">`available`</mark>  |
| <p>Confidentiality: R<br>Sensitivity: PSY</p> | Confidentiality: R                            | <mark style="color:green;">`available`</mark>  |
| Sensitivity: PSY                              | Confidentiality: R                            | <mark style="color:orange;">`no access`</mark> |
| Sensitivity: HIV                              | Confidentiality: R                            | <mark style="color:orange;">`no access`</mark> |
| _no security labels_                          | Confidentiality: R                            | <mark style="color:orange;">`no access`</mark> |
| Confidentiality: V                            | <p>Confidentiality: R<br>Sensitivity: PSY</p> | <mark style="color:orange;">`no access`</mark> |
| Confidentiality: R                            | <p>Confidentiality: R<br>Sensitivity: PSY</p> | <mark style="color:green;">`available`</mark>  |
| Confidentiality: L                            | <p>Confidentiality: R<br>Sensitivity: PSY</p> | <mark style="color:green;">`available`</mark>  |
| <p>Confidentiality: R<br>Sensitivity: PSY</p> | <p>Confidentiality: R<br>Sensitivity: PSY</p> | <mark style="color:green;">`available`</mark>  |
| Sensitivity: PSY                              | <p>Confidentiality: R<br>Sensitivity: PSY</p> | <mark style="color:green;">`available`</mark>  |
| Sensitivity: HIV                              | <p>Confidentiality: R<br>Sensitivity: PSY</p> | <mark style="color:orange;">`no access`</mark> |
| _no security labels_                          | <p>Confidentiality: R<br>Sensitivity: PSY</p> | <mark style="color:orange;">`no access`</mark> |
| Confidentiality: V                            | Sensitivity: PSY                              | <mark style="color:orange;">`no access`</mark> |
| Confidentiality: R                            | Sensitivity: PSY                              | <mark style="color:orange;">`no access`</mark> |
| Confidentiality: L                            | Sensitivity: PSY                              | <mark style="color:orange;">`no access`</mark> |
| <p>Confidentiality: R<br>Sensitivity: PSY</p> | Sensitivity: PSY                              | <mark style="color:green;">`available`</mark>  |
| Sensitivity: PSY                              | Sensitivity: PSY                              | <mark style="color:green;">`available`</mark>  |
| Sensitivity: HIV                              | Sensitivity: PSY                              | <mark style="color:orange;">`no access`</mark> |
| _no security labels_                          | Sensitivity: PSY                              | <mark style="color:orange;">`no access`</mark> |

### Resource-element level access (masking)

To perform masking:

1. The resource itself should have the `http://terminology.hl7.org/CodeSystem/v3-ActCode|PROCESSINLINELABEL` security label in its meta.
2. The resource properties should be tagged with the [Inline Security Label](http://hl7.org/fhir/uv/security-label-ds4p/STU1/StructureDefinition-extension-inline-sec-label.html) extension.

#### Masking examples

The requestor has access to all `Encounter` fields but the `subject`.

{% tabs %}
{% tab title="Resource" %}
```yaml
resourceType: Encounter
id: enc-1
meta:
  security:
    - code: PROCESSINLINELABEL
      system: http://terminology.hl7.org/CodeSystem/v3-ActCode
    - code: L
      system: http://terminology.hl7.org/CodeSystem/v3-Confidentiality
status: finished
class:
  system: http://terminology.hl7.org/CodeSystem/v3-ActCode
  code: IMP
subject:
  reference: "Patient/pt-1"
  extension:
    - url: http://hl7.org/fhir/uv/security-label-ds4p/StructureDefinition/extension-inline-sec-label
      valueCoding:
        code: CTCOMPT
        system: http://terminology.hl7.org/CodeSystem/v3-ActCode
        display: care teamcompartment
```
{% endtab %}

{% tab title="Request context" %}
```yaml
request_method: GET
uri: /fhir/Encounter/enc-1
security_labels:
  - system: http://terminology.hl7.org/CodeSystem/v3-Confidentiality
    code: R
    display: Restricted
  - system: http://terminology.hl7.org/CodeSystem/v3-ActCode
    code: FMCOMPT
    display: financial management compartment
```
{% endtab %}

{% tab title="Masking outcome" %}
```yaml
resourceType: Encounter
id: enc-1
meta:
  security:
    - code: PROCESSINLINELABEL
      system: http://terminology.hl7.org/CodeSystem/v3-ActCode
    - code: L
      system: http://terminology.hl7.org/CodeSystem/v3-Confidentiality
status: finished
class:
  system: http://terminology.hl7.org/CodeSystem/v3-ActCode
  code: IMP
subject:
  extension:
    - url: http://terminology.hl7.org/CodeSystem/data-absent-reason
      valueCode: masked
```
{% endtab %}
{% endtabs %}

### Remove security labels from the response

To prevent security labels from appearing in the outcome, set the `strip labels` env:

```yaml
BOX_FEATURES_SECURITY__LABELS_STRIP__LABELS=true
```

**Stripping examples**

The security labels from `meta.security` and `_status` fields have been removed from the outcome.

{% tabs %}
{% tab title="Resource" %}
```yaml
resourceType: Encounter
id: enc-1
meta:
  security:
    - code: PROCESSINLINELABEL
      system: http://terminology.hl7.org/CodeSystem/v3-ActCode
    - code: L
      system: http://terminology.hl7.org/CodeSystem/v3-Confidentiality
status: finished
_status:
  extension:
    - url: http://hl7.org/fhir/uv/security-label-ds4p/StructureDefinition/extension-inline-sec-label
      valueCoding:
        code: FMCOMPT
        system: http://terminology.hl7.org/CodeSystem/v3-ActCode
        display: financial management compartment
class:
  system: http://terminology.hl7.org/CodeSystem/v3-ActCode
  code: IMP
subject:
  reference: "Patient/pt-1"
  extension:
    - url: http://hl7.org/fhir/uv/security-label-ds4p/StructureDefinition/extension-inline-sec-label
      valueCoding:
        code: CTCOMPT
        system: http://terminology.hl7.org/CodeSystem/v3-ActCode
        display: care teamcompartment
```
{% endtab %}

{% tab title="Request context" %}
```
request_method: GET
uri: /fhir/Encounter/enc-1
security_labels:
  - system: http://terminology.hl7.org/CodeSystem/v3-Confidentiality
    code: R
    display: Restricted
  - system: http://terminology.hl7.org/CodeSystem/v3-ActCode
    code: FMCOMPT
    display: financial management compartment
```
{% endtab %}

{% tab title="Outcome " %}
```
resourceType: Encounter
id: enc-1
status: finished
class:
  system: http://terminology.hl7.org/CodeSystem/v3-ActCode
  code: IMP
subject:
  extension:
    - url: http://terminology.hl7.org/CodeSystem/data-absent-reason
      valueCode: masked
```
{% endtab %}
{% endtabs %}

## Superadmin Role with Label-based Access Control

As mentioned [earlier](./#resource-level-access-control), resources without security labels cannot be accessed. This can affect the functionality of the Aidbox UI console, making resources like User, Client, Access Policy, etc. inaccessible until they are labeled.\
\
To avoid the need to label all resources displayed in the UI console, use the `superadmin` Role.\
\
Create a `Role` resource with the name `superadmin` and reference to the User used to log in to the UI console before enabling Label-based Access Control.

```yaml
POST /Role
content-type: text/yaml
accept: text/yaml

name: superadmin
user:
  id: <user-id>
  resourceType: User
```
