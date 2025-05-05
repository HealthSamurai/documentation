---
description: >-
  This guide explains how safe API for Patients and their resources can be
  enabled
---

# How to enable patient data access API

## Prerequisites

### Docker and Docker Compose

You should have Docker and Docker Compose installed before go further. To get it installed follow the [instructions](https://docs.docker.com/engine/install/).

### Aidbox license

To get the Aidbox License:

1. Go the Aidbox user portal [https://aidbox.app](https://aidbox.app/)
2. Login to the portal
3. Create new **self-hosted** Aidbox License or use the license that you already have

## Create Aidbox project

Aidbox is configured by the [Aidbox Configuration Projects](../../../deprecated/deprecated/zen-related/aidbox-zen-lang-project/). To create sample project run command below&#x20;

{% tabs %}
{% tab title="FHIR R4" %}
```sh
git clone \
  --branch=main \
  --depth=1 \
  https://github.com/Aidbox/aidbox-project-template.git \
  aidbox-project && \
  cd aidbox-project && \
  rm -rf .git
```
{% endtab %}
{% endtabs %}

{% hint style="info" %}
More details at [running Aidbox locally](../../../getting-started/run-aidbox-locally-with-docker.md#using-configuration-project-method)
{% endhint %}

### Apply the license

Populate the `.env` file with the Aidbox License.&#x20;

{% code title=".env" %}
```ini
AIDBOX_LICENSE=YOUR_AIDBOX_LICENSE_KEY
...
```
{% endcode %}

## Enable Patients data access API

To enable safe Patients API add necessary imports to the `zrc/main.edn` file.

{% tabs %}
{% tab title="FHIR R4" %}
Add `aidbox.patient-api.v1` to the import section.

{% code title="zrc/main.edn" %}
```clojure
{ns main
 import #{aidbox
          aidbox.patient-api.v1 ;; import safe API
          config}
 
 box
 {:zen/tags #{aidbox/system}
  :config   config/base-config
  :services {:admin-user-seed config/admin-user-seed
             :root-client-seed config/root-client-seed}}}
```
{% endcode %}
{% endtab %}
{% endtabs %}

## Start Aidbox with Docker Compose

To start Aidbox run the command in the `aidbox-project` directory.

```bash
docker compose up --force-recreate
```

When Aidbox starts, navigate to the [http://localhost:8888](http://localhost:8888/) and sign in to the Aidbox UI using the credentials `admin` / `password`.

## Ensure Patient data access API works

### Create sample resources

Use Aidbox UI Rest Console to create nested Organization resources.

{% code title="status: 200 Ok" %}
```yaml
PUT /
content-type: text/yaml

- id: pt-1
  resourceType: Patient
- id: pt-2
  resourceType: Patient
- id: obs-1
  resourceType: Observation
  status: registered
  code:
    coding:
    - system: http://loinc.org
      code: 15074-8
      display: Glucose [Moles/volume] in Blood
  subject:
    resourceType: Patient
    id: pt-1
```
{% endcode %}

You should have 2 Patients and 1 Observation resources.

```
Patient/pt-1
└── Observation/obs-1

Patient/pt-2
```

### Check safe Patient access API works

#### Patient/pt-1 sees its data&#x20;

{% code title="status: 200" %}
```yaml
GET /patient/fhir/Observation/obs-1
content-type: text/yaml
X-Patient-Id: pt-1
```
{% endcode %}

#### Patient/pt-2 cannot see other Patient's data&#x20;

{% code title="status: 404 Not found" %}
```yaml
GET /patient/fhir/Observation/obs-1
content-type: text/yaml
X-Patient-Id: pt-2
```
{% endcode %}
