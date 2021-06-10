# $import & /fhir/$import

`$import` is an implementation of the upcoming FHIR Bulk Import API. This is an async Operation, which returns url to monitor progress. Here is self descriptive example:

```yaml
POST /fhir/$import

id: synthea
inputFormat: application/fhir+ndjson
contentEncoding: gzip
mode: bulk
inputs:
- resourceType: Encounter
  url: https://storage.googleapis.com/aidbox-public/synthea/100/Encounter.ndjson.gz
- resourceType: Organization
  url: https://storage.googleapis.com/aidbox-public/synthea/100/Organization.ndjson.gz
- resourceType: Patient
  url: https://storage.googleapis.com/aidbox-public/synthea/100/Patient.ndjson.gz
```

You post import body with id and can monitor progress of import using:

```yaml
GET /BulkImportStatus/[id]
```

## 

