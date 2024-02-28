---
description: This pages explains how security labels access control control works in Aidbox
---

# Security Labels

## What are Security Labels?

Security Labels is a set of permissions associated with the request. When security labels are present in the request context, the requester is allowed to gather information according to the security labels.

Two security label code systems are currently supported:

1. http://terminology.hl7.org/CodeSystem/v3-Confidentiality
2. http://terminology.hl7.org/CodeSystem/v3-ActCode

## Security Labels in the request context

There are two ways of the security labels appear to the request context:

1. JWT’s `scope` claim
2. Aidbox User’s property `securityLabel`

### scope claim in JWT <a href="#docs-internal-guid-71b74bb2-7fff-d9f8-f70b-bfac58a2d392" id="docs-internal-guid-71b74bb2-7fff-d9f8-f70b-bfac58a2d392"></a>

Aidbox parses the `scope` claim and fetches security labels. There can be multiple security labels on the scope.

A security label must be defined using the pattern `system|code`. For example, `http://terminology.hl7.org/CodeSystem/v3-ActCode|PSY.`

### User.securityLabel

If the request context is associated with a Aidbox user, Aidbox tries to get security labels from the `User.securityLabel`.&#x20;

For example, the user resource containing two security labels.

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

For instance, the R code expands to R, N, M, L, and U.

## How access control works

Security Labels access control is done in two steps:

1. Resource-level access control
2. Resource-element level access (masking)

### Resource-level access control

If the security labels of the request context intersect with the security labels of the resource, the requester can access the resource. Otherwise, no access.

If a resource has no security labels, no one can access the resource.

| Resource security labels            | Request security labels               | Accessibility                                  |
| ----------------------------------- | ------------------------------------- | ---------------------------------------------- |
| Confidentiality: V                  | Confidentiality: R                    | <mark style="color:orange;">`no access`</mark> |
| Confidentiality: R                  | Confidentiality: R                    | <mark style="color:green;">`available`</mark>  |
| Confidentiality: L                  | Confidentiality: R                    | <mark style="color:green;">`available`</mark>  |
| Confidentiality: R Sensitivity: PSY | Confidentiality: R                    | <mark style="color:green;">`available`</mark>  |
| Sensitivity: PSY                    | Confidentiality: R                    | <mark style="color:orange;">`no access`</mark> |
| Sensitivity: HIV                    | Confidentiality: R                    | <mark style="color:orange;">`no access`</mark> |
| _no security labels_                | Confidentiality: R                    | <mark style="color:orange;">`no access`</mark> |
| Confidentiality: V                  | Confidentiality: R   Sensitivity: PSY | <mark style="color:orange;">`no access`</mark> |
| Confidentiality: R                  | Confidentiality: R   Sensitivity: PSY | <mark style="color:green;">`available`</mark>  |
| Confidentiality: L                  | Confidentiality: R   Sensitivity: PSY | <mark style="color:green;">`available`</mark>  |
| Confidentiality: R Sensitivity: PSY | Confidentiality: R   Sensitivity: PSY | <mark style="color:green;">`available`</mark>  |
| Sensitivity: PSY                    | Confidentiality: R   Sensitivity: PSY | <mark style="color:green;">`available`</mark>  |
| Sensitivity: HIV                    | Confidentiality: R   Sensitivity: PSY | <mark style="color:orange;">`no access`</mark> |
| _no security labels_                | Confidentiality: R   Sensitivity: PSY | <mark style="color:orange;">`no access`</mark> |

