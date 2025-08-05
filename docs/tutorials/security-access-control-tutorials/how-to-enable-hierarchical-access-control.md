---
description: >-
  This guide explains how multi-tenancy on the Organization resources can be
  enabled
---

# How to Enable Hierarchical Access Control

Since Aidbox version 2412, to enable OrgBAC in [FHIRSchema mode](../../modules/profiling-and-validation/fhir-schema-validator/), use:

```
BOX_FEATURES_ORGBAC_ENABLE=true
```

If your Aidbox version is lower or you do not use FHIRSchema mode, use [the Aidbox Configuration project](../../deprecated/deprecated/zen-related/aidbox-zen-lang-project/) and import `aidbox.multitenancy.v1.fhir-r4`or `aidbox.multitenancy.v1.fhir-r5`namespace.

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

#### Patient is not visible from the nested Organization (org-c)

{% code title="status: 403" %}
```yaml
GET /Organization/org-c/fhir/Patient/pt-1
```
{% endcode %}

## Configuring AccessPolicies

To allow some user/client to interact with a organization-based resources, AccessPolicy should be configured to check the organization id from the `https://aidbox.app/tenant-organization-id` extension of the User/Client resource.

This example allows an org-based user (created by `PUT /Organization/<org-id>/fhir/User`) to see patients that are also created in the same organization.

{% tabs %}
{% tab title="Recommended way" %}
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
{% endtab %}

{% tab title="Correct Aidbox format is false" %}
**We do not recommend setting** [**Correct Aidbox format**](../../reference/settings/fhir.md#fhir.validation.correct-aidbox-format) **to false.**

<pre><code><strong>PUT /AccessPolicy/as-user-allow-org-patients
</strong>
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
          valueReference:
            $reference:
              id: .params.organization/id
</code></pre>
{% endtab %}
{% endtabs %}
