# Authentication and Authorization

There are several options for authentication but let's start from the simplest one: basic authentication. It's widely supported and easy to use.

## Basic Authentication

When an [Aidbox](https://www.health-samurai.io/aidbox) box is created \(for example, `<your-box>`\), you get a fully functional FHIR server and can make requests to it by the URL like`https://<your-box>.aidbox.app`. Make sure that you use the proper name of your new box instead of `<your-box>`.   
To give an example, let's try to obtain a patient list. 

![](../.gitbook/assets/scr-2018-10-17_11-08-38.png)

{% api-method method="get" host="https://<your-box>.aidbox.app" path="/fhir/Patient" %}
{% api-method-summary %}
Get 403
{% endapi-method-summary %}

{% api-method-description %}
The attempt to obtain the patient list from a secure FHIR server.
{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-body-parameters %}
{% api-method-parameter name="Authorization" type="string" required=false %}
We don't know what to put here yet.
{% endapi-method-parameter %}
{% endapi-method-body-parameters %}
{% endapi-method-request %}

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

### Create a client

Error message and response code give us a tip that the server requires authentication. To make it possible, we need to create a subject for authentication \(a client\).

Open [https://ui.aidbox.app](https://ui.aidbox.app), choose your box, open section **Auth clients**, click the button **New** to create an auth client, and type the following body for the resource:

{% tabs %}
{% tab title="YAML" %}
```yaml
resourceType: Client
id: USERNAME
secret: PASSWORD
grant_types: ['basic']
```
{% endtab %}

{% tab title="JSON" %}
```javascript
{ 
  "resourceType": "Client",
  "id": "USERNAME",
  "secret": "PASSWORD",
  "grant_types": ['basic']
}
```
{% endtab %}
{% endtabs %}

Place proper values instead of `USERNAME` and `PASSWORD`, click **Save**.

### Create an access policy

After that, we need to create a policy that authorizes the created client to read and edit all resources. Open the **Access Control** section, create a new access policy using the **New** button:

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

Now USERNAME:PASSWORD can be used to access the box via basic authentication.

### Checking Authorization

To check authorization, we need a client. We can use [Postman](https://www.getpostman.com/) as a client. This is how it's done in Postman.

{% hint style="info" %}
Option 1. Basic Auth
{% endhint %}

 In Postman, create a new request, switch to **Authorization** tab, select **Basic Auth**.

Enter Username and Password. It should be the same values as you entered in the resources Client and AccessPolicy in your instance of Aidbox.Cloud. Now the request will be executed successfully.

![](../.gitbook/assets/scr-2018-10-17_11-11-59.png)

{% hint style="info" %}
Option 2. Manually generated value in Headers/Authorization
{% endhint %}

Basic authentication scheme is described in [RFC 2617](https://tools.ietf.org/html/rfc2617#page-5) and requires an `Authorization` header value in the following format: `Basic VVNFUk5BTUU6UEFTU1dPUkQK`.

In a command console, encode your username and password to base64 encoding with the following command: 

```bash
echo -n "USERNAME:PASSWORD" | base64

# The output will be:

# VVNFUk5BTUU6UEFTU1dPUkQK
```

In Postman, create a new request, access the Headers tab,  select the Authorization key, and enter the generated value with appended word `Basic`: `Basic VVNFUk5BTUU6UEFTU1dPUkQK`. 

![](../.gitbook/assets/scr-2018-10-17_11-09-28.png)

{% api-method method="get" host="https://<your-box>.aidbox.app" path="/fhir/Patient" %}
{% api-method-summary %}
Get patient list
{% endapi-method-summary %}

{% api-method-description %}
Get the bundle resource which contains a list of patients.
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
}The endpoint https://<your-box>.aidbox.app/Patient allows you to get a list of patients but requires an authentication in most cases. Let's prepare an authorization header to help the server to authenticate our client and authorize the request to /Patient.
```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

It works! You are awesome!

