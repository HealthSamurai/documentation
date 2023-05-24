# $everything on Patient

This operation is used to return all the information related to one or more patients described in the resource or context on which this operation is invoked. The response is a bundle. Returns resources that the server has that are related to the patient\
\
Run Patient-everything:

```yaml
GET [base]/Patient/[id]/$everything
```

### Patient-everything parameters <a href="#patient-everything-parameters" id="patient-everything-parameters"></a>

<table><thead><tr><th width="212.70358306188928">Query parameter</th><th></th><th>Support status</th></tr></thead><tbody><tr><td>_since</td><td>Include resources updated after specified timestamp</td><td>supported</td></tr><tr><td>_count</td><td>Limits return of each resource type. Without pagination</td><td>partial support</td></tr><tr><td>_type</td><td>Filter result output by resource type</td><td>not supported</td></tr><tr><td>start</td><td>Filter by care date start</td><td>not supported</td></tr><tr><td>end</td><td>Filter by care date end</td><td>not supported</td></tr></tbody></table>

## Examples

Get all resources directly linked to patient `pt-1`:

```
GET /Patient/pt-1/$everything
```

Get all resources directly linked to patient `pt-1` that were created or updated after jan 1st 2021:

```
GET /Patient/pt-1/$everything?_since=2021-01-01T00:00:00Z
```
