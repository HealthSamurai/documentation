# Release Notes

Aidbox release cycle is about 1-2 weeks. Stable releases are labeled with version  - like `0.4.0`See the list of versions on [docker-hub](https://hub.docker.com/r/healthsamurai/devbox/tags)

Version under development published with `-SNAPSHOT` and `-RC[x]` postfix. For example if latest release was `0.3.0`, you will see in registry `0.4.0-SNAPSHOT` version during development and `0.4.0-RC1`in stabilisation period.

Starting from `0.4.0` Aidbox.Cloud support clusters, which can be located in different  geo regions and run different Aidbox versions. Clusters labeled _**stable**_ have latest stable version \(i.e. `max(version)`\). As well clusters with **edge** label run latest development version \(snapshot or release candidates\).

{% hint style="info" %}
* Current stable version: **0.4.7**
* Next release October:   **0.4.8**
{% endhint %}

## Road Map

* Bulk API \(export, import\)
* $upload
* Uni-version FHIR server \(i.e. support all FHIR versions, probably not all resources\)
* Async validation
* Terminology under `fhir` prefix \(issues \#51 \#50 \#38\)
* \_filter parameter support
* Managing Indexes by **AidboxIndex** meta-resource

## 0.4.8-SNAPSHOT

### TBD

## 0.4.7 \[26 September 2019\]

### Features

{% hint style="warning" %}
Now references to contained resources are represented as `localRef` attribute in [Aidbox Format](../basic-concepts/aidbox-and-fhir-formats.md)
{% endhint %}

* Managed SQL for FHIR search by [SearchQuery](../basic-concepts/search-1/searchquery.md)
* default timeout for Search Operations with [\_timeout](../basic-concepts/search-1/_timeout.md) param for hight load
* Ordered search in [$lookup](../basic-concepts/search-1/usdlookup.md)
* microseconds precision for timestamps \(Resource.meta.lastUpdated\) 
* Better Aidbox &lt;=&gt; FHIR conversion of first class extensions
* Add JWT sub & iss and client ip address to logs
* aidbox-cli elastic search logger now creates new index for each day
* Upgrade/Fix [PATCH](../basic-concepts/crud-1/patch.md) to be fully RFC conformant
* Add [X-Client-Auth](https://app.gitbook.com/@aidbox/s/project/auth-betta/access-token-introspection#x-client-auth) header
* Add **cts** column \(createdAt\) to resources \([read more](0.4.7-addendum.md)\).
* Entity.history='none' for custom resources to turn-off history
* Unique validation by `isUnique=true` in Attribute
* New Test toolkit [stresty](https://github.com/Aidbox/stresty) to report bugs and test regression \(see [samples](https://github.com/Aidbox/aidbox-tests/tree/master/test)\).

## 0.4.6 \[2 August 2019\]

{% hint style="info" %}
It's time to upgrade your database - please stop your container and change image to **aidbox/db:11.4.0 !**
{% endhint %}

### Features

* In REST Console support for multiline URL line using `\`
* New  `/<RT>/$lookup`  operation for efficient lookup search in millions of records - [read more](../basic-concepts/search-1/usdlookup.md)
* [AidboxQuery](../tutorials/custom-search.md) extended with count-query and params types and defaults
* New `/<RT>/$load` and `/$load` [bulk operations](../basic-concepts/bulk-api-1/#usdload) were added and [$import](../basic-concepts/bulk-api-1/#usdimport) was upgraded
* aidbox/db upgrade to 11.4 + fixes in json\_knife pg extension - \(deep patterns, indexes\) 
* Highlighted logs for humans in [Aidbox.Dev](../installation/setup-aidbox.dev.md) by default: docker logs -f &lt;your-box-container&gt;
* New [Aidbox.Enterprise](../installation/aidbox.enterprise.md)

## 0.4.5 \[3 July 2019\]

### Features

* `/auth/test-policy` operation for[ policy debug](../security/access-control.md#debugging)
* Support for [ident interpolation](../security/access-control.md#sql-engine) in SQL policy - `SELECT * FROM {{!params.resource/type}}`
* [SQL Migrations](../advanced/usdpsql.md#sql-migrations)
* New [\_explain](../basic-concepts/search-1/#_explain) parameter for Search API
* Beta version of bulk [import operation](../basic-concepts/bulk-api-1/#usdimport) 
* History & Snippets for REST & DB Console

## 0.4.4 _\[17 June 2019\]_

### Features

* Support for [RFC-7662 Token Introspection](https://tools.ietf.org/html/rfc7662) with TokenIntrospector resource. JwtAuthenticator is now TokenIntrospector as well with `type=jwt`
*  GraphQL Alpha - see GraphQL in Aidbox user interface

## 0.4.3 _\[20 May 2019\]_

### Features

* [Sequence API](../advanced/sequence-api.md) - use PostgreSQL sequences through REST API
* Operation bound policy in App manifest
* Clear resources deleted in App manifest \([\#70](https://github.com/Aidbox/Issues/issues/70)\)
* Bulk load terminologies - [tutorial](https://docs.aidbox.app/terminology/terminology-tutorials/load-terminologies)
* Logs from Aidbox to ElasticSearch - tutorial

### Bug Fixes

* Sort by polymorphic elements \([\#90](https://github.com/Aidbox/Issues/issues/90)\)
* Fix `ge` and `le` operators for date search \([\#85](https://github.com/Aidbox/Issues/issues/85)\)
* Search \_has and param name with dash \([\#95](https://github.com/Aidbox/Issues/issues/95)\)

## 0.4.2 _\[22 April 2019\]_

### Features

* [User Level Logs](../administration/logging-and-audit.md)
* [Structured Data Capture](../tutorials/sdc-with-custom-resources.md) with Custom Resources \(Alpha\)
* [History based replication](../tutorials/sync-data-from-aidbox.md)
* Search: new [\_ilike](../basic-concepts/search-1/#_ilike-search-non-fhir) search parameter
* Search: [\_include=\*](../basic-concepts/search-1/#_include-and-_revinclude)  support
* Search: [\_list](../basic-concepts/search-1/#_list) parameter support
* [$dump](../basic-concepts/bulk-api-1/#usddump) - Bulk Export Operation
* [Health-Check](../administration/health-check.md) endpoint by [RFC](https://inadarei.github.io/rfc-healthcheck/)

### Bug Fixes

* Auth first\_party JWT session
* html sanitise in Narrative
* Fix date validation
* Better validation error reporting

## 0.4.1 _\[8 April 2019\]_

This release is mostly dedicated to stabilisation of new auth module. Check-out it's [documentation](../auth-betta/)!

### Features

* Support for `_txid` parameter and `ETAG` header for history pooling
* Support for `X-HTTP-Method-Override` header for inflexible http clients
* Support `X-Correlation-Id` and `X-Request-Id` headers

### Bug Fixes

* Reference search with `:identifier`  - \#47
* Support for `isOpen` for applications - \#69
* Failure of search with invalid search param - \#65
* FHIR transaction endpoint urls without `/` - \#62
* Search: multiple `_sort` parameters - \#73

## 0.4.0 _\[22 March 2019\]_

### Features

* New [Auth module](../auth-betta/) \(OAuth2, OpenID Connect and SCIM implementations\)
* Web UI for Aidbox.Dev
* Migrate to Java 11
* Clusters in Cloud

#### Breaking Changes

* Auth module endpoint's changed `oauth => auth`
* Structure of Auth Client and User changed
* Turn-off insecure Aidbox.Dev start - now you have to provide AIDBOX\_CLIENT\_ID and AIDBOX\_CLIENT\_SECRET

### Bug fixes

* Fix search by reference with `:identifier` modifier
* Fix `Bundle.total` value when performing search inside Compartment
* [\#51](https://github.com/Aidbox/Issues/issues/51) Fix CodeSystem create on `/fhir` api
* [\#49](https://github.com/Aidbox/Issues/issues/49) Fix \_sort with dashes in parameter name

## 0.3.5  Hotfix

* [\#51](https://github.com/Aidbox/Issues/issues/51) - Fix `CodeSystem` create on  `/fhir` api

## 0.3.4

* Subscriptions `$poll` operation and `webhook` channel: [Subscriptions](../advanced/subscriptions.md);
* Fixed [Aidbox](https://www.health-samurai.io/aidbox) to FHIR data transformation bug when polymorphic attributes wasn't properly handled in recursive elements such as `QuestionnaireResponse.item`
* Support [`_total`](https://build.fhir.org/search.html#total) search parameter \(\_totalMethod=count also works\)
* Support `page` search parameter \(\_page - also works\)
* Added [$query](../basic-concepts/search-1/custom-search.md) - SQL query into REST Endpoint
* [\_query](../basic-concepts/search-1/custom-search.md) - custom search related to resourceType
* Implemented Compartments for requests like `/fhir/Patient/xxx/Observation`; Compartments are defined with CompartmentDefinition resource
* `Observation/$lastn` is accessible as a part of Patient's compartment: `/fhir/Patient/ID/Observation/$lastn`
* Implemented `:iterate` modifier for `_include` search parameter
* Refactored FHIR search engine internals to leverage PostgreSQL's GIN index
* Add `Box-Name` headers \(allows to specify box, without changing the url\)
* Implement [full-text search](https://docs.aidbox.app/api/history#full-text-search) with RUM indices \(requires PostgreSQL 11 with the `rum` extension\)
* Experimental support for FHIR 4 resources and search parameters
* Experimental XML support for endpoints starting with `/fhir/` prefix

## 0.3.3

* Introduced refresh tokens into `oauth` module;
* Implemented `/Observation/$lastn` and `/fhir/Observation/$lastn` endpoints;
* Support `__debug=policy` parameter to inspect and debug request authorization layer;
* `CapabilityStatement` metadata endpoint;
* Added `If-Match` header support for atomic updates;
* Fix metadata migration bug \(fixes issues \#16 and \#6\)
* Added `complex` engine for AccessPolicy resource

## 0.0.3

* Started to reflect all the changes in the CHANGELOG.

