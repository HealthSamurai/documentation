# Table of contents

* [Getting started](README.md)

## Getting Started

* [Features](getting-started/features.md)
* [Licensing and Support](getting-started/editions-and-pricing.md)
* [Versioning & Release Notes](getting-started/versioning-and-release-notes/README.md)
  * [Release Notes](getting-started/versioning-and-release-notes/release-notes.md)
  * [Releases Archive](getting-started/versioning-and-release-notes/release-notes-1/README.md)
    * [0.4.7 addendum](getting-started/versioning-and-release-notes/release-notes-1/0.4.7-addendum.md)
* [Installation & Configuration](getting-started/installation/README.md)
  * [🎓 Setup Devbox](getting-started/installation/setup-aidbox.dev.md)
  * [Configure Devbox/Aidbox distribution](getting-started/installation/use-devbox-aidbox.md)
  * [Use aidboxdb image](getting-started/installation/aidboxdb-image.md)
  * [🎓 Getting Started with Devbox in Cloud](getting-started/installation/getting-started-with-box.md)

---

* [FAQ](faq.md)

## Storage <a id="storage-1"></a>

* [Database](storage-1/database.md)
* [AWS S3](storage-1/aws-s3.md)
* [GCP Cloud Storage](storage-1/gcp-cloud-storage.md)
* [Azure Blob Storage](storage-1/azure.md)

## User Management <a id="user-management-1"></a>

* [Auth](user-management-1/auth/README.md)
  * [Basic Auth](user-management-1/auth/basic-auth.md)
  * [Client Credentials Grant](user-management-1/auth/client-credentials.md)
  * [Resource Owner Grant](user-management-1/auth/resource-owner-password.md)
  * [Authorization Code Grant](user-management-1/auth/authorization-code.md)
  * [Implicit Grant](user-management-1/auth/implicit.md)
  * [Validating Foreign Access Tokens](user-management-1/auth/access-token-introspection.md)
  * [External Oauth 2.0 Providers](user-management-1/auth/external-oauth-2.0-providers.md)
  * [Two Factor Authentication](user-management-1/auth/two-factor-authentication.md)
  * [SMART on FHIR](user-management-1/auth/smart-app.md)
  * [Configuration options](user-management-1/auth/configuration-options.md)
  * [Discovery API](user-management-1/auth/well-known-endpoint.md)

## Security & Access Control <a id="security-and-access-control-1"></a>

* [Access Control](security-and-access-control-1/security/README.md)
  * [Access Policies](security-and-access-control-1/security/access-control.md)
  * [Role-Based Access Policies](security-and-access-control-1/security/role-based-access-policies.md)
  * [🎓 Access Control](security-and-access-control-1/security/access-policy.md)
  * [🎓 Sample: Patient can see their own data](security-and-access-control-1/security/sample-patient-can-see-its-own-data.md)

## Core Modules

* [Entities & Attributes](core-modules/entities-and-attributes.md)
* [$json-schema](core-modules/usdjson-schema.md)
* [Logging & Audit](core-modules/logging-and-audit/README.md)
  * [🎓 Monitoring & Audit Tutorial](core-modules/logging-and-audit/monitoring-and-audit-tutorial.md)

## Modules <a id="modules-1"></a>

* [Terminology](modules-1/terminology/README.md)
  * [Concept](modules-1/terminology/concept.md)
  * [Terminology API](modules-1/terminology/terminology-api.md)
  * [CodeSystem](modules-1/terminology/codesystem-and-concept/README.md)
    * [CodeSystem Concept Lookup](modules-1/terminology/codesystem-and-concept/concept-lookup.md)
    * [CodeSystem Subsumption testing](modules-1/terminology/codesystem-and-concept/subsumption-testing.md)
    * [CodeSystem Code Composition](modules-1/terminology/codesystem-and-concept/codesystem-code-composition.md)
  * [ValueSet](modules-1/terminology/valueset/README.md)
    * [ValueSet Expansion](modules-1/terminology/valueset/value-set-expansion.md)
    * [ValueSet Code Validation](modules-1/terminology/valueset/value-set-validation.md)
  * [Terminology Tutorials](modules-1/terminology/terminology-tutorials/README.md)
    * [Load ICD-10 terminology into Aidbox](modules-1/terminology/terminology-tutorials/load-terminologies.md)
* [HL7 v2 Integration](modules-1/hl7-v2-integration.md)
* [FHIR Resources](modules-1/fhir-resources/README.md)
  * [Capability Statement](modules-1/fhir-resources/metadata.md)
  * [Aidbox & FHIR formats](modules-1/fhir-resources/aidbox-and-fhir-formats.md)
* [Custom Resources](modules-1/custom-resources/README.md)
  * [$metadata](modules-1/custom-resources/custom-metadata.md)
  * [🎓 Custom Resources](modules-1/custom-resources/getting-started-with-custom-resources.md)
* [Aidbox Search](modules-1/aidbox-search/README.md)
  * [SQL endpoints](modules-1/aidbox-search/usdpsql.md)
* [First-Class Extensions](modules-1/first-class-extensions.md)

## API <a id="api-1"></a>

* [FHIR API](api-1/fhir-api/README.md)
  * [$validate](api-1/fhir-api/usdvalidate.md)
  * [Search](api-1/fhir-api/search-1/README.md)
    * [SearchParameter](api-1/fhir-api/search-1/searchparameter.md)
    * [\_id](api-1/fhir-api/search-1/_id.md)
    * [\_lastUpdated & \_createdAt](api-1/fhir-api/search-1/_lastupdated.md)
    * [\_text & \_content](api-1/fhir-api/search-1/_text-and-_content.md)
    * [\_ilike](api-1/fhir-api/search-1/_ilike.md)
    * [\_elements](api-1/fhir-api/search-1/_elements.md)
    * [\_summary](api-1/fhir-api/search-1/_summary.md)
    * [\_list](api-1/fhir-api/search-1/_list.md)
    * [\_count & \_page](api-1/fhir-api/search-1/_count-and-_page.md)
    * [\_total or \_countMethod](api-1/fhir-api/search-1/_total-or-_countmethod.md)
    * [\_timeout](api-1/fhir-api/search-1/_timeout.md)
    * [\_sort](api-1/fhir-api/search-1/_sort.md)
    * [\_include & \_revinclude](api-1/fhir-api/search-1/_include-and-_revinclude.md)
    * [Chained Parameters](api-1/fhir-api/search-1/chained-parameters.md)
    * [. expressions](api-1/fhir-api/search-1/.-expressions.md)
    * [\_result](api-1/fhir-api/search-1/_result.md)
    * [\_explain](api-1/fhir-api/search-1/_explain.md)
    * [AidboxQuery](api-1/fhir-api/search-1/custom-search.md)
    * [SearchQuery](api-1/fhir-api/search-1/searchquery.md)
    * [Search Resource](api-1/fhir-api/search-1/search-resource.md)
    * [\_filter](api-1/fhir-api/search-1/_filter.md)
    * [$lookup](api-1/fhir-api/search-1/usdlookup.md)
  * [Observation/$lastn](api-1/fhir-api/misc.md)
  * [History](api-1/fhir-api/history-1.md)
* [REST API](api-1/api/README.md)
  * [CRUD](api-1/api/crud-1/README.md)
    * [Create](api-1/api/crud-1/fhir-and-aidbox-crud.md)
    * [Read](api-1/api/crud-1/read.md)
    * [Update](api-1/api/crud-1/update.md)
    * [Patch](api-1/api/crud-1/patch.md)
    * [Delete](api-1/api/crud-1/delete.md)
* [Bulk API](api-1/bulk-api-1/README.md)
  * [Configure Access Policies for Bulk API](api-1/bulk-api-1/configure-access-policies-for-bulk-api.md)
  * [Generate sample data for Bulk API](api-1/bulk-api-1/generate-sample-data-for-bulk-api.md)
  * [$dump](api-1/bulk-api-1/usddump.md)
  * [$dump-sql](api-1/bulk-api-1/usddump-sql.md)
  * [$dump-csv](api-1/bulk-api-1/usddump-csv.md)
  * [$load](api-1/bulk-api-1/usdload.md)
  * [$import & /fhir/$import](api-1/bulk-api-1/usdimport-and-fhir-usdimport.md)
  * [🎓 Synthea by Bulk APi](api-1/bulk-api-1/synthea-by-bulk-api.md)
  * [🎓 $dump-sql tutorial](api-1/bulk-api-1/usddump-sql-tutorial.md)
* [Batch Upsert](api-1/batch-upsert.md)
* [Batch/Transaction](api-1/transaction.md)
* [ETAG support](api-1/etag-support.md)
* [Reactive API](api-1/reactive-api-and-subscriptions/README.md)
  * [Changes API](api-1/reactive-api-and-subscriptions/usdsnapshot-usdwatch-and-usdversions-api.md)
  * [Subscriptions](api-1/reactive-api-and-subscriptions/subscriptions-1.md)
  * [FHIR R4/Subscriptions](api-1/reactive-api-and-subscriptions/subscriptions.md)
* [Sequence API](api-1/sequence-api.md)
* [Encryption API](api-1/encryption-api.md)
* [Compartments API](api-1/compartments.md)
* [GraphQL API](api-1/graphql-api.md)

## Plan API

* [Plan API Overview](plan-api/plan-api-overview.md)

## Tools

* [Mappings](tools/mappings.md)
* [Aidbox SDK](tools/aidbox-sdk/README.md)
  * [Apps](tools/aidbox-sdk/aidbox-apps.md)
  * [NodeJs SDK](tools/aidbox-sdk/nodejs.md)
  * [Clojure SDK](tools/aidbox-sdk/clojure-sdk.md)
  * [Python SDK](tools/aidbox-sdk/python-sdk.md)
  * [SDK internals](tools/aidbox-sdk/sdk-internals.md)

## App development guides

* [Tutorials](app-development-guides/tutorials/README.md)
  * [Log analysis and visualization](app-development-guides/tutorials/how-track-logs.md)
  * [Restricting Access to Patient Data](app-development-guides/tutorials/restricting-access-to-patient-data.md)
  * [Working with REST Console](app-development-guides/tutorials/rest-console.md)
  * [Uploading Sample Data](app-development-guides/tutorials/load-bundle-into-fhir-server.md)
  * [Custom Search](app-development-guides/tutorials/custom-search.md)
  * [Authentication and Authorization](app-development-guides/tutorials/authentication-and-authorization.md)
  * [Working with Aidbox from .NET](app-development-guides/tutorials/working-with-aidbox-from-.net.md)
  * [Profiling and Validation](app-development-guides/tutorials/profiling.md)
  * [Patient Encounter notification Application](app-development-guides/tutorials/patient-encounter-notification-application.md)
  * [Working with pgAgent](app-development-guides/tutorials/working-wih-pgagent.md)
  * [Working with Extensions](app-development-guides/tutorials/working-with-extensions.md)
  * [SMART on FHIR](app-development-guides/tutorials/smart-of-fhir.md)
  * [Sync Data From Aidbox](app-development-guides/tutorials/sync-data-from-aidbox.md)
  * [SDC with Custom Resources](app-development-guides/tutorials/sdc-with-custom-resources.md)
  * [Testing with Stresty](app-development-guides/tutorials/testing-with-stresty.md)
  * [Subscribe to new Patient resource](app-development-guides/tutorials/subscribe-to-new-patient-resource.md)
* [Administration](app-development-guides/administration.md)
* [Receive logs from your app](app-development-guides/receive-logs-from-your-app/README.md)
  * [X-Audit header](app-development-guides/receive-logs-from-your-app/x-audit-header.md)
  * [Health Check](app-development-guides/receive-logs-from-your-app/health-check.md)
  * [Elastic Logs and Monitoring Integration](app-development-guides/receive-logs-from-your-app/elastic-logs-and-monitoring-integration.md)
  * [🎓 Export logs to ElasticSearch/Kibana](app-development-guides/receive-logs-from-your-app/export-logs-to-elasticsearch-kibana.md)
  * [Datadog Log management integration](app-development-guides/receive-logs-from-your-app/aidbox-logs-and-datadog-integration.md)
  * [🎓 Export logs to Datadog](app-development-guides/receive-logs-from-your-app/datadog-guide.md)
* [$matcho](app-development-guides/usdmatcho.md)
* [$to-format](app-development-guides/usdto-format-fhir-aidbox.md)

---

* [Contact us](contact-us.md)

