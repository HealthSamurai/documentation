---
title: "How to resolve FHIR SearchParameters conflicting codes"
slug: "resolve-fhir-searchparameters-conflicting"
published: "2024-09-02"
author: "Aleksandr Kislitsyn"
reading-time: "3 min read"
tags: []
category: "FHIR"
teaser: "What is SearchParameter’s code?\nWhich SearchParameters are available in the FHIR Server?\nIs it possible to have a clash of two different SearchParameters having the same code?\nHow should this clash be resolved according to the FHIR specification?\nWhat to do in such situations in Aidbox?"
image: "cover.jpg"
---

## What is SearchParameter’s code?

In FHIR, [SearchParameter](https://build.fhir.org/searchparameter.html) is a resource that defines how searches can be performed on other FHIR resources. It specifies the parameters that can be used in a search query and how those parameters relate to the elements within a FHIR resource.

SearchParameter.code, in particular, [defines the search parameter name](https://build.fhir.org/searchparameter-definitions.html) that is recommended to be used in the URL.

For example, the Patient-name search Parameter from the core FHIR specification, has

```javascript
"code" : "name"
```

which means we should expect any FHIR Server should to be able to execute the following search:

```javascript
GET /fhir/Patient?name=John
```

and return the appropriate results.

## Which SearchParameters are available in the FHIR Server?

According to the [FHIR specification](http://hl7.org/fhir/search.html#standard), the FHIR Server should support the following SearchParameters:

1. All the SearchParameters defined by the [core FHIR specification.](http://hl7.org/fhir/searchparameter-registry.html)
2. Externally defined SearchParameters.   
   Most FHIR Servers:
   1. Provide the ability to define the supported IGs (and that usually means that all the SearchParameters from the IG will be available in the FHIR Server)
   2. Provide the ability to define SearchParameters directly, e.g. by defining SearchParameter resources

Aidbox, in particular, allows:

- [Importing IGs in runtime](https://www.health-samurai.io/articles/4-ways-to-upload-igs-to-aidbox-pros-and-cons).
- [Defining SearchParameters in runtime](https://docs.aidbox.app/api-1/fhir-api/search-1/custom-search-parameter/custom-search-parameters).

## Is it possible to have a clash of two different SearchParameters having the same code?

Definitely!

If you are going to use multiple implementation guides (IGs) in your FHIR Server, you may find the clashing parameters across different IGs, e.g. 86% of search parameters, defined in the [US Core Implementation Guide](https://www.hl7.org/fhir/us/core/search-parameters-and-operations.html#search-parameters-defined-by-this-implementation-guide), have the parameter with the same code from FHIR core specification within the same Resource.

And, of course, if you are creating custom SearchParameters in your FHIR Server, most likely nothing will prevent you from creating the SearchParameters with the codes, already used in the standard FHIR search parameters or in other externally defined search parameters.

## How should this clash be resolved according to the FHIR specification?

<https://hl7.org/fhir/searchparameter-definitions.html#SearchParameter.code>

```javascript
SearchParameter.code

Element Id
SearchParameter.code
Definition
The label that is recommended to be used in the URL or the parameter name in a parameters resource for this search parameter. In some cases, servers may need to use a different CapabilityStatement searchParam.name to differentiate between multiple SearchParameters that happen to have the same code.
```

<https://build.fhir.org/capabilitystatement-definitions.html#CapabilityStatement.rest.resource.searchParam.name>

```javascript
CapabilityStatement.rest.resource.searchParam.name

Element Id
CapabilityStatement.rest.resource.searchParam.name
Definition
The label used for the search parameter in this particular system's API - i.e. the 'name' portion of the name-value pair that will appear as part of the search URL. This SHOULD be the same as the SearchParameter.code of the defining SearchParameter. However, it can sometimes differ if necessary to disambiguate when a server supports multiple SearchParameters that happen to share the same code
```

In case there are multiple SearchParameters with the same code, FHIR Server should resolve the clash and decide which SearchParameter will be responsible for handling the request and this should be reflected in the server’s Capability Statement - the **CapabilityStatement.rest.resource.searchParam.name** must be unique in the context of the resource.

## What to do in such situations in Aidbox?

**Current Situation**   
As of now Aibox uses SearchParameter.name for the URL label instead of SearchParameter.code for all the SearchParameters, no matter if there’s a clash or not, e.g. [USCorePatientName](https://www.hl7.org/fhir/us/core/SearchParameter-us-core-patient-name.html) SearchParameter is used in the following request:

```javascript
GET /fhir/Patient?USCorePatientName=John
```

**Nearest Plans**

We want to provide a clear and flexible configuration method in Aidbox to specify which SearchParameter will handle searches by particular label.

The configuration will be available at runtime with REST API and in the UI with the custom configuration Resource that we are going to introduce.

We will use this approach to manage all the similar use cases (default profiles for validation of the resources, operations with non-unique names and so forth).

Stay tuned to see what we can achieve in one of the upcoming releases!

Aidbox offers [free development licenses](https://docs.aidbox.app/getting-started/run-aidbox-locally-with-docker/run-aidbox-locally), allowing you to bring your FHIR ideas to life without expiration concerns. These licenses are ideal for running these configurations, experimenting with FHIR, participating in FHIR Connectathons, and conducting POCs, with a database size limit of 5 GB.   
[Get started with Aidbox](https://aidbox.app/ui/portal#/)
