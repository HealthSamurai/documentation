# Get suggested indexes

Since version 2211, Aidbox can suggest indexes for Search API.&#x20;

{% hint style="warning" %}
Index suggestion API is in the draft stage.
{% endhint %}

Supported FHIR Search parameter types:

* string
* number
* date
* token
* quantity
* reference
* uri

Supported special FHIR parameters:

* \_id
* \_ilike
* \_text
* \_content
* \_lastUpdated

Supported Aidbox search:

* \_createdAt
* [Dot expressions](../../api/rest-api/aidbox-search.md#dot-expressions)

Not supported:

* zen Search Parameters
* \_filter
* _include,_ \_revinclude
* chained Search Parameters

### aidbox.index/suggest-index

Required parameters: `resource-type` and `search-param`.

```yaml
POST /rpc

method: aidbox.index/suggest-index
params:
  resource-type: <resourceType>
  search-param: <searchParameter>
```

Example:

```yaml
POST /rpc

method: aidbox.index/suggest-index
params:
  resource-type: Observation
  search-param: date
```

Result:

```yaml
result:
  - index-name: observation_date_param_knife_date_min_tstz
    name: date
    resource-type: Observation
    statement: >-
      CREATE INDEX CONCURRENTLY IF NOT EXISTS
      "observation_date_param_knife_date_min_tstz" ON "observation" USING btree
      ((knife_extract_min_timestamptz("observation".resource,
      '[["effective","Period","start"],["effective","Period","end"],["effective","dateTime"],["effective","Timing","event"],["effective","instant"]]'))
      )
    subtypes:
      - null
      - eq
      - ne
      - lt
      - le
      - btw
    type: date
  - index-name: observation_date_param_knife_date_max_tstz
    name: date
    resource-type: Observation
    statement: >-
      CREATE INDEX CONCURRENTLY IF NOT EXISTS
      "observation_date_param_knife_date_max_tstz" ON "observation" USING btree
      ((knife_extract_max_timestamptz("observation".resource,
      '[["effective","Period","start"],["effective","Period","end"],["effective","dateTime"],["effective","Timing","event"],["effective","instant"]]'))
      )
    subtypes:
      - null
      - eq
      - ne
      - gt
      - ge
      - btw
    type: date
```

Suggested two indexes: first one to search using `lt`, `le` and `eq` prefixes, second one to search using`gt`, `ge`, `eq` prefixes.&#x20;

### aidbox.index/suggest-index-query

You can get all indexes for specific query using suggest-index-query.

Required parameters: `resource-type` and `query`.

```yaml
POST /rpc

method: aidbox.index/suggest-index-query
params:
  resource-type: <resourceType>
  query: <query>
```

Example:

```yaml
POST /rpc

method: aidbox.index/suggest-index-query
params:
  resource-type: Observation
  query: date=gt2022-01-01&_id=myid
```

Response:

```yaml
result:
  - index-name: observation_date_param_knife_date_min_tstz
    name: date
    resource-type: Observation
    statement: >-
      CREATE INDEX CONCURRENTLY IF NOT EXISTS
      "observation_date_param_knife_date_min_tstz" ON "observation" USING btree
      ((knife_extract_min_timestamptz("observation".resource,
      '[["effective","Period","start"],["effective","Period","end"],["effective","dateTime"],["effective","Timing","event"],["effective","instant"]]'))
      )
    subtypes:
      - null
      - eq
      - ne
      - lt
      - le
      - btw
    type: date
  - index-name: observation_date_param_knife_date_max_tstz
    name: date
    resource-type: Observation
    statement: >-
      CREATE INDEX CONCURRENTLY IF NOT EXISTS
      "observation_date_param_knife_date_max_tstz" ON "observation" USING btree
      ((knife_extract_max_timestamptz("observation".resource,
      '[["effective","Period","start"],["effective","Period","end"],["effective","dateTime"],["effective","Timing","event"],["effective","instant"]]'))
      )
    subtypes:
      - null
      - eq
      - ne
      - gt
      - ge
      - btw
    type: date
  - index-name: observation_resource_id
    name: id
    resource-type: Observation
    statement: >-
      CREATE INDEX CONCURRENTLY IF NOT EXISTS "observation_resource_id" ON
      "observation" USING btree (("observation".id) )
    subtypes:
      - in
      - null
    type: id
```

Suggested indexes will increase performance of Observation.date and Observation.\_id.
