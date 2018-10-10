# Authentication and Authorization



For this topic you have many options, but let's start from simplest one, basic auth.

## Basic auth

When box created \(\`testbox1\` for example\) you already have a fully-functional fhir server and can make requests to [https://testbox1.aidbox.app/fhir](https://testbox1.aidbox.app/fhir). Make sure that you use proper name of your new box instead of \`testbox1\`. Lets obtain a patient list for example. 

{% api-method method="get" host="https://testbox1.aidbox.app" path="/fhir/Patient" %}
{% api-method-summary %}
Get Patients and 403
{% endapi-method-summary %}

{% api-method-description %}
Attempt to obtain patient list from secure fhir server.
{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=403 %}
{% api-method-response-example-description %}
Obviously results to failure
{% endapi-method-response-example-description %}

```
{
  "message": "Access Denied"
}
```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

Error message and response code give us a tip that server requires authentication. To make it possible we need to create a subject for authentication \(client\).

### Create a client \(user\)

Open [https://ui.aidbox.app](https://ui.aidbox.app), choose your box, open section **Auth clients**, click button **new** to create auth client and type following body for resource:

```yaml
resourceType: Client
id: USERNAME
secret: PASSWORD
```

Place needed values instead of `USERNAME` and `PASSWORD`. Also, we used a yaml 

{% api-method method="get" host="https://testbox1.aidbox.app" path="/fhir/Patient" %}
{% api-method-summary %}

{% endapi-method-summary %}

{% api-method-description %}
This endpoint allows you to get list of patients.
{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-headers %}
{% api-method-parameter name="Authentication" type="string" required=true %}
String which contains word \`Basic\`, space and USERNAME:PASSWORD base64 encoded string 
{% endapi-method-parameter %}
{% endapi-method-headers %}
{% endapi-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=200 %}
{% api-method-response-example-description %}
Patients successfully retrieved.
{% endapi-method-response-example-description %}

```javascript
{
  "resourceType": "Bundle",
  "type": "searchset",
  "params": [],
  "query-sql": [
    "SELECT \"patient\".* FROM \"patient\" LIMIT ? OFFSET ?",
    100,
    0
  ],
  "query-time": 2,
  "entry": [],
  "total": "_undefined",
  "link": []
}
```
{% endapi-method-response-example %}

{% api-method-response-example httpCode=403 %}
{% api-method-response-example-description %}
Could appear in case wrong credentials was provided.
{% endapi-method-response-example-description %}

```javascript
{
    "message": "Access denied"
}
```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

