# Sample: Patient can see it’s own data

## Prerequisites

To complete this tutorial you should install Postman and get access to the Aidbox Console \(see [here](../installation/) how to install your Aidbox instance\) .

Once you access the Aidbox REST Console, load resources that you will need to work with policies:

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

### Structure of imported resources

On previous step we have imported a client that will authenticate users, and two users with corresponding sets of related resources shown on the picture below. Overlapping outlines means that enclosed resources are related. Similar diagram is applied to User-2.

![](../.gitbook/assets/image%20%2813%29.png)

## User Login‌ <a id="user-login"></a>

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

{% tab title="Response" %}
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

Notice the `patient_id` field of `userinfo` . This is the id of a Patient resource associated with our user. It will be used further in Access Policies to decide if access should be granted or not. In general you need to specify `data.patinet_id: some_patient_id` in your User resource to establish a relation with a Patient resource.‌

The `access-token` field of `user-info` will be needed to perform requests on behalf of our User. 

![](../.gitbook/assets/image%20%2810%29.png)

At this point there are no access policies that allow the user to access any resources. So all attempts to make requests for Resources will be denied.

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
    resource/id: .user.data.patient_id  request-method: get

```
{% endtab %}

{% tab title="Response" %}
```javascript
engine: matcho
matcho:
  uri: '#/Patient/.*'
  params: 
    resource/id: .user.data.patient_id
  request-method: get
id: patient-access
resourceType: AccessPolicy
meta: 
  lastUpdated: '2020-11-10T15:00:59.497835Z'
  createdAt: '2020-11-10T15:00:59.497835Z'
  versionId: '110'
```
{% endtab %}
{% endtabs %}

Here we specified that Access Policy will grant `GET` access to a uri that matches `#/Patient/.*` regex if the request parameter named `resource/id` matches `data.patient` value of the user that makes the request.‌

So now we can read our patient. The part of the url after `/Patient/` namely `new-patient` is parsed by Access Policy engine as the `resource/id` parameter of the request: 

{% tabs %}
{% tab title="Request" %}
```yaml
GET /Patient/new-patient
```
{% endtab %}

{% tab title="Response" %}
```yaml
{
    "name": [
        {
            "given": ["Luke"]
        },
        {
            "family": "Skywalker"
        }
    ],
    "gender": "male",
    "birthDate": "2145-08-12",
    "id": "new-patient",
    "resourceType": "Patient",
    "meta": {
        "lastUpdated": "2020-11-10T13:51:16.780576Z",
        "createdAt": "2020-11-10T11:38:52.402256Z",
        "versionId": "83"
    }
}
```
{% endtab %}
{% endtabs %}

You can check that access to any other existing Patient resource, for instance that one with id `new-patient1`, will be denied.

## Access to Encounters

Now let's give our user ability to retrieve all encounters where they are referred to as a subject:

{% tabs %}
{% tab title="Request" %}
```yaml
POST /AccessPolicy

id: search-patient-encounter
engine: matcho
matcho:
  uri: /Encounter
  params:
    patient: .user.data.patient_id

```
{% endtab %}

{% tab title="Response" %}
```yaml
engine: matcho
matcho:
  uri: /Encounter
  params:
    patient: .user.data.patient_id
resourceType: AccessPolicy
id: search-patient-encounter
meta:
  lastUpdated: '2020-11-05T15:28:58.054136Z'
  createdAt: '2020-11-05T15:28:58.054136Z'
  versionId: '0'
```
{% endtab %}
{% endtabs %}

And this policy works a bit trickier. The allowed uri is `/Encounter` and it doesn't contain any additional parts that could be identified as request parameters as in the previous case. So, in order to provide the required request parameter `patient` to the Access Policy matching engine, we have to specify it as the query parameter of our request. And after the Access Policy engine allows such a request, the Search Engine comes into play. It filters out encounters that do not match the condition of `patient = our-patient-id`. To know more about how the AidBox Search works, see the [Search section](../basic-concepts/search-1/). To know more about the available search parameters, refer to the [Search Parameters section](%20https://www.hl7.org/fhir/encounter.html#search) of the FHIR documentation for the resource of interest.

Finally, we can make a request for a list of patient's encounters.

{% tabs %}
{% tab title="Request" %}
```yaml
GET /Encounter?patient=new-patient
```
{% endtab %}

{% tab title="Response" %}
```yaml
{
  "query-time": 7,
  "meta": {
    "versionId": "155"
  },
  "type": "searchset",
  "resourceType": "Bundle",
  "total": 1,
  "link": [
    {
      "relation": "first",
      "url": "/Encounter?patient=new-patient&page=1"
    },
    {
      "relation": "self",
      "url": "/Encounter?patient=new-patient&page=1"
    }
  ],
  "query-timeout": 60000,
  "entry": [
    {
      "resource": {
         "class": {
            "code": "AMB"
          },
         "status": "planned",
         "subject": {
            "id": "new-patient",
            "resourceType": "Patient"
          },
         "participant": [
           {
             "individual": {
               "id": "practitioner-1",
               "resourceType": "Practitioner"
             }
           }
         ],
         "id": "enc1",
               "resourceType": "Encounter",
               "meta": {
                    "lastUpdated": "2020-11-10T11:11:39.464261Z",
                    "createdAt": "2020-11-06T19:14:46.247628Z",
                    "versionId": "150"
               }
      },
      "fullUrl": "/Encounter/enc1",
      "link": [
        {
          "relation": "self",
          "url": "/Encounter/enc1"
        }
      ]
    }
  ],
  "query-sql": [
    "SELECT \"encounter\".* FROM \"encounter\" WHERE \"encounter\".resource @> ? LIMIT ? OFFSET ? ",
    "{\"subject\":{\"id\":\"new-patient\",\"resourceType\":\"Patient\"}}",
    100,
    0
  ]
}
```
{% endtab %}
{% endtabs %}



![](https://lh4.googleusercontent.com/EaY4y_DhDfpjxiIlRq-MLwXjhUfqbJX1p4X9uq1BS80XzQnJBZ76bB0jDbmZ7GuWAzKxCnG8GvdZBM78__Fpm-3uY_CNh2bYUYogyM0WkWSavjHL8C8hw6Ge4eP1zmSYfe0hj1Qf)



Coming soon.

