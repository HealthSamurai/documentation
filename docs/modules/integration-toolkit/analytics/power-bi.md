---
description: Export Aidbox data to Power BI
---

# Power BI

## Prerequisites

* Use `stable`, `latest` or `edge` Aidbox version
* Set up the correct Aidbox instance URL in `AIDBOX_BASE_URL` environment variable
* Set [`AIDBOX_COMPLIANCE`](../../../reference/environment-variables/optional-environment-variables.md#enable-aidbox-compliance-mode) environment variable

## Authenticate Power BI connection in Aidbox

Power BI offers two authentication modes:

### Anonymous access

This mode requires you to have your data publicly available. Power BI needs access to FHIR read operation `GET /fhir/<resourceType>` . \
\
AccessPolicy to make your data publicly available via FHIR read operation:

```yaml
id: allow-read-all-resources
resourceType: AccessPolicy
link:
  - id: FhirRead
    resourceType: Operation
engine: allow
```

### Azure Active Directory (organizational) authentication&#x20;

Configure integration with Azure Active Directory that provides organizational authentication.

* [Azure AD](../../security-and-access-control/set-up-external-identity-provider/azure-ad.md)

## Connect Power BI to Aidbox

{% embed url="https://docs.microsoft.com/en-us/power-query/connectors/fhir/fhir#connect-to-a-fhir-server-from-power-query-desktop" %}
