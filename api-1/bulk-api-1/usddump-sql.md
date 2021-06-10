# $dump-sql

## Dump results of the sql query

`$dump-sql` operation takes the sql query and responds with the Chunked Encoded stream in CSV format or in NDJSON format. Useful to export data for analytics.

```typescript
POST [base]/$dump-sql
```

<table>
  <thead>
    <tr>
      <th style="text-align:left">Parameter</th>
      <th style="text-align:left">Required?</th>
      <th style="text-align:left">Type</th>
      <th style="text-align:left">Description</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td style="text-align:left">Headers</td>
      <td style="text-align:left"></td>
      <td style="text-align:left"></td>
      <td style="text-align:left"></td>
    </tr>
    <tr>
      <td style="text-align:left"><b>content-type</b>
      </td>
      <td style="text-align:left">true</td>
      <td style="text-align:left">String</td>
      <td style="text-align:left">Content-type of the query body</td>
    </tr>
    <tr>
      <td style="text-align:left">Query parameters</td>
      <td style="text-align:left"></td>
      <td style="text-align:left"></td>
      <td style="text-align:left"></td>
    </tr>
    <tr>
      <td style="text-align:left"><b>_format</b>
      </td>
      <td style="text-align:left">false</td>
      <td style="text-align:left">String</td>
      <td style="text-align:left">
        <p></p>
        <ul>
          <li>json/ndjson: return output as ndjosn</li>
          <li>otherwise: return output as TSV</li>
        </ul>
      </td>
    </tr>
    <tr>
      <td style="text-align:left">Body parameters</td>
      <td style="text-align:left"></td>
      <td style="text-align:left"></td>
      <td style="text-align:left"></td>
    </tr>
    <tr>
      <td style="text-align:left"><b>query</b>
      </td>
      <td style="text-align:left">true</td>
      <td style="text-align:left">String</td>
      <td style="text-align:left">Sql query to execute</td>
    </tr>
  </tbody>
</table>

### Example

Get id and name of each patient

{% tabs %}
{% tab title="Request" %}
#### REST Console

```yaml
POST /$dump-sql

query: select id, resource#>>'{name,0,given,0}' from patient
```

#### Curl

```bash
curl -u bulk-client:secret $AIDBOX_BASE_URL/\$dump-sql \
    -H 'Content-Type: application/yaml' -d@- <<EOF
query: select id, resource#>>'{name,0,given,0}' from patient
EOF
```
{% endtab %}

{% tab title="Response" %}
#### Status

200 OK

#### Headers

| Header | Value |
| :--- | :--- |
| Content-Type | text/tab-separated-values |
| Transfer-Encoding | Chunked |

#### Body

```yaml
pt-1	Alice
pt-2	Bob
pt-3	Charles
```

#### Body as table

|  |  |
| :--- | :--- |
| pt-1 | Alice |
| pt-2 | Bob |
{% endtab %}
{% endtabs %}



