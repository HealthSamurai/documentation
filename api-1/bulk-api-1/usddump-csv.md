# $dump-csv

Dump resource in CSV format

`$dump-csv` operation dumps resource data in Aidbox format as CSV \([RFC4180](https://datatracker.ietf.org/doc/html/rfc4180)\). In this formatб columns are paths to JSON values and Rows are values. It includes the header.

Neither the specific order of columns nor the order of rows is guaranteed.

{% tabs %}
{% tab title="Request format" %}
```yaml
GET [base]/<resourceType>/$dump-csv
```
{% endtab %}
{% endtabs %}

| URL Parameter | Description |
| :--- | :--- |
| `resourceType` | Type of the resource to dump |

### Examples

{% tabs %}
{% tab title="Request" %}
#### Rest Console

```typescript
GET /Patient/$dump-csv
```

#### Curl

```bash
$ curl "$AIDBOX_BASE/\$dump-csv" \
    -H 'authorization: Basic YnVsay1jbGllbnQ6c2VjcmV0'
```
{% endtab %}
{% endtabs %}

