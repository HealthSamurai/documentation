# Table of contents

* [Getting started](README.md)

## Getting Started

* [Installation & Configuration](getting-started/installation/README.md)
  * [🎓 Devbox](getting-started/installation/setup-aidbox.dev.md)
  * [🎓 Devbox in Cloud](getting-started/installation/getting-started-with-box.md)
  * [🎓 Devbox with bb](getting-started/installation/devbox-with-bb.md)
  * [Configure Devbox/Aidbox/Multibox](getting-started/installation/configure-devbox-aidbox-multibox.md)
  * [aidboxdb image](getting-started/installation/aidboxdb-image.md)
  * [🆕 Update Aidbox](getting-started/installation/update-aidbox.md)
* [Features](getting-started/features.md)
* [Licensing and Support](getting-started/editions-and-pricing.md)
* [Versioning & Release Notes](getting-started/versioning-and-release-notes/README.md)
  * [Release Notes](getting-started/versioning-and-release-notes/release-notes.md)
  * [Releases Archive](getting-started/versioning-and-release-notes/release-notes-1/README.md)
    * [0.4.7 addendum](getting-started/versioning-and-release-notes/release-notes-1/0.4.7-addendum.md)

***

* [FAQ](faq.md)

## Aidbox configuration

* [Aidbox project](aidbox-configuration/aidbox-zen-lang-project.md)
* [API constructor (beta)](aidbox-configuration/aidbox-api-constructor.md)
* [Setup SMTP provider](aidbox-configuration/setup-smtp-provider.md)
* [Zen Configuration](aidbox-configuration/zen-configuration.md)

## API <a href="#api-1" id="api-1"></a>

* [FHIR API](api-1/fhir-api/README.md)
  * [Capability Statement](api-1/fhir-api/metadata.md)
  * [$validate](api-1/fhir-api/usdvalidate.md)
  * [Search](api-1/fhir-api/search-1/README.md)
    * [\_id](api-1/fhir-api/search-1/\_id.md)
    * [\_lastUpdated & \_createdAt](api-1/fhir-api/search-1/\_lastupdated.md)
    * [\_profile](api-1/fhir-api/search-1/\_profile.md)
    * [\_text & \_content](api-1/fhir-api/search-1/\_text-and-\_content.md)
    * [\_ilike](api-1/fhir-api/search-1/\_ilike.md)
    * [\_elements](api-1/fhir-api/search-1/\_elements.md)
    * [Summary parameter](api-1/fhir-api/search-1/\_summary.md)
    * [\_list](api-1/fhir-api/search-1/\_list.md)
    * [\_count & \_page](api-1/fhir-api/search-1/\_count-and-\_page.md)
    * [\_total or \_totalMethod](api-1/fhir-api/search-1/\_total-or-\_countmethod.md)
    * [\_timeout](api-1/fhir-api/search-1/\_timeout.md)
    * [\_sort](api-1/fhir-api/search-1/\_sort.md)
    * [Token search](api-1/fhir-api/search-1/token-search.md)
    * [\_include & \_revinclude](api-1/fhir-api/search-1/\_include-and-\_revinclude.md)
    * [Chained Parameters](api-1/fhir-api/search-1/chained-parameters.md)
    * [. expressions](api-1/fhir-api/search-1/.-expressions.md)
    * [\_result](api-1/fhir-api/search-1/\_result.md)
    * [\_explain](api-1/fhir-api/search-1/\_explain.md)
    * [AidboxQuery](api-1/fhir-api/search-1/custom-search.md)
    * [SearchQuery](api-1/fhir-api/search-1/searchquery.md)
    * [SearchParameter](api-1/fhir-api/search-1/searchparameter.md)
    * [Search Resource](api-1/fhir-api/search-1/search-resource.md)
    * [\_filter](api-1/fhir-api/search-1/\_filter.md)
    * [$lookup](api-1/fhir-api/search-1/usdlookup.md)
  * [Observation/$lastn](api-1/fhir-api/misc.md)
  * [History](api-1/fhir-api/history-1.md)
  * [$everything on Patient](api-1/fhir-api/usdeverything-on-patient.md)
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
  * [🎓 $dump-sql tutorial](api-1/bulk-api-1/usddump-sql-tutorial.md)
  * [$dump-csv](api-1/bulk-api-1/usddump-csv.md)
  * [$export](api-1/bulk-api-1/usdexport.md)
  * [$load](api-1/bulk-api-1/usdload.md)
  * [$import & /fhir/$import](api-1/bulk-api-1/usdimport-and-fhir-usdimport.md)
  * [aidbox.bulk data import](api-1/bulk-api-1/aidbox.bulk-data-import.md)
  * [🎓 Synthea by Bulk APi](api-1/bulk-api-1/synthea-by-bulk-api.md)
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
* [RPC API](api-1/rpc-api.md)

***

* [Aidbox UI](aidbox-ui/README.md)
  * [Aidbox Notebooks](aidbox-ui/notebooks.md)
  * [REST Console](aidbox-ui/rest-console-1.md)
  * [DB Console](aidbox-ui/db-console.md)
  * [Attrs stats](aidbox-ui/attrs-stats.md)
  * [DB Tables](aidbox-ui/db-tables.md)
  * [DB Queries](aidbox-ui/db-queries.md)

## Profiling and validation

* [Profiling and validation overview](profiling-and-validation/profiling.md)
* [Profiling with zen-lang](profiling-and-validation/profiling-with-zen-lang/README.md)
  * [Write a custom zen profile](profiling-and-validation/profiling-with-zen-lang/write-a-custom-zen-profile.md)
  * [🎓 Extend an IG with a custom zen profile](profiling-and-validation/profiling-with-zen-lang/draft-profiling-with-zen-lang.md)
* [Asynchronous resource validation](profiling-and-validation/validation-api.md)
* [Profiling with AidboxProfile](profiling-and-validation/profiling-with-aidboxprofile.md)

## Terminology

* [Aidbox terminology module overview](terminology/terminology.md)
* [Import external (not-present) terminologies](terminology/terminology-api.md)
* [Concept](terminology/concept.md)
* [CodeSystem](terminology/codesystem-and-concept/README.md)
  * [CodeSystem Concept Lookup](terminology/codesystem-and-concept/concept-lookup.md)
  * [CodeSystem Subsumption testing](terminology/codesystem-and-concept/subsumption-testing.md)
  * [CodeSystem Code Composition](terminology/codesystem-and-concept/codesystem-code-composition.md)
* [ValueSet](terminology/valueset/README.md)
  * [ValueSet Expansion](terminology/valueset/value-set-expansion.md)
  * [ValueSet Code Validation](terminology/valueset/value-set-validation.md)
* [$translate on ConceptMap](terminology/usdtranslate-on-conceptmap.md)
* [Terminology Tutorials](terminology/terminology-tutorials/README.md)
  * [🎓 Load ICD-10 terminology into Aidbox](terminology/terminology-tutorials/load-terminologies.md)

## FHIR Implementation Guides

* [🎓 HL7 FHIR Da Vinci PDex Plan Net IG](fhir-implementation-guides/hl7-fhir-da-vinci-pdex-plan-net-ig.md)

## App development guides

* [Tutorials](app-development-guides/tutorials/README.md)
  * [Authentication Tutorial](app-development-guides/tutorials/basic-auth-tutorial.md)
  * [Restricting Access to Patient Data](app-development-guides/tutorials/restricting-access-to-patient-data.md)
  * [Uploading Sample Data](app-development-guides/tutorials/load-bundle-into-fhir-server.md)
  * [Custom Search](app-development-guides/tutorials/custom-search.md)
  * [Working with Aidbox from .NET](app-development-guides/tutorials/working-with-aidbox-from-.net.md)
  * [Patient Encounter notification Application](app-development-guides/tutorials/patient-encounter-notification-application.md)
  * [Working with pgAgent](app-development-guides/tutorials/working-with-pgagent.md)
  * [Working with Extensions](app-development-guides/tutorials/working-with-extensions.md)
  * [Sync Data From Aidbox](app-development-guides/tutorials/sync-data-from-aidbox.md)
  * [SDC with Custom Resources](app-development-guides/tutorials/sdc-with-custom-resources.md)
  * [Testing with Stresty](app-development-guides/tutorials/testing-with-stresty.md)
  * [APM Aidbox](app-development-guides/tutorials/apm-aidbox.md)
  * [Subscribe to new Patient resource](app-development-guides/tutorials/subscribe-to-new-patient-resource.md)
* [Administration](app-development-guides/administration.md)
* [Receive logs from your app](app-development-guides/receive-logs-from-your-app/README.md)
  * [X-Audit header](app-development-guides/receive-logs-from-your-app/x-audit-header.md)
  * [Health Check](app-development-guides/receive-logs-from-your-app/health-check.md)
* [$matcho](app-development-guides/usdmatcho.md)
* [$to-format](app-development-guides/usdto-format-fhir-aidbox.md)

## Security & Access Control <a href="#security-and-access-control-1" id="security-and-access-control-1"></a>

* [Overview](security-and-access-control-1/overview.md)
* [Authentication Flows](security-and-access-control-1/auth/README.md)
  * [Basic Auth](security-and-access-control-1/auth/basic-auth.md)
  * [Client Credentials Grant](security-and-access-control-1/auth/client-credentials.md)
  * [Resource Owner Grant](security-and-access-control-1/auth/resource-owner-password.md)
  * [Authorization Code Grant](security-and-access-control-1/auth/authorization-code.md)
  * [Implicit Grant](security-and-access-control-1/auth/implicit.md)
  * [Validating Foreign Access Tokens](security-and-access-control-1/auth/access-token-introspection.md)
  * [External Oauth 2.0 Providers](security-and-access-control-1/auth/external-oauth-2.0-providers/README.md)
    * [🎓 Aidbox](security-and-access-control-1/auth/external-oauth-2.0-providers/aidbox.md)
    * [🎓 Okta](security-and-access-control-1/auth/external-oauth-2.0-providers/configure-okta.md)
  * [Two Factor Authentication](security-and-access-control-1/auth/two-factor-authentication.md)
  * [SMART on FHIR](security-and-access-control-1/auth/smart-app/README.md)
    * [SMART on FHIR App Launch](security-and-access-control-1/auth/smart-app/smart-on-fhir-app-launch.md)
    * [🎓 SMART App Launch tutorial](security-and-access-control-1/auth/smart-app/smart-of-fhir.md)
  * [Configuration options](security-and-access-control-1/auth/configuration-options.md)
  * [Discovery API](security-and-access-control-1/auth/well-known-endpoint.md)
* [Access Control](security-and-access-control-1/security/README.md)
  * [Access Policies](security-and-access-control-1/security/access-control/README.md)
    * [Access Policies tutorial](security-and-access-control-1/security/access-control/search-parameters.md)
  * [Role-Based Access Policies](security-and-access-control-1/security/role-based-access-policies.md)
  * [🎓 Access Control](security-and-access-control-1/security/access-policy.md)
  * [🎓 Sample: Patient can see their own data](security-and-access-control-1/security/sample-patient-can-see-its-own-data.md)
  * [Debug](security-and-access-control-1/security/debug.md)

## Storage <a href="#storage-1" id="storage-1"></a>

* [Archiving](storage-1/archiving.md)
* [Database](storage-1/database.md)
* [AWS S3](storage-1/aws-s3.md)
* [GCP Cloud Storage](storage-1/gcp-cloud-storage.md)
* [Azure Blob Storage](storage-1/azure.md)

## Core Modules

* [Entities & Attributes](core-modules/entities-and-attributes.md)
* [$json-schema](core-modules/usdjson-schema.md)
* [Monitoring](core-modules/monitoring/README.md)
  * [Aidbox Metrics Server](core-modules/monitoring/aidbox-metrics-server.md)
  * [Grafana integration](core-modules/monitoring/grafana-integration.md)
* [Logging & Audit](core-modules/logging-and-audit/README.md)
  * [Elastic Logs and Monitoring Integration](core-modules/logging-and-audit/elastic-logs-and-monitoring-integration.md)
  * [Export logs to ElasticSearch/Kibana](core-modules/logging-and-audit/export-logs-to-elasticsearch-kibana.md)
  * [🎓 Log analysis and visualization tutorial](core-modules/logging-and-audit/how-to-explore-and-visualize-aidbox-logs-with-kibana-and-grafana.md)
  * [Datadog Log management integration](core-modules/logging-and-audit/aidbox-logs-and-datadog-integration.md)
  * [🎓 Export logs to Datadog tutorial](core-modules/logging-and-audit/datadog-guide.md)

## Modules <a href="#modules-1" id="modules-1"></a>

* [HL7 v2 Integration](modules-1/hl7-v2-integration.md)
* [FHIR Resources](modules-1/fhir-resources/README.md)
  * [Aidbox & FHIR formats](modules-1/fhir-resources/aidbox-and-fhir-formats.md)
* [Custom Resources](modules-1/custom-resources/README.md)
  * [$metadata](modules-1/custom-resources/custom-metadata.md)
  * [🎓 Custom Resources](modules-1/custom-resources/getting-started-with-custom-resources.md)
* [Aidbox Search](modules-1/aidbox-search/README.md)
  * [SQL endpoints](modules-1/aidbox-search/usdpsql.md)
* [First-Class Extensions](modules-1/first-class-extensions.md)

## Multibox

* [Multibox box manager API](multibox/multibox-box-manager-api.md)

## Plan API

* [Plan API Overview](plan-api/plan-api-overview.md)
* [Patient Access API](plan-api/patient-access-api.md)

## Tools

* [Mappings](tools/mappings.md)
* [Aidbox SDK](tools/aidbox-sdk/README.md)
  * [Apps](tools/aidbox-sdk/aidbox-apps.md)
  * [NodeJs SDK](tools/aidbox-sdk/nodejs.md)
  * [Clojure SDK](tools/aidbox-sdk/clojure-sdk.md)
  * [Python SDK](tools/aidbox-sdk/python-sdk.md)
  * [SDK internals](tools/aidbox-sdk/sdk-internals.md)

***

* [Contact us](contact-us.md)
