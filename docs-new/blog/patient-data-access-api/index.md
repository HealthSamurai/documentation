---
title: "Patient data access API"
slug: "patient-data-access-api"
published: "2025-04-11"
author: "Rostislav Antonov"
reading-time: "4 min "
tags: []
category: "FHIR"
teaser: "We often need to access data for a specific patient while ensuring that access is limited to them. This can be done by adding a patient reference search parameter to each request."
image: "cover.png"
---

## Patient data API

We often need to access data for a specific patient while ensuring that access is limited to them. This can be done by adding a patient reference search parameter to each request, but Aidbox [FHIR server takes](https://www.health-samurai.io/aidbox) another approach: it uses SMART on FHIR scopes and patient context in the authorization token to restrict access to resources associated with the patient.

### Patient access api

To restrict access to a specific patient's data in Aidbox, the request must meet the following conditions:

- Authorization token must be valid JWT;
- This token must contain only patient-level scopes in “scope” claim;
- JWT token must contain patient ID in “context.patient” claim.

With this approach, you can be confident that you will not be able to retrieve data using the FHIR API for patients other than those specified in the context. An example of JWT token claims:

```javascript
{
...
  "atv": 2,
  "scope": "launch/patient openid fhirUser offline_access patient/*.cruds",
  "context": {
    "patient": "patient-id"
  }
...
}
```
