# GraphQL API

Aidbox supports default GraphQL implementation without any extensions ([specification](http://spec.graphql.org/June2018/))\
Queries are supported, but mutations are not.

In Aidbox UI there is a GraphiQL interface, you can try your queries there.\
GraphQL console sends all your requests to `$graphql` endpoint which you can use from your application too

## GraphQL endpoint

Aidbox uses the `/$graphql` endpoint to serve GraphQL requests.

To make a GraphQL request send a GraphQL request object using `POST` method.

GraphQL request object can contain three properties:

* `query` — the GraphQL query to evaluate.
* `operationName` — the name of the operation to evaluate.
* `variables` — the JSON object containing variable values.

Refer to [the GraphQL documentation](https://graphql.org/learn/serving-over-http/) to get more information about these properties.

## Examples

#### Simple query

Get IDs of two Patients. This query is similar to FHIR query

```
GET /fhir/Patient?_count=3
```

Request:

```graphql
POST /$graphql
content-type: text/yaml
accept: text/yaml

query: |
  query {
    PatientList(_count: 3) {
      id
    }
  }
```

Response:

```yaml
data:
  PatientList:
    - id: patient-1
    - id: patient-2
    - id: patient-3
```

#### Query with variables

It is the same query as above but uses a variable to specify the number of results returned.

Request:

```graphql
POST /$graphql
content-type: text/yaml
accept: text/yaml

query: |
  query($count: integer) {
    PatientList(_count: $count) {
      id
    }
  }
variables:
  count: 3
```

Response:

```yaml
data:
  PatientList:
    - id: patient-1
    - id: patient-2
    - id: patient-3
```

#### Query with a timeout

You can set a timeout (_in_ _seconds_) for the query.

Request:

```graphql
POST /$graphql?timeout=10
content-type: text/yaml
accept: text/yaml

query: |
  query($count: integer) {
    PatientList(_count: $count) {
      id
    }
  }
variables:
  count: 3
```

## Objects, unions, scalars

Aidbox generates an object for every known resource and non-primitive data type.

Aidbox generates a scalar for every known primitive type i.e. for every entity with type `primitive`.

Aidbox generates a union for every reference field. Additionally, Aidbox generates the `AllResources` union which contains every resource object.

## Fields

Aidbox generates a field for every field in a resource. There are some special fields:

* reference fields
* revinclude fields

### References

Reference fields contain all usual fields of Aidbox references (`id`, `resourceType`, `display`, `identifier`) and a special `resource` fields.

The `resource` field is an `AllResource` union. You can use it to fetch the referred resource using GraphQL fragments.

#### Example

The following query is similar to

```
GET /Patient?_include=organization:Organization
```

Request:

```graphql
query {
  PatientList {
    id
    name {
      given
    }
    managingOrganization {
      id
      resource {
        ... on Organization {
          name
          id
        }
      }
    }
  }
}that
```

Response:

```yaml
data:
  PatientList:
    - id: pt-1
      name:
        - given:
            - Patient name
      managingOrganization:
        id: org-1
        resource:
          name: Organization name
          id: org-1
```

### Revincludes

Aidbox generates special fields to include resources that reference this resource.

Generated fields have the following name structure:

```
<sourceResourceType>_as_<path_to_reference>
```

_Note: unlike FHIR revincludes, GraphQL revincludes use field path, not parameter name._

#### Example

The following query is similar to

```
GET /Organization?_revinclude=CareTeam:participant
```

The request:

```graphql
query {
  PractitionerList {
    id
    name {
      given
    }
    careteams_as_participant_member {
      id
      name
    }
  }
}
```

The response:

```yaml
data:
  PractitionerList:
    - id: pr-1
      name:
        - given:
            - Practitioner name
      careteams_as_participant_member:
        - id: ct-1
          name: CareTeam name
```

## Queries

Aidbox generates three types of queries:

* get by ID,
* search,
* history.

### Get by ID

Aidbox generates query with name `<ResourceType>`

This query accepts a single argument `id` and returns a resource with the specified `id`.

#### Example

The following query is similar to

```
GET /Patient/pt-1
```

Request:

```graphql
query {
  Patient(id: "pt-1") {
    id
    name {
      given
    }
  }
}
```

Response:

```yaml
data:
  Patient:
    id: pt-1
    name:
      - given:
          - Patient
```

### History

Aidbox generates a query with the name `<ResourceType>History`

The query accepts `id` argument and return history of a resource with the specified `id`.

#### Example

The following query is similar to

```
GET /Practitioner/pr-1/_history
```

Request:

```graphql
query {
  PractitionerHistory(id: "pr-1") {
    id
    name {
      given
    }
    meta {
      versionId
    }
  }
}
```

Response:

```yaml
data:
  PractitionerHistory:
    - id: pr-1
      name:
        - given:
            - New Practitioner name
      meta:
        versionId: '11001992'
    - id: pr-1
      name:
        - given:
            - Practitioner name
      meta:
        versionId: '11001990'
```

## Search

Aidbox generates a query with the name `<ResourceType>List`.

The query can accept multiple arguments. Aidbox generates arguments from search parameters.

Each search parameter leads to 2 arguments:

* `<parameter>` — simple argument, equivalent to using FHIR search parameter
* `<parameter>_list` — represents AND condition

### Example

The following query is similar to

```
GET /Practitioner?name=another
```

Request

```graphql
POST /$graphql
content-type: text/yaml
accept: text/yaml

query: |
  query {
    PractitionerList(name: "another") {
      id
      name {
        given
      }
    }
  }
```

Response

```yaml
data:
  PractitionerList:
    - id: pr-2
      name:
        - given:
            - Another Practitioner name
```

### Search with conditions

It is possible to encode simple AND and OR conditions for a single parameter.

FHIR allows to encode of the following type of conditions for a single parameter:

```
(A OR B OR ...) AND (C OR D OR ...) AND ...
```

In GraphQL API the `<parameter>_list` parameter represents AND condition.

E.g. `PatientList(name_list: ["James", "Mary"])` searches for patients who have both names: James and Mary.

Comma represents OR condition.

E.g. `PatientList(name: "James,Mary")` searches for patients who have either the name James or Mary

You can use both conditions at the same time.

E.g. `PatientList(name_list: ["James,Mary", "Robert,Patricia"])` searches for patients who have name James or Mary and name Robert or Patricia.

### Search total

Aidbox generates a special field `total_` that contains the total count of the matching result. When you use this field, Aidbox can make a query to calculate total, which can be slow (depending on data).

#### Example

Request:

```graphql
query: |
  query {
    PatientList(_count: 2) {
      id
      name {
        given
      }
      total_
    }
  }
```

Response:

```yaml
data:
  PatientList:
    - id: pt-1
      name:
        - given:
            - Patient name
      total_: 10000
    - id: pt-2
      name:
        - given:
            - Another Patient name
      total_: 10000
```

## Complex examples

### Multiple fragments

Get id of the DeviceRequestList resource, and add the address of the Organizations and Practitioners referenced in DeviceRequestList.requester:

```graphql
query {
  DeviceRequestList {
    id,
    requester {
      resourceType
      resource {
        ... on Organization {
          id,
          address {
            use
          }
        }
        ... on Practitioner {
          id,
          address {
            use
          }
        }
      }
    }
  }
}
```

### Common fragment

This example demonstrates how to use fragments, both types of search parameter arguments and reverse includes.

Request

```graphql
fragment PractitionerRoleWithPractitioner on PractitionerRole {
  id
  code {
    coding {
      code
      system
      display
    }
  }
  practitioner {
    resource {
      ... on Practitioner {
        id
        name {
          given
        }
      }
    }
  }
}
{
  PatientList(active: true, identifier_list: [\"tenantId|org1\", \"mrn|5678\"]) {
    id
    name {
      given
    }
    generalPractitioner {
      resource {
        ...PractitionerRoleWithPractitioner
      }
    }
    observations_as_subject {
      id
      code {
        coding {
          code
          system
          display
        }
      }
      performer {
        resource {
          ...PractitionerRoleWithPractitioner
        }
      }
    }
  }
}
```

Response

```json
{
  "data" : {
    "PatientList" : [ {
      "id" : "51f507b5-8723-4dda-bee6-d3cbd86da28f",
      "name" : [ {
        "given" : [ "Tom" ]
      } ],
      "generalPractitioner" : [ {
        "resource" : {
          "id" : "54513486-7b9c-4baf-84b6-8d1bdb279ba3",
          "code" : [ {
            "coding" : [ {
              "code" : "therapist",
              "system" : "sys",
              "display" : "Therapist"
            } ]
          } ],
          "practitioner" : {
            "resource" : {
              "id" : "0091e1ee-8a5b-45a1-a1be-c2638e6ed482",
              "name" : [ {
                "given" : [ "Doc" ]
              } ]
            }
          }
        }
      } ],
      "observations_as_subject" : [ {
        "id" : "68100e43-4d1c-4fb7-b4d9-2f14d359fe90",
        "code" : {
          "coding" : [ {
            "code" : "15074-8",
            "system" : "http://loinc.org",
            "display" : "Glucose"
          } ]
        },
        "performer" : [ {
          "resource" : {
            "id" : "54513486-7b9c-4baf-84b6-8d1bdb279ba3",
            "code" : [ {
              "coding" : [ {
                "code" : "therapist",
                "system" : "sys",
                "display" : "Therapist"
              } ]
            } ],
            "practitioner" : {
              "resource" : {
                "id" : "0091e1ee-8a5b-45a1-a1be-c2638e6ed482",
                "name" : [ {
                  "given" : [ "Doc" ]
                } ]
              }
            }
          }
        } ]
      } ]
    } ]
  }
}
```

## Configuration

### Set timeout

Sets the timeout for GraphQL queries in seconds. Default value is `60`.

```
BOX_FEATURES_GRAPHQL_TIMEOUT=<integer>
```

### Warmup

By default, Aidbox does an in-memory index cache warmup when the first request comes in.

You can change it to warmup cache on startup.

```
BOX_FEATURES_GRAPHQL_WARMUP__ON__STARTUP=true
```

### Revincludes with any type

For the sake of performance, Aidbox does not provide revincludes for references of type `Reference(Any)`, e.g. for `Task.for`.

{% hint style="info" %}
When this feature is enabled, schema generation **will take 2 minutes** (approximately), Until the schema is generated **GraphQL requests will wait**.
{% endhint %}

You can enable them using the following environment variable:

```
BOX_FEATURES_GRAPHQL_REFERENCE__ANY=true
```

### Enable access control in GraphQL

By default, if the `POST /$graphql` request passes request, it can query every resource without access control checks.

To enable access control, set the environmental variable:

```
BOX_FEATURES_GRAPHQL_ACCESS__CONTROL=rest-search
```

Under the hood, GraphQL uses Search API. You can create [AccessPolicies](../modules/access-control/authorization/access-policies.md) for GET requests.

To allow Client `my-client` to query the request

```graphql
query { PatientList(_count: 1) { id } }
```

the AccessPolicy which allows `GET /Patient` is required.

```yaml
PUT /AccessPolicy/my-client-allow-patient
Content-Type: text/yaml
Accept: text/yaml

link:
  - id: my-client
    resourceType: Client
engine: matcho
matcho:
  request-method: get
  uri: /Patient
```

Of course, any [AccessPolicy engine](broken-reference) can be used. For example, using `sql` engine to allow the request if `organization_id` in the JWT is the same as `Patient.managingOrganization`:

```yaml
PUT /AccessPolicy/my-client-allow-patient
Content-Type: text/yaml
Accept: text/yaml

sql:
  query: |
    SELECT resource->'managingOrganization'
    @> jsonb_build_object('resourceType', 'Organization', 'id',
    {{jwt.organization_id}}::text) FROM patient
    WHERE id = {{params._id}};
engine: sql
resourceType: AccessPolicy
```
