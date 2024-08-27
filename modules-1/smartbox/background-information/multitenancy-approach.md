---
description: The article explains Smartbox approach for multitenancy
---

# Multitenancy approach

Smartbox is distributed as an aidbox configuration project on top of Aidbox. It follows [Aidbox multitenancy approach](../../../security-and-access-control-1/multitenancy/) on API constructor.

All data is stored in one Postgres database. Multitenancy is achived by Smartbox on couple of levels:

* on API level Smartbox introduced a FHIR API for every tenant, and
* on data level Aidbox expects every tenant resource is marked with tenant reference.

Smartbox introduced Tenant resource in Aidbox. By creating a Tenant resource you enable FHIR APIs with SMART on FHIR support for patients and providers.

```yaml
id: my-clinic
resourceType: Tenant
name: My Clinic
```

{% hint style="info" %}
You may be confused as you don't see Tenant resource on entities page in Aidbox Console. It happens because Tenant resource is defined in aidbox configuration project within zen-lang. You may follow profiles page and find Tenant resource definition in `smartbox.multitenancy` namespace.
{% endhint %}

Every FHIR resource from [us-core IG](http://hl7.org/fhir/us/core/STU3.1.1/) is enhanced with reference to a tenant. E.g. Patient resource:

```yaml
id: pt-1
resourceType: Patient
...
meta:
  tenant:
    id: my-clinic
    resourceType: Tenant
```

### FHIR API and portal for patients

Once you created Tenant resource, FHIR API, a dedicated authorization server with SMART on FHIR support and patient portal were enabled for the the tenant.

#### Patient portal

Patient portal is available on

`[aidbox-base-url]/tenant/[tenant-id]/patient/portal`

Patient portal provides to patient an ability

* to authorize access to their EHI data for external SMART apps and revoke the access
* change and reset their password.

#### FHIR API for patient-facing SMART apps

FHIR API for patient-facing SMART apps is avaiable on

`[aidbox-base-url]/tenant/[tenant-id]/patient/smart-api`

Patient doesn't have direct access to that API, but only via SMART apps, authorized by the patient.

#### Authorization server

Authorization server is available on

`[aidbox-base-url]/tenant/[tenant-id]/patient/auth`

Why is there dedicated auth server for every tenant? If a person is a patient in two different clinics they should be able to be logged into both patient portals independently. Otherwise it may cause undesirable behaviour when patient wants to launch a SMART app and share their data from both clinics.

### FHIR API for providers

#### FHIR API for provider-facing SMART apps

FHIR API for provider-facing SMART apps is avaiable on

`[aidbox-base-url]/tenant/[tenant-id]/provider/smart-api`

Provider doesn't have direct access to that API, but only via SMART apps, authorized by the provider.

#### Authorization server

Authorization server for patients is available on

`[aidbox-base-url]/tenant/[tenant-id]/provider/auth`

#### Bulk API

Bulk API is available on

`[aidbox-base-url]/tenant/[tenant-id]/bulk-api`

It works in accordance to [Bulk Data Access IG](https://hl7.org/fhir/uv/bulkdata/STU1.0.1/).
