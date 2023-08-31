# Defining flat views with View Definitions

## Creating flat views

### With Aidbox UI

To create a flat view of the resource, you have to define it with a special resource called ViewDefinition. You can do it with a View Definitions editor in Aidbox UI.

<figure><img src="../../.gitbook/assets/image (97).png" alt=""><figcaption><p>View Definitions editor</p></figcaption></figure>

View definitions you've created can be viewed in a menu in the right part of the screen. There also are several samples to get you started. Note that samples are presented as View Definitions only and have no corresponding views in the database by default.

You can use the _Run_ button or `Ctrl+Enter` to preview your view. To save a View Definition and materialize it as a view in the database, press _Save_. _Delete_ button deletes both a View Definition and the corresponding view in the database.

Note that a ViewDefinition resource used in Aidbox does not have the same structure as the one described by the [SQL on FHIR specification](https://build.fhir.org/ig/FHIR/sql-on-fhir-v2/StructureDefinition-ViewDefinition.html). This may change in the future.

### With REST API

As ViewDefinition is a resource, it can be created via REST API. For example:

{% tabs %}
{% tab title="Request" %}
```yaml
PUT /ViewDefinition/patient_view

name: patient_view
resource: '{"name":"patient_view","resource":"Patient","desc":"Patient flat view","select":[{"name":"id","expr":"id"},{"name":"bod","expr":"birthDate"},{"name":"gender","expr":"gender"}]}'
```
{% endtab %}

{% tab title="Response" %}
```yaml
name: patient_view
resource: >-
  {"name":"patient_view","resource":"Patient","desc":"Patient flat
  view","select":[{"name":"id","expr":"id"},{"name":"bod","expr":"birthDate"},{"name":"gender","expr":"gender"}]}
id: >-
  patient_view
resourceType: ViewDefinition
```
{% endtab %}
{% endtabs %}

Be aware that this step will only create a ViewDefinition, but not the corresponding flat view. You can materialize your View Definition via Aidbox UI as shown above (even if you've created it with REST API) or use a `sof/materialize-view` RPC call as follows:

{% tabs %}
{% tab title="Request" %}
```yaml
POST /rpc

method: sof/materialize-view
params:
  view:
    name: patient_view
    resource: Patient
    desc: Patient flat view
    select:
    - name: id
      expr: id
    - name: name
      expr: name
    - name: bod
      expr: birthDate
    - name: gender
      expr: gender
    limit: 100
```
{% endtab %}

{% tab title="Response" %}
```
result:
  status: ok
  sql: >-
    SELECT id as id , jsonb_path_query_first( r.resource , '$  . name' ) #>>
    '{}' as name , jsonb_path_query_first( r.resource , '$  . birthDate' ) #>>
    '{}' as bod , jsonb_path_query_first( r.resource , '$  . gender' ) #>> '{}'
    as gender FROM "patient" as r LIMIT 100
  data:
    - id: pt1
      name: '[{"given": ["John"], "family": "Smith"}]'
      bod: '1976-12-12'
      gender: male
    - id: pt2
      name: '[{"given": ["Ella El"], "family": "Smith"}]'
      bod: '1998-11-22'
      gender: female
    - id: pt3
      name: '[{"given": ["Frodo"], "family": "Baggins"}]'
      bod: '1954-09-22'
      gender: male
    - id: pt4
      name: '[{"given": ["Kot"], "family": "Terex"}]'
      bod: '1978-02-29'
      gender: male
```
{% endtab %}
{% endtabs %}

Note that it's not necessary to create a ViewDefinition resource to create a flat view with this RPC call. It's not advised though, as ViewDefinition provides more control over your flat views.
