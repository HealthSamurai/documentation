# Release Notes

## March 2022 - v:2203 _`latest`_

* Released a [February 2022 - v:2202 _`LTS`_](release-notes.md#february-2022-v-2202-stable).  __  The Aidbox team will backport security and critical bug fixes to it throughout a one-year support window.
* Added [`aidbox-validation-skip`](../../profiling-and-validation/profiling.md#aidbox-validation-skip-request-header) header that allows skipping resource reference validation.
* Standardized [Aidbox project](../../aidbox-configuration/aidbox-zen-lang-project.md) entrypoints.&#x20;
* Added fixes to [zen FHIR packages](../../profiling-and-validation/profiling-with-zen-lang/#zen-fhir-packages) and published [Structured Data Capture IG](https://build.fhir.org/ig/HL7/sdc/) as a zen FHIR package.
* Supported [`:of-type`](../../api-1/fhir-api/search-1/token-search.md) modifier for token/Identifier search.
* Enhanced [matcho engine](../../security-and-access-control-1/security/access-control/#matcho-engine) with `$every` and `$not` patterns.
* Added `patient` query parameter to the [bulk data export ](../../api-1/bulk-api-1/usdexport.md)operation.
* Updated [HL7 v2 module](https://docs.aidbox.app/modules-1/hl7-v2-integration) documentation.&#x20;
* Fixed [issues ](https://github.com/Aidbox/Issues/issues?q=is%3Aissue+milestone%3A%22March+2022+-+v%3A2203%22+is%3Aclosed)submitted by Aidbox users.

## February 2022 - v:2202 _`LTS`_

{% hint style="info" %}
February 2022 - v:2202 is available as a long-term support version. End of life is April 2023.
{% endhint %}

* Released a beta version of [Aidbox API constructor ](../../aidbox-configuration/aidbox-api-constructor.md)that allows to define REST API granularly.
* Added Access Control debug option: [su header](../../security-and-access-control-1/security/debug.md#su-request-header). It allows doing a request on behalf of a certain user.
* Added [Grafana dashboard RPC](../../core-modules/monitoring/grafana-integration.md) API that allows to get Aidbox metrics dashboards and import it to your Grafana.
* Added [`_count`](../../api-1/fhir-api/search-1/\_count-and-\_page.md), [`_total`](../../api-1/fhir-api/search-1/\_total-or-\_countmethod.md) and [`_timeout`](../../api-1/fhir-api/search-1/\_timeout.md) environment variables to configure default values.
* Improved logging for RPC and GraphQL calls.
* Fixed Aidbox UI pretty view display.
* Fixed [issues ](https://github.com/Aidbox/Issues/milestone/9?closed=1)submitted by Aidbox users.
* Deprecated APM and JMX in default build. Please reach out to us if you're using it.

## January 2022 - v:2201 `stable`

{% hint style="info" %}
Starting from January 2022 we're switching to a new release cycle. We added`:latest and LTS` (long-term support) versions. Check [the updated release cycle](./).
{% endhint %}

* Added FHIR R4 search parameters to [zen FHIR packages](../../profiling-and-validation/profiling-with-zen-lang/#zen-fhir-packages) (alpha) as a part of our roadmap to run Aidbox on managed PostgreSQL databases.
* Released [load-from-bucket](../../api-1/bulk-api-1/aidbox.bulk-data-import.md#aidbox.bulk-load-from-bucket) import operation for huge imports that allows loading terabytes of data from an AWS bucket directly to the Aidbox database with maximum performance.
* Added Aidbox UI new tools: [DB Tables](../../aidbox-ui/db-tables.md) and [DB Queries](../../aidbox-ui/db-queries.md) to improve database administration and introspection.
* Added new env `box_compatibility_validation_json__schema_regex="#{:fhir-datetime}` to enable strict date time validation in JSON schema validation engine per [FHIR spec](https://www.hl7.org/fhir/datatypes.html#dateTime).
* Improved [`$export`](../../api-1/bulk-api-1/usdexport.md) error statuses.
* Added Search resource `reference` [support](../../api-1/fhir-api/search-1/search-resource.md#reference-search).
* Improved [Search parameter](../../api-1/fhir-api/search-1/searchparameter.md#expression) expression error reporting.
* Improved [zen profiles](../../profiling-and-validation/profiling-with-zen-lang/) support in [GraphQL API](../../api-1/graphql-api.md).
* Released [Multibox box manager API](../../multibox/multibox-box-manager-api.md).
* Added Aidbox UI [Analyze Attributes](../../aidbox-ui/attrs-stats.md) tab description.



## December 2021 - v:2112 `stable`

* Added [slicings ](https://docs.aidbox.app/profiling-and-validation/profiling-with-zen-lang/write-a-custom-zen-profile#slicing)support to zen FHIR profiles.
* Released [Devbox performance test suite](https://github.com/Aidbox/devbox#performance-tests).
* Added new community [notebooks](../../aidbox-ui/notebooks.md) that demonstrate Aidbox functionality including Bulk export API, Aidbox terminology, Custom resources, etc.&#x20;
* Fixed bugs submitted by Aidbox users and updated the documentation.

## November 2021 - v:2111 `stable`

* Implemented the [`$translate`](../../terminology/usdtranslate-on-conceptmap.md) operation. So now you can translate code from one value set to another, based on the existing value set and concept maps resources, and/or other additional knowledge available to Aidbox.
* Released FHIR bulk data export. Using [$export](../../api-1/bulk-api-1/usdexport.md) you can export patient-level, group level or system-level data to GCP, AWS storage in ndjson format.
* Extended Aidbox [Access Policies](../../security-and-access-control-1/security/access-control/) to [GraphQL API](../../api-1/graphql-api.md).&#x20;
* Released [metrics server](../../core-modules/monitoring/) as an Aidbox component that implements the new metrics API for PostgreSQL, HikariCP and JVM metrics. &#x20;
* Added zen FHIR packages version check. Aidbox won't start if you use an outdated zen FHIR package.
* Extended `AuthConfig` resource with `forgotPasswordUrl` attribute.
* Added Aidbox, Multibox, Devbox and Aidboxdb multi-arch Images (ARM64 and AMD64) to resolve Apple Silicon M1 processors performance issues.&#x20;
* Added GraphQL access control and Aidbox Terminology community [notebooks](../../aidbox-ui/notebooks.md).
* Added [`AIDBOX_COMPLIANCE`](https://docs.aidbox.app/getting-started/installation/configure-devbox-aidbox-multibox#aidbox-compliance-mode) mode that changes Aidbox behavior to pass HL7® FHIR Conformance Tests.
* Fixed bugs submitted by Aidbox users and updated the documentation.

## October 2021 - v:2110 _`stable`_

* Released new API for Bulk Data import. Using [Aidbox.bulk](../../api-1/bulk-api-1/aidbox.bulk-data-import.md) you will be able to import data in both Aidbox and FHIR formats, validate uploaded resources and references asynchronously.
* Added Smart App Launch sandbox to the [Aidbox portal sample app](https://github.com/Aidbox/aidbox-react-app#aidbox-react-sample-app).
* Added [zen FHIR packages](../../profiling-and-validation/profiling-with-zen-lang/#zen-lang-packages) that can be used to configure Aidboxes and validate resources against zen FHIR profiles. You can use your custom profiles, convert FHIR profiles to zen FHIR profiles or use zen FHIR packages released by our team:
  * FHIR R4
    * `hl7-fhir-us-core` - US Core
    * `hl7-fhir-us-davinci-pdex` - Payer Data Exchange (PDex)
    * `hl7-fhir-us-davinci-pdex-plan-net` - PDEX Payer Network
    * `hl7-fhir-us-davinci-hrex` - The Da Vinci Payer Health Record exchange (HRex)
    * `hl7-fhir-us-davinci-drug-formulary` - DaVinci Payer Data Exchange US Drug Formulary
    * `hl7-fhir-us-carin-bb` - CARIN Consumer Directed Payer Data Exchange (CARIN IG for Blue Button®)
    * `hl7-fhir-us-mcode` - mCODE™ (short for Minimal Common Oncology Data Elements)
  * FHIR STU 3
    *   `nictiz-fhir-nl-stu3-zib2017` - Nictiz NL, including MedMij and HL7 NL


* Added `AIDBOX_DEV_MODE` env that enables  `_debug=policy` for [access policy debugging](https://docs.aidbox.app/security-and-access-control-1/security/access-policy#policy-debugging). We'll add more functionality that will be available for development purposes and can be disabled on production.
* Fixed bugs submitted by Aidbox users and updated documentation.

## September 2021 - v:2109 _`stable`_

* Added [Aidbox projects](../../aidbox-configuration/aidbox-zen-lang-project.md) that can be used to configure Aidboxes and validate data. Basically, Aidbox project is a directory with zen-lang edn files that describe Aidbox configuration.
* 5 FHIR compartments are available as default in Aidbox. More details on [Compartments API](https://docs.aidbox.app/api-1/compartments).
* Added Datadog [integration URL configuration](https://docs.aidbox.app/core-modules/logging-and-audit/aidbox-logs-and-datadog-integration#datadog-logging). So now you can specify in configuration if you want to use one of the following domains`datadoghq.com, us3.datadoghq.com, datadoghq.eu, ddog-gov.com`.
* Added a tutorial on how to configure [HL7 FHIR Da Vinci PDex Plan Net IG](../../fhir-implementation-guides/hl7-fhir-da-vinci-pdex-plan-net-ig.md) on Aidbox.
* Supported SMART Application Launch Framework Implementation Guide: Patient Portal Launch, Patient Standalone Launch, Provider EHR Launch, Provider Standalone Launch. Check the [sample](https://github.com/Aidbox/aidbox-project-samples#smart-on-fhir-aidbox-installation).&#x20;
* Released [Aidbox API constructor on zen (alpha version)](../../aidbox-configuration/aidbox-api-constructor.md).

## August 2021 - v:2108 _`stable`_

* Released [Aidbox Notebooks](../../aidbox-ui/notebooks.md).  Interactive notebooks for REST, SQL, RPC and Markdown. So now you can create your own notebooks or import community notebooks.

![Aidbox notebooks](../../.gitbook/assets/2021-09-03\_16-53-32.png)

* Released a beta version of [zen profiling](../../profiling-and-validation/profiling-with-zen-lang/extend-an-ig-with-a-custom-zen-profile.md). Advanced profiling with zen-lang to configure Aidboxes and validate data.
* Added [Asynchronous Batch Validation](../../profiling-and-validation/validation-api.md#asynchronous-batch-validation) mode to validate data in Aidbox against new profiles
* Released [Aidbox RPC API](../../api-1/rpc-api/)
* Supported conditional patch (e.g.: `PATCH /Patient?name=foo`)&#x20;
* Added an [environment variable](../../core-modules/logging-and-audit/aidbox-logs-and-datadog-integration.md) to pass the environment to Datadog (dev/staging/prod).
* Added history for [$load](../../api-1/bulk-api-1/usdload.md) and [$import](../../api-1/bulk-api-1/usdimport-and-fhir-usdimport.md) so now when using bulk import you have a source of truth for the history of every resource.
* Added [empty query params remove #238](https://github.com/Aidbox/Issues/issues/238). Please **pay attention** **when** **using json-schema** **access policy** engine: Fields with empty values, such as `[], {}, "", null`, are removed before passing request into access policy processing. Make sure to add `require` check of the fields that are validated by a json schema
* Fixed some bugs submitted by Aidbox users. Check it [here](https://github.com/Aidbox/Issues/milestone/3?closed=1).

## July 2021 - v:2107 _`stable`_

* We've released a major Aidbox UI upgrade

![New Aidbox UI](<../../.gitbook/assets/image (49) (7) (1) (1) (1) (9).png>)

* Updated REST Console (check out[ the tutorial](https://bit.ly/rest\_console\_tutorial)):
  * Explicit request headers `content-type, accept` etc
  * Show raw response
  * Added syntax highlight
* Upgrade Aidbox Java version to 16.
* Added `user.email`, `user.name` to the User grid to improve UX. [#397](https://github.com/Aidbox/Issues/issues/397).
* Improved logging.&#x20;
  * Reviewed and updated log event schema. The updated schema is available [here](https://docs.aidbox.app/core-modules/logging-and-audit#logs-schema).
  * Add w\_r - templated request URL for better aggregation. For example, requests like `GET /Patient/pt-1` will become  `GET /Patient/?` thus allowing aggregate all read requests for monitoring.
  * Log additional DB metrics from Aidbox.Dev. &#x20;
  * Added ELK, Kibana, and Grafana to Aidbox image. So now you can start exploring and analyzing logs from scratch. Check our tutorial on exploring and visualizing logs [here](https://docs.aidbox.app/app-development-guides/tutorials/how-to-explore-and-visualize-aidbox-logs-with-kibana-and-grafana).
* We added a new auth mechanism for authorization Aidbox.Cloud and Aidbox.Multibox users by JWT.
* Support for [OKTA](https://www.okta.com) as an external OAuth 2.0 provider.  Check out [the tutorial](../../security-and-access-control-1/auth/external-oauth-2.0-providers/configure-okta.md).
* Added Intercom so you can get help directly from your Aidbox.Dev or Aidbox.Cloud.
* Added a guide on search performance optimization to our docs. Check it [here](https://docs.aidbox.app/api-1/api/search-parameters#optimization-of-search-parameters).

## June 2021 - v:20210610 _`LTS`_

* Added support for [Bulk API export in CSV](https://docs.aidbox.app/api-1/bulk-api-1/usddump-csv).     You can use **/\[resourceType]/$dump-csv** endpoint to generate CSV file in which JSON resource structure is flattened into comma-separated format.  Such an option for data export is useful for integrations with external EHR systems. &#x20;
* Added support for [If-Match header](https://docs.aidbox.app/api-1/api/crud-1/delete) in DELETE operation of FHIR REST API.    If-Match is most often used to prevent accidental overwrites when multiple user agents might be acting in parallel on the same resource (i.e., to prevent the "lost update" problem). &#x20;
* Added support for additional mime types according to the [FHIR specification](http://hl7.org/fhir/http.html#mime-type)   Accept: _application/fhir+json_, Accept: _application/json+fhir._   When one of the headers is specified for your request, the same Content-Type header is returned by Aidbox.  &#x20;
* Implemented integration with [Datadog](https://www.datadoghq.com).      Datadog offers cloud-based monitoring and analytics platform which integrates and automates infrastructure monitoring, application performance monitoring, and log management for real-time observability of customers.  You can [configure it](https://docs.aidbox.app/app-development-guides/receive-logs-from-your-app/aidbox-logs-and-datadog-integration) as storage for Aidbox logs. The detailed guide on how to use Datadog monitoring capabilities in your Aidbox-based system you can find [here. ](https://docs.aidbox.app/app-development-guides/receive-logs-from-your-app/datadog-guide) This is an easy way to leverage HIPAA-compliant log management SaaS platform to unify logs, metrics, and traces in a single view.&#x20;
* Logs that are published on Aidbox startup are cleaned up from useless data.&#x20;
* SSL connection between Aidbox and PostgreSQL is now supported.  Please, read the [configuration instructions](https://docs.aidbox.app/getting-started/installation/use-devbox-aidbox#configuring-ssl-connection-with-postgresql) for more details. &#x20;
* Fixed a bug with race condition occurring during CRUD operations with If-Match header.   Transaction rollback is implemented for the case when concurrent change happens to the resource.&#x20;
* Fixed a bug in the user management module when a second registration for a deleted user resulted in an error.

## May 2021.04 - v:20210512

* Add support for the [Prefer](https://www.hl7.org/fhir/http.html#ops) header per FHIR spec
* Add [issue](https://github.com/Aidbox/Issues/issues/371) field for conditional update error
* Add proper [error message ](https://github.com/Aidbox/Issues/issues/59)for sign up with existing email
* Add support for [If-Match](https://github.com/Aidbox/Issues/issues/296) header for PATCH request
* Add FHIR support for [$validate](https://docs.aidbox.app/api-1/fhir-api/usdvalidate) operation
* Fixes for [#363](https://github.com/Aidbox/Issues/issues/363), [#376](https://github.com/Aidbox/Issues/issues/376), [#58](https://github.com/Aidbox/Issues/issues/58)

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

* Builds of [aidboxdb](../installation/aidboxdb-image.md) for PostgreSQL 11.11, 12.6, 13.2 are [released](https://hub.docker.com/r/healthsamurai/aidboxdb/tags?page=1\&ordering=last\_updated).&#x20;
* Aidbox now supports deployment on top of Azure PostgreSQL.
* Improvements of [$changes API](../../api-1/reactive-api-and-subscriptions/usdsnapshot-usdwatch-and-usdversions-api.md): FHIR support, pagination, upper version limit. $changes is now available at the resource level.
* [Enhancement of Transaction Bundle API](https://docs.aidbox.app/api-1/transaction) that allows to populate both resource and history tables in one transaction.
* During transaction bundle processing attributes of url type that store relative references are now interpreted as Reference type. See the [FHIR spec](https://www.hl7.org/fhir/datatypes.html#attachment) on Attachment data type for details.
* [Enhancement of Search resource](../../api-1/fhir-api/search-1/search-resource.md#token-search-1) that for token search allows fallback to default modifier implementation; (last example in the linked article)
* Fixed issue with $dump and $dump-sql not allowing CORS requests

## January 2021 - v:25012021

* [Elastic APM](https://www.elastic.co/apm) support for advanced performance monitoring
* [Two Factor Authentication](https://docs.aidbox.app/auth/two-factor-authentication) with TOTP implementation
* [AWS S3](https://docs.aidbox.app/storage-1/aws-s3) and [GCP Cloud Storage](https://docs.aidbox.app/storage-1/gcp-cloud-storage) integrations for storing content in the cloud
* Basic [\_filter](https://docs.aidbox.app/api-1/fhir-api/search-1/\_filter) query parameter support&#x20;
* New [versioning scheme](https://docs.aidbox.app/versioning-and-release-notes)
* Fixed [#354](https://github.com/Aidbox/Issues/issues/354)
