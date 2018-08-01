---
description: Aidbox REST API reference
---

# API

This API reference describes basic FHIR endpoints for resources management and retrieval, as well as some custom Aidbox extensions.

### Base URL

Aidbox API is served over HTTPS. All URLs referenced in the documentation supposed to be prefixed with `https://<your-box-name>.aidbox.app`

### Errors

Aidbox uses semantic HTTP response codes, which generally means that all `4xx` responses indicates error on client side and all `5xx` responses means server-side error.

Among with HTTP status code you'll get an [OperationOutcome](https://www.hl7.org/fhir/operationoutcome.html) resource in the response with additional error information, such as error message:

```javascript
{
  "resourceType": "OperationOutcome",
  "status": 404,
  "text": "Resource Patient/42 not found"
}
```

### Cross Origin Resource Sharing \(CORS\)

CORS is enabled by default for all Aidbox API endpoints.



