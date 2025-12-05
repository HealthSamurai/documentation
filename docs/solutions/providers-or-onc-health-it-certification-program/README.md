---
description: Aidbox ONC-certified FHIR API module for EHR vendors with US Core validation, SMART authorization, and bulk export.
---

# ONC Health IT Certification Program

This solution configures Aidbox as an ONC-certified FHIR API module for EHR vendors and provider organizations seeking certification under the 2015 Edition Cures Update.

## What You Get <a href="#what-you-get" id="what-you-get"></a>

* FHIR R4 API with US Core validation
* SMART on FHIR authorization (Standalone + EHR Launch)
* Patient search API
* C-CDA document generation
* Bulk EHI export to S3
* Complete audit logging

## Coverage <a href="#coverage" id="coverage"></a>

**API & Interoperability:**

* **(g)(10)** Standardized API for patient and population services
* **(g)(7)** Patient selection
* **(g)(9)** All data request (C-CDA / FHIR)
* **(b)(10)** EHI export
* **(g)(6)** C-CDA creation performance

**Privacy & Security:**

* **(d)(1)** Authentication & access control
* **(d)(2)** Auditable events
* **(d)(3)** Audit reports
* **(d)(10)** Auditing on health information
* **(d)(12)** Encrypt authentication credentials
* **(d)(13)** Multi-factor authentication

### How It Works <a href="#how-it-works" id="how-it-works"></a>

Your EHR Database → ETL → Aidbox Server → Certified FHIR API

Developers register SMART apps in Developer Sandbox. You approve them in Admin Portal. Patients launch approved apps from App Gallery. Apps call FHIR API with patient token. Aidbox validates scope and returns US Core data.

### Next Steps <a href="#next-steps" id="next-steps"></a>

1. Deployment Profile – Configure Aidbox for ONC
2. [API Guide](https://www.perplexity.ai/search/api-guide) – Map criteria to endpoints
3. [Pass Inferno Tests](https://www.perplexity.ai/search/inferno) – Run certification test suite
