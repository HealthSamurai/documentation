---
description: Aidbox module: FHIR Resources for extended FHIR functionality and integration.
---

# FHIR Resources

HL7v2 module is using Aidbox as data storage for custom resources defined via FHIRSchema and either for accessing FHIR resources. Important part here that you should setup an access policies for app Client resource to use it properly.

### FHIR Resources

FHIR Resources is used to obtain the search bundle that is used in the HL7v2 mapping. Required permissions are GET and SEARCH. Listed resources are used with default mapping. If you choose to use HL7v2ModuleMapping resources, you must also provide access policies for additional resources.

1. **`DocumentReference`** - Is used as MDM root resource.
2. **`Encounter`** - Is used in PV1 segment mapping.
3. **`Patient`** - Is used in PID segment mapping.
4. **`Practitioner`** - Is used in several segments across the mapping.
5. **`Location`** - Is used in several segments across the mapping.
6. **`Organization`** - Is used in several segments across the mapping.

### HL7v2Module IG resources

In addition to the FHIR resources, the HL7v2 module defines an additional IG for persistence and configuration of the entire mapping and sending process. To properly use the module, you should also create an access policy for these resources. This custom IG should be loaded automatically when the module server starts.

Required permissions are GET, PUT and POST.

1. **`HL7v2ModuleDeliveryConfig`** - Is used to store the delivery configuration used when sending messages to consumers via sftp or webhook.
2. **`HL7v2ModuleMessage`** - It is used to store the entire state of the HL7v2 message, including the current status, the message itself, the delivery configuration reference, etc.
3. **`HL7v2ModuleMapping`** - It is used to override the default mapping, search query, and root resource.
