---
title: "How to create a FHIR Implementation Guide"
slug: "how-to-create-a-fhir-implementation-guide"
published: "2025-06-18"
author: "Gennady Razmakhnin"
reading-time: "7 minutes"
tags: []
category: "FHIR"
teaser: "Learn how to create a FHIR Implementation Guide—your organization’s rulebook for turning flexible FHIR standards into actionable, interoperable healthcare data. From profiles and extensions to terminology, search, and packaging, this guide walks you through the essential steps to ensure consist"
image: "cover.png"
---

# The interoperability problem

Imagine you are a doctor seeing a new patient who has visited multiple hospitals, undergone various tests, and is currently taking several medications. But right now, all that critical information is scattered across different systems that don’t communicate with one another effectively. This is healthcare's interoperability problem.

Healthcare IT systems are deeply fragmented. Each hospital, clinic, lab, and insurance company stores patient data differently—using custom databases, proprietary formats, incompatible APIs. When patients move between providers, their medical history doesn’t follow them seamlessly. Your lab results are in one system, prescriptions in another, and imaging in a third. Doctors waste time hunting for information or, worse, making decisions with incomplete data.

Every new connection between systems requires custom development. The patient record from Hospital 'A' looks nothing like that from Clinic 'B'. APIs are inconsistent. Data formats vary wildly, and critical information gets lost in translation. Allergies, medications, and previous procedures—all scattered across incompatible systems that can't exchange coherent messages.

Healthcare organizations become trapped by proprietary systems. Switching vendors means rebuilding all integrations from scratch—a costly, risky undertaking that few can afford.

# FHIR: The Universal Healthcare Language

FHIR (Fast Healthcare Interoperability Resources) is revolutionizing how healthcare systems communicate. Think of it as a universal vocabulary that every healthcare system can understand. Instead of building custom bridges between every pair of systems, each organization connects to a FHIR server using the same standardized language.

Patient, Observation, Medication, Condition—all healthcare data across all clinics use the same resource structures. A Patient record from Hospital "A" looks identical to one from Clinic "B". No more custom parsers or data transformation layers.

Under the hood, FHIR is a RESTful API, so every FHIR server supports the same operations:

- `` `GET /Patient/001` `` – retrieve patient data

- `` `GET /Observation?patient=001&code=blood-pressure` `` – search observations

- `` `POST /Patient` `` – create new patient record

Yet FHIR's flexibility creates new problems. The base Patient resource has 50+ optional fields. Which ones should your pediatric clinic require? Your cardiac center? Your insurance system?

Different teams interpret the FHIR spec their own way: one stores patient weight in kilograms, another in pounds. One lab codes tests with LOINC, another invents local codes. Search parameters vary between implementations.

Your hospital likely needs specific workflows that core FHIR doesn't address. How do you enforce your data quality rules? What custom search operations do your clinical team and integration systems need?

Vendors often cherry-pick subsets of FHIR: System "A" supports Patient search by name; System "B" doesn't. Your integration breaks when you switch vendors, even though both claim "FHIR compliance". Without constraints, "FHIR-compliant" systems still fail to share meaningful data. You get structural interoperability (same JSON format) but lose semantic interoperability (shared meaning).

That’s why the next step—a tailored Implementation Guide (IG)—locks everyone into the same playbook and turns FHIR’s promise into day‑to‑day reality.

# Implementation Guide: Your FHIR Rulebook

A FHIR Implementation Guide turns the FHIR spec into an unambiguous contract tailored to your organization.

Instead of 50+ optional Patient fields, your IG specifies exactly which ones are mandatory. Pediatric clinics require birth date and guardian contact, while cardiac centers mandate weight and blood-pressure history.

Your IG defines that weight must be recorded in kilograms, dates follow ISO format, and blood-pressure observations use specific LOINC codes. No room for guessing or inconsistent interpretations.

Through `ValueSets` and `CodeSystems`, your IG restricts blood type to valid values (A+, B-, O+), lab codes to approved LOINC terms, and diagnosis codes to your organization's ICD-10 subset.

Extensions let you capture "citizenship", “race”, or "insurance tier level" without breaking FHIR compatibility. Data stays structured and searchable while meeting unique business needs.

Your IG lists the search parameters every server must support. Because each vendor implements the same query set, you can swap systems with confidence.

FHIR servers can validate incoming data against your Implementation Guide (IG) profiles, rejecting incomplete or malformed records before they enter your system.

The result: "FHIR-compliant according to our IG" means systems truly work together, not just merely share the same JSON structure.

While FHIR provides the foundation, Implementation Guides let you define exactly how your organization uses FHIR. Think of FHIR as English, and your IG as your company's style guide—same language, but with specific rules for your context.

With multiple services, teams, vendors working with one FHIR server, you need them to speak one dialect: what fields are required, what values are allowed, what APIs must be supported. This means a hospital app and an insurance system can both work with the same Patient data without coordination. Searches like GET /Observation?patient=123&code=loinc|1234-5 behave the same across all compliant servers, making integration predictable.

# Terminology: Speaking the Same Language

A ValueSet and a CodeSystem are FHIR resources that represent your controlled vocabularies. They define which codes are allowed in coded fields and ensure consistent terminology across your entire system.

Why does this matter? A "blood type" field needs specific values like A+, B-, O+, not random text like "red blood" or "type awesome". Different healthcare domains use different coding systems—LOINC for lab results, SNOMED CT for clinical terms, ICD-10-CM for diagnoses.

The `CodeSystem` resource defines the actual codes and their meanings, like creating your domain-specific dictionary. Display value can be translated to multiple languages, preserving the intended meaning regardless of language used.

In turn, the `ValueSet` resource selects which codes from one or more `CodeSystems` are allowed in specific contexts—imagine a “Blood Type” `ValueSet` that plucks only eight codes (`A+`, `A‑`, `B+`, `B‑`, `AB+`, `AB‑`, `O+`, `O‑`) from the much larger SNOMED catalog. Different `ValueSets` that extract their specific context-related codes can be based on the same `CodeSystem`, but serve different workflows.

Benefits include enforcing valid codes only in coded fields, preventing data entry errors through validation, and enabling semantic interoperability where systems understand meaning, not just structure. Instead of accepting any string value in coded fields, your system validates against predefined vocabularies, ensuring clinical data uses standardized terminology that other systems can interpret correctly. This controlled approach transforms free-text chaos into structured, meaningful data that supports clinical decision-making, regulatory compliance, and data analytics/machine learning.

Let's explore a practical example: time zone management in eHealth applications. When your application serves patients across different regions, accurate appointment scheduling requires time zone awareness. FHIR's CodeSystem resource provides a structured way to define and manage these time zone values.

```javascript
{
  "url": "http://my-organization.io/pacakage-1/CodeSystem/timezone-codes",
  "status": "active",
  "content": "complete",
  "resourceType": "CodeSystem",
  "concept": [
    { "code": "America/Los_Angeles", "display": "Pacific Time (US & Canada)" },
    { "code": "Europe/Andorra", "display": "Central European Time (Andorra)" },
    { "code": "Australia/Melbourne", "display": "Australian Eastern Time (Melbourne)" }
  ]
}
```

To enforce validation rules using our `CodeSystem`, we create a `ValueSet` that references it—profiles always bind to ValueSets rather than directly to `CodeSystems`. The canonical URL serves as a unique identifier allowing our profile to reference this `ValueSet`, while the compose attribute acts as a domain-specific language defining rules for including codes from `CodeSystems`. In our simple case, we're including all codes from our timezone `CodeSystem`.

```javascript
{
  "url": "http://my-organization.io/pacakage-1/ValueSet/timezone-codes",
  "status": "active",
  "resourceType": "ValueSet",
  "compose": {
    "include": [{ "system": "http://my-organization.io/pacakage-1/CodeSystem/timezone-codes" }]
  }
}
```

Result: the server now rejects any time‑zone code outside this list, keeping schedules accurate across the globe.

# Extensions: Adding Your Custom Fields

Extensions = FHIR resources + **your custom fields for existing resources**. They let you add data that doesn't exist in standard FHIR resources without forking the standard.

Why bother? The core Patient resource lacks "citizenship", “race”, or "insurance tier level", but our business and medical requirements depend on this information. Remember, FHIR is a backbone —comprehensive, but not an exhaustive medical ontology.

**Key benefits**

- Add domain‑specific fields and stay 100 % FHIR‑compliant.
- Maintain interoperability while capturing your unique requirements.
- Store custom data in a structured, searchable format—never buried in free text.

Instead of modifying core FHIR resources or using unstructured text fields, your system captures precise additional data in a standardized, searchable format. This structured approach ensures custom data remains queryable and exchangeable while preserving FHIR's interoperability promise.

### ValueSet is bound to the extension

With our `ValueSet` defined, we can now bind it to our timezone extension. The binding reference points to our ValueSet's canonical URL, while the binding strength is set to "required" to ensure strict validation. This means the FHIR server will reject any resources using timezone codes not in our `ValueSet`.

```javascript
{
  "url": "http://my-organization.io/package-1/time-zone",
  "baseDefinition": "http://hl7.org/fhir/StructureDefinition/Extension",
  "abstract": false,
  "name": "TimeZone",
  "status": "active",
  "kind": "complex-type",
  "type": "Extension",
  "derivation": "constraint",
  "resourceType": "StructureDefinition",
  "context": [{ "expression": "Patient", "type": "element" }],
  "differential": {
    "element": [
      {
        "path": "Extension.url",
        "id": "Extension.url",
        "fixedUri": "http://my-organization.io/package-1/time-zone"
      },
      {
        "id": "Extension.value[x]",
        "path": "Extension.value[x]",
        "type": [{ "code": "code" }],
        "min": 1,
        "binding": {
          "strength": "required",
          "valueSet": "http://my-organization.io/pacakage-1/ValueSet/timezone-codes"
        }
      }
    ]
  }
}
```

# Profiles: Tailoring FHIR to Your Needs

Profiles = FHIR resources + **your restrictions on them**. They make abstract resources work for your specific needs. FHIR provides a set of resources with a specific schema, and most of the resource's fields are optional by default.

Why create a profile? A cardiac clinic needs patient data that differs from a pediatric clinic. Each medical specialty has unique clinical guidelines and reporting requirements that shape what data must be collected and how. Legal requirements also vary by country and specialty.

Instead of accepting any Patient resource that meets the base FHIR specification, a profile directs your system to accept only records that satisfy your specific clinical or business requirements and ensures your data pipeline collects information that improves data quality and completeness. This targeted approach reduces integration errors and ensures data consistency across your healthcare ecosystem.

Let's examine a Patient profile that enforces data quality and time zone awareness in our healthcare application. This profile demonstrates how to combine FHIR's core capabilities with custom extensions to meet specific business needs.

The profile requires three key elements:

1. Patient name (required for proper identification)

2. Gender (required for accurate clinical context)

3. Our Timezone extension (required for appointment scheduling)

By making these fields mandatory and incorporating our timezone extension, we ensure consistent data collection across our system.

```javascript
{
  "url": "http://my-organization.io/package-1/my-patient",
  "baseDefinition": "http://hl7.org/fhir/StructureDefinition/Patient",
  "name": "MyPatient",
  "status": "active",
  "kind": "resource",
  "type": "Patient",
  "abstract": false,
  "derivation": "constraint",
  "resourceType": "StructureDefinition",
  "differential": {
    "element": [
      {
        "id": "Patient.name",
        "path": "Patient.name",
        "min": 1
      },
      {
        "id": "Patient.gender",
        "path": "Patient.gender",
        "min": 1
      },
      {
        "id": "Patient.extension",
        "path": "Patient.extension",
        "min": 1,
        "slicing": {
          "rules": "open",
          "discriminator": [{ "path": "url", "type": "value" }]
        }
      },
      {
        "min": 1,
        "max": "1",
        "sliceName": "TimeZone",
        "path": "Patient.extension",
        "id": "Patient.extension:TimeZone",
        "type": [
          {
            "profile": [ "http://my-organization.io/package-1/time-zone" ],
            "code": "Extension"
          }
        ]
      }
    ]
  }
}
```

Note: No matter how specialized your profiles become—whether you're inheriting from multiple profiles or adding strict validation rules—any resource that passes your profile validation remains a valid FHIR resource at its core. Other FHIR systems can still work with your resources, even if they don't understand your specific profile rules. That's what makes FHIR truly powerful—you get both standardization and flexibility.

**Valid minimal patient example by our final version of the profile:**

```javascript
{
  "meta": { "profile": ["http://my-organization.io/package-1/my-patient"] },
  "extension": [
    {
      "url": "http://my-organization.io/package-1/time-zone",
      "valueCode": "America/Los_Angeles"
    }
  ],
  "name": [{ "given": ["John"], "family": "Doe"}],
  "gender": "male",
  "resourceType": "Patient"
}
```

# SearchParameter: Finding What You Need

SearchParameter = FHIR resources + **your custom search capabilities**. They let you invent new ways to search and filter resources beyond standard FHIR search parameters.

Why extend the search? Standard FHIR search parameters like "name" or "birthdate" are helpful, but real-world workflows demand richer queries—find patients by custom fields (extensions), locate all lab results above a threshold,or search across multiple resources in one query.

Different teams need different ways to find data: clinical by symptoms and diagnoses, billing by insurance, and procedure codes. Instead of building complex client-side query logic or maintaining custom search endpoints, your `SearchParameter` definitions make advanced queries available through standard FHIR APIs. This standardized approach ensures all systems can perform the same searches consistently.

Example: Let’s reuse our time zone extension to identify patients within specific time zones. If a Patient resource includes an extension specifying their time zone, a SearchParameter can use FHIRPath to filter Patients based on this extension's value. This allows for searches like `` `GET /Patient?timezone=America/Los_Angeles` `` to retrieve patients in the Pacific Time Zone.

```javascript
{
  "url": "http://my-organization.io/package-1/SearchParameter/Patient-timeZone",
  "id": "Patient-timeZone",
  "base": ["Patient"],
  "description": "Search patient by their time zone",
  "expression": "Patient.extension.where(url='http://my-organization.io/package-1/time-zone').value",
  "name": "timezone",
  "status": "active",
  "type": "token",
  "code": "timezone",
  "resourceType": "SearchParameter"
}
```

### **How FHIRPath works (quick tour)**

- Dot navigation – `Patient.address.city` walks the JSON tree like object notation.
- `where() filter` – `extension.where(url='…')` keeps only matching elements.
- `ofType()` cast – `value.ofType(Quantity)` narrows polymorphic fields (holds more than one datatype).
- Chaining – You can string these together to reach any nested value.

Because every server understands FHIRPath, the *same* expression delivers identical results across vendors.

Your IG can require support with specific search parameters. This ensures all implementations provide the search capabilities your organization needs.

# Operations: Domain business logic

Operations = Standardized FHIR API + **your custom domain-specific endpoints**.

They let you define complex API methods beyond simple CRUD operations on resources.

Why define operations? Healthcare workflows sometimes need to calculate drug interactions, generate clinical reports, or process complex eligibility checks. Standard FHIR REST operations (`GET`, `POST`, `PUT`, `DELETE`) work great for basic resource management, but they don’t execute sophisticated business logic.

`OperationDefinition` describes your custom operation's interface—input parameters, output format, and behavior documentation.

**Example: Custom Operation Patient/$schedule-teleconsultation-window**

In distributed care environments, especially with telemedicine, scheduling must account for both patient and provider time zones. A custom FHIR operation can automate the coordination of time windows suitable for virtual consultations.

This operation supports optional input parameters to customize scheduling logic:

- **practitioner** (Reference)  
  *Who the patient wants to schedule with.*A reference to a Practitioner or PractitionerRole resource. Their availability is used to compute possible slots.
- **daysAhead** (integer, default: 7)  
  *How far into the future to look.*Limits the search to the upcoming N days. Useful to avoid unnecessary computation.
- **slotDuration** (Duration, default: 30 minutes)  
  *Minimum appointment length.*Filters out shorter slots — for example, a consultation must be at least 30 minutes long.

These parameters help the client (e.g., a patient app or care coordination tool) tailor the result to real-world constraints like provider schedules, patient needs, or system policies.

```javascript
{
  "resourceType": "OperationDefinition",
  "id": "schedule-teleconsultation-window",
  "url": "http://my-organization.io/package-1/OperationDefinition/schedule-teleconsultation-window",
  "name": "ScheduleTeleconsultationWindow",
  "status": "active",
  "kind": "operation",
  "code": "schedule-teleconsultation-window",
  "resource": ["Patient"],
  "system": false,
  "type": true,
  "instance": true,
  "parameter": [
    {
      "name": "practitioner",
      "use": "in",
      "type": "Reference",
      "min": 1,
      "max": "1",
      "documentation": "Practitioner to check availability for"
    },
    {
      "name": "daysAhead",
      "use": "in",
      "min": 0,
      "max": "0",
      "type": "integer",
      "documentation": "Number of days into the future to check"
    },
    {
      "name": "slotDuration",
      "use": "in",
      "min": 0,
      "max": "1",
      "type": "Duration",
      "documentation": "Minimum desired duration for appointment slot"
    },
    {
      "name": "availableSlot",
      "use": "out",
      "min": 1,
      "max": "100",
      "type": "BackboneElement",
      "part": [
        { "name": "start", "use": "out", "type": "dateTime", "min": 1, "max": "1" },
        { "name": "end",   "use": "out", "type": "dateTime", "min": 1, "max": "1" }
      ]
    }
  ]
}
```

Note: Defining an `OperationDefinition` in your IG documents the operation's interface and requirements, but doesn't automatically implement the functionality—your FHIR server must provide the actual implementation.

Supporting every operation from all existing implementation guides would be impractical and resource-intensive. Instead, FHIR servers typically implement commonly used operations from widely adopted public IGs and provide the ability to extend the FHIR API with your operations. For example, Aidbox offers a flexible API Gateway pattern through its "App" functionality, enabling you to implement custom operations via dedicated lambda functions/microservices that integrate seamlessly with the FHIR server.

# Distribution and Packaging

FHIR Implementation Guides can be distributed as NPM packages, making them easy to version, share, and integrate into different systems. This approach leverages the familiar NPM ecosystem that developers already use for JavaScript/TypeScript dependencies.

Our minimal package.json for a FHIR IG might look like:

```javascript
{
    "name": "my.organization.fhir",
    "version": "0.0.1",
    "description": "My Organization IG FHIR Package",
    "author": "My Organization",
    "dependencies": {
        "hl7.fhir.r4.core": "4.0.1"
    }
}
```

This package structure supports semantic versioning of your IG, dependency management for base FHIR specs and other IGs, easy distribution through NPM registries, clear identification of FHIR version compatibility, and automated tooling integration.

To package the Implementation Guide, navigate to your IG directory and run: `npm pack`

This will create a .tgz file like: `my-organization-fhir-ig-package-1.0.0.tgz`. Alternatively, you can make a tarball archive manually.

The Implementation Guide example from the article is available at: <https://storage.googleapis.com/my-implementation-guide/my.organization.fhir-0.0.1.tgz>

Create your aidbox instance and try out the implementation guide at <http://aidbox.app>.

To load the IG, follow [the documentation](https://docs.aidbox.app/tutorials/validation-tutorials/upload-fhir-implementation-guide/aidbox-ui/local-ig-package).

# Real-World IG Examples

FHIR IGs aren't merely theoretical—they’re foundational to real-world interoperability efforts. Here are a few widely adopted guides:

- [**US Core**](https://build.fhir.org/ig/HL7/US-Core): Defines the minimum data set and constraints for clinical data exchange in the United States. Many national APIs and EHRs are built on it.
- [**International Patient Summary (IPS)**](https://build.fhir.org/ig/HL7/fhir-ips): Enables cross-border patient summary exchange. Used in the EU and internationally.
- [**mCODE (Minimal Common Oncology Data Elements)**](https://build.fhir.org/ig/HL7/fhir-mCODE-ig): Tailors FHIR to oncology, enabling structured, analyzable cancer data for clinical trials, care, and research.

These examples show how IGs address specific domains and use cases, setting a standard for data interoperability and quality. By examining them, you can align your own IGs with community best practices.
