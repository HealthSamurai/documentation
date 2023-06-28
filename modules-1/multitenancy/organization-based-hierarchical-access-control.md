---
description: Achieve logical multi-tenancy with Aidbox
---

# Organization-based hierarchical access control

Hierarchical organization-based access control in Aidbox allows for the restriction of access to data based on the organization (tenant) to which it belongs. When this feature is enabled, the FHIR Organization resource in Aidbox gains new semantics and functionality.

When an Organization resource is created in Aidbox, a dedicated FHIR API is automatically activated for that organization. This API provides access to all resources associated with the organization through the Organizational FHIR API.

This means that when users interact with the Organizational FHIR API, they will only be able to access the resources that belong to their organization or tenant. The hierarchical organization-based access control ensures that data is logically isolated and accessible only within the appropriate organizational context.

<figure><img src="../../.gitbook/assets/Screenshot 2023-06-28 at 15.42.54.png" alt=""><figcaption><p>FHIR APIs &#x26; data space reflection in organizational-based access control</p></figcaption></figure>

