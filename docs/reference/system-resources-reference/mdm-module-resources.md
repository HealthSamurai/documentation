---
description: Aidbox MDM module resources for master data management and record linkage.
---

# MDM Module Resources

Resources for MDM module.

 ## AidboxLinkageModel

MDM (Master Data Management) Linkage Model resource for probabilistic record matching

```fhir-structure
[ {
  "path" : "resource",
  "name" : "resource",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "thresholds",
  "name" : "thresholds",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : ""
}, {
  "path" : "thresholds.auto",
  "name" : "auto",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "decimal",
  "desc" : ""
}, {
  "path" : "thresholds.manual",
  "name" : "manual",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "decimal",
  "desc" : ""
}, {
  "path" : "vars",
  "name" : "vars",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : ""
}, {
  "path" : "features",
  "name" : "features",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "Object",
  "desc" : ""
}, {
  "path" : "blocks",
  "name" : "blocks",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "Object",
  "desc" : ""
} ]
```

