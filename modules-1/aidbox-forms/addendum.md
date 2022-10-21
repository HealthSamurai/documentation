---
description: Overview of addendum scenario
---

# Addendum

There are two scenarios when you can do amendmets after signing the form.&#x20;

* The first one, if you can't change data then you have the ability to add the addendum note, in this case you don't need to unlock the form.
* &#x20;The second one, if you have the ability to change data then you need to unlock the form, change data and sign the form agian.

Addendum module defines new resource `SDCAddendum` which contains any additional information about `SDCWorkflow`, `SDCDocument` resources.

With this module implemented several secondary or maintenance features of SDCForms

* History of document/workflow with snapshots on important points
* Amendment analysis with diff between snapshots
* Addendum Note with free text
* Comments, related to workflow/document

`SDCAddendum` has next basic keys in resource.

| key    | description                                    | type             |
| ------ | ---------------------------------------------- | ---------------- |
| type   | type of addendum                               | string           |
| target | link to resource for which addendum is created | zenbox/Reference |
| user   | author of addendum                             | zenbox/Reference |
| date   | timestamp (generated automatically)            | zen/datetime     |

Also every Addendum Type has additional keys.

Example:

```
resourceType: SDCAddendum
type: "aidbox.sdc.addendum/History"
target: {id: "doc-1", resourceType "SDCDocument"}
user: {id: "user-1", resourceType "User"}
date: "2022-10-10T10:10:11.000Z"
status: "in-amendment"
changed: false
snapshot: {...}
```



You can use [Addendum API](../../reference/aidbox-forms/addendum-api.md)

* [create-addendum](addendum.md#create-addendum-wip) - creates custom addendum resource for given source (SDCDocument/SDCWorkflow)
* [add-note](addendum.md#add-note) - creates addendum Note for SDCDocument/SDCWorkflow&#x20;
* [add-to-history](addendum.md#add-to-history) - add history addendum for resource for given status. When resource in status `completed/amended` - snapshot can be saved (if resource is differs)
* [create-amendment](addendum.md#create-amendment)&#x20;
* [add-comment](addendum.md#add-comment) - add comment for document/workflow with optional path to commented value in resource
