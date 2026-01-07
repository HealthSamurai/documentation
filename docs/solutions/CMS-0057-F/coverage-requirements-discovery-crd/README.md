# Coverage Requirements Discovery (CRD)

## Overview

The Aidbox Coverage Requirements Discovery (CRD) implementation enables real-time communication between EHR systems and payers to determine documentation and prior authorization requirements at the point of care. Built on the [HL7 Da Vinci Coverage Requirements Discovery Implementation Guide](https://hl7.org/fhir/us/davinci-crd/), it helps providers understand payer requirements before ordering services.

When combined with Documentation Templates and Rules (DTR) and [Prior Authorization Support (PAS)](../prior-authorization-support-pas-api.md), CRD ensures that providers are informed of coverage requirements early in the workflow, reducing claim denials and improving the authorization process.

## CDS Hooks

The core of the CRD Implementation Guide is built on [CDS Hooks](https://cds-hooks.hl7.org/STU2/) — an HL7 standard that enables clinical decision support to be invoked from within a clinician's workflow. All hooks in the CMS-0057-F Aidbox module follow the same processing workflow:

1. **Receive Hook Request** — The EHR system sends a CDS Hooks request (e.g., `order-sign`, `order-select`) containing context data (patientId, encounterId, draftOrders etc.) and prefetch resources.

2. **Validate Request Structure** — The request is validated against the CDS Hooks schema to ensure all required fields are present and correctly formatted.

3. **Validate FHIR Resources** — All FHIR resources from `draftOrders` and `prefetch` are validated together using Aidbox validation. This includes resolving all references within each resource.

4. **Fetch Missing Resources** — If validation fails due to missing referenced resources, the module automatically fetches them from the `fhirServer` URL provided in the hook request using the `fhirAuthorization` token. This resolution continues recursively until all references are resolved.

5. **Persist Resources** — All validated resources are persisted to Aidbox using a transaction bundle for audit and future reference.

6. **Enrich and Proxy** — The request is enriched with resolved prefetch data and proxied to the configured decision-making service, which analyzes the clinical data and returns CDS Cards with coverage requirements, documentation needs, or prior authorization information.

## Decision Making Service

The CMS-0057-F Aidbox module (`healthsamurai/cms-0057:edge`) proxies validated CDS Hooks requests to an external decision-making service. To configure the decision service URL, set the following environment variable:

```
CDS_DECISION_SERVICE_URL=https://your-decision-service.example.com
```

For a complete setup example, see [Run Aidbox + CMS App locally](../getting-started/run-aidbox-+-cms-app-locally.md).

It is the responsibility of the decision-making service to analyze the clinical data and build a valid CDS Hooks response containing appropriate Cards with coverage requirements, prior authorization information, or links to SMART apps for additional documentation gathering.

{% content-ref url="cds-hooks-api.md" %}
[CDS Hooks API](cds-hooks-api.md)
{% endcontent-ref %}