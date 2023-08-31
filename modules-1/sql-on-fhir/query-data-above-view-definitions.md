# Query data from flat views

## Using flat views

Once you've saved your View Definition, the corresponding flat view will be created in a database in `sof` schema. For example, to select all the rows from a view named `patient_view` you'll need a query like this:

```sql
select * from sof.patient_view
```

Note that in the current version of Aidbox, View Definitions can be materialized as SQL views only.

From here, you can use your flat views however you like. Popular use cases include building complex queries for data analysis and using BI-tools to build dashboards.
