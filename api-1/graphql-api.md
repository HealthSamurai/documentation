# GraphQL API

Aidbox supports default GraphQL implementation without any extensions \(spec located [here](http://spec.graphql.org/June2018/)\)  
Queries are supported, but mutations are not \(yet\)

In Aidbox UI there is GraphiQL interface, you can try your queries there.  
GraphQL console sends all your requests to `$graphql` endpoint which you can use from your application too

{% api-method method="post" host="" path="/$graphql" %}
{% api-method-summary %}
GraphQL endpoint
{% endapi-method-summary %}

{% api-method-description %}
This endpoint allows you to execute GraphQL queries .
{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-body-parameters %}
{% api-method-parameter name="query" type="string" required=true %}
GraphQL query
{% endapi-method-parameter %}

{% api-method-parameter name="variables" type="object" required=false %}
JSON object with variables
{% endapi-method-parameter %}
{% endapi-method-body-parameters %}
{% endapi-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=200 %}
{% api-method-response-example-description %}
Query successfully executed.
{% endapi-method-response-example-description %}

```javascript
{"data": {<query exectuion result>}
```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

Aidbox generates different GraphQL scalars, objects, queries with args and unions from FHIR metadata.

## Queries

For each ResourceType two queries are generated:

* `<resourceType>` e.g.: `Patient`. Receives a single parameter `id` and returns a resource with the requested `id`. For example: `Patient (id: "pat-1")` 
* `<resourceType>List`e.g.: `PatientList`. Receives FHIR search parameters for that resourceType. SearchParameters have underscores instead of dashes and referenced later in this doc as `search_parameter`. For each SearchParameter there are two args generated:
  * `<search_parameter>` e.g.: `PatientList(address_state: "CA")` Accepts a string. Is an equivalent of using FHIR search parameter
  * `<search_parameter>_list` e.g.: `PatientList(language_list: ["en", "de"])` Accepts a list of strings. It is an equivalent of repeating search parameters in FHIR search. _`<search_parameter>_list` arg is needed because args can't be repeated in the GraphQL query._

### Examples

* `PatientList(language_list: ["en", "de"])` will return a set of Patients the same as `GET /Patient?language=en&language=de`  and those will be patients with `en` **AND** `de` as their communication language specified 
* `PatientList(language: "en,de")` will return a set of Patients the same as `GET /Patient?language=en,de`  and those will be patients with `en` **OR** `de` as their communication language specified 
* `PatientList(language_list: ["en", "de,fr"])` will return a set of Patients the same as `GET /Patient?language=en&language=de,fr`  and those will be patients with `en` **AND** \(`de` **OR** `fr`\) as their communication language specified 
* `PatientList(language: "en", language: "de")` is an **error**, it will ignore all `language` arg repetitions except of the last and will return a set of Patients the same as

  `GET /Patient?language=de`

## Objects

For each ResourceType object with fields is generated.  
For every FHIR resource attribute field is created.  
Also for attributes with Reference type unions are created for direct and reverse includes  
Reverse include fields have such format: `<revIncludeResourceType>s_as_<includedResourceReferenceSearchParameter>` e.g.:  
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

