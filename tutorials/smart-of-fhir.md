# SMART on FHIR

SMART apps it is third-party applications which interact with medical data provided FHIR server. They usually run on behalf of a patient or clinician. For more information - see [spec](http://www.hl7.org/fhir/smart-app-launch/).

### Application registration

| Parameter | Description |
| :--- | :--- |
| launch\_uri | required, the base URL of the application, usually starts the authorization process |
| redirect\_uri | required, app will redirected here with authorization code |

### Base **patient** flow

#### Launch app

{% api-method method="post" host="\[base-url\]/smart/launch/" path=" " %}
{% api-method-summary %}
Launch application
{% endapi-method-summary %}

{% api-method-description %}
Redirect to application `launch_uri` with `iss` and `launch` params
{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-form-data-parameters %}
{% api-method-parameter name="patientId" type="string" required=true %}
patient identifier
{% endapi-method-parameter %}

{% api-method-parameter name="scope" type="string" required=true %}
requested scope
{% endapi-method-parameter %}

{% api-method-parameter name="clientId" type="string" required=true %}
smart app identifier
{% endapi-method-parameter %}
{% endapi-method-form-data-parameters %}
{% endapi-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=302 %}
{% api-method-response-example-description %}

{% endapi-method-response-example-description %}

```
location [base-url]/launch.html?iss=http%3A%2F%2Flocalhost%3A8081&launch=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODEiLCJzdWIiOiJncm93dGhfY2hhcnQiLCJjdHgiOnsicGF0aWVudCI6InBhdGllbnQxIn0sInNjb3BlIjoibGF1bmNoIn0.l1gsqq6f5svgTg24SlRaIqETEkpcjSFI4Jxk8mZf9oA
```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

Now `scope` parameter supports only with value **launch.** 

| Redirect parameter | Description |
| :--- | :--- |
| iss | base FHIR endpoint |
| launch | launch context encoded to JWT |

After launch, SMART app will obtain authorization endpoints - authorize and token. It can be done in two ways - requesting `/metadata/`

{% api-method method="get" host="\[base-url\]/metadata/" path=" " %}
{% api-method-summary %}
Metadata
{% endapi-method-summary %}

{% api-method-description %}
Obtaining `CapabilityStatement`
{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=200 %}
{% api-method-response-example-description %}

{% endapi-method-response-example-description %}

```javascript
"rest": [{
    "security": {
        "extension": [{
            "url": "http://fhir-registry.smarthealthit.org/StructureDefinition/oauth-uris",
                "extension": [
                {
                    "url": "token",
                    "valueUri": "[base-url]/auth/token"
                },
                {
                    "url": "authorize",
                    "valueUri": "[base-url]/auth/authorize"
                }
            ]
        }
   ]

```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

Or requests `/.well-known/` endpoint.

{% api-method method="get" host="\[base-url\]/.well-known/smart-configuration/" path=" " %}
{% api-method-summary %}
Smart-configuration
{% endapi-method-summary %}

{% api-method-description %}
Obtaining configuration metadata
{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=200 %}
{% api-method-response-example-description %}

{% endapi-method-response-example-description %}

```javascript
{
  "authorization_endpoint": "[base-url]/auth/authorize",
  "token_endpoint": "[base-url]/auth/token",
  ...
}
```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

After this application runs authorization code flow with additional parameter - `launch` - encoded data bonded  with current application session. When application successfully authorize it will requests token and expects the response will contain context \(e.g. patient id\).

{% tabs %}
{% tab title="Token response" %}
```javascript
{"token_type": "Bearer",
 "access_token": "ODYzZmE4NDAtNTI5OC00NWU4LWIzODctODA3YjE1OGQ0ZDZi",
 "patient": "patient-id-1"}
```
{% endtab %}
{% endtabs %}

### Launch real app

We will launch [Growth Chart](https://github.com/smart-on-fhir/growth-chart-app) - application which displays statistical data for the patient. See information in repo about how run it locally.

Then create `Client` resource for this app

{% tabs %}
{% tab title="Client" %}
```javascript
{"resourceType": "Client",
 "id": "growth_chart",
 "grant_types":  ["authorization_code"],
 "smart": {"launch_uri":   "http://localhost:9000/launch.html"}
 "auth": {"authorization_code":
           {"redirect_uri": "http://localhost:9000/"}}}
```
{% endtab %}
{% endtabs %}

Load base dataset

{% tabs %}
{% tab title="Dataset" %}
```text

```
{% endtab %}

{% tab title="Second Tab" %}

{% endtab %}
{% endtabs %}

