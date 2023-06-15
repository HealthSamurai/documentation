# GraphQL API

Aidbox supports default GraphQL implementation without any extensions ([specification](http://spec.graphql.org/June2018/))\
Queries are supported, but mutations are not.

In Aidbox UI there is GraphiQL interface, you can try your queries there.\
GraphQL console sends all your requests to `$graphql` endpoint which you can use from your application too

## GraphQL endpoint

Aidbox uses the `/$graphql` endpoint to serve GraphQL requests.

To make a GraphQL request send a GraphQL request object using `POST` method.

GraphQL request object can contain three properties:

* `query` — the GraphQL query to evaluate.
* `operationName` — the name of the operation to evaluate.
* `variables` — the JSON object containing variable values.

You can set the `timeout` query parameter to limit execution time. Also there is a [config](../reference/configuration/environment-variables/optional-environment-variables.md#box\_features\_graphql\_timeout) for the default value if the parameter wasn't set.

Refer to [the GraphQL documentation](https://graphql.org/learn/serving-over-http/) to get more information about these properties.

### Examples

#### Simple query

Get IDs of two Patients. This query is similar to FHIR query

```
GET /fhir/Patient?_count=3
```

Request

```
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

Response

```
data:
  PatientList:
    - id: patient-1
    - id: patient-2
    - id: patient-3
```

#### Query with variables

Same query as above, but using a variable to specify the number of results returned.

Request:

```
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

```
data:
  PatientList:
    - id: patient-1
    - id: patient-2
    - id: patient-3
```

#### Query with a timeout

You can set a timeout (_in_ _seconds_) for the query.

Request:

```
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

Aidbox generates a union for every reference field. Additionally Aidbox generates `AllResources` union which contains every resource object.

## Fields

Aidbox generates a field for every field in a resource. There are some special fields:

* reference fields
* revinclude fields

### References

Reference fields contain all usual fields of Aidbox references (`id`, `resourceType`, `display`, `identifier`) and a special `resource` fields.

The `resource` field is an `AllResource` union. You can use it to fetch referred resource using GraphQL fragments.

#### Example

The following query is similar to

```
GET /Patient?_include=organization:Organization
```

Request

```
POST /$graphql
content-type: text/yaml
accept: text/yaml

query: |
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
  }
```

Response

```
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

Aidbox generates special fields to include resources which reference this resource.

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

Request

```
POST /$graphql
content-type: text/yaml
accept: text/yaml

query: |
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

Response

```
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

Aidbox generates query with name

```
<ResourceType>
```

This query accepts a single argument `id` and returns a resource with the specified `id`.

#### Example

The following query is similar to

```
GET /Patient/pt-1
```

Request

```
POST /$graphql
content-type: text/yaml
accept: text/yaml

query: |
  query {
    Patient(id: "pt-1") {
      id
      name {
        given
      }
    }
  }
```

Response

```
data:
  Patient:
    id: pt-1
    name:
      - given:
          - Patient
```

### History

Aidbox generates query with name

```
<ResourceType>History
```

The query accepts `id` argument and return history of a resource with the specified `id`.

#### Example

The following query is similar to

```
GET /Practitioner/pr-1/_history
```

Request

```
POST /$graphql
content-type: text/yaml
accept: text/yaml

query: |
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

&#x20;Response

```
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

Aidbox generates query with name

```
<ResourceType>List
```

The query can accept multiple arguments. Aidbox generates arguments from search parameters.

Each search parameter lead to 2 arguments:

* `<parameter>` — simple argument, equivalent to using FHIR search parameter
* `<parameter>_list` — represents AND condition

### Example

The following query is similar to

```
GET /Practitioner?name=another
```

Request

```
POST /$graphql
content-type: text/yaml
accept: text/yaml

query: |
  query {
    PractitionerList(name:"another") {
      id
      name {
        given
      }
    }
  }
```

Response

```
data:
  PractitionerList:
    - id: pr-2
      name:
        - given:
            - Another Practitioner name
```

### Search with conditions

It is possible to encode simple AND and OR conditions for a single parameter.

FHIR allows to encode the following type of conditions for a single parameter:

```
(A OR B OR ...) AND (C OR D OR ...) AND ...
```

In GraphQL API `<parameter>_list` parameter represents AND condition.

E.g. `PatientList(name_list: ["James", "Mary"])` searches for patients which have both names: James and Mary.

Comma represents OR condition.

E.g. `PatientList(name: "James,Mary")` searches for patients which have either name James or Mary

You can use both conditions at the same time.

E.g. `PatientList(name_list: ["James,Mary", "Robert,Patricia"])` searches for patients which have name James or Mary and name Robert or Patricia.

### Search total

Aidbox generates special field `total_` which contains total count of matching result. When you use this field, Aidbox can make a query to calculate total, which can be slow (depending on data).

#### Example

Request

```
POST /$graphql
content-type: text/yaml
accept: text/yaml

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

Response

```
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

Get id of DeviceRequestList resource, add address of Organizations and Practitioners referenced in DeviceRequestList.requester

```
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

```
{
  "query" : "
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
"}
```

Response

```
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

### Warmup

By default, Aidbox does in memory index cache warmup when the first request comes in.&#x20;

You can change it to warmup cache on startup.

```
BOX_FEATURES_GRAPHQL_WARMUP__ON__STARTUP=true
```

Or, using Aidbox project:

```
[:features :graphql :warmup-on-startup]
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

or by setting

```
[:features :graphql :reference-any]
```

configuration value in Aidbox project.
