---
description: Simple API to react on resource changes
---

# Changes API

By `GET /<resource-type>/$changes` without the `version` parameter you will get latest version, which can be used to poll for changes by `GET /<resource-type>/$changes?version=<version>`

### Endpoints

`GET /<resourceType>/$changes` returns the latest version for the `resourceType`\
`GET /<resourceType>/<id>/$changes` returns latest version of a specific resource\
Returned version value can be used with the version query-string parameter

### Query-string parameters

`version=<version>` returns changes since the specified version\
`version=<lower-version>,<upper-version>` returns changes after the `lower-version` (exclusive) up to the`upper-version` (inclusive)\
\
`fhir=<boolean>` if set to `true` converts `changes.*.resource` to the FHIR format\
_(note, since Changes API is not `/fhir/` endpoint, the rest of the body isn't FHIR compliant)_

With parameters which start with dot you can filter resources by equality, e.g. `.name.0.family=<string>`

`omit-resources=<boolean>` if set to `true` omits resources leaving only `id` & `resourceType` fields

`_count` & `_page` work as described [here](https://docs.aidbox.app/api-1/fhir-api/search-1/\_count-and-\_page)\
`_total` & `_totalMethod` work as described [here](https://docs.aidbox.app/api-1/fhir-api/search-1/\_total-or-\_countmethod)

### Notes

Polling request is cheap! If you want to watch rare changes (minutes-hours), this API is very resource efficient  (no subscriptions, no queues) and provides you lots of control. If nothing has been changed, you will get a response with status `304`,  otherwise a list of changes and a new **version** to poll next time.

### Examples

```yaml
---
GET /Patient/$changes
Accept: text/yaml

# status 200
version: 1

---
GET /Patient/$changes?version=1
Accept: text/yaml

# status 304 (Not Modified)

---
POST /Patient
Accept: text/yaml
Content-Type: text/yaml

id: pt-1
name:
- family: Smith
  given: [John]

---
POST /Patient
Accept: text/yaml
Content-Type: text/yaml

id: pt-2
name:
- family: Wood
  given: [Amanda]

---
GET /Patient/$changes?version=1
Accept: text/yaml

# status 200
version: 3
changes:
- event: created
  resource:
    id: pt-1
    name:
    - family: Smith
      given: [John]
- event: created
  resource:
    id: pt-2
    name:
    - family: Wood
      given: [Amanda]

---
GET /Patient/$changes?version=1&.name.0.family=Wood
Accept: text/yaml

# status 200
version: 3
changes:
- event: created
  resource:
    id: pt-2
    name:
    - family: Wood
      given: [Amanda]
      
---
GET /Patient/$changes?version=1,2
Accept: text/yaml

# status 200
version: 2
changes:
- event: created
  resource:
    id: pt-1
    name:
    - family: Smith
      given: [John]
---
GET /Patient/pt-1/$changes
Accept: text/yaml

# status 200
version: 2
---
GET /Patient/pt-1/$changes?version=1
Accept: text/yaml

# status 200
version: 2
changes:
- event: created
  resource:
    id: pt-1
    name:
    - family: Smith
      given: [John]
---
GET /Patient/$changes?version=1&omit-resources=true
Accept: text/yaml

# status 200
version: 3
changes:
- event: created
  resource: {id: pt-1, resourceType: Patient}
- event: created
  resource: {id: pt-2, resourceType: Patient}
```
