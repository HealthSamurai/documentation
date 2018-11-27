---
description: History operation
---

# History

## Overview

The history operation retrieves the history of either a particular resource, all resources of a given type, or all resources supported by the system. The operation is performed by HTTP `GET` command. See the FHIR documentation [http://hl7.org/fhir/http.html\#history](http://hl7.org/fhir/http.html#history) for more details.

| Operation Scope | Request | Aidbox |
| :--- | :--- | :--- |
| Specific Resource | `GET /fhir/<RESOURCE_TYPE>/<ID>/_history{?[parameters]&_format=[mime-type]}` | `Supported` |
| Resource Type | `GET /fhir/<RESOURCE_TYPE>/_history{?[parameters]&_format=[mime-type]}` | `Supported` |
| All Resources | `GET /fhir/_history{?[parameters]&_format=[mime-type]}` | `Not Supported` |



