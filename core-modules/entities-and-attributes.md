---
description: Aidbox Core Engine Overview
---

# Entities & Attributes

{% hint style="warning" %}
Since the 2405 release, using Aidbox in FHIRSchema mode is recommended, which is incompatible with zen or Entity/Attribute options.

[Setup Aidbox with FHIR Schema validation engine](../modules-1/profiling-and-validation/fhir-schema-validator.md)
{% endhint %}

{% hint style="info" %}
Note: you can not use Attributes and [zen profiles](../profiling-and-validation/profiling-with-zen-lang/) on the same resource at the same time
{% endhint %}

In Aidbox, almost everything is a **Resource**. Resource has a type, which is placed in **resourceType** attribute. All resource types are described with "meta-resources" - **Entity** and **Attribute**. **Entity** defines the resource or complex type and a set of **Attributes** describes its structure.

### Entity

Entity is meta-resource, which describes all resources in Aidbox. There are three types of Entities: resource, type, and primitive.

#### Primitives:

Aidbox has a set of built-in primitive types. You can get a list of primitive types by this request :

```yaml
GET /Entity?type=primitive\
  &_result=array\
  &_elements=id,description\
  &_count=1000\
  &_sort=_id

# 200
- {id: base64Binary, description: Primitive Type base64Binary}
- {id: boolean, description: Primitive Type boolean}
- {id: canonical, description: Primitive Type canonical}
- {id: code, description: Primitive Type code}
- {id: date, description: Primitive Type date}
- {id: dateTime, description: Primitive Type dateTime}
- {id: decimal, description: Primitive Type decimal}
- {id: email, description: Primitive Type email}
- {id: id, description: Primitive Type id}
- {id: instant, description: Primitive Type instant}
- {id: integer, description: Primitive Type integer}
- {id: keyword, description: Primitive type for keywords and ids}
...
```

For now, there is no way to define custom primitive type. Contact us if you need it.

#### Complex Types:

Complex types are built from primitive types and are re-used to define repeating complex elements in resources. Here is the request to inspect complex types in your box.

```yaml
GET /Entity?type=type\
  &_result=array\
  &_elements=id,description\
  &_count=1000\
  &_sort=_id

# 200
- {id: Address, description: An address expressed using postal conventions (as opposed to GPS or other location definition formats)}
- {id: Age, description: A duration of time during which an organism (or a process) has existed}
- {id: Annotation, description: Text node with attribution}
- {id: Attachment, description: Content in a format defined elsewhere}
- {id: CodeableConcept, description: Concept - reference to a terminology or just  text}
- {id: Coding, description: A reference to a code defined by a terminology system}
- {id: ContactDetail, description: Contact information}
- {id: ContactPoint, description: 'Details of a Technology mediated contact point (phone, fax, email, etc.)'}
- {id: Contributor, description: Contributor information}
- {id: Count, description: A measured or measurable amount}
```

You can list Attributes of complex type with:

```yaml
GET /Attribute?entity=Address\
  &_result=array\
  &_elements=path,type.id,description\
  &_sort=_id

# 200
- path: [city]
  type: {id: string}
  description: Name of city, town etc.
- path: [country]
  type: {id: string}
  description: Country (e.g. can be ISO 3166 2 or 3 letter code)
- path: [district]
  type: {id: string}
  description: District name (aka county)
- path: [id]
  type: {id: string}
  description: Unique id for inter-element referencing
...
```

#### Resources

Entities with the type **resource** represent Aidbox resources. Aidbox generates the database schema to store resources on the fly, REST endpoints to Create/Read/Update/Patch/Delete/Search, validation schema etc.

You can list resources in your box with:

```yaml
GET /Entity?type=resource\
  &_result=array\
  &_elements=id,module,description\
  &_count=1000\
  &_sort=_id

# 200
- {id: AccessPolicy, module: auth}
- {id: Account, module: fhir-4.0.0, description: 'Tracks balance, charges, for patient or cost center'}
- {id: ActivityDefinition, module: fhir-4.0.0, description: 'The definition of a specific activity to be taken, independent of any particular patient or context'}
- {id: AdverseEvent, module: fhir-4.0.0, description: 'Medical care, research study or other healthcare event causing physical injury'}
- {id: AidboxConfig, module: box}
- {id: AidboxJob, module: proto, description: Aidbox jobs to run}
- {id: AidboxJobStatus, module: proto, description: Aidbox jobs status}
- {id: AidboxMigration, module: proto, description: Aidbox migrations}
- {id: AidboxProfile, module: proto, description: Aidbox validation profile}
- {id: AidboxQuery, module: proto, description: Aidbox custom query}
- {id: AidboxSubscription, module: proto, description: Subscribe to resources}
- {id: AllergyIntolerance, module: fhir-4.0.0, description: 'Allergy or Intolerance (generally: Risk of adverse reaction to a substance)'}
```

And see attributes of the specific resource:

```yaml
GET /Attribute?entity=Patient\
  &_result=array\
  &_elements=path,type.id,description\
  &_sort=_id
# 200

- path: [active]
  type: {id: boolean}
  description: Whether this patient's record is in active use
- path: [address]
  type: {id: Address}
  description: An address for the individual
- path: [deceased]
  description: Indicates if the individual is deceased or not
- path: [multipleBirth]
  description: Whether patient is part of a multiple birth
- path: [birthDate]
  type: {id: date}
  description: The date of birth for the individual
- path: [communication]
  description: A language which may be used to communicate with the patient about his or her health
- path: [communication, id]
  type: {id: string}
  description: Unique id for inter-element referencing
...
```
