# $everything on Patient

This operation is used to return all the information related to one or more patients described in the resource or context on which this operation is invoked. The response is a bundle. Returns resources that the server has that are related to the patient\
\
Run Patient-everything:

```yaml
GET [base]/Patient/[id]/$everything
```

### Patient-everything parameters <a href="#patient-everything-parameters" id="patient-everything-parameters"></a>

| Query parameter |                                                         | Support status  |
| --------------- | ------------------------------------------------------- | --------------- |
| \_since         | Include resources updated after specified timestamp     | supported       |
| \_count         | Limits return of each resource type. Without pagination | partial support |
| \_type          | Filter result output by resource type                   | not supported   |
| start           | Filter by care date start                               | not supported   |
| end             | Filter by care date end                                 | not supported   |

## Examples

Get all resources directly linked to patient `pt-1`:

```
GET /Patient/pt-1/$everything
```

Get all resources directly linked to patient `pt-1` that were created or updated after jan 1st 2021:

```
GET /Patient/pt-1/$everything?_since=2021-01-01T00:00:00Z
```
