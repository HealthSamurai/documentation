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
<tr class="top-element required-field"><td width="290">resource</td><td width="70">1..1</td><td width="150">code</td><td></td></tr>
<tr class="top-element"><td width="290">url</td><td width="70">0..1</td><td width="150">uri</td><td></td></tr>
<tr class="top-element"><td width="290">experimental</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr class="top-element"><td width="290">constant</td><td width="70">0..*</td><td width="150">BackboneElement</td><td></td></tr>
<tr class="nested-element"><td width="290">constant.valueBase64Binary</td><td width="70">0..1</td><td width="150">base64Binary</td><td></td></tr>
<tr class="nested-element"><td width="290">constant.valueUri</td><td width="70">0..1</td><td width="150">uri</td><td></td></tr>
<tr class="nested-element"><td width="290">constant.valueString</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">constant.valueCode</td><td width="70">0..1</td><td width="150">code</td><td></td></tr>
<tr class="nested-element"><td width="290">constant.valuePositiveInt</td><td width="70">0..1</td><td width="150">positiveInt</td><td></td></tr>
<tr class="nested-element"><td width="290">constant.valueCanonical</td><td width="70">0..1</td><td width="150">canonical</td><td></td></tr>
<tr class="nested-element"><td width="290">constant.valueDecimal</td><td width="70">0..1</td><td width="150">decimal</td><td></td></tr>
<tr class="nested-element"><td width="290">constant.valueDateTime</td><td width="70">0..1</td><td width="150">dateTime</td><td></td></tr>
<tr class="nested-element required-field"><td width="290">constant.name</td><td width="70">1..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">constant.valueInstant</td><td width="70">0..1</td><td width="150">instant</td><td></td></tr>
<tr class="nested-element required-field"><td width="290">constant.value</td><td width="70">1..1</td><td width="150"></td><td></td></tr>
<tr class="nested-element"><td width="290">constant.valueBoolean</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr class="nested-element"><td width="290">constant.valueDate</td><td width="70">0..1</td><td width="150">date</td><td></td></tr>
<tr class="nested-element"><td width="290">constant.valueUnsignedInt</td><td width="70">0..1</td><td width="150">unsignedInt</td><td></td></tr>
<tr class="nested-element"><td width="290">constant.valueUuid</td><td width="70">0..1</td><td width="150">uuid</td><td></td></tr>
<tr class="nested-element"><td width="290">constant.valueTime</td><td width="70">0..1</td><td width="150">time</td><td></td></tr>
<tr class="nested-element"><td width="290">constant.valueId</td><td width="70">0..1</td><td width="150">id</td><td></td></tr>
<tr class="nested-element"><td width="290">constant.valueUrl</td><td width="70">0..1</td><td width="150">url</td><td></td></tr>
<tr class="nested-element"><td width="290">constant.valueOid</td><td width="70">0..1</td><td width="150">oid</td><td></td></tr>
<tr class="nested-element"><td width="290">constant.valueInteger</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr class="top-element"><td width="290">where</td><td width="70">0..*</td><td width="150">BackboneElement</td><td></td></tr>
<tr class="nested-element required-field"><td width="290">where.path</td><td width="70">1..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">where.description</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">name</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element required-field"><td width="290">select</td><td width="70">0..*</td><td width="150">BackboneElement</td><td></td></tr>
<tr class="nested-element"><td width="290">select.column</td><td width="70">0..*</td><td width="150">BackboneElement</td><td></td></tr>
<tr class="nested-element"><td width="290">select.column.tag</td><td width="70">0..*</td><td width="150">BackboneElement</td><td></td></tr>
<tr class="nested-element required-field"><td width="290">select.column.tag.name</td><td width="70">1..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element required-field"><td width="290">select.column.tag.value</td><td width="70">1..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element required-field"><td width="290">select.column.name</td><td width="70">1..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element required-field"><td width="290">select.column.path</td><td width="70">1..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">select.column.type</td><td width="70">0..1</td><td width="150">uri</td><td></td></tr>
<tr class="nested-element"><td width="290">select.column.collection</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr class="nested-element"><td width="290">select.column.description</td><td width="70">0..1</td><td width="150">markdown</td><td></td></tr>
<tr class="nested-element"><td width="290">select.select</td><td width="70">0..*</td><td width="150"></td><td></td></tr>
<tr class="nested-element"><td width="290">select.forEach</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="nested-element"><td width="290">select.unionAll</td><td width="70">0..*</td><td width="150"></td><td></td></tr>
<tr class="nested-element"><td width="290">select.forEachOrNull</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element required-field"><td width="290">status</td><td width="70">1..1</td><td width="150">code</td><td></td></tr>
<tr class="top-element"><td width="290">identifier</td><td width="70">0..1</td><td width="150">Identifier</td><td></td></tr>
<tr class="top-element"><td width="290">title</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">fhirVersion</td><td width="70">0..*</td><td width="150">code</td><td></td></tr>
<tr class="top-element"><td width="290">copyright</td><td width="70">0..1</td><td width="150">markdown</td><td></td></tr>
<tr class="top-element"><td width="290">publisher</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr class="top-element"><td width="290">meta</td><td width="70">0..1</td><td width="150">Meta</td><td></td></tr>
<tr class="top-element"><td width="290">useContext</td><td width="70">0..*</td><td width="150">UsageContext</td><td></td></tr>
<tr class="top-element"><td width="290">contact</td><td width="70">0..*</td><td width="150">ContactDetail</td><td></td></tr>
<tr class="top-element"><td width="290">description</td><td width="70">0..1</td><td width="150">markdown</td><td></td></tr></tbody>
</table>

