---
description: >-
  This page contains the list of supported C-CDA documents and sections level
  templates.
---

# List of supported templates

The C-CDA to FHIR Converter comes with pre-built scripts for converting C-CDA documents to FHIR Bundles.&#x20;

These scripts can be extended or modified to suit specific conversion needs. The flexibility of the conversion script enables the inclusion of extra entry or section level templates (e.g., open templates) to adhere to any changes in C-CDA specifications or to accommodate other specifications based on the HL7 CDA domain.&#x20;

Below is a list of the most commonly used C-CDA document templates and their corresponding section templates.&#x20;

Note that sections can be reused in multiple document templates, making it easier to cover documents not listed in the table.&#x20;

If you have specific document or section-level requirements, feel free to [contact us](https://docs.aidbox.app/overview/contact-us) for more details.


| C-CDA document templates                                                                                                                                                                                                                                                                                             | Supported sections                                                                                                                                                                                                                                           |
| -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| <p><strong>Continuity Of Care</strong> </p><p>The Continuity of Care Document (CCD) is a core set of important administrative, demographic, and clinical information about a patient's healthcare. </p><p>It allows healthcare providers or systems to gather and share patient data to support continuous care.</p> | Advance Directives, Allergies and Intolerances, Encounters, Family History, Functional Status, Immunizations, Medical Equipment, Medications, Mental Status, Nutrition, Payers, Plan of Treatment, Problem, Procedures, Results, Social History, Vital Signs |
| <p><strong>Consultation Note</strong> </p><p>A Consultation Note contains the reason for referral, history of present illness, physical examination, and components for decision-making (Assessment and Plan).</p>                                                                                                   | Allergies and Intolerances, Assessment, Family History, Functional Status, Immunizations, Medical Equipment, Mental Status, Nutrition, Plan of Treatment, Problem, Procedures, Results, Social History, Vital Signs                                          |
| <p><strong>Discharge Summary</strong> </p><p>The Discharge Summary is a document that summarizes a patient's admission to a hospital, provider, or other setting. </p><p>It offers information to support ongoing care post-discharge.</p>                                                                           | Allergies and Intolerances, Family History, Functional Status, Hospital Course, Hospital Discharge Instructions, Nutrition, Plan of Treatment, Problem, Procedures, Social History                                                                           |
| <p>Care Plan </p><p>A Care Plan represents one or more Plans of Care, aiming to reconcile and resolve conflicts among the plans developed for a specific patient by different providers.</p>                                                                                                                         | Goals, Health Concerns                                                                                                                                                                                                                                       |


| Section Name | LOINCs | Alias | Narrative
| --- | --- | --- | --- | 
|[Allergies and Intolerances Section (entries optional) (V3)](/modules-1/integration-toolkit/ccda-converter/sections/allergiesandintolerancessectioneo.md)|48765-2|allergies|✅
|[Allergies and Intolerances Section (entries required) (V3)](/modules-1/integration-toolkit/ccda-converter/sections/allergiesandintolerancessectioner.md)|48765-2|allergies|✅
|[Assessment Section](/modules-1/integration-toolkit/ccda-converter/sections/assessmentsection.md)|51848-0|N/A|❌
|[Default Section Rules](/modules-1/integration-toolkit/ccda-converter/sections/default.md)|&nbsp;|default|❌
|[Document Header](/modules-1/integration-toolkit/ccda-converter/sections/header.md)|&nbsp;|header|❌
|[Encounters Section (entries optional) (V3)](/modules-1/integration-toolkit/ccda-converter/sections/encounterssectionentriesoptionalv3.md)|46240-8|encounters|❌
|[Encounters Section (entries required) (V3)](/modules-1/integration-toolkit/ccda-converter/sections/encounterssectionentriesrequiredv3.md)|46240-8|encounters|✅
|[Family History Section (V3)](/modules-1/integration-toolkit/ccda-converter/sections/familyhistorysectionv3.md)|10157-6|family-history|❌
|[Functional Status Section (V2)](/modules-1/integration-toolkit/ccda-converter/sections/functionalstatussectionv2.md)|47420-5|funcstatus|❌
|[Goals Section](/modules-1/integration-toolkit/ccda-converter/sections/goalssection.md)|61146-7|goals|✅
|[Health Concerns Section (V2)](/modules-1/integration-toolkit/ccda-converter/sections/healthconcernssectionv2.md)|75310-3|health-concerns|❌
|[Hospital Course Section](/modules-1/integration-toolkit/ccda-converter/sections/hospitalcoursesection.md)|8648-8|N/A|❌
|[Hospital Discharge Instructions Section](/modules-1/integration-toolkit/ccda-converter/sections/hospitaldischargeinstructionssectio.md)|8653-8|N/A|❌
|[Immunizations Section (entries optional) (V3)](/modules-1/integration-toolkit/ccda-converter/sections/immunizationssectionentriesoptiona.md)|11369-6|immunizations|✅
|[Immunizations Section (entries required) (V3)](/modules-1/integration-toolkit/ccda-converter/sections/immunizationssectionentriesrequire.md)|11369-6|immunizations|✅
|[Medical Equipment Section (V2)](/modules-1/integration-toolkit/ccda-converter/sections/medicalequipmentsectionv2.md)|46264-8|medical-equipment|✅
|[Medications Section (entries optional) (V2)](/modules-1/integration-toolkit/ccda-converter/sections/medicationssectionentriesoptional.md)|10160-0|medications|✅
|[Medications Section (entries required) (V2)](/modules-1/integration-toolkit/ccda-converter/sections/medicationssectionentriesrequired.md)|10160-0|medications|✅
|[Mental Status Section (V2)](/modules-1/integration-toolkit/ccda-converter/sections/mentalstatussectionv2.md)|10190-7|mental-status|❌
|[Notes](/modules-1/integration-toolkit/ccda-converter/sections/notessection.md)|18748-4, 11488-4, 28570-0, 11502-2, 34117-2, 18842-5, 11506-3|N/A|✅
|[Nutrition Section](/modules-1/integration-toolkit/ccda-converter/sections/nutritionsection.md)|61144-2|nutrition|❌
|[Payers Section (V3)](/modules-1/integration-toolkit/ccda-converter/sections/payerssectionv3.md)|48768-6|payers|❌
|[Plan of Treatment Section (V2)](/modules-1/integration-toolkit/ccda-converter/sections/planoftreatmentsectionv2.md)|18776-5|plan-of-treatment|✅
|[Problem Section (entries optional) (V3)](/modules-1/integration-toolkit/ccda-converter/sections/problemsectionentriesoptionalv3.md)|11450-4|problems|✅
|[Problem Section (entries required) (V3)](/modules-1/integration-toolkit/ccda-converter/sections/problemsectionentriesrequiredv3.md)|11450-4|problems|✅
|[Procedures Section (entries optional) (V2)](/modules-1/integration-toolkit/ccda-converter/sections/proceduressectionentriesoptionalv2.md)|47519-4|procedures|✅
|[Procedures Section (entries required) (V2)](/modules-1/integration-toolkit/ccda-converter/sections/proceduressectionentriesrequiredv.md)|47519-4|procedures|✅
|[Results Section (entries optional) (V3)](/modules-1/integration-toolkit/ccda-converter/sections/resultssectionentriesoptionalv3.md)|30954-2|results|✅
|[Results Section (entries required) (V3)](/modules-1/integration-toolkit/ccda-converter/sections/resultssectionentriesrequiredv3.md)|30954-2|results|✅
|[Social History Section (V3)](/modules-1/integration-toolkit/ccda-converter/sections/socialhistorysectionv3.md)|29762-2|social-history|✅
|[Vital Signs Section (entries optional) (V3)](/modules-1/integration-toolkit/ccda-converter/sections/vitalsignssectionentriesoptional.md)|8716-3|vital-signs|✅
|[Vital Signs Section (entries required) (V3)](/modules-1/integration-toolkit/ccda-converter/sections/vitalsignssectionentriesrequired.md)|8716-3|vital-signs|✅