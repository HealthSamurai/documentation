---
description: >-
  This guide explains how multi-tenancy on the Organization resources can be
  enabled
---

# How to enable hierarchical access control

## Prerequisites

### Docker and Docker Compose

You should have Docker and Docker Compose installed before go further. To get it installed follow the [instructions](https://docs.docker.com/engine/install/).

### Aidbox license

To get the Aidbox License:

1. Go the Aidbox user portal [https://aidbox.app](https://aidbox.app/)
2. Login to the portal
3. Create new **self-hosted** Aidbox License or use the license that you already have

## Create Aidbox project

Aidbox is configured by the [Aidbox Configuration Project.](../../../aidbox-configuration/aidbox-zen-lang-project/) To create sample project run command below&#x20;

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

{% tab title="FHIR R5" %}
```shell
git clone \
  --branch=fhir-r5 \
  --depth=1 \
  https://github.com/Aidbox/aidbox-project-template.git \
  aidbox-project && \
  cd aidbox-project && \
  rm -rf .git
```
{% endtab %}
{% endtabs %}

### OrgBAC with FHIR Schema validator

To use hierarchical access control with the [FHIR Schema validator](../../profiling-and-validation/fhir-schema-validator.md), you need to have [Aidbox Configuration Project](../../../aidbox-configuration/aidbox-zen-lang-project/) and enable the [FHIR Schema validator](../../profiling-and-validation/fhir-schema-validator.md). To create a sample project with hierarchical access control and the FHIR Schema validator, run the command below. This will clone the `orgbac-with-fhir-schema` branch, which has the FHIR Schema validator enabled and the Aidbox Configuration Project configured:

{% tabs %}
{% tab title="FHIR R4" %}
```sh
git clone \
  --branch=orgbac-with-fhir-schema \
  --depth=1 \
  https://github.com/Aidbox/aidbox-project-template.git \
  aidbox-project && \
  cd aidbox-project && \
  rm -rf .git
```
{% endtab %}
{% endtabs %}

{% hint style="info" %}
See more details related the [running Aidbox locally](../../../getting-started/run-aidbox-locally-with-docker/).
{% endhint %}

### Apply the license

Populate the `.env` file with the Aidbox License.&#x20;

{% code title=".env" %}
```ini
AIDBOX_LICENSE=YOUR_AIDBOX_LICENSE_KEY

...
```
{% endcode %}

## Enable multi-tenancy

To enable hierarchical access control (multi-tenancy on Organization resources) add necessary imports to the `zrc/main.edn` file.

{% tabs %}
{% tab title="FHIR R4" %}
Add `aidbox.multitenancy.v1.fhir-r4` to the import section.

{% code title="zrc/main.edn" %}
```clojure
{ns main
 import #{aidbox
          aidbox.multitenancy.v1.fhir-r4 ;; import multitenancy
          config}
 
 box
 {:zen/tags #{aidbox/system}
  :config   config/base-config
  :services {:admin-user-seed config/admin-user-seed
             :root-client-seed config/root-client-seed}}}
```
{% endcode %}
{% endtab %}

{% tab title="FHIR R5" %}
Add `aidbox.multitenancy.v1.fhir-r5` to the import section.

{% code title="zrc/main.edn" %}
```clojure
{ns main
 import #{aidbox
          aidbox.multitenancy.v1.fhir-r5 ;; import multitenancy
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

## Ensure the hierarchical access control works

### Create nested Organization resources

Use Aidbox UI Rest Console to create nested Organization resources.

#### Root organization

{% code title="status: 201 (created)" %}
```yaml
PUT /fhir/Organization/org-a

name: Organization A
```
{% endcode %}

#### Child organization

{% code title="status: 201 (created)" %}
```yaml
PUT /fhir/Organization/org-b

partOf:
  resourceType: Organization
  id: org-a
name: Organization B
```
{% endcode %}

#### Grant-child organization

{% code title="status: 201 (created)" %}
```yaml
PUT /fhir/Organization/org-c

partOf:
  resourceType: Organization
  id: org-b
name: Organization C
```
{% endcode %}

You should have 3 nested organizations for now

```
org-a
└── org-b
   └── org-c
```

### Create resource in the Organization B

Use Aidbox UI Rest Console to create Patient resource in the organization B.

{% tabs %}
{% tab title="FHIR API" %}
{% code title="status: 201 (created)" %}
```yaml
PUT /Organization/org-b/fhir/Patient/pt-1
```
{% endcode %}
{% endtab %}

{% tab title="Aidbox API" %}
{% code title="status: 201 (created)" %}
```yaml
PUT /Organization/org-b/aidbox/Patient/pt-1
```
{% endcode %}
{% endtab %}
{% endtabs %}

### Check access control works

#### Patient is visible from the Organization above (org-a)

{% tabs %}
{% tab title="FHIR API" %}
{% code title="status: 200" %}
```yaml
GET /Organization/org-a/fhir/Patient/pt-1
```
{% endcode %}
{% endtab %}

{% tab title="Aidbox API" %}
{% code title="status: 200" %}
```yaml
GET /Organization/org-a/aidbox/Patient/pt-1
```
{% endcode %}
{% endtab %}
{% endtabs %}

#### Patient is visible from its Organization (org-b)

{% tabs %}
{% tab title="FHIR API" %}
{% code title="status: 200" %}
```yaml
GET /Organization/org-b/fhir/Patient/pt-1
```
{% endcode %}
{% endtab %}

{% tab title="Aidbox API" %}
{% code title="status: 200" %}
```yaml
GET /Organization/org-b/aidbox/Patient/pt-1
```
{% endcode %}
{% endtab %}
{% endtabs %}

#### Patient is not visible from the nested Organization (org-c)&#x20;

{% tabs %}
{% tab title="FHIR API" %}
{% code title="status: 403" %}
```yaml
GET /Organization/org-c/fhir/Patient/pt-1
```
{% endcode %}
{% endtab %}

{% tab title="Aidbox API" %}
{% code title="status: 403" %}
```yaml
GET /Organization/org-c/aidbox/Patient/pt-1
```
{% endcode %}
{% endtab %}
{% endtabs %}
