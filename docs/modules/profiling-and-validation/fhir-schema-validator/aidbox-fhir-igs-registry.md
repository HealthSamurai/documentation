---
description: >-
  List of supported Implementation Guides, which synchronise automatically every
  day at 00:00 UTC
---

# Aidbox FHIR IGs Registry

## Configure Aidbox

To begin using FHIR IGs, enable the FHIR Schema validator engine in Aidbox.

* [FHIR Schema Validator](README.md)

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

***

### DK MedCom Core

**Versions**

* `medcom.fhir.dk.core#2.4.0`
* `medcom.fhir.dk.core#2.3.0`
* `medcom.fhir.dk.core#2.2.0`
* `medcom.fhir.dk.core#2.1.0`
* `medcom.fhir.dk.core#2.0.0`

***

### ig.fhir-il-community.org.t17

**Versions**

* `ig.fhir-il-community.org.t17#1.0.0`
* `ig.fhir-il-community.org.t17#0.1.0`

***

### Dental Data Exchange

**Versions**

* `hl7.fhir.us.dental-data-exchange#1.0.0`
* `hl7.fhir.us.dental-data-exchange#0.1.0`

***

### PACIO Transitions of Care Implementation Guide

**Versions**

* `hl7.fhir.us.pacio-toc#1.0.0-ballot`

***

### de.gkvsv.eRezeptAbrechnungsdaten

**Versions**

* `de.gkvsv.eRezeptAbrechnungsdaten#1.0.0`
* `de.gkvsv.eRezeptAbrechnungsdaten#1.0.0-rc`
* `de.gkvsv.eRezeptAbrechnungsdaten#0.9.1-beta`
* `de.gkvsv.eRezeptAbrechnungsdaten#0.9.0-beta`
* `de.gkvsv.eRezeptAbrechnungsdaten#0.2.0-beta`
* `de.gkvsv.eRezeptAbrechnungsdaten#0.1.0-beta`

***

### hl7.fhir.BNW.core

**Versions**

* `hl7.fhir.BNW.core#1.0.0`

***

### logex.internal.fhir.profiles

**Versions**

* `logex.internal.fhir.profiles#1.0.0`

***

### dqa.outburn.r4

**Versions**

* `dqa.outburn.r4#0.0.1`

***

### incisive.fhir

**Versions**

* `incisive.fhir#1.0.1-preview`

***

### uk.nhsdigital.r4.test

**Versions**

* `uk.nhsdigital.r4.test#2.14.0-prerelease`
* `uk.nhsdigital.r4.test#2.11.0-prerelease`
* `uk.nhsdigital.r4.test#2.10.1-prerelease`
* `uk.nhsdigital.r4.test#2.10.0-prerelease`
* `uk.nhsdigital.r4.test#2.9.0-prerelease`
* `uk.nhsdigital.r4.test#2.8.9-prerelease`
* `uk.nhsdigital.r4.test#2.8.8-prerelease`
* `uk.nhsdigital.r4.test#2.8.7-prerelease`
* `uk.nhsdigital.r4.test#2.8.6-prerelease`
* `uk.nhsdigital.r4.test#2.8.5-prerelease`
* `uk.nhsdigital.r4.test#2.8.21-prerelease`
* `uk.nhsdigital.r4.test#2.8.20-prerelease`
* `uk.nhsdigital.r4.test#2.8.19-prerelease`
* `uk.nhsdigital.r4.test#2.8.18-prerelease`
* `uk.nhsdigital.r4.test#2.8.17-prerelease`
* `uk.nhsdigital.r4.test#2.8.16-prerelease`
* `uk.nhsdigital.r4.test#2.8.15-prerelease`
* `uk.nhsdigital.r4.test#2.8.14-prerelease`
* `uk.nhsdigital.r4.test#2.8.13-prerelease`
* `uk.nhsdigital.r4.test#2.8.12-prerelease`
* `uk.nhsdigital.r4.test#2.8.11-prerelease`
* `uk.nhsdigital.r4.test#2.8.10-prerelease`

***

### dvmd.kdl.r4

**Versions**

* `dvmd.kdl.r4#2025.0.0`
* `dvmd.kdl.r4#2024.0.0`
* `dvmd.kdl.r4#2024.0.0-qa3`
* `dvmd.kdl.r4#2024.0.0-qa2`
* `dvmd.kdl.r4#2024.0.0-qa`
* `dvmd.kdl.r4#2023.0.1`
* `dvmd.kdl.r4#2023.0.0`

***

### HL7 Laboratory Report

**Versions**

* `hl7.fhir.uv.lab-report#1.0.0-ballot`

***

### Guía de Implementación Core-CL FHIR R4 (Standard Trial For Use (STU))

**Versions**

* `hl7.fhir.cl.CoreCH#1.0.0`

***

### ontariomicdrfhirimplementationguide-0.10.00

**Versions**

* `ontariomicdrfhirimplementationguide-0.10.00#0.9.14`
* `ontariomicdrfhirimplementationguide-0.10.00#0.9.13`
* `ontariomicdrfhirimplementationguide-0.10.00#0.9.12`
* `ontariomicdrfhirimplementationguide-0.10.00#0.9.11`
* `ontariomicdrfhirimplementationguide-0.10.00#0.9.9-beta`
* `ontariomicdrfhirimplementationguide-0.10.00#0.9.8-beta`
* `ontariomicdrfhirimplementationguide-0.10.00#0.9.7-beta`
* `ontariomicdrfhirimplementationguide-0.10.00#0.9.6-beta`
* `ontariomicdrfhirimplementationguide-0.10.00#0.9.5-beta`
* `ontariomicdrfhirimplementationguide-0.10.00#0.9.4-beta`
* `ontariomicdrfhirimplementationguide-0.10.00#0.9.3-beta`
* `ontariomicdrfhirimplementationguide-0.10.00#0.9.10-beta`
* `ontariomicdrfhirimplementationguide-0.10.00#0.9.1-beta`
* `ontariomicdrfhirimplementationguide-0.10.00#0.9.0-beta`

***

### ths-greifswald.ttp-fhir-gw

**Versions**

* `ths-greifswald.ttp-fhir-gw#2025.1.0-DEVEL.1`
* `ths-greifswald.ttp-fhir-gw#2024.3.0-DEVEL.1`
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

***

### de.abda.erezeptabgabedaten

**Versions**

* `de.abda.erezeptabgabedaten#1.5.0`
* `de.abda.erezeptabgabedaten#1.5.0-rc2`
* `de.abda.erezeptabgabedaten#1.5.0-rc1`
* `de.abda.erezeptabgabedaten#1.5.0-rc`
* `de.abda.erezeptabgabedaten#1.4.2`
* `de.abda.erezeptabgabedaten#1.4.1`
* `de.abda.erezeptabgabedaten#1.4.0`
* `de.abda.erezeptabgabedaten#1.4.1-rc`
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

***

### de.gematik.ti

**Versions**

* `de.gematik.ti#1.0.0`

***

### Respiratory Virus Hospitalization Surveillance Network (RESP-NET) Content Implementation Guide

**Versions**

* `hl7.fhir.us.resp-net#1.0.0-ballot`

***

### gen.kanta.r4

**Versions**

* `gen.kanta.r4#0.9.1`

***

### package.test.new

**Versions**

* `package.test.new#2.0.0`

***

### KBV.Basis

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

***

### FHIR for FAIR - FHIR Implementation Guide

**Versions**

* `hl7.fhir.uv.fhir-for-fair#1.0.0`
* `hl7.fhir.uv.fhir-for-fair#0.1.0`

***

### Guide d'implémentation FHIR - Mesures de santé

**Versions**

* `ans.fhir.fr.mesures#3.1.0`
* `ans.fhir.fr.mesures#3.1.0-ballot`
* `ans.fhir.fr.mesures#3.0.1`
* `ans.fhir.fr.mesures#3.0.0`

***

### ca.on.oh-seris

**Versions**

* `ca.on.oh-seris#0.10.0-alpha1.0.5`
* `ca.on.oh-seris#0.10.0-alpha1.0.4`
* `ca.on.oh-seris#0.10.0-alpha1.0.3`
* `ca.on.oh-seris#0.10.0-alpha1.0.2`
* `ca.on.oh-seris#0.10.0-alpha1.0.1`
* `ca.on.oh-seris#0.9.0-alpha1.0.1`

***

### acme.product.r4

**Versions**

* `acme.product.r4#3.0.0`
* `acme.product.r4#2.0.0`
* `acme.product.r4#1.0.0`
* `acme.product.r4#1.0.0-alpha`

***

### FHIRcast

**Versions**

* `hl7.fhir.uv.fhircast#3.0.0-ballot`
* `hl7.fhir.uv.fhircast#2.1.0-ballot`

***

### de.gematik.erp-servicerequest

**Versions**

* `de.gematik.erp-servicerequest#1.2.0-rc2`
* `de.gematik.erp-servicerequest#1.2.0-rc1`
* `de.gematik.erp-servicerequest#1.1.0`
* `de.gematik.erp-servicerequest#1.0.1`
* `de.gematik.erp-servicerequest#1.0.0`

***

### Clinical Order Workflows

**Versions**

* `hl7.fhir.uv.cow#1.0.0-ballot`

***

### Electronic Long-Term Services and Supports (eLTSS) Release 1 - US Realm

**Versions**

* `hl7.fhir.us.eltss#2.0.0`
* `hl7.fhir.us.eltss#2.0.0-ballot`
* `hl7.fhir.us.eltss#1.0.0`
* `hl7.fhir.us.eltss#0.1.0`

***

### vzvz.fhir.nl-vzvz-core

**Versions**

* `vzvz.fhir.nl-vzvz-core#0.3.2`
* `vzvz.fhir.nl-vzvz-core#0.3.1`
* `vzvz.fhir.nl-vzvz-core#0.3.0`
* `vzvz.fhir.nl-vzvz-core#0.2.2`
* `vzvz.fhir.nl-vzvz-core#0.2.1`
* `vzvz.fhir.nl-vzvz-core#0.1.1`
* `vzvz.fhir.nl-vzvz-core#0.1.0-beta`
* `vzvz.fhir.nl-vzvz-core#0.1.0-beta-2`

***

### Touchstone-ereferralontario.core

**Versions**

* `Touchstone-ereferralontario.core#0.1.0-beta`

***

### WorkshopJena3.v1

**Versions**

* `WorkshopJena3.v1#0.0.1-beta`

***

### uk.nhsdigital.bars.r4

**Versions**

* `uk.nhsdigital.bars.r4#1.35.0`
* `uk.nhsdigital.bars.r4#1.34.0`
* `uk.nhsdigital.bars.r4#1.33.0`
* `uk.nhsdigital.bars.r4#1.32.0`
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

***

### 臺灣核心實作指引(TW Core IG)

**Versions**

* `tw.gov.mohw.twcore#0.3.2`
* `tw.gov.mohw.twcore#0.3.1`
* `tw.gov.mohw.twcore#0.3.0`
* `tw.gov.mohw.twcore#0.2.2`
* `tw.gov.mohw.twcore#0.2.1`
* `tw.gov.mohw.twcore#0.2.0`
* `tw.gov.mohw.twcore#0.1.1`
* `tw.gov.mohw.twcore#0.1.0`

***

### dk.4s-online.raplito

**Versions**

* `dk.4s-online.raplito#0.1.2`
* `dk.4s-online.raplito#0.1.1`
* `dk.4s-online.raplito#0.1.0`

***

### FHIR Tooling Extensions IG

**Versions**

* `hl7.fhir.uv.tools#0.5.0`
* `hl7.fhir.uv.tools#0.4.1`
* `hl7.fhir.uv.tools#0.4.0`
* `hl7.fhir.uv.tools#0.3.0`
* `hl7.fhir.uv.tools#0.2.0`
* `hl7.fhir.uv.tools#0.1.0`

***

### lung.ca.screen.assignment

**Versions**

* `lung.ca.screen.assignment#1.0.1`
* `lung.ca.screen.assignment#1.0.0`

***

### Validated Healthcare Directory

**Versions**

* `hl7.fhir.uv.vhdir#1.0.0`
* `hl7.fhir.uv.vhdir#0.2.0`
* `hl7.fhir.uv.vhdir#0.1.0`

***

### ufp.core

**Versions**

* `ufp.core#0.6.0`
* `ufp.core#0.5.1`
* `ufp.core#0.4.0`

***

### MII IG Pathologie

**Versions**

* `de.medizininformatikinitiative.kerndatensatz.patho#2025.0.2`
* `de.medizininformatikinitiative.kerndatensatz.patho#2025.0.1`
* `de.medizininformatikinitiative.kerndatensatz.patho#2025.0.0-alpha3`
* `de.medizininformatikinitiative.kerndatensatz.patho#2025.0.0-alpha2`
* `de.medizininformatikinitiative.kerndatensatz.patho#2025.0.0-alpha1`
* `de.medizininformatikinitiative.kerndatensatz.patho#1.0.0`
* `de.medizininformatikinitiative.kerndatensatz.patho#0.9.0`

***

### MCC eCare Plan Implementation Guide

**Versions**

* `hl7.fhir.us.mcc#1.0.0`
* `hl7.fhir.us.mcc#1.0.0-ballot2`
* `hl7.fhir.us.mcc#1.0.0-ballot`

***

### CarePlanRT.Eng

**Versions**

* `CarePlanRT.Eng#1.0.0`

***

### junk.sample-preview

**Versions**

* `junk.sample-preview#2.0.3`
* `junk.sample-preview#2.0.2`
* `junk.sample-preview#2.0.1`

***

### ee.hl7.fhir.base.r4

**Versions**

* `ee.hl7.fhir.base.r4#1.0.4-beta`
* `ee.hl7.fhir.base.r4#1.0.3-beta`
* `ee.hl7.fhir.base.r4#1.0.2-beta`
* `ee.hl7.fhir.base.r4#1.0.1-beta`
* `ee.hl7.fhir.base.r4#1.0.0-beta`

***

### Patient.Hospice-LOC

**Versions**

* `Patient.Hospice-LOC#1.0.1`

***

### HL7 BE Laboratory WG Implementation Guide

**Versions**

* `hl7.fhir.be.lab#1.0.0`

***

### ca.infoway.io.core

**Versions**

* `ca.infoway.io.core#1.0.0-dft-preballot`
* `ca.infoway.io.core#1.0.0-dft-preballot-beta2`
* `ca.infoway.io.core#1.0.0-dft-preballot-beta-01`
* `ca.infoway.io.core#0.3.1-dft-ballot`
* `ca.infoway.io.core#0.3.1-dft-ballot-alpha`
* `ca.infoway.io.core#0.3.0-dft-preballot`
* `ca.infoway.io.core#0.3.0-dft-ballot-2`
* `ca.infoway.io.core#0.3.0-dft-ballot-1`
* `ca.infoway.io.core#0.2.0-dft`
* `ca.infoway.io.core#0.2.0-dft-ballot`
* `ca.infoway.io.core#0.1.0-dft`
* `ca.infoway.io.core#0.1.0-DFT-Ballot`
* `ca.infoway.io.core#0.1.0-DFT-Ballot-pre`

***

### CardX Hypertension Management

**Versions**

* `hl7.fhir.uv.cardx-htn-mng#1.0.0`
* `hl7.fhir.uv.cardx-htn-mng#1.0.0-ballot`

***

### IHE FormatCode Vocabulary

**Versions**

* `ihe.formatcode.fhir#1.3.0`
* `ihe.formatcode.fhir#1.2.0`
* `ihe.formatcode.fhir#1.1.1`
* `ihe.formatcode.fhir#1.1.0`
* `ihe.formatcode.fhir#1.0.0`
* `ihe.formatcode.fhir#0.2.4`
* `ihe.formatcode.fhir#0.2.2`

***

### DK MedCom HospitalNotification

**Versions**

* `medcom.fhir.dk.hospitalnotification#3.0.1`
* `medcom.fhir.dk.hospitalnotification#3.0.0`
* `medcom.fhir.dk.hospitalnotification#2.0.0`

***

### Guide d'implémentation Fr Core

**Versions**

* `hl7.fhir.fr.core#2.1.0`
* `hl7.fhir.fr.core#2.1.0-ballot`
* `hl7.fhir.fr.core#2.0.1`
* `hl7.fhir.fr.core#2.0.0`
* `hl7.fhir.fr.core#2.0.0-ballot`

***

### US-Medication FHIR IG

**Versions**

* `hl7.fhir.us.meds#1.2.0`
* `hl7.fhir.us.meds#1.1.0`
* `hl7.fhir.us.meds#1.0.0`
* `hl7.fhir.us.meds#0.0.1`

***

### Nictiz profiling guidelines for FHIR R4

**Versions**

* `nictiz.fhir.nl.r4.profilingguidelines#0.9.0`
* `nictiz.fhir.nl.r4.profilingguidelines#0.8.0`
* `nictiz.fhir.nl.r4.profilingguidelines#0.7.0`
* `nictiz.fhir.nl.r4.profilingguidelines#0.6.0`
* `nictiz.fhir.nl.r4.profilingguidelines#0.5.0`
* `nictiz.fhir.nl.r4.profilingguidelines#0.4.0`
* `nictiz.fhir.nl.r4.profilingguidelines#0.3.0`
* `nictiz.fhir.nl.r4.profilingguidelines#0.2.0`

***

### rl.fhir.r4.draft

**Versions**

* `rl.fhir.r4.draft#0.1.3`
* `rl.fhir.r4.draft#0.1.2`
* `rl.fhir.r4.draft#0.1.1`
* `rl.fhir.r4.draft#0.1.0`
* `rl.fhir.r4.draft#0.0.23`
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

***

### Annuaire Santé

**Versions**

* `ans.fhir.fr.annuaire#1.1.0`
* `ans.fhir.fr.annuaire#1.1.0-snapshot`
* `ans.fhir.fr.annuaire#1.1.0-snapshot-6`
* `ans.fhir.fr.annuaire#1.1.0-snapshot-5`
* `ans.fhir.fr.annuaire#1.1.0-snapshot-4`
* `ans.fhir.fr.annuaire#1.1.0-snapshot-3`
* `ans.fhir.fr.annuaire#1.1.0-snapshot-2`
* `ans.fhir.fr.annuaire#1.0.1`
* `ans.fhir.fr.annuaire#1.0.0`
* `ans.fhir.fr.annuaire#1.0.0-ballot`
* `ans.fhir.fr.annuaire#1.0.0-ballot-4`
* `ans.fhir.fr.annuaire#1.0.0-ballot-3`
* `ans.fhir.fr.annuaire#1.0.0-ballot-2`
* `ans.fhir.fr.annuaire#0.1.0`

***

### FHIR R3 package : Expansions

**Versions**

* `hl7.fhir.r3.expansions#3.0.2`

***

### dguv.basis

**Versions**

* `dguv.basis#1.2.0`
* `dguv.basis#1.1.0`
* `dguv.basis#1.1.1-Kommentierung`

***

### US Core Implementation Guide

**Versions**

* `hl7.fhir.us.core#8.0.0-ballot`
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

***

### FHIR implementation of iWlz

**Versions**

* `nictiz.fhir.nl.stu3.iwlz#1.0.0-poc8`
* `nictiz.fhir.nl.stu3.iwlz#1.0.0-poc7`
* `nictiz.fhir.nl.stu3.iwlz#1.0.0-poc6`
* `nictiz.fhir.nl.stu3.iwlz#1.0.0-poc5`
* `nictiz.fhir.nl.stu3.iwlz#1.0.0-poc4`
* `nictiz.fhir.nl.stu3.iwlz#1.0.0-poc2`

***

### beact.es.publishingtutorial

**Versions**

* `beact.es.publishingtutorial#0.0.1-test`

***

### FHIR R3 package : Core

**Versions**

* `hl7.fhir.r3.core#3.0.2`

***

### rapportendoscopiequebec.test

**Versions**

* `rapportendoscopiequebec.test#0.0.69`
* `rapportendoscopiequebec.test#0.0.68`
* `rapportendoscopiequebec.test#0.0.67`
* `rapportendoscopiequebec.test#0.0.66`
* `rapportendoscopiequebec.test#0.0.65`
* `rapportendoscopiequebec.test#0.0.64`
* `rapportendoscopiequebec.test#0.0.63`
* `rapportendoscopiequebec.test#0.0.62`
* `rapportendoscopiequebec.test#0.0.61`
* `rapportendoscopiequebec.test#0.0.60`
* `rapportendoscopiequebec.test#0.0.59`
* `rapportendoscopiequebec.test#0.0.58`
* `rapportendoscopiequebec.test#0.0.57`
* `rapportendoscopiequebec.test#0.0.56`
* `rapportendoscopiequebec.test#0.0.55`
* `rapportendoscopiequebec.test#0.0.54`
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
* `rapportendoscopiequebec.test#0.0.4`
* `rapportendoscopiequebec.test#0.0.3`
* `rapportendoscopiequebec.test#0.0.2`

***

### de.medizininformatikinitiative.use-case.omi

**Versions**

* `de.medizininformatikinitiative.use-case.omi#0.3.9`
* `de.medizininformatikinitiative.use-case.omi#0.3.8`
* `de.medizininformatikinitiative.use-case.omi#0.3.7`
* `de.medizininformatikinitiative.use-case.omi#0.3.5`
* `de.medizininformatikinitiative.use-case.omi#0.3.4`
* `de.medizininformatikinitiative.use-case.omi#0.3.3`
* `de.medizininformatikinitiative.use-case.omi#0.3.1`
* `de.medizininformatikinitiative.use-case.omi#0.3.0`
* `de.medizininformatikinitiative.use-case.omi#0.2.1`
* `de.medizininformatikinitiative.use-case.omi#0.2.0`
* `de.medizininformatikinitiative.use-case.omi#0.1.0-beta2`

***

### CH Core (R4)

**Versions**

* `ch.fhir.ig.ch-core#5.0.0`
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

***

### ca-on-covax.v1

**Versions**

* `ca-on-covax.v1#1.0.0`

***

### devdays.r4.example.conformanceresources

**Versions**

* `devdays.r4.example.conformanceresources#1.0.0`
* `devdays.r4.example.conformanceresources#1.0.0-beta`

***

### de.kvtelematik.eterminservice.r4

**Versions**

* `de.kvtelematik.eterminservice.r4#0.1.1`
* `de.kvtelematik.eterminservice.r4#0.1.0`

***

### RCPA Cancer Protocols IG

**Versions**

* `hl7.fhir.au.rcpa#0.1.0`

***

### Patient Identifier Cross-referencing for mobile (PIXm)

**Versions**

* `IHE.ITI.PIXm#3.0.0`
* `IHE.ITI.PIXm#2.2.0`

***

### MIO.KBV.Mutterpass

**Versions**

* `MIO.KBV.Mutterpass#1.0.0`

***

### de.abda.eRezeptAbgabedatenBasis

**Versions**

* `de.abda.eRezeptAbgabedatenBasis#1.1.0-rc1`

***

### HL7 FHIR® Implementation Guide: Ophthalmology Retinal, Release 1

**Versions**

* `hl7.fhir.uv.eyecare#0.1.0`

***

### Bulk Data Access IG

**Versions**

* `hl7.fhir.uv.bulkdata#2.0.0`
* `hl7.fhir.uv.bulkdata#1.1.0`
* `hl7.fhir.uv.bulkdata#1.0.1`
* `hl7.fhir.uv.bulkdata#1.0.0`

***

### ca.on.mi.pkg

**Versions**

* `ca.on.mi.pkg#0.9.0-beta-previewf`
* `ca.on.mi.pkg#0.9.0-beta-2-testing`
* `ca.on.mi.pkg#0.9.0-beta-1-testing`

***

### de.gematik.epa

**Versions**

* `de.gematik.epa#1.0.5-ballot.1`
* `de.gematik.epa#1.1.0`
* `de.gematik.epa#1.1.0-rc1`
* `de.gematik.epa#1.0.5`
* `de.gematik.epa#1.0.2`
* `de.gematik.epa#1.0.1`
* `de.gematik.epa#1.0.0`
* `de.gematik.epa#0.0.3`

***

### ANS.annuaire.fhir.r4

**Versions**

* `ANS.annuaire.fhir.r4#0.2.0`

***

### National Healthcare Safety Network (NHSN) Digital Quality Measure (dQM) Reporting Implementation Guide

**Versions**

* `hl7.fhir.us.nhsn-dqm#1.0.0-ballot`

***

### socialcaredataservice.stu3

**Versions**

* `socialcaredataservice.stu3#0.0.3`
* `socialcaredataservice.stu3#0.0.2`

***

### devdays.letsbuildafhirspec.simplifier1

**Versions**

* `devdays.letsbuildafhirspec.simplifier1#0.0.3-devdaysus2021`

***

### At-Home In-Vitro Test Report

**Versions**

* `hl7.fhir.us.home-lab-report#1.1.0`
* `hl7.fhir.us.home-lab-report#1.0.0`
* `hl7.fhir.us.home-lab-report#1.0.0-ballot`

***

### MedNet interface implementation guide

**Versions**

* `swiss.mednet.fhir#0.16.0`
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

***

### ca.on.health.sadie

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

***

### dvmd.kdl.r4.2020

**Versions**

* `dvmd.kdl.r4.2020#2020.1.0`

***

### Consumer Real-Time Pharmacy Benefit Check

**Versions**

* `hl7.fhir.us.carin-rtpbc#1.0.0`
* `hl7.fhir.us.carin-rtpbc#0.1.0`

***

### il.core.fhir.r4

**Versions**

* `il.core.fhir.r4#0.17.2`
* `il.core.fhir.r4#0.17.1`
* `il.core.fhir.r4#0.17.0`
* `il.core.fhir.r4#0.17.1-params`
* `il.core.fhir.r4#0.16.2`
* `il.core.fhir.r4#0.16.1`
* `il.core.fhir.r4#0.16.0`
* `il.core.fhir.r4#0.15.4`
* `il.core.fhir.r4#0.15.3`
* `il.core.fhir.r4#0.15.2`
* `il.core.fhir.r4#0.15.1`
* `il.core.fhir.r4#0.15.0`
* `il.core.fhir.r4#0.14.3`
* `il.core.fhir.r4#0.14.2`
* `il.core.fhir.r4#0.14.1`
* `il.core.fhir.r4#0.14.0`
* `il.core.fhir.r4#0.13.2`
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

***

### acme.usecase.r4

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

***

### HL7® Austria FHIR® Core Implementation Guide

**Versions**

* `hl7.at.fhir.core.r4#2.0.0`

***

### Standardized Medication Profile (SMP) FHIR IG

**Versions**

* `hl7.fhir.us.smp#1.0.0-ballot`

***

### SFM.030521

**Versions**

* `SFM.030521#1.0.0`

***

### National Directory of Healthcare Providers & Services (NDH) Implementation Guide

**Versions**

* `hl7.fhir.us.ndh#1.0.0`
* `hl7.fhir.us.ndh#1.0.0-ballot`

***

### FHIR R4 package : Expansions

**Versions**

* `hl7.fhir.r4.expansions#4.0.1`

***

### Cahier De Liaison

**Versions**

* `ans.fhir.fr.cdl#3.0.0`

***

### healthdata.be.r4.laboratorytestresult

**Versions**

* `healthdata.be.r4.laboratorytestresult#0.5.0-beta`
* `healthdata.be.r4.laboratorytestresult#0.4.0-beta`
* `healthdata.be.r4.laboratorytestresult#0.3.0-alpha`
* `healthdata.be.r4.laboratorytestresult#0.2.0-alpha`
* `healthdata.be.r4.laboratorytestresult#0.1.0`

***

### basisprofil.tiplu.de.r4

**Versions**

* `basisprofil.tiplu.de.r4#1.1.3`
* `basisprofil.tiplu.de.r4#1.1.2`
* `basisprofil.tiplu.de.r4#1.1.1`
* `basisprofil.tiplu.de.r4#1.1.0`

***

### OntarioacCDRFHIRImplementationGuide-0.10.00

**Versions**

* `OntarioacCDRFHIRImplementationGuide-0.10.00#0.1.0`

***

### AU Base Implementation Guide

**Versions**

* `at.unofficial.au.base#2.2.0-ballot2`
* `at.unofficial.au.base#2.2.0-ballot`

***

### SMART Health Cards and Links FHIR IG

**Versions**

* `hl7.fhir.uv.smart-health-cards-and-links#1.0.0-ballot`

***

### CH EPR PPQm (R4)

**Versions**

* `ch.fhir.ig.ch-epr-ppqm#2.0.0`
* `ch.fhir.ig.ch-epr-ppqm#2.0.0-ballot`
* `ch.fhir.ig.ch-epr-ppqm#0.2.0`
* `ch.fhir.ig.ch-epr-ppqm#0.1.0`

***

### d3b.kidsfirst.r4

**Versions**

* `d3b.kidsfirst.r4#0.1.0`

***

### TInterop.23

**Versions**

* `TInterop.23#1.0.0-alpha`

***

### Arkhn.core

**Versions**

* `Arkhn.core#0.0.2`

***

### CH eTOC (R4)

**Versions**

* `ch.fhir.ig.ch-etoc#3.0.0`
* `ch.fhir.ig.ch-etoc#3.0.0-ballot`
* `ch.fhir.ig.ch-etoc#2.0.1`
* `ch.fhir.ig.ch-etoc#2.0.0`
* `ch.fhir.ig.ch-etoc#2.0.0-ballot`
* `ch.fhir.ig.ch-etoc#1.0.0`
* `ch.fhir.ig.ch-etoc#0.1.0`

***

### Patsientide üldandmete teenus / Master Patient Index

**Versions**

* `hl7.fhir.ee.mpi#1.0.0`

***

### de.dktk.oncology

**Versions**

* `de.dktk.oncology#1.8.0`
* `de.dktk.oncology#1.7.0`
* `de.dktk.oncology#1.6.0`
* `de.dktk.oncology#1.5.0`
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

***

### de.gevko.evo.ekb

**Versions**

* `de.gevko.evo.ekb#1.0.0`
* `de.gevko.evo.ekb#0.9.0`

***

### kl.dk.fhir.rehab

**Versions**

* `kl.dk.fhir.rehab#2.2.0`
* `kl.dk.fhir.rehab#2.1.1`
* `kl.dk.fhir.rehab#2.1.0`
* `kl.dk.fhir.rehab#2.0.0`
* `kl.dk.fhir.rehab#1.0.0`

***

### MII IG Meta

**Versions**

* `de.medizininformatikinitiative.kerndatensatz.meta#2025.0.0-rc.1`
* `de.medizininformatikinitiative.kerndatensatz.meta#2025.0.0-alpha.0`
* `de.medizininformatikinitiative.kerndatensatz.meta#2025.0.0`
* `de.medizininformatikinitiative.kerndatensatz.meta#2025.0.0-alpha4`
* `de.medizininformatikinitiative.kerndatensatz.meta#2025.0.0-alpha3`
* `de.medizininformatikinitiative.kerndatensatz.meta#2025.0.0-alpha2`
* `de.medizininformatikinitiative.kerndatensatz.meta#2025.0.0-alpha1`
* `de.medizininformatikinitiative.kerndatensatz.meta#1.0.3`

***

### cezih.osnova

**Versions**

* `cezih.osnova#0.2.3`

***

### surescripts.RecordLocatorExchange

**Versions**

* `surescripts.RecordLocatorExchange#1.0.0-beta`

***

### kbv.basis.with.expansion

**Versions**

* `kbv.basis.with.expansion#1.7.0`

***

### International Patient Access

**Versions**

* `hl7.fhir.uv.ipa#1.1.0`
* `hl7.fhir.uv.ipa#1.0.0`
* `hl7.fhir.uv.ipa#0.1.0`

***

### LOINC – IVD Test Code (LIVD) Mapping

**Versions**

* `hl7.fhir.uv.livd#1.0.0-ballot`
* `hl7.fhir.uv.livd#0.3.0`
* `hl7.fhir.uv.livd#0.2.0`
* `hl7.fhir.uv.livd#0.1.0`

***

### Implementation Guide for fælleskommunal informationsmodel

**Versions**

* `kl.dk.fhir.core#1.2.0`

***

### CH ATC (R4)

**Versions**

* `ch.fhir.ig.ch-atc#3.3.0`
* `ch.fhir.ig.ch-atc#3.3.0-ballot`
* `ch.fhir.ig.ch-atc#3.2.0`
* `ch.fhir.ig.ch-atc#3.2.0-ballot`
* `ch.fhir.ig.ch-atc#3.1.0`

***

### Nictiz FHIR NL STU3 Images

**Versions**

* `nictiz.fhir.nl.stu3.images#1.0.3`
* `nictiz.fhir.nl.stu3.images#1.0.2`
* `nictiz.fhir.nl.stu3.images#1.0.1`
* `nictiz.fhir.nl.stu3.images#1.0.0`

***

### MII IG Fall

**Versions**

* `de.medizininformatikinitiative.kerndatensatz.fall#2025.0.0-rc.1`
* `de.medizininformatikinitiative.kerndatensatz.fall#2025.0.0`
* `de.medizininformatikinitiative.kerndatensatz.fall#2025.0.0-alpha4`
* `de.medizininformatikinitiative.kerndatensatz.fall#2025.0.0-alpha3`
* `de.medizininformatikinitiative.kerndatensatz.fall#2025.0.0-alpha2`
* `de.medizininformatikinitiative.kerndatensatz.fall#2025.0.0-alpha1`
* `de.medizininformatikinitiative.kerndatensatz.fall#2024.1.0-alpha2`
* `de.medizininformatikinitiative.kerndatensatz.fall#2024.1.0-alpha1`
* `de.medizininformatikinitiative.kerndatensatz.fall#2024.0.1`
* `de.medizininformatikinitiative.kerndatensatz.fall#2024.0.0`
* `de.medizininformatikinitiative.kerndatensatz.fall#2024.0.0-ballot`
* `de.medizininformatikinitiative.kerndatensatz.fall#2024.0.0-alpha6`
* `de.medizininformatikinitiative.kerndatensatz.fall#1.0.1`

***

### Central Cancer Registry Reporting Content IG

**Versions**

* `hl7.fhir.us.central-cancer-registry-reporting#1.0.0`
* `hl7.fhir.us.central-cancer-registry-reporting#0.1.0`

***

### Mobile access to Health Documents (MHD)

**Versions**

* `ihe.iti.mhd#4.2.2`
* `ihe.iti.mhd#4.2.1`
* `ihe.iti.mhd#4.2.0`
* `ihe.iti.mhd#4.2.0-ballot`
* `ihe.iti.mhd#4.1.0`

***

### first.package

**Versions**

* `first.package#0.0.1`

***

### ca.on.oh.patient-summary

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

***

### com.hl7.cyb.r5

**Versions**

* `com.hl7.cyb.r5#0.1.11`
* `com.hl7.cyb.r5#0.1.10`
* `com.hl7.cyb.r5#0.1.9`
* `com.hl7.cyb.r5#0.1.8`
* `com.hl7.cyb.r5#0.1.7`
* `com.hl7.cyb.r5#0.1.6`
* `com.hl7.cyb.r5#0.1.5`
* `com.hl7.cyb.r5#0.1.4`
* `com.hl7.cyb.r5#0.1.3`
* `com.hl7.cyb.r5#0.1.2`
* `com.hl7.cyb.r5#0.1.0`

***

### who.ved

**Versions**

* `who.ved#4.0.0`

***

### Cercle De Soins

**Versions**

* `ans.fhir.fr.cds#2.0.0`
* `ans.fhir.fr.cds#2.0.0-ballot`

***

### eRS.r4.assets

**Versions**

* `eRS.r4.assets#1.0.0`

***

### smart4health.eu.core

**Versions**

* `smart4health.eu.core#0.5.2`
* `smart4health.eu.core#0.5.1`
* `smart4health.eu.core#0.5.0`
* `smart4health.eu.core#0.4.0`
* `smart4health.eu.core#0.3.0`
* `smart4health.eu.core#0.2.2`
* `smart4health.eu.core#0.2.1`
* `smart4health.eu.core#0.2.0`
* `smart4health.eu.core#0.1.1`
* `smart4health.eu.core#0.1.0`

***

### rl.fhir.r4

**Versions**

* `rl.fhir.r4#0.0.16`
* `rl.fhir.r4#0.0.15`
* `rl.fhir.r4#0.0.14`
* `rl.fhir.r4#0.0.13`
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

***

### Finnish Scheduling

**Versions**

* `hl7.fhir.fi.scheduling#2.0.0-rc1`
* `hl7.fhir.fi.scheduling#0.3.1`
* `hl7.fhir.fi.scheduling#0.2.0`

***

### kbv.mio.mutterpass

**Versions**

* `kbv.mio.mutterpass#1.1.0`
* `kbv.mio.mutterpass#1.1.0-kommentierungsphase`
* `kbv.mio.mutterpass#1.1.0-benehmensherstellung`

***

### de.gematik.erg

**Versions**

* `de.gematik.erg#1.1.0-rc2`
* `de.gematik.erg#1.1.0-rc1`
* `de.gematik.erg#1.0.0-rc1`
* `de.gematik.erg#1.0.0-CC`

***

### ForgePatientChart.0830

**Versions**

* `ForgePatientChart.0830#0.1.0`

***

### test.project1

**Versions**

* `test.project1#0.2.0`

***

### de.gematik.erezept-workflow.r4

**Versions**

* `de.gematik.erezept-workflow.r4#1.5.0`
* `de.gematik.erezept-workflow.r4#1.5.0-rc3`
* `de.gematik.erezept-workflow.r4#1.5.0-rc2`
* `de.gematik.erezept-workflow.r4#1.5.0-rc1`
* `de.gematik.erezept-workflow.r4#1.5.0-beta7`
* `de.gematik.erezept-workflow.r4#1.5.0-beta6`
* `de.gematik.erezept-workflow.r4#1.5.0-beta5`
* `de.gematik.erezept-workflow.r4#1.5.0-beta4`
* `de.gematik.erezept-workflow.r4#1.5.0-beta10`
* `de.gematik.erezept-workflow.r4#1.4.3`
* `de.gematik.erezept-workflow.r4#1.4.2`
* `de.gematik.erezept-workflow.r4#1.4.1`
* `de.gematik.erezept-workflow.r4#1.4.0`
* `de.gematik.erezept-workflow.r4#1.4.2-rc2`
* `de.gematik.erezept-workflow.r4#1.4.2-rc1`
* `de.gematik.erezept-workflow.r4#1.4.0-rc3`
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

***

### de.medizininformatikinitiative.kerndatensatz.mikrobiologie

**Versions**

* `de.medizininformatikinitiative.kerndatensatz.mikrobiologie#2025.0.1`
* `de.medizininformatikinitiative.kerndatensatz.mikrobiologie#2025.0.0`
* `de.medizininformatikinitiative.kerndatensatz.mikrobiologie#2024.0.0`

***

### PACIO Personal Functioning and Engagement Implementation Guide

**Versions**

* `hl7.fhir.us.pacio-pfe#2.0.0-ballot`
* `hl7.fhir.us.pacio-pfe#1.0.0`
* `hl7.fhir.us.pacio-pfe#1.0.0-ballot`

***

### FHIR.DGMC

**Versions**

* `FHIR.DGMC#0.1.0`

***

### Clinical Practice Guidelines (CPG) on EBMonFHIR

**Versions**

* `ebm-cpg.netzwerk-universitaetsmedizin.de#0.6.0`

***

### ntt.healthgenig

**Versions**

* `ntt.healthgenig#0.0.4`
* `ntt.healthgenig#0.0.3`
* `ntt.healthgenig#0.0.2`

***

### test.prova

**Versions**

* `test.prova#1.0.0`

***

### ans.annuaire.fhir.r4

**Versions**

* `ans.annuaire.fhir.r4#0.2.0-pat23`

***

### DL.base

**Versions**

* `DL.base#1.1.0`

***

### rlfhir.lab

**Versions**

* `rlfhir.lab#1.0.0`

***

### Physical Activity Implementation Guide

**Versions**

* `hl7.fhir.us.physical-activity#1.0.0`
* `hl7.fhir.us.physical-activity#1.0.0-ballot`

***

### NRLF.poc

**Versions**

* `NRLF.poc#1.0.1`

***

### Clinical Practice Guidelines

**Versions**

* `hl7.fhir.uv.cpg#2.0.0`
* `hl7.fhir.uv.cpg#2.0.0-ballot`
* `hl7.fhir.uv.cpg#1.0.0`
* `hl7.fhir.uv.cpg#0.1.0`

***

### eHealth Platform Federal infsec Profiles

**Versions**

* `hl7.fhir.be.infsec#1.2.0`
* `hl7.fhir.be.infsec#1.1.0`
* `hl7.fhir.be.infsec#1.0.0`

***

### de.gematik.erezept.eu

**Versions**

* `de.gematik.erezept.eu#1.0.0-rc4`
* `de.gematik.erezept.eu#1.0.0-rc3`
* `de.gematik.erezept.eu#1.0.0-rc2`
* `de.gematik.erezept.eu#1.0.0-rc1`
* `de.gematik.erezept.eu#0.9.1-beta`
* `de.gematik.erezept.eu#0.9.0-beta`

***

### sfm.030521

**Versions**

* `sfm.030521#1.0.1`

***

### Guía de Implementación Receta-CL

**Versions**

* `hl7.fhir.cl.recetachile#0.9`

***

### il.hdp.fhir.r4

**Versions**

* `il.hdp.fhir.r4#0.0.1`

***

### eu.epi.february2025b

**Versions**

* `eu.epi.february2025b#1.0.0`

***

### kbv.ita.erp

**Versions**

* `kbv.ita.erp#1.2.0`
* `kbv.ita.erp#1.2.0-alpha`
* `kbv.ita.erp#1.1.2`
* `kbv.ita.erp#1.1.1`
* `kbv.ita.erp#1.1.0`
* `kbv.ita.erp#1.1.0-PreRelease`
* `kbv.ita.erp#1.0.2`

***

### QI-Core Implementation Guide

**Versions**

* `hl7.fhir.us.qicore#7.0.0-ballot`
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

***

### HIE2.packages

**Versions**

* `HIE2.packages#0.1.1`

***

### BCPatientSummary.Draft

**Versions**

* `BCPatientSummary.Draft#0.0.1-draft`

***

### cezih.hr.condition-management

**Versions**

* `cezih.hr.condition-management#0.2.1`

***

### phis.ig.dev

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

***

### difuture.trustcenter.sp

**Versions**

* `difuture.trustcenter.sp#1.0.0-beta-3`
* `difuture.trustcenter.sp#1.0.0-beta-2`

***

### test.module3.v2

**Versions**

* `test.module3.v2#2.0.0`

***

### amwell.fhir.profiles

**Versions**

* `amwell.fhir.profiles#2.0.0`
* `amwell.fhir.profiles#1.0.1-preview`
* `amwell.fhir.profiles#1.0.0-preview`

***

### FHIR Tooling Extensions IG

**Versions**

* `hl7.fhir.uv.tools.r3#0.5.0`
* `hl7.fhir.uv.tools.r3#0.4.1`
* `hl7.fhir.uv.tools.r3#0.4.0`
* `hl7.fhir.uv.tools.r3#0.3.0`

***

### de.aqua.ebi

**Versions**

* `de.aqua.ebi#0.9.0`

***

### KBV.ITA.ERP

**Versions**

* `KBV.ITA.ERP#1.0.1`
* `KBV.ITA.ERP#1.0.0`

***

### National Healthcare Directory Exchange

**Versions**

* `hl7.fhir.us.directory-exchange#1.0.0-ballot`

***

### Non-patient File Sharing (NPFS)

**Versions**

* `ihe.iti.npfs#2.2.0`
* `ihe.iti.npfs#2.2.0-comment`

***

### ee.tehik.mpi

**Versions**

* `ee.tehik.mpi#0.1.0-beta`

***

### hie2.packages

**Versions**

* `hie2.packages#1.0.1`

***

### KBV.MIO.Impfpass

**Versions**

* `KBV.MIO.Impfpass#1.1.0-Kommentierung`
* `KBV.MIO.Impfpass#1.00.000`

***

### kbv.basis.terminology

**Versions**

* `kbv.basis.terminology#1.7.1`
* `kbv.basis.terminology#1.7.0`
* `kbv.basis.terminology#1.7.1-Expansions`
* `kbv.basis.terminology#1.7.0-Expansions`

***

### HL7 FHIR Profile: Occupational Data for Health (ODH), Release 1, STU 1.3 (Standard for Trial Use)

**Versions**

* `hl7.fhir.us.odh#1.3.0`
* `hl7.fhir.us.odh#1.2.0`
* `hl7.fhir.us.odh#1.1.0`
* `hl7.fhir.us.odh#1.0.0`
* `hl7.fhir.us.odh#0.1.0`

***

### ma.ambulanter.fall

**Versions**

* `ma.ambulanter.fall#1.0.6`
* `ma.ambulanter.fall#1.0.5`
* `ma.ambulanter.fall#1.0.4`
* `ma.ambulanter.fall#1.0.3`
* `ma.ambulanter.fall#1.0.2`
* `ma.ambulanter.fall#1.0.1`
* `ma.ambulanter.fall#1.0.0`

***

### basisprofil.de

**Versions**

* `basisprofil.de#0.2.40`
* `basisprofil.de#0.2.30`

***

### Sharing Valuesets, Codes, and Maps (SVCM)

**Versions**

* `ihe.iti.svcm#1.5.1`
* `ihe.iti.svcm#1.5.0`
* `ihe.iti.svcm#1.4.0`

***

### de.thieme-compliance.anamnese

**Versions**

* `de.thieme-compliance.anamnese#0.0.1-dev`

***

### Nictiz FHIR NL STU3 Zib2017

**Versions**

* `nictiz.fhir.nl.stu3.zib2017#2.2.20`
* `nictiz.fhir.nl.stu3.zib2017#2.2.18`
* `nictiz.fhir.nl.stu3.zib2017#2.2.17`
* `nictiz.fhir.nl.stu3.zib2017#2.2.16`
* `nictiz.fhir.nl.stu3.zib2017#2.2.15`
* `nictiz.fhir.nl.stu3.zib2017#2.2.14`
* `nictiz.fhir.nl.stu3.zib2017#2.2.13`
* `nictiz.fhir.nl.stu3.zib2017#2.2.12`
* `nictiz.fhir.nl.stu3.zib2017#2.2.11`
* `nictiz.fhir.nl.stu3.zib2017#2.2.10`
* `nictiz.fhir.nl.stu3.zib2017#2.2.9`
* `nictiz.fhir.nl.stu3.zib2017#2.2.8`
* `nictiz.fhir.nl.stu3.zib2017#2.2.7`
* `nictiz.fhir.nl.stu3.zib2017#2.2.6`
* `nictiz.fhir.nl.stu3.zib2017#2.2.5`
* `nictiz.fhir.nl.stu3.zib2017#2.2.4`
* `nictiz.fhir.nl.stu3.zib2017#2.2.3`
* `nictiz.fhir.nl.stu3.zib2017#2.2.1`
* `nictiz.fhir.nl.stu3.zib2017#2.2.0`
* `nictiz.fhir.nl.stu3.zib2017#2.1.12`
* `nictiz.fhir.nl.stu3.zib2017#2.1.11`
* `nictiz.fhir.nl.stu3.zib2017#2.1.10`
* `nictiz.fhir.nl.stu3.zib2017#2.1.9`
* `nictiz.fhir.nl.stu3.zib2017#2.1.8`
* `nictiz.fhir.nl.stu3.zib2017#2.1.7`
* `nictiz.fhir.nl.stu3.zib2017#2.1.6`
* `nictiz.fhir.nl.stu3.zib2017#2.1.5`
* `nictiz.fhir.nl.stu3.zib2017#2.1.4`
* `nictiz.fhir.nl.stu3.zib2017#2.1.3`
* `nictiz.fhir.nl.stu3.zib2017#2.1.1`
* `nictiz.fhir.nl.stu3.zib2017#2.1.0`
* `nictiz.fhir.nl.stu3.zib2017#2.0.6`
* `nictiz.fhir.nl.stu3.zib2017#2.0.5`
* `nictiz.fhir.nl.stu3.zib2017#2.0.4`
* `nictiz.fhir.nl.stu3.zib2017#2.0.3`
* `nictiz.fhir.nl.stu3.zib2017#2.0.1`
* `nictiz.fhir.nl.stu3.zib2017#2.0.0`
* `nictiz.fhir.nl.stu3.zib2017#1.3.19`
* `nictiz.fhir.nl.stu3.zib2017#1.3.18`
* `nictiz.fhir.nl.stu3.zib2017#1.3.17`
* `nictiz.fhir.nl.stu3.zib2017#1.3.16`
* `nictiz.fhir.nl.stu3.zib2017#1.3.15`
* `nictiz.fhir.nl.stu3.zib2017#1.3.14`
* `nictiz.fhir.nl.stu3.zib2017#1.3.13`
* `nictiz.fhir.nl.stu3.zib2017#1.3.12`
* `nictiz.fhir.nl.stu3.zib2017#1.3.11`
* `nictiz.fhir.nl.stu3.zib2017#1.3.10`
* `nictiz.fhir.nl.stu3.zib2017#1.3.9`
* `nictiz.fhir.nl.stu3.zib2017#1.3.6`
* `nictiz.fhir.nl.stu3.zib2017#1.3.5`
* `nictiz.fhir.nl.stu3.zib2017#1.3.4`
* `nictiz.fhir.nl.stu3.zib2017#1.3.3`
* `nictiz.fhir.nl.stu3.zib2017#1.3.2`
* `nictiz.fhir.nl.stu3.zib2017#1.3.1`
* `nictiz.fhir.nl.stu3.zib2017#1.3.0`
* `nictiz.fhir.nl.stu3.zib2017#1.2.0`
* `nictiz.fhir.nl.stu3.zib2017#1.1.4`
* `nictiz.fhir.nl.stu3.zib2017#1.1.3`
* `nictiz.fhir.nl.stu3.zib2017#1.1.2`
* `nictiz.fhir.nl.stu3.zib2017#1.1.1`
* `nictiz.fhir.nl.stu3.zib2017#1.1.0`
* `nictiz.fhir.nl.stu3.zib2017#1.0.0`

***

### NHSN Healthcare Associated Infection (HAI) Reports Long Term Care Facilities

**Versions**

* `hl7.fhir.us.hai-ltcf#1.1.0`
* `hl7.fhir.us.hai-ltcf#1.0.0`
* `hl7.fhir.us.hai-ltcf#0.1.0`

***

### marburg.schulung

**Versions**

* `marburg.schulung#1.0.0`

***

### FHIR implementation of ELZ

**Versions**

* `nictiz.fhir.nl.r4.elz#0.2.0-beta.1`
* `nictiz.fhir.nl.r4.elz#0.1.0-alfa.2`
* `nictiz.fhir.nl.r4.elz#0.1.0-alfa.1`

***

### healthhub.fhir

**Versions**

* `healthhub.fhir#2.0.4`
* `healthhub.fhir#2.0.3`
* `healthhub.fhir#2.0.2`
* `healthhub.fhir#2.0.1`
* `healthhub.fhir#2.0.0`
* `healthhub.fhir#1.0.2`
* `healthhub.fhir#1.0.1`
* `healthhub.fhir#1.0.0`

***

### FHIR Application Feature Framework Implementation Guide

**Versions**

* `hl7.fhir.uv.application-feature#1.0.0-ballot`

***

### kbv.fhir.vos

**Versions**

* `kbv.fhir.vos#1.30.0`

***

### STB.ACTIVE

**Versions**

* `STB.ACTIVE#1.43.0`

***

### de.gevko.dev.tetvz

**Versions**

* `de.gevko.dev.tetvz#1.1.2`
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

***

### kbv.ita.eau

**Versions**

* `kbv.ita.eau#1.2.0`
* `kbv.ita.eau#1.1.1`
* `kbv.ita.eau#1.1.0`
* `kbv.ita.eau#1.1.0-PreRelease`

***

### FHIR implementation of CiO

**Versions**

* `nictiz.fhir.nl.r4.cio#1.0.0-beta.5`
* `nictiz.fhir.nl.r4.cio#1.0.0-beta.4`
* `nictiz.fhir.nl.r4.cio#1.0.0-beta.3`
* `nictiz.fhir.nl.r4.cio#1.0.0-beta.2`
* `nictiz.fhir.nl.r4.cio#1.0.0-beta.1`

***

### de.gematik.dev.terminology

**Versions**

* `de.gematik.dev.terminology#1.0.5-alpha.6`
* `de.gematik.dev.terminology#1.0.5-alpha.5`
* `de.gematik.dev.terminology#1.0.5-alpha.4`
* `de.gematik.dev.terminology#1.0.1-alpha.3`
* `de.gematik.dev.terminology#1.0.1-alpha.1`

***

### Health New Zealand Te Whatu Ora Digital Tooling Implementation Guide

**Versions**

* `tewhatuora.digitaltooling.ig#0.0.3`
* `tewhatuora.digitaltooling.ig#0.0.2`

***

### Quality Measure Implementation Guide

**Versions**

* `hl7.fhir.us.cqfmeasures#5.0.0`
* `hl7.fhir.us.cqfmeasures#5.0.0-ballot2`
* `hl7.fhir.us.cqfmeasures#5.0.0-ballot`
* `hl7.fhir.us.cqfmeasures#4.0.0`
* `hl7.fhir.us.cqfmeasures#4.0.0-ballot`
* `hl7.fhir.us.cqfmeasures#3.0.0`
* `hl7.fhir.us.cqfmeasures#2.1.0`
* `hl7.fhir.us.cqfmeasures#2.0.0`
* `hl7.fhir.us.cqfmeasures#1.1.0`
* `hl7.fhir.us.cqfmeasures#1.0.0`
* `hl7.fhir.us.cqfmeasures#0.1.0`

***

### resource.versioning

**Versions**

* `resource.versioning#0.2.0-beta`
* `resource.versioning#0.1.0-alpha`

***

### supportedhospitaldischarge.stu3

**Versions**

* `supportedhospitaldischarge.stu3#0.1.5`
* `supportedhospitaldischarge.stu3#0.1.4`
* `supportedhospitaldischarge.stu3#0.1.3`

***

### Integrated Reporting Applications

**Versions**

* `ihe.rad.ira#1.0.0`
* `ihe.rad.ira#1.0.0-comment`

***

### Pharmacist Services and Summaries - FHIR (PhCP)

**Versions**

* `hl7.fhir.us.phcp#1.0.0`
* `hl7.fhir.us.phcp#0.2.0`
* `hl7.fhir.us.phcp#0.1.0`

***

### ca.on.oh.mha-pds

**Versions**

* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.38`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.37`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.36`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.35`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.34`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.33`
* `ca.on.oh.mha-pds#0.9.0-alpha-1.9.7.32`
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

***

### Point-of-Care Device Implementation Guide

**Versions**

* `hl7.fhir.uv.pocd#0.3.0`
* `hl7.fhir.uv.pocd#0.2.0`

***

### kbv.ita.aws

**Versions**

* `kbv.ita.aws#1.2.0`

***

### de.medizininformatikinitiative.kerndatensatz.consent

**Versions**

* `de.medizininformatikinitiative.kerndatensatz.consent#2025.0.1`
* `de.medizininformatikinitiative.kerndatensatz.consent#2025.0.0`
* `de.medizininformatikinitiative.kerndatensatz.consent#2025.0.0-alpha`
* `de.medizininformatikinitiative.kerndatensatz.consent#1.0.7`
* `de.medizininformatikinitiative.kerndatensatz.consent#1.0.6`
* `de.medizininformatikinitiative.kerndatensatz.consent#1.0.5`
* `de.medizininformatikinitiative.kerndatensatz.consent#1.0.4`
* `de.medizininformatikinitiative.kerndatensatz.consent#1.0.3`
* `de.medizininformatikinitiative.kerndatensatz.consent#1.0.2`
* `de.medizininformatikinitiative.kerndatensatz.consent#1.0.1`
* `de.medizininformatikinitiative.kerndatensatz.consent#1.0.0-ballot1`

***

### Common Data Models Harmonization

**Versions**

* `hl7.fhir.us.cdmh#1.0.0`

***

### SMART Web Messaging

**Versions**

* `hl7.fhir.uv.smart-web-messaging#1.0.0`

***

### Single Institutional Review Board (sIRB) Implementation Guide

**Versions**

* `hl7.fhir.us.sirb#1.0.0`
* `hl7.fhir.us.sirb#0.1.0`

***

### Structured Data Capture - Data Element Exchange Specification

**Versions**

* `hl7.fhir.us.sdcde#2.0`
* `hl7.fhir.us.sdcde#1.6`

***

### il.lab.tasmc.fhir.r4

**Versions**

* `il.lab.tasmc.fhir.r4#0.1.3`
* `il.lab.tasmc.fhir.r4#0.1.2`
* `il.lab.tasmc.fhir.r4#0.1.1`
* `il.lab.tasmc.fhir.r4#0.1.0`

***

### de.gematik.fhir.atf

**Versions**

* `de.gematik.fhir.atf#1.4.0-rc2`
* `de.gematik.fhir.atf#1.4.0-rc1`
* `de.gematik.fhir.atf#1.3.0`
* `de.gematik.fhir.atf#1.2.0`
* `de.gematik.fhir.atf#1.1.0`
* `de.gematik.fhir.atf#1.0.4`
* `de.gematik.fhir.atf#1.0.3`
* `de.gematik.fhir.atf#1.0.2`
* `de.gematik.fhir.atf#1.0.1`
* `de.gematik.fhir.atf#1.0.0`

***

### Pseudo.Frog

**Versions**

* `Pseudo.Frog#1.0.0`

***

### test.colo.qc

**Versions**

* `test.colo.qc#0.0.2`
* `test.colo.qc#0.0.1`

***

### CH EMED (R4)

**Versions**

* `ch.fhir.ig.ch-emed#5.0.0`
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

***

### signal.core.r4

**Versions**

* `signal.core.r4#0.2.10`
* `signal.core.r4#0.2.9`
* `signal.core.r4#0.2.8`
* `signal.core.r4#0.2.7`
* `signal.core.r4#0.2.6`
* `signal.core.r4#0.2.5`
* `signal.core.r4#0.2.4`
* `signal.core.r4#0.2.3`
* `signal.core.r4#0.2.2`
* `signal.core.r4#0.2.1`
* `signal.core.r4#0.2.0`
* `signal.core.r4#0.1.4`
* `signal.core.r4#0.1.3-preview-20230707`
* `signal.core.r4#0.1.2-preview-20230628`
* `signal.core.r4#0.1.1-beta-1`

***

### PGO.02

**Versions**

* `PGO.02#1.02.1`

***

### fume.outburn.r4

**Versions**

* `fume.outburn.r4#0.1.1`
* `fume.outburn.r4#0.1.0`

***

### kbv.mio.diga

**Versions**

* `kbv.mio.diga#1.1.0`
* `kbv.mio.diga#1.1.0-kommentierung`
* `kbv.mio.diga#1.1.0-benehmensherstellung`
* `kbv.mio.diga#1.0.0-festlegung`
* `kbv.mio.diga#1.0.0-benehmensherstellung`

***

### FHIR Extensions Pack

**Versions**

* `hl7.fhir.uv.extensions.r5#5.2.0`
* `hl7.fhir.uv.extensions.r5#5.1.0`
* `hl7.fhir.uv.extensions.r5#1.0.0`

***

### de.basisprofil.r4

**Versions**

* `de.basisprofil.r4#1.5.3`
* `de.basisprofil.r4#1.5.2`
* `de.basisprofil.r4#1.5.1`
* `de.basisprofil.r4#1.5.0`
* `de.basisprofil.r4#1.5.1-beta2`
* `de.basisprofil.r4#1.5.1-beta1`
* `de.basisprofil.r4#1.5.0-ballot3`
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

***

### Implementierungsleitfaden DEMIS - Erkrankungsmeldung

**Versions**

* `rki.demis.disease#2.2.0`
* `rki.demis.disease#2.0.2`
* `rki.demis.disease#2.0.1`
* `rki.demis.disease#2.0.0`
* `rki.demis.disease#1.4.0`
* `rki.demis.disease#1.3.0`
* `rki.demis.disease#1.2.0`
* `rki.demis.disease#1.1.0`

***

### stb.combined

**Versions**

* `stb.combined#1.32.0`
* `stb.combined#1.31.0`
* `stb.combined#1.20.0`
* `stb.combined#1.19.0`
* `stb.combined#1.18.0`

***

### Order Catalog Implementation Guide

**Versions**

* `hl7.fhir.uv.order-catalog#0.1.0`

***

### WHO Digital Documentation of COVID-19 Certificates (DDCC)

**Versions**

* `who.ddcc#1.0.1`
* `who.ddcc#1.0.0`

***

### de.gecco

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

***

### geosalud.UY.implementationGuide

**Versions**

* `geosalud.UY.implementationGuide#0.0.1-preview`

***

### de.gkvsv.evp

**Versions**

* `de.gkvsv.evp#1.0.1`
* `de.gkvsv.evp#1.0.0`
* `de.gkvsv.evp#1.0.0-rc1`

***

### ca.ab.fhir.psab

**Versions**

* `ca.ab.fhir.psab#1.1.0`
* `ca.ab.fhir.psab#1.1.0-pre4`
* `ca.ab.fhir.psab#1.1.0-pre3`
* `ca.ab.fhir.psab#1.1.0-pre2`
* `ca.ab.fhir.psab#1.1.0-pre1`
* `ca.ab.fhir.psab#1.0.1`
* `ca.ab.fhir.psab#1.0.0`
* `ca.ab.fhir.psab#0.3.0-pre9`
* `ca.ab.fhir.psab#0.3.0-pre8`
* `ca.ab.fhir.psab#0.3.0-pre7`
* `ca.ab.fhir.psab#0.3.0-pre6`
* `ca.ab.fhir.psab#0.3.0-pre5`
* `ca.ab.fhir.psab#0.3.0-pre4`
* `ca.ab.fhir.psab#0.3.0-pre3`
* `ca.ab.fhir.psab#0.3.0-pre2`
* `ca.ab.fhir.psab#0.3.0-pre14`
* `ca.ab.fhir.psab#0.3.0-pre13`
* `ca.ab.fhir.psab#0.3.0-pre12`
* `ca.ab.fhir.psab#0.3.0-pre11`
* `ca.ab.fhir.psab#0.3.0-pre10`
* `ca.ab.fhir.psab#0.3.0-pre1`

***

### Package.JSON

**Versions**

* `Package.JSON#1.0.0`

***

### FHIR Shorthand

**Versions**

* `hl7.fhir.uv.shorthand#1.0.0`
* `hl7.fhir.uv.shorthand#0.12.0`

***

### NHSD.Assets.STU3

**Versions**

* `NHSD.Assets.STU3#1.6.0`
* `NHSD.Assets.STU3#1.5.0`
* `NHSD.Assets.STU3#1.4.0`
* `NHSD.Assets.STU3#1.3.0`
* `NHSD.Assets.STU3#1.2.0`
* `NHSD.Assets.STU3#1.1.0`
* `NHSD.Assets.STU3#1.0.0`

***

### kvdigital.terminschnittstelle-fuer-dritte

**Versions**

* `kvdigital.terminschnittstelle-fuer-dritte#1.0.0`

***

### NTT.HealthgenIG

**Versions**

* `NTT.HealthgenIG#0.0.1`

***

### ans.cisis.nde.fhir.r4

**Versions**

* `ans.cisis.nde.fhir.r4#1.0.0`

***

### ishmed.i14y.r4.de

**Versions**

* `ishmed.i14y.r4.de#3.0.0`
* `ishmed.i14y.r4.de#2.0.0`

***

### Implementation Guide CHMED16AF

**Versions**

* `ch.mediplan.chmed16af#0.2.0`

***

### de.gevko.evo.hlm

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

***

### Clinical Study Schedule of Activities

**Versions**

* `hl7.fhir.uv.vulcan-schedule#1.0.0`
* `hl7.fhir.uv.vulcan-schedule#1.0.0-ballot`

***

### phis.ig.createtest

**Versions**

* `phis.ig.createtest#0.0.2`

***

### myhie.v4

**Versions**

* `myhie.v4#1.0.0`

***

### eReferralOntarioNew.core

**Versions**

* `eReferralOntarioNew.core#0.10.0`

***

### sunway.hie.r4

**Versions**

* `sunway.hie.r4#0.0.1-pilot`

***

### Application Data Exchange Assessment Framework and Functional Requirements for Mobile Health

**Versions**

* `hl7.fhir.uv.mhealth-framework#0.1.0`

***

### 臺灣傳染病檢驗報告實作指引

**Versions**

* `tw.gov.mohw.cdc.twidir#0.1.1`
* `tw.gov.mohw.cdc.twidir#0.1.0`

***

### Test.Luxottica2

**Versions**

* `Test.Luxottica2#0.0.1-beta`

***

### Test.Template

**Versions**

* `Test.Template#0.0.1`

***

### NHSN Reporting: Adverse Drug Events - Hypoglycemia

**Versions**

* `hl7.fhir.us.nhsn-ade#1.0.0`
* `hl7.fhir.us.nhsn-ade#0.1.0`

***

### pcr.r4.1-1-0-pkg

**Versions**

* `pcr.r4.1-1-0-pkg#1.2.5`
* `pcr.r4.1-1-0-pkg#1.2.0`

***

### Continuous Glucose Monitoring

**Versions**

* `hl7.fhir.uv.cgm#1.0.0-ballot`

***

### Finnish Base Profiles

**Versions**

* `hl7.fhir.fi.base#2.0.0-rc1`
* `hl7.fhir.fi.base#1.0.0`
* `hl7.fhir.fi.base#1.0.0-rc24`
* `hl7.fhir.fi.base#1.0.0-rc23`
* `hl7.fhir.fi.base#1.0.0-rc22`
* `hl7.fhir.fi.base#1.0.0-rc21`
* `hl7.fhir.fi.base#1.0.0-rc20`
* `hl7.fhir.fi.base#1.0.0-rc19`
* `hl7.fhir.fi.base#1.0.0-rc18`
* `hl7.fhir.fi.base#1.0.0-rc17`

***

### Womens Health Technology Coordinated Registry Network

**Versions**

* `hl7.fhir.us.womens-health-registries#0.2.0`
* `hl7.fhir.us.womens-health-registries#0.1.0`

***

### hsos.eWundbericht

**Versions**

* `hsos.eWundbericht#0.9.0`

***

### healthdata.be.r4.clinicalreportresearch

**Versions**

* `healthdata.be.r4.clinicalreportresearch#0.2.0-alpha`
* `healthdata.be.r4.clinicalreportresearch#0.1.0-alpha`

***

### dw.gardenia

**Versions**

* `dw.gardenia#0.2.0`

***

### International Birth And Child Model Implementation Guide

**Versions**

* `hl7.fhir.uv.ibcm#1.0.0-ballot2`
* `hl7.fhir.uv.ibcm#1.0.0-ballot`

***

### test.public.project.pvt.package

**Versions**

* `test.public.project.pvt.package#0.0.4`

***

### ontario-mha-old-v0.9-alpha

**Versions**

* `ontario-mha-old-v0.9-alpha#0.9.1-alpha-test`

***

### FHIR Core package

**Versions**

* `hl7.fhir.r6.core#6.0.0-ballot3`
* `hl7.fhir.r6.core#6.0.0-ballot2`
* `hl7.fhir.r6.core#6.0.0-ballot1`

***

### pbm.V1.fhir

**Versions**

* `pbm.V1.fhir#0.5.0-draft`

***

### dvmd.kdl.r4.2019

**Versions**

* `dvmd.kdl.r4.2019#2019.1.0`

***

### Kontaktregister.Profiles

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

***

### Medicolegal Death Investigation (MDI) FHIR Implementation Guide

**Versions**

* `hl7.fhir.us.mdi#2.0.0-snapshot2`
* `hl7.fhir.us.mdi#2.0.0-snapshot1`
* `hl7.fhir.us.mdi#2.0.0-ballot2`
* `hl7.fhir.us.mdi#2.0.0-ballot`
* `hl7.fhir.us.mdi#1.1.0`
* `hl7.fhir.us.mdi#1.0.0`
* `hl7.fhir.us.mdi#1.0.0-ballot`

***

### de.ihe-d.terminology

**Versions**

* `de.ihe-d.terminology#3.0.1`
* `de.ihe-d.terminology#3.0.0`
* `de.ihe-d.terminology#3.0.0-alpha2`

***

### HL7 Terminology (THO)

**Versions**

* `hl7.terminology.r4b#6.0.2`
* `hl7.terminology.r4b#6.0.1`
* `hl7.terminology.r4b#6.0.0`
* `hl7.terminology.r4b#5.5.0`
* `hl7.terminology.r4b#5.4.0`
* `hl7.terminology.r4b#5.3.0`
* `hl7.terminology.r4b#5.2.0`
* `hl7.terminology.r4b#5.1.0`
* `hl7.terminology.r4b#5.0.0`
* `hl7.terminology.r4b#4.0.0`
* `hl7.terminology.r4b#3.1.0`
* `hl7.terminology.r4b#3.0.0`
* `hl7.terminology.r4b#2.1.0`
* `hl7.terminology.r4b#2.0.0`
* `hl7.terminology.r4b#1.0.0`

***

### CH IG (R4)

**Versions**

* `ch.fhir.ig.ch-ig#1.2.1`
* `ch.fhir.ig.ch-ig#1.2.0`

***

### SMART App Launch

**Versions**

* `hl7.fhir.uv.smart-app-launch#2.2.0`
* `hl7.fhir.uv.smart-app-launch#2.2.0-ballot`
* `hl7.fhir.uv.smart-app-launch#2.1.0`
* `hl7.fhir.uv.smart-app-launch#2.1.0-ballot`
* `hl7.fhir.uv.smart-app-launch#1.1.0`
* `hl7.fhir.uv.smart-app-launch#1.0.0`

***

### Mobile Care Services Discovery (mCSD)

**Versions**

* `ihe.iti.mcsd#4.0.0-comment`
* `ihe.iti.mcsd#3.9.0`
* `ihe.iti.mcsd#3.8.0`
* `ihe.iti.mcsd#3.7.0`
* `ihe.iti.mcsd#3.6.1`
* `ihe.iti.mcsd#3.5.0`
* `ihe.iti.mcsd#3.4.0`

***

### myhie.v4-test

**Versions**

* `myhie.v4-test#2.1.1`
* `myhie.v4-test#0.0.1-alpha`

***

### Evidence Based Medicine on FHIR Implementation Guide

**Versions**

* `hl7.fhir.uv.ebm#1.0.0-ballot2`
* `hl7.fhir.uv.ebm#1.0.0-ballot`

***

### HL7 Europe Extensions

**Versions**

* `hl7.fhir.eu.extensions#0.1.0`

***

### test.v202111591

**Versions**

* `test.v202111591#0.0.2`
* `test.v202111591#0.0.1`

***

### Da Vinci - Documentation Templates and Rules

**Versions**

* `hl7.fhir.us.davinci-dtr#2.1.0`
* `hl7.fhir.us.davinci-dtr#2.1.0-preview`
* `hl7.fhir.us.davinci-dtr#2.0.1`
* `hl7.fhir.us.davinci-dtr#2.0.0`
* `hl7.fhir.us.davinci-dtr#1.1.0-ballot`
* `hl7.fhir.us.davinci-dtr#1.0.0`
* `hl7.fhir.us.davinci-dtr#0.2.0`
* `hl7.fhir.us.davinci-dtr#0.1.0`

***

### Scheduling

**Versions**

* `ihe.iti.scheduling#1.0.0`
* `ihe.iti.scheduling#1.0.0-comment`

***

### Pan-Canadian Patient Summary

**Versions**

* `ca.infoway.vc.ps#0.0.3`

***

### Structured Data Capture

**Versions**

* `hl7.fhir.uv.sdc#4.0.0-ballot`
* `hl7.fhir.uv.sdc#3.0.0`
* `hl7.fhir.uv.sdc#3.0.0-preview`
* `hl7.fhir.uv.sdc#2.7.0`
* `hl7.fhir.uv.sdc#2.5.0`
* `hl7.fhir.uv.sdc#2.0.0`
* `hl7.fhir.uv.sdc#1.6`

***

### healthhub.fhir.dk.core

**Versions**

* `healthhub.fhir.dk.core#1.0.24`
* `healthhub.fhir.dk.core#1.0.23`
* `healthhub.fhir.dk.core#1.0.22`
* `healthhub.fhir.dk.core#1.0.21`
* `healthhub.fhir.dk.core#1.0.17`
* `healthhub.fhir.dk.core#1.0.16`
* `healthhub.fhir.dk.core#1.0.15`
* `healthhub.fhir.dk.core#1.0.14`
* `healthhub.fhir.dk.core#1.0.13`
* `healthhub.fhir.dk.core#1.0.12`
* `healthhub.fhir.dk.core#1.0.11`
* `healthhub.fhir.dk.core#1.0.10`
* `healthhub.fhir.dk.core#1.0.9`
* `healthhub.fhir.dk.core#1.0.8`
* `healthhub.fhir.dk.core#1.0.7`
* `healthhub.fhir.dk.core#1.0.6`
* `healthhub.fhir.dk.core#1.0.5`
* `healthhub.fhir.dk.core#1.0.4`
* `healthhub.fhir.dk.core#1.0.3-alpha`
* `healthhub.fhir.dk.core#1.0.2-alpha`
* `healthhub.fhir.dk.core#1.0.1-alpha`
* `healthhub.fhir.dk.core#1.0.0-alpha`

***

### Terminology Change Set Exchange

**Versions**

* `hl7.fhir.uv.termchangeset#1.0.0-ballot`

***

### rki.demis.ars

**Versions**

* `rki.demis.ars#1.0.0-alpha.1`

***

### de.gematik.dev.ti

**Versions**

* `de.gematik.dev.ti#1.0.0-alpha.2`
* `de.gematik.dev.ti#1.0.0-alpha.1`

***

### ehelse.fhir.no.grunndata

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
* `ehelse.fhir.no.grunndata#2.2.2`
* `ehelse.fhir.no.grunndata#2.2.1`
* `ehelse.fhir.no.grunndata#2.2.0`
* `ehelse.fhir.no.grunndata#2.1.0`

***

### ndhm.in.mirror

**Versions**

* `ndhm.in.mirror#1.2.0-rc2`
* `ndhm.in.mirror#1.2.0-rc1`
* `ndhm.in.mirror#1.2.0-rc0`

***

### ca.on.patient-summary

**Versions**

* `ca.on.patient-summary#0.0.1-alpha`

***

### ca.on.ehr.r4

**Versions**

* `ca.on.ehr.r4#1.0.0`
* `ca.on.ehr.r4#1.0.0-snapshot4`
* `ca.on.ehr.r4#1.0.0-snapshot3`
* `ca.on.ehr.r4#1.0.0-snapshot2`
* `ca.on.ehr.r4#1.0.0-snapshot1`
* `ca.on.ehr.r4#0.9.4`
* `ca.on.ehr.r4#0.9.3`
* `ca.on.ehr.r4#0.9.2`
* `ca.on.ehr.r4#0.9.1`
* `ca.on.ehr.r4#0.9.0`

***

### PHIS.IG.CreateTest

**Versions**

* `PHIS.IG.CreateTest#0.0.1`

***

### Data Exchange For Quality Measures Implementation Guide

**Versions**

* `hl7.fhir.us.davinci-deqm#5.0.0-ballot`
* `hl7.fhir.us.davinci-deqm#4.0.0`
* `hl7.fhir.us.davinci-deqm#4.0.0-ballot`
* `hl7.fhir.us.davinci-deqm#3.1.0`
* `hl7.fhir.us.davinci-deqm#3.0.0`
* `hl7.fhir.us.davinci-deqm#2.1.0`
* `hl7.fhir.us.davinci-deqm#2.0.0`
* `hl7.fhir.us.davinci-deqm#1.1.0`
* `hl7.fhir.us.davinci-deqm#1.0.0`
* `hl7.fhir.us.davinci-deqm#0.1.0`

***

### kbv.mio.emp

**Versions**

* `kbv.mio.emp#1.0.0-kommentierung.1`

***

### ca.infoway.io.cafex

**Versions**

* `ca.infoway.io.cafex#2.1.0-DFT-preBallot`
* `ca.infoway.io.cafex#2.1.0-DFT-Ballot`
* `ca.infoway.io.cafex#2.0.0`
* `ca.infoway.io.cafex#2.0.0-dft-pre`
* `ca.infoway.io.cafex#2.0.0-DFT-Ballot`

***

### CH Term (R4)

**Versions**

* `ch.fhir.ig.ch-term#3.1.0`
* `ch.fhir.ig.ch-term#3.0.0`

***

### acme.canada.2023

**Versions**

* `acme.canada.2023#1.2.0`
* `acme.canada.2023#0.0.1-alpha`

***

### bbmri.cz

**Versions**

* `bbmri.cz#0.1.0`

***

### uk.nhsdigital.clinical.r4

**Versions**

* `uk.nhsdigital.clinical.r4#2.1.2-dev`
* `uk.nhsdigital.clinical.r4#2.1.1-dev`
* `uk.nhsdigital.clinical.r4#2.1.0-dev`

***

### Implementation Guide for fælles faglige instrumenter (FFInst)

**Versions**

* `kl.dk.fhir.ffinst#1.0.0`

***

### br.ufg.cgis.rnds-lite

**Versions**

* `br.ufg.cgis.rnds-lite#0.2.4`
* `br.ufg.cgis.rnds-lite#0.2.3`
* `br.ufg.cgis.rnds-lite#0.2.2`
* `br.ufg.cgis.rnds-lite#0.2.1`
* `br.ufg.cgis.rnds-lite#0.2.0`
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

***

### cens.fhir.ssas-cdr

**Versions**

* `cens.fhir.ssas-cdr#1.0.0`

***

### COVID-19 FHIR Profile Library IG Informative Version

**Versions**

* `hl7.fhir.us.covid19library#1.0.0`
* `hl7.fhir.us.covid19library#0.14.0`
* `hl7.fhir.us.covid19library#0.13.0`

***

### 醫查實作指引

**Versions**

* `tw.cathay.fhir.imri#1.0.0`

***

### Implementierungsleitfaden DEMIS IGS (Integrierte Genomische Surveillance)

**Versions**

* `rki.demis.igs#3.0.0`

***

### ASIC.Package

**Versions**

* `ASIC.Package#0.0.1-draft`

***

### CH EPR mHealth (R4)

**Versions**

* `ch.fhir.ig.ch-epr-mhealth#3.0.0`
* `ch.fhir.ig.ch-epr-mhealth#3.0.0-ballot`
* `ch.fhir.ig.ch-epr-mhealth#1.1.0`
* `ch.fhir.ig.ch-epr-mhealth#1.0.0`
* `ch.fhir.ig.ch-epr-mhealth#0.2.0`
* `ch.fhir.ig.ch-epr-mhealth#0.1.2`
* `ch.fhir.ig.ch-epr-mhealth#0.1.1`
* `ch.fhir.ig.ch-epr-mhealth#0.1.0`

***

### Data Access Framework

**Versions**

* `hl7.fhir.us.daf#2.0.0`
* `hl7.fhir.us.daf#1.6.0`

***

### Clinical Practice Guidelines (CPG) on EBMonFHIR

**Versions**

* `de.netzwerk-universitaetsmedizin.ebm-cpg#1.2.0`
* `de.netzwerk-universitaetsmedizin.ebm-cpg#1.1.1`
* `de.netzwerk-universitaetsmedizin.ebm-cpg#1.1.0`
* `de.netzwerk-universitaetsmedizin.ebm-cpg#1.0.2`
* `de.netzwerk-universitaetsmedizin.ebm-cpg#1.0.1`
* `de.netzwerk-universitaetsmedizin.ebm-cpg#1.0.0`
* `de.netzwerk-universitaetsmedizin.ebm-cpg#0.10.0`
* `de.netzwerk-universitaetsmedizin.ebm-cpg#0.9.2`
* `de.netzwerk-universitaetsmedizin.ebm-cpg#0.9.1`
* `de.netzwerk-universitaetsmedizin.ebm-cpg#0.9.0`
* `de.netzwerk-universitaetsmedizin.ebm-cpg#0.8.0`
* `de.netzwerk-universitaetsmedizin.ebm-cpg#0.7.0`
* `de.netzwerk-universitaetsmedizin.ebm-cpg#0.7.1-snapshot`

***

### Enhancing Oncology Model

**Versions**

* `globalalliant.us.eom-updates#1.1.0-alpha-2`
* `globalalliant.us.eom-updates#1.1.0-alpha-1`

***

### Vital Signs

**Versions**

* `hl7.fhir.us.vitalsigns#0.1.0`

***

### uk.gm

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

***

### Immunization Decision Support Forecast

**Versions**

* `hl7.fhir.uv.immds#0.2.0`
* `hl7.fhir.uv.immds#0.1.0`

***

### duwel.nl.stu3.sandbox.dev

**Versions**

* `duwel.nl.stu3.sandbox.dev#0.0.1-dev.5`
* `duwel.nl.stu3.sandbox.dev#0.0.1-dev.4`
* `duwel.nl.stu3.sandbox.dev#0.0.1-dev.3`
* `duwel.nl.stu3.sandbox.dev#0.0.1-dev.2`
* `duwel.nl.stu3.sandbox.dev#0.0.1-dev.1`

***

### Koppeltaalv2.00

**Versions**

* `Koppeltaalv2.00#0.7.0-preview`

***

### MII IG Symptom

**Versions**

* `de.medizininformatikinitiative.kerndatensatz.symptom#2024.0.0-ballot`

***

### Birth And Fetal Death (BFDR) FHIR Implementation Guide

**Versions**

* `hl7.fhir.us.bfdr#2.0.0`
* `hl7.fhir.us.bfdr#2.0.0-ballot`
* `hl7.fhir.us.bfdr#1.1.0`
* `hl7.fhir.us.bfdr#1.0.0`
* `hl7.fhir.us.bfdr#0.1.0`

***

### ten.fhir

**Versions**

* `ten.fhir#0.5.0`

***

### kbv.mio.u-heft

**Versions**

* `kbv.mio.u-heft#1.0.1-festlegungsversion`
* `kbv.mio.u-heft#1.0.1-benehmensversion`

***

### FHIR implementation of Medication Process 9 (MP9)

**Versions**

* `nictiz.fhir.nl.r4.medicationprocess9#2.0.0-beta.6`
* `nictiz.fhir.nl.r4.medicationprocess9#2.0.0-beta.5`
* `nictiz.fhir.nl.r4.medicationprocess9#2.0.0-beta.4`
* `nictiz.fhir.nl.r4.medicationprocess9#2.0.0-beta.2`
* `nictiz.fhir.nl.r4.medicationprocess9#2.0.0-beta.1`
* `nictiz.fhir.nl.r4.medicationprocess9#1.0.0`

***

### test.pro

**Versions**

* `test.pro#0.3.0`

***

### National Healthcare Directory Query

**Versions**

* `hl7.fhir.us.directory-query#1.0.0-ballot`

***

### cp.test.project.v1-0

**Versions**

* `cp.test.project.v1-0#1.0.1-beta`
* `cp.test.project.v1-0#1.0.0-beta`

***

### kbv.mio.impfpass

**Versions**

* `kbv.mio.impfpass#1.1.0`
* `kbv.mio.impfpass#1.1.0-benehmensherstellung`

***

### KL Terminologi

**Versions**

* `kl.dk.fhir.term#2.2.0`
* `kl.dk.fhir.term#2.1.0`
* `kl.dk.fhir.term#2.0.0`
* `kl.dk.fhir.term#1.1.0`
* `kl.dk.fhir.term#1.0.0`

***

### Guia de Implementação

**Versions**

* `br.ufg.cgis.ig#0.0.3`
* `br.ufg.cgis.ig#0.0.2`
* `br.ufg.cgis.ig#0.0.1`

***

### iknl.fhir.nl.r4.palga

**Versions**

* `iknl.fhir.nl.r4.palga#1.0.1`
* `iknl.fhir.nl.r4.palga#1.0.0`
* `iknl.fhir.nl.r4.palga#0.3.0-beta`
* `iknl.fhir.nl.r4.palga#0.2.0`
* `iknl.fhir.nl.r4.palga#0.1.0`

***

### de.gematik.isik-labor

**Versions**

* `de.gematik.isik-labor#4.0.0-rc`

***

### DK MedCom Document

**Versions**

* `medcom.fhir.dk.document#1.0.0`

***

### kbv.mio.laborbefund

**Versions**

* `kbv.mio.laborbefund#1.0.0-kommentierung`

***

### SMART Verifiable IPS for Pilgrimage

**Versions**

* `smart.who.int.ips-pilgrimage#1.0.0`

***

### uk.adsv2.r4

**Versions**

* `uk.adsv2.r4#1.6.1`
* `uk.adsv2.r4#1.6.0`
* `uk.adsv2.r4#1.4.0`
* `uk.adsv2.r4#1.3.0`

***

### CH ELM (R4)

**Versions**

* `ch.fhir.ig.ch-elm#1.9.1`
* `ch.fhir.ig.ch-elm#1.9.0`
* `ch.fhir.ig.ch-elm#1.8.0`
* `ch.fhir.ig.ch-elm#1.7.0`
* `ch.fhir.ig.ch-elm#1.6.0`
* `ch.fhir.ig.ch-elm#1.5.0`
* `ch.fhir.ig.ch-elm#1.4.0`
* `ch.fhir.ig.ch-elm#1.3.1`
* `ch.fhir.ig.ch-elm#1.3.0`
* `ch.fhir.ig.ch-elm#1.2.0`
* `ch.fhir.ig.ch-elm#1.1.1`
* `ch.fhir.ig.ch-elm#1.1.0`
* `ch.fhir.ig.ch-elm#1.0.0`
* `ch.fhir.ig.ch-elm#1.0.0-trialuse`

***

### Protocols for Clinical Registry Extraction and Data Submission (CREDS) IG

**Versions**

* `hl7.fhir.us.registry-protocols#1.0.0`
* `hl7.fhir.us.registry-protocols#1.0.0-ballot`

***

### SMART TS

**Versions**

* `smart.who.int.ts#0.1.0`

***

### de.einwilligungsmanagement

**Versions**

* `de.einwilligungsmanagement#1.1.0-alpha.1`
* `de.einwilligungsmanagement#2.0.0`
* `de.einwilligungsmanagement#1.1.0-ballot1`
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

***

### qurasoft.saniq

**Versions**

* `qurasoft.saniq#1.0.0`

***

### Consolidated CDA (C-CDA)

**Versions**

* `hl7.cda.us.ccda#4.0.0-ballot`
* `hl7.cda.us.ccda#3.0.0`
* `hl7.cda.us.ccda#3.0.0-ballot`

***

### HL7 FHIR Implementation Guide Laboratory Report

**Versions**

* `hl7.fhir.it.lab-report#0.2.0`

***

### Implementierungsleitfaden DEMIS - Erregernachweismeldung

**Versions**

* `rki.demis.laboratory#3.0.0-alpha.1`
* `rki.demis.laboratory#3.2.1`
* `rki.demis.laboratory#3.2.0`
* `rki.demis.laboratory#3.0.0`
* `rki.demis.laboratory#2.0.1`
* `rki.demis.laboratory#2.0.0`
* `rki.demis.laboratory#1.24.1`
* `rki.demis.laboratory#1.24.0`
* `rki.demis.laboratory#1.24.2-new`

***

### C-CDA on FHIR

**Versions**

* `hl7.fhir.us.ccda#2.0.0-ballot`
* `hl7.fhir.us.ccda#1.6.0`
* `hl7.fhir.us.ccda#1.2.0`
* `hl7.fhir.us.ccda#1.2.0-ballot`
* `hl7.fhir.us.ccda#1.1.0`
* `hl7.fhir.us.ccda#1.0.0`

***

### Berkay.Sandbox

**Versions**

* `Berkay.Sandbox#0.0.1`

***

### Structured Data Capture

**Versions**

* `hl7.fhir.uv.sdc.r4#3.0.0`

***

### Belgian MyCareNet Profiles

**Versions**

* `hl7.fhir.be.mycarenet#2.1.0`
* `hl7.fhir.be.mycarenet#2.0.0`

***

### pathologyencountertissue.tryout

**Versions**

* `pathologyencountertissue.tryout#1.0.0`

***

### de.basisprofil.onkologie

**Versions**

* `de.basisprofil.onkologie#1.0.0-ballot`

***

### HL7® FHIR® Te Aho o Te Kahu, Cancer Control Agency Implementation Guide

**Versions**

* `hl7.fhir.nz.cca#0.1.0`

***

### Nictiz FHIR NL R4 Lab Exchange

**Versions**

* `nictiz.fhir.nl.r4.labexchange#3.0.0-beta.2`

***

### rki.emiga.common

**Versions**

* `rki.emiga.common#1.1.0-alpha.1`
* `rki.emiga.common#1.1.0`
* `rki.emiga.common#1.0.0`

***

### Implementierungsleitfaden EMIGA - Ausbruch

**Versions**

* `rki.emiga.outbreak#0.1.2`
* `rki.emiga.outbreak#0.1.1`
* `rki.emiga.outbreak#0.1.0`

***

### Interactive Multimedia Report (IMR)

**Versions**

* `ihe.rad.imr#1.1.0`
* `ihe.rad.imr#1.0.0`
* `ihe.rad.imr#1.0.0-comment`
* `ihe.rad.imr#0.1.0`

***

### FHIR Clinical Documents

**Versions**

* `hl7.fhir.uv.fhir-clinical-document#1.0.0-ballot`

***

### FHIR R4 package : Core

**Versions**

* `hl7.fhir.r4.core#4.0.1`

***

### nictiz.fhir.nl.stu3.zib2015

**Versions**

* `nictiz.fhir.nl.stu3.zib2015#1.0.0`

***

### UK.NHSDigital.BARS.R4

**Versions**

* `UK.NHSDigital.BARS.R4#0.1.0-test`

***

### cezih.hr.encounter-management

**Versions**

* `cezih.hr.encounter-management#0.2.2`

***

### de.gematik.vsdm2

**Versions**

* `de.gematik.vsdm2#1.0.0-rc4`
* `de.gematik.vsdm2#1.0.0-rc3`
* `de.gematik.vsdm2#1.0.0-rc1`
* `de.gematik.vsdm2#1.0.0-RC2`

***

### HL7 Norway no-basis

**Versions**

* `hl7.fhir.no.basis#2.2.2`
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

***

### uk.nhsdigital.stu3.test

**Versions**

* `uk.nhsdigital.stu3.test#0.0.2-prerelease`
* `uk.nhsdigital.stu3.test#0.0.1-prerelease`

***

### FHIR Extensions Pack

**Versions**

* `hl7.fhir.uv.extensions.r3#5.2.0`
* `hl7.fhir.uv.extensions.r3#5.1.0`
* `hl7.fhir.uv.extensions.r3#1.0.0`

***

### nhsdigital.fhir.stu3

**Versions**

* `nhsdigital.fhir.stu3#1.3.0`
* `nhsdigital.fhir.stu3#1.2.0`
* `nhsdigital.fhir.stu3#1.1.0`

***

### kbv.ita.vos

**Versions**

* `kbv.ita.vos#2.1.0`
* `kbv.ita.vos#1.20.0`

***

### portuguese.use-cases.guide

**Versions**

* `portuguese.use-cases.guide#1.0.1`

***

### acme.profiling.tutorial.r4

**Versions**

* `acme.profiling.tutorial.r4#2.0.0`
* `acme.profiling.tutorial.r4#1.0.0`

***

### iknl.fhir.nl.r4.ncr-ehr.r4

**Versions**

* `iknl.fhir.nl.r4.ncr-ehr.r4#1.2.0`

***

### PCR.R4.1-1-0-pkg

**Versions**

* `PCR.R4.1-1-0-pkg#1.1.0`

***

### AU eRequesting Implementation Guide

**Versions**

* `hl7.fhir.au.ereq#0.3.0-preview`
* `hl7.fhir.au.ereq#0.2.0-preview`
* `hl7.fhir.au.ereq#0.1.0-ballot`

***

### Da Vinci Payer Data Exchange

**Versions**

* `hl7.fhir.us.davinci-pdex#2.1.0-ballot`
* `hl7.fhir.us.davinci-pdex#2.0.0`
* `hl7.fhir.us.davinci-pdex#2.0.0-ballot`
* `hl7.fhir.us.davinci-pdex#1.0.0`
* `hl7.fhir.us.davinci-pdex#0.1.0`

***

### dev.cihub.ClinicalSite.org

**Versions**

* `dev.cihub.ClinicalSite.org#0.0.2`
* `dev.cihub.ClinicalSite.org#0.0.1`

***

### i.s.h.med FHIR R4 International API

**Versions**

* `ishmed.i14y.r4#2.0.0`
* `ishmed.i14y.r4#1.0.0`

***

### MII IG Onkologie

**Versions**

* `de.medizininformatikinitiative.kerndatensatz.onkologie#2025.1.0-alpha`
* `de.medizininformatikinitiative.kerndatensatz.onkologie#2025.0.4`
* `de.medizininformatikinitiative.kerndatensatz.onkologie#2025.0.3`
* `de.medizininformatikinitiative.kerndatensatz.onkologie#2025.0.2`
* `de.medizininformatikinitiative.kerndatensatz.onkologie#2025.0.1`
* `de.medizininformatikinitiative.kerndatensatz.onkologie#2025.0.0`
* `de.medizininformatikinitiative.kerndatensatz.onkologie#2025.0.0-alpha2`
* `de.medizininformatikinitiative.kerndatensatz.onkologie#2025.0.0-alpha1`
* `de.medizininformatikinitiative.kerndatensatz.onkologie#2024.0.0-beta-2`
* `de.medizininformatikinitiative.kerndatensatz.onkologie#2024.0.0-ballot`
* `de.medizininformatikinitiative.kerndatensatz.onkologie#2024.0.0-ballot-beta-1`
* `de.medizininformatikinitiative.kerndatensatz.onkologie#2024.0.0-ballot-alpha-1`

***

### Israel Division of Governmental Hospitals Profiles

**Versions**

* `il.fhir.r4.dgmc#0.2.0`
* `il.fhir.r4.dgmc#0.1.23`
* `il.fhir.r4.dgmc#0.1.22`
* `il.fhir.r4.dgmc#0.1.21`
* `il.fhir.r4.dgmc#0.1.20`
* `il.fhir.r4.dgmc#0.1.19`
* `il.fhir.r4.dgmc#0.1.18`
* `il.fhir.r4.dgmc#0.1.17`
* `il.fhir.r4.dgmc#0.1.16`
* `il.fhir.r4.dgmc#0.1.15`
* `il.fhir.r4.dgmc#0.1.14`
* `il.fhir.r4.dgmc#0.1.13`
* `il.fhir.r4.dgmc#0.1.12`
* `il.fhir.r4.dgmc#0.1.11`
* `il.fhir.r4.dgmc#0.1.10`
* `il.fhir.r4.dgmc#0.1.9`
* `il.fhir.r4.dgmc#0.1.8`
* `il.fhir.r4.dgmc#0.1.7`
* `il.fhir.r4.dgmc#0.1.6`
* `il.fhir.r4.dgmc#0.1.5`
* `il.fhir.r4.dgmc#0.1.4`

***

### Mobile Aggregate Data Exchange

**Versions**

* `ihe.qrph.madx#3.0.0-comment`

***

### tsti.adultcheck

**Versions**

* `tsti.adultcheck#0.1.2`
* `tsti.adultcheck#0.1.1`

***

### de.maris.fhir

**Versions**

* `de.maris.fhir#1.0.0`

***

### ca.on.dhdr.r4.v4

**Versions**

* `ca.on.dhdr.r4.v4#0.1.2-beta`
* `ca.on.dhdr.r4.v4#0.1.1-beta`
* `ca.on.dhdr.r4.v4#0.1.0-beta`
* `ca.on.dhdr.r4.v4#0.0.9-beta`
* `ca.on.dhdr.r4.v4#0.0.8-beta`
* `ca.on.dhdr.r4.v4#0.0.7-beta`
* `ca.on.dhdr.r4.v4#0.0.6-beta`

***

### PathologyEncounterTissue.tryout

**Versions**

* `PathologyEncounterTissue.tryout#0.1.1`

***

### rambam-fhir.health.gov.il

**Versions**

* `rambam-fhir.health.gov.il#0.1.5`
* `rambam-fhir.health.gov.il#0.1.4`
* `rambam-fhir.health.gov.il#0.1.3`
* `rambam-fhir.health.gov.il#0.1.2`
* `rambam-fhir.health.gov.il#0.1.1`

***

### ch.chmed16af.emediplan.fhir

**Versions**

* `ch.chmed16af.emediplan.fhir#1.0.0`

***

### Implementation Guide CH VACD

**Versions**

* `ch.fhir.ig.ch-vacd#5.0.0`
* `ch.fhir.ig.ch-vacd#5.0.0-ballot`
* `ch.fhir.ig.ch-vacd#4.0.1`
* `ch.fhir.ig.ch-vacd#4.0.0`
* `ch.fhir.ig.ch-vacd#4.0.0-ballot`
* `ch.fhir.ig.ch-vacd#3.0.0`
* `ch.fhir.ig.ch-vacd#2.1.0`
* `ch.fhir.ig.ch-vacd#2.0.0`
* `ch.fhir.ig.ch-vacd#1.0.0`
* `ch.fhir.ig.ch-vacd#0.1.0`

***

### International Patient Summary Implementation Guide

**Versions**

* `hl7.fhir.uv.ips#2.0.0-ballot`
* `hl7.fhir.uv.ips#1.1.0`
* `hl7.fhir.uv.ips#1.0.0`
* `hl7.fhir.uv.ips#0.3.0`
* `hl7.fhir.uv.ips#0.2.0`
* `hl7.fhir.uv.ips#0.1.0`

***

### Da Vinci Value-Based Performance Reporting Implementation Guide

**Versions**

* `hl7.fhir.us.davinci-vbpr#1.0.0`
* `hl7.fhir.us.davinci-vbpr#1.0.0-ballot`

***

### kbv.mio.kh-entlassbrief

**Versions**

* `kbv.mio.kh-entlassbrief#1.0.0-update`
* `kbv.mio.kh-entlassbrief#1.0.0-kommentierung`

***

### FHIR Implementation Guide for ABDM Preview

**Versions**

* `ig.in#0.1.0`

***

### kbv.basis.terminology.with.expansion

**Versions**

* `kbv.basis.terminology.with.expansion#1.7.0`

***

### test.touchstone.at.package

**Versions**

* `test.touchstone.at.package#0.0.3-beta`

***

### ca.on.oh-olis

**Versions**

* `ca.on.oh-olis#2.0.0-0.0.1`

***

### Cancer Pathology Data Sharing

**Versions**

* `hl7.fhir.us.cancer-reporting#2.0.0-ballot`
* `hl7.fhir.us.cancer-reporting#1.0.1`
* `hl7.fhir.us.cancer-reporting#1.0.0`
* `hl7.fhir.us.cancer-reporting#0.1.0`

***

### MII IG Medikation

**Versions**

* `de.medizininformatikinitiative.kerndatensatz.medikation#2025.0.0-rc.2`
* `de.medizininformatikinitiative.kerndatensatz.medikation#2025.0.0-rc.1`
* `de.medizininformatikinitiative.kerndatensatz.medikation#2025.0.0`
* `de.medizininformatikinitiative.kerndatensatz.medikation#2025.0.0-alpha5`
* `de.medizininformatikinitiative.kerndatensatz.medikation#2025.0.0-alpha4`
* `de.medizininformatikinitiative.kerndatensatz.medikation#2025.0.0-alpha3`
* `de.medizininformatikinitiative.kerndatensatz.medikation#2025.0.0-alpha2`
* `de.medizininformatikinitiative.kerndatensatz.medikation#2025.0.0-alpha1`
* `de.medizininformatikinitiative.kerndatensatz.medikation#2.0.0`

***

### HL7 FHIR Implementation Guide: Military Service History and Status Release 1 - US Realm | STU1

**Versions**

* `hl7.fhir.us.military-service#1.0.0`
* `hl7.fhir.us.military-service#0.1.0`

***

### Nictiz FHIR NL STU3 BgZ

**Versions**

* `nictiz.fhir.nl.stu3.bgz#2.0.3-beta.1`
* `nictiz.fhir.nl.stu3.bgz#2.0.2-beta.1`
* `nictiz.fhir.nl.stu3.bgz#2.0.1-beta.1`
* `nictiz.fhir.nl.stu3.bgz#2.0.0-beta.1`

***

### de.gematik.epa.audit

**Versions**

* `de.gematik.epa.audit#1.0.5-ballot.1`

***

### FHIR 4.3.0 package : Expansions

**Versions**

* `hl7.fhir.r4b.expansions#4.3.0`
* `hl7.fhir.r4b.expansions#4.1.0`

***

### accdr.fhir.ig.pkg

**Versions**

* `accdr.fhir.ig.pkg#0.9.0-alpha-0.9.23`
* `accdr.fhir.ig.pkg#0.9.0-alpha-0.9.22`
* `accdr.fhir.ig.pkg#0.9.0-alpha-0.9.21`
* `accdr.fhir.ig.pkg#0.9.0-alpha-0.9.20`
* `accdr.fhir.ig.pkg#0.9.0-alpha-0.9.19`
* `accdr.fhir.ig.pkg#0.9.0-alpha-0.9.18`
* `accdr.fhir.ig.pkg#0.9.0-alpha-0.9.17`
* `accdr.fhir.ig.pkg#0.9.0-alpha-0.9.16`
* `accdr.fhir.ig.pkg#0.9.24`
* `accdr.fhir.ig.pkg#0.9.23`
* `accdr.fhir.ig.pkg#0.9.22`
* `accdr.fhir.ig.pkg#0.9.21`
* `accdr.fhir.ig.pkg#0.9.20`
* `accdr.fhir.ig.pkg#0.9.19`
* `accdr.fhir.ig.pkg#0.9.18`
* `accdr.fhir.ig.pkg#0.9.15`

***

### Subscriptions R5 Backport

**Versions**

* `hl7.fhir.uv.subscriptions-backport.r4b#1.1.0`

***

### HL7® FHIR® New Zealand Base Implementation Guide

**Versions**

* `fhir.org.nz.ig.base#3.0.1`

***

### demis.fhir.profiles

**Versions**

* `demis.fhir.profiles#1.17.0`
* `demis.fhir.profiles#1.15.0`

***

### Clinical Document Architecture

**Versions**

* `hl7.cda.uv.core#2.0.1-sd`
* `hl7.cda.uv.core#2.0.0-sd`
* `hl7.cda.uv.core#2.0.0-sd-snapshot1`
* `hl7.cda.uv.core#2.0.0-sd-ballot`

***

### Implementation Guide for FFB messaging (FFB udvekslingsdatasæt)

**Versions**

* `kl.dk.fhir.ffbmessaging#1.0.0`

***

### de.gematik.isik-terminplanung

**Versions**

* `de.gematik.isik-terminplanung#4.0.3`
* `de.gematik.isik-terminplanung#4.0.2`
* `de.gematik.isik-terminplanung#4.0.1`
* `de.gematik.isik-terminplanung#4.0.0`
* `de.gematik.isik-terminplanung#4.0.0-rc2`
* `de.gematik.isik-terminplanung#4.0.0-rc`
* `de.gematik.isik-terminplanung#3.0.7`
* `de.gematik.isik-terminplanung#3.0.6`
* `de.gematik.isik-terminplanung#3.0.5`
* `de.gematik.isik-terminplanung#3.0.4`
* `de.gematik.isik-terminplanung#3.0.3`
* `de.gematik.isik-terminplanung#3.0.2`
* `de.gematik.isik-terminplanung#2.0.6`
* `de.gematik.isik-terminplanung#2.0.5`
* `de.gematik.isik-terminplanung#2.0.4`
* `de.gematik.isik-terminplanung#2.0.3`

***

### Digital Tooling Implementation Guide

**Versions**

* `tewhatuora.digitaltooling#0.0.11`
* `tewhatuora.digitaltooling#0.0.10`
* `tewhatuora.digitaltooling#0.0.9`
* `tewhatuora.digitaltooling#0.0.8`
* `tewhatuora.digitaltooling#0.0.7`
* `tewhatuora.digitaltooling#0.0.6`

***

### devdays.letsbuildafhirspec.simplifier

**Versions**

* `devdays.letsbuildafhirspec.simplifier#0.0.4-preview`
* `devdays.letsbuildafhirspec.simplifier#0.0.3-devdaysus2021`
* `devdays.letsbuildafhirspec.simplifier#0.0.2-test`
* `devdays.letsbuildafhirspec.simplifier#0.0.1-test`

***

### de.gematik.dev.epa

**Versions**

* `de.gematik.dev.epa#1.1.0-alpha.12`
* `de.gematik.dev.epa#1.1.0-alpha.6`
* `de.gematik.dev.epa#1.1.0-alpha.5`
* `de.gematik.dev.epa#1.1.0-alpha.4`
* `de.gematik.dev.epa#1.0.5-alpha.3`

***

### fi.kela.kanta.hrp.potilastiedot

**Versions**

* `fi.kela.kanta.hrp.potilastiedot#1.1.0-RC1`
* `fi.kela.kanta.hrp.potilastiedot#1.0.0`

***

### KBV.MIO.ZAEB

**Versions**

* `KBV.MIO.ZAEB#1.1.0-Kommentierung`
* `KBV.MIO.ZAEB#1.00.000`

***

### kbv.mio.tele

**Versions**

* `kbv.mio.tele#1.0.0`
* `kbv.mio.tele#1.0.0-kommentierung`
* `kbv.mio.tele#1.0.0-benehmensherstellung`

***

### package.teste

**Versions**

* `package.teste#1.0.7-draft`
* `package.teste#1.0.3-draft`
* `package.teste#1.0.2-draft`
* `package.teste#1.0.1-draft`

***

### eng.fhir.profile.dev

**Versions**

* `eng.fhir.profile.dev#0.0.7-beta`
* `eng.fhir.profile.dev#0.0.6-beta`
* `eng.fhir.profile.dev#0.0.5-beta`
* `eng.fhir.profile.dev#0.0.4-beta`
* `eng.fhir.profile.dev#0.0.3-beta`
* `eng.fhir.profile.dev#0.0.2-beta`
* `eng.fhir.profile.dev#0.0.1-beta`

***

### mrrt.mintmedical

**Versions**

* `mrrt.mintmedical#4.0.2-preview`
* `mrrt.mintmedical#1.0.0`

***

### sfm.130323

**Versions**

* `sfm.130323#3.0.1`

***

### il.core.fhir.r4.2023

**Versions**

* `il.core.fhir.r4.2023#0.1.0`

***

### ca.on.oh.mha.pds

**Versions**

* `ca.on.oh.mha.pds#2.0.0-alpha-preview-0.1`
* `ca.on.oh.mha.pds#2.0.0-alpha-preview`

***

### MII IG Molekulares Tumorboard

**Versions**

* `de.medizininformatikinitiative.kerndatensatz.mtb#2025.0.0-ballot-alpha.1`

***

### eHealth Platform R4 Federal Profiles

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

***

### digi.derm

**Versions**

* `digi.derm#0.1.2`
* `digi.derm#0.1.1`
* `digi.derm#0.1.0`

***

### SIL-TH Terminology (STU1)

**Versions**

* `silth.fhir.terminology.core#0.1.2`

***

### de.gematik.epa.research

**Versions**

* `de.gematik.epa.research#1.0.0-cc1`

***

### Da Vinci Prior Authorization Support (PAS) FHIR IG

**Versions**

* `hl7.fhir.us.davinci-pas#2.1.0`
* `hl7.fhir.us.davinci-pas#2.1.0-preview`
* `hl7.fhir.us.davinci-pas#2.0.1`
* `hl7.fhir.us.davinci-pas#1.2.0-ballot`
* `hl7.fhir.us.davinci-pas#1.1.0`
* `hl7.fhir.us.davinci-pas#1.0.0`
* `hl7.fhir.us.davinci-pas#0.1.0`

***

### Real Time Location Services Implementation Guide

**Versions**

* `hl7.fhir.uv.rtls#1.0.0-ballot`

***

### gpc.stu3.fhir-assets

**Versions**

* `gpc.stu3.fhir-assets#1.1.3`
* `gpc.stu3.fhir-assets#1.1.2`
* `gpc.stu3.fhir-assets#1.1.1`
* `gpc.stu3.fhir-assets#1.1.0`
* `gpc.stu3.fhir-assets#1.0.0`

***

### de.medizininformatikinitiative.kerndatensatz.bildgebung

**Versions**

* `de.medizininformatikinitiative.kerndatensatz.bildgebung#2025.0.1`
* `de.medizininformatikinitiative.kerndatensatz.bildgebung#2025.0.0`
* `de.medizininformatikinitiative.kerndatensatz.bildgebung#2025.0.0-ballot`

***

### DK MedCom Terminology

**Versions**

* `medcom.fhir.dk.terminology#1.8.0`
* `medcom.fhir.dk.terminology#1.7.0`
* `medcom.fhir.dk.terminology#1.6.0`
* `medcom.fhir.dk.terminology#1.5.0`
* `medcom.fhir.dk.terminology#1.4.0`
* `medcom.fhir.dk.terminology#1.3.0`
* `medcom.fhir.dk.terminology#1.2.0`
* `medcom.fhir.dk.terminology#1.1.1`
* `medcom.fhir.dk.terminology#1.1.0`
* `medcom.fhir.dk.terminology#1.0.0`

***

### Da Vinci Clinical Data Exchange (CDex)

**Versions**

* `hl7.fhir.us.davinci-cdex#2.1.0`
* `hl7.fhir.us.davinci-cdex#2.1.0-snapshot`
* `hl7.fhir.us.davinci-cdex#2.0.0`
* `hl7.fhir.us.davinci-cdex#2.0.0-ballot`
* `hl7.fhir.us.davinci-cdex#1.1.0`
* `hl7.fhir.us.davinci-cdex#1.1.0-ballot`
* `hl7.fhir.us.davinci-cdex#1.0.0`
* `hl7.fhir.us.davinci-cdex#0.2.0`
* `hl7.fhir.us.davinci-cdex#0.1.0`

***

### mint.fhir

**Versions**

* `mint.fhir#1.2.1`
* `mint.fhir#1.2.0`
* `mint.fhir#1.2.2-preview`
* `mint.fhir#1.2.1-preview`
* `mint.fhir#1.2.0-preview`

***

### ehealth4u.package

**Versions**

* `ehealth4u.package#1.0.0`

***

### kbv.basis

**Versions**

* `kbv.basis#1.7.0`
* `kbv.basis#1.7.0-alpha2`
* `kbv.basis#1.7.0-alpha`
* `kbv.basis#1.7.0-Ressources`
* `kbv.basis#1.7.0-Expansions`
* `kbv.basis#1.6.0`
* `kbv.basis#1.6.0-Expansions`
* `kbv.basis#1.5.0`
* `kbv.basis#1.5.0-Expansions`
* `kbv.basis#1.4.0`
* `kbv.basis#1.4.0-Expansions`
* `kbv.basis#1.3.0`
* `kbv.basis#1.2.1`
* `kbv.basis#1.2.0`

***

### CodeX Radiation Therapy

**Versions**

* `hl7.fhir.us.codex-radiation-therapy#2.0.0`
* `hl7.fhir.us.codex-radiation-therapy#2.0.0-ballot`
* `hl7.fhir.us.codex-radiation-therapy#1.0.0`
* `hl7.fhir.us.codex-radiation-therapy#1.0.0-ballot`

***

### KBV.MIO.U-Heft

**Versions**

* `KBV.MIO.U-Heft#1.0.0`

***

### hl7.fhir.rdsuwearme

**Versions**

* `hl7.fhir.rdsuwearme#1.0.1`
* `hl7.fhir.rdsuwearme#1.0.0`

***

### Patsiendi üldandmete teenus / Master Patient Index

**Versions**

* `ee.fhir.mpi#1.2.2`
* `ee.fhir.mpi#1.2.1`
* `ee.fhir.mpi#1.2.0`
* `ee.fhir.mpi#1.1.1`
* `ee.fhir.mpi#1.1.0`

***

### Resumen Clínico del Paciente de Chile

**Versions**

* `hl7.fhir.cl.clips#0.4.0`
* `hl7.fhir.cl.clips#0.3.0-ballot`
* `hl7.fhir.cl.clips#0.2.0`

***

### HIP FHIR Common Terminology Guide

**Versions**

* `tewhatuora.hip-core#1.9.2`
* `tewhatuora.hip-core#1.9.1`
* `tewhatuora.hip-core#1.9.0`

***

### us.cdc.phinvads

**Versions**

* `us.cdc.phinvads#0.12.0`
* `us.cdc.phinvads#0.11.0`
* `us.cdc.phinvads#0.10.0`
* `us.cdc.phinvads#0.9.0`
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

***

### Northwell.Person.Extensions

**Versions**

* `Northwell.Person.Extensions#1.0.0-alpha`

***

### nictiz.fhir.nl.r4.outcomebasedcare

**Versions**

* `nictiz.fhir.nl.r4.outcomebasedcare#1.0.0-alpha.1`

***

### de.abda.erezeptabgabedatenbasis

**Versions**

* `de.abda.erezeptabgabedatenbasis#1.5.0`
* `de.abda.erezeptabgabedatenbasis#1.5.0-rc2`
* `de.abda.erezeptabgabedatenbasis#1.5.0-rc1`
* `de.abda.erezeptabgabedatenbasis#1.5.0-rc`
* `de.abda.erezeptabgabedatenbasis#1.4.2`
* `de.abda.erezeptabgabedatenbasis#1.4.1`
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

***

### Adverse Event Clinical Research R4 Backport

**Versions**

* `hl7.fhir.uv.ae-research-backport-ig#1.0.1`
* `hl7.fhir.uv.ae-research-backport-ig#1.0.0`
* `hl7.fhir.uv.ae-research-backport-ig#1.0.0-ballot`

***

### de.biv-ot.everordnungabgabedaten

**Versions**

* `de.biv-ot.everordnungabgabedaten#0.1.0-preview`

***

### FHIR Tooling Extensions IG

**Versions**

* `hl7.fhir.uv.tools.r4#0.5.0`
* `hl7.fhir.uv.tools.r4#0.4.1`
* `hl7.fhir.uv.tools.r4#0.4.0`
* `hl7.fhir.uv.tools.r4#0.3.0`

***

### devdays.letsbuild.simplifier

**Versions**

* `devdays.letsbuild.simplifier#0.1.0-test`

***

### SIL-TH FHIR Extension Library (STU2)

**Versions**

* `silth.fhir.th.extensions#1.0.0`
* `silth.fhir.th.extensions#0.1.0`

***

### Tiga interface implementation guide

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

***

### Mobile access to Health Documents (MHD)

**Versions**

* `ihe.mhd.fhir#4.0.2`
* `ihe.mhd.fhir#4.0.1`
* `ihe.mhd.fhir#4.0.0-comment`

***

### canadian.fsh.demo

**Versions**

* `canadian.fsh.demo#0.1.0-test`

***

### il.dqa.fhir.r4

**Versions**

* `il.dqa.fhir.r4#0.0.1`

***

### nxh.fhir.r4

**Versions**

* `nxh.fhir.r4#0.0.0-beta.20`
* `nxh.fhir.r4#0.0.0-beta.19`
* `nxh.fhir.r4#0.0.0-beta.18`
* `nxh.fhir.r4#0.0.0-beta.17`
* `nxh.fhir.r4#0.0.0-beta.16`
* `nxh.fhir.r4#0.0.0-beta.15`
* `nxh.fhir.r4#0.0.0-beta.14`
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

***

### star.rhecord.beta

**Versions**

* `star.rhecord.beta#0.0.3-beta`

***

### Query for Existing Data for Mobile (QEDm)

**Versions**

* `ihe.pcc.qedm#3.0.0`
* `ihe.pcc.qedm#3.0.0-comment1`

***

### jp-core.draft1

**Versions**

* `jp-core.draft1#1.0.1-beta`

***

### KBV.ITA.VOS

**Versions**

* `KBV.ITA.VOS#1.10.010`

***

### Northwell.Extensions

**Versions**

* `Northwell.Extensions#0.0.1`

***

### HL7 Belgium NIHDI Terminology

**Versions**

* `hl7.fhir.be.nihdi-terminology#1.0.0`

***

### portuguese.core.guide

**Versions**

* `portuguese.core.guide#1.0.0`

***

### de.dit-connectathon.r4

**Versions**

* `de.dit-connectathon.r4#0.3.0`
* `de.dit-connectathon.r4#0.2.0`
* `de.dit-connectathon.r4#0.1.0`

***

### hl7.fhir.essilux.core

**Versions**

* `hl7.fhir.essilux.core#0.0.2`
* `hl7.fhir.essilux.core#0.0.1`

***

### nexuzhealth.fhir.r4

**Versions**

* `nexuzhealth.fhir.r4#1.0.1`
* `nexuzhealth.fhir.r4#1.0.0`

***

### ehelse.fhir.no.grunndata.test

**Versions**

* `ehelse.fhir.no.grunndata.test#2.3.3`
* `ehelse.fhir.no.grunndata.test#2.3.2`
* `ehelse.fhir.no.grunndata.test#2.3.1`
* `ehelse.fhir.no.grunndata.test#2.3.0`
* `ehelse.fhir.no.grunndata.test#2.2.0`

***

### Implementierungsleitfaden DEMIS - Statistische Erhebungen

**Versions**

* `rki.demis.statistic#1.0.0`

***

### firely.com.accessibilitytesting

**Versions**

* `firely.com.accessibilitytesting#0.0.1-test`

***

### MyHIE.v4

**Versions**

* `MyHIE.v4#2.0.0-alpha`

***

### Nictiz FHIR NL STU3 Questionnaires

**Versions**

* `nictiz.fhir.nl.stu3.questionnaires#2.0.8`
* `nictiz.fhir.nl.stu3.questionnaires#2.0.7`
* `nictiz.fhir.nl.stu3.questionnaires#2.0.6`
* `nictiz.fhir.nl.stu3.questionnaires#2.0.5`
* `nictiz.fhir.nl.stu3.questionnaires#2.0.4`
* `nictiz.fhir.nl.stu3.questionnaires#2.0.3`
* `nictiz.fhir.nl.stu3.questionnaires#2.0.2`
* `nictiz.fhir.nl.stu3.questionnaires#2.0.1`
* `nictiz.fhir.nl.stu3.questionnaires#2.0.0`
* `nictiz.fhir.nl.stu3.questionnaires#1.0.3`
* `nictiz.fhir.nl.stu3.questionnaires#1.0.2`
* `nictiz.fhir.nl.stu3.questionnaires#1.0.0`
* `nictiz.fhir.nl.stu3.questionnaires#0.0.3-beta3`
* `nictiz.fhir.nl.stu3.questionnaires#0.0.2-beta2`
* `nictiz.fhir.nl.stu3.questionnaires#0.0.1-beta1`

***

### Document Subscription for Mobile (DSUBm)

**Versions**

* `ihe.iti.dsubm#1.0.0`
* `ihe.iti.dsubm#1.0.0-comment`

***

### Implementierungsleitfaden EMIGA - Fall

**Versions**

* `rki.emiga.case#0.1.0`

***

### pbm.v1.fhir

**Versions**

* `pbm.v1.fhir#0.7.0`
* `pbm.v1.fhir#0.6.0`

***

### PACIO Re-Assessment Timepoints Implementation Guide

**Versions**

* `hl7.fhir.us.pacio-rt#1.0.0`
* `hl7.fhir.us.pacio-rt#0.1.0`

***

### uk.nhsdigital.medicines.r4.test

**Versions**

* `uk.nhsdigital.medicines.r4.test#2.8.7-prerelease`
* `uk.nhsdigital.medicines.r4.test#2.8.3-prerelease`
* `uk.nhsdigital.medicines.r4.test#2.8.21-prerelease`
* `uk.nhsdigital.medicines.r4.test#2.8.20-prerelease`
* `uk.nhsdigital.medicines.r4.test#2.8.19-prerelease`
* `uk.nhsdigital.medicines.r4.test#2.8.18-prerelease`
* `uk.nhsdigital.medicines.r4.test#2.8.16-prerelease`
* `uk.nhsdigital.medicines.r4.test#2.8.11-prerelease`
* `uk.nhsdigital.medicines.r4.test#2.7.1-prerelease`
* `uk.nhsdigital.medicines.r4.test#2.6.6-prerelease`

***

### eHealth Infrastructure

**Versions**

* `dk.ehealth.sundhed.fhir.ig.core#3.5.1`
* `dk.ehealth.sundhed.fhir.ig.core#3.5.0`
* `dk.ehealth.sundhed.fhir.ig.core#3.4.1`
* `dk.ehealth.sundhed.fhir.ig.core#3.4.0`
* `dk.ehealth.sundhed.fhir.ig.core#3.3.0`
* `dk.ehealth.sundhed.fhir.ig.core#3.2.0`
* `dk.ehealth.sundhed.fhir.ig.core#3.1.0`

***

### DaVinci Payer Data Exchange (PDex) US Drug Formulary

**Versions**

* `hl7.fhir.us.davinci-drug-formulary#2.1.0`
* `hl7.fhir.us.davinci-drug-formulary#2.0.1`
* `hl7.fhir.us.davinci-drug-formulary#2.0.0`
* `hl7.fhir.us.davinci-drug-formulary#1.2.0`
* `hl7.fhir.us.davinci-drug-formulary#1.1.0`
* `hl7.fhir.us.davinci-drug-formulary#1.0.1`
* `hl7.fhir.us.davinci-drug-formulary#1.0.0`
* `hl7.fhir.us.davinci-drug-formulary#0.1.0`

***

### acme.fsh.ig.example

**Versions**

* `acme.fsh.ig.example#0.0.1-demo`

***

### SMART ICVP

**Versions**

* `smart.who.int.icvp#0.1.0`

***

### Da Vinci PDex Plan Net

**Versions**

* `hl7.fhir.us.davinci-pdex-plan-net#1.2.0`
* `hl7.fhir.us.davinci-pdex-plan-net#1.1.0`
* `hl7.fhir.us.davinci-pdex-plan-net#1.0.0`
* `hl7.fhir.us.davinci-pdex-plan-net#0.1.0`

***

### Patient Identifier Cross-referencing for mobile (PIXm)

**Versions**

* `ihe.iti.pixm#3.0.4`
* `ihe.iti.pixm#3.0.3`
* `ihe.iti.pixm#3.0.2`

***

### de.medizininformatikinitiative.kerndatensatz.studie

**Versions**

* `de.medizininformatikinitiative.kerndatensatz.studie#2025.0.0`
* `de.medizininformatikinitiative.kerndatensatz.studie#1.0.0`
* `de.medizininformatikinitiative.kerndatensatz.studie#1.0.0-ballot`

***

### CH IPS (R4)

**Versions**

* `ch.fhir.ig.ch-ips#1.0.0`
* `ch.fhir.ig.ch-ips#1.0.0-ballot`

***

### Implementation Guide CHMED20AF (R4)

**Versions**

* `ch.chmed20af.emediplan#2.0.0`
* `ch.chmed20af.emediplan#1.0.0`

***

### de.gematik.isik-dokumentenaustausch

**Versions**

* `de.gematik.isik-dokumentenaustausch#4.0.1`
* `de.gematik.isik-dokumentenaustausch#4.0.0`
* `de.gematik.isik-dokumentenaustausch#4.0.0-rc2`
* `de.gematik.isik-dokumentenaustausch#4.0.0-rc`
* `de.gematik.isik-dokumentenaustausch#3.0.5`
* `de.gematik.isik-dokumentenaustausch#3.0.4`
* `de.gematik.isik-dokumentenaustausch#3.0.3`
* `de.gematik.isik-dokumentenaustausch#3.0.2`
* `de.gematik.isik-dokumentenaustausch#3.0.1`
* `de.gematik.isik-dokumentenaustausch#3.0.0`

***

### uk.gpc.updaterecord

**Versions**

* `uk.gpc.updaterecord#1.1.0`
* `uk.gpc.updaterecord#1.0.0`

***

### NICEProfiling.v23Q1

**Versions**

* `NICEProfiling.v23Q1#0.1.0`

***

### ca.on.phsd.r4-alpha

**Versions**

* `ca.on.phsd.r4-alpha#0.1.0`

***

### basisprofil.tiplu.de.r4.dev

**Versions**

* `basisprofil.tiplu.de.r4.dev#1.0.1`
* `basisprofil.tiplu.de.r4.dev#1.0.0`

***

### ema.qrdvalidation

**Versions**

* `ema.qrdvalidation#0.0.6`

***

### AndersonSanto.Tarefa6

**Versions**

* `AndersonSanto.Tarefa6#1.0.0`

***

### de.TestprojektUKF.rmy

**Versions**

* `de.TestprojektUKF.rmy#0.1.0`

***

### FHIR implementation of Patient Corrections

**Versions**

* `nictiz.fhir.nl.r4.patientcorrections#1.0.6`
* `nictiz.fhir.nl.r4.patientcorrections#1.0.1`
* `nictiz.fhir.nl.r4.patientcorrections#1.0.0`

***

### MII IG Diagnose

**Versions**

* `de.medizininformatikinitiative.kerndatensatz.diagnose#2025.0.0-rc.4`
* `de.medizininformatikinitiative.kerndatensatz.diagnose#2025.0.0-rc.3`
* `de.medizininformatikinitiative.kerndatensatz.diagnose#2025.0.0-rc.2`
* `de.medizininformatikinitiative.kerndatensatz.diagnose#2025.0.0-rc.1`
* `de.medizininformatikinitiative.kerndatensatz.diagnose#2025.0.0`
* `de.medizininformatikinitiative.kerndatensatz.diagnose#2025.0.0-alpha3`
* `de.medizininformatikinitiative.kerndatensatz.diagnose#2025.0.0-alpha2`
* `de.medizininformatikinitiative.kerndatensatz.diagnose#2025.0.0-alpha1`
* `de.medizininformatikinitiative.kerndatensatz.diagnose#2024.0.0`
* `de.medizininformatikinitiative.kerndatensatz.diagnose#2024.0.0-ballot`
* `de.medizininformatikinitiative.kerndatensatz.diagnose#2024.0.0-alpha2`
* `de.medizininformatikinitiative.kerndatensatz.diagnose#1.0.4`

***

### Health New Zealand Te Whatu Ora Digital Tooling Implementation Guide

**Versions**

* `tewhatuora.digitaltooling.iguat#0.0.6`
* `tewhatuora.digitaltooling.iguat#0.0.5`
* `tewhatuora.digitaltooling.iguat#0.0.4`

***

### IdHIE.0v

**Versions**

* `IdHIE.0v#0.0.1`

***

### gefyra.sandbox

**Versions**

* `gefyra.sandbox#0.0.4`

***

### LogicNets.NCCN

**Versions**

* `LogicNets.NCCN#0.8.1`

***

### Genomics Reporting Implementation Guide

**Versions**

* `hl7.fhir.uv.genomics-reporting#3.0.0`
* `hl7.fhir.uv.genomics-reporting#3.0.0-ballot`
* `hl7.fhir.uv.genomics-reporting#2.0.0`
* `hl7.fhir.uv.genomics-reporting#1.1.0`
* `hl7.fhir.uv.genomics-reporting#1.0.0`
* `hl7.fhir.uv.genomics-reporting#0.3.0`
* `hl7.fhir.uv.genomics-reporting#0.1.0`

***

### SMART Health Cards: Vaccination & Testing Implementation Guide

**Versions**

* `hl7.fhir.uv.shc-vaccination#0.6.2`

***

### HL7 Belgium Vaccination (Patient Dossier)

**Versions**

* `hl7.fhir.be.vaccination#1.1.1`
* `hl7.fhir.be.vaccination#1.1.0`
* `hl7.fhir.be.vaccination#1.0.3`
* `hl7.fhir.be.vaccination#1.0.2`
* `hl7.fhir.be.vaccination#1.0.1`
* `hl7.fhir.be.vaccination#1.0.0`

***

### Israel Division of Governmental Hospitals Profiles

**Versions**

* `fhir.dgmc#0.1.3`
* `fhir.dgmc#0.1.2`
* `fhir.dgmc#0.1.1`

***

### Health Care Surveys Content Implementation Guide (IG)

**Versions**

* `hl7.fhir.us.health-care-surveys-reporting#1.0.0`
* `hl7.fhir.us.health-care-surveys-reporting#0.1.0`

***

### hl7.fhir.us.breastcancer

**Versions**

* `hl7.fhir.us.breastcancer#0.2.0`
* `hl7.fhir.us.breastcancer#0.1.0`

***

### FHIR Implementation of Vaccination-Immunization

**Versions**

* `nictiz.fhir.nl.r4.immunization#2.0.0-alpha.3`
* `nictiz.fhir.nl.r4.immunization#2.0.0-beta.2`
* `nictiz.fhir.nl.r4.immunization#2.0.0-beta.1`
* `nictiz.fhir.nl.r4.immunization#2.0.0`

***

### sfm.030322

**Versions**

* `sfm.030322#2.0.1`

***

### ca.infoway.io.erec

**Versions**

* `ca.infoway.io.erec#1.1.1-dft-ballot`
* `ca.infoway.io.erec#1.1.0-dft-ballot`
* `ca.infoway.io.erec#1.1.0-dft-ab`
* `ca.infoway.io.erec#1.0.3-dft-projectathon`
* `ca.infoway.io.erec#1.0.2-dft`
* `ca.infoway.io.erec#1.0.2-dft-projectathon`
* `ca.infoway.io.erec#1.0.1-dft`
* `ca.infoway.io.erec#1.0.0-dft`
* `ca.infoway.io.erec#1.0.0-dft-ballot`

***

### ereferralontario.core

**Versions**

* `ereferralontario.core#0.10.2`

***

### de.emperra.esysta

**Versions**

* `de.emperra.esysta#1.0.0`

***

### kvdigital.vermittlungscode-abrufen-pvs

**Versions**

* `kvdigital.vermittlungscode-abrufen-pvs#1.3.0`
* `kvdigital.vermittlungscode-abrufen-pvs#1.1.0`
* `kvdigital.vermittlungscode-abrufen-pvs#1.0.0`

***

### FHIR Human Services Directory

**Versions**

* `hl7.fhir.us.hsds#1.0.0`
* `hl7.fhir.us.hsds#1.0.0-ballot`

***

### CH RAD-Order (R4)

**Versions**

* `ch.fhir.ig.ch-rad-order#2.0.0`
* `ch.fhir.ig.ch-rad-order#2.0.0-ballot`
* `ch.fhir.ig.ch-rad-order#1.0.0`
* `ch.fhir.ig.ch-rad-order#0.1.0`

***

### OntarioContextManagement.core

**Versions**

* `OntarioContextManagement.core#0.1.0`

***

### MoPH Primary Care 1 (MoPH-PC-1) - FHIR Implementation Guide (STU2)

**Versions**

* `silth.fhir.th.mophpc1#1.0.0`
* `silth.fhir.th.mophpc1#0.1.1`

***

### Finance and Insurance Service (FAIS)

**Versions**

* `ihe.iti.fais#1.0.0`
* `ihe.iti.fais#1.0.0-comment`

***

### HL7 Europe Laboratory Report

**Versions**

* `hl7.fhir.eu.laboratory#0.1.1`
* `hl7.fhir.eu.laboratory#0.1.0`
* `hl7.fhir.eu.laboratory#0.1.0-ballot`

***

### Mobile Antepartum Summary

**Versions**

* `ihe.pcc.maps#1.0.0-comment`

***

### DK MedCom Condition List

**Versions**

* `medcom.fhir.dk.conditionlist#1.0.0`

***

### vzvz.covid-vaccinations

**Versions**

* `vzvz.covid-vaccinations#0.5.1-beta`
* `vzvz.covid-vaccinations#0.5.0-beta`
* `vzvz.covid-vaccinations#0.4.0-beta`
* `vzvz.covid-vaccinations#0.3.1-beta`
* `vzvz.covid-vaccinations#0.3.0-beta`
* `vzvz.covid-vaccinations#0.2.1-beta`
* `vzvz.covid-vaccinations#0.2.0-beta`
* `vzvz.covid-vaccinations#0.1.3`
* `vzvz.covid-vaccinations#0.1.2`
* `vzvz.covid-vaccinations#0.1.1`
* `vzvz.covid-vaccinations#0.1.0`

***

### elona.health

**Versions**

* `elona.health#1.0.3`
* `elona.health#1.0.1`
* `elona.health#1.0.0`

***

### SocialCareDataService.STU3

**Versions**

* `SocialCareDataService.STU3#0.0.1`

***

### FHIR 6.0.0-ballot3 package : Expansions

**Versions**

* `hl7.fhir.r6.expansions#6.0.0-ballot3`
* `hl7.fhir.r6.expansions#6.0.0-ballot2`
* `hl7.fhir.r6.expansions#6.0.0-ballot1`

***

### RIVO-Noord Zorgviewer Implementation Guide

**Versions**

* `hl7.fhir.nl.zorgviewer#1.14.0`
* `hl7.fhir.nl.zorgviewer#1.13.0`
* `hl7.fhir.nl.zorgviewer#1.12.0`
* `hl7.fhir.nl.zorgviewer#1.10.0`
* `hl7.fhir.nl.zorgviewer#1.8.0`
* `hl7.fhir.nl.zorgviewer#1.6.0`
* `hl7.fhir.nl.zorgviewer#1.5.0`
* `hl7.fhir.nl.zorgviewer#1.4.0`
* `hl7.fhir.nl.zorgviewer#1.3.0`
* `hl7.fhir.nl.zorgviewer#1.2.0`
* `hl7.fhir.nl.zorgviewer#0.26.0`
* `hl7.fhir.nl.zorgviewer#0.25.0`
* `hl7.fhir.nl.zorgviewer#0.24.0`

***

### questinnaire.profiles

**Versions**

* `questinnaire.profiles#0.0.2`

***

### MII IG Prozedur

**Versions**

* `de.medizininformatikinitiative.kerndatensatz.prozedur#2025.0.0-rc.1`
* `de.medizininformatikinitiative.kerndatensatz.prozedur#2025.0.0`
* `de.medizininformatikinitiative.kerndatensatz.prozedur#2025.0.0-alpha2`
* `de.medizininformatikinitiative.kerndatensatz.prozedur#2025.0.0-alpha1`
* `de.medizininformatikinitiative.kerndatensatz.prozedur#2024.0.0`
* `de.medizininformatikinitiative.kerndatensatz.prozedur#2024.0.0-ballot`
* `de.medizininformatikinitiative.kerndatensatz.prozedur#2024.0.0-alpha8`
* `de.medizininformatikinitiative.kerndatensatz.prozedur#1.0.8`

***

### How to Publish a FHIR Implementation Guide

**Versions**

* `ca.argentixinfo.howtopub#1.1.0`
* `ca.argentixinfo.howtopub#1.0.0`

***

### CH LAB-Report (R4)

**Versions**

* `ch.fhir.ig.ch-lab-report#1.0.0`
* `ch.fhir.ig.ch-lab-report#1.0.0-ballot`
* `ch.fhir.ig.ch-lab-report#0.1.1`
* `ch.fhir.ig.ch-lab-report#0.1.0`

***

### HRSA 2024 Uniform Data System (UDS) Patient Level Submission (PLS) (UDS+) FHIR IG

**Versions**

* `fhir.hrsa.uds-plus#2.0.0`
* `fhir.hrsa.uds-plus#1.1.0`
* `fhir.hrsa.uds-plus#1.0.1`

***

### Finnish Implementation Guide for SMART App Launch

**Versions**

* `hl7.fhir.fi.smart#2.0.0-rc1`
* `hl7.fhir.fi.smart#1.0.0`
* `hl7.fhir.fi.smart#1.0.0-rc9`
* `hl7.fhir.fi.smart#1.0.0-rc8`
* `hl7.fhir.fi.smart#1.0.0-rc7`
* `hl7.fhir.fi.smart#1.0.0-rc6`
* `hl7.fhir.fi.smart#1.0.0-rc5`
* `hl7.fhir.fi.smart#1.0.0-rc2`
* `hl7.fhir.fi.smart#1.0.0-rc1`

***

### This package contains the Koppeltaal 2.0 profiles

**Versions**

* `koppeltaalv2.00#0.14.0-beta.13`
* `koppeltaalv2.00#0.14.0-beta.12`
* `koppeltaalv2.00#0.14.0-beta.11`
* `koppeltaalv2.00#0.14.0-beta.10`
* `koppeltaalv2.00#0.14.0-beta.9`
* `koppeltaalv2.00#0.14.0-beta.8`
* `koppeltaalv2.00#0.14.0-beta.7`
* `koppeltaalv2.00#0.14.0-beta.6`
* `koppeltaalv2.00#0.14.0-beta.5`
* `koppeltaalv2.00#0.14.0-beta.4`
* `koppeltaalv2.00#0.14.0-beta.3`
* `koppeltaalv2.00#0.14.0-beta.2`
* `koppeltaalv2.00#0.14.0-beta.1`
* `koppeltaalv2.00#0.12.0-beta.6`
* `koppeltaalv2.00#0.12.0-beta.5`
* `koppeltaalv2.00#0.12.0-beta.4`
* `koppeltaalv2.00#0.12.0-beta.3`
* `koppeltaalv2.00#0.12.0-beta.2`
* `koppeltaalv2.00#0.12.0-beta.1`
* `koppeltaalv2.00#0.14.1`
* `koppeltaalv2.00#0.14.0`
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

***

### FHIR Core package

**Versions**

* `hl7.fhir.r4b.core#4.3.0`
* `hl7.fhir.r4b.core#4.1.0`

***

### EHR and PHR System Functional Models - Record Lifecycle Events Implementation Guide

**Versions**

* `hl7.fhir.uv.ehrs-rle#1.1.0`
* `hl7.fhir.uv.ehrs-rle#1.1.0-ballot`
* `hl7.fhir.uv.ehrs-rle#1.0.0-ballot`

***

### laniado.test.fhir.r4

**Versions**

* `laniado.test.fhir.r4#0.1.92`
* `laniado.test.fhir.r4#0.1.91`
* `laniado.test.fhir.r4#0.1.90`
* `laniado.test.fhir.r4#0.1.80`
* `laniado.test.fhir.r4#0.1.73`
* `laniado.test.fhir.r4#0.1.72`
* `laniado.test.fhir.r4#0.1.71`
* `laniado.test.fhir.r4#0.1.70`
* `laniado.test.fhir.r4#0.1.69`
* `laniado.test.fhir.r4#0.1.68`
* `laniado.test.fhir.r4#0.1.67`
* `laniado.test.fhir.r4#0.1.66`
* `laniado.test.fhir.r4#0.1.65`
* `laniado.test.fhir.r4#0.1.64`
* `laniado.test.fhir.r4#0.1.63`
* `laniado.test.fhir.r4#0.1.62`
* `laniado.test.fhir.r4#0.1.61`
* `laniado.test.fhir.r4#0.1.60`
* `laniado.test.fhir.r4#0.1.57`
* `laniado.test.fhir.r4#0.1.56`
* `laniado.test.fhir.r4#0.1.55`
* `laniado.test.fhir.r4#0.1.54`
* `laniado.test.fhir.r4#0.1.53`
* `laniado.test.fhir.r4#0.1.52`
* `laniado.test.fhir.r4#0.1.51`
* `laniado.test.fhir.r4#0.1.50`
* `laniado.test.fhir.r4#0.1.49`
* `laniado.test.fhir.r4#0.1.48`
* `laniado.test.fhir.r4#0.1.47`
* `laniado.test.fhir.r4#0.1.46`
* `laniado.test.fhir.r4#0.1.45`
* `laniado.test.fhir.r4#0.1.44`
* `laniado.test.fhir.r4#0.1.43`
* `laniado.test.fhir.r4#0.1.42`
* `laniado.test.fhir.r4#0.1.41`
* `laniado.test.fhir.r4#0.1.40`
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

***

### ca.on.oh-setp

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
* `ca.on.oh-setp#1.0.39`
* `ca.on.oh-setp#1.0.38`
* `ca.on.oh-setp#1.0.37`
* `ca.on.oh-setp#1.0.36`
* `ca.on.oh-setp#1.0.35`
* `ca.on.oh-setp#1.0.34`

***

### Danish Implementation Guide for SMART App Launch

**Versions**

* `hl7.fhir.dk.smart#1.0.0`

***

### de.gematik.dev.epa.medication

**Versions**

* `de.gematik.dev.epa.medication#1.1.0-alpha.6`
* `de.gematik.dev.epa.medication#1.1.0-alpha.5`
* `de.gematik.dev.epa.medication#1.1.0-alpha.4`

***

### MII IG Laborbefund

**Versions**

* `de.medizininformatikinitiative.kerndatensatz.laborbefund#2025.0.2`
* `de.medizininformatikinitiative.kerndatensatz.laborbefund#2025.0.1`
* `de.medizininformatikinitiative.kerndatensatz.laborbefund#2025.0.0`
* `de.medizininformatikinitiative.kerndatensatz.laborbefund#2025.0.0-alpha2`
* `de.medizininformatikinitiative.kerndatensatz.laborbefund#2025.0.0-alpha1`
* `de.medizininformatikinitiative.kerndatensatz.laborbefund#1.0.6`
* `de.medizininformatikinitiative.kerndatensatz.laborbefund#1.0.7-alpha1`

***

### alpha.core.r4

**Versions**

* `alpha.core.r4#1.0.0`

***

### kbv.all.st

**Versions**

* `kbv.all.st#1.21.0`
* `kbv.all.st#1.20.0`
* `kbv.all.st#1.19.0`
* `kbv.all.st#1.18.0`
* `kbv.all.st#1.17.0`
* `kbv.all.st#1.16.0`
* `kbv.all.st#1.15.0`
* `kbv.all.st#1.14.0`
* `kbv.all.st#1.13.0`
* `kbv.all.st#1.12.0`
* `kbv.all.st#1.11.0`
* `kbv.all.st#1.10.0`
* `kbv.all.st#1.8.0`
* `kbv.all.st#1.7.0`
* `kbv.all.st#1.6.0`
* `kbv.all.st#1.5.0`
* `kbv.all.st#1.4.0`
* `kbv.all.st#1.3.1`

***

### acme.dallas.cowboy

**Versions**

* `acme.dallas.cowboy#0.1.0-alpha`

***

### acme.minneapolis.cowboy

**Versions**

* `acme.minneapolis.cowboy#0.1.0`

***

### implementation.guide.nhdr

**Versions**

* `implementation.guide.nhdr#1.0.0`

***

### eVO.himi

**Versions**

* `eVO.himi#0.0.1`

***

### odilab.evo

**Versions**

* `odilab.evo#0.0.5`
* `odilab.evo#0.0.4`
* `odilab.evo#0.0.3`
* `odilab.evo#0.0.2`
* `odilab.evo#0.0.1`

***

### fhir.argonaut.clinicalnotes

**Versions**

* `fhir.argonaut.clinicalnotes#1.0.0`

***

### cidadex.testex

**Versions**

* `cidadex.testex#1.0.1-beta`

***

### fhir.argonaut.questionnaire

**Versions**

* `fhir.argonaut.questionnaire#1.0.0`

***

### Rastreamento de Câncer de Colo de Útero

**Versions**

* `br.gov.saude.ccu#0.0.1`

***

### furore.test.fsh.demo

**Versions**

* `furore.test.fsh.demo#0.1.0-rc1`

***

### kbv.all.st-rc

**Versions**

* `kbv.all.st-rc#1.9.0`
* `kbv.all.st-rc#1.8.0`
* `kbv.all.st-rc#1.7.0`
* `kbv.all.st-rc#1.6.0`
* `kbv.all.st-rc#1.5.0`
* `kbv.all.st-rc#1.4.0`
* `kbv.all.st-rc#1.3.0`
* `kbv.all.st-rc#1.2.0`

***

### rdc.interoperability.guide

**Versions**

* `rdc.interoperability.guide#1.0.0`

***

### Patient Demographics Query for mobile (PDQm)

**Versions**

* `IHE.ITI.PDQm#2.3.0`
* `IHE.ITI.PDQm#2.2.1`

***

### Terminology Support (r4)

**Versions**

* `fhir.tx.support.r4#0.28.0`
* `fhir.tx.support.r4#0.27.0`
* `fhir.tx.support.r4#0.26.0`
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

***

### de.gematik.isik-vitalparameter

**Versions**

* `de.gematik.isik-vitalparameter#4.0.2`
* `de.gematik.isik-vitalparameter#4.0.1`
* `de.gematik.isik-vitalparameter#4.0.0`
* `de.gematik.isik-vitalparameter#4.0.0-rc2`
* `de.gematik.isik-vitalparameter#4.0.0-rc`
* `de.gematik.isik-vitalparameter#4.0.0-alpha-validation-MII`
* `de.gematik.isik-vitalparameter#4.0.0-alpha-validation-MII-2`
* `de.gematik.isik-vitalparameter#3.0.5`
* `de.gematik.isik-vitalparameter#3.0.4`
* `de.gematik.isik-vitalparameter#3.0.3`
* `de.gematik.isik-vitalparameter#3.0.2`
* `de.gematik.isik-vitalparameter#3.0.1`
* `de.gematik.isik-vitalparameter#2.0.4`
* `de.gematik.isik-vitalparameter#2.0.3`

***

### eMERGEClinicalGeneticsReports.FHIRExtensionResources

**Versions**

* `eMERGEClinicalGeneticsReports.FHIRExtensionResources#0.1.3`
* `eMERGEClinicalGeneticsReports.FHIRExtensionResources#0.1.2`

***

### commonwell-consent-trial01.01

**Versions**

* `commonwell-consent-trial01.01#0.0.1`

***

### e44.evo13

**Versions**

* `e44.evo13#1.1.0`

***

### iknl.fhir.nl.r4.performation

**Versions**

* `iknl.fhir.nl.r4.performation#0.2.0`
* `iknl.fhir.nl.r4.performation#0.1.0`

***

### Common FHIR profile vendor collaboration

**Versions**

* `care.commonprofiles.fhir#1.0.1`
* `care.commonprofiles.fhir#1.0.0`

***

### Using CQL with FHIR

**Versions**

* `hl7.fhir.uv.cql#2.0.0-ballot`
* `hl7.fhir.uv.cql#1.0.0`
* `hl7.fhir.uv.cql#1.0.0-snapshot`
* `hl7.fhir.uv.cql#1.0.0-ballot`

***

### Nomenclatures des objets de santé (NOS)

**Versions**

* `ans.fr.nos#1.5.0`
* `ans.fr.nos#1.4.0`
* `ans.fr.nos#1.3.0`
* `ans.fr.nos#1.2.0`
* `ans.fr.nos#1.1.0`

***

### Da Vinci Unsolicited Notifications

**Versions**

* `hl7.fhir.us.davinci-alerts#1.1.0`
* `hl7.fhir.us.davinci-alerts#1.1.0-preview`
* `hl7.fhir.us.davinci-alerts#1.0.0`
* `hl7.fhir.us.davinci-alerts#0.2.0`
* `hl7.fhir.us.davinci-alerts#0.1.0`

***

### Basic Audit Log Patterns (BALP)

**Versions**

* `ihe.iti.balp#1.1.3`
* `ihe.iti.balp#1.1.2`
* `ihe.iti.balp#1.1.1`
* `ihe.iti.balp#1.1.0`

***

### MedNet interface implementation guide

**Versions**

* `TigaCoreHUB.Patient#1.1.0`

***

### uk.nhsdigital.r4

**Versions**

* `uk.nhsdigital.r4#2.11.0`
* `uk.nhsdigital.r4#2.10.0`
* `uk.nhsdigital.r4#2.9.0`
* `uk.nhsdigital.r4#2.8.0`
* `uk.nhsdigital.r4#2.7.0`
* `uk.nhsdigital.r4#2.6.1`
* `uk.nhsdigital.r4#2.6.0`
* `uk.nhsdigital.r4#2.5.2`
* `uk.nhsdigital.r4#2.5.1`
* `uk.nhsdigital.r4#2.5.0`
* `uk.nhsdigital.r4#2.4.7`
* `uk.nhsdigital.r4#2.4.6`
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
* `uk.nhsdigital.r4#2.0.14-dev`

***

### US Situational Awareness Framework for Reporting (US SAFR) Implementation Guide

**Versions**

* `hl7.fhir.us.safr#1.0.0-ballot`

***

### de.gematik.epa.medication

**Versions**

* `de.gematik.epa.medication#1.0.5-ballot.1`
* `de.gematik.epa.medication#1.1.1`
* `de.gematik.epa.medication#1.1.0-rc1`
* `de.gematik.epa.medication#1.0.5`
* `de.gematik.epa.medication#1.0.3`
* `de.gematik.epa.medication#1.0.2`
* `de.gematik.epa.medication#1.0.1`
* `de.gematik.epa.medication#1.0.0`
* `de.gematik.epa.medication#1.0.5-1`
* `de.gematik.epa.medication#1.0.2-rc1`
* `de.gematik.epa.medication#1.0.0-RC`
* `de.gematik.epa.medication#0.0.3`

***

### Essais CLiniques Accessibles Interconnectés pour la Recherche ouverts à l'Ecosystème

**Versions**

* `ans.fhir.fr.eclaire#0.3.0`
* `ans.fhir.fr.eclaire#0.2.0`
* `ans.fhir.fr.eclaire#0.1.0`

***

### NHSN Healthcare Associated Infection (HAI) Reports

**Versions**

* `hl7.fhir.us.hai#2.1.0`
* `hl7.fhir.us.hai#2.0.0`
* `hl7.fhir.us.hai#1.1.0`
* `hl7.fhir.us.hai#1.0.0`
* `hl7.fhir.us.hai#0.1.0`

***

### gdrl.fhir.r4

**Versions**

* `gdrl.fhir.r4#1.3.1`
* `gdrl.fhir.r4#1.3.0`
* `gdrl.fhir.r4#1.2.0`
* `gdrl.fhir.r4#1.2.0-----beta`
* `gdrl.fhir.r4#1.1.0`
* `gdrl.fhir.r4#1.0.0`

***

### ca.bc.bcy-ids

**Versions**

* `ca.bc.bcy-ids#1.0.0`

***

### HL7 Terminology (THO)

**Versions**

* `hl7.terminology#6.3.0`
* `hl7.terminology#6.2.0`
* `hl7.terminology#6.1.0`
* `hl7.terminology#6.0.2`
* `hl7.terminology#6.0.1`
* `hl7.terminology#6.0.0`
* `hl7.terminology#5.5.0`
* `hl7.terminology#5.4.0`
* `hl7.terminology#5.3.0`
* `hl7.terminology#5.2.0`
* `hl7.terminology#5.1.0`
* `hl7.terminology#4.0.0`
* `hl7.terminology#3.1.0`
* `hl7.terminology#3.0.0`
* `hl7.terminology#2.1.0`
* `hl7.terminology#2.0.0`
* `hl7.terminology#1.0.0`

***

### leumit.fhir.r4

**Versions**

* `leumit.fhir.r4#0.5.0`
* `leumit.fhir.r4#0.4.0`
* `leumit.fhir.r4#0.3.0`
* `leumit.fhir.r4#0.2.0`
* `leumit.fhir.r4#0.1.0`

***

### pbmesolutions.v1.fhir

**Versions**

* `pbmesolutions.v1.fhir#1.1.1`
* `pbmesolutions.v1.fhir#1.1.0`

***

### Argonaut Provider Directory Guide

**Versions**

* `fhir.argonaut.pd#1.0.0`
* `fhir.argonaut.pd#0.1.0`

***

### ca.on.oh.corhealth

**Versions**

* `ca.on.oh.corhealth#0.2.0-beta`

***

### hl7.fhir.nume.dev

**Versions**

* `hl7.fhir.nume.dev#1.0.3`
* `hl7.fhir.nume.dev#1.0.2`
* `hl7.fhir.nume.dev#1.0.1`
* `hl7.fhir.nume.dev#1.0.0`

***

### kbv.mio.ddtk

**Versions**

* `kbv.mio.ddtk#1.0.0-kommentierung`

***

### HL7 Terminology (THO)

**Versions**

* `hl7.terminology.r3#6.3.0`
* `hl7.terminology.r3#6.2.0`
* `hl7.terminology.r3#6.1.0`
* `hl7.terminology.r3#6.0.2`
* `hl7.terminology.r3#6.0.1`
* `hl7.terminology.r3#6.0.0`
* `hl7.terminology.r3#5.5.0`
* `hl7.terminology.r3#5.4.0`
* `hl7.terminology.r3#5.3.0`
* `hl7.terminology.r3#5.2.0`
* `hl7.terminology.r3#5.1.0`
* `hl7.terminology.r3#5.0.0`
* `hl7.terminology.r3#4.0.0`
* `hl7.terminology.r3#3.1.0`
* `hl7.terminology.r3#3.0.0`
* `hl7.terminology.r3#2.1.0`
* `hl7.terminology.r3#2.0.0`
* `hl7.terminology.r3#1.0.0`

***

### Dk Terminology for XDS Metadata

**Versions**

* `medcom.fhir.dk.xdsmetadata#1.0.0`

***

### Pharmaceutical Quality (Industry)

**Versions**

* `hl7.fhir.uv.pharm-quality#1.0.0`
* `hl7.fhir.uv.pharm-quality#1.0.0-ballot`

***

### Adverse Event Clinical Research

**Versions**

* `hl7.fhir.uv.ae-research-ig#1.0.1`
* `hl7.fhir.uv.ae-research-ig#1.0.0`
* `hl7.fhir.uv.ae-research-ig#1.0.0-ballot`

***

### testprojekt.sl.r4

**Versions**

* `testprojekt.sl.r4#0.0.2`
* `testprojekt.sl.r4#0.0.1`

***

### Modelisationdesstructuresetdesprofessionnels.sept2021

**Versions**

* `Modelisationdesstructuresetdesprofessionnels.sept2021#0.1.0`

***

### fi.digious.kanta.test

**Versions**

* `fi.digious.kanta.test#0.0.1-preview-2`
* `fi.digious.kanta.test#0.0.1-preview-1`

***

### fhirtest.project

**Versions**

* `fhirtest.project#0.1.0`

***

### CanonicalVersioningTest-0.01.01

**Versions**

* `CanonicalVersioningTest-0.01.01#0.01.01-alpha`

***

### test.no.basis

**Versions**

* `test.no.basis#2.2.0-beta3`
* `test.no.basis#2.2.0-beta2`
* `test.no.basis#2.2.0-beta`
* `test.no.basis#2.2.0-alpha`

***

### eu.epi.jan2025

**Versions**

* `eu.epi.jan2025#1.0.0`

***

### WHO SMART Trust

**Versions**

* `smart.who.int.trust#1.1.6`
* `smart.who.int.trust#1.1.5`
* `smart.who.int.trust#1.1.4`

***

### kvdigital.terminsynchronisation-tvs

**Versions**

* `kvdigital.terminsynchronisation-tvs#1.0.0-beta`

***

### Vital Records Death Reporting (VRDR) FHIR Implementation Guide

**Versions**

* `hl7.fhir.us.vrdr#3.0.0`
* `hl7.fhir.us.vrdr#3.0.0-ballot`
* `hl7.fhir.us.vrdr#2.2.0`
* `hl7.fhir.us.vrdr#2.1.0`
* `hl7.fhir.us.vrdr#2.0.0`
* `hl7.fhir.us.vrdr#1.2.0`
* `hl7.fhir.us.vrdr#1.0.0`
* `hl7.fhir.us.vrdr#0.1.0`

***

### cens.fhir.poclis

**Versions**

* `cens.fhir.poclis#1.0.0`

***

### de.gematik.isik-medikation

**Versions**

* `de.gematik.isik-medikation#4.0.3`
* `de.gematik.isik-medikation#4.0.2`
* `de.gematik.isik-medikation#4.0.1`
* `de.gematik.isik-medikation#4.0.0`
* `de.gematik.isik-medikation#4.0.0-rc2`
* `de.gematik.isik-medikation#4.0.0-rc`
* `de.gematik.isik-medikation#3.0.5`
* `de.gematik.isik-medikation#3.0.4`
* `de.gematik.isik-medikation#3.0.3`
* `de.gematik.isik-medikation#3.0.2`
* `de.gematik.isik-medikation#3.0.1`
* `de.gematik.isik-medikation#3.0.0`
* `de.gematik.isik-medikation#2.0.5`
* `de.gematik.isik-medikation#2.0.4`
* `de.gematik.isik-medikation#2.0.3`
* `de.gematik.isik-medikation#2.0.2`

***

### de.bbmri.fhir

**Versions**

* `de.bbmri.fhir#1.2.0`
* `de.bbmri.fhir#1.1.0`

***

### PharmacyClaimProfile.test

**Versions**

* `PharmacyClaimProfile.test#1.0.1-beta`

***

### qualitype.fhir.samples

**Versions**

* `qualitype.fhir.samples#1.0.0`

***

### STB.DRAFT

**Versions**

* `STB.DRAFT#1.29.0`

***

### uk.nhsdigital.medicines.r4

**Versions**

* `uk.nhsdigital.medicines.r4#2.7.9`
* `uk.nhsdigital.medicines.r4#2.7.1`
* `uk.nhsdigital.medicines.r4#2.6.0`
* `uk.nhsdigital.medicines.r4#2.5.0`
* `uk.nhsdigital.medicines.r4#2.3.0`
* `uk.nhsdigital.medicines.r4#2.2.1`
* `uk.nhsdigital.medicines.r4#2.2.0`
* `uk.nhsdigital.medicines.r4#2.1.9-alpha`
* `uk.nhsdigital.medicines.r4#2.1.8-alpha`
* `uk.nhsdigital.medicines.r4#2.1.7-alpha`
* `uk.nhsdigital.medicines.r4#2.1.6-alpha`
* `uk.nhsdigital.medicines.r4#2.1.5-alpha`
* `uk.nhsdigital.medicines.r4#2.1.4-alpha`
* `uk.nhsdigital.medicines.r4#2.1.3-alpha`
* `uk.nhsdigital.medicines.r4#2.1.2-alpha`
* `uk.nhsdigital.medicines.r4#2.1.14-alpha`
* `uk.nhsdigital.medicines.r4#2.1.13-alpha`
* `uk.nhsdigital.medicines.r4#2.1.12-alpha`
* `uk.nhsdigital.medicines.r4#2.1.11-alpha`
* `uk.nhsdigital.medicines.r4#2.1.10-alpha`
* `uk.nhsdigital.medicines.r4#2.1.1-alpha`
* `uk.nhsdigital.medicines.r4#2.1.0-alpha`
* `uk.nhsdigital.medicines.r4#2.0.37-alpha`
* `uk.nhsdigital.medicines.r4#2.0.36-alpha`
* `uk.nhsdigital.medicines.r4#2.0.35-alpha`
* `uk.nhsdigital.medicines.r4#2.0.34-alpha`
* `uk.nhsdigital.medicines.r4#2.0.33-alpha`
* `uk.nhsdigital.medicines.r4#2.0.32-alpha`
* `uk.nhsdigital.medicines.r4#2.0.31-alpha`
* `uk.nhsdigital.medicines.r4#2.0.30-alpha`
* `uk.nhsdigital.medicines.r4#2.0.29-alpha`
* `uk.nhsdigital.medicines.r4#2.0.28-alpha`
* `uk.nhsdigital.medicines.r4#2.0.27-alpha`
* `uk.nhsdigital.medicines.r4#2.0.26-alpha`
* `uk.nhsdigital.medicines.r4#2.0.25-alpha`
* `uk.nhsdigital.medicines.r4#2.0.24-alpha`
* `uk.nhsdigital.medicines.r4#2.0.23-alpha`
* `uk.nhsdigital.medicines.r4#2.0.22-alpha`
* `uk.nhsdigital.medicines.r4#2.0.21-alpha`
* `uk.nhsdigital.medicines.r4#2.0.20-alpha`
* `uk.nhsdigital.medicines.r4#2.0.19-alpha`
* `uk.nhsdigital.medicines.r4#2.0.18-alpha`
* `uk.nhsdigital.medicines.r4#2.0.17-alpha`

***

### FHIR Core package

**Versions**

* `hl7.fhir.core#3.5.0`
* `hl7.fhir.core#3.2.0`
* `hl7.fhir.core#1.8.0`
* `hl7.fhir.core#1.4.0`

***

### CygnetHealth.00.00.01

**Versions**

* `CygnetHealth.00.00.01#0.0.1`

***

### Tel Aviv Sourasky Medical Center

**Versions**

* `il.tasmc.fhir.r4#0.3.5`
* `il.tasmc.fhir.r4#0.3.4`
* `il.tasmc.fhir.r4#0.3.3`
* `il.tasmc.fhir.r4#0.3.2`
* `il.tasmc.fhir.r4#0.3.1`
* `il.tasmc.fhir.r4#0.3.0`
* `il.tasmc.fhir.r4#0.2.1`
* `il.tasmc.fhir.r4#0.2.0`
* `il.tasmc.fhir.r4#0.1.1`
* `il.tasmc.fhir.r4#0.1.0`

***

### Person-Centered Outcomes (PCO) Implementation Guide

**Versions**

* `hl7.fhir.us.pco#1.0.0-ballot`

***

### on.accdr.pkg

**Versions**

* `on.accdr.pkg#0.9.24-beta`

***

### CH AllergyIntolerance (R4)

**Versions**

* `ch.fhir.ig.ch-allergyintolerance#3.0.0`
* `ch.fhir.ig.ch-allergyintolerance#3.0.0-ballot`
* `ch.fhir.ig.ch-allergyintolerance#2.0.1`
* `ch.fhir.ig.ch-allergyintolerance#2.0.0`
* `ch.fhir.ig.ch-allergyintolerance#2.0.0-ballot`
* `ch.fhir.ig.ch-allergyintolerance#1.0.0`
* `ch.fhir.ig.ch-allergyintolerance#0.2.0`

***

### DGUV.Basis

**Versions**

* `DGUV.Basis#1.0.0`

***

### de.medizininformatikinitiative.kerndatensatz.icu

**Versions**

* `de.medizininformatikinitiative.kerndatensatz.icu#2025.0.0-ballot.1`
* `de.medizininformatikinitiative.kerndatensatz.icu#2025.0.4`
* `de.medizininformatikinitiative.kerndatensatz.icu#2025.0.3`
* `de.medizininformatikinitiative.kerndatensatz.icu#2025.0.2`
* `de.medizininformatikinitiative.kerndatensatz.icu#2025.0.1`
* `de.medizininformatikinitiative.kerndatensatz.icu#2025.0.0`
* `de.medizininformatikinitiative.kerndatensatz.icu#2025.0.0-ballot`
* `de.medizininformatikinitiative.kerndatensatz.icu#2024.0.0-alpha4`
* `de.medizininformatikinitiative.kerndatensatz.icu#2024.0.0-alpha3`
* `de.medizininformatikinitiative.kerndatensatz.icu#2024.0.0-alpha2`
* `de.medizininformatikinitiative.kerndatensatz.icu#2024.0.0-alpha1`
* `de.medizininformatikinitiative.kerndatensatz.icu#1.0.0`

***

### CDC MME CQL Calculator

**Versions**

* `fhir.cdc.opioid-mme-r4#3.0.0`

***

### Argonaut Scheduling Implementation Guide

**Versions**

* `fhir.argonaut.scheduling#1.0.0`

***

### progetto.eng

**Versions**

* `progetto.eng#0.0.1`

***

### eu.epi.february2025

**Versions**

* `eu.epi.february2025#1.0.0`

***

### dvmd.kdl.r4.2022

**Versions**

* `dvmd.kdl.r4.2022#2022.1.2`
* `dvmd.kdl.r4.2022#2022.1.1`

***

### Situational Awareness for Novel Epidemic Response

**Versions**

* `hl7.fhir.uv.saner#1.0.0`
* `hl7.fhir.uv.saner#0.1.0`

***

### acme.base

**Versions**

* `acme.base#2.0.3`
* `acme.base#2.0.2`
* `acme.base#2.0.1`
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

***

### ch.cel.thetest2-core

**Versions**

* `ch.cel.thetest2-core#0.8.1`

***

### Implementierungsleitfaden DEMIS - Erregernachweismeldung

**Versions**

* `rki.demis.r4.core#1.23.2`
* `rki.demis.r4.core#1.23.1`
* `rki.demis.r4.core#1.22.2`

***

### de.diga.abatonra

**Versions**

* `de.diga.abatonra#2.0.0`

***

### ca.on.oh-erec-ig

**Versions**

* `ca.on.oh-erec-ig#0.12.2-alpha1.0.1`

***

### HL7 FHIR Implementation Guide: Profiles for Transfusion and Vaccination Adverse Event Detection and Reporting

**Versions**

* `hl7.fhir.us.icsr-ae-reporting#1.0.1`
* `hl7.fhir.us.icsr-ae-reporting#1.0.0`
* `hl7.fhir.us.icsr-ae-reporting#0.1.0`

***

### CARIN Digital Insurance Card

**Versions**

* `hl7.fhir.us.insurance-card#1.1.0`
* `hl7.fhir.us.insurance-card#1.0.0`
* `hl7.fhir.us.insurance-card#0.1.0`

***

### MedMorph Research Data Exchange Content IG

**Versions**

* `hl7.fhir.us.medmorph-research-dex#0.1.0`

***

### EMIGA Organizationsverzeichnis Basisinhalte

**Versions**

* `rki.emiga.orgv#1.1.1`
* `rki.emiga.orgv#1.1.0`
* `rki.emiga.orgv#1.0.0`

***

### Partage de Documents de Santé en mobilité (PDSm)

**Versions**

* `ans.fhir.fr.pdsm#3.1.0`
* `ans.fhir.fr.pdsm#3.0.1`
* `ans.fhir.fr.pdsm#3.0.0`

***

### RapportEndoscopieQuebec.test

**Versions**

* `RapportEndoscopieQuebec.test#0.0.1-test`

***

### Implementation Guide for FFB reporting (FFB uddatasæt)

**Versions**

* `kl.dk.fhir.ffbreporting#1.0.0`

***

### de.transfer-abrechnungsdaten.r4

**Versions**

* `de.transfer-abrechnungsdaten.r4#1.1.2`
* `de.transfer-abrechnungsdaten.r4#1.1.1`
* `de.transfer-abrechnungsdaten.r4#1.1.0-beta`
* `de.transfer-abrechnungsdaten.r4#1.0.0`

***

### kbv.mio.patientenkurzakte

**Versions**

* `kbv.mio.patientenkurzakte#1.0.0`
* `kbv.mio.patientenkurzakte#1.0.0-kommentierung`
* `kbv.mio.patientenkurzakte#1.0.0-benehmensherstellung`

***

### fi.kela.kanta.pta.ajanvaraus.r4

**Versions**

* `fi.kela.kanta.pta.ajanvaraus.r4#0.1.0`

***

### eRS.STU3.Parameters

**Versions**

* `eRS.STU3.Parameters#1.0.0`

***

### ans.cnsa.fhir

**Versions**

* `ans.cnsa.fhir#0.1.2`
* `ans.cnsa.fhir#0.1.1`
* `ans.cnsa.fhir#0.1.0`
* `ans.cnsa.fhir#0.0.1`

***

### kbv.ita.for

**Versions**

* `kbv.ita.for#1.2.0`
* `kbv.ita.for#1.1.0`
* `kbv.ita.for#1.1.0-PreRelease`

***

### navify.fhir.r5.rdc

**Versions**

* `navify.fhir.r5.rdc#1.0.0`

***

### FHIR Extensions Pack

**Versions**

* `hl7.fhir.uv.extensions.r4#5.2.0`
* `hl7.fhir.uv.extensions.r4#5.1.0`
* `hl7.fhir.uv.extensions.r4#1.0.0`

***

### ca.on.oh-erec

**Versions**

* `ca.on.oh-erec#0.12.1-alpha1.0.3`
* `ca.on.oh-erec#0.12.1-alpha1.0.2`
* `ca.on.oh-erec#0.12.1-alpha1.0.1`

***

### Vital.MedikationsplanPlus

**Versions**

* `Vital.MedikationsplanPlus#1.1.0`
* `Vital.MedikationsplanPlus#1.0.0`
* `Vital.MedikationsplanPlus#0.3.0`
* `Vital.MedikationsplanPlus#0.2.0`
* `Vital.MedikationsplanPlus#0.1.0`

***

### Núcleo de Interoperabilidad de Datos (NID) - MINSAL

**Versions**

* `hl7.fhir.cl.minsal.nid#0.4.6`
* `hl7.fhir.cl.minsal.nid#0.4.5`
* `hl7.fhir.cl.minsal.nid#0.4.4`

***

### 醫療保險理賠實作指引

**Versions**

* `tw.cathay.fhir.iclaim#1.0.0`
* `tw.cathay.fhir.iclaim#0.1.3`
* `tw.cathay.fhir.iclaim#0.1.2`
* `tw.cathay.fhir.iclaim#0.1.1`
* `tw.cathay.fhir.iclaim#0.1.0`

***

### DK MedCom acknowledgement

**Versions**

* `medcom.fhir.dk.acknowledgement#2.0.2`
* `medcom.fhir.dk.acknowledgement#2.0.1`

***

### ICHOM Patient Centered Outcomes Measure Set for Breast Cancer

**Versions**

* `hl7.fhir.uv.ichom-breast-cancer#1.0.0`
* `hl7.fhir.uv.ichom-breast-cancer#1.0.0-ballot`

***

### test.public.project

**Versions**

* `test.public.project#0.0.2`
* `test.public.project#0.0.1`

***

### notts.scr.poc

**Versions**

* `notts.scr.poc#0.1.0`

***

### Personal Health Device Implementation Guide

**Versions**

* `hl7.fhir.uv.phd#2.0.0-ballot`
* `hl7.fhir.uv.phd#1.1.0`
* `hl7.fhir.uv.phd#1.0.0`
* `hl7.fhir.uv.phd#0.3.0`
* `hl7.fhir.uv.phd#0.2.0`

***

### FHIR implementation of zibs 2020

**Versions**

* `nictiz.fhir.nl.r4.nl-core#0.11.0-beta.1`
* `nictiz.fhir.nl.r4.nl-core#0.10.0-beta.1`
* `nictiz.fhir.nl.r4.nl-core#0.9.0-beta.1`
* `nictiz.fhir.nl.r4.nl-core#0.8.0-beta.1`
* `nictiz.fhir.nl.r4.nl-core#0.7.0-beta.1`
* `nictiz.fhir.nl.r4.nl-core#0.6.0-beta.2`
* `nictiz.fhir.nl.r4.nl-core#0.5.0-beta1`

***

### uk.nhsengland.genomics.r4

**Versions**

* `uk.nhsengland.genomics.r4#0.4.4`
* `uk.nhsengland.genomics.r4#0.4.3`
* `uk.nhsengland.genomics.r4#0.4.2`
* `uk.nhsengland.genomics.r4#0.4.1`
* `uk.nhsengland.genomics.r4#0.4.1-prerelease`
* `uk.nhsengland.genomics.r4#0.4.0-prerelease`
* `uk.nhsengland.genomics.r4#0.3.2`
* `uk.nhsengland.genomics.r4#0.3.1`
* `uk.nhsengland.genomics.r4#0.3.0`
* `uk.nhsengland.genomics.r4#0.2.0-prerelease`

***

### LS.fhir.r4.DevDays2019.labobservations

**Versions**

* `LS.fhir.r4.DevDays2019.labobservations#0.1.0-beta`

***

### KLChildren implementation guide, an implementation of FBU

**Versions**

* `kl.dk.fhir.children#2.1.0`
* `kl.dk.fhir.children#2.0.1`
* `kl.dk.fhir.children#2.0.0`
* `kl.dk.fhir.children#1.0.0`

***

### Breast Radiology Reporting - 1st STU ballot

**Versions**

* `hl7.fhir.us.breast-radiology#0.2.0`

***

### FHIR Data Segmentation for Privacy

**Versions**

* `hl7.fhir.uv.security-label-ds4p#1.0.0`
* `hl7.fhir.uv.security-label-ds4p#0.3.0`
* `hl7.fhir.uv.security-label-ds4p#0.2.0`
* `hl7.fhir.uv.security-label-ds4p#0.1.0`

***

### ch.fhir.BNW.ch-core

**Versions**

* `ch.fhir.BNW.ch-core#1.0.0`

***

### Implementierungsleitfaden DEMIS Common (Basismeldeinhalte)

**Versions**

* `rki.demis.common#2.0.0-alpha.1`
* `rki.demis.common#2.1.0`
* `rki.demis.common#2.0.0`
* `rki.demis.common#1.0.4`
* `rki.demis.common#1.0.3`
* `rki.demis.common#1.0.2`
* `rki.demis.common#1.0.1`
* `rki.demis.common#1.0.0`

***

### Pharmaceutical Quality - Chemistry, Manufacturing and Controls (PQ-CMC) Submissions to FDA

**Versions**

* `hl7.fhir.us.pq-cmc-fda#2.0.0-ballot`
* `hl7.fhir.us.pq-cmc-fda#1.0.0`
* `hl7.fhir.us.pq-cmc-fda#1.0.0-ballot`

***

### agha.fhir.genclipr

**Versions**

* `agha.fhir.genclipr#0.1.0`

***

### HL7 FHIR Implementation Guide: DK Core

**Versions**

* `hl7.fhir.dk.core#3.4.0`
* `hl7.fhir.dk.core#3.3.0`
* `hl7.fhir.dk.core#3.2.0`
* `hl7.fhir.dk.core#3.1.0`
* `hl7.fhir.dk.core#3.0.0`
* `hl7.fhir.dk.core#2.2.0`
* `hl7.fhir.dk.core#2.1.0`
* `hl7.fhir.dk.core#2.0.0`
* `hl7.fhir.dk.core#1.1.0`

***

### acme.base.r4

**Versions**

* `acme.base.r4#4.1.7`
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

***

### Taiwan Digital COVID-19 Certificate

**Versions**

* `hitstdio.tw.fhir.dcc#0.0.1`

***

### egde.health.gateway

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

***

### 電子病歷交換單張實作指引(EMR-IG)

**Versions**

* `tw.gov.mohw.emr#0.1.0`

***

### CH ORF (R4)

**Versions**

* `ch.fhir.ig.ch-orf#3.0.0`
* `ch.fhir.ig.ch-orf#3.0.0-ballot`
* `ch.fhir.ig.ch-orf#2.0.1`
* `ch.fhir.ig.ch-orf#2.0.0`
* `ch.fhir.ig.ch-orf#2.0.0-ballot`
* `ch.fhir.ig.ch-orf#1.0.0`
* `ch.fhir.ig.ch-orf#0.10.0`
* `ch.fhir.ig.ch-orf#0.9.1`

***

### Post-Acute Orders (PAO) (DME-Orders)

**Versions**

* `hl7.fhir.us.dme-orders#0.2.0`
* `hl7.fhir.us.dme-orders#0.1.0`

***

### rki.emiga.vzd

**Versions**

* `rki.emiga.vzd#2.0.0-alpha.4`
* `rki.emiga.vzd#2.0.0-alpha.3`
* `rki.emiga.vzd#2.0.0-alpha.2`
* `rki.emiga.vzd#1.0.0`

***

### Médicosocial - Transfert de données DUI

**Versions**

* `ans.fhir.fr.tddui#1.1.0`
* `ans.fhir.fr.tddui#1.1.0-ballot`
* `ans.fhir.fr.tddui#1.0.1`
* `ans.fhir.fr.tddui#1.0.1-ballot`

***

### apo.cdl.test

**Versions**

* `apo.cdl.test#0.0.1-alpha`

***

### hl7.at.fhir.gkl.ig-tooling

**Versions**

* `hl7.at.fhir.gkl.ig-tooling#0.3.0`
* `hl7.at.fhir.gkl.ig-tooling#0.2.0`
* `hl7.at.fhir.gkl.ig-tooling#0.1.0`

***

### CH CRL (R4)

**Versions**

* `ch.fhir.ig.ch-crl#0.9.0`
* `ch.fhir.ig.ch-crl#0.2.1`
* `ch.fhir.ig.ch-crl#0.2.0`
* `ch.fhir.ig.ch-crl#0.1.1`
* `ch.fhir.ig.ch-crl#0.1.0`

***

### nrlf.poc

**Versions**

* `nrlf.poc#1.0.4`
* `nrlf.poc#1.0.3`
* `nrlf.poc#1.0.2`

***

### MyHIE.v4-test

**Versions**

* `MyHIE.v4-test#2.1.0-alpha`

***

### Enhancing Oncology Model

**Versions**

* `globalalliant.us.eom#1.0.1-rc2`

***

### cisis.cds

**Versions**

* `cisis.cds#4.0.0`
* `cisis.cds#3.0.0`
* `cisis.cds#2.0.0`
* `cisis.cds#1.0.0`

***

### Estonian Base Implementation Guide

**Versions**

* `ee.fhir.base#1.1.1`
* `ee.fhir.base#1.1.0`

***

### vzvz.fhir.aof

**Versions**

* `vzvz.fhir.aof#0.15.0`
* `vzvz.fhir.aof#0.13.0`
* `vzvz.fhir.aof#0.12.3`
* `vzvz.fhir.aof#0.11.0-beta`
* `vzvz.fhir.aof#0.9.0-beta`
* `vzvz.fhir.aof#0.1.0-beta`

***

### AU Patient Summary Implementation Guide

**Versions**

* `hl7.fhir.au.ps#0.1.0-preview`

***

### careplanrt.eng

**Versions**

* `careplanrt.eng#1.0.1`

***

### d4l-data4life.covid-19.r4

**Versions**

* `d4l-data4life.covid-19.r4#0.8.0`
* `d4l-data4life.covid-19.r4#0.7.0`

***

### Estonian Base Implementation Guide

**Versions**

* `ee.hl7.fhir.base.r5#1.0.2`
* `ee.hl7.fhir.base.r5#1.0.1`

***

### iteyes.myhw.core

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

***

### Subscriptions R5 Backport

**Versions**

* `hl7.fhir.uv.subscriptions-backport#1.2.0-ballot`
* `hl7.fhir.uv.subscriptions-backport#1.1.0`
* `hl7.fhir.uv.subscriptions-backport#1.0.0`
* `hl7.fhir.uv.subscriptions-backport#0.1.0`

***

### National Healthcare Directory Attestation and Verification

**Versions**

* `hl7.fhir.us.directory-attestation#1.0.0-ballot`

***

### de.abda.eRezeptAbgabedaten

**Versions**

* `de.abda.eRezeptAbgabedaten#1.0.0`

***

### de.acticore.export

**Versions**

* `de.acticore.export#0.9.0`

***

### Medication

**Versions**

* `hl7.fhir.be.medication#1.0.0`

***

### CH EPR FHIR (R4)

**Versions**

* `ch.fhir.ig.ch-epr-fhir#4.0.1`
* `ch.fhir.ig.ch-epr-fhir#4.0.1-ballot`
* `ch.fhir.ig.ch-epr-fhir#4.0.1-ballot-2`
* `ch.fhir.ig.ch-epr-fhir#4.0.0-ballot`

***

### Immunization Decision Support Forecast (ImmDS) Implementation Guide

**Versions**

* `hl7.fhir.us.immds#1.0.0`

***

### synapxe.rcm.snapshots

**Versions**

* `synapxe.rcm.snapshots#1.0.0`

***

### Test20171286.neu

**Versions**

* `Test20171286.neu#0.8.15`

***

### NHSDigital.FHIR.STU3

**Versions**

* `NHSDigital.FHIR.STU3#1.0.0`

***

### CGM.FHIR.Workshop

**Versions**

* `CGM.FHIR.Workshop#1.0.0-beta`

***

### Subscriptions R5 Backport

**Versions**

* `hl7.fhir.uv.subscriptions-backport.r4#1.1.0`

***

### HL7 Terminology (THO)

**Versions**

* `hl7.terminology.r5#6.3.0`
* `hl7.terminology.r5#6.2.0`
* `hl7.terminology.r5#6.1.0`
* `hl7.terminology.r5#6.0.2`
* `hl7.terminology.r5#6.0.1`
* `hl7.terminology.r5#6.0.0`
* `hl7.terminology.r5#5.5.0`
* `hl7.terminology.r5#5.4.0`
* `hl7.terminology.r5#5.3.0`
* `hl7.terminology.r5#5.2.0`
* `hl7.terminology.r5#5.1.0`
* `hl7.terminology.r5#5.0.0`
* `hl7.terminology.r5#4.0.0`
* `hl7.terminology.r5#3.1.0`
* `hl7.terminology.r5#3.0.0`
* `hl7.terminology.r5#2.1.0`
* `hl7.terminology.r5#2.0.0`
* `hl7.terminology.r5#1.0.0`

***

### Patient Demographics Query for Mobile (PDQm)

**Versions**

* `ihe.iti.pdqm#3.1.0`
* `ihe.iti.pdqm#3.0.0`
* `ihe.iti.pdqm#3.0.0-comment`
* `ihe.iti.pdqm#2.4.0`

***

### HL7France-Clinicalprofils.072021

**Versions**

* `HL7France-Clinicalprofils.072021#0.1.0`

***

### us.nlm.vsac

**Versions**

* `us.nlm.vsac#0.21.0`
* `us.nlm.vsac#0.20.0`
* `us.nlm.vsac#0.19.0`
* `us.nlm.vsac#0.18.0`
* `us.nlm.vsac#0.17.0`
* `us.nlm.vsac#0.16.0`
* `us.nlm.vsac#0.15.0`
* `us.nlm.vsac#0.14.0`
* `us.nlm.vsac#0.13.0`
* `us.nlm.vsac#0.12.0`
* `us.nlm.vsac#0.11.0`
* `us.nlm.vsac#0.10.0`
* `us.nlm.vsac#0.9.0`
* `us.nlm.vsac#0.8.0`
* `us.nlm.vsac#0.7.0`
* `us.nlm.vsac#0.6.0`
* `us.nlm.vsac#0.5.0`
* `us.nlm.vsac#0.4.0`
* `us.nlm.vsac#0.3.0`
* `us.nlm.vsac#0.2.0`
* `us.nlm.vsac#0.1.0`

***

### healthdata.be.r4.dcd

**Versions**

* `healthdata.be.r4.dcd#0.2.0-beta`
* `healthdata.be.r4.dcd#0.1.0-beta`

***

### Structured Data Capture

**Versions**

* `hl7.fhir.uv.sdc.r4b#3.0.0`

***

### cens.fhir.poclis-ssmso

**Versions**

* `cens.fhir.poclis-ssmso#1.0.0`

***

### de.medizininformatikinitiative.kerndatensatz.biobank

**Versions**

* `de.medizininformatikinitiative.kerndatensatz.biobank#2025.0.4`
* `de.medizininformatikinitiative.kerndatensatz.biobank#2025.0.3`
* `de.medizininformatikinitiative.kerndatensatz.biobank#2025.0.2`
* `de.medizininformatikinitiative.kerndatensatz.biobank#2025.0.1`
* `de.medizininformatikinitiative.kerndatensatz.biobank#2025.0.0`
* `de.medizininformatikinitiative.kerndatensatz.biobank#2025.0.0-rc`
* `de.medizininformatikinitiative.kerndatensatz.biobank#2024.0.4`
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

***

### MyHealth@Eu Laboratory Report

**Versions**

* `myhealth.eu.fhir.laboratory#0.1.1`
* `myhealth.eu.fhir.laboratory#0.1.0`

***

### de.gevko.dev.te-tvz

**Versions**

* `de.gevko.dev.te-tvz#1.0.0`

***

### nhsd.assets.stu3

**Versions**

* `nhsd.assets.stu3#1.7.0`

***

### MyHealthWay

**Versions**

* `kr.mohw.myhealthway#1.0.2-beta.4`
* `kr.mohw.myhealthway#1.0.1`
* `kr.mohw.myhealthway#1.0.0`
* `kr.mohw.myhealthway#1.0.2-beta`
* `kr.mohw.myhealthway#0.1.1`

***

### DK MedCom Messaging

**Versions**

* `medcom.fhir.dk.messaging#2.2.1`
* `medcom.fhir.dk.messaging#2.2.0`
* `medcom.fhir.dk.messaging#2.1.0`
* `medcom.fhir.dk.messaging#2.0.0`

***

### HL7 Terminology (THO)

**Versions**

* `hl7.terminology.r4#6.3.0`
* `hl7.terminology.r4#6.2.0`
* `hl7.terminology.r4#6.1.0`
* `hl7.terminology.r4#6.0.2`
* `hl7.terminology.r4#6.0.1`
* `hl7.terminology.r4#6.0.0`
* `hl7.terminology.r4#5.5.0`
* `hl7.terminology.r4#5.4.0`
* `hl7.terminology.r4#5.3.0`
* `hl7.terminology.r4#5.2.0`
* `hl7.terminology.r4#5.1.0`
* `hl7.terminology.r4#5.0.0`
* `hl7.terminology.r4#4.0.0`
* `hl7.terminology.r4#3.1.0`
* `hl7.terminology.r4#3.0.0`
* `hl7.terminology.r4#2.1.0`
* `hl7.terminology.r4#2.0.0`
* `hl7.terminology.r4#1.0.0`

***

### TH Claim Consolidation (ClaimCon) - FHIR Implementation Guide (STU1)

**Versions**

* `silth.fhir.th.claimcon#0.1.2`

***

### ans.cisis.fhir.r4

**Versions**

* `ans.cisis.fhir.r4#3.0.0`
* `ans.cisis.fhir.r4#3.0.0-pat23v2`
* `ans.cisis.fhir.r4#3.0.0-pat23`
* `ans.cisis.fhir.r4#2.1.0`
* `ans.cisis.fhir.r4#2.0.0`
* `ans.cisis.fhir.r4#1.0.0`

***

### PACIO Advance Directive Interoperability Implementation Guide

**Versions**

* `hl7.fhir.us.pacio-adi#1.0.0`
* `hl7.fhir.us.pacio-adi#0.1.0`

***

### Da Vinci - Member Attribution (ATR) List

**Versions**

* `hl7.fhir.us.davinci-atr#2.0.0`
* `hl7.fhir.us.davinci-atr#2.0.0-ballot`
* `hl7.fhir.us.davinci-atr#1.0.0`
* `hl7.fhir.us.davinci-atr#0.1.0`

***

### Nictiz FHIR NL STU3 eOverdracht v4.1

**Versions**

* `nictiz.fhir.nl.stu3.eoverdracht#4.1.0-alpha.3`
* `nictiz.fhir.nl.stu3.eoverdracht#4.1.0-alpha.1`
* `nictiz.fhir.nl.stu3.eoverdracht#1.0.0-rc.23`
* `nictiz.fhir.nl.stu3.eoverdracht#1.0.0-rc.22`
* `nictiz.fhir.nl.stu3.eoverdracht#1.0.0-rc.21`
* `nictiz.fhir.nl.stu3.eoverdracht#4.0.0`
* `nictiz.fhir.nl.stu3.eoverdracht#1.0.0-rc9`
* `nictiz.fhir.nl.stu3.eoverdracht#1.0.0-rc8`
* `nictiz.fhir.nl.stu3.eoverdracht#1.0.0-rc7`
* `nictiz.fhir.nl.stu3.eoverdracht#1.0.0-rc6`
* `nictiz.fhir.nl.stu3.eoverdracht#1.0.0-rc5`
* `nictiz.fhir.nl.stu3.eoverdracht#1.0.0-rc3`
* `nictiz.fhir.nl.stu3.eoverdracht#1.0.0-rc15`
* `nictiz.fhir.nl.stu3.eoverdracht#1.0.0-rc14`
* `nictiz.fhir.nl.stu3.eoverdracht#1.0.0-rc11`
* `nictiz.fhir.nl.stu3.eoverdracht#1.0.0-rc10`
* `nictiz.fhir.nl.stu3.eoverdracht#1.0.0-rc1`
* `nictiz.fhir.nl.stu3.eoverdracht#0.3.0-beta1`
* `nictiz.fhir.nl.stu3.eoverdracht#0.2.0-beta4`
* `nictiz.fhir.nl.stu3.eoverdracht#0.2.0-beta3`
* `nictiz.fhir.nl.stu3.eoverdracht#0.2.0-beta2`
* `nictiz.fhir.nl.stu3.eoverdracht#0.1.0-beta1`

***

### medmij.dental.care.r4

**Versions**

* `medmij.dental.care.r4#1.0.0-alpha.3`
* `medmij.dental.care.r4#1.0.0-alpha.2`

***

### sunwayhie.r4

**Versions**

* `sunwayhie.r4#0.0.1-preview-1`

***

### Da Vinci - Payer Coverage Decision Exchange (PCDE)

**Versions**

* `hl7.fhir.us.davinci-pcde#1.0.0`
* `hl7.fhir.us.davinci-pcde#0.1.0`

***

### surescripts.specialty

**Versions**

* `surescripts.specialty#1.2.0-beta`
* `surescripts.specialty#1.1.0-beta`
* `surescripts.specialty#1.0.0-beta`

***

### DICOM® SR to FHIR Resource Mapping IG

**Versions**

* `hl7.fhir.uv.dicom-sr#1.0.0-ballot`

***

### Swissnoso Implementation Guide (R4)

**Versions**

* `ch.fhir.ig.swissnoso#1.0.0`
* `ch.fhir.ig.swissnoso#0.1.0`

***

### Nictiz FHIR NL STU3 Zib2020-preadopt

**Versions**

* `nictiz.fhir.nl.stu3.zib2020-preadopt#0.1.3`
* `nictiz.fhir.nl.stu3.zib2020-preadopt#0.1.2`
* `nictiz.fhir.nl.stu3.zib2020-preadopt#0.1.1`
* `nictiz.fhir.nl.stu3.zib2020-preadopt#0.1.0`
* `nictiz.fhir.nl.stu3.zib2020-preadopt#0.0.1-beta1`

***

### IHE ITI Basic AuditEvent Implementation Guide

**Versions**

* `ihe.iti.basicaudit#1.0.1`

***

### ca.on.consent.r4

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

***

### Clinical Quality Framework Common FHIR Assets

**Versions**

* `fhir.cqf.common#4.0.1`

***

### Australian Provider Directory IG

**Versions**

* `hl7.fhir.au.pd#2.0.1`
* `hl7.fhir.au.pd#1.0.0`
* `hl7.fhir.au.pd#0.9.0`
* `hl7.fhir.au.pd#0.5.0`
* `hl7.fhir.au.pd#0.4.0`
* `hl7.fhir.au.pd#0.3.0`
* `hl7.fhir.au.pd#0.2.0`
* `hl7.fhir.au.pd#0.1.0`

***

### ca.on.oh-cms

**Versions**

* `ca.on.oh-cms#1.0.2-alpha1.0.2`
* `ca.on.oh-cms#1.0.1-alpha1.0.1`

***

### Guia de Implementação da SES GO - CORE

**Versions**

* `br.go.ses.core#0.0.1`

***

### Patient Master Identity Registry (PMIR)

**Versions**

* `ihe.iti.pmir#1.5.0`
* `ihe.iti.pmir#1.4.0`

***

### Estonian Terminology resources

**Versions**

* `ee.fhir.terminology#0.2.2`
* `ee.fhir.terminology#0.2.1`
* `ee.fhir.terminology#0.2.0`
* `ee.fhir.terminology#0.0.1`

***

### Guide d'implémentation de la télésurveillance

**Versions**

* `ans.fhir.fr.telesurveillance#0.1.0`
* `ans.fhir.fr.telesurveillance#0.1.0-ballot`

***

### kbv.mio.zaeb

**Versions**

* `kbv.mio.zaeb#1.1.0`
* `kbv.mio.zaeb#1.1.0-benehmensherstellung`

***

### duwel.nl.r4.sandbox.dev

**Versions**

* `duwel.nl.r4.sandbox.dev#0.0.1-dev.4`
* `duwel.nl.r4.sandbox.dev#0.0.1-dev.3`
* `duwel.nl.r4.sandbox.dev#0.0.1-dev.2`

***

### Radiation Dose Summary for Diagnostic Procedures on FHIR

**Versions**

* `hl7.fhir.uv.radiation-dose-summary#0.1.0`

***

### hl7.fhir.stt

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

***

### fbe.babycare.app

**Versions**

* `fbe.babycare.app#1.0.1`

***

### Nictiz FHIR NL STU3 eAfspraak

**Versions**

* `nictiz.fhir.nl.stu3.eafspraak#1.0.6`
* `nictiz.fhir.nl.stu3.eafspraak#1.0.5`
* `nictiz.fhir.nl.stu3.eafspraak#1.0.3`
* `nictiz.fhir.nl.stu3.eafspraak#1.0.2`
* `nictiz.fhir.nl.stu3.eafspraak#1.0.1`
* `nictiz.fhir.nl.stu3.eafspraak#1.0.0`

***

### acme.canada

**Versions**

* `acme.canada#0.0.4`
* `acme.canada#0.0.3`
* `acme.canada#0.0.2`
* `acme.canada#0.0.1`

***

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

***

### Mobile Cross-Enterprise Document Data Element Extraction (mXDE)

**Versions**

* `ihe.iti.mxde#1.3.0`
* `ihe.iti.mxde#1.3.0-comment`

***

### IHE SDC/electronic Cancer Protocols (eCPs) on FHIR

**Versions**

* `hl7.fhir.uv.ihe-sdc-ecc#1.0.0`
* `hl7.fhir.uv.ihe-sdc-ecc#1.0.0-ballot`

***

### KBV.ITA.FOR

**Versions**

* `KBV.ITA.FOR#1.0.3`
* `KBV.ITA.FOR#1.0.2`
* `KBV.ITA.FOR#1.0.1`

***

### HL7 FHIR Implementation Guide: Public Health IG Release 1 - BE Realm | STU1

**Versions**

* `hl7.fhir.be.public-health#1.0.3`
* `hl7.fhir.be.public-health#1.0.2`
* `hl7.fhir.be.public-health#1.0.1`
* `hl7.fhir.be.public-health#1.0.0`

***

### kbv.mio.ueberleitungsbogen

**Versions**

* `kbv.mio.ueberleitungsbogen#1.0.0`
* `kbv.mio.ueberleitungsbogen#1.0.0-kommentierung`
* `kbv.mio.ueberleitungsbogen#1.0.0-benehmensherstellung`

***

### Electronic Medicinal Product Information (ePI) FHIR Implementation Guide

**Versions**

* `hl7.fhir.uv.emedicinal-product-info#1.0.0`
* `hl7.fhir.uv.emedicinal-product-info#1.0.0-ballot`

***

### Making Electronic Data More available for Research and Public Health (MedMorph)

**Versions**

* `hl7.fhir.us.medmorph#1.0.0`
* `hl7.fhir.us.medmorph#0.2.0`
* `hl7.fhir.us.medmorph#0.1.0`

***

### de.diga.caracare

**Versions**

* `de.diga.caracare#1.0.0`

***

### eHealth Platform Federal Core Profiles

**Versions**

* `hl7.fhir.be.core#2.1.2`
* `hl7.fhir.be.core#2.1.1`
* `hl7.fhir.be.core#2.1.0`
* `hl7.fhir.be.core#2.0.1`
* `hl7.fhir.be.core#2.0.0`

***

### cce.fhir

**Versions**

* `cce.fhir#0.1.0`

***

### AU Base Implementation Guide

**Versions**

* `hl7.fhir.au.base#5.1.0-preview`
* `hl7.fhir.au.base#5.0.0`
* `hl7.fhir.au.base#4.2.2-preview`
* `hl7.fhir.au.base#4.2.2-ballot`
* `hl7.fhir.au.base#4.2.1-preview`
* `hl7.fhir.au.base#4.2.0-preview`
* `hl7.fhir.au.base#4.1.0`
* `hl7.fhir.au.base#4.1.2-preview`
* `hl7.fhir.au.base#4.1.1-preview`
* `hl7.fhir.au.base#4.1.0-ballot`
* `hl7.fhir.au.base#4.0.0`
* `hl7.fhir.au.base#2.2.0-ballot`
* `hl7.fhir.au.base#2.0.0`
* `hl7.fhir.au.base#1.1.1`
* `hl7.fhir.au.base#1.0.2`
* `hl7.fhir.au.base#1.0.1`
* `hl7.fhir.au.base#1.0.0`
* `hl7.fhir.au.base#0.9.3`
* `hl7.fhir.au.base#0.5.0`
* `hl7.fhir.au.base#0.4.0`
* `hl7.fhir.au.base#0.3.0`
* `hl7.fhir.au.base#0.2.0`
* `hl7.fhir.au.base#0.1.0`

***

### test.circular.snapshot

**Versions**

* `test.circular.snapshot#0.0.1`

***

### ca-on-dhdr-r4.v09

**Versions**

* `ca-on-dhdr-r4.v09#0.9.0`

***

### Retrieval of Real World Data for Clinical Research

**Versions**

* `hl7.fhir.uv.vulcan-rwd#1.0.0`
* `hl7.fhir.uv.vulcan-rwd#1.0.0-ballot`

***

### bla.abel.org

**Versions**

* `bla.abel.org#1.0.1`

***

### MII IG Person

**Versions**

* `de.medizininformatikinitiative.kerndatensatz.person#2025.0.0-rc.1`
* `de.medizininformatikinitiative.kerndatensatz.person#2025.0.0`
* `de.medizininformatikinitiative.kerndatensatz.person#2025.0.0-alpha4`
* `de.medizininformatikinitiative.kerndatensatz.person#2025.0.0-alpha3`
* `de.medizininformatikinitiative.kerndatensatz.person#2025.0.0-alpha2`
* `de.medizininformatikinitiative.kerndatensatz.person#2025.0.0-alpha1`
* `de.medizininformatikinitiative.kerndatensatz.person#2024.0.0`
* `de.medizininformatikinitiative.kerndatensatz.person#2024.0.0-ballot`
* `de.medizininformatikinitiative.kerndatensatz.person#2024.0.0-alpha6`
* `de.medizininformatikinitiative.kerndatensatz.person#1.0.17`

***

### Patient Cost Transparency Implementation Guide

**Versions**

* `hl7.fhir.us.davinci-pct#2.0.0-ballot`
* `hl7.fhir.us.davinci-pct#1.1.0`
* `hl7.fhir.us.davinci-pct#1.0.0`
* `hl7.fhir.us.davinci-pct#0.1.0`

***

### FHIR Profile for Digital Health Applications that treat self-reported nicotine usage (F17.2)

**Versions**

* `com.alextherapeutics.fhir.nicotine#1.0.1`
* `com.alextherapeutics.fhir.nicotine#0.1.0-draft`

***

### Guía de Implementación ''cl core'' FHIR R4, (Versión Evolutiva)

**Versions**

* `hl7.fhir.cl.clcore#1.9.2`
* `hl7.fhir.cl.clcore#1.9.1`
* `hl7.fhir.cl.clcore#1.8.10`
* `hl7.fhir.cl.clcore#1.8.9`
* `hl7.fhir.cl.clcore#1.8.6`
* `hl7.fhir.cl.clcore#1.8.5`
* `hl7.fhir.cl.clcore#1.8.4`
* `hl7.fhir.cl.clcore#1.8.3`
* `hl7.fhir.cl.clcore#1.8.2`
* `hl7.fhir.cl.clcore#1.8.1`
* `hl7.fhir.cl.clcore#1.8.0`

***

### Clic Santé

**Versions**

* `ca.clicsante.base#1.0.0`

***

### Guía de Implementación Core-CL FHIR R4, (Versión Evolutiva)

**Versions**

* `hl7.fhir.cl.corecl#1.7.0`

***

### CH LAB-Order (R4)

**Versions**

* `ch.fhir.ig.ch-lab-order#2.0.0`
* `ch.fhir.ig.ch-lab-order#2.0.0-ballot`
* `ch.fhir.ig.ch-lab-order#1.0.0`
* `ch.fhir.ig.ch-lab-order#0.1.0`

***

### Répertoire national de l’Offre et des Ressources en santé et accompagnement médico-social

**Versions**

* `ans.fhir.fr.ror#0.5.0`
* `ans.fhir.fr.ror#0.4.0`
* `ans.fhir.fr.ror#0.4.0-snapshot-1`
* `ans.fhir.fr.ror#0.3.0`
* `ans.fhir.fr.ror#0.2.0`
* `ans.fhir.fr.ror#0.1.1`
* `ans.fhir.fr.ror#0.1.0`

***

### org.example.ProfilingTrainingMay

**Versions**

* `org.example.ProfilingTrainingMay#0.0.1`

***

### FHIR Implementation Guide for NDHM

**Versions**

* `hl7.fhir.in#1.0.0`

***

### Pan-Canadian Patient Summary

**Versions**

* `ca.infoway.io.psca#2.1.0-DFT-Ballot`
* `ca.infoway.io.psca#2.0.3-DFT-Ballot`
* `ca.infoway.io.psca#2.0.2-DFT-Ballot`
* `ca.infoway.io.psca#2.0.0-DFT-preBallot`
* `ca.infoway.io.psca#2.0.0-DFT-Ballot`
* `ca.infoway.io.psca#2.0.0-DFT-Ballot-alpha2`
* `ca.infoway.io.psca#2.0.0-DFT-Ballot-alpha-1`
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

***

### nictiz.fhir.nl.stu3.geboortezorg

**Versions**

* `nictiz.fhir.nl.stu3.geboortezorg#1.3.2`
* `nictiz.fhir.nl.stu3.geboortezorg#1.3.1`
* `nictiz.fhir.nl.stu3.geboortezorg#1.3.0`
* `nictiz.fhir.nl.stu3.geboortezorg#1.3.3-rc6`
* `nictiz.fhir.nl.stu3.geboortezorg#1.3.3-rc5`
* `nictiz.fhir.nl.stu3.geboortezorg#1.3.3-rc4`
* `nictiz.fhir.nl.stu3.geboortezorg#1.3.3-rc3`
* `nictiz.fhir.nl.stu3.geboortezorg#1.3.3-rc2`
* `nictiz.fhir.nl.stu3.geboortezorg#1.3.3-rc1`
* `nictiz.fhir.nl.stu3.geboortezorg#1.2.2`
* `nictiz.fhir.nl.stu3.geboortezorg#1.2.1`
* `nictiz.fhir.nl.stu3.geboortezorg#1.2.0`
* `nictiz.fhir.nl.stu3.geboortezorg#1.1.1`
* `nictiz.fhir.nl.stu3.geboortezorg#1.1.0`
* `nictiz.fhir.nl.stu3.geboortezorg#1.0.0`
* `nictiz.fhir.nl.stu3.geboortezorg#0.9.0`
* `nictiz.fhir.nl.stu3.geboortezorg#0.1.0`

***

### custom-extensions.ammy.test

**Versions**

* `custom-extensions.ammy.test#1.0.0`

***

### kbv.itv.evdga

**Versions**

* `kbv.itv.evdga#1.2.1`
* `kbv.itv.evdga#1.2.0`
* `kbv.itv.evdga#1.1.1`
* `kbv.itv.evdga#1.1.0`
* `kbv.itv.evdga#1.0.0`
* `kbv.itv.evdga#1.0.0-cc`

***

### Test.Training

**Versions**

* `Test.Training#1.0.0`

***

### nhn.fhir.no.kjernejournal

**Versions**

* `nhn.fhir.no.kjernejournal#1.0.1`
* `nhn.fhir.no.kjernejournal#1.0.0`

***

### kl.dk.fhir.gateway

**Versions**

* `kl.dk.fhir.gateway#1.2.0`
* `kl.dk.fhir.gateway#1.1.0`

***

### Personal Health Records

**Versions**

* `hl7.fhir.uv.phr#1.0.0-ballot2`
* `hl7.fhir.uv.phr#1.0.0-ballot`

***

### com.medipee.fhir.uroli-export

**Versions**

* `com.medipee.fhir.uroli-export#1.0.0`

***

### KBV.ITA.EAU

**Versions**

* `KBV.ITA.EAU#1.0.2`
* `KBV.ITA.EAU#1.0.1`

***

### HL7 Version 2 to FHIR

**Versions**

* `hl7.fhir.uv.v2mappings#1.0.0-ballot`
* `hl7.fhir.uv.v2mappings#0.1.0`

***

### NHSN Reporting: Inpatient Medication Administration

**Versions**

* `hl7.fhir.us.nhsn-med-admin#1.0.0`
* `hl7.fhir.us.nhsn-med-admin#0.1.0`

***

### SMART Base

**Versions**

* `smart.who.int.base#0.2.0`
* `smart.who.int.base#0.1.0`

***

### CH EPR Term

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

***

### kl.dk.fhir.prevention

**Versions**

* `kl.dk.fhir.prevention#2.1.1`
* `kl.dk.fhir.prevention#2.1.0`
* `kl.dk.fhir.prevention#2.0.0`
* `kl.dk.fhir.prevention#1.0.0`

***

### dvmd.kdl.r4.2021

**Versions**

* `dvmd.kdl.r4.2021#2021.1.1`
* `dvmd.kdl.r4.2021#2021.1.0`

***

### ezprava.fhir

**Versions**

* `ezprava.fhir#1.0.2`
* `ezprava.fhir#1.0.1`
* `ezprava.fhir#1.0.0`

***

### Da Vinci Risk Adjustment Implementation Guide

**Versions**

* `hl7.fhir.us.davinci-ra#2.0.0`
* `hl7.fhir.us.davinci-ra#2.0.0-ballot`
* `hl7.fhir.us.davinci-ra#1.0.0`
* `hl7.fhir.us.davinci-ra#0.1.0`

***

### PCR.Core.STU3

**Versions**

* `PCR.Core.STU3#1.0.1`
* `PCR.Core.STU3#1.0.0`

***

### HL7Norway.STU3.no.basis

**Versions**

* `HL7Norway.STU3.no.basis#1.0.3`
* `HL7Norway.STU3.no.basis#1.0.2`
* `HL7Norway.STU3.no.basis#1.0.1`

***

### de.gematik.isip

**Versions**

* `de.gematik.isip#1.0.2`

***

### AU Core Implementation Guide

**Versions**

* `hl7.fhir.au.core#1.1.0-preview`
* `hl7.fhir.au.core#1.0.0`
* `hl7.fhir.au.core#1.0.0-preview`
* `hl7.fhir.au.core#1.0.0-ballot`
* `hl7.fhir.au.core#0.4.1-preview`
* `hl7.fhir.au.core#0.4.0-preview`
* `hl7.fhir.au.core#0.3.0-ballot`
* `hl7.fhir.au.core#0.2.2-preview`
* `hl7.fhir.au.core#0.2.1-preview`
* `hl7.fhir.au.core#0.2.0-preview`
* `hl7.fhir.au.core#0.1.0-draft`

***

### maccabi.maccabident.medicationrequest.r4

**Versions**

* `maccabi.maccabident.medicationrequest.r4#1.0.8`
* `maccabi.maccabident.medicationrequest.r4#1.0.7`
* `maccabi.maccabident.medicationrequest.r4#1.0.6`
* `maccabi.maccabident.medicationrequest.r4#1.0.5`
* `maccabi.maccabident.medicationrequest.r4#1.0.4`
* `maccabi.maccabident.medicationrequest.r4#1.0.3`
* `maccabi.maccabident.medicationrequest.r4#1.0.2`
* `maccabi.maccabident.medicationrequest.r4#1.0.1`

***

### de.gematik.isik-basismodul

**Versions**

* `de.gematik.isik-basismodul#4.0.3`
* `de.gematik.isik-basismodul#4.0.2`
* `de.gematik.isik-basismodul#4.0.1`
* `de.gematik.isik-basismodul#4.0.0`
* `de.gematik.isik-basismodul#4.0.0-rc3`
* `de.gematik.isik-basismodul#4.0.0-rc2`
* `de.gematik.isik-basismodul#4.0.0-rc`
* `de.gematik.isik-basismodul#3.0.7`
* `de.gematik.isik-basismodul#3.0.6`
* `de.gematik.isik-basismodul#3.0.5`
* `de.gematik.isik-basismodul#3.0.4`
* `de.gematik.isik-basismodul#3.0.3`
* `de.gematik.isik-basismodul#3.0.2`
* `de.gematik.isik-basismodul#3.0.1`
* `de.gematik.isik-basismodul#2.0.9`
* `de.gematik.isik-basismodul#2.0.7`
* `de.gematik.isik-basismodul#2.0.6`
* `de.gematik.isik-basismodul#2.0.5`

***

### acme.product

**Versions**

* `acme.product#0.0.3`
* `acme.product#0.0.2-beta`

***

### Da Vinci Health Record Exchange (HRex)

**Versions**

* `hl7.fhir.us.davinci-hrex#1.1.0`
* `hl7.fhir.us.davinci-hrex#1.1.0-ballot`
* `hl7.fhir.us.davinci-hrex#1.0.0`
* `hl7.fhir.us.davinci-hrex#0.2.0`
* `hl7.fhir.us.davinci-hrex#0.1.0`

***

### CARIN Consumer Directed Payer Data Exchange (CARIN IG for Blue Button®)

**Versions**

* `hl7.fhir.us.carin-bb#2.1.0`
* `hl7.fhir.us.carin-bb#2.1.0-snapshot1`
* `hl7.fhir.us.carin-bb#2.0.0`
* `hl7.fhir.us.carin-bb#1.2.0`
* `hl7.fhir.us.carin-bb#1.1.0`
* `hl7.fhir.us.carin-bb#1.0.0`
* `hl7.fhir.us.carin-bb#0.1.0`

***

### careconnect.stu3.03.00.00

**Versions**

* `careconnect.stu3.03.00.00#3.1.0`

***

### cce.fhir.full

**Versions**

* `cce.fhir.full#0.2.0`

***

### fmcna.caredata.fhir.ig.r4.copy

**Versions**

* `fmcna.caredata.fhir.ig.r4.copy#1.0.0`

***

### HL7® Austria FHIR® Core Implementation Guide

**Versions**

* `hl7.at.fhir.core.r5#2.0.0`

***

### iknl.fhir.nl.r4.ncr-ehr

**Versions**

* `iknl.fhir.nl.r4.ncr-ehr#1.2.2`
* `iknl.fhir.nl.r4.ncr-ehr#1.2.1`
* `iknl.fhir.nl.r4.ncr-ehr#1.2.0`
* `iknl.fhir.nl.r4.ncr-ehr#1.1.0`
* `iknl.fhir.nl.r4.ncr-ehr#1.0.2`
* `iknl.fhir.nl.r4.ncr-ehr#1.0.1`
* `iknl.fhir.nl.r4.ncr-ehr#1.0.0`
* `iknl.fhir.nl.r4.ncr-ehr#0.4.0-alpha`
* `iknl.fhir.nl.r4.ncr-ehr#0.3.0`
* `iknl.fhir.nl.r4.ncr-ehr#0.2.0`
* `iknl.fhir.nl.r4.ncr-ehr#0.1.0`

***

### ca.on.oh-ereferral-econsult

**Versions**

* `ca.on.oh-ereferral-econsult#0.12.0-alpha1.0.7`
* `ca.on.oh-ereferral-econsult#0.12.0-alpha1.0.6`
* `ca.on.oh-ereferral-econsult#0.12.0-alpha1.0.5`
* `ca.on.oh-ereferral-econsult#0.12.0-alpha1.0.4`
* `ca.on.oh-ereferral-econsult#0.12.0-alpha1.0.3`
* `ca.on.oh-ereferral-econsult#0.12.0-projectathon1.0.2`
* `ca.on.oh-ereferral-econsult#0.12.0-alpha1.0.2`
* `ca.on.oh-ereferral-econsult#0.12.0-projectathon1.0.1`
* `ca.on.oh-ereferral-econsult#0.12.0-alpha1.0.1`
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
* `ca.on.oh-ereferral-econsult#0.11.0-alpha-1.0.2`
* `ca.on.oh-ereferral-econsult#0.11.1-alpha1.0.1`
* `ca.on.oh-ereferral-econsult#0.11.1-alpha1.0.0`

***

### FHIR Tooling Extensions IG

**Versions**

* `hl7.fhir.uv.tools.r5#0.5.0`
* `hl7.fhir.uv.tools.r5#0.4.1`
* `hl7.fhir.uv.tools.r5#0.4.0`
* `hl7.fhir.uv.tools.r5#0.3.0`

***

### tiga.health.clinical

**Versions**

* `tiga.health.clinical#1.1.0`

***

### uk.gpc.struct

**Versions**

* `uk.gpc.struct#1.5.0`

***

### de.gevko.emdaf

**Versions**

* `de.gevko.emdaf#1.3.0`
* `de.gevko.emdaf#1.2.0`
* `de.gevko.emdaf#1.1.2`
* `de.gevko.emdaf#1.1.1`
* `de.gevko.emdaf#1.1.0`
* `de.gevko.emdaf#1.0.0`
* `de.gevko.emdaf#0.9.0`

***

### Canadian Baseline

**Versions**

* `hl7.fhir.ca.baseline#1.2.0`
* `hl7.fhir.ca.baseline#1.1.8`
* `hl7.fhir.ca.baseline#1.1.7`
* `hl7.fhir.ca.baseline#1.1.6`
* `hl7.fhir.ca.baseline#1.1.4`
* `hl7.fhir.ca.baseline#1.1.3`
* `hl7.fhir.ca.baseline#1.1.6-pre`
* `hl7.fhir.ca.baseline#1.1.5-pre`

***

### SUPPORT.R4

**Versions**

* `SUPPORT.R4#3.0.0`

***

### FHIR Bulk Data Access

**Versions**

* `hl7.fhir.us.bulkdata#0.1.0`

***

### MedCom HomeCareObservation

**Versions**

* `medcom.fhir.dk.homecareobservation#1.0.0`

***

### BRCore-01.00.00

**Versions**

* `BRCore-01.00.00#1.0.0`

***

### de.gematik.isik-basismodul-stufe1

**Versions**

* `de.gematik.isik-basismodul-stufe1#1.0.10`

***

### colonoscopyreport.no

**Versions**

* `colonoscopyreport.no#0.7.23`
* `colonoscopyreport.no#0.7.22`
* `colonoscopyreport.no#0.7.21`
* `colonoscopyreport.no#0.7.20`
* `colonoscopyreport.no#0.7.19`
* `colonoscopyreport.no#0.7.18`
* `colonoscopyreport.no#0.7.17`
* `colonoscopyreport.no#0.7.16`
* `colonoscopyreport.no#0.7.15`
* `colonoscopyreport.no#0.7.14`
* `colonoscopyreport.no#0.7.13`
* `colonoscopyreport.no#0.7.12`
* `colonoscopyreport.no#0.7.11`
* `colonoscopyreport.no#0.7.10`
* `colonoscopyreport.no#0.7.9`
* `colonoscopyreport.no#0.7.8`
* `colonoscopyreport.no#0.7.7`
* `colonoscopyreport.no#0.7.6`
* `colonoscopyreport.no#0.7.5`
* `colonoscopyreport.no#0.7.4`
* `colonoscopyreport.no#0.7.3`
* `colonoscopyreport.no#0.7.2`
* `colonoscopyreport.no#0.7.1`
* `colonoscopyreport.no#0.7.0`
* `colonoscopyreport.no#0.6.0`
* `colonoscopyreport.no#0.5.1`
* `colonoscopyreport.no#0.5.0`
* `colonoscopyreport.no#0.4.1-beta7`
* `colonoscopyreport.no#0.4.1-beta6`
* `colonoscopyreport.no#0.4.1-beta5`
* `colonoscopyreport.no#0.4.1-beta4`
* `colonoscopyreport.no#0.4.1-beta3`
* `colonoscopyreport.no#0.4.1-beta2`
* `colonoscopyreport.no#0.4.1-beta1`
* `colonoscopyreport.no#0.4.0-beta4`
* `colonoscopyreport.no#0.4.0-beta3`
* `colonoscopyreport.no#0.4.0-beta2`
* `colonoscopyreport.no#0.4.0-beta1`
* `colonoscopyreport.no#0.3.0-beta1`
* `colonoscopyreport.no#0.2.0-beta1`
* `colonoscopyreport.no#0.1.0-beta2`
* `colonoscopyreport.no#0.1.0-beta`

***

### Longitudinal Maternal & Infant Health Information for Research

**Versions**

* `hl7.fhir.us.mihr#1.0.0`
* `hl7.fhir.us.mihr#1.0.0-ballot`

***

### de.gematik.fhir.directory

**Versions**

* `de.gematik.fhir.directory#0.13.0`
* `de.gematik.fhir.directory#0.12.0`
* `de.gematik.fhir.directory#0.11.25`
* `de.gematik.fhir.directory#0.11.24`
* `de.gematik.fhir.directory#0.11.23`
* `de.gematik.fhir.directory#0.11.22`
* `de.gematik.fhir.directory#0.11.21`
* `de.gematik.fhir.directory#0.11.20`
* `de.gematik.fhir.directory#0.11.19`
* `de.gematik.fhir.directory#0.11.18`
* `de.gematik.fhir.directory#0.11.17`
* `de.gematik.fhir.directory#0.11.16`
* `de.gematik.fhir.directory#0.11.15`
* `de.gematik.fhir.directory#0.11.14`
* `de.gematik.fhir.directory#0.11.13`
* `de.gematik.fhir.directory#0.11.12`
* `de.gematik.fhir.directory#0.11.11`
* `de.gematik.fhir.directory#0.11.10`
* `de.gematik.fhir.directory#0.11.9`
* `de.gematik.fhir.directory#0.11.8`
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

***

### FHIR Core package

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

***

### FHIR Extensions Pack

**Versions**

* `hl7.fhir.uv.extensions#5.2.0`
* `hl7.fhir.uv.extensions#5.2.0-ballot`
* `hl7.fhir.uv.extensions#5.1.0`
* `hl7.fhir.uv.extensions#5.1.0-snapshot1`
* `hl7.fhir.uv.extensions#5.1.0-cibuild`
* `hl7.fhir.uv.extensions#5.1.0-ballot1`
* `hl7.fhir.uv.extensions#1.0.0`
* `hl7.fhir.uv.extensions#0.1.0`

***

### de.abda.erezeptabgabedatenpkv

**Versions**

* `de.abda.erezeptabgabedatenpkv#1.4.0`
* `de.abda.erezeptabgabedatenpkv#1.4.0-rc2`
* `de.abda.erezeptabgabedatenpkv#1.4.0-rc1`
* `de.abda.erezeptabgabedatenpkv#1.4.0-rc`
* `de.abda.erezeptabgabedatenpkv#1.3.1`
* `de.abda.erezeptabgabedatenpkv#1.3.0`
* `de.abda.erezeptabgabedatenpkv#1.3.0-rc`
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

***

### ca.on.oh-dhdr

**Versions**

* `ca.on.oh-dhdr#4.0.2-alpha1.0.6`
* `ca.on.oh-dhdr#4.0.2-alpha1.0.5`
* `ca.on.oh-dhdr#4.0.2-alpha1.0.4`
* `ca.on.oh-dhdr#4.0.2-alpha1.0.3`
* `ca.on.oh-dhdr#4.0.2-alpha1.0.2`
* `ca.on.oh-dhdr#4.0.2-alpha1.0.1`
* `ca.on.oh-dhdr#4.0.2-alpha1.0.0`
* `ca.on.oh-dhdr#4.0.1-alpha1.0.0`

***

### Vital Signs with Qualifying Elements

**Versions**

* `hl7.fhir.us.vitals#1.0.0`

***

### Specialty Medication Enrollment

**Versions**

* `hl7.fhir.us.specialty-rx#2.0.0`
* `hl7.fhir.us.specialty-rx#2.0.0-ballot`
* `hl7.fhir.us.specialty-rx#1.0.0`
* `hl7.fhir.us.specialty-rx#0.1.0`

***

### eu.miabis.r4

**Versions**

* `eu.miabis.r4#0.2.0`
* `eu.miabis.r4#0.1.0`

***

### surescripts.CareEventNotifications

**Versions**

* `surescripts.CareEventNotifications#1.0.0-beta`

***

### Service d'Accès aux Soins

**Versions**

* `ans.fhir.fr.sas#1.1.0`
* `ans.fhir.fr.sas#1.0.0`
* `ans.fhir.fr.sas#1.0.0-ballot`

***

### Canonical Resource Management Infrastructure Implementation Guide

**Versions**

* `hl7.fhir.uv.crmi#1.0.0`
* `hl7.fhir.uv.crmi#1.0.0-snapshot`
* `hl7.fhir.uv.crmi#1.0.0-ballot2`
* `hl7.fhir.uv.crmi#1.0.0-ballot`

***

### Quality Measure Implementation Guide

**Versions**

* `hl7.fhir.uv.cqm#1.0.0-ballot`

***

### de.gematik.erezept-patientenrechnung.r4

**Versions**

* `de.gematik.erezept-patientenrechnung.r4#1.1.0`
* `de.gematik.erezept-patientenrechnung.r4#1.1.0-rc2`
* `de.gematik.erezept-patientenrechnung.r4#1.1.0-rc1`
* `de.gematik.erezept-patientenrechnung.r4#1.0.4`
* `de.gematik.erezept-patientenrechnung.r4#1.0.3`
* `de.gematik.erezept-patientenrechnung.r4#1.0.2`
* `de.gematik.erezept-patientenrechnung.r4#1.0.1`
* `de.gematik.erezept-patientenrechnung.r4#1.0.0`
* `de.gematik.erezept-patientenrechnung.r4#1.0.4-rc2`

***

### shin.interconnect

**Versions**

* `shin.interconnect#1.0.0-beta`

***

### Patient Request for Corrections Implementation Guide

**Versions**

* `hl7.fhir.uv.patient-corrections#1.0.0-ballot`

***

### Da Vinci - Coverage Requirements Discovery

**Versions**

* `hl7.fhir.us.davinci-crd#2.1.0`
* `hl7.fhir.us.davinci-crd#2.1.0-preview`
* `hl7.fhir.us.davinci-crd#2.0.1`
* `hl7.fhir.us.davinci-crd#2.0.0`
* `hl7.fhir.us.davinci-crd#1.1.0-ballot`
* `hl7.fhir.us.davinci-crd#1.0.0`
* `hl7.fhir.us.davinci-crd#0.3.0`
* `hl7.fhir.us.davinci-crd#0.1.0`

***

### jp-core.r4

**Versions**

* `jp-core.r4#1.1.0`
* `jp-core.r4#1.1.1-rc`
* `jp-core.r4#1.1.1-draft1`

***

### myhealthway.main.r4

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

***

### ai4health.it.test.r4

**Versions**

* `ai4health.it.test.r4#0.0.8`

***

### acme.cowboy.fsh

**Versions**

* `acme.cowboy.fsh#0.0.1`

***

### tiplu.maia.schnittstellendefinition

**Versions**

* `tiplu.maia.schnittstellendefinition#1.1.2`
* `tiplu.maia.schnittstellendefinition#1.1.1`
* `tiplu.maia.schnittstellendefinition#1.1.0`
* `tiplu.maia.schnittstellendefinition#1.0.4`
* `tiplu.maia.schnittstellendefinition#1.0.3`
* `tiplu.maia.schnittstellendefinition#1.0.2`

***

### Gestion d'Agendas Partagés (GAP)

**Versions**

* `ans.fhir.fr.gap#3.0.0`

***

### Privacy Consent on FHIR (PCF)

**Versions**

* `ihe.iti.pcf#1.1.0`
* `ihe.iti.pcf#1.0.0`
* `ihe.iti.pcf#1.0.0-comment`

***

### sk.pnc.r4

**Versions**

* `sk.pnc.r4#0.1.0`

***

### PACIO Cognitive Status Implementation Guide

**Versions**

* `hl7.fhir.us.pacio-cs#1.0.0`
* `hl7.fhir.us.pacio-cs#0.1.0`

***

### de.medizininformatikinitiative.kerndatensatz.molgen

**Versions**

* `de.medizininformatikinitiative.kerndatensatz.molgen#2025.0.0`
* `de.medizininformatikinitiative.kerndatensatz.molgen#2025.0.0-alpha2`
* `de.medizininformatikinitiative.kerndatensatz.molgen#2025.0.0-alpha1`
* `de.medizininformatikinitiative.kerndatensatz.molgen#1.0.0`

***

### NaminderSTU3.testing

**Versions**

* `NaminderSTU3.testing#1.0.1`
* `NaminderSTU3.testing#1.0.0`

***

### cisis.test

**Versions**

* `cisis.test#1.2.0`
* `cisis.test#1.1.0`
* `cisis.test#1.0.0`

***

### test.fhir

**Versions**

* `test.fhir#0.0.1-test`

***

### ca.on.ppr.r4

**Versions**

* `ca.on.ppr.r4#1.2.0`
* `ca.on.ppr.r4#1.1.0`
* `ca.on.ppr.r4#1.0.0-beta`

***

### US Prescription Drug Monitoring Program (PDMP)

**Versions**

* `hl7.fhir.us.pdmp#1.0.0`
* `hl7.fhir.us.pdmp#1.0.0-ballot`

***

### CH EMED EPR

**Versions**

* `ch.fhir.ig.ch-emed-epr#2.0.0`
* `ch.fhir.ig.ch-emed-epr#1.0.0`

***

### NCN2.01

**Versions**

* `NCN2.01#0.0.02`

***

### DIFUTURE.trustcenter.sp

**Versions**

* `DIFUTURE.trustcenter.sp#1.0.0-beta-1`

***

### HSPC FHIR Implementation Guide

**Versions**

* `fhir.hspc.core#0.1.0`

***

### hl7.fhir.r4.id.core

**Versions**

* `hl7.fhir.r4.id.core#0.1.0`

***

### de.nfdi4health.mds

**Versions**

* `de.nfdi4health.mds#2.0.0`
* `de.nfdi4health.mds#1.0.0`

***

### Ontario COVaxON FHIR Implementation Guide

**Versions**

* `ca-on.fhir.ig.covaxon#0.1.10`

***

### FHIR 5.0.0 package : Expansions

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

***

### minimal Common Oncology Data Elements (mCODE) Implementation Guide

**Versions**

* `hl7.fhir.us.mcode#4.0.0`
* `hl7.fhir.us.mcode#4.0.0-ballot`
* `hl7.fhir.us.mcode#3.0.0`
* `hl7.fhir.us.mcode#3.0.0-ballot`
* `hl7.fhir.us.mcode#2.1.0`
* `hl7.fhir.us.mcode#2.0.0`
* `hl7.fhir.us.mcode#1.16.0`
* `hl7.fhir.us.mcode#1.0.0`
* `hl7.fhir.us.mcode#0.9.1`

***

### Formation.FHIR

**Versions**

* `Formation.FHIR#1.0.1`

***

### aws.dummy

**Versions**

* `aws.dummy#0.0.2`
* `aws.dummy#0.0.1`

***

### healthdata.be.r4.cbb

**Versions**

* `healthdata.be.r4.cbb#0.16.0-beta`
* `healthdata.be.r4.cbb#0.15.0-beta`
* `healthdata.be.r4.cbb#0.14.0-beta`
* `healthdata.be.r4.cbb#0.13.0-beta`
* `healthdata.be.r4.cbb#0.12.0-beta`
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

***

### Vital Records Common Library (VRCL) FHIR Implementation Guide

**Versions**

* `hl7.fhir.us.vr-common-library#2.0.0`
* `hl7.fhir.us.vr-common-library#2.0.0-ballot`
* `hl7.fhir.us.vr-common-library#1.1.0`
* `hl7.fhir.us.vr-common-library#1.0.0`
* `hl7.fhir.us.vr-common-library#0.1.0`

***

### 臺灣健保癌症用藥事前審查實作指引

**Versions**

* `tw.gov.mohw.nhi.pas#1.0.1`
* `tw.gov.mohw.nhi.pas#1.0.0`

***

### de.nichtraucherhelden.export

**Versions**

* `de.nichtraucherhelden.export#1.0.0`

***

### de.gkvsv.erezeptabrechnungsdaten

**Versions**

* `de.gkvsv.erezeptabrechnungsdaten#1.5.1`
* `de.gkvsv.erezeptabrechnungsdaten#1.5.0`
* `de.gkvsv.erezeptabrechnungsdaten#1.5.0-rc2`
* `de.gkvsv.erezeptabrechnungsdaten#1.5.0-rc1`
* `de.gkvsv.erezeptabrechnungsdaten#1.5.0-rc`
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

***

### mes.fhir.fr.mesure

**Versions**

* `mes.fhir.fr.mesure#1.0.0`

***

### de.gevko.eahb

**Versions**

* `de.gevko.eahb#0.9.0`

***

### Việt Nam CORE IG (dựa trên phiên bản HL7 FHIR Release 4.0.1) - Draft version

**Versions**

* `hl7.fhir.vn.core#1.0.0-beta`

***

### fmcna.caredata.fhir.ig.r4

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

***

### de.gematik.terminology

**Versions**

* `de.gematik.terminology#1.0.5-ballot.1`
* `de.gematik.terminology#1.0.5`
* `de.gematik.terminology#1.0.1`
* `de.gematik.terminology#1.0.0`

***

### PACIO Functional Status Implementation Guide

**Versions**

* `hl7.fhir.us.pacio-fs#1.0.0`
* `hl7.fhir.us.pacio-fs#0.1.0`

***

### ezFHIR2.pk

**Versions**

* `ezFHIR2.pk#2.0.0`

***

### HL7 FHIR Implementation Guide: Transversal Clinical Core

**Versions**

* `hl7.fhir.be.core-clinical#1.0.0`

***

### SDOH Clinical Care

**Versions**

* `hl7.fhir.us.sdoh-clinicalcare#2.2.0`
* `hl7.fhir.us.sdoh-clinicalcare#2.1.0`
* `hl7.fhir.us.sdoh-clinicalcare#2.0.0`
* `hl7.fhir.us.sdoh-clinicalcare#1.1.0`
* `hl7.fhir.us.sdoh-clinicalcare#1.0.0`
* `hl7.fhir.us.sdoh-clinicalcare#0.1.0`

***

### Bidirectional Services eReferral (BSeR)

**Versions**

* `hl7.fhir.us.bser#2.0.0-ballot`
* `hl7.fhir.us.bser#1.0.0`
* `hl7.fhir.us.bser#0.2.0`

***

### Médicosocial - Suivi Décisions Orientation

**Versions**

* `ans.fhir.fr.sdo#4.0.3`
* `ans.fhir.fr.sdo#4.0.2`
* `ans.fhir.fr.sdo#4.0.1`
* `ans.fhir.fr.sdo#4.0.3-ballot`
* `ans.fhir.fr.sdo#4.0.2-ballot-3`
* `ans.fhir.fr.sdo#4.0.2-ballot-2`

***

### Swedish Base Profiles Implementation Guide

**Versions**

* `hl7se.fhir.base#1.0.0`

***

### HL7 Belgium Patientwill (Patient Dossier)

**Versions**

* `hl7.fhir.be.patientwill#1.0.0`

***

### Potential Drug-Drug Interaction (PDDI) CDS IG : STU1 Ballot 2

**Versions**

* `hl7.fhir.uv.pddi#1.0.0-ballot`
* `hl7.fhir.uv.pddi#0.2.0`
* `hl7.fhir.uv.pddi#0.1.0`

***

### de.gevko.evo.khb

**Versions**

* `de.gevko.evo.khb#0.9.1`
* `de.gevko.evo.khb#0.9.0`

***

### hl7.fhir.sami.core

**Versions**

* `hl7.fhir.sami.core#0.0.1`

***

### cce.fhir.minimal

**Versions**

* `cce.fhir.minimal#0.4.0`
* `cce.fhir.minimal#0.3.0`

***

### 診所門診病摘實作指引

**Versions**

* `tw.cohesion.fhir.clinemr#1.0.1`
* `tw.cohesion.fhir.clinemr#1.0.0`

***

### de.abda.eRezeptAbgabedatenPKV

**Versions**

* `de.abda.eRezeptAbgabedatenPKV#1.1.0-rc1`

***

### kbv.basis.ressources.only

**Versions**

* `kbv.basis.ressources.only#1.7.0`

***

### US Medication Risk Evaluation and Mitigation Strategies (REMS) FHIR IG

**Versions**

* `hl7.fhir.us.medication-rems#1.0.0`
* `hl7.fhir.us.medication-rems#1.0.0-ballot`

***

### US Public Health Profiles Library

**Versions**

* `hl7.fhir.us.ph-library#1.0.0`
* `hl7.fhir.us.ph-library#1.0.0-ballot`

***

### MyDummyProject.01

**Versions**

* `MyDummyProject.01#0.0.1`

***

### FHIR implementation of zibs 2020

**Versions**

* `nictiz.fhir.nl.r4.zib2020#0.11.0-beta.1`
* `nictiz.fhir.nl.r4.zib2020#0.10.0-beta.1`
* `nictiz.fhir.nl.r4.zib2020#0.9.0-beta.1`
* `nictiz.fhir.nl.r4.zib2020#0.8.0-beta.1`
* `nictiz.fhir.nl.r4.zib2020#0.7.0-beta.1`
* `nictiz.fhir.nl.r4.zib2020#0.6.0-beta.2`
* `nictiz.fhir.nl.r4.zib2020#0.5.0-beta1`
* `nictiz.fhir.nl.r4.zib2020#0.4.0-beta1`
* `nictiz.fhir.nl.r4.zib2020#0.3.0-beta1`
* `nictiz.fhir.nl.r4.zib2020#0.2.0-beta1`
* `nictiz.fhir.nl.r4.zib2020#0.1.0-beta1`

***

### de.gematik.isik

**Versions**

* `de.gematik.isik#5.0.0-rc`

***

### ca.on.oh.erec

**Versions**

* `ca.on.oh.erec#0.12.0-alpha1.0.4`
* `ca.on.oh.erec#0.12.0-alpha1.0.3`
* `ca.on.oh.erec#0.12.0-projectathon1.0.2`
* `ca.on.oh.erec#0.12.0-alpha1.0.2`
* `ca.on.oh.erec#0.12.0-projectathon1.0.1`
* `ca.on.oh.erec#0.12.0-alpha1.0.1`
* `ca.on.oh.erec#0.12.0-alpha1.0.0`

***

### IHE FHIR Profile: Occupational Data for Health (ODH) - International

**Versions**

* `ihe.pcc.odh#1.0.0`
* `ihe.pcc.odh#1.0.0-comment`

***

### CH EMS (R4)

**Versions**

* `ch.fhir.ig.ch-ems#1.9.0`

***

### de.gematik.elektronische-versicherungsbescheinigung

**Versions**

* `de.gematik.elektronische-versicherungsbescheinigung#1.0.0`
* `de.gematik.elektronische-versicherungsbescheinigung#1.0.0-rc3`
* `de.gematik.elektronische-versicherungsbescheinigung#1.0.0-rc2`
* `de.gematik.elektronische-versicherungsbescheinigung#1.0.0-rc1`
* `de.gematik.elektronische-versicherungsbescheinigung#0.8.1-beta`
* `de.gematik.elektronische-versicherungsbescheinigung#0.8.0-beta`

***

### HL7 FHIR® Implementation Guide: Electronic Case Reporting (eCR) - US Realm

**Versions**

* `hl7.fhir.us.ecr#2.1.2`
* `hl7.fhir.us.ecr#2.1.1`
* `hl7.fhir.us.ecr#2.1.0`
* `hl7.fhir.us.ecr#2.0.0`
* `hl7.fhir.us.ecr#1.1.0`
* `hl7.fhir.us.ecr#1.0.0`
* `hl7.fhir.us.ecr#0.2.0`
* `hl7.fhir.us.ecr#0.1.0`

***

### Da Vinci Postable Remittance

**Versions**

* `hl7.fhir.us.davinci-pr#1.0.0`
* `hl7.fhir.us.davinci-pr#1.0.0-ballot`

***

### uk.ads.r4

**Versions**

* `uk.ads.r4#1.2.0-bundle2-alpha`
* `uk.ads.r4#1.1.0-bundle1-alpha`
* `uk.ads.r4#1.0.2-mvp-alpha`
* `uk.ads.r4#1.0.1-mvp-alpha`
* `uk.ads.r4#1.0.0-mvp-alpha`

***

### dk.fhir.ig.kl.common.caresocial

**Versions**

* `dk.fhir.ig.kl.common.caresocial#0.1.7`
* `dk.fhir.ig.kl.common.caresocial#0.1.6`

***

### Guía de Estandares de Informacionde Salud

**Versions**

* `hl7.fhir.cl.minsal.eis#0.1.0`

***

### DK MedCom Carecommunication

**Versions**

* `medcom.fhir.dk.carecommunication#4.0.2`
* `medcom.fhir.dk.carecommunication#4.0.1`
* `medcom.fhir.dk.carecommunication#4.0.0`
* `medcom.fhir.dk.carecommunication#3.0.0`
* `medcom.fhir.dk.carecommunication#2.1.0`
* `medcom.fhir.dk.carecommunication#2.0.0`

***

### acme.fsh.example

**Versions**

* `acme.fsh.example#0.0.1-demo`

***

### synapxe.rcm

**Versions**

* `synapxe.rcm#1.1.0`
* `synapxe.rcm#1.0.0`

***

### Allergy (Patient Dossier)

**Versions**

* `hl7.fhir.be.allergy#1.2.0`
* `hl7.fhir.be.allergy#1.1.0`
* `hl7.fhir.be.allergy#1.0.1`
* `hl7.fhir.be.allergy#1.0.0`

***

### touchstone-ereferralontario.core

**Versions**

* `touchstone-ereferralontario.core#0.1.1-beta`

***

### niceprofiling.v23q1

**Versions**

* `niceprofiling.v23q1#0.5.0`
* `niceprofiling.v23q1#0.4.0`
* `niceprofiling.v23q1#0.3.0`
* `niceprofiling.v23q1#0.2.0`

***

### NorthwellHealth.Extensions.Person

**Versions**

* `NorthwellHealth.Extensions.Person#0.0.1`

***

### Mobile Antepartum Summary US Realm

**Versions**

* `ihe.pcc.maps.us#1.0.0-comment`

***

### SupportedHospitalDischarge.STU3

**Versions**

* `SupportedHospitalDischarge.STU3#0.1.2`
* `SupportedHospitalDischarge.STU3#0.1.0`

***

### fhir.dicom

**Versions**

* `fhir.dicom#2025.2.20250411`
* `fhir.dicom#2025.1.20250221`
* `fhir.dicom#2024.2.20240331`
* `fhir.dicom#2024.1.20240120`
* `fhir.dicom#2023.5.2023111`
* `fhir.dicom#2023.4.20230907`
* `fhir.dicom#2023.3.20230704`
* `fhir.dicom#2023.1.20230123`
* `fhir.dicom#2022.4.20221006`
* `fhir.dicom#2022.1.20220124`
* `fhir.dicom#2021.4.20210910`

***

### Eu.Dedalus.D4Solutions.COVID-19

**Versions**

* `Eu.Dedalus.D4Solutions.COVID-19#1.0.2`
* `Eu.Dedalus.D4Solutions.COVID-19#1.0.1`
* `Eu.Dedalus.D4Solutions.COVID-19#1.0.0`
* `Eu.Dedalus.D4Solutions.COVID-19#1.0.0-develop`

***

### kbv.all.st-combined

**Versions**

* `kbv.all.st-combined#1.11.0`
* `kbv.all.st-combined#1.10.0`
* `kbv.all.st-combined#1.9.0`
* `kbv.all.st-combined#1.8.0`
* `kbv.all.st-combined#1.7.0`
* `kbv.all.st-combined#1.6.0`
* `kbv.all.st-combined#1.5.0`

***

### Terminology Support (r3)

**Versions**

* `fhir.tx.support.r3#0.25.0`
* `fhir.tx.support.r3#0.24.0`
* `fhir.tx.support.r3#0.22.0`
* `fhir.tx.support.r3#0.20.0`
* `fhir.tx.support.r3#0.19.0`
* `fhir.tx.support.r3#0.18.0`
* `fhir.tx.support.r3#0.17.0`
* `fhir.tx.support.r3#0.16.0`
* `fhir.tx.support.r3#0.15.0`
* `fhir.tx.support.r3#0.14.0`
* `fhir.tx.support.r3#0.13.0`
* `fhir.tx.support.r3#0.12.0`
* `fhir.tx.support.r3#0.11.0`
* `fhir.tx.support.r3#0.10.0`
* `fhir.tx.support.r3#0.9.0`
* `fhir.tx.support.r3#0.8.0`
* `fhir.tx.support.r3#0.7.0`
* `fhir.tx.support.r3#0.6.0`
* `fhir.tx.support.r3#0.5.0`
* `fhir.tx.support.r3#0.4.0`
* `fhir.tx.support.r3#0.3.2`
* `fhir.tx.support.r3#0.3.1`
* `fhir.tx.support.r3#0.3.0`
* `fhir.tx.support.r3#0.2.0`
* `fhir.tx.support.r3#0.1.0`

***

### Interoperable Digital Identity and Patient Matching

**Versions**

* `hl7.fhir.us.identity-matching#2.0.0-ballot`
* `hl7.fhir.us.identity-matching#1.0.0`
* `hl7.fhir.us.identity-matching#1.0.0-ballot`

***

### Terminologies de Santé

**Versions**

* `ans.fr.terminologies#0.1.0-ballot`

***

### robin.gittest

**Versions**

* `robin.gittest#1.1.22-beta`
* `robin.gittest#1.1.21-beta`
* `robin.gittest#1.1.1-beta`
