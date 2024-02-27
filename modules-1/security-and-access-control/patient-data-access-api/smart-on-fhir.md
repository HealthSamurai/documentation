---
description: This guide explains how SMART App launch (1.0.0 & 2.0.0) can be enabled
---

# How to enable SMART on FHIR on Patient Access API

## Prerequisites

### Docker and Docker Compose

You should have Docker and Docker Compose installed before go further. To get it installed follow the [instructions](https://docs.docker.com/engine/install/).&#x20;

### Aidbox license

To get the Aidbox License:

1. Go the Aidbox user portal [https://aidbox.app](https://aidbox.app/)
2. Login to the portal
3. Create new **self-hosted** Aidbox License or use the license that you already have

## Expose Aidbox port to the Internet

In this guide we are using `ngrok` to make Aidbox accessible from the Internet. Aidbox will be listening the 8888 port.

To launch ngrok run the command below.

```sh
ngrok http 8888
```

When ngrok starts it shows the domain name which is linked to the 8888 port. The forwarding  domain name is used to defined `AIDBOX_BASE_URL` env value.

## Create Aidbox project

Aidbox is configured by the [Aidbox Configuration Projects](../../../aidbox-configuration/aidbox-zen-lang-project/). To create sample project run command below&#x20;

<pre class="language-sh"><code class="lang-sh"><strong>git clone \
</strong>  --branch=main \
  --depth=1 \
  https://github.com/Aidbox/aidbox-project-template \
  aidbox-project &#x26;&#x26; \
  cd aidbox-project &#x26;&#x26; \
  rm -rf .git
</code></pre>

{% hint style="info" %}
See more details related the [running Aidbox locally](../../../getting-started-1/run-aidbox/run-aidbox-locally-with-docker.md)
{% endhint %}

### Apply the license and AIDBOX\_BASE\_URL

Populate the `.env` file with the Aidbox License.&#x20;

{% code title=".env" %}
```ini
AIDBOX_LICENSE=YOUR_AIDBOX_LICENSE_KEY

AIDBOX_BASE_URL=YOUR_NGROK_FORWARDING_DOMAIN_NAME
...
```
{% endcode %}

## Enable SMART on FHIR

To enable hierarchical access control (multi-tenancy on Organization resources) add necessary imports to the `zrc/main.edn` file.

1. Add `aidbox.auth` , `aidbox.oauth2` and  `aidbox.patient-api.v1` to the import section.
2. Add `grant-lookup-method` definition

```
{ns main
 import #{aidbox
          aidbox.auth           ;; import auth
          aidbox.oauth2         ;; import oauth2
          aidbox.patient-api.v1 ;; import Patient API
          config}

 ;; define grant-lookup-method
 grant-lookup-method
 {:zen/tags #{aidbox.auth/grant-lookup}
  :method   aidbox.auth/single-patient-grant-lookup}

 box
 {:zen/tags #{aidbox/system}
  :config   config/base-config
  :services {:admin-user-seed config/admin-user-seed
             :root-client-seed config/root-client-seed}}}
```

## Start Aidbox with Docker Compose

To start Aidbox run the command in the `aidbox-project` directory.

```bash
docker compose up --force-recreate
```

When Aidbox starts, navigate to the [http://localhost:8888](http://localhost:8888/) and sign in to the Aidbox UI using the credentials `admin` / `password`.

## Register OAuth & SMART on FHIR scopes

### Load Scope resources for SMART App launch 1 & 2

Use Aidbox UI Rest Console to load Scope resources.

<pre class="language-yaml"><code class="lang-yaml"><strong>POST /$load
</strong>Content-Type: text/yaml

source: 'https://storage.googleapis.com/aidbox-public/smart-on-fhir/scopes.ndjson.gz'
</code></pre>

### Create AccessPolicy resources

Use Aidbox UI Rest Console to create AccessPoliciy resources.

<pre class="language-yaml"><code class="lang-yaml"><strong>PUT /
</strong>Content-Type: text/yaml

- id: allow-public-operation
  resourceType: AccessPolicy
  engine: matcho
  matcho:
    uri:
      $one-of:
      - /patient/fhir/metadata
      - /patient/fhir/style-v1.json
      - /patient/fhir/.well-known/smart-configuration
      - /patient/auth/login
      - /patient/auth/authorize
      - /patient/auth/authenticate
      - /patient/auth/grant
- id: allow-patient-access-api-over-smart-on-fhir
  resourceType: AccessPolicy
  engine: complex
  and:
  - engine: matcho
    matcho:
      uri: '#/patient/fhir/.+'
      client:
        type: smart-app
  - engine: smart-on-fhir
</code></pre>

### Create Patient and User resources

Use Aidbox UI Rest Console to create the Patient and User resources.

```yaml
PUT /
Content-Type: text/yaml

- id: my-patient
  resourceType: Patient
- id: my-user              # my-user is a patient login
  resourceType: User
  password: password       # password is a patient password
  fhirUser:
    id: my-patient
    resourceType: Patient
```

### Create Client resource

To make Client support SMART App launch 1.0.0 and SMART App launch 2.0.0 add scopes for both versions.

```yaml
PUT /
Content-Type: text/yaml

- fhir-base-url: /patient/fhir
  type: smart-app
  grant_types: [authorization_code]
  resourceType: Client
  auth:
    authorization_code:
      pkce: false
      redirect_uri: https://inferno.healthit.gov/suites/custom/smart/redirect
      refresh_token: true
      secret_required: false
      access_token_expiration: 3600
  secret: secret
  active: true
  id: smart-app
  allowed-scopes:
  - {resourceType: Scope, id: patient-medication-rs}
  - {resourceType: Scope, id: patient-allergyintolerance-rs}
  - {resourceType: Scope, id: patient-careplan-rs}
  - {resourceType: Scope, id: patient-careteam-rs}
  - {resourceType: Scope, id: patient-condition-rs}
  - {resourceType: Scope, id: patient-device-rs}
  - {resourceType: Scope, id: patient-diagnosticreport-rs}
  - {resourceType: Scope, id: patient-documentreference-rs}
  - {resourceType: Scope, id: patient-encounter-rs}
  - {resourceType: Scope, id: patient-goal-rs}
  - {resourceType: Scope, id: patient-immunization-rs}
  - {resourceType: Scope, id: patient-location-rs}
  - {resourceType: Scope, id: patient-medicationrequest-rs}
  - {resourceType: Scope, id: patient-observation-rs}
  - {resourceType: Scope, id: patient-organization-rs}
  - {resourceType: Scope, id: patient-patient-rs}
  - {resourceType: Scope, id: patient-practitioner-rs}
  - {resourceType: Scope, id: patient-procedure-rs}
  - {resourceType: Scope, id: patient-provenance-rs}
  - {resourceType: Scope, id: patient-practitionerrole-rs}
  - {resourceType: Scope, id: patient-medication-read}
  - {resourceType: Scope, id: patient-allergyintolerance-read}
  - {resourceType: Scope, id: patient-careplan-read}
  - {resourceType: Scope, id: patient-careteam-read}
  - {resourceType: Scope, id: patient-condition-read}
  - {resourceType: Scope, id: patient-device-read}
  - {resourceType: Scope, id: patient-diagnosticreport-read}
  - {resourceType: Scope, id: patient-documentreference-read}
  - {resourceType: Scope, id: patient-encounter-read}
  - {resourceType: Scope, id: patient-goal-read}
  - {resourceType: Scope, id: patient-immunization-read}
  - {resourceType: Scope, id: patient-location-read}
  - {resourceType: Scope, id: patient-medicationrequest-read}
  - {resourceType: Scope, id: patient-observation-read}
  - {resourceType: Scope, id: patient-organization-read}
  - {resourceType: Scope, id: patient-patient-read}
  - {resourceType: Scope, id: patient-practitioner-read}
  - {resourceType: Scope, id: patient-procedure-read}
  - {resourceType: Scope, id: patient-provenance-read}
  - {resourceType: Scope, id: patient-practitionerrole-read}
  - {resourceType: Scope, id: openid}
  - {resourceType: Scope, id: launch-patient}
  - {resourceType: Scope, id: fhiruser}
  - {resourceType: Scope, id: offline-access}
```

## Run Inferno test session

Create Inferno test session by following the link [https://inferno.healthit.gov/onc-certification-g10-test-kit](https://inferno.healthit.gov/onc-certification-g10-test-kit).

### 1 Standalone Patient App - Full Access

1. Click the `Standalone Patient App` link in the left sidebar
2. Click the `Run tests` button
3. Provide require parameters for tests
   * FHIR Endpoint:\
     `[AIDBOX_BASE_URL]/patient/fhir`
   * Standalone Client ID: `smart-app`
   * Standalone Client Secret: `secret`
4. Click the `Submit` button

Once you run tests, follow the Inferno instructions.

{% hint style="info" %}
Use  `my-user` as a login and `password` as a password to enter, if Aidbox asks to provide user credentials during the SMART App launch.
{% endhint %}

### 2 Standalone Patient App - Limited Access

{% hint style="warning" %}
This test depends on the `Standalone Patient App` test. Pass the first sequence then continue that one
{% endhint %}

1. Click the `2 Limited Access App` link in the left sidebar
2. Click the `Run tests` button
3. Click the `Submit` button

Once you run tests, follow the Inferno instructions.

{% hint style="info" %}
By default the test expects to not get access to all the resources but `Patient`, `Condition`, `Observation`.

To pass the test you should:

1. Uncheck all the resources but those ones on the Consent screen
2. Keep following check boxes checked `Launch Patient`, `Open ID`, `FHIR User` and `Offline Access`
{% endhint %}
