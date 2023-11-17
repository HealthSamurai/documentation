# Defining flat views with View Definitions

SQL on FHIR utilizes ViewDefinition resources to describe the structure of flat views. A simple ViewDefinition may look like this:

```json
{
    "name": "obs_view",
    "resource": "Observation",
    "status": "active",
    "select": [{
        "alias": "id",
        "path": "id",
    }, {
        "alias": "pid",
        "path": "subject.getId('Patient')"
    }]
}
```

The view is described with its name, resource type, status and the declaration of the rows it contains. In this example, we define a flat view named `obs_view` for Observations, that will have 2 fields: `id`, defined as the Observation's own id, and `pid`, defined as the subject Patient's id.

This example only scratches the surface. For more complex examples, see [query-data-above-view-definitions.md](query-data-above-view-definitions.md "mention"). For an in-depth overview of a ViewDefinition's structure consult [reference.md](reference.md "mention").

## Creating flat views

### With Aidbox UI

To create a flat view of the resource, you have to define it with a special resource called ViewDefinition. You can do it with a View Definitions editor in Aidbox UI.

<figure><img src="../../.gitbook/assets/2023-09-25-151846.png" alt=""><figcaption><p>View Definitions editor</p></figcaption></figure>

View definitions you've created can be viewed in a menu in the right part of the screen. There also are several samples to get you started. Note that samples are presented as View Definitions only and have no corresponding views in the database by default.

You can use the _Run_ button or `Ctrl+Enter` to preview your view. To save a View Definition and materialize it as a view in the database, press _Save_. _Delete_ button deletes both a View Definition and the corresponding view in the database.

Note that a ViewDefinition resource used in Aidbox may difer from [SQL on FHIR specification](https://build.fhir.org/ig/FHIR/sql-on-fhir-v2/StructureDefinition-ViewDefinition.html). To see how Aidbox's ViewDefinition is structured, consult the [reference.md](reference.md "mention") page.

### With REST API

As ViewDefinition is a resource, it can be created via REST API. For example:

{% tabs %}
{% tab title="Request" %}
```yaml
POST /ViewDefinition/

name: patient_view
resource: Patient
description: Patient flat view
status: draft
select:
- alias: id
  path: id
- alias: bod
  path: birthDate
- alias: gender
  path: gender

```
{% endtab %}

{% tab title="Response" %}
```yaml
name: patient_view
select:
  - path: id
    alias: id
  - path: birthDate
    alias: bod
  - path: gender
    alias: gender
status: draft
resource: Patient
description: Patient flat view
id: >-
  30d1cf2a-6610-4887-9fc2-fbd425837d4e
resourceType: ViewDefinition
```
{% endtab %}
{% endtabs %}

This step will create both a ViewDefinition resource and the corresponding flat view. `PUT` and `DELETE` operations will also affect both ViewDefinitions and their flat views.

## Additional options

By default views you define are materialized as views in `sof` schema. You can alter this behavior by adding an extension.

```yaml
extension:
  - url: https://fhir.aidbox.app/fhir/Extension/view-definition
    extension:
      - url: materialization
        value:
          code: table
      - url: schema
        value:
          string: public
```

In the example above, view will be materialized as a table in a schema called `public`. Available materialization options are `table`, `materialized-view` and `view`.

If you create views via Aidbox UI, you can use the shorthand parameters `_materialization` and `_schema` instead, like this:

```json
{
    "_materialization": "table",
    "_schema": "public"
}
```

Note that these shorthands are not available for REST API.
