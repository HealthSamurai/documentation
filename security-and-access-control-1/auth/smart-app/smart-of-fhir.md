---
description: Learn how to launch SMART app on your local Devbox
---

# 🎓 SMART App Launch tutorial

SMART apps are third-party applications which interact with the clinical data provided by a FHIR server. They are usually run on behalf of a patient or clinician. For more information, see check the [SMART on FHIR page](./).

### Application registration

| Parameter     | Description                                                                         |
| ------------- | ----------------------------------------------------------------------------------- |
| launch\_uri   | required, the base URL of the application, usually starts the authorization process |
| redirect\_uri | required, app will redirected here with authorization code                          |

### Base **patient **flow

#### Launch app

{% swagger baseUrl="[base-url]/smart/launch/" path=" " method="post" summary="Launch application" %}
{% swagger-description %}
Redirect to application 

`launch_uri`

 with 

`iss`

 and 

`launch`

 params
{% endswagger-description %}

{% swagger-parameter in="body" name="patientId" type="string" %}
patient identifier
{% endswagger-parameter %}

{% swagger-parameter in="body" name="scope" type="string" %}
requested scope
{% endswagger-parameter %}

{% swagger-parameter in="body" name="clientId" type="string" %}
smart app identifier
{% endswagger-parameter %}

{% swagger-response status="302" description="" %}
```
location [base-url]/launch.html?iss=http%3A%2F%2Flocalhost%3A8081&launch=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODEiLCJzdWIiOiJncm93dGhfY2hhcnQiLCJjdHgiOnsicGF0aWVudCI6InBhdGllbnQxIn0sInNjb3BlIjoibGF1bmNoIn0.l1gsqq6f5svgTg24SlRaIqETEkpcjSFI4Jxk8mZf9oA
```
{% endswagger-response %}
{% endswagger %}

Now `scope` parameter is supported only with value **launch. **

| Redirect parameter | Description                   |
| ------------------ | ----------------------------- |
| iss                | base FHIR endpoint            |
| launch             | launch context encoded to JWT |

After launch, SMART app will obtain authorization endpoints - authorize and token. It can be done in two ways - requesting `/metadata/`

{% swagger baseUrl="[base-url]/smart/metadata/" path=" " method="get" summary="Metadata" %}
{% swagger-description %}
Obtaining 

`CapabilityStatement`
{% endswagger-description %}

{% swagger-response status="200" description="" %}
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
{% endswagger-response %}
{% endswagger %}

Or requests `/.well-known/` endpoint.

{% swagger baseUrl="[base-url]/smart/.well-known/smart-configuration" path=" " method="get" summary="Smart-configuration" %}
{% swagger-description %}
Obtaining configuration metadata
{% endswagger-description %}

{% swagger-response status="200" description="" %}
```javascript
{
  "authorization_endpoint": "[base-url]/auth/authorize",
  "token_endpoint": "[base-url]/auth/token",
  ...
}
```
{% endswagger-response %}
{% endswagger %}

After this application runs the authorization code flow with an additional parameter - `launch` - encoded data bonded with the current application session. When the application successfully authorizes, it will request the token and expect the response which contains the context (e.g. patient id).

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

We will launch [Growth Chart](https://github.com/smart-on-fhir/growth-chart-app) - application which displays statistical data for the patient. See the information in repo about how to run it locally.

Then create `Client` and `User` resources for this app

{% tabs %}
{% tab title="Client" %}
```yaml
POST /Client

resourceType: Client
id: growth_chart
grant_types:
- authorization_code
smart:
  launch_uri: http://localhost:9000/launch.html
auth:
  authorization_code:
    redirect_uri: http://localhost:9000/
```
{% endtab %}

{% tab title="User" %}
```javascript
POST /User

email: user@test.com
password: '123456'
 
 // don't forget to change credentials
```
{% endtab %}
{% endtabs %}

And load base dataset with `Patient` , `Observations` and `Encounters`

{% tabs %}
{% tab title="Dataset" %}
```javascript
POST /

type: transaction
entry:
- resource:
    id: patient-1
    name:
    - given:
      - Marcus
      family: Berg
      use: usual
    birthDate: '2000-12-27'
    active: true 
    gender: male
  fullUrl: '/Patient/patient-1'

  request:
    method: POST
    url: "/Patient"

- resource:
    id: enc-1
    status: finished
    class:
      system: http://terminology.hl7.org/CodeSystem/v3-ActCode
      code: AMB
      display: ambulatory
    period:
      start: '2003-11-28'
      end: '2003-11-28'
    subject:
      resourceType: Patient
      id: patient-1
  fullUrl: 'Encounter/enc-1'

  request:
    method: POST
    url: "/Encounter"

- resource:
    id: height-1
    encounter:
      uri: 'Encounter/enc-1'
    value:
      Quantity:
        system: http://unitsofmeasure.org
        unit: cm
        value: 115.316
        code: cm
    status: final
    effective:
      dateTime: '2003-11-28'    
    code:
      coding:
      - system: http://loinc.org
        code: 8302-2
        display: height
      text: height
    subject:
      resourceType: Patient
      id: patient-1
  fullUrl: 'Observation/height-1'

  request:
    method: POST
    url: "/Observation"

- resource:
    id: weight-1
    encounter:
      uri: 'Encounter/enc-1'
    value:
      Quantity:
        system: http://unitsofmeasure.org
        unit: kg
        value: 18.55193
        code: kg
    resourceType: Observation
    status: final
    effective:
      dateTime: '2003-11-28'
    code:
      coding:
      - system: http://loinc.org
        code: 3141-9
        display: weight
      text: weight
    subject:
      resourceType: Patient
      id: patient-1
  fullUrl: 'Observation/weight-1'

  request:
    method: POST
    url: "/Observation"

- resource:
    id: bmi-1
    encounter:
      uri: 'Encounter/enc-1'
    value:
      Quantity:
        system: http://unitsofmeasure.org
        unit: kg/m2
        value: 13.9
        code: kg/m2
    resourceType: Observation
    status: final
    effective:
      dateTime: '2003-11-28'
    code:
      coding:
      - system: http://loinc.org
        code: 39156-5
        display: bmi
      text: bmi
    subject:
      resourceType: Patient
      id: patient-1
  fullUrl: 'Observation/bmi-1'

  request:
    method: POST
    url: "/Observation"


- resource:
    status: finished
    id: enc-2
    class:
      system: http://terminology.hl7.org/CodeSystem/v3-ActCode
      code: AMB
      display: ambulatory
    period:
      start: '2004-11-28'
      end: '2004-11-28'
    subject:
      resourceType: Patient
      id: patient-1
  fullUrl: 'Encounter/enc-2'

  request:
    method: POST
    url: "/Encounter"

- resource:
    encounter:
      uri: 'Encounter/enc-2'
    value:
      Quantity:
        system: http://unitsofmeasure.org
        unit: cm
        value: 125.316
        code: cm
    resourceType: Observation
    status: final
    effective:
      dateTime: '2004-11-28'
    id: height-2
    code:
      coding:
      - system: http://loinc.org
        code: 8302-2
        display: height
      text: height
    subject:
      resourceType: Patient
      id: patient-1
  fullUrl: 'Observation/height-2'

  request:
    method: POST
    url: "/Observation"

- resource:
    encounter:
      uri: 'Encounter/enc-2'
    value:
      Quantity:
        system: http://unitsofmeasure.org
        unit: kg
        value: 22.55193
        code: kg
    resourceType: Observation
    status: final
    effective:
      dateTime: '2004-11-28'
    id: weight-2
    code:
      coding:
      - system: http://loinc.org
        code: 3141-9
        display: weight
      text: weight
    subject:
      resourceType: Patient
      id: patient-1
  fullUrl: 'Observation/weight-2'

  request:
    method: POST
    url: "/Observation"

- resource:
    encounter:
      uri: 'Encounter/enc-2'
    value:
      Quantity:
        system: http://unitsofmeasure.org
        unit: kg/m2
        value: 17.6
        code: kg/m2
    resourceType: Observation
    status: final
    effective:
      dateTime: '2004-11-28'
    id: bmi-2
    code:
      coding:
      - system: http://loinc.org
        code: 39156-5
        display: bmi
      text: bmi
    subject:
      resourceType: Patient
      id: patient-1
  fullUrl: 'Observation/bmi-2'

  request:
    method: POST
    url: "/Observation"

```
{% endtab %}
{% endtabs %}

Then find the created patient in the **resources **section and click the application launch button, you will be redirected to a new tab with started app.

![Growth charts](../../../.gitbook/assets/screenshot-2019-03-11-12.09.53.png)
