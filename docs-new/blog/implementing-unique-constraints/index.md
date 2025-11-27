---
title: "Implementing Unique Constraints in Aidbox: Ensuring SSN Uniqueness"
slug: "implementing-unique-constraints"
published: "2025-04-11"
author: "Evgeny Mukha"
reading-time: "5 min"
tags: []
category: "FHIR"
teaser: "Your storage system must ensure that the SSN identifier is unique across all patient records. This means that if you try to create a new patient with an SSN and another patient with the same SSN already exists, the system will reject the request to prevent duplicate records."
image: "cover.png"
---

The problem: Your storage system must ensure that the SSN identifier is unique across all patient records. This means that if you try to create a new patient with an SSN and another patient with the same SSN already exists, the system will reject the request to prevent duplicate records.

To solve this problem in the Aidbox FHIR Server, you can create an index that enforces this rule:

```javascript
CREATE UNIQUE INDEX patient_email_unique_idx1
ON patient ((jsonb_path_query_first(resource, '$.identifier[*] ? (@.system == "http://hl7.org/fhir/sid/us-ssn").value') #>> '{}'));
```

Explanation:

- **CREATE UNIQUE INDEX patient\_ssn\_unique\_idx1**This defines a unique index named `patient\_ssn\_unique\_idx1`. When you create a **unique index**, PostgreSQL prevents duplicate values in the specified column(s). If a duplicate value is inserted, the database will throw a **unique constraint violation** error.
- **ON patient**The index applies to the patient table.
- **(jsonb\_path\_query\_first(resource, '$.identifier[\*] ? (@.system == "http://hl7.org/fhir/sid/us-ssn").value') #>> '{}')**   
  - **resource**: Refers to the JSONB column storing patient data.
  - **jsonb\_path\_query\_first(...)**: Extracts the first identifier that meets the condition.
  - **'$.identifier[\*] ? (@.system == "http://hl7.org/fhir/sid/us-ssn").value'**:   
    - Searches for all identifier elements in the resource JSON.
    - Filters identifiers where the system is "http://hl7.org/fhir/sid/us-ssn" (indicating an SSN).
    - Extracts the value field, which contains the actual SSN.
  - **#>> '{}'**: Converts the extracted JSONB result into a plain text value for indexing.

[Test it on your local FHIR server](https://www.health-samurai.io/aidbox).
