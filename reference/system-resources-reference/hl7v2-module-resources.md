# HL7v2 Module Resources

## Overview

HL7v2 module includes the following resource types:

- Hl7v2Config
- Hl7v2Message

## Hl7v2Config

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
<tr><td width="290">extensions</td><td width="70">0..*</td><td width="150"></td><td></td></tr>
<tr><td width="290">extensions.after</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">extensions.msh</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">extensions.segment</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">extensions.fields</td><td width="70">0..*</td><td width="150"></td><td></td></tr>
<tr><td width="290">extensions.quantifier</td><td width="70">0..1</td><td width="150">string</td><td><b>Allowed values</b>: * | ? | +</td></tr>
<tr><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">isStrict</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">sortTopLevelExtensions</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">text</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">text.div</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">text.status</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">mapping</td><td width="70">0..1</td><td width="150">Reference</td><td>References: Mapping</td></tr>
<tr><td width="290">meta</td><td width="70">0..1</td><td width="150">Meta</td><td></td></tr>
<tr><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
</table>


## Hl7v2Message

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
<tr><td width="290">apiOperation</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">config</td><td width="70">0..1</td><td width="150">Reference</td><td>References: Hl7v2Config</td></tr>
<tr><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">event</td><td width="70">0..1</td><td width="150">code</td><td></td></tr>
<tr><td width="290">src</td><td width="70">1..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">status</td><td width="70">1..1</td><td width="150">string</td><td><b>Allowed values</b>: received | processed | error</td></tr>
<tr><td width="290">parsed</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">type</td><td width="70">0..1</td><td width="150">code</td><td></td></tr>
<tr><td width="290">meta</td><td width="70">0..1</td><td width="150">Meta</td><td></td></tr>
<tr><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">outcome</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr></tbody>
</table>

