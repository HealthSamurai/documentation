---
title: "Using FHIR to Simplify Healthcare Application Development "
slug: "using-fhir-to-simplify-healthcare-application-development"
published: "2018-03-14"
author: "Pavel Smirnov"
reading-time: "7 min read"
tags: []
category: "FHIR"
teaser: "FHIR standard simplifies the development process with out of the box healthcare data models, RESTful APIs and robust guidelines for integration with legacy healthcare data standards such as HL7 V2, CCD, and others."
image: "cover.png"
---

Healthcare organizations have many choices to make when developing solutions with regards to interoperability, technology stacks, HIPAA compliance, integration and data management. Changes in health information technology must be taken into consideration to ensure products are future-proof, and solutions need to be interoperable to support the transition to value-based care. FHIR is a open standard that has gained widespread industry recognition and support for its potential to make interoperability a reality in healthcare. FHIR is well documented, easy to learn and is supported by a passionate, growing community of health IT experts, and shows all the signs of a standard that is here to stay. FHIR does not impose any limits on the development practices you use, and offers many benefits for data management and interoperability.  This blog explains why organizations should use FHIR and how using a FHIR backend simplifies the development process and enables interoperability.

## Why do you need FHIR?

When developing solutions for healthcare, a company makes a lot of choices between technology stacks, development platforms, HIPAA hostings, EHR integration pathways, standards and terminologies. Some of these choices do not affect the immediate outcome, but long term consequences might be enormous.

Two solutions can equally address current business needs, but their fates may become very different. One solution will become outdated quickly and the other will flourish over the years. To make decisions that will give your solution a long lifecycle, a company must understand what will happen to health information technology in the coming years.

Managing population health in a value-based system is a challenge for any healthcare organization. No single digital healthcare solution can address this challenge alone, and there is a growing need to support an ecosystem of connected, interoperable healthcare applications.

Such an ecosystem was hardly imaginable a few years ago because of the lack of an **interoperability** standard recognized by the industry. Nowadays there is a clear winner among standards - Fast Healthcare Interoperability Resources (FHIR) - and many vendors and provider organizations have already started their journeys to the new world of digital health platforms, open APIs and application marketplaces.

Choosing FHIR for your next health care project could be the most important choice in making your solution future-proof and ready for the next-generation health information ecosystem where data flows seamlessly between EHRs, patient-facing mobile apps, and medical devices.

## How is development different with a FHIR backend?

When you develop your solution on top of a FHIR backend, many things will stay the same. Building with a FHIR backend doesn’t alter your development process. You will still need to complete a project discovery phase, formulate a project vision and a beginning scope. You can continue to use your favorite Agile Practices, Test Driven Development, DevOps and Continuous Integration. FHIR will not get in your way with any restrictions on your processes.

Using a FHIR backend also does not affect the choice of your technology stack either. Whether you prefer Java, Ruby, .NET, Swift or Clojure, you will find open source libraries that supports your technology of choice. You will use modern web technologies such as REST APIs for working with your backend but FHIR will not create any barriers for you to continue using your favorite tech stack.

The moment when you start working with data is when the first significant difference comes in. If you are developing your solution from scratch, you must design data models and structures to store your data. But with FHIR, all you have to do is to find the right fit for your data in the open FHIR data models. It may initially be a challenge for engineers who have no previous FHIR or healthcare IT experience, but they will catch up very quickly as FHIR is well documented and easy to learn.

The FHIR documentation is freely available online at <https://www.hl7.org/fhir/overview.html> and is concise and easy to understand. An entire community of **interoperability experts is only one click away.** Just register at <https://chat.fhir.org/login/> and you can get all your questions answered in a matter of hours, if not minutes. Health Samurai has significant experience in applying FHIR data models to our clients’ data, but if we have even a minor doubt we discuss our concerns with the FHIR community right away. In doing so, you not only ensure that your use of the FHIR resources and their fields is correct but also provide valuable feedback about the standard and its documentation to the community.

> Get started with the Aidbox [FHIR Server](https://www.health-samurai.io/aidbox) for data storage, integrations, healthcare analytics, and more, or [hire our team](https://www.health-samurai.io/services) to support your software development needs.

### Mapping your data to FHIR

Let’s imagine that you are recording information about patient diagnoses. If you are using a FHIR server as your backend you will need to learn how FHIR represents patient diagnoses. You will learn that FHIR calls diagnoses ‘conditions’ and will find a description of the [condition resource](https://www.hl7.org/fhir/condition.html) in the FHIR specification. You might not need all the fields of the condition resource and as you might already know, almost no fields are mandatory.

There is a good chance that FHIR will remind you about important data elements that have slipped away from your attention and guide you to the right terminologies. For example, those who are new to healthcare IT will learn about the SNOMED terminology when reading about the FHIR condition resource.

Begin by choosing the fields you need and document the mapping you got. After that, you are ready to store and manipulate data about diagnoses in your FHIR backend.

### Extending FHIR data models

As mentioned earlier, you might not need all the fields from the FHIR resources that you are using. But at the same time you might find that some of your data doesn’t fit into the FHIR format very well. It might surprise you that some data elements that are very common in accordance with your organization aren’t considered common by the FHIR community. For example race, ethnicity, organ donor status, nationality, and religion haven’t been included into the core Patient resource. If you feel that some missing data elements or even resources have been treated this way undeservedly, you can always raise this concern within the FHIR community as FHIR is an open, community driven standard. And you can alway add missing data elements with help of [FHIR extensions](https://www.hl7.org/fhir/extensibility.html).

For example, a race [extension example](http://hl7.org/fhir/DSTU2/patient-example-us-extensions.json.html) might look like this:

> { "url": "http://hl7.org/fhir/StructureDefinition/us-core-race",   
> "valueCodeableConcept": {  
> "coding": [ {   
> "system": "http://hl7.org/fhir/v3/Race",  
> "code": "1096-7"   
> }]}}

Now here is the part where Health Samurai’s [Aidbox FHIR backend](https://www.health-samurai.io/new-aidbox) provides an unorthodox approach to the problem of complex FHIR extensions. While doing real life implementations of FHIR based solutions, we came across many situations where FHIR resources had to be heavily extended to support client workflows. What we found was that following standard practices of working with extensions added a significant burden on engineers and increased time to market and cost of the development for the solution. So we made Aidbox treat extensions just like any core data without discrimination, to simplify extending FHIR for engineers. To add race to a patient resource in Aidbox, you can simply write ‘patient.race’, whereas normal FHIR extensions would require much longer notation.

But what happens to **interoperability** when FHIR extensions are simplified? Health Samurai handles this for you with Aidbox. Aidbox keeps all the FHIR metadata and knows when engineers add a field to the resource that is not a part of the FHIR specification. And with this knowledge, Aidbox generates a FHIR compliant API layer for all your data which re-wraps all the additional fields into FHIR-compliant extensions.

## Integration of your FHIR based solution with EHR systems

Integration of your FHIR-based solution with EHRs is much easier than it is for a custom solution. If you are developing a SMART on FHIR application, you’ll be receiving EHR data in the FHIR format right away. And storing that EHR data to your FHIR backend will not require any efforts on your end. SMART on FHIR is getting more popular and is supported by several major EHR vendors.

Epic’s FHIR API can be explored at <https://open.epic.com/Interface/FHIR> and Cerner’s can be explored at <http://fhir.cerner.com/>.

Even if you are dealing with legacy EHR data, doing this with a FHIR backend is still easier than with a custom solution. Many popular HL7 engines that are implemented in hospital systems can easily translate legacy HL7 v.2 data to FHIR. There are also well-documented mappings for HL7 v.2, CCD and X12 data. We've implemented a feature in our Aidbox backend for translating legacy standards data to FHIR as well. Our HL7 v.2 to FHIR connector allows customization of translation mapping via specially designed configuration files. This approach allowed us to handle different versions of HL7 v.2 and vendor specific Z-segments without changing the solution’s codebase.

Overall, building with a FHIR backend facilitates integration with EHR technologies and improves the interoperability of your healthcare solutions by enabling plug and play connectivity in the FHIR ecosystem.

[![Pluggable Aidbox FHIR API for EHRs](image-1.png)](https://www.health-samurai.io/fhir-api)
