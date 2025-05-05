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

<figure><img src="../../../../.gitbook/assets/Screenshot 2023-06-28 at 16.40.54.png" alt=""><figcaption><p>Organization hierarchy structure</p></figcaption></figure>

To achieve such a behavior, you may consider an Aidbox feature called organization-based access control.

Let's create the organization structure in Aidbox:

```
POST /fhir

type: batch
entry:
- request:
    method: PUT
    url: Organization/org-a
  resource:
    name: Organization A
- request:
    method: PUT
    url: Organization/org-b
  resource:
    name: Organization B
    partOf: 
      reference: Organization/org-a
- request:
    method: PUT
    url: Organization/org-c
  resource:
    name: Organization C
    partOf: 
      reference: Organization/org-a
- request:
    method: PUT
    url: Organization/org-d
  resource:
    name: Organization D
- request:
    method: PUT
    url: Organization/org-E
  resource:
    name: Organization E
    partOf: 
      reference: Organization/org-d
```

When an Organization resource is created, a dedicated FHIR API is deployed for that organization. This API provides access to the associated FHIR resources. Nested organization FHIR resources are accessible through the parent Organization API.

The Organization-based FHIR API base url:

```
<AIDBOX_BASE_URL>/Organization/<org-id>/fhir
```

The Organization-based [Aidbox API ](../../../../api/rest-api/other/aidbox-and-fhir-formats.md)base url:

```
<AIDBOX_BASE_URL>/Organization/<org-id>/aidbox
```

<figure><img src="../../../../.gitbook/assets/Screenshot 2023-06-28 at 15.42.54.png" alt=""><figcaption><p>FHIR APIs reflection in organization-based access control</p></figcaption></figure>

### Try Org-BAC

Let's play with new APIs.

We will create a Patient resource in Org B:

```
PUT /Organization/org-b/fhir/Patient/pt-1
content-type: text/yaml
accept: text/yaml

name: [{given: [John], family: Smith}]
gender: male
```

Now we can read it:

```
GET /Organization/org-b/fhir/Patient/pt-1
```

Note, that patient has a  `https://aidbox.app/tenant-organization-id` extension, which references `org-b`.&#x20;

```
id: >-
  pt-1
meta:
  extension:
    - url: https://aidbox.app/tenant-organization-id
      valueReference:
        reference: Organization/org-b
    - url: ex:createdAt
      valueInstant: '2024-10-03T15:02:09.039005Z'
  lastUpdated: '2024-10-03T15:02:09.039005Z'
  versionId: '336'
name:
  - given:
      - John
    family: Smith
gender: male
resourceType: Patient
```

The resource is also accessible through Org A API:

```
GET /Organization/org-a/fhir/Patient/pt-1
```

But this resource is not accessible through Org C, Org D and Org E API:

```
GET /Organization/org-c/fhir/Patient/pt-1 
# 403 Forbidden
```

### Limitations

Some Aidbox features do not respect Organization-based access control. The resources managing these features are inaccessible under the Organization API.

For example, there is `SubsSubscription` resource.

Any request to the `SubsSubscription` resource will return `OperationOutcome` with the `422` HTTP code and issue code `not-supported`.

If `SubsSubscription` resource is created using regular API (not Organization API), Aidbox Subscriptions will send notifications irrespectively of Organization hierarchy.

## FHIR API over Organization resources

### Create

```
POST <AIDBOX_BASE_URL>/Organization/<org-id>/fhir/<resource-type>
```

### Read

```
GET <AIDBOX_BASE_URL>/Organization/<org-id>/fhir/<resource-type>/<id>
```

### Update

#### Put

```
PUT <AIDBOX_BASE_URL>/Organization/<org-id>/fhir/<resource-type>/<id>
```

#### Patch

<pre><code><strong>PATCH &#x3C;AIDBOX_BASE_URL>/Organization/&#x3C;org-id>/fhir/&#x3C;resource-type>/&#x3C;id>?[_method={ json-patch | merge-patch | fhirpath-patch }]
</strong></code></pre>

All PATCH methods are supported. See also [patch.md](../../../../api/rest-api/crud/patch.md "mention")

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

### $everything

```
GET <AIDBOX_BASE_URL>/Organization/<org-id>/fhir/Patient/$everything
```

See also [usdeverything-on-patient.md](../../../../api/rest-api/usdeverything-on-patient.md "mention")

### $document

```
GET <AIDBOX_BASE_URL>/Organization/<org-id>/fhir/Composition/$document
```

See also [usddocument.md](../../../../api/rest-api/other/usddocument.md "mention")

### History

Resource full history

```
GET <AIDBOX_BASE_URL>/Organization/<org-id>/fhir/<resource-type>/<id>/_history
```

Specific version history entry

```
GET <AIDBOX_BASE_URL>/Organization/<org-id>/fhir/<resource-type>/<id>/_history/<vid>
```

### Bundle

Supported `transaction` and `batch` bundle types.

```yaml
POST /Organization/org-a/fhir/
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
- request:
    method: PATCH
    url: 'Patient/pt-3?_method=json-patch'
  resource:
  - op: replace
    path: birthDate
    value: '2021-01-01'
```

It is also possible to use org-based url in a `request.url`:

<pre class="language-yaml"><code class="lang-yaml">POST /
Accept: text/yaml
Content-Type: text/yaml
<strong>
</strong><strong>resourceType: Bundle
</strong># transaction | batch
type: transaction
entry:
- request:
    method: GET
    url: '/Organization/org-a/fhir/Patient/pt-1'
- request:
    method: PUT
    url: '/Organization/org-b/fhir/Patient/pt-3'
  resource:
    birthDate: '2021-01-01'
- request:
    method: POST
    url: '/Organization/org-a/fhir/Patient'
  resource:
    birthDate: '2021-01-01'
    id: 'pt-4'
</code></pre>

See also [transaction.md](../../../../api/transaction.md "mention")

### Metadata

```
GET <AIDBOX_BASE_URL>/Organization/<org-id>/fhir/metadata
```

### AidboxQuery

{% hint style="info" %}
[Learn more about AidboxQuery](../../../../api/rest-api/aidbox-search.md#aidboxquery).
{% endhint %}

To use `$query` endpoint under organization-based hierarchical access control, it is necessary to create explicitly `organization` param in `AidboxQuery`.

```yaml
PUT /AidboxQuery/<query-name>

params:
  organization:
    type: string
query: "SELECT * from patient pt WHERE pt.resource#>>'{meta,organization,id}' = {{params.organization}}"
count-query: "SELECT count(*) from patient pt WHERE pt.resource#>>'{meta,organization,id}' = {{params.organization}}"
type: query
```

Now `org-id` is automatically available in the query in `{{params.organization}}`.&#x20;

```yaml
GET /Organization/<org-id>/$query/<query-name>
```

### GraphQL

```
POST /Organization/<org-id>/aidbox/$graphql
```

Since version 2503 GraphQL is supported in OrgBAC mode. Note that it can be accessed only on the non-FHIR endpoint, because our GraphQL implementation is slightly different from FHIR.

See also: [graphql-api.md](../../../../api/graphql-api.md "mention")

## Shared resource mode

By default, nested API has no access to a resource that belongs to the upper organizations. Sometimes it is necessary to have resources that can be accessed by the nested APIs. To achieve it the resource should be marked as `share`.

{% hint style="warning" %}
Update and delete operations are not allowed from nested organizations' APIs. To update or delete `shared`resource use its root organization API.
{% endhint %}

### Create a shared resource

To create a shared resource, use the `https://aidbox.app/tenant-resource-mode` extension.

```
PUT /Organization/org-a/fhir/Practitioner/prac-1
content-type: text/yaml

meta:
  extension:
  - url: https://aidbox.app/tenant-resource-mode
    valueString: "shared"
```

### Access shared resource from a nested API

```
GET /Organization/org-b/fhir/Practitioner/prac-1
```
