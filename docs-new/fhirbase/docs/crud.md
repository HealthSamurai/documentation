# CRUD

Fhirbase provides functions to Create, Read, Delete and Update resources. There are two variants of these functions: first accepts `txid` and second does not.&#x20;

### fhirbase\_create (resource jsonb, \[txid bigint]) <a href="#fhirbase_create" id="fhirbase_create"></a>

This function creates or recreates a FHIR resource. When a resource with the same ID exist, it will be moved into the history table and the new resource will have `recreated` status. If resource argument doesn't contain an `id` attribute, the value returned by `fhirbase_genid` function will be used as an ID.

```sql
SELECT fhirbase_create('{"resourceType": "Patient", "id": "pt1"}'::jsonb);

> {
>  "resourceType": "Patient",
>  "id": "pt1", 
>  "meta": {"versionId": "90", "lastUpdated": "2018-08-11T19:33:37.845344+00:00"}
> }
​
-- without an ID
SELECT fhirbase_create(
 '{"resourceType": "Patient", "name": [{"family": "Smith"}]}'::jsonb
);
​
-- with txid
SELECT fhirbase_create('{"resourceType": "Patient", "id": "pt2"}'::jsonb, 100);
```

