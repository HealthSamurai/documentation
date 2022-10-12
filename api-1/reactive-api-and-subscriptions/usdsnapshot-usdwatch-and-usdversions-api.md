---
description: Simple API to react on resource changes
---

# Changes API



Polling request is cheap! If you want to watch rare changes (minutes-hours), this API is very resource efficient (no subscriptions, no queues) and provides you lots of control. If nothing has been changed, you will get a response with status `304`, otherwise a list of changes and a new **version** to poll next time.

### Endpoints

`GET /<resourceType>/$changes` — returns the latest version for the `resourceType`.\
`GET /<resourceType>/<id>/$changes`  — returns latest version of a specific resource.

### Query-string parameters

| Parameter                                 | Meaning                                                                                                                                                                                                             |
| ----------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `version=<version>`                       | returns changes since the specified version                                                                                                                                                                         |
| `version=<lower-version>,<upper-version>` | returns changes after the `lower-version` (exclusive) up to the`upper-version` (inclusive)                                                                                                                          |
| `fhir=<boolean>`                          | <p>if set to <code>true</code> converts <code>changes.*.resource</code> to the FHIR format<br><em>(note, since Changes API is not <code>/fhir/</code> endpoint, the rest of the body isn't FHIR compliant)</em></p> |
| `omit-resources=<boolean>`                | if set to `true` omits resources leaving only `id` & `resourceType` fields                                                                                                                                          |
| `_count` & `_page`                        | work as described [here](https://docs.aidbox.app/api-1/fhir-api/search-1/\_count-and-\_page)                                                                                                                        |
| `_total` & `_totalMethod`                 | work as described [here](https://docs.aidbox.app/api-1/fhir-api/search-1/\_total-or-\_countmethod)                                                                                                                  |

With parameters which start with [dot](../fhir-api/search-1/.-expressions.md) you can filter resources by equality, e.g. `.name.0.family=<string>`

### Cache performance

Changes API uses a cache to track a resourceType last change. To build the cache it runs a query to get the `max` value of the `txid` column. To make this operation efficient, it is recommended to build an index on the `txid` column for tables where Changes API will be used.

Use query:

```sql
CREATE INDEX IF NOT EXISTS <resourceType>_txid_btree ON <resourceType> using btree(txid);

CREATE INDEX IF NOT EXISTS <resourceType>_history_txid_btree ON <resourceType>_history using btree(txid);
```

Replace \<resourceType> with table name, for example:

`CREATE INDEX IF NOT EXISTS patient_txid_btree ON patient using btree(txid);`

`CREATE INDEX IF NOT EXISTS patient_history_txid_btree ON patient_history using btree(txid);`

### Example

Get the latest version of the Patient resource.

<pre class="language-yaml"><code class="lang-yaml"><strong>GET /Patient/$changes
</strong>Accept: text/yaml

# status 200
version: 1</code></pre>

Assume the latest version is 1, no changes since version 1.

```yaml
GET /Patient/$changes?version=1
Accept: text/yaml

# status 304 (Not Modified)
```

Let's add 2 patients to change version of Patient resource.

```yaml
POST /Patient
Accept: text/yaml
Content-Type: text/yaml

id: pt-1
name:
- family: Smith
  given: [John]
```

```yaml
POST /Patient
Accept: text/yaml
Content-Type: text/yaml

id: pt-2
name:
- family: Wood
  given: [Amanda]-a
```

Since version 1, two events happened: created 2 patients.

```yaml
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
```

We can filter events by [dot expressions](../fhir-api/search-1/.-expressions.md). Filtering Patient events by family name:

```yaml
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
```

Check changes happened since version 1 until version 2.

```yaml
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
```

Check version of patient with id `pt-1`.

```yaml
GET /Patient/pt-1/$changes
Accept: text/yaml

# status 200
version: 2
```

```
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
```

Use `omit-resources=true` to request only id and resourceType fields in resources.

```yaml
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
