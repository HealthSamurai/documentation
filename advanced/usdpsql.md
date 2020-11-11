# SQL endpoints

{% api-method method="get" host="<base-url>/$sql" path="" %}
{% api-method-summary %}
$sql
{% endapi-method-summary %}

{% api-method-description %}
Execute SQL in Aidbox
{% endapi-method-description %}

{% api-method-spec %}
{% api-method-request %}
{% api-method-body-parameters %}
{% api-method-parameter name="body" type="array" required=false %}
JSON: SQL string or jdbc friendly array  \[SQL, param, param\]
{% endapi-method-parameter %}
{% endapi-method-body-parameters %}
{% endapi-method-request %}

{% api-method-response %}
{% api-method-response-example httpCode=200 %}
{% api-method-response-example-description %}

{% endapi-method-response-example-description %}

```

```
{% endapi-method-response-example %}
{% endapi-method-response %}
{% endapi-method-spec %}
{% endapi-method %}

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

`POST /db/migrations` accepts array of `{id,sql}` objects,  if migration with such id wasn't executed - execute it;  Execution will be stopped on first exception. Operation returns only freshly executed migrations, which mean if there is no pending migrations you will get empty array in response body.

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

For your application you can keep `migrations.yaml` file under source control. Add new migrations to the end of this file when this is required. With each deployment you can ensure migrations are applied on your server using simple script like this:

```bash
curl -X POST \
  --data-binary @migrations.yaml \
  -H "Content-type: text/yaml" \
  -u $client_id:$client_secret \
  $box_url/db/migrations
```

By `GET /db/migrations`  you can introspect, which migrations were already applied on server:

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

