---
description: >-
  List of supported Implementation Guides, which synchronise automatically every
  day at 00:00 UTC
---

# Supported FHIR Implementation Guides (IGs)

## Configure Aidbox

To begin using FHIR IGs, enable the FHIR Schema validator engine in Aidbox.

{% content-ref url="setup.md" %}
[setup.md](setup.md)
{% endcontent-ref %}

**Example configuration**

{% code title=".env" %}
```bash
AIDBOX_FHIR_SCHEMA_VALIDATION=true
AIDBOX_FHIR_PACKAGES=hl7.fhir.us.core#5.0.1:hl7.fhir.us.mcode#3.0.0
AIDBOX_TERMINOLOGY_SERVICE_BASE_URL=https://tx.fhir.org/r4
```
{% endcode %}

## Supported FHIR Implementation Guides (IGs) Packages

Here is the complete list of supported and ready-to-use Implementation Guides (IGs) for the new validator engine. We use [packages2.fhir.org](http://packages2.fhir.org/) as the source of truth for implementation guides and synchronise them daily at 00:00 UTC. The package includes the following resources: FHIR NPM Package manifest, FHIR Schemas, StructureDefinitions (for introspection purposes only), SearchParameters, CompartmentDefinitions, and ValueSet resources (for introspection purposes only). This documentation page is also updated automatically.

---

### Danish MedCom Core

<details>
<summary>Description</summary>

Danish MedCom Core IG (built Tue, Oct 31, 2023 14:20+0100+01:00)

</details>

**Versions**

* `medcom.fhir.dk.core#2.3.0`
* `medcom.fhir.dk.core#2.2.0`
* `medcom.fhir.dk.core#2.1.0`
* `medcom.fhir.dk.core#2.0.0`


---

### ig.fhir-il-community.org.t17

<details>
<summary>Description</summary>

A national project for managing the pre authorization process between HMOs and Hospitals for ambulatory hospital services 

</details>

**Versions**

* `ig.fhir-il-community.org.t17#1.0.0`
* `ig.fhir-il-community.org.t17#0.1.0`


---

### Dental Data Exchange

<details>
<summary>Description</summary>

This implementation guide provides HL7 FHIR resources to define standards for bi-directional information exchange between a medical and a dental provider or between dental providers. This publication provides the data model, defined data items and their corresponding code and value sets specific to a dental referral note and dental consultation note. This guide describes constraints on the [C-CDA on FHIR](http://www.hl7.org/fhir/us/ccda/) header and body elements for dental information, which are derived from requirements developed by the Dental Summary Exchange Project of the Health Level Seven (HL7) Payer/Provider Information Exchange Work Group (PIE WG). Resources in this US Realm implementation guide are specific to dental referral and consultation notes for exchange and interoperability among dental providers and with medical providers.

This guide contains a library of FHIR profiles and is compliant with FHIR Release 4. At a minimum, a document bundle (C-CDA on FHIR Referral Note or Consultation Note) will be exchanged along with a ServiceRequest, Patient, and associated medical and dental information. This guide specifies how and where these resources are included within the C-CDA on FHIR profiles. 

This guide defines 7 new profiles:
* Dental Bundle
* Dental Referral Note
* Dental Service Request
* Dental Consult Note
* Dental Condition
* Dental Finding
* Dental Communication 

All proprietary documents, guides, guidance, standards, codes, and values contained herein remain the property of their respective Standards Developing Organization (SDO). HL7 does not make any claim to ownership herein.

This HL7 FHIR® R4 Implementation Guide: Dental Data Exchange is developed in parallel to the HL7 CDA® R2 Implementation Guide: Dental Data Exchange.
 (built Tue, Nov 2, 2021 16:19+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.dental-data-exchange#1.0.0`
* `hl7.fhir.us.dental-data-exchange#0.1.0`


---

### de.gkvsv.eRezeptAbrechnungsdaten

<details>
<summary>Description</summary>

Der Abrechnungsdatensatz zum E-Rezept

</details>

**Versions**

* `de.gkvsv.eRezeptAbrechnungsdaten#1.0.0`
* `de.gkvsv.eRezeptAbrechnungsdaten#1.0.0-rc`
* `de.gkvsv.eRezeptAbrechnungsdaten#0.9.1-beta`
* `de.gkvsv.eRezeptAbrechnungsdaten#0.9.0-beta`
* `de.gkvsv.eRezeptAbrechnungsdaten#0.2.0-beta`
* `de.gkvsv.eRezeptAbrechnungsdaten#0.1.0-beta`


---

### hl7.fhir.BNW.core

<details>
<summary>Description</summary>

lets start to get swiss pathology structured

</details>

**Versions**

* `hl7.fhir.BNW.core#1.0.0`


---

### incisive.fhir

<details>
<summary>Description</summary>

European AI Project

</details>

**Versions**

* `incisive.fhir#1.0.1-preview`


---

### uk.nhsdigital.r4.test

<details>
<summary>Description</summary>

NHS (England) National Services Implementation Guide

</details>

**Versions**

* `uk.nhsdigital.r4.test#2.8.9-prerelease`
* `uk.nhsdigital.r4.test#2.8.8-prerelease`
* `uk.nhsdigital.r4.test#2.8.7-prerelease`
* `uk.nhsdigital.r4.test#2.8.6-prerelease`
* `uk.nhsdigital.r4.test#2.8.5-prerelease`
* `uk.nhsdigital.r4.test#2.8.17-prerelease`
* `uk.nhsdigital.r4.test#2.8.16-prerelease`
* `uk.nhsdigital.r4.test#2.8.15-prerelease`
* `uk.nhsdigital.r4.test#2.8.14-prerelease`
* `uk.nhsdigital.r4.test#2.8.13-prerelease`
* `uk.nhsdigital.r4.test#2.8.12-prerelease`
* `uk.nhsdigital.r4.test#2.8.11-prerelease`
* `uk.nhsdigital.r4.test#2.8.10-prerelease`


---

### dvmd.kdl.r4

<details>
<summary>Description</summary>

Publikation der Terminologie-Ressourcen für die Nutzung der Klinische Dokumentenklassen-Liste (KDL) des DVMD
Version 2023

</details>

**Versions**

* `dvmd.kdl.r4#2024.0.0`
* `dvmd.kdl.r4#2024.0.0-qa3`
* `dvmd.kdl.r4#2024.0.0-qa2`
* `dvmd.kdl.r4#2024.0.0-qa`
* `dvmd.kdl.r4#2023.0.1`
* `dvmd.kdl.r4#2023.0.0`


---

### Guía de Implementación Core-CL FHIR R4 (Standard Trial For Use (STU))

<details>
<summary>Description</summary>

Guía de Implementación para los perfiles Core que se van a requerir a nivel de desarrollo Nacional para Sistemas que Intercambien datos en estandar FHIR-R4 (built Thu, Jan 20, 2022 16:37-0300-03:00)

</details>

**Versions**

* `hl7.fhir.cl.CoreCH#1.0.0`


---

### ontariomicdrfhirimplementationguide-0.10.00

<details>
<summary>Description</summary>

Ontario Health

</details>

**Versions**

* `ontariomicdrfhirimplementationguide-0.10.00#0.9.8-beta`
* `ontariomicdrfhirimplementationguide-0.10.00#0.9.7-beta`
* `ontariomicdrfhirimplementationguide-0.10.00#0.9.6-beta`
* `ontariomicdrfhirimplementationguide-0.10.00#0.9.5-beta`
* `ontariomicdrfhirimplementationguide-0.10.00#0.9.4-beta`
* `ontariomicdrfhirimplementationguide-0.10.00#0.9.3-beta`
* `ontariomicdrfhirimplementationguide-0.10.00#0.9.1-beta`
* `ontariomicdrfhirimplementationguide-0.10.00#0.9.0-beta`


---

### ths-greifswald.ttp-fhir-gw

<details>
<summary>Description</summary>

Sammlung von Erweiterungen, Profilen und Value Sets zur Nutzung der Treuhandstellen-Werkzeuge im Kontext von FHIR

</details>

**Versions**

* `ths-greifswald.ttp-fhir-gw#2024.1.1`
* `ths-greifswald.ttp-fhir-gw#2024.1.0`
* `ths-greifswald.ttp-fhir-gw#2024.1.0-alpha`
* `ths-greifswald.ttp-fhir-gw#2023.1.1`
* `ths-greifswald.ttp-fhir-gw#2023.1.0`
* `ths-greifswald.ttp-fhir-gw#2.3.0-alpha1`
* `ths-greifswald.ttp-fhir-gw#2.2.0-alpha3`
* `ths-greifswald.ttp-fhir-gw#2.2.0-alpha2`
* `ths-greifswald.ttp-fhir-gw#2.2.0-alpha1`
* `ths-greifswald.ttp-fhir-gw#2.1.0`
* `ths-greifswald.ttp-fhir-gw#2.0.0`


---

### de.abda.erezeptabgabedaten

<details>
<summary>Description</summary>

eRezeptAbgabedatensatz 1.4.0 vom 16.05.2024

</details>

**Versions**

* `de.abda.erezeptabgabedaten#1.4.0`
* `de.abda.erezeptabgabedaten#1.4.0-rc2`
* `de.abda.erezeptabgabedaten#1.4.0-rc`
* `de.abda.erezeptabgabedaten#1.3.2`
* `de.abda.erezeptabgabedaten#1.3.1`
* `de.abda.erezeptabgabedaten#1.3.0`
* `de.abda.erezeptabgabedaten#1.3.0-rc4`
* `de.abda.erezeptabgabedaten#1.3.0-rc3`
* `de.abda.erezeptabgabedaten#1.3.0-rc2`
* `de.abda.erezeptabgabedaten#1.3.0-rc1`
* `de.abda.erezeptabgabedaten#1.2.0`
* `de.abda.erezeptabgabedaten#1.2.0-rc5`
* `de.abda.erezeptabgabedaten#1.2.0-rc4`
* `de.abda.erezeptabgabedaten#1.2.0-rc3`
* `de.abda.erezeptabgabedaten#1.2.0-rc2`
* `de.abda.erezeptabgabedaten#1.2.0-rc`
* `de.abda.erezeptabgabedaten#1.1.2`
* `de.abda.erezeptabgabedaten#1.1.1`
* `de.abda.erezeptabgabedaten#1.1.0`
* `de.abda.erezeptabgabedaten#1.1.0-rc`
* `de.abda.erezeptabgabedaten#1.0.3`
* `de.abda.erezeptabgabedaten#1.0.2`
* `de.abda.erezeptabgabedaten#1.0.1`


---

### Respiratory Virus Hospitalization Surveillance Network (RESP-NET) Content Implementation Guide

<details>
<summary>Description</summary>

The RESP-NET Content IG focuses on the respiratory virus surveillance data that will be extracted from EHRs via FHIR and APIs and sent to RESP-NET sites. (built Thu, Dec 21, 2023 13:44+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.resp-net#1.0.0-ballot`


---

### KBV.Basis

<details>
<summary>Description</summary>

KBV-Basis-Profile V 1.1.3

</details>

**Versions**

* `KBV.Basis#1.1.3`
* `KBV.Basis#1.1.2`
* `KBV.Basis#1.1.1`
* `KBV.Basis#1.0.4`
* `KBV.Basis#1.00.003`
* `KBV.Basis#1.00.002`
* `KBV.Basis#1.00.001`
* `KBV.Basis#1.00.000`
* `KBV.Basis#1.00.000-Alpha1`


---

### FHIR for FAIR - FHIR Implementation Guide

<details>
<summary>Description</summary>

The FHIR for FAIR - FHIR Implementation Guide aims to provide guidance on how HL7 FHIR can be used for supporting FAIR health data implementation and assessment. (built Wed, Sep 28, 2022 16:10+0000+00:00)

</details>

**Versions**

* `hl7.fhir.uv.fhir-for-fair#1.0.0`
* `hl7.fhir.uv.fhir-for-fair#0.1.0`


---

### Guide d'implémentation FHIR - Mesures de santé



**Versions**

* `ans.fhir.fr.mesures#3.1.0-ballot`
* `ans.fhir.fr.mesures#3.0.1`
* `ans.fhir.fr.mesures#3.0.0`


---

### ca.on.oh-seris

<details>
<summary>Description</summary>

The purpose of SERIS is to improve surgical performance in Ontario through the measurement and report.

</details>

**Versions**

* `ca.on.oh-seris#0.10.0-alpha1.0.5`
* `ca.on.oh-seris#0.10.0-alpha1.0.4`
* `ca.on.oh-seris#0.10.0-alpha1.0.3`
* `ca.on.oh-seris#0.10.0-alpha1.0.2`
* `ca.on.oh-seris#0.10.0-alpha1.0.1`
* `ca.on.oh-seris#0.9.0-alpha1.0.1`


---

### acme.product.r4

<details>
<summary>Description</summary>

Derived profiles from the ACME Base project, explaining the creation and use of FHIR packages.

</details>

**Versions**

* `acme.product.r4#3.0.0`
* `acme.product.r4#2.0.0`
* `acme.product.r4#1.0.0`
* `acme.product.r4#1.0.0-alpha`


---

### FHIRcast

<details>
<summary>Description</summary>

FHIRcast synchronizes healthcare applications in real time to show the same clinical content to a common user. (built Wed, Apr 10, 2024 15:14+0000+00:00)

</details>

**Versions**

* `hl7.fhir.uv.fhircast#3.0.0-ballot`
* `hl7.fhir.uv.fhircast#2.1.0-ballot`


---

### de.gematik.erp-servicerequest

<details>
<summary>Description</summary>

Ressourcen zur Rezeptanforderung eines E-Rezeptes

</details>

**Versions**

* `de.gematik.erp-servicerequest#1.1.0`
* `de.gematik.erp-servicerequest#1.0.1`
* `de.gematik.erp-servicerequest#1.0.0`


---

### Electronic Long-Term Services and Supports (eLTSS) Release 1 - US Realm

<details>
<summary>Description</summary>

Provides guidance to US Realm implementers to use the FHIR for implementing access and exchange Electronic Long-Term Services and Supports (eLTSS) Dataset data  elements (built Mon, May 27, 2024 02:28+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.eltss#2.0.0`
* `hl7.fhir.us.eltss#2.0.0-ballot`
* `hl7.fhir.us.eltss#1.0.0`
* `hl7.fhir.us.eltss#0.1.0`


---

### Touchstone-ereferralontario.core

<details>
<summary>Description</summary>

Ontario-specific business use cases and content

</details>

**Versions**

* `Touchstone-ereferralontario.core#0.1.0-beta`


---

### uk.nhsdigital.bars.r4

<details>
<summary>Description</summary>

NHS Booking and Referrals

</details>

**Versions**

* `uk.nhsdigital.bars.r4#1.31.0`
* `uk.nhsdigital.bars.r4#1.30.0`
* `uk.nhsdigital.bars.r4#1.29.0-test+001`
* `uk.nhsdigital.bars.r4#1.28.0`
* `uk.nhsdigital.bars.r4#1.27.0`
* `uk.nhsdigital.bars.r4#1.26.0`
* `uk.nhsdigital.bars.r4#1.25.0`
* `uk.nhsdigital.bars.r4#1.24.0-alpha`
* `uk.nhsdigital.bars.r4#1.23.0-alpha`
* `uk.nhsdigital.bars.r4#1.22.0-alpha`
* `uk.nhsdigital.bars.r4#1.20.0-alpha`
* `uk.nhsdigital.bars.r4#1.19.0-alpha`
* `uk.nhsdigital.bars.r4#1.18.0-alpha`
* `uk.nhsdigital.bars.r4#1.17.0-alpha`
* `uk.nhsdigital.bars.r4#1.16.0-alpha`
* `uk.nhsdigital.bars.r4#1.12.0-alpha`
* `uk.nhsdigital.bars.r4#1.11.0-alpha`
* `uk.nhsdigital.bars.r4#1.10.0-alpha`
* `uk.nhsdigital.bars.r4#1.9.0-alpha`
* `uk.nhsdigital.bars.r4#1.8.0-alpha`
* `uk.nhsdigital.bars.r4#1.7.0-alpha`
* `uk.nhsdigital.bars.r4#1.6.0`
* `uk.nhsdigital.bars.r4#1.6.0+001`
* `uk.nhsdigital.bars.r4#1.5.0`
* `uk.nhsdigital.bars.r4#1.4.0`
* `uk.nhsdigital.bars.r4#1.3.0`
* `uk.nhsdigital.bars.r4#1.2.0`
* `uk.nhsdigital.bars.r4#1.1.0`
* `uk.nhsdigital.bars.r4#1.0.0`
* `uk.nhsdigital.bars.r4#0.5.0-test`
* `uk.nhsdigital.bars.r4#0.4.0-test`
* `uk.nhsdigital.bars.r4#0.3.0-test`
* `uk.nhsdigital.bars.r4#0.2.0-test`


---

### 臺灣核心實作指引(TW Core IG)

<details>
<summary>Description</summary>

臺灣核心實作指引(TW Core IG) (built Tue, May 28, 2024 21:46+0800+08:00)

</details>

**Versions**

* `tw.gov.mohw.twcore#0.2.2`
* `tw.gov.mohw.twcore#0.2.1`
* `tw.gov.mohw.twcore#0.2.0`
* `tw.gov.mohw.twcore#0.1.1`
* `tw.gov.mohw.twcore#0.1.0`


---

### dk.4s-online.raplito

<details>
<summary>Description</summary>

Enlito FHIR API for measurement extraction (built Mon, Mar 8, 2021 18:40+0100+01:00)

</details>

**Versions**

* `dk.4s-online.raplito#0.1.2`
* `dk.4s-online.raplito#0.1.1`
* `dk.4s-online.raplito#0.1.0`


---

### FHIR Tooling Extensions IG

<details>
<summary>Description</summary>

This IG defines the extensions that the tools use internally. Some of these extensions are content that are being evaluated for elevation into the main spec, and others are tooling concerns (built Fri, Apr 26, 2024 06:44+1000+10:00)

</details>

**Versions**

* `hl7.fhir.uv.tools#0.2.0`
* `hl7.fhir.uv.tools#0.1.0`


---

### lung.ca.screen.assignment

<details>
<summary>Description</summary>

This is a project as part of assignment of FHIR Course hosted by SIL-TH

</details>

**Versions**

* `lung.ca.screen.assignment#1.0.1`
* `lung.ca.screen.assignment#1.0.0`


---

### Validated Healthcare Directory



**Versions**

* `hl7.fhir.uv.vhdir#0.2.0`


---

### ufp.core

<details>
<summary>Description</summary>

The United Federation of Planet (UFP) FHIR Implementation Guide of intergalactic scope

</details>

**Versions**

* `ufp.core#0.6.0`
* `ufp.core#0.5.1`
* `ufp.core#0.4.0`


---

### MII IG Pathologie

<details>
<summary>Description</summary>

Medizininformatik Initiative - Modul Pathologie Befund

</details>

**Versions**

* `de.medizininformatikinitiative.kerndatensatz.patho#1.0.0`
* `de.medizininformatikinitiative.kerndatensatz.patho#0.9.0`


---

### MCC eCare Plan Implementation Guide

<details>
<summary>Description</summary>

### Introduction
This HL7&reg; Multiple Chronic Condition (MCC) Care Plan Implementation Guide (IG) defines FHIR R4 profiles, structures, extensions, transactions and value sets needed to represent, query for, and exchange Care Plan information. It defines how to represent coded content used to support the care planning activities focusing on the needs of patients with multiple chronic conditions. This initial version focuses on Chronic Kidney Disease Type 2 diabetes mellitus, common cardiovascular disease (hypertension, ischemic heart disease and heart failure), chronic pain and Long Covid.The profiles defined within this IG were based on data elements of importance identified by the National Institute of Health's (NIH) National Institute of Diabetes and Digestive and Kidney Disease technical expert panels.

A Care Plan is a consensus-driven dynamic plan that represents a patient’s and Care Team Members’ prioritized concerns, goals, planned and actual interventions and the resultant care outcomes. It serves as a blueprint shared by all Care Team Members (including the patient, their caregivers, and providers), to guide the patient’s care. A Care Plan integrates multiple interventions proposed by multiple providers and disciplines for multiple conditions. A Care Plan may represent one or more Care Plans and serves to reconcile and resolve conflicts between the various plans developed for a specific patient by different providers. It supports the following use cases:
 
1.         Query for patient data across providers and compile into a consolidated care plan representation.
2.         Encourage capture of and communication of a patient's health concerns and related goals, interventions, and outcomes.
3.         Gather and aggregate patient data for uses beyond the point of care (e.g. public health, population health, quality measurement, risk adjustment, quality improvement, and research.)

This FHIR IG addresses the needs of multiple audiences. It provides technical artifacts that assist programmers when implementing standards-based FHIR application program interfaces (APIs) for specific purposes. It provides instructive material that explains how FHIR is used to accomplish specific use cases. It also provides general information that helps business analysts and technology decision-makers understand the use cases and benefits associated with achieving specific data exchange capabilities. A FHIR IG is as much a business planning tool as it is an educational resource and a technical specification.

### How to Read this Guide and Cautions
This Guide is divided into several pages which are listed in the navigation bar at the top each page. The contents of each page are listed in a yellow "Contents" box at the top right of each page.

### Value Set Library

Rather than creating nearly 1000 profiles covering important concepts for chronic care coordination for use within the MCC FHIR Care Plan profile, we have created 21 foundation profiles which conform to US Core Profiles (where available) adding constraints or extensions pertinent to their representation within a dynamic FHIR care plan and then creating libraries of value sets pertinent to the base profiles for representing chronic conditions, and their related interventions and goals and outcomes. 
This project has built and houses its value sets in the National Library of Medicine's (NLM) Value Set Authority Center (VSAC). The value sets are not directly bound within the foundation profiles, but value set library pages are provided with links to the value sets in VSAC and descriptions of where within the profile each value set may be used. Because the value sets are housed in VSAC, it is necessary for implementation sites to obtain a free (for the US) UMLS license. In addition, HL7 FHIR Policies requiring everything to be open source in all realms, may present some implementation validation issues when interfacing with a value set housed in VSAC. This IG will provide guidance for this issue.

### History of the Care Plan and Electronic Care Plans

The concept of a care plan began as a teaching tool for nursing students to research and document the medical conditions their patients had and identify the associated nursing care and family support needed. This was documented in a structured paper format that fostered the nursing process. The nursing process is the identification of health concerns and related goals, and the interventions needed to meet those goals and then evaluating the meeting of those goals – the outcomes. Over the years, the care plan evolved from a student teaching tool to something required for most patients in some format by various regulating or certifying bodies, and further, from a nursing care based plan, to an interdisciplinary patient focused tool.

Most major vendors have electronic Care Plan modules, but the degree to which the modules are implemented and actively used is unknown. Few, if any, are exchange standards ready or mapped to exchange standards. The first HL7 standard representing Care Plan was the C-CDA Care Plan document designed to represent an instance of an EHR Care Plan similar to the Continuity of Care (CCD) CDA. Many of the learnings and designs for the FHIR Care Plan Resource and this IG were derived from the C-CDA Care Plan. There is also an IHE [Dynamic Care Planning (DCP)](https://wiki.ihe.net/index.php/Dynamic_Care_Planning_(DCP)) that provides the structures and transactions for care planning, creating, updating and sharing Care Plans that meet the needs of many, such as providers, patients and payers..  The Office of the National Coordinator (ONC) has put into regulation requirements around goals and health concerns which can currently be met by free text. The Dynamic Care Planning (DCP) Profile provides leveraged FHIR Resources to provide the structures and transactions for care planning and sharing Care Plans that meet the needs of many, such as providers, patients and payers. The DCP profile leverages the FHIR Care Plan resource, but does not specify, describe or define the use of it for representing specific conditions or multiple conditions. We are unaware of any real world implementations of it at this time.

We believe that this IG will be the first fully defined HL7 FHIR IG leveraging the FHIR Care Plan resources as its backbone structure for representing a dynamic care plan and testing its ability to bring together aggregated patient care plan data including patient outcomes.

### Project Overview

More than 25 percent of Americans have MCC, accounting for more than 65 percent of U.S. healthcare spending. These individuals have complex health needs handled by diverse providers, across multiple settings of care. As a result, their care is often fragmented, poorly coordinated, and inefficient. Therefore, data aggregation is particularly important and challenging for people with MCC. These challenges will increasingly strain the U.S. health system, with the aging of the U.S. population. Projections suggest numbers of adults aged 65 and older will more than double and numbers of those aged 85 and older will triple by 2050.
 
Care plans are a prominent part of multifaceted, care coordination interventions that reduce mortality and hospitalizations and improve disease management and satisfaction. In addition, proactive care planning promotes person-centeredness, improves outcomes, and reduces the cost of care. By design, care plans take a patient-centered approach, both by making comprehensive health data available across providers and settings and through the incorporation of data elements that have not traditionally been included in health IT systems (e.g., social determinants of health SDOH, patient health and life goals, patient preferences). While Care Plans have been developed, they remain paper-based in many U.S. healthcare settings and are not standardized and interoperable across care settings when electronic. While care plans focused on a single disease or condition are unlikely to be tenable for patients with MCC or their providers, existing care plans infrequently address individuals with MCC. The development of care plans based on structured data has been proposed as a method for enabling electronic systems to pull together and share data elements automatically and dynamically. Such aggregated data would not only provide actionable information to identify and achieve health and wellness goals for individuals with MCC, but also would reduce missingness and improve quality of point-of-care data for use in pragmatic research.
 
The Fast Healthcare Interoperability Resources (FHIR) specification is an open-source standard for exchanging healthcare information electronically based on emerging industry approaches. The FHIR workflow specification includes a CarePlan request resource that may facilitate transfer of data for an e-care plan across healthcare settings. SMART (https://smarthealthit.org/) and SMART on FHIR standards include open specifications to integrate applications with health IT systems and may enable the development of an e-care plan application that can integrate with a variety of electronic health record (EHR) systems.

Initiated by the National Institute of Diabetes and Digestive and Kidney Diseases (NIDDK), the multiple chronic conditions (MCC) electronic care (eCare) Plan Project aims to develop, test, and pilot an interoperable eCare plan that will facilitate aggregation and sharing of critical patient-centered data across home, community, clinic, and research-based settings for persons with MCC, including chronic kidney disease (CKD), type 2 diabetes mellitus (T2D), cardiovascular disease (CVD), pain with opioid use disorder (OUD), and long COVID. 

The HL7-based activities of the MCC eCare Plan Project include:
 
 - Identified use cases to support the documentation and exchange of MCC eCare plan data within EHRs and related systems.
 - Identified, developed, and prioritized the necessary MCC data elements and clinical terminology standards and FHIR® mappings that will enable the standardized transfer of data across health settings.
 - Develop, test, and ballot an HL7® Fast Health Interoperability Resources (FHIR®) Implementation Guide based on the defined use cases and MCC data elements.
 
Non-HL7 related activities of the MCC eCare Plan project will be facilitated through the [AHRQ eCare Plan Project Confluence](https://ecareplan.ahrq.gov/collaborate):
 - The project includes the develoment and testing of an open-source clinician and patient facing SMART-on-FHIR eCare plan application for managing persons with MCC.

####  Project Timelines
 - January 2024: A For comment ballot to gather community input prior to the formal STU Ballot
 - September 2024: Formal STU Ballot
 - May 2025 Formal HL7 Publication


### Guidance
The guidance section provides general implementation guidance and best practices. It describes the relation to and reuse of the US Core Implementation Guide profiles and reuse of its conformance requirements and expectations for the servers and client applications. Vocabulary use and value set binding heuristics are described.

####  Relationship to US Core

This Implementation Guide reuses US Core profiles either through direct use or by constraining select profiles for representation within a FHIR Care Plan profile. Where US Core does not have a profile or function that is needed for the use cases or data elements, the IG constrains or directly reuses other resources, or profiles defined in other FHIR IGs. 
This guide will reuse the US Core Care Plan. However, the required US Core CarePlan.text and the required Care.Plan.category:AssessPlan may be limiting factors for this context of use: aggregation of multiple chronic condition care plans and the ability to query for structured data within a structured Care Plan. As such, we recommend adding additional Care Plan categories if deemed more informational to reflect multiple chronic condition care coordination and plans and to hard code “Multiple Chronic Condition Care Coordination Plan” at CarePlan.text


#####  General US Core IG Conformance

This guide will adhere to or build on US conformance requirements, most of its [General Guidance](https://www.hl7.org/fhir/us/core/general-guidance.html), and its [Capability Statements](https://www.hl7.org/fhir/us/core/capstatements.html) where applicable. 

#####  Provenance

This IG recommends implementers adhere to guidelines and definitions provided in US Core’s [Basic Provenance Guidance](https://www.hl7.org/fhir/us/core/basic-provenance.html). 

#####  Must Support

This IG will adhere to the US Core [Must Support](https://www.hl7.org/fhir/us/core/general-guidance.html#must-support) concept and rules.

### Acknowledgements
This Implementation Guide was made possible through the visionary leadership of the [National Institute of Diabetes and Digestive and Kidney Diseases (NIDDK) ](https://www.niddk.nih.gov/)and the [Agency for Healthcare Research and Quality (AHRQ) ](https://www.ahrq.gov/)with funding from the [Office of the Assistant Secretary for Planning and Evaluation (ASPE)](https://aspe.hhs.gov/collaborations-committees-advisory-groups/os-pcortf/explore-portfolio). The joint NIDDK-AHRQ project team can be found [here](https://cmext.ahrq.gov/confluence/display/EC/Project+Team). We would also like to thank the many clinicians, patients, caregivers, researchers, advocates, and subject matter experts who served on our [Technical Expert Panels](https://cmext.ahrq.gov/confluence/display/EC/Technical+Expert+Panels) and [Contract Monitoring Board ](https://cmext.ahrq.gov/confluence/display/EC/Contract+Monitoring+Board) for their time, insight, and support. 

This Implementation Guide was created under the supervision and review of the [HL7 Patient Care Work Group](https://www.hl7.org/Special/committees/patientcare/index.cfm). This is the HL7 project page for the [Multiple Chronic Conditions e-Care Project.](https://confluence.hl7.org/display/PC/Multiple+Chronic+Conditions+%28MCC%29+eCare+Plan)

 (built Fri, Jul 28, 2023 17:45+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.mcc#1.0.0-ballot2`
* `hl7.fhir.us.mcc#1.0.0-ballot`


---

### CarePlanRT.Eng

<details>
<summary>Description</summary>

CarePlanEng

</details>

**Versions**

* `CarePlanRT.Eng#1.0.0`


---

### junk.sample-preview

<details>
<summary>Description</summary>

Simplifier Sample version 2

</details>

**Versions**

* `junk.sample-preview#2.0.3`
* `junk.sample-preview#2.0.2`
* `junk.sample-preview#2.0.1`


---

### ee.hl7.fhir.base.r4

<details>
<summary>Description</summary>

Estonian HL7 FHIR base Implementation Guide and profiles

</details>

**Versions**

* `ee.hl7.fhir.base.r4#1.0.4-beta`
* `ee.hl7.fhir.base.r4#1.0.3-beta`
* `ee.hl7.fhir.base.r4#1.0.2-beta`
* `ee.hl7.fhir.base.r4#1.0.1-beta`
* `ee.hl7.fhir.base.r4#1.0.0-beta`


---

### Patient.Hospice-LOC

<details>
<summary>Description</summary>

This extension is to store level of care data for hospice patient. It extends the patient resource

</details>

**Versions**

* `Patient.Hospice-LOC#1.0.1`


---

### HL7 BE Laboratory WG Implementation Guide

<details>
<summary>Description</summary>

HL7 BE Laboratory WG Implementation Guide (built Thu, Sep 8, 2022 14:30+0200+02:00)

</details>

**Versions**

* `hl7.fhir.be.lab#1.0.0`


---

### ca.infoway.io.core

<details>
<summary>Description</summary>

The CA Core+ project provides profiles based on the business, conceptual, and logical models in the pan-Canadian Health Data Content Framework. These models capture the data that is essential for clinical and business workflows to provide better health outcomes for Canadians.

</details>

**Versions**

* `ca.infoway.io.core#0.2.0-dft-ballot`
* `ca.infoway.io.core#0.1.0-dft`
* `ca.infoway.io.core#0.1.0-DFT-Ballot`
* `ca.infoway.io.core#0.1.0-DFT-Ballot-pre`


---

### CardX Hypertension Management



**Versions**

* `hl7.fhir.uv.cardx-htn-mng#1.0.0-ballot`


---

### IHE FormatCode Vocabulary

<details>
<summary>Description</summary>

Implementation Guide for IHE defined FormatCode vocabulary. (built Fri, May 17, 2024 12:02-0500-05:00)

</details>

**Versions**

* `ihe.formatcode.fhir#1.3.0`
* `ihe.formatcode.fhir#1.2.0`
* `ihe.formatcode.fhir#1.1.1`
* `ihe.formatcode.fhir#1.1.0`
* `ihe.formatcode.fhir#1.0.0`
* `ihe.formatcode.fhir#0.2.4`
* `ihe.formatcode.fhir#0.2.2`


---

### DK MedCom HospitalNotification

<details>
<summary>Description</summary>

The DK MedCom HospitalNotification IG (built Thu, Mar 2, 2023 09:52+0100+01:00)

</details>

**Versions**

* `medcom.fhir.dk.hospitalnotification#3.0.1`
* `medcom.fhir.dk.hospitalnotification#3.0.0`
* `medcom.fhir.dk.hospitalnotification#2.0.0`


---

### Guide d'implémentation Fr Core



**Versions**

* `hl7.fhir.fr.core#2.0.1`
* `hl7.fhir.fr.core#2.0.0`
* `hl7.fhir.fr.core#2.0.0-ballot`


---

### Nictiz profiling guidelines for FHIR R4

<details>
<summary>Description</summary>

This package contains profiles to check conformance to the Nictiz profiling guidelines for FHIR R4.

</details>

**Versions**

* `nictiz.fhir.nl.r4.profilingguidelines#0.9.0`
* `nictiz.fhir.nl.r4.profilingguidelines#0.8.0`
* `nictiz.fhir.nl.r4.profilingguidelines#0.7.0`
* `nictiz.fhir.nl.r4.profilingguidelines#0.6.0`
* `nictiz.fhir.nl.r4.profilingguidelines#0.5.0`
* `nictiz.fhir.nl.r4.profilingguidelines#0.4.0`
* `nictiz.fhir.nl.r4.profilingguidelines#0.3.0`
* `nictiz.fhir.nl.r4.profilingguidelines#0.2.0`


---

### rl.fhir.r4.draft

<details>
<summary>Description</summary>

Progetto FHIR per Regione Lombardia

</details>

**Versions**

* `rl.fhir.r4.draft#0.0.22`
* `rl.fhir.r4.draft#0.0.21`
* `rl.fhir.r4.draft#0.0.20`
* `rl.fhir.r4.draft#0.0.19`
* `rl.fhir.r4.draft#0.0.18`
* `rl.fhir.r4.draft#0.0.17`
* `rl.fhir.r4.draft#0.0.13`
* `rl.fhir.r4.draft#0.0.12`
* `rl.fhir.r4.draft#0.0.11`
* `rl.fhir.r4.draft#0.0.10`
* `rl.fhir.r4.draft#0.0.8`
* `rl.fhir.r4.draft#0.0.7`
* `rl.fhir.r4.draft#0.0.6`
* `rl.fhir.r4.draft#0.0.5`
* `rl.fhir.r4.draft#0.0.4`


---

### Annuaire Santé

<details>
<summary>Description</summary>

Ressources de conformité basées sur le modèle d'exposition de l'Annuaire Santé. (built Thu, Apr 25, 2024 11:41+0000+00:00)

</details>

**Versions**

* `ans.fhir.fr.annuaire#1.0.1`
* `ans.fhir.fr.annuaire#1.0.0`
* `ans.fhir.fr.annuaire#1.0.0-ballot`
* `ans.fhir.fr.annuaire#1.0.0-ballot-4`
* `ans.fhir.fr.annuaire#1.0.0-ballot-3`
* `ans.fhir.fr.annuaire#1.0.0-ballot-2`
* `ans.fhir.fr.annuaire#0.1.0`


---

### dguv.basis

<details>
<summary>Description</summary>

Diese bilden die Grundlage zur Umsetzung der zukünftigen digitalen Formtexte (Formulare) der Deutsche Gesetzliche Unfallversicherung.

</details>

**Versions**

* `dguv.basis#1.1.0`
* `dguv.basis#1.1.1-Kommentierung`


---

### US Core Implementation Guide

<details>
<summary>Description</summary>

The US Core Implementation Guide is based on FHIR Version R4 and defines the minimum conformance requirements for accessing patient data. The Argonaut pilot implementations, ONC 2015 Edition Common Clinical Data Set (CCDS), and ONC U.S. Core Data for Interoperability (USCDI) v1 provided the requirements for this guide. The prior Argonaut search and vocabulary requirements, based on FHIR DSTU2, are updated in this guide to support FHIR Version R4. This guide was used as the basis for further testing and guidance by the Argonaut Project Team to provide additional content and guidance specific to Data Query Access for purpose of ONC Certification testing. These profiles are the foundation for future US Realm FHIR implementation guides. In addition to Argonaut, they are used by DAF-Research, QI-Core, and CIMI. Under the guidance of HL7 and the HL7 US Realm Steering Committee, the content will expand in future versions to meet the needs specific to the US Realm.
These requirements were originally developed, balloted, and published in FHIR DSTU2 as part of the Office of the National Coordinator for Health Information Technology (ONC) sponsored Data Access Framework (DAF) project. For more information on how DAF became US Core see the US Core change notes. (built Wed, May 8, 2024 15:33+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.core#7.0.0`
* `hl7.fhir.us.core#7.0.0-ballot`
* `hl7.fhir.us.core#6.1.0`
* `hl7.fhir.us.core#6.1.0-snapshot1`
* `hl7.fhir.us.core#6.0.0`
* `hl7.fhir.us.core#6.0.0-ballot`
* `hl7.fhir.us.core#5.0.1`
* `hl7.fhir.us.core#5.0.0`
* `hl7.fhir.us.core#4.1.0`
* `hl7.fhir.us.core#4.0.0`
* `hl7.fhir.us.core#3.2.0`
* `hl7.fhir.us.core#3.1.1`
* `hl7.fhir.us.core#3.1.0`
* `hl7.fhir.us.core#3.0.1`
* `hl7.fhir.us.core#3.0.0`
* `hl7.fhir.us.core#2.1.0`
* `hl7.fhir.us.core#2.0.0`
* `hl7.fhir.us.core#1.1.0`
* `hl7.fhir.us.core#1.0.1`
* `hl7.fhir.us.core#1.0.0`


---

### beact.es.publishingtutorial

<details>
<summary>Description</summary>

TestingFHIR

</details>

**Versions**

* `beact.es.publishingtutorial#0.0.1-test`


---

### rapportendoscopiequebec.test

<details>
<summary>Description</summary>

Envoi du rapport d’endoscopie au dépôt provincial

</details>

**Versions**

* `rapportendoscopiequebec.test#0.0.53`
* `rapportendoscopiequebec.test#0.0.52`
* `rapportendoscopiequebec.test#0.0.51`
* `rapportendoscopiequebec.test#0.0.50`
* `rapportendoscopiequebec.test#0.0.49`
* `rapportendoscopiequebec.test#0.0.48`
* `rapportendoscopiequebec.test#0.0.47`
* `rapportendoscopiequebec.test#0.0.46`
* `rapportendoscopiequebec.test#0.0.45`
* `rapportendoscopiequebec.test#0.0.44`
* `rapportendoscopiequebec.test#0.0.43`
* `rapportendoscopiequebec.test#0.0.42`
* `rapportendoscopiequebec.test#0.0.41`
* `rapportendoscopiequebec.test#0.0.40`
* `rapportendoscopiequebec.test#0.0.39`
* `rapportendoscopiequebec.test#0.0.38`
* `rapportendoscopiequebec.test#0.0.37`
* `rapportendoscopiequebec.test#0.0.36`
* `rapportendoscopiequebec.test#0.0.35`
* `rapportendoscopiequebec.test#0.0.34`
* `rapportendoscopiequebec.test#0.0.33`
* `rapportendoscopiequebec.test#0.0.32`
* `rapportendoscopiequebec.test#0.0.31`
* `rapportendoscopiequebec.test#0.0.30`
* `rapportendoscopiequebec.test#0.0.29`
* `rapportendoscopiequebec.test#0.0.28`
* `rapportendoscopiequebec.test#0.0.27`
* `rapportendoscopiequebec.test#0.0.26`
* `rapportendoscopiequebec.test#0.0.25`
* `rapportendoscopiequebec.test#0.0.24`
* `rapportendoscopiequebec.test#0.0.23`
* `rapportendoscopiequebec.test#0.0.22`
* `rapportendoscopiequebec.test#0.0.21`
* `rapportendoscopiequebec.test#0.0.20`
* `rapportendoscopiequebec.test#0.0.19`
* `rapportendoscopiequebec.test#0.0.18`
* `rapportendoscopiequebec.test#0.0.17`
* `rapportendoscopiequebec.test#0.0.16`
* `rapportendoscopiequebec.test#0.0.15`
* `rapportendoscopiequebec.test#0.0.14`
* `rapportendoscopiequebec.test#0.0.13`
* `rapportendoscopiequebec.test#0.0.12`
* `rapportendoscopiequebec.test#0.0.11`
* `rapportendoscopiequebec.test#0.0.10`
* `rapportendoscopiequebec.test#0.0.9`
* `rapportendoscopiequebec.test#0.0.8`
* `rapportendoscopiequebec.test#0.0.7`
* `rapportendoscopiequebec.test#0.0.6`
* `rapportendoscopiequebec.test#0.0.5`
* `rapportendoscopiequebec.test#0.0.2`


---

### CH Core (R4)

<details>
<summary>Description</summary>

FHIR implementation guide CH Core (built Thu, May 16, 2024 16:08+0000+00:00)

</details>

**Versions**

* `ch.fhir.ig.ch-core#5.0.0-ballot`
* `ch.fhir.ig.ch-core#4.0.1`
* `ch.fhir.ig.ch-core#4.0.0`
* `ch.fhir.ig.ch-core#4.0.0-ballot`
* `ch.fhir.ig.ch-core#3.0.0`
* `ch.fhir.ig.ch-core#2.1.0`
* `ch.fhir.ig.ch-core#2.0.0`
* `ch.fhir.ig.ch-core#1.2.0`
* `ch.fhir.ig.ch-core#1.0.0`
* `ch.fhir.ig.ch-core#0.1.0`


---

### ca-on-covax.v1

<details>
<summary>Description</summary>

Ontario COVaxON FHIR Implementation Guide

</details>

**Versions**

* `ca-on-covax.v1#1.0.0`


---

### Patient Identifier Cross-referencing for mobile (PIXm)

<details>
<summary>Description</summary>

ImplementationGuide for IHE IT Infrastructure Technical Framework Supplement Patient Identifier Cross-referencing for mobile (PIXm) (built Mon, Nov 8, 2021 19:37-0600-06:00)

</details>

**Versions**

* `IHE.ITI.PIXm#3.0.0`
* `IHE.ITI.PIXm#2.2.0`


---

### MIO.KBV.Mutterpass

<details>
<summary>Description</summary>

Medizinische Informationsobjekte (MIO) Mutterpass V1.0.0

</details>

**Versions**

* `MIO.KBV.Mutterpass#1.0.0`


---

### de.abda.eRezeptAbgabedatenBasis

<details>
<summary>Description</summary>

Basis-Profile für die Abgabedaten im eRezept-Kontext

</details>

**Versions**

* `de.abda.eRezeptAbgabedatenBasis#1.1.0-rc1`


---

### HL7 FHIR® Implementation Guide: Ophthalmology Retinal, Release 1

<details>
<summary>Description</summary>

The overarching goal is to define and standardize the means through which all types of eye care and general medical providers can communicate (built Thu, Aug 12, 2021 15:08+0000+00:00)

</details>

**Versions**

* `hl7.fhir.uv.eyecare#0.1.0`


---

### Bulk Data Access IG

<details>
<summary>Description</summary>

FHIR based approach for exporting large data sets from a FHIR server to a client application (built Fri, Nov 26, 2021 05:56+1100+11:00)

</details>

**Versions**

* `hl7.fhir.uv.bulkdata#2.0.0`
* `hl7.fhir.uv.bulkdata#1.1.0`


---

### de.gematik.epa

<details>
<summary>Description</summary>

Die Basisfunktionalitäten der elektronischen Patientenakte

</details>

**Versions**

* `de.gematik.epa#1.0.1`
* `de.gematik.epa#1.0.0`
* `de.gematik.epa#0.0.3`


---

### ANS.annuaire.fhir.r4

<details>
<summary>Description</summary>

Description de la structure des données d'identification des acteurs de santé exposées par les nouveaux services Annuaire Santé d'interrogation FHIR de l'Agence du Numérique en Santé (ANS). 

</details>

**Versions**

* `ANS.annuaire.fhir.r4#0.2.0`


---

### devdays.letsbuildafhirspec.simplifier1

<details>
<summary>Description</summary>

Building an Implementation Guide and Package with Simplifier.net

</details>

**Versions**

* `devdays.letsbuildafhirspec.simplifier1#0.0.3-devdaysus2021`


---

### At-Home In-Vitro Test Report

<details>
<summary>Description</summary>

|Implementation Guide (IG) Characteristic  |  Value |
|------------------------------------------------------|--------------------------------------------|
|**FHIR Version:** | FHIR R4 |
|**IG Realm:** | US |
|**IG Type:** | STU Ballot |
|**Exchange Methods:** | RESTfulAPI, Messages, Transactions,  Tasks |
|**IG Note:** | This HL7 FHIR Guide utilizes and adopts guidance or profiles developed in the US Core FHIR&reg; Implementation Guide. This FHIR IG has narrative pages describing the HL7 V2 Message for At-Home In-Vitro Test Reporting and provides some example messages. Presently public health departments usually only can handle and store HL7 V2|
{:.table-striped}

|IG Dependencies |
|----------------------------------|
| [HL7 FHIR US Core Version 5.0.1](http://hl7.org/fhir/us/core/STU5.0.1/) |
{:.table-striped}


### Background

Point-of-care (POC) and At-Home In-Vitro Tests offer increased accessibility to needed diagnostic solutions during a pandemic. Test results provide value to both the individual taking the tests and also to public health authorities responsible for coordinating a regional and national response. Digital platforms are being developed that allow individual test results, with permission of the individual, to be captured, organized, and transmitted to public health systems. Test manufacturers, working in partnership with these digital platforms, are seeking guidance on how to send test results. With multiple tests already entering the market, there is a need for a unified strategy on data flow of test results from the apps to downstream systems. A national data exchange standard should be supported that guides data exchange from test results residing on individual users’ digital test applications to public health authorities, at both the federal and regional levels.  

### Introduction
This implementation guide (IG) constrains the FHIR [US Core Diagnostic Report for Laboratory Results](http://hl7.org/fhir/us/core/StructureDefinition/us-core-diagnosticreport-lab) and the  [US Core Laboratory Observation Profile](http://hl7.org/fhir/us/core/StructureDefinition/us-core-observation-lab) for use in transmitting At-Home In-Vitro Test results to local, state, territorial and federal health agencies. The IG will be be used in real world testing. It was developed as part of a collaborative project with the NIH/NIBIB, ONC, a vendor manufacturer of diagnostic healthcare products that makes one of the FDA approved At-Home In-Vitro Test kits, and an app developer who is a leading provider of secure interoperability solutions. The intention of this guide is to assist developers in producing and sending standardized FHIR test result data from tests perfomed at home. FHIR eases app development and the FHIR US Core IG is in a rapid adoption curve. This guide points to already existing US Core Guidance and other guidance with respect to FHIR Parameters, FHIR operations, and RESTful and SMART on FHIR information.

Due to Public Health Departments' familiarity with V2, a test implementation project related to this implementation guide will initially send COVID-19 At-Home In-Vitro Test Reports as V2 lab messages. Longer term the intent is to leverage FHIR for app creativity, growth, and interoperability. For as long as needed to comply with Public Health Department dependecies on V2, the apps will be designed to transform from FHIR to V2. In addition, as a parallel effort, a V2 <-> FHIR data mapping table has been developed [In-Vitro At-Home Test V2 FHIR Mapping.xlsx].

**Please note** : This guide is a framework for future work. It contains "framework" profiles that contain constraints common to all At-Home In-Vitro Test Reporting use cases. These framework profiles can be further constrained to a particular use case such as the COVID-19 At-Home In-Vitro Test Reporting use case. The COVID-19 use case profiles have been created and included in this guide.

### Further Information
[RADx® MARS - Mobile Application Reporting through Standards](https://www.nibib.nih.gov/covid-19/radx-tech-program/mars)

[LOINC In Vitro Diagnostic (LIVD) Test Code Mapping for SARS-CoV-2 Tests](https://www.cdc.gov/csels/dls/sars-cov-2-livd-codes.html)

[Test-Specific HL7v2 Field Values Tool](https://app.powerbigov.us/view?r=eyJrIjoiNjQyZjBkOGEtNjE0ZS00NjUyLTg3NjEtZTIxN2JmODE0ZGE1IiwidCI6IjE0Yjc3NTc4LTk3NzMtNDJkNS04NTA3LTI1MWNhMmRjMmIwNiJ9&pageName=ReportSectionhttps://app.powerbigov.us/view?r=eyJrIjoiNjQyZjBkOGEtNjE0ZS00NjUyLTg3NjEtZTIxN2JmODE0ZGE1IiwidCI6IjE0Yjc3NTc4LTk3NzMtNDJkNS04NTA3LTI1MWNhMmRjMmIwNiJ9&pageName=ReportSection3147535a75468ee60d16)

### Acknowledgements/Primary Authors
* [NIBIB-Interagency Project Team](mailto:krishna.Juluru@pif.gov)
* [CareEvolution](https://careevolution.com)
* [Association of Public Health Laboratories](https://www.aphl.org/Pages/default.aspx)
* [Gay Dolin MSN RN (Namaste Informatics)](mailto:gdolin@NamasteInformatics.com)
* [Sarah Gaunt](mailto:sarah.gaunt@lantanaconsulting.com) (built Sat, Mar 25, 2023 16:44+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.home-lab-report#1.0.0`
* `hl7.fhir.us.home-lab-report#1.0.0-ballot`


---

### MedNet interface implementation guide

<details>
<summary>Description</summary>

This Guide describes mednet interfaces (built Fri, Jul 15, 2022 16:34+0200+02:00)

</details>

**Versions**

* `swiss.mednet.fhir#0.15.0`
* `swiss.mednet.fhir#0.14.0`
* `swiss.mednet.fhir#0.13.0`
* `swiss.mednet.fhir#0.12.0`
* `swiss.mednet.fhir#0.11.0`
* `swiss.mednet.fhir#0.10.0`
* `swiss.mednet.fhir#0.9.0`
* `swiss.mednet.fhir#0.8.0`
* `swiss.mednet.fhir#0.7.0`
* `swiss.mednet.fhir#0.6.0`
* `swiss.mednet.fhir#0.5.0`
* `swiss.mednet.fhir#0.4.0`
* `swiss.mednet.fhir#0.3.0`
* `swiss.mednet.fhir#0.2.0`


---

### ca.on.health.sadie

<details>
<summary>Description</summary>

Special Authorization Digital Information Exchange will have the initial package for the first QA release. 

</details>

**Versions**

* `ca.on.health.sadie#0.1.9-beta`
* `ca.on.health.sadie#0.1.8-beta`
* `ca.on.health.sadie#0.1.7-beta`
* `ca.on.health.sadie#0.1.6-beta`
* `ca.on.health.sadie#0.1.5-beta`
* `ca.on.health.sadie#0.1.4-beta`
* `ca.on.health.sadie#0.1.3-beta`
* `ca.on.health.sadie#0.1.27-beta`
* `ca.on.health.sadie#0.1.26-beta`
* `ca.on.health.sadie#0.1.25-beta`
* `ca.on.health.sadie#0.1.24-beta`
* `ca.on.health.sadie#0.1.23-beta`
* `ca.on.health.sadie#0.1.22-beta`
* `ca.on.health.sadie#0.1.21-beta`
* `ca.on.health.sadie#0.1.20-beta`
* `ca.on.health.sadie#0.1.2-beta`
* `ca.on.health.sadie#0.1.19-beta`
* `ca.on.health.sadie#0.1.18-beta`
* `ca.on.health.sadie#0.1.17-beta`
* `ca.on.health.sadie#0.1.16-beta`
* `ca.on.health.sadie#0.1.15-beta`
* `ca.on.health.sadie#0.1.14-beta`
* `ca.on.health.sadie#0.1.13-beta`
* `ca.on.health.sadie#0.1.12-beta`
* `ca.on.health.sadie#0.1.11-beta`
* `ca.on.health.sadie#0.1.10-beta`
* `ca.on.health.sadie#0.1.1-beta`
* `ca.on.health.sadie#0.1.0-beta`


---

### Consumer Real-Time Pharmacy Benefit Check

<details>
<summary>Description</summary>

This is a guide for implementing the Consumer-Focused Real-Time Pharmacy Benefit Check (consumer RTPBC) using FHIR R4. Using RTPBC, a patient can learn the cost and insurance coverage related to medications they've been prescribed. Specifically: how a medication will be covered by their insurance, including out of pocket costs and any coverage restrictions or requirements that might apply, discount pricing available for the medication. Consumer RTPBC leverages the predetermination process supported by the Claim and ClaimResponse FHIR resources. Accompanying resources carry information that identifies the patient and their insurance coverage, prescription information and the preferred pharmacy (Patient, Coverage, MedicationRequest, Practitioner and Organization). (built Tue, Aug 25, 2020 19:54+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.carin-rtpbc#1.0.0`
* `hl7.fhir.us.carin-rtpbc#0.1.0`


---

### il.core.fhir.r4

<details>
<summary>Description</summary>

IL core project vesrion 0.15.0

</details>

**Versions**

* `il.core.fhir.r4#0.15.0`
* `il.core.fhir.r4#0.14.3`
* `il.core.fhir.r4#0.14.2`
* `il.core.fhir.r4#0.14.1`
* `il.core.fhir.r4#0.14.0`
* `il.core.fhir.r4#0.13.1`
* `il.core.fhir.r4#0.13.0`
* `il.core.fhir.r4#0.12.0`
* `il.core.fhir.r4#0.12.0-snapshots`
* `il.core.fhir.r4#0.11.1`
* `il.core.fhir.r4#0.11.0`
* `il.core.fhir.r4#0.10.1`
* `il.core.fhir.r4#0.10.0`
* `il.core.fhir.r4#0.10.1-snapshots`
* `il.core.fhir.r4#0.9.1`
* `il.core.fhir.r4#0.9.0`
* `il.core.fhir.r4#0.8.1`
* `il.core.fhir.r4#0.8.0`
* `il.core.fhir.r4#0.7.0`
* `il.core.fhir.r4#0.6.0`
* `il.core.fhir.r4#0.5.0`
* `il.core.fhir.r4#0.4.0`
* `il.core.fhir.r4#0.3.0`
* `il.core.fhir.r4#0.2.0`
* `il.core.fhir.r4#0.1.1`
* `il.core.fhir.r4#0.1.0`


---

### acme.usecase.r4

<details>
<summary>Description</summary>

A project from ACME Corp for a specific use case, building on the R4 ACME Base Profiles

</details>

**Versions**

* `acme.usecase.r4#1.1.7`
* `acme.usecase.r4#1.1.6`
* `acme.usecase.r4#1.1.5`
* `acme.usecase.r4#1.1.3`
* `acme.usecase.r4#1.1.2-preview1`
* `acme.usecase.r4#0.1.1`
* `acme.usecase.r4#0.1.0`
* `acme.usecase.r4#0.0.9`
* `acme.usecase.r4#0.0.8`
* `acme.usecase.r4#0.0.7`
* `acme.usecase.r4#0.0.6`
* `acme.usecase.r4#0.0.5`
* `acme.usecase.r4#0.0.4`
* `acme.usecase.r4#0.0.3`
* `acme.usecase.r4#0.0.2`
* `acme.usecase.r4#0.0.1`


---

### SFM.030521

<details>
<summary>Description</summary>

Medication related projects (SFM, PLL, SAFEST, KIKJ)

</details>

**Versions**

* `SFM.030521#1.0.0`


---

### National Directory of Healthcare Providers & Services (NDH) Implementation Guide



**Versions**

* `hl7.fhir.us.ndh#1.0.0-ballot`


---

### FHIR R4 package : Expansions

<details>
<summary>Description</summary>

Expansions for the R4 version of the FHIR standard

</details>

**Versions**

* `hl7.fhir.r4.expansions#4.0.1`


---

### healthdata.be.r4.laboratorytestresult

<details>
<summary>Description</summary>

LaboratoryTestResult message specification based on HdBe clinical building blocks (healthdata.be.r4.cbb).

</details>

**Versions**

* `healthdata.be.r4.laboratorytestresult#0.5.0-beta`
* `healthdata.be.r4.laboratorytestresult#0.4.0-beta`
* `healthdata.be.r4.laboratorytestresult#0.3.0-alpha`
* `healthdata.be.r4.laboratorytestresult#0.2.0-alpha`
* `healthdata.be.r4.laboratorytestresult#0.1.0`


---

### basisprofil.tiplu.de.r4

<details>
<summary>Description</summary>

Projekt Basisprofilierung (DE) (R4) (Tiplu GmbH)

</details>

**Versions**

* `basisprofil.tiplu.de.r4#1.1.3`
* `basisprofil.tiplu.de.r4#1.1.2`
* `basisprofil.tiplu.de.r4#1.1.1`
* `basisprofil.tiplu.de.r4#1.1.0`


---

### OntarioacCDRFHIRImplementationGuide-0.10.00

<details>
<summary>Description</summary>

Ontario Health

</details>

**Versions**

* `OntarioacCDRFHIRImplementationGuide-0.10.00#0.1.0`


---

### AU Base Implementation Guide

<details>
<summary>Description</summary>

Unofficial package of the AU Base profile for rendering purposes of derived profiles. Will be unlisted once the package is registerd in the FHIR Package registry.

</details>

**Versions**

* `at.unofficial.au.base#2.2.0-ballot2`
* `at.unofficial.au.base#2.2.0-ballot`


---

### CH EPR PPQm (R4)

<details>
<summary>Description</summary>

Implementation Guide for the Privacy Policy Query for Mobile (PPQm) for the Swiss EPR (built Wed, Dec 20, 2023 11:33+0100+01:00)

</details>

**Versions**

* `ch.fhir.ig.ch-epr-ppqm#2.0.0`
* `ch.fhir.ig.ch-epr-ppqm#2.0.0-ballot`
* `ch.fhir.ig.ch-epr-ppqm#0.2.0`
* `ch.fhir.ig.ch-epr-ppqm#0.1.0`


---

### TInterop.23

<details>
<summary>Description</summary>

Projet de test Formation FHIR

</details>

**Versions**

* `TInterop.23#1.0.0-alpha`


---

### Arkhn.core

<details>
<summary>Description</summary>

Arkhn profiles and extensions

</details>

**Versions**

* `Arkhn.core#0.0.2`


---

### CH eTOC (R4)

<details>
<summary>Description</summary>

Implementation guide for eTOC based on CH ORF (built Fri, May 17, 2024 12:54+0000+00:00)

</details>

**Versions**

* `ch.fhir.ig.ch-etoc#3.0.0-ballot`
* `ch.fhir.ig.ch-etoc#2.0.1`
* `ch.fhir.ig.ch-etoc#2.0.0`
* `ch.fhir.ig.ch-etoc#2.0.0-ballot`
* `ch.fhir.ig.ch-etoc#1.0.0`
* `ch.fhir.ig.ch-etoc#0.1.0`


---

### Patsientide üldandmete teenus / Master Patient Index



**Versions**

* `hl7.fhir.ee.mpi#1.0.0`


---

### de.dktk.oncology

<details>
<summary>Description</summary>

Data model of the German Cancer Consortium (DKTK) and the Comprehensive Cancer Center Network

</details>

**Versions**

* `de.dktk.oncology#1.3.0`
* `de.dktk.oncology#1.2.0`
* `de.dktk.oncology#1.1.1`
* `de.dktk.oncology#1.1.0`
* `de.dktk.oncology#1.0.5`
* `de.dktk.oncology#1.0.4`
* `de.dktk.oncology#1.0.3`
* `de.dktk.oncology#1.0.2`
* `de.dktk.oncology#1.0.1`
* `de.dktk.oncology#1.0.0`
* `de.dktk.oncology#0.9.0`
* `de.dktk.oncology#0.2.0`


---

### de.gevko.evo.ekb

<details>
<summary>Description</summary>

Umsetzung der elektronischen Heilmittelverordnung

</details>

**Versions**

* `de.gevko.evo.ekb#0.9.0`


---

### kl.dk.fhir.rehab

<details>
<summary>Description</summary>

Danish municipalities implementation guide for §140 rehabilitation reporting (built Fri, Jun 14, 2024 10:44+0200+02:00)

</details>

**Versions**

* `kl.dk.fhir.rehab#2.0.0`
* `kl.dk.fhir.rehab#1.0.0`


---

### de.medizininformatikinitiative.kerndatensatz.meta

<details>
<summary>Description</summary>

Medizininformatik Initiative - Kerndatensatz

</details>

**Versions**

* `de.medizininformatikinitiative.kerndatensatz.meta#1.0.3`


---

### surescripts.RecordLocatorExchange

<details>
<summary>Description</summary>

Record Locator & Exchange

</details>

**Versions**

* `surescripts.RecordLocatorExchange#1.0.0-beta`


---

### International Patient Access

<details>
<summary>Description</summary>

This IG  describes how an application acting on behalf of a patient can access information about the patient from an clinical records system using a FHIR based API. The clinical records system may be supporting a clinical care provider (e.g. a hospital, or a general practitioner), or a health data exchange, including a national health record system. (built Sun, Mar 26, 2023 20:50+0000+00:00)

</details>

**Versions**

* `hl7.fhir.uv.ipa#1.0.0`
* `hl7.fhir.uv.ipa#0.1.0`


---

### LOINC – IVD Test Code (LIVD) Mapping

<details>
<summary>Description</summary>

Recommended LOINC mappings for IVD Devices (built Thu, Dec 21, 2023 20:53+0000+00:00)

</details>

**Versions**

* `hl7.fhir.uv.livd#1.0.0-ballot`
* `hl7.fhir.uv.livd#0.3.0`
* `hl7.fhir.uv.livd#0.2.0`
* `hl7.fhir.uv.livd#0.1.0`


---

### Implementation Guide for fælleskommunal informationsmodel

<details>
<summary>Description</summary>

Danish municipalities implementation guide for common informationmodel (built Wed, Jun 7, 2023 17:46+0200+02:00)

</details>

**Versions**

* `kl.dk.fhir.core#1.2.0`


---

### CH ATC (R4)

<details>
<summary>Description</summary>

Implementation Guide for CH ATC (Audit Trail Consumption) (built Fri, May 17, 2024 08:56+0000+00:00)

</details>

**Versions**

* `ch.fhir.ig.ch-atc#3.3.0-ballot`
* `ch.fhir.ig.ch-atc#3.2.0`
* `ch.fhir.ig.ch-atc#3.2.0-ballot`
* `ch.fhir.ig.ch-atc#3.1.0`


---

### MII IG Fall

<details>
<summary>Description</summary>

Medizininformatik Initiative - Modul Fall

</details>

**Versions**

* `de.medizininformatikinitiative.kerndatensatz.fall#2024.0.1`
* `de.medizininformatikinitiative.kerndatensatz.fall#2024.0.0`
* `de.medizininformatikinitiative.kerndatensatz.fall#2024.0.0-ballot`
* `de.medizininformatikinitiative.kerndatensatz.fall#2024.0.0-alpha6`


---

### Central Cancer Registry Reporting Content IG

<details>
<summary>Description</summary>

The Central Cancer Registry Reporting Content IG provides healthcare organizations the necessary data exchange mechanisms to report cancer data to public health agencies. (built Fri, May 31, 2024 12:55+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.central-cancer-registry-reporting#1.0.0`
* `hl7.fhir.us.central-cancer-registry-reporting#0.1.0`


---

### Mobile access to Health Documents (MHD)

<details>
<summary>Description</summary>

ImplementationGuide for IHE IT Infrastructure Technical Framework Supplement Mobile access to Health Documents (MHD) (built Sat, May 18, 2024 12:30-0500-05:00)

</details>

**Versions**

* `ihe.iti.mhd#4.2.2`
* `ihe.iti.mhd#4.2.1`
* `ihe.iti.mhd#4.2.0`
* `ihe.iti.mhd#4.2.0-ballot`
* `ihe.iti.mhd#4.1.0`


---

### ca.on.oh.patient-summary

<details>
<summary>Description</summary>

Ontario Patient Summary

</details>

**Versions**

* `ca.on.oh.patient-summary#0.12.8`
* `ca.on.oh.patient-summary#0.12.7`
* `ca.on.oh.patient-summary#0.12.6`
* `ca.on.oh.patient-summary#0.12.5`
* `ca.on.oh.patient-summary#0.12.4`
* `ca.on.oh.patient-summary#0.12.3`
* `ca.on.oh.patient-summary#0.12.2`
* `ca.on.oh.patient-summary#0.12.1`
* `ca.on.oh.patient-summary#0.12.0`
* `ca.on.oh.patient-summary#0.11.3`
* `ca.on.oh.patient-summary#0.11.2`
* `ca.on.oh.patient-summary#0.11.1`
* `ca.on.oh.patient-summary#0.11.0`
* `ca.on.oh.patient-summary#0.10.0-alpha-9`
* `ca.on.oh.patient-summary#0.10.0-alpha-8`
* `ca.on.oh.patient-summary#0.10.0-alpha-6`
* `ca.on.oh.patient-summary#0.10.0-alpha-5`
* `ca.on.oh.patient-summary#0.10.0-alpha-4`
* `ca.on.oh.patient-summary#0.10.0-alpha-3`
* `ca.on.oh.patient-summary#0.10.0-alpha-2`
* `ca.on.oh.patient-summary#0.10.0-alpha-12`
* `ca.on.oh.patient-summary#0.10.0-alpha-11`
* `ca.on.oh.patient-summary#0.10.0-alpha-10`
* `ca.on.oh.patient-summary#0.10.0-alpha-1`
* `ca.on.oh.patient-summary#0.9.1-alpha-7`
* `ca.on.oh.patient-summary#0.9.1-alpha-6`
* `ca.on.oh.patient-summary#0.9.1-alpha-5`
* `ca.on.oh.patient-summary#0.9.1-alpha-2`
* `ca.on.oh.patient-summary#0.9.1-alpha-1`
* `ca.on.oh.patient-summary#0.0.9-alpha-2`
* `ca.on.oh.patient-summary#0.0.9-alpha-1`


---

### eRS.r4.assets

<details>
<summary>Description</summary>

e Referral Service

</details>

**Versions**

* `eRS.r4.assets#1.0.0`


---

### rl.fhir.r4

<details>
<summary>Description</summary>

Progetto FHIR per Regione Lombardia

</details>

**Versions**

* `rl.fhir.r4#0.0.12`
* `rl.fhir.r4#0.0.11`
* `rl.fhir.r4#0.0.10`
* `rl.fhir.r4#0.0.9`
* `rl.fhir.r4#0.0.8`
* `rl.fhir.r4#0.0.6`
* `rl.fhir.r4#0.0.5`
* `rl.fhir.r4#0.0.4`
* `rl.fhir.r4#0.0.3`
* `rl.fhir.r4#0.0.2`
* `rl.fhir.r4#0.0.1`


---

### kbv.mio.mutterpass

<details>
<summary>Description</summary>

Medizinische Informationsobjekte (MIO) Mutterpass V1.1.0

</details>

**Versions**

* `kbv.mio.mutterpass#1.1.0`
* `kbv.mio.mutterpass#1.1.0-kommentierungsphase`
* `kbv.mio.mutterpass#1.1.0-benehmensherstellung`


---

### ForgePatientChart.0830

<details>
<summary>Description</summary>

Put a description here

</details>

**Versions**

* `ForgePatientChart.0830#0.1.0`


---

### de.gematik.erezept-workflow.r4

<details>
<summary>Description</summary>

ePrescription workflow specification

</details>

**Versions**

* `de.gematik.erezept-workflow.r4#1.4.0-rc1`
* `de.gematik.erezept-workflow.r4#1.3.1`
* `de.gematik.erezept-workflow.r4#1.3.0`
* `de.gematik.erezept-workflow.r4#1.3.0-rc3`
* `de.gematik.erezept-workflow.r4#1.3.0-rc2`
* `de.gematik.erezept-workflow.r4#1.3.0-rc1`
* `de.gematik.erezept-workflow.r4#1.2.3`
* `de.gematik.erezept-workflow.r4#1.2.2`
* `de.gematik.erezept-workflow.r4#1.2.1`
* `de.gematik.erezept-workflow.r4#1.2.0`


---

### PACIO Personal Functioning and Engagement Implementation Guide

<details>
<summary>Description</summary>

FHIR Implementation Guide to exchange assessments of and data on a person's functioning, including body functions, activities, and participation, between post-acute care (PAC) and other providers, patients, and key stakeholders (built Fri, Jan 5, 2024 16:53+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.pacio-pfe#1.0.0`
* `hl7.fhir.us.pacio-pfe#1.0.0-ballot`


---

### FHIR.DGMC

<details>
<summary>Description</summary>

DGMC

</details>

**Versions**

* `FHIR.DGMC#0.1.0`


---

### Clinical Practice Guidelines (CPG) on EBMonFHIR



**Versions**

* `ebm-cpg.netzwerk-universitaetsmedizin.de#0.6.0`


---

### ntt.healthgenig

<details>
<summary>Description</summary>

Healthgen IG

</details>

**Versions**

* `ntt.healthgenig#0.0.4`
* `ntt.healthgenig#0.0.3`
* `ntt.healthgenig#0.0.2`


---

### ans.annuaire.fhir.r4

<details>
<summary>Description</summary>

Description de la structure des données d'identification des acteurs de santé exposées par les nouveaux services Annuaire Santé d'interrogation FHIR de l'Agence du Numérique en Santé (ANS). 

</details>

**Versions**

* `ans.annuaire.fhir.r4#0.2.0-pat23`


---

### DL.base

<details>
<summary>Description</summary>

Collection of Doctolibs base profiles

</details>

**Versions**

* `DL.base#1.1.0`


---

### Physical Activity Implementation Guide

<details>
<summary>Description</summary>

This implementation guide provides standardization around patient physical activity, including:&#x0a;* measures for recording a patient's level of physical activity;&#x0a;* measures to support assertions of physical activity, including device-based measures;&#x0a;* goals and care plans related to improving a patient's physical activity level;&#x0a;* orders for interventions seeking to improve a patient's physical activity level; and&#x0a;* processes to support closing the loop and evaluating the success of such interventions. (built Fri, Aug 25, 2023 15:28+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.physical-activity#1.0.0`
* `hl7.fhir.us.physical-activity#1.0.0-ballot`


---

### NRLF.poc

<details>
<summary>Description</summary>

NRL Futures API POC

</details>

**Versions**

* `NRLF.poc#1.0.1`


---

### Clinical Practice Guidelines

<details>
<summary>Description</summary>

Implementation guidance for creating Clinical Practice Guidelines with formal artifacts to facilitate sharing and implementation of the guideline (built Tue, Dec 19, 2023 19:42+0000+00:00)

</details>

**Versions**

* `hl7.fhir.uv.cpg#2.0.0-ballot`
* `hl7.fhir.uv.cpg#1.0.0`
* `hl7.fhir.uv.cpg#0.1.0`


---

### eHealth Platform Federal infsec Profiles

<details>
<summary>Description</summary>

eHealth Platform Federal infsec Profiles (built Wed, Jan 24, 2024 09:08+0100+01:00)

</details>

**Versions**

* `hl7.fhir.be.infsec#1.1.0`
* `hl7.fhir.be.infsec#1.0.0`


---

### sfm.030521

<details>
<summary>Description</summary>

Medication related projects (SFM, PLL, SAFEST, KIKJ)

</details>

**Versions**

* `sfm.030521#1.0.1`


---

### Guía de Implementación Receta-CL

<details>
<summary>Description</summary>

Guía de Implementación para los perfiles de Receta complementarios a los de CORE Nacional, para su uso en Recete Electrónica Nacional. Esta Guía se encuentra aún en proceso de creación por lo que esta versión esta sujeta a observaciones y cambios. Aquellos implementadores que tengan acceso a ella se les sugiere desarrollar comentarios para su mejora) (built Thu, Sep 29, 2022 18:39-0300-03:00)

</details>

**Versions**

* `hl7.fhir.cl.recetachile#0.9`


---

### kbv.ita.erp

<details>
<summary>Description</summary>

Umsetzung des elektronischen Rezepts

</details>

**Versions**

* `kbv.ita.erp#1.1.2`
* `kbv.ita.erp#1.1.1`
* `kbv.ita.erp#1.1.0`
* `kbv.ita.erp#1.1.0-PreRelease`
* `kbv.ita.erp#1.0.2`


---

### QI-Core Implementation Guide

<details>
<summary>Description</summary>

The QICore Implementation Guide defines a set of FHIR profiles with extensions and bindings needed to create interoperable, quality-focused applications. The profiles in this implementation guide derive from and extend the [US Core](http://hl7.org/fhir/us/core) profiles to provide a common foundation for building, sharing, and evaluating knowledge artifacts across quality improvement efforts in the US Realm. (built Fri, Mar 1, 2024 18:46+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.qicore#6.0.0`
* `hl7.fhir.us.qicore#6.0.0-ballot`
* `hl7.fhir.us.qicore#5.0.0`
* `hl7.fhir.us.qicore#5.0.0-ballot`
* `hl7.fhir.us.qicore#4.9.0`
* `hl7.fhir.us.qicore#4.1.1`
* `hl7.fhir.us.qicore#4.1.0`
* `hl7.fhir.us.qicore#4.0.0`
* `hl7.fhir.us.qicore#3.3.0`
* `hl7.fhir.us.qicore#3.2.0`
* `hl7.fhir.us.qicore#3.1.0`
* `hl7.fhir.us.qicore#2.1.0`
* `hl7.fhir.us.qicore#2.0.0`
* `hl7.fhir.us.qicore#1.6.0`


---

### HIE2.packages

<details>
<summary>Description</summary>

Malaysia Health Information Exchange

</details>

**Versions**

* `HIE2.packages#0.1.1`


---

### phis.ig.dev

<details>
<summary>Description</summary>

2023. 10. 26. FHIR IG 생성 테스트 목적

</details>

**Versions**

* `phis.ig.dev#0.1.7`
* `phis.ig.dev#0.1.6`
* `phis.ig.dev#0.1.5`
* `phis.ig.dev#0.1.4`
* `phis.ig.dev#0.1.3`
* `phis.ig.dev#0.1.2`
* `phis.ig.dev#0.1.1`
* `phis.ig.dev#0.1.0`
* `phis.ig.dev#0.0.9`
* `phis.ig.dev#0.0.8`
* `phis.ig.dev#0.0.7`
* `phis.ig.dev#0.0.6`
* `phis.ig.dev#0.0.5`
* `phis.ig.dev#0.0.4`
* `phis.ig.dev#0.0.3`
* `phis.ig.dev#0.0.2`
* `phis.ig.dev#0.0.1`


---

### difuture.trustcenter.sp

<details>
<summary>Description</summary>

FHIR Resources for MII

</details>

**Versions**

* `difuture.trustcenter.sp#1.0.0-beta-3`
* `difuture.trustcenter.sp#1.0.0-beta-2`


---

### amwell.fhir.profiles

<details>
<summary>Description</summary>

profiles for amwell 

</details>

**Versions**

* `amwell.fhir.profiles#2.0.0`
* `amwell.fhir.profiles#1.0.1-preview`
* `amwell.fhir.profiles#1.0.0-preview`


---

### de.aqua.ebi

<details>
<summary>Description</summary>

Die elektronische Behandlungsinformation der Knappschaft.

</details>

**Versions**

* `de.aqua.ebi#0.9.0`


---

### KBV.ITA.ERP

<details>
<summary>Description</summary>

Umsetzung des elektronischen Rezepts

</details>

**Versions**

* `KBV.ITA.ERP#1.0.1`
* `KBV.ITA.ERP#1.0.0`


---

### National Healthcare Directory Exchange

<details>
<summary>Description</summary>

National Directory (built Tue, Aug 9, 2022 16:36+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.directory-exchange#1.0.0-ballot`


---

### Non-patient File Sharing (NPFS)

<details>
<summary>Description</summary>

The Non-Patient File Sharing (NPFS) Profile defines how to share non-patient files such as clinical workflow definitions, domain policies, and stylesheets. Those files can be created and consumed by many different systems involved in a wide variety of data sharing workflows. (built Thu, Nov 16, 2023 16:06-0600-06:00)

</details>

**Versions**

* `ihe.iti.npfs#2.2.0`
* `ihe.iti.npfs#2.2.0-comment`


---

### ee.tehik.mpi

<details>
<summary>Description</summary>

Estonian Patients

</details>

**Versions**

* `ee.tehik.mpi#0.1.0-beta`


---

### hie2.packages

<details>
<summary>Description</summary>

Malaysia Health Information Exchange

</details>

**Versions**

* `hie2.packages#1.0.1`


---

### KBV.MIO.Impfpass

<details>
<summary>Description</summary>

Medizinische Informationsobjekte (MIO) Impfpass V1.1.0-Kommentierung

</details>

**Versions**

* `KBV.MIO.Impfpass#1.1.0-Kommentierung`
* `KBV.MIO.Impfpass#1.00.000`


---

### HL7 FHIR Profile: Occupational Data for Health (ODH), Release 1, STU 1.3 (Standard for Trial Use)

<details>
<summary>Description</summary>

**HL7 FHIR Profile: Occupational Data for Health (ODH), Release 1.3 (Standard for Trial Use)**

This Implementation Guide is a reconciled version, containing changes in response to comments received in the Sept. 2018 ballot. It has been updated to FHIR R4.0.1.

**Introduction and Guidance**

This Implementation Guide (IG) contains profiles to implement support for Occupational Data for Health (ODH). ODH describes structured work information primarily designed to facilitate clinical care, including population health and value-based care. ODH also can be used to support public health reporting. ODH is not designed to support billing activities. 
This set of FHIR profiles is specified as a composition resource, but it is not intended to be used as a stand-alone composition. Rather, the desired content should be included in broader IGs and available as a response to requests for ODH information. Some use cases may leverage only a subset of the ODH profiles, and these should be specified within those work products. For instance, in the Vital Records Death Reporting (VRDR) IG, the data requirements for work information are limited to  those in the Usual Work profile.
While this profile is specified for the US Realm, the design is intended to also support international needs. Three of the referenced value sets—Occupation, Industry, and Supervisory Level— are necessarily US specific. The remaining three value sets—Work Schedule, Employment Status, and Work Classification—use international concepts. Input is requested regarding whether these should be specified as ‘extensible’, ‘required’ (using ‘text only’ where a concept does not yet exist such as a new occupation), or as an ‘example’ for those US specific concepts.

**Background**

The majority of adults in the U.S. spend more than half their waking hours at work. Therefore, health and work are inextricably inter-related. For example, the management of chronic conditions requires taking the patient’s work environment into consideration. Work-related conditions are often first brought to the attention of a primary care provider. Some conditions related to exposure to hazards in the workplace can have a long latency, requiring knowledge of a person’s work history for recognition, diagnosis, and treatment. The recognition of new conditions related to previously unknown workplace hazards has often come from astute clinicians, which requires knowledge of the patient’s work. ONC has indicated recognition of the value of work information for health care.
The incorporation of ODH into Electronic Health Records (EHRs) and other health IT systems presents an opportunity to improve health in relation to work. ODH provides a structure and standardization for work information that can be used across systems to take advantage of system tools for clinical decision support, population health, and public health. Research has been conducted and guidance is available to support clinicians, and the use of ODH by health IT systems can support identification of patients that would benefit the most from this knowledge.

**Scope**

The Occupational Data for Health (ODH) FHIR IG covers information about a patient’s work, including some voluntary work, or a patient’s household members’ work. ODH is designed for the social history section of a medical record, to facilitate clinical care in multiple disciplines and delivery environments. ODH can be used for clinical decision support, population health activities and value-based care, and public health reporting. The scope of the work information in ODH includes:

• Employment Status

• Retirement Date

• Combat Zone Period

• Past or Present Job for the patient or a household member, which includes:
  
  o Past or Present Job Occupation
  
  o Past or Present Job Industry
  
  o Work Classification
  
  o Work Schedule, which includes:
    
    - Weekly Work Days
    
    - Daily Work Hours
  
  o Job Duty
  
  o Occupational Hazard
  
  o Employer name
  
  o Employer address
  
  o Related Subject (when it is Past or Present Job of a household member of the person)
  
  o Start/End Dates

• Usual Work of the patient or a household member, which includes:
  
  o Usual Occupation
  
  o Usual Industry
  
  o Usual Occupation Duration
  
  o Related Subject (when it is Usual Work of a household member of the person)
  
  o Start Date

**Known Issues and Limitations**

This IG includes more extensive occupational data than typically collected in current systems. The content and structure of this IG is intended to inform clinical care, support population health, and contribute to public health activities. While there may be some overlap with administrative and billing information maintained by some systems, the information in this IG is not designed to support billing and administrative needs.
While multiple retirement dates are supported, the retirement date is not linked to any specific job, or usual occupation.

**Credits**

Co-Editor:
Lori Reed-Fourquet
e-HealthSign, LLC
lfourquet@ehealthsign.com

Co-Editor:
Rob Hausam
Hausam Consulting
rob@hausamconsulting.com

Co-Editor:
Mark Kramer
MITRE Corporation
mkramer@mitre.org

This set of FHIR profiles was produced and developed through the efforts of a project of the National Institute of Occupational Safety and Health (NIOSH), the U.S. federal agency responsible for conducting research and making recommendations for the prevention of work-related injury and illness. NIOSH is a part of the U.S. Centers for Disease Control and Prevention (CDC). NIOSH consulted stakeholders in clinical care, public health, health IT, health informatics and  U.S. government agencies to develop ODH. The HL7® Public Health and Emergency Response Work Group sponsored development of this set of FHIR profiles. Co-sponsoring HL7® Work Groups were Orders and Observations (OO), Patient Administration (PA), and Clinical Quality Initiative (CQI).
The following individuals provided subject matter expertise for this set of FHIR profiles: Genevieve Barkocy Luensman, NIOSH; Eileen Storey, Professional Services Partners, formerly NIOSH; Margaret S. Filios, NIOSH; Christina Socias-Morales, NIOSH; Lauren Brewer, NIOSH; Barbara Wallace, Professional Services Partners.

**Authors**

Name Email/URL
HL7 International - Public Health http://www.hl7.org/Special/committees/pher (built Mon, Mar 27, 2023 09:54+1100+11:00)

</details>

**Versions**

* `hl7.fhir.us.odh#1.3.0`
* `hl7.fhir.us.odh#1.2.0`
* `hl7.fhir.us.odh#1.1.0`
* `hl7.fhir.us.odh#1.0.0`


---

### Sharing Valuesets, Codes, and Maps (SVCM)

<details>
<summary>Description</summary>

The Sharing Valuesets, Codes, and Maps (SVCM) Profile defines a lightweight interface through which healthcare systems may retrieve centrally managed uniform nomenclature and mappings between code systems based on the HL7 Fast Healthcare Interoperability Resources (FHIR) specification. (built Wed, Aug 2, 2023 12:46-0500-05:00)

</details>

**Versions**

* `ihe.iti.svcm#1.5.1`
* `ihe.iti.svcm#1.5.0`
* `ihe.iti.svcm#1.4.0`


---

### NHSN Healthcare Associated Infection (HAI) Reports Long Term Care Facilities

<details>
<summary>Description</summary>

This implementation guide (IG) specifies standards for electronic submission of Healthcare Associated Infection (HAI) Long Term Care Facilities (LTCF) reports to the National Healthcare Safety Network (NHSN) of the Centers for Disease Control and Prevention (CDC). This IG contains a library of FHIR profiles for electronic submission of HAI LTCF reports to the NHSN.

As reports are modified and new report types are defined, CDC and Health Level Seven (HL7) will develop and publish additional constraints.

Throughout this process, CDC remains the authority on NHSN data collection protocols. When healthcare enterprises choose to participate in NHSN, they must report to CDC reportable events such as identified MDRO (multidrug-resistant organism) or CDI (C. difficile infection) occurrences such as specific reportable procedures, even those without complications, and events such as a bloodstream infection, either confirmed by a positive blood culture or supported by a patients clinical symptoms. This specification opens the channel for data submission by all applications compliant with the data coding requirements defined here.

Note that participation in the NHSN requires enrollment and filing of reporting plans, which are not defined by this specification. For an overview of NHSN and full information on NHSN participation requirements, see: [http://www.cdc.gov/nhsn](http://www.cdc.gov/nhsn). Provisions of the Public Health Service Act protect all data reported to NHSN from discovery through the Freedom of Information Act (FOIA).


### Relationship to Another Standard

HL7 has developed this FHIR implementation guide in parallel with the CDA implementation guide. We anticipate several STU releases on the path to a Normative Release 1 of the HL7 implementation guides for CDA and FHIR for Healthcare Associated Infection (HAI) Reports from Long Term Care Facilities (LTCF). The FHIR and CDA implementation guides will align. A change to one standard will require the same change in the other standard. 

In this release, the new forms included in both the CDA and FHIR standards are:
* **NHSN HAI LTCF Population Summary Report**: MDRO and CDI LabID Event Reporting Monthly Summary Data for LTCF
* **NHSN HAI LTCF Single-Person Event Report**: Laboratory-identified MDRO or CDI Event for LTCF

For further details see the [NHSN website](https://www.cdc.gov/nhsn/) for reporting healthcare-associated infections in long-term care facilities.


### Audience

The audience for this work is all developers of software systems who want to enable their systems for reporting HAI data to the NHSN.

### Change Notification Process

CDC maintains an e-mail list of contacts at organizations interested in or responsible for implementations of FHIR for LTCF HAI reporting to NHSN. To be added to the list, send a request with your contact information to nhsncda@cdc.gov. CDC uses the list for e-mail notifications of changes, including new data requirements. Changes may apply to this IG and to other documents such as business rules that are needed to implement and support FHIR for LTCF HAI reporting to NHSN. NHSN CDA related information may be found at https://www.cdc.gov/nhsn/cdaportal/index.html.  

### Acknowledgements

This implementation guide was produced and developed by Lantana Consulting Group in conjunction with the Division of Healthcare Quality Promotion in the National Center for Emerging and Zoonotic Infectious Diseases (NCEZID) at the Centers for Disease Control and Prevention (CDC). Its development and deployment are results of the dedication of the team—led by Daniel A. Pollock, M.D., Surveillance Branch Chief, Division of Healthcare Quality Promotion, NCEZID, CDC and  Jeneita Bell, MD, MPH, Long-term Care Team Lead, DHQP, NCEZID, CDC—and their support of the development of interoperable data standards for the CDC’s National Healthcare Safety Network (NHSN).  

Special thanks and acknowledgment to stakeholders who participated in calls and provided feedback. Specifically, we would like to thank  Cindy Frakes, Steve Herron, Jamie Gatzke, Kelly Luden, Prasath Govindarajulu from Cerner;  Laura Ditz, Nancy Chi, Nichole (Nicki) Fetterman, Michael Furman, Patti Barton, Aga Lee from Point Click Care; Donna Doneski from NASL; and  Denise Wassenaar, Doc DeVore, Rob Price from Matrix  Care. 

The best standards are those driven by business requirements. A strong set of Healthcare Associated Infection (HAI) surveillance application vendors monitor, evaluate, and test each release of this guide.  


|-----|-----|-----|-----| 

|Primary Editor:|Sarah Gaunt|Lantana Consulting Group|sarah.gaunt@lantanagroup.com| 
|Primary Editor:|Zabrina Gonzaga|Lantana Consulting Group|zabrina.gonzaga@lantanagroup.com| 
|Primary Editor:|Dave deRoode|Lantana Consulting Group|david.deroode@lantanagroup.com| 
|Co-Editor:|Jeneita Bell, MD, MPH|CDC|hpq8@cdc.gov| 
|Co-Editor:|Angella Antilla PhD, MSN|CDC|vtb9@cdc.gov| 
|Co-Editor:|Daniel Pollock, M.D.|CDC|DPollock@cdc.gov| 
|Co-Editor:|Ahmed Tahir|Leidos Consultant to CDC/NHSN|nmn8@cdc.gov| 
|Co-Editor:|Mindy Durrance|Leidos Consultant to CDC/NHSN|mdq1@cdc.gov| 
|Co-Editor:|Sheri Chernetsky Tejedor, MD|CDC|yei9@cdc.gov| 
|Co-Editor:|Sheila Abner|CDC|sha8@cdc.gov| 
|Co-Chair:|Erin Holt MPH|Tennessee Department of Health|erin.holt@tn.gov| 
|Co-Chair:|Laura Rappleye|Altarum|laura.rappleye@altarum.org| 
|Co-Chair:|Craig Newman|Altarum|craig.newman@altarum.org| 
|Co-Chair:|Danny Wise|Allscripts|danny.wise@allscripts.com| 
|Co-Chair:|Joginder Madra|Madra Consulting Inc.|hl7@madraconsulting.com| 
|Co-Chair:|Gaye Dolin M.S.N., R.N. |Intelligent Medical Objects |gdolin@imo-online.com| 
|Co-Chair:|Calvin Beebe|Mayo Clinic|cbeebe@mayo.edu| 
|Co-Chair:|Austin Kreisler|Leidos Consultant to CDC/NHSN|duz1@cdc.gov| 
|Co-Chair:|Andrew Statler|Cerner Corp|andrew.statler@cerner.com| 
|Co-Chair:|Sean McIlvenna| Lantana Consulting Group|sean.mcilvenna@lantanagroup.com| 
|Co-Chair:|Benjamin Flessner|Redox|benjamin@redoxengine.com| 
|Co-Editor:|Beau Bannerman|Lantana Consulting Group|beau.bannerman@lantanagroup.com| 
|Technical Editor:|Diana Wright|Lantana Consulting Group|diana.wright@lantanagroup.com| 
|Technical Editor:|Chris Hannigan|Lantana Consulting Group|chris.hannigan@lantanagroup.com| 
 (built Wed, Apr 19, 2023 14:58+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.hai-ltcf#1.1.0`
* `hl7.fhir.us.hai-ltcf#1.0.0`
* `hl7.fhir.us.hai-ltcf#0.1.0`


---

### FHIR implementation of ELZ

<details>
<summary>Description</summary>

NL package of FHIR R4 conformance resources for ELZ.

</details>

**Versions**

* `nictiz.fhir.nl.r4.elz#0.2.0-beta.1`
* `nictiz.fhir.nl.r4.elz#0.1.0-alfa.2`
* `nictiz.fhir.nl.r4.elz#0.1.0-alfa.1`


---

### de.gevko.dev.tetvz

<details>
<summary>Description</summary>

Put a description here

</details>

**Versions**

* `de.gevko.dev.tetvz#1.1.1`
* `de.gevko.dev.tetvz#1.1.0`
* `de.gevko.dev.tetvz#1.0.14`
* `de.gevko.dev.tetvz#1.0.13`
* `de.gevko.dev.tetvz#1.0.12`
* `de.gevko.dev.tetvz#1.0.11`
* `de.gevko.dev.tetvz#1.0.10`
* `de.gevko.dev.tetvz#1.0.9`
* `de.gevko.dev.tetvz#1.0.8`
* `de.gevko.dev.tetvz#1.0.7`
* `de.gevko.dev.tetvz#1.0.6`
* `de.gevko.dev.tetvz#1.0.5`
* `de.gevko.dev.tetvz#1.0.4`
* `de.gevko.dev.tetvz#1.0.3`
* `de.gevko.dev.tetvz#1.0.2`
* `de.gevko.dev.tetvz#1.0.1`
* `de.gevko.dev.tetvz#1.0.0`


---

### kbv.ita.eau

<details>
<summary>Description</summary>

Umsetzung der elektronischen Arbeitsunfähigkeit

</details>

**Versions**

* `kbv.ita.eau#1.1.1`
* `kbv.ita.eau#1.1.0`
* `kbv.ita.eau#1.1.0-PreRelease`


---

### nictiz.fhir.nl.r4.cio

<details>
<summary>Description</summary>

Put a description here

</details>

**Versions**

* `nictiz.fhir.nl.r4.cio#1.0.0-beta.2`
* `nictiz.fhir.nl.r4.cio#1.0.0-beta.1`


---

### Health New Zealand Te Whatu Ora Digital Tooling Implementation Guide

<details>
<summary>Description</summary>

FHIR profiles used in FHIR Publishing processes within Health New Zealand Te Whatu Ora (built Sat, Jun 15, 2024 00:39+0000+00:00)

</details>

**Versions**

* `tewhatuora.digitaltooling.ig#0.0.3`
* `tewhatuora.digitaltooling.ig#0.0.2`


---

### Quality Measure Implementation Guide (STU5)

<details>
<summary>Description</summary>

The Fast Healthcare Interoperability Resource (FHIR) Quality Measure Implementation Guide (this IG) describes an approach to representing Quality Measures (QMs) using the FHIR Clinical Reasoning Module and Clinical Quality Language (CQL) in the US Realm. However, this Implementation Guide can be usable for multiple use cases across domains, and much of the content is likely to be usable outside the US Realm. (built Tue, Apr 9, 2024 13:15+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.cqfmeasures#5.0.0-ballot2`
* `hl7.fhir.us.cqfmeasures#5.0.0-ballot`
* `hl7.fhir.us.cqfmeasures#4.0.0`
* `hl7.fhir.us.cqfmeasures#4.0.0-ballot`
* `hl7.fhir.us.cqfmeasures#3.0.0`
* `hl7.fhir.us.cqfmeasures#2.1.0`
* `hl7.fhir.us.cqfmeasures#2.0.0`
* `hl7.fhir.us.cqfmeasures#1.1.0`
* `hl7.fhir.us.cqfmeasures#0.1.0`


---

### resource.versioning

<details>
<summary>Description</summary>

For demonstrating purposes only

</details>

**Versions**

* `resource.versioning#0.2.0-beta`
* `resource.versioning#0.1.0-alpha`


---

### Integrated Reporting Applications

<details>
<summary>Description</summary>

The Integrated Reporting Applications (IRA) profile helps applications that are used together during reporting (e.g., image display, report creator, clinical applications, AI tools, etc) to share information using a standard called FHIRcast. Each application can share what it is doing and the data it is creating, referred to as Context and Content, respectively. Other applications are notified so they can then intelligently synchronize their behavior or use the new data. (built Tue, Oct 3, 2023 19:27-0500-05:00)

</details>

**Versions**

* `ihe.rad.ira#1.0.0`
* `ihe.rad.ira#1.0.0-comment`


---

### Pharmacist Services and Summaries - FHIR (PhCP)

<details>
<summary>Description</summary>

This document describes constraints on the the FHIR Composition resource for a Pharmacist Care Plan, which are derived from requirements set forth by the Pharmacy Health Information Technology (HIT) Collaborative1 and the National Council for Prescription Drug Programs (NCPDP) WG10 Professional Pharmacy Services,2 vendors, and Health Level Seven (HL7) stakeholder workgroups. Templates in this US Realm implementation guide are specific to pharmacy management treatment and interventions that will promote interoperability and will create information suitable for reuse in quality measurement, public health reporting, research, and reimbursement.

This guide contains a library of Fast Health Interoperability Resources (FHIR) profiles, and is compliant with the  C-CDA on FHIR specification.

### Content and Organization

* [Background](background.html): describes the purpose, audience, general background, and use cases for this guide
* [Specification](specification.html): covers the detailed implementation requirements and conformance expectation
* [Acknowledgments](acknowledgments.html): identifies the individuals and organizations involved in developing this implementation guide
* [Downloads](downloads.html): allows download of this and other specifications as well as other useful files
* [Artifacts Summary](artifacts.html): introduces and provides links to the FHIR STU3 and R4 profiles, search parameters and other FHIR artifacts used in this implementation guide
 (built Thu, Feb 11, 2021 17:05+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.phcp#1.0.0`
* `hl7.fhir.us.phcp#0.2.0`


---

### ca.on.oh.mha-pds

<details>
<summary>Description</summary>

Ontario Mental Health and Addictions Provincial Data Set

</details>

**Versions**

* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.31`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.30`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.29`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.28`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.27`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.26`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.25`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.24`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.23`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.22`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.21`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.20`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.19`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.18`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.17`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.16`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.15`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.14`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.13`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.11`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.10`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.9`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.8`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.7`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.6`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.5`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.4`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.3`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.2`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.1`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.6`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.4`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.3`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.2`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.1`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.8`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.7`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.6`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.5`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.4`


---

### Point-of-Care Device Implementation Guide

<details>
<summary>Description</summary>

ImplementationGuide for Point-of-Care Devices (PoCD), such as those typically found in a hospital care setting (e.g., physiological monitors, infusion pumps, ventilators, pulse-oximeters, etc.). (built Sat, Aug 14, 2021 20:37+0000+00:00)

</details>

**Versions**

* `hl7.fhir.uv.pocd#0.3.0`
* `hl7.fhir.uv.pocd#0.2.0`


---

### kbv.ita.aws

<details>
<summary>Description</summary>

PVS-Archivierungs- und Wechsel-Schnittstelle Gemäß § 371 Absatz 1

</details>

**Versions**

* `kbv.ita.aws#1.2.0`


---

### de.medizininformatikinitiative.kerndatensatz.consent

<details>
<summary>Description</summary>

Dies ist ein Bug-Fix, siehe https://github.com/medizininformatik-initiative/kerndatensatzmodul-consent/issues/14

</details>

**Versions**

* `de.medizininformatikinitiative.kerndatensatz.consent#1.0.7`
* `de.medizininformatikinitiative.kerndatensatz.consent#1.0.6`
* `de.medizininformatikinitiative.kerndatensatz.consent#1.0.5`
* `de.medizininformatikinitiative.kerndatensatz.consent#1.0.4`
* `de.medizininformatikinitiative.kerndatensatz.consent#1.0.3`
* `de.medizininformatikinitiative.kerndatensatz.consent#1.0.2`
* `de.medizininformatikinitiative.kerndatensatz.consent#1.0.1`
* `de.medizininformatikinitiative.kerndatensatz.consent#1.0.0-ballot1`


---

### Common Data Models Harmonization

<details>
<summary>Description</summary>

CDMH Maps data elements and classes between FHIR and Sentinel, i2b2, PCORnet CDM, OMOP research models. (built Mon, Dec 6, 2021 18:52+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.cdmh#1.0.0`


---

### SMART Web Messaging

<details>
<summary>Description</summary>

The SMART Web Messaging Implementation Guide enables SMART-launched apps a standard way to communicate with the fixture that launched the app. (built Fri, May 6, 2022 17:41+0000+00:00)

</details>

**Versions**

* `hl7.fhir.uv.smart-web-messaging#1.0.0`


---

### Single Institutional Review Board (sIRB) Implementation Guide

<details>
<summary>Description</summary>

Data standards to move data and documents from clinical research sites to a single ethics review board in support of the "NIH Policy on the Use of a Single Institutional Review Board for Multi-Site Research. (built Thu, Apr 20, 2023 13:43+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.sirb#1.0.0`
* `hl7.fhir.us.sirb#0.1.0`


---

### de.gematik.fhir.atf

<details>
<summary>Description</summary>

A framework for applications that use KIM or TI-Messenger for end to end data transport

</details>

**Versions**

* `de.gematik.fhir.atf#1.3.0`
* `de.gematik.fhir.atf#1.2.0`
* `de.gematik.fhir.atf#1.1.0`
* `de.gematik.fhir.atf#1.0.4`
* `de.gematik.fhir.atf#1.0.3`
* `de.gematik.fhir.atf#1.0.2`
* `de.gematik.fhir.atf#1.0.1`
* `de.gematik.fhir.atf#1.0.0`


---

### Pseudo.Frog

<details>
<summary>Description</summary>

PseudoFrog Profiling

</details>

**Versions**

* `Pseudo.Frog#1.0.0`


---

### CH EMED (R4)

<details>
<summary>Description</summary>

Implementation Guide for the eMedication in Switzerland. (built Thu, May 16, 2024 19:01+0000+00:00)

</details>

**Versions**

* `ch.fhir.ig.ch-emed#5.0.0-ballot`
* `ch.fhir.ig.ch-emed#4.0.1`
* `ch.fhir.ig.ch-emed#4.0.0`
* `ch.fhir.ig.ch-emed#4.0.0-ballot`
* `ch.fhir.ig.ch-emed#3.0.0`
* `ch.fhir.ig.ch-emed#2.1.0`
* `ch.fhir.ig.ch-emed#2.0.0`
* `ch.fhir.ig.ch-emed#1.0.0`
* `ch.fhir.ig.ch-emed#0.2.0`
* `ch.fhir.ig.ch-emed#0.1.1`
* `ch.fhir.ig.ch-emed#0.1.0`


---

### signal.core.r4

<details>
<summary>Description</summary>

Signal Managed Service Organization

</details>

**Versions**

* `signal.core.r4#0.2.8`
* `signal.core.r4#0.2.7`
* `signal.core.r4#0.2.6`
* `signal.core.r4#0.2.5`
* `signal.core.r4#0.1.4`
* `signal.core.r4#0.1.3-preview-20230707`
* `signal.core.r4#0.1.2-preview-20230628`
* `signal.core.r4#0.1.1-beta-1`


---

### kbv.mio.diga

<details>
<summary>Description</summary>

Medizinische Informationsobjekte (MIO) DiGA Toolkit V1.1.0

</details>

**Versions**

* `kbv.mio.diga#1.1.0`
* `kbv.mio.diga#1.1.0-kommentierung`
* `kbv.mio.diga#1.1.0-benehmensherstellung`
* `kbv.mio.diga#1.0.0-festlegung`
* `kbv.mio.diga#1.0.0-benehmensherstellung`


---

### FHIR Extensions Pack

<details>
<summary>Description</summary>

This IG defines the global extensions - the ones defined for everyone. These extensions are always in scope wherever FHIR is being used (built Sat, Apr 27, 2024 18:39+1000+10:00)

</details>

**Versions**

* `hl7.fhir.uv.extensions.r5#5.1.0`
* `hl7.fhir.uv.extensions.r5#1.0.0`


---

### de.basisprofil.r4

<details>
<summary>Description</summary>

Projekt Basisprofilierung R4 (HL7 Deutschland e.V.)

</details>

**Versions**

* `de.basisprofil.r4#1.5.0-ballot2`
* `de.basisprofil.r4#1.5.0-ballot`
* `de.basisprofil.r4#1.5.0-alpha9`
* `de.basisprofil.r4#1.5.0-alpha6`
* `de.basisprofil.r4#1.5.0-alpha5`
* `de.basisprofil.r4#1.5.0-alpha4`
* `de.basisprofil.r4#1.5.0-alpha3`
* `de.basisprofil.r4#1.5.0-alpha2`
* `de.basisprofil.r4#1.5.0-alpha10`
* `de.basisprofil.r4#1.5.0-alpha1`
* `de.basisprofil.r4#1.4.0`


---

### Implementierungsleitfaden DEMIS - Erkrankungsmeldung

<details>
<summary>Description</summary>

Beinhaltet die in DEMIS für die Umsetzung der Arztmeldung genutzten Informationsmodellartefakte

</details>

**Versions**

* `rki.demis.disease#1.3.0`
* `rki.demis.disease#1.2.0`
* `rki.demis.disease#1.1.0`


---

### Order Catalog Implementation Guide

<details>
<summary>Description</summary>

An Order Catalog is an administered homogeneous collection of items such as medication products, laboratory tests, procedures, medical devices or knowledge artifacts such as order sets, which support the ordering process, or more generally healthcare processes. (built Sun, Aug 9, 2020 15:18+0000+00:00)

</details>

**Versions**

* `hl7.fhir.uv.order-catalog#0.1.0`


---

### de.gecco

<details>
<summary>Description</summary>

Nationales Forschungsnetzwerk der Universitätsmedizin zu Covid-19

</details>

**Versions**

* `de.gecco#1.0.5-rc.3`
* `de.gecco#1.0.4-rc.3`
* `de.gecco#1.0.5-rc.2`
* `de.gecco#1.0.4-rc.2`
* `de.gecco#1.0.5-rc.1`
* `de.gecco#1.0.4-rc.1`
* `de.gecco#1.0.5`
* `de.gecco#1.0.4`
* `de.gecco#1.0.3`
* `de.gecco#1.0.2`
* `de.gecco#1.0.1`
* `de.gecco#1.0.0`
* `de.gecco#0.9.4`
* `de.gecco#0.9.3`
* `de.gecco#0.9.2`
* `de.gecco#0.9.1`
* `de.gecco#0.9.0`


---

### geosalud.UY.implementationGuide

<details>
<summary>Description</summary>

Building an Implementation Guide and Package with Simplifier.net

</details>

**Versions**

* `geosalud.UY.implementationGuide#0.0.1-preview`


---

### FHIR Shorthand

<details>
<summary>Description</summary>

Describes FHIR Shorthand (FSH), a domain-specific language (DSL) for defining the content of FHIR Implementation Guides (IG). (built Mon, Oct 12, 2020 15:26+1100+11:00)

</details>

**Versions**

* `hl7.fhir.uv.shorthand#1.0.0`
* `hl7.fhir.uv.shorthand#0.12.0`


---

### NTT.HealthgenIG

<details>
<summary>Description</summary>

Healthgen IG

</details>

**Versions**

* `NTT.HealthgenIG#0.0.1`


---

### ans.cisis.nde.fhir.r4

<details>
<summary>Description</summary>

Ressources de conformité (profils, extensions, capability statements...) du volet Notification d'évènements (NdE) FHIR R4 du cadre d'interopérabilité des systèmes d'information de santé, CI-SIS, de l'ANS

</details>

**Versions**

* `ans.cisis.nde.fhir.r4#1.0.0`


---

### ishmed.i14y.r4.de

<details>
<summary>Description</summary>

i.s.h.med FHIR R4 Germany API Endpoint

</details>

**Versions**

* `ishmed.i14y.r4.de#3.0.0`
* `ishmed.i14y.r4.de#2.0.0`


---

### de.gevko.evo.hlm

<details>
<summary>Description</summary>

Umsetzung der elektronischen Heilmittelverordnung

</details>

**Versions**

* `de.gevko.evo.hlm#1.3.0`
* `de.gevko.evo.hlm#1.2.1`
* `de.gevko.evo.hlm#1.2.0`
* `de.gevko.evo.hlm#1.1.0`
* `de.gevko.evo.hlm#1.0.3`
* `de.gevko.evo.hlm#1.0.2`
* `de.gevko.evo.hlm#1.0.1`
* `de.gevko.evo.hlm#1.0.0`
* `de.gevko.evo.hlm#0.9.4`
* `de.gevko.evo.hlm#0.9.3`
* `de.gevko.evo.hlm#0.9.2`
* `de.gevko.evo.hlm#0.9.1`
* `de.gevko.evo.hlm#0.9.0`


---

### DaVinci Payer Data Exchange (PDex) US Drug Formulary



**Versions**

* `hl7.fhir.us.Davinci-drug-formulary#1.0.0`
* `hl7.fhir.us.Davinci-drug-formulary#0.1.0`


---

### Clinical Study Schedule of Activities

<details>
<summary>Description</summary>

FHIR Implementation Guide representing a Clinical Study Schedule of Activities (built Tue, Apr 18, 2023 16:34+0000+00:00)

</details>

**Versions**

* `hl7.fhir.uv.vulcan-schedule#1.0.0`
* `hl7.fhir.uv.vulcan-schedule#1.0.0-ballot`


---

### phis.ig.createtest

<details>
<summary>Description</summary>

2023. 10. 26. FHIR IG 생성 테스트 목적

</details>

**Versions**

* `phis.ig.createtest#0.0.2`


---

### eReferralOntarioNew.core

<details>
<summary>Description</summary>

Ontario-specific business use cases and content

</details>

**Versions**

* `eReferralOntarioNew.core#0.10.0`


---

### Application Data Exchange Assessment Framework and Functional Requirements for Mobile Health

<details>
<summary>Description</summary>

aDocument the functional requirements that can be used to assess devices, applications, and FHIR profiles to ensure that the essential data needed for clinical, patient and research uses is present in communications between applications. (built Thu, Apr 16, 2020 18:23+0000+00:00)

</details>

**Versions**

* `hl7.fhir.uv.mhealth-framework#0.1.0`


---

### 臺灣傳染病檢驗報告實作指引

<details>
<summary>Description</summary>

臺灣傳染病檢驗報告實作指引 (built Fri, Feb 2, 2024 13:13+0800+08:00)

</details>

**Versions**

* `tw.gov.mohw.cdc.twidir#0.1.1`
* `tw.gov.mohw.cdc.twidir#0.1.0`


---

### Test.Luxottica2

<details>
<summary>Description</summary>

Test resource extension

</details>

**Versions**

* `Test.Luxottica2#0.0.1-beta`


---

### Test.Template

<details>
<summary>Description</summary>

This is a test project

</details>

**Versions**

* `Test.Template#0.0.1`


---

### NHSN Reporting: Adverse Drug Events - Hypoglycemia

<details>
<summary>Description</summary>

Adverse drug events (ADEs) are among the most common causes of iatrogenic harm in U.S. hospitals. An effort to establish an EHR- and vendor-neutral standard for submitting ADE data to NHSN is being sought to improve patient safety and facilitate quality improvement effort. This promising initiative seeks to enable EHRs of acute care facilities to serve as source systems for reporting ADE data to the National Healthcare Safety Network (NHSN) via industry electronic messages. This initiative leverages NHSN’s longstanding experience working closely with the Health Level Seven (HL7) standards development organization and HL7 consultants in developing and maintaining electronic healthcare-associated and antibiotic use and resistance reporting implementation guidance for EHRs and infection surveillance system vendors in the acute care arena. That experience, and the working relationships developed over a 10-year partnership, are an important foundation for a collaborative effort in which NHSN, HL7, HL7 consultants, and EHR vendors join forces to advance the field of electronic ADE reporting.

This first module, Hypoglycemia, planned for the NHSN Medication Safety Component, would enable hospitals to track and benchmark inpatient medication-related hypoglycemia (low blood sugar). Inpatient hypoglycemia can be severe and life-threatening and is associated with longer hospital stays and increased medical costs. Severe hypoglycemia (<40 mg/dL) occurs in 2%-5% of hospitalized patients with diabetes mellitus (DM). Medication-related hypoglycemic events are common causes of adverse drug events (ADEs) occurring in inpatient settings, with rates of severe hypoglycemia varying across hospitals, suggesting opportunities for improvement in the quality of care. Measurement of medication-related hypoglycemia in a meaningful and standardized way may improve glycemic management. The NHSN ADE – Hypoglycemia Module provides a mechanism for facilities to report and analyze medication-related hypoglycemia as part of patient safety and glycemic management quality improvement efforts.

The measures planned for used in this module are based on quality reporting metrics previously- or currently-endorsed by the National Quality Forum (NQF): NQF #2363 Glycemic Control - Hypoglycemia and NQF #3503 Hospital Harm – Severe Hypoglycemia. The metrics reported in this module would be primarily based on linked eMAR (antidiabetic medication administration) and laboratory (blood glucose) data. These long-standing linkages links between public health and vendors can be leveraged in ways that yield benefits for surveillance and prevention that are tightly coupled to business models and growth. (built Tue, Oct 12, 2021 13:32+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.nhsn-ade#1.0.0`
* `hl7.fhir.us.nhsn-ade#0.1.0`


---

### pcr.r4.1-1-0-pkg

<details>
<summary>Description</summary>

Implementation Guide and FHIR artifacts for the Ontario Provincial Provider Registry 

</details>

**Versions**

* `pcr.r4.1-1-0-pkg#1.2.5`
* `pcr.r4.1-1-0-pkg#1.2.0`


---

### Finnish Base Profiles

<details>
<summary>Description</summary>

A core set of FHIR resources profiled for use in Finland, published and maintained by HL7 Finland (built Fri, Nov 10, 2023 00:30+0200+02:00)

</details>

**Versions**

* `hl7.fhir.fi.base#1.0.0`
* `hl7.fhir.fi.base#1.0.0-rc24`
* `hl7.fhir.fi.base#1.0.0-rc23`
* `hl7.fhir.fi.base#1.0.0-rc22`
* `hl7.fhir.fi.base#1.0.0-rc21`
* `hl7.fhir.fi.base#1.0.0-rc20`
* `hl7.fhir.fi.base#1.0.0-rc19`
* `hl7.fhir.fi.base#1.0.0-rc18`
* `hl7.fhir.fi.base#1.0.0-rc17`


---

### Womens Health Technology Coordinated Registry Network



**Versions**

* `hl7.fhir.us.womens-health-registries#0.2.0`
* `hl7.fhir.us.womens-health-registries#0.1.0`


---

### hsos.eWundbericht

<details>
<summary>Description</summary>

Deutscher eWundbericht

</details>

**Versions**

* `hsos.eWundbericht#0.9.0`


---

### healthdata.be.r4.clinicalreportresearch

<details>
<summary>Description</summary>

Generic ClinicalReportResearch message based on HdBe-R4-Clinical Building Blocks (CBB)

</details>

**Versions**

* `healthdata.be.r4.clinicalreportresearch#0.2.0-alpha`
* `healthdata.be.r4.clinicalreportresearch#0.1.0-alpha`


---

### International Birth And Child Model Implementation Guide



**Versions**

* `hl7.fhir.uv.ibcm#1.0.0-ballot`


---

### ontario-mha-old-v0.9-alpha

<details>
<summary>Description</summary>

Ontario Virtual Visit

</details>

**Versions**

* `ontario-mha-old-v0.9-alpha#0.9.1-alpha-test`


---

### pbm.V1.fhir

<details>
<summary>Description</summary>

Covering Patient-Blood-Management related resources for analysis and optimization of pre-operative patients

</details>

**Versions**

* `pbm.V1.fhir#0.5.0-draft`


---

### Kontaktregister.Profiles

<details>
<summary>Description</summary>

For testing, under utvikling.

</details>

**Versions**

* `Kontaktregister.Profiles#0.1.23`
* `Kontaktregister.Profiles#0.1.22`
* `Kontaktregister.Profiles#0.1.21`
* `Kontaktregister.Profiles#0.1.20`
* `Kontaktregister.Profiles#0.1.9`
* `Kontaktregister.Profiles#0.1.8`
* `Kontaktregister.Profiles#0.1.7`
* `Kontaktregister.Profiles#0.1.6`
* `Kontaktregister.Profiles#0.1.5`
* `Kontaktregister.Profiles#0.1.4`
* `Kontaktregister.Profiles#0.1.3`
* `Kontaktregister.Profiles#0.1.2`
* `Kontaktregister.Profiles#0.1.1`
* `Kontaktregister.Profiles#0.1.0`


---

### Vital Records Medicolegal Death Investigation (MDI) FHIR Implementation Guide

<details>
<summary>Description</summary>

This US-specific implementation guide (IG) provides guidance on the exchange of information to and from medicolegal death investigation (MDI) information systems. It supports interoperability between the MDI case management systems (CMS) used by medical examiner and coroner offices; forensic toxicology and other laboratory information management systems (LIMS); electronic death registration systems (EDRS) of jurisdictional vital records offices (VROs); and ancillary workflows whose systems have the capability of using Fast Healthcare Interoperability Resources (FHIR). This guide provides MDI CMS developers with the technical details and best practices to standardize MDI fields and interfaces. Stakeholders may use the narrative portions of this guide to inform policies and practices for data exchange between systems contributing to, and using information from, death investigations. This guide can serve as a base for local specifications. (built Thu, Nov 16, 2023 21:49+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.mdi#2.0.0-ballot`
* `hl7.fhir.us.mdi#1.1.0`
* `hl7.fhir.us.mdi#1.0.0`
* `hl7.fhir.us.mdi#1.0.0-ballot`


---

### de.ihe-d.terminology

<details>
<summary>Description</summary>

de.ihe-d.terminology

</details>

**Versions**

* `de.ihe-d.terminology#3.0.0`
* `de.ihe-d.terminology#3.0.0-alpha2`


---

### SMART App Launch



**Versions**

* `hl7.fhir.uv.smart-app-launch#2.2.0`
* `hl7.fhir.uv.smart-app-launch#2.2.0-ballot`
* `hl7.fhir.uv.smart-app-launch#2.1.0`
* `hl7.fhir.uv.smart-app-launch#2.1.0-ballot`
* `hl7.fhir.uv.smart-app-launch#1.1.0`
* `hl7.fhir.uv.smart-app-launch#1.0.0`


---

### Mobile Care Services Discovery (mCSD)

<details>
<summary>Description</summary>

The IHE Mobile Care Services Discovery (mCSD) IG provides a transaction for mobile and lightweight browser-based applications to find and update care services resources. (built Fri, Aug 12, 2022 09:41-0500-05:00)

</details>

**Versions**

* `ihe.iti.mcsd#3.8.0`
* `ihe.iti.mcsd#3.7.0`
* `ihe.iti.mcsd#3.6.1`
* `ihe.iti.mcsd#3.5.0`
* `ihe.iti.mcsd#3.4.0`


---

### Evidence Based Medicine on FHIR Implementation Guide



**Versions**

* `hl7.fhir.uv.ebm#1.0.0-ballot`


---

### Da Vinci - Documentation Templates and Rules

<details>
<summary>Description</summary>

The Documentation Templates and Rules (DTR) Implementation Guide (IG) specifies how payer rules can be executed in a provider context to ensure that documentation requirements are met. The IG is a companion to the Coverage Requirements Discovery (CRD) IG, which uses CDS Hooks to query payers to determine if there are documentation requirements for a proposed medication, procedure or other service. (built Tue, Nov 7, 2023 13:45+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.davinci-dtr#2.0.0`
* `hl7.fhir.us.davinci-dtr#1.1.0-ballot`
* `hl7.fhir.us.davinci-dtr#1.0.0`
* `hl7.fhir.us.davinci-dtr#0.2.0`
* `hl7.fhir.us.davinci-dtr#0.1.0`


---

### Pan-Canadian Patient Summary

<details>
<summary>Description</summary>

Patient Summary - Canada

</details>

**Versions**

* `ca.infoway.vc.ps#0.0.3`


---

### Structured Data Capture

<details>
<summary>Description</summary>

The SDC specification provides an infrastructure to standardize the capture and expanded use of patient-level data collected within an EHR.<br/>This includes two components:<br/>* Support more sophisticated questionnaire/form use-cases such as those needed for research, oncology, pathology and other clinical domains.<br/>*Support pre-population and auto-population of EHR data into forms/questionnaires for uses outside direct clinical care (patient safety, adverse event reporting, public health reporting, etc.). (built Tue, Mar 8, 2022 18:36+0000+00:00)

</details>

**Versions**

* `hl7.fhir.uv.sdc#3.0.0`
* `hl7.fhir.uv.sdc#3.0.0-preview`
* `hl7.fhir.uv.sdc#2.7.0`
* `hl7.fhir.uv.sdc#2.5.0`
* `hl7.fhir.uv.sdc#2.0.0`


---

### healthhub.fhir.dk.core

<details>
<summary>Description</summary>

PLSP Health Hub

</details>

**Versions**

* `healthhub.fhir.dk.core#1.0.2-alpha`
* `healthhub.fhir.dk.core#1.0.1-alpha`
* `healthhub.fhir.dk.core#1.0.0-alpha`


---

### ehelse.fhir.no.grunndata

<details>
<summary>Description</summary>

Grunndata profiles
HL7 FHIR R4
hl7.fhir.no.basis 2.0.14



</details>

**Versions**

* `ehelse.fhir.no.grunndata#2.3.5`
* `ehelse.fhir.no.grunndata#2.3.3`
* `ehelse.fhir.no.grunndata#2.3.2`
* `ehelse.fhir.no.grunndata#2.3.1`
* `ehelse.fhir.no.grunndata#2.3.0`
* `ehelse.fhir.no.grunndata#2.3.5-buildnumbersuffix2`
* `ehelse.fhir.no.grunndata#2.3.5-buildingnumbersuffix`
* `ehelse.fhir.no.grunndata#2.2.4`
* `ehelse.fhir.no.grunndata#2.2.3`


---

### ndhm.in.mirror

<details>
<summary>Description</summary>

NDHM Mirror

</details>

**Versions**

* `ndhm.in.mirror#1.2.0-rc2`
* `ndhm.in.mirror#1.2.0-rc1`
* `ndhm.in.mirror#1.2.0-rc0`


---

### ca.on.patient-summary

<details>
<summary>Description</summary>

Ontario Patient Summary

</details>

**Versions**

* `ca.on.patient-summary#0.0.1-alpha`


---

### PHIS.IG.CreateTest

<details>
<summary>Description</summary>

2023. 10. 26. FHIR IG 생성 테스트 목적

</details>

**Versions**

* `PHIS.IG.CreateTest#0.0.1`


---

### Data Exchange For Quality Measures Implementation Guide



**Versions**

* `hl7.fhir.us.davinci-deqm#4.0.0`
* `hl7.fhir.us.davinci-deqm#4.0.0-ballot`
* `hl7.fhir.us.davinci-deqm#3.1.0`
* `hl7.fhir.us.davinci-deqm#3.0.0`
* `hl7.fhir.us.davinci-deqm#2.1.0`
* `hl7.fhir.us.davinci-deqm#2.0.0`
* `hl7.fhir.us.davinci-deqm#1.1.0`
* `hl7.fhir.us.davinci-deqm#0.1.0`


---

### kbv.mio.emp

<details>
<summary>Description</summary>

Medikationsplan

</details>

**Versions**

* `kbv.mio.emp#1.0.0-kommentierung.1`


---

### ca.infoway.io.cafex

<details>
<summary>Description</summary>

The CA:FeX Interoperability Specifications (Canadian FHIR Exchange (CA:FeX))  seek to promote FHIR RESTful exchange patterns, developed by industry-leading FHIR standards that can be applied on top of an existing infrastructure just as easily as it can be applied on top of FHIR servers. 

</details>

**Versions**

* `ca.infoway.io.cafex#2.0.0`
* `ca.infoway.io.cafex#2.0.0-dft-pre`
* `ca.infoway.io.cafex#2.0.0-DFT-Ballot`


---

### CH Term (R4)

<details>
<summary>Description</summary>

Implementation Guide for Swiss Terminology (built Thu, May 16, 2024 10:33+0000+00:00)

</details>

**Versions**

* `ch.fhir.ig.ch-term#3.0.0`


---

### acme.canada.2023

<details>
<summary>Description</summary>

This is a demonstration package for FHIR North 2023

</details>

**Versions**

* `acme.canada.2023#1.2.0`
* `acme.canada.2023#0.0.1-alpha`


---

### uk.nhsdigital.clinical.r4

<details>
<summary>Description</summary>

NHS Digital FHIR Clinical ImplementationGuide

</details>

**Versions**

* `uk.nhsdigital.clinical.r4#2.1.2-dev`
* `uk.nhsdigital.clinical.r4#2.1.1-dev`
* `uk.nhsdigital.clinical.r4#2.1.0-dev`


---

### Implementation Guide for fælles faglige instrumenter (FFInst)

<details>
<summary>Description</summary>

Danish municipalities implementation guide for FFInst (built Sun, Aug 27, 2023 10:19+0200+02:00)

</details>

**Versions**

* `kl.dk.fhir.ffinst#1.0.0`


---

### br.ufg.cgis.rnds-lite



**Versions**

* `br.ufg.cgis.rnds-lite#0.1.8`
* `br.ufg.cgis.rnds-lite#0.1.7`
* `br.ufg.cgis.rnds-lite#0.1.6`
* `br.ufg.cgis.rnds-lite#0.1.5`
* `br.ufg.cgis.rnds-lite#0.1.4`
* `br.ufg.cgis.rnds-lite#0.1.3`
* `br.ufg.cgis.rnds-lite#0.1.2`
* `br.ufg.cgis.rnds-lite#0.1.1`
* `br.ufg.cgis.rnds-lite#0.1.0`
* `br.ufg.cgis.rnds-lite#0.0.3`
* `br.ufg.cgis.rnds-lite#0.0.2`
* `br.ufg.cgis.rnds-lite#0.0.1`


---

### cens.fhir.ssas-cdr

<details>
<summary>Description</summary>

Registro de datos clínicos relacionados a exámenes de Laboratorio

</details>

**Versions**

* `cens.fhir.ssas-cdr#1.0.0`


---

### COVID-19 FHIR Profile Library IG Informative Version

<details>
<summary>Description</summary>

The COVID-19 IG describes structured data to be collected and communicated between providers and aggregators. (built Mon, Jul 25, 2022 13:21+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.covid19library#1.0.0`
* `hl7.fhir.us.covid19library#0.14.0`
* `hl7.fhir.us.covid19library#0.13.0`


---

### CH EPR mHealth (R4)

<details>
<summary>Description</summary>

Implementation Guide for the Mobile access to Health Documents (epr-mhealth) Profile for the Swiss EPR (built Tue, Mar 5, 2024 12:06+0100+01:00)

</details>

**Versions**

* `ch.fhir.ig.ch-epr-mhealth#3.0.0`
* `ch.fhir.ig.ch-epr-mhealth#3.0.0-ballot`
* `ch.fhir.ig.ch-epr-mhealth#1.1.0`
* `ch.fhir.ig.ch-epr-mhealth#1.0.0`
* `ch.fhir.ig.ch-epr-mhealth#0.2.0`
* `ch.fhir.ig.ch-epr-mhealth#0.1.2`
* `ch.fhir.ig.ch-epr-mhealth#0.1.1`
* `ch.fhir.ig.ch-epr-mhealth#0.1.0`


---

### Clinical Practice Guidelines (CPG) on EBMonFHIR



**Versions**

* `de.netzwerk-universitaetsmedizin.ebm-cpg#0.9.1`
* `de.netzwerk-universitaetsmedizin.ebm-cpg#0.9.0`
* `de.netzwerk-universitaetsmedizin.ebm-cpg#0.8.0`
* `de.netzwerk-universitaetsmedizin.ebm-cpg#0.7.0`
* `de.netzwerk-universitaetsmedizin.ebm-cpg#0.7.1-snapshot`


---

### Vital Signs



**Versions**

* `hl7.fhir.us.vitalsigns#0.1.0`


---

### uk.gm

<details>
<summary>Description</summary>

Greater Manchester LHCRE Implementation Guide

</details>

**Versions**

* `uk.gm#0.0.9-dev`
* `uk.gm#0.0.8-dev`
* `uk.gm#0.0.7-dev`
* `uk.gm#0.0.6-dev`
* `uk.gm#0.0.5-dev`
* `uk.gm#0.0.4-dev`
* `uk.gm#0.0.3-dev`
* `uk.gm#0.0.2-dev`
* `uk.gm#0.0.12-dev`
* `uk.gm#0.0.11-dev`
* `uk.gm#0.0.10-dev`
* `uk.gm#0.0.1-dev`


---

### Koppeltaalv2.00

<details>
<summary>Description</summary>

First draft of used FHIR resource profiles in Koppeltaal 2.0. 

</details>

**Versions**

* `Koppeltaalv2.00#0.7.0-preview`


---

### MII IG Symptom

<details>
<summary>Description</summary>

Medizininformatik Initiative - Modul Symptom

</details>

**Versions**

* `de.medizininformatikinitiative.kerndatensatz.symptom#2024.0.0-ballot`


---

### Birth And Fetal Death (BFDR) - STU2-ballot

<details>
<summary>Description</summary>

### Description

Birth and fetal death reporting includes the transmission of data from health care providers to jurisdictional vital records offices and national health statistics agencies. Data associated with the mother of the baby or delivered fetus may be communicated independently from data associated directly with the labor and delivery encounter at the responsible healthcare facility. Note that for the purposes of this guide, &quot;mother&quot; always refers to the woman who delivered the infant or fetus. In cases of surrogacy or gestational carrier, the information reported should be for the surrogate or the gestational carrier, that is, the woman who delivered the infant. Also, the national statistics agency referred to in this guide is the [National Center for Health Statistics - CDC](https://www.cdc.gov/nchs/index.htm) (NCHS).

This implementation guide (IG) defines a series of Health Level Seven (HL7®) Fast Healthcare Interoperability Resources (FHIR®) profiles on the Composition resource to represent electronic birth and fetal death reporting (BFDR). It includes the content of medical/health information on live births and fetal deaths for select state and federal birth and fetal death reporting, as indicated in the [2003 Revision of the U.S. Standard Certificate of Live Birth](https://www.cdc.gov/nchs/data/dvs/birth11-03final-ACC.pdf) and the [2003 Revision of the U.S. Standard Report of Fetal Death](https://www.cdc.gov/nchs/data/dvs/FDEATH11-03finalACC.pdf). Additionally, it includes the content that is exchanged between electronic health record (EHR) systems, jurisdictions, and the Centers for Disease Control and Prevention/ National Center for Health Statistics (CDC/NCHS).

### Relationship to Other Standards
This BFDR IG standard complements other vital records standards to support the expansion of information flows to and from the national statistics agency. The BFDR STU 1 drew on foundational work of early standards listed in the Background section, below. This current version of the BFDR IG is informed by :
* [HL7, Vital Records Death Reporting (VRDR) FHIR Implementation Guide](http://hl7.org/fhir/us/vrdr/)
* [Office of the National Coordinator for Health Information Technology (ONC), United States Core Data for Interoperability (USCDI)](https://www.healthit.gov/isa/united-states-core-data-interoperability-uscdi)

### Dependencies
This implementation guide re-uses and further constrains profiles from the following guides:
* [US Core Implementation Guide, STU5.0.1](http://hl7.org/fhir/us/core/STU5.0.1/)
* [Vital Records Common Profiles Library](http://hl7.org/fhir/us/vr-common-library)
* [Occupational Data for Health (ODH)](http://hl7.org/fhir/us/odh/)

### Audience
This guide is for analysts and developers who require guidance on the use of the HL7 FHIR for providing birth and fetal death reporting information. The IG is informative to health care provider organizations, jurisdictional vital records offices, CDC/NCHS, health information exchange organizations, and other vital records fetal death reporting stakeholders.

### Background 
This FHIR IG builds on previous electronic data standards for transmitting vital records of death, birth, and fetal death. 
* [HL7, Version 2.6 Implementation Guide: Vital Records Birth and Fetal Death Reporting](https://www.hl7.org/implement/standards/product_brief.cfm?product_id=320)
* [HL7, CDA® R2 Implementation Guide: Birth and Fetal Death Reporting](http://www.hl7.org/implement/standards/product_brief.cfm?product_id=387)
* [IHE , Quality, Research and Public Health Technical Framework Supplement - Birth and Fetal Death Reporting-Enhanced (BFDR-E)](https://www.ihe.net/uploadedFiles/Documents/QRPH/IHE_QRPH_Suppl_BFDR-E.pdf)

This IG provides standardized data strutures for transmission of reliable and relevant clinical information to jurisdictional vital records offices and transfer of information from vital records offices to the national statistics agency. 

Electronic vital records work started with the HL7 Vital Records Domain Analysis Model (VR DAM), published as an Informative Specification in 2011. The VR DAM was updated in 2017 with the HL7 Cross-Paradigm Domain Analysis Model: Vital Records, Release 2, in 2018, with Release 3, and in November 2020 as Release 4. [VR DAM, Release 5](http://www.hl7.org/implement/standards/product_brief.cfm?product_id=466) was published in May 2022. Implementers who review or reference the VR DAM should note that the active vital records FHIR IGs (such as BFDR and VRDR) may no longer aligned with the VR DAM. 

This FHIR IG uses the US Core profiles. Where this FHIR IG is unable to use a US Core profile, we have followed the Cross Group Projects Work Group's variance request process, and have provided the US Realm Steering Committee an approved rationale for deviation in the IG.

### How to Read This Guide

This Guide is divided into several pages which are listed at the top of each page in the menu bar.

* Home: Introduction and background for HL7 FHIR® Vital Records Common Profiles Library
* [The Specification](the_specification.html): A technical overview of implementing the specification
* [Use Cases](use_cases.html): The use cases supported by this guide
* Implementer Guidance: The [worksheet Questionnaire format](patient_worksheet_questionnaires.html), [IJE Mapping](ije_mapping.html), and [Vital Records Forms Mapping](vital_records_forms_mapping.html)
* [Terminology](terminology.html): A listing of the value sets used in this guide
* [Downloads](downloads.html): Links to downloadable artifacts
* [Change Log](change_log.html): Details of changes made in each version of this IG
* [Artifact Index](artifacts.html): A list of the FHIR artifacts (profiles, examples, and value sets) defined as part of this guide
* Appendices: Examples of live birth and fetal death reports and worksheets

### Other Information

This is an update (STU 1.1) of the first FHIR BFDR standard.

This guide is compliant with FHIR Release 4.

For Clinical Safety Information please refer to the [FHIR Implementer’s Safety Checklist](http://hl7.org/fhir/safety.html).

Disclaimer: All proprietary documents, guides, guidance, standards, codes, and values contained herein remain the property of their respective Standards Developing Organization (SDO). HL7 does not make any claim to ownership herein. (built Tue, Oct 10, 2023 13:06+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.bfdr#2.0.0-ballot`
* `hl7.fhir.us.bfdr#1.1.0`
* `hl7.fhir.us.bfdr#1.0.0`
* `hl7.fhir.us.bfdr#0.1.0`


---

### kbv.mio.u-heft

<details>
<summary>Description</summary>

Medizinische Informationsobjekte (MIO) Kinderuntersuchungsheft V1.0.1

</details>

**Versions**

* `kbv.mio.u-heft#1.0.1-festlegungsversion`
* `kbv.mio.u-heft#1.0.1-benehmensversion`


---

### FHIR implementation of Medication Process 9 (MP9)

<details>
<summary>Description</summary>

Package of HL7 FHIR R4 conformance resources for the information standard Medication Process 9. 

</details>

**Versions**

* `nictiz.fhir.nl.r4.medicationprocess9#2.0.0-beta.5`
* `nictiz.fhir.nl.r4.medicationprocess9#2.0.0-beta.4`
* `nictiz.fhir.nl.r4.medicationprocess9#2.0.0-beta.2`
* `nictiz.fhir.nl.r4.medicationprocess9#2.0.0-beta.1`
* `nictiz.fhir.nl.r4.medicationprocess9#1.0.0`


---

### National Healthcare Directory Query

<details>
<summary>Description</summary>

National Directory Query (built Tue, Aug 9, 2022 18:48+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.directory-query#1.0.0-ballot`


---

### kbv.mio.impfpass

<details>
<summary>Description</summary>

Medizinische Informationsobjekte (MIO) Impfpass V1.1.0

</details>

**Versions**

* `kbv.mio.impfpass#1.1.0`
* `kbv.mio.impfpass#1.1.0-benehmensherstellung`


---

### KL Terminologi



**Versions**

* `kl.dk.fhir.term#2.1.0`
* `kl.dk.fhir.term#2.0.0`
* `kl.dk.fhir.term#1.1.0`
* `kl.dk.fhir.term#1.0.0`


---

### Guia de Implementação

<details>
<summary>Description</summary>

Ilustra criação de guias de implementação (built Sun, Mar 31, 2024 18:31-0300-03:00)

</details>

**Versions**

* `br.ufg.cgis.ig#0.0.3`
* `br.ufg.cgis.ig#0.0.2`
* `br.ufg.cgis.ig#0.0.1`


---

### iknl.fhir.nl.r4.palga

<details>
<summary>Description</summary>

IKNL profiles to fill the NCR (NKR) with data that is sent to IKNL by Palga

</details>

**Versions**

* `iknl.fhir.nl.r4.palga#1.0.0`
* `iknl.fhir.nl.r4.palga#0.3.0-beta`
* `iknl.fhir.nl.r4.palga#0.2.0`
* `iknl.fhir.nl.r4.palga#0.1.0`


---

### de.gematik.isik-labor

<details>
<summary>Description</summary>

Labor-Modul der Informationstechnischen Systeme im Krankenhaus Ausbaustufe 4; Support-Status!

</details>

**Versions**

* `de.gematik.isik-labor#4.0.0-rc`


---

### kbv.mio.laborbefund

<details>
<summary>Description</summary>

Medizinische Informationsobjekte (MIO) Laborbefund V1.0.0

</details>

**Versions**

* `kbv.mio.laborbefund#1.0.0-kommentierung`


---

### uk.adsv2.r4

<details>
<summary>Description</summary>

Put a description here

</details>

**Versions**

* `uk.adsv2.r4#1.6.1`
* `uk.adsv2.r4#1.6.0`
* `uk.adsv2.r4#1.4.0`
* `uk.adsv2.r4#1.3.0`


---

### CH ELM (R4)

<details>
<summary>Description</summary>

FHIR® Implementation Guide for the Electronic Laboratory Report of the Swiss Federal Office of Public Health (built Mon, Jun 17, 2024 08:46+0000+00:00)

</details>

**Versions**

* `ch.fhir.ig.ch-elm#1.3.1`
* `ch.fhir.ig.ch-elm#1.3.0`
* `ch.fhir.ig.ch-elm#1.2.0`
* `ch.fhir.ig.ch-elm#1.1.1`
* `ch.fhir.ig.ch-elm#1.1.0`
* `ch.fhir.ig.ch-elm#1.0.0`
* `ch.fhir.ig.ch-elm#1.0.0-trialuse`


---

### Protocols for Clinical Registry Extraction and Data Submission (CREDS) IG

<details>
<summary>Description</summary>

The IG demonstrates a process and workflow to support the needs of clinical registries to define how registry submissions can be automatically extracted from multiple data sources and combined into a registry submission. (built Tue, Nov 14, 2023 18:48+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.registry-protocols#1.0.0`
* `hl7.fhir.us.registry-protocols#1.0.0-ballot`


---

### de.einwilligungsmanagement

<details>
<summary>Description</summary>

Einwilligungsmanagement

</details>

**Versions**

* `de.einwilligungsmanagement#1.1.0-alpha.1`
* `de.einwilligungsmanagement#1.0.2`
* `de.einwilligungsmanagement#1.0.1`
* `de.einwilligungsmanagement#1.0.0`
* `de.einwilligungsmanagement#1.0.0-RC2`
* `de.einwilligungsmanagement#1.0.0-RC1`
* `de.einwilligungsmanagement#0.0.5-snapshot`
* `de.einwilligungsmanagement#0.0.4-snapshot`
* `de.einwilligungsmanagement#0.0.3-snapshot`
* `de.einwilligungsmanagement#0.0.2-snapshot`
* `de.einwilligungsmanagement#0.0.1-snapshot`


---

### qurasoft.saniq

<details>
<summary>Description</summary>

SaniQ

</details>

**Versions**

* `qurasoft.saniq#1.0.0`


---

### Consolidated CDA (C-CDA)



**Versions**

* `hl7.cda.us.ccda#3.0.0`
* `hl7.cda.us.ccda#3.0.0-ballot`


---

### HL7 FHIR Implementation Guide Laboratory Report

<details>
<summary>Description</summary>

HL7 FHIR Implementation Guide Laboratory Report specifica come utilizzare lo standard HL7 FHIR per documentare un referto di medicina di laboratorio. (built Fri, Mar 8, 2024 08:50+0100+01:00)

</details>

**Versions**

* `hl7.fhir.it.lab-report#0.2.0`


---

### Implementierungsleitfaden DEMIS - Erregernachweismeldung

<details>
<summary>Description</summary>

Beinhaltet die in DEMIS zur Umsetzung der Erregernachweismeldungen definierten Informationsmodellartefakte.

</details>

**Versions**

* `rki.demis.laboratory#1.24.1`
* `rki.demis.laboratory#1.24.0`


---

### C-CDA On FHIR Implementation Guide

<details>
<summary>Description</summary>

Consolidated CDA (C-CDA) is one of the most widely implemented implementation guides for CDA and covers a significant scope of clinical care. Its target of the 'common/essential' elements of healthcare is closely aligned with FHIR's focus on the '80%'. There is significant interest in industry and government in the ability to interoperate between CDA and FHIR and C-CDA is a logical starting point. Implementers and regulators have both expressed an interest in the ability to map between FHIR and C-CDA.

This Implementation Guide (IG) defines a series of FHIR profiles on the Composition resource to represent the various document types in C-CDA. This release does not directly map every C-CDA template to FHIR profiles, rather tries to accomplish the C-CDA use case using Composition resource profiles created under this project (the equivalent of Level 2 CDA documents), and begins by linking to the profiles created under the US Core project for any coded entries that would normally be included in C-CDA sections. To have a simpler, more streamlined standard that reuses existing work and focuses on the 80% that implementers actually need in production systems, the resources of US Core represents a portion of the 80% needed for coded entries for coded entries of CCD, Care Plan & Discharge Summary).

The Composition profiles in this IG do not require coded data in any section. This is a departure from C-CDA, which requires coded data for Problems, Results, Medications, etc. This departure is intentional, as the C-CDA requirement for one or more coded entries in these sections resulted in some very complicated workarounds using nullFlavors to handle the fact that sometimes a patient is not on any medications, or has no current problems. In general, FHIR takes the approach that if something is nullable, it should simply be optional to ease the burden on implementers, thus C-CDA on FHIR does not require any coded entries, but rather uses the "required if known" approach, meaning that if an implementer's system has data for a section that requires data under Meaningful Use, they need to send it, but if they have no data there is no need for a null entry.

We encourage feedback on these Composition profiles, and the general approach to the project as a whole. We also encourage implementers who wish to see more of the coded data from C-CDA mapped to FHIR to comment on the US Core project and make their requests known there. Once US Core creates new profiles, this project can reference them.

### Scope

To represent Consolidated CDA Templates for Clinical Notes (C-CDA) 2.1 templates using FHIR profiles.

This first stage of the project defines all the C-CDA document-level profiles on the Composition resource and contained sections.

Any coded data used by sections will be represented using relevant U.S. Core FHIR profiles where they exist. FHIR profiles defined by other work groups or unconstrained FHIR resources may also be referenced if no appropriate US Core Profile exist.

For further information see the C-CDA specification here: http://www.hl7.org/implement/standards/product_brief.cfm?product_id=408. (built Tue, Mar 12, 2024 18:59+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.ccda#1.6.0`
* `hl7.fhir.us.ccda#1.2.0`
* `hl7.fhir.us.ccda#1.2.0-ballot`
* `hl7.fhir.us.ccda#1.1.0`


---

### Berkay.Sandbox

<details>
<summary>Description</summary>

Dies ist Berkay's Sandbox

</details>

**Versions**

* `Berkay.Sandbox#0.0.1`


---

### Structured Data Capture

<details>
<summary>Description</summary>

The SDC specification provides an infrastructure to standardize the capture and expanded use of patient-level data collected within an EHR.<br/>This includes two components:<br/>* Support more sophisticated questionnaire/form use-cases such as those needed for research, oncology, pathology and other clinical domains.<br/>*Support pre-population and auto-population of EHR data into forms/questionnaires for uses outside direct clinical care (patient safety, adverse event reporting, public health reporting, etc.). (built Tue, Mar 8, 2022 18:36+0000+00:00)

</details>

**Versions**

* `hl7.fhir.uv.sdc.r4#3.0.0`


---

### Belgian MyCareNet Profiles



**Versions**

* `hl7.fhir.be.mycarenet#2.0.0`


---

### pathologyencountertissue.tryout

<details>
<summary>Description</summary>

lets start to get swiss pathology structured

</details>

**Versions**

* `pathologyencountertissue.tryout#1.0.0`


---

### de.basisprofil.onkologie

<details>
<summary>Description</summary>

Basisprofile Onkologie von HL7 Deutschland e.V.

</details>

**Versions**

* `de.basisprofil.onkologie#1.0.0-ballot`


---

### Nictiz FHIR NL R4 Lab Exchange

<details>
<summary>Description</summary>

Nictiz NL package of FHIR R4 conformance resources for Lab Exchange.

</details>

**Versions**

* `nictiz.fhir.nl.r4.labexchange#3.0.0-beta.2`


---

### Interactive Multimedia Report (IMR)

<details>
<summary>Description</summary>

Support encoding and presentation of an interactive multimedia report (built Mon, Jul 25, 2022 14:36-0500-05:00)

</details>

**Versions**

* `ihe.rad.imr#1.0.0`
* `ihe.rad.imr#1.0.0-comment`
* `ihe.rad.imr#0.1.0`


---

### FHIR R4 package : Core

<details>
<summary>Description</summary>

Definitions (API, structures and terminologies) for the R4 version of the FHIR standard

</details>

**Versions**

* `hl7.fhir.r4.core#4.0.1`


---

### UK.NHSDigital.BARS.R4

<details>
<summary>Description</summary>

NHS Booking and Referrals

</details>

**Versions**

* `UK.NHSDigital.BARS.R4#0.1.0-test`


---

### HL7 Norway no-basis

<details>
<summary>Description</summary>

Norwegian base profiles for R4

## Version 2.2

Feature release to add no-basis-Appointment and no-basis-AppointmentResponse profile definition (issue #97).
Including bugfixes:

issue #87 HNR, lokale hjelpenummer
issue #99 Wrong canonical on NoBasisRelatedPersonReference
issue #97 Removed deprecated proposal for no-basis-Encounter

### 2.2.0 Changelog
Release date: 2023-10-06

no-basis-Appointment Added profile, with example
no-basis-AppointmentResponse Added profile, with example
no-basis-Virtual-Service Added extension
no-basis-group Added extension
no-basis-partof Added extension
no-basis-shortnotice Added extension
No Basis VirtualServiceType Value Set Added valueset

</details>

**Versions**

* `hl7.fhir.no.basis#2.2.0`
* `hl7.fhir.no.basis#2.1.2`
* `hl7.fhir.no.basis#2.1.1`
* `hl7.fhir.no.basis#2.1.0`
* `hl7.fhir.no.basis#2.1.2-delta`
* `hl7.fhir.no.basis#2.1.2-beta`
* `hl7.fhir.no.basis#2.1.2-alpha`
* `hl7.fhir.no.basis#2.0.14`
* `hl7.fhir.no.basis#2.0.13`
* `hl7.fhir.no.basis#2.0.12`
* `hl7.fhir.no.basis#2.0.11`
* `hl7.fhir.no.basis#2.0.17-alpha`
* `hl7.fhir.no.basis#2.0.16-beta`


---

### kbv.ita.vos

<details>
<summary>Description</summary>

Verordnungssoftware-Schnittstelle gemäß § 371 Absatz 1 SGB V

</details>

**Versions**

* `kbv.ita.vos#2.1.0`


---

### acme.profiling.tutorial.r4

<details>
<summary>Description</summary>

ACME project explaining the creation and use of FHIR profiles using Forge and Simplifier. Mainly used in DevDays 'Let's Build!' workshops.

</details>

**Versions**

* `acme.profiling.tutorial.r4#2.0.0`
* `acme.profiling.tutorial.r4#1.0.0`


---

### PCR.R4.1-1-0-pkg

<details>
<summary>Description</summary>

Implementation Guide and FHIR artifacts for the Ontario Provincial Provider Registry 

</details>

**Versions**

* `PCR.R4.1-1-0-pkg#1.1.0`


---

### Da Vinci Payer Data Exchange

<details>
<summary>Description</summary>

This specification has undergone ballot and connectathon testing. It is expected to evolve, possibly significantly, as part of that process.
Feedback is welcome and may be submitted through the FHIR JIRA tracker indicating US Da Vinci PDex as the specification. If balloting on this IG, please submit your comments via the tracker and reference them in your ballot submission implementation guide.

This guide can be reviewed offline. Go to the Downloads section. Click on the link to download the full Implementation Guide as a zip file. Expand the zip file and use a web browser to launch the index.html file in the directory created by the zip extract process. External hyperlinks in the guide will not be available unless you have an active internet connection. 

[Financial Management](https://confluence.hl7.org/display/FM/Financial+Management+Home) is the Sponsoring Work Group for this Implementation Guide.

**The Payer Data Exchange (PDex) Implementation Guide (IG) is provided for Payers/Health Plans to enable them to create a Member's Health History using clinical resources (based on US Core 3.1.1 Profiles based on FHIR R4) which can be understood by providers and, if they choose to, committed to their Electronic Medical Records (EMR) System.**

The PDex work group has made changes to the original version of the IG following the publication of the final CMS Interoperability and Patient Access Rule.

This IG uses the same Member Health History "payload" for member-authorized exchange of information with other Health Plans and with Third-Party Applications. It describes the interaction patterns that, when followed, allow the various parties involved in managing healthcare and payer data to more easily integrate and exchange data securely and effectively.

This IG covers the exchange of:
- Claims-based information
- Clinical Information (such as Lab Results, Allergies and Conditions)

This IG covers the exchange of this information using US Core and Da Vinci Health Record Exchange (HRex) Profiles. This superset of clinical profiles forms the Health Plan Member's Health History. 

This IG covers the exchange of a Member's Health History in the following scenarios:
- Provider requested Provider-Health Plan Exchange using CDS-Hooks and SMART-on-FHIR
- Member-authorized Health Plan to Health Plan exchange
- Member-authorized Health Plan to Third-Party Application exchange

The latter two scenarios are provided to meet the requirements identified in the CMS Interoperability and Patient Access Final Rule.

**There are items in this guide that are subject to update**. This includes:
- Value Sets
- Vocabularies (X12, NUBC etc.)
- Examples

**The Vocabulary, Value Sets and codings used to express data in this IG are subject to review and will be reconciled with**  [X12](http://www.x12.org).

See the [Table of Contents](toc.html) for more information.
 (built Sat, Jan 6, 2024 03:03+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.davinci-pdex#2.0.0`
* `hl7.fhir.us.davinci-pdex#2.0.0-ballot`
* `hl7.fhir.us.davinci-pdex#1.0.0`
* `hl7.fhir.us.davinci-pdex#0.1.0`


---

### i.s.h.med FHIR R4 International API

<details>
<summary>Description</summary>

i.s.h.med FHIR R4 International API Endpoint

</details>

**Versions**

* `ishmed.i14y.r4#2.0.0`
* `ishmed.i14y.r4#1.0.0`


---

### MII IG Onkologie

<details>
<summary>Description</summary>

Medizininformatik Initiative - Modul Onkologie

Version zur Ballotierung durch HL7 Deutschland und Kommentierung durch die Medizininformatik-Initiative

</details>

**Versions**

* `de.medizininformatikinitiative.kerndatensatz.onkologie#2024.0.0-ballot`
* `de.medizininformatikinitiative.kerndatensatz.onkologie#2024.0.0-ballot-alpha-1`


---

### tsti.adultcheck

<details>
<summary>Description</summary>

成人預防保健實作指引(Adult Check IG) 採用HL7® FHIR® standard（Fast Healthcare Interoperability Resources）IG建置方法

</details>

**Versions**

* `tsti.adultcheck#0.1.2`
* `tsti.adultcheck#0.1.1`


---

### PathologyEncounterTissue.tryout

<details>
<summary>Description</summary>

lets start to get swiss pathology structured

</details>

**Versions**

* `PathologyEncounterTissue.tryout#0.1.1`


---

### rambam-fhir.health.gov.il

<details>
<summary>Description</summary>

Rambam FHIR Project

</details>

**Versions**

* `rambam-fhir.health.gov.il#0.1.4`
* `rambam-fhir.health.gov.il#0.1.3`
* `rambam-fhir.health.gov.il#0.1.2`
* `rambam-fhir.health.gov.il#0.1.1`


---

### Implementation Guide CH VACD

<details>
<summary>Description</summary>

Implementation guide CH VACD (built Fri, Jun 18, 2021 09:33+0200+02:00)

</details>

**Versions**

* `ch.fhir.ig.ch-vacd#5.0.0-ballot`
* `ch.fhir.ig.ch-vacd#4.0.1`
* `ch.fhir.ig.ch-vacd#4.0.0`
* `ch.fhir.ig.ch-vacd#4.0.0-ballot`
* `ch.fhir.ig.ch-vacd#3.0.0`
* `ch.fhir.ig.ch-vacd#2.1.0`
* `ch.fhir.ig.ch-vacd#2.0.0`
* `ch.fhir.ig.ch-vacd#1.0.0`
* `ch.fhir.ig.ch-vacd#0.1.0`


---

### International Patient Summary Implementation Guide

<details>
<summary>Description</summary>

International Patient Summary (IPS) FHIR Implementation Guide (built Tue, Nov 22, 2022 03:24+0000+00:00)

</details>

**Versions**

* `hl7.fhir.uv.ips#1.1.0`
* `hl7.fhir.uv.ips#1.0.0`
* `hl7.fhir.uv.ips#0.3.0`
* `hl7.fhir.uv.ips#0.2.0`


---

### Da Vinci Value-Based Performance Reporting Implementation Guide

<details>
<summary>Description</summary>

The Da Vinci Fast Healthcare Interoperability Resource (FHIR) Value-Based Performance Reporting Implementation Guide (this IG) specifies standard representations of value-based performance reports that are exchanged payers and providers, which include performance metrics on financial, utilization, etc. and quality measures for varies types of value-based contracts. (built Mon, Jun 17, 2024 13:48+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.davinci-vbpr#1.0.0`
* `hl7.fhir.us.davinci-vbpr#1.0.0-ballot`


---

### kbv.mio.kh-entlassbrief

<details>
<summary>Description</summary>

Medizinische Informationsobjekte (MIO) KH-Entlassbrief V1.0.0

</details>

**Versions**

* `kbv.mio.kh-entlassbrief#1.0.0-update`
* `kbv.mio.kh-entlassbrief#1.0.0-kommentierung`


---

### FHIR Implementation Guide for ABDM Preview



**Versions**

* `ig.in#0.1.0`


---

### test.touchstone.at.package

<details>
<summary>Description</summary>

Test

</details>

**Versions**

* `test.touchstone.at.package#0.0.3-beta`


---

### Cancer Pathology Data Sharing

<details>
<summary>Description</summary>

This implementation guide (IG) provides Health Level Seven (HL7 FHIR) resources to define standards for cancer pathology information exchange from a hospital or facility-based laboratory information system (LIS) to a hospital or facility-based electronic health record (EHR) system or to a central cancer registry. When sending to a central registry, the FHIR Messaging paradigm shall be required; however when sending between LIS and EHR systems, implementors may choose to use alterantive transport and processing modalities, such as FHIR transaciton bundles. This publication provides the data model, defined data items and their corresponding code and value sets specific to a cancer pathology synoptic report. This guide contains a library of FHIR profiles to create a cancer pathology message bundle and is compliant with FHIR Release 4. (built Tue, Apr 16, 2024 18:41+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.cancer-reporting#1.0.1`
* `hl7.fhir.us.cancer-reporting#1.0.0`
* `hl7.fhir.us.cancer-reporting#0.1.0`


---

### HL7 FHIR Implementation Guide: Military Service History and Status Release 1 - US Realm | STU1

<details>
<summary>Description</summary>

Military Service History and Status is an implementation guide for military service history and veteran status verification/confirmation. (built Tue, May 30, 2023 14:59+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.military-service#1.0.0`
* `hl7.fhir.us.military-service#0.1.0`


---

### FHIR 4.3.0 package : Expansions

<details>
<summary>Description</summary>

Expansions for the 4.3.0 version of the FHIR standard (built Sat, May 28, 2022 12:47+1000+10:00)

</details>

**Versions**

* `hl7.fhir.r4b.expansions#4.3.0`
* `hl7.fhir.r4b.expansions#4.1.0`


---

### accdr.fhir.ig.pkg

<details>
<summary>Description</summary>

Ontario Health

</details>

**Versions**

* `accdr.fhir.ig.pkg#0.9.15`


---

### Subscriptions R5 Backport

<details>
<summary>Description</summary>

The Subscription R5 Backport Implementation Guide enables servers running versions of FHIR earlier than R5 to implement a subset of R5 Subscriptions in a standardized way. (built Wed, Jan 11, 2023 15:34+1100+11:00)

</details>

**Versions**

* `hl7.fhir.uv.subscriptions-backport.r4b#1.1.0`


---

### demis.fhir.profiles

<details>
<summary>Description</summary>

Deutsches Elektronisches Melde- und Informationssystem für den Infektionsschutz

</details>

**Versions**

* `demis.fhir.profiles#1.17.0`
* `demis.fhir.profiles#1.15.0`


---

### Clinical Document Architecture



**Versions**

* `hl7.cda.uv.core#2.0.0-sd`
* `hl7.cda.uv.core#2.0.0-sd-snapshot1`
* `hl7.cda.uv.core#2.0.0-sd-ballot`


---

### Implementation Guide for FFB messaging (FFB udvekslingsdatasæt)

<details>
<summary>Description</summary>

Danish municipalities implementation guide for FFB messaging. A standard for exchanging social information from citizen journals between municipalities. The aim is to support citizens continuously, e.g. when they move, or receives interventions outside their home municipality. (built Sun, Aug 27, 2023 07:52+0200+02:00)

</details>

**Versions**

* `kl.dk.fhir.ffbmessaging#1.0.0`


---

### de.gematik.isik-terminplanung

<details>
<summary>Description</summary>

Package Release des ISiK Modul Terminplanung

</details>

**Versions**

* `de.gematik.isik-terminplanung#4.0.0-rc`
* `de.gematik.isik-terminplanung#3.0.4`
* `de.gematik.isik-terminplanung#3.0.3`
* `de.gematik.isik-terminplanung#3.0.2`
* `de.gematik.isik-terminplanung#2.0.4`
* `de.gematik.isik-terminplanung#2.0.3`


---

### devdays.letsbuildafhirspec.simplifier

<details>
<summary>Description</summary>

Building an Implementation Guide and Package with Simplifier.net

</details>

**Versions**

* `devdays.letsbuildafhirspec.simplifier#0.0.4-preview`
* `devdays.letsbuildafhirspec.simplifier#0.0.3-devdaysus2021`
* `devdays.letsbuildafhirspec.simplifier#0.0.2-test`
* `devdays.letsbuildafhirspec.simplifier#0.0.1-test`


---

### KBV.MIO.ZAEB

<details>
<summary>Description</summary>

Medizinische Informationsobjekte (MIO) Zahnärztliches Bonusheft V1.1.0-Kommentierung

</details>

**Versions**

* `KBV.MIO.ZAEB#1.1.0-Kommentierung`
* `KBV.MIO.ZAEB#1.00.000`


---

### kbv.mio.tele

<details>
<summary>Description</summary>

Medizinische Informationsobjekte (MIO) Telemedizinisches Monitoring V1.0.0

</details>

**Versions**

* `kbv.mio.tele#1.0.0`
* `kbv.mio.tele#1.0.0-kommentierung`
* `kbv.mio.tele#1.0.0-benehmensherstellung`


---

### eng.fhir.profile.dev

<details>
<summary>Description</summary>

Raccolta profili FHIR aziendali (dev)

</details>

**Versions**

* `eng.fhir.profile.dev#0.0.7-beta`
* `eng.fhir.profile.dev#0.0.6-beta`
* `eng.fhir.profile.dev#0.0.5-beta`
* `eng.fhir.profile.dev#0.0.3-beta`
* `eng.fhir.profile.dev#0.0.2-beta`


---

### mrrt.mintmedical

<details>
<summary>Description</summary>

Test to upload Profiles and validate Instances against them 

</details>

**Versions**

* `mrrt.mintmedical#4.0.2-preview`
* `mrrt.mintmedical#1.0.0`


---

### sfm.130323

<details>
<summary>Description</summary>

Medication related projects (SFM, PLL, SAFEST, KIKJ)

</details>

**Versions**

* `sfm.130323#3.0.1`


---

### il.core.fhir.r4.2023

<details>
<summary>Description</summary>

IL core project 

</details>

**Versions**

* `il.core.fhir.r4.2023#0.1.0`


---

### eHealth Platform R4 Federal Profiles

<details>
<summary>Description</summary>

eHealth Platform R4 Federal Profiles

</details>

**Versions**

* `ehealthplatform.be.r4.federalprofiles#1.3.3-beta`
* `ehealthplatform.be.r4.federalprofiles#1.3.1-beta`
* `ehealthplatform.be.r4.federalprofiles#1.3.0-beta`
* `ehealthplatform.be.r4.federalprofiles#1.2.2`
* `ehealthplatform.be.r4.federalprofiles#1.2.1`
* `ehealthplatform.be.r4.federalprofiles#1.2.0`
* `ehealthplatform.be.r4.federalprofiles#1.1.9-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.8-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.7-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.6-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.5-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.49-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.48-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.47-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.46-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.45-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.44-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.43-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.42-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.41-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.40-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.4-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.38-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.37-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.36-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.35-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.34-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.33-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.32-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.31-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.30-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.3-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.29-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.28-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.27-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.26-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.25-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.24-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.23-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.22-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.21-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.20-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.2-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.19-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.18-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.17-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.16-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.15-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.14-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.12-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.11-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.10-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.1-beta`
* `ehealthplatform.be.r4.federalprofiles#1.1.0-beta`
* `ehealthplatform.be.r4.federalprofiles#1.0.0`
* `ehealthplatform.be.r4.federalprofiles#0.1.9`
* `ehealthplatform.be.r4.federalprofiles#0.1.8-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.7-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.6-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.53-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.52-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.51-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.50-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.5-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.49-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.48-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.47-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.46-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.45-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.44-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.43-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.42-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.41-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.40-bet`
* `ehealthplatform.be.r4.federalprofiles#0.1.39-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.38-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.37-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.36-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.36-bet`
* `ehealthplatform.be.r4.federalprofiles#0.1.35-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.34-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.33-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.32-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.31-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.30-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.3-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.29-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.28-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.27-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.26-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.25-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.24-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.23-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.22-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.21-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.20-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.2-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.19-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.18-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.17-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.16-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.14-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.13-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.12-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.11-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.10-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.1-beta`
* `ehealthplatform.be.r4.federalprofiles#0.1.0-beta`


---

### SIL-TH Terminology (STU1)

<details>
<summary>Description</summary>

A terminology registry which an be cited by FHIR artifacts (built Wed, Oct 11, 2023 23:14+0700+07:00)

</details>

**Versions**

* `silth.fhir.terminology.core#0.1.2`


---

### Da Vinci Prior Authorization Support (PAS) FHIR IG

<details>
<summary>Description</summary>

Guidelines for conveying coverage requirements to clinicians when planning treatment. (built Fri, Dec 1, 2023 20:54+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.davinci-pas#2.0.1`
* `hl7.fhir.us.davinci-pas#1.2.0-ballot`
* `hl7.fhir.us.davinci-pas#1.1.0`
* `hl7.fhir.us.davinci-pas#1.0.0`
* `hl7.fhir.us.davinci-pas#0.1.0`


---

### DK MedCom Terminology

<details>
<summary>Description</summary>

The DK MedCom Terminology IG (built Mon, Feb 5, 2024 08:46+0100+01:00)

</details>

**Versions**

* `medcom.fhir.dk.terminology#1.6.0`
* `medcom.fhir.dk.terminology#1.5.0`
* `medcom.fhir.dk.terminology#1.4.0`
* `medcom.fhir.dk.terminology#1.3.0`
* `medcom.fhir.dk.terminology#1.2.0`
* `medcom.fhir.dk.terminology#1.1.1`
* `medcom.fhir.dk.terminology#1.1.0`
* `medcom.fhir.dk.terminology#1.0.0`


---

### Da Vinci Clinical Data Exchange (CDex)

<details>
<summary>Description</summary>

This IG provides detailed guidance that helps implementers use FHIR-based interactions to support specific exchanges of clinical data between providers and payers (or other providers). (built Wed, Mar 22, 2023 00:14+1100+11:00)

</details>

**Versions**

* `hl7.fhir.us.davinci-cdex#2.0.0`
* `hl7.fhir.us.davinci-cdex#2.0.0-ballot`
* `hl7.fhir.us.davinci-cdex#1.1.0`
* `hl7.fhir.us.davinci-cdex#1.1.0-ballot`
* `hl7.fhir.us.davinci-cdex#1.0.0`
* `hl7.fhir.us.davinci-cdex#0.2.0`
* `hl7.fhir.us.davinci-cdex#0.1.0`


---

### mint.fhir

<details>
<summary>Description</summary>

Development of an implementation guide for the company mint medical.

</details>

**Versions**

* `mint.fhir#1.2.1`
* `mint.fhir#1.2.0`
* `mint.fhir#1.2.2-preview`
* `mint.fhir#1.2.1-preview`
* `mint.fhir#1.2.0-preview`


---

### ehealth4u.package

<details>
<summary>Description</summary>

The eHealth4U is a research project that undertakes the challenge of designing and developing a prototype of the national integrated EHR system in Cyprus. More information: http://ehealth4u.cs.ucy.ac.cy/

</details>

**Versions**

* `ehealth4u.package#1.0.0`


---

### kbv.basis

<details>
<summary>Description</summary>

KBV-Basis-Profile V 1.6.0

</details>

**Versions**

* `kbv.basis#1.6.0`
* `kbv.basis#1.6.0-Expansions`
* `kbv.basis#1.5.0`
* `kbv.basis#1.5.0-Expansions`
* `kbv.basis#1.4.0`
* `kbv.basis#1.4.0-Expansions`
* `kbv.basis#1.3.0`
* `kbv.basis#1.2.1`
* `kbv.basis#1.2.0`


---

### CodeX Radiation Therapy

<details>
<summary>Description</summary>

CodeX™ Radiation Therapy is an initiative intended to assemble a core set of structured data elements for radiation therapy electronic health records. (built Tue, Apr 9, 2024 14:34+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.codex-radiation-therapy#2.0.0-ballot`
* `hl7.fhir.us.codex-radiation-therapy#1.0.0`
* `hl7.fhir.us.codex-radiation-therapy#1.0.0-ballot`


---

### KBV.MIO.U-Heft

<details>
<summary>Description</summary>

Medizinische Informationsobjekte (MIO) Kinderuntersuchungsheft V1.0.0

</details>

**Versions**

* `KBV.MIO.U-Heft#1.0.0`


---

### hl7.fhir.rdsuwearme

<details>
<summary>Description</summary>

Package version 1.0.0

</details>

**Versions**

* `hl7.fhir.rdsuwearme#1.0.1`
* `hl7.fhir.rdsuwearme#1.0.0`


---

### us.cdc.phinvads



**Versions**

* `us.cdc.phinvads#0.12.0`
* `us.cdc.phinvads#0.11.0`
* `us.cdc.phinvads#0.10.0`
* `us.cdc.phinvads#0.8.0`
* `us.cdc.phinvads#0.7.0`
* `us.cdc.phinvads#0.6.0`
* `us.cdc.phinvads#0.5.0`
* `us.cdc.phinvads#0.4.0`
* `us.cdc.phinvads#0.3.0`
* `us.cdc.phinvads#0.2.0`
* `us.cdc.phinvads#0.1.2`
* `us.cdc.phinvads#0.1.1`
* `us.cdc.phinvads#0.1.0`


---

### Northwell.Person.Extensions

<details>
<summary>Description</summary>

Custom extensions on Person resource for BirthSex, Importance, Religion

</details>

**Versions**

* `Northwell.Person.Extensions#1.0.0-alpha`


---

### de.abda.erezeptabgabedatenbasis

<details>
<summary>Description</summary>

Basis-Profile für die Abgabedaten im eRezept-Kontext

</details>

**Versions**

* `de.abda.erezeptabgabedatenbasis#1.4.0`
* `de.abda.erezeptabgabedatenbasis#1.4.1-rc`
* `de.abda.erezeptabgabedatenbasis#1.4.0-rc2`
* `de.abda.erezeptabgabedatenbasis#1.4.0-rc`
* `de.abda.erezeptabgabedatenbasis#1.3.1`
* `de.abda.erezeptabgabedatenbasis#1.3.0`
* `de.abda.erezeptabgabedatenbasis#1.3.0-rc4`
* `de.abda.erezeptabgabedatenbasis#1.3.0-rc3`
* `de.abda.erezeptabgabedatenbasis#1.3.0-rc2`
* `de.abda.erezeptabgabedatenbasis#1.3.0-rc1`
* `de.abda.erezeptabgabedatenbasis#1.2.1`
* `de.abda.erezeptabgabedatenbasis#1.2.0`
* `de.abda.erezeptabgabedatenbasis#1.2.0-rc5`
* `de.abda.erezeptabgabedatenbasis#1.2.0-rc4`
* `de.abda.erezeptabgabedatenbasis#1.2.0-rc3`
* `de.abda.erezeptabgabedatenbasis#1.2.0-rc2`
* `de.abda.erezeptabgabedatenbasis#1.2.0-rc`
* `de.abda.erezeptabgabedatenbasis#1.1.3`
* `de.abda.erezeptabgabedatenbasis#1.1.2`
* `de.abda.erezeptabgabedatenbasis#1.1.1`
* `de.abda.erezeptabgabedatenbasis#1.1.0`
* `de.abda.erezeptabgabedatenbasis#1.1.0-rc2`


---

### Adverse Event Clinical Research R4 Backport

<details>
<summary>Description</summary>

### Intent The intent of this guide is to provide a profile on the FHIR AdverseEvent Resource suitable for Clinical Research.  ### Overview A single Adverse Event (AE) may need to be reported in multiple ways. Choosing the appropriate form of the reporting is dependent upon workflow patterns. In particular, the implementation guides for Clinical Care adverse events and Clinical Research adverse events provide important extensions, value-sets and examples for implementing AdverseEvent.  This guide, the Clinical Research adverse event implementation guide, is for the clinical research setting. In this setting, the event is tracked and evaluated as part of the clinical research process for the research study.  In the research setting an adverse event is the result of an intervention that caused unintentional harm to a specific subject or group of subjects (this is surfaced in the profile as a constraint of ‘actual’ for the value of ‘actuality’). An example of an adverse event in the clinical research setting would be a patient develops renal failure while on a study drug. These events are characterized by the need to capture cause-and-effect (although they might not be known at the time of the event), severity, and outcome.  The context of an adverse event is also important, and captured in the AdverseEvent Clinical Research Profile data elements. A subject may have condition(s) or current treatments (medications, diet, devices) that impact their response to a newly introduced medication, device or procedure. Knowledge of these variables is essential in establishing a cause-and-effect relationship for an adverse event. This information is represented with corresponding resources (e.g. Procedure Resource for procedures, etc.) and referenced.  A potential adverse event may also be called a near miss or an error, these are not reported with the AdverseEvent Clinical Research Profile.  ### Scope This FHIR IG enables the collection of adverse events in real-world data (RWD) sources such as electronic health records (EHR) and personal health records (PHR) that occur during clinical trials. It ensures the appropriate AE representation required to support clinical research trials within a regulated environment. As the AEs are collected in RWD sources, the data can be transmitted via FHIR to clinical trial management systems, regulatory agencies, sponsors, and clinical research organizations for further processing and reporting.  In the pre-market clinical research setting, serious adverse events must be reported to the sponsor, clinical research organization, and regulatory agencies within a specific time frame for Institutional Review Boards (IRBs) and Data Safety Monitoring Board (DSMB) review. By using this IG, a clinical investigator can document an AE in the EHR, it can be received by a secondary clinical trial management system for triage and then forwarded to the sponsor and regulatory agencies. Similarly, a patient on a clinical trial can record an adverse event in their PHR that is then shared with the clinical investigator and reported to the sponsor and regulatory agencies as necessary. In a post-market situation, a patient, provider, or manufacturer can record the adverse event in a system and then report it to the FDA as a FHIR based MedWatch form.  Within this guide are several examples. Every effort has been made to capture the most important details of the use of the AdverseEvent profile. However, some examples may provide only a stub to referenced resources (e.g. instances of Patient Resource will be referenced using logical ids but are not resolvable, implementation of Patient is left for other guidance and is not the subject of this guide). Connectathons are ideal opportunities to create, compare and consider the holistic implementation of all FHIR Resources. (built Tue, Apr 30, 2024 20:50+0000+00:00)

</details>

**Versions**

* `hl7.fhir.uv.ae-research-backport-ig#1.0.1`
* `hl7.fhir.uv.ae-research-backport-ig#1.0.0`
* `hl7.fhir.uv.ae-research-backport-ig#1.0.0-ballot`


---

### devdays.letsbuild.simplifier

<details>
<summary>Description</summary>

Let's Build - Profiles with Forge 

</details>

**Versions**

* `devdays.letsbuild.simplifier#0.1.0-test`


---

### SIL-TH FHIR Extension Library (STU2)

<details>
<summary>Description</summary>

An extension registry which an be cited by FHIR artifacts (built Thu, Oct 12, 2023 00:10+0700+07:00)

</details>

**Versions**

* `silth.fhir.th.extensions#1.0.0`
* `silth.fhir.th.extensions#0.1.0`


---

### Tiga interface implementation guide

<details>
<summary>Description</summary>

This Guide describes Tiga interfaces (built Fri, Jul 15, 2022 16:34+0200+02:00)

</details>

**Versions**

* `tigacorehub.patient#1.1.30`
* `tigacorehub.patient#1.1.29`
* `tigacorehub.patient#1.1.28`
* `tigacorehub.patient#1.1.27`
* `tigacorehub.patient#1.1.26`
* `tigacorehub.patient#1.1.25`
* `tigacorehub.patient#1.1.24`
* `tigacorehub.patient#1.1.23`
* `tigacorehub.patient#1.1.22`
* `tigacorehub.patient#1.1.21`
* `tigacorehub.patient#1.1.20`
* `tigacorehub.patient#1.1.19`
* `tigacorehub.patient#1.1.18`
* `tigacorehub.patient#1.1.17`
* `tigacorehub.patient#1.1.16`
* `tigacorehub.patient#1.1.15`
* `tigacorehub.patient#1.1.14`
* `tigacorehub.patient#1.1.13`
* `tigacorehub.patient#1.1.12`
* `tigacorehub.patient#1.1.11`
* `tigacorehub.patient#1.1.10`
* `tigacorehub.patient#1.1.9`
* `tigacorehub.patient#1.1.8`
* `tigacorehub.patient#1.1.7`
* `tigacorehub.patient#1.1.6`
* `tigacorehub.patient#1.1.5`
* `tigacorehub.patient#1.1.4`
* `tigacorehub.patient#1.1.3`
* `tigacorehub.patient#1.1.2`
* `tigacorehub.patient#1.1.1`


---

### Mobile access to Health Documents (MHD)

<details>
<summary>Description</summary>

ImplementationGuide for IHE IT Infrastructure Technical Framework Supplement Mobile access to Health Documents (MHD) Rev. 4.0.1 normative (built Mon, Nov 8, 2021 19:09-0600-06:00)

</details>

**Versions**

* `ihe.mhd.fhir#4.0.2`
* `ihe.mhd.fhir#4.0.1`
* `ihe.mhd.fhir#4.0.0-comment`


---

### canadian.fsh.demo

<details>
<summary>Description</summary>

Demo project for FSH use in Simiplifier. 

</details>

**Versions**

* `canadian.fsh.demo#0.1.0-test`


---

### nxh.fhir.r4

<details>
<summary>Description</summary>

Technical profiles used by nexuzhealth for the import and export of data. 

</details>

**Versions**

* `nxh.fhir.r4#0.0.0-beta.13`
* `nxh.fhir.r4#0.0.0-beta.12`
* `nxh.fhir.r4#0.0.0-beta.11`
* `nxh.fhir.r4#0.0.0-beta.10`
* `nxh.fhir.r4#0.0.0-beta.9`
* `nxh.fhir.r4#0.0.0-beta.8`
* `nxh.fhir.r4#0.0.0-beta.7`
* `nxh.fhir.r4#0.0.0-beta.6`
* `nxh.fhir.r4#0.0.0-beta.5`
* `nxh.fhir.r4#0.0.0-beta.4`
* `nxh.fhir.r4#0.0.0-beta.3`
* `nxh.fhir.r4#0.0.0-beta.2`
* `nxh.fhir.r4#0.0.0-beta.1`
* `nxh.fhir.r4#0.0.0-beta`


---

### star.rhecord.beta

<details>
<summary>Description</summary>

Medizinische Inofmrationsobjekte (MIO) Rhecord

</details>

**Versions**

* `star.rhecord.beta#0.0.3-beta`


---

### jp-core.draft1

<details>
<summary>Description</summary>

JP-CORE Draft V1
This Project id for Releasing  JP-CORE Draft V1.
This site is for resources except Implementation Guide.
Implementation Guide for  JP-CORE Draft V1 is on another project site called "JP-CORE".


</details>

**Versions**

* `jp-core.draft1#1.0.1-beta`


---

### Northwell.Extensions

<details>
<summary>Description</summary>

Northwell Extensions for EmployeeStatus and VeteranStatus

</details>

**Versions**

* `Northwell.Extensions#0.0.1`


---

### de.dit-connectathon.r4

<details>
<summary>Description</summary>

Patient-Profile für den DIT Connectathon

</details>

**Versions**

* `de.dit-connectathon.r4#0.3.0`
* `de.dit-connectathon.r4#0.2.0`
* `de.dit-connectathon.r4#0.1.0`


---

### hl7.fhir.essilux.core

<details>
<summary>Description</summary>

Essilor Luxottica IT EYECARE FHIR PROJECT

</details>

**Versions**

* `hl7.fhir.essilux.core#0.0.2`
* `hl7.fhir.essilux.core#0.0.1`


---

### ehelse.fhir.no.grunndata.test

<details>
<summary>Description</summary>

GD R4 Test-og-lek

Benyttes til testing
Grunndata

SearchParameter fixes and flyttedato, bostedsadressedato, startdatoForKontrakt and sluttdatoForKontrakt documentation

Please read changelog.

</details>

**Versions**

* `ehelse.fhir.no.grunndata.test#2.3.3`
* `ehelse.fhir.no.grunndata.test#2.3.2`
* `ehelse.fhir.no.grunndata.test#2.3.1`
* `ehelse.fhir.no.grunndata.test#2.3.0`
* `ehelse.fhir.no.grunndata.test#2.2.0`


---

### Implementierungsleitfaden DEMIS - Statistische Erhebungen

<details>
<summary>Description</summary>

Beinhaltet die in DEMIS für die Umsetzung verschiedener statistischer Erhebungen genutzten Informationsmodellartefakte

</details>

**Versions**

* `rki.demis.statistic#1.0.0`


---

### firely.com.accessibilitytesting

<details>
<summary>Description</summary>

A project to test the accessibility of the platform 

</details>

**Versions**

* `firely.com.accessibilitytesting#0.0.1-test`


---

### Document Subscription for Mobile (DSUBm)

<details>
<summary>Description</summary>

profile describes the use of document subscription and notification mechanisms for RESTful applications. (built Thu, Feb 29, 2024 12:23-0600-06:00)

</details>

**Versions**

* `ihe.iti.dsubm#1.0.0`
* `ihe.iti.dsubm#1.0.0-comment`


---

### pbm.v1.fhir

<details>
<summary>Description</summary>

Covering Patient-Blood-Management related resources for analysis and optimization of pre-operative patients

</details>

**Versions**

* `pbm.v1.fhir#0.7.0`
* `pbm.v1.fhir#0.6.0`


---

### PACIO Re-Assessment Timepoints Implementation Guide

<details>
<summary>Description</summary>

PACIO Re-Assessment Timepoints Implementation Guide (built Wed, Sep 28, 2022 16:27+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.pacio-rt#1.0.0`
* `hl7.fhir.us.pacio-rt#0.1.0`


---

### uk.nhsdigital.medicines.r4.test

<details>
<summary>Description</summary>

Electronic Prescription Service

</details>

**Versions**

* `uk.nhsdigital.medicines.r4.test#2.8.7-prerelease`
* `uk.nhsdigital.medicines.r4.test#2.8.3-prerelease`
* `uk.nhsdigital.medicines.r4.test#2.8.16-prerelease`
* `uk.nhsdigital.medicines.r4.test#2.8.11-prerelease`
* `uk.nhsdigital.medicines.r4.test#2.7.1-prerelease`
* `uk.nhsdigital.medicines.r4.test#2.6.6-prerelease`


---

### DaVinci Payer Data Exchange (PDex) US Drug Formulary

<details>
<summary>Description</summary>

DaVinci Payer Data Exchange (PDex) US Drug Formulary, Release 2.0.1 - US Realm STU" (built Fri, Dec 1, 2023 22:17+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.davinci-drug-formulary#2.0.1`
* `hl7.fhir.us.davinci-drug-formulary#2.0.0`
* `hl7.fhir.us.davinci-drug-formulary#1.2.0`
* `hl7.fhir.us.davinci-drug-formulary#1.1.0`
* `hl7.fhir.us.davinci-drug-formulary#1.0.1`
* `hl7.fhir.us.davinci-drug-formulary#1.0.0`
* `hl7.fhir.us.davinci-drug-formulary#0.1.0`


---

### acme.fsh.ig.example

<details>
<summary>Description</summary>

Example project from ACME Corp on using FHIR Shorthand/sushi and the IG Publisher 

</details>

**Versions**

* `acme.fsh.ig.example#0.0.1-demo`


---

### DaVinci PDEX Plan Net

<details>
<summary>Description</summary>

Davinci PDEX Plan Net (built Mon, Apr 4, 2022 14:01+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.davinci-pdex-plan-net#1.1.0`
* `hl7.fhir.us.davinci-pdex-plan-net#1.0.0`
* `hl7.fhir.us.davinci-pdex-plan-net#0.1.0`


---

### Patient Identifier Cross-referencing for mobile (PIXm)

<details>
<summary>Description</summary>

ImplementationGuide for IHE IT Infrastructure Technical Framework Supplement Patient Identifier Cross-referencing for mobile (PIXm) (built Thu, Feb 22, 2024 13:07-0600-06:00)

</details>

**Versions**

* `ihe.iti.pixm#3.0.4`
* `ihe.iti.pixm#3.0.3`
* `ihe.iti.pixm#3.0.2`


---

### de.medizininformatikinitiative.kerndatensatz.studie

<details>
<summary>Description</summary>

Medizininformatik Initiative - Modul Medizinisches Forschungsvorhaben

</details>

**Versions**

* `de.medizininformatikinitiative.kerndatensatz.studie#1.0.0`
* `de.medizininformatikinitiative.kerndatensatz.studie#1.0.0-ballot`


---

### CH IPS (R4)

<details>
<summary>Description</summary>

The FHIR implementation guide CH IPS is derived from the Swiss Core IG (CH Core) and ensures conformity with the International Patient Summary (IPS). (built Fri, May 17, 2024 10:21+0000+00:00)

</details>

**Versions**

* `ch.fhir.ig.ch-ips#1.0.0-ballot`


---

### Implementation Guide CHMED20AF (R4)

<details>
<summary>Description</summary>

CHMED20AF is the FHIR Implementation Guide to represent the eMediplan in FHIR (built Wed, Jun 30, 2021 14:02+0200+02:00)

</details>

**Versions**

* `ch.chmed20af.emediplan#2.0.0`
* `ch.chmed20af.emediplan#1.0.0`


---

### de.gematik.isik-dokumentenaustausch

<details>
<summary>Description</summary>

Dokumentenaustausch-Modul der Informationstechnischen Systeme im Krankenhaus Ausbaustufe 4

</details>

**Versions**

* `de.gematik.isik-dokumentenaustausch#4.0.0-rc`
* `de.gematik.isik-dokumentenaustausch#3.0.3`
* `de.gematik.isik-dokumentenaustausch#3.0.2`
* `de.gematik.isik-dokumentenaustausch#3.0.1`
* `de.gematik.isik-dokumentenaustausch#3.0.0`


---

### AndersonSanto.Tarefa6

<details>
<summary>Description</summary>

FHIR Training Personal

</details>

**Versions**

* `AndersonSanto.Tarefa6#1.0.0`


---

### de.TestprojektUKF.rmy

<details>
<summary>Description</summary>

Testprojekt für Fhir-Schulung Gefyra

</details>

**Versions**

* `de.TestprojektUKF.rmy#0.1.0`


---

### FHIR implementation of Patient Corrections

<details>
<summary>Description</summary>

NL package of FHIR R4 conformance resources for Patient Corrections.

</details>

**Versions**

* `nictiz.fhir.nl.r4.patientcorrections#1.0.6`
* `nictiz.fhir.nl.r4.patientcorrections#1.0.1`
* `nictiz.fhir.nl.r4.patientcorrections#1.0.0`


---

### Health New Zealand Te Whatu Ora Digital Tooling Implementation Guide

<details>
<summary>Description</summary>

FHIR profiles used in FHIR Publishing processes within Health New Zealand Te Whatu Ora (built Mon, Jun 17, 2024 02:48+0000+00:00)

</details>

**Versions**

* `tewhatuora.digitaltooling.iguat#0.0.6`
* `tewhatuora.digitaltooling.iguat#0.0.5`
* `tewhatuora.digitaltooling.iguat#0.0.4`


---

### LogicNets.NCCN

<details>
<summary>Description</summary>

LogicNets FHIR Projects

</details>

**Versions**

* `LogicNets.NCCN#0.8.1`


---

### Genomics Reporting Implementation Guide

<details>
<summary>Description</summary>

Guidelines for reporting of clinical genomics results using HL7 FHIR. (built Mon, Dec 18, 2023 22:39+0000+00:00)

</details>

**Versions**

* `hl7.fhir.uv.genomics-reporting#3.0.0-ballot`
* `hl7.fhir.uv.genomics-reporting#2.0.0`
* `hl7.fhir.uv.genomics-reporting#1.1.0`
* `hl7.fhir.uv.genomics-reporting#1.0.0`
* `hl7.fhir.uv.genomics-reporting#0.3.0`
* `hl7.fhir.uv.genomics-reporting#0.1.0`


---

### SMART Health Cards: Vaccination &amp; Testing Implementation Guide

<details>
<summary>Description</summary>

Defines the clinical and patient information contained within a SMART Health Card (SHC) related to vaccination and lab results related to an infectious disease like COVID-19. (built Fri, Aug 13, 2021 13:26+0000+00:00)

</details>

**Versions**

* `hl7.fhir.uv.shc-vaccination#0.6.2`


---

### HL7 Belgium Vaccination (Patient Dossier)

<details>
<summary>Description</summary>

Belgian Patient profiles (built Mon, Jun 26, 2023 17:48+0200+02:00)

</details>

**Versions**

* `hl7.fhir.be.vaccination#1.0.3`
* `hl7.fhir.be.vaccination#1.0.2`
* `hl7.fhir.be.vaccination#1.0.1`
* `hl7.fhir.be.vaccination#1.0.0`


---

### fhir.dgmc

<details>
<summary>Description</summary>

DGMC

</details>

**Versions**

* `fhir.dgmc#0.1.1`


---

### Health Care Surveys Content Implementation Guide (IG)

<details>
<summary>Description</summary>

The Health Care Surveys Content IG provides healthcare organizations the necessary data exchange mechanisms to report health care survey data to public health agencies. (built Tue, Aug 22, 2023 19:16+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.health-care-surveys-reporting#1.0.0`
* `hl7.fhir.us.health-care-surveys-reporting#0.1.0`


---

### sfm.030322

<details>
<summary>Description</summary>

Medication related projects (SFM, PLL, SAFEST, KIKJ)

</details>

**Versions**

* `sfm.030322#2.0.1`


---

### ca.infoway.io.erec

<details>
<summary>Description</summary>

The CA:eReC iGuide seeks to provide guidance around the messaging paradigm and other patterns related to sending and receiving eReferrals and eConsults.

</details>

**Versions**

* `ca.infoway.io.erec#1.0.0-dft-ballot`


---

### ereferralontario.core

<details>
<summary>Description</summary>

Ontario-specific business use cases and content

</details>

**Versions**

* `ereferralontario.core#0.10.2`


---

### de.emperra.esysta

<details>
<summary>Description</summary>

Spezifikation des Exportformats

</details>

**Versions**

* `de.emperra.esysta#1.0.0`


---

### FHIR Human Services Directory

<details>
<summary>Description</summary>

fhir-human-services-directory (built Wed, Oct 4, 2023 18:49+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.hsds#1.0.0`
* `hl7.fhir.us.hsds#1.0.0-ballot`


---

### CH RAD-Order (R4)

<details>
<summary>Description</summary>

Implementation guide CH RAD-Order (R4) (built Fri, May 17, 2024 11:21+0000+00:00)

</details>

**Versions**

* `ch.fhir.ig.ch-rad-order#2.0.0-ballot`
* `ch.fhir.ig.ch-rad-order#1.0.0`
* `ch.fhir.ig.ch-rad-order#0.1.0`


---

### OntarioContextManagement.core

<details>
<summary>Description</summary>

Based on FHIR Cast

</details>

**Versions**

* `OntarioContextManagement.core#0.1.0`


---

### MoPH Primary Care 1 (MoPH-PC-1) - FHIR Implementation Guide (STU2)

<details>
<summary>Description</summary>

An implementation guide for adopting FHIR for health information exchange in 43files-plus format. (built Mon, Sep 18, 2023 10:56+0700+07:00)

</details>

**Versions**

* `silth.fhir.th.mophpc1#1.0.0`
* `silth.fhir.th.mophpc1#0.1.1`


---

### Mobile Antepartum Summary

<details>
<summary>Description</summary>

Antepartum Summary is a content profile that defines the structure for the aggregation of significant events, diagnoses, and plans of care derived from the visits over the course of an antepartum episode. (built Tue, Jun 4, 2024 15:27-0500-05:00)

</details>

**Versions**

* `ihe.pcc.maps#1.0.0-comment`


---

### elona.health

<details>
<summary>Description</summary>

Technical specification of data exports used in apps by Elona Health.

</details>

**Versions**

* `elona.health#1.0.3`
* `elona.health#1.0.1`
* `elona.health#1.0.0`


---

### questinnaire.profiles

<details>
<summary>Description</summary>

Test Profiles for Questionnaire App

</details>

**Versions**

* `questinnaire.profiles#0.0.2`


---

### CH LAB-Report (R4)

<details>
<summary>Description</summary>

FHIR® Implementation Guide for Laboratory Reports in Switzerland (built Fri, May 17, 2024 09:30+0000+00:00)

</details>

**Versions**

* `ch.fhir.ig.ch-lab-report#1.0.0-ballot`
* `ch.fhir.ig.ch-lab-report#0.1.1`
* `ch.fhir.ig.ch-lab-report#0.1.0`


---

### HRSA 2023 Uniform Data System (UDS) Patient Level Submission (PLS) (UDS+) FHIR IG

<details>
<summary>Description</summary>

A brief description of what udsplus is about (probably the same text as in your readme) (built Fri, Dec 1, 2023 16:16+1100+11:00)

</details>

**Versions**

* `fhir.hrsa.uds-plus#1.0.1`


---

### Finnish Implementation Guide for SMART App Launch

<details>
<summary>Description</summary>

Guidelines for using the SMART App Launch mechanism in Finland. (built Thu, Nov 9, 2023 22:50+0200+02:00)

</details>

**Versions**

* `hl7.fhir.fi.smart#1.0.0`
* `hl7.fhir.fi.smart#1.0.0-rc9`
* `hl7.fhir.fi.smart#1.0.0-rc8`
* `hl7.fhir.fi.smart#1.0.0-rc7`
* `hl7.fhir.fi.smart#1.0.0-rc6`
* `hl7.fhir.fi.smart#1.0.0-rc5`
* `hl7.fhir.fi.smart#1.0.0-rc2`
* `hl7.fhir.fi.smart#1.0.0-rc1`


---

### koppeltaalv2.00

<details>
<summary>Description</summary>

Draft of used FHIR resource profiles in Koppeltaal 2.0.

</details>

**Versions**

* `koppeltaalv2.00#0.12.0-beta.6`
* `koppeltaalv2.00#0.12.0-beta.5`
* `koppeltaalv2.00#0.12.0-beta.4`
* `koppeltaalv2.00#0.12.0-beta.3`
* `koppeltaalv2.00#0.12.0-beta.2`
* `koppeltaalv2.00#0.12.0-beta.1`
* `koppeltaalv2.00#0.12.0`
* `koppeltaalv2.00#0.11.1`
* `koppeltaalv2.00#0.11.0`
* `koppeltaalv2.00#0.10.1`
* `koppeltaalv2.00#0.10.0`
* `koppeltaalv2.00#0.9.9`
* `koppeltaalv2.00#0.9.8`
* `koppeltaalv2.00#0.9.7`
* `koppeltaalv2.00#0.9.6`
* `koppeltaalv2.00#0.9.5`
* `koppeltaalv2.00#0.9.3`
* `koppeltaalv2.00#0.9.2`
* `koppeltaalv2.00#0.9.1`
* `koppeltaalv2.00#0.9.0`
* `koppeltaalv2.00#0.8.0`
* `koppeltaalv2.00#0.7.3`
* `koppeltaalv2.00#0.7.2`
* `koppeltaalv2.00#0.7.1`
* `koppeltaalv2.00#0.7.2-beta`


---

### FHIR Core package

<details>
<summary>Description</summary>

FHIR Core package - the NPM package that contains all the definitions for the base FHIR specification (built Sat, May 28, 2022 12:47+1000+10:00)

</details>

**Versions**

* `hl7.fhir.r4b.core#4.3.0`
* `hl7.fhir.r4b.core#4.1.0`


---

### EHR and PHR System Functional Models - Record Lifecycle Events Implementation Guide

<details>
<summary>Description</summary>

EHRS Functional Model - Record Lifecycle Events - FHIR Implementation Guide (built Tue, Jan 2, 2024 21:56+0000+00:00)

</details>

**Versions**

* `hl7.fhir.uv.ehrs-rle#1.1.0`
* `hl7.fhir.uv.ehrs-rle#1.0.0-ballot`


---

### laniado.test.fhir.r4

<details>
<summary>Description</summary>

Laniado Hospital Test Project

</details>

**Versions**

* `laniado.test.fhir.r4#0.1.31`
* `laniado.test.fhir.r4#0.1.30`
* `laniado.test.fhir.r4#0.1.29`
* `laniado.test.fhir.r4#0.1.28`
* `laniado.test.fhir.r4#0.1.27`
* `laniado.test.fhir.r4#0.1.26`
* `laniado.test.fhir.r4#0.1.25`
* `laniado.test.fhir.r4#0.1.24`
* `laniado.test.fhir.r4#0.1.23`
* `laniado.test.fhir.r4#0.1.22`
* `laniado.test.fhir.r4#0.1.21`
* `laniado.test.fhir.r4#0.1.20`
* `laniado.test.fhir.r4#0.1.19`
* `laniado.test.fhir.r4#0.1.18`
* `laniado.test.fhir.r4#0.1.17`
* `laniado.test.fhir.r4#0.1.16`
* `laniado.test.fhir.r4#0.1.15`
* `laniado.test.fhir.r4#0.1.14`
* `laniado.test.fhir.r4#0.1.13`
* `laniado.test.fhir.r4#0.1.12`
* `laniado.test.fhir.r4#0.1.11`
* `laniado.test.fhir.r4#0.1.10`
* `laniado.test.fhir.r4#0.1.9`
* `laniado.test.fhir.r4#0.1.8`
* `laniado.test.fhir.r4#0.1.7`
* `laniado.test.fhir.r4#0.1.6`
* `laniado.test.fhir.r4#0.1.5`
* `laniado.test.fhir.r4#0.1.4`
* `laniado.test.fhir.r4#0.1.3`
* `laniado.test.fhir.r4#0.1.2`
* `laniado.test.fhir.r4#0.1.1`


---

### ca.on.oh-setp

<details>
<summary>Description</summary>

The purpose of SETP is to improve surgical performance in Ontario through the measurement and report

</details>

**Versions**

* `ca.on.oh-setp#0.9.0-alpha1.0.33`
* `ca.on.oh-setp#0.9.0-alpha1.0.32`
* `ca.on.oh-setp#0.9.0-alpha1.0.31`
* `ca.on.oh-setp#0.9.0-alpha1.0.30`
* `ca.on.oh-setp#0.9.0-alpha1.0.29`
* `ca.on.oh-setp#0.9.0-alpha1.0.28`
* `ca.on.oh-setp#0.9.0-alpha1.0.27`
* `ca.on.oh-setp#0.9.0-alpha1.0.26`
* `ca.on.oh-setp#0.9.0-alpha1.0.25`
* `ca.on.oh-setp#0.9.0-alpha1.0.24`
* `ca.on.oh-setp#0.9.0-alpha1.0.23`
* `ca.on.oh-setp#0.9.0-alpha1.0.22`
* `ca.on.oh-setp#0.9.0-alpha1.0.18`
* `ca.on.oh-setp#0.9.0-alpha1.0.17`
* `ca.on.oh-setp#0.9.0-alpha1.0.16`
* `ca.on.oh-setp#0.9.0-alpha1.0.15`
* `ca.on.oh-setp#0.9.0-alpha1.0.14`
* `ca.on.oh-setp#0.9.0-alpha1.0.13`
* `ca.on.oh-setp#0.9.0-alpha1.0.12`
* `ca.on.oh-setp#0.9.0-alpha1.0.11`
* `ca.on.oh-setp#0.9.0-alpha1.0.10`
* `ca.on.oh-setp#0.9.0-alpha1.0.9`
* `ca.on.oh-setp#0.9.0-alpha1.0.8`
* `ca.on.oh-setp#0.9.0-alpha1.0.7`
* `ca.on.oh-setp#0.9.0-alpha-1.0.6`
* `ca.on.oh-setp#0.9.0-alpha1.0.5`
* `ca.on.oh-setp#1.0.38`
* `ca.on.oh-setp#1.0.37`
* `ca.on.oh-setp#1.0.36`
* `ca.on.oh-setp#1.0.35`
* `ca.on.oh-setp#1.0.34`


---

### de.medizininformatikinitiative.kerndatensatz.laborbefund

<details>
<summary>Description</summary>

Put a description here

</details>

**Versions**

* `de.medizininformatikinitiative.kerndatensatz.laborbefund#1.0.6`
* `de.medizininformatikinitiative.kerndatensatz.laborbefund#1.0.7-alpha1`


---

### alpha.core.r4

<details>
<summary>Description</summary>

National Alpha country profiles

</details>

**Versions**

* `alpha.core.r4#1.0.0`


---

### acme.dallas.cowboy

<details>
<summary>Description</summary>

Definition of the Dallas Cowboy

</details>

**Versions**

* `acme.dallas.cowboy#0.1.0-alpha`


---

### acme.minneapolis.cowboy

<details>
<summary>Description</summary>

This is our Cowboy project

</details>

**Versions**

* `acme.minneapolis.cowboy#0.1.0`


---

### eVO.himi

<details>
<summary>Description</summary>

elektronische Verordnung

</details>

**Versions**

* `eVO.himi#0.0.1`


---

### odilab.evo

<details>
<summary>Description</summary>

Testprojekt für Hilfs- und Heilmittel eVerordnungen

</details>

**Versions**

* `odilab.evo#0.0.5`
* `odilab.evo#0.0.4`
* `odilab.evo#0.0.3`
* `odilab.evo#0.0.2`
* `odilab.evo#0.0.1`


---

### cidadex.testex

<details>
<summary>Description</summary>

rede de dados em saúde da cidade X

</details>

**Versions**

* `cidadex.testex#1.0.1-beta`


---

### furore.test.fsh.demo

<details>
<summary>Description</summary>

Furore FSH generated test profiles for validation. 

</details>

**Versions**

* `furore.test.fsh.demo#0.1.0-rc1`


---

### Patient Demographics Query for mobile (PDQm)

<details>
<summary>Description</summary>

The Patient Demographics Query for Mobile (PDQm) Profile defines a lightweight RESTful interface to a patient demographics supplier leveraging technologies readily available to mobile applications and lightweight browser based applications. (built Mon, Nov 8, 2021 19:29-0600-06:00)

</details>

**Versions**

* `IHE.ITI.PDQm#2.3.0`
* `IHE.ITI.PDQm#2.2.1`


---

### Terminology Support (r4)

<details>
<summary>Description</summary>

Various supporting code systems for tx.fhir.org (R4)

</details>

**Versions**

* `fhir.tx.support.r4#0.25.0`
* `fhir.tx.support.r4#0.24.0`
* `fhir.tx.support.r4#0.22.0`
* `fhir.tx.support.r4#0.21.0`
* `fhir.tx.support.r4#0.20.0`
* `fhir.tx.support.r4#0.19.0`
* `fhir.tx.support.r4#0.18.0`
* `fhir.tx.support.r4#0.17.0`
* `fhir.tx.support.r4#0.16.0`
* `fhir.tx.support.r4#0.15.0`
* `fhir.tx.support.r4#0.14.0`
* `fhir.tx.support.r4#0.13.0`
* `fhir.tx.support.r4#0.12.0`
* `fhir.tx.support.r4#0.11.0`
* `fhir.tx.support.r4#0.10.0`
* `fhir.tx.support.r4#0.9.0`
* `fhir.tx.support.r4#0.8.0`
* `fhir.tx.support.r4#0.7.0`
* `fhir.tx.support.r4#0.6.0`
* `fhir.tx.support.r4#0.5.0`
* `fhir.tx.support.r4#0.4.0`
* `fhir.tx.support.r4#0.3.1`
* `fhir.tx.support.r4#0.3.0`
* `fhir.tx.support.r4#0.2.0`
* `fhir.tx.support.r4#0.1.0`


---

### de.gematik.isik-vitalparameter

<details>
<summary>Description</summary>

Package Release des ISiK Modul Vitalparameter

</details>

**Versions**

* `de.gematik.isik-vitalparameter#4.0.0-rc`
* `de.gematik.isik-vitalparameter#4.0.0-alpha-validation-MII`
* `de.gematik.isik-vitalparameter#4.0.0-alpha-validation-MII-2`
* `de.gematik.isik-vitalparameter#3.0.3`
* `de.gematik.isik-vitalparameter#3.0.2`
* `de.gematik.isik-vitalparameter#3.0.1`
* `de.gematik.isik-vitalparameter#2.0.3`


---

### commonwell-consent-trial01.01

<details>
<summary>Description</summary>

CommonWell consent - attempt 01

</details>

**Versions**

* `commonwell-consent-trial01.01#0.0.1`


---

### iknl.fhir.nl.r4.performation

<details>
<summary>Description</summary>

FHIR R4 profielen voor samenwerking tussen IKNL en Performation

</details>

**Versions**

* `iknl.fhir.nl.r4.performation#0.2.0`
* `iknl.fhir.nl.r4.performation#0.1.0`


---

### Common FHIR profile vendor collaboration



**Versions**

* `care.commonprofiles.fhir#1.0.1`
* `care.commonprofiles.fhir#1.0.0`


---

### Using CQL with FHIR

<details>
<summary>Description</summary>

This implementation guide defines profiles, operations and guidance for the use of CQL with FHIR, both as a mechanism for querying, as well as inline and integrated usage as part of knowledge artifacts. (built Fri, May 31, 2024 14:18+0000+00:00)

</details>

**Versions**

* `hl7.fhir.uv.cql#1.0.0`
* `hl7.fhir.uv.cql#1.0.0-snapshot`
* `hl7.fhir.uv.cql#1.0.0-ballot`


---

### Nomenclatures des objets de santé (NOS)

<details>
<summary>Description</summary>

Les nomenclatures des objets de Sante (built Wed, Feb 28, 2024 14:48+0000+00:00)

</details>

**Versions**

* `ans.fr.nos#1.2.0`
* `ans.fr.nos#1.1.0`


---

### Da Vinci Unsolicited Notifications



**Versions**

* `hl7.fhir.us.davinci-alerts#1.0.0`
* `hl7.fhir.us.davinci-alerts#0.2.0`
* `hl7.fhir.us.davinci-alerts#0.1.0`


---

### Basic Audit Log Patterns (BALP)

<details>
<summary>Description</summary>

The Basic Audit Log Patterns (BALP) Implementation Guide is a Content Profile that defines some basic and reusable AuditEvent patterns. This includes basic audit log profiles for FHIR RESTful operations to be used when there is not a more specific audit event defined. A focus is enabling Privacy centric AuditEvent logs that hold well formed indication of the Patient when they are the subject of the activity being recorded in the log. Where a more specific audit event can be defined it should be derived off of these basic patterns. (built Wed, Feb 14, 2024 15:23-0600-06:00)

</details>

**Versions**

* `ihe.iti.balp#1.1.3`
* `ihe.iti.balp#1.1.2`
* `ihe.iti.balp#1.1.1`
* `ihe.iti.balp#1.1.0`


---

### MedNet interface implementation guide

<details>
<summary>Description</summary>

This Guide describes Tiga interfaces (built Fri, Jul 15, 2022 16:34+0200+02:00)

</details>

**Versions**

* `TigaCoreHUB.Patient#1.1.0`


---

### uk.nhsdigital.r4

<details>
<summary>Description</summary>

NHS (England) National Services Implementation Guide

</details>

**Versions**

* `uk.nhsdigital.r4#2.8.0`
* `uk.nhsdigital.r4#2.7.0`
* `uk.nhsdigital.r4#2.6.1`
* `uk.nhsdigital.r4#2.6.0`
* `uk.nhsdigital.r4#2.5.2`
* `uk.nhsdigital.r4#2.5.1`
* `uk.nhsdigital.r4#2.5.0`
* `uk.nhsdigital.r4#2.4.7`
* `uk.nhsdigital.r4#2.4.5`
* `uk.nhsdigital.r4#2.4.4`
* `uk.nhsdigital.r4#2.4.3`
* `uk.nhsdigital.r4#2.1.9-discovery`
* `uk.nhsdigital.r4#2.1.8-discovery`
* `uk.nhsdigital.r4#2.1.7-discovery`
* `uk.nhsdigital.r4#2.1.6-discovery`
* `uk.nhsdigital.r4#2.1.5-discovery`
* `uk.nhsdigital.r4#2.1.4-discovery`
* `uk.nhsdigital.r4#2.1.34-discovery`
* `uk.nhsdigital.r4#2.1.33-discovery`
* `uk.nhsdigital.r4#2.1.32-discovery`
* `uk.nhsdigital.r4#2.1.31-discovery`
* `uk.nhsdigital.r4#2.1.30-discovery`
* `uk.nhsdigital.r4#2.1.3-dev`
* `uk.nhsdigital.r4#2.1.29-discovery`
* `uk.nhsdigital.r4#2.1.28-discovery`
* `uk.nhsdigital.r4#2.1.27-discovery`
* `uk.nhsdigital.r4#2.1.26-discovery`
* `uk.nhsdigital.r4#2.1.25-discovery`
* `uk.nhsdigital.r4#2.1.24-discovery`
* `uk.nhsdigital.r4#2.1.23-discovery`
* `uk.nhsdigital.r4#2.1.22-discovery`
* `uk.nhsdigital.r4#2.1.21-discovery`
* `uk.nhsdigital.r4#2.1.20-discovery`
* `uk.nhsdigital.r4#2.1.2-dev`
* `uk.nhsdigital.r4#2.1.19-discovery`
* `uk.nhsdigital.r4#2.1.18-discovery`
* `uk.nhsdigital.r4#2.1.17-discovery`
* `uk.nhsdigital.r4#2.1.16-discovery`
* `uk.nhsdigital.r4#2.1.15-discovery`
* `uk.nhsdigital.r4#2.1.14-discovery`
* `uk.nhsdigital.r4#2.1.13-discovery`
* `uk.nhsdigital.r4#2.1.12-discovery`
* `uk.nhsdigital.r4#2.1.11-discovery`
* `uk.nhsdigital.r4#2.1.10-discovery`
* `uk.nhsdigital.r4#2.1.1-dev`
* `uk.nhsdigital.r4#2.1.0-dev`
* `uk.nhsdigital.r4#2.0.24-dev`
* `uk.nhsdigital.r4#2.0.23-dev`
* `uk.nhsdigital.r4#2.0.22-dev`
* `uk.nhsdigital.r4#2.0.21-dev`
* `uk.nhsdigital.r4#2.0.20-dev`
* `uk.nhsdigital.r4#2.0.19-dev`
* `uk.nhsdigital.r4#2.0.18-dev`
* `uk.nhsdigital.r4#2.0.17-dev`
* `uk.nhsdigital.r4#2.0.16-dev`
* `uk.nhsdigital.r4#2.0.15-dev`


---

### de.gematik.epa.medication

<details>
<summary>Description</summary>

Die ePA-Medication Spezifikation

</details>

**Versions**

* `de.gematik.epa.medication#1.0.1`
* `de.gematik.epa.medication#1.0.0`
* `de.gematik.epa.medication#1.0.2-rc1`
* `de.gematik.epa.medication#1.0.0-RC`
* `de.gematik.epa.medication#0.0.3`


---

### Essais CLiniques Accessibles Interconnectés pour la Recherche ouverts à l'Ecosystème

<details>
<summary>Description</summary>

Implementation Guide pour définir l'API FHIR pour ECLAIRE (Essais CLiniques Accessibles Interconnectés pour la Recherche ouverts à l'Ecosystème) (built Mon, Feb 26, 2024 13:32+0000+00:00)

</details>

**Versions**

* `ans.fhir.fr.eclaire#0.3.0`
* `ans.fhir.fr.eclaire#0.2.0`
* `ans.fhir.fr.eclaire#0.1.0`


---

### NHSN Healthcare Associated Infection (HAI) Reports

<details>
<summary>Description</summary>

This implementation guide (IG) specifies standards for electronic submission of Healthcare Associated Infection (HAI) reports to the National Healthcare Safety Network (NHSN) of the Centers for Disease Control and Prevention (CDC). This IG contains a library of FHIR profiles for electronic submission of HAI reports to the NHSN.

As reports are modified and new report types are defined, CDC and Health Level Seven (HL7) will develop and publish additional constraints.

Throughout this process, CDC remains the authority on NHSN data collection protocols. When healthcare enterprises choose to participate in NHSN, they must report to CDC occurrences such as specific reportable procedures, even those without complications, and events such as a bloodstream infection, either confirmed by a positive blood culture or supported by a patients clinical symptoms. This specification opens the channel for data submission by all applications compliant with the data coding requirements defined here.

Note that participation in the NHSN requires enrollment and filing of reporting plans, which are not defined by this specification. For an overview of NHSN and full information on NHSN participation requirements, see: [http://www.cdc.gov/nhsn](http://www.cdc.gov/nhsn). Provisions of the Public Health Service Act protect all data reported to NHSN from discovery through the Freedom of Information Act (FOIA). (built Wed, Sep 6, 2023 19:56+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.hai#2.1.0`
* `hl7.fhir.us.hai#2.0.0`
* `hl7.fhir.us.hai#0.1.0`


---

### gdrl.fhir.r4

<details>
<summary>Description</summary>

Progetto OMr Gestione Rete Digitale Rete Laboratori per Regione Lombardia

</details>

**Versions**

* `gdrl.fhir.r4#1.2.0`
* `gdrl.fhir.r4#1.2.0-----beta`
* `gdrl.fhir.r4#1.1.0`
* `gdrl.fhir.r4#1.0.0`


---

### HL7 Terminology (THO)

<details>
<summary>Description</summary>

Defines and exposes all HL7 terminologies managed through the unified terminology governance process (built Sat, Mar 9, 2024 10:53-0700-07:00)

</details>

**Versions**

* `hl7.terminology#5.5.0`
* `hl7.terminology#5.4.0`
* `hl7.terminology#5.3.0`
* `hl7.terminology#5.2.0`
* `hl7.terminology#5.1.0`
* `hl7.terminology#4.0.0`
* `hl7.terminology#3.1.0`
* `hl7.terminology#3.0.0`


---

### leumit.fhir.r4

<details>
<summary>Description</summary>

leumit FHIR entities

</details>

**Versions**

* `leumit.fhir.r4#0.2.0`
* `leumit.fhir.r4#0.1.0`


---

### pbmesolutions.v1.fhir

<details>
<summary>Description</summary>

Trying to cover hospital Cases and Patient Blood Management Relevant information

</details>

**Versions**

* `pbmesolutions.v1.fhir#1.1.1`
* `pbmesolutions.v1.fhir#1.1.0`


---

### hl7.fhir.nume.dev

<details>
<summary>Description</summary>

FHIR Nume - Dev

</details>

**Versions**

* `hl7.fhir.nume.dev#1.0.3`
* `hl7.fhir.nume.dev#1.0.2`
* `hl7.fhir.nume.dev#1.0.1`
* `hl7.fhir.nume.dev#1.0.0`


---

### kbv.mio.ddtk

<details>
<summary>Description</summary>

Medizinische Informationsobjekte (MIO) DiGA Device Toolkit V1.0.0

</details>

**Versions**

* `kbv.mio.ddtk#1.0.0-kommentierung`


---

### Pharmaceutical Quality (Industry)

<details>
<summary>Description</summary>

This IG is developed for the HL7 International Pharmaceutical Quality (PQ) - Industry Use Case project, sponsored by the Biomedical Research and Regulation Work Group 
* [Project Proposal: PSS-2137](https://jira.hl7.org/browse/PSS-2137)
* [Project Scope Statement: PSS-2145](https://jira.hl7.org/browse/PSS-2145) (built Wed, May 8, 2024 13:18+0000+00:00)

</details>

**Versions**

* `hl7.fhir.uv.pharm-quality#1.0.0`
* `hl7.fhir.uv.pharm-quality#1.0.0-ballot`


---

### Adverse Event Clinical Research

<details>
<summary>Description</summary>

### Intent The intent of this guide is to provide a profile on the FHIR AdverseEvent Resource suitable for Clinical Research.  ### Overview A single Adverse Event (AE) may need to be reported in multiple ways. Choosing the appropriate form of the reporting is dependent upon workflow patterns. In particular, the implementation guides for Clinical Care adverse events and Clinical Research adverse events provide important extensions, value-sets and examples for implementing AdverseEvent.  This guide, the Clinical Research adverse event implementation guide, is for the clinical research setting. In this setting, the event is tracked and evaluated as part of the clinical research process for the research study.  In the research setting an adverse event is the result of an intervention that caused unintentional harm to a specific subject or group of subjects (this is surfaced in the profile as a constraint of ‘actual’ for the value of ‘actuality’). An example of an adverse event in the clinical research setting would be a patient develops renal failure while on a study drug. These events are characterized by the need to capture cause-and-effect (although they might not be known at the time of the event), severity, and outcome.  The context of an adverse event is also important, and captured in the AdverseEvent Clinical Research Profile data elements. A subject may have condition(s) or current treatments (medications, diet, devices) that impact their response to a newly introduced medication, device or procedure. Knowledge of these variables is essential in establishing a cause-and-effect relationship for an adverse event. This information is represented with corresponding resources (e.g. Procedure Resource for procedures, etc.) and referenced.  A potential adverse event may also be called a near miss or an error, these are not reported with the AdverseEvent Clinical Research Profile.  ### Scope This FHIR IG enables the collection of adverse events in real-world data (RWD) sources such as electronic health records (EHR) and personal health records (PHR) that occur during clinical trials. It ensures the appropriate AE representation required to support clinical research trials within a regulated environment. As the AEs are collected in RWD sources, the data can be transmitted via FHIR to clinical trial management systems, regulatory agencies, sponsors, and clinical research organizations for further processing and reporting.  In the pre-market clinical research setting, serious adverse events must be reported to the sponsor, clinical research organization, and regulatory agencies within a specific time frame for Institutional Review Boards (IRBs) and Data Safety Monitoring Board (DSMB) review. By using this IG, a clinical investigator can document an AE in the EHR, it can be received by a secondary clinical trial management system for triage and then forwarded to the sponsor and regulatory agencies. Similarly, a patient on a clinical trial can record an adverse event in their PHR that is then shared with the clinical investigator and reported to the sponsor and regulatory agencies as necessary. In a post-market situation, a patient, provider, or manufacturer can record the adverse event in a system and then report it to the FDA as a FHIR based MedWatch form.  Within this guide are several examples. Every effort has been made to capture the most important details of the use of the AdverseEvent profile. However, some examples may provide only a stub to referenced resources (e.g. instances of Patient Resource will be referenced using logical ids but are not resolvable, implementation of Patient is left for other guidance and is not the subject of this guide). Connectathons are ideal opportunities to create, compare and consider the holistic implementation of all FHIR Resources. (built Tue, Apr 30, 2024 20:20+0000+00:00)

</details>

**Versions**

* `hl7.fhir.uv.ae-research-ig#1.0.1`
* `hl7.fhir.uv.ae-research-ig#1.0.0`
* `hl7.fhir.uv.ae-research-ig#1.0.0-ballot`


---

### Modelisationdesstructuresetdesprofessionnels.sept2021

<details>
<summary>Description</summary>

Description de la structure des données d'identification des acteurs de santé exposées par les nouveaux services Annuaire Santé d'interrogation FHIR de l'Agence du Numérique en Santé (ANS). 

</details>

**Versions**

* `Modelisationdesstructuresetdesprofessionnels.sept2021#0.1.0`


---

### CanonicalVersioningTest-0.01.01

<details>
<summary>Description</summary>

Test

</details>

**Versions**

* `CanonicalVersioningTest-0.01.01#0.01.01-alpha`


---

### test.no.basis

<details>
<summary>Description</summary>

Testprofiler for basisprofiler

</details>

**Versions**

* `test.no.basis#2.2.0-beta3`
* `test.no.basis#2.2.0-beta2`
* `test.no.basis#2.2.0-beta`
* `test.no.basis#2.2.0-alpha`


---

### Vital Records Death Reporting (VRDR) FHIR Implementation Guide

<details>
<summary>Description</summary>

The VRDR FHIR IG provides guidance regarding the use of FHIR resources as a conduit for data required in the bidirectional exchange of mortality data between State-run Public Health Agencies (PHA) Vital Records offices and U.S. Centers for Disease Control and Prevention (CDC)/National Center for Health Statistics (NCHS).

Bidirectional exchange of mortality data between PHA Vital Records offices and NCHS is essential to effective public health surveillance and emergency response efforts. Automation of the reporting process adds efficiencies that dramatically improves the efficacy of event response, data analysis, and evidence-based measurable prevention of the causes of death.

The VRDR FHIR IG will provide guidance for the use of standard FHIR resources as a conduit for data required by vital records death reporting. The use of FHIR as a platform for automation of vital records death reporting is expected to improve existing automation by enabling wide-scale adoption and leveraging the potential of electronic health records and clinical decision support systems.

The VRDR FHIR IG will lay a foundation for expansion of automated standards-driven information exchange to include tributary flows of information from entities such as physicians, medical examiners, coroners, funeral directors, and family members to public health agencies and between public health agencies and secondary users of detailed mortality data and aggregate statistics.

This FHIR implementation guide is the primary work product of [project #1475](https://bit.ly/34DRIoA) "Vital Records Mortality and Morbidity Reporting FHIR IG" sponsored by the Health Level Seven (HL7) Public Health Work Group (PHWG). 

The scope of the project is to produce and ballot a Standard for Trail Use (STU) Fast Healthcare Interoperability Resources (FHIR) implementation guide (IG) for use in reporting of death events to the U.S. National Center for Health Statistics (NCHS) by State and Local Public Health Agencies (PHA). The VRDR FHIR IG is based upon FHIR R4. The VRDR FHIR IG was successfully balloted as a standard for trial use (STU) in May 2019.

This is a U.S. Realm Specification. This guide and related materials are based on reporting specifications in U.S. jurisdictions. The data content of this IG are based upon the [U.S. Standard Certificate of Death](https://www.cdc.gov/nchs/data/dvs/DEATH11-03final-ACC.pdf). (built Fri, Aug 13, 2021 12:58+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.vrdr#3.0.0-ballot`
* `hl7.fhir.us.vrdr#2.2.0`
* `hl7.fhir.us.vrdr#2.1.0`
* `hl7.fhir.us.vrdr#2.0.0`
* `hl7.fhir.us.vrdr#1.2.0`
* `hl7.fhir.us.vrdr#1.0.0`
* `hl7.fhir.us.vrdr#0.1.0`


---

### cens.fhir.poclis

<details>
<summary>Description</summary>

Prueba de Concepto LIS

</details>

**Versions**

* `cens.fhir.poclis#1.0.0`


---

### de.gematik.isik-medikation

<details>
<summary>Description</summary>

Medikations-Modul der Informationstechnischen Systeme im Krankenhaus Ausbaustufe 4

</details>

**Versions**

* `de.gematik.isik-medikation#4.0.0-rc`
* `de.gematik.isik-medikation#3.0.2`
* `de.gematik.isik-medikation#3.0.1`
* `de.gematik.isik-medikation#3.0.0`
* `de.gematik.isik-medikation#2.0.2`


---

### de.bbmri.fhir

<details>
<summary>Description</summary>

Profiles for the BBMRI.de / GBA biobanking project.

</details>

**Versions**

* `de.bbmri.fhir#1.2.0`
* `de.bbmri.fhir#1.1.0`


---

### qualitype.fhir.samples

<details>
<summary>Description</summary>

The official documentation of the HL7 FHIR interface of SAMPLES by qualitype GmbH.

</details>

**Versions**

* `qualitype.fhir.samples#1.0.0`


---

### uk.nhsdigital.medicines.r4

<details>
<summary>Description</summary>

Electronic Prescription Service

</details>

**Versions**

* `uk.nhsdigital.medicines.r4#2.7.9`
* `uk.nhsdigital.medicines.r4#2.7.1`
* `uk.nhsdigital.medicines.r4#2.6.0`
* `uk.nhsdigital.medicines.r4#2.5.0`
* `uk.nhsdigital.medicines.r4#2.3.0`
* `uk.nhsdigital.medicines.r4#2.2.1`
* `uk.nhsdigital.medicines.r4#2.2.0`
* `uk.nhsdigital.medicines.r4#2.1.14-alpha`
* `uk.nhsdigital.medicines.r4#2.1.13-alpha`
* `uk.nhsdigital.medicines.r4#2.1.12-alpha`
* `uk.nhsdigital.medicines.r4#2.1.11-alpha`


---

### FHIR Core package

<details>
<summary>Description</summary>

FHIR Core package - the NPM package that contains all the definitions for the base FHIR specification

</details>

**Versions**

* `hl7.fhir.core#3.5.0`
* `hl7.fhir.core#1.8.0`
* `hl7.fhir.core#1.4.0`


---

### CygnetHealth.00.00.01

<details>
<summary>Description</summary>

Cygnet Health FHIR System

</details>

**Versions**

* `CygnetHealth.00.00.01#0.0.1`


---

### CH AllergyIntolerance (R4)

<details>
<summary>Description</summary>

Implementation guide CH AllergyIntolerance (R4) (built Fri, May 17, 2024 08:01+0000+00:00)

</details>

**Versions**

* `ch.fhir.ig.ch-allergyintolerance#3.0.0-ballot`
* `ch.fhir.ig.ch-allergyintolerance#2.0.1`
* `ch.fhir.ig.ch-allergyintolerance#2.0.0`
* `ch.fhir.ig.ch-allergyintolerance#2.0.0-ballot`
* `ch.fhir.ig.ch-allergyintolerance#1.0.0`
* `ch.fhir.ig.ch-allergyintolerance#0.2.0`


---

### DGUV.Basis

<details>
<summary>Description</summary>

Initiale Veröffentlichung der DGUV Basis Profile. Diese bilden die Grundlage zur Umsetzung der zukünftigen digitalen Formtexte (Formulare).

</details>

**Versions**

* `DGUV.Basis#1.0.0`


---

### de.medizininformatikinitiative.kerndatensatz.icu

<details>
<summary>Description</summary>

Medizininformatik Initiative - Modul Intensivmedizin PDMS-Daten und hochauflösende Biosignaldaten

</details>

**Versions**

* `de.medizininformatikinitiative.kerndatensatz.icu#2024.0.0-alpha2`
* `de.medizininformatikinitiative.kerndatensatz.icu#2024.0.0-alpha1`
* `de.medizininformatikinitiative.kerndatensatz.icu#1.0.0`


---

### CDC MME CQL Calculator

<details>
<summary>Description</summary>

Opioid Morphine Milligram Equivalent (MME) calculation logic in FHIR and Clinical Quality Language (CQL) (built Thu, Nov 25, 2021 15:13+1100+11:00)

</details>

**Versions**

* `fhir.cdc.opioid-mme-r4#3.0.0`


---

### progetto.eng

<details>
<summary>Description</summary>

prova_silvia

</details>

**Versions**

* `progetto.eng#0.0.1`


---

### dvmd.kdl.r4.2022

<details>
<summary>Description</summary>

Publikation der Terminologie-Ressourcen für die Nutzung der Klinische Dokumentenklassen-Liste (KDL) des DVMD
Version 2022

</details>

**Versions**

* `dvmd.kdl.r4.2022#2022.1.2`
* `dvmd.kdl.r4.2022#2022.1.1`


---

### Situational Awareness for Novel Epidemic Response

<details>
<summary>Description</summary>

The Situational Awareness for Novel Epidemic Response Implementation Guide enables transmission of high level situational awareness information from healthcare facilities to centralized data repositories to support the treatment of the novel coronavirus illness. (built Tue, Sep 7, 2021 19:01+0000+00:00)

</details>

**Versions**

* `hl7.fhir.uv.saner#1.0.0`
* `hl7.fhir.uv.saner#0.1.0`


---

### acme.base

<details>
<summary>Description</summary>

ACME Base Profiles that ACME projects and products can build upon.

</details>

**Versions**

* `acme.base#2.0.0`
* `acme.base#2.0.0-rc1`
* `acme.base#1.0.0`
* `acme.base#0.0.11`
* `acme.base#0.0.9`
* `acme.base#0.0.8`
* `acme.base#0.0.7`
* `acme.base#0.0.6`
* `acme.base#0.0.5`
* `acme.base#0.0.4`
* `acme.base#0.0.2`
* `acme.base#0.0.1`
* `acme.base#0.0.10-no-move-examples`


---

### ch.cel.thetest2-core

<details>
<summary>Description</summary>

Project for testing packaging

</details>

**Versions**

* `ch.cel.thetest2-core#0.8.1`


---

### Implementierungsleitfaden DEMIS - Erregernachweismeldung

<details>
<summary>Description</summary>

Deutsches Elektronisches Melde- und Informationssystem für den Infektionsschutz

</details>

**Versions**

* `rki.demis.r4.core#1.23.2`
* `rki.demis.r4.core#1.23.1`
* `rki.demis.r4.core#1.22.2`


---

### de.diga.abatonra

<details>
<summary>Description</summary>

Spezifikationen für DiGas zur digitalen Behandlung von rheumatoider Arthritis

</details>

**Versions**

* `de.diga.abatonra#2.0.0`


---

### HL7 FHIR Implementation Guide: Profiles for Transfusion and Vaccination Adverse Event Detection and Reporting

<details>
<summary>Description</summary>

A set of profiles that define the data elements needed to detect conditions and observations arising from transfusions or vaccinations that are candidates for Adverse Events as well as profiles that allow the reporting of Adverse Events (built Tue, Aug 29, 2023 21:13+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.icsr-ae-reporting#1.0.1`
* `hl7.fhir.us.icsr-ae-reporting#1.0.0`
* `hl7.fhir.us.icsr-ae-reporting#0.1.0`


---

### CARIN Digital Insurance Card

<details>
<summary>Description</summary>

CARIN Digital Insurance Card (built Tue, Apr 16, 2024 17:25+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.insurance-card#1.1.0`
* `hl7.fhir.us.insurance-card#1.0.0`
* `hl7.fhir.us.insurance-card#0.1.0`


---

### MedMorph Research Data Exchange Content IG

<details>
<summary>Description</summary>

MedMorph Research Content IG enables researchers to access data from EHRs leveraging the MedMorph Reference Architecture. (built Thu, Dec 9, 2021 14:36+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.medmorph-research-dex#0.1.0`


---

### Partage de Documents de Santé en mobilité (PDSm)



**Versions**

* `ans.fhir.fr.pdsm#3.1.0`
* `ans.fhir.fr.pdsm#3.0.1`
* `ans.fhir.fr.pdsm#3.0.0`


---

### RapportEndoscopieQuebec.test

<details>
<summary>Description</summary>

Envoi du rapport d’endoscopie au dépôt provincial

</details>

**Versions**

* `RapportEndoscopieQuebec.test#0.0.1-test`


---

### Implementation Guide for FFB reporting (FFB uddatasæt)

<details>
<summary>Description</summary>

Danish municipalities implementation guide for FFB reporting. A standard for reporting social information from citizen journals to a gateway. The aim is to use the data in population based studies and management information systems. (built Sun, Aug 27, 2023 00:02+0200+02:00)

</details>

**Versions**

* `kl.dk.fhir.ffbreporting#1.0.0`


---

### de.transfer-abrechnungsdaten.r4

<details>
<summary>Description</summary>

Transfer von Abrechnungsdaten - Generisches Format für Abrechnungsdaten im deutschen Gesundheitswesen

</details>

**Versions**

* `de.transfer-abrechnungsdaten.r4#1.1.2`
* `de.transfer-abrechnungsdaten.r4#1.1.1`
* `de.transfer-abrechnungsdaten.r4#1.1.0-beta`
* `de.transfer-abrechnungsdaten.r4#1.0.0`


---

### kbv.mio.patientenkurzakte

<details>
<summary>Description</summary>

Medizinische Informationsobjekte (MIO) Patientenkurzakte V1.0.0

</details>

**Versions**

* `kbv.mio.patientenkurzakte#1.0.0`
* `kbv.mio.patientenkurzakte#1.0.0-kommentierung`
* `kbv.mio.patientenkurzakte#1.0.0-benehmensherstellung`


---

### ans.cnsa.fhir

<details>
<summary>Description</summary>

Ce projet contient l'ensemble des ressources de conformité FHIR R4 produite par l'Agence du Numérique en Santé (ANS) dans le cadre des Grands Chantiers du médico-social pilotés par la CNSA.

</details>

**Versions**

* `ans.cnsa.fhir#0.1.2`
* `ans.cnsa.fhir#0.1.1`
* `ans.cnsa.fhir#0.1.0`
* `ans.cnsa.fhir#0.0.1`


---

### kbv.ita.for

<details>
<summary>Description</summary>

Umsetzung der formularübergreifende Vorgaben für Digitale Muster der KBV

</details>

**Versions**

* `kbv.ita.for#1.1.0`
* `kbv.ita.for#1.1.0-PreRelease`


---

### FHIR Extensions Pack

<details>
<summary>Description</summary>

This IG defines the global extensions - the ones defined for everyone. These extensions are always in scope wherever FHIR is being used (built Sat, Apr 27, 2024 18:39+1000+10:00)

</details>

**Versions**

* `hl7.fhir.uv.extensions.r4#5.1.0`
* `hl7.fhir.uv.extensions.r4#1.0.0`


---

### Vital.MedikationsplanPlus

<details>
<summary>Description</summary>

Der MedikationsplanPlus als Implementierung in R4

</details>

**Versions**

* `Vital.MedikationsplanPlus#1.1.0`
* `Vital.MedikationsplanPlus#1.0.0`
* `Vital.MedikationsplanPlus#0.3.0`
* `Vital.MedikationsplanPlus#0.2.0`
* `Vital.MedikationsplanPlus#0.1.0`


---

### 醫療保險理賠實作指引

<details>
<summary>Description</summary>

醫療保險理賠實作指引 (built Mon, Dec 25, 2023 23:25+0800+08:00)

</details>

**Versions**

* `tw.cathay.fhir.iclaim#0.1.3`
* `tw.cathay.fhir.iclaim#0.1.2`
* `tw.cathay.fhir.iclaim#0.1.1`
* `tw.cathay.fhir.iclaim#0.1.0`


---

### DK MedCom acknowledgement

<details>
<summary>Description</summary>

The DK MedCom acknowledgement IG (built Mon, Feb 5, 2024 12:24+0100+01:00)

</details>

**Versions**

* `medcom.fhir.dk.acknowledgement#2.0.2`
* `medcom.fhir.dk.acknowledgement#2.0.1`


---

### ICHOM Patient Centered Outcomes Measure Set for Breast Cancer



**Versions**

* `hl7.fhir.uv.ichom-breast-cancer#1.0.0`
* `hl7.fhir.uv.ichom-breast-cancer#1.0.0-ballot`


---

### Personal Health Device Implementation Guide

<details>
<summary>Description</summary>

ImplementationGuide for Personal Health Devices (PHD), used in remote patient monitoring (e.g., weight scales, blood pressure cuffs, glucose monitors, pulse-oximeters, etc.). (built Thu, May 12, 2022 20:20+0000+00:00)

</details>

**Versions**

* `hl7.fhir.uv.phd#1.0.0`
* `hl7.fhir.uv.phd#0.3.0`
* `hl7.fhir.uv.phd#0.2.0`


---

### FHIR implementation of zibs 2020

<details>
<summary>Description</summary>

NL package of FHIR R4 conformance resources for zib (Zorginformatiebouwstenen, Clinical Information Models) release 2020.

</details>

**Versions**

* `nictiz.fhir.nl.r4.nl-core#0.10.0-beta.1`
* `nictiz.fhir.nl.r4.nl-core#0.9.0-beta.1`
* `nictiz.fhir.nl.r4.nl-core#0.8.0-beta.1`
* `nictiz.fhir.nl.r4.nl-core#0.7.0-beta.1`
* `nictiz.fhir.nl.r4.nl-core#0.6.0-beta.2`
* `nictiz.fhir.nl.r4.nl-core#0.5.0-beta1`


---

### KLChildren implementation guide, an implementation of FBU

<details>
<summary>Description</summary>

KLChildren contains the specification for reporting data from the Danish health promotion and disease prevention program for children in Denmark (Sundhedsplejen). Data is reported to FK Gateway (built Wed, Jun 5, 2024 10:40+0200+02:00)

</details>

**Versions**

* `kl.dk.fhir.children#2.0.0`
* `kl.dk.fhir.children#1.0.0`


---

### Breast Radiology Reporting - 1st STU ballot

<details>
<summary>Description</summary>

Breast Radiology Reporting Implementation Guide (built Tue, Apr 7, 2020 16:39+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.breast-radiology#0.2.0`


---

### FHIR Data Segmentation for Privacy

<details>
<summary>Description</summary>

FHIR data segmentation for privacy security label implementation guide (built Mon, Apr 17, 2023 19:19+0000+00:00)

</details>

**Versions**

* `hl7.fhir.uv.security-label-ds4p#1.0.0`
* `hl7.fhir.uv.security-label-ds4p#0.3.0`
* `hl7.fhir.uv.security-label-ds4p#0.2.0`
* `hl7.fhir.uv.security-label-ds4p#0.1.0`


---

### ch.fhir.BNW.ch-core

<details>
<summary>Description</summary>

lets start to get swiss pathology structured

</details>

**Versions**

* `ch.fhir.BNW.ch-core#1.0.0`


---

### Implementierungsleitfaden DEMIS Common (Basismeldeinhalte)

<details>
<summary>Description</summary>

Das DEMIS Common Package definiert das Datenmodell der Basismeldeinhalte.

</details>

**Versions**

* `rki.demis.common#1.0.1`
* `rki.demis.common#1.0.0`


---

### Pharmaceutical Quality - Chemistry, Manufacturing and Controls (PQ-CMC) Submissions to FDA

<details>
<summary>Description</summary>

The FDA PQ-CMC FHIR IG  is for submission of structured and standardized information regarding drug product quality, chemistry, manufacturing and processes controls.  This data is intended for submission to the US FDA by biopharmaceutical companies for the purpose of drug application review. (built Mon, Apr 8, 2024 17:45+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.pq-cmc-fda#1.0.0-ballot`


---

### agha.fhir.genclipr

<details>
<summary>Description</summary>

FHIR implementation guide for the Genomics Clinical Picture Repository standard developed as part of

</details>

**Versions**

* `agha.fhir.genclipr#0.1.0`


---

### HL7 FHIR Implementation Guide: DK Core

<details>
<summary>Description</summary>

A FHIR Implementation Guide for the Danish common needs across healthcare sectors (built Mon, May 6, 2024 15:20+0200+02:00)

</details>

**Versions**

* `hl7.fhir.dk.core#3.2.0`
* `hl7.fhir.dk.core#3.1.0`
* `hl7.fhir.dk.core#3.0.0`
* `hl7.fhir.dk.core#2.2.0`
* `hl7.fhir.dk.core#2.1.0`
* `hl7.fhir.dk.core#2.0.0`
* `hl7.fhir.dk.core#1.1.0`


---

### acme.base.r4

<details>
<summary>Description</summary>

ACME Base Profiles R4

</details>

**Versions**

* `acme.base.r4#4.1.6`
* `acme.base.r4#4.1.5`
* `acme.base.r4#4.1.4`
* `acme.base.r4#4.1.3`
* `acme.base.r4#4.1.2`
* `acme.base.r4#4.1.1`
* `acme.base.r4#4.1.0`
* `acme.base.r4#4.0.18`
* `acme.base.r4#4.0.17`
* `acme.base.r4#4.0.16`
* `acme.base.r4#4.0.15`
* `acme.base.r4#4.0.14`
* `acme.base.r4#4.0.13`
* `acme.base.r4#4.0.12`
* `acme.base.r4#4.0.11`
* `acme.base.r4#4.0.10`
* `acme.base.r4#4.0.9`
* `acme.base.r4#4.0.7`
* `acme.base.r4#4.0.6`
* `acme.base.r4#4.0.5`
* `acme.base.r4#4.0.4`
* `acme.base.r4#4.0.3`
* `acme.base.r4#4.0.2`
* `acme.base.r4#4.0.1`
* `acme.base.r4#4.0.0`
* `acme.base.r4#3.0.0`
* `acme.base.r4#2.0.0`
* `acme.base.r4#1.0.0`
* `acme.base.r4#1.0.0-beta2`
* `acme.base.r4#0.0.1-alpha2`


---

### Taiwan Digital COVID-19 Certificate

<details>
<summary>Description</summary>

Taiwan Digital COVID-19 Certificate FHIR Implementation Guide (built Tue, Aug 17, 2021 11:13+0800+08:00)

</details>

**Versions**

* `hitstdio.tw.fhir.dcc#0.0.1`


---

### egde.health.gateway

<details>
<summary>Description</summary>

Egde Health Gateway FHIR facade

</details>

**Versions**

* `egde.health.gateway#0.1.7`
* `egde.health.gateway#0.1.6`
* `egde.health.gateway#0.1.5`
* `egde.health.gateway#0.1.4`
* `egde.health.gateway#0.1.3`
* `egde.health.gateway#0.1.2`
* `egde.health.gateway#0.1.1`
* `egde.health.gateway#0.1.3-alpha`
* `egde.health.gateway#0.1.0-alpha`


---

### 電子病歷交換單張實作指引(EMR-IG)

<details>
<summary>Description</summary>

電子病歷交換單張實作指引 EMR IG (built Mon, May 6, 2024 17:37+0800+08:00)

</details>

**Versions**

* `tw.gov.mohw.emr#0.1.0`


---

### CH ORF (R4)

<details>
<summary>Description</summary>

Order & Referral by Form - Implementation Guide (CH ORF) (built Fri, May 17, 2024 06:30+0000+00:00)

</details>

**Versions**

* `ch.fhir.ig.ch-orf#3.0.0-ballot`
* `ch.fhir.ig.ch-orf#2.0.1`
* `ch.fhir.ig.ch-orf#2.0.0`
* `ch.fhir.ig.ch-orf#2.0.0-ballot`
* `ch.fhir.ig.ch-orf#1.0.0`
* `ch.fhir.ig.ch-orf#0.10.0`
* `ch.fhir.ig.ch-orf#0.9.1`


---

### Post-Acute Orders (PAO) (DME-Orders)

<details>
<summary>Description</summary>

# General
This is a FHIR R4 Implementation Guide (IG) to support the electronic exchange of post-acute orders and referrals, along with the exchange of supporting documentation between the ordering provider and the specific rendering provider.
* The initial version of the implementation guide (IG) will focus on orders and documentation for Durable Medical Equipment (DME) and Home Health Services. It is important to note that supporting DME also requires the support for medications associated with specific devices such as nebulizers and infusion pumps. It is the goal of future versions of this implementation guide to support orders for all post-acute services. 
* This specification is currently undergoing connectathon testing. It is expected to evolve, possibly significantly, as part of that process.

This implementation guide is focused on enabling ordering providers to create, communicate and track orders and referrals in the post-acute setting. By enabling ordering providers to communicate supporting documentation in real-time to rendering providers, patients can receive appropriate treatment more rapidly and reduce the burden on rendering providers to comply with payer documentation requirements.

# Change log
* **0.2.* :**QA versions for the September 2020 ballot cycle (minor version 0.2.x) include QA feedback
* **0.3.0 :** Final version for the September 2020 ballot cycle
 
  
# Known issues and to-dos
* Example of message bundle for message based ordering
 (built Mon, Aug 10, 2020 18:26+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.dme-orders#0.2.0`
* `hl7.fhir.us.dme-orders#0.1.0`


---

### Médicosocial - Transfert de données DUI



**Versions**

* `ans.fhir.fr.tddui#1.0.1`
* `ans.fhir.fr.tddui#1.0.1-ballot`


---

### apo.cdl.test

<details>
<summary>Description</summary>

Test IG Kereval

</details>

**Versions**

* `apo.cdl.test#0.0.1-alpha`


---

### hl7.at.fhir.gkl.ig-tooling

<details>
<summary>Description</summary>

This IG is built in order to describe the IG tooling in Austria. (built Fri, Apr 5, 2024 08:43+0000+00:00)

</details>

**Versions**

* `hl7.at.fhir.gkl.ig-tooling#0.2.0`
* `hl7.at.fhir.gkl.ig-tooling#0.1.0`


---

### CH CRL (R4)

<details>
<summary>Description</summary>

Implementation Guide CH CRL (R4) (built Fri, Feb 4, 2022 14:13+0100+01:00)

</details>

**Versions**

* `ch.fhir.ig.ch-crl#0.9.0`
* `ch.fhir.ig.ch-crl#0.2.1`
* `ch.fhir.ig.ch-crl#0.2.0`
* `ch.fhir.ig.ch-crl#0.1.1`
* `ch.fhir.ig.ch-crl#0.1.0`


---

### nrlf.poc

<details>
<summary>Description</summary>

NRL Futures API POC

</details>

**Versions**

* `nrlf.poc#1.0.2`


---

### cisis.cds

<details>
<summary>Description</summary>

test

</details>

**Versions**

* `cisis.cds#4.0.0`
* `cisis.cds#3.0.0`
* `cisis.cds#2.0.0`
* `cisis.cds#1.0.0`


---

### careplanrt.eng

<details>
<summary>Description</summary>

CarePlanEng

</details>

**Versions**

* `careplanrt.eng#1.0.1`


---

### Estonian Base Implementation Guide

<details>
<summary>Description</summary>

Estonian HL7 FHIR base Implementation Guide and profiles.

</details>

**Versions**

* `ee.hl7.fhir.base.r5#1.0.2`
* `ee.hl7.fhir.base.r5#1.0.1`


---

### iteyes.myhw.core

<details>
<summary>Description</summary>

마이헬스웨이 FHIR Resource Profiles

</details>

**Versions**

* `iteyes.myhw.core#1.1.9`
* `iteyes.myhw.core#1.1.8`
* `iteyes.myhw.core#1.1.7`
* `iteyes.myhw.core#1.1.6`
* `iteyes.myhw.core#1.1.5`
* `iteyes.myhw.core#1.1.4`
* `iteyes.myhw.core#1.1.3`
* `iteyes.myhw.core#1.1.2`
* `iteyes.myhw.core#1.1.1`
* `iteyes.myhw.core#1.1.9-beta`
* `iteyes.myhw.core#1.1.8-beta`
* `iteyes.myhw.core#1.1.6-beta`
* `iteyes.myhw.core#1.1.5-beta`
* `iteyes.myhw.core#1.1.4-beta`
* `iteyes.myhw.core#1.1.3-beta`
* `iteyes.myhw.core#1.1.2-beta`
* `iteyes.myhw.core#1.0.9`
* `iteyes.myhw.core#1.0.7`
* `iteyes.myhw.core#1.0.6`
* `iteyes.myhw.core#1.0.5`
* `iteyes.myhw.core#1.0.4`
* `iteyes.myhw.core#1.0.3`
* `iteyes.myhw.core#1.0.2`
* `iteyes.myhw.core#1.0.1`
* `iteyes.myhw.core#1.0.9-beta`
* `iteyes.myhw.core#1.0.8-beta`
* `iteyes.myhw.core#1.0.7-beta`
* `iteyes.myhw.core#1.0.6-beta`
* `iteyes.myhw.core#1.0.5-beta`
* `iteyes.myhw.core#1.0.4-beta`
* `iteyes.myhw.core#1.0.3-beta`


---

### Subscriptions R5 Backport

<details>
<summary>Description</summary>

The Subscription R5 Backport Implementation Guide enables servers running versions of FHIR earlier than R5 to implement a subset of R5 Subscriptions in a standardized way. (built Mon, Dec 18, 2023 22:14+0000+00:00)

</details>

**Versions**

* `hl7.fhir.uv.subscriptions-backport#1.2.0-ballot`
* `hl7.fhir.uv.subscriptions-backport#1.1.0`
* `hl7.fhir.uv.subscriptions-backport#1.0.0`
* `hl7.fhir.uv.subscriptions-backport#0.1.0`


---

### National Healthcare Directory Attestation and Verification

<details>
<summary>Description</summary>

National Directory (built Tue, Aug 9, 2022 12:51+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.directory-attestation#1.0.0-ballot`


---

### de.abda.eRezeptAbgabedaten

<details>
<summary>Description</summary>

eRezeptAbgabedatensatz 1.0.0 vom 05.03.2021

</details>

**Versions**

* `de.abda.eRezeptAbgabedaten#1.0.0`


---

### de.acticore.export

<details>
<summary>Description</summary>

Spezifikation der Exportschnittstelle

</details>

**Versions**

* `de.acticore.export#0.9.0`


---

### Medication

<details>
<summary>Description</summary>

Medication (built Tue, May 31, 2022 09:42+0200+02:00)

</details>

**Versions**

* `hl7.fhir.be.medication#1.0.0`


---

### CH EPR FHIR (R4)

<details>
<summary>Description</summary>

Implementation Guide for the Mobile access to Health Documents (EPR FHIR) Profile for the Swiss EPR (built Thu, May 16, 2024 21:43+0000+00:00)

</details>

**Versions**

* `ch.fhir.ig.ch-epr-fhir#4.0.1-ballot`
* `ch.fhir.ig.ch-epr-fhir#4.0.0-ballot`


---

### Immunization Decision Support Forecast (ImmDS) Implementation Guide

<details>
<summary>Description</summary>

An IG for querying a decision support engine for a personalized immunization forecast. (built Fri, Jun 25, 2021 15:56+1000+10:00)

</details>

**Versions**

* `hl7.fhir.us.immds#1.0.0`


---

### Subscriptions R5 Backport

<details>
<summary>Description</summary>

The Subscription R5 Backport Implementation Guide enables servers running versions of FHIR earlier than R5 to implement a subset of R5 Subscriptions in a standardized way. (built Wed, Jan 11, 2023 15:34+1100+11:00)

</details>

**Versions**

* `hl7.fhir.uv.subscriptions-backport.r4#1.1.0`


---

### HL7 Terminology (THO)

<details>
<summary>Description</summary>

Defines and exposes all HL7 terminologies managed through the unified terminology governance process (built Sat, Mar 9, 2024 10:53-0700-07:00)

</details>

**Versions**

* `hl7.terminology.r5#5.5.0`
* `hl7.terminology.r5#5.4.0`
* `hl7.terminology.r5#5.3.0`
* `hl7.terminology.r5#5.2.0`
* `hl7.terminology.r5#5.1.0`
* `hl7.terminology.r5#5.0.0`
* `hl7.terminology.r5#4.0.0`
* `hl7.terminology.r5#3.1.0`
* `hl7.terminology.r5#3.0.0`


---

### Patient Demographics Query for Mobile (PDQm)

<details>
<summary>Description</summary>

The Patient Demographics Query for Mobile (PDQm) Profile defines a lightweight RESTful interface to a patient demographics supplier leveraging technologies readily available to mobile applications and lightweight browser based applications. (built Fri, Feb 23, 2024 14:36-0600-06:00)

</details>

**Versions**

* `ihe.iti.pdqm#3.0.0`
* `ihe.iti.pdqm#3.0.0-comment`
* `ihe.iti.pdqm#2.4.0`


---

### HL7France-Clinicalprofils.072021

<details>
<summary>Description</summary>

Profiles, extensions, value sets, code systems and implementation guides standardizing the exchange or sharing of clinical content across applications in France

</details>

**Versions**

* `HL7France-Clinicalprofils.072021#0.1.0`


---

### healthdata.be.r4.dcd

<details>
<summary>Description</summary>

Data Collection Definitions (DCD) and their related HL7 FHIR R4 compliant profiles and related conformance resources. Based on Clinical Building Blocks.

</details>

**Versions**

* `healthdata.be.r4.dcd#0.2.0-beta`
* `healthdata.be.r4.dcd#0.1.0-beta`


---

### Structured Data Capture

<details>
<summary>Description</summary>

The SDC specification provides an infrastructure to standardize the capture and expanded use of patient-level data collected within an EHR.<br/>This includes two components:<br/>* Support more sophisticated questionnaire/form use-cases such as those needed for research, oncology, pathology and other clinical domains.<br/>*Support pre-population and auto-population of EHR data into forms/questionnaires for uses outside direct clinical care (patient safety, adverse event reporting, public health reporting, etc.). (built Tue, Mar 8, 2022 18:36+0000+00:00)

</details>

**Versions**

* `hl7.fhir.uv.sdc.r4b#3.0.0`


---

### cens.fhir.poclis-ssmso

<details>
<summary>Description</summary>

Proyecto POC LIS para el Servicio de Salud Metropolitano Sur Oriente

</details>

**Versions**

* `cens.fhir.poclis-ssmso#1.0.0`


---

### de.medizininformatikinitiative.kerndatensatz.biobank

<details>
<summary>Description</summary>

Medizininformatik Initiative - Kerndatensatz Erweiterungsmodul Biobank / Bioprobendaten

</details>

**Versions**

* `de.medizininformatikinitiative.kerndatensatz.biobank#1.0.8`
* `de.medizininformatikinitiative.kerndatensatz.biobank#1.0.7`
* `de.medizininformatikinitiative.kerndatensatz.biobank#1.0.6`
* `de.medizininformatikinitiative.kerndatensatz.biobank#1.0.5`
* `de.medizininformatikinitiative.kerndatensatz.biobank#1.0.4`
* `de.medizininformatikinitiative.kerndatensatz.biobank#1.0.3`
* `de.medizininformatikinitiative.kerndatensatz.biobank#1.0.2`
* `de.medizininformatikinitiative.kerndatensatz.biobank#1.0.1`
* `de.medizininformatikinitiative.kerndatensatz.biobank#1.0.0`
* `de.medizininformatikinitiative.kerndatensatz.biobank#0.9.0`


---

### de.gevko.dev.te-tvz

<details>
<summary>Description</summary>

Put a description here

</details>

**Versions**

* `de.gevko.dev.te-tvz#1.0.0`


---

### MyHealthWay

<details>
<summary>Description</summary>

KR Core V1 기반의 건강정보 고속도로 상세규격 - 1.0.2-beta.4

</details>

**Versions**

* `kr.mohw.myhealthway#1.0.2-beta.4`
* `kr.mohw.myhealthway#1.0.1`
* `kr.mohw.myhealthway#1.0.0`
* `kr.mohw.myhealthway#1.0.2-beta`
* `kr.mohw.myhealthway#0.1.1`


---

### DK MedCom Messaging

<details>
<summary>Description</summary>

The DK MedCom Messaging IG (built Tue, Oct 31, 2023 14:54+0100+01:00)

</details>

**Versions**

* `medcom.fhir.dk.messaging#2.1.0`
* `medcom.fhir.dk.messaging#2.0.0`


---

### HL7 Terminology (THO)

<details>
<summary>Description</summary>

Defines and exposes all HL7 terminologies managed through the unified terminology governance process (built Sat, Mar 9, 2024 10:53-0700-07:00)

</details>

**Versions**

* `hl7.terminology.r4#5.5.0`
* `hl7.terminology.r4#5.4.0`
* `hl7.terminology.r4#5.3.0`
* `hl7.terminology.r4#5.2.0`
* `hl7.terminology.r4#5.1.0`
* `hl7.terminology.r4#5.0.0`
* `hl7.terminology.r4#4.0.0`
* `hl7.terminology.r4#3.1.0`
* `hl7.terminology.r4#3.0.0`


---

### TH Claim Consolidation (ClaimCon) - FHIR Implementation Guide (STU1)

<details>
<summary>Description</summary>

A representation of Thailand's public health claim dataset in FHIR format (built Sat, Sep 9, 2023 15:29+0700+07:00)

</details>

**Versions**

* `silth.fhir.th.claimcon#0.1.2`


---

### ans.cisis.fhir.r4

<details>
<summary>Description</summary>

Ressources de conformité (profils, extensions, capability statements...) des volets FHIR R4 du cadre d'interopérabilité des systèmes d'information de santé, CI-SIS, de l'ANS.

</details>

**Versions**

* `ans.cisis.fhir.r4#3.0.0`
* `ans.cisis.fhir.r4#3.0.0-pat23v2`
* `ans.cisis.fhir.r4#3.0.0-pat23`
* `ans.cisis.fhir.r4#2.1.0`
* `ans.cisis.fhir.r4#2.0.0`
* `ans.cisis.fhir.r4#1.0.0`


---

### PACIO Advance Directive Interoperability Implementation Guide

<details>
<summary>Description</summary>

PACIO Advance Directive Interoperability Implementation Guide (built Thu, Jan 11, 2024 17:40+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.pacio-adi#1.0.0`
* `hl7.fhir.us.pacio-adi#0.1.0`


---

### Da Vinci - Member Attribution (ATR) List

<details>
<summary>Description</summary>

Exchange of member attribution list between payers and providers  (built Tue, Jan 9, 2024 20:14+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.davinci-atr#2.0.0`
* `hl7.fhir.us.davinci-atr#2.0.0-ballot`
* `hl7.fhir.us.davinci-atr#1.0.0`
* `hl7.fhir.us.davinci-atr#0.1.0`


---

### Da Vinci - Payer Coverage Decision Exchange (PCDE)

<details>
<summary>Description</summary>

Guidelines for conveying coverage information from one payer to another when a patient changes insurance (built Wed, Dec 23, 2020 03:18+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.davinci-pcde#1.0.0`
* `hl7.fhir.us.davinci-pcde#0.1.0`


---

### surescripts.specialty

<details>
<summary>Description</summary>

This project holds the profiles, message examples, and simplifier implementation guide for the Surescripts Specialty Patient Enrollment products.

</details>

**Versions**

* `surescripts.specialty#1.2.0-beta`
* `surescripts.specialty#1.1.0-beta`
* `surescripts.specialty#1.0.0-beta`


---

### Swissnoso Implementation Guide (R4)

<details>
<summary>Description</summary>

Implementation guide to specify the exchange format for data transmission to Swissnoso in the context of monitoring and prevention of healthcare-associated infections. (built Tue, Jun 14, 2022 13:09+0200+02:00)

</details>

**Versions**

* `ch.fhir.ig.swissnoso#1.0.0`
* `ch.fhir.ig.swissnoso#0.1.0`


---

### IHE ITI Basic AuditEvent Implementation Guide

<details>
<summary>Description</summary>

The Basic Audit Log Patterns (BasicAudit) **Content Profile** defines some basic and reusable AuditEvent patterns. This includes basic audit log profiles for FHIR RESTful operations, to be used when there is not a more specific audit event defined. Where a more specific audit event can be defined it should be derived off of these basic patterns. (built Wed, Mar 2, 2022 07:55-0600-06:00)

</details>

**Versions**

* `ihe.iti.basicaudit#1.0.1`


---

### ca.on.consent.r4

<details>
<summary>Description</summary>

Ontario Provincial Consent Implementation Guide

</details>

**Versions**

* `ca.on.consent.r4#0.10.11`
* `ca.on.consent.r4#0.10.10`
* `ca.on.consent.r4#0.10.9`
* `ca.on.consent.r4#0.10.8`
* `ca.on.consent.r4#0.10.7`
* `ca.on.consent.r4#0.10.6`
* `ca.on.consent.r4#0.10.5`
* `ca.on.consent.r4#0.10.4`
* `ca.on.consent.r4#0.10.3`
* `ca.on.consent.r4#0.10.2`
* `ca.on.consent.r4#0.10.1`
* `ca.on.consent.r4#0.10.0`
* `ca.on.consent.r4#0.9.0`


---

### Clinical Quality Framework Common FHIR Assets

<details>
<summary>Description</summary>

This implementation guide contains common FHIR assets for use in CQFramework content IGs, including FHIRHelpers and the FHIR-ModelInfo libraries. (built Fri, Nov 12, 2021 16:25+1100+11:00)

</details>

**Versions**

* `fhir.cqf.common#4.0.1`


---

### Australian Provider Directory IG



**Versions**

* `hl7.fhir.au.pd#2.0.1`
* `hl7.fhir.au.pd#0.1.0`


---

### ca.on.oh-cms

<details>
<summary>Description</summary>

Based on FHIR Cast

</details>

**Versions**

* `ca.on.oh-cms#1.0.1-alpha1.0.1`


---

### Patient Master Identity Registry (PMIR)

<details>
<summary>Description</summary>

The Patient Master Identity Registry (PMIR) Profile supports the creating, updating and deprecating of patient master identity information about a subject of care, as well as subscribing to changes to the patient master identity, using the HL7 FHIR standard resources and RESTful transactions. (built Mon, Aug 8, 2022 16:03-0500-05:00)

</details>

**Versions**

* `ihe.iti.pmir#1.5.0`
* `ihe.iti.pmir#1.4.0`


---

### Guide d'implémentation de la télésurveillance



**Versions**

* `ans.fhir.fr.telesurveillance#0.1.0-ballot`


---

### kbv.mio.zaeb

<details>
<summary>Description</summary>

Medizinische Informationsobjekte (MIO) Zahnärztliches Bonusheft V1.1.0

</details>

**Versions**

* `kbv.mio.zaeb#1.1.0`
* `kbv.mio.zaeb#1.1.0-benehmensherstellung`


---

### duwel.nl.r4.sandbox.dev

<details>
<summary>Description</summary>

Jorn Duwel sandbox

</details>

**Versions**

* `duwel.nl.r4.sandbox.dev#0.0.1-dev.4`
* `duwel.nl.r4.sandbox.dev#0.0.1-dev.3`
* `duwel.nl.r4.sandbox.dev#0.0.1-dev.2`


---

### Radiation Dose Summary for Diagnostic Procedures on FHIR

<details>
<summary>Description</summary>

This IG standardizes the sharing of minimal radiation information following a performed exam. Minimal radiation information is required by multiple stakeholders including RIS, EHR, and national/regional stakeholders and their associated regulations. This IG standardizes data sharing from dose management systems to third parties. The IG exposes radiation information related to (and coming from) imaging procedures, but not medications. (built Wed, Dec 8, 2021 15:50+0000+00:00)

</details>

**Versions**

* `hl7.fhir.uv.radiation-dose-summary#0.1.0`


---

### hl7.fhir.stt

<details>
<summary>Description</summary>

Sistema Integrado de Telemedicina e Telessaúde - Universidade Federal de Santa Catarina

</details>

**Versions**

* `hl7.fhir.stt#1.0.0-beta9`
* `hl7.fhir.stt#1.0.0-beta8`
* `hl7.fhir.stt#1.0.0-beta7`
* `hl7.fhir.stt#1.0.0-beta6`
* `hl7.fhir.stt#1.0.0-beta5`
* `hl7.fhir.stt#1.0.0-beta4`
* `hl7.fhir.stt#1.0.0-beta3`
* `hl7.fhir.stt#1.0.0-beta2`
* `hl7.fhir.stt#1.0.0-beta14`
* `hl7.fhir.stt#1.0.0-beta13`
* `hl7.fhir.stt#1.0.0-beta12`
* `hl7.fhir.stt#1.0.0-beta11`
* `hl7.fhir.stt#1.0.0-beta10`
* `hl7.fhir.stt#1.0.0-beta`


---

### fbe.babycare.app

<details>
<summary>Description</summary>

BabyCare Mobile App for iOS and Android

</details>

**Versions**

* `fbe.babycare.app#1.0.1`


---

### acme.canada

<details>
<summary>Description</summary>

This is a demonstration project for FHIR North

</details>

**Versions**

* `acme.canada#0.0.4`
* `acme.canada#0.0.3`
* `acme.canada#0.0.2`
* `acme.canada#0.0.1`


---

### FHIR Implementation Guide for ABDM



**Versions**

* `ndhm.in#6.0.0`
* `ndhm.in#5.0.0`
* `ndhm.in#4.0.0`
* `ndhm.in#3.1.0`
* `ndhm.in#3.0.1`
* `ndhm.in#3.0.0`
* `ndhm.in#2.5.0`
* `ndhm.in#2.0.1`
* `ndhm.in#2.0`
* `ndhm.in#1.2`


---

### Mobile Cross-Enterprise Document Data Element Extraction (mXDE)

<details>
<summary>Description</summary>

The Mobile Cross-Enterprise Document Data Element Extraction (mXDE) Profile provides the means to access data elements extracted from shared structured documents. The profile enables the deployment of health data exchange infrastructures where fine-grained access to health data coexists and complements the sharing of coarse-grained documents and the fine-grained data elements they contain. (built Fri, Aug 4, 2023 10:53-0500-05:00)

</details>

**Versions**

* `ihe.iti.mxde#1.3.0`
* `ihe.iti.mxde#1.3.0-comment`


---

### IHE SDC/electronic Cancer Protocols (eCPs) on FHIR

<details>
<summary>Description</summary>

Integrating the Healthcare Enterprise (IHE) Structured Data Capture (SDC) on FHIR uses a form-driven workflow to capture and transmit encoded data by creating FHIR Observations (built Tue, Dec 12, 2023 21:56+0000+00:00)

</details>

**Versions**

* `hl7.fhir.uv.ihe-sdc-ecc#1.0.0`
* `hl7.fhir.uv.ihe-sdc-ecc#1.0.0-ballot`


---

### KBV.ITA.FOR

<details>
<summary>Description</summary>

Umsetzung der formularübergreifende Vorgaben für Digitale Muster der KBV

</details>

**Versions**

* `KBV.ITA.FOR#1.0.3`
* `KBV.ITA.FOR#1.0.2`
* `KBV.ITA.FOR#1.0.1`


---

### HL7 FHIR Implementation Guide: Public Health IG Release 1 - BE Realm | STU1

<details>
<summary>Description</summary>

Public Health (built Wed, Jun 12, 2024 10:51+0200+02:00)

</details>

**Versions**

* `hl7.fhir.be.public-health#1.0.3`
* `hl7.fhir.be.public-health#1.0.2`
* `hl7.fhir.be.public-health#1.0.1`
* `hl7.fhir.be.public-health#1.0.0`


---

### kbv.mio.ueberleitungsbogen

<details>
<summary>Description</summary>

Pflegerisches Informationsobjekt (PIO) Überleitungsbogen V1.0.0

</details>

**Versions**

* `kbv.mio.ueberleitungsbogen#1.0.0`
* `kbv.mio.ueberleitungsbogen#1.0.0-kommentierung`
* `kbv.mio.ueberleitungsbogen#1.0.0-benehmensherstellung`


---

### Electronic Medicinal Product Information (ePI) FHIR Implementation Guide

<details>
<summary>Description</summary>

FHIR Implementation Guide representing how to create Electronic Medicinal Product Information. (built Wed, Jul 26, 2023 13:29+0000+00:00)

</details>

**Versions**

* `hl7.fhir.uv.emedicinal-product-info#1.0.0`
* `hl7.fhir.uv.emedicinal-product-info#1.0.0-ballot`


---

### Making Electronic Data More available for Research and Public Health (MedMorph)

<details>
<summary>Description</summary>

MedMorph describes a framework to enable submission of data from healthcare organizations to public health and research organizations. (built Thu, Jun 8, 2023 18:48+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.medmorph#1.0.0`
* `hl7.fhir.us.medmorph#0.2.0`
* `hl7.fhir.us.medmorph#0.1.0`


---

### de.diga.caracare

<details>
<summary>Description</summary>

Spezifikationen für DiGas zur digitalen Behandlung von Verdauungserkrankungen

</details>

**Versions**

* `de.diga.caracare#1.0.0`


---

### eHealth Platform Federal Core Profiles

<details>
<summary>Description</summary>

eHealth Platform Federal Core Profiles (built Tue, Jun 11, 2024 14:06+0200+02:00)

</details>

**Versions**

* `hl7.fhir.be.core#2.1.1`
* `hl7.fhir.be.core#2.1.0`
* `hl7.fhir.be.core#2.0.1`
* `hl7.fhir.be.core#2.0.0`


---

### AU Base Implementation Guide

<details>
<summary>Description</summary>

This implementation guide is provided to support the use of FHIR®© in an Australian context. (built Sat, Mar 9, 2024 23:11+1100+11:00)

</details>

**Versions**

* `hl7.fhir.au.base#4.2.0-preview`
* `hl7.fhir.au.base#4.1.0`
* `hl7.fhir.au.base#4.1.2-preview`
* `hl7.fhir.au.base#4.1.1-preview`
* `hl7.fhir.au.base#4.1.0-ballot`
* `hl7.fhir.au.base#4.0.0`
* `hl7.fhir.au.base#2.2.0-ballot`
* `hl7.fhir.au.base#2.0.0`
* `hl7.fhir.au.base#0.5.0`
* `hl7.fhir.au.base#0.4.0`
* `hl7.fhir.au.base#0.1.0`


---

### ca-on-dhdr-r4.v09

<details>
<summary>Description</summary>

Implementation Guide for the Ontario DHDR Version 3

</details>

**Versions**

* `ca-on-dhdr-r4.v09#0.9.0`


---

### Retrieval of Real World Data for Clinical Research

<details>
<summary>Description</summary>

A FHIR Implementation Guide that provides profiles and use cases that show how real world data can be exposed in such a way that it can be used for research purposes. (built Fri, May 26, 2023 22:51+1000+10:00)

</details>

**Versions**

* `hl7.fhir.uv.vulcan-rwd#1.0.0`
* `hl7.fhir.uv.vulcan-rwd#1.0.0-ballot`


---

### MII IG Person

<details>
<summary>Description</summary>

Medizininformatik Initiative - Modul Person

</details>

**Versions**

* `de.medizininformatikinitiative.kerndatensatz.person#2024.0.0-ballot`
* `de.medizininformatikinitiative.kerndatensatz.person#1.0.17`


---

### Patient Cost Transparency Implementation Guide

<details>
<summary>Description</summary>

To support the request for cost information for specific services and items from the payer and return them in near real-time to allow effective decision making by the patient in consultation with the 'ordering' provider. (built Mon, Apr 8, 2024 16:49+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.davinci-pct#2.0.0-ballot`
* `hl7.fhir.us.davinci-pct#1.1.0`
* `hl7.fhir.us.davinci-pct#1.0.0`
* `hl7.fhir.us.davinci-pct#0.1.0`


---

### FHIR Profile for Digital Health Applications that treat self-reported nicotine usage  (F17.2)

<details>
<summary>Description</summary>

A FHIR profile to export patient data for DiGAs that treat ICD-10 F17.2. (built Thu, Apr 1, 2021 18:14+0200+02:00)

</details>

**Versions**

* `com.alextherapeutics.fhir.nicotine#1.0.1`
* `com.alextherapeutics.fhir.nicotine#0.1.0-draft`


---

### Guía de Implementación ''cl core'' FHIR R4, (Versión Evolutiva)

<details>
<summary>Description</summary>

Guía de Implementación Versión de Desarrollo Sobre STU 1, para los perfiles Core que se van a requerir a nivel de desarrollo Nacional para Sistemas que Intercambien datos en estandar FHIR-R4 (built Tue, Mar 19, 2024 15:38-0300-03:00)

</details>

**Versions**

* `hl7.fhir.cl.clcore#1.8.10`
* `hl7.fhir.cl.clcore#1.8.6`
* `hl7.fhir.cl.clcore#1.8.5`
* `hl7.fhir.cl.clcore#1.8.4`
* `hl7.fhir.cl.clcore#1.8.3`
* `hl7.fhir.cl.clcore#1.8.2`
* `hl7.fhir.cl.clcore#1.8.1`
* `hl7.fhir.cl.clcore#1.8.0`


---

### Guía de Implementación Core-CL FHIR R4, (Versión Evolutiva)

<details>
<summary>Description</summary>

Guía de Implementación Versión de Desarrollo Sobre STU 1, para los perfiles Core que se van a requerir a nivel de desarrollo Nacional para Sistemas que Intercambien datos en estandar FHIR-R4 (built Tue, Sep 6, 2022 11:46-0300-03:00)

</details>

**Versions**

* `hl7.fhir.cl.corecl#1.7.0`


---

### CH LAB-Order (R4)

<details>
<summary>Description</summary>

Implementation guide CH CH LAB-Order (R4) (built Tue, Jun 18, 2024 13:27+0000+00:00)

</details>

**Versions**

* `ch.fhir.ig.ch-lab-order#2.0.0`
* `ch.fhir.ig.ch-lab-order#2.0.0-ballot`
* `ch.fhir.ig.ch-lab-order#1.0.0`
* `ch.fhir.ig.ch-lab-order#0.1.0`


---

### Répertoire national de l’Offre et des Ressources en santé et accompagnement médico-social

<details>
<summary>Description</summary>

Ressources de conformité basées sur le modèle d'exposition 3.0 du ROR (built Wed, May 22, 2024 08:41+0000+00:00)

</details>

**Versions**

* `ans.fhir.fr.ror#0.4.0-snapshot-1`
* `ans.fhir.fr.ror#0.3.0`
* `ans.fhir.fr.ror#0.2.0`
* `ans.fhir.fr.ror#0.1.1`
* `ans.fhir.fr.ror#0.1.0`


---

### org.example.ProfilingTrainingMay

<details>
<summary>Description</summary>

Profiles for the May profiling training

</details>

**Versions**

* `org.example.ProfilingTrainingMay#0.0.1`


---

### FHIR Implementation Guide for NDHM



**Versions**

* `hl7.fhir.in#1.0.0`


---

### Pan-Canadian Patient Summary

<details>
<summary>Description</summary>

Pan-Canadian Patient Summary (PS-CA) 1.1.0 DFT - Trial Ballot Release to support Limited Production Rollouts (LPRs) of Patient Summary in Canada

</details>

**Versions**

* `ca.infoway.io.psca#1.1.0`
* `ca.infoway.io.psca#1.0.0`
* `ca.infoway.io.psca#1.0.0-projectathon-pre`
* `ca.infoway.io.psca#1.0.0-pre`
* `ca.infoway.io.psca#0.4.0-pre`
* `ca.infoway.io.psca#0.3.2`
* `ca.infoway.io.psca#0.3.1`
* `ca.infoway.io.psca#0.3.0`
* `ca.infoway.io.psca#0.3.0-pre`
* `ca.infoway.io.psca#0.0.4`


---

### custom-extensions.ammy.test

<details>
<summary>Description</summary>

customExtensions

</details>

**Versions**

* `custom-extensions.ammy.test#1.0.0`


---

### kbv.itv.evdga

<details>
<summary>Description</summary>

Der Gesetzgeber hat im Zuge des „Gesetzes für eine bessere Versorgung durch Digitalisierung und Innovation“ im Rahmen des § 33a SGB V festgelegt, dass Versicherte einen Leistungsanspruch auf Versorgung mit digitalen Gesundheitsanwendungen haben, welche Medizinprodukte niedriger Risikoklasse darstellen, deren Hauptfunktion wesentlich auf digitalen Technologien beruht und die dazu bestimmt sind, bei den Versicherten oder in der Versorgung durch Leistungserbringer die Erkennung, Überwachung, Behandlung oder Linderung von Krankheiten oder die Erkennung, Behandlung, Linderung oder Kompensierung von Verletzungen oder Behinderungen zu unterstützen. Damit wurde der Kreis der auch digital verordnungsfähigen Produkte und Leistungen gemäß § 86 SGB V Abs. 1 Satz 1 Nummer 2 auf digitale Gesundheitsanwendungen erweitert. 

Weitere technischen Vorgaben zur Verwendung der Profile finden sich unter https://update.kbv.de/ita-update/DigitaleMuster/eVDGA/ in dem Dokument [KBV_ITA_VGEX_Technische_Anlage_EVDGA].

</details>

**Versions**

* `kbv.itv.evdga#1.0.0-cc`


---

### nhn.fhir.no.kjernejournal

<details>
<summary>Description</summary>

Kjernejournal-profiler FHIR R4

</details>

**Versions**

* `nhn.fhir.no.kjernejournal#1.0.1`
* `nhn.fhir.no.kjernejournal#1.0.0`


---

### kl.dk.fhir.gateway



**Versions**

* `kl.dk.fhir.gateway#1.1.0`


---

### com.medipee.fhir.uroli-export

<details>
<summary>Description</summary>

Medipee Export-Spezifikation

</details>

**Versions**

* `com.medipee.fhir.uroli-export#1.0.0`


---

### KBV.ITA.EAU

<details>
<summary>Description</summary>

Umsetzung der elektronischen Arbeitsunfähigkeit

</details>

**Versions**

* `KBV.ITA.EAU#1.0.2`
* `KBV.ITA.EAU#1.0.1`


---

### HL7 Version 2 to FHIR

<details>
<summary>Description</summary>

The HL7 V2 to FHIR Implementation Guide supports the mapping of HL7 Version 2 messages segments, datatypes and vocabulary to HL7 FHIR Release 4.0 Bundles, Resources, Data Types and Coding Systems. (built Wed, Dec 20, 2023 15:18+0000+00:00)

</details>

**Versions**

* `hl7.fhir.uv.v2mappings#1.0.0-ballot`
* `hl7.fhir.uv.v2mappings#0.1.0`


---

### NHSN Reporting: Inpatient Medication Administration

<details>
<summary>Description</summary>

This IG will support electronic submission of patient/line-level medication administration data to the National Healthcare Safety Network (NHSN). The intent of this project is to establish an electronic submission standard that is vendor-neutral that leverages existing workflows and eliminates duplicate documentation. This project will work with EHR vendors to identify data elements that can be used to describe medications administered (name, formulation, route, dose, duration) to hospitalized patients (inpatients) diagnosed with COVID-19 as part of NHSN COVID-19 reporting pathways. Medication administration events would be reported irrespective of whether or not medication administration was linked to an adverse event, refer to clinician-administered events (e.g., nurse giving a patient their medication dose), and only those occurring in inpatient settings; this excludes: emergency department, observation/short stay, inpatient rehabilitation, outpatient surgical centers, and other outpatient (including physician office) settings.

Medication data are integral to informing the quality, safety, and costs of U.S. healthcare, supporting federal, state, and local public health, and guiding clinical decision-making in patient care. In inpatient workflows, medication administration—as captured by electronic medication administration (eMAR) records—is considered the gold standard for accurately measuring in-hospital medication exposure, including identifying the exact medications patients have received, in what formulations, doses, and for what duration of time. The need for medication administration information has never been clearer as during the COVID-19 pandemic, where identification of the medications that acutely ill hospitalized patients with COVID-19 had received was integral to understanding clinical management of this new public health threat and directing public health resources, including scarce medications. The continued reliance on the "medication list" resource instead of the "medication administration" resource is a severe limitation in achieving accurate representation of medication exposure in U.S. healthcare data through FHIR resources.

### IG

This implementation guide provides HL7 FHIR resources to define standards for exchange between a hospital and NHSN. 

This publication provides the data model, defined data items, and their corresponding code and value sets, for reporting inpatient medication administration data for inpatients diagnosed with COVID-19. This guide describes constraints on the US Core and base FHIR resources for reporting, which are derived from requirements developed by the NHSN in consultation with the Health Level Seven (HL7) Pharmacy Work Group. Resources in this US Realm implementation guide are specific to reporting medication administration data to NHSN.

This guide contains a library of FHIR profiles and is compliant with FHIR Release 4. At a minimum, a Document Bundle and Composition will be exchanged along with a Patient, and associated COVID-19 Condition and laboratory results.

This guide defines 4 new Profiles:

* [Composition - Inpatient Medication Administration](StructureDefinition-Composition-inpatient-med-admin.html)
* [Condition - Lab Confirmed COVID-19](StructureDefinition-Condition-lab-confirmed-covid.html)
* [Condition - Suspected COVID-19](StructureDefinition-Condition-suspected-covid.html)
* [Observation - Laboratory SARS COVID-19](StructureDefinition-Observation-lab-sars-cov.html) (built Tue, Oct 12, 2021 13:08+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.nhsn-med-admin#1.0.0`
* `hl7.fhir.us.nhsn-med-admin#0.1.0`


---

### CH EPR Term

<details>
<summary>Description</summary>

Implementation guide for the meta data specified in the framework of Annex 3 and 9 of the FDHA Ordinance on the electronic patient record in Switzerland (built Tue, Dec 19, 2023 12:36+0100+01:00)

</details>

**Versions**

* `ch.fhir.ig.ch-epr-term#2.0.10`
* `ch.fhir.ig.ch-epr-term#2.0.9`
* `ch.fhir.ig.ch-epr-term#2.0.8`
* `ch.fhir.ig.ch-epr-term#2.0.7`
* `ch.fhir.ig.ch-epr-term#2.0.6`
* `ch.fhir.ig.ch-epr-term#2.0.5`
* `ch.fhir.ig.ch-epr-term#2.0.4`
* `ch.fhir.ig.ch-epr-term#2.0.3`
* `ch.fhir.ig.ch-epr-term#2.0.2`
* `ch.fhir.ig.ch-epr-term#2.0.0`


---

### kl.dk.fhir.prevention

<details>
<summary>Description</summary>

Danish municipalities implementation guide for reporting §119 disease prevention and health promotion (built Fri, Jun 14, 2024 10:01+0200+02:00)

</details>

**Versions**

* `kl.dk.fhir.prevention#2.0.0`
* `kl.dk.fhir.prevention#1.0.0`


---

### dvmd.kdl.r4.2021

<details>
<summary>Description</summary>

Publikation der Terminologie-Ressourcen für die Nutzung der Klinische Dokumentenklassen-Liste (KDL) des DVMD
Version 2020

</details>

**Versions**

* `dvmd.kdl.r4.2021#2021.1.1`
* `dvmd.kdl.r4.2021#2021.1.0`


---

### Da Vinci Risk Adjustment Implementation Guide

<details>
<summary>Description</summary>

The Da Vinci Fast Healthcare Interoperability Resource (FHIR) Risk Adjustment Implementation Guide (this IG) describes exchange of risk-based coding gaps among stakeholders such as payers, providers, and government care programs in support of driving towards accurate and complete documentation of health conditions that would lead to more accurate risk-adjustment payment calculations. (built Tue, Mar 28, 2023 02:00+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.davinci-ra#2.0.0-ballot`
* `hl7.fhir.us.davinci-ra#1.0.0`
* `hl7.fhir.us.davinci-ra#0.1.0`


---

### de.gematik.isip

<details>
<summary>Description</summary>

Package Release des ISIP Basis Modul

</details>

**Versions**

* `de.gematik.isip#1.0.2`


---

### AU Core Implementation Guide

<details>
<summary>Description</summary>

This implementation guide is provided to support the use of FHIR®© in an Australian context, and defines the minimum set of constraints on the FHIR resources to create the AU Core profiles. This implementation guide forms the foundation to build future AU Realm FHIR implementation guides and its content will continue to grow to meet the needs of AU implementers. (built Sun, Mar 10, 2024 01:01+1100+11:00)

</details>

**Versions**

* `hl7.fhir.au.core#0.3.0-ballot`
* `hl7.fhir.au.core#0.2.2-preview`
* `hl7.fhir.au.core#0.2.1-preview`
* `hl7.fhir.au.core#0.2.0-preview`
* `hl7.fhir.au.core#0.1.0-draft`


---

### de.gematik.isik-basismodul

<details>
<summary>Description</summary>

Put a description here

</details>

**Versions**

* `de.gematik.isik-basismodul#4.0.0-rc2`
* `de.gematik.isik-basismodul#4.0.0-rc`
* `de.gematik.isik-basismodul#3.0.5`
* `de.gematik.isik-basismodul#3.0.4`
* `de.gematik.isik-basismodul#3.0.3`
* `de.gematik.isik-basismodul#3.0.2`
* `de.gematik.isik-basismodul#3.0.1`
* `de.gematik.isik-basismodul#2.0.6`
* `de.gematik.isik-basismodul#2.0.5`


---

### Da Vinci Health Record Exchange (HRex)

<details>
<summary>Description</summary>

The Da Vinci Payer Health Record exchange (HRex) Framework/library specifies the FHIR elements used in multiple Da Vinci implementation guides. This includes FHIR profiles, functions, operations, and constraints on other specifications such as CDS-Hooks and other aspects of Da Vinci Use Cases that are common across more than a single use case.

Da Vinci HRex Implementation Guide (IG) will make use of US Core profiles that are based on the FHIR R4 specification wherever practical. The HRex IG will use the HL7 FHIR Release 4/US Core STU3 specification as its base but will provide additional guidance and documentation to support implementations that follow the HL7 FHIR STU3/US Core STU2 and HL7 FHIR DSTU2/Argonaut specifications.

The HRex profiles documented in this IG will be used to exchange data between providers systems (e.g. EHRs) and other providers, payers, and third-party applications where appropriate. In addition, exchanges from payer systems to providers, other payers, and third-party applications are supported by the HRex profiles and operations.

HRex may define new extensions, profiles, value sets, constraints/extension to other specification (e.g. specific CDS-Hooks) that are specific Da Vinci requirements. Where appropriate these Da Vinci specific artifacts will be promoted for incorporation into the future versions of existing standards (e.g. R4 US Core profiles) and deprecated in this guide on publication in the updated standard. (built Wed, Mar 23, 2022 18:55+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.davinci-hrex#1.0.0`
* `hl7.fhir.us.davinci-hrex#0.2.0`
* `hl7.fhir.us.davinci-hrex#0.1.0`


---

### CARIN Consumer Directed Payer Data Exchange (CARIN IG for Blue Button®)

<details>
<summary>Description</summary>

CARIN Consumer Directed Payer Data Exchange (CARIN IG for Blue Button®) (built Mon, Nov 28, 2022 15:15+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.carin-bb#2.0.0`
* `hl7.fhir.us.carin-bb#1.2.0`
* `hl7.fhir.us.carin-bb#1.1.0`
* `hl7.fhir.us.carin-bb#1.0.0`
* `hl7.fhir.us.carin-bb#0.1.0`


---

### fmcna.caredata.fhir.ig.r4.copy

<details>
<summary>Description</summary>

Fresenius Medical Care Data Model

</details>

**Versions**

* `fmcna.caredata.fhir.ig.r4.copy#1.0.0`


---

### iknl.fhir.nl.r4.ncr-ehr

<details>
<summary>Description</summary>

FHIR R4 profiles for the electronic health record (EHR) submissions to the Netherlands Cancer Registry (NCR). (This version is identical to 1.0.1 and is meant for testing the release system)

</details>

**Versions**

* `iknl.fhir.nl.r4.ncr-ehr#1.0.2`
* `iknl.fhir.nl.r4.ncr-ehr#1.0.1`
* `iknl.fhir.nl.r4.ncr-ehr#1.0.0`
* `iknl.fhir.nl.r4.ncr-ehr#0.4.0-alpha`
* `iknl.fhir.nl.r4.ncr-ehr#0.3.0`
* `iknl.fhir.nl.r4.ncr-ehr#0.2.0`
* `iknl.fhir.nl.r4.ncr-ehr#0.1.0`


---

### ca.on.oh-ereferral-econsult

<details>
<summary>Description</summary>

Ontario-specific business use cases and content

</details>

**Versions**

* `ca.on.oh-ereferral-econsult#0.11.1-alpha1.0.12`
* `ca.on.oh-ereferral-econsult#0.11.1-alpha1.0.11`
* `ca.on.oh-ereferral-econsult#0.11.1-alpha1.0.10`
* `ca.on.oh-ereferral-econsult#0.11.0-alpha1.0.8`
* `ca.on.oh-ereferral-econsult#0.11.0-alpha1.0.7`
* `ca.on.oh-ereferral-econsult#0.11.0-alpha1.0.6`
* `ca.on.oh-ereferral-econsult#0.11.0-alpha-1.0.6`
* `ca.on.oh-ereferral-econsult#0.11.0-alpha1.0.5`
* `ca.on.oh-ereferral-econsult#0.11.0-alpha1.0.4`
* `ca.on.oh-ereferral-econsult#0.11.1-alpha1.0.3`
* `ca.on.oh-ereferral-econsult#0.11.0-alpha1.0.3`
* `ca.on.oh-ereferral-econsult#0.11.1-alpha1.0.2`
* `ca.on.oh-ereferral-econsult#0.11.1-alpha1.0.1`
* `ca.on.oh-ereferral-econsult#0.11.1-alpha1.0.0`


---

### tiga.health.clinical

<details>
<summary>Description</summary>

tiga-health-clinical

</details>

**Versions**

* `tiga.health.clinical#1.1.0`


---

### de.gevko.emdaf

<details>
<summary>Description</summary>

elektronisches Medikations-Datenaustauschformat

</details>

**Versions**

* `de.gevko.emdaf#1.3.0`
* `de.gevko.emdaf#1.2.0`
* `de.gevko.emdaf#1.1.2`
* `de.gevko.emdaf#1.1.1`
* `de.gevko.emdaf#1.1.0`
* `de.gevko.emdaf#1.0.0`
* `de.gevko.emdaf#0.9.0`


---

### Canadian Baseline

<details>
<summary>Description</summary>

Canadian Baseline (CA Baseline) FHIR Profiles

The goal of the CA Baseline specification is to expose the implementation guide author community and vendor community to a set of profiles that identify the data elements, code systems and value sets that are commonly present across Canada for a given FHIR resource (e.g., patient, medication, etc.) regardless of use case, jurisdiction or implementation.

</details>

**Versions**

* `hl7.fhir.ca.baseline#1.1.7`
* `hl7.fhir.ca.baseline#1.1.6`
* `hl7.fhir.ca.baseline#1.1.4`
* `hl7.fhir.ca.baseline#1.1.3`
* `hl7.fhir.ca.baseline#1.1.6-pre`
* `hl7.fhir.ca.baseline#1.1.5-pre`


---

### MedCom HomeCareObservation



**Versions**

* `medcom.fhir.dk.homecareobservation#1.0.0`


---

### de.gematik.isik-basismodul-stufe1

<details>
<summary>Description</summary>

Informationstechnische Systeme im Krankenhaus

</details>

**Versions**

* `de.gematik.isik-basismodul-stufe1#1.0.10`


---

### colonoscopyreport.no

<details>
<summary>Description</summary>

Sending colonoscopy data for cancer screening to a central registry in Norway

</details>

**Versions**

* `colonoscopyreport.no#0.7.23`
* `colonoscopyreport.no#0.7.22`
* `colonoscopyreport.no#0.7.21`
* `colonoscopyreport.no#0.7.20`
* `colonoscopyreport.no#0.7.19`
* `colonoscopyreport.no#0.7.18`
* `colonoscopyreport.no#0.7.17`


---

### Longitudinal Maternal & Infant Health Information for Research

<details>
<summary>Description</summary>

### Scope
The Longitudinal Maternal & Child Health Information for Research FHIR R4 implementation guide (IG) defines a framework to enable maternal health researchers to aggregate, calculate, and analyze clinical information of research populations to explore the root causes for maternal and child morbidity and mortality. It uses Clinical Quality Language (CQL) expressions to assist researchers in capturing clinical data based on population study cohort criteria. This IG focuses on information relevant to longitudinal maternal care, which includes antepartum (including pre-pregnancy), intrapartum, and postpartum care of a pregnant woman. It includes how to link maternal longitudinal record with associated child/children records. 

This US Realm IG supports the use of US Core profiles where possible, as well as base FHIR and Vital Records Common Profiles FHIR IG data model for the structural linkage of mother and child clinical records. 


### Background
The rates of maternal mortality have been rising in the United States since 1987. Clinical data relevant to understanding this trend are not standardized, and data exchange is not interoperable across many relevant settings. Maternal health and associated child health are inextricably linked – what happens during gestation, delivery, and after informs health outcomes of both mother and child – but relevant data is often held in separate, unconnected records. These issues impede research on maternal morbidity and longitudinal maternal care and associated impacts to infant health. Research on root causes of maternal mortality, pediatric developmental problems, and effective treatments requires exchange of information stored in disparate sources, such as electronic health record (EHR) systems, registries, and public health agencies (PHAs).

The types of information needed to research maternal health and morbidity include social determinants of health (SDOH) and associated clinical data such as antepartum, intrapartum, and postpartum care of a pregnant woman; pregnancy-related conditions and outcomes; maternal co-morbidities; child health data; and procedures. The goal of this FHIR IG is to define a model to support data exchange for predictive analysis, risk assessment, and retrospective maternal health research across the spectrum and duration of care. 

Future users may include health departments using EHR data to inform public health interventions (e.g., case identification for reportable conditions, identifying persons lost to care, etc.) and maternal and child health researchers. The standards development effort will also examine options for data exchange mechanisms, including point-in-time query (data pull) and research population creation, i.e., patient enrollment in a study.

### Maternal Research Use Cases

This IG will eventually support mapping maternal data across health records from specialty care and linking mother and child data harmonized across a broad set of use cases. This will support researchers in identifying root causes of maternal mortality and pediatric developmental problems, including SDOH such as limited income, poor nutrition, lack of medical coverage, etc. The goal of the project is to create a method to standardize data capture for comparative analysis over time to improve health outcomes and define a framework for studying additional research populations in the future.

Initial use cases of this IG focus on hypertensive disorders of pregnancy pre, ante, and postpartum and pregnancy and subsequent death within a specific timeframe. The intent is to specify the consistent capture of clinical data of interest to maternal health researchers and outline implementing FHIR resources for that capture. Currently, the IG defines two initial, separate research use case populations: 

* Pregnancy and subsequent death within a specific time frame: This cohort includes women who died within a year (365 days) of a pregnancy regardless of cause of death or pregnancy outcome.
* Hypertensive Disorders of pregnancy: This use case focuses on women with a diagnosis of hypertensive disorders of pregnancy.

In both instances, the IG will establish linkages via the US Core Related Person profile to collect associated child health data that may inform maternal health research outcomes. 

In the future, the IG will expand this framework to a range of use cases including:
* Risks for children related to maternal exposure to medications taken during pregnancy
* Potential adverse maternal obstetric history impacts on child outcomes
* Access to relevant sensitive health information
* Retrospective population-based analysis of inherited disorders
* The impacts of [work habits, work environment, and work-associated health insurance](http://hl7.org/fhir/us/odh/) impacts to pregnancy and maternal health

This guide fundamentally relies on creating structural relationships between:
* Maternal and child records to effectively diagnose and treat otherwise fatal child outcomes
* Maternal and child birth records and/or maternal and child death records
* Maternal and child records in multiple disparate systems

### Audience 
The audience for this IG includes EHR vendors, developers of software tooling researchers, and associated information management systems. Researchers, business analysts, and policy managers can also benefit from a basic understanding of the use of this guide to support measure calculation for research purposes.

### Authors & Project Team
This table lists the authors, subject matter experts, and the affiliations which contributed to this standard. 

<style type="text/css">
.tg  {border-collapse:collapse;border-spacing:0;}
.tg td{border-color:black;border-style:solid;border-width:1px;font-family:Arial, sans-serif;font-size:14px;
  overflow:hidden;padding:10px 5px;word-break:normal;}
.tg th{border-color:black;border-style:solid;border-width:1px;font-family:Arial, sans-serif;font-size:14px;
  font-weight:normal;overflow:hidden;padding:10px 5px;word-break:normal;}
.tg .tg-4erg{border-color:inherit;font-style:italic;font-weight:bold;text-align:left;vertical-align:top}
.tg .tg-0r4h{border-color:inherit;font-family:serif !important;font-weight:bold;text-align:left;vertical-align:top}
.tg .tg-fymr{border-color:inherit;font-weight:bold;text-align:left;vertical-align:top}
.tg .tg-0pky{border-color:inherit;text-align:left;vertical-align:top}
</style>
<table class="tg">
<thead>
  <tr>
    <th class="tg-fymr">Name &amp; Affiliation</th>
    <th class="tg-0r4h">Role</th>
    <th class="tg-fymr">Contact</th>
  </tr>
</thead>
<tbody>
  <tr>
    <td class="tg-4erg">Lantana Consulting Group</td>
    <td class="tg-0pky"> </td>
    <td class="tg-0pky"> </td>
  </tr>
  <tr>
    <td class="tg-0pky">Courtney Panaia-Rodi </td>
    <td class="tg-0pky">Project Executive</td>
    <td class="tg-0pky">courtney.panaia-rodi@lantanagroup.com</td>
  </tr>
  <tr>
    <td class="tg-0pky">Wendy Wise</td>
    <td class="tg-0pky">Project Manager</td>
    <td class="tg-0pky">wendy.wise@lantanagroup.com</td>
  </tr>
  <tr>
    <td class="tg-0pky">Lani Johnson</td>
    <td class="tg-0pky">Associate Project Manager</td>
    <td class="tg-0pky">lani.johnson@lantanagroup.com</td>
  </tr>
  <tr>
    <td class="tg-0pky">Rick Geimer</td>
    <td class="tg-0pky">FHIR Subject Matter Expert</td>
    <td class="tg-0pky">rick.geimer@lantanagroup.com</td>
  </tr>
  <tr>
    <td class="tg-0pky">Zabrina Gonzaga</td>
    <td class="tg-0pky">Terminology Subject Matter Expert</td>
    <td class="tg-0pky">zabrina.gonzaga@lantanagroup.com</td>
  </tr>
  <tr>
    <td class="tg-0pky">Sarah Gaunt</td>
    <td class="tg-0pky">Senior FHIR/CDA Analyst</td>
    <td class="tg-0pky">sarah.gaunt@lantanagroup.com</td>
  </tr>
  <tr>
    <td class="tg-0pky">Dave deRoode</td>
    <td class="tg-0pky">FHIR/CDA Analyst</td>
    <td class="tg-0pky">david.deroode@lantanagroup.com</td>
  </tr>
  <tr>
    <td class="tg-0pky">Ming Dunajick</td>
    <td class="tg-0pky">FHIR/CDA Analyst</td>
    <td class="tg-0pky">ming.dunajick@lantanagroup.com</td>
  </tr>
  <tr>
    <td class="tg-0pky">Ruby Nash</td>
    <td class="tg-0pky">FHIR Analyst</td>
    <td class="tg-0pky">ruby.nash@lantanagroup.com</td>
  </tr>
  <tr>
    <td class="tg-4erg">Office of the Assistant Secretary for Planning and Evaluation (ASPE)</td>
    <td class="tg-0pky"> </td>
    <td class="tg-0pky"> </td>
  </tr>
  <tr>
    <td class="tg-0pky">Violanda Grigorescu, MD, MSPH</td>
    <td class="tg-0pky">Senior Health Scientist <br>Division of Healthcare Quality and Outcomes, Office of Health Policy</td>
    <td class="tg-0pky">violanda.grigorescu@hhs.gov</td>
  </tr>
  <tr>
    <td class="tg-4erg">Centers for Disease Control and Prevention (CDC)</td>
    <td class="tg-0pky"> </td>
    <td class="tg-0pky"> </td>
  </tr>
  <tr>
    <td class="tg-0pky">Margaret Lampe, RN, MPH</td>
    <td class="tg-0pky">Nurse Epidemiologist &amp; Project Officer <br>Perinatal HIV Prevention Program</td>
    <td class="tg-0pky">mol0@cdc.gov</td>
  </tr>
  <tr>
    <td class="tg-0pky">Lisa Romero, DrPH</td>
    <td class="tg-0pky">Health Scientist <br>Division of Adolescent School Health</td>
    <td class="tg-0pky">eon1@cdc.gov</td>
  </tr>
  <tr>
    <td class="tg-4erg">National Institutes of Health (NIH) <br>Eunice Kennedy Shriver National Institute of Child Health and Human Development (NICHD) <br>National Information Center on Health Services Research and Health Care Technology (NICHSR)</td>
    <td class="tg-0pky"> </td>
    <td class="tg-0pky"> </td>
  </tr>
  <tr>
    <td class="tg-0pky">Alison Cernich</td>
    <td class="tg-0pky">NICHD Deputy Director</td>
    <td class="tg-0pky">alison.cernich@nih.hhs.gov</td>
  </tr>
  <tr>
    <td class="tg-0pky">John (Jack) Moye, Jr., MD</td>
    <td class="tg-0pky">Acting Director - National Children's Study <br>NICHD Medical Officer - Maternal &amp; Pediatric Infectious Disease Branch</td>
    <td class="tg-0pky">moyej@exchange.nih.gov</td>
  </tr>
  <tr>
    <td class="tg-0pky">Nahida Chakhtoura, MD, MsGH</td>
    <td class="tg-0pky">NICHD Medical Officer <br>Maternal and Pediatric Infectious Disease Branch</td>
    <td class="tg-0pky">nahida.chakhtoura@nih.gov</td>
  </tr>
  <tr>
    <td class="tg-0pky">Juanita Chinn, PhD</td>
    <td class="tg-0pky">NICHD Program Director <br>Population Dynamics Branch</td>
    <td class="tg-0pky">juanita.chinn@nih.gov</td>
  </tr>
  <tr>
    <td class="tg-0pky">Valerie Cotton</td>
    <td class="tg-0pky">NICHD Deputy Director <br>Office of Data Science and Sharing</td>
    <td class="tg-0pky">valerie.cotton@nih.gov</td>
  </tr>
  <tr>
    <td class="tg-0pky">Liz Amos, MLIS</td>
    <td class="tg-0pky">Special Assistant to the Chief Health Data Standards Officer <br>National Library of Medicine</td>
    <td class="tg-0pky">liz.amos@nih.gov</td>
  </tr>
  <tr>
    <td class="tg-4erg">Office of the National Coordinator for Health IT (ONC)</td>
    <td class="tg-0pky"> </td>
    <td class="tg-0pky"> </td>
  </tr>
  <tr>
    <td class="tg-0pky">Carmen Smiley</td>
    <td class="tg-0pky">IT Specialist (Systems Analysis)</td>
    <td class="tg-0pky">carmen.smiley@hhs.gov</td>
  </tr>
  <tr>
    <td class="tg-0pky">Rachel Abbey</td>
    <td class="tg-0pky">Public Health Analyst &amp; Program Officer</td>
    <td class="tg-0pky">rachel.abbey@hhs.gov</td>
  </tr>
  <tr>
    <td class="tg-0pky">Stephanie Garcia</td>
    <td class="tg-0pky">Senior Program Analyst</td>
    <td class="tg-0pky">stephanie.garcia@hhs.gov</td>
  </tr>
  <tr>
    <td class="tg-0pky">Brittney Boakye, MPH</td>
    <td class="tg-0pky">Program Assistant <br>Scientific Advancement Branch</td>
    <td class="tg-0pky">brittney.boakye@hhs.gov<br></td>
  </tr>
  <tr>
    <td class="tg-0pky">Alan Taylor</td>
    <td class="tg-0pky">Medical Informatics Officer, Standards and Terminology</td>
    <td class="tg-0pky">albert.taylor@hhs.gov</td>
  </tr>
</tbody>
</table>

### Acknowledgements
This guide was developed and produced through the efforts of Health Level Seven (HL7) and created using the Trifolia-on-FHIR tool, provided by Lantana Consulting Group. The HL7 Project Insight reference number for this project is 1736.
The editors appreciate the support and sponsorship of the HL7 Public Health Workgroup, and all volunteers and staff associated with the creation of this document. This guide would not have been possible without the support of the following groups.
Health Level Seven, HL7, CDA, CCD, FHIR and the [FLAME DESIGN] are registered trademarks of Health Level Seven International, registered in the US Trademark Office.

This IG includes content from SNOMED CT, which is copyright © 2002+ International Health Terminology Standards Development Organisation (IHTSDO), and distributed by agreement between IHTSDO and HL7. Implementer use of SNOMED CT is not covered by this agreement.

This material contains content from [LOINC](http://loinc.org). LOINC is copyright © 1995-2021, Regenstrief Institute, Inc. and the Logical Observation Identifiers Names and Codes (LOINC) Committee and is available at no cost under the license at https://loinc.org/kb/license/. LOINC® is a registered United States trademark of Regenstrief Institute, Inc. (built Wed, Mar 29, 2023 19:24+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.mihr#1.0.0`
* `hl7.fhir.us.mihr#1.0.0-ballot`


---

### de.gematik.fhir.directory

<details>
<summary>Description</summary>

Verzeichnisdienst der Telematikinfrastruktur 

</details>

**Versions**

* `de.gematik.fhir.directory#0.11.7`
* `de.gematik.fhir.directory#0.11.6`
* `de.gematik.fhir.directory#0.11.5`
* `de.gematik.fhir.directory#0.11.4`
* `de.gematik.fhir.directory#0.11.3`
* `de.gematik.fhir.directory#0.11.2`
* `de.gematik.fhir.directory#0.11.1`
* `de.gematik.fhir.directory#0.11.0`
* `de.gematik.fhir.directory#0.10.2`
* `de.gematik.fhir.directory#0.10.1`


---

### FHIR Core package

<details>
<summary>Description</summary>

FHIR Core package - the NPM package that contains all the definitions for the base FHIR specification (built Sun, Mar 26, 2023 15:21+1100+11:00)

</details>

**Versions**

* `hl7.fhir.r5.core#5.0.0`
* `hl7.fhir.r5.core#5.0.0-snapshot3`
* `hl7.fhir.r5.core#5.0.0-snapshot1`
* `hl7.fhir.r5.core#5.0.0-draft-final`
* `hl7.fhir.r5.core#5.0.0-ballot`
* `hl7.fhir.r5.core#4.6.0`
* `hl7.fhir.r5.core#4.5.0`
* `hl7.fhir.r5.core#4.4.0`
* `hl7.fhir.r5.core#4.2.0`


---

### FHIR Extensions Pack

<details>
<summary>Description</summary>

This IG defines the global extensions - the ones defined for everyone. These extensions are always in scope wherever FHIR is being used (built Sat, Apr 27, 2024 18:39+1000+10:00)

</details>

**Versions**

* `hl7.fhir.uv.extensions#5.1.0`
* `hl7.fhir.uv.extensions#5.1.0-cibuild`
* `hl7.fhir.uv.extensions#5.1.0-ballot1`
* `hl7.fhir.uv.extensions#1.0.0`
* `hl7.fhir.uv.extensions#0.1.0`


---

### de.abda.erezeptabgabedatenpkv

<details>
<summary>Description</summary>

Der PKV-Abgabedatensatz enthält die notwendigen Informationen für die Abrechnung einer elektronischen Arzneimittelverordnung (E-Rezept) für Privatversicherte durch eine Apotheke.

</details>

**Versions**

* `de.abda.erezeptabgabedatenpkv#1.2.0`
* `de.abda.erezeptabgabedatenpkv#1.1.0`
* `de.abda.erezeptabgabedatenpkv#1.1.0-rc9`
* `de.abda.erezeptabgabedatenpkv#1.1.0-rc8`
* `de.abda.erezeptabgabedatenpkv#1.1.0-rc7`
* `de.abda.erezeptabgabedatenpkv#1.1.0-rc6`
* `de.abda.erezeptabgabedatenpkv#1.1.0-rc5`
* `de.abda.erezeptabgabedatenpkv#1.1.0-rc4`
* `de.abda.erezeptabgabedatenpkv#1.1.0-rc3`
* `de.abda.erezeptabgabedatenpkv#1.1.0-rc2`
* `de.abda.erezeptabgabedatenpkv#1.1.0-rc12`
* `de.abda.erezeptabgabedatenpkv#1.1.0-rc11`
* `de.abda.erezeptabgabedatenpkv#1.1.0-rc10`


---

### Vital Signs with Qualifying Elements

<details>
<summary>Description</summary>

This IG describes vital signs observations that include qualifying information such as cuff size for blood pressure, or an associated situation of "during exercise" for blood pressure or heart rate, etc. (built Mon, Sep 12, 2022 00:44+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.vitals#1.0.0`


---

### Specialty Medication Enrollment

<details>
<summary>Description</summary>

This implementation guide describes the exchange of information needed to dispense specialty medications and enroll patients in associated programs offered by pharmaceutical manufacturers and others. (built Thu, May 4, 2023 14:22+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.specialty-rx#2.0.0`
* `hl7.fhir.us.specialty-rx#2.0.0-ballot`
* `hl7.fhir.us.specialty-rx#1.0.0`
* `hl7.fhir.us.specialty-rx#0.1.0`


---

### surescripts.CareEventNotifications

<details>
<summary>Description</summary>

Put a description here

</details>

**Versions**

* `surescripts.CareEventNotifications#1.0.0-beta`


---

### Canonical Resource Management Infrastructure Implementation Guide

<details>
<summary>Description</summary>

This implementation guide defines profiles, operations, capability statements and guidance to facilitate the content management lifecycle for authoring, publishing, distribution, and implementation of FHIR knowledge artifacts such as value sets, profiles, libraries, rules, and measures. The guide is intended to be used by specification and content implementation guide authors as both a dependency for validation of published artifacts, and a guide for construction and publication of content. (built Fri, May 31, 2024 16:38+0000+00:00)

</details>

**Versions**

* `hl7.fhir.uv.crmi#1.0.0`
* `hl7.fhir.uv.crmi#1.0.0-snapshot`
* `hl7.fhir.uv.crmi#1.0.0-ballot2`
* `hl7.fhir.uv.crmi#1.0.0-ballot`


---

### de.gematik.erezept-patientenrechnung.r4

<details>
<summary>Description</summary>

Abrechnungsinformationen des E-Rezeptes für den Patienten

</details>

**Versions**

* `de.gematik.erezept-patientenrechnung.r4#1.0.3`
* `de.gematik.erezept-patientenrechnung.r4#1.0.2`
* `de.gematik.erezept-patientenrechnung.r4#1.0.1`
* `de.gematik.erezept-patientenrechnung.r4#1.0.0`


---

### Patient Request for Corrections Implementation Guide

<details>
<summary>Description</summary>

The Patient Request for Corrections Implementation Guide provides a method for communicating a patient's request for corrections to their patient data, as well as a way for health care organizations to respond to those requests. (built Tue, Mar 29, 2022 16:29+0000+00:00)

</details>

**Versions**

* `hl7.fhir.uv.patient-corrections#1.0.0-ballot`


---

### Da Vinci - Coverage Requirements Discovery

<details>
<summary>Description</summary>

Guidelines for conveying coverage requirements to clinicians when planning treatment (built Mon, Jan 8, 2024 18:55+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.davinci-crd#2.0.1`
* `hl7.fhir.us.davinci-crd#2.0.0`
* `hl7.fhir.us.davinci-crd#1.1.0-ballot`
* `hl7.fhir.us.davinci-crd#1.0.0`
* `hl7.fhir.us.davinci-crd#0.3.0`
* `hl7.fhir.us.davinci-crd#0.1.0`


---

### myhealthway.main.r4

<details>
<summary>Description</summary>

마이헬스웨이 상세규격

</details>

**Versions**

* `myhealthway.main.r4#0.0.11`
* `myhealthway.main.r4#0.0.10`
* `myhealthway.main.r4#0.0.9`
* `myhealthway.main.r4#0.0.8`
* `myhealthway.main.r4#0.0.7`
* `myhealthway.main.r4#0.0.6`
* `myhealthway.main.r4#0.0.5`
* `myhealthway.main.r4#0.0.4`
* `myhealthway.main.r4#0.0.3`
* `myhealthway.main.r4#0.0.12-beta`


---

### ai4health.it.test.r4

<details>
<summary>Description</summary>

Test FHIR Project

</details>

**Versions**

* `ai4health.it.test.r4#0.0.8`


---

### tiplu.maia.schnittstellendefinition

<details>
<summary>Description</summary>

Grundlage für die FHIR-Schnittstelle der CDS Software MAIA der Tiplu GmbH

</details>

**Versions**

* `tiplu.maia.schnittstellendefinition#1.0.3`
* `tiplu.maia.schnittstellendefinition#1.0.2`


---

### Gestion d'Agendas Partagés (GAP)



**Versions**

* `ans.fhir.fr.gap#3.0.0`


---

### Privacy Consent on FHIR (PCF)

<details>
<summary>Description</summary>

The Privacy Consent on FHIR (PCF) Profile provides support for patient privacy consents and access control where a FHIR API is used to access Document Sharing Health Information Exchanges. (built Thu, Feb 22, 2024 13:50-0600-06:00)

</details>

**Versions**

* `ihe.iti.pcf#1.1.0`
* `ihe.iti.pcf#1.0.0`
* `ihe.iti.pcf#1.0.0-comment`


---

### sk.pnc.r4

<details>
<summary>Description</summary>

Pediatric Neonatal Care profiles

</details>

**Versions**

* `sk.pnc.r4#0.1.0`


---

### PACIO Cognitive Status Implementation Guide

<details>
<summary>Description</summary>

To advance interoperable health data exchange between post-acute care (PAC) and other providers, patients, and key stakeholders (built Wed, Nov 3, 2021 21:43+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.pacio-cs#1.0.0`
* `hl7.fhir.us.pacio-cs#0.1.0`


---

### de.medizininformatikinitiative.kerndatensatz.molgen

<details>
<summary>Description</summary>

Medizininformatik Initiative - Modul Molekulargenetischer Befundbericht

</details>

**Versions**

* `de.medizininformatikinitiative.kerndatensatz.molgen#1.0.0`


---

### cisis.test

<details>
<summary>Description</summary>

test

</details>

**Versions**

* `cisis.test#1.2.0`
* `cisis.test#1.1.0`
* `cisis.test#1.0.0`


---

### test.fhir

<details>
<summary>Description</summary>

测试项目

</details>

**Versions**

* `test.fhir#0.0.1-test`


---

### ca.on.ppr.r4

<details>
<summary>Description</summary>

FHIR-based Implementation Guide for Ontario Provincial Provider Registry System

</details>

**Versions**

* `ca.on.ppr.r4#1.2.0`
* `ca.on.ppr.r4#1.1.0`
* `ca.on.ppr.r4#1.0.0-beta`


---

### US Prescription Drug Monitoring Program (PDMP)

<details>
<summary>Description</summary>

US Prescription Drug Monitoring Program (PDMP) FHIR IG (built Thu, Apr 4, 2024 15:32+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.pdmp#1.0.0-ballot`


---

### CH EMED EPR

<details>
<summary>Description</summary>

FHIR Implementation Guide which defines the documents for the exchange of medication information in the context of the Swiss eMedication service (built Fri, Jan 26, 2024 09:50+0100+01:00)

</details>

**Versions**

* `ch.fhir.ig.ch-emed-epr#1.0.0`


---

### DIFUTURE.trustcenter.sp

<details>
<summary>Description</summary>

FHIR Resources for MII

</details>

**Versions**

* `DIFUTURE.trustcenter.sp#1.0.0-beta-1`


---

### hl7.fhir.r4.id.core

<details>
<summary>Description</summary>

Indonesia Health Service FHIR R4 Package

</details>

**Versions**

* `hl7.fhir.r4.id.core#0.1.0`


---

### de.nfdi4health.mds

<details>
<summary>Description</summary>

NFDI4Health' Metadata Schema V3.3 for clinical, epidemiological and Public Health studies

</details>

**Versions**

* `de.nfdi4health.mds#2.0.0`
* `de.nfdi4health.mds#1.0.0`


---

### Ontario COVaxON FHIR Implementation Guide

<details>
<summary>Description</summary>

Ontario COVaxON FHIR Implementation Guide (built Thu, Jan 27, 2022 21:15-0500-05:00)

</details>

**Versions**

* `ca-on.fhir.ig.covaxon#0.1.10`


---

### FHIR 5.0.0 package : Expansions

<details>
<summary>Description</summary>

Expansions for the 5.0.0 version of the FHIR standard (built Sun, Mar 26, 2023 15:21+1100+11:00)

</details>

**Versions**

* `hl7.fhir.r5.expansions#5.0.0`
* `hl7.fhir.r5.expansions#5.0.0-snapshot3`
* `hl7.fhir.r5.expansions#5.0.0-snapshot1`
* `hl7.fhir.r5.expansions#5.0.0-draft-final`
* `hl7.fhir.r5.expansions#5.0.0-ballot`
* `hl7.fhir.r5.expansions#4.6.0`
* `hl7.fhir.r5.expansions#4.5.0`
* `hl7.fhir.r5.expansions#4.4.0`
* `hl7.fhir.r5.expansions#4.2.0`


---

### minimal Common Oncology Data Elements (mCODE) Implementation Guide

<details>
<summary>Description</summary>

mCODE™ (short for Minimal Common Oncology Data Elements) is an initiative intended to assemble a core set of structured data elements for oncology electronic health records. (built Wed, Apr 10, 2024 13:47+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.mcode#4.0.0-ballot`
* `hl7.fhir.us.mcode#3.0.0`
* `hl7.fhir.us.mcode#3.0.0-ballot`
* `hl7.fhir.us.mcode#2.1.0`
* `hl7.fhir.us.mcode#2.0.0`
* `hl7.fhir.us.mcode#1.16.0`
* `hl7.fhir.us.mcode#1.0.0`
* `hl7.fhir.us.mcode#0.9.1`


---

### Formation.FHIR

<details>
<summary>Description</summary>

Desciption

</details>

**Versions**

* `Formation.FHIR#1.0.1`


---

### healthdata.be.r4.cbb

<details>
<summary>Description</summary>

Clinical building blocks (CBB) and their related HL7 FHIR R4 compliant profiles and related conformance materials for data collections supported by healthdata.be (Sciensano).

</details>

**Versions**

* `healthdata.be.r4.cbb#0.16.0-beta`
* `healthdata.be.r4.cbb#0.15.0-beta`
* `healthdata.be.r4.cbb#0.14.0-beta`
* `healthdata.be.r4.cbb#0.13.0-beta`
* `healthdata.be.r4.cbb#0.10.0-beta`
* `healthdata.be.r4.cbb#0.9.0-alpha`
* `healthdata.be.r4.cbb#0.8.0-alpha`
* `healthdata.be.r4.cbb#0.7.0-alpha`
* `healthdata.be.r4.cbb#0.6.0-alpha2`
* `healthdata.be.r4.cbb#0.5.0-alpha`
* `healthdata.be.r4.cbb#0.4.1-alpha`
* `healthdata.be.r4.cbb#0.4.0-alpha`
* `healthdata.be.r4.cbb#0.3.0-alpha`
* `healthdata.be.r4.cbb#0.2.0-alpha`
* `healthdata.be.r4.cbb#0.1.0-alpha`


---

### Vital Records Common Library (VRCL) STU2-ballot

<details>
<summary>Description</summary>

### Description

This guide is a Fast Healthcare Interoperability Resources (FHIR) Profiles Library to support the needs of multiple vital records implementation guides (IGs). It does not provide any use case (scenario) specific content or additional guidance on how to use these artifacts, but serves as a source for a standard set of profiles for reuse in multiple use case specific IGs focusing on the exchange of vital records information. Implementation details such as how and when to use a given artifact will be supplied in these guides.

### Background
Two FHIR IG projects, [Birth Defects](https://www.hl7.org/special/Committees/projman/searchableProjectIndex.cfm?action=edit&ProjectNumber=1532) and [Birth and Fetal Death Reporting](http://hl7.org/fhir/us/bfdr/), share many common data elements or concepts. The initial scope of this Vital Records Common Profiles Library included data elements common to these two FHIR projects in a US Realm framework. The current update adds data elements shared by two other vital records FHIR IGs, [Vital Records Death Reporting (VRDR)](http://hl7.org/fhir/us/vrdr/), and the [Medicolegal Death Investigation (MDI)](http://hl7.org/fhir/us/mdi/).

Many of the data elements in the vital records IGs (BFDR, VRDR, and this Library) can be identified using the IJE (Inter-Jurisdictional Exchange) data element names (codes). The IJE codes are used for exchange of data among jurisdictions and with authorized data partners, such as NCHS. NCHS has implemented IJE codes for exchange of mortality data with jurisdictions via the VRDR IG, however, the use of IJE codes has not yet been implemented for birth and fetal death reporting to NCHS. [FHIR profile mappings to the IJE format are available for Mortality, Natality, and Fetal Death.](https://github.com/HL7/vr-common-library/raw/master/input/mapping/IJE_File_Layouts_Version_2021_FHIR-2023-02-22-All-Combined.xlsx) The mappings are based on information from: 
 * [200X NCHS Mortality - Demographic File Description](https://www.cdc.gov/nchs/data/dvs/200XMOR_web_with%20clearance%20revisions-acc.pdf)
 * [200X NCHS Natality File Description](https://www.cdc.gov/nchs/data/dvs/200XNAT_web_with%20clearance%20revisions-acc.pdf)
 * [200X NCHS Fetal Death File Description](https://www.cdc.gov/nchs/data/dvs/200XFET_web_with%20clearance%20revisions-acc.pdf)

A FHIR common profiles library avoids defining the same data element multiple times across IGs by allowing each IG to reference them. This profile library will provide a standard framework for vital records information to support interoperability among public health systems and reduce provider and implementer burden. This FHIR IG uses US Core profiles. Where unable to use a US Core profile, we have followed the Cross-Group Projects Work Group's variance request process and have provided the US Realm Steering Committee an approved rationale for deviation in the IG.

For Clinical Safety Information please refer to the [FHIR Implementer’s Safety Checklist](http://hl7.org/fhir/safety.html).

### How to Read This Guide
This Guide is divided into several pages which are listed at the top of each page in the menu bar.

* Home: The introduction and background for HL7® FHIR® Vital Records Common Profiles Library
* [Terminology](terminology.html): A listing of the value sets used in this guide
* [Downloads](downloads.html): Links to downloadable artifacts
* [Change Log](change_log.html): Details of changes made in each version of this IG
* [Artifact Index](artifacts.html): A list of the defined FHIR artifacts (profiles, examples, and value sets) in this guide (built Fri, Oct 6, 2023 12:58+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.vr-common-library#2.0.0-ballot`
* `hl7.fhir.us.vr-common-library#1.1.0`
* `hl7.fhir.us.vr-common-library#1.0.0`
* `hl7.fhir.us.vr-common-library#0.1.0`


---

### de.nichtraucherhelden.export

<details>
<summary>Description</summary>

Spezifikation der DiGa-Exportschnittstelle

</details>

**Versions**

* `de.nichtraucherhelden.export#1.0.0`


---

### de.gkvsv.erezeptabrechnungsdaten

<details>
<summary>Description</summary>

Der Abrechnungsdatensatz zum E-Rezept

</details>

**Versions**

* `de.gkvsv.erezeptabrechnungsdaten#1.4.1`
* `de.gkvsv.erezeptabrechnungsdaten#1.4.0`
* `de.gkvsv.erezeptabrechnungsdaten#1.4.0-rc`
* `de.gkvsv.erezeptabrechnungsdaten#1.3.3`
* `de.gkvsv.erezeptabrechnungsdaten#1.3.2`
* `de.gkvsv.erezeptabrechnungsdaten#1.3.1`
* `de.gkvsv.erezeptabrechnungsdaten#1.3.0`
* `de.gkvsv.erezeptabrechnungsdaten#1.3.0-rc2`
* `de.gkvsv.erezeptabrechnungsdaten#1.3.0-rc1`
* `de.gkvsv.erezeptabrechnungsdaten#1.2.0`
* `de.gkvsv.erezeptabrechnungsdaten#1.2.0-rc3`
* `de.gkvsv.erezeptabrechnungsdaten#1.2.0-rc2`
* `de.gkvsv.erezeptabrechnungsdaten#1.2.0-rc`
* `de.gkvsv.erezeptabrechnungsdaten#1.1.0`
* `de.gkvsv.erezeptabrechnungsdaten#1.1.0-rc`
* `de.gkvsv.erezeptabrechnungsdaten#1.0.6`
* `de.gkvsv.erezeptabrechnungsdaten#1.0.5`
* `de.gkvsv.erezeptabrechnungsdaten#1.0.4`
* `de.gkvsv.erezeptabrechnungsdaten#1.0.3`
* `de.gkvsv.erezeptabrechnungsdaten#1.0.2`
* `de.gkvsv.erezeptabrechnungsdaten#1.0.1`


---

### mes.fhir.fr.mesure

<details>
<summary>Description</summary>

Ce projet regroupe l'ensemble des ressources FHIR utilisées par l'API Mesures de Mon espace santé.

</details>

**Versions**

* `mes.fhir.fr.mesure#1.0.0`


---

### de.gevko.eahb

<details>
<summary>Description</summary>

Antrag auf Anschlussrehabilitation, mit dem Ärztlichen Befundbericht als Anlage.

</details>

**Versions**

* `de.gevko.eahb#0.9.0`


---

### Việt Nam CORE IG (dựa trên phiên bản HL7 FHIR Release 4.0.1) - Draft version

<details>
<summary>Description</summary>

Vietnam CoreData for Interoperability

</details>

**Versions**

* `hl7.fhir.vn.core#1.0.0-beta`


---

### fmcna.caredata.fhir.ig.r4

<details>
<summary>Description</summary>

Fresenius Medical Care Data Model

</details>

**Versions**

* `fmcna.caredata.fhir.ig.r4#1.1.9`
* `fmcna.caredata.fhir.ig.r4#1.1.8`
* `fmcna.caredata.fhir.ig.r4#1.1.7`
* `fmcna.caredata.fhir.ig.r4#1.1.6`
* `fmcna.caredata.fhir.ig.r4#1.1.5`
* `fmcna.caredata.fhir.ig.r4#1.1.4`
* `fmcna.caredata.fhir.ig.r4#1.1.3`
* `fmcna.caredata.fhir.ig.r4#1.1.2`
* `fmcna.caredata.fhir.ig.r4#1.1.1`
* `fmcna.caredata.fhir.ig.r4#0.1.1`


---

### PACIO Functional Status Implementation Guide

<details>
<summary>Description</summary>

To advance interoperable health data exchange between post-acute care (PAC) and other providers, patients, and key stakeholders (built Wed, Nov 3, 2021 21:35+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.pacio-fs#1.0.0`
* `hl7.fhir.us.pacio-fs#0.1.0`


---

### ezFHIR2.pk

<details>
<summary>Description</summary>

ezFHIR

</details>

**Versions**

* `ezFHIR2.pk#2.0.0`


---

### HL7 FHIR Implementation Guide: Transversal Clinical Core

<details>
<summary>Description</summary>

HL7 FHIR Implementation Guide: Transversal Clinical Core (built Tue, May 31, 2022 09:29+0200+02:00)

</details>

**Versions**

* `hl7.fhir.be.core-clinical#1.0.0`


---

### SDOH Clinical Care

<details>
<summary>Description</summary>

This HL7 Implementation Guide (IG) defines how to exchange Social Determinants of Health (SDOH) content defined by the Gravity Project using the HL7 FHIR standard.. (built Thu, Jul 27, 2023 20:34+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.sdoh-clinicalcare#2.1.0`
* `hl7.fhir.us.sdoh-clinicalcare#2.0.0`
* `hl7.fhir.us.sdoh-clinicalcare#1.1.0`
* `hl7.fhir.us.sdoh-clinicalcare#1.0.0`
* `hl7.fhir.us.sdoh-clinicalcare#0.1.0`


---

### Bidirectional Services eReferral (BSeR)

<details>
<summary>Description</summary>

The Bidirectional Services eReferral (BSeR) FHIR implementation guide provides guidance for using the HL7 Fast Healthcare Interoperability Resources (FHIR) standard as an exchange format for clinical and non-clinical service requests. It is a collection of profiled FHIR resources designed for use in information exchanges supporting the placement of a service request by a referral initiating provider and the reporting of service delivery outcomes by a referral recipient provider. The goal of the BSeR project is to streamline and enhance the efficacy of the exchange of health information between health care systems and community services organizations involved in addressing chronic health conditions by establishing information exchange standards for electronic referrals and referral outcome reporting. (built Mon, Mar 2, 2020 22:24+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.bser#2.0.0-ballot`
* `hl7.fhir.us.bser#1.0.0`
* `hl7.fhir.us.bser#0.2.0`


---

### Médicosocial - Suivi Décisions Orientation



**Versions**

* `ans.fhir.fr.sdo#4.0.1`
* `ans.fhir.fr.sdo#4.0.2-ballot-3`
* `ans.fhir.fr.sdo#4.0.2-ballot-2`


---

### Swedish Base Profiles Implementation Guide

<details>
<summary>Description</summary>

Implementation Guide for Swedish Base Profiles (built Wed, Mar 8, 2023 16:20+0100+01:00)

</details>

**Versions**

* `hl7se.fhir.base#1.0.0`


---

### HL7 Belgium Patientwill (Patient Dossier)

<details>
<summary>Description</summary>

Belgian Patient profiles (built Mon, Jan 8, 2024 16:07+0100+01:00)

</details>

**Versions**

* `hl7.fhir.be.patientwill#1.0.0`


---

### Potential Drug-Drug Interaction (PDDI) CDS IG : STU1 Ballot 2

<details>
<summary>Description</summary>

This implementation guide provides conformance requirements and guidance for the representation, distribution, and evaluation of potential drug-drug interactions. (built Mon, Aug 10, 2020 16:56+0000+00:00)

</details>

**Versions**

* `hl7.fhir.uv.pddi#1.0.0-ballot`
* `hl7.fhir.uv.pddi#0.2.0`
* `hl7.fhir.uv.pddi#0.1.0`


---

### de.gevko.evo.khb

<details>
<summary>Description</summary>

Umsetzung der elektronischen Verordnung von Krankenhausbehandlung

</details>

**Versions**

* `de.gevko.evo.khb#0.9.1`
* `de.gevko.evo.khb#0.9.0`


---

### de.abda.eRezeptAbgabedatenPKV

<details>
<summary>Description</summary>

Der PKV-Abgabedatensatz enthält die notwendigen Informationen für die Abrechnung einer elektronischen Arzneimittelverordnung (E-Rezept) für Privatversicherte durch eine Apotheke.

</details>

**Versions**

* `de.abda.eRezeptAbgabedatenPKV#1.1.0-rc1`


---

### US Medication Risk Evaluation and Mitigation Strategies (REMS) FHIR IG

<details>
<summary>Description</summary>

FHIR implementation options and guidance for medication REMS participants (built Fri, Apr 5, 2024 14:52+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.medication-rems#1.0.0-ballot`


---

### US Public Health Profiles Library

<details>
<summary>Description</summary>

The US Public Health Profiles Library is a collection of reusable architecture and content profiles representing common public health concepts and patterns. It is intended as a complement to US Core that is used to ease implementation burden of healthcare organizations, electronic health record companies, public health agencies, and others involved in the US public health endeavor.

The US Public Health Profiles Library will be instituted in close conjunction with US Core and have an analogous process for implementation, moderation, review, and approval. The intent is to re-use US Core profiles wherever possible and only add profiles that are needed for common public health needs. The library will evolve over time and may be supported by an adjunct profiles registry that includes developing and informational profiles for public health use.

After evaluation and harmonization analysis of two large multi-condition and multi-use case public health projects – eCR and MedMorph, it was determined that there are many common elements between the two IGs. The short-term scope of this library  includes elements common to the above-mentioned FHIR IGs and will define a US Realm specific framework that defines common elements for the implementation guides. The longer-term scope of this will include analysis and inclusion of data elements from Vital Records Death Reporting, Vital Records Birth and Fetal Death Reporting, and other Public Health use cases.

To avoid defining the same profiles multiple times, we have created a US Public Health Profiles Library for use by Public Health and other FHIR standards development efforts to define appropriate FHIR profiles, value sets, etc., once and allow them to be referenced by each of the specific implementation guides. This USPHPL will provide a starting point and framework for inclusion in multiple use case specific implementation guides focused on the exchange of Public Health information to support interoperability among public health systems and reduce provider and implementer burden. (built Fri, Aug 5, 2022 18:56+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.ph-library#1.0.0`
* `hl7.fhir.us.ph-library#1.0.0-ballot`


---

### MyDummyProject.01

<details>
<summary>Description</summary>

Some description

</details>

**Versions**

* `MyDummyProject.01#0.0.1`


---

### FHIR implementation of zibs 2020

<details>
<summary>Description</summary>

NL package of FHIR R4 conformance resources for zib (Zorginformatiebouwstenen, Clinical Information Models) release 2020.

</details>

**Versions**

* `nictiz.fhir.nl.r4.zib2020#0.10.0-beta.1`
* `nictiz.fhir.nl.r4.zib2020#0.9.0-beta.1`
* `nictiz.fhir.nl.r4.zib2020#0.8.0-beta.1`
* `nictiz.fhir.nl.r4.zib2020#0.7.0-beta.1`
* `nictiz.fhir.nl.r4.zib2020#0.6.0-beta.2`
* `nictiz.fhir.nl.r4.zib2020#0.5.0-beta1`


---

### IHE FHIR Profile: Occupational Data for Health (ODH) - International

<details>
<summary>Description</summary>

This IG covers the specific data that covers past or present jobs, usual work, employment status, retirement date and combat zone period for the subject. It also includes the past or present jobs and usual work of other household members. This IG is International scope. (built Fri, Mar 8, 2024 10:41-0600-06:00)

</details>

**Versions**

* `ihe.pcc.odh#1.0.0-comment`


---

### CH EMS (R4)



**Versions**

* `ch.fhir.ig.ch-ems#1.9.0`


---

### de.gematik.elektronische-versicherungsbescheinigung

<details>
<summary>Description</summary>

Definition Versichertenstammdaten zum Versand via KIM für GKV VSDM Ersatzbescheinigung und PKV Online Check-In

</details>

**Versions**

* `de.gematik.elektronische-versicherungsbescheinigung#1.0.0`
* `de.gematik.elektronische-versicherungsbescheinigung#1.0.0-rc3`
* `de.gematik.elektronische-versicherungsbescheinigung#1.0.0-rc2`
* `de.gematik.elektronische-versicherungsbescheinigung#1.0.0-rc1`
* `de.gematik.elektronische-versicherungsbescheinigung#0.8.1-beta`
* `de.gematik.elektronische-versicherungsbescheinigung#0.8.0-beta`


---

### HL7 FHIR® Implementation Guide: Electronic Case Reporting (eCR) - US Realm

<details>
<summary>Description</summary>

# Introduction and Purpose

With the adoption and maturing of Electronic Health Records (EHRs) there are opportunities to better support public health surveillance as well as to better support the delivery of relevant public health information to clinical care. Electronic Case Reporting (eCR) can provide more complete and timely case data, support disease / condition monitoring, and assist in outbreak management and control. It can also improve bidirectional communications through the delivery of public health information in the context of a patient's condition and local disease trends and by facilitating ad hoc communications. eCR will also reduce healthcare provider burden by automating the completion of legal reporting requirements.

With the advent of FHIR standards, there is a need for FHIR implementation guidance to specify appropriate resources and transactions needed for the eCR process. FHIR offers opportunities to further enable automated triggering and reporting of cases from EHRs, to ease implementation and integration, to support the acquisition of public health investigation supplemental data, and to connect public health information (e.g., guidelines) with clinical workflows. Over time, FHIR may also support the distribution of reporting rules to clinical care to better align data authorities and make broader clinical data available to public health decision support services inside the clinical care environment.

For more supporting information, use cases, and other background context and material, see Volume 1 of both the [HL7 CDA R2 Electronic Initial Case Report (eICR) Standard for Trial Use (STU) IG](http://www.hl7.org/implement/standards/product_brief.cfm?product_id=436) (see the link under "STU Documents" for STU 2.0) and the [HL7 CDA R2 Reportability Response (RR) STU IG](https://www.hl7.org/implement/standards/product_brief.cfm?product_id=470).

For Clinical Safety Information please refer to the [FHIR Implementer’s Safety Checklist](http://hl7.org/fhir/safety.html).

<div style="background-color: #ffffcc; border-left: 6px solid #ffeb3b;">
  <p><b>Known Issue:</b></p>
  <p>The following issue is related to a publication tooling issue. A technical correction is planned once the tooling issue has been addressed.</p>
  <ul>
    <li><a href="https://jira.hl7.org/browse/FHIR-30477" rel="external" target="_blank">FHIR-30477</a>: Resolution errors for FHIRHelpers and FHIR-ModelInfo.</li>
  </ul>
</div>

# Stakeholders
* Regulatory Agencies
* Standards Development Organizations
* Vendors: EHR, PHR; Health Care IT; Clinical Decision Support Systems, Public Health Surveillance Systems
* Providers: Ambulatory and Healthcare Institutions (hospitals, long term care, mental health)
* Local, State, Tribal and Federal Public Health Agencies (built Thu, Dec 14, 2023 17:36+0000+00:00)

</details>

**Versions**

* `hl7.fhir.us.ecr#2.1.1`
* `hl7.fhir.us.ecr#2.1.0`
* `hl7.fhir.us.ecr#2.0.0`
* `hl7.fhir.us.ecr#1.1.0`
* `hl7.fhir.us.ecr#1.0.0`
* `hl7.fhir.us.ecr#0.1.0`


---

### uk.ads.r4

<details>
<summary>Description</summary>

This package contains additional FHIR assets to support the ADS FHIR Bundle for Bundle 2 data items.

</details>

**Versions**

* `uk.ads.r4#1.2.0-bundle2-alpha`
* `uk.ads.r4#1.1.0-bundle1-alpha`
* `uk.ads.r4#1.0.2-mvp-alpha`
* `uk.ads.r4#1.0.1-mvp-alpha`
* `uk.ads.r4#1.0.0-mvp-alpha`


---

### dk.fhir.ig.kl.common.caresocial

<details>
<summary>Description</summary>

Danish municipalities implementation guide for common informationmodel (built Wed, Oct 28, 2020 11:17+0000+00:00)

</details>

**Versions**

* `dk.fhir.ig.kl.common.caresocial#0.1.7`
* `dk.fhir.ig.kl.common.caresocial#0.1.6`


---

### DK MedCom Carecommunication

<details>
<summary>Description</summary>

The DK MedCom Carecommunication IG (built Wed, Oct 4, 2023 16:20+0200+02:00)

</details>

**Versions**

* `medcom.fhir.dk.carecommunication#3.0.0`
* `medcom.fhir.dk.carecommunication#2.1.0`
* `medcom.fhir.dk.carecommunication#2.0.0`


---

### acme.fsh.example

<details>
<summary>Description</summary>

Example project from ACME Corp on using FHIR Shorthand/sushi

</details>

**Versions**

* `acme.fsh.example#0.0.1-demo`


---

### Allergy (Patient Dossier)

<details>
<summary>Description</summary>

Allergy (Patient Dossier) (built Mon, Dec 18, 2023 11:58+0100+01:00)

</details>

**Versions**

* `hl7.fhir.be.allergy#1.2.0`
* `hl7.fhir.be.allergy#1.1.0`
* `hl7.fhir.be.allergy#1.0.1`
* `hl7.fhir.be.allergy#1.0.0`


---

### touchstone-ereferralontario.core

<details>
<summary>Description</summary>

Ontario-specific business use cases and content

</details>

**Versions**

* `touchstone-ereferralontario.core#0.1.1-beta`


---

### NorthwellHealth.Extensions.Person

<details>
<summary>Description</summary>

Publishing custom Extensions for Person for Northwell Health

</details>

**Versions**

* `NorthwellHealth.Extensions.Person#0.0.1`


---

### Mobile Antepartum Summary US Realm

<details>
<summary>Description</summary>

Mobile Antepartum Summary is a content profile that defines the structure for the aggregation of significant events, diagnoses, and plans of care derived from the visits over the course of an antepartum episode. (built Tue, Jun 4, 2024 16:23-0500-05:00)

</details>

**Versions**

* `ihe.pcc.maps.us#1.0.0-comment`


---

### fhir.dicom



**Versions**

* `fhir.dicom#2023.4.20230907`
* `fhir.dicom#2023.1.20230123`
* `fhir.dicom#2022.4.20221006`
* `fhir.dicom#2021.4.20210910`


---

### Interoperable Digital Identity and Patient Matching



**Versions**

* `hl7.fhir.us.identity-matching#1.0.0`
* `hl7.fhir.us.identity-matching#1.0.0-ballot`


---

### robin.gittest

<details>
<summary>Description</summary>

only testing qsf qssq  ccqc

</details>

**Versions**

* `robin.gittest#1.1.22-beta`
* `robin.gittest#1.1.21-beta`
* `robin.gittest#1.1.1-beta`


