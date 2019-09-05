---
description: In this guide we will generate synthea data and load it into aidbox
---

# 🎓 Synthea by Bulk APi

### Generate synthea data

We gonna generate synthetic data with [synthea](https://github.com/synthetichealth/synthea) project:

```bash
# brew install gradle
git clone https://github.com/synthetichealth/synthea
cd synthea

# edit src/main/resources/synthea.properties
# set exporter.fhir.bulk_data = true

# generate 100 pts
./run_synthea -p 100

cd output/fhir
ls -lah
# create all.ndjson
cat *.ndjson > all.ndjson

# gzip all ndjson
gzip *.ndjson
ls -lah

#load to storage
gsutil cp *.ndjson.gz  gs://your-bucket/dir/
```

### Load by Resource Type

Now we can load for example Patients and Observations into your box:

```yaml
POST /fhir/Patient/$load

source: 'https://storage.googleapis.com/aidbox-public/synthea/100/Patient.ndjson.gz'

#resp
{total: 124}
```

```yaml
POST /fhir/Observation/$load

source: 'https://storage.googleapis.com/aidbox-public/synthea/100/Observation.ndjson.gz'

#resp
{total: 20382}
```

Let see we have data in aidbox:

```yaml
GET /Patient?_ilike=John&_revinclude=Observation:patient
```

### Load all at once with /$load

Using /$load you can load ndjson file with multiple resource types in one step:

```yaml
POST /$load

source: 'https://storage.googleapis.com/aidbox-public/synthea/100/all.ndjson.gz'

# resp

{CarePlan: 356, Observation: 20382, MedicationAdministration: 150, Goal: 301, Patient: 124, DiagnosticReport: 1430, Practitioner: 181, ExplanationOfBenefit: 3460, Immunization: 1636, Claim: 4488, MedicationRequest: 1028, Encounter: 3460, Condition: 871, Procedure: 2854, Organization: 181, AllergyIntolerance: 40, ImagingStudy: 134}
```

Let's see database stats:

```sql
SELECT relname, reltuples 
FROM pg_class r 
 JOIN pg_namespace n ON (relnamespace = n.oid) 
WHERE relkind = 'r' AND n.nspname = 'public'
order by reltuples desc
LIMIT 20

--- 

observation	20382
attribute	7257
claim	    4488
encounter	3460
```

### Cleanup data:

Truncate tables from db console:

```sql
truncate CarePlan;
truncate Observation;
truncate MedicationAdministration
truncate Goal;
truncate Patient;
truncate DiagnosticReport;
truncate Practitioner;
truncate ExplanationOfBenefit;
truncate Immunization;
truncate Claim;
truncate MedicationRequest;
truncate Encounter;
truncate Condition;
truncate "procedure";
truncate Organization;
truncate AllergyIntolerance;
truncate ImagingStudy;

```

### Load with Bulk $import

To load data in a async way using new FHIR Bulk $import:

```yaml
POST /fhir/$import

id: synthea
inputFormat: application/fhir+ndjson
contentEncoding: gzip
mode: bulk
inputs:
- resourceType: AllergyIntolerance
  url: https://storage.googleapis.com/aidbox-public/synthea/100/AllergyIntolerance.ndjson.gz
- resourceType: CarePlan
  url: https://storage.googleapis.com/aidbox-public/synthea/100/CarePlan.ndjson.gz
- resourceType: Claim
  url: https://storage.googleapis.com/aidbox-public/synthea/100/Claim.ndjson.gz
- resourceType: Condition
  url: https://storage.googleapis.com/aidbox-public/synthea/100/Condition.ndjson.gz
- resourceType: DiagnosticReport
  url: https://storage.googleapis.com/aidbox-public/synthea/100/DiagnosticReport.ndjson.gz
- resourceType: Encounter
  url: https://storage.googleapis.com/aidbox-public/synthea/100/Encounter.ndjson.gz
- resourceType: ExplanationOfBenefit
  url: https://storage.googleapis.com/aidbox-public/synthea/100/ExplanationOfBenefit.ndjson.gz
- resourceType: Goal
  url: https://storage.googleapis.com/aidbox-public/synthea/100/Goal.ndjson.gz
- resourceType: ImagingStudy
  url: https://storage.googleapis.com/aidbox-public/synthea/100/ImagingStudy.ndjson.gz
- resourceType: Immunization
  url: https://storage.googleapis.com/aidbox-public/synthea/100/Immunization.ndjson.gz
- resourceType: MedicationAdministration
  url: https://storage.googleapis.com/aidbox-public/synthea/100/MedicationAdministration.ndjson.gz
- resourceType: MedicationRequest
  url: https://storage.googleapis.com/aidbox-public/synthea/100/MedicationRequest.ndjson.gz
- resourceType: Observation
  url: https://storage.googleapis.com/aidbox-public/synthea/100/Observation.ndjson.gz
- resourceType: Organization
  url: https://storage.googleapis.com/aidbox-public/synthea/100/Organization.ndjson.gz
- resourceType: Patient
  url: https://storage.googleapis.com/aidbox-public/synthea/100/Patient.ndjson.gz
- resourceType: Practitioner
  url: https://storage.googleapis.com/aidbox-public/synthea/100/Practitioner.ndjson.gz
- resourceType: Procedure
  url: https://storage.googleapis.com/aidbox-public/synthea/100/Procedure.ndjson.gz
```

Operation will return 200  instantly and you can monitor status of import with:

```yaml
GET /BulkImportStatus/synthea
```

