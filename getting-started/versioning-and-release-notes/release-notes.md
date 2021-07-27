# Release Notes

## 2021.06 \(July, 2021\), 20210610. Pre-release available [here](https://hub.docker.com/r/healthsamurai/devbox/tags?page=1&ordering=last_updated&name=alpha).

* We've released a major Aidbox UI upgrade

![New Aidbox UI](../../.gitbook/assets/image%20%2841%29.png)

* Updated REST Console. The tutorial is available [here](https://docs.aidbox.app/app-development-guides/tutorials/rest-console).
* Added user.email, user.name to the User grid so now it's more convenient to work with the Aidbox UI. [\#397](https://github.com/Aidbox/Issues/issues/397).
* Updated Authentification  
* Improved logging. 

  * Reviewed and updated log event schema. The updated schema is available [here](https://docs.aidbox.app/core-modules/logging-and-audit#logs-schema).
  * Add w\_r - templated request URL for better aggregation. For example requests like `GET /Patient/pt-1` will become  `GET /Patient/?` thus allowing aggregate all read requests for monitoring.
  * Started logging additional information about Aidbox.Dev resources such size of the  database and tables.  
  * Added ELK, Kibana and Grafana to Aidbox image. So now you can start exploring and analyzing logs from scratch. Check our tutorial on exploring and visualizing logs [here](https://docs.aidbox.app/app-development-guides/tutorials/how-to-explore-and-visualize-aidbox-logs-with-kibana-and-grafana).

* Aidbox.Cloud and Aidbox.Multibox superuser login by JWT. We added a new auth mechanism for authorization cloud and Multibox users by JWT. 
* Added OKTA as an external Oauth 2.0 provider.  Check our tutorial on how to configure Okta IdentityProvider with Aidbox [here](https://docs.aidbox.app/user-management-1/auth/external-oauth-2.0-providers/configure-okta#login-into-aidbox-with-okta).

![](../../.gitbook/assets/image%20%2840%29.png)

![](../../.gitbook/assets/image%20%2852%29.png)

![](../../.gitbook/assets/image%20%2850%29.png)

## 2021.05 \(June 10, 2021\), 20210610

* Added support for [Bulk API export in CSV](https://docs.aidbox.app/api-1/bulk-api-1/usddump-csv).     You can use **/\[resourceType\]/$dump-csv** endpoint to generate CSV file in which JSON resource structure is flattened into comma-separated format.  Such an option for data export is useful for integrations with external EHR systems.  
* Added support for [If-Match header](https://docs.aidbox.app/api-1/api/crud-1/delete) in DELETE operation of FHIR REST API.    If-Match is most often used to prevent accidental overwrites when multiple user agents might be acting in parallel on the same resource \(i.e., to prevent the "lost update" problem\).  
* Added support for additional mime types according to the [FHIR specification](http://hl7.org/fhir/http.html#mime-type)   Accept: _application/fhir+json_, Accept: _application/json+fhir._   When one of the headers is specified for your request, the same Content-Type header is returned by Aidbox.   
* Implemented integration with [Datadog](https://www.datadoghq.com/).      Datadog offers cloud-based monitoring and analytics platform which integrates and automates infrastructure monitoring, application performance monitoring, and log management for real-time observability of customers.  You can [configure it](https://docs.aidbox.app/app-development-guides/receive-logs-from-your-app/aidbox-logs-and-datadog-integration) as storage for Aidbox logs. The detailed guide on how to use Datadog monitoring capabilities in your Aidbox-based system you can find [here. ](https://docs.aidbox.app/app-development-guides/receive-logs-from-your-app/datadog-guide) This is an easy way to leverage HIPAA-compliant log management SaaS platform to unify logs, metrics, and traces in a single view. 
* Logs that are published on Aidbox startup are cleaned up from useless data. 
* SSL connection between Aidbox and PostgreSQL is now supported.  Please, read the [configuration instructions](https://docs.aidbox.app/getting-started/installation/use-devbox-aidbox#configuring-ssl-connection-with-postgresql) for more details.  
* Fixed a bug with race condition occurring during CRUD operations with If-Match header.   Transaction rollback is implemented for the case when concurrent change happens to the resource. 
* Fixed a bug in the user management module when a second registration for a deleted user resulted in an error.

## [2021.04 \(12th of May\)](https://github.com/Aidbox/Issues/projects/6), 20210512

* Add support for the [Prefer](https://www.hl7.org/fhir/http.html#ops) header per FHIR spec
* Add [issue](https://github.com/Aidbox/Issues/issues/371) field for conditional update error
* Add proper [error message ](https://github.com/Aidbox/Issues/issues/59)for sign up with existing email
* Add support for [If-Match](https://github.com/Aidbox/Issues/issues/296) header for PATCH request
* Add FHIR support for [$validate](https://docs.aidbox.app/api-1/fhir-api/usdvalidate) operation
* Fixes for [\#363](https://github.com/Aidbox/Issues/issues/363), [\#376](https://github.com/Aidbox/Issues/issues/376), [\#58](https://github.com/Aidbox/Issues/issues/58)

## [2021.03 \(12th of April\)](https://github.com/Aidbox/Issues/projects/5), 20210412

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

## [2021.02 \(19th of March\)](https://github.com/Aidbox/Issues/projects/4), 20210319

* Builds of [aidboxdb](../installation/aidboxdb-image.md) for PostgreSQL 11.11, 12.6, 13.2 are [released](https://hub.docker.com/r/healthsamurai/aidboxdb/tags?page=1&ordering=last_updated). 
* Aidbox now supports deployment on top of Azure PostgreSQL.
* Improvements of [$changes API](../../api-1/reactive-api-and-subscriptions/usdsnapshot-usdwatch-and-usdversions-api.md): FHIR support, pagination, upper version limit. $changes is now available at the resource level.
* [Enhancement of Transaction Bundle API](https://docs.aidbox.app/api-1/transaction) that allows to populate both resource and history tables in one transaction.
* During transaction bundle processing attributes of url type that store relative references are now interpreted as Reference type. See the [FHIR spec](https://www.hl7.org/fhir/datatypes.html#attachment) on Attachment data type for details.
* [Enhancement of Search resource](../../api-1/fhir-api/search-1/search-resource.md#token-search-1) that for token search allows fallback to default modifier implementation; \(last example in the linked article\)
* Fixed issue with $dump and $dump-sql not allowing CORS requests

## [2021.01 \(25th of January\)](https://github.com/Aidbox/Issues/projects/3), 25012021

* [Elastic APM](https://www.elastic.co/apm) support for advanced performance monitoring
* [Two Factor Authentication](https://docs.aidbox.app/auth/two-factor-authentication) with TOTP implementation
* [AWS S3](https://docs.aidbox.app/storage-1/aws-s3) and [GCP Cloud Storage](https://docs.aidbox.app/storage-1/gcp-cloud-storage) integrations for storing content in the cloud
* Basic [\_filter](https://docs.aidbox.app/api-1/fhir-api/search-1/_filter) query parameter support 
* New [versioning scheme](https://docs.aidbox.app/versioning-and-release-notes)
* Fixed [\#354](https://github.com/Aidbox/Issues/issues/354)

