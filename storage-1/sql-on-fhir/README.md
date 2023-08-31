---
description: Make analytics easier
---

# SQL on FHIR

{% hint style="info" %}
**SQL on FHIR** engine is currently in **preview**
{% endhint %}

Performing analysis on FHIR data requires extracting data from deeply nested structures of resources, which may be cumbersome in some cases. To address this problem, Aidbox implements [SQL on FHIR](https://build.fhir.org/ig/FHIR/sql-on-fhir-v2/index.html) specification allowing users to create flat views of their resources in a simple, straightforward way

## Create View Definitions

To utilize SQL on FHIR it's important to understand what a View Definition is and how to use it to define flat views.

{% content-ref url="view-definition.md" %}
[view-definition.md](view-definition.md)
{% endcontent-ref %}

## Query data from the defined views

Once your flat view is defined and materialized, you can query data from it using plain SQL.

{% content-ref url="query-data-above-view-definitions.md" %}
[query-data-above-view-definitions.md](query-data-above-view-definitions.md)
{% endcontent-ref %}

## SQL on FHIR reference

To dive deeper into the nuances of using SQL on FHIR in Aidbox, consult the reference page.

{% content-ref url="reference.md" %}
[reference.md](reference.md)
{% endcontent-ref %}
