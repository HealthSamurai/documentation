# Bulk Module Resources

## Overview

Bulk module includes the following resource types:

- BulkExportStatus
- BulkImportStatus
- DisabledIndex
- LoaderFile

## BulkExportStatus

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
<tr><td width="290">url</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">transactionTime</td><td width="70">0..1</td><td width="150">dateTime</td><td></td></tr>
<tr><td width="290">output</td><td width="70">0..*</td><td width="150"></td><td></td></tr>
<tr><td width="290">output.type</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">output.url</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">output.count</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">requester</td><td width="70">0..1</td><td width="150">Reference</td><td></td></tr>
<tr><td width="290">params</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">params.since</td><td width="70">0..1</td><td width="150">dateTime</td><td></td></tr>
<tr><td width="290">params.requester</td><td width="70">0..1</td><td width="150">Reference</td><td></td></tr>
<tr><td width="290">params.output-format</td><td width="70">0..1</td><td width="150">string</td><td><b>Allowed values</b>: application/fhir+ndjson | application/ndjson+fhir | application/ndjson | ndjson | application/fhir+ndjson+gzip | application/fhir+gzip+ndjson | application/ndjson+fhir+gzip | application/ndjson+gzip+fhir | application/gzip+fhir+ndjson | application/gzip+ndjson+fhir</td></tr>
<tr><td width="290">params.storage</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">params.storage.type</td><td width="70">0..1</td><td width="150">string</td><td><b>Allowed values</b>: test-delay | test-cloud | aidbox | gcp | aws | azure</td></tr>
<tr><td width="290">params.storage.bucket</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">params.storage.account</td><td width="70">0..1</td><td width="150">Reference</td><td></td></tr>
<tr><td width="290">params.output-file-ext</td><td width="70">0..1</td><td width="150">string</td><td><b>Allowed values</b>: .ndjson | .ndjson.gz</td></tr>
<tr><td width="290">params.fhir?</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">params.types</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr><td width="290">params.tenant</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">params.patient-ids</td><td width="70">0..*</td><td width="150">string</td><td></td></tr>
<tr><td width="290">params.export-level</td><td width="70">0..1</td><td width="150">string</td><td><b>Allowed values</b>: patient | group | system</td></tr>
<tr><td width="290">params.gzip?</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">params.group-id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">error</td><td width="70">0..*</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">error.type</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">error.url</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">error.count</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">extension</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td><b>Allowed values</b>: accepted | in-progress | completed | error | cancelled</td></tr>
<tr><td width="290">request</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">requiresAccessToken</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr></tbody>
</table>


## BulkImportStatus

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
<tr><td width="290">contentEncoding</td><td width="70">0..1</td><td width="150">string</td><td><b>Allowed values</b>: gzip | plain</td></tr>
<tr><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">update</td><td width="70">0..1</td><td width="150">boolean</td><td></td></tr>
<tr><td width="290">mode</td><td width="70">0..1</td><td width="150">string</td><td><b>Allowed values</b>: bulk | transaction</td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td><b>Allowed values</b>: active | failed | cancelled | finished</td></tr>
<tr><td width="290">time</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">time.end</td><td width="70">0..1</td><td width="150">dateTime</td><td></td></tr>
<tr><td width="290">time.start</td><td width="70">0..1</td><td width="150">dateTime</td><td></td></tr>
<tr><td width="290">source</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">type</td><td width="70">0..1</td><td width="150">string</td><td><b>Allowed values</b>: aidbox | fhir</td></tr>
<tr><td width="290">inputFormat</td><td width="70">0..1</td><td width="150">string</td><td><b>Allowed values</b>: application/fhir+ndjson</td></tr>
<tr><td width="290">meta</td><td width="70">0..1</td><td width="150">Meta</td><td></td></tr>
<tr><td width="290">inputs</td><td width="70">0..*</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">inputs.time</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">inputs.resourceType</td><td width="70">1..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">storageDetail</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
</table>


## DisabledIndex

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
<tr><td width="290">indexname</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">tablespace</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">indexdef</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td><b>Allowed values</b>: restored | disabled</td></tr>
<tr><td width="290">schemaname</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">meta</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">restore</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">restore.start</td><td width="70">0..1</td><td width="150">dateTime</td><td></td></tr>
<tr><td width="290">restore.end</td><td width="70">0..1</td><td width="150">dateTime</td><td></td></tr>
<tr><td width="290">restore.duration</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">tablename</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
</table>


## LoaderFile

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
<tr><td width="290">message</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">loaded</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">bucket</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">error</td><td width="70">0..1</td><td width="150">BackboneElement</td><td></td></tr>
<tr><td width="290">error.source</td><td width="70">0..1</td><td width="150">string</td><td><b>Allowed values</b>: postgres | aws | aidbox</td></tr>
<tr><td width="290">error.code</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td><b>Allowed values</b>: pending | in-progress | done | error | skiped</td></tr>
<tr><td width="290">start</td><td width="70">0..1</td><td width="150">dateTime</td><td></td></tr>
<tr><td width="290">type</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">meta</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">file</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">last-modified</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">size</td><td width="70">0..1</td><td width="150">number</td><td></td></tr>
<tr><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">end</td><td width="70">0..1</td><td width="150">dateTime</td><td></td></tr></tbody>
</table>

