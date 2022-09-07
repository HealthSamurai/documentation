---
description: This article describes top-level steps to import data from CCDA documents.
---

# CCDA Import Pipeline

While importing data from CCDA documents, there are many nuances on every step. How your system receives CCDA documents? What data elements you want to persist? How would your system deduplicate patient records? Because answers to those questions are strictly system-specific, Aidbox does not provide a ready-to-use solution here. But you can implement this pipeline on your own, using your favourite technology stack. This article provide top-level overview of such pipeline.

### Receiving CCDA files

* queue?
* sftp

### Converting to FHIR

* error handling
* filtering out required data elements

### Persisting to Aidbox

* Patient MPI
* Creating DocumentReference
* Using Provenance to mark origin of populated resources
* FHIR Transaction
* Simplest approach - Bundle.type = 'transaction'

