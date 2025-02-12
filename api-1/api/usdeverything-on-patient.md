# $everything on Patient

This operation returns all the information related to one or more patients described in the resource or context on which this operation is invoked. The response is a bundle. Returns resources that the server has that are related to the patient\
\
Run Patient-everything:

```yaml
GET [base]/Patient/[id]/$everything
```

## Patient-everything supported parameters <a href="#patient-everything-parameters" id="patient-everything-parameters"></a>

<table><thead><tr><th width="212.70358306188928">Query parameter</th><th></th></tr></thead><tbody><tr><td><strong>_since</strong></td><td>Include resources updated after the specified timestamp.</td></tr><tr><td><a href="search-1/search-parameters-list/_timeout.md"><strong>_timeout</strong></a></td><td>Set timeout for every internal search. </td></tr><tr><td><a href="search-1/search-parameters-list/_count-and-_page.md"><strong>_count</strong></a></td><td>Limits return of each resource type. Partial support without pagination.</td></tr></tbody></table>

## Examples

Get all resources directly linked to the patient `pt-1`:

```
GET /Patient/pt-1/$everything
```

Get all resources directly linked to the patient `pt-1` that were created or updated after Jan 1st 2021:

```
GET /Patient/pt-1/$everything?_since=2021-01-01T00:00:00Z
```

## Performance note

`$everything` is a heavy operation that performs several SQL requests. If one of the request's duration is too long, the operation will fail due to timeout.

This can be fixed in two ways:

* Add `_timeout=<n-seconds>` search parameter;
* Optimize the request by adding an index.

Here's the list of search parameters that Aidbox uses to find everything about the patient.

```sql
select resource->'resource' from compartmentdefinition where id = 'patient'
```

For example, we suspect, that $everything operation is slow because we have a lot of Observations. The internal search for Observations that refer Patient is:

```
- code: Observation
  param:
    - subject
    - performer
```

It will be transformed into:

```
GET /fhir/Observation?_filter=(subject eq Patient/pt1) or (performer eq Patient/pt1)
```

To debug this, we can query using [\_explain](search-1/search-parameters-list/_explain.md) search parameter:

```
GET /fhir/Observation?_filter=(subject eq Patient/pt1) or (performer eq Patient/pt1)&_explain=1
```

And now we see the SQL and the PostgreSQL plan:

```
query:
  - >-
    SELECT "observation".* FROM "observation" WHERE (("observation".resource @>
    ?) OR ("observation".resource @> ?)) LIMIT ? OFFSET ? 
  - '{"subject":{"id":"Patient"}}'
  - '{"performer":[{"id":"Patient"}]}'
  - 100
  - 0
plan: >-
  <slow plan here>
  Planning Time: 0.045 ms
  Execution Time: 130.015 ms
```

The next step is to optimize the request.&#x20;

See:

{% content-ref url="../../deployment-and-maintenance/indexes/" %}
[indexes](../../deployment-and-maintenance/indexes/)
{% endcontent-ref %}

{% content-ref url="../../deployment-and-maintenance/indexes/create-indexes-manually.md" %}
[create-indexes-manually.md](../../deployment-and-maintenance/indexes/create-indexes-manually.md)
{% endcontent-ref %}

{% content-ref url="../../deployment-and-maintenance/indexes/get-suggested-indexes.md" %}
[get-suggested-indexes.md](../../deployment-and-maintenance/indexes/get-suggested-indexes.md)
{% endcontent-ref %}
