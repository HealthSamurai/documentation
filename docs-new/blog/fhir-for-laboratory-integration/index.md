---
title: "Choosing FHIR for Laboratory Integration"
slug: "fhir-for-laboratory-integration"
published: "2015-01-08"
author: "Niquola Ryzhikov"
reading-time: "3 min read"
tags: []
category: "FHIR"
teaser: "Deciding between which healthcare data standard to use can be hard. We evaluated whether HL7 v2, v3 & CDA, or FHIR is the best choice for a laboratory integration project. We found FHIR is the best choice because the FHIR API perfectly compliments the custom API of the laboratory system. "
image: "cover.jpg"
---

*Published on January 8, 2015 by Nicola Rizhikov*

Saint-Petersburg government started a project whose goal is to create a united integration bus for exchanging laboratory orders and results for state ambulatories, hospitals and laboratories.

A lot of objectives are expected: paperless orders, quick access to results for physicians and patients, resources planing & optimization, information for analytic & data mining by state healthcare organizations.

The first iteration was to analyze current processes and create a working prototype. This iteration was completed by creating a custom SOAP API and integrating some organizations from the focus group.

At the end of the pilot project it became clear that some international standard should be used as a foundation. We've been invited to help make choice between standards (we have experience in integration, but in the USA realm).

We have considered HL7 v2, HL7 v3 & FHIR and compared them with a custom API.

### HL7 v2

At first sight, [HL7 v2](http://www.hl7.org/implement/standards/product_brief.cfm?product_id=185) looks to be the best candidate. This may be true for the United States, because there are plenty of working systems (HIS, EHR, LIS) already exchanging HL7 v2 messages. But the picture in Russia is different: most laboratory information systems are custom made and do not use HL7 v2. So the key argument for HL7 v2 is not valid, but all birth injuries of v2 come into play.

HL7 v2 protocol was evolved ad-hoc and we have archaic messages format, no built-in extensibility & machine-readable profiling/constraints and no future (standard in only-support/non-development stage).

Archaic messages format requires to implement awkward parsers and builders almost from scratch. Only Java & .Net developers could reuse [HAPI](http://hl7api.sourceforge.net/) and [nHAPI](http://nhapi.sourceforge.net/home.php) libraries, but other popular platforms (node.js, erlang, php, python, ruby, etc) do not have convenient & robust libraries.

Absence of extensibility and profiling/constraints could be fixed using approach like [LRI](http://www.hl7.org/implement/standards/product_brief.cfm?product_id=279). But at least half of LRI is just fixes & patches for HL7 v2 bugs without bringing real value.

***In summary***: HL7 v2 requires a lot of meaningless work to deploy an obsolete standard.

### HL7 v3 & CDA

Another candidate is HL7 v3 & CDA. There are two **pro** arguments: some of HL7 v3 standards are now normative in Russia and HL7 working group released in 2013 DSTU for laboratory orders & results.

HL7 v3 has a solid theoretical foundation and is designed for real semantic interoperability, but missed one important property — implementability. This could be illustrated by fact, that even until now there are no good implementation libraries. It also has heavy learning curve and is complex for implementers. HL7 v3 is not one standard, but deep stack of standards, so even localization of this standard looks time-consuming.

The project has a deadline of about 6 months and it does not look realistic to use HL7 v3 & CDA to bootstrap it. All of this time will be spent on developer education.

### FHIR

The next option is [FHIR](http://www.hl7.org/implement/standards/fhir/). This is a modern standard for **fast** interoperability. The standard is designed to find the right balance between needs of semantic healthcare information exchange and implementation complexity.

It has built-in extensibility & profiling and a good toolbox of libraries and services. FHIR allows you to implement only the required part of standard, which could dramatically simplify the project start and makes possible incremental addition of new features.

We've compared the custom API developed in the first iteration with the FHIR API and found almost a perfect match. To be FHIR compliant the current API should re-implement only a few resources: [Organization](http://www.hl7.org/implement/standards/fhir/organization.html), [Practitioner](http://www.hl7.org/implement/standards/fhir/practitioner.html), [Patient](http://www.hl7.org/implement/standards/fhir/practitioner.html), [Encounter](http://www.hl7.org/implement/standards/fhir/encounter.html), DiagnosticOrder, [DiagnosticReport](http://www.hl7.org/implement/standards/fhir/diagnosticreport.html) & [Observation](http://www.hl7.org/implement/standards/fhir/observation.html). Required business rules could be expressed as FHIR extensions. Laboratory, Hospital & Ambulatory systems could communicate with the bus via a REST API, using an available reference implementation and client libraries. Public FHIR services could be used as sandboxes for development. Health Samurai with the support of HL7 Russia already localized most of [FHIR documentation](http://fhir-ru.github.io/).

Surely there are a lot of small technical questions, but we have to say that FHIR perfectly fits the needs and requirements of this project.

We hope that managers and decision makers will make the right choice with FHIR and achieve their goals.

> Get started with the Aidbox [FHIR Server](https://www.health-samurai.io/aidbox) for data storage, integrations, healthcare analytics, and more, or [hire our team](https://www.health-samurai.io/services) to support your software development needs.

[![Aidbox FHIR Platform Free Trial](image-1.png)](https://www.health-samurai.io/aidbox)
