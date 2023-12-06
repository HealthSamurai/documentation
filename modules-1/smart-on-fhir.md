---
description: Get to know, what SMART on FHIR features are supported in Aidbox
---

# SMART on FHIR

The SMART App Launch Framework connects third-party applications to Electronic Health Record data, allowing apps to launch from inside or outside the user interface of an EHR system. The framework supports apps for use by clinicians, patients, and others via a PHR or Patient Portal or any FHIR system where a user can give permissions to launch an app.

Aidbox supports SMART on FHIR. You can find support status in this guide.

### **Uses case**

1. Patients apps that launch standalone (Patient Access for Standalone Apps)\
   Read [how to enable SMART on FHIR on Patient Access API](patient-data-access-api/smart-on-fhir.md) guide.
2. Patient apps that launch from a portal (Patient Access for EHR Launch)
3. **\[WIP]** Provider apps that launch standalone (Clinician Access for Standalone)
4. **\[WIP]** Provider apps that launch from a portal (Clinician Access for EHR Launch)

### **Launch sequences**

1. Standalone launch
2. EHR launch (Portal)

### **Authorization flows**

1. authorization code for smart apps
2. **\[WIP]** client\_credentials for pre-authorized backend services

### **Client Authentication**

1. Symmetric (client secret)
2. **\[WIP]** Asymmetric (JWT)

## **Scopes**

### User Indentity

<table><thead><tr><th width="110">status</th><th width="247">scope</th><th>description</th></tr></thead><tbody><tr><td>Active</td><td><code>openid</code> <code>fhirUser</code></td><td>This pair of scopes permits the client to request details about the logged in user. They enable the OpenID Connect <code>id_token</code> claim. When the <code>fhirUser</code> scope is used, the ID Token will contain a claim (also called <code>fhirUser</code>) that contains a link to the FHIR Resource accociated with the logged in user.</td></tr><tr><td>Active</td><td><code>openid</code> <code>profile</code></td><td>This pair of scopes permits the client to request details about the logged in user. They enable the OpenID Connect <code>id_token</code> claim. Using the <code>profile</code> scope is an alternative to the <code>fhirUser</code> scope discussed above, and is supported by Smile CDR, but it is deprecated in the SMART App Launch specification.</td></tr></tbody></table>

### Refresh Tokens

| status | scope            | description                                                                                                                                                                                                                                        |
| ------ | ---------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| WIP    | `online_access`  | This scope permits that the client be issued a Refresh Token upon authentication, and permits the user to exchange the Refresh Token for an Access Token. This scope has no effect for clients that do not support the `refresh_token` grant type. |
| Active | `offline_access` | This scope permits that the client be issued a Refresh Token upon authentication, and permits the user to exchange the Refresh Token for an Access Token. This scope has no effect for clients that do not support the `refresh_token` grant type. |

### Patient-specific scopes

<table><thead><tr><th width="163">status</th><th width="320">scope</th><th>description</th></tr></thead><tbody><tr><td>Active</td><td><code>patient/[resourceType].read</code></td><td>This scope permits the client to read (read/search) all data for the given resource type for all patients on the server. Note that [resourceType] must be a valid FHIR Resource type (e.g. <code>Observation</code>).<br><br>Smart on FHIR v1.</td></tr><tr><td>WIP</td><td><code>patient/[resourceType].write</code></td><td>This scope permits the client to write (create/update) all data for the given resource type for all patients on the server. Note that [resourceType] must be a valid FHIR Resource type (e.g. <code>Observation</code>).<br><br>Smart on FHIR v1.</td></tr><tr><td>WIP</td><td><code>patient/[resourceType].c</code></td><td>This scope permits the client to create all data for the given resource type for all patients on the server. Note that [resourceType] must be a valid FHIR Resource type (e.g. <code>Observation</code>).<br><br><br>Smart on FHIR v2.</td></tr><tr><td>Active</td><td><code>patient/[resourceType].r</code></td><td>This scope permits the client to read (read) all data for the given resource type for all patients on the server. Note that [ResourceType] must be a valid FHIR Resource type (e.g. <code>Observation</code>).<br><br>Smart on FHIR v2.</td></tr><tr><td>WIP</td><td><code>patient/[resourceType].u</code></td><td>This scope permits the client to update all data for the given resource type for all patients on the server. Note that [ResourceType] must be a valid FHIR Resource type (e.g. <code>Observation</code>).<br><br>Smart on FHIR v2.</td></tr><tr><td>WIP</td><td><code>patient/[resourceType].d</code></td><td>This scope permits the client to delete all data for the given resource type for all patients on the server. Note that [ResourceType] must be a valid FHIR Resource type (e.g. <code>Observation</code>).<br><br>Smart on FHIR v2.</td></tr><tr><td>Active</td><td><code>patient/[resourceType].s</code></td><td>This scope permits the client to search all data for the given resource type for all patients on the server. Note that [ResourceType] must be a valid FHIR Resource type (e.g. <code>Observation</code>).<br><br>Smart on FHIR v2.</td></tr></tbody></table>

#### **Finer-grained resource constraints using search parameters**

Aidbox supports scope restriction via query filters for patient-specific search scope (`patient/Encounter.s`). Aidbox transforms FHIR search parameters for search over resource types into query filter.

Example:

FHIR spec supports search parameter `class` for Encounter search operation.

`GET [fhir-base-url]/Encounter?class=AMB`

And these search parameter can be used in scope definition

`patient/Encounter.s?class=AMB`

### User-level scopes

WIP

### System-level scopes

WIP

## Talk to a Health Samurai Engineer

If you'd like to learn more about using Aidbox or have any questions about this guide, [connect with us on Telegram](https://t.me/aidbox). We're happy to help.

\
