---
description: Do multiple operations in one call
---

# Batch/Transaction

### Introduction

Transaction interaction allows performing several interactions using one http request. There are two types of transaction interaction (type is specified by field `type`): batch and transaction. The first one just executes requests one by one, the second one does the same, but roll backs all changes if any request fails.

```
POST [base]
```

The body of such request contains one resource of type Bundle, which contains field entry with an array of interactions, for example: \\

```yaml
POST /
​Accept: text/yaml
Content-Type: text/yaml

type: transaction
entry:
- resource:
    id: admin
    email: "admin@mail.com" # Change this value
    password: "password" # Change this value
  request:
    method: POST
    url: "/User"

- resource:
    id: SPA
    redirect_uri: http://localhost:4200
  request:
    method: POST
    url: "/Client"
    
- resource:
    engine: json-schema
    schema:
      type: object
      required:
      - user
  request:
    method: POST
    url: "/AccessPolicy"
```

Each element of the entry array contains a resource field (body of the request) and a request field (request line in terms of the HTTP request).

```yaml
resource: # not needed for DELETE and GET
  # resource here

fullUrl: "something-here" # needed if you want to refer
                          # the resource inside bundle
request:
  method: POST # POST/GET/PUT/DELETE
  url: "/ResourceType" # request url
```

### Processing rules and Conditional refs

Transaction interaction is processed in the order provided in a bundle, each interaction is executed one by one. It differs from the FHIR transaction [processing rules](https://www.hl7.org/fhir/http.html#trules).

For `type: batch` references to resources inside a bundle won't be resolved.

For `type: transaction` before processing interactions, all references in a resource will attempt to resolve. In this example ProcedureRequest will refer to a newly created patient:

```yaml
POST /
Accept: text/yaml
Content-Type: text/yaml

type: transaction
entry:
- resource:
    resourceType: Patient
  fullUrl: urn:uuid:<uuid-here>
  request:
    method: POST
    url: "/Patient?_identifier=mrn:123"
    
- resource:
    resourceType: Encounter
    status: proposal
    subject:
      reference: urn:uuid:<uuid-here>
  request:
    method: POST
    url: "/Encounter"
```

You can provide a full Url with value like `"urn:<uuid-here>"` and reference to the resource created by such interaction using ref: `{reference: "urn:<uuid-here>"}`. Those references are temporary and will be translated to valid Aidbox references when interaction entry is processed by a server.

{% hint style="danger" %}
You SHALL NOT refer resource, which is created later using conditional operations!
{% endhint %}

### Multiple resources with the same id

If you have multiple entries with the same resource id, aidbox will execute them one by one and thus you are able to create a resource with a history in within a single transaction:

```yaml
POST /
Accept: text/yaml
Content-Type: text/yaml

resourceType: Bundle
type: transaction
entry:
- request: {method: PUT, url: 'Patient/pt-1'}
  resource: {birthDate: '2021-01-01'}
- request: {method: PUT, url: 'Patient/pt-1'}
  resource: {birthDate: '2021-01-02'}
- request: {method: PUT, url: 'Patient/pt-1'}
  resource: {birthDate: '2021-01-03'}
```

{% tabs %}
{% tab title="Request" %}
```yaml
GET /Patient/pt-1
Accept: text/yaml
```
{% endtab %}

{% tab title="Response" %}
```yaml
{birthDate: '2021-01-03', id: pt-1, resourceType: Patient}
```
{% endtab %}
{% endtabs %}

```yaml
GET /Patient/pt-1/_history
Accept: text/yaml
Content-Type: text/yaml

resourceType: Bundle
type: history
total: 3
entry:
- resource: {birthDate: '2021-01-03', id: pt-1, resourceType: Patient}
- resource: {birthDate: '2021-01-02', id: pt-1, resourceType: Patient}
- resource: {birthDate: '2021-01-01', id: pt-1, resourceType: Patient}
```

## Change transaction isolation level

By default Aidbox uses `SERIALIZABLE` transaction isolation level. This may lead to some transactions being rejected when there are many concurrent requests.

See more about [transaction isolation in Postgres documentation](https://www.postgresql.org/docs/current/transaction-iso.html).

The best way to handle rejected transactions is to retry them. If it is not possible, you can set maximum isolation level with HTTP header or [environment variable](../reference/configuration/environment-variables/optional-environment-variables.md#box\_features\_fhir\_transaction\_max\_\_isolation\_level). If both HTTP header and environment variable are provided, header will be used.

{% hint style="danger" %}
Using isolation level lower than serializable may lead to data serialization anomalies.
{% endhint %}

Example:

```yaml
POST /fhir
x-max-isolation-level: read-committed
content-type: text/yaml
accept: text/yaml

resourceType: Bundle
type: transaction
entry:
  - request:
      method: PUT
      url: "/Patient/pt-1"
    resource:
      active: true
  - request:
      method: PUT
      url: "/Patient/pt-2"
    resource:
      active: false
```
