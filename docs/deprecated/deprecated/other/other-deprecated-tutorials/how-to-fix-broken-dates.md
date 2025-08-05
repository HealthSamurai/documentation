# How to Fix Broken Dates

In some cases, you can have invalid data in your database- after importing data, or an error in SQL migrations.

For example, we have Patient resource with incorrect `birthDate` in the database. If we will try to search Patient by date, we will catch an error:

<mark style="color:blue;">`GET`</mark> `/fhir/Patient?birthDate=2020-02-01`

```javascript
resourceType: OperationOutcome
id: >-
  exception
text:
  status: generated
  div: 'ERROR: date/time field value out of range: "2020-02-30T00:00:00"'
issue:
  - severity: fatal
    code: exception
    diagnostics: 'ERROR: date/time field value out of range: "2020-02-30T00:00:00"'
```

To fix this error we should find all incorrect dates in table via SQL query

```
select max((resource#>>'{birthDate}')::date) from patient;
```

In response we catch an error

```
ERROR: date/time field value out of range: "2020-02-30"
```

Now we can fix these dates:

```
update patient
set resource = resource || '{"birthDate": "2020-02-29"}'
where resource#>>'{birthDate}' = '2020-02-30';
```
