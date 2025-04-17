# Bulk Module Resources


           Resources for configuration and management Aidbox Bulk operations.

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
<tr><td width="290">url</td><td width="70">0..1</td><td width="150">string</td><td>URL where the client can check the export status.</td></tr>
<tr><td width="290">transactionTime</td><td width="70">0..1</td><td width="150">dateTime</td><td>Time when the bulk export was initiated.</td></tr>
<tr><td width="290">output</td><td width="70">0..*</td><td width="150"></td><td>Information about the exported data files.</td></tr>
<tr><td width="290">output.<strong>type</strong></td><td width="70">0..1</td><td width="150">string</td><td>FHIR resource type for this output file.</td></tr>
<tr><td width="290">output.<strong>url</strong></td><td width="70">0..1</td><td width="150">string</td><td>URL where the client can download this output file.</td></tr>
<tr><td width="290">output.<strong>count</strong></td><td width="70">0..1</td><td width="150">integer</td><td>Number of resources in this output file.</td></tr>
<tr><td width="290">requester</td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to the user or client that requested the export.</td></tr>
<tr><td width="290">params</td><td width="70">0..1</td><td width="150">BackboneElement</td><td>Parameters specified for the bulk export request.</td></tr>
<tr><td width="290">params.<strong>since</strong></td><td width="70">0..1</td><td width="150">dateTime</td><td>Export only resources modified after this date.</td></tr>
<tr><td width="290">params.<strong>requester</strong></td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to the user or client that requested the export.</td></tr>
<tr><td width="290">params.<strong>output-format</strong></td><td width="70">0..1</td><td width="150">string</td><td>Format of the exported data files. 

<strong>Allowed values</strong>: application/fhir+ndjson | application/ndjson+fhir | application/ndjson | ndjson | application/fhir+ndjson+gzip | application/fhir+gzip+ndjson | application/ndjson+fhir+gzip | application/ndjson+gzip+fhir | application/gzip+fhir+ndjson | application/gzip+ndjson+fhir</td></tr>
<tr><td width="290">params.<strong>storage</strong></td><td width="70">0..1</td><td width="150">BackboneElement</td><td>Storage configuration for exported files.</td></tr>
<tr><td width="290">params.<strong>storage</strong>.<strong>type</strong></td><td width="70">0..1</td><td width="150">string</td><td>Type of storage backend to use. 

<strong>Allowed values</strong>: test-delay | test-cloud | aidbox | gcp | aws | azure</td></tr>
<tr><td width="290">params.<strong>storage</strong>.<strong>bucket</strong></td><td width="70">0..1</td><td width="150">string</td><td>Name of the storage bucket to use.</td></tr>
<tr><td width="290">params.<strong>storage</strong>.<strong>account</strong></td><td width="70">0..1</td><td width="150">Reference</td><td>Reference to account with credentials for accessing the storage.</td></tr>
<tr><td width="290">params.<strong>output-file-ext</strong></td><td width="70">0..1</td><td width="150">string</td><td>File extension for the exported files. 

<strong>Allowed values</strong>: .ndjson | .ndjson.gz</td></tr>
<tr><td width="290">params.<strong>fhir?</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Whether to export in FHIR format.</td></tr>
<tr><td width="290">params.<strong>types</strong></td><td width="70">0..*</td><td width="150">string</td><td>List of resource types to include in the export.</td></tr>
<tr><td width="290">params.<strong>tenant</strong></td><td width="70">0..1</td><td width="150">string</td><td>Tenant identifier for multi-tenant environments.</td></tr>
<tr><td width="290">params.<strong>patient-ids</strong></td><td width="70">0..*</td><td width="150">string</td><td>List of patient IDs to export data for.</td></tr>
<tr><td width="290">params.<strong>export-level</strong></td><td width="70">0..1</td><td width="150">string</td><td>Level at which to perform the export (patient, group, or system). 

<strong>Allowed values</strong>: patient | group | system</td></tr>
<tr><td width="290">params.<strong>gzip?</strong></td><td width="70">0..1</td><td width="150">boolean</td><td>Whether to compress the exported files using gzip.</td></tr>
<tr><td width="290">params.<strong>group-id</strong></td><td width="70">0..1</td><td width="150">string</td><td>ID of the group to export data for, when export-level is 'group'.</td></tr>
<tr><td width="290">error</td><td width="70">0..*</td><td width="150">BackboneElement</td><td>Information about errors that occurred during export.</td></tr>
<tr><td width="290">error.<strong>type</strong></td><td width="70">0..1</td><td width="150">string</td><td>Type of the resource that caused the error.</td></tr>
<tr><td width="290">error.<strong>url</strong></td><td width="70">0..1</td><td width="150">string</td><td>URL with detailed error information.</td></tr>
<tr><td width="290">error.<strong>count</strong></td><td width="70">0..1</td><td width="150">integer</td><td>Number of resources that encountered errors.</td></tr>
<tr><td width="290">extension</td><td width="70">0..1</td><td width="150"></td><td>Additional information about the export.</td></tr>
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td>Current status of the bulk export operation. 

<strong>Allowed values</strong>: accepted | in-progress | completed | error | cancelled</td></tr>
<tr><td width="290">request</td><td width="70">0..1</td><td width="150">string</td><td>Original request that initiated this export.</td></tr>
<tr><td width="290">requiresAccessToken</td><td width="70">0..1</td><td width="150">boolean</td><td>Whether an access token is required to download the exported files.</td></tr></tbody>
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
<tr><td width="290">contentEncoding</td><td width="70">0..1</td><td width="150">string</td><td>Encoding of the imported content (gzip or plain). 

<strong>Allowed values</strong>: gzip | plain</td></tr>
<tr><td width="290">update</td><td width="70">0..1</td><td width="150">boolean</td><td>Whether to update existing resources during import.</td></tr>
<tr><td width="290">mode</td><td width="70">0..1</td><td width="150">string</td><td>Mode of import operation (bulk or transaction). 

<strong>Allowed values</strong>: bulk | transaction</td></tr>
<tr><td width="290">_source</td><td width="70">0..1</td><td width="150">string</td><td>System Property. DO NOT USE IT.</td></tr>
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td>Current status of the import operation. 

<strong>Allowed values</strong>: active | failed | cancelled | finished</td></tr>
<tr><td width="290">time</td><td width="70">0..1</td><td width="150">BackboneElement</td><td>Timing information for the import operation.</td></tr>
<tr><td width="290">time.<strong>end</strong></td><td width="70">0..1</td><td width="150">dateTime</td><td>Time when the import operation completed.</td></tr>
<tr><td width="290">time.<strong>start</strong></td><td width="70">0..1</td><td width="150">dateTime</td><td>Time when the import operation started.</td></tr>
<tr><td width="290">source</td><td width="70">0..1</td><td width="150">string</td><td>Source location of the imported data.</td></tr>
<tr><td width="290">type</td><td width="70">0..1</td><td width="150">string</td><td>Type of data being imported (aidbox or fhir). 

<strong>Allowed values</strong>: aidbox | fhir</td></tr>
<tr><td width="290">inputFormat</td><td width="70">0..1</td><td width="150">string</td><td>Format of the input data files. 

<strong>Allowed values</strong>: application/fhir+ndjson</td></tr>
<tr><td width="290">inputs</td><td width="70">0..*</td><td width="150">BackboneElement</td><td>Information about input files processed during import.</td></tr>
<tr><td width="290">inputs.<strong>time</strong></td><td width="70">0..1</td><td width="150">integer</td><td>Time taken to process this input file in milliseconds.</td></tr>
<tr><td width="290">inputs.<strong>resourceType</strong></td><td width="70">1..1</td><td width="150">string</td><td>Type of resources contained in this input file.</td></tr>
<tr><td width="290">storageDetail</td><td width="70">0..1</td><td width="150"></td><td>Details about the storage used for the import.</td></tr></tbody>
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
<tr><td width="290">indexname</td><td width="70">0..1</td><td width="150">string</td><td>Name of the database index that has been disabled.</td></tr>
<tr><td width="290">tablespace</td><td width="70">0..1</td><td width="150">string</td><td>Tablespace where the index is stored.</td></tr>
<tr><td width="290">indexdef</td><td width="70">0..1</td><td width="150">string</td><td>SQL definition of the index.</td></tr>
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td>Current status of the index (restored or disabled). 

<strong>Allowed values</strong>: restored | disabled</td></tr>
<tr><td width="290">schemaname</td><td width="70">0..1</td><td width="150">string</td><td>Name of the database schema containing the index.</td></tr>
<tr><td width="290">restore</td><td width="70">0..1</td><td width="150">BackboneElement</td><td>Information about the index restoration process.</td></tr>
<tr><td width="290">restore.<strong>start</strong></td><td width="70">0..1</td><td width="150">dateTime</td><td>Time when the index restoration started.</td></tr>
<tr><td width="290">restore.<strong>end</strong></td><td width="70">0..1</td><td width="150">dateTime</td><td>Time when the index restoration completed.</td></tr>
<tr><td width="290">restore.<strong>duration</strong></td><td width="70">0..1</td><td width="150">integer</td><td>Duration of the index restoration in milliseconds.</td></tr>
<tr><td width="290">tablename</td><td width="70">0..1</td><td width="150">string</td><td>Name of the table associated with this index.</td></tr></tbody>
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
<tr><td width="290">message</td><td width="70">0..1</td><td width="150">string</td><td>Status or error message related to the file loading process.</td></tr>
<tr><td width="290">loaded</td><td width="70">0..1</td><td width="150">integer</td><td>Number of resources successfully loaded from this file.</td></tr>
<tr><td width="290">bucket</td><td width="70">0..1</td><td width="150">string</td><td>Storage bucket where the file is located.</td></tr>
<tr><td width="290">error</td><td width="70">0..1</td><td width="150">BackboneElement</td><td>Information about errors encountered during file loading.</td></tr>
<tr><td width="290">error.<strong>source</strong></td><td width="70">0..1</td><td width="150">string</td><td>Source of the error. 

<strong>Allowed values</strong>: postgres | aws | aidbox</td></tr>
<tr><td width="290">error.<strong>code</strong></td><td width="70">0..1</td><td width="150">string</td><td>Error code or identifier.</td></tr>
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td>Current status of the file loading process. 

<strong>Allowed values</strong>: pending | in-progress | done | error | skiped</td></tr>
<tr><td width="290">start</td><td width="70">0..1</td><td width="150">dateTime</td><td>Time when the file loading process started.</td></tr>
<tr><td width="290">type</td><td width="70">0..1</td><td width="150">string</td><td>Type of the file or contained resources.</td></tr>
<tr><td width="290">file</td><td width="70">0..1</td><td width="150">string</td><td>Path or name of the file being loaded.</td></tr>
<tr><td width="290">last-modified</td><td width="70">0..1</td><td width="150">string</td><td>Last modification timestamp of the file.</td></tr>
<tr><td width="290">size</td><td width="70">0..1</td><td width="150">number</td><td>Size of the file in bytes.</td></tr>
<tr><td width="290">end</td><td width="70">0..1</td><td width="150">dateTime</td><td>Time when the file loading process completed.</td></tr></tbody>
</table>

