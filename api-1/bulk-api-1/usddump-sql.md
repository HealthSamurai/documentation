# $dump-sql

## Dump results of the sql query

`$dump-sql` operation takes the sql query and responds with the Chunked Encoded stream in CSV format or in NDJSON format. Useful to export data for analytics.

```typescript
POST [base]/$dump-sql
```

### Headers

| Parameter        | Required? | Type   | Description                    |
| ---------------- | --------- | ------ | ------------------------------ |
| **content-type** | true      | String | Content-type of the query body |

### Query parameters

| Parameter    | Required? | Type   | Description                                                                                            |
| ------------ | --------- | ------ | ------------------------------------------------------------------------------------------------------ |
| **\_format** | false     | String | <p></p><ul><li>json/ndjson: return output as ndjosn </li><li>otherwise: return output as TSV</li></ul> |

### Body parameters

| Parameter | Required? | Type   | Description          |
| --------- | --------- | ------ | -------------------- |
| **query** | true      | String | Sql query to execute |

## Example

Get id and name of each patient

{% tabs %}
{% tab title="Request" %}
#### REST Console

```yaml
POST /$dump-sql
Content-Type: text/yaml

query: select id, resource#>>'{name,0,given,0}' from patient
```

#### Curl

```bash
curl -u bulk-client:secret $AIDBOX_BASE_URL/\$dump-sql \
    -H 'Content-Type: text/yaml' -d@- <<EOF
query: select id, resource#>>'{name,0,given,0}' from patient
EOF
```
{% endtab %}

{% tab title="Response" %}
#### Status

200 OK

#### Headers

| Header            | Value                     |
| ----------------- | ------------------------- |
| Content-Type      | text/tab-separated-values |
| Transfer-Encoding | Chunked                   |

#### Body

```yaml
pt-1	Alice
pt-2	Bob
pt-3	Charles
```

#### Body as table

|      |       |
| ---- | ----- |
| pt-1 | Alice |
| pt-2 | Bob   |
{% endtab %}
{% endtabs %}

