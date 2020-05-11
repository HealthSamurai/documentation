---
description: Change result format
---

# \_result

 By default search result is returned as a FHIR Bundle. You can change this behavior by setting `_result=array` and your search result will be returned as JSON array with resources, without Bundle envelope:

```yaml
GET /Patient?_result=array
# 200
- id: pt1
  resourceType: Patient
  name:
  - given: [Adam]
    family: Smith
- name:
  - given: [Andrew]
    family: John
  id: pt-1
  resourceType: Patient
```

