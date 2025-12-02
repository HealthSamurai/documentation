---
description: >-
  This article will guide you through passing Inferno ONC Certification (g)(10)
  Standardized API Test Kit Version: 7.2.6
---

# Pass Inferno Tests with Aidbox

{% hint style="warning" %}
This guide is valid for Aidbox version 2510 and higher.
{% endhint %}

{% hint style="info" %}
This setup has been tested using the next test suite

* ONC Certification (g)(10) Standardized API Test Kit v.7.2.6
* US Core 6.1.0 / USCDI v3, SMART App Launch 2.2.0, Bulk Data 2.0.0
{% endhint %}

## Prerequisites

#### 1. Aidbox is publicly available on the Internet

* Run Aidbox in the cloud [Sandbox](https://aidbox.app/) or
* Run [Aidbox on Kubernetes](../../../deployment-and-maintenance/deploy-aidbox/run-aidbox-in-kubernetes/) or
* Run [Aidbox locally](../../../getting-started/run-aidbox-locally.md) and proxy it with tools like ngrok/cloudflared tunnel

#### 2. Aidbox settings

From the Aidbox UI left sidebar menu, go to Settings and check these two:

* Use correct range arithmetic in search - should be **true**
* Default number of results per search page - change it to **200**

#### 3. Set up bucket for Bulk Export test

You need to [set up](../../../api/bulk-api/export.md#setup-storage) S3 Bucket for `Multi-Patient Authorization and API STU2` test step.

#### 4. Load US Core Package

From the Aidbox UI left sidebar menu, go to FAR -> Import Package -> find hl7.fhir.us.core 6.1.0 -> click Import button

#### 5. Load fixtures (read below)

Once you have your Aidbox instance up and running you need to

* Patient record with all USCDIv3 data elements in us-core format,
* User resource, associated with the patient record
* User resource, associated with the practitioner record
* Confidential and Public Client resources for smart launch flows
* Client resource for bulk api

### Prepare fixtures

Demo patient record with all USCDIv3 elements for Inferno test is available on Google Storage and maintained by Health Samurai team. You can upload with `/$load` endpoint:

```json
POST /fhir/$load
content-type: application/json
accept: application/json

{
  "source": "https://storage.googleapis.com/aidbox-public/smartbox/dataset_uscdi_v3.ndjson.gz"
}
```

It contains at least two patients `85` and `86`. Second patient will be required for Multi-Patient API test.

Let's created then User resources for patient and practitioner:

```json
POST /fhir/User
content-type: application/json
accept: application/json

{
  "email": "patient@mail.com",
  "password": "password",
  "resourceType": "User",
  "name": {
    "givenName": "Amy",
    "familyName": "Shaw"
  },
  "active": true,
  "fhirUser": {
    "reference": "Patient/85"
  },
  "roles": [
    {
      "type": "patient"
    }
  ]
}
```

Now you can login to Aidbox with `patient@mail.com / password`.

### Create client resources for Inferno

Inferno will act as smart app/bulk client app. Let's register inferno's apps as Client resources in Aidbox.

{% hint style="warning" %}
You need to replace `audience` with your Aidbox base URL in the `authorization_code` block.
{% endhint %}

```yaml
POST /fhir
content-type: application/json
accept: application/json

{
  "resourceType": "Bundle",
  "type": "transaction",
  "entry": [
    {
      "resource": {
        "id": "inferno-confidential-smart-app",
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
            "audience": ["<Aidbox-base-url>/fhir"],
            "refresh_token": true,
            "secret_required": true,
            "token_format": "jwt",
            "access_token_expiration": 300
          }
        },
        "jwks_uri": "https://inferno.healthit.gov/suites/custom/g10_certification/.well-known/jwks.json",
        "smart": {
          "launch_uri": "https://inferno.healthit.gov/suites/custom/smart/launch"
        },
        "secret": "verysecret"
      },
      "request": {
        "method": "PUT",
        "url": "Client/inferno-confidential-smart-app"
      }
    },
    {
      "resource": {
        "id": "inferno-public-smart-app",
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
            "client_assertion_types": [
              "urn:ietf:params:oauth:client-assertion-type:jwt-bearer"
            ],
            "access_token_expiration": 300
          }
        },
        "jwks_uri": "https://inferno.healthit.gov/suites/custom/g10_certification/.well-known/jwks.json",
        "smart": {
          "launch_uri": "https://inferno.healthit.gov/suites/custom/smart/launch"
        }
      },
      "request": {
        "method": "PUT",
        "url": "Client/inferno-public-smart-app"
      }
    },
    {
      "resource": {
        "id": "inferno-bulk-client",
        "resourceType": "Client",
        "type": "bulk-api-client",
        "active": true,
        "auth": {
          "client_credentials": {
            "client_assertion_types": [
              "urn:ietf:params:oauth:client-assertion-type:jwt-bearer"
            ],
            "access_token_expiration": 300
          }
        },
        "scope": [
          "system/*.rs"
        ],
        "jwks_uri": "https://inferno.healthit.gov/suites/custom/g10_certification/.well-known/jwks.json",
        "grant_types": [
          "client_credentials"
        ]
      },
      "request": {
        "method": "PUT",
        "url": "Client/inferno-bulk-client"
      }
    },
    {
      "resource": {
        "id": "inferno-client-allow",
        "resourceType": "AccessPolicy",
        "engine": "allow",
        "link": [
          {
            "reference": "Client/inferno-confidential-smart-app"
          },
          {
            "reference": "Client/inferno-public-smart-app"
          },
          {
            "reference": "Client/inferno-bulk-client"
          }
        ]
      },
      "request": {
        "method": "PUT",
        "url": "AccessPolicy/inferno-client-allow"
      }
    }
  ]
}
```

## Create Inferno test session

Create Inferno test session:

* Offitial TestKit - [https://inferno.healthit.gov/onc-certification-g10-test-kit](https://inferno.healthit.gov/onc-certification-g10-test-kit)
* Run Inferno TestKit locally - [https://github.com/onc-healthit/onc-certification-g10-test-kit](https://github.com/onc-healthit/onc-certification-g10-test-kit)

### Run Inferno tests one by one

#### 1 Standalone Patient App - Full Access

1. Click the `Standalone Patient App` link in the left sidebar
2. Click the `Run tests` button
3.  Provide require parameters for tests

    * FHIR Endpoint: `[aidbox-url]/fhir`
    * Scopes. Make sure that scopes text area contains all these scopes:

    `launch/patient openid fhirUser offline_access patient/Medication.rs patient/AllergyIntolerance.rs patient/CarePlan.rs patient/CareTeam.rs patient/Condition.rs patient/Device.rs patient/DiagnosticReport.rs patient/DocumentReference.rs patient/Encounter.rs patient/Goal.rs patient/Immunization.rs patient/Location.rs patient/MedicationRequest.rs patient/Observation.rs patient/Organization.rs patient/Patient.rs patient/Practitioner.rs patient/Procedure.rs patient/Provenance.rs patient/PractitionerRole.rs patient/ServiceRequest.rs patient/Coverage.rs patient/MedicationDispense.rs patient/Specimen.rs patient/RelatedPerson.rs`

    * Standalone Client ID: `inferno-confidential-smart-app`
    * Standalone Client Secret: `verysecret`
4. Click the `Submit` button

Once you run tests, follow the Inferno instructions and login to Aidbox as a patient `patient@mail.com / password`.

#### 2 Standalone Patient App - Limited Access

{% hint style="warning" %}
This test depends on the `Standalone Patient App` test. Pass the first sequence then continue that one
{% endhint %}

1. Click the `2 Limited Access App` link in the left sidebar
2. Click the `Run tests` button
3. Click the `Submit` button

Once you run tests, follow the Inferno instructions.

{% hint style="info" %}
By default the test expects to not get accees to all the resources but `Patient`, `Condition`, `Observation`.

To pass the test you should uncheck all the resources but those ones on the Consent screen
{% endhint %}

#### 3 EHR Practitioner App

1. Click the `EHR Practitioner App` link in the left sidebar
2. Click the `Run tests` button
3.  Provide require parameters for tests

    * FHIR Endpoint:\
      `[aidbox-url]/fhir`
    * Scopes. Make sure that scopes text area contains all these scopes:

    `launch openid fhirUser offline_access user/Medication.rs user/AllergyIntolerance.rs user/CarePlan.rs user/CareTeam.rs user/Condition.rs user/Device.rs user/DiagnosticReport.rs user/DocumentReference.rs user/Encounter.rs user/Goal.rs user/Immunization.rs user/Location.rs user/MedicationRequest.rs user/Observation.rs user/Organization.rs user/Patient.rs user/Practitioner.rs user/Procedure.rs user/Provenance.rs user/PractitionerRole.rs user/ServiceRequest.rs user/Coverage.rs user/MedicationDispense.rs user/Specimen.rs user/RelatedPerson.rs`

    * EHR Launch Client ID: `inferno-confidential-smart-app`
    * EHR Launch Client Secret: `verysecret`
4. Click the `Submit` button
5. Now you need to launch App. To do so obtain launch URI using Aidbox endpoint:

```yaml
POST /rpc
authorization: Basic aW5mZXJuby1jb25maWRlbnRpYWwtc21hcnQtYXBwOnZlcnlzZWNyZXQ=

{
  "method": "aidbox.smart/get-launch-uri",
  "params": {
    "user": "patient",
    "iss": "<Aidbox-base-url>/fhir",
    "client": "inferno-confidential-smart-app",
    "ctx": {
      "patient": "85",
      "encounter": "4fedd1a7-a5fa-444a-8b1b-05f19db339d0"
    }
  }
}
```

6. Copy URI from response and open it in the same broser you have Inferno TestKit runnning.

#### 4 Single Patient API

{% hint style="warning" %}
This test depends on the `Standalone Patient App` test. Pass the first sequence then continue that one
{% endhint %}

1. Click the `4 Single Patient API` link in the left sidebar
2. Click the `Run tests` button
3. Click the `Submit` button

Once you run tests, follow the Inferno instructions.

#### 8 Multi-Patient Authorization and API STU2

1. Click the `7 Multi-Patient API` link in the left sidebar
2. Click the `Run tests` button
3. Provide require parameters for tests
   * Bulk Data FHIR URL:\
     `[aidbox-url]/fhir`
   * Group ID: `1a`
   * Patient IDs in exported Group: `85,86`
   * Backend Services Token Endpoint:\
     `[aidbox-url]/auth/token`
   * Scopes: `system/*.rs`
   * Bulk Data Client ID: `inferno-bulk-client`
   * Encryption method `RS384`
4. Click the `Submit` button

#### 9.16 Public Client Standalone Launch with OpenID Connect

1. Click the `9.16 SMART Public Client Launch` link in the left sidebar
2. Click the `Run tests` button
3. Provide require parameters for tests
   * Bulk Data FHIR URL:\
     `[aidbox-url]/fhir`
   * Public Launch Client ID: `inferno-public-smart-app`
4. Click the `Submit` button

Once you run tests, follow the Inferno instructions and login to Aidbox as a patient `patient@mail.com / password`.

#### 9.3 Token Revocation

{% hint style="warning" %}
This test depends on the `Standalone Patient App` test. Pass the first sequence then continue that one
{% endhint %}

{% hint style="info" %}
Before you launch the test you should:

1. Open the Aidbox UI `[aidbox-url]/auth/login`
2. Click on `Sessions` tab
3. Click the `Revoke` button for all Sessions
{% endhint %}

1. Click the `9.3 Token Revocation` link in the left sidebar
2. Click the `Run tests` button
3. Provide require parameters for tests
   * Prior to executing test, Health IT developer demonstrated revoking tokens provided during patient standalone launch.: `Yes`
   * Keep other parameters unchanged
4. Wait up to 30 seconds
5. Click the `Submit` button

#### 9.4 Invalid AUD Parameter

1. Click the `9.4 Invalid AUD Launch` link in the left sidebar
2. Click the `Run tests` button
3. Provide require parameters for tests
   * FHIR Endpoint:\
     `[aidbox-url]/fhir`
   * Standalone Client ID: `inferno-confidential-smart-app`
   * Standalone Client Secret: `verysecret`
4. Click the `Submit` button
5. Click the `Perform Invalid Launch` link

#### 9.17 Invalid Access Token Request

1. Click the `9.5 Invalid Token Request` link in the left sidebar
2. Click the `Run tests` button
3. Provide require parameters for tests
   * FHIR Endpoint:\
     `[aidbox-url]/tenant/fhir`
   * Standalone Client ID: `inferno-confidential-smart-app`
   * Standalone Client Secret: `verysecret`
4. Click the `Submit` button
5. Click the `Follow this link to authorize with the SMART server` link
6. Press the `Allow` button on the consent screen

#### 9.18 Invalid PKCE Code Verifier

1. Click `9.18 Invalid PKCE Code Verifier` link in the left sidebar
2. Click the `Run tests` button
3. Provide require parameters for tests
   * FHIR Endpoint:\
     `[aidbox-url]/fhir`
   * Standalone Client ID: `inferno-confidential-smart-app`
   * Standalone Client Secret: `verysecret`
4. Click the `Submit` button
5. Click the `Follow this link to authorize with the SMART server` link
6. Press the `Allow` button on the consent screen
7. Repeat 4 times

#### 9.19 EHR Launch with Patient Scopes

1. Click the `9.19 EHR Launch with Patient Scopes` link in the left sidebar
2. Click the `Run tests` button
3. Provide require parameters for tests
   * FHIR Endpoint:\
     `[aidbox-url]/fhir`
   * Client ID: `inferno-confidential-smart-app`
   * Client Secret: `verysecret`
4. Click the `Submit` button
5.  Now you need to launch App. To do so obtain launch URI using Aidbox endpoint:

    ```yaml
    POST /rpc
    authorization: Basic aW5mZXJuby1jb25maWRlbnRpYWwtc21hcnQtYXBwOnZlcnlzZWNyZXQ=

    {
      "method": "aidbox.smart/get-launch-uri",
      "params": {
        "user": "patient",
        "iss": "<Aidbox-base-url>/fhir",
        "client": "inferno-confidential-smart-app",
        "ctx": {
          "patient": "85",
          "encounter": "4fedd1a7-a5fa-444a-8b1b-05f19db339d0"
        }
      }
    }
    ```
6. Copy URI from response and open it in the same broser you have Inferno TestKit runnning.
7. Click the `Follow this link to authorize with the SMART server` link
8. Press the `Allow` button on the consent screen

#### 9.20 Token Introspection

1. Click the `9.20 Token Introspection` link in the left sidebar
2. Click the `Run tests` button
3. Provide require parameters for tests
   * FHIR Endpoint:\
     `[aidbox-url]/fhir`
   * Custom HTTP Headers for Introspection Request: `Authorization: Basic aW5mZXJuby1jb25maWRlbnRpYWwtc21hcnQtYXBwOnZlcnlzZWNyZXQ=`
   * Client ID: `inferno-confidential-smart-app`
   * Client Secret: `verysecret`
4. Click the `Submit` button
5. Click the `Follow this link to authorize with the SMART server` link
6. Press the `Allow` button on the consent screen

#### 9.23 Asymmetric Client Standalone Launch

1. Click the `9.23 Asymmetric Client Standalone Launch` link in the left sidebar
2. Click the `Run tests` button
3. Provide require parameters for tests
   * FHIR Endpoint:\
     `[aidbox-url]/fhir`
   * Client ID: `inferno-public-smart-app`
   * Encryption Algorithm: `RS384`

#### 9.22 App Launch with SMART v1 scopes

1. Click the `9.20 App Launch with v1 scopes` link in the left sidebar
2. Click the `Run tests` button
3. Provide require parameters for tests
   * FHIR Endpoint:\
     `[aidbox-url]/fhir`
   * Client ID: `inferno-confidential-smart-app`
   * Client Secret: `verysecret`
4. Click the `Submit` button
5. Click the `Follow this link to authorize with the SMART server` link
6. Press the `Allow` button on the consent screen

#### 9.23.1 Granular Scopes 1

1. Click the `9.23.1 Granular Scopes 1` link in the left sidebar
2. Click the `Run tests` button
3. Provide require parameters for tests
   * FHIR Endpoint:\
     `[aidbox-url]/fhir`
   * Auth Type: `Confidential Symmetric`
   * Client ID: `inferno-confidential-smart-app`
   * Client Secret: `verysecret`
4. Click the `Submit` button
5. Click the `Follow this link to authorize with the SMART server` link
6. Press the `Allow` button on the consent screen

#### 9.23.2 Granular Scopes 2

1. Click the `9.23.2 Granular Scopes 2` link in the left sidebar
2. Click the `Run tests` button
3. Provide require parameters for tests
   * FHIR Endpoint:\
     `[aidbox-url]/fhir`
   * Auth Type: `Confidential Symmetric`
   * Client ID: `inferno-confidential-smart-app`
   * Client Secret: `verysecret`
4. Click the `Submit` button
5. Click the `Follow this link to authorize with the SMART server` link
6. Press the `Allow` button on the consent screen

#### 9.23 SMART Granular Scope Selection

1. Click the `9.23.1 Granular Scopes 2` link in the left sidebar
2. Click the `Run tests` button
3. Provide require parameters for tests
   * FHIR Endpoint:\
     `[aidbox-url]/fhir`
   * Auth Type: `Confidential Symmetric`
   * Client ID: `inferno-confidential-smart-app`
   * Client Secret: `verysecret`
4. Click the `Submit` button
5. Click the `Follow this link to authorize with the SMART server` link
6. On the concent screen you need to click on the row with resource `Condition` and `Observation` -> click `add filter`
   * For Condition filter will be `category` `http://terminology.hl7.org/CodeSystem/condition-category|encounter-diagnosis,http://terminology.hl7.org/CodeSystem/condition-category|problem-list-item,http://hl7.org/fhir/us/core/CodeSystem/condition-category|health-concern`
   * For Observation filter will be `category` `http://terminology.hl7.org/CodeSystem/observation-category|exam,http://terminology.hl7.org/CodeSystem/observation-category|imaging,http://terminology.hl7.org/CodeSystem/observation-category|procedure,http://terminology.hl7.org/CodeSystem/observation-category|social-history,http://hl7.org/fhir/us/core/CodeSystem/us-core-category|sdoh,http://terminology.hl7.org/CodeSystem/observation-category|survey,http://terminology.hl7.org/CodeSystem/observation-category|vital-signs`
7. Press the `Allow` button on the consent screen

#### 11 Visual Inspection and Attestation

To pass the visual inspection see the [Pass Inferno Visual Inspection and Attestation](pass-inferno-visual-inspection-and-attestation.md) guide.
