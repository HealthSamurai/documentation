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



\#TODO Describe structure of loaded resources.‌

## User Login <a id="user-login"></a>

‌

Now you can use Postman to login as a user.

{% tabs %}
{% tab title="Request" %}
```javascript
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

{% tab title="Responce" %}
```javascript
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

‌

Notice the `patient_id` field of `userinfo` . This is the id of a Patient resource associated with our user. It will be used further in Access Policies to decide on granting access or not. In general you need to specify `data.patinet_id: some_patient_id` in your User resource to establish a relation with a Patient resource.‌

The `access-token` filed of `user-info` will be needed to perform requests on behalf of our User

![](../.gitbook/assets/image%20%2810%29.png)

At this moment there are no Access Policies that allow User to access any resources. So all attempts to make requests for Resources will be declined.

## Access to a Patient Resource <a id="access-to-patient-resource"></a>

Let's add out first policy that will grant us access to the Patient resource, associated with our user.

{% tabs %}
{% tab title="Request" %}
```yaml
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

{% tab title="Responce" %}
```javascript
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

Here we specified that Access Policy will grant access to a uri that matches `#/Patient/.*` regex if the request's parameter named `resource/id` matches `data.patient` value of the user that makes the request.‌

So now we can make request‌



Coming soon.

