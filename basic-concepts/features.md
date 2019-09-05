---
description: Overview of Aidbox features
---

# Features

### REST \(**FHIR\) API** 

FHIR-compliant RESTful API that allows secure access and manipulation of clinical, financial and administrative healthcare data. Aidbox supports all major versions of FHIR: DSTU2, STU3, and R4. Strict validation ensures data consistency and integrity for all FHIR resources. A lot of valuable extensions  to FHIR API. 

### **PostgreSQL storage**

Aidbox uses exclusively PostgreSQL, but squeeze everything from this technology.  Most of aidbox flexibility and performance coming from advanced PostgreSQL features like binary json, rich indexing system etc. SQL is Second Aidbox API, which gives you extra power on your structured data. Read more about [our database internals](database.md).

### **Custom resources** 

Not all healthcare data fits the FHIR data models. Aidbox allows adding custom resources and attributes by an easy update of metadata over RESTful API.

### **Terminology server** 

Aidbox has efficient built-in Terminology Service. Included popular medical coding systems \(such as FHIR, ICD10, SNOMED, RxNorm, NPI\) and custom dictionaries \(ValueSets\).

### **Authentication & Access Control** 

Aidbox has built-in OAuth 2.0  OpenID Connect server or can work as Resource Server. Flexible security rules allow granular access control to healthcare application data.

### **Subscriptions** 

With the subscriptions mechanism, you can execute custom logic in your application when specific data is changing.

### **Cloud Infrastructure** 

Multi-tenant installation to a Kubernetes cluster in any public or private cloud. Aidbox handles terabytes of your healthcare data without hiccups.

### **Add-ons** 

Aidbox offers a rich ecosystem of add-ons which extend Aidbox by adding new resources and operations for specific customer needs \(e.g. Stripe integration, SMS notifications, X12 format parsing, etc.\)

### **SDK** 

Integrates quickly and easily with an SDK that supports your development team's language of choice.

### **Validation** 

Strict validation ensures data consistency and integrity for all FHIR resources.

