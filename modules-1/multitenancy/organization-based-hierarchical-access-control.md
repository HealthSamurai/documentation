---
description: Achieve logical multi-tenancy with Aidbox
---

# Organization-based hierarchical access control

Hierarchical organization-based access control in Aidbox allows for the restriction of access to data based on the organization to which it belongs. When this feature is enabled, the FHIR Organization resource in Aidbox gains new semantics and functionality.

This means that when users interact with the Organizational FHIR API, they are only able to access the resources that belong to their organization or tenant. The hierarchical organization-based access control ensures that data is logically isolated and accessible only within the appropriate organizational context.

## Problem

FHIR resources must be separated per organizations. Organizations can be nested. Every organization has access to their own resources and to the nested organization resources.

## Solution

Let's consider the next organization structure. There are two independent organizations Org A & Org D, each of them has nested, dependent organizations. Org B & Org C are nested to Org A, and Org E is nested to Org D.&#x20;

<figure><img src="../../.gitbook/assets/Screenshot 2023-06-28 at 16.40.54.png" alt=""><figcaption><p>Organization hierarchy structure</p></figcaption></figure>

To achieve such a behavior, you may consider an Aidbox feature called organization-based access control.&#x20;

Let's create the organization structure in Aidbox:

{% code title="status: 200 OK" %}
```
PUT /
content-type: text/yaml
accept: text/yaml

- id: org-a
  resourceType: Organization
- id: org-b
  resourceType: Organization
  partOf: {resourceType: Organization, id: org-a}
- id: org-c
  resourceType: Organization
  partOf: {resourceType: Organization, id: org-a}
- id: org-d
  resourceType: Organization
- id: org-e
  resourceType: Organization
  partOf: {resourceType: Organization, id: org-d}
```
{% endcode %}

When an Organization resource is created, a dedicated FHIR API is deployed for that organization. This API provides access to the associated FHIR resources.  Nested organization FHIR resources are accessible through the parent Organization API.

The Organization-based FHIR API base url:

```
<AIDBOX_BASE_URL>/Organization/<org-id>/fhir
```

<figure><img src="../../.gitbook/assets/Screenshot 2023-06-28 at 15.42.54.png" alt=""><figcaption><p>FHIR APIs reflection in organization-based access control</p></figcaption></figure>

Let's play with a new FHIR APIs.

We will create a Patient resource in Org B:

{% code title="status: 201 Created" %}
```yaml
PUT /Organization/org-b/fhir/Patient/pt-1
content-type: text/yaml
accept: text/yaml

name: [{given: [John], family: Smith}]
gender: male
```
{% endcode %}

Now we can read it:

{% code title="status: 200 OK" %}
```yaml
GET /Organization/org-b/fhir/Patient/pt-1
```
{% endcode %}

The resource is also accessible through Org A FHIR API:

{% code title="status: 200 OK" %}
```yaml
GET /Organization/org-a/fhir/Patient/pt-1
```
{% endcode %}

But this resource is not accessible through Org C, Org D and Org E FHIR API:

{% code title="status: 403 Forbidden" %}
```yaml
GET /Organization/org-c/fhir/Patient/pt-1
```
{% endcode %}

## FHIR API over Organization resources

```
<AIDBOX_BASE_URL>/Organization/<org-id>/fhir
```

### Create

```
POST <AIDBOX_BASE_URL>/Organization/<org-id>/fhir/<resource-type>
```

### Read&#x20;

```
GET <AIDBOX_BASE_URL>/Organization/<org-id>/fhir/<resource-type>/<id>
```

### Update

```
PUT <AIDBOX_BASE_URL>/Organization/<org-id>/fhir/<resource-type>/<id>
```

```
PATCH <AIDBOX_BASE_URL>/Organization/<org-id>/fhir/<resource-type>/<id>
```

### Delete

```
DELETE <AIDBOX_BASE_URL>/Organization/<org-id>/fhir/<resource-type>/<id>
```

### Search

```
GET <AIDBOX_BASE_URL>/Organization/<org-id>/fhir/<resource-type>
```

{% hint style="warning" %}
The search API does not support search parameters:

* `_has`
* `_assoc`
* `_with`
{% endhint %}

### Bundle

```
POST /Organization/org-b/fhir/
Accept: text/yaml
Content-Type: text/yaml

resourceType: Bundle
type: transaction
entry:
- request:
    method: POST
    url: 'Patient'
  resource:
    birthDate: '2021-01-01'
    id: 'pt-1'
    meta:
      organization:
        id: 'org-c'
        resourceType: 'Organization'
- request:
    method: POST
    url: 'Patient'
  resource:
    birthDate: '2021-01-01'
    id: 'pt-2'
```

{% hint style="warning" %}
Supported only bundle type `transaction`.
{% endhint %}

### Metadata

```
GET <AIDBOX_BASE_URL>/Organization/<org-id>/fhir/metadata
```

## Shared resource mode

By default, nested API has ho access to a resource that is belonged to the upper organizations. Sometimes it is necessary to have resources that can be accessed by the nested APIs. To achive it the resource should be marked as `share`.&#x20;

### Create a shared resource

To create a shared resource, use the `https://aidbox.app/tenant-resource-mode` extension.

{% code title="status: 201 Created" %}
```yaml
PUT /Organization/org-a/fhir/Practitioner/prac-1
content-type: text/yaml

meta:
  extension:
  - url: https://aidbox.app/tenant-resource-mode
    valueString: "shared"
```
{% endcode %}

### Access shared resource from a nested AP

{% code title="status: 200 OK" %}
```yaml
GET /Organization/org-b/fhir/Practitioner/prac-1
```
{% endcode %}
