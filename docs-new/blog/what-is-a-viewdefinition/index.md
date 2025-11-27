---
title: "SQL on FHIR: What is a ViewDefinition, and how does it work?"
slug: "what-is-a-viewdefinition"
published: "2024-07-26"
author: "Nikolai Ryzhikov"
reading-time: "6 min read"
tags: ["SQL on FHIR", "Database", "Analytics"]
category: "FHIR"
teaser: "Discover how to flatten complex healthcare data using ViewDefinition resource. Dive into our article for tips and examples."
image: "cover.png"
---

# What is a ViewDefinition, and how does it work?

The "SQL on FHIR v2" working group is close to its first release, scheduled for the end of summer 2024. The specification aims to build a bridge between FHIR data and modern databases and analytics ecosystems. The core idea is to introduce a standardized way to flatten FHIR resources into relational tables. We believe that a flat representation of healthcare data will make data engineers and analytical tools more efficient.

This flattening transformation is defining by a special resource type: ViewDefinition. Although there are no universal flat views for most FHIR resources, we believe many useful, use-case-specific views could exist. ViewDefinitions are CanonicalResources and can be published as part of Implementation Guides. With standard ANSI SQL queries, they can form the basis for interoperable analytics and reporting on FHIR. This post will help you understand how ViewDefinition works.

A ViewDefinition is an algorithm that describes the flattening transformation of FHIR resources, composed of combinations of a few functions.
- `column({name:column_name,path: fhirpath},...)` - the main workhorse of transformation, this function will extract elements using FHIRPath expressions and put the result into columns
- `where(fhirpath)` - this function filters resources by FHIRPath expression. For example, you may want to transform only specific profiles, like blood pressure, into a simple table
- `forEach(expr, transform)` - this function unnests collection elements into separate rows
- `select(rows1, rows2)` - this function cross-joins rows1 and rows2, and is mostly used to join the results of forEach with top-level columns
- `union(rows, rows)` - this function concatenates sets of rows. The main use case is combining rows from different branches of a resource (for example, `telecom` and `contact.telecom`)

> Use our free online [ViewDefinition Builder](https://sqlonfhir.aidbox.app/) to convert FHIR data stored in JSON representation into a tabular, flat format for convenient data analysis. [Go to ViewDefinition Builder](https://sqlonfhir.aidbox.app/)

A ViewDefinition is represented as a FHIR Resource (JSON document) where the elements (keywords) correspond to functions:

```javascript
{
    "resourceType": "ViewDefinition",
    "resource": "Patient",
    // (0)
    "where": [{filter: "active = true"}],
    // (5)
    "select": [
        {
          // (4)
          "column": [
             {"path": "getResourceKey()", "name": "id"},
             {"path": "identifier.where(system='ssn')", "name": "ssn"},
          ]
        },
        { 
          // (3)
          "unionAll": [
            {
              // (1)
              "forEach": "telecom.where(system='phone')",
              "column": [{"path": "value", "name": "phone"}] 
            },
            { 
              // (2)
              "forEach": "contact.telecom.where(system='phone')",
              "column": [{"path": "value",  "name": "phone"}] 
            }
       ]}       
    ]
}
```

This view produces a table of patient contacts, with each row representing a telecom entry.
0. "where" filters only active patients
1. "forEach" unnests `Patient.telecom` and selects phone numbers
2. "forEach" unnests `Patient.contact.telecom` and selects phone numbers
3. "unionAll" concatenates the results of the two "forEach" operations
4. "column" statement extracts `id` and `ssn`
5. "select" statement cross-joins `id` and `ssn` with telecom phone numbers

Here is an example of the input and output for this ViewDefinition.

```javascript
1[
2 {
3    "resourceType": "Patient", 
4    "id": "pt1", 
5    "identifier": [{"system": "ssn", "value": "s1"}],
6    "telecom":   [{"system": "phone", "value": "tt1"}],
7    "contact": [
8         {"telecom": [{"system": "phone", "value": "t12"}]},
9         {"telecom": [{"system": "phone", "value": "t13"}]}
10     ]
11 },
12 {
13    "resourceType": "Patient", 
14    "id": "pt2", 
15    "identifier": [{"system": "ssn", "value": "s2"}],
16    "telecom":   [{"system": "phone", "value": "t21"}],
17    "contact": [
18         {"telecom": [{"system": "phone", "value": "t22"}]},
19         {"telecom": [{"system": "phone", "value": "t23"}]}
20     ]
21 }
22]
```

### Result

| id | ssn | phone |
| --- | --- | --- |
| pt1 | s1 | t11 |
| pt1 | s1 | t12 |
| pt1 | s1 | t13 |
| pt2 | s1 | t21 |
| pt2 | s1 | t22 |
| pt2 | s1 | t23 |

### FHIRPath subset

ViewDefinitions use a [minimal subset of FHIRPath](https://build.fhir.org/ig/FHIR/sql-on-fhir-v2/StructureDefinition-ViewDefinition.html#supported-fhirpath-functionality) to make implementation as simple as possible. Additionally, the specification introduces a few special functions:
- `getResourceKey` - indirectly gets the resource ID. Sometimes it can be complicated, which is why this layer of indirection is used
- `getReferenceKey(resourceType)` - a similar function that gets the ID from a reference

### Functions / Keywords

Let’s walk through each function in detail.

#### column

The 'column' function extracts elements into columns using FHIRPath expressions. The algorithm starts by receiving a list of {name, path} pairs. For each record in the given context, it evaluates the path expression to extract the desired elements. The resulting values are then added as columns to the output row.

```javascript
{
    "column": [
        {"name": "id",          "path": "getResourceKey()"},
        {"name": "bod",         "path": "birthDate"},
        {"name": "first_name",  "path": "name.first().given.join(' ')"},
        {"name": "last_name",   "path": "name.first().family"},
        {"name": "ssn",         "path": "identifier.where(system='ssn').value.first()"},
        {"name": "phone",       "path": "telecom.where(system='phone').value.first()"},
    ]
}
```

Here is the naive JavaScript implementation:

```javascript
function column(cols, rows) {
    return rows.map((row)=> {
        return cols.reduce((res, col ) => {
            res[col.name] = fhirpath(col.path, row)
            return res
        }, {})
    })
}
```

#### where

The 'where' function retains only those records for which its FHIRPath expression returns true.

```javascript
{
  "resourceType": "ViewDefinition",
  "resource": "Patient",
  "where": [
      {"filter": "meta.profile.where($this = 'myprofile').exists()"},
      {"filter": "active = 'true'"}
  ]
}
```

Basic JavaScript implementation:

```javascript
function where(exprs, rows) {
    return rows.filter((row)=> {
        return exprs.every((expr)=>{
            return fhirpath(expr, row) == true;
        })
    })
}
```

#### forEach & forEachOrNull
The `forEach` function is intended for flattening nested collections by applying a transformation to each element. It consists of a FHIRPath expression for the collection to iterate over and a transformation to apply to each item. This function is akin to `flatMap` or `mapcat` in other programming languages.

```javascript
{
    "resourceType": "ViewDefinition",
    "resource": "Patient",
    "select": [{
      "forEach": "name",
      "column": [
        {"path": "given.join(' ')", "name": "first_name"},
        {"path": "family", "name": "last_name"}
      ]
    }]
}
```
There are two versions of this function: `forEach` and `forEachOrNul`. The primary difference is that `forEach` removes records where the FHIRPath expression returns no results, whereas `forEachOrNull` keeps an empty record in such cases.

```javascript
function forEach(path, expr, rows) {
    return rows.flatMap((row)=> {
        return fhirpath(expr, row).map((item)=>{
            // evalKeyword will call column, select or other functions
            return evalKeyword(expr, item)
        })
    })
}
```

#### select
The select function is used in combination with forEach to cross-join parent elements (like `Patient.id`) with unnested collection elements (like `Patient.name`). This function merges columns from each row set, resulting in a comprehensive combination of data from the input collections.

```javascript
{
    "resourceType": "ViewDefinition",
    "resource": "Patient",
    "select": [
     {
         "column": [
            {"path": "getResourceKey()", "name": "id"}
         ]
     },
     {
        "forEach": "name",
        "column": [
          {"path": "given.join(' ')", "name": "first_name"},
          {"path": "family", "name": "last_name"}
        ]
     }
    ]
}
```

Naive implementation is:

```javascript
function select(rows1, rows2){
    return rows1.flatMap((r1)=> {
        return rows2.map((r2)=>{
            // merge r1 and r2
            return { ...r1, ...r2 }
        })
    })
}

select([{a: 1}, {a: 2}], [{b: 1}, {b: 2}])
//=>
[{a: 1, b: 1}, 
 {a: 1, b: 2},
 {a: 2, b: 1},
 {a: 2, b: 2}]
```

#### unionAll
The `unionAll` function combines rows from different branches of a resource tree by concatenating multiple record sets. This function essentially concatenates several collections of records into a single, unified collection, preserving all rows from the input sets.

```javascript
{
    "resourceType": "ViewDefinition",
    "resource": "Patient",
    "select": [
        {
          "column": [
             {"path": "getResourceKey()", "name": "id"}
          ]
        },
        { 
          "unionAll": [
            {
              "forEach": "telecom.where(system='phone')",
              "column": [{"path": "value", "name": "phone"}] 
            },
            { 
              "forEach": "contact.telecom.where(system='phone')",
              "column": [{"path": "value",  "name": "phone"}] 
            }
       ]}       
    ]
}
```

The implementation is just a simple concatenation:

```javascript
function unionAll(rowSets){
    return rowSet.flatMap((rows)=> { return rows})
}

unionAll([1,2,3], [3,4,5])
//=>
[1,2,3,3,4,5]
```
In a resource, different keywords can appear at the same level. For example, `select`, `forEach` and `unionAll` can all be present in the same JSON node. To interpret such nodes, you have to reorder the keywords (functions) according to precedence, with higher precedence functions bubbling up:

- forEach(OrNull)
- select
- unionAll
- column

```javascript
{
    "forEach":   FOREACH,
    "column":    [COLUMNS], // got into select
    "unionALL":  [UNIONS],  // got into select
    "select":    [SELECTS]
}
//=>
{     
    "forEach": FOREACH
    "select": [
       {"column":   [COLUMNS]},
       {"unionAll": [UNIONS]},
       SELECTS...
    ]
}
```

Check out the [reference implementation.](https://github.com/FHIR/sql-on-fhir-v2/blob/master/sof-js/src/index.js#L144)

### ViewDefinition Engines

The ViewDefinition can be executed by an engine to produce flat views from FHIR resources. There are two categories of engines:

- **In-memory engines**: These engines consume resources, flatten them, and output results into a stream, a file, or a table. You can imagine an ETL pipeline transforming FHIR Bulk export NDJSON files into Parquet files.

- **In-database engines**: These engines translate ViewDefinition into a SQL query over a FHIR-native database. In this case, the view can be a real database view. In-database engines can be much more efficient than in-memory engines in terms of speed and storage resources but are more complex for implementers.

![](image-1.jpeg)

There is an official list of implementations available at <https://fhir.github.io/sql-on-fhir-v2/#impls>. Most implementations are in-memory engines. Aidbox (PostgreSQL) and Pathling (Spark SQL) are in-database engines.

### Aidbox (in-database engine)

Aidbox is a FHIR Server and Database for FHIR-native systems with out-of-the-box support for SQL on FHIR. Aidbox transpiles a ViewDefinition into a PostgreSQL SQL query, which can be run "as is" or used to create a database view.

For example, this ViewDefinition

```javascript
{
  "resource": "Patient",
  "select": [
    {
      "column": [
        {
          "name": "id",
          "path": "getResourceKey()"
        }
      ]
    },
    {
      "forEach": "name",
      "select": [
        {
          "column": [
            {
              "name": "family",
              "path": "family"
            },
            {
              "name": "given",
              "path": "given.join(' ')"
            }
          ]
        }
      ]
    }
  ]
}
```

will be transpiled into:

```javascript
SELECT
  cast(id AS text) as "id",
  cast(
    jsonb_path_query_first(q1_1, '$  . family') #>> '{}' AS text
  ) as "family",
  coalesce(
    array_to_string(
      (
        SELECT
          array_agg(x)
        FROM     jsonb_array_elements_text(jsonb_path_query_array(q1_1, '$  . given  [*]')) as x
      ),
      ' '
    ),
    ''
  ) as "given"
FROM
  "patient" as r
  JOIN LATERAL jsonb_path_query(r.resource, '$  . name  [*]') q1_1 
  ON true
LIMIT 100
```

> You can run Aidbox locally or in cloud Sandbox in minutes - [https://www.health-samurai.io/aidbox#run](https://www.health-samurai.io/aidbox?utm_source=article&utm_medium=view%20definition&utm_campaign=from%20text#run).

You can visually build and debug ViewDefiniton with FHIRPath autocompletes using our [ViewDefinition Builder.](https://sqlonfhir.aidbox.app/?utm_source=article&utm_medium=view%20definition&utm_campaign=from%20text)

![](image-2.png)

ViewDefinition in FHIR is a powerful tool that allows for flexible data representation management by creating customizable views based on various conditions and parameters. This is particularly useful when you need to tailor the display of information to meet specific user or system requirements. By using ViewDefinition, you can significantly simplify and expedite the data integration process while ensuring data integrity and accessibility.

  
To practically explore the capabilities of ViewDefinition, you can install the [free version of Aidbox](https://www.health-samurai.io/aidbox#run). It allows you to test all features without limitations, providing an ideal environment for development and experimentation.

Demo the ELT implementation for PostgreSQL using [Aidbox](https://health-samurai.io/aidbox?utm_source=web&utm_medium=artviewdef&utm_campaign=nikolai), open-source [**ViewDefinition Builder**](https://sqlonfhir.aidbox.app/?utm_source=web&utm_medium=artviewdef&utm_campaign=nikolai&__hstc=96403715.6949fa01ff24027a1f8ca2dfe7e9bd7f.1703067308257.1730969951321.1730973906861.675&__hssc=96403715.8.1730973906861&__hsfp=2929422519), and Grafana.

### Join the Working Group

If you want to ask any questions or contribute to SQL on FHIR, join us in the chat at [chat.fhir.org](https://chat.fhir.org/). For personal questions, feel free to ask me on [LinkedIn](https://www.linkedin.com/in/nikolai-ryzhikov-586a6913/).
