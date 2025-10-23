# Bulk Module Resources

Resources for configuration and management Aidbox Bulk operations.

 ## BulkExportStatus

```fhir-structure
[ {
  "path" : "error",
  "name" : "error",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "Information about errors that occurred during export."
}, {
  "path" : "error.type",
  "name" : "type",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of the resource that caused the error."
}, {
  "path" : "error.url",
  "name" : "url",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "URL with detailed error information."
}, {
  "path" : "error.count",
  "name" : "count",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Number of resources that encountered errors."
}, {
  "path" : "extension",
  "name" : "extension",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Additional information about the export."
}, {
  "path" : "output",
  "name" : "output",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "",
  "desc" : "Information about the exported data files."
}, {
  "path" : "output.type",
  "name" : "type",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "FHIR resource type for this output file."
}, {
  "path" : "output.url",
  "name" : "url",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "URL where the client can download this output file."
}, {
  "path" : "output.count",
  "name" : "count",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Number of resources in this output file."
}, {
  "path" : "params",
  "name" : "params",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Parameters specified for the bulk export request."
}, {
  "path" : "params.since",
  "name" : "since",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Export only resources modified after this date."
}, {
  "path" : "params.requester",
  "name" : "requester",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to the user or client that requested the export."
}, {
  "path" : "params.output-format",
  "name" : "output-format",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Format of the exported data files. \n\n**Allowed values**: `application/fhir+ndjson` | `application/ndjson+fhir` | `application/ndjson` | `ndjson` | `application/fhir+ndjson+gzip` | `application/fhir+gzip+ndjson` | `application/ndjson+fhir+gzip` | `application/ndjson+gzip+fhir` | `application/gzip+fhir+ndjson` | `application/gzip+ndjson+fhir`"
}, {
  "path" : "params.storage",
  "name" : "storage",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Storage configuration for exported files."
}, {
  "path" : "params.storage.type",
  "name" : "type",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of storage backend to use. \n\n**Allowed values**: `test-delay` | `test-cloud` | `aidbox` | `gcp` | `aws` | `azure`"
}, {
  "path" : "params.storage.bucket",
  "name" : "bucket",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Name of the storage bucket to use."
}, {
  "path" : "params.storage.account",
  "name" : "account",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to account with credentials for accessing the storage."
}, {
  "path" : "params.output-file-ext",
  "name" : "output-file-ext",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "File extension for the exported files. \n\n**Allowed values**: `.ndjson` | `.ndjson.gz`"
}, {
  "path" : "params.fhir?",
  "name" : "fhir?",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether to export in FHIR format."
}, {
  "path" : "params.types",
  "name" : "types",
  "lvl" : 1,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "List of resource types to include in the export."
}, {
  "path" : "params.tenant",
  "name" : "tenant",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Tenant identifier for multi-tenant environments."
}, {
  "path" : "params.patient-ids",
  "name" : "patient-ids",
  "lvl" : 1,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "List of patient IDs to export data for."
}, {
  "path" : "params.export-level",
  "name" : "export-level",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Level at which to perform the export (patient, group, or system). \n\n**Allowed values**: `patient` | `group` | `system`"
}, {
  "path" : "params.gzip?",
  "name" : "gzip?",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether to compress the exported files using gzip."
}, {
  "path" : "params.group-id",
  "name" : "group-id",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "ID of the group to export data for, when export-level is 'group'."
}, {
  "path" : "request",
  "name" : "request",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Original request that initiated this export."
}, {
  "path" : "requester",
  "name" : "requester",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to the user or client that requested the export."
}, {
  "path" : "requiresAccessToken",
  "name" : "requiresAccessToken",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether an access token is required to download the exported files."
}, {
  "path" : "status",
  "name" : "status",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Current status of the bulk export operation. \n\n**Allowed values**: `accepted` | `in-progress` | `completed` | `error` | `cancelled`"
}, {
  "path" : "transactionTime",
  "name" : "transactionTime",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Time when the bulk export was initiated."
}, {
  "path" : "url",
  "name" : "url",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "URL where the client can check the export status."
} ]
```


## BulkImportStatus

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
  "path" : "contentEncoding",
  "name" : "contentEncoding",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Encoding of the imported content (gzip or plain). \n\n**Allowed values**: `gzip` | `plain`"
}, {
  "path" : "inputFormat",
  "name" : "inputFormat",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Format of the input data files. \n\n**Allowed values**: `application/fhir+ndjson`"
}, {
  "path" : "inputs",
  "name" : "inputs",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "Information about input files processed during import."
}, {
  "path" : "inputs.time",
  "name" : "time",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Time taken to process this input file in milliseconds."
}, {
  "path" : "inputs.resourceType",
  "name" : "resourceType",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of resources contained in this input file."
}, {
  "path" : "inputs.url",
  "name" : "url",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Url of imported file."
}, {
  "path" : "inputs.ts",
  "name" : "ts",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Time when the import operation completed."
}, {
  "path" : "inputs.total",
  "name" : "total",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Total number of imported resources"
}, {
  "path" : "inputs.status",
  "name" : "status",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Import progress status"
}, {
  "path" : "inputs.duration",
  "name" : "duration",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Import duration time"
}, {
  "path" : "mode",
  "name" : "mode",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Mode of import operation (bulk or transaction). \n\n**Allowed values**: `bulk` | `transaction`"
}, {
  "path" : "source",
  "name" : "source",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Source location of the imported data."
}, {
  "path" : "status",
  "name" : "status",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Current status of the import operation. \n\n**Allowed values**: `active` | `failed` | `cancelled` | `finished`"
}, {
  "path" : "storageDetail",
  "name" : "storageDetail",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Details about the storage used for the import."
}, {
  "path" : "storageDetail.type",
  "name" : "type",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "\n\n**Allowed values**: `file` | `https`"
}, {
  "path" : "time",
  "name" : "time",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Timing information for the import operation."
}, {
  "path" : "time.end",
  "name" : "end",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Time when the import operation completed."
}, {
  "path" : "time.start",
  "name" : "start",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Time when the import operation started."
}, {
  "path" : "type",
  "name" : "type",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of data being imported (aidbox or fhir). \n\n**Allowed values**: `aidbox` | `fhir`"
}, {
  "path" : "update",
  "name" : "update",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether to update existing resources during import."
} ]
```


## DisabledIndex

```fhir-structure
[ {
  "path" : "indexdef",
  "name" : "indexdef",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "SQL definition of the index."
}, {
  "path" : "indexname",
  "name" : "indexname",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Name of the database index that has been disabled."
}, {
  "path" : "restore",
  "name" : "restore",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Information about the index restoration process."
}, {
  "path" : "restore.start",
  "name" : "start",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Time when the index restoration started."
}, {
  "path" : "restore.end",
  "name" : "end",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Time when the index restoration completed."
}, {
  "path" : "restore.duration",
  "name" : "duration",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Duration of the index restoration in milliseconds."
}, {
  "path" : "schemaname",
  "name" : "schemaname",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Name of the database schema containing the index."
}, {
  "path" : "status",
  "name" : "status",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Current status of the index (restored or disabled). \n\n**Allowed values**: `restored` | `disabled`"
}, {
  "path" : "tablename",
  "name" : "tablename",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Name of the table associated with this index."
}, {
  "path" : "tablespace",
  "name" : "tablespace",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Tablespace where the index is stored."
} ]
```


## LoaderFile

```fhir-structure
[ {
  "path" : "bucket",
  "name" : "bucket",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Storage bucket where the file is located."
}, {
  "path" : "end",
  "name" : "end",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Time when the file loading process completed."
}, {
  "path" : "error",
  "name" : "error",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Information about errors encountered during file loading."
}, {
  "path" : "error.source",
  "name" : "source",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Source of the error. \n\n**Allowed values**: `postgres` | `aws` | `aidbox`"
}, {
  "path" : "error.code",
  "name" : "code",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Error code or identifier."
}, {
  "path" : "file",
  "name" : "file",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Path or name of the file being loaded."
}, {
  "path" : "last-modified",
  "name" : "last-modified",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Last modification timestamp of the file."
}, {
  "path" : "loaded",
  "name" : "loaded",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Number of resources successfully loaded from this file."
}, {
  "path" : "message",
  "name" : "message",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Status or error message related to the file loading process."
}, {
  "path" : "size",
  "name" : "size",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "number",
  "desc" : "Size of the file in bytes."
}, {
  "path" : "start",
  "name" : "start",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Time when the file loading process started."
}, {
  "path" : "status",
  "name" : "status",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Current status of the file loading process. \n\n**Allowed values**: `pending` | `in-progress` | `done` | `error` | `skiped`"
}, {
  "path" : "type",
  "name" : "type",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of the file or contained resources."
} ]
```

