# Authentication and Authorization

There are several options for authentication but let's start from the simplest one: basic authentication. It's widely supported and easy to use.

## Basic Authentication

When an Aidbox box is created \( `<your-box>` for example\) you get a fully functional FHIR server and can make requests to it by the URL like`https://<your-box>.aidbox.app`. Make sure that you use the proper name of your new box instead of `<your-box>`. Let's try to obtain a patient list for example. 

{% api-method method="get" host="https://<your-box>.aidbox.app" path="/Patient" %}
{% api-method-summary %}
Get 403
{% endapi-method-summary %}

{% api-method-description %}
The attempt to obtain patient list from a secure FHIR server.
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

### Create a client \(user\)

![](../.gitbook/assets/scr-2018-10-15_22-59-42.png)

Error message and response code give us a tip that server requires authentication. To make it possible we need to create a subject for authentication \(a client\).

Open [https://ui.aidbox.app](https://ui.aidbox.app), choose your box, open section **Auth clients**, click button **New** to create an auth client, and type the following body for resource:

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

Place proper values instead of `USERNAME` and `PASSWORD`, click **Save**.

### Create an access policy

After that, we need to create a policy which will authorize created client to read and edit all resources. Open **Access Control** section, create new access policy using the **New** button:

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

![](../.gitbook/assets/scr-2018-10-15_19-40-40.png)

Enter Username and Password. It should be same values as you entered in the resources Client and AccessPolicy in your instance of Aidbox.Cloud. Now the request will be executed successfully.

In a command console, encode your username and password to base64 encoding with the following command: 

![](../.gitbook/assets/scr-2018-10-15_19-40-51.png)

{% hint style="info" %}
Option 2. Manually generated value in Headers/Authorization
{% endhint %}

Basic authentication scheme is described in [RFC 2617](https://tools.ietf.org/html/rfc2617#page-5) and requires an `Authorization` header value in the following format: `Basic VVNFUk5BTUU6UEFTU1dPUkQK`.

```bash
echo "USERNAME:PASSWORD" | base64

# The output will be:

# VVNFUk5BTUU6UEFTU1dPUkQK
```

{% api-method method="get" host="https://<your-box>.aidbox.app" path="/Patient" %}
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
}
```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

It works! You are awesome!

In Postman, create a new request, access the Headers tab,  select the Authorization key, and enter the generated value with appended word `Basic`: `Basic VVNFUk5BTUU6UEFTU1dPUkQK`. 

![](../.gitbook/assets/scr-2018-10-15_22-46-58.png)

The endpoint `https://<your-box>.aidbox.app/Patient` allows you to get a list of patients but requires an authentication in most cases. Let's prepare an authorization header to help the server to authenticate our client and authorize the request to `/Patient`.

