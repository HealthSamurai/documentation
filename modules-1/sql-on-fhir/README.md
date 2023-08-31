---
description: Make analytics easier
---

# SQL on FHIR

Performing analysis on FHIR data requires extracting data from deeply nested structures of resources, which may be cumbersome in some cases. To address this problem, Aidbox implements [SQL on FHIR](https://build.fhir.org/ig/FHIR/sql-on-fhir-v2/index.html) specification allowing users to create flat views of their resources in a simple, straightforward way.&#x20;

### Creating flat views

To create a flat view of the resource, you have to define it with a special resource called [view-definition.md](view-definition.md "mention"). You can do it with a View Definitions editor in Aidbox UI.

<figure><img src="../../.gitbook/assets/image (97).png" alt=""><figcaption><p>View Definitions editor</p></figcaption></figure>

View definitions you've created can be viewed in a menu in the right part of the screen. There also are several samples to get you started. Note that samples are presented as View Definitions only and have no corresponding views in the database by default.

You can use the _Run_ button or `Ctrl+Enter` to preview your view. To save a View Definition and materialize it as a view in the database, press _Save_. _Delete_ button deletes both a View Definition and the corresponding view in the database.

You can learn more about View Definition syntax and ways to create View Definitons with REST API on the corresponding page.

### Using flat views

Once you've saved your View Definition, the corresponding flat view will be created in a database in `sof` schema. For example, to select all the rows from a view named `patient_view` you'll need a query like this:

```sql
select * from sof.patient_view
```

Note that in the current version of Aidbox, View Definitions can be materialized as SQL views only.

From here, you can use your flat views however you like. Popular use cases include building complex queries for data analysis and using BI-tools to build dashboards.
