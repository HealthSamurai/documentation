# HL7v2 Module Resources

Resources for configuration Aidbox HL7v2 module.

 ## Hl7v2Config

```fhir-structure
[ {
  "path" : "_source",
  "name" : "_source",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "System Property. DO NOT USE IT."
}, {
  "path" : "extensions",
  "name" : "extensions",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "",
  "desc" : "Extensions for the HL7v2 message processing configuration."
}, {
  "path" : "extensions.after",
  "name" : "after",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "extensions.msh",
  "name" : "msh",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Message header information."
}, {
  "path" : "extensions.segment",
  "name" : "segment",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "HL7v2 segment identifier."
}, {
  "path" : "extensions.fields",
  "name" : "fields",
  "lvl" : 1,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "Fields within the segment."
}, {
  "path" : "extensions.fields.key",
  "name" : "key",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "extensions.fields.name",
  "name" : "name",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "extensions.fields.type",
  "name" : "type",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "extensions.quantifier",
  "name" : "quantifier",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Occurrence quantifier. \n\n**Allowed values**: `*` | `?` | `+`"
}, {
  "path" : "isStrict",
  "name" : "isStrict",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether strict validation should be applied."
}, {
  "path" : "mapping",
  "name" : "mapping",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to a mapping resource for message transformation. \n\n**Allowed references**: Mapping"
}, {
  "path" : "sortTopLevelExtensions",
  "name" : "sortTopLevelExtensions",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether to sort top-level extensions during processing."
}, {
  "path" : "text",
  "name" : "text",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Narrative text representation."
}, {
  "path" : "text.div",
  "name" : "div",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "HTML content of the narrative."
}, {
  "path" : "text.status",
  "name" : "status",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Status of the narrative text."
} ]
```


## Hl7v2Message

```fhir-structure
[ {
  "path" : "_source",
  "name" : "_source",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "System Property. DO NOT USE IT."
}, {
  "path" : "apiOperation",
  "name" : "apiOperation",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "API operation to be performed with this message."
}, {
  "path" : "config",
  "name" : "config",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to the configuration for processing this message. \n\n**Allowed references**: Hl7v2Config"
}, {
  "path" : "event",
  "name" : "event",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "code",
  "desc" : "Event type code from the HL7v2 message."
}, {
  "path" : "outcome",
  "name" : "outcome",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Outcome of message processing."
}, {
  "path" : "parsed",
  "name" : "parsed",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Parsed content of the HL7v2 message."
}, {
  "path" : "src",
  "name" : "src",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Original source text of the HL7v2 message."
}, {
  "path" : "status",
  "name" : "status",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Processing status of the message (received, processed, or error). \n\n**Allowed values**: `received` | `processed` | `error`"
}, {
  "path" : "type",
  "name" : "type",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "code",
  "desc" : "Message type code from the HL7v2 message."
} ]
```

