---
description: Bulk export & import operations
---

# Bulk API

## $dump 

You can dump all resources of specific type with $dump operation - `GET [resource-type]/$dump` - Aidbox will respond with [Chunked Transfer Encoding](https://en.wikipedia.org/wiki/Chunked_transfer_encoding) [ndjson](http://ndjson.org/) stream. This is memory efficient operation - Aidbox just stream database cursor to socket. If your HTTP Client support processing of Chunked Encoding  you can process resources in stream one by one without waiting for end of response.

```yaml
GET /Patient/$dump

#response

HTTP/1.1 200 OK
Content-Type: application/ndjson
Transfer-Encoding: chunked

{"resourceType": "Patient", "id": .............}
{"resourceType": "Patient", "id": .............}
{"resourceType": "Patient", "id": .............}
{"resourceType": "Patient", "id": .............}
.........
```

## $dump-sql

Take sql query and responds with Chunked Encoded stream in CSV format. Useful to export data for analytics.

```yaml
POST /$dump-sql

query:  select id, resource#>>'{name,0,family}'
format: csv # ndjson; sql; elastic-bulk?

HTTP/1.1 200 OK
Content-Type: application/CSV
Transfer-Encoding: chunked

pt-1    Doe    John
pt-2    Smith    Mike
................
```

## Demo

{% embed url="https://www.youtube.com/watch?v=BtLxICcQNWw&feature=youtu.be" %}



Create Client & AccessPolicy for your API agent - in our case curl

```yaml
PUT /

- resourceType: Client
  id: bulk-client
  secret: secret
  grant_types: ['basic']
- resourceType: AccessPolicy
  id: bulk-client
  engine: allow
  link:
  - {id: 'bulk-client', resourceType: 'Client'}
```

Generate some number of patients using SQL - in DB Console:

```sql
INSERT INTO patient (id,txid, status, resource) 
SELECT g.id, g.id, 'created', '{"name": [{"family": "John"}]}' 
FROM generate_series(1, 100000) AS g (id);
--
SELECT count(*) FROM Patient;
```

Now we can test bulk export using $dump operation with curl program:

```bash
curl -u bulk-client:secret /Patient/\$dump > /tmp/pt.ndjson

>  % Total    % Received % Xferd  Average Speed   Time    Time     Time  Current
>                                 Dload  Upload   Total   Spent    Left  Speed
> 100 12.0M    0 12.0M    0     0  9527k      0 --:--:--  0:00:01 --:--:-- 9523k

less /tmp/pt.ndjson
```

We've got 100K  patients in less then a second!

Do not forget to clean up the database:

```sql
TRUNCATE Patient;
```

### Load data into BigQuery

```bash
# load tsv data
curl -v -X POST -u bulk-client:secret -H 'content-type:application/json' \
   https://<YOURBOX>/\$dump-sql \
   -d '{"query": "select id, ts, resource#>>'"'"'{module}'"'"' from entity"}' \
   > data.tsv

# create dataset
bq mk test
bq ls

# load datast
bq load --source_format=CSV \
  --field_delimiter=tab \
  test.entities ./data.tsv res,ts,mod

# list ids
bq query 'select id from test.entities'

# remove dataset
bq rm -r test
```

