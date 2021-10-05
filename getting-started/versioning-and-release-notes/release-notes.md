# Release Notes

## September 2021 - v:2109 _`stable`_

* Added [Aidbox projects](../../aidbox-configuration/aidbox-zen-lang-project.md) that can be used to configure Aidboxes and validate data. 
* Added 5 FHIR compartments are available as default in Aidbox. More details on [Compartments API](https://docs.aidbox.app/api-1/compartments).
* Added Datadog [integration URL configuration](https://docs.aidbox.app/core-modules/logging-and-audit/aidbox-logs-and-datadog-integration#datadog-logging). So now you can specify in configuration if you want to use one of the following domains`datadoghq.com, us3.datadoghq.com, datadoghq.eu, ddog-gov.com`.
* Added a tutorial on how to configure [HL7 FHIR Da Vinci PDex Plan Net IG](../../fhir-implementation-guides/hl7-fhir-da-vinci-pdex-plan-net-ig.md) on Aidbox.
* Supported SMART Application Launch Framework Implementation Guide: Patient Portal Launch, Patient Standalone Launch, Provider EHR Launch, Provider Standalone Launch. Check the [sample](https://github.com/Aidbox/aidbox-project-samples#smart-on-fhir-aidbox-installation). 
* Released [Aidbox API constructor on zen \(alpha version\)](../../aidbox-configuration/aidbox-api-constructor.md).

## August 2021 - v:2108 _`stable`_

* Released [Aidbox Notebooks](../../aidbox-ui/notebooks.md).  Interactive notebooks for REST, SQL, RPC and Markdown. So now you can create your own notebooks or import community notebooks.

![Aidbox notebooks](../../.gitbook/assets/2021-09-03_16-53-32.png)

* Released a beta version of [zen profiling](../../profiling/draft-profiling-with-zen-lang.md). Advanced profiling with zen-lang to configure Aidboxes and validate data.
* Added [Asynchronous Batch Validation](../../profiling/validation-api.md#asynchronous-batch-validation) mode to validate data in Aidbox against new profiles
* Released [Aidbox RPC API](../../api-1/rpc-api.md)
* Supported conditional patch \(e.g.: `PATCH /Patient?name=foo`\) 
* Added an [environment variable](../../core-modules/logging-and-audit/aidbox-logs-and-datadog-integration.md) to pass the environment to Datadog \(dev/staging/prod\).
* Added history for [$load](../../api-1/bulk-api-1/usdload.md) and [$import](../../api-1/bulk-api-1/usdimport-and-fhir-usdimport.md) so now when using bulk import you have a source of truth for the history of every resource.
* Added [empty query params remove \#238](https://github.com/Aidbox/Issues/issues/238). Please **pay attention** **when** **using json-schema** **access policy** engine: Fields with empty values, such as `[], {}, "", null`, are removed before passing request into access policy processing. Make sure to add `require` check of the fields that are validated by a json schema
* Fixed some bugs submitted by Aidbox users. Check it [here](https://github.com/Aidbox/Issues/milestone/3?closed=1).

##  July 2021 - v:2107 _`stable`_

* We've released a major Aidbox UI upgrade

![New Aidbox UI](../../.gitbook/assets/image%20%2849%29%20%287%29%20%281%29%20%281%29.png)

* Updated REST Console \(check out[ the tutorial](https://bit.ly/rest_console_tutorial)\):
  * Explicit request headers `content-type, accept` etc
  * Show raw response
  * Added syntax highlight
* Upgrade Aidbox Java version to 16.
* Added `user.email`, `user.name` to the User grid to improve UX. [\#397](https://github.com/Aidbox/Issues/issues/397).
* Improved logging. 
  * Reviewed and updated log event schema. The updated schema is available [here](https://docs.aidbox.app/core-modules/logging-and-audit#logs-schema).
  * Add w\_r - templated request URL for better aggregation. For example, requests like `GET /Patient/pt-1` will become  `GET /Patient/?` thus allowing aggregate all read requests for monitoring.
  * Log additional DB metrics from Aidbox.Dev.  
  * Added ELK, Kibana, and Grafana to Aidbox image. So now you can start exploring and analyzing logs from scratch. Check our tutorial on exploring and visualizing logs [here](https://docs.aidbox.app/app-development-guides/tutorials/how-to-explore-and-visualize-aidbox-logs-with-kibana-and-grafana).
* We added a new auth mechanism for authorization Aidbox.Cloud and Aidbox.Multibox users by JWT.
* Support for [OKTA](https://www.okta.com/) as an external OAuth 2.0 provider.  Check out [the tutorial](../../security-and-access-control-1/auth/external-oauth-2.0-providers/configure-okta.md).
* Added Intercom so you can get help directly from your Aidbox.Dev or Aidbox.Cloud.
* Added a guide on search performance optimization to our docs. Check it [here](https://docs.aidbox.app/api-1/api/search-parameters#optimization-of-search-parameters).

## June __2021 - v:20210610 

* Added support for [Bulk API export in CSV](https://docs.aidbox.app/api-1/bulk-api-1/usddump-csv).     You can use **/\[resourceType\]/$dump-csv** endpoint to generate CSV file in which JSON resource structure is flattened into comma-separated format.  Such an option for data export is useful for integrations with external EHR systems.  
* Added support for [If-Match header](https://docs.aidbox.app/api-1/api/crud-1/delete) in DELETE operation of FHIR REST API.    If-Match is most often used to prevent accidental overwrites when multiple user agents might be acting in parallel on the same resource \(i.e., to prevent the "lost update" problem\).  
* Added support for additional mime types according to the [FHIR specification](http://hl7.org/fhir/http.html#mime-type)   Accept: _application/fhir+json_, Accept: _application/json+fhir._   When one of the headers is specified for your request, the same Content-Type header is returned by Aidbox.   
* Implemented integration with [Datadog](https://www.datadoghq.com/).      Datadog offers cloud-based monitoring and analytics platform which integrates and automates infrastructure monitoring, application performance monitoring, and log management for real-time observability of customers.  You can [configure it](https://docs.aidbox.app/app-development-guides/receive-logs-from-your-app/aidbox-logs-and-datadog-integration) as storage for Aidbox logs. The detailed guide on how to use Datadog monitoring capabilities in your Aidbox-based system you can find [here. ](https://docs.aidbox.app/app-development-guides/receive-logs-from-your-app/datadog-guide) This is an easy way to leverage HIPAA-compliant log management SaaS platform to unify logs, metrics, and traces in a single view. 
* Logs that are published on Aidbox startup are cleaned up from useless data. 
* SSL connection between Aidbox and PostgreSQL is now supported.  Please, read the [configuration instructions](https://docs.aidbox.app/getting-started/installation/use-devbox-aidbox#configuring-ssl-connection-with-postgresql) for more details.  
* Fixed a bug with race condition occurring during CRUD operations with If-Match header.   Transaction rollback is implemented for the case when concurrent change happens to the resource. 
* Fixed a bug in the user management module when a second registration for a deleted user resulted in an error.

## May 2021.04 - v:20210512

* Add support for the [Prefer](https://www.hl7.org/fhir/http.html#ops) header per FHIR spec
* Add [issue](https://github.com/Aidbox/Issues/issues/371) field for conditional update error
* Add proper [error message ](https://github.com/Aidbox/Issues/issues/59)for sign up with existing email
* Add support for [If-Match](https://github.com/Aidbox/Issues/issues/296) header for PATCH request
* Add FHIR support for [$validate](https://docs.aidbox.app/api-1/fhir-api/usdvalidate) operation
* Fixes for [\#363](https://github.com/Aidbox/Issues/issues/363), [\#376](https://github.com/Aidbox/Issues/issues/376), [\#58](https://github.com/Aidbox/Issues/issues/58)

## March 2021- v:20210412

* Change release name format from `DDMMYYYY` to `YYYYMMDD`
* Add zen lang [validation engine](https://docs.aidbox.app/app-development-guides/tutorials/profiling#validation-with-zen)
* Add[ `x-audit-req-body` header ](https://docs.aidbox.app/core-modules/logging-and-audit#aidbox-logging)for logging request body
* Add [`$loggy` endpoint](https://docs.aidbox.app/app-development-guides/receive-logs-from-your-app) for custom logs
* Add `$dump` endpoint [optional parameters](https://docs.aidbox.app/api-1/bulk-api-1#dump-data):
  * FHIR format conversion
  * gzip compression
  * `_since` parameter for filtering by `createdAt` date
* Add `$changes` API [omit-resources parameter](https://docs.aidbox.app/api-1/reactive-api-and-subscriptions/usdsnapshot-usdwatch-and-usdversions-api#query-string-parameters)
* Add jsonknife jsonpath engine [missing functions](https://github.com/Aidbox/Issues/issues/370)
* Add SearchQuery [parameterized order-by support](https://docs.aidbox.app/api-1/fhir-api/search-1/searchquery#add-order-by-into-parameters)
* Fix SearchQuery [revinclude for array references](https://github.com/Aidbox/Issues/issues/365)
* And other various bug fixes.

## February 2021 - v:20210319

* Builds of [aidboxdb](../installation/aidboxdb-image.md) for PostgreSQL 11.11, 12.6, 13.2 are [released](https://hub.docker.com/r/healthsamurai/aidboxdb/tags?page=1&ordering=last_updated). 
* Aidbox now supports deployment on top of Azure PostgreSQL.
* Improvements of [$changes API](../../api-1/reactive-api-and-subscriptions/usdsnapshot-usdwatch-and-usdversions-api.md): FHIR support, pagination, upper version limit. $changes is now available at the resource level.
* [Enhancement of Transaction Bundle API](https://docs.aidbox.app/api-1/transaction) that allows to populate both resource and history tables in one transaction.
* During transaction bundle processing attributes of url type that store relative references are now interpreted as Reference type. See the [FHIR spec](https://www.hl7.org/fhir/datatypes.html#attachment) on Attachment data type for details.
* [Enhancement of Search resource](../../api-1/fhir-api/search-1/search-resource.md#token-search-1) that for token search allows fallback to default modifier implementation; \(last example in the linked article\)
* Fixed issue with $dump and $dump-sql not allowing CORS requests

## January 2021 - v:25012021

* [Elastic APM](https://www.elastic.co/apm) support for advanced performance monitoring
* [Two Factor Authentication](https://docs.aidbox.app/auth/two-factor-authentication) with TOTP implementation
* [AWS S3](https://docs.aidbox.app/storage-1/aws-s3) and [GCP Cloud Storage](https://docs.aidbox.app/storage-1/gcp-cloud-storage) integrations for storing content in the cloud
* Basic [\_filter](https://docs.aidbox.app/api-1/fhir-api/search-1/_filter) query parameter support 
* New [versioning scheme](https://docs.aidbox.app/versioning-and-release-notes)
* Fixed [\#354](https://github.com/Aidbox/Issues/issues/354)

