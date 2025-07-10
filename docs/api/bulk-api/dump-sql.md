# $dump-sql

## Dump results of the sql query

`$dump-sql` operation takes the sql query and responds with the Chunked Encoded stream in CSV format or in NDJSON format. Useful to export data for analytics.

```typescript
POST [base]/$dump-sql
```

### Headers

<table data-header-hidden><thead><tr><th width="160">Parameter</th><th width="150">Required?</th><th>Type</th><th>Description</th></tr></thead><tbody><tr><td>Parameter</td><td>Required?</td><td>Type</td><td>Description</td></tr><tr><td><strong>content-type</strong></td><td>true</td><td>String</td><td>Content-type of the query body</td></tr></tbody></table>

### Query parameters

<table data-header-hidden><thead><tr><th width="150">Parameter</th><th width="169.89801699716716">Required?</th><th width="156">Type</th><th>Description</th></tr></thead><tbody><tr><td>Parameter</td><td>Required?</td><td>Type</td><td>Description</td></tr><tr><td><strong>_format</strong></td><td>false</td><td>String</td><td><ul><li>json/ndjson: return output as NDJSON</li><li>otherwise: return output as TSV</li></ul></td></tr></tbody></table>

### Body parameters

<table data-header-hidden><thead><tr><th width="171">Parameter</th><th>Required?</th><th width="150">Type</th><th>Description</th></tr></thead><tbody><tr><td>Parameter</td><td>Required?</td><td>Type</td><td>Description</td></tr><tr><td><strong>query</strong></td><td>true</td><td>String</td><td>Sql query to execute</td></tr></tbody></table>

## Example

Get id and name of each patient

{% tabs %}
{% tab title="Request" %}
**REST Console**

```yaml
POST /$dump-sql
Content-Type: text/yaml

query: select id, resource#>>'{name,0,given,0}' from patient
```

**Curl**

```bash
curl -u bulk-client:secret $AIDBOX_BASE_URL/\$dump-sql \
    -H 'Content-Type: text/yaml' -d@- <<EOF
query: select id, resource#>>'{name,0,given,0}' from patient
EOF
```
{% endtab %}

{% tab title="Response" %}
**Status**

200 OK

**Headers**

| Header            | Value                     |
| ----------------- | ------------------------- |
| Content-Type      | text/tab-separated-values |
| Transfer-Encoding | Chunked                   |

**Body**

```yaml
pt-1	Alice
pt-2	Bob
pt-3	Charles
```

**Body as table**

|      |       |
| ---- | ----- |
| pt-1 | Alice |
| pt-2 | Bob   |
{% endtab %}
{% endtabs %}
