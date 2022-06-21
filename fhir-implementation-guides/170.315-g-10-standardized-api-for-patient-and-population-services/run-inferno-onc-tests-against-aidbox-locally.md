---
description: How to setup Aidbox and Inferno ONC locally to pass the tests
---

# 🎓 Run Inferno ONC tests against Aidbox locally

## Install Devbox

Follow `Step 1,` `Step 2` and `Step 3` of the instruction to [install Devbox](https://docs.aidbox.app/getting-started/installation/setup-aidbox.dev)

## Extend `.env` file

In addition of the `Step 3` of the [instruction](https://docs.aidbox.app/getting-started/installation/setup-aidbox.dev) define following values in the `.env` file.

```
# Define aidbox parameters
# (https://docs.aidbox.app/getting-started/installation/setup-aidbox.dev#install-devbox)
AIDBOX_BASE_URL=http://host.docker.internal:8888
AIDBOX_CREATED_AT_URL=http://example.com/createdat
AIDBOX_PORT=8888
AIDBOX_FHIR_VERSION=4.0.1
AIDBOX_COMPLIANCE=enabled

# Define zen project
BOX_PROJECT_GIT_PROTOCOL=https
BOX_PROJECT_GIT_URL=https://github.com/Aidbox/aidbox-project-samples.git
BOX_PROJECT_GIT_SUB__PATH=aidbox-project-samples
BOX_PROJECT_ENTRYPOINT=smartbox.portal/box

# Define access to you the bucket (https://docs.aidbox.app/api-1/bulk-api-1/usdexport#setup-storage)
box_bulk__storage_backend=
box_bulk__storage_gcp_service__account=
box_bulk__storage_gcp_bucket=
```

## Run Devbox

Execute the command bellow to run Devbox.

```bash
docker-compose up -d
```

## Data samples for ONC certification

On startup Aidbox populates necessary resources Inferno validates across the tests:

* `Patient`
* `AllergyIntolerance`
* `CarePlan`
* `CareTeam`
* `Condition`
* `Device`
* `DiagnosticReport`
* `DocumentReference`
* `Goal`
* `Encounter`
* `Immunization`
* `MedicationRequest`
* `Observation`
* `Organization`
* `Practitioner`
* `Procedure`
* `Provenance`
* `Location`

## Configure Aidbox

To let Inferno connect to the Aidbox we should add Application (`Client` in terms of Aidbox).

### Login to Aidbox

1. Open Aidbox UI in your web-browser `http://localhost:8888`
2. Enter `login` and `password` being defined in `AIDBOX_ADMIN_ID` and `AIDBOX_ADMIN_PASSWORD`
3. Get to the [REST Console](../../aidbox-ui/rest-console-1.md)

### Create `Client` resource

In the `REST Console` run the command to create the `Client`.

```yaml
PUT /Client/inferno-g10-client

id: inferno-g10-client
resourceType: Client
auth:
  authorization_code:
    pkce: false
    audience:ml
    - http://host.docker.internal:8888/smart
    redirect_uri: http://localhost/custom/smart/redirect
    refresh_token: true
    secret_required: false
    access_token_expiration: 36000
smart:
  launch_uri: http://localhost/custom/smart/launch
secret: inferno-secret
grant_types:
- authorization_code
- basic
```

## Launch Inferno

Follow [this](https://github.com/onc-healthit/onc-certification-g10-test-kit#local-installation-instruction) instruction to install and run Inferno locally.

In the Inferno UI (`http://localhost`) select `ONC Certification (g)(10) Standardized API` test suite and launch the tests.
