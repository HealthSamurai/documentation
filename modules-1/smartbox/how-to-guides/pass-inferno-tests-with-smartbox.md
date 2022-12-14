---
description: >-
  This article will guide you through passing ONC Certification (g)(10)
  Standardized API on Inferno framework.
---

# Pass Inferno tests with Smartbox

{% hint style="info" %}
The article has be reviewed for next Inferno framework v0.3.12 and test suite

* ONC Certification (g)(10) Standardized API v3.2.0
* US Core 3.1.1 / USCDI v1, SMART App Launch 1.0.0, Bulk Data 1.0.1
{% endhint %}

## Prerequisites

Once you have your Smartbox instance [up and running](../get-started/set-up-smartbox.md) you need to register a FHIR server by creating Tenant resource and upload necessary resources for Inferno.

* Patient record with all USCDIv1 data elements in us-core format,
* User resource, associated with the patient record
* Client resource for smart launch flows
* Client resource for bulk api

## Create Tenant

```yaml
PUT /Tenant/my-clinic
Content-Type: text/yaml

name: My Clinic
```

## Patient record

Demo patient record with all USCDI elements for Inferno test is available on Google Storage and maintained by Health Samurai team. You can upload with `/$load` endpoint:

```yaml
POST /$load
Content-Type: text/yaml

source: 'https://storage.googleapis.com/aidbox-public/smartbox/rows.ndjson.gz'
merge:
  meta:
    tenant:
      id: my-clinic
      resourceType: Tenant
```

It contains at least two patients `test-pt-1` and `test-pt-2`. Second patient will be required for Multi-Patient API test.

Let's created then User resource for `test-pt-1`:

```yaml
POST /User
Content-Type: text/yaml

email: example@mail.com
name:
  givenName: Amy
  familyName: Shaw
active: true
fhirUser:
  id: test-pt-1
  resourceType: Patient
roles:
- type: patient
password: password
meta:
  tenant:
    id: my-clinic
    resourceType: Tenant
```

Now you can login to My Clinic patient portal with `example@mail.com / password`.

## Client resources

In the requests below the simples way of adding Clients is shown. In some cases it's important to create Client resources without predefined `secret` and `id` (see [that article](../background-information/adding-clients-for-inferno-tests.md) for more details).

#### inferno-confidential-patient-smart-app

```yaml
PUT /Client/inferno-confidential-patient-smart-app
Content-Type: text/yaml

type: patient-facing-smart-app
active: true
secret: inferno-confidential-patient-smart-app-secret
grant_types:
- authorization_code
auth:
  authorization_code:
    pkce: false
    redirect_uri: 'https://inferno.healthit.gov/suites/custom/smart/redirect'
    refresh_token: true
    secret_required: true
    access_token_expiration: 300
smart:
  launch_uri: 'https://inferno.healthit.gov/suites/custom/smart/launch'
```

#### inferno-my-clinic-bulk-client

```yaml
PUT /Client/inferno-my-clinic-bulk-client
Content-Type: text/yaml

type: bulk-api-client
active: true
auth:
  client_credentials:
    client_assertion_types: ['urn:ietf:params:oauth:client-assertion-type:jwt-bearer']
    access_token_expiration: 300
scope: [system/*.read]
jwks_uri: https://inferno.healthit.gov/suites/custom/g10_certification/.well-known/jwks.json
grant_types:
- client_credentials
meta:
  tenant:
    id: my-clinic
    resourceType: Tenant
```

#### inferno-public-patient-smart-app

```yaml
PUT /Client/inferno-public-patient-smart-app
Content-Type: text/yaml

type: patient-facing-smart-app
active: true
grant_types:
- authorization_code
auth:
  authorization_code:
    pkce: false
    redirect_uri: 'https://inferno.healthit.gov/suites/custom/smart/redirect'
    refresh_token: true
    secret_required: false
    access_token_expiration: 300
smart:
  launch_uri: 'https://inferno.healthit.gov/suites/custom/smart/launch'
```

## Create Inferno test session

Create Inferno test session by following the link [https://inferno.healthit.gov/onc-certification-g10-test-kit](https://inferno.healthit.gov/onc-certification-g10-test-kit).

{% hint style="info" %}
1. To pass the `EHR Practitioner App` inferno sequence see the guide [How-to perform EHR launch](how-to-perform-ehr-launch.md)
2. See [How-to revoke granted access](revoke-granted-access.md) to pass the `Token Revocation` Inferno test
{% endhint %}

### Run all Inferno tests

Press the `Run all tests` button then provide require parameters for the tests:

* FHIR Endpoint:\
  `[aidbox-url]/tenant/my-clinic/patient/smart-api`
* Standalone Client ID: `inferno-confidential-patient-smart-app`
* Standalone Client Secret: `inferno-confidential-patient-smart-app-secret`
* EHR Launch Client ID: `inferno-confidential-patient-smart-app`
* EHR Launch Client Secret: `inferno-confidential-patient-smart-app-secret`
* Bulk Data FHIR URL:\
  `[aidbox-url]/tenant/my-clinic/bulk-api`
* Backend Services Token Endpoint:\
  `[aidbox-url]/auth/token`
* Bulk Data Client ID: `inferno-my-clinic-bulk-client`
* Encryption method `RS384`
* Group ID: `test-group-1`
* Patient IDs in exported Group: `test-pt-1,test-pt-2`
* Public Launch Client ID: `inferno-public-patient-smart-app`
* EHR Launch Client ID: `inferno-confidential-patient-smart-app`
* EHR Launch Client Secret: `inferno-confidential-patient-smart-app-secret`

Once you run all tests, follow Inferno instructions.

### Run Inferno tests one by one

#### 1 Standalone Patient App - Full Access

1. Click the `Standalone Patient App` link in the left sidebar
2. Click the `Run tests` button
3. Provide require parameters for tests
   * FHIR Endpoint:\
     `[aidbox-url]/tenant/my-clinic/patient/smart-api`
   * Standalone Client ID: `inferno-confidential-patient-smart-app`
   * Standalone Client Secret: `inferno-confidential-patient-smart-app-secret`
4. Click the `Submit` button

Once you run tests, follow the Inferno instructions.

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

To pass the test you should:

1. Uncheck all the resources but those oness on the Consent screen
2. Keep following checkboxes checked `Launch Patient`, `Open ID`, `FHIR User` and `Offline Access` 
{% endhint %}

#### 3 EHR Practitioner App

1. Click the `EHR Practitioner App` link in the left sidebar
2. Click the `Run tests` button
3. Provide require parameters for tests
   * FHIR Endpoint:\
     `[aidbox-url]/tenant/my-clinic/patient/smart-api`
   * EHR Launch Client ID: `inferno-confidential-patient-smart-app`
   * EHR Launch Client Secret: `inferno-confidential-patient-smart-app-secret`
4. Click the `Submit` button
5. Open the patient portal UI `[aidbox-url]/tenant/my-clinic/patient/portal`
6. Login to the portal using credentials we created before `example@mail.com / password`
7. Find the `inferno-confidential-patient-smart-app` application
8. Click the `Launch` button

Once you perform EHR launch, follow the Inferno instructions.
