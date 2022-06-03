# GraphQL API

Aidbox supports default GraphQL implementation without any extensions (spec located [here](http://spec.graphql.org/June2018/))\
Queries are supported, but mutations are not (yet)

In Aidbox UI there is GraphiQL interface, you can try your queries there.\
GraphQL console sends all your requests to `$graphql` endpoint which you can use from your application too

{% swagger baseUrl="" path="/$graphql" method="post" summary="GraphQL endpoint" %}
{% swagger-description %}
This endpoint allows you to execute GraphQL queries .
{% endswagger-description %}

{% swagger-parameter in="body" name="query" type="string" %}
GraphQL query
{% endswagger-parameter %}

{% swagger-parameter in="body" name="variables" type="object" %}
JSON object with variables
{% endswagger-parameter %}

{% swagger-response status="200" description="Query successfully executed." %}
```javascript
{"data": {<query exectuion result>}
```
{% endswagger-response %}
{% endswagger %}

Aidbox generates different GraphQL scalars, objects, queries with args and unions from FHIR metadata.

## Queries

For each ResourceType these queries are generated:

* `<resourceType>`  — get fields of the specified resource.\
  Accepts a single argument `id` and returns a resource with the specified `id`.\
  Example: `Patient (id: "pat-1")`\

* `<resourceType>List`  — search resources of given resource type.\
  Accepts FHIR search parameters for that resourceType. SearchParameters have underscores instead of dashes and referenced later in this documentation as `search_parameter`. For each SearchParameter two arguments are generated:
  * `<search_parameter>` e.g.: `PatientList(address_state: "CA")` Accepts a string. Is an equivalent of using FHIR search parameter
  * `<search_parameter>_list` e.g.: `PatientList(language_list: ["en", "de"])` Accepts a list of strings. It is an equivalent of repeating search parameters in FHIR search. _`<search_parameter>_list` arg is needed because args can't be repeated in the GraphQL query._
* `<resourceType>History`  — get resource history.\
  Accepts `id` argument and returns history of the resource with the specified `id`.\
  Example: `PatientHistory(id: "pt1", _sort: "txid") {name}`

### Examples

* `PatientList(language_list: ["en", "de"])` will return a set of Patients the same as `GET /Patient?language=en&language=de`  and those will be patients with `en` **AND** `de` as their communication language specified\

* `PatientList(language: "en,de")` will return a set of Patients the same as `GET /Patient?language=en,de`  and those will be patients with `en` **OR** `de` as their communication language specified\

* `PatientList(language_list: ["en", "de,fr"])` will return a set of Patients the same as `GET /Patient?language=en&language=de,fr`  and those will be patients with `en` **AND** (`de` **OR** `fr`) as their communication language specified\

*   `PatientList(language: "en", language: "de")` is an **error**, it will ignore all `language` arg repetitions except of the last and will return a set of Patients the same as

    `GET /Patient?language=de`

## Objects

For each ResourceType object with fields is generated.\
For every FHIR resource attribute field is created.\
Also for attributes with Reference type unions are created for direct and reverse includes\
Reverse include fields have such format: `<revIncludeResourceType>s_as_<includedResourceReferenceSearchParameter>` e.g.:\
`observations_as_subject` for Patient will be equivalent of `_revinclude=Observation:subject`

## Example

{% tabs %}
{% tab title="Request" %}
{% code title="POST /$graphql" %}
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
{% endcode %}
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

If you have any questions feel free to reach us at [Aidbox users chat](https://t.me/aidbox).
