---
description: Control access with SMART scopes, Access Policies, RBAC, ABAC, patient compartments, and organization-based hierarchical access.
---

# Authorization

Aidbox supports different authorization mechanisms.

## Access Policies

An [Access Policy](access-policies.md) consists of a set of rules, where:

* All rules within a policy must evaluate to true for the policy to be granted
* Access is granted if at least one Access Policy evaluates to true
* Access is denied if no Access Policy evaluates to true

## SMART scopes

[SMART scopes](smart-on-fhir/) check that there are scopes in a request for a specific operation; if not, reject the request.\
For example, `GET /fhir/Observation` require `Observation.read` scope. Scopes can be taken from application grants.

## Scoped APIs

Scoped APIs are special APIs limited to resources within a specific compartment. Such API do not require complicated access control logic, because this logic is built into the API definition.

* [Compartments API](scoped-api/compartments-api.md) - API for resources within a certain [FHIR compartment](https://www.hl7.org/fhir/compartmentdefinition.html)
* [Patient Data API](scoped-api/patient-data-access-api.md) - API for patient-related resources that allows access to resources that belong to a specific patient
* [Organization-based hierarchical access control](scoped-api/organization-based-hierarchical-access-control.md) - API to control access to resources within a specific organization. Supports hierarchical access control

## Multibox

Multibox is a type of Aidbox distribution that isolates data to separate databases in the same PostgreSQL cluster and provides separate base URLs for each of them. Separate APIs with different base URLs are used for different tenants. Multibox allows hosting hundreds and thousands of Aidbox services in a single runtime.

{% hint style="success" %}
* Data isolation is guaranteed by the separation of databases and APIs.
* Each tenant has its own unique base URL.
* Each tenant can have a different configuration, e.g. different FHIR versions.
* Query performance of a tenant database is not affected by the data size of other tenants.
{% endhint %}

{% hint style="warning" %}
* Due to the database separation, it may be difficult to perform analytics, migrations, etc. across multiple tenants.
* Data can't be easily shared between tenants, e.g., terminologies, Practitioner, Organization resources etc.
{% endhint %}

See also:

{% content-ref url="../../tutorials/security-access-control-tutorials/run-multibox-locally.md" %}
[run-multibox-locally.md](../../tutorials/security-access-control-tutorials/run-multibox-locally.md)
{% endcontent-ref %}

## Security Labels framework

Security Labels framework is a mechanism to restrict access to bundles, resources, or resource elements based on security labels and permissions:

* Security labels must be assigned to resources during creation and updates
* Resources are filtered and elements are masked based on the security labels defined in the resource and the permissions associated with the request
* Enables implementation of:
  * Fine-grained access control at the resource and element level
  * Data segmentation based on sensitivity, purpose of use, etc.

## Role-Based Access Control

Aidbox does not have an explicit concept of roles. But you can use Access Policies to achieve role-based access control.

See tutorials:

{% content-ref url="../../tutorials/security-access-control-tutorials/rbac/rbac-with-jwt-containing-role.md" %}
[rbac-with-jwt-containing-role.md](../../tutorials/security-access-control-tutorials/rbac/rbac-with-jwt-containing-role.md)
{% endcontent-ref %}

{% content-ref url="../../tutorials/security-access-control-tutorials/rbac/flexible-rbac-built-in-to-aidbox.md" %}
[flexible-rbac-built-in-to-aidbox.md](../../tutorials/security-access-control-tutorials/rbac/flexible-rbac-built-in-to-aidbox.md)
{% endcontent-ref %}

## Attribute-Based Access Control

Attribute-Based Access Control (ABAC) is an access control model that evaluates multiple attributes to make authorization decisions. Unlike simpler models like Role-Based Access Control (RBAC), ABAC can consider a rich combination of:

1. **Subject Attributes** (Who is requesting?)
   * Role
   * Department
   * Specialty
   * Clearance level
   * Organization
2. **Resource Attributes** (What is being accessed?)
   * Resource type
   * Sensitivity level
   * Department ownership
   * Creation date
   * Tags/labels
3. **Action Attributes** (What operation?)
   * Read
   * Write
   * Delete
   * Search
4. **Context Attributes** (Under what circumstances?)
   * Time of access
   * Location
   * Device type
   * Emergency status

There is no full support for attribute-based access control in Aidbox. But some elements of it can be implemented using Access Policies and/or the Security Labels framework.

See tutorials:

* [Allow Patient to see its own data](../../tutorials/security-access-control-tutorials/allow-patients-to-see-their-own-data.md)
* [Relationship-based access control](../../tutorials/security-access-control-tutorials/relationship-based-access-control.md)

## Consent-Based Access Control

Consent-Based Access Control is a way to control access to resources based on the patient's consent.\
It can be implemented using FHIR Consent resource:

1. Store Consent resource.
2. Check if consent is granted for the operation using the Search API.

See the following tutorial:

{% content-ref url="../../tutorials/security-access-control-tutorials/how-to-implement-consent-based-access-control-using-fhir-search-and-aidbox-access-policy.md" %}
[how-to-implement-consent-based-access-control-using-fhir-search-and-aidbox-access-policy.md](../../tutorials/security-access-control-tutorials/how-to-implement-consent-based-access-control-using-fhir-search-and-aidbox-access-policy.md)
{% endcontent-ref %}

