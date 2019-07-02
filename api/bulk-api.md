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

## $load

You can efficiently load data into Aidbox  in ndjson format from external service with:

```yaml
POST /$load

source: 'https://storage.googleapis.com/aidbox-public/icd10cm.ndjson.gz'
temp_table: _icd10cm
validation: 
  enable: off # | async | on
  checks: struct # | refs | all
method: copy # | insert
```

## $import

To asynchronically import a big portion of data you cant use $import operation. It is now in beta state. The $import request format is

```yaml
POST /$import
resource:
   id: "imp-testing-1" | nil #can be omitted
   action: "start" | "pause" | "abort" | "resume" | "force-resume"
   #if action is omitted the default value is "start"
   secret: "your-very-secret" #ignored in beta
   #all the items below are ignored for commands other than "start" 
   inputFormat: "application/fhir+ndjson" #only gzipped ndjson files are supported 
   inputSource: "source" #ignored
   inputs:
      - 
        type: "Patient" #ressource type
        url:  "/path/to/Patient.ndjson.gz" | "https://your-domain/Patient.ndjson.gz"
        storageDetail:
               type: "resource-file" | "url"
      -
        type: "Encounter"
        url: "/path/to/Encounter.ndjson.gz" | "https://your-domain/Encounter.ndjson.gz" 
        storageDetail: 
               type: "resource-file" | "url"
```

Currently for beta version authorization is not supported, so the operation can be aborted by anyone who knows it's **id**. 

Only **resource-file** and **url** data types are supported. The resource file have to be entered in relative local file system paths \(for using inside the REPL\), **url** type can be used both for local files with absolute path or http urls.

If the **id** field is omitted the default UUID string will be automatically assigned. You can get this name from the response; so please careful, if you lose the response - the only way to get the automatically assigned **id** is by database access.

The response can be successful \(codes 200, 201\) or unsuccessful \(other\).

General errors:

1. If the user is not authorised to **abort \| resume \| pause** a session, status **403 Forbidden** is returned**.** _This is not yet supported._
2. If the request is not well formed status **400 \(Bad request\)** is returned. _This is not yet supported._
3. In the case of internal error **status 500** is returned.

Other error codes depend on the current session state. Session states cannot be directly controlled by the user and listed here for explanatory purposes. Currently the following states for import session are supported: 

1. **active.** The import session is started and still running.
2. **paused**. The session was started and then user-paused.
3. **aborted.** The session was started and then user-aborted.
4. **error**. The session was started but got error during the execution. The error can be caused by server itself, incorrect user input or external conditions beyond the direct control.
5. **done.** The session was started and successfully completed.

Responses on different actions.

1. start
   1. If the session id is supplied and exists in DB in any state - **status 409 \(Conflict\)** is returned.
   2. If the session id is not supplied - it will be auto generated and returned in response with status 201.
   3. The newly created resource url is given in **content-location** field in response headers field.
2. abort
   1. If the session id exists in DB in states **active \| paused** - **status 200**  is returned and session goes to the aborted state. _Currently you cannot abort paused session, if you try status 500 is returned._
   2. If the session id exists in DB in states **aborted \| error \| done** - **status 406 Not Acceptable** is returned and session doesn’t change the state.
   3. If the session id does not exist  - **status 404 \(Not found\)** is returned.
3. resume
   1. If the session id exists in DB in state **paused** - **status 200** is returned and session goes to the active state.
   2. If the session id exists in DB in states **active \| aborted \| error \| done - status 406 Not Acceptable**  is returned and session doesn’t change the state
   3. If the session id does not exist  - **status 404 \(Not found\)** is returned.
4. force-resume. Try this action to restore session from any state but **done \| aborted.** In some cases the _unpredicted behaviour_ can occur. Use this option to try to restore from ungraceful termination. If succeeded the session goes to the active state and restores from the latest possible state.
5. pause
   1. If the session id exists in DB in state active - **status 200** is returned and session goes to the paused state.
   2. If the session id exists in DB in state **paused \| aborted \| error \| done** - **status 406 Not Acceptable** is returned and session doesn’t change the state.
   3. If the session id does not exist  - **status 404 \(Not found\)** is returned.
6. In all the unsuccessful responses possible explanation is given in the **message** field.

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

