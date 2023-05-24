---
description: Simple API to react on resource changes
---

# Changes API

Changes API can be used to get changes of resourceType or specific resource by versions. Each event (creating, updating, deleting) will increase version of the resource by 1.

Polling request is cheap! If you want to watch rare changes (minutes-hours), this API is very resource efficient (no subscriptions, no queues) and provides you lots of control. If nothing has been changed, you will get a response with status `304`, otherwise Changes API will response with a list of changes and a new **version** to poll next time.

### Endpoints

`GET /<resourceType>/$changes` — returns the latest version for the `resourceType`.

`GET /<resourceType>/$changes?<parameters>`— depending on parameters returns changes of the `resourceType`.&#x20;

\
`GET /<resourceType>/<id>/$changes`  — returns latest version of a specific resource.

`GET /<resourceType>/<id>/$changes?<parameters>`— depending on parameters returns changes of the specific resource.&#x20;

### Query-string parameters

Below are parameters to use in both resourceType and specific resource endpoints.

<table><thead><tr><th width="434">Parameter</th><th>Meaning</th></tr></thead><tbody><tr><td><code>version=&#x3C;version></code></td><td>Returns changes since the specified version</td></tr><tr><td><code>version=&#x3C;lower-version>,&#x3C;upper-version></code></td><td>Returns changes after the <code>lower-version</code> (exclusive) up to the<code>upper-version</code> (inclusive)</td></tr><tr><td><code>fhir=&#x3C;boolean></code></td><td>If set to <code>true</code> converts <code>changes.*.resource</code> to the FHIR format<br><em>(note</em>: <em>since Changes API is not <code>/fhir/</code> endpoint, the rest of the body isn't FHIR compliant)</em></td></tr><tr><td><code>omit-resources=&#x3C;boolean></code></td><td>If set to <code>true</code> omits resources leaving only <code>id</code> &#x26; <code>resourceType</code> fields</td></tr><tr><td><code>_count</code> &#x26; <code>_page</code></td><td>Work as described <a href="https://docs.aidbox.app/api-1/fhir-api/search-1/_count-and-_page">here</a></td></tr><tr><td><code>_total</code> &#x26; <code>_totalMethod</code></td><td>Work as described <a href="https://docs.aidbox.app/api-1/fhir-api/search-1/_total-or-_countmethod">here</a></td></tr></tbody></table>

With parameters which start with [dot](../fhir-api/search-1/.-expressions.md) you can filter resources by equality, e.g. `.name.0.family=<string>`

### Example

Get the latest version of the Patient resource.

<pre class="language-yaml"><code class="lang-yaml"><strong>GET /Patient/$changes
</strong>Accept: text/yaml

# status 200
version: 1
</code></pre>

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
