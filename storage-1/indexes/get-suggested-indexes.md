# Index management

Since version 2211, Aidbox can suggest indexes for Search API.&#x20;

{% hint style="warning" %}
Index suggestion API is in draft stage. It will change in future!
{% endhint %}

Supported FHIR Search parameters types:

* string
* number
* date
* token
* quantity
* reference
* uri

Supported [underscored parameters](../../api-1/fhir-api/search-1/#special-parameters)

* \_id
* \_ilike
* \_text
* \_content
* \_createdAt
* \_lastUpdated
* [Dot expressions](../../api-1/fhir-api/search-1/.-expressions.md)

Not supported:

* zen Search Parameters
* \_filter
* _include,_ \_revinclude

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
  observation_date_param_knife_date_min_tstz:
    for:
      - resource-type: Observation
        name: date
        type: date
        subtype: le
      - resource-type: Observation
        name: date
        type: date
        subtype: eq
      - resource-type: Observation
        name: date
        type: date
        subtype: ne
      - resource-type: Observation
        name: date
        type: date
        subtype: lt
      - resource-type: Observation
        name: date
        type: date
        subtype: null
    statement: >-
      CREATE INDEX CONCURRENTLY IF NOT EXISTS
      "observation_date_param_knife_date_min_tstz" ON "observation" USING btree
      ((knife_extract_min_timestamptz("observation".resource,
      '[["effective","Period","start"],["effective","Period","end"],
      ["effective","dateTime"],["effective","Timing","event"],
      ["effective","instant"]]')) )
  observation_date_param_knife_date_max_tstz:
    for:
      - resource-type: Observation
        name: date
        type: date
        subtype: eq
      - resource-type: Observation
        name: date
        type: date
        subtype: ne
      - resource-type: Observation
        name: date
        type: date
        subtype: null
      - resource-type: Observation
        name: date
        type: date
        subtype: gt
      - resource-type: Observation
        name: date
        type: date
        subtype: ge
    statement: >-
      CREATE INDEX CONCURRENTLY IF NOT EXISTS
      "observation_date_param_knife_date_max_tstz" ON "observation" USING btree
      ((knife_extract_max_timestamptz("observation".resource,
      '[["effective","Period","start"],["effective","Period","end"],["effective","dateTime"],["effective","Timing","event"],["effective","instant"]]'))
      )
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
  observation_date_param_knife_date_min_tstz:
    for:
      - resource-type: Observation
        name: date
        <...>
    statement: >-
      CREATE INDEX CONCURRENTLY IF NOT EXISTS
      "observation_date_param_knife_date_min_tstz" ON "observation" USING btree
      ((knife_extract_min_timestamptz("observation".resource,
      '[["effective","Period","start"],["effective","Period","end"],["effective","dateTime"],["effective","Timing","event"],["effective","instant"]]'))
      )
  observation_date_param_knife_date_max_tstz:
    for:
      - resource-type: Observation
        name: date
        <...>
    statement: >-
      CREATE INDEX CONCURRENTLY IF NOT EXISTS
      "observation_date_param_knife_date_max_tstz" ON "observation" USING btree
      ((knife_extract_max_timestamptz("observation".resource,
      '[["effective","Period","start"],["effective","Period","end"],["effective","dateTime"],["effective","Timing","event"],["effective","instant"]]'))
      )
  observation_resource_knife_resource:
    for:
      - resource-type: Observation
        name: subject
        <...>
    statement: >-
      CREATE INDEX CONCURRENTLY IF NOT EXISTS
      "observation_resource_knife_resource" ON "observation" USING gin
      (("observation".resource) jsonb_ops)
  observation_subject_param_knife_reference_text:
    for:
      - resource-type: Observation
        name: subject
        <...>
    statement: >-
      CREATE INDEX CONCURRENTLY IF NOT EXISTS
      "observation_subject_param_knife_reference_text" ON "observation" USING
      gin ((CAST(jsonb_path_query_array("observation".resource,
      CAST(('$."subject"[*]') AS jsonpath)) AS text)) gin_trgm_ops)
  observation_resource_knife_id:
    for:
      - resource-type: Observation
        name: id
        <...>
    statement: >-
      CREATE INDEX CONCURRENTLY IF NOT EXISTS "observation_resource_knife_id" ON
      "observation" USING btree (("observation".id) )

```

Suggested indexes will increase performance of Observation.date, Observation.subject and Observation.\_id.
