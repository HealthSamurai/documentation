---
description: SMART on FHIR Considerations for Testing with Inferno ONC implementation in Aidbox for healthcare app integration.
---

# Considerations for Testing with Inferno ONC

## Mandatory software components & configurations

### Mandatory software components

Aidbox minimum installation consists of two mandatory components:

1. PostreSQL relations database management system as data persistence layer
2. Aidbox itself configured working to the `PostgreSQL`

#### PostgreSQL

As an `Aidboxdb` docker container can be got [here](https://hub.docker.com/r/healthsamurai/aidboxdb). It has all necessary extensions on board.

#### Aidbox

The powerful [FHIR-server](https://www.health-samurai.io/aidbox). It also supports `SMART on FHIR` authorization flow.

Aidbox is distributed as a Docker container:

* [aidboxone](https://hub.docker.com/r/healthsamurai/aidboxone)
* [multibox](https://hub.docker.com/r/healthsamurai/multibox)

### Mandatory software configurations

#### Aidbox

Main configuration aspects:

* S3 account & bucket should be prepared as Aidbox uploads exported data to the bucket
* Aidbox should be configured as a zen-project

#### TLS for HTTP

ONC Inferno requires certain TLS version usage over HTTP requests. The allowed versions are v1.2+.

## Technical requirements and attributes necessary for registration

### `confidential` and `public` applications

There are [two types](http://www.hl7.org/fhir/smart-app-launch/app-launch.html#support-for-public-and-confidential-apps) of the applications using SMART on FHIR API:

* `confidential` apps are able to protect issued secrets
* `public` ones are not able to do it

### `confidential` application

```yaml
PUT /Client/inferno-g10-client
content-type: text/yaml
accept: text/yaml

id: inferno-g10-client
resourceType: Client
secret: some-very-secret
grant_types:
  - authorization_code
  - basic                          # used to exchange authorization_code for access_token
auth:
  authorization_code:
    pkce: false                    # no PKCE allowed
    audience:
      - https://cmpl.aidbox.app/smart
    redirect_uri: https://inferno.healthit.gov/suites/custom/smart/redirect
    refresh_token: true
    secret_required: true          # secret is allowed
    access_token_expiration: 3600  # 1 hour
smart:
  launch_uri: https://inferno.healthit.gov/suites/custom/smart/launch
```

### `public` application

`public`, which don't have backend service and are not able to keep secret securely, shouldn't have secret, basic grant type and `auth.authorization_code.secret_required` should be disabled. Example:

```yaml
PUT /Client/inferno-g10-client
content-type: text/yaml
accept: text/yaml

id: inferno-g10-client
resourceType: Client
grant_types:
  - authorization_code
auth:
  authorization_code:
    pkce: true                    # PKCE is activated
    audience:
      - https://cmpl.aidbox.app/smart
    redirect_uri: https://inferno.healthit.gov/suites/custom/smart/redirect
    refresh_token: true
    secret_required: false        # secret is disabled
    access_token_expiration: 3600 # 1 hour
smart:
  launch_uri: https://inferno.healthit.gov/suites/custom/smart/launch
```

### `bulk` client for `back-end` application

Client example for `bulk` application.

```yaml
PUT /Client/inferno-g10-bulk-client
content-type: text/yaml
accept: text/yaml

id: inferno-g10-bulk-client
resourceType: Client
type: bulk
grant_types:
  - client_credentials
auth:
  client_credentials:
    client_assertion_types:
      - urn:ietf:params:oauth:client-assertion-type:jwt-bearer
    access_token_expiration: 300 # 5 minutes
scope:
  - system/*.read
jwks_uri: https://inferno.healthit.gov/suites/custom/g10_certification/.well-known/jwks.json
```

## Expanding `scope`

`scope` are used to let `SMART on FHIR` know what resources `an` application needs to have access to. `scope` can be defined in two ways:

1. Exact resource name like `patient/Device.read`. In this case `read` access to the `Device` is requested
2. Wildcard definition like `patient/*.read` says `all` the patients resources `read` access requested

### How Aidbox expands wildcard `*` scope

`patient/*.read` expands to:

* `patient/Patient.read`
* `patient/AllergyIntolerance.read`
* `patient/CarePlan.read`
* `patient/CareTeam.read`
* `patient/Condition.read`
* `patient/Device.read`
* `patient/DiagnosticReport.read`
* `patient/DocumentReference.read`
* `patient/Goal.read`
* `patient/Encounter.read`
* `patient/Immunization.read`
* `patient/MedicationRequest.read`
* `patient/Observation.read`
* `patient/Procedure.read`
* `patient/Provenance.read`
* `patient/Practitioner.read`
* `patient/Organization.read`
* `patient/Location.read`

`user/*.read` expands to:

* `user/Patient.read`
* `user/AllergyIntolerance.read`
* `user/CarePlan.read`
* `user/CareTeam.read`
* `user/Condition.read`
* `user/Device.read`
* `user/DiagnosticReport.read`
* `user/DocumentReference.read`
* `user/Goal.read`
* `user/Encounter.read`
* `user/Immunization.read`
* `user/MedicationRequest.read`
* `user/Observation.read`
* `user/Procedure.read`
* `user/Provenance.read`
* `user/Practitioner.read`
* `user/Organization.read`
* `user/Location.read`

`system/*.read` expands to:

* `system/Patient.read`
* `system/AllergyIntolerance.read`
* `system/CarePlan.read`
* `system/CareTeam.read`
* `system/Condition.read`
* `system/Device.read`
* `system/DiagnosticReport.read`
* `system/DocumentReference.read`
* `system/Goal.read`
* `system/Encounter.read`
* `system/Immunization.read`
* `system/MedicationRequest.read`
* `system/Observation.read`
* `system/Procedure.read`
* `system/Provenance.read`
* `system/Practitioner.read`
* `system/Organization.read`
* `system/Location.read`
