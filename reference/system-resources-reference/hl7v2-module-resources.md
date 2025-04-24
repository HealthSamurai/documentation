# HL7v2 Module Resources

Resources for configuration Aidbox HL7v2 module.

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
<tr><td width="290">extensions</td><td width="70">0..*</td><td width="150"></td><td>Extensions for the HL7v2 message processing configuration.</td></tr>
<tr><td width="290">extensions.<strong>after</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">extensions.<strong>msh</strong></td><td width="70">0..1</td><td width="150">string</td><td>Message header information.</td></tr>
<tr><td width="290">extensions.<strong>segment</strong></td><td width="70">0..1</td><td width="150">string</td><td>HL7v2 segment identifier.</td></tr>
<tr><td width="290">extensions.<strong>fields</strong></td><td width="70">0..*</td><td width="150">BackboneElement</td><td>Fields within the segment.</td></tr>
<tr><td width="290">extensions.<strong>fields</strong>.<strong>key</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">extensions.<strong>fields</strong>.<strong>name</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">extensions.<strong>fields</strong>.<strong>type</strong></td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">extensions.<strong>quantifier</strong></td><td width="70">0..1</td><td width="150">string</td><td>Occurrence quantifier. 

<strong>Allowed values</strong>: * | ? | +</td></tr>
<tr><td width="290">isStrict</td><td width="70">0..1</td><td width="150">boolean</td><td>Whether strict validation should be applied.</td></tr>
<tr><td width="290">sortTopLevelExtensions</td><td width="70">0..1</td><td width="150">boolean</td><td>Whether to sort top-level extensions during processing.</td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">text</td><td width="70">0..1</td><td width="150"></td><td>Narrative text representation.</td></tr>
<tr><td width="290">text.<strong>div</strong></td><td width="70">0..1</td><td width="150">string</td><td>HTML content of the narrative.</td></tr>
<tr><td width="290">text.<strong>status</strong></td><td width="70">0..1</td><td width="150">string</td><td>Status of the narrative text.</td></tr>
<tr><td width="290">mapping</td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to a mapping resource for message transformation. 

<strong>Allowed references</strong>: Mapping</td></tr></tbody>
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
<tr><td width="290">apiOperation</td><td width="70">0..1</td><td width="150">string</td><td>API operation to be performed with this message.</td></tr>
<tr><td width="290">config</td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to the configuration for processing this message. 

<strong>Allowed references</strong>: Hl7v2Config</td></tr>
<tr><td width="290">event</td><td width="70">0..1</td><td width="150">code</td><td>Event type code from the HL7v2 message.</td></tr>
<tr><td width="290">src</td><td width="70">1..1</td><td width="150">string</td><td>Original source text of the HL7v2 message.</td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">status</td><td width="70">1..1</td><td width="150">string</td><td>Processing status of the message (received, processed, or error). 

<strong>Allowed values</strong>: received | processed | error</td></tr>
<tr><td width="290">parsed</td><td width="70">0..1</td><td width="150">Object</td><td>Parsed content of the HL7v2 message.</td></tr>
<tr><td width="290">type</td><td width="70">0..1</td><td width="150">code</td><td>Message type code from the HL7v2 message.</td></tr>
<tr><td width="290">outcome</td><td width="70">0..1</td><td width="150">Object</td><td>Outcome of message processing.</td></tr></tbody>
</table>

