---
title: "HIPAA-compliant Aidbox instance synchronization "
slug: "hipaa-compliant-aidbox-instance-synchronization"
published: "2023-03-02"
author: "Paul Chayka, WaveAccess"
reading-time: "9 min read"
tags: ["data synchronization", "waveacess", "aidbox", "hipaa"]
category: "Integrations"
teaser: "Do you have offices or clinics all over the world and want to collect statistical data in a single storage? In this article, we'll share our experiences in implementing HIPAA-compliant data synchronization across multiple Aidbox instances. "
image: "cover.png"
---

Do you have offices or clinics all over the world and want to collect statistical data in a single storage? Do you also want to be 100% sure that your system is HIPAA-compliant and follows local regulator rules (like [GDPR](https://gdpr.eu/))? Or do you have multiple offices in the US and want to implement a medical data warehouse to improve performance at the local level? All of this can be achieved through medical data storage synchronization. In this article, I’d like to tell you about our experiences in implementing HIPAA-compliant data synchronization across multiple Aidbox instances.

Data synchronization between multiple systems is not new. Synchronization is used in distributed systems with multiple data storage, usually in order to implement integration with multiple systems or increase performance. In the healthcare sphere, this process is also associated with PHI protection. For example, personal patient data shouldn’t be transmitted to storage that is located in other countries.

The WaveAccess team, in partnership with Health Samurai, has developed an extension solution for Aidbox for a US client with clinics in multiple countries. This extension supports HIPAA-compliant data synchronization between multiple local Aidbox instances and a global Aidbox. The other essential part of this service is the data anonymization module that helps to protect PHI by removing all patient identifiers from data before sending it to another Aidbox instance.

Now let's look at our real-world experience in implementing this solution. We will also go over our specific technical challenges and tell you exactly how we solved these problems.

This article will be useful for CTOs, technical managers, project managers, team leaders and other technical people who are responsible for transferring data between multiple FHIR servers.

Keep reading to find out about:

- Use case description
- HIPAA compliance
- Synchronization challenges
- Synchronization module structure and workflow

## 1. Use case description

The client has a chain of clinics in multiple countries with an HQ in the US. It has several local applications for practitioners that use Aidbox instances as medical data storage, and a global application for administrative purposes with its own [Aidbox](https://www.health-samurai.io/aidbox). The global Aidbox should contain medical information from the local level for statistics, management etc. On the other hand, local Aidboxes should have consistent information about organizational structure and geopositional data contained in the global Aidbox. This means that local Aidboxes should send certain anonymized information about patients, appointments, observations, diagnoses and other connected entities to the global Aidbox and receive some specific information in turn regarding their location (such as a list of available countries, states, organizations etc).

To implement the data exchange process, we should develop a synchronization module that can configure and perform both-side synchronization with the ability to anonymize the patients’ PHI.

The high-level architecture looks like this:

![](image-1.png)

The local [Aidboxes](https://www.health-samurai.io/aidbox) collect the data from the local applications. Synchronization services receive this data, anonymize it, and transfer it to the global Aidbox. These services also manage the process of sending data from the global Aidbox to the local level.

This approach helps to protect patient PHI. The synchronization services are stored locally which means that the sensitive information is processed locally as well. After the anonymization process is fulfilled by these services, the medical data can be safely transferred to the global Aidbox.

## **2. HIPAA Compliance**

The Health Insurance Portability and Accountability Act of 1996 (HIPAA) is a federal law that requires the creation of national standards to protect sensitive patient health information from being disclosed without the patient’s consent or knowledge. This sensitive patient data is called protected health information (PHI) and includes demographic data and data that identifies individuals including name, address, date of birth and Social Security number.

How can organizations transfer, collect and process patients’ medical data? According to the [HIPAA Privacy Rule](https://www.hhs.gov/hipaa/for-professionals/privacy/index.html), there are no restrictions on the use or disclosure of de-identified (anonymized) health information. The easiest way to de-identify data is to remove all specified identifiers and other information that could be used to identify a patient.

This means that a HIPAA-compliant solution should have an anonymization functionality, which removes all patient identifiers from the medical data before transferring it to other storage.

If you want to know more about HIPAA, you can find more detailed information on the [U.S. Department of Health & Human Services site](https://www.hhs.gov/hipaa/index.html) or read our article [Aidbox HIPAA book / Part 1 / Technical safeguards](https://www.health-samurai.io/articles/aidbox-hipaa-book-technical-safeguards).

## **3. Aidbox synchronization module**

Now let's take a look at our solution. According to the requirements, the synchronization service should have these functionalities:

1. Both-side synchronization between local and global [Aidboxes](https://www.health-samurai.io/aidbox)
2. HIPAA-compliant data anonymization
3. Synchronization configuration

### **3.1 Common structure**

In the most common case, the architecture of the synchronization module looks like the diagram below:

![](image-2.png)

The module receives data from a local [Aidbox](https://www.health-samurai.io/aidbox), anonymizes it and sends it to the global Aidbox. During the development process, we faced the following challenges:

1. How can we understand what data in a local Aidbox was changed and should therefore be sent to the global Aidbox?
2. How can we make sure that only predefined Aidbox instances can perform synchronization with the global Aidbox?
3. How should we implement data anonymization?

Let’s go through these challenges.

### 3.2 **Tracking data changes**

The task of data synchronization can’t be solved without determining what entities were changed since the previous synchronization iteration. Aidbox provides a mechanism for tracking the resource changes called [Aidboxes Changes API](https://www.health-samurai.io/docs/aidbox/api/other/changes-api). Instead of exporting-importing entities or bulk operations, the Changes API uses a mechanism based on iterations of different versions that provides additional process transparency. It is very helpful in case of any emergencies that can happen during the process of synchronization.

We also created a custom resource called ***ChangesInfo*** that contains an entity name and a version number. During the process of synchronization, we receive this resource for each of the synchronizing FHIR resource types, get its version and call the ***$changes*** endpoint. If any entities have been changed since the previous synchronization iteration, the endpoint returns the list of these entities. And finally, we create an [Aidbox transaction](https://docs.aidbox.app/api-1/transaction) with a ***PUT***, ***POST*** or ***DELETE*** operation for each of the changed entities.

### 3.3 **Synchronization control**

The synchronization process should be safe and controllable. This is why we defined another custom resource, ***AidboxInstance***, which was added to the global Aidbox. It helps to restrict local Aidboxes' access to the global Aidbox for performing synchronization. In every interaction of the background process, we check the ***AidboxInstance*** entity for the local Aidbox to make sure that it exists in the global Aidbox’s DB and should be synchronized.

### 3.4 **Data anonymization**

Data anonymization is the process of protecting private or sensitive information by erasing or encrypting identifiers that connect an individual to stored data. In our case, the sensitive information that should be anonymized is the ***identifier***, ***name***, ***telecom*** and ***address*** attributes of the Patient FHIR resource, the custom ***patientFirstName***, ***patientLastName*** and ***patientMiddleName*** attributesin the QuestionnaireResponse FHIR resource, and the ***answer*** attribute of some QuestionnaireResponse FHIR resources associated with questions such as "Personal Email", "Name", "Phone", "Date of Birth" and "Address".

When the data is moved from the synchronization module, it clears these attributes. It goes without saying that the set of FHIR entities and attributes that need to be cleared can be configured in the application.

### 3.5 **Synchronization workflow**

The process of data synchronization is displayed in the next two diagrams. The first illustrates the process of sending data from the local [Aidboxes](https://www.health-samurai.io/aidbox) to the global one. The second shows the process in reverse.

![](image-3.png)

The first step of transferring data from the local Aidbox to the global one is identifying all data changes using [the Aidbox Changes API](https://docs.aidbox.app/api-1/reactive-api-and-subscriptions/usdsnapshot-usdwatch-and-usdversions-api). After that, the data is processed by the anonymization module to remove all PHI attributes. Finally, the data is moved to the global Aidbox.

![](image-4.png)

The process of transferring data from the Global [Aidbox](https://www.health-samurai.io/aidbox) to the local one is the same, except for the anonymization part. The data that transfers from the global level is more administrative and doesn’t contain any information about patients. This type of medical data isn’t covered by HIPAA and can be transferred without any restrictions.

### 3.6 **Summary**

As a result, the synchronization module is an isolated service that works in the background and achieves synchronization between the defined local Aidbox entities and the global Aidbox entity. It also has an API for performing manual synchronization of separate types of entities.

To finish this section, I want to list the advantages of this solution:

- **Easy maintenance**. Based on [the Changes API](https://docs.aidbox.app/api-1/reactive-api-and-subscriptions/usdsnapshot-usdwatch-and-usdversions-api), the service provides information about every step of the sync process for every entity, which makes it easy to track down any issues.
- **High customization.** We can add any other intermediary steps as we did with the anonymization process. This can affect the final performance, but we apply multithreading and parallelism to cut down on these additional costs.
- **Security guarantee.** We specified access for synchronization for every local [Aidbox](https://www.health-samurai.io/aidbox) instance. This means that only certain Aidboxes can perform synchronization with the global instance. ****
- **HIPAA compliance**. The global Aidbox receives and stores only de-identified health information. The anonymization module removes all identifiers and other sensitive information from the data which makes it safe for transferring.

To explore HIPAA-compliant synchronization in your systems, consider using the [free version of Aidbox](https://www.health-samurai.io/aidbox#run). It provides a secure environment to test and develop synchronization processes, offering all necessary tools without any feature limitations.

## **4. Conclusion**

In this article, we shared our experience in implementing a module for the HIPAA-compliant [Aidbox](https://www.health-samurai.io/aidbox) synchronization process. Synchronization of multiple medical data storage environments is hugely important for distributed or global health information systems. A well-configured synchronization process allows medical organizations to efficiently collect statistical data that can be used for medical or management research, or improve performance by integrating warehouses into the system’s architecture. Whatever goal you want to achieve, you shouldn't forget about the protection of the patient PHI and data anonymization.

If you have offices in different countries, want to integrate a medical data warehouse or just have any questions, feel free to contact us. We and the Health Samurai team will be happy to help you.

Follow US

*Author*:   
**Paul Chayka**,   
Healthcare solution expert | Project Manager and Team Leader at [WaveAccess](https://www.wave-access.com/)

[![WaveAccess Logo](image-5.png)](https://www.wave-access.com/)

[![](image-6.png)](https://www.health-samurai.io/contacts)
