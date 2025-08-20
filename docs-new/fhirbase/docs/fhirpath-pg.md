# Fhirpath.pg

### Why fhirpath? <a href="#why-fhirpath" id="why-fhirpath"></a>

To search though jsonb resources in database sometimes you need to access deeply nested attributes by specific criteria. For example to search patient by official name you have to do something like this:

```sql
SELECT id, nm 
FROM (
   SELECT id as id, jsonb_array_elements(resource->'name') AS nm 
   FROM patient
) _ 
WHERE nm->>'use' = 'official' and nm->>'family' ilike '%Chalmers%' 
LIMIT 10;

```

Here is the version with fhirpath:

```
SELECT id, resource->'name' 
 FROM patient
 WHERE 
  fhirpath(resource,$$name.where(use='official').family$$)->>0  ilike 'Chal%';

```

### Implement FHIR search with fhirpath <a href="#implement-fhir-search-with-fhirpath" id="implement-fhir-search-with-fhirpath"></a>

Having fhirpath in your database also is very handy to implement FHIR Search API. In FHIR SearchParameters are described by fhirpath expressions. For example for Patient we have following [search parameters](https://www.hl7.org/fhir/patient.html#search):

| **Name**             | **Type**                                                      | **Expression**                                                                                                                                       |
| -------------------- | ------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------- |
| active               | ​[token](https://www.hl7.org/fhir/search.html#token)​         | Patient.active                                                                                                                                       |
| address              | ​[string](https://www.hl7.org/fhir/search.html#string)​       | Patient.address                                                                                                                                      |
| address-city         | ​[string](https://www.hl7.org/fhir/search.html#string)​       | Patient.address.city                                                                                                                                 |
| address-country      | ​[string](https://www.hl7.org/fhir/search.html#string)​       | Patient.address.country                                                                                                                              |
| address-postalcode   | ​[string](https://www.hl7.org/fhir/search.html#string)​       | Patient.address.postalCode                                                                                                                           |
| address-state        | ​[string](https://www.hl7.org/fhir/search.html#string)​       | Patient.address.state                                                                                                                                |
| address-use          | ​[token](https://www.hl7.org/fhir/search.html#token)​         | Patient.address.use                                                                                                                                  |
| animal-breed         | ​[token](https://www.hl7.org/fhir/search.html#token)​         | Patient.animal.breed                                                                                                                                 |
| animal-species       | ​[token](https://www.hl7.org/fhir/search.html#token)​         | Patient.animal.species                                                                                                                               |
| birthdate            | ​[date](https://www.hl7.org/fhir/search.html#date)​           | Patient.birthDate                                                                                                                                    |
| death-date           | ​[date](https://www.hl7.org/fhir/search.html#date)​           | Patient.deceased.as(DateTime)                                                                                                                        |
| deceased             | ​[token](https://www.hl7.org/fhir/search.html#token)​         | Patient.deceased.exists()                                                                                                                            |
| email                | ​[token](https://www.hl7.org/fhir/search.html#token)​         | Patient.telecom.where(system='email')                                                                                                                |
| family               | ​[string](https://www.hl7.org/fhir/search.html#string)​       | Patient.name.family                                                                                                                                  |
| gender               | ​[token](https://www.hl7.org/fhir/search.html#token)​         | Patient.gender                                                                                                                                       |
| general-practitioner | ​[reference](https://www.hl7.org/fhir/search.html#reference)​ | Patient.generalPractitioner ([Practitioner](https://www.hl7.org/fhir/practitioner.html), [Organization](https://www.hl7.org/fhir/organization.html)) |
| given                | ​[string](https://www.hl7.org/fhir/search.html#string)​       | Patient.name.given                                                                                                                                   |
| identifier           | ​[token](https://www.hl7.org/fhir/search.html#token)​         | Patient.identifier                                                                                                                                   |
| language             | ​[token](https://www.hl7.org/fhir/search.html#token)​         | Patient.communication.language                                                                                                                       |
| link                 | ​[reference](https://www.hl7.org/fhir/search.html#reference)​ | Patient.link.other ([Patient](https://www.hl7.org/fhir/patient.html), [RelatedPerson](https://www.hl7.org/fhir/relatedperson.html))                  |
| name                 | ​[string](https://www.hl7.org/fhir/search.html#string)​       | Patient.name                                                                                                                                         |
| organization         | ​[reference](https://www.hl7.org/fhir/search.html#reference)​ | Patient.managingOrganization ([Organization](https://www.hl7.org/fhir/organization.html))                                                            |
| phone                | ​[token](https://www.hl7.org/fhir/search.html#token)​         | Patient.telecom.where(system='phone')                                                                                                                |
| phonetic             | ​[string](https://www.hl7.org/fhir/search.html#string)​       | Patient.name                                                                                                                                         |
| telecom              | ​[token](https://www.hl7.org/fhir/search.html#token)​         | Patient.telecom                                                                                                                                      |

With FHIR search there is one tricky moment - most of expressions return complex types like HumanName or Identifier, which is not easy to search by SQL. So fhirpath provides specialised function to output search friendly results:

```
SELECT fhirsearch_string(resource, $$Patient.name.where(user='official')$$, 'HumanName')
-- '^John$ ^Doe$ ^Johny$'
​
-- so to search for startWith we can do
SELECT * from patient
WHERE fhirsearch_string(...) ilike '%^Joh%'
```

### Invariants with fhirpath.pg <a href="#invariants-with-fhirpath-pg" id="invariants-with-fhirpath-pg"></a>

With fhirpath we can create invariants as PostgreSQL Constraints. Let's add a check that all patients have SSN number:

```sql
-- clear patient table if you have somthing invalid in
truncate patient;
​
ALTER TABLE patient 
ADD CONSTRAINT patient_ssn 
CHECK (
(
  fhirpath(
   resource, 
   $$identifier.where(system = 'ssn').value.exists()$$
  )->>0
 )::boolean
);
```

Now when you will try to add patient without SSN - you will see an ERROR:

```sql
INSERT INTO patient (resource) 
VALUES ($$
  {
    "resourceType":"Patient",
    "id":"example", 
    "identifier": [{"value": "777777"}]}
$$);
-- ERROR:  new row for relation "patient" violates check constraint "patient_ssn"
--DETAIL:  Failing row contains ({"id": "example", "identifier": [{"value": "777777"}], "resource...).
​
-- but this will work
INSERT INTO patient (resource) 
VALUES ($$
  {
   "resourceType":"Patient",
   "id":"example", 
   "identifier": [{"system": "ssn", "value": "777777"}]
   }
$$);
-- INSERT 0 1
```

