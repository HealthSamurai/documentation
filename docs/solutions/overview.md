# Overview

Aidbox provides pre-configured solution profiles to meet specific regulatory requirements. This section focuses on the **ONC Certified API** solution for EHRs.

#### Available Profiles

| Solution                    | Target Audience | Key Regulations                            |
| --------------------------- | --------------- | ------------------------------------------ |
| **ONC Certified API (EHR)** | EHR Vendors     | ONC Health IT Certification (Cures Update) |
| _\[CMS Interoperability]_   | _Health Plans_  | _CMS-0057-F (Coming soon)_                 |

## Aidbox + FHIR App Portal

The Aidbox Platform consists of two core components that work together to deliver a certified API solution:

### 1. Aidbox FHIR Server

The backend engine that provides:

* **FHIR R4 Store:** High-performance storage on PostgreSQL (JSONB).
* **Validation:** Strict conformance to US Core and other IGs.
* **Security:** OAuth 2.0 provider, SMART App Launch v1 and v2,  user management, audit logging,

### 2. FHIR App Portal <a href="#id-2-fhir-app-portal" id="id-2-fhir-app-portal"></a>

A unified UI suite managing the ecosystem workflow:

* **Developer Sandbox (Public):** Self-service app registration & testing.
* **Admin Portal (Internal):** App approval, portal configuration, metrics reporting
* **App Gallery (Patient-facing):** App discovery, SMART Launch, & consent management.
