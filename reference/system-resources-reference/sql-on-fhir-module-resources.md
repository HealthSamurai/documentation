# SQL on FHIR Module Resources

## Overview

SQL on FHIR module includes the following resource types:

- ViewDefinition

## ViewDefinition

<table>
<thead>
<tr>
<th width="290">Path</th>
<th width="70">Card.</th>
<th width="150">Type</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr><td width="290">resource</td><td width="70">1..1</td><td width="150">code</td><td>The FHIR resource that the view is based upon, e.g. 'Patient' or 'Observation'.</td></tr>
<tr><td width="290">url</td><td width="70">0..1</td><td width="150">uri</td><td>Canonical identifier for this view definition, represented as a URI (globally unique)</td></tr>
<tr><td width="290">experimental</td><td width="70">0..1</td><td width="150">boolean</td><td>For testing purposes, not real usage</td></tr>
<tr><td width="290">constant</td><td width="70">0..*</td><td width="150">BackboneElement</td><td>A constant is a value that is injected into a FHIRPath expression through the use of a FHIRPath
external constant with the same name.</td></tr>
<tr><td width="290">constant.<strong>valueBase64Binary</strong></td><td width="70">0..1</td><td width="150">base64Binary</td><td>The value that will be substituted in place of the constant reference. This
is done by including `%your_constant_name` in a FHIRPath expression, which effectively converts
the FHIR literal defined here to a FHIRPath literal used in the path expression.

Support for additional types may be added in the future.</td></tr>
<tr><td width="290">constant.<strong>valueUri</strong></td><td width="70">0..1</td><td width="150">uri</td><td>The value that will be substituted in place of the constant reference. This
is done by including `%your_constant_name` in a FHIRPath expression, which effectively converts
the FHIR literal defined here to a FHIRPath literal used in the path expression.

Support for additional types may be added in the future.</td></tr>
<tr><td width="290">constant.<strong>valueString</strong></td><td width="70">0..1</td><td width="150">string</td><td>The value that will be substituted in place of the constant reference. This
is done by including `%your_constant_name` in a FHIRPath expression, which effectively converts
the FHIR literal defined here to a FHIRPath literal used in the path expression.

Support for additional types may be added in the future.</td></tr>
<tr><td width="290">constant.<strong>valueCode</strong></td><td width="70">0..1</td><td width="150">code</td><td>The value that will be substituted in place of the constant reference. This
is done by including `%your_constant_name` in a FHIRPath expression, which effectively converts
the FHIR literal defined here to a FHIRPath literal used in the path expression.

Support for additional types may be added in the future.</td></tr>
<tr><td width="290">constant.<strong>valuePositiveInt</strong></td><td width="70">0..1</td><td width="150">positiveInt</td><td>The value that will be substituted in place of the constant reference. This
is done by including `%your_constant_name` in a FHIRPath expression, which effectively converts
the FHIR literal defined here to a FHIRPath literal used in the path expression.

Support for additional types may be added in the future.</td></tr>
<tr><td width="290">constant.<strong>valueCanonical</strong></td><td width="70">0..1</td><td width="150">canonical</td><td>The value that will be substituted in place of the constant reference. This
is done by including `%your_constant_name` in a FHIRPath expression, which effectively converts
the FHIR literal defined here to a FHIRPath literal used in the path expression.

Support for additional types may be added in the future.</td></tr>
<tr><td width="290">constant.<strong>valueDecimal</strong></td><td width="70">0..1</td><td width="150">decimal</td><td>The value that will be substituted in place of the constant reference. This
is done by including `%your_constant_name` in a FHIRPath expression, which effectively converts
the FHIR literal defined here to a FHIRPath literal used in the path expression.

Support for additional types may be added in the future.</td></tr>
<tr><td width="290">constant.<strong>valueDateTime</strong></td><td width="70">0..1</td><td width="150">dateTime</td><td>The value that will be substituted in place of the constant reference. This
is done by including `%your_constant_name` in a FHIRPath expression, which effectively converts
the FHIR literal defined here to a FHIRPath literal used in the path expression.

Support for additional types may be added in the future.</td></tr>
<tr><td width="290">constant.<strong>name</strong></td><td width="70">1..1</td><td width="150">string</td><td>Name of constant (referred to in FHIRPath as %[name])</td></tr>
<tr><td width="290">constant.<strong>valueInstant</strong></td><td width="70">0..1</td><td width="150">instant</td><td>The value that will be substituted in place of the constant reference. This
is done by including `%your_constant_name` in a FHIRPath expression, which effectively converts
the FHIR literal defined here to a FHIRPath literal used in the path expression.

Support for additional types may be added in the future.</td></tr>
<tr><td width="290">constant.<strong>value</strong></td><td width="70">1..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">constant.<strong>valueBoolean</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>The value that will be substituted in place of the constant reference. This
is done by including `%your_constant_name` in a FHIRPath expression, which effectively converts
the FHIR literal defined here to a FHIRPath literal used in the path expression.

Support for additional types may be added in the future.</td></tr>
<tr><td width="290">constant.<strong>valueDate</strong></td><td width="70">0..1</td><td width="150">date</td><td>The value that will be substituted in place of the constant reference. This
is done by including `%your_constant_name` in a FHIRPath expression, which effectively converts
the FHIR literal defined here to a FHIRPath literal used in the path expression.

Support for additional types may be added in the future.</td></tr>
<tr><td width="290">constant.<strong>valueUnsignedInt</strong></td><td width="70">0..1</td><td width="150">unsignedInt</td><td>The value that will be substituted in place of the constant reference. This
is done by including `%your_constant_name` in a FHIRPath expression, which effectively converts
the FHIR literal defined here to a FHIRPath literal used in the path expression.

Support for additional types may be added in the future.</td></tr>
<tr><td width="290">constant.<strong>valueUuid</strong></td><td width="70">0..1</td><td width="150">uuid</td><td>The value that will be substituted in place of the constant reference. This
is done by including `%your_constant_name` in a FHIRPath expression, which effectively converts
the FHIR literal defined here to a FHIRPath literal used in the path expression.

Support for additional types may be added in the future.</td></tr>
<tr><td width="290">constant.<strong>valueTime</strong></td><td width="70">0..1</td><td width="150">time</td><td>The value that will be substituted in place of the constant reference. This
is done by including `%your_constant_name` in a FHIRPath expression, which effectively converts
the FHIR literal defined here to a FHIRPath literal used in the path expression.

Support for additional types may be added in the future.</td></tr>
<tr><td width="290">constant.<strong>valueId</strong></td><td width="70">0..1</td><td width="150">id</td><td>The value that will be substituted in place of the constant reference. This
is done by including `%your_constant_name` in a FHIRPath expression, which effectively converts
the FHIR literal defined here to a FHIRPath literal used in the path expression.

Support for additional types may be added in the future.</td></tr>
<tr><td width="290">constant.<strong>valueUrl</strong></td><td width="70">0..1</td><td width="150">url</td><td>The value that will be substituted in place of the constant reference. This
is done by including `%your_constant_name` in a FHIRPath expression, which effectively converts
the FHIR literal defined here to a FHIRPath literal used in the path expression.

Support for additional types may be added in the future.</td></tr>
<tr><td width="290">constant.<strong>valueOid</strong></td><td width="70">0..1</td><td width="150">oid</td><td>The value that will be substituted in place of the constant reference. This
is done by including `%your_constant_name` in a FHIRPath expression, which effectively converts
the FHIR literal defined here to a FHIRPath literal used in the path expression.

Support for additional types may be added in the future.</td></tr>
<tr><td width="290">constant.<strong>valueInteger</strong></td><td width="70">0..1</td><td width="150">integer</td><td>The value that will be substituted in place of the constant reference. This
is done by including `%your_constant_name` in a FHIRPath expression, which effectively converts
the FHIR literal defined here to a FHIRPath literal used in the path expression.

Support for additional types may be added in the future.</td></tr>
<tr><td width="290">where</td><td width="70">0..*</td><td width="150">BackboneElement</td><td>A series of zero or more FHIRPath constraints to filter resources for the view. Every constraint
must evaluate to true for the resource to be included in the view.</td></tr>
<tr><td width="290">where.<strong>path</strong></td><td width="70">1..1</td><td width="150">string</td><td>A FHIRPath expression that defines a filter that must evaluate to true for a resource to be
included in the output. The input context is the collection of resources of the type specified in
the resource element. Constants defined in Reference({constant}) can be referenced as %[name].</td></tr>
<tr><td width="290">where.<strong>description</strong></td><td width="70">0..1</td><td width="150">string</td><td>A human-readable description of the above where constraint.</td></tr>
<tr><td width="290">name</td><td width="70">0..1</td><td width="150">string</td><td>Name of the view definition, must be in a database-friendly format.</td></tr>
<tr><td width="290">select</td><td width="70">0..*</td><td width="150">BackboneElement</td><td>The select structure defines the columns to be used in the resulting view. These are expressed
in the `column` structure below, or in nested `select`s for nested resources.</td></tr>
<tr><td width="290">select.<strong>column</strong></td><td width="70">0..*</td><td width="150">BackboneElement</td><td>A column to be produced in the resulting table. The column is relative to the select structure
that contains it.</td></tr>
<tr><td width="290">select.<strong>column</strong>.<strong>tag</strong></td><td width="70">0..*</td><td width="150">BackboneElement</td><td>Tags can be used to attach additional metadata to columns, such as implementation-specific 
directives or database-specific type hints.</td></tr>
<tr><td width="290">select.<strong>column</strong>.<strong>tag</strong>.<strong>name</strong></td><td width="70">1..1</td><td width="150">string</td><td>A name that identifies the meaning of the tag. A namespace should be used to scope the tag to 
a particular context. For example, 'ansi/type' could be used to indicate the type that should 
be used to represent the value within an ANSI SQL database.</td></tr>
<tr><td width="290">select.<strong>column</strong>.<strong>tag</strong>.<strong>value</strong></td><td width="70">1..1</td><td width="150">string</td><td>Value of tag</td></tr>
<tr><td width="290">select.<strong>column</strong>.<strong>name</strong></td><td width="70">1..1</td><td width="150">string</td><td>Name of the column produced in the output, must be in a database-friendly format. The column 
names in the output must not have any duplicates.</td></tr>
<tr><td width="290">select.<strong>column</strong>.<strong>path</strong></td><td width="70">1..1</td><td width="150">string</td><td>A FHIRPath expression that evaluates to the value that will be output in the column for each 
resource. The input context is the collection of resources of the type specified in the resource 
element. Constants defined in Reference({constant}) can be referenced as %[name].</td></tr>
<tr><td width="290">select.<strong>column</strong>.<strong>type</strong></td><td width="70">0..1</td><td width="150">uri</td><td>A FHIR StructureDefinition URI for the column's type. Relative URIs are implicitly given
the 'http://hl7.org/fhir/StructureDefinition/' prefix. The URI may also use FHIR element ID notation to indicate
a backbone element within a structure. For instance, `Observation.referenceRange` may be specified to indicate
the returned type is that backbone element.

This field *must* be provided if a ViewDefinition returns a non-primitive type. Implementations should report an error
if the returned type does not match the type set here, or if a non-primitive type is returned but this field is unset.</td></tr>
<tr><td width="290">select.<strong>column</strong>.<strong>collection</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Indicates whether the column may have multiple values. Defaults to `false` if unset.

ViewDefinitions must have this set to `true` if multiple values may be returned. Implementations SHALL
report an error if multiple values are produced when that is not the case.</td></tr>
<tr><td width="290">select.<strong>column</strong>.<strong>description</strong></td><td width="70">0..1</td><td width="150">markdown</td><td>A human-readable description of the column.</td></tr>
<tr><td width="290">select.<strong>select</strong></td><td width="70">0..*</td><td width="150"></td><td>Nested select relative to a parent expression. If the parent `select` has a `forEach` or `forEachOrNull`, this child select will apply for each item in that expression. </td></tr>
<tr><td width="290">select.<strong>forEach</strong></td><td width="70">0..1</td><td width="150">string</td><td>A FHIRPath expression to retrieve the parent element(s) used in the containing select, relative to the root resource or parent `select`,
if applicable. `forEach` will produce a row for each element selected in the expression. For example, using forEach on `address` in Patient will
generate a new row for each address, with columns defined in the corresponding `column` structure.</td></tr>
<tr><td width="290">select.<strong>unionAll</strong></td><td width="70">0..*</td><td width="150"></td><td>A `unionAll` combines the results of multiple selection structures. Each structure under the `unionAll` must produce the same column names
and types. The results from each nested selection will then have their own row.</td></tr>
<tr><td width="290">select.<strong>forEachOrNull</strong></td><td width="70">0..1</td><td width="150">string</td><td>Same as forEach, but produces a single row with null values in the nested expression if the collection is empty. For example,
with a Patient resource, a `forEachOrNull` on address will produce a row for each patient even if there are no addresses; it will
simply set the address columns to `null`.</td></tr>
<tr><td width="290">status</td><td width="70">1..1</td><td width="150">code</td><td>draft | active | retired | unknown</td></tr>
<tr><td width="290">identifier</td><td width="70">0..1</td><td width="150">Identifier</td><td>Additional identifier for the view definition</td></tr>
<tr><td width="290">title</td><td width="70">0..1</td><td width="150">string</td><td>A optional human-readable description of the view.</td></tr>
<tr><td width="290">fhirVersion</td><td width="70">0..*</td><td width="150">code</td><td>The FHIR version(s) for the FHIR resource. The value of this element is the
formal version of the specification, without the revision number, e.g.
[publication].[major].[minor].</td></tr>
<tr><td width="290">copyright</td><td width="70">0..1</td><td width="150">markdown</td><td>Use and/or publishing restrictions</td></tr>
<tr><td width="290">publisher</td><td width="70">0..1</td><td width="150">string</td><td>Name of the publisher/steward (organization or individual)</td></tr>
<tr><td width="290">meta</td><td width="70">0..1</td><td width="150">Meta</td><td>Metadata about the view definition</td></tr>
<tr><td width="290">useContext</td><td width="70">0..*</td><td width="150">UsageContext</td><td>The context that the content is intended to support</td></tr>
<tr><td width="290">contact</td><td width="70">0..*</td><td width="150">ContactDetail</td><td>Contact details for the publisher</td></tr>
<tr><td width="290">description</td><td width="70">0..1</td><td width="150">markdown</td><td>Natural language description of the view definition</td></tr></tbody>
</table>

