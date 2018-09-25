# Value Set Expansion

Aidbox use de-normalized  approach to ValueSets. That means we pre-calculate valuesets in design time and store valueset id's into _Concept.valuset_ element \(see /Concept article\). That's why ValueSet expansion in aidbox is just a special case of Concept Search:

```http
GET [base]/fhir/ValueSet/23/$expand?filter=abdo
```

means

```http
GET [base]/Concept?valueset=23&filter=abdo
```

### Api

Official documentation [FHIR Terminology ValueSet Expansion](https://www.hl7.org/fhir/valueset-operations.html#expand)

```text
GET/POST URL: [base]/ValueSet/$expand
```

```text
GET/POST URL: [base]/ValueSet/[id]/$expand
```

Example for expand default AdministrativeGender ValueSet. [List of default ValueSets.](https://www.hl7.org/fhir/terminologies-valuesets.html)

```text
GET [base]/ValueSet/administrative-gender/$expand
```

Parameters

| Parameter | Type | Status | Example |
| :--- | :--- | :--- | :--- |
| url | [uri](https://www.hl7.org/fhir/datatypes.html#uri) | `supported` | [url](value-set-expansion.md#url) |
| valueSet | [ValueSet](https://www.hl7.org/fhir/valueset.html) | `supported` | [valueSet](value-set-expansion.md#valueset) |
| context | [uri](https://www.hl7.org/fhir/datatypes.html#uri) | `not supported` |  |
| filter | [string](https://www.hl7.org/fhir/datatypes.html#string) | `supported` | [filter](value-set-expansion.md#filter) |
| profile |  |  |  |
| date |  |  |  |
| offset |  | + |  |
| count |  | + |  |
| includeDesignations |  |  |  |
| includeDefinition |  |  |  |
| activeOnly |  | + |  |
| excludeNested |  |  |  |
| excludeNotForUI |  |  |  |
| excludePostCoordinated |  |  |  |
| displayLanguage |  |  |  |
| limitedExpansion |  |  |  |

#### url

```text
GET [base]/ValueSet/$expand?url=http://hl7.org/fhir/ValueSet/administrative-gender
```

```text
POST [base]/ValueSet/$expand

{ "resourceType" : "Parameters",
  "parameter" : [
     {"name" : "url",
      "valueUri" : "http://hl7.org/fhir/ValueSet/administrative-gender"
     }
   }
 ]
}
```

#### valueSet

```text

```

#### filter



