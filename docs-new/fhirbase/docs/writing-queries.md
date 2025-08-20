---
description: Beginner tutorial on writing JSONB queries.
---

# Writing Queries

{% embed url="https://www.youtube.com/watch?v=zgU5c3RwjD4" %}
Video version of this tutorial
{% endembed %}

Using SQL with FHIR data is a hot topic which gained a lot of attention in the FHIR community. Amount of interest is not surprising — SQL is a lingua franca for developers, data analytics, and users of BI tools. Leveraging it provides a flexible way to transform and measure FHIR data.

This tutorial explains basic techniques to query FHIR data in PostgreSQL database using Fhirbase data model. It's assumed that a reader already installed PostgreSQL and Fhirbase, and loaded sample FHIR bundle as described in the [Getting Started Tutorial](https://fhirbase.aidbox.app/getting-started). Alternatively, you can run all the examples in this tutorial in the [Fhirbase Online Demo](https://fbdemo.aidbox.app/).

### World without JSONB

In the beginning, let's imagine how we would store FHIR resources in a relational database without JSON support. Usually, one would create a VARCHAR column in the table and put JSON object in it. It works fine while JSON is being parsed on the application side and there is no need to access it in the database itself. In this approach, from database's perspective, FHIR resources are structureless blobs. To perform search, you'll need to denormalize values from JSON into separate columns. For example, to find patients by name you'll have a given\_name and family\_name columns. Such denormalization can easily get out of sync. For instance, you can update denormalized columns with UPDATE statement and forget to apply same change for JSON part. And obviously, you'll have no way to update JSON at database side. The only way to update it is to SELECT all the data to the application, perform parse-transform-serialize job and UPDATE changes back into the database. Needless to say, such approach will be orders of magnitude slower than database-side UPDATE.

### JSON and JSONB

Luckily, PostgreSQL has built-in support for JSON format since version 9.4. Even more, it comes in two variations: JSON and JSONB. Let's find out what's the difference between them. Both JSON and JSONB are data types, like timestamp or UUID, for example. The difference between JSON and JSONB is how data is being stored internally. JSON stores its content in the serialized form, actually same way as in our example above with VARCHAR column. Unlike VARCHAR column, it provides you an access to discrete values in the JSON document and will not allow storing invalid JSON documents (it will fail on parsing step). The downside here is that JSON document will be parsed every time you access values stored inside. Obviously, it's quite inefficient.

JSONB works differently: it stores JSON document in binary form (this is where the JSONB name came from — JSON Binary). In binary form, discrete values can be accessed much faster than in textual form — there is no need to parse a JSON document again and again. A document is being parsed only once — when PostgreSQL coerces VARCHAR value into JSONB. As a downside, you cannot preserve the formatting of original JSON document when you save it in JSONB: binary form discards whitespaces but in most cases it's not a big deal.

Another huge difference is that JSONB values can be indexed. We will demonstrate indexing capabilities later in this video.

Now, when you know about these two folks — JSON and JSONB — forget the first one. When we work with Fhirbase and FHIR resources, we need only JSONB. Don't be confused with similar names.

### Fhirbase Schema

Before we proceed further, let's take a look inside Fhirbase database to see what's inwards. Open `psql` and type `\dt`. You will see a list of all tables inside Fhirbase database. There are two tables per FHIR resource: a table which contains current version of a resource, and history table containing all previous resource versions. Tables are named with lowercase resource type names. In the example for Patient resource, there will be `patient` and `patient_history` tables.

As mentioned in [Fhirbase documentation](https://aidbox.gitbook.io/fhirbase/schema), the schema is regular which means all tables have a similar structure:

```sql
CREATE TABLE "patient" (
  id text PRIMARY KEY,               // id of resource
  txid bigint not null,              // version id and logical transaction id
  ts timestamptz DEFAULT NOW(),      // last updated time
  resource_type text,                // resource type
  status resource_status not null,   // resource status
  resource jsonb not null            // resource body
);
```

Most interesting columns for us are `id` and `resource`. Other columns contain meta-information which is rarely used.

### Operators

To operate with JSONB data, PostgreSQL provides a set of operators and functions. We're not going to cover all of them — only the most commonly used ones. Please proceed to the [PostgreSQL documentation](https://www.postgresql.org/docs/10/static/functions-json.html) to see the complete list.

### Access Operators

There are several operators to access and change JSONB data. Let's start with simple arrow operator which accesses the value by key:

```sql
SELECT resource->'name'
FROM patient LIMIT 5;
```

Arrow operator itself returns JSONB value so you can chain them to get deeply nested values:

```sql
SELECT resource->'name'->0->'given'->0
FROM patient LIMIT 5;
```

But if you would try to use this operator in a WHERE clause, for example to find patient with name "Aaron", you'll get an error:

```sql
SELECT resource->'name'->0->'given'->0
FROM patient
WHERE resource->'name'->0->'given'->0 = 'Aaron697'
LIMIT 5;
```

```
ERROR:  invalid input syntax for type json
```

It happens because PostgreSQL has strict type system and it cannot compare 'Aaron697' (type VARCHAR) and JSONB value, even it's only one string inside JSONB. To make this query to work, we need to apply double-arrow operator (`->>`) which returns VARCHAR instead JSONB:

```sql
SELECT resource->'name'->0->'given'->>0
FROM patient
WHERE resource->'name'->0->'given'->>0 = 'Aaron697'
LIMIT 5;
```

Now, query result column has VARCHAR type instead of JSONB as well.

There are two similar operators which accept paths instead of single keys. In such manner you can get deeply nested value in one hop:

```sql
SELECT resource#>'{name,0,given,0}'
FROM patient
WHERE resource#>>'{name,0,given,0}' = 'Aaron697'
LIMIT 5;
```

### Predicate Operators

Predicate operators check if a specific value or key is present in JSON value. Checks can be performed at any depth. The most important difference between predicate operators and field access operators (`->`, `#>`, `->>`, `#>>`) is that all predicate operators can be optimized using just one GIN index.

Most commonly used predicate operator is `@>` which returns TRUE if right-side JSONB value is contained in left-side value:

```sql
SELECT '["FHIR", "HL7", "FHIRBASE"]'::jsonb @> '["HL7", "FHIR"]'::jsonb;
```

```sql
SELECT '{"name": [{"given": "Aharon"}, {"given": "Aaron"}]}'::jsonb @> '{"name": [{"given": "Aaron"}]}'::jsonb;
```

For example, we can use containment operator to find all Observations that indicates that patient is an active smoker:

```sql
SELECT * FROM observation
WHERE (resource @> '{"code": {"coding": [{"code": "72166-2"}]}}'::jsonb) -- LOINC: Smoking status
  AND (resource @> '{"value": {"CodeableConcept": { "coding": [{"code": "449868002"}]}}}'); -- SNOMED: Current every day smoker
```

It's a good idea to turn on query execution timer with `\timing` command. As we see, this query takes about 100ms to execute. It's pretty fast but the bad news is that query time will increase linearly when you have got more records in the table. This happens because PostgreSQL performs a sequential scan on this query and actually checks every Observation for specified criteria.

GIN index can help us to optimize this query. Create a GIN index on the `observation.resource` column...

```sql
CREATE INDEX observation_idx ON observation USING GIN (resource);
```

...and run the same query again. Whoop! 12ms, about 10 times faster! And cool thing here is that same index can be used for containment checks on any attributes:

```sql
-- select all observations for specific patient
SELECT * FROM observation
WHERE resource @> '{"subject": {"id": "a0147591-83e4-49f9-8624-96373b37e1cf", "type": "Patient"}}';
```

### Modifying Operators

Merge operator `||` is useful to update fields in JSONB object, or concatenate arrays:

```sql
SELECT '[1, 2]'::jsonb || '[2, 3, 4]'::jsonb;
```

```sql
SELECT '{"foo": 42, "bar": true }'::jsonb || '{"bar": false}'::jsonb;
```

For example, to set all observations' status to "preliminary", issue the following statement:

```sql
UPDATE observation
SET resource = resource || '{"status": "preliminary"}'::jsonb;
```

The minus operator `-` deletes a key or array element from a JSONB value:

```sql
SELECT '[1, 2]'::jsonb - 0; '-' removes first element (N/B: index starts from 0)
```

```sql
SELECT '{"foo": 42, "bar": true }'::jsonb - 'bar'; '-' removes 'bar' key
```

### Business Rule Query

Now, when we know how to use basic JSONB operators let's write an example query to get some useful data. Let's imagine we're developing an EHR system which needs to display a list of high-risk patients on some screen. High-risk criteria are following:

1. male
2. age > 45 years
3. former or active smoker

We already wrote a query to fetch active smoker's observations. Let's modify it to include past smokers as well:

```sql
SELECT * FROM observation
WHERE (resource @> '{"code": {"coding": [{"code": "72166-2"}]}}'::jsonb) -- LOINC: Smoking status
  AND ((resource @> '{"value": {"CodeableConcept": { "coding": [{"code": "449868002"}]}}}') -- SNOMED: Current every day smoker
       OR (resource @> '{"value": {"CodeableConcept": { "coding": [{"code": "8517006"}]}}}')); -- SNOMED: Former smoker
```

After we implemented the 3rd criterion, let's JOIN this query with a patient table to implement first and second ones:

```sql
SELECT DISTINCT(p.id), p.resource#>'{name,0}'
FROM patient p
JOIN observation o ON o.resource#>>'{subject,id}' = p.id
WHERE (o.resource @> '{"code": {"coding": [{"code": "72166-2"}]}}'::jsonb) -- LOINC: Smoking status
  AND ((o.resource @> '{"value": {"CodeableConcept": { "coding": [{"code": "449868002"}]}}}') -- SNOMED: Current every day smoker
       OR (o.resource @> '{"value": {"CodeableConcept": { "coding": [{"code": "8517006"}]}}}')); -- SNOMED: Former smoker
```

We added aliases `o` and `p` to distinguish `observation.resource` and `patient.resource` columns. Now let's add the first two criteria:

```sql
SELECT DISTINCT(p.id), p.resource#>'{name,0}'
FROM patient p
JOIN observation o ON o.resource#>>'{subject,id}' = p.id
WHERE (o.resource @> '{"code": {"coding": [{"code": "72166-2"}]}}'::jsonb) -- LOINC: Smoking status
  AND ((o.resource @> '{"value": {"CodeableConcept": { "coding": [{"code": "449868002"}]}}}') -- SNOMED: Current every day smoker
       OR (o.resource @> '{"value": {"CodeableConcept": { "coding": [{"code": "8517006"}]}}}')) -- SNOMED: Former smoker
  AND (extract(year from age(now(), (p.resource->>'birthDate')::date)) > 45) -- patient's age > 45
  AND (p.resource->>'gender' = 'male'); -- patient gender is male
```

Let me explain the age part: `extract(year from age(now(), (p.resource->>'birthDate')::date)) > 45`.

The `(p.resource->>'birthDate')::date` piece extracts `Patient.birthDate` from the resource and coerces it to PostgreSQL's DATE type. Accidentally, the DATE type accepts dates in FHIR format.

`age(DATE, DATE)` returns an INTERVAL value representing time distance between the two timestamps. `age(NOW(), (p.resource->>'birthDate')::date)` returns a patient's age.

The last thing we need is to extract the year from the returned interval because we're interested only in whole years. `extract(year from INTERVAL)` does it. By the way, do you like this weird way of passing arguments to the `extract()` function? I don't.

Amazing, we just found our 60 high-risk patients. It wasn't so hard, right?

### In-array Lookups

To demonstrate another common case, let's imagine that we need to grab first names, last names, and phone numbers of patients to generate a report. Because our imaginary reporting engine is generic, it cannot handle JSON/FHIR data. It only accepts data in tabular format therefore we need to extract those fields on the database side.

As we saw previously, first and last names are easy to get. But to get a phone number, we need to find an element in the`Patient.telecom` array where the `system` attribute is equal to `"phone"`, and return its `value` attribute.

There is no way to express such logic using standard PostgreSQL operators but there is the special function `jsonb_array_elements` which transforms a JSONB array into a set of SQL records:

```sql
SELECT *
FROM jsonb_array_elements('[1, 2, 3, 4, 5, 6]')
WHERE (value::text)::int > 3;
```

We can use this function to find a phone among other patient's telecoms:

```sql
SELECT resource#>>'{name,0,given,0}',
       resource#>>'{name,0,family}',
       tel->>'value'
FROM patient,
     jsonb_array_elements(resource->'telecom') tel
WHERE tel->>'system' = 'phone';
```

### Conclusion

During this tutorial we've learned the most commonly used JSONB operators and functions, examined how to apply them to implement real-world queries, and saw how to speedup queries with indexes.

SQL language may look tough at the beginning but with some practice you'll find that it's not so hard. Don't miss such a great tool for your FHIR-related tasks.
