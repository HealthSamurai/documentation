# Base Module Resources

System resources that do not belong to any specific module.

 ## AidboxArchive

```fhir-structure
[ {
  "path" : "archiveFile",
  "name" : "archiveFile",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Name of the archive file."
}, {
  "path" : "archivedResourcesCount",
  "name" : "archivedResourcesCount",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "number",
  "desc" : "Count of resources that have been archived."
}, {
  "path" : "bucket",
  "name" : "bucket",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Storage bucket where archives are stored."
}, {
  "path" : "criteriaPaths",
  "name" : "criteriaPaths",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "Paths to use for filtering resources to archive."
}, {
  "path" : "history",
  "name" : "history",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether to include resource history in the archive."
}, {
  "path" : "period",
  "name" : "period",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Time period for the resources to archive."
}, {
  "path" : "period.start",
  "name" : "start",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Start date/time for the archive period."
}, {
  "path" : "period.end",
  "name" : "end",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "End date/time for the archive period."
}, {
  "path" : "serviceAccount",
  "name" : "serviceAccount",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Service account credentials for accessing storage."
}, {
  "path" : "serviceAccount.id",
  "name" : "id",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Identifier for the service account."
}, {
  "path" : "serviceAccount.resourceType",
  "name" : "resourceType",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of resource that contains service account credentials."
}, {
  "path" : "serviceAccount.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Human-readable name for the service account."
}, {
  "path" : "serviceAccount.secret-key",
  "name" : "secret-key",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Secret key for service account authentication."
}, {
  "path" : "storageBackend",
  "name" : "storageBackend",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of storage backend to use. \n\n**Allowed values**: `gcp` | `aws` | `local`"
}, {
  "path" : "targetResourceType",
  "name" : "targetResourceType",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Resource type to be archived."
} ]
```


## AidboxConfig

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
  "path" : "auth",
  "name" : "auth",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Authentication configuration settings."
}, {
  "path" : "auth.keys",
  "name" : "keys",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Cryptographic keys for authentication."
}, {
  "path" : "auth.keys.secret",
  "name" : "secret",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Secret key used for signing."
}, {
  "path" : "auth.keys.public",
  "name" : "public",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Public key used for verification."
}, {
  "path" : "box",
  "name" : "box",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Box configuration parameters."
} ]
```


## AidboxJob

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
  "path" : "action",
  "name" : "action",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Action to be performed when the job runs."
}, {
  "path" : "at",
  "name" : "at",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Time of day when the job should run (for each-day type)."
}, {
  "path" : "every",
  "name" : "every",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Frequency in seconds at which the job should run."
}, {
  "path" : "module",
  "name" : "module",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Module that this job belongs to."
}, {
  "path" : "status",
  "name" : "status",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Current status information for the job."
}, {
  "path" : "text",
  "name" : "text",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Human-readable description of the job."
}, {
  "path" : "type",
  "name" : "type",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of job scheduling pattern. \n\n**Allowed values**: `periodic` | `each-day`"
} ]
```


## AidboxJobStatus

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
  "path" : "error",
  "name" : "error",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Error information if the job failed."
}, {
  "path" : "job",
  "name" : "job",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to the job definition."
}, {
  "path" : "locked",
  "name" : "locked",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Indicates whether the job is currently locked to prevent concurrent execution."
}, {
  "path" : "module",
  "name" : "module",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Module that this job status belongs to."
}, {
  "path" : "nextStart",
  "name" : "nextStart",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Scheduled time for the next execution of the job."
}, {
  "path" : "result",
  "name" : "result",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Result data from the job execution."
}, {
  "path" : "start",
  "name" : "start",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Time when the job execution started."
}, {
  "path" : "status",
  "name" : "status",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Current status of the job execution."
}, {
  "path" : "stop",
  "name" : "stop",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Time when the job execution stopped."
}, {
  "path" : "text",
  "name" : "text",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Human-readable status information."
} ]
```


## AidboxMigration

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
  "path" : "action",
  "name" : "action",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Action to be performed for the migration."
}, {
  "path" : "dateTime",
  "name" : "dateTime",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Time when the migration was created or executed."
}, {
  "path" : "module",
  "name" : "module",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Module that this migration belongs to."
}, {
  "path" : "result",
  "name" : "result",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Result data from the migration execution."
}, {
  "path" : "status",
  "name" : "status",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Current status of the migration. \n\n**Allowed values**: `pending` | `done` | `error`"
}, {
  "path" : "text",
  "name" : "text",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Human-readable description of the migration."
} ]
```


## AidboxProfile

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
  "path" : "bind",
  "name" : "bind",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to the resource to which this profile is bound."
}, {
  "path" : "schema",
  "name" : "schema",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "Object",
  "desc" : "Schema definition for the profile."
} ]
```


## AidboxQuery

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
  "path" : "count-query",
  "name" : "count-query",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "SQL query to count total results."
}, {
  "path" : "enable-links",
  "name" : "enable-links",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether to generate FHIR links for pagination."
}, {
  "path" : "omit-sql",
  "name" : "omit-sql",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether to omit SQL in response metadata."
}, {
  "path" : "params",
  "name" : "params",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Parameters for the query."
}, {
  "path" : "query",
  "name" : "query",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "SQL query string."
}, {
  "path" : "type",
  "name" : "type",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of query operation. \n\n**Allowed values**: `query` | `execute`"
} ]
```


## AidboxSubscription

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
  "path" : "action",
  "name" : "action",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Action to be performed when the subscription is triggered."
}, {
  "path" : "module",
  "name" : "module",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Module that this subscription belongs to."
}, {
  "path" : "resources",
  "name" : "resources",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "List of resource types this subscription applies to."
}, {
  "path" : "type",
  "name" : "type",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of subscription execution model. \n\n**Allowed values**: `sync` | `async`"
} ]
```


## App

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
  "path" : "apiVersion",
  "name" : "apiVersion",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "integer",
  "desc" : "Version of the API this app uses."
}, {
  "path" : "endpoint",
  "name" : "endpoint",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Endpoint configuration for the app."
}, {
  "path" : "endpoint.secret",
  "name" : "secret",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Secret key for endpoint authentication."
}, {
  "path" : "endpoint.url",
  "name" : "url",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "URL of the endpoint."
}, {
  "path" : "endpoint.type",
  "name" : "type",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "code",
  "desc" : "Type of endpoint protocol. \n\n**Allowed values**: `http-rpc` | `ws-rpc` | `native`"
}, {
  "path" : "entities",
  "name" : "entities",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Entities defined by the app."
}, {
  "path" : "hooks",
  "name" : "hooks",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Hooks configuration for the app."
}, {
  "path" : "migrations",
  "name" : "migrations",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "List of migrations for this app."
}, {
  "path" : "migrations.id",
  "name" : "id",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Unique identifier for the migration."
}, {
  "path" : "migrations.dateTime",
  "name" : "dateTime",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Timestamp for the migration."
}, {
  "path" : "migrations.action",
  "name" : "action",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Action to be performed for the migration."
}, {
  "path" : "operations",
  "name" : "operations",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Operations defined by the app."
}, {
  "path" : "subscriptions",
  "name" : "subscriptions",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Subscriptions configuration for the app."
}, {
  "path" : "type",
  "name" : "type",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "code",
  "desc" : "Type of application. \n\n**Allowed values**: `app` | `addon`"
} ]
```


## Attribute

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
  "path" : "description",
  "name" : "description",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Human-readable description of the attribute."
}, {
  "path" : "enum",
  "name" : "enum",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "Enumeration of allowed values for this attribute."
}, {
  "path" : "extensionUrl",
  "name" : "extensionUrl",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "URL for the extension this attribute represents."
}, {
  "path" : "isCollection",
  "name" : "isCollection",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether this attribute is a collection."
}, {
  "path" : "isModifier",
  "name" : "isModifier",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether this attribute changes the meaning of the resource."
}, {
  "path" : "isOpen",
  "name" : "isOpen",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether this attribute allows additional properties."
}, {
  "path" : "isRequired",
  "name" : "isRequired",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether this attribute is required."
}, {
  "path" : "isSummary",
  "name" : "isSummary",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether this attribute is included in summary views."
}, {
  "path" : "isUnique",
  "name" : "isUnique",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether this attribute has unique values."
}, {
  "path" : "module",
  "name" : "module",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Module that this attribute belongs to."
}, {
  "path" : "order",
  "name" : "order",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Order for display or processing."
}, {
  "path" : "path",
  "name" : "path",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "Path to the attribute within the resource."
}, {
  "path" : "refers",
  "name" : "refers",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "Resource types this attribute can reference."
}, {
  "path" : "resource",
  "name" : "resource",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to the resource type this attribute belongs to."
}, {
  "path" : "schema",
  "name" : "schema",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Schema for the attribute."
}, {
  "path" : "text",
  "name" : "text",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Human-readable text about the attribute."
}, {
  "path" : "type",
  "name" : "type",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to the attribute's data type."
}, {
  "path" : "union",
  "name" : "union",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "Reference",
  "desc" : "References to other attributes in a union type."
}, {
  "path" : "valueSet",
  "name" : "valueSet",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Value set constraint for this attribute."
}, {
  "path" : "valueSet.resourceType",
  "name" : "resourceType",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of the value set resource."
}, {
  "path" : "valueSet.id",
  "name" : "id",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Identifier of the value set."
}, {
  "path" : "valueSet.uri",
  "name" : "uri",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "URI of the value set."
} ]
```


## BatchValidationError

```fhir-structure
[ {
  "path" : "errors",
  "name" : "errors",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "Object",
  "desc" : "List of validation errors found."
}, {
  "path" : "profiles",
  "name" : "profiles",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "Profiles against which the resource was validated."
}, {
  "path" : "resource",
  "name" : "resource",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Reference to the resource that failed validation."
}, {
  "path" : "resource.id",
  "name" : "id",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Resource identifier."
}, {
  "path" : "resource.resourceType",
  "name" : "resourceType",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of resource."
}, {
  "path" : "resource.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Human-readable display name for the resource."
}, {
  "path" : "run",
  "name" : "run",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Reference to the validation run that produced this error."
}, {
  "path" : "run.id",
  "name" : "id",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Validation run identifier."
}, {
  "path" : "run.resourceType",
  "name" : "resourceType",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of the validation run resource."
}, {
  "path" : "run.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Human-readable display name for the validation run."
} ]
```


## BatchValidationRun

```fhir-structure
[ {
  "path" : "async",
  "name" : "async",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether validation is performed asynchronously."
}, {
  "path" : "duration",
  "name" : "duration",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Duration of the validation run in milliseconds."
}, {
  "path" : "errorsThreshold",
  "name" : "errorsThreshold",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Maximum number of errors before stopping validation."
}, {
  "path" : "filter",
  "name" : "filter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Expression to filter resources for validation."
}, {
  "path" : "invalid",
  "name" : "invalid",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Count of invalid resources found."
}, {
  "path" : "limit",
  "name" : "limit",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Maximum number of resources to validate."
}, {
  "path" : "profiles",
  "name" : "profiles",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "FHIR profiles to validate against."
}, {
  "path" : "resource",
  "name" : "resource",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Resource type to validate."
}, {
  "path" : "schemas",
  "name" : "schemas",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "List of schemas to validate against."
}, {
  "path" : "status",
  "name" : "status",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Current status of the validation run. \n\n**Allowed values**: `in-progress` | `complete`"
}, {
  "path" : "valid",
  "name" : "valid",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Count of valid resources found."
} ]
```


## Concept

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
  "path" : "ancestors",
  "name" : "ancestors",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "List of ancestor concepts in the hierarchy."
}, {
  "path" : "code",
  "name" : "code",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Symbol or identifier for the concept within the system."
}, {
  "path" : "definition",
  "name" : "definition",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Formal definition of the concept."
}, {
  "path" : "deprecated",
  "name" : "deprecated",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Indicates whether the concept is deprecated."
}, {
  "path" : "designation",
  "name" : "designation",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Additional representations for the concept."
}, {
  "path" : "designation.definition",
  "name" : "definition",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Additional definitions for the concept."
}, {
  "path" : "designation.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Display names in different languages or contexts."
}, {
  "path" : "display",
  "name" : "display",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Human-readable representation of the concept."
}, {
  "path" : "hierarchy",
  "name" : "hierarchy",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "Hierarchies this concept belongs to."
}, {
  "path" : "property",
  "name" : "property",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Additional properties associated with the concept."
}, {
  "path" : "system",
  "name" : "system",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Code system that defines the concept."
}, {
  "path" : "valueset",
  "name" : "valueset",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "Value sets that include this concept."
} ]
```


## ConceptMapRule

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
  "path" : "conceptmapId",
  "name" : "conceptmapId",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "ID of the parent concept map."
}, {
  "path" : "conceptmapUrl",
  "name" : "conceptmapUrl",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "URL of the parent concept map."
}, {
  "path" : "element",
  "name" : "element",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Element mapping definition."
}, {
  "path" : "element.target",
  "name" : "target",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Target mapping information."
}, {
  "path" : "element.target.comment",
  "name" : "comment",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Comment about the mapping."
}, {
  "path" : "element.target.equivalence",
  "name" : "equivalence",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Equivalence relationship between source and target."
}, {
  "path" : "source",
  "name" : "source",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Source system for the mapping."
}, {
  "path" : "sourceValueSet",
  "name" : "sourceValueSet",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Source value set for the mapping."
}, {
  "path" : "target",
  "name" : "target",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Target system for the mapping."
}, {
  "path" : "targetValueSet",
  "name" : "targetValueSet",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Target value set for the mapping."
}, {
  "path" : "unmapped",
  "name" : "unmapped",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Rules for handling unmapped concepts."
}, {
  "path" : "unmapped.url",
  "name" : "url",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "URL for unmapped value set."
}, {
  "path" : "unmapped.mode",
  "name" : "mode",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Mode for handling unmapped concepts."
} ]
```


## Entity

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
  "path" : "description",
  "name" : "description",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Human-readable description of the entity."
}, {
  "path" : "history",
  "name" : "history",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "History tracking mode for this entity. \n\n**Allowed values**: `none` | `diff`"
}, {
  "path" : "idGeneration",
  "name" : "idGeneration",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Strategy for generating IDs for this entity. \n\n**Allowed values**: `sequence` | `uuid`"
}, {
  "path" : "isMeta",
  "name" : "isMeta",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether this entity is a metadata entity."
}, {
  "path" : "isOpen",
  "name" : "isOpen",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether this entity allows additional properties."
}, {
  "path" : "module",
  "name" : "module",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Module that this entity belongs to."
}, {
  "path" : "nonPersistable",
  "name" : "nonPersistable",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether this entity should not be persisted."
}, {
  "path" : "schema",
  "name" : "schema",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Schema definition for the entity."
}, {
  "path" : "sequencePrefix",
  "name" : "sequencePrefix",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Prefix for sequence-generated IDs."
}, {
  "path" : "text",
  "name" : "text",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Human-readable text about the entity."
}, {
  "path" : "type",
  "name" : "type",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of entity definition. \n\n**Allowed values**: `abstract` | `resource` | `type` | `primitive`"
} ]
```


## FlatImportStatus

```fhir-structure
[ {
  "path" : "error",
  "name" : "error",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Error information when import fails."
}, {
  "path" : "error.code",
  "name" : "code",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Error code."
}, {
  "path" : "error.message",
  "name" : "message",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Error message description."
}, {
  "path" : "params",
  "name" : "params",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Parameters used for the import."
}, {
  "path" : "result",
  "name" : "result",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Result information for successful import."
}, {
  "path" : "result.count",
  "name" : "count",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Count of successfully imported resources."
}, {
  "path" : "status",
  "name" : "status",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Current status of the import process. \n\n**Allowed values**: `in-progress` | `done` | `fail`"
} ]
```


## FtrConfig

```fhir-structure
[ {
  "path" : "module",
  "name" : "module",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Module that this configuration belongs to."
}, {
  "path" : "package-name",
  "name" : "package-name",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Name of the FHIR terminology package."
}, {
  "path" : "tag",
  "name" : "tag",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Tag for terminology resources."
}, {
  "path" : "tag-index",
  "name" : "tag-index",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Index of tags and their associations."
} ]
```


## IndexCreationJob

```fhir-structure
[ {
  "path" : "end",
  "name" : "end",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Time when the index creation finished."
}, {
  "path" : "index",
  "name" : "index",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Name of the database index being created."
}, {
  "path" : "message",
  "name" : "message",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Status or error message."
}, {
  "path" : "params",
  "name" : "params",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "Parameters for the index creation."
}, {
  "path" : "procstatus",
  "name" : "procstatus",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Current status of the index creation process. \n\n**Allowed values**: `pending` | `in-progress` | `done` | `error`"
}, {
  "path" : "resource",
  "name" : "resource",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Resource type for which the index is being created."
}, {
  "path" : "start",
  "name" : "start",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Time when the index creation started."
} ]
```


## Lambda

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
  "path" : "code",
  "name" : "code",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Code to be executed by the lambda."
}, {
  "path" : "hook",
  "name" : "hook",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "code",
  "desc" : "Type of hook this lambda responds to. \n\n**Allowed values**: `audit`"
} ]
```


## Mapping

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
  "path" : "body",
  "name" : "body",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "Object",
  "desc" : "Mapping transformation definition."
}, {
  "path" : "params",
  "name" : "params",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Parameters for the mapping execution."
}, {
  "path" : "params.omit-drop-blanks",
  "name" : "omit-drop-blanks",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether to omit blank values from the result."
}, {
  "path" : "returns",
  "name" : "returns",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "code",
  "desc" : "Type of result returned by the mapping. \n\n**Allowed values**: `transaction` | `resource`"
}, {
  "path" : "scopeSchema",
  "name" : "scopeSchema",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Schema defining the scope for mapping."
}, {
  "path" : "text",
  "name" : "text",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Human-readable text about the mapping."
}, {
  "path" : "text.status",
  "name" : "status",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Status of the text."
}, {
  "path" : "text.div",
  "name" : "div",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "HTML representation of the text."
}, {
  "path" : "type",
  "name" : "type",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of the mapping."
} ]
```


## Module

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
  "path" : "meta",
  "name" : "meta",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Meta",
  "desc" : "Metadata for the module."
}, {
  "path" : "meta.pre-sql",
  "name" : "pre-sql",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "SQL to execute before module installation."
}, {
  "path" : "meta.post-sql",
  "name" : "post-sql",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "SQL to execute after module installation."
}, {
  "path" : "module",
  "name" : "module",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Identifier for the module."
}, {
  "path" : "version",
  "name" : "version",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Version number of the module."
} ]
```


## Notebook

```fhir-structure
[ {
  "path" : "cells",
  "name" : "cells",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "",
  "desc" : "Cells contained in the notebook."
}, {
  "path" : "cells.id",
  "name" : "id",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Unique identifier for the cell."
}, {
  "path" : "cells.evaluating?",
  "name" : "evaluating?",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether the cell is currently being evaluated."
}, {
  "path" : "cells.folded",
  "name" : "folded",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Folding state of the cell."
}, {
  "path" : "cells.folded.code",
  "name" : "code",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether the code section is folded."
}, {
  "path" : "cells.folded.result",
  "name" : "result",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether the result section is folded."
}, {
  "path" : "cells.type",
  "name" : "type",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of cell content. \n\n**Allowed values**: `rpc` | `rest` | `empty` | `markdown` | `sql`"
}, {
  "path" : "cells.value",
  "name" : "value",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Content value of the cell."
}, {
  "path" : "cells.result",
  "name" : "result",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Result of cell evaluation."
}, {
  "path" : "cells.error",
  "name" : "error",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Error information if evaluation failed."
}, {
  "path" : "cells.nb-title",
  "name" : "nb-title",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Title for the cell."
}, {
  "path" : "description",
  "name" : "description",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Description of the notebook."
}, {
  "path" : "edit-secret",
  "name" : "edit-secret",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Secret for edit access to the notebook."
}, {
  "path" : "name",
  "name" : "name",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Name of the notebook."
}, {
  "path" : "notebook-superuser-secret",
  "name" : "notebook-superuser-secret",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Secret for superuser access to the notebook."
}, {
  "path" : "origin",
  "name" : "origin",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Origin information for the notebook."
}, {
  "path" : "publication-id",
  "name" : "publication-id",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Identifier for the published version of the notebook."
}, {
  "path" : "source",
  "name" : "source",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Source content for the notebook."
}, {
  "path" : "tags",
  "name" : "tags",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "Tags associated with the notebook."
} ]
```


## Operation

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
  "path" : "action",
  "name" : "action",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Action to be performed by the operation."
}, {
  "path" : "app",
  "name" : "app",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to associated App. \n\n**Allowed references**: App"
}, {
  "path" : "data",
  "name" : "data",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Additional operation data."
}, {
  "path" : "description",
  "name" : "description",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Human-readable description of the operation."
}, {
  "path" : "fhirCode",
  "name" : "fhirCode",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "FHIR operation code."
}, {
  "path" : "fhirResource",
  "name" : "fhirResource",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "FHIR resources this operation applies to."
}, {
  "path" : "fhirUrl",
  "name" : "fhirUrl",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "FHIR URL pattern for the operation."
}, {
  "path" : "implicit-params",
  "name" : "implicit-params",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Parameters that are implicitly included in the operation."
}, {
  "path" : "implicit-params.path",
  "name" : "path",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Path parameters."
}, {
  "path" : "implicit-params.query",
  "name" : "query",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Query parameters."
}, {
  "path" : "module",
  "name" : "module",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Module that this operation belongs to."
}, {
  "path" : "no-op-logs",
  "name" : "no-op-logs",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether to disable operation logging."
}, {
  "path" : "public",
  "name" : "public",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether the operation is publicly accessible."
}, {
  "path" : "request",
  "name" : "request",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "Object",
  "desc" : "Request configurations."
}, {
  "path" : "route-params",
  "name" : "route-params",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Parameters for route matching."
}, {
  "path" : "transform",
  "name" : "transform",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Transformation configuration for the operation."
}, {
  "path" : "transform.request",
  "name" : "request",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Request transformation settings."
}, {
  "path" : "transform.request.engine",
  "name" : "engine",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "code",
  "desc" : "Transformation engine to use."
}, {
  "path" : "transform.request.template",
  "name" : "template",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to a template for transformation."
}, {
  "path" : "transform.request.part",
  "name" : "part",
  "lvl" : 2,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Part of the request to transform. \n\n**Allowed values**: `body`"
} ]
```


## PGSequence

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
  "path" : "cycle",
  "name" : "cycle",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether the sequence should cycle when reaching max/min value."
}, {
  "path" : "data_type",
  "name" : "data_type",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "PostgreSQL data type for the sequence. \n\n**Allowed values**: `smallint` | `integer` | `bigint`"
}, {
  "path" : "increment",
  "name" : "increment",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Value to increment by for each sequence call."
}, {
  "path" : "maxvalue",
  "name" : "maxvalue",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Maximum value for the sequence."
}, {
  "path" : "minvalue",
  "name" : "minvalue",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Minimum value for the sequence."
}, {
  "path" : "start",
  "name" : "start",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Starting value for the sequence."
} ]
```


## Search

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
  "path" : "format",
  "name" : "format",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Replaces `?` with the actual value provided in the search query. Useful to use in ILIKE SQL expression."
}, {
  "path" : "module",
  "name" : "module",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Module that this search belongs to."
}, {
  "path" : "multi",
  "name" : "multi",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "If you set multi = 'array', parameters will be coerced as PostgreSQL array. \n\n**Allowed values**: `array`"
}, {
  "path" : "name",
  "name" : "name",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Name of the search parameter."
}, {
  "path" : "order-by",
  "name" : "order-by",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "SQL to use in the ORDER BY expression. Supports {{table}} and {{param}}. Note that it is used only when _sort=<name> present in the query."
}, {
  "path" : "param-parser",
  "name" : "param-parser",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Parse value as string, identifier, or reference. See below. \n\n**Allowed values**: `token` | `reference`"
}, {
  "path" : "resource",
  "name" : "resource",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to the resource type this search applies to. ResourceType is always Entity"
}, {
  "path" : "token-sql",
  "name" : "token-sql",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "SQL templates for token parameter handling."
}, {
  "path" : "token-sql.text",
  "name" : "text",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "SQL template for text search."
}, {
  "path" : "token-sql.both",
  "name" : "both",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "SQL template when both system and code are provided."
}, {
  "path" : "token-sql.only-system",
  "name" : "only-system",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "SQL template when only system is provided."
}, {
  "path" : "token-sql.only-code",
  "name" : "only-code",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "SQL template when only code is provided."
}, {
  "path" : "token-sql.text-format",
  "name" : "text-format",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Format for text search."
}, {
  "path" : "token-sql.no-system",
  "name" : "no-system",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "SQL template when no system is provided."
}, {
  "path" : "where",
  "name" : "where",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "SQL to use in the WHERE expression. Supports `{{table}}` and `{{param}}`."
} ]
```


## SearchQuery

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
  "path" : "as",
  "name" : "as",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Alias for the resource in the query."
}, {
  "path" : "includes",
  "name" : "includes",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Resources to include with the results."
}, {
  "path" : "limit",
  "name" : "limit",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Maximum number of results to return."
}, {
  "path" : "params",
  "name" : "params",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Search parameters for the query."
}, {
  "path" : "query",
  "name" : "query",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Detailed query configuration."
}, {
  "path" : "query.order-by",
  "name" : "order-by",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Column or expression to order results by."
}, {
  "path" : "query.join",
  "name" : "join",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Join conditions for the query."
}, {
  "path" : "query.where",
  "name" : "where",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Where clause for the query."
}, {
  "path" : "resource",
  "name" : "resource",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to the resource type to search."
}, {
  "path" : "total",
  "name" : "total",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether to include total count in results."
} ]
```


## SeedImport

```fhir-structure
[ {
  "path" : "filename",
  "name" : "filename",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Name of the seed import file."
}, {
  "path" : "md5-hash",
  "name" : "md5-hash",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "MD5 hash of the import file for integrity verification."
}, {
  "path" : "resources",
  "name" : "resources",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "",
  "desc" : "Resources to be imported."
}, {
  "path" : "resources.resourceType",
  "name" : "resourceType",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of resource."
}, {
  "path" : "resources.id",
  "name" : "id",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Identifier for the resource."
}, {
  "path" : "resources.meta",
  "name" : "meta",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Metadata for the resource."
}, {
  "path" : "version",
  "name" : "version",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Version of the seed import format. \n\n**Allowed values**: `v2`"
} ]
```


## SubsNotification

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
  "path" : "duration",
  "name" : "duration",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Duration of the notification processing in milliseconds."
}, {
  "path" : "notification",
  "name" : "notification",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Content of the notification that was sent."
}, {
  "path" : "response",
  "name" : "response",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Response received from the notification endpoint."
}, {
  "path" : "retried",
  "name" : "retried",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether this notification is a retry."
}, {
  "path" : "retries",
  "name" : "retries",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "Reference",
  "desc" : "References to retry notifications. \n\n**Allowed references**: SubsSubscription"
}, {
  "path" : "retryOf",
  "name" : "retryOf",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to the original notification this is retrying. \n\n**Allowed references**: SubsSubscription"
}, {
  "path" : "status",
  "name" : "status",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "code",
  "desc" : "Status of the notification delivery. \n\n**Allowed values**: `success` | `failed`"
}, {
  "path" : "subscription",
  "name" : "subscription",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to the subscription that generated this notification. \n\n**Allowed references**: SubsSubscription"
} ]
```


## SubsSubscription

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
  "path" : "channel",
  "name" : "channel",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : "Channel configuration for delivering notifications."
}, {
  "path" : "channel.timeout",
  "name" : "timeout",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Timeout in milliseconds for notification delivery."
}, {
  "path" : "channel.heartbeatPeriod",
  "name" : "heartbeatPeriod",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Period in milliseconds for sending heartbeat notifications."
}, {
  "path" : "channel.headers",
  "name" : "headers",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "HTTP headers to include with notifications."
}, {
  "path" : "channel.type",
  "name" : "type",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "code",
  "desc" : "Type of channel for notifications. \n\n**Allowed values**: `rest-hook`"
}, {
  "path" : "channel.endpoint",
  "name" : "endpoint",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "url",
  "desc" : "URL endpoint where notifications are sent."
}, {
  "path" : "channel.payload",
  "name" : "payload",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Content to be sent in the notification."
}, {
  "path" : "channel.payload.content",
  "name" : "content",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "code",
  "desc" : "\n\n**Allowed values**: `id-only` | `full-resource`"
}, {
  "path" : "channel.payload.contentType",
  "name" : "contentType",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "code",
  "desc" : "\n\n**Allowed values**: `json` | `fhir+json`"
}, {
  "path" : "channel.payload.context",
  "name" : "context",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "identifier",
  "name" : "identifier",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "Identifier",
  "desc" : "Business identifiers for the subscription."
}, {
  "path" : "status",
  "name" : "status",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "code",
  "desc" : "Current status of the subscription. \n\n**Allowed values**: `active` | `off`"
}, {
  "path" : "trigger",
  "name" : "trigger",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Events that trigger this subscription."
} ]
```


## TerminologyBundleFile

```fhir-structure
[ {
  "path" : "filename",
  "name" : "filename",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Name of the terminology bundle file."
}, {
  "path" : "status",
  "name" : "status",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Current status of the bundle file processing. \n\n**Allowed values**: `pending` | `in-progress` | `fail` | `success`"
} ]
```


## ui_history

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
  "path" : "command",
  "name" : "command",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Command that was executed."
}, {
  "path" : "type",
  "name" : "type",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of history entry. \n\n**Allowed values**: `http` | `sql`"
}, {
  "path" : "user",
  "name" : "user",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to the user who performed the action. \n\n**Allowed references**: User"
} ]
```


## ui_snippet

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
  "path" : "collection",
  "name" : "collection",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "command",
  "name" : "command",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "title",
  "name" : "title",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "type",
  "name" : "type",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "\n\n**Allowed values**: `http` | `sql`"
}, {
  "path" : "user",
  "name" : "user",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "\n\n**Allowed references**: User"
} ]
```

