---
description: A simple way to insert or update a collection of resources
---

# Batch Upsert

This custom operation \(a lightweight alternative to transaction\) simplifies **upsert**  \(update or create\) resources into FHIR server. Just put an array of resources \(with id's\)  on `/`

```yaml
PUT /
Accept: text/yaml
Content-Type: text/yaml

- status: planned
  class: {code: IMP}
  period: {start: "2013-06-08T10:57:34+00:00", end: "2013-06-08T12:00:00+00:00"}
  resourceType: Encounter
  id: enc-1

- status: planned
  class: {code: IMP}
  period: {start: "2013-06-08T11:00:05+00:00", end: "2013-06-08T11:30:00+00:00"}
  resourceType: Encounter
  id: enc-2

- status: planned
  class: {code: AMB}
  period: {start: "2013-06-08T10:21:01+00:00", end: "2013-06-08T11:42:11+00:00"}
  resourceType: Encounter
  id: enc-3

- status: planned
  class: {code: IMP}
  period: {start: "2013-06-07T09:02:01+00:00", end: "2013-06-07T15:10:09+00:00"}
  resourceType: Encounter
  id: enc-3
```

