---
description: Standard endpoint for health checks
---

# Health Check

Aidbox serves REST health check endpoint `GET /health` to configure liveness, readiness and startup probes. The response format is described by [RFC](https://inadarei.github.io/rfc-healthcheck/) (Health Check Response Format for HTTP APIs):

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

## Logging /health endpoint request

By default `/health` endpoint requests are logged. To disable it use `BOX_LOGGING_DISABLE__HEALTH__LOGS` ENV variable.
