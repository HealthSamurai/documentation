# Set up Aidbox with Postman

This tutorial will guide you through the process of setting up and using Postman with Aidbox for API testing and development.

## Prerequisites

* Aidbox instance up and running. You can [run Aidbox locally](../../getting-started/run-aidbox-locally.md) or [in the Health Samurai sandbox](../../getting-started/run-aidbox-in-sandbox.md).
* Postman installed on your computer
* Basic understanding of REST APIs and FHIR

## Step 1: Create a Client in Aidbox

First, you need to create a client in Aidbox that will be used for authentication. You can do this through the Aidbox UI or by making a direct API call.

{% tabs %}
{% tab title=" JSON in IAM/Client/New tab" %}
```json
 {
  "resourceType": "Client",
  "id": "postman",
  "secret": "secret",
  "grant_types": ["basic"]
}
```
{% endtab %}

{% tab title="Aidbox REST Console" %}
```json
POST /fhir/Client
Content-Type: application/json

{
  "resourceType": "Client",
  "id": "postman",
  "secret": "secret",
  "grant_types": ["basic"]
}
```
{% endtab %}
{% endtabs %}

## Step 2: Create Access Policy

Create an access policy to define what operations your Postman client can perform. While Aidbox supports multiple engines (`matcho`, `json-schema`, and others), we'll use the simplest `allow` engine.&#x20;

{% tabs %}
{% tab title="JSON in IAM/AccessPolicy/New tab" %}
```json
{
 "resourceType": "AccessPolicy" 
 "engine": "allow",
 "id": "postman-access-policy",
 "link": [
   {
     "id": "postman",
     "resourceType": "Client"
   }
 ]
}
```
{% endtab %}

{% tab title="Aidbox REST Console" %}
```json
POST /fhir/AccessPolicy
Content-Type: application/json

{
 "resourceType": "AccessPolicy" 
 "engine": "allow",
 "id": "postman-access-policy",
 "link": [
   {
     "id": "postman",
     "resourceType": "Client"
   }
 ]
}
```
{% endtab %}
{% endtabs %}

## Step 3: Set up Postman&#x20;

1. Open Postman and create a new request
2. Configure **Authorization** using&#x20;
   1. Select Auth Type `Basic Auth`
   2.  Specify Username as `postman` and Password as `secret` \


       <figure><img src="../../.gitbook/assets/image.png" alt=""><figcaption><p>Postman</p></figcaption></figure>
3. Set the request method to GET
4. Set the URL to `{{base_url}}/fhir/Patient`
5. Add the following header:
   * `Content-Type: application/json`
6. &#x20;Run query

## Debugging Tips

1. Check the response headers for additional information about the request processing
2. If you get a 401 Unauthorized error:
   * Verify your client credentials
   * Check that your access policy is correctly configured

### Additional Resources

{% content-ref url="../../modules/access-control/" %}
[access-control](../../modules/access-control/)
{% endcontent-ref %}

{% content-ref url="../../modules/access-control/authorization/access-policies.md" %}
[access-policies.md](../../modules/access-control/authorization/access-policies.md)
{% endcontent-ref %}

{% content-ref url="../security-access-control-tutorials/accesspolicy-best-practices.md" %}
[accesspolicy-best-practices.md](../security-access-control-tutorials/accesspolicy-best-practices.md)
{% endcontent-ref %}
