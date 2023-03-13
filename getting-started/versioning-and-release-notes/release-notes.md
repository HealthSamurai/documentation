# Release Notes

## February 2023 _`latest`_

* Validation and Terminology
  * Reduced [zen FHIR IGs](../../aidbox-configuration/aidbox-zen-lang-project/enable-igs.md) size
  * Introduced [New FTR extraction engine](../../terminology/fhir-terminology-repository/creating-aidbox-project-with-ftr/ftr-from-ftr-direct-dependency.md): creating new ValueSets based on existing FTRs
* Data API
  * Implemented [JSONPath Sort](../../api-1/fhir-api/search-1/configure-search-api.md) (previously there was fallback to jsonknife)
  * Implemented [#>> operation](../../api-1/fhir-api/search-1/configure-search-api.md#preferred-operator) to optimize some query types (enabled via env)
  * Improved [\_explain](../../api-1/fhir-api/search-1/search-parameters-list/\_explain.md) to show SQL even if the query failed with an error
  * Implemented handling of unique errors: it is now possible to enforce uniqueness on some combination of fields
  * Improved conformance of conditional queries
* MDM (Master Data Management)
  * Fixed bugs with Python integration
* C-CDA to FHIR converter
  * Introduced FHIR to CCD transformation (alpha)
* Security and Access Control
  * Supported Aidbox [on path](../../reference/configuration/environment-variables/optional-environment-variables.md#aidbox\_base\_url)
  * Improved [`__debug=policy`](../../security-and-access-control-1/security/access-policy.md#policy-debugging)``
* Ops
  * [Devbox images](../../overview/aidbox-user-portal/licenses.md#development-license) are no longer supported. Please use [AidboxOne image](https://hub.docker.com/r/healthsamurai/aidboxone)  with [development license](../../overview/aidbox-user-portal/licenses.md#development-license)
  * Supported [box\_web\_\*](../../reference/configuration/environment-variables/optional-environment-variables.md#box\_web\_max\_\_body) configs for Multibox
  * Introduced [Aidbox status page](https://status.aidbox.app)
* Aidbox Forms&#x20;
  * Added new forms templates, incl. Lifestyle, Medications, Problem List to [forms library](https://github.com/Aidbox/sdc-forms-library/)
  * Improved Forms UX
    * Supported tooltip for forms and input fields
    * Supported Markdown for labels and tooltips
  * Added Forms [styling/branding](../../modules-1/aidbox-forms/how-to-customize-form-appearance.md): logo, button texts, colors, fonts
    * Added [3 predefined themes](https://github.com/HealthSamurai/aidbox-zen-sdc/blob/61ec12dba9d530b47afbffad4bf2227c95b65590/zrc/sdc-box.edn#L18) (monochrome/HealthSamurai/NHS)
  * Added [SDCForm to Questionnaire converter](../../reference/aidbox-forms/api-reference.md#convert-forms)
  * Added customizable [redirect-button](../../reference/aidbox-forms/api-reference.md#aidbox.sdc-generate-form-link) to Forms
* Google Cloud Pub/Sub integration
  * Added [Setting ](../../api-1/reactive-api-and-subscriptions/gcp-pub-sub.md#specify-resource-types-and-boxes-for-which-to-publish-notification)to specify for what resources should trigger message publishing
  * Added [Option ](../../api-1/reactive-api-and-subscriptions/gcp-pub-sub.md#publish-a-message-before-a-resource-is-saved-to-a-database)to optimistically publish a message before the resource is saved to DB

## January 2023 _`stable`_

* Terminology
  * Added [ICD-10 FTR](../../terminology/fhir-terminology-repository/load-icd-10-cm-into-aidbox.md) integration
* Data API
  * [Zen Search API](../../api-1/fhir-api/search-1/zen-search-parameters.md) is now considered stable
  * [Zen Index API](../../storage-1/indexes/zen-indexes.md) is now considered stable
  * Implemented automatic loading of search parameters on extensions from IG
* C-CDA to FHIR converter
  * Added configuration for [/ccda/to-fhir](../../modules-1/ccda-converter.md#endpoint-options) endpoint
  * Added configuration for [/ccda/persist](../../modules-1/ccda-converter.md#endpoint-options-1) endpoint
  * Added tenant support for [/ccda/persist](../../modules-1/ccda-converter.md#persisting-result-of-c-cda-to-fhir-conversion) endpoint
* Security and Access Control
  * Supported passing any launch context parameters on SMART on FHIR 2.0 app launch flow with [fhirContext](../../security-and-access-control-1/auth/smart-app/smart-on-fhir-app-launch.md#generate-launch-uri-for-ehr-launch-sequence) parameter
* Ops
  * Add GraphQL request body in gq/resp log event
  * Changed container OS to Alpine and reduced total container size
  * [Changed internal container User to non-root user.](https://github.com/Aidbox/Issues/issues/539)
  * Introduced [Telemetry API ](../../reference/configuration/environment-variables/optional-environment-variables.md#telemetry)for errors and usage stats.
* Aidbox Forms
  * Added new forms templates to [forms library](https://github.com/Aidbox/sdc-forms-library/)
  * Added [metadata properties](../../reference/aidbox-forms/form-dsl.md) to forms
  * Added [`redirect-on-sign`](../../reference/aidbox-forms/api-reference.md#aidbox.sdc-generate-form-link) parameter

## December 2022 _`2212`_

* MDM (Master Data Management)
  * Added [$match](../../mdm/mdm-module.md) operation
  * [Python API](https://github.com/Aidbox/mdm) for Aidbox MDM module
* Archive/Restore API
  * [Task API](../../api-1/task-api.md) allowing to define asynchronous operation called tasks.
  * [Archive/Restore API](../../api-1/archive-restore-api/) allowing to archive and restore resources to/from S3 bucket powered by Task API.
  * [Scheduler API](../../api-1/scheduler-api.md) allowing to execute tasks by schedule.
* Aidbox Configuration Projects
  * Added a [FHIR 4.0.1 compliant Configuration Project](https://github.com/Aidbox/fhir-r4-configuration-project) (also available on Aidbox User Portal).
  * Added a project with [Touchstone FHIR 4.0.1 basic server test suite fixtures](https://github.com/zen-fhir/touchstone-fhir-r4-fixtures).
  * Added [a guide on passing Touchstone FHIR 4.0.1 basic server test suite](../../tutorials/fhir-conformance/touchstone-fhir-4.0.1-basic-server.md#pass-with-aidbox-user-portal-sandbox) on Aidbox User Portal.
* Data API
  * Implemented [jsonpath search engine](../../api-1/fhir-api/search-1/configure-search-api.md)
  * GraphQL: added [warmup cache on startup](../../api-1/graphql-api.md#configure-graphql) option
* Terminology API
  * Added [SNOMED CT FTR integration](../../terminology/fhir-terminology-repository/load-snomed-ct-into-aidbox.md)
* Aidbox Forms
  * Simplified DSL ([brief definition, less layers to describe for basic behavior](../../modules-1/aidbox-forms/how-to-create-a-form.md))
  * Added layout autogeneration
  * Added [extraction based on templates](../../reference/aidbox-forms/finalize-dsl.md)
  * Added API for generate layers ([layout, finalize, finalize-constraints](../../reference/aidbox-forms/api-reference.md))
* C-CDA to FHIR converter
  * Added a guide on[ how to persist FHIR resources to a DB](../../modules-1/ccda-converter.md#persisting-a-result-of-ccda-to-fhir-conversion) after C-CDA to FHIR transformation.
  * Improved Observation data mapping.
* [Google Cloud Pub/Sub integration](../../api-1/reactive-api-and-subscriptions/gcp-pub-sub.md)

## November 2022 _`2211`_

* Aidbox deployment and maintenance
  * Released [index suggestion API](../../storage-1/indexes/get-suggested-indexes.md) and updated documentation on [index management](../../storage-1/indexes/)
  * Updated documentation on [highly available Aidbox](../run-aidbox-in-kubernetes/high-available-aidbox.md)
  * Updated documentation on [production-ready deployment to Kubernetes](../run-aidbox-in-kubernetes/deploy-aidbox-in-kubernetes.md)
* Smartbox
  * Improved admin UI with configuration of [EHR-level logo](../../modules-1/smartbox/how-to-guides/set-up-ehr-level-customization.md#with-smartbox-ui), [email provider](../../modules-1/smartbox/how-to-guides/setup-email-provider.md) and [bulk export](../../modules-1/smartbox/how-to-guides/setup-email-provider-1.md)
  * Added documentation on [deployment to Kubernetes](../../modules-1/smartbox/get-started/deploy-smartbox-to-kubernetes.md)
  * Added guides on passing [Inferno tests](../../modules-1/smartbox/how-to-guides/pass-inferno-tests-with-smartbox.md) and [Inferno Visual Inspection and Attestation](../../modules-1/smartbox/how-to-guides/pass-inferno-visual-inspection-and-attestation.md)
* Aidbox Configuration Projects
  * Improved startup time for Aidbox instances [configured with zen FHIR IGs](../../aidbox-configuration/aidbox-zen-lang-project/enable-igs.md)
  * Improved [local development workflow](broken-reference)
  * Improved Aidbox [support](../../fhir-implementation-guides/us-core-ig/us-core-ig-support-reference.md) of [US Core IG](../../fhir-implementation-guides/us-core-ig/) artifacts
  * Added configuration of Aidbox features via zen-lang instead of envs
* Aidbox User Portal
  * Added support of setting multiple Aidbox Configuration Projects and zen FHIR IGs for Sandbox instances
* C-CDA to FHIR converter
  * Confirmed [USCDI v1](https://www.healthit.gov/isa/united-states-core-data-interoperability-uscdi#uscdi-v1) compliance
* Aidbox Forms
  * Added forms as a configuration project to the [Aidbox User Portal](../../overview/aidbox-user-portal/)
  * Introduced the [form repository](https://github.com/Aidbox/sdc-forms-library) with the commonly used templates, such as PHQ-9, vital signs and GAD-7
  * Published tutorials on how to create and manage forms in [Aidbox Forms](../../modules-1/aidbox-forms.md)
* Supported tls for [SMTP provider](../../aidbox-configuration/setup-smtp-provider.md)

## October 2022 _`2210`_

* Terminology
  * Introduced [FHIR Terminology repositories ](../../terminology/fhir-terminology-repository/)(alpha) to store any terminology elements (code systems, value sets, concepts) in an effective way and distribute them among Aidbox instances
* C-CDA to FHIR converter
  * Supported [validation against XSD and Schematron schemas](../../modules-1/ccda-converter.md#validating-a-ccda-document)
  * Supported Diagnostic Imaging Report sections:
    * DICOM
    * Findings
    * History
    * Indications for procedure
  * Supported conversion of different sections of C-CDA documents to ClinicalImpression resources:
    * Consultation notes
    * Discharge notes
    * Procedure history notes
    * Nursing notes
  * Supported Care Team section mapping
* Search API
  * Added [execute type in AidboxQuery](../../api-1/fhir-api/search-1/custom-search.md#query-types)
  * Added [FHIR compliant date search](../../api-1/fhir-api/search-1/date-search.md)
  * Added 180-second [timeout](../../api-1/fhir-api/search-1/search-parameters-list/\_timeout.md) for [(rev)include queries](../../api-1/fhir-api/search-1/search-parameters-list/\_include-and-\_revinclude.md)
  * **Breaking change:** removed limit of 1000 in [\_count queries](../../api-1/fhir-api/search-1/search-parameters-list/\_count-and-\_page.md)
* FHIR API for EHRs
  * Supported [multitenancy](../../modules-1/smartbox/background-information/multitenancy-approach.md)
  * Added an option set up [EHR-level customization](../../modules-1/smartbox/how-to-guides/set-up-ehr-level-customization.md) (logos and templates)
* Aidbox forms
  * Supported rules, text type and display type conversion on [FHIR Questionnaire -> SDCDocument converter](../../reference/aidbox-forms/api-reference.md#aidbox.sdc-convert-questionnaire)
  * Supported [amendments and addendums](../../modules-1/aidbox-forms/addendum.md) of forms
* Aidbox user portal
  * Introduced [CI/CD licenses](../editions-and-pricing.md#aidbox-licenses) on the [Aidbox user portal](../../overview/aidbox-user-portal/). You can get this license to run multiple instances of Aidbox and Multibox in parallel for 72 hours. Both development and CI/CD licenses are available for existing customers at no cost
  * Added an option to configure Aidbox instances hosted in Aidbox Cloud with FHIR IGs for data validation
* Logging and audit
  * Supported [log streaming to browser](../../core-modules/logging-and-audit/#logs-in-browser)
  * Fixed incorrect calculation of request duration for resources in transaction bundles
* Updated documentation
  * [Index management](../../storage-1/indexes/create-indexes-manually.md)
  * [US Core IG configuration](../../fhir-implementation-guides/us-core-ig/)
  * [Deleting data guide](../../storage-1/delete-data.md)
* Fixed [issues](https://github.com/Aidbox/Issues/issues?q=is%3Aissue+milestone%3A%22October+2022+-+v%3A2210%22+is%3Aclosed) submitted by Aidbox users and minor bugs

## September 2022 _`2209`_

* Released [Seed v2 service](../../aidbox-configuration/aidbox-zen-lang-project/seed-v2.md) for [Aidbox configuration projects](../../aidbox-configuration/aidbox-zen-lang-project/)
* Added [Client.audience](../../security-and-access-control-1/overview.md#client.audience) regex support in SMART on FHIR App launch
* Added an option to [filter Aidbox stdout logs](../../core-modules/logging-and-audit/#stdout-log) by severity. By default Aidbox sends only `error` severity level logs to stdout.
* Added response headers to [Aidbox REST Console](../../overview/aidbox-ui/rest-console-1.md) and [Aidbox Notebooks](../../overview/aidbox-ui/notebooks.md)
* Enhanced the Aidbox configuration process with [Aidbox configuration projects](../../aidbox-configuration/aidbox-zen-lang-project/)
* Supported [`/health` endpoint](../../app-development-guides/receive-logs-from-your-app/health-check.md) for Multibox
* Updated documentation on how to configure Aidbox with [Aidbox configuration project](../../aidbox-configuration/aidbox-zen-lang-project/)
* Updated documentation on how to [configure highly available Aidbox](../run-aidbox-in-kubernetes/high-available-aidbox.md)
* Added support for [PostgreSQL 15](../../storage-1/aidboxdb-image.md) (beta)
* Added documentation on how to create [most common indexes](../../storage-1/indexes/create-indexes-manually.md)
* Added `aidbox` format support for [bulk import](../../api-1/bulk-api-1/aidbox.bulk-data-import.md#aidbox.bulk-import-start)
* Added `enable-links` parameter for [AidboxQuery](../../api-1/fhir-api/search-1/custom-search.md#return-links)
* Fixed issues submitted by Aidbox users and fixed minor bugs.

## August 2022 _`2208`_

* Aidbox FHIR API module (SMARTbox) is officially certified by ICSA Labs to comply with the [ONC’s 2015 Edition Cures Update](https://www.healthit.gov/topic/certification-ehrs/2015-edition-cures-update-test-method) requirements / 170.315(g)(10).
* Added [Aidbox FHIR API module (SMARTbox) set up guide](../../modules-1/smartbox/get-started/set-up-smartbox.md).
* Added an option to [get Aidbox hosted on AWS](../run-aidbox-as-a-saas/aidbox-as-a-saas-on-aws.md) directly from the Aidbox user portal. [Aidbox FHIR platform](https://aws.amazon.com/marketplace/pp/prodview-l5djlpvsd6o5g) is available on AWS marketplace.
* Released [Aidbox forms](../../modules-1/aidbox-forms.md) alpha version.
* Added [RPC method](../../terminology/terminology-api/import-using-an-aidbox-project.md#track-import-progress) to track Aidbox project terminology bundles async load status.
* Added env to [automatically create a User resource](../../security-and-access-control-1/background-information/access-token-introspection.md#create-user-automatically) on auth via TokenIntrospector. This allows to use both TokenIntrospector and IdentityProvider auth for the same User in Aidbox.
* Added log event [:op/timeout](../../core-modules/logging-and-audit/aidbox-log-schema.md) for logging custom operations timeout
* Added FHIR Bundle transaction conditional CRUD with `urn:uuid` support
* Added [SNOMED CT bundle](../../terminology/terminology-api/)
* Enhanced [zen.fhir profiles](../../profiling-and-validation/profiling-with-zen-lang/) with RequiredPattern and FixedValue constraints
* Added [seed service](../../aidbox-configuration/aidbox-zen-lang-project/seed-import.md) errors output.
* Added [Azure Active Directory (Azure AD)](../../security-and-access-control-1/how-to-guides/set-up-external-identity-provider/azure-ad.md) and [Keycloak](../../security-and-access-control-1/how-to-guides/set-up-external-identity-provider/keycloak.md) external identity providers integration guides
* Added [Mailgun](../../tutorials/integrations/mailgun-integration.md) and [Postmark](../../tutorials/integrations/postmark-integration.md) email providers integration guides

## July 2022 _`2207`_

* [Published](https://github.com/zen-lang/fhir/releases/tag/0.5.18) multiple versions of [zen FHIR IGs ](../../profiling-and-validation/profiling-with-zen-lang/#zen-fhir-packages)to provide an option to use a specific version of an implementation guide.
* Improved GraphQL API performance.
* Improved validation with zen-lang performance.
* Added RPC method to get the [import status](../../api-1/bulk-api-1/import-from-a-bucket.md#aidbox.bulk-load-from-bucket-status) when running [load-from-bucket](../../api-1/bulk-api-1/import-from-a-bucket.md) import operation.
* Added an option to start Aidbox with an invalid [Aidbox project](../../aidbox-configuration/aidbox-zen-lang-project/) in [dev-mode](../../reference/configuration/environment-variables/aidbox-project-environment-variables.md#aidbox\_zen\_dev\_mode) to improve the debugging experience. By default, Aidbox doesn't start with invalid Aidbox projects.
* Added an option to enable [ACL checks for searches in conditional operations](../../security-and-access-control-1/background-information/acl.md#conditional-crud).
* Added `BOX_FEATURES_TERMINOLOGY_IMPORT_SYNC` environment variable to enable sync [terminology bundle file load](../../terminology/terminology-api/).
* Added `plain` option to use [`$import`](../../api-1/bulk-api-1/usdimport-and-fhir-usdimport.md) with non-gzipped files.
* Added support of [entry.search.mode ](../../api-1/fhir-api/search-1/search-with-related-resources.md#distinguish-between-matched-and-related-resources)field when using [\_include or \_revinclude](../../api-1/fhir-api/search-1/search-parameters-list/\_include-and-\_revinclude.md) search parameters.
* Added [`profile`](../../api-1/fhir-api/metadata.md#notes) property to the CapabilityStatement resource.
* Added configurable `refresh_token_expiration` parameter. If not defined refresh token doesn't expire.
* Fixed issues submitted by Aidbox users and fixed minor bugs.

## June 2022 _`2206, LTS`_

* Added an option to [load Aidbox project from a remote Git repository](../../aidbox-configuration/aidbox-zen-lang-project/#load-project-from-git-repository).
* Improved Aidbox startup time when loading [Aidbox project](../../aidbox-configuration/aidbox-zen-lang-project/#aidbox\_zen\_paths) from [a zip archive](../../aidbox-configuration/aidbox-zen-lang-project/#aidbox\_zen\_paths).
* Added [Aidbox projects terminology bundle](../../terminology/terminology-api/#import-using-aidbox-project) load cache. Aidbox doesn't load a terminology bundle into its database if the bundle has no changes.
* [Aidbox project terminology](../../terminology/terminology-api/#import-using-aidbox-project) bundle load is now async and doesn't affect Aidbox startup time.
* Added nested resources validation when using [zen profiling](../../profiling-and-validation/profiling-with-zen-lang/).
* Added params to [load-from-bucket](../../api-1/bulk-api-1/import-from-a-bucket.md#parameters) import operation.
* Improved chained search parameters performance.
* Added ability to specify SQL migrations in [Aidbox project seed service](../../aidbox-configuration/aidbox-zen-lang-project/#seed-service).
* Enhanced [API constructor ACL](../../security-and-access-control-1/background-information/acl.md) with conditional CRUD and patient/group level `$export`.
* Added a tutorial on how to [create a user and give full access](../../tutorials/security-and-access-control/creating-user-and-set-up-full-user-access.md).
* Fixed issues submitted by Aidbox users and fixed minor bugs.

## May 2022 _`2205`_

* Added RPC API access control engines: [allow-rpc engine](../../security-and-access-control-1/security/access-control.md#allow-rpc-engine) and attribute-based [matcho-rpc engine](../../security-and-access-control-1/security/access-control.md#matcho-rpc-engine).
* Added GraphQL API resource [history search](../../api-1/graphql-api.md#queries) to retrieve a resource change history.
* Added [token expiration mechanism](../../security-and-access-control-1/overview.md#session-expiration) for stored Sessions.
* Enhanced [API constructor ACL](../../security-and-access-control-1/background-information/acl.md) with new operation engine [filter table insert on create](../../security-and-access-control-1/background-information/acl.md#filter).
* **Changed** auth layer 400/401/403 **errors response body** to OperationOutcome resource.
* Added [CapabilityStatement configuration](../../api-1/fhir-api/metadata.md#configure-capabilitystatement) options.
* Improved setting [PostgreSQL schema](broken-reference/) for Aidbox tables.
* Added option to set JWT private/public keys and secret[ via env](broken-reference/).
* Added [aidboxdb PostgreSQL 14.2 version](../../storage-1/aidboxdb-image.md). Supported versions are 14.2 and 13.6.
* Added tutorial [how to fix broken dates](../../tutorials/tutorials/).
* Released Infrabox preview. Infrabox is a simple and efficient tool to deploy and manage production-ready Aidbox infrastructure on k8s.

## April 2022 _`2204`_

* Added [ACL (access-control list)](../../security-and-access-control-1/background-information/acl.md) functionality to [API constructor](../../aidbox-configuration/aidbox-api-constructor.md).
* Released [`seed service`](../../aidbox-configuration/aidbox-zen-lang-project/#seed-import) for Aidbox project. Declare a set of resources in Aidbox project and get them loaded in one or many Aidboxes on start.
* Added option to set [PostgreSQL schema](broken-reference/) for Aidbox tables.
* Added `content-type: application/json` as default if `content-type` and `accept` headers are missing.
* Added [$drop-cache](../../api-1/cache.md#drop-cache-operation) operation and and [`multibox/drop-box-caches`](../../multibox/multibox-box-manager-api.md#multibox-drop-box-caches) RPC
* Added [GET /$version operation](../../api-1/api/aidbox-version.md) to get Aidbox version.
* Enhanced `POST /` operation error handling and added [FHIR bundle resource documentation](../../api-1/fhir-api/bundle.md).
* Enhanced GraphQL with ConceptMap.group property support
* Reviewed first-class extensions with zen-lang and updated [docs](../../modules-1/first-class-extensions.md#first-class-extension-as-zen-profile).
* Reviewed and updated [Aidbox multitenancy docs](../../security-and-access-control-1/background-information/multitenancy.md).

## March 2022 _`2203`_

* Released a [February 2022 - v:2202 _`LTS`_](release-notes.md#february-2022-v-2202-stable). \_\_ The Aidbox team will backport security and critical bug fixes to it throughout a one-year support window.
* Added [`aidbox-validation-skip`](../../profiling-and-validation/profiling.md#aidbox-validation-skip-request-header) header that allows skipping resource reference validation.
* Standardized [Aidbox project](../../aidbox-configuration/aidbox-zen-lang-project/) entrypoints.
* Added fixes to [zen FHIR packages](../../profiling-and-validation/profiling-with-zen-lang/#zen-fhir-packages) and published [Structured Data Capture IG](https://build.fhir.org/ig/HL7/sdc/) as a zen FHIR package.
* Supported [`:of-type`](../../api-1/fhir-api/search-1/token-search.md) modifier for token/Identifier search.
* Enhanced [matcho engine](../../security-and-access-control-1/security/access-control.md#matcho-engine) with `$every` and `$not` patterns.
* Added `patient` query parameter to the [bulk data export ](../../api-1/bulk-api-1/usdexport.md)operation.
* Updated [HL7 v2 module](https://docs.aidbox.app/modules-1/hl7-v2-integration) documentation.
* Fixed [issues ](https://github.com/Aidbox/Issues/issues?q=is%3Aissue+milestone%3A%22March+2022+-+v%3A2203%22+is%3Aclosed)submitted by Aidbox users.

## February 2022 - `2202` , _`LTS`_

{% hint style="info" %}
February 2022 - v:2202 is available as a long-term support version. End of life is April 2023.
{% endhint %}

* Released a beta version of [Aidbox API constructor ](../../aidbox-configuration/aidbox-api-constructor.md)that allows to define REST API granularly.
* Added Access Control debug option: [su header](../../security-and-access-control-1/how-to-guides/debug.md#su-request-header). It allows doing a request on behalf of a certain user.
* Added [Grafana dashboard RPC](../../core-modules/monitoring/grafana-integration.md) API that allows to get Aidbox metrics dashboards and import it to your Grafana.
* Added [`_count`](../../api-1/fhir-api/search-1/search-parameters-list/\_count-and-\_page.md), [`_total`](../../api-1/fhir-api/search-1/search-parameters-list/\_total-or-\_countmethod.md) and [`_timeout`](../../api-1/fhir-api/search-1/search-parameters-list/\_timeout.md) environment variables to configure default values.
* Improved logging for RPC and GraphQL calls.
* Fixed Aidbox UI pretty view display.
* Fixed [issues ](https://github.com/Aidbox/Issues/milestone/9?closed=1)submitted by Aidbox users.
* Deprecated APM and JMX in default build. Please reach out to us if you're using it.

## January 2022 - v:2201

{% hint style="info" %}
Starting from January 2022 we're switching to a new release cycle. We added`:latest and LTS` (long-term support) versions. Check [the updated release cycle](./).
{% endhint %}

* Added FHIR R4 search parameters to [zen FHIR packages](../../profiling-and-validation/profiling-with-zen-lang/#zen-fhir-packages) (alpha) as a part of our roadmap to run Aidbox on managed PostgreSQL databases.
* Released [load-from-bucket](../../api-1/bulk-api-1/aidbox.bulk-data-import.md#aidbox.bulk-load-from-bucket) import operation for huge imports that allows loading terabytes of data from an AWS bucket directly to the Aidbox database with maximum performance.
* Added Aidbox UI new tools: [DB Tables](../../overview/aidbox-ui/db-tables.md) and [DB Queries](../../overview/aidbox-ui/db-queries.md) to improve database administration and introspection.
* Added new env `box_compatibility_validation_json__schema_regex="#{:fhir-datetime}` to enable strict date time validation in JSON schema validation engine per [FHIR spec](https://www.hl7.org/fhir/datatypes.html#dateTime).
* Improved [`$export`](../../api-1/bulk-api-1/usdexport.md) error statuses.
* Added Search resource `reference` [support](../../api-1/fhir-api/search-1/search-resource.md#reference-search).
* Improved [Search parameter](../../api-1/fhir-api/search-1/searchparameter.md#expression) expression error reporting.
* Improved [zen profiles](../../profiling-and-validation/profiling-with-zen-lang/) support in [GraphQL API](../../api-1/graphql-api.md).
* Released [Multibox box manager API](../../multibox/multibox-box-manager-api.md).
* Added Aidbox UI [Analyze Attributes](../../overview/aidbox-ui/attrs-stats.md) tab description.

## December 2021 - v:2112

* Added [slicings ](https://docs.aidbox.app/profiling-and-validation/profiling-with-zen-lang/write-a-custom-zen-profile#slicing)support to zen FHIR profiles.
* Released [Devbox performance test suite](https://github.com/Aidbox/devbox#performance-tests).
* Added new community [notebooks](../../overview/aidbox-ui/notebooks.md) that demonstrate Aidbox functionality including Bulk export API, Aidbox terminology, Custom resources, etc.
* Fixed bugs submitted by Aidbox users and updated the documentation.

## November 2021 - v:2111

* Implemented the [`$translate`](../../terminology/usdtranslate-on-conceptmap.md) operation. So now you can translate code from one value set to another, based on the existing value set and concept maps resources, and/or other additional knowledge available to Aidbox.
* Released FHIR bulk data export. Using [$export](../../api-1/bulk-api-1/usdexport.md) you can export patient-level, group level or system-level data to GCP, AWS storage in ndjson format.
* Extended Aidbox [Access Policies](../../security-and-access-control-1/security/access-control.md) to [GraphQL API](../../api-1/graphql-api.md).
* Released [metrics server](../../core-modules/monitoring/) as an Aidbox component that implements the new metrics API for PostgreSQL, HikariCP and JVM metrics.
* Added zen FHIR packages version check. Aidbox won't start if you use an outdated zen FHIR package.
* Extended `AuthConfig` resource with `forgotPasswordUrl` attribute.
* Added Aidbox, Multibox, Devbox and Aidboxdb multi-arch Images (ARM64 and AMD64) to resolve Apple Silicon M1 processors performance issues.
* Added GraphQL access control and Aidbox Terminology community [notebooks](../../overview/aidbox-ui/notebooks.md).
* Added [`AIDBOX_COMPLIANCE`](https://docs.aidbox.app/getting-started/installation/configure-devbox-aidbox-multibox#aidbox-compliance-mode) mode that changes Aidbox behavior to pass HL7® FHIR Conformance Tests.
* Fixed bugs submitted by Aidbox users and updated the documentation.

## October 2021 - v:2110

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
    * `nictiz-fhir-nl-stu3-zib2017` - Nictiz NL, including MedMij and HL7 NL
* Added `AIDBOX_DEV_MODE` env that enables `_debug=policy` for [access policy debugging](https://docs.aidbox.app/security-and-access-control-1/security/access-policy#policy-debugging). We'll add more functionality that will be available for development purposes and can be disabled on production.
* Fixed bugs submitted by Aidbox users and updated documentation.

## September 2021 - v:2109

* Added [Aidbox projects](../../aidbox-configuration/aidbox-zen-lang-project/) that can be used to configure Aidboxes and validate data. Basically, Aidbox project is a directory with zen-lang edn files that describe Aidbox configuration.
* 5 FHIR compartments are available as default in Aidbox. More details on [Compartments API](https://docs.aidbox.app/api-1/compartments).
* Added Datadog [integration URL configuration](https://docs.aidbox.app/core-modules/logging-and-audit/aidbox-logs-and-datadog-integration#datadog-logging). So now you can specify in configuration if you want to use one of the following domains`datadoghq.com, us3.datadoghq.com, datadoghq.eu, ddog-gov.com`.
* Added a tutorial on how to configure [HL7 FHIR Da Vinci PDex Plan Net IG](../../tutorials/fhir-conformance/hl7-fhir-da-vinci-pdex-plan-net-ig.md) on Aidbox.
* Supported SMART Application Launch Framework Implementation Guide: Patient Portal Launch, Patient Standalone Launch, Provider EHR Launch, Provider Standalone Launch. Check the [sample](https://github.com/Aidbox/aidbox-project-samples#smart-on-fhir-aidbox-installation).
* Released [Aidbox API constructor on zen (alpha version)](../../aidbox-configuration/aidbox-api-constructor.md).

## August 2021 - v:2108

* Released [Aidbox Notebooks](../../overview/aidbox-ui/notebooks.md). Interactive notebooks for REST, SQL, RPC and Markdown. So now you can create your own notebooks or import community notebooks.

![Aidbox notebooks](../../.gitbook/assets/2021-09-03\_16-53-32.png)

* Released a beta version of [zen profiling](../../profiling-and-validation/profiling-with-zen-lang/extend-an-ig-with-a-custom-zen-profile.md). Advanced profiling with zen-lang to configure Aidboxes and validate data.
* Added [Asynchronous Batch Validation](../../profiling-and-validation/validation-api.md#asynchronous-batch-validation) mode to validate data in Aidbox against new profiles
* Released [Aidbox RPC API](../../api-1/rpc-api.md)
* Supported conditional patch (e.g.: `PATCH /Patient?name=foo`)
* Added an [environment variable](../../core-modules/logging-and-audit/aidbox-logs-and-datadog-integration.md) to pass the environment to Datadog (dev/staging/prod).
* Added history for [$load](../../api-1/bulk-api-1/usdload.md) and [$import](../../api-1/bulk-api-1/usdimport-and-fhir-usdimport.md) so now when using bulk import you have a source of truth for the history of every resource.
* Added [empty query params remove #238](https://github.com/Aidbox/Issues/issues/238). Please **pay attention** **when** **using json-schema** **access policy** engine: Fields with empty values, such as `[], {}, "", null`, are removed before passing request into access policy processing. Make sure to add `require` check of the fields that are validated by a json schema
* Fixed some bugs submitted by Aidbox users. Check it [here](https://github.com/Aidbox/Issues/milestone/3?closed=1).

## July 2021 - v:2107

* We've released a major Aidbox UI upgrade

![New Aidbox UI](<../../.gitbook/assets/image (49) (7) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (3) (1) (1) (1) (1) (1) (1) (1) (12) (1) (10) (10) (1) (11) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (1) (11) (10) (1) (1) (10).png>)

* Updated REST Console (check out[ the tutorial](https://bit.ly/rest\_console\_tutorial)):
  * Explicit request headers `content-type, accept` etc
  * Show raw response
  * Added syntax highlight
* Upgrade Aidbox Java version to 16.
* Added `user.email`, `user.name` to the User grid to improve UX. [#397](https://github.com/Aidbox/Issues/issues/397).
* Improved logging.
  * Reviewed and updated log event schema. The updated schema is available [here](https://docs.aidbox.app/core-modules/logging-and-audit#logs-schema).
  * Add w\_r - templated request URL for better aggregation. For example, requests like `GET /Patient/pt-1` will become `GET /Patient/?` thus allowing aggregate all read requests for monitoring.
  * Log additional DB metrics from Aidbox.Dev.
  * Added ELK, Kibana, and Grafana to Aidbox image. So now you can start exploring and analyzing logs from scratch. Check our tutorial on exploring and visualizing logs [here](https://docs.aidbox.app/app-development-guides/tutorials/how-to-explore-and-visualize-aidbox-logs-with-kibana-and-grafana).
* We added a new auth mechanism for authorization Aidbox.Cloud and Aidbox.Multibox users by JWT.
* Support for [OKTA](https://www.okta.com/) as an external OAuth 2.0 provider. Check out [the tutorial](../../security-and-access-control-1/how-to-guides/set-up-external-identity-provider/configure-okta.md).
* Added Intercom so you can get help directly from your Aidbox.Dev or Aidbox.Cloud.
* Added a guide on search performance optimization to our docs. Check it [here](https://docs.aidbox.app/api-1/api/search-parameters#optimization-of-search-parameters).

## June 2021 - v:20210610

* Added support for [Bulk API export in CSV](https://docs.aidbox.app/api-1/bulk-api-1/usddump-csv). You can use **/\[resourceType]/$dump-csv** endpoint to generate CSV file in which JSON resource structure is flattened into comma-separated format. Such an option for data export is useful for integrations with external EHR systems.
* Added support for [If-Match header](https://docs.aidbox.app/api-1/api/crud-1/delete) in DELETE operation of FHIR REST API. If-Match is most often used to prevent accidental overwrites when multiple user agents might be acting in parallel on the same resource (i.e., to prevent the "lost update" problem).
* Added support for additional mime types according to the [FHIR specification](http://hl7.org/fhir/http.html#mime-type) Accept: _application/fhir+json_, Accept: _application/json+fhir._ When one of the headers is specified for your request, the same Content-Type header is returned by Aidbox.
* Implemented integration with [Datadog](https://www.datadoghq.com/). Datadog offers cloud-based monitoring and analytics platform which integrates and automates infrastructure monitoring, application performance monitoring, and log management for real-time observability of customers. You can [configure it](https://docs.aidbox.app/app-development-guides/receive-logs-from-your-app/aidbox-logs-and-datadog-integration) as storage for Aidbox logs. The detailed guide on how to use Datadog monitoring capabilities in your Aidbox-based system you can find [here. ](https://docs.aidbox.app/app-development-guides/receive-logs-from-your-app/datadog-guide)This is an easy way to leverage HIPAA-compliant log management SaaS platform to unify logs, metrics, and traces in a single view.
* Logs that are published on Aidbox startup are cleaned up from useless data.
* SSL connection between Aidbox and PostgreSQL is now supported. Please, read the [configuration instructions](https://docs.aidbox.app/getting-started/installation/use-devbox-aidbox#configuring-ssl-connection-with-postgresql) for more details.
* Fixed a bug with race condition occurring during CRUD operations with If-Match header. Transaction rollback is implemented for the case when concurrent change happens to the resource.
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

* Builds of [aidboxdb](../../storage-1/aidboxdb-image.md) for PostgreSQL 11.11, 12.6, 13.2 are [released](https://hub.docker.com/r/healthsamurai/aidboxdb/tags?page=1\&ordering=last\_updated).
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
* Basic [\_filter](https://docs.aidbox.app/api-1/fhir-api/search-1/\_filter) query parameter support
* New [versioning scheme](https://docs.aidbox.app/versioning-and-release-notes)
* Fixed [#354](https://github.com/Aidbox/Issues/issues/354)
* [Elastic APM](https://www.elastic.co/apm) support for advanced performance monitoring
* [Two Factor Authentication](https://docs.aidbox.app/auth/two-factor-authentication) with TOTP implementation
* [AWS S3](https://docs.aidbox.app/storage-1/aws-s3) and [GCP Cloud Storage](https://docs.aidbox.app/storage-1/gcp-cloud-storage) integrations for storing content in the cloud
* Basic [\_filter](https://docs.aidbox.app/api-1/fhir-api/search-1/\_filter) query parameter support
* New [versioning scheme](https://docs.aidbox.app/versioning-and-release-notes)
* Fixed [#354](https://github.com/Aidbox/Issues/issues/354)
