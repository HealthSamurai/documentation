---
description: In this post we will see how load popular terminologies into aidbox.
---

# Load Terminologies

### Load terminology packs

We can use $import operation to load terminology packages prepared by Aidbox team:

```yaml
POST /terminology/$import

url: 'https://storage.googleapis.com/aidbox-public/icd10/icd10cm.ndjson.gz'

# response 200

result: {CodeSystem: 1, ValueSet: 1, Concept: 44487}
```

```yaml
GET /Concept
GET /CodeSystem
GET /ValueSet
```

You can inspect concepts in Aidbox Console - got to Concepts button:

![](../../.gitbook/assets/screen-shot-2019-05-14-at-17.23.53.png)

```sql
truncate concept;
truncate codesystem;
truncate valueset;
```

### Create your terminology

