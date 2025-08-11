# Default Mappings

In cases where the default mappings or search are not sufficient for your needs, you can use the `HL7v2ModuleMapping` resource.

There are a few options with this resource to make the mapping process more flexible:

1. `baseResourceType` - this is for future use to map different message types, for now only one available is DocumentReference. Anyway, this would
2. `type` - HL7v2 message type.
3. `searchQuery` - This is used to define the search query, excluding the "\_id" part..
4. `mapping` - most important part of this resource. The main idea here is that the resource mapping overrides the default segments mapping of the module. This part looks like key-value structure with segment and field names as keys and several options objects as values:
   * `fhirPath` - You can define the [fhirPath expression](https://build.fhir.org/ig/HL7/FHIRPath/) for each field or type component. This will be evaluated using the search bundle result. The search bundle is converted for easier use of fhirPath. The updated structure uses the `entry` value as the root under the `resources` key and resolves all references as resources, so keep in mind that if, for example, `Encounter.subject` reference is provided - it should automatically be converted to the entire Patient resource.
   * `fixedValue` - You can use this property to hardcode some values that shouldn't vary from message to message. When this property is provided, type mapping is skipped for that field.
   * `req` - By default, the module attempts to validate the existence of segments and fields required by the HL7v2 specification (ver 2.3.1 for this iteration). You can change this behavior by setting this property to true or false for each field.
   * `type` - By default, the module tries to resolve types according to the HL7v2 specification. If one of these types is not what you need to generate - you can define one of the existing supported HL7v2 types or define your own custom type. It uses the same concepts as field mapping, so either req, type or fhirPath should work the same way.
5. `opts` - some other options (currently only 1)
   * `timeZone` – Timezone offset must match the pattern: `Z`, `±h`, `±hh`, `±hh:mm`, `±hhmm`, `±hh:mm:ss`, `±hhmmss`

### Example:

```json
{
  "baseResourceType": "DocumentReference",
  "mapping": {
    "MSH": {
      "MSH.1": {
        "fixedValue": "|"
      },
      "MSH.2": {
        "fixedValue": "^~\\\\&"
      },
      "MSH.4": {
        "fixedValue": "^Virtual Clinic"
      },
      "MSH.9": {
        "fixedValue": "MDM^T02"
      },
      "MSH.11": {
        "fixedValue": "P^Not present"
      },
      "MSH.12": {
        "fixedValue": "2.3.1"
      }
    },
    "PID": {
      "PID.2": {
        "type": {
          "CX": [
            {
              "desc": "ID Number",
              "name": "CX.1",
              "type": "ST",
              "fhirpath": "value"
            },
            {
              "desc": "Check Digit",
              "name": "CX.2",
              "type": "ST",
              "fhirpath": "check_digit"
            },
            {
              "desc": "Check Digit Scheme",
              "name": "CX.3",
              "type": "ID",
              "fhirpath": "check_scheme"
            },
            {
              "desc": "Assigning Authority",
              "name": "CX.4",
              "type": "HD",
              "fhirpath": "iif(assigner.identifier[0].exists(), assigner.identifier[0], system)"
            },
            {
              "desc": "Identifier Type Code",
              "name": "CX.5",
              "type": "IS",
              "fhirpath": "type.coding.where(system = '<http://terminology.hl7.org/CodeSystem/v2-0203>').code"
            },
            {
              "desc": "Assigning Facility",
              "name": "CX.6",
              "type": "HD",
              "fhirpath": "assigner.identifier[0]"
            }
          ]
        },
        "fhirPath": "resources.where(resourceType = 'Patient').identifier.where(use = 'official')"
      }
    },
    "OBX": {
      "OBX.11": {
        "req": false
      }
    },
    "TXA": {
      "TXA.1": {
        "req": false
      },
      "TXA.19": {
        "fixedValue": "AV"
      }
    }
  },
  "resourceType": "HL7v2ModuleMapping",
  "searchQuery": "_include=DocumentReference:encounter:Encounter&_include=DocumentReference:author:Patient&_include=Encounter:practitioner:Practitioner&_include=Encounter:patient:Patient&_include=Encounter:service-provider:Organization&_include=Encounter:location:Location",
  "type": {
    "coding": [
      {
        "code": "MDM",
        "system": "<http://terminology.hl7.org/CodeSystem/v2-0076>"
      }
    ]
  }
}
```
