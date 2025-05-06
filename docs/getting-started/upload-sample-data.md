---
description: This page explain how you can load synthetic data to Aidbox
---

# Upload Sample Data

### Datasets

Pre-built datasets:

* 100 Patient records:   200K resources and 300MB
* 1K Patient records:   2M resources and 3GB
* 10K Patient records:   20M resources and 30GB
* 100K Patient records:   200M resources and 300GB

Contained resources:  Patient, AllergyIntolerance, CarePlan, CareTeam, Claim, Condition, Device, DiagnosticReport, DocumentReference, Encounter, ExplanationOfBenefit, ImagingStudy, Immunization, Location, Medication, MedicationAdministration, MedicationRequest, Observation, Organization, Practitioner, PractitionerRole, Procedure, Provenance, SupplyDelivery.

### Load dataset

To load the dataset use [/v2/fhir/$import](../api/bulk-api/usdimport-and-fhir-usdimport.md#v2-usdimport-on-top-of-the-workflow-engine) operation.&#x20;

Run the following request in the REST console and wait a few minutes.&#x20;

You can track import status in `Workflow Engine` interface.

**FHIR R4 dataset:**

{% tabs %}
{% tab title="100 ~ 300MB" %}
{% hint style="success" %}
This dataset can be used for local development and cloud sandboxes.
{% endhint %}

```yaml
POST /v2/fhir/$import
Accept: text/yaml
Content-Type: text/yaml

id: synthea-100
contentEncoding: gzip
inputs:
- resourceType: AllergyIntolerance
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100/fhir/AllergyIntolerance.ndjson.gz
- resourceType: CarePlan
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100/fhir/CarePlan.ndjson.gz
- resourceType: CareTeam
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100/fhir/CareTeam.ndjson.gz
- resourceType: Claim
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100/fhir/Claim.ndjson.gz
- resourceType: Condition
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100/fhir/Condition.ndjson.gz
- resourceType: Device
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100/fhir/Device.ndjson.gz
- resourceType: DiagnosticReport
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100/fhir/DiagnosticReport.ndjson.gz
- resourceType: DocumentReference
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100/fhir/DocumentReference.ndjson.gz
- resourceType: Encounter
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100/fhir/Encounter.ndjson.gz
- resourceType: ExplanationOfBenefit
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100/fhir/ExplanationOfBenefit.ndjson.gz
- resourceType: ImagingStudy
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100/fhir/ImagingStudy.ndjson.gz
- resourceType: Immunization
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100/fhir/Immunization.ndjson.gz
- resourceType: Location
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100/fhir/Location.ndjson.gz
- resourceType: Medication
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100/fhir/Medication.ndjson.gz
- resourceType: MedicationAdministration
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100/fhir/MedicationAdministration.ndjson.gz
- resourceType: MedicationRequest
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100/fhir/MedicationRequest.ndjson.gz
- resourceType: Observation
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100/fhir/Observation.ndjson.gz
- resourceType: Organization
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100/fhir/Organization.ndjson.gz
- resourceType: Patient
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100/fhir/Patient.ndjson.gz
- resourceType: Practitioner
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100/fhir/Practitioner.ndjson.gz
- resourceType: PractitionerRole
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100/fhir/PractitionerRole.ndjson.gz
- resourceType: Procedure
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100/fhir/Procedure.ndjson.gz
- resourceType: Provenance
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100/fhir/Provenance.ndjson.gz
- resourceType: SupplyDelivery
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100/fhir/SupplyDelivery.ndjson.gz
```
{% endtab %}

{% tab title="1K ~ 3GB" %}
{% hint style="success" %}
This dataset can be used for local development and cloud sandboxes.
{% endhint %}

```yaml
POST /v2/fhir/$import
Accept: text/yaml
Content-Type: text/yaml

id: synthea-1000
contentEncoding: gzip
inputs:
- resourceType: AllergyIntolerance
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/1000/fhir/AllergyIntolerance.ndjson.gz
- resourceType: CarePlan
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/1000/fhir/CarePlan.ndjson.gz
- resourceType: CareTeam
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/1000/fhir/CareTeam.ndjson.gz
- resourceType: Claim
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/1000/fhir/Claim.ndjson.gz
- resourceType: Condition
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/1000/fhir/Condition.ndjson.gz
- resourceType: Device
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/1000/fhir/Device.ndjson.gz
- resourceType: DiagnosticReport
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/1000/fhir/DiagnosticReport.ndjson.gz
- resourceType: DocumentReference
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/1000/fhir/DocumentReference.ndjson.gz
- resourceType: Encounter
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/1000/fhir/Encounter.ndjson.gz
- resourceType: ExplanationOfBenefit
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/1000/fhir/ExplanationOfBenefit.ndjson.gz
- resourceType: ImagingStudy
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/1000/fhir/ImagingStudy.ndjson.gz
- resourceType: Immunization
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/1000/fhir/Immunization.ndjson.gz
- resourceType: Location
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/1000/fhir/Location.ndjson.gz
- resourceType: Medication
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/1000/fhir/Medication.ndjson.gz
- resourceType: MedicationAdministration
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/1000/fhir/MedicationAdministration.ndjson.gz
- resourceType: MedicationRequest
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/1000/fhir/MedicationRequest.ndjson.gz
- resourceType: Observation
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/1000/fhir/Observation.ndjson.gz
- resourceType: Organization
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/1000/fhir/Organization.ndjson.gz
- resourceType: Patient
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/1000/fhir/Patient.ndjson.gz
- resourceType: Practitioner
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/1000/fhir/Practitioner.ndjson.gz
- resourceType: PractitionerRole
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/1000/fhir/PractitionerRole.ndjson.gz
- resourceType: Procedure
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/1000/fhir/Procedure.ndjson.gz
- resourceType: Provenance
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/1000/fhir/Provenance.ndjson.gz
- resourceType: SupplyDelivery
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/1000/fhir/SupplyDelivery.ndjson.gz
```
{% endtab %}

{% tab title="10K ~ 30GB" %}
```yaml
POST /v2/fhir/$import
Accept: text/yaml
Content-Type: text/yaml

id: synthea-10000
contentEncoding: gzip
inputs:
- resourceType: AllergyIntolerance
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/10000/fhir/AllergyIntolerance.ndjson.gz
- resourceType: CarePlan
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/10000/fhir/CarePlan.ndjson.gz
- resourceType: CareTeam
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/10000/fhir/CareTeam.ndjson.gz
- resourceType: Claim
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/10000/fhir/Claim.ndjson.gz
- resourceType: Condition
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/10000/fhir/Condition.ndjson.gz
- resourceType: Device
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/10000/fhir/Device.ndjson.gz
- resourceType: DiagnosticReport
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/10000/fhir/DiagnosticReport.ndjson.gz
- resourceType: DocumentReference
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/10000/fhir/DocumentReference.ndjson.gz
- resourceType: Encounter
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/10000/fhir/Encounter.ndjson.gz
- resourceType: ExplanationOfBenefit
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/10000/fhir/ExplanationOfBenefit.ndjson.gz
- resourceType: ImagingStudy
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/10000/fhir/ImagingStudy.ndjson.gz
- resourceType: Immunization
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/10000/fhir/Immunization.ndjson.gz
- resourceType: Location
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/10000/fhir/Location.ndjson.gz
- resourceType: Medication
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/10000/fhir/Medication.ndjson.gz
- resourceType: MedicationAdministration
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/10000/fhir/MedicationAdministration.ndjson.gz
- resourceType: MedicationRequest
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/10000/fhir/MedicationRequest.ndjson.gz
- resourceType: Observation
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/10000/fhir/Observation.ndjson.gz
- resourceType: Organization
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/10000/fhir/Organization.ndjson.gz
- resourceType: Patient
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/10000/fhir/Patient.ndjson.gz
- resourceType: Practitioner
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/10000/fhir/Practitioner.ndjson.gz
- resourceType: PractitionerRole
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/10000/fhir/PractitionerRole.ndjson.gz
- resourceType: Procedure
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/10000/fhir/Procedure.ndjson.gz
- resourceType: Provenance
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/10000/fhir/Provenance.ndjson.gz
- resourceType: SupplyDelivery
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/10000/fhir/SupplyDelivery.ndjson.gz

```
{% endtab %}

{% tab title="100K ~ 300GB" %}
{% hint style="warning" %}
This dataset required around 300GB of Database storage size!
{% endhint %}

```yaml
POST /v2/fhir/$import
Accept: text/yaml
Content-Type: text/yaml

id: synthea-100000
contentEncoding: gzip
inputs:
- resourceType: AllergyIntolerance
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100000/fhir/AllergyIntolerance.ndjson.gz
- resourceType: CarePlan
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100000/fhir/CarePlan.ndjson.gz
- resourceType: CareTeam
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100000/fhir/CareTeam.ndjson.gz
- resourceType: Claim
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100000/fhir/Claim.ndjson.gz
- resourceType: Condition
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100000/fhir/Condition.ndjson.gz
- resourceType: Device
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100000/fhir/Device.ndjson.gz
- resourceType: DiagnosticReport
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100000/fhir/DiagnosticReport.ndjson.gz
- resourceType: DocumentReference
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100000/fhir/DocumentReference.ndjson.gz
- resourceType: Encounter
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100000/fhir/Encounter.ndjson.gz
- resourceType: ExplanationOfBenefit
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100000/fhir/ExplanationOfBenefit.ndjson.gz
- resourceType: ImagingStudy
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100000/fhir/ImagingStudy.ndjson.gz
- resourceType: Immunization
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100000/fhir/Immunization.ndjson.gz
- resourceType: Location
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100000/fhir/Location.ndjson.gz
- resourceType: Medication
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100000/fhir/Medication.ndjson.gz
- resourceType: MedicationAdministration
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100000/fhir/MedicationAdministration.ndjson.gz
- resourceType: MedicationRequest
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100000/fhir/MedicationRequest.ndjson.gz
- resourceType: Observation
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100000/fhir/Observation.ndjson.gz
- resourceType: Organization
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100000/fhir/Organization.ndjson.gz
- resourceType: Patient
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100000/fhir/Patient.ndjson.gz
- resourceType: Practitioner
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100000/fhir/Practitioner.ndjson.gz
- resourceType: PractitionerRole
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100000/fhir/PractitionerRole.ndjson.gz
- resourceType: Procedure
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100000/fhir/Procedure.ndjson.gz
- resourceType: Provenance
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100000/fhir/Provenance.ndjson.gz
- resourceType: SupplyDelivery
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/100000/fhir/SupplyDelivery.ndjson.gz
```
{% endtab %}
{% endtabs %}

**FHIR R6 dataset with 100 patients:**

```yaml
POST /v2/fhir/$import
Accept: text/yaml
Content-Type: text/yaml

id: synthea-r6-100
contentEncoding: gzip
inputs:
- resourceType: Appointment
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/R6/100/fhir/Appointment.ndjson.gz
- resourceType: Claim
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/R6/100/fhir/Claim.ndjson.gz
- resourceType: Condition
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/R6/100/fhir/Condition.ndjson.gz
- resourceType: DiagnosticReport
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/R6/100/fhir/DiagnosticReport.ndjson.gz
- resourceType: Encounter
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/R6/100/fhir/Encounter.ndjson.gz
- resourceType: Immunization
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/R6/100/fhir/Immunization.ndjson.gz
- resourceType: Location
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/R6/100/fhir/Location.ndjson.gz
- resourceType: Medication
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/R6/100/fhir/Medication.ndjson.gz
- resourceType: Observation
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/R6/100/fhir/Observation.ndjson.gz
- resourceType: Organization
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/R6/100/fhir/Organization.ndjson.gz
- resourceType: Patient
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/R6/100/fhir/Patient.ndjson.gz
- resourceType: Practitioner
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/R6/100/fhir/Practitioner.ndjson.gz
- resourceType: PractitionerRole
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/R6/100/fhir/PractitionerRole.ndjson.gz
- resourceType: Procedure
  url: https://storage.googleapis.com/aidbox-public/synthea/v2/R6/100/fhir/Procedure.ndjson.gz
```
