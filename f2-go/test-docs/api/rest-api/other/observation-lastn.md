# Observation/$lastn

### Observation/$lastn

Implementation of [standard FHIR endpoint](https://www.hl7.org/fhir/observation-operations.html#10.1.20.2) to fetch last N observations of every available type. NB this endpoint accepts Obervations's search parameters in query string, so you can specify search criteria for Observations:

```text
GET /fhir/Observation/$lastn?subject=Patient/123&max=5
```

Example above will return last 5 observations of every available type belonging to `Patient/123`

```text
GET /fhir/Observation/$lastn?subject=Patient/123&code=code-1,code-2,code-3
```

Example above will limit types of observations returned to `code-1`, `code-2` and `code-3`.

