---
description: This article describes top-level steps to import data from CCDA documents.
---

# CCDA Import Pipeline

While importing data from CCDA documents, there are many nuances on every step. How does your system receive CCDA documents? What data elements do you want to persist? How would your system deduplicate patient records? Because answers to those questions are strictly system-specific, Aidbox does not provide a ready-to-use solution here. But you can implement this pipeline on your own, using your favorite technology stack. This article provides a top-level overview of such a pipeline.

### Receiving CCDA files

The first step is to understand how you would receive CCDA files. In case when you have an user-facing application like Patient Portal or EHR user will upload his CCDA file manually. The other common case is bulk import from remote storage like SFTP server or S3 bucket.

Processing received files is usually a resource consuming task, so it’s better to make it asynchronous. This way your system would be responsive while the file is being converted, and the entire conversion process would be much more manageable. For example, you may notice a bug in the CCDA converting logic, your engineers will fix it and you would like to process all recently received files. Holding all received files in a queue will give you this ability.

There are dozens of ways to set up such a queue, probably the most simple one is to use the [DocumentReference](https://www.hl7.org/fhir/documentreference.html) resource (if you know a patient to whom this document belongs). Alternatively you can use a dedicated queue service like [Apache Kafka](https://kafka.apache.org/) or just a custom table in your RDBMS. However, despite the storage you would use, you would store the original CCDA file to be able to access it at any time.

On the next step some kind of background job or a worker will pick received files from a queue and process them one after another.

### Converting to FHIR

There are many nuances in how the newly received file is processed and most of them are system-dependent. For instance, EHR data ingestion logic is very different from Public Health Data Lake’s. So it’s up to you to figure out an algorithm to meet your needs.

But besides differences, there is a common ground here. One of them is usage of [`/ccda/$to-fhir` endpoint](../ccda-converter.md#converting-a-ccda-document-to-fhir), which would convert a CCDA document to a FHIR document. This operation will slice out the complexity of the CCDA document and will leave you with a bunch of FHIR resources in your hands.

Please note that resources that came from a [`/ccda/$to-fhir` endpoint](../ccda-converter.md#converting-a-ccda-document-to-fhir) aren’t persisted yet. They are just serialized pieces of data with [Bundle-local references](https://www.hl7.org/fhir/bundle.html#references) between them. And in most cases before actually persisting them you would like to transmute them in some way.

For example, your system may be only interested in patient observations, so you would need to filter out any other resource types: MedicationStatements, AllergyIntolerances, etc. Or let’s imagine that your system uses an internal proprietary [CodeSystem](https://hl7.org/fhir/terminologies-systems.html) for medications, so before persisting a MedicationStatement resource, you need to translate MedicationStatement.medication code from RxNorm to this code system.

And don’t forget to properly handle any exception that happens during this step. In this case you need to record exception details as an outcome for the currently processed queue item and mark this item as erroneous. It will help you to analyse and fix this error later.

### Persisting FHIR resources to Aidbox

Once you have prepared all the data you want to persist and discarded all the stuff you don’t need, it’s time to think about data deduplication. What if your system received two identical CCDA documents by mistake? Would you create the same set of FHIR resources twice? Most likely you wouldn’t. Or let’s imagine a case when a single patient submits two CCDA documents produced by EHRs from two independent hospitals. In this case your system has to figure out that the patient is the same and to link all the data from two documents to a single Patient resource.

In general, your system needs to eliminate duplicating copies of the same data to keep the database clean and dry. How you would achieve this is very dependent on the data and nature of your system, but for Patient resource there is [`/Patient/$match` operation](https://www.hl7.org/fhir/patient-operation-match.html) which will help you to find an already existing Patient in the database. The other way is to use resource’s identifier attribute when it’s available to find an existing copy of the same resource.

Another question is what to do when you find an already existing resource in the database, but it’s data is different from what you have from the CCDA file. It’s resource-dependent and system-dependent, how you would reconcile those data differences.

Once you have cleaned up and deduplicated your CCDA data, you’re ready to submit it to Aidbox as a [FHIR Transaction](https://www.hl7.org/fhir/http.html#transaction) bundle - a set of CREATE/UPDATE/DELETE operations to your FHIR database. A good idea is to use [FHIR Provenance](https://www.hl7.org/fhir/provenance.html) to mark all populated resources as a result of ingestion of this specific CCDA file (TODO: example here).

### Conclusion

There’s no one-size-fits-all solution for a CCDA ingestion problem, because challenges that occur during this process are very system-specific. However, Aidbox helps to solve most common ones.

