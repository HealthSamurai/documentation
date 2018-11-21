# Release Notes

## Unreleased

* Subscriptions `$poll` operation and `webhook` channel: [Subscriptions](api/subscriptions.md);
* Fixed Aidbox to FHIR data transformation bug when polymorphic attributes wasn't properly handled in recursive elements such as `QuestionnaireResponse.item`
* Support [`_total`](https://build.fhir.org/search.html#total) search parameter \(\_totalMethod=count also works\)
* Support `page` search parameter \(\_page - also works\)

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

