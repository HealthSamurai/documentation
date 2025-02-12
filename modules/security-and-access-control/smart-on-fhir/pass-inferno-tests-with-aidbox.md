# Pass Inferno tests with Aidbox

{% hint style="info" %}
This functionality is available starting from version 2411.\
The [FHIR Schema Validator Engine](https://docs.aidbox.app/modules/profiling-and-validation/fhir-schema-validator/setup#enable-the-fhir-schema-validator-engine) should be enabled.
{% endhint %}

Aidbox successfully passes the Inferno SMART App Launch Test Kit STU1.

## Prerequisites

{% hint style="info" %}
Aidbox must be publicly available from the Internet in order to Inferno could reach Aidbox and run tests.\
\
• Run Aidbox on [Aidbox Portal](https://aidbox.app/)\
• Run Aidbox [locally](../../../getting-started/run-aidbox-locally-with-docker.md)
{% endhint %}

Once you have your Aidbox instance up and running you need to upload necessary resources for Inferno:

### Patient's data

Test patient data for Inferno test is available on Google Storage and maintained by Health Samurai team. You can upload it with `/$load` endpoint:

```http
POST /$load
content-type: application/json
accept: application/json

{
  "source": "https://storage.googleapis.com/aidbox-public/smartbox/rows.ndjson.gz"
}
```

### User resource, associated with the patient record

```yaml
PUT /User/test-user
content-type: application/json
accept: application/json

{
  "email": "example@mail.com",
  "password": "password",
  "name": {
    "givenName": "Amy",
    "familyName": "Shaw"
  },
  "active": true,
  "fhirUser": {
    "id": "test-pt-1",
    "resourceType": "Patient"
  },
  "id": "test-user"
}
```

Now you can login to Aidbox as a patient with `example@mail.com / password`.

### Client resource for smart launch flows and Access Policy&#x20;

```yaml
PUT /
content-type: application/json
accept: application/json

[
  {
    "id": "inferno-patient-smart-app",
    "resourceType": "Client",
    "type": "smart-app",
    "active": true,
    "grant_types": [
      "authorization_code",
      "basic"
    ],
    "auth": {
      "authorization_code": {
        "pkce": true,
        "redirect_uri": "https://inferno.healthit.gov/suites/custom/smart/redirect",
        "refresh_token": true,
        "token_format": "jwt",
        "access_token_expiration": 300
      }
    },
    "smart": {
      "launch_uri": "https://inferno.healthit.gov/suites/custom/smart/launch"
    },
    "secret": "verysecret"
  },
  {
    "id": "inferno-client-allow",
    "link": [
      {
        "id": "inferno-patient-smart-app",
        "resourceType": "Client"
      }
    ],
    "engine": "allow",
    "resourceType": "AccessPolicy"
  }
]
```

## Create Inferno test session

To create Inferno test session follow [the link ](https://inferno.healthit.gov/test-kits/smart-app-launch/)and click on `Create Test Session` button. Make sure that `SMART App Launch STU1` Test Suite has been selected.

## Run Inferno tests one by one

### 1. **Standalone Launch**

1. Click the `Standalone Launch` link in the left sidebar
2. Click the `Run tests` button
3. Provide required parameters for tests
   * FHIR Endpoint: `[aidbox-url]/fhir`
   * Standalone Client ID: `inferno-patient-smart-app`
   * Proof Key for Code Exchange (PKCE): `Enabled`
4. Click the `Submit` button

Once you run tests, follow the Inferno instructions:

* Follow the link
* Login as a patient user `example@mail.com / password`
* Allow requested scopes

### 2. **EHR Launch**

Before running this step you need to have launch URI. To get it use `get-launch-uri` endpoint. You can run this request from Aidbox REST console or any HTTP client:

```json
POST /rpc
content-type: application/json
accept: application/json
authorization: Basic aW5mZXJuby1wYXRpZW50LXNtYXJ0LWFwcDp2ZXJ5c2VjcmV0

{
  "method": "aidbox.smart/get-launch-uri",
  "params": {
    "user": "test-user",
    // change to your Aidbox URL
    "iss": "<aidbox-url>/fhir",
    "client": "inferno-patient-smart-app",
    "ctx": {
      "patient": "test-pt-1"
    }
  }
}
```

Copy `uri` value from the response.

1. Click the `Standalone Launch` link in the left sidebar
2. Click the `Run tests` button
3. Provide required parameters for tests
   * FHIR Endpoint: `[aidbox-url]/fhir`
   * EHR Launch Client ID: `inferno-patient-smart-app`
   * Proof Key for Code Exchange (PKCE): `Enabled`
4. Click the `Submit` button
5. Run launch URI copied before in the browser.

Once you perform launch URI, follow the Inferno instructions:

* Follow the link
* Allow requested scopes
