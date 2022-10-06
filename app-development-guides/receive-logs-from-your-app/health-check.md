---
description: Standard endpoint for health check
---

# Health Check

Aidbox serves REST endpoint `GET /health` for automated health-checks as described by [RFC](https://inadarei.github.io/rfc-healthcheck/) (Health Check Response Format for HTTP APIs):

{% code title="Request" %}
```yaml
GET /health
```
{% endcode %}

Response

{% code title="Response" lineNumbers="true" %}
```yaml
status: pass
description: health of aidbox
about:
  version: '2210'
  channel: edge
  commit: 444950a51
  zen-fhir-version: 0.5.21-14
  timestamp: '2022-10-06T13:43:45Z'
checks:
  uptime:
    - componentType: system
      observedValue: 1316
      observedUnit: s
      status: pass
      time: '2022-10-06T14:00:50.886Z'
  memory:utilization:
    - componentType: system
      status: pass
      observedValue: 176
      observedUnit: Mb
  db:responseTime:
    - status: pass
      componentType: database
      observedValue: 2
      observedUnit: msM
```
{% endcode %}
