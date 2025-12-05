---
description: Poll for FHIR resource changes using version-based Changes API with efficient resource tracking and event handling.
---

# Changes API

{% hint style="warning" %}
Changes API is prone to race conditions.

Please review [#possible-race-condition](changes-api.md#possible-race-condition) section. It outlines scenarios that could lead to event loss and suggests potential solutions to mitigate this risk.
{% endhint %}

Changes API can be used to get changes of resourceType or specific resource by versions. Each event (creating, updating, deleting) will increase version of the resource by 1.

Polling request is cheap! If you want to watch rare changes (minutes-hours), this API is very resource efficient (no subscriptions, no queues) and provides you lots of control. If nothing has been changed, you will get a response with status `304`, otherwise Changes API will response with a list of changes and a new **version** to poll next time.

### Endpoints

`GET /<resourceType>/$changes` — returns the latest version for the `resourceType`.

`GET /<resourceType>/$changes?<parameters>`— depending on parameters returns changes of the `resourceType`.

\
`GET /<resourceType>/<id>/$changes` — returns latest version of a specific resource.

`GET /<resourceType>/<id>/$changes?<parameters>`— depending on parameters returns changes of the specific resource.

### Query-string parameters

Below are parameters to use in both resourceType and specific resource endpoints.

<table><thead><tr><th width="434">Parameter</th><th>Meaning</th></tr></thead><tbody><tr><td><code>version=&#x3C;version></code></td><td>Returns changes since the specified version</td></tr><tr><td><code>version=&#x3C;lower-version>,&#x3C;upper-version></code></td><td>Returns changes after the <code>lower-version</code> (exclusive) up to the<code>upper-version</code> (inclusive)</td></tr><tr><td><code>fhir=&#x3C;boolean></code></td><td>If set to <code>true</code> converts <code>changes.*.resource</code> to the FHIR format<br><em>(note</em>: <em>since Changes API is not <code>/fhir/</code> endpoint, the rest of the body isn't FHIR compliant)</em></td></tr><tr><td><code>omit-resources=&#x3C;boolean></code></td><td>If set to <code>true</code> omits resources leaving only <code>id</code> &#x26; <code>resourceType</code> fields</td></tr><tr><td><code>_count</code> &#x26; <code>_page</code></td><td>Work as described <a href="../rest-api/fhir-search/searchparameter.md">here</a></td></tr><tr><td><code>_total</code> &#x26; <code>_totalMethod</code></td><td>Work as described <a href="../rest-api/fhir-search/searchparameter.md">here</a></td></tr></tbody></table>

With parameters which start with [dot](../rest-api/aidbox-search.md#dot-expressions) you can filter resources by equality, e.g. `.name.0.family=<string>`

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

We can filter events by [dot expressions](../rest-api/aidbox-search.md#dot-expressions). Filtering Patient events by family name:

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

### Possible Race Condition

Consider a scenario where we have two database transactions: **transaction-1**, which creates **patient-1**, and **transaction-2**, which creates **patient-2**. These transactions are initiated almost simultaneously.

In this scenario, let's assume **transaction-1** starts first, retrieving the value `99` from a database sequence object for `txid`. **Transaction-2** then starts and retrieves the next sequence value, `100`, for its `txid`. Although **transaction-1** started earlier, both transactions are processed concurrently in the database, which may result in **transaction-2** being committed before **transaction-1**.

\
If the `$changes` API is called after **transaction-2** has been committed but before **transaction-1** has been committed, the response will look like this:\\

<pre class="language-yaml"><code class="lang-yaml">GET /Patient/$changes?version=98&#x26;omit-resources=true
<strong>Accept: text/yaml
</strong>
# status 200
version: 100
changes:
- event: created
  resource: {id: patient-2, resourceType: Patient}
</code></pre>

In this case, the application will likely start its next iteration from getting the changes starting with version `100`, causing version `99` to be missed.\
\
\
If the missing a single event is crucial for your situation, the potential solutions could be:

1. Periodically re-read all changes to ensure no events have been missed.
2. Implement the solution based on [Aidbox Topic-Based subscriptions](../../modules/topic-based-subscriptions/aidbox-topic-based-subscriptions.md) which can provide you `at-least-once` delivery guaranties.
