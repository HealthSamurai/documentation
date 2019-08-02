---
description: Bulk export & import operations
---

# Bulk API

## $dump 

You can dump all resources of specific type with $dump operation - `GET [resource-type]/$dump` - Aidbox will respond with [Chunked Transfer Encoding](https://en.wikipedia.org/wiki/Chunked_transfer_encoding) [ndjson](http://ndjson.org/) stream. This is memory efficient operation - Aidbox just stream database cursor to socket. If your HTTP Client support processing of Chunked Encoding  you can process resources in stream one by one without waiting for end of response.

```yaml
GET /Patient/$dump

#response

HTTP/1.1 200 OK
Content-Type: application/ndjson
Transfer-Encoding: chunked

{"resourceType": "Patient", "id": .............}
{"resourceType": "Patient", "id": .............}
{"resourceType": "Patient", "id": .............}
{"resourceType": "Patient", "id": .............}
.........
```

## $load

You can efficiently load data into Aidbox  in ndjson format from external service with:

```yaml
POST /$load

source: 'https://storage.googleapis.com/aidbox-public/icd10cm.ndjson.gz'
temp_table: _icd10cm
validation: 
  enable: off # | async | on
  checks: struct # | refs | all
method: copy # | insert
```

## $import & /fhir/$import

To asynchronously import a big portion of data you cant use $import operation. It is now in beta state. The $import request format is

```yaml
POST /fhir/$import
resource:
   id: "imp-testing-1" | nil #can be omitted
   action: "start" | "pause" | "abort" | "resume" | "force-resume"
   #if action is omitted the default value is "start"
   secret: "your-very-secret" #ignored in beta
   #all the items below are ignored for commands other than "start" 
   inputFormat: "application/fhir+ndjson" #only gzipped ndjson files are supported 
   inputSource: "source" #ignored
   inputs:
      - 
        type: "Patient" #resource type
        url:  "/path/to/Patient.ndjson.gz" | "https://your-domain/Patient.ndjson.gz"
        storageDetail:
        type: "resource-file" | "url"
      -
        type: "Encounter"
        url: "/path/to/Encounter.ndjson.gz" | "https://your-domain/Encounter.ndjson.gz" 
        storageDetail: 
        type: "resource-file" | "url"
```

Currently for beta version authorization is not supported, so the operation can be aborted by anyone who knows it's **id**. 

Only **resource-file** and **url** data types are supported. The resource file have to be entered in relative local file system paths \(for using inside the REPL\), **url** type can be used both for local files with absolute path or http urls.

If the **id** field is omitted the default UUID string will be automatically assigned. You can get this name from the response; so please careful, if you lose the response - the only way to get the automatically assigned **id** is by database access.

The response can be successful \(codes 200, 201\) or unsuccessful \(other\).

General errors:

1. If the user is not authorised to **abort \| resume \| pause** a session, status **403 Forbidden** is returned**.** _This is not yet supported._
2. If the request is not well formed status **400 \(Bad request\)** is returned. _This is not yet supported._
3. In the case of internal error **status 500** is returned.

Other error codes depend on the current session state. Session states cannot be directly controlled by the user and listed here for explanatory purposes. Currently the following states for import session are supported: 

1. **active.** The import session is started and still running.
2. **paused**. The session was started and then user-paused.
3. **aborted.** The session was started and then user-aborted.
4. **error**. The session was started but got error during the execution. The error can be caused by server itself, incorrect user input or external conditions beyond the direct control.
5. **done.** The session was started and successfully completed.

Responses on different actions.

1. start
   1. If the session id is supplied and exists in DB in any state - **status 409 \(Conflict\)** is returned.
   2. If the session id is not supplied - it will be auto generated and returned in response with status 201.
   3. The newly created resource url is given in **content-location** field in response headers field.
2. abort
   1. If the session id exists in DB in states **active \| paused** - **status 200**  is returned and session goes to the aborted state. _Currently you cannot abort paused session, if you try status 500 is returned._
   2. If the session id exists in DB in states **aborted \| error \| done** - **status 406 Not Acceptable** is returned and session doesn’t change the state.
   3. If the session id does not exist  - **status 404 \(Not found\)** is returned.
3. resume
   1. If the session id exists in DB in state **paused** - **status 200** is returned and session goes to the active state.
   2. If the session id exists in DB in states **active \| aborted \| error \| done - status 406 Not Acceptable**  is returned and session doesn’t change the state
   3. If the session id does not exist  - **status 404 \(Not found\)** is returned.
4. force-resume. Try this action to restore session from any state but **done \| aborted.** In some cases the _unpredicted behaviour_ can occur. Use this option to try to restore from ungraceful termination. If succeeded the session goes to the active state and restores from the latest possible state.
5. pause
   1. If the session id exists in DB in state active - **status 200** is returned and session goes to the paused state.
   2. If the session id exists in DB in state **paused \| aborted \| error \| done** - **status 406 Not Acceptable** is returned and session doesn’t change the state.
   3. If the session id does not exist  - **status 404 \(Not found\)** is returned.
6. In all the unsuccessful responses possible explanation is given in the **message** field.

### Example

Here we give a short guide how to import previously obtained or generated data to Aidbox.

* Firstly we have prepared ndjson data generated by [synthea](https://github.com/synthetichealth/synthea). You can take a look over synthea params file \(synthea.properties\) to get know how to generate ndjson bulk data \(exporter.fhir.bulk\_data = true\). Then we gzip each file individually.
* The publicly accessible data with 500+ \(518 to be precise\) patients are uploaded here [https://storage.googleapis.com/aidbox-public/500/](https://storage.googleapis.com/aidbox-public/500/)

![](../.gitbook/assets/image.png)

* Now using the Aidbox ui REST console we sent the following request  \(YAML format\)

```yaml
POST /$import

id: "imp-a" 
action: "start" 
inputFormat: "application/fhir+ndjson"
inputSource: "source"
inputs:
       -
        type: "Patient"
        url:  "https://storage.googleapis.com/aidbox-public/500/Patient.ndjson.gz"
        storageDetail:
               type: "url"
       -
        type: "Claim"
        url:  "https://storage.googleapis.com/aidbox-public/500/Claim.ndjson.gz"
        storageDetail:
               type: "url"
       -
        type: "Condition"
        url:  "https://storage.googleapis.com/aidbox-public/500/Condition.ndjson.gz"
        storageDetail:
               type: "url"
       -
        type: "DiagnosticReport"
        url:  "https://storage.googleapis.com/aidbox-public/500/DiagnosticReport.ndjson.gz"
        storageDetail:
               type: "url"
       -
        type: "Encounter"
        url:  "https://storage.googleapis.com/aidbox-public/500/Encounter.ndjson.gz"
        storageDetail:
               type: "url" 
       -
        type: "ExplanationOfBenefit"
        url:  "https://storage.googleapis.com/aidbox-public/500/ExplanationOfBenefit.ndjson.gz"
        storageDetail:
               type: "url"     
       -
        type: "Immunization"
        url:  "https://storage.googleapis.com/aidbox-public/500/Immunization.ndjson.gz"
        storageDetail:
               type: "url"                                   
       -
        type: "MedicationRequest"
        url:  "https://storage.googleapis.com/aidbox-public/500/MedicationRequest.ndjson.gz"
        storageDetail:
               type: "url"     
       -
        type: "Observation"
        url:  "https://storage.googleapis.com/aidbox-public/500/Observation.ndjson.gz"
        storageDetail:
               type: "url"     
       -
        type: "Organization"
        url:  "https://storage.googleapis.com/aidbox-public/500/Organization.ndjson.gz"
        storageDetail:
               type: "url"     
       -
        type: "Practitioner"
        url:  "https://storage.googleapis.com/aidbox-public/500/Practitioner.ndjson.gz"
        storageDetail:
               type: "url"     
       -
        type: "Procedure"
        url:  "https://storage.googleapis.com/aidbox-public/500/Procedure.ndjson.gz"
        storageDetail:
               type: "url"                                                                              
```

After a while the resulted status BulkImportStatus resource given by  GET /BulkImportStatus/imp-a should be as follows

```yaml
meta: {lastUpdated: '2019-07-02T16:26:12.645Z', versionId: '65706'}
action: start
inputs:
- url: https://storage.googleapis.com/aidbox-public/500/Patient.ndjson.gz
  type: Patient
  storageDetail: {type: url}
- url: https://storage.googleapis.com/aidbox-public/500/Claim.ndjson.gz
  type: Claim
  storageDetail: {type: url}
- url: https://storage.googleapis.com/aidbox-public/500/Condition.ndjson.gz
  type: Condition
  storageDetail: {type: url}
- url: https://storage.googleapis.com/aidbox-public/500/DiagnosticReport.ndjson.gz
  type: DiagnosticReport
  storageDetail: {type: url}
- url: https://storage.googleapis.com/aidbox-public/500/Encounter.ndjson.gz
  type: Encounter
  storageDetail: {type: url}
- url: https://storage.googleapis.com/aidbox-public/500/ExplanationOfBenefit.ndjson.gz
  type: ExplanationOfBenefit
  storageDetail: {type: url}
- url: https://storage.googleapis.com/aidbox-public/500/Immunization.ndjson.gz
  type: Immunization
  storageDetail: {type: url}
- url: https://storage.googleapis.com/aidbox-public/500/MedicationRequest.ndjson.gz
  type: MedicationRequest
  storageDetail: {type: url}
- url: https://storage.googleapis.com/aidbox-public/500/Observation.ndjson.gz
  type: Observation
  storageDetail: {type: url}
- url: https://storage.googleapis.com/aidbox-public/500/Organization.ndjson.gz
  type: Organization
  storageDetail: {type: url}
- url: https://storage.googleapis.com/aidbox-public/500/Practitioner.ndjson.gz
  type: Practitioner
  storageDetail: {type: url}
- url: https://storage.googleapis.com/aidbox-public/500/Procedure.ndjson.gz
  type: Procedure
  storageDetail: {type: url}
status:
  jobs:
  - url: https://storage.googleapis.com/aidbox-public/500/Patient.ndjson.gz
    type: Patient
    threads:
    - bytes: {kbs-rate: 668.1975010076582, bytes-read: 1657798, objects-read: 518, symbols-read: 1657759, bytes-written: 1660478, objects-copied: 518}
      state: finished-copying
    tmp-table: temp_patient_33fd2a4f_1513_4581_835f_373bbaefd1a3
  - url: https://storage.googleapis.com/aidbox-public/500/Claim.ndjson.gz
    type: Claim
    threads:
    - bytes: {kbs-rate: 646.2955635996296, bytes-read: 7677345, objects-read: 4452, symbols-read: 7677345, bytes-written: 7846877, objects-copied: 4452}
      state: finished-copying
    tmp-table: temp_claim_7d90669a_8a05_483d_b917_4d55f41addbf
  - url: https://storage.googleapis.com/aidbox-public/500/Condition.ndjson.gz
    type: Condition
    threads:
    - bytes: {kbs-rate: 737.2432432432432, bytes-read: 81834, objects-read: 119, symbols-read: 81834, bytes-written: 84809, objects-copied: 119}
      state: finished-copying
    tmp-table: temp_condition_e73f4329_4c54_4f37_ac57_d798c46535ee
  - url: https://storage.googleapis.com/aidbox-public/500/DiagnosticReport.ndjson.gz
    type: DiagnosticReport
    threads:
    - bytes: {kbs-rate: 7029, bytes-read: 14058, objects-read: 18, symbols-read: 14058, bytes-written: 14688, objects-copied: 18}
      state: finished-copying
    tmp-table: temp_diagnosticreport_9ed47da5_6343_4d6f_b49e_b9f223f6226e
  - url: https://storage.googleapis.com/aidbox-public/500/Encounter.ndjson.gz
    type: Encounter
    threads:
    - bytes: {kbs-rate: 629.3567366579178, bytes-read: 2877419, objects-read: 4255, symbols-read: 2877419, bytes-written: 3004889, objects-copied: 4255}
      state: finished-copying
    tmp-table: temp_encounter_a8955035_7e22_4e24_8f66_d07ab21a51b5
  - url: https://storage.googleapis.com/aidbox-public/500/ExplanationOfBenefit.ndjson.gz
    type: ExplanationOfBenefit
    threads:
    - bytes: {kbs-rate: 660.615830307853, bytes-read: 26566005, objects-read: 4255, symbols-read: 26566005, bytes-written: 27008659, objects-copied: 4255}
      state: finished-copying
    tmp-table: temp_explanationofbenefit_c65a5890_692c_4eff_8803_2856de7ef7fb
  - url: https://storage.googleapis.com/aidbox-public/500/Immunization.ndjson.gz
    type: Immunization
    threads:
    - bytes: {kbs-rate: 623.015307111025, bytes-read: 3215382, objects-read: 6611, symbols-read: 3215382, bytes-written: 3380657, objects-copied: 6611}
      state: finished-copying
    tmp-table: temp_immunization_78b8e383_6867_41e7_bef6_a4561cf5eb0e
  - url: https://storage.googleapis.com/aidbox-public/500/MedicationRequest.ndjson.gz
    type: MedicationRequest
    threads:
    - bytes: {kbs-rate: 693.7151515151515, bytes-read: 114463, objects-read: 197, symbols-read: 114463, bytes-written: 121358, objects-copied: 197}
      state: finished-copying
    tmp-table: temp_medicationrequest_781a02d6_9204_4e71_8135_2fd39c9b74c8
  - url: https://storage.googleapis.com/aidbox-public/500/Observation.ndjson.gz
    type: Observation
    threads:
    - bytes: {kbs-rate: 7100.5, bytes-read: 14201, objects-read: 18, symbols-read: 14201, bytes-written: 14741, objects-copied: 18}
      state: finished-copying
    tmp-table: temp_observation_1abd0adc_1de8_45d1_b83c_bc706d74caed
  - url: https://storage.googleapis.com/aidbox-public/500/Organization.ndjson.gz
    type: Organization
    threads:
    - bytes: {kbs-rate: 681.8031674208145, bytes-read: 301357, objects-read: 525, symbols-read: 301357, bytes-written: 301357, objects-copied: 525}
      state: finished-copying
    tmp-table: temp_organization_a7d46818_8a6d_4cc0_bd79_9b40afc89c44
  - url: https://storage.googleapis.com/aidbox-public/500/Practitioner.ndjson.gz
    type: Practitioner
    threads:
    - bytes: {kbs-rate: 720.7038461538461, bytes-read: 187383, objects-read: 525, symbols-read: 187383, bytes-written: 187383, objects-copied: 525}
      state: finished-copying
    tmp-table: temp_practitioner_8f607207_2103_4b92_9d92_d13c7ca98203
  - url: https://storage.googleapis.com/aidbox-public/500/Procedure.ndjson.gz
    type: Procedure
    threads:
    - bytes: {kbs-rate: 715.6639344262295, bytes-read: 87311, objects-read: 143, symbols-read: 87311, bytes-written: 92316, objects-copied: 143}
      state: finished-copying
    tmp-table: temp_procedure_215c75fb_ffcd_489e_9e78_f87cf69c0b2d
  message: ''
  process: done
inputFormat: application/fhir+ndjson
inputSource: source
id: imp-b
resourceType: BulkImportStatus
```

with insignificant instance dependant values. The `process=done` field means that the process has been correctly finished and you can find all the loaded resources in correspondent tables.

## $dump-sql

Take sql query and responds with Chunked Encoded stream in CSV format. Useful to export data for analytics.

```yaml
POST /$dump-sql

query:  select id, resource#>>'{name,0,family}'
format: csv # ndjson; sql; elastic-bulk?

HTTP/1.1 200 OK
Content-Type: application/CSV
Transfer-Encoding: chunked

pt-1    Doe    John
pt-2    Smith    Mike
................
```

## Demo

{% embed url="https://www.youtube.com/watch?v=BtLxICcQNWw&feature=youtu.be" %}



Create Client & AccessPolicy for your API agent - in our case curl

```yaml
PUT /

- resourceType: Client
  id: bulk-client
  secret: secret
  grant_types: ['basic']
- resourceType: AccessPolicy
  id: bulk-client
  engine: allow
  link:
  - {id: 'bulk-client', resourceType: 'Client'}
```

Generate some number of patients using SQL - in DB Console:

```sql
INSERT INTO patient (id,txid, status, resource) 
SELECT g.id, g.id, 'created', '{"name": [{"family": "John"}]}' 
FROM generate_series(1, 100000) AS g (id);
--
SELECT count(*) FROM Patient;
```

Now we can test bulk export using $dump operation with curl program:

```bash
curl -u bulk-client:secret /Patient/\$dump > /tmp/pt.ndjson

>  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
>                                 Dload  Upload   Total   Spent    Left  Speed
> 100 12.0M    0 12.0M    0     0  9527k      0 --:--:--  0:00:01 --:--:-- 9523k

less /tmp/pt.ndjson
```

We've got 100K  patients in less then a second!

Do not forget to clean up the database:

```sql
TRUNCATE Patient;
```

### Load data into BigQuery

```bash
# load tsv data
curl -v -X POST -u bulk-client:secret -H 'content-type:application/json' \
   https://<YOURBOX>/\$dump-sql \
   -d '{"query": "select id, ts, resource#>>'"'"'{module}'"'"' from entity"}' \
   > data.tsv

# create dataset
bq mk test
bq ls

# load datast
bq load --source_format=CSV \
  --field_delimiter=tab \
  test.entities ./data.tsv res,ts,mod

# list ids
bq query 'select id from test.entities'

# remove dataset
bq rm -r test
```

