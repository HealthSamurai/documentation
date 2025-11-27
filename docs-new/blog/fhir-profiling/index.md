---
title: "FHIR Profiling"
slug: "fhir-profiling"
published: "2025-04-11"
author: "Ivan Bagrov"
reading-time: "3 min "
tags: []
category: "FHIR"
teaser: "The base FHIR specification outlines a set of fundamental resources utilized in various contexts within healthcare."
image: "cover.png"
---

## FHIR profile

The base FHIR specification outlines a set of fundamental resources utilized in various contexts within healthcare. However, notable variation exists among jurisdictions and throughout the healthcare ecosystem concerning data requirements.

For this reason, FHIR provides the ability to impose additional constraints on the basic resources:

- Require completion of a specific field in a [resource](https://www.health-samurai.io/articles/extending-fhir-resources). (cardinality)
- Limit the maximum number of items in an array field. (cardinality)
- Define a permissible set of values. (binding)
- Apply limitations to a particular element of an array (slicing)
- Describe more complex validation rules using the FHIRPath language (constraints)
