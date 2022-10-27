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

### Create Tenant

```yaml
PUT /Tenant/my-clinic
Content-Type: text/yaml

name: My Clinic
```

### Patient record

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

### Client resources

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
meta:
  tenant:
    id: my-clinic
    resourceType: Tenant
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
meta:
  tenant:
    id: my-clinic
    resourceType: Tenant
```

### Create Inferno test session and run test

Create Inferno test session by following the link [https://inferno.healthit.gov/onc-certification-g10-test-kit](https://inferno.healthit.gov/onc-certification-g10-test-kit). Then press the button "Run all tests". You have to provide require parameters for test:

* FHIR Endpoint:\
  `[aidbox-url]/tenant/my-clinic/patient/smart-api`
* Standalone Client ID: `inferno-confidential-patient-smart-app`
* Standalone Client Secret: `inferno-confidential-patient-smart-app-secret`
* EHR Launch Client ID: `inferno-confidential-patient-smart-app`
* EHR Launch Client Secret: `inferno-confidential-patient-smart-app-secret`
* Bulk Data FHIR URL: \
  `[aidbox-url]/tenant/my-clinic/bulk-api`
* Backend Services Token Endpoint:\
  `[aidbox-url]/auth/token`
* Bulk Data Client ID: `inferno-my-clinic-bulk-client`
* Group ID: `test-group-1`
* Patient IDs in exported Group: `test-pt-1,test-pt-2`
* Public Launch Client ID: `inferno-public-patient-smart-app`
* EHR Launch Client ID: `inferno-confidential-patient-smart-app`
* EHR Launch Client Secret: `inferno-confidential-patient-smart-app-secret`

Once you run all tests, follow inferno instructions.
