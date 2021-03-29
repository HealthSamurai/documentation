# Release Notes

### 2021.02 \(19th of March\), 19032021

* Builds of [aidboxdb](../installation/aidboxdb-image.md) for PostgreSQL 11.11, 12.6, 13.2 are [released](https://hub.docker.com/r/healthsamurai/aidboxdb/tags?page=1&ordering=last_updated). 
* Aidbox now supports deployment on top of Azure PostgreSQL.
* Improvements of [$changes API](../advanced/reactive-api-and-subscriptions/usdsnapshot-usdwatch-and-usdversions-api.md): FHIR support, pagination, upper version limit. $changes is now available at the resource level.
* [Enhancement of Transaction Bundle API](https://docs.aidbox.app/basic-concepts/transaction#multiple-resources-with-the-same-id) that allows to populate both resource and history tables in one transaction.
* During transaction bundle processing attributes of url type that store relative references are now interpreted as Reference type. See the [FHIR spec](https://www.hl7.org/fhir/datatypes.html#attachment) on Attachment data type for details.
* [Enhancement of Search resource](../basic-concepts/search-1/search-resource.md#token-search-1) that for token search allows fallback to default modifier implementation; \(last example in the linked article\)
* Fixed issue with $dump and $dump-sql not allowing CORS requests

### 2021.01 \(25th of January\), 25012021

* [Elastic APM](https://www.elastic.co/apm) support for advanced performance monitoring
* [Two Factor Authentication](https://docs.aidbox.app/auth/two-factor-authentication) with TOTP implementation
* [AWS S3](https://docs.aidbox.app/integrations/aws-s3) and [GCP Cloud Storage](https://docs.aidbox.app/integrations/gcp-cloud-storage) integrations for storing content in the cloud
* Basic [\_filter](https://docs.aidbox.app/basic-concepts/search-1/_filter) query parameter support 
* New [versioning scheme](https://docs.aidbox.app/versioning-and-release-notes)
* Fixed [\#354](https://github.com/Aidbox/Issues/issues/354)

