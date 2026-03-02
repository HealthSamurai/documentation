---
description: Export FHIR resources as CSV with $dump-csv operation, converting JSON paths to columns.
---

# $dump-csv

Dump resource in CSV format

`$dump-csv` operation dumps resource data in Aidbox format as CSV ([RFC4180](https://datatracker.ietf.org/doc/html/rfc4180)). In this format, columns are paths to JSON values and Rows are values. It includes the header.

Neither the specific order of columns nor the order of rows is guaranteed.

{% tabs %}
{% tab title="Request format" %}
```yaml
GET [base]/<resourceType>/$dump-csv
```
{% endtab %}
{% endtabs %}

| URL Parameter  | Description                  |
| -------------- | ---------------------------- |
| `resourceType` | Type of the resource to dump |

### Examples

Dump patients in CSV

{% tabs %}
{% tab title="Request" %}
**Rest Console**

```typescript
GET /Patient/$dump-csv
```

**Curl**

```bash
$ curl "$AIDBOX_BASE/Patient/\$dump-csv" \
    -H 'authorization: Basic YnVsay1jbGllbnQ6c2VjcmV0'
```
{% endtab %}

{% tab title="Response" %}
**Status**

200 OK

**Headers**

| Header            | Value    |
| ----------------- | -------- |
| Content-Type      | text/csv |
| Transfer-Encoding | chunked  |

**Body**

```
citizenship.0.code.text,id,name.0.given.0,resourceType
,pt-1,Alice,Patient
,pt-2,Bob,Patient
ru,pt-3,Charles,Patient
```

**Body as table**

| citizenship.0.code.text | id   | name.0.given.0 | resourceType |
| ----------------------- | ---- | -------------- | ------------ |
|                         | pt-1 | Alice          | Patient      |
|                         | pt-2 | Bob            | Patient      |
| ru                      | pt-3 | Charles        | Patient      |
{% endtab %}
{% endtabs %}

Dump Appointments in CSV

{% tabs %}
{% tab title="Request" %}
**REST Console**

```
GET /Appointment/$dump-csv
```

**Curl**

```bash
curl -u bulk-client:secret $AIDBOX_BASE_URL/Appointment/\$dump-csv
```
{% endtab %}

{% tab title="Response" %}
**Status**

200 OK

**Headers**

| Header            | Value    |
| ----------------- | -------- |
| Content-Type      | text/csv |
| Transfer-Encoding | chunked  |

**Body**

```
id,participant.0.actor.id,participant.0.actor.resourceType,participant.0.status,resourceType,status
ap-1,pt-1,Patient,accepted,Appointment,fulfilled
ap-2,pt-1,Patient,accepted,Appointment,booked
ap-3,pt-2,Patient,accepted,Appointment,fulfilled
```

**Body as table**

| id   | participant.0.actor.id | participant.0.actor.resourceType | participant.0.status | resourceType | status    |
| ---- | ---------------------- | -------------------------------- | -------------------- | ------------ | --------- |
| ap-1 | pt-1                   | Patient                          | accepted             | Appointment  | fulfilled |
| ap-2 | pt-1                   | Patient                          | accepted             | Appointment  | booked    |
| ap-3 | pt-2                   | Patient                          | accepted             | Appointment  | fulfilled |
{% endtab %}
{% endtabs %}
