# Release Notes

## Unreleased \(0.4.0\)

### **Planned**

Terminology under `fhir` prefix \(issues \#51 \#50 \#38\)

### Features

* New [Auth module](auth-betta/) \(OAuth2 implementation\)
* Web UI for Aidbox.Dev
* Migrate to Java 11

### Bug fixes

* Fix search by reference with `:identifier` modifier
* Fix `Bundle.total` value when performing search inside Compartment
* [\#51](https://github.com/Aidbox/Issues/issues/51) Fix CodeSystem create on `/fhir` api
* [\#49](https://github.com/Aidbox/Issues/issues/49) Fix \_sort with dashes in parameter name

## 0.3.5  Hotfix

* [\#51](https://github.com/Aidbox/Issues/issues/51) - Fix `CodeSystem` create on  `/fhir` api

## 0.3.4

* Subscriptions `$poll` operation and `webhook` channel: [Subscriptions](api/subscriptions.md);
* Fixed [Aidbox](https://www.health-samurai.io/aidbox) to FHIR data transformation bug when polymorphic attributes wasn't properly handled in recursive elements such as `QuestionnaireResponse.item`
* Support [`_total`](https://build.fhir.org/search.html#total) search parameter \(\_totalMethod=count also works\)
* Support `page` search parameter \(\_page - also works\)
* Added [$query](api/custom-search.md) - SQL query into REST Endpoint
* [\_query](api/custom-search.md) - custom search related to resourceType
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

