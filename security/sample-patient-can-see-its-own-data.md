# Sample: Patient can see it’s own data

## Prerequisites

To complete this tutorial you should install Postman and get access to Aidbox Console \(see [here](../installation/) how to set up your Aidbox instance\) .

The `access-token` filed of `user-info` will be needed to perform requests on behalf of our User

As soon as you access Aidbox REST Console, load resources that you will need to work with policies:

```text
POST /$import

id: patient_import
inputFormat: application/fhir+ndjson
contentEncoding: gzip
mode: bulk
inputs:
- resourceType: Client
  url: https://storage.googleapis.com/aidbox-public/client.ndjson.gz
- resourceType: User
  url: https://storage.googleapis.com/aidbox-public/usr.ndjson.gz
- resourceType: Patient
  url: https://storage.googleapis.com/aidbox-public/pt.ndjson.gz
- resourceType: Encounter
  url: https://storage.googleapis.com/aidbox-public/enc.ndjson.gz
- resourceType: Observation
  url: https://storage.googleapis.com/aidbox-public/observ.ndjson.gz
  
```

{% api-method method="get" host="GET /Patients/" path="" %}
{% api-method-summary %}

{% endapi-method-summary %}

{% api-method-description %}

{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-path-parameters %}
{% api-method-parameter name="" type="string" required=false %}

{% endapi-method-parameter %}
{% endapi-method-path-parameters %}
{% endapi-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=200 %}
{% api-method-response-example-description %}

{% endapi-method-response-example-description %}

```

```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

\#TODO Describe structure of loaded resources.

![](https://lh3.googleusercontent.com/OYIQf9EEYkzAHaw9dlvrv_nF5nFSCQtQ5M7K2wtD0WWzsYr-NuTYe9aehy5Qbw1r26jtf34jc4ksXD7Y2ZXFa-IiYfXPTQCq4uJtBT8JTJ4w5OlhzrDBwG24IMScQ6PFBj_lj1YZ)

## User Login

## Access to Patient Resource

Now you can use Postman to login as a user.

Let's add out first policy that will grant us access to the Patient resource, associated with our user.

{% tabs %}
{% tab title="Request" %}
```text
POST /auth/token

{
  "client_id": "myapp"
  "client_secret": "verysecret"
  "username": "patient-user"
  "password": "admin" 
  "grant_type": "password"
}
```
{% endtab %}

{% tab title="Response" %}
**Status:** `200`

```yaml
{
  "token_type": "Bearer",
  "userinfo": {
    "data": {
      "roles": ["Patient"],
      "patient_id": "new-patient"
    },
    "resourceType": "User",
    "id": "patient-user",
    "meta": {
      "lastUpdated": "2020-11-06T12:18:19.530001Z",
      "createdAt": "2020-11-03T14:09:03.010136Z",
      "versionId": "426"
    }
  },
  "access_token": "MjYzOTkyZDEtODg4ZC00NTBlLTgxNDEtNjIzM2Y4NWQ1M2Vk"
}
```
{% endtab %}
{% endtabs %}

{% tabs %}
{% tab title="Request" %}
```text
POST AccessPolicy/

id: patient-access
engine: matcho
matcho:
  uri: '#/Patient/.*'
  params:
    resource/id: .user.data.patient_id
  request-method: get

```
{% endtab %}

{% tab title="Response" %}
**Status:** `200`

```yaml
{
  "token_type": "Bearer",
  "userinfo": {
    "data": {
      "roles": ["Patient"],
      "patient_id": "new-patient"
    },
    "resourceType": "User",
    "id": "patient-user",
    "meta": {
      "lastUpdated": "2020-11-06T12:18:19.530001Z",
      "createdAt": "2020-11-03T14:09:03.010136Z",
      "versionId": "426"
    }
  },
  "access_token": "MjYzOTkyZDEtODg4ZC00NTBlLTgxNDEtNjIzM2Y4NWQ1M2Vk"
}
```
{% endtab %}
{% endtabs %}

Notice the `patient_id` field of `userinfo` . This is the id of a Patient resource associated with our user. It will be used further in Access Policies to decide on granting access or not.  In general you need to specify `data.patinet_id: some_patient_id` in your User resource to establish a relation with a Patient resource.

Here we specified that Access Policy will grant access to uri that matches `#/Patient/.*` regex. The request's parameter named `resource/id` must match  `data.patient` value of the user that makes the request. 

![](https://lh6.googleusercontent.com/BLglPPYixoYOH1C1qo7n8EsRbwiel1tWznbGTqHgGBdPJ3JdvGABwMRUM9FkVNKjZLa_8lr0ZdUHQjicwdEJY40Q0o5c_WNAMVOLHj3fSeRnNXaHkNta4xxUWXu6hSuXAda2Z1EX)

So now we can make request 

At this moment there are no Access Policies that allow User to access any resources. So all attempts to make requests for Resources will be declined.

Coming soon.

