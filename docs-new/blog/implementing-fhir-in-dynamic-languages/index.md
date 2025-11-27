---
title: "Implementing FHIR in Dynamic Languages"
slug: "implementing-fhir-in-dynamic-languages"
published: "2015-02-03"
author: "Niquola Ryzhikov"
reading-time: "4 min read"
tags: []
category: "System Design"
teaser: "The FHIR standard is becoming the de facto way to manage healthcare data. Our CTO explains why FHIR implementations with dynamic languages could provide valuable feedback to the FHIR standard and FHIR community. "
image: "cover.jpg"
---

*Published on February 3, 2015 by Nicola Rizhikov*

Most of the [FHIR](http://www.hl7.org/implement/standards/fhir/) reference implementations are based on code-generation for strongly typed languages. So there are missed land of functional and/or dynamically typed languages like JavaScript, Ruby, Python, Clojure, Grovy etc.

It is no secret that development using dynamic languages is sometimes much faster and more fun. We could code-generate SDKs for them, but then we get the worst from both worlds: dynamic languages do not have compiler checks and good support for IDE and we absolutely do not use the power of dynamism.

In this post I want to discuss implementing FHIR in dynamic languages and collect ideas and guidelines for implementers.

**Internal Representation**

Most of FHIR implementations generate classes to represent resources in memory, but dynamic languages have hash-maps (dicts, tables) and arrays (vectors) out of box with convenient literals, so internal representation could be very close to json format:

//jsvar pt = {resourceType: "Patient", name:[{text: "Smith"}]};; clj(def pt {:resourceType "Patient" :name [{:text "Smith"}]})# rbpt = {resourceType: "Patient", name:[{text: "Smith"}]}# pypt = {'resourceType': 'Pateint', 'name': [{'text':'Smith'}]}

FHIR primitive types (date, integer etc) could be mapped to corresponding primitive types of concrete language.

This representation is very generic and convenient for manipulation and interaction with other libraries and frameworks.

> *“It is better to have 100 functions operate on one data structure than 10 functions on 10 data structures.”*

> *Alan Perlis*

#### Minimalistic API

We could create a simple and solid API, which consists only of few functions:

// Coerce primitive types from string to appropriate types.// profiles - collection of profiles with meta information // (ie types of elements, multiplicity etc)function coerce(profiles, resource){...}// walk recursively through resource and validate against profile// returns collection of errors and warnings // (OperationOutcome.issue)function validate(profiles, resource){}// parse json or xml into internal representation, optional function parse(str, format, profiles){}//serialize resource into xml or json stringfunction generate(str, format, profiles){}//convert from one format into anotherfunction convert(resource, toFormat, profiles)

Functions `**coerce**` and `**validate**` are coupled, because some validations (date, uri formats etc) should be performed before coercion and some of them require already coerced data (for example: date invariants, numeric expressions). Function `**convert**` is derivative from parse->generate.

#### Pure Transformations

Almost all functions requires meta-information (Profiles). Theoretically it’s possible to drop some dependencies from metadata, if json->xml and xml->json formats would be mapped to each other unambiguously . Now pure transformation from json->xml is not possible, because xml requires strict order of elements (accidental constraint), and xml->json is broken because xml does not distinct collections and singular attributes, but json does.

There are two options for meta-information: it could be hard-coded into implementation or parametrized (i.e. passed as arguments). Hard-coded version is similar to code-generated one and easy to use, but every changes in basic profiles require to regenerate implementation. Parametrised version looks more stable to such changes and could be updated in runtime.

#### Polymorphic Attributes

If we take json-like internal representation, then we have semantic flaws related to **polymorphic attributes** (attr[x]): in generated ref. implementations and profiles such attributes are represented as one element or field, but in serialization formats they loose this semantic and expressed by postfixing keys or tags:

in Observation profile we have applies[x]in Java: observation.setApplies/getAppliesin json and xml: appliesPeriod,appliesDateTime

If we would like to check `**applies**` element for presence (for example in validation), we have to scan and manipulate on keys or tags by pattern `**applied\***`. Another consequence is impossibility to assign multiplicity more then one for such attributes. This problem could be solved by introducing another serialization rules for polymorphic attributes:

applies: {  Period: { start: '...', end: '...'}}//orapplies: {  type: 'Period',  value: {start: '...', end: '...'}}

#### Schematron rules

Additional invariants for resources are published as schematron rules. Schematron operates on xml, so you need additional conversion into xml and all xml stuff (xquery, xslt etc) or parse and simulate schematron expressions.

For symmetry rules could be expressed in JavaScript with set of predefined helper functions. Today JavaScript/JSON is supported everywhere (displacing xml) in languages, browsers and databases.

#### Executable specification

For all reference implementations it would be helpful to create a top level tests suite. For example: format’s conversion tests described as pairs of xml <-> json source/result fixtures with many corner cases, validation cases as pairs InvalidResource->OperationOutcome.</->

#### Clojure & JavaScript implementation

We’ve started clojure implementation of FHIR — <https://github.com/fhirbase/fhir.clj>. It is an early alpha, but illustrate described approach. Library is compact and simple (only 6 files; ~600 sloc) and not sensitive to addition/changes of profiles. When design of clojure library become solid, we are going to port it to [fhir.js](https://github.com/fhir/fhir.js)

#### Conclusion

Our opinion is that implementation of FHIR for dynamic languages could give valuable feedback to standard and assists to separate implementation concerns from the core of the standard. We encourage you to participate in the discussion and make contributions. The expected result is guidelines and API design for FHIR implementations. Here is empty project to start collaborate — <https://github.com/FHIR/fhir-impl-guidelines>.

Dynamic languages are more quick and fun for development and good choice for connectathon’s mini-projects. I hope in Paris we will present interactive REPL/console for clojure and javascript ☺.

> Get started with the Aidbox [FHIR Server](https://www.health-samurai.io/aidbox) for data storage, integrations, healthcare analytics, and more, or [hire our team](https://www.health-samurai.io/services) to support your software development needs.

[![Aidbox FHIR Platform Free Trial](image-1.png)](https://www.health-samurai.io/aidbox)
