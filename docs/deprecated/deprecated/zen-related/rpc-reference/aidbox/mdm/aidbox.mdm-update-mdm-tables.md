# aidbox.mdm/update-mdm-tables

Create denormalized MDM tables for specified resource type. These tables are required for MDM operation.

## Syntax

```
POST /rpc

method: aidbox.mdm/update-mdm-tables
params:
  resource-type: <string, required>

---
# out
result:
  ok <string, fixed>
```

## Parameters

`params.resource-type: <string, required>`

Resource type for which to create/update MDM tables. MDM model has to be set up for this resource type.

## Examples

Create MDM tables for Patient resource

```
POST /rpc

method: aidbox.mdm/update-mdm-tables
params:
  resource-type: Patient
```

Returns

```
result:
  ok
```
