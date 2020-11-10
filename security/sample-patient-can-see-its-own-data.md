# Sample: Patient can see it’s own data

## Prerequisites

To complete this tutorial you should install Postman and get access to Aidbox Console \(see [here](../installation/) how to set up your Aidbox instance\) .

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

\#TODO Describe structure of loaded resources.

## User Login

Now you can use Postman to login as a user.

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

Notice the `patient_id` field of `userinfo` . This is the id of a Patient resource associated with our user. It will be used further in Access Policies to decide on granting access or not.

![](https://lh6.googleusercontent.com/BLglPPYixoYOH1C1qo7n8EsRbwiel1tWznbGTqHgGBdPJ3JdvGABwMRUM9FkVNKjZLa_8lr0ZdUHQjicwdEJY40Q0o5c_WNAMVOLHj3fSeRnNXaHkNta4xxUWXu6hSuXAda2Z1EX)

Coming soon.

