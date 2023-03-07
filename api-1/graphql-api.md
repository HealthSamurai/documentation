# GraphQL API

Aidbox supports default GraphQL implementation without any extensions ([specification](http://spec.graphql.org/June2018/))\
Queries are supported, but mutations are not.

In Aidbox UI there is GraphiQL interface, you can try your queries there.\
GraphQL console sends all your requests to `$graphql` endpoint which you can use from your application too

{% swagger baseUrl="" path="/$graphql" method="post" summary="GraphQL endpoint" %}
{% swagger-description %}
This endpoint allows you to execute GraphQL queries .
{% endswagger-description %}

{% swagger-parameter in="body" name="query" type="string" required="false" %}
GraphQL query
{% endswagger-parameter %}

{% swagger-parameter in="body" name="variables" type="object" required="false" %}
JSON object with variables
{% endswagger-parameter %}

{% swagger-response status="200" description="Query successfully executed." %}
```javascript
{"data": {<query exectuion result>}
```
{% endswagger-response %}
{% endswagger %}

Aidbox generates different GraphQL scalars, objects, queries with arguments and unions from FHIR metadata.

## Queries

For each ResourceType these queries are generated:

* `<resourceType>` — get fields of the specified resource.\
  Accepts a single argument `id` and returns a resource with the specified `id`.\
  Example: `Patient (id: "pat-1")`\\
* `<resourceType>List` — search resources of given resource type.\
  Accepts FHIR search parameters for that resourceType. SearchParameters have underscores instead of dashes and referenced later in this documentation as `search_parameter`. For each SearchParameter two arguments are generated:
  * `<search_parameter>` e.g.: `PatientList(address_state: "CA")` Accepts a string. Is an equivalent of using FHIR search parameter
  * `<search_parameter>_list` e.g.: `PatientList(language_list: ["en", "de"])` Accepts a list of strings. It is an equivalent of repeating search parameters in FHIR search. _`<search_parameter>_list` arg is needed because args can't be repeated in the GraphQL query._
* `<resourceType>History` — get resource history.\
  Accepts `id` argument and returns history of the resource with the specified `id`.\
  Example: `PatientHistory(id: "pt1", _sort: "txid") {name}`

### Examples

* `PatientList(language_list: ["en", "de"])` will return a set of Patients the same as `GET /Patient?language=en&language=de` and those will be patients with `en` **AND** `de` as their communication language specified\\
* `PatientList(language: "en,de")` will return a set of Patients the same as `GET /Patient?language=en,de` and those will be patients with `en` **OR** `de` as their communication language specified\\
* `PatientList(language_list: ["en", "de,fr"])` will return a set of Patients the same as `GET /Patient?language=en&language=de,fr` and those will be patients with `en` **AND** (`de` **OR** `fr`) as their communication language specified\\
*   `PatientList(language: "en", language: "de")` is an **error**, it will ignore all `language` arg repetitions except of the last and will return a set of Patients the same as

    `GET /Patient?language=de`

## Objects

For each ResourceType object with fields is generated.\
For every FHIR resource attribute field is created.\
Also for attributes with Reference type unions are created for direct and reverse includes.

### Handling \_revinclude

FHIR GraphQL [does not support](https://hl7.org/fhir/graphql.html#searching) [\_revinclude](fhir-api/search-1/search-parameters-list/\_include-and-\_revinclude.md) Search parameter. In Aidbox you can use reverse include in such format:&#x20;

```
<revIncludeResourceType>s_as_<path_to_reference_field> 
```

Here `revIncludeResourceType` should be lowercase name of a resource; `path_to_reference_field` is a path to the field with type reference, where path separator is underscore (`_`).

For example:\
`observations_as_subject` for Patient will be equivalent of `_revinclude=Observation:subject`

&#x20;Here's an example showing how to create [first class extension](../modules-1/first-class-extensions.md), search parameter and use it in GraphQL:

{% tabs %}
{% tab title="First Class Extension" %}
```yaml
PUT /Attribute/ServiceRequest.requestedOrganizationDepartment

description: Department in the organization that made the service request
resource: {id: ServiceRequest, resourceType: Entity}
path: [requestedOrganizationDepartment]
type: {id: Reference, resourceType: Entity}
refers: 
  - Organization
extensionUrl: urn:extension:requestedOrganizationDepartment
```
{% endtab %}

{% tab title="Search Parameter" %}
```yaml
PUT /SearchParameter/ServiceRequest.requestedOrganizationDepartment

name: requestedOrganizationDepartment
type: reference
resource: {id: ServiceRequest, resourceType: Entity}
expression: [[requestedOrganizationDepartment]]
```
{% endtab %}

{% tab title="ServiceRequest" %}
```yaml
PUT /ServiceRequest/sr3

intent: plan
status: final
subject:
  id: pt1
  resourceType: Patient
requestedOrganizationDepartment:
  id: org2
  resourceType: Organization
id: sr3
resourceType: ServiceRequest
```
{% endtab %}

{% tab title="GraphQL" %}
```yaml
query {
 OrganizationList(_count: 5) {
  id,
    servicerequests_as_requestedOrganizationDepartment{
      id
    }
  }
}

```
{% endtab %}

{% tab title="Response" %}
```yaml
{
  "data": {
    "OrganizationList": [
      {
        "id": "org-1",
        "servicerequests_as_a": []
      },
      {
        "id": "org2",
        "servicerequests_as_a": [
          {
            "id": "sr3"
          }
        ]
      }
    ]
  }
}
```
{% endtab %}
{% endtabs %}

## Example

This example demonstrates how to use fragments, both types of search parameter arguments and reverse includes.

{% tabs %}
{% tab title="Request" %}
```graphql
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
{% endtab %}

{% tab title="Response" %}
```javascript
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
{% endtab %}
{% endtabs %}

## Configure GraphQL

By default, Aidbox does in memory index cache warmup when the first request comes in.&#x20;

You can change it to warmup cache on startup.

```
BOX_FEATURES_GRAPHQL_WARMUP__ON__STARTUP=true
```
