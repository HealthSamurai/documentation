---
description: Overview Aidbox REST API
---

# REST API

{% hint style="info" %}
Aidbox provides two REST APIs - FHIR and Aidbox. The  main difference is [a format of resources](aidbox-and-fhir-formats.md).  Base URL for FHIR API is **/fhir** and for Aidbox **/**
{% endhint %}

|  |  |  |
| :--- | :--- | :--- |
| **Instance Level Interactions** |  |  |
| [read](crud-1/read.md) | Read the current state of the resource | both |
| [vread](crud-1/read.md#vread) | Read the state of a specific version of the resource | both |
| [update](crud-1/update.md) | Update an existing resource by its id \(or create it if it is new\) | both |
| [patch](crud-1/patch.md) | Update an existing resource by posting a set of changes to it | both |
| [delete](crud-1/delete.md) | Delete a resource | both |
| [history](history-1.md) | Retrieve the change history for a particular resource | both |
| **Type Level Interactions** |  |  |
| [create](crud-1/fhir-and-aidbox-crud.md) | Create a new resource | both |
| [search](search-1/) | Search the resource type based on some filter criteria | both |
| [history](history-1.md) | Retrieve the change history for a particular resource type | both |
| **Whole System Interactions** |  |  |
| [capabilities](introspection/metadata.md) | Get a capability statement for the system | FHIR |
| [batch/transaction](transaction.md) | Update, create or delete a set of resources in a single interaction | both |
| [batch upsert](batch-upsert.md) | Batch create or update interaction | AIdbox |
| history | Not supported for performance reason |  |
| search | Not supported for performance reason |  |

