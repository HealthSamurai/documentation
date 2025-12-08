---
description: Aidbox SQL on FHIR module resources for defining and executing SQL views over FHIR data.
---

# SQL on FHIR Module Resources

## ViewDefinition

```fhir-structure
[ {
  "path" : "constant",
  "name" : "constant",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "A constant is a value that is injected into a FHIRPath expression through the use of a FHIRPath\nexternal constant with the same name."
}, {
  "path" : "constant.valueBase64Binary",
  "name" : "valueBase64Binary",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "base64Binary",
  "desc" : "The value that will be substituted in place of the constant reference. This\nis done by including `%your_constant_name` in a FHIRPath expression, which effectively converts\nthe FHIR literal defined here to a FHIRPath literal used in the path expression.\n\nSupport for additional types may be added in the future."
}, {
  "path" : "constant.valueUri",
  "name" : "valueUri",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "uri",
  "desc" : "The value that will be substituted in place of the constant reference. This\nis done by including `%your_constant_name` in a FHIRPath expression, which effectively converts\nthe FHIR literal defined here to a FHIRPath literal used in the path expression.\n\nSupport for additional types may be added in the future."
}, {
  "path" : "constant.valueString",
  "name" : "valueString",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The value that will be substituted in place of the constant reference. This\nis done by including `%your_constant_name` in a FHIRPath expression, which effectively converts\nthe FHIR literal defined here to a FHIRPath literal used in the path expression.\n\nSupport for additional types may be added in the future."
}, {
  "path" : "constant.valueCode",
  "name" : "valueCode",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "code",
  "desc" : "The value that will be substituted in place of the constant reference. This\nis done by including `%your_constant_name` in a FHIRPath expression, which effectively converts\nthe FHIR literal defined here to a FHIRPath literal used in the path expression.\n\nSupport for additional types may be added in the future."
}, {
  "path" : "constant.valuePositiveInt",
  "name" : "valuePositiveInt",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "positiveInt",
  "desc" : "The value that will be substituted in place of the constant reference. This\nis done by including `%your_constant_name` in a FHIRPath expression, which effectively converts\nthe FHIR literal defined here to a FHIRPath literal used in the path expression.\n\nSupport for additional types may be added in the future."
}, {
  "path" : "constant.valueCanonical",
  "name" : "valueCanonical",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "canonical",
  "desc" : "The value that will be substituted in place of the constant reference. This\nis done by including `%your_constant_name` in a FHIRPath expression, which effectively converts\nthe FHIR literal defined here to a FHIRPath literal used in the path expression.\n\nSupport for additional types may be added in the future."
}, {
  "path" : "constant.valueDecimal",
  "name" : "valueDecimal",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "decimal",
  "desc" : "The value that will be substituted in place of the constant reference. This\nis done by including `%your_constant_name` in a FHIRPath expression, which effectively converts\nthe FHIR literal defined here to a FHIRPath literal used in the path expression.\n\nSupport for additional types may be added in the future."
}, {
  "path" : "constant.valueDateTime",
  "name" : "valueDateTime",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "The value that will be substituted in place of the constant reference. This\nis done by including `%your_constant_name` in a FHIRPath expression, which effectively converts\nthe FHIR literal defined here to a FHIRPath literal used in the path expression.\n\nSupport for additional types may be added in the future."
}, {
  "path" : "constant.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Name of constant (referred to in FHIRPath as %[name])"
}, {
  "path" : "constant.valueInstant",
  "name" : "valueInstant",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "instant",
  "desc" : "The value that will be substituted in place of the constant reference. This\nis done by including `%your_constant_name` in a FHIRPath expression, which effectively converts\nthe FHIR literal defined here to a FHIRPath literal used in the path expression.\n\nSupport for additional types may be added in the future."
}, {
  "path" : "constant.value",
  "name" : "value",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "constant.valueBoolean",
  "name" : "valueBoolean",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "The value that will be substituted in place of the constant reference. This\nis done by including `%your_constant_name` in a FHIRPath expression, which effectively converts\nthe FHIR literal defined here to a FHIRPath literal used in the path expression.\n\nSupport for additional types may be added in the future."
}, {
  "path" : "constant.valueDate",
  "name" : "valueDate",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "date",
  "desc" : "The value that will be substituted in place of the constant reference. This\nis done by including `%your_constant_name` in a FHIRPath expression, which effectively converts\nthe FHIR literal defined here to a FHIRPath literal used in the path expression.\n\nSupport for additional types may be added in the future."
}, {
  "path" : "constant.valueUnsignedInt",
  "name" : "valueUnsignedInt",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "unsignedInt",
  "desc" : "The value that will be substituted in place of the constant reference. This\nis done by including `%your_constant_name` in a FHIRPath expression, which effectively converts\nthe FHIR literal defined here to a FHIRPath literal used in the path expression.\n\nSupport for additional types may be added in the future."
}, {
  "path" : "constant.valueUuid",
  "name" : "valueUuid",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "uuid",
  "desc" : "The value that will be substituted in place of the constant reference. This\nis done by including `%your_constant_name` in a FHIRPath expression, which effectively converts\nthe FHIR literal defined here to a FHIRPath literal used in the path expression.\n\nSupport for additional types may be added in the future."
}, {
  "path" : "constant.valueTime",
  "name" : "valueTime",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "time",
  "desc" : "The value that will be substituted in place of the constant reference. This\nis done by including `%your_constant_name` in a FHIRPath expression, which effectively converts\nthe FHIR literal defined here to a FHIRPath literal used in the path expression.\n\nSupport for additional types may be added in the future."
}, {
  "path" : "constant.valueId",
  "name" : "valueId",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "id",
  "desc" : "The value that will be substituted in place of the constant reference. This\nis done by including `%your_constant_name` in a FHIRPath expression, which effectively converts\nthe FHIR literal defined here to a FHIRPath literal used in the path expression.\n\nSupport for additional types may be added in the future."
}, {
  "path" : "constant.valueUrl",
  "name" : "valueUrl",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "url",
  "desc" : "The value that will be substituted in place of the constant reference. This\nis done by including `%your_constant_name` in a FHIRPath expression, which effectively converts\nthe FHIR literal defined here to a FHIRPath literal used in the path expression.\n\nSupport for additional types may be added in the future."
}, {
  "path" : "constant.valueOid",
  "name" : "valueOid",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "oid",
  "desc" : "The value that will be substituted in place of the constant reference. This\nis done by including `%your_constant_name` in a FHIRPath expression, which effectively converts\nthe FHIR literal defined here to a FHIRPath literal used in the path expression.\n\nSupport for additional types may be added in the future."
}, {
  "path" : "constant.valueInteger",
  "name" : "valueInteger",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "The value that will be substituted in place of the constant reference. This\nis done by including `%your_constant_name` in a FHIRPath expression, which effectively converts\nthe FHIR literal defined here to a FHIRPath literal used in the path expression.\n\nSupport for additional types may be added in the future."
}, {
  "path" : "contact",
  "name" : "contact",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "ContactDetail",
  "desc" : "Contact details for the publisher"
}, {
  "path" : "copyright",
  "name" : "copyright",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "markdown",
  "desc" : "Use and/or publishing restrictions"
}, {
  "path" : "description",
  "name" : "description",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "markdown",
  "desc" : "Natural language description of the view definition"
}, {
  "path" : "experimental",
  "name" : "experimental",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "For testing purposes, not real usage"
}, {
  "path" : "fhirVersion",
  "name" : "fhirVersion",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "code",
  "desc" : "The FHIR version(s) for the FHIR resource. The value of this element is the\nformal version of the specification, without the revision number, e.g.\n[publication].[major].[minor]."
}, {
  "path" : "identifier",
  "name" : "identifier",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Identifier",
  "desc" : "Additional identifier for the view definition"
}, {
  "path" : "meta",
  "name" : "meta",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Meta",
  "desc" : "Metadata about the view definition"
}, {
  "path" : "name",
  "name" : "name",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Name of the view definition, must be in a database-friendly format."
}, {
  "path" : "publisher",
  "name" : "publisher",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Name of the publisher/steward (organization or individual)"
}, {
  "path" : "resource",
  "name" : "resource",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "code",
  "desc" : "The FHIR resource that the view is based upon, e.g. 'Patient' or 'Observation'."
}, {
  "path" : "select",
  "name" : "select",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "The select structure defines the columns to be used in the resulting view. These are expressed\nin the `column` structure below, or in nested `select`s for nested resources."
}, {
  "path" : "select.column",
  "name" : "column",
  "lvl" : 1,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "A column to be produced in the resulting table. The column is relative to the select structure\nthat contains it."
}, {
  "path" : "select.column.tag",
  "name" : "tag",
  "lvl" : 2,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "Tags can be used to attach additional metadata to columns, such as implementation-specific \ndirectives or database-specific type hints."
}, {
  "path" : "select.column.tag.name",
  "name" : "name",
  "lvl" : 3,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "A name that identifies the meaning of the tag. A namespace should be used to scope the tag to \na particular context. For example, 'ansi/type' could be used to indicate the type that should \nbe used to represent the value within an ANSI SQL database."
}, {
  "path" : "select.column.tag.value",
  "name" : "value",
  "lvl" : 3,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Value of tag"
}, {
  "path" : "select.column.name",
  "name" : "name",
  "lvl" : 2,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Name of the column produced in the output, must be in a database-friendly format. The column \nnames in the output must not have any duplicates."
}, {
  "path" : "select.column.path",
  "name" : "path",
  "lvl" : 2,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "A FHIRPath expression that evaluates to the value that will be output in the column for each \nresource. The input context is the collection of resources of the type specified in the resource \nelement. Constants defined in Reference({constant}) can be referenced as %[name]."
}, {
  "path" : "select.column.type",
  "name" : "type",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "uri",
  "desc" : "A FHIR StructureDefinition URI for the column's type. Relative URIs are implicitly given\nthe 'http://hl7.org/fhir/StructureDefinition/' prefix. The URI may also use FHIR element ID notation to indicate\na backbone element within a structure. For instance, `Observation.referenceRange` may be specified to indicate\nthe returned type is that backbone element.\n\nThis field *must* be provided if a ViewDefinition returns a non-primitive type. Implementations should report an error\nif the returned type does not match the type set here, or if a non-primitive type is returned but this field is unset."
}, {
  "path" : "select.column.collection",
  "name" : "collection",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Indicates whether the column may have multiple values. Defaults to `false` if unset.\n\nViewDefinitions must have this set to `true` if multiple values may be returned. Implementations SHALL\nreport an error if multiple values are produced when that is not the case."
}, {
  "path" : "select.column.description",
  "name" : "description",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "markdown",
  "desc" : "A human-readable description of the column."
}, {
  "path" : "select.select",
  "name" : "select",
  "lvl" : 1,
  "min" : 0,
  "max" : "*",
  "type" : "",
  "desc" : "Nested select relative to a parent expression. If the parent `select` has a `forEach` or `forEachOrNull`, this child select will apply for each item in that expression. "
}, {
  "path" : "select.forEach",
  "name" : "forEach",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A FHIRPath expression to retrieve the parent element(s) used in the containing select, relative to the root resource or parent `select`,\nif applicable. `forEach` will produce a row for each element selected in the expression. For example, using forEach on `address` in Patient will\ngenerate a new row for each address, with columns defined in the corresponding `column` structure."
}, {
  "path" : "select.unionAll",
  "name" : "unionAll",
  "lvl" : 1,
  "min" : 0,
  "max" : "*",
  "type" : "",
  "desc" : "A `unionAll` combines the results of multiple selection structures. Each structure under the `unionAll` must produce the same column names\nand types. The results from each nested selection will then have their own row."
}, {
  "path" : "select.forEachOrNull",
  "name" : "forEachOrNull",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Same as forEach, but produces a single row with null values in the nested expression if the collection is empty. For example,\nwith a Patient resource, a `forEachOrNull` on address will produce a row for each patient even if there are no addresses; it will\nsimply set the address columns to `null`."
}, {
  "path" : "select.repeat",
  "name" : "repeat",
  "lvl" : 1,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : ""
}, {
  "path" : "status",
  "name" : "status",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "code",
  "desc" : "draft | active | retired | unknown"
}, {
  "path" : "title",
  "name" : "title",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A optional human-readable description of the view."
}, {
  "path" : "url",
  "name" : "url",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "uri",
  "desc" : "Canonical identifier for this view definition, represented as a URI (globally unique)"
}, {
  "path" : "useContext",
  "name" : "useContext",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "UsageContext",
  "desc" : "The context that the content is intended to support"
}, {
  "path" : "where",
  "name" : "where",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "A series of zero or more FHIRPath constraints to filter resources for the view. Every constraint\nmust evaluate to true for the resource to be included in the view."
}, {
  "path" : "where.path",
  "name" : "path",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "A FHIRPath expression that defines a filter that must evaluate to true for a resource to be\nincluded in the output. The input context is the collection of resources of the type specified in\nthe resource element. Constants defined in Reference({constant}) can be referenced as %[name]."
}, {
  "path" : "where.description",
  "name" : "description",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A human-readable description of the above where constraint."
} ]
```

