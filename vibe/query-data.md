<!-- Merge into: database.md -->

# How to query data?

Healthcare data analysis requires flexible querying capabilities 
that can handle both clinical workflows and analytical use cases. 
Aidbox provides three primary approaches for data access: 
FHIR search for clinical operations, direct SQL for complex queries, and [SQL on FHIR](https://build.fhir.org/ig/FHIR/sql-on-fhir-v2/) for analytics workflows.

This section explains how each approach works, when to use them, and how they leverage PostgreSQL's JSONB capabilities to provide efficient data access across millions of healthcare records.

## FHIR Search Implementation

Aidbox implements [FHIR search](https://www.hl7.org/fhir/search.html) by translating 
search parameters into optimized PostgreSQL queries against JSONB data. 
Each search parameter type uses different [PostgreSQL JSONB functions](https://www.postgresql.org/docs/current/functions-json.html) 
and operators.

### Reference Search

[Reference searches](https://www.hl7.org/fhir/search.html#reference) locate resources that reference other resources:

```http
GET /Observation?subject=Patient/123
```

Translates to SQL using JSONB path queries:

```sql
SELECT * FROM observation 
WHERE jsonb_path_query_first(resource, '$.subject.reference') = 'Patient/123';
```

### Token Search  

[Token searches](https://www.hl7.org/fhir/search.html#token) use exact matching for coded values like identifiers:

```http
GET /Patient?identifier=ssn|123-45-6789
```

Uses PostgreSQL's [JSONB containment operator](https://www.postgresql.org/docs/current/datatype-json.html#JSON-CONTAINMENT):

```sql
SELECT * FROM patient
WHERE resource @> '{"identifier": [{"system": "ssn", "value": "123-45-6789"}]}';
```

### String Search

[String searches](https://www.hl7.org/fhir/search.html#string) handle text matching with normalization and case-insensitive matching:

```http
GET /Patient?name=Smith
```

Extracts text values using JSONPath and applies text search functions:

```sql
SELECT * FROM patient
WHERE jsonb_path_query_array(
    resource,
    '($.name[*]).** ? (@.type() == "string")'
)::text ILIKE '%Smith%';
```

See also: [FHIR Search](../api/rest-api/fhir-search/README.md)

## Direct SQL Access

PostgreSQL provides powerful capabilities for querying JSONB data directly. 
Aidbox exposes these through multiple interfaces while maintaining data consistency.

### SQL Endpoint

The [`$sql` endpoint](../api/rest-api/other/sql-endpoints.md) allows direct SQL execution via REST API:

```yaml
POST /$sql?_format=yaml

SELECT 
  id,
  resource->>'gender' as gender,
  resource->'name'->0->>'family' as last_name
FROM patient
WHERE resource->'active' = 'true'
LIMIT 10
```

For parameterized queries use JDBC-style arrays:

```yaml
POST /$sql

["SELECT count(*) FROM patient WHERE resource->>'gender' = ?", "female"]
```

### JSONB Query Patterns

PostgreSQL provides rich operators for JSONB queries:

```sql
-- Existence check
SELECT * FROM patient WHERE resource ? 'birthDate';

-- Path-based extraction  
SELECT id, resource#>>'{name,0,given,0}' as first_name FROM patient;

-- Array containment
SELECT * FROM patient 
WHERE resource->'identifier' @> '[{"system": "http://example.com/ids"}]';

-- Text search across nested fields
SELECT * FROM patient
WHERE resource::text ILIKE '%Boston%';
```

Aidbox provides additional SQL functions for working with FHIR resources.

See also: 
- [Aidbox SQL Functions](../reference/aidbox-sql-functions.md)
- [SQL Endpoints](../api/rest-api/other/sql-endpoints.md)

## SQL on FHIR

[SQL on FHIR](https://build.fhir.org/ig/FHIR/sql-on-fhir-v2/) transforms nested FHIR resources into flat, 
tabular views optimized for analytics. 
Aidbox implements this specification using ViewDefinition resources that create PostgreSQL views.

### ViewDefinition Basics

ViewDefinitions describe how to flatten FHIR resources using [FHIRPath expressions](https://www.hl7.org/fhir/fhirpath.html):

```json
{
  "resourceType": "ViewDefinition", 
  "name": "patient_demographics",
  "resource": "Patient",
  "status": "active",
  "select": [
    {
      "column": [
        {
          "name": "id",
          "path": "getResourceKey()"
        },
        {
          "name": "gender", 
          "path": "gender"
        },
        {
          "name": "birth_date",
          "path": "birthDate"
        }
      ]
    },
    {
      "forEach": "name.where(use = 'official').first()",
      "column": [
        {
          "name": "family_name",
          "path": "family"
        },
        {
          "name": "given_names", 
          "path": "given.join(' ')"
        }
      ]
    }
  ]
}
```

This creates a view in the `sof` schema that can be queried with standard SQL:

```sql
SELECT family_name, gender, birth_date 
FROM sof.patient_demographics
WHERE gender = 'female' 
  AND birth_date > '1990-01-01';
```

### Multi-Resource Analytics

SQL on FHIR enables complex joins across resource types:

```sql
-- COVID-19 patient cohort analysis
SELECT 
  p.family_name,
  p.birth_date,
  c.onset_date,
  c.code_display
FROM sof.patient_demographics p
JOIN sof.condition_summary c ON c.patient_id = p.id
WHERE c.code = '840539006'  -- COVID-19 SNOMED code
  AND c.onset_date > '2021-01-01'
ORDER BY c.onset_date DESC;
```

### Business Intelligence Integration

SQL on FHIR views integrate directly with BI tools:

```sql
-- Dashboard metrics for healthcare operations
SELECT 
  DATE_TRUNC('month', enc.start_date) as month,
  loc.name as location,
  COUNT(*) as encounter_count,
  COUNT(DISTINCT enc.patient_id) as unique_patients
FROM sof.encounter_summary enc
JOIN sof.location_view loc ON enc.location_id = loc.id
WHERE enc.start_date >= '2023-01-01'
GROUP BY month, loc.name
ORDER BY month, encounter_count DESC;
```

See also: [SQL on FHIR](../modules/sql-on-fhir/README.md)

