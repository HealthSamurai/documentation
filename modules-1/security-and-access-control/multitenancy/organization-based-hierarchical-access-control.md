---
description: Achieve logical multi-tenancy with Aidbox
---

# Organization-based hierarchical access control

Hierarchical organization-based access control in Aidbox allows for the restriction of access to data based on the organization to which it belongs. When this feature is enabled, the FHIR Organization resource in Aidbox gains new semantics and functionality.

This means that when users interact with the Organizational FHIR API, they are only able to access the resources that belong to their organization or tenant. The hierarchical organization-based access control ensures that data is logically isolated and accessible only within the appropriate organizational context.

## Problem

FHIR resources must be separated per organizations. Organizations can be nested. Every organization has access to their own resources and to the nested organization resources.

## Solution

Let's consider the next organization structure. There are two independent organizations Org A & Org D, each of them has nested, dependent organizations. Org B & Org C are nested to Org A, and Org E is nested to Org D.

<figure><img src="../../../.gitbook/assets/Screenshot 2023-06-28 at 16.40.54.png" alt=""><figcaption><p>Organization hierarchy structure</p></figcaption></figure>

To achieve such a behavior, you may consider an Aidbox feature called organization-based access control.

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

When an Organization resource is created, a dedicated FHIR API is deployed for that organization. This API provides access to the associated FHIR resources. Nested organization FHIR resources are accessible through the parent Organization API.

The Organization-based FHIR API base url:

```
<AIDBOX_BASE_URL>/Organization/<org-id>/fhir
```

The Organization-based Aidbox API base url:

```
<AIDBOX_BASE_URL>/Organization/<org-id>/aidbox
```

<figure><img src="../../../.gitbook/assets/Screenshot 2023-06-28 at 15.42.54.png" alt=""><figcaption><p>FHIR APIs reflection in organization-based access control</p></figcaption></figure>

Let's play with a new APIs.

We will create a Patient resource in Org B:

{% tabs %}
{% tab title="FHIR API" %}
{% code title="status: 201 Created" %}
```yaml
PUT /Organization/org-b/fhir/Patient/pt-1
content-type: text/yaml
accept: text/yaml

name: [{given: [John], family: Smith}]
gender: male
```
{% endcode %}
{% endtab %}

{% tab title="Aidbox API" %}
{% code title="status: 201 Created" %}
```yaml
PUT /Organization/org-b/aidbox/Patient/pt-1
content-type: text/yaml
accept: text/yaml

name: [{given: [John], family: Smith}]
gender: male
```
{% endcode %}
{% endtab %}
{% endtabs %}

Now we can read it:

{% tabs %}
{% tab title="FHIR API" %}
{% code title="status: 200 OK" %}
```
GET /Organization/org-b/fhir/Patient/pt-1
```
{% endcode %}
{% endtab %}

{% tab title="Aidbox API" %}
{% code title="status: 200 OK" %}
```
GET /Organization/org-b/aidbox/Patient/pt-1
```
{% endcode %}
{% endtab %}
{% endtabs %}

The resource is also accessible through Org A API:

{% tabs %}
{% tab title="FHIR API" %}
{% code title="status: 200 OK" %}
```
GET /Organization/org-a/fhir/Patient/pt-1
```
{% endcode %}
{% endtab %}

{% tab title="Aidbox API" %}
{% code title="status: 200 OK" %}
```
GET /Organization/org-a/aidbox/Patient/pt-1
```
{% endcode %}
{% endtab %}
{% endtabs %}

But this resource is not accessible through Org C, Org D and Org E API:

{% tabs %}
{% tab title="FHIR API" %}
{% code title="status: 403 Forbidden" %}
```
GET /Organization/org-c/fhir/Patient/pt-1
```
{% endcode %}
{% endtab %}

{% tab title="Aidbox API" %}
{% code title="status: 403 Forbidden" %}
```
GET /Organization/org-c/aidbox/Patient/pt-1
```
{% endcode %}
{% endtab %}
{% endtabs %}

## FHIR API over Organization resources

{% tabs %}
{% tab title="FHIR API" %}
```
<AIDBOX_BASE_URL>/Organization/<org-id>/fhir
```
{% endtab %}

{% tab title="Aidbox API" %}
```
<AIDBOX_BASE_URL>/Organization/<org-id>/aidbox
```
{% endtab %}
{% endtabs %}

### Create

{% tabs %}
{% tab title="FHIR API" %}
```
POST <AIDBOX_BASE_URL>/Organization/<org-id>/fhir/<resource-type>
```
{% endtab %}

{% tab title="Aidbox API" %}
```
POST <AIDBOX_BASE_URL>/Organization/<org-id>/aidbox/<resource-type>
```
{% endtab %}
{% endtabs %}

### Read

{% tabs %}
{% tab title="FHIR API" %}
```
GET <AIDBOX_BASE_URL>/Organization/<org-id>/fhir/<resource-type>/<id>
```
{% endtab %}

{% tab title="Aidbox API" %}
```
GET <AIDBOX_BASE_URL>/Organization/<org-id>/aidbox/<resource-type>/<id>
```
{% endtab %}
{% endtabs %}

### Update

{% tabs %}
{% tab title="FHIR API" %}
```
PUT <AIDBOX_BASE_URL>/Organization/<org-id>/fhir/<resource-type>/<id>

PATCH <AIDBOX_BASE_URL>/Organization/<org-id>/fhir/<resource-type>/<id>
```
{% endtab %}

{% tab title="Aidbox API" %}
```
PUT <AIDBOX_BASE_URL>/Organization/<org-id>/aidbox/<resource-type>/<id>

PATCH <AIDBOX_BASE_URL>/Organization/<org-id>/aidbox/<resource-type>/<id>
```
{% endtab %}
{% endtabs %}

### Delete

{% tabs %}
{% tab title="FHIR API" %}
```
DELETE <AIDBOX_BASE_URL>/Organization/<org-id>/fhir/<resource-type>/<id>
```
{% endtab %}

{% tab title="Aidbox API" %}
```
DELETE <AIDBOX_BASE_URL>/Organization/<org-id>/aidbox/<resource-type>/<id>
```
{% endtab %}
{% endtabs %}

### Search

{% tabs %}
{% tab title="FHIR API" %}
```
GET <AIDBOX_BASE_URL>/Organization/<org-id>/fhir/<resource-type>
```
{% endtab %}

{% tab title="Aidbox API" %}
```
GET <AIDBOX_BASE_URL>/Organization/<org-id>/aidbox/<resource-type>
```
{% endtab %}
{% endtabs %}

{% hint style="warning" %}
The search API does not support search parameters:

* `_has`
* `_assoc`
* `_with`
{% endhint %}

### History

Resource full history

{% tabs %}
{% tab title="FHIR API" %}
```
GET <AIDBOX_BASE_URL>/Organization/<org-id>/fhir/<resource-type>/<id>/_history
```
{% endtab %}

{% tab title="Aidbox API" %}
```
GET <AIDBOX_BASE_URL>/Organization/<org-id>/aidbox/<resource-type>/<id>/_history
```
{% endtab %}
{% endtabs %}

Specific version history entry

{% tabs %}
{% tab title="FHIR API" %}
```
GET <AIDBOX_BASE_URL>/Organization/<org-id>/fhir/<resource-type>/<id>/_history/<vid>
```
{% endtab %}

{% tab title="Aidbox API" %}
```
GET <AIDBOX_BASE_URL>/Organization/<org-id>/aidbox/<resource-type>/<id>/_history/<vid>
```
{% endtab %}
{% endtabs %}

### Bundle

Supported transaction and batch bundle type.

{% tabs %}
{% tab title="FHIR API" %}
```yaml
POST /Organization/org-b/fhir/
Accept: text/yaml
Content-Type: text/yaml

resourceType: Bundle
# transaction | batch
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
{% endtab %}

{% tab title="Aidbox API" %}
```yaml
POST /Organization/org-b/aidbox/
Accept: text/yaml
Content-Type: text/yaml

resourceType: Bundle
# transaction | batch
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
{% endtab %}
{% endtabs %}

### Metadata

{% tabs %}
{% tab title="FHIR API" %}
```
GET <AIDBOX_BASE_URL>/Organization/<org-id>/fhir/metadata
```
{% endtab %}

{% tab title="Aidbox API" %}
```
GET <AIDBOX_BASE_URL>/Organization/<org-id>/aidbox/metadata
```
{% endtab %}
{% endtabs %}

## Shared resource mode

By default, nested API has no access to a resource that belongs to the upper organizations. Sometimes it is necessary to have resources that can be accessed by the nested APIs. To achieve it the resource should be marked as `share`.

### Create a shared resource

To create a shared resource, use the `https://aidbox.app/tenant-resource-mode` extension.

{% tabs %}
{% tab title="FHIR API" %}
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
{% endtab %}

{% tab title="Aidbox API" %}
{% code title="status: 201 Created" %}
```yaml
PUT /Organization/org-a/aidbox/Practitioner/prac-1
content-type: text/yaml

meta:
  mode: "shared"
```
{% endcode %}
{% endtab %}
{% endtabs %}

### Access shared resource from a nested API

{% tabs %}
{% tab title="FHIR API" %}
{% code title="status: 200 OK" %}
```
GET /Organization/org-b/fhir/Practitioner/prac-1
```
{% endcode %}
{% endtab %}

{% tab title="Aidbox API" %}
{% code title="status: 200 OK" %}
```
GET /Organization/org-b/aidbox/Practitioner/prac-1
```
{% endcode %}
{% endtab %}
{% endtabs %}
