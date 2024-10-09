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

Aidbox is configured by the [Aidbox Configuration Project.](../../../aidbox-configuration/aidbox-zen-lang-project/) To create sample project run command below.&#x20;

Note that to use hierarchical access control with the [FHIR Schema validator](../../profiling-and-validation/fhir-schema-validator/), you need to clone the `orgbac-with-fhir-schema` branch, which has the FHIR Schema validator enabled and the Aidbox Configuration Project configured.

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

{% tab title="FHIR Schema R4" %}
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

{% tab title="FHIR Schema R5" %}
<pre class="language-bash"><code class="lang-bash"><strong>git clone \
</strong>  --branch=orgbac-with-fhir-schema-r5 \
  --depth=1 \
  https://github.com/Aidbox/aidbox-project-template.git \
  aidbox-project &#x26;&#x26; \
  cd aidbox-project &#x26;&#x26; \
  rm -rf .git
</code></pre>
{% endtab %}
{% endtabs %}

### Apply the license

Populate the `.env` file with the Aidbox License.&#x20;

{% code title=".env" %}
```ini
AIDBOX_LICENSE=YOUR_AIDBOX_LICENSE_KEY

...
```
{% endcode %}

## Enable multi-tenancy

To enable hierarchical access control (multi-tenancy on Organization resources) ensure that  necessary imports are present in `zrc/main.edn` file.

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
  reference: Organization/org-a
name: Organization B
```
{% endcode %}

#### Grant-child organization

{% code title="status: 201 (created)" %}
```yaml
PUT /fhir/Organization/org-c

partOf:
  reference: Organization/org-b
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

{% code title="status: 201 (created)" %}
```yaml
PUT /Organization/org-b/fhir/Patient/pt-1
```
{% endcode %}

### Check access control works

#### Patient is visible from the Organization above (org-a)

{% code title="status: 200" %}
```yaml
GET /Organization/org-a/fhir/Patient/pt-1
```
{% endcode %}

#### Patient is visible from its Organization (org-b)

{% code title="status: 200" %}
```yaml
GET /Organization/org-b/fhir/Patient/pt-1
```
{% endcode %}

#### Patient is not visible from the nested Organization (org-c)&#x20;

{% code title="status: 403" %}
```yaml
GET /Organization/org-c/fhir/Patient/pt-1
```
{% endcode %}

## Configuring AccessPolicies

To allow some user/client to interact with a organization-based resources, AccessPolicy should be configured to check organization id from the `https://aidbox.app/tenant-organization-id` extension of User/Client resource.

This example allows org-based user (created by `PUT /Organization/<org-id>/fhir/User`) to see patients that are also created by OrgBAC.

```
PUT /AccessPolicy/as-user-allow-org-patients

description: A user should be able to get every patient in their organization.
engine: matcho
matcho:
  params:
    resource/type: Patient
  request-method: get
  user:
    meta:
      extension:
        $contains:
          url: https://aidbox.app/tenant-organization-id
          value:
            Reference:
              id: .params.organization/id
```
