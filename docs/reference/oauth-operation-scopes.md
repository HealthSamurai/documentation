# OAuth Operation Scopes

In addition to standard SMART on FHIR clinical scopes, Aidbox supports custom OAuth scopes that control access to specific Aidbox operations.
These scopes follow the same token-based authorization model but are not part of the SMART on FHIR specification.

{% hint style="info" %}
When SMART on FHIR access control is active, requests to protected endpoints without the corresponding scope are rejected with HTTP 403 Forbidden.
{% endhint %}

{% hint style="warning" %}
These scopes only unlock access to the endpoint itself — they do not check SMART resource-level scopes (e.g. `patient/Patient.r`).
An AccessPolicy that permits the request must still be in place.
Without a matching AccessPolicy the request will be rejected even if the scope is present.
{% endhint %}

## Supported scopes

| Scope                       | Endpoint                                                      | Description                                     |
|-----------------------------|---------------------------------------------------------------|-------------------------------------------------|
| `aidbox_graphql`            | `/$graphql`                                                   | Grants access to the GraphQL endpoint.          |
| `aidbox_viewdefinition_run` | `/fhir/ViewDefinition/$run`, `/fhir/ViewDefinition/{id}/$run` | Grants access to run ViewDefinition operations. |

These scopes are included in the `scope` claim of the JWT token alongside standard SMART scopes:

```
scope: "openid fhirUser patient/Patient.r aidbox_graphql aidbox_viewdefinition_run"
```

## GraphQL scope

The `aidbox_graphql` scope controls access to the `/$graphql` endpoint.

### Examples

#### Request with scope

{% tabs %}
{% tab title="Request" %}
```http
POST /$graphql
Authorization: Bearer <token>
Content-Type: application/json

{"query": "query { PatientList { name { given family } } }"}
```

Token scope: `openid fhirUser patient/Patient.r aidbox_graphql`
{% endtab %}
{% tab title="Response" %}
```json
200 OK

{
  "data": {
    "PatientList": [{
      "name": [{
        "given": ["John"],
        "family": "Doe"
      }]
    }, ...]
  }
}
```
{% endtab %}
{% endtabs %}

#### Request without scope

{% tabs %}
{% tab title="Request" %}
```http
POST /$graphql
Authorization: Bearer <token>
Content-Type: application/json

{"query": "query { PatientList { name { given family } } }"}
```

Token scope: `openid fhirUser patient/Patient.r`
{% endtab %}
{% tab title="Response" %}
```
403 Forbidden
```
{% endtab %}
{% endtabs %}

## ViewDefinition scope

The `aidbox_viewdefinition_run` scope controls access to the `/fhir/ViewDefinition/$run` and `/fhir/ViewDefinition/{id}/$run` endpoints.

### Examples

#### Run ViewDefinition

{% tabs %}
{% tab title="Request" %}
```http
POST /fhir/ViewDefinition/$run
Authorization: Bearer <token>
Content-Type: text/yaml
Accept: application/json

resourceType: Parameters
parameter:
  - name: viewResource
    resource:
      select:
        - column:
            - name: id
              path: id
      status: draft
      resource: Patient
      resourceType: ViewDefinition
  - name: _format
    valueCode: json
```

Token scope: `openid fhirUser patient/Patient.r aidbox_viewdefinition_run`
{% endtab %}
{% tab title="Response" %}
```
200 OK

[
 {"id": "d2624db9-5ff7-4855-a41a-7ba4a7750a39"},
 {"id": "a048cef4-3846-4c18-8c1e-df2be29418b2"},
 {"id": "408c02fb-3965-4b36-ab33-2945a22dd645"},
 ...
]
```
{% endtab %}
{% endtabs %}

#### Request without scope

{% tabs %}
{% tab title="Request" %}
```http
POST /fhir/ViewDefinition/$run
Authorization: Bearer <token>
Content-Type: text/yaml
Accept: application/json

resourceType: Parameters
parameter:
  - name: viewResource
    resource:
      select:
        - column:
            - name: id
              path: id
      status: draft
      resource: Patient
      resourceType: ViewDefinition
  - name: _format
    valueCode: json
```

Token scope: `openid fhirUser patient/Patient.r`
{% endtab %}
{% tab title="Response" %}
```
403 Forbidden
```
{% endtab %}
{% endtabs %}
