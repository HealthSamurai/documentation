---
description: Standard endpoint for health check
---

# Health Check

Aidbox serves REST endpoint `GET /health` for automated health-checks as described by [RFC](https://inadarei.github.io/rfc-healthcheck/) \(Health Check Response Format for HTTP APIs\):

{% code title="health-check-request" %}
```yaml
GET /health

#response 

status: pass
description: health of aidbox
version: 0.4.2
releaseId: ????
checks:
  uptime:
  - {componentType: system, 
     observedValue: 87, 
     observedUnit: s, 
     status: pass, 
     time: '2019-04-18T19:33:16.194Z'}
  memory:utilization:
  - {componentType: system, 
     status: pass, 
     observedValue: 205, 
     observedUnit: Mb}
  db:responseTime:
  - {status: pass, 
     componentType: database, 
     observedValue: 2, 
     observedUnit: ms}
```
{% endcode %}



