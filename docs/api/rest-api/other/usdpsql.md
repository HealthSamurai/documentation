# SQL endpoints

{% swagger baseUrl="<base-url>/$sql" path="" method="get" summary="$sql" %}
{% swagger-description %}
Execute SQL in Aidbox
{% endswagger-description %}

{% swagger-parameter in="body" name="body" type="array" %}
JSON: SQL string or jdbc friendly array  [SQL, param, param]
{% endswagger-parameter %}

{% swagger-response status="200" description="" %}
```
```
{% endswagger-response %}
{% endswagger %}

Example request:

{% tabs %}
{% tab title="Without jdbc params" %}
```yaml
POST /$sql?_format=yaml

SELECT count(*) FROM patient

# Response 
#
# - {count: 7}
```
{% endtab %}

{% tab title="With jdbc params" %}
```
POST /$sql?_format=yaml

["SELECT count(*) FROM patient where resource->'status' = ?", true]

# Response 
#
# - {count: 2}
```
{% endtab %}
{% endtabs %}

### SQL migrations

Aidbox provides `POST and GET /db/migrations` operations to enable SQL migrations, which can be used to migrate/transform data, create helper functions, views etc.

`POST /db/migrations` accepts array of `{id,sql}` objects. If the migration with such id wasn't executed, execute it. Execution will be stopped on the first exception. This operation returns only freshly executed migrations. It means that if there are no pending migrations, you will get an empty array in the response body.

```yaml
POST /db/migrations

- id: remove-extensions-from-patients
  sql: |
    update patient set resource = resource - 'extension'
- id: create-policy-helper
  sql: |
    create function patient_for_user(u jsonb) returns jsonb 
    as $$
        select resource || jsonb_build_object('id', id)
           from patient
           where id = u#>>'{data,patient_id}'
    $$ language sql

-- first run response
- id: remove-extensions-from-patients
  sql: ...
- id: create-policy-helper
  sql: ...
  
-- second run response
[]
```

For your application you can keep `migrations.yaml` file under source control. Add new migrations to the end of this file when this is required. With each deployment you can ensure migrations are applied on your server using a simple script like this:

```bash
curl -X POST \
  --data-binary @migrations.yaml \
  -H "Content-type: text/yaml" \
  -u $client_id:$client_secret \
  $box_url/db/migrations
```

By `GET /db/migrations`  you can introspect which migrations were already applied on the server:

```yaml
GET /db/migrations

-- resp
- id: remove-extensions-from-patients
  ts: <timestamp>
  sql: ...
- id: create-policy-helper
  ts: <timestamp>
  sql: ...

```
