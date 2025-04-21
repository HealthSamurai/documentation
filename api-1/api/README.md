---
description: This page describes types of Aidbox REST API operations in general
---

# REST API

{% hint style="info" %}
Aidbox provides two REST APIs - FHIR and Aidbox. The main difference is [a format of resources](../../storage-1/other/aidbox-and-fhir-formats.md). Base URL for FHIR API is **/fhir** and for Aidbox **/**
{% endhint %}

| Interaction type                                       |                                                                     | Format |
| ------------------------------------------------------ | ------------------------------------------------------------------- | ------ |
| **Instance Level Interactions**                        |                                                                     |        |
| [read](crud-1/read.md)                                 | Read the current state of the resource                              | both   |
| [vread](crud-1/read.md#vread)                          | Read the state of a specific version of the resource                | both   |
| [update](crud-1/update.md)                             | Update an existing resource by its id (or create it if it is new)   | both   |
| [patch](crud-1/patch.md)                               | Update an existing resource by posting a set of changes to it       | both   |
| [delete](crud-1/delete.md)                             | Delete a resource                                                   | both   |
| [history](history-1.md)                                | Retrieve the change history for a particular resource               | both   |
| **Type Level Interactions**                            |                                                                     |        |
| [create](crud-1/fhir-and-aidbox-crud.md)               | Create a new resource                                               | both   |
| [search](fhir-search/)                                 | Search the resource type based on some filter criteria              | both   |
| [history](history-1.md)                                | Retrieve the change history for a particular resource type          | both   |
| [$dump](../bulk-api-1/#usddump)                        | Dump all resources of specific type                                 | Aidbox |
| [$load](../bulk-api-1/#usdload)                        | Load resources of specific type                                     | both   |
| **Whole System Interactions**                          |                                                                     |        |
| [capabilities](other/metadata.md)                      | Get a capability statement for the system                           | FHIR   |
| [batch/transaction](../transaction.md)                 | Update, create or delete a set of resources in a single interaction | both   |
| [batch upsert](../other/batch-upsert.md)               | Batch create or update interaction                                  | Aidbox |
| [$import](../bulk-api-1/#usdimport-and-fhir-usdimport) | Bulk Import async operation                                         | both   |
| [$load](../bulk-api-1/#usdload)                        | Bulk load ndjson file with resources                                | both   |
| [$dump-sql](../bulk-api-1/#usddump-sql)                | Bulk export result of SQL Query                                     | Aidbox |
| history                                                | Not supported for performance reason                                |        |
| search                                                 | Not supported for performance reason                                |        |
