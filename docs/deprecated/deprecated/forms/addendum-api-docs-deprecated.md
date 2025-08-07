---
hidden: true
---

# Addendum API (docs deprecased)

* [create-addendum](addendum-api-docs-deprecated.md#create-addendum-wip)
* [add-note](addendum-api-docs-deprecated.md#add-note)
* [add-to-history](addendum-api-docs-deprecated.md#add-to-history)
* [create-amendment](addendum-api-docs-deprecated.md#create-amendment)
* [add-comment](addendum-api-docs-deprecated.md#add-comment)
* [get-addendums](addendum-api-docs-deprecated.md#get-addendums)

### create-addendum (WIP)

Creates custom addendum resource for given source (SDCDocument/SDCWorkflow)

> Extensible via ZEN

Params:

| Param    | Description       | Type                         | required? |
| -------- | ----------------- | ---------------------------- | --------- |
| addendum | Addendum resource | SDCAddendum (without 'date') | yes       |

Should be used only for custom created(by user) SDCAddendum resources

Request:

```
POST /rpc?

method: aidbox.sdc.addendum/create-addendum
params:
  addendum:
    type: aidbox.sdc.addendum/Note
    target: 
      id: doc-1
      resourceType: SDCDocument
    user:
      id: user-1
      resourceType: User
    appointment: "2022-10-10T10:10:10.000Z"
    text: "Temperature measuarements are not correct. Should be 100"
```

Response:

```
result:
  type: aidbox.sdc.addendum/Note
  id: note-1
  date: "2022-12-12T10:10:10.000Z"
  target: 
    id: doc-1
    resourceType: SDCDocument
  user:
    id: user-1
    resourceType: User
  appointment: "2022-10-10T10:10:10.000Z"
  text: "Temperature measuarements are not correct. Should be 100"
```

### add-note

Creates addendum Note for SDCDocument/SDCWorkflow

Params:

| Param  | Description                       | Type             | required? |
| ------ | --------------------------------- | ---------------- | --------- |
| target | reference to target resource      | zenbox/Reference | yes       |
| user   | reference to user which adds note | zenbox/Reference | yes       |
| text   | reference to target resource      | zen/string       | yes       |

Should be used for creating Note addendum for SDCDocument/SDCWorkflow

Request:

```
POST /rpc?

method: aidbox.sdc.addendum/add-note
params:
  target: 
    id: doc-1
    resourceType: SDCDocument
  user:
    id: user-1
    resourceType: User
  text: "Temperature measuarements are not correct. Should be 100"
```

Response:

```
result:
  type: aidbox.sdc.addendum/Note
  id: note-1
  date: "2022-12-12T10:10:10.000Z"
  target: 
    id: doc-1
    resourceType: SDCDocument
  user:
    id: user-1
    resourceType: User
  appointment: "2022-10-10T10:10:10.000Z"
  text: "Temperature measuarements are not correct. Should be 100"
```

### add-to-history

Add history addendum for resource for given status. When resource in status 'completed/amended' - snapshot can be saved (if resource is differs)

Params:

| Param    | Description                       | Type                    | required? |
| -------- | --------------------------------- | ----------------------- | --------- |
| resource | resource body                     | SDCDocument/SDCWorkflow | yes       |
| user     | reference to user which adds note | zenbox/Reference        | yes       |

Should be used for storing status changes for SDCDocument/SDCWorkflow.

> When status is `completed`/`amended` - try find latest `complete`/`amended` History resource with snapshot and checks is resource different - if so - ads snapshot field to new History addendum

Request:

```
POST /rpc?

method: aidbox.sdc.addendum/add-to-history
params:
  resource: 
     id: doc-1,
     patient: {id: pt-1, resourceType: Patient},
     encounter: {id: enc-1, resourceType: Encounter},
     type: box.sdc.sdc-example/VitalsDocument,
     resourceType: SDCDocument,
     status: "completed"
     loinc-59408-5: {value: 97},
     author: {id: doc-1, resourceType: User},
     loinc-8310-5: {value: 36.6, unit: C},
     loinc-8867-4: {value: 72}
  user:
    id: user-1
    resourceType: User
```

Response:

```
result:
  type: aidbox.sdc.addendum/History
  id: note-1
  date: "2022-12-12T10:10:10.000Z"
  target: 
    id: doc-1
    resourceType: SDCDocument
  user:
    id: user-1
    resourceType: User
  changed: true
  snapshot: 
     id: doc-1,
     patient: {id: pt-1, resourceType: Patient},
     encounter: {id: enc-1, resourceType: Encounter},
     type: box.sdc.sdc-example/VitalsDocument,
     resourceType: SDCDocument,
     status: "completed"
     loinc-59408-5: {value: 97},
     author: {id: doc-1, resourceType: User},
     loinc-8310-5: {value: 36.6, unit: C},
     loinc-8867-4: {value: 72}
```

### create-amendment

Finds History addendums for given target and try generate Amendment report - which contains difference between 2 latest snapshots of that resource.

Params:

| Param  | Description                                  | Type                    | required? |
| ------ | -------------------------------------------- | ----------------------- | --------- |
| target | reference to resource with History addendums | SDCDocument/SDCWorkflow | yes       |
| user   | reference to user which adds note            | zenbox/Reference        | yes       |

Should be used for compute diff for two latest History snapshots of SDCDocument/SDCWorkflow and created Addendum resource for it.

> If there are less than 2 History addendums with `completed`/`ammended` status and with snapshot - Amendment should not be created.

Difference is array of diff objects

Diff object is a map of keys

| key  | type                  |                          |
| ---- | --------------------- | ------------------------ |
| type | change operation type | "remove"/"replace"/"add" |
| path | path of change        | vector of strings        |
| old  | old value (if exists) | zen/string? (optional)   |
| new  | new value (if exitts) | zen/string (optional)    |

Request:

```
POST /rpc?

method: aidbox.sdc.addendum/create-amendment
params:
  target: 
     id: doc-1,
     resourceType: SDCDocument,
  user:
    id: user-1
    resourceType: User
```

Response:

```
result:
  type: aidbox.sdc.addendum/Amendment
  id: a-1
  date: "2022-12-12T10:10:10.000Z"
  user:
    id: user-1
    resourceType: User
  target: 
    id: doc-1
    resourceType: SDCDocument
  old-revision:
    id: "a-1"
    resourceType: "SDCAddendum",
  new-revision: 
    id "a-2"
    resourceType: "SDCAddendum",
  diff: 
    - type: remove
      path: ["loinc-8867-4" "value"]
      old 72
    - type: add
      path: ["loinc-59408-5" "value"]
      new: 41
    - type: replace
      path: ["loinc-8310-5" "value"]
      old: 90
      new: 97
```

### add-comment

Add comment for docuement/workflow with optional path to commented value in resource

Params:

| Param  | Description                         | Type              | required? |
| ------ | ----------------------------------- | ----------------- | --------- |
| target | reference to target resource        | zenbox/Reference  | yes       |
| user   | reference to user which adds note   | zenbox/Reference  | yes       |
| path   | path to commented value in resource | vector of strings | no        |
| text   | reference to target resource        | zen/string        | yes       |

Should be used for creating Comment addendum for SDCDocument/SDCWorkflow

Request:

```
POST /rpc?

method: aidbox.sdc.addendum/add-comment
params:
  target: 
    id: doc-1
    resourceType: SDCDocument
  user:
    id: user-1
    resourceType: User
  path: ["loinc-59408-5" "value"]
  text: "Temperature measuarements are not correct. Should be 100"
```

Response:

```
result:
  type: aidbox.sdc.addendum/Comment
  id: note-1
  date: "2022-12-12T10:10:10.000Z"
  target: 
    id: doc-1
    resourceType: SDCDocument
  user:
    id: user-1
    resourceType: User
  path: ["loinc-59408-5" "value"]
  text: "Temperature measuarements are not correct. Should be 100"
```

> It's looks like `Comment` similar to `Note` - but semantically they are different. Comment should not releated to status of `SDCDocument`/`SDCWorkflow` and used for informal conversations, which doesn't have any legal force. `Note` should be used for `SDCDocument`/`SDCWorkflow` in `completed`/`amended` statuses.

### get-addendums

Returns collection of addendums for the given target resource.

Params:

| Param  | Description                  | Type             | required? |
| ------ | ---------------------------- | ---------------- | --------- |
| target | reference to target resource | zenbox/Reference | yes       |

Request:

```
POST /rpc?

method: aidbox.sdc.addendum/get-addendums
params:
  target:
    id: doc-1
    resourceType: SDCDocument
```

Response:

```
result:
  - date: '2022-10-01T12:00:00.000Z'
    meta: ...
    type: aidbox.sdc.addendum/History
    resourceType: SDCAddendum
    snapshot: ...
    status: completed
    id: 039455f7-ed08-4462-90a6-14b5b679d728
    changed: true
    target:
      id: doc-1
      resourceType: SDCDocument
    user: ...
```

Server responds with `HTTP 422 Unprocessable Entity` if wrong target is provided.

Request:

```
POST /rpc?

method: aidbox.sdc.addendum/get-addendums
params:
    target:
        id: some-unknown-doc-id
        resourceType: SDCDocument
```

Result:

> Error

```
error:
  message: Resource not found
```

\\
