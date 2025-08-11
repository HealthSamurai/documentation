# Module Limitations

This module is implemented to map only one type of message (MDM v2.3.1) with default mappings, but it is designed to be flexible for custom mappings and easy to support another types generation. Regarding above there is some limitations:

* Module is supported to generate only MDM T02 messages v2.3.1.
* Module is not tested properly with repeatable segments, but has logic to support this.
* By default the module trying to generate message according to the HL7v2 v2.3.1 specification. If expected result is differs, than you should redefine needed segments via [HL7v2ModuleMapping](default-mappings.md) resource.
* Module is supporting only types that is used in MDM T02, another types may be defined via [HL7v2ModuleMapping](default-mappings.md) resource.

#### Supported complex types:

There is a list of supported complex type mappings out of the box below. All other types can be defined directly through the HL7v2ModuleMapping resource.

* CX
* EI
* TS
* XCN
* PT
* CM\_MSG
* CE VID
* HD
* XPN
* XTN
* XAD
* PL
