---
description: DRAFT
---

# aidbox.bulk.\* \[draft\]

{% hint style="warning" %}
This is a draft design document for the _new_ bulk load API
{% endhint %}

Bulk load is a challenge - there are many requirements, limitations and tradeoffs: performance, validation, transactional consistency. This is a proposal for the new Bulk API to give the user explicit options.

#### Validation Problem

There are two problems with validation during bulk upload:

* The first is a performance, especially when you want to validate references and terminology, which are transformed into database queries
* The second is to decide when upload operation should  fail - on the first error, on nth errors, or try to inspect all errors for your dataset

#### Consistency Problem

You may want to rollback the whole upload on any errors. The bulk upload will take some time during this time the writes & updates may happen, which will be overridden by the bulk dataset, i.e. lost.

#### Protocol & Performance Problems

We do not want to eat the whole memory on the server during the upload. This requires some kind of stream processing implementation. If we want to load a huge amount of data every operation \(even just parsing JSON\) may be a performance problem. The current state of HTTP does not support uploading huge files in a stream, most of the implementations \(like AWS S3\) split files into chunks and assemble the resulting file on the server. We solve this by inverting upload into streaming-friendly download.

#### Errors introspection problem

It may be challenging to investigate errors in bulk. Ideally, users want to see as many errors as possible to fix them in one iteration. There are maybe a lot of errors, so some grouping and introspection tools may simplify debug process.

### Parts of solution

* Use PostgreSQL copy protocol to stream data into the database
* Use staging table for initial data upload and transactionally move data from the staging table into the resource table
* Use PostgreSQL "unlogged table" for staging table to reduce replication load
* Batch validation of the whole bulk dataset in a stage table: more performant algorithm may be implemented for validation of all references and codes, escaping \(N+1\)\*M queries problem
* Save errors in the staging table for problems introspection

Basic steps of bulk upload may be:

1. Create staging table
2. Stream data into staging table
3. Run structure validation \(optionally in parallel\)
4. Run bulk references and terminology bindings validations
5. Copy data into resource table \(overriding or preserving history\)
6. In case of errors - introspect and analyze the staging table
7. Fix problems in the staging table and try again
8. Truncate/Drop staging table

These atomic steps may be composed into a complex operation like **`aidbox.bulk.import`**, which will consist of load, validate,  if no errors: do merge, drop stage

The general idea is to explicitly introduce a Staging Table \(Staging Resource\)  and basic operations on it:

```yaml
---
method: aidbox.bulk.stage/create
params:
  type: Patient
  
---
method: aidbox.bulk.stage/load
params:
  type: Patient
  source: bucket
  
---
method: aidbox.bulk.stage/validate
params:
  type: Patient
  profiles: ['us-core/Patient']
  references: true
  bindings: true

---
method: aidbox.bulk.stage/report
params:
  type: Patient

---
method: aidbox.bulk.stage/errors
params:
  type: Patient
  filter: ....
  limit: 100

---
method: aidbox.bulk.stage/merge
params:
  type: Patient
  preserveHistory: true
  
---
method: aidbox.bulk.stage/truncate
params:
  type: Patient

---
method: aidbox.bulk.stage/drop
params:
  type: Patient
  
# Or complex operatioon

method: aidbox.bulk.import
params:
  source: ....
  on-error: ....
```

