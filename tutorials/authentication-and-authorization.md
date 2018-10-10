# Authentication and Authorization



For this topic you have many options, but let's start from simplest one, basic auth.

## Basic auth

When box created \(\`testbox1\` for example\) you already have a fully-functional fhir server and can make requests to [https://testbox1.aidbox.app/fhir](https://testbox1.aidbox.app/fhir). Make sure that you use proper name of your new box instead of \`testbox1\`. Lets obtain a patient list for example. 

{% api-method method="get" host="https://testbox1.aidbox.app" path="/fhir/Patient" %}
{% api-method-summary %}
Get 403
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

{% tabs %}
{% tab title="YAML" %}
```yaml
resourceType: Client
id: USERNAME
secret: PASSWORD
```
{% endtab %}

{% tab title="JSON" %}
```javascript
{ 
  "resourceType": "Client",
  "id": "USERNAME",
  "secret": "PASSWORD"
}
```
{% endtab %}
{% endtabs %}

Place needed values instead of `USERNAME` and `PASSWORD`, click `save`.

### Create an access policy

 After that we need create a policy, which will authorize created client to read and edit all resources. Open **Access Control** section, create new access policy using **new** button:

{% tabs %}
{% tab title="YAML" %}
```yaml
resourceType: AccessPolicy
id: client1-access
engine: json-schema
schema:
  required: ['client']
  properties:
    client:
      required: ['id']
      properties:
        id: { constant: USERNAME }
```
{% endtab %}

{% tab title="JSON" %}
```javascript
{
  "resourceType": "AccessPolicy",
  "id": "client1-access",
  "engine": "json-schema",
  "schema": {
    "required": [
      "client"
    ],
    "properties": {
      "client": {
        "required": [
          "id"
        ],
        "properties": {
          "id": {
            "constant": "USERNAME"
          }
        }
      }
    }
  }
}
```
{% endtab %}
{% endtabs %}

Now USERNAME:PASSWORD can be used to access the box via basic auth.

### Checking authorization

This endpoint \`https://testbox1.aidbox.app/fhir/Patient\` allows you to get list of patients, but requires authentication in most cases. Let's prepare an authorization header to help server authenticate our client and authorize request to `/fhir/Patient`.

```bash
echo "USERNAME:PASSWORD" | base64

# The output will be:

# VVNFUk5BTUU6UEFTU1dPUkQK
```

Basic authentication scheme described in [rfc](https://tools.ietf.org/html/rfc2617#page-5) and requires `Authorization` header value in the following format: `Basic VVNFUk5BTUU6UEFTU1dPUkQK`.

{% api-method method="get" host="https://testbox1.aidbox.app" path="/fhir/Patient" %}
{% api-method-summary %}
Get patient list
{% endapi-method-summary %}

{% api-method-description %}
Get bundle resource, which contains list of patients
{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-headers %}
{% api-method-parameter name="Authentication" type="string" required=false %}
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
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

It works! You are awesome!

