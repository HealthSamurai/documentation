---
title: "Aidbox HIPAA book / Part 1 / Technical safeguards"
slug: "aidbox-hipaa-book-technical-safeguards"
published: "2021-10-25"
author: "Mike K."
reading-time: "9 min read"
tags: ["hipaa", "aidbox", "compliance"]
category: "Compliance"
teaser: "Spoiler alert: HIPAA is easy when you are using Aidbox. The Health Insurance Portability and Accountability Act (HIPAA) sets standards in the United States for protecting individually identifiable health information. "
image: "cover.png"
---

The Health Insurance Portability and Accountability Act (HIPAA) sets standards in the United States for protecting individually identifiable health information. HIPAA applies to “covered entities” which include: health plans, healthcare providers, healthcare clearinghouses, and organizations that perform certain functions on their behalf, known as “business associates”. For example, a medical office is a covered entity, and a vendor of the EHR system that is used by the physicians in that office is a business associate.  
  
The individually identifiable health information **managed by covered entities and business associates** is called protected health information or PHI. It is important to understand that not all identifiable health information is PHI. For instance, if you are developing software that receives health information directly from patients, your organization and software are not regulated by HIPAA if no covered entity is involved.  
  
The HIPAA Security Rule requires covered entities and their business associates to protect the confidentiality, integrity, and availability of PHI with administrative, physical, and technical safeguards. [Aidbox FHIR backend](https://www.health-samurai.io/new-aidbox) and its automated infrastructure have many built-in components that address technical and administrative [HIPAA safeguards](https://www.health-samurai.io/articles/aidbox-hipaa-book-technical-safeguards).  
  
Let’s talk about how Aidbox-powered healthcare systems stay on track with HIPAA compliance.  
  
***Spoiler alert: HIPAA is easy when you are using Aidbox.***

## HIPAA technical safeguards

### **STANDARD § 164.312(a)(1) Access Control**

The Access Control standard requires you to implement technical policies and procedures for your solutions that maintain ePHI to allow access only to those persons or software that have been granted access rights.  
  
Four implementation specifications are associated with the Access Control standard.  
*1. Unique User Identification (Required)   
2. Emergency Access Procedure (Required)   
3. Automatic Logoff (Addressable)   
4. Encryption and Decryption (Addressable)  
*

1. UNIQUE USER IDENTIFICATION (R) - § 164.312(a)(2)(i) - **required**

> Assign a unique name and/or number for identifying and tracking user identity.

This specification requires healthcare IT solutions to assign a unique identifier for each user in the system. All the user activity including sessions, API calls, etc. should be tagged with this identifier. This implementation model gives you transparency and traceability and allows you to track specific user activity and hold users accountable for functions performed on information systems with ePHI.  
  
*HIPAA doesn’t define technical requirements for user identification formats so the implementers' approaches may differ.*   
  
The Aidbox [user management and access control module](https://www.health-samurai.io/docs/aidbox/access-control/access-control) uses UUIDs for user identification. Every single user gets a unique ID during the process of registration. Using this ID makes it possible to manage permissions granularly. These features are available in Aidbox with User, AccessPolicy, and Role resources.

For established organizations with complex IT infrastructures, Aidbox allows the use of [federated identity mechanisms](https://www.health-samurai.io/docs/aidbox/access-control/access-control) to manage and map user identities trusted across multiple IT systems or even organizations. In this scenario, a unique UUID is still assigned for Aidbox to identify a user.

  
2. EMERGENCY ACCESS PROCEDURE (R) - § 164.312(a)(2)(ii) - **required**

> Establish (and implement as needed) procedures for obtaining necessary electronic protected health information during an emergency.

This implementation specification mandates establishing (and implementing as needed) procedures for obtaining necessary ePHI during an emergency. When an incident that compromises system availability occurs you should have documented instructions and operational practices in place that will allow you to obtain access to the necessary ePHI. This includes not only tech safeguards but also an administrative process that gives users the privileges necessary to access ePHI under emergency conditions if manual intervention is unavoidable.  
  
The Aidbox emergency access procedure leverages the backup and replication mechanisms of the Aidbox automated database infrastructure that ensure data integrity. These measures minimize the possibility of data loss in case of emergency and provide the ability to restore to the most recent uncorrupted ePHI.

For instance, Aidbox infrastructure employs an active-passive database architecture where a passive node replicates all the changes on an active one in real time. With the right configuration, the delay is minimized to several minutes decreasing the possibility of data loss in case of emergency. This approach also ensures high availability because the passive node is promoted to active in case the active node becomes unavailable.  
  
The Aidbox [access control](https://www.health-samurai.io/docs/aidbox/access-control/access-control) module supports the creation of emergency access policies that will allow necessary access to ePHI for identified users. Administrators can identify users that require emergency access and configure emergency access policies to retrieve the necessary ePHI from a restored environment in case of emergency.

  
3. AUTOMATIC LOGOFF (A) - § 164.312(a)(2)(iii) - **addressable**

> Implement electronic procedures that terminate an electronic session after a predetermined time of inactivity.

This specification is a reasonable safeguard but it is not required and will not break your compliance with HIPAA if you decide not to implement it for any reason. Any user who logs into the system should have a default session timeout. The session length can be determined depending on the user role, info sensitivity etc. If a user is inactive during this period, the user should authenticate before continuing to use services. 

The Aidbox [access control](https://www.health-samurai.io/docs/aidbox/access-control/access-control) module lets you set an expiration time for a user’s JWT session token. An expiration time can also be configured for API clients. Multiple client resources may be configured for user roles that require different session lengths.

4. ENCRYPTION AND DECRYPTION (A) - § 164.312(a)(2)(iv) - **addressable**

> Implement a mechanism to encrypt and decrypt electronic protected health information.

The ePHI data should be encrypted to decrease the possibility of unauthorized access. This can be applied not only to database storage but also to backups and activity logs to prevent unauthorized access and ePHI exposure.  
  
[Aidbox](https://www.health-samurai.io/new-aidbox)'s automated infrastructure uses encrypted partitions for data at rest by default. Data is encrypted/decrypted automatically during the database I/O using the keys issued by the cloud provider. Encryption keys are rotated by a cloud provider, and the rotation period is also configurable. Cloud provider IAM ([AWS](https://aws.amazon.com/iam/?nc1=h_ls), [Azure](https://azure.microsoft.com/en-us/product-categories/identity/) and [Google](https://cloud.google.com/iam)) is used to prevent unauthorized access to the underlying cloud infrastructure.

### STANDARD § 164.312(b) Audit Controls

> Implement hardware, software, and/or procedural mechanisms that record and examine activity in information systems that contain or use electronic protected health information.

Audit control requires a system to have audit capabilities to hold users accountable for operations performed using ePHI. An audit log should be captured for all software components used in HIPAA-compliant services. The best up-to-date guidance for healthcare applications’ audit logs can be found in the ASTM E2147-18 standard, “Standard Specification for Audit and Disclosure Logs for Use in Health Information Systems”, which was chosen by ONC for certified EHR technology.  
  
Aidbox [automatically logs](https://docs.aidbox.app/core-modules/logging-and-audit) all auth, API, database and network events. Log records include the timestamp of the event, type of action, user ID, request body, DB query, API client ID etc., depending on the log type.  
  
By default, Aidbox uses [ElasticSearch for log persistence](https://docs.aidbox.app/modules-1/observability/logging-and-audit/how-to-guides/elastic-logs-and-monitoring-integration), and Kibana/Grafana for log analysis and visualization. You can also configure integration with third-party monitoring and log management solutions like [Datadog](https://docs.aidbox.app/core-modules/logging-and-audit/datadog-guide), Splunk or SumoLogic. 

In addition, monitoring and logging can be also set up on the cloud provider side with solutions like Stackdriver, CloudWatch etc. With this approach, you can investigate client activity in software components that surround the Aidbox backend and database. These components include network routers, Nginx proxy servers, container orchestration engines and application runtimes.

You can find more details on logging and audit in [our documentation](https://docs.aidbox.app/core-modules/logging-and-audit).

### STANDARD § 164.312(c)(1) Integrity

> Implement policies and procedures to protect electronic protected health information from improper alteration or destruction.

The Integrity standard requires software to implement policies and procedures to protect ePHI from improper alteration or destruction. 

Aidbox infrastructure has several means of ensuring data integrity. First of all, most API operations are wrapped in PostgreSQL transactions with an isolation level of at least “read committed”. This prevents undesirable concurrent changes and allows rollback in case an error occurs during query execution.  
  
Besides, Aidbox ensures data integrity through the use of [standard PostgreSQL database replication and backup mechanisms](https://www.health-samurai.io/aidbox#Infrastructure). The backup system is configured to run backups on a given schedule (hourly, daily, nightly) and archive WAL records continuously. In case of data corruption, for example if a malicious query was executed, this approach allows you to execute point-in-time recovery i.e. restore to the most recent uncorrupted version of the data. To be able to restore from the backup as quickly as possible it is recommended to test the recovery process proactively.  
  
The backups are stored in highly available and encrypted cloud storage. Additional cloud provider storage configuration implementing multi-region redundancy is possible.  
  
Database replication and backups can be configured by the Aidbox team during project deployment.  

1. MECHANISM TO AUTHENTICATE ELECTRONIC PROTECTED HEALTH INFORMATION (A) - § 164.312(c)(2) - **addressable**

> Implement electronic mechanisms to corroborate that electronic protected health information has not been altered or destroyed in an unauthorized manner.

Aidbox allows only authorized users or services to use its APIs, thereby ensuring the avoidance of improper or accidental destruction or alteration of ePHI. The [logging and audit module](https://www.health-samurai.io/docs/aidbox/access-control/audit-and-logging) enables research to investigate all users’ activities so that any undesirable operations can be detected and reversed.

### STANDARD § 164.312(d) Person or Entity Authentication

> Implement procedures to verify that a person or entity seeking access to electronic protected health information is the one claimed.

Authentication confirms that a person who tries to access ePHI is in fact who they claim to be. This is achieved by providing proof of identity. There are a few ways to provide proof of identity for authentication including passwords, private keys, device possession, or even biometrics.

Aidbox's access control provides a secure authentication experience with password sign in and built-in password receive and confirmation flows.  
  
For enhanced security Aidbox provides [multi-factor authentication](https://docs.aidbox.app/security-and-access-control-1/auth/two-factor-authentication) (MFA) to protect user accounts from unauthorized access. You can enable 2FA for a user and configure it with AuthConfig, Client and AidboxConfig. A TOTP (time-based one-time password) is used to obtain an access token for your app.

For development purposes simple authentication methods like basic auth are also supported.

### STANDARD § 164.312(e)(1) Transmission Security

> Implement technical security measures to guard against unauthorized access to electronic protected health information that is being transmitted over an electronic communications network.

1. INTEGRITY CONTROLS (A) - § 164.312(e)(2)(i) - **addressable**

> Implement security measures to ensure that electronically transmitted electronic protected health information is not improperly modified without detection until disposed of.

[Aidbox](https://www.health-samurai.io/new-aidbox) uses HTTP over TLS for ePHI transmission and this protocol by design ensures that the data sent is the same as the data received. No additional effort is required to comply with this specification if your application uses Aidbox for all its data transmission needs.

2. ENCRYPTION (A) - § 164.312(e)(2)(ii) - **addressable**

> Implement a mechanism to encrypt electronic protected health information whenever deemed appropriate.

[Aidbox](https://www.health-samurai.io/new-aidbox) uses HTTP over TLS for ePHI transmission and this protocol encrypts all transmitted data. Aidbox's automated infrastructure comes with an SSL certificate manager that issues and updates SSL certificates automatically so that you don’t miss their expiration. In general, we use certificates provided by Let’s Encrypt but any ACME-compatible provider may be configured. Additionally, usage of the existing certificate issued by the cloud provider (Amazon’s ACM, for example) is also possible.

  
In the next chapters we will explain how Aidbox helps cover administrative HIPAA safeguards.

> Get started with the Aidbox [FHIR Server](https://www.health-samurai.io/aidbox) for data storage, integrations, healthcare analytics, and more, or [hire our team](https://www.health-samurai.io/services) to support your software development needs.

Follow US

[![Aidbox FHIR Platform Free Trial](image-1.png)](https://www.health-samurai.io/aidbox)
