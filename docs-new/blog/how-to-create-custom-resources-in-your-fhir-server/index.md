---
title: "How to create custom resources in your FHIR server?"
slug: "how-to-create-custom-resources-in-your-fhir-server"
published: "2024-09-10"
author: "‍Evgeny Mukha, Vasiliy Kupriakov"
reading-time: "4 min read"
tags: ["custom resources", "fhir"]
category: "FHIR"
teaser: "The FHIR R5 specification includes 157 resources, from common ones like Patient to specialized ones. But when your data doesn't fit any existing resource, you may need to create a custom type. This article explores 3 ways to create custom FHIR resources and weighs their pros and cons."
image: "cover.png"
---

## Struggle is ‘Not’ Real

The FHIR R5 specification includes 157 resources, from basic ones like Patient and Observation to specific ones like ExplanationOfBenefit for billing. However, there are cases when your data doesn’t fit any existing FHIR resource, and you want to create a custom resource type.

In this article we’ll cover 3 ways to create custom resource types on your FHIR server and evaluate their strengths and weaknesses.

## Let's Get Closer

Overall, FHIR suggests several ways for creating custom resources:

1. **Use the Basic resource with extensions**: This lets you customize your data model while staying compatible with the FHIR framework.
2. **Create new resources in the same way FHIR does**: It is not allowed by the FHIR specification, but the result is the most FHIR-like.
3. **Use logical models**: Anyone can define them, but they often feel alienated from the FHIR resource model.

Let's explore each of these methods to determine the best fit for your needs.

## 1. Use the Basic Resource in FHIR

The Basic resource is designed to handle concepts that are not yet defined in the FHIR framework. It primarily provides metadata about the resource, with all other data elements expressed using FHIR extensions.

**Pros**:

- Recommended by FHIR itself
- Ensures interoperability

**Cons**:

- Requires extension management via an Implementation Guide
- Can lead to a complex data model

One of the main issues is you need to map your data to and from basic resources. Here is an example of a simple resource

```javascript
id: my-custom-resource
resourceType: MyCustomResource
myKey: test
```

And this is how it looks when modeled using the Basic resource:

```javascript
id: my-custom-resource
resourceType: Basic
code:
  coding:
    - system: http://example.org/CodeSystem/my-custom-resorces
      code: my-custom-resource
meta:
  profile:
    - http://example.org/StructureDefinition/my-custom-resource
extension:
  - url: http://example.org/StructureDefinition/my-custom-resource-my-key
    valueString: test
```

## 2. Create new resources in the same way FHIR does

Alternative way is to define a custom resource using StructureDefinition with ‘kind: resource’ and ‘derivation: specialization’. This is the way FHIR defines all their resources.

However, the specification prohibits doing this for anyone but the FHIR specification itself.

Still, there are some limitations in this approach. Some FHIR resources like SearchParameter or CapabilityStatement refer to resource types, and they allow to use only the resource types defined in the FHIR specification (via required binding to some ValueSet). Another limitation is a custom resource cannot be the target of a reference.

**Pros**:

- Straightforward resource definition using StructureDefinition, specifically designed for this purpose
- Some implementations already use this method

**Cons**:

- Results in non-interoperable resources
- Prohibited by the FHIR specification
- Have some limitations in interaction with other resources

## 3. Use logical models

Another way to define a custom resource is by using StructureDefinition with 'kind' set to 'logical' and 'derivation' set to 'specialization.' Unlike the previous method, anyone can create these resources. These StructureDefinitions, often called FHIR logical data models or simply logical models, are intended to model any data using FHIR abstractions.

A good example of the recent use of logical models is the [SQL on FHIR Implementation Guide](https://build.fhir.org/ig/FHIR/sql-on-fhir-v2/) and the ViewDefinition resource. This model represents a tabular projection of a FHIR resource, where the columns and inclusion criteria are defined by the FHIRPath expressions.

The [Da Vinci Project](https://www.hl7.org/about/davinci/) also leverages logical models in its Implementation Guides to enhance interoperability and support value-based care initiatives. For instance, the Coverage Requirements Discovery (CRD) guide includes logical models to describe the information that should be captured for each CRD transaction, ensuring consistent data collection and reporting across different systems.

However, this approach has its downsides. Logical models were not designed to be resources. It is expected that a logical model is derived from the Base or the Element type. While it is not prohibited to derive a logical model from Resource, there is no guidance from the FHIR specification on semantics in this case. If you define a new resource type using a logical model, all the limitations from the previous approach apply. And even more, Bundles can only contain logical models derived from the Resource type. Additionally, the 'type' property of the logical model must contain a full URL, including the schema and other URL details, which requires additional processing from you as an implementer in the case of CRUD interactions.

**Pros**:

- Straightforward resource definition via StructureDefinition
- Interoperable

**Cons**:

- Logical models are a second-class citizen
- Should derive from too bare-bone types
- Have limitations in interaction with other resources
- Have limitations in interaction with the Bundle resource

### No Magic: Just Methods that Work

Here's how you can create custom resources using the built-in capabilities of the [Aidbox FHIR Server](https://www.health-samurai.io/aidbox), and why this method is so convenient.

**Define custom resources like standard FHIR ones**

While the FHIR specification doesn’t allow it, following FHIR's approach for custom resources is often the easiest way. It behaves exactly like any other resource. You can use standard tooling to create them. Aidbox offers a way to address the limitations of this method.

However, remember that custom resources are not interoperable and should only be used if FHIR’s predefined resources cannot meet your needs.

**Solving previous limitations**

FHIR defines some ValueSets that list all resource types. During validation, Aidbox checks that either the resource type is included in the ValueSet or it is a custom resource. This allows the use of custom resources within other resources like CapabilityStatement or SearchParameter, or in the ‘type’ property of references.

FHIR allows references to only point to FHIR resources. Aidbox extends this capability by allowing you to specify custom resources as reference targets as well.

**👉 Click here to read more in the** [**Aidbox Docs**](https://docs.aidbox.app/storage-1/custom-resources)**.**

## Run Aidbox Locally or in the Cloud

Aidbox can be run locally or in the cloud, providing flexibility to test all these methods. Whether you're exploring custom resources or leveraging existing ones, Aidbox offers a robust platform to enhance your FHIR server's capabilities.

[Free Development licenses](https://aidbox.app/ui/portal#/signin) are also available.

*Authors*:   
[**Evgeny Mukha**](https://www.linkedin.com/in/evgeny-mukha-192293228),   
Full-Stack Software Engineer at Health Samurai

[**Vasiliy Kupriakov**](https://www.linkedin.com/in/rublag/),   
Full-Stack Software Engineer at Health Samurai
