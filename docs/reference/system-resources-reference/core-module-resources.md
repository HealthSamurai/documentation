---
description: Aidbox core system resources.
---

# Core Module Resources

Core system resources.

 ## AccessPolicy

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
  "path" : "and",
  "name" : "and",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "Object",
  "desc" : "A list of conditions that must all be satisfied for the policy to grant access."
}, {
  "path" : "clj",
  "name" : "clj",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Clojure code that defines access policy rules. DEPRECATED. DO NOT USE IT."
}, {
  "path" : "description",
  "name" : "description",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A textual description of the access policy."
}, {
  "path" : "engine",
  "name" : "engine",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Specifies the evaluation engine used for the policy. \n\n**Allowed values**: `json-schema` | `allow` | `sql` | `complex` | `matcho` | `clj` | `matcho-rpc` | `allow-rpc` | `signed-rpc` | `smart-on-fhir`"
}, {
  "path" : "link",
  "name" : "link",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "Reference",
  "desc" : "References to resources associated with this policy. \n\n**Allowed references**: Client, User, Operation"
}, {
  "path" : "matcho",
  "name" : "matcho",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Defines rules using the Matcho pattern-matching syntax."
}, {
  "path" : "module",
  "name" : "module",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Module that this policy belongs to."
}, {
  "path" : "or",
  "name" : "or",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "Object",
  "desc" : "A list of conditions where at least one must be satisfied for the policy to grant access."
}, {
  "path" : "roleName",
  "name" : "roleName",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Symbolic link to Role by name."
}, {
  "path" : "rpc",
  "name" : "rpc",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Defines rules for Remote Procedure Calls (RPCs)."
}, {
  "path" : "schema",
  "name" : "schema",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "JSON Schema used to validate requests against the policy."
}, {
  "path" : "sql",
  "name" : "sql",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "SQL-based policy definition."
}, {
  "path" : "sql.query",
  "name" : "query",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "SQL query used to evaluate access conditions."
}, {
  "path" : "type",
  "name" : "type",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The type or category of the access policy. \n\n**Allowed values**: `scope` | `rest` | `rpc`"
} ]
```


## AidboxArchive

Archive configuration resource for Aidbox data archival.

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
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Storage bucket where archives are stored."
}, {
  "path" : "criteriaPaths",
  "name" : "criteriaPaths",
  "lvl" : 0,
  "min" : 1,
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
  "min" : 1,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Time period for the resources to archive."
}, {
  "path" : "period.end",
  "name" : "end",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "End date/time for the archive period."
}, {
  "path" : "period.start",
  "name" : "start",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Start date/time for the archive period."
}, {
  "path" : "serviceAccount",
  "name" : "serviceAccount",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Service account credentials for accessing storage."
}, {
  "path" : "serviceAccount.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Human-readable name for the service account."
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
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of storage backend to use. \n\n**Allowed values**: `gcp` | `aws` | `local`"
}, {
  "path" : "targetResourceType",
  "name" : "targetResourceType",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Resource type to be archived."
} ]
```


## AidboxConfig

Configuration resource for Aidbox settings.

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
  "path" : "auth.keys.public",
  "name" : "public",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Public key used for verification."
}, {
  "path" : "auth.keys.secret",
  "name" : "secret",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Secret key used for signing."
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

Aidbox Job resource for scheduling and executing periodic tasks.

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

Status tracking for Aidbox job executions.

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

Database migration tracking resource for Aidbox.

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
  "desc" : "Action to be performed for the migration. \n\n**Allowed values**: `far-migration-fhir-package-install` | `far-migration-fhir-package-uninstall` | `aidbox-migration-run-sql`"
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
  "path" : "params",
  "name" : "params",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Parameters for the migration."
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
  "desc" : "Current status of the migration. \n\n**Allowed values**: `pending` | `done` | `error` | `to-run`"
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

Profile resource for Aidbox schema validation. Deprecated after 2507 LTS.

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

Custom SQL query resource for Aidbox.

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

Aidbox subscription resource for event-driven workflows.

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


## AidboxSubscriptionStatus

The status of an AidboxTopicDestination during notifications.

```fhir-structure
[ {
  "path" : "error",
  "name" : "error",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "CodeableConcept",
  "desc" : "List of errors on the subscription."
}, {
  "path" : "eventsSinceSubscriptionStart",
  "name" : "eventsSinceSubscriptionStart",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "decimal",
  "desc" : "Events since the AidboxTopicDestination was created."
}, {
  "path" : "notificationEvent",
  "name" : "notificationEvent",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "Detailed information about any events relevant to this notification."
}, {
  "path" : "notificationEvent.additionalContext",
  "name" : "additionalContext",
  "lvl" : 1,
  "min" : 0,
  "max" : "*",
  "type" : "Reference",
  "desc" : "References related to the focus resource and/or context of this event. \n\n**Allowed references**: Resource"
}, {
  "path" : "notificationEvent.eventNumber",
  "name" : "eventNumber",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "decimal",
  "desc" : "Sequencing index of this event."
}, {
  "path" : "notificationEvent.focus",
  "name" : "focus",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to the primary resource or information of this event. \n\n**Allowed references**: Resource"
}, {
  "path" : "notificationEvent.timestamp",
  "name" : "timestamp",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "instant",
  "desc" : "The instant this event occurred."
}, {
  "path" : "status",
  "name" : "status",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "code",
  "desc" : "requested | active | error | off | entered-in-error. \n\n**Allowed values**: `requested` | `active` | `error` | `off` | `entered-in-error`"
}, {
  "path" : "topic",
  "name" : "topic",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "canonical",
  "desc" : "Reference to the AidboxSubscriptionTopic this notification relates to."
}, {
  "path" : "topic-destination",
  "name" : "topic-destination",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to the AidboxTopicDestination responsible for this notification. \n\n**Allowed references**: AidboxTopicDestination"
}, {
  "path" : "type",
  "name" : "type",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "handshake | heartbeat | event-notification | query-status | query-event. \n\n**Allowed values**: `handshake` | `heartbeat` | `event-notification` | `query-status` | `query-event`"
} ]
```


## AidboxSubscriptionTopic

Defines the data sources and events that clients can subscribe to. Acts as a configuration that establishes what events matter by specifying which resources and conditions warrant notifications.

```fhir-structure
[ {
  "path" : "approvalDate",
  "name" : "approvalDate",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "date",
  "desc" : "When AidboxSubscriptionTopic is/was approved by publisher."
}, {
  "path" : "contact",
  "name" : "contact",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "ContactDetail",
  "desc" : "Contact details for the publisher."
}, {
  "path" : "copyright",
  "name" : "copyright",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "markdown",
  "desc" : "Use and/or publishing restrictions."
}, {
  "path" : "copyrightLabel",
  "name" : "copyrightLabel",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Copyright holder and year(s)."
}, {
  "path" : "date",
  "name" : "date",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Date status first applied."
}, {
  "path" : "derivedFrom",
  "name" : "derivedFrom",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "canonical",
  "desc" : "Based on FHIR protocol or definition."
}, {
  "path" : "description",
  "name" : "description",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "markdown",
  "desc" : "Natural language description of the AidboxSubscriptionTopic."
}, {
  "path" : "effectivePeriod",
  "name" : "effectivePeriod",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Period",
  "desc" : "The effective date range for the AidboxSubscriptionTopic."
}, {
  "path" : "experimental",
  "name" : "experimental",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "If for testing purposes, not real usage."
}, {
  "path" : "identifier",
  "name" : "identifier",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "Identifier",
  "desc" : "Business identifier for subscription topic."
}, {
  "path" : "jurisdiction",
  "name" : "jurisdiction",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "CodeableConcept",
  "desc" : "Intended jurisdiction of the AidboxSubscriptionTopic (if applicable)."
}, {
  "path" : "lastReviewDate",
  "name" : "lastReviewDate",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "date",
  "desc" : "Date the AidboxSubscriptionTopic was last reviewed by the publisher."
}, {
  "path" : "name",
  "name" : "name",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Name for this subscription topic (computer friendly)."
}, {
  "path" : "publisher",
  "name" : "publisher",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The name of the individual or organization that published the AidboxSubscriptionTopic."
}, {
  "path" : "purpose",
  "name" : "purpose",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "markdown",
  "desc" : "Why this AidboxSubscriptionTopic is defined."
}, {
  "path" : "status",
  "name" : "status",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "code",
  "desc" : "draft | active | retired | unknown. \n\n**Allowed values**: `draft` | `active` | `retired` | `unknown`"
}, {
  "path" : "title",
  "name" : "title",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Name for this subscription topic (human friendly)."
}, {
  "path" : "trigger",
  "name" : "trigger",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "Definition of a resource-based trigger for the subscription topic."
}, {
  "path" : "trigger.canFilterBy",
  "name" : "canFilterBy",
  "lvl" : 1,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "Properties by which a AidboxTopicDestination can filter notifications from the AidboxSubscriptionTopic."
}, {
  "path" : "trigger.canFilterBy.comparator",
  "name" : "comparator",
  "lvl" : 2,
  "min" : 1,
  "max" : "*",
  "type" : "code",
  "desc" : "eq | ne | gt | lt | ge | le | sa | eb | ap."
}, {
  "path" : "trigger.canFilterBy.description",
  "name" : "description",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "markdown",
  "desc" : "Description of this filter parameter."
}, {
  "path" : "trigger.canFilterBy.filterDefinitionFhirPathExpression",
  "name" : "filterDefinitionFhirPathExpression",
  "lvl" : 2,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "trigger.canFilterBy.filterParameter",
  "name" : "filterParameter",
  "lvl" : 2,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Human-readable and computation-friendly name for a filter parameter usable by subscriptions on this topic."
}, {
  "path" : "trigger.description",
  "name" : "description",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "markdown",
  "desc" : "Text representation of the resource trigger."
}, {
  "path" : "trigger.fhirPathCriteria",
  "name" : "fhirPathCriteria",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "FHIRPath based trigger rule."
}, {
  "path" : "trigger.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "uri",
  "desc" : "Data Type or Resource (reference to definition) for this trigger definition."
}, {
  "path" : "trigger.supportedInteraction",
  "name" : "supportedInteraction",
  "lvl" : 1,
  "min" : 0,
  "max" : "*",
  "type" : "code",
  "desc" : "create | update | delete. \n\n**Allowed values**: `create` | `update` | `delete`"
}, {
  "path" : "url",
  "name" : "url",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "uri",
  "desc" : "Canonical identifier for this subscription topic, represented as an absolute URI (globally unique)."
}, {
  "path" : "useContext",
  "name" : "useContext",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "UsageContext",
  "desc" : "Content intends to support these contexts."
}, {
  "path" : "version",
  "name" : "version",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Business version of the subscription topic."
}, {
  "path" : "versionAlgorithm[x]",
  "name" : "versionAlgorithm[x]",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "How to compare versions."
} ]
```


## AidboxTask

```fhir-structure
[ {
  "path" : "allowedRetryCount",
  "name" : "allowedRetryCount",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Maximum number of retries allowed for this task."
}, {
  "path" : "concurrencyLimit",
  "name" : "concurrencyLimit",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "decimal",
  "desc" : "Maximum number of concurrent tasks allowed."
}, {
  "path" : "concurrencyPath",
  "name" : "concurrencyPath",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "",
  "desc" : ""
}, {
  "path" : "definition",
  "name" : "definition",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Identifier for the task definition."
}, {
  "path" : "error",
  "name" : "error",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Error details if task failed."
}, {
  "path" : "execId",
  "name" : "execId",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Unique execution identifier."
}, {
  "path" : "executeAt",
  "name" : "executeAt",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Scheduled time for task execution."
}, {
  "path" : "inProgressTimeout",
  "name" : "inProgressTimeout",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "decimal",
  "desc" : "Maximum duration in seconds that a task can remain in in-progress status before timing out."
}, {
  "path" : "label",
  "name" : "label",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Label for the task."
}, {
  "path" : "outcome",
  "name" : "outcome",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Final outcome of the task execution. \n\n**Allowed values**: `succeeded` | `failed` | `canceled`"
}, {
  "path" : "outcomeReason",
  "name" : "outcomeReason",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Detailed reason for the task outcome."
}, {
  "path" : "outcomeReason.data",
  "name" : "data",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Additional data related to the outcome."
}, {
  "path" : "outcomeReason.message",
  "name" : "message",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Human-readable explanation of the outcome."
}, {
  "path" : "outcomeReason.type",
  "name" : "type",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of outcome reason. \n\n**Allowed values**: `awf.task/failed-due-to-in-progress-timeout` | `awf.task/failed-by-executor` | `awf.executor/unknown-error`"
}, {
  "path" : "params",
  "name" : "params",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : "Parameters required for task execution."
}, {
  "path" : "requestedToStartTimeout",
  "name" : "requestedToStartTimeout",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "decimal",
  "desc" : "Maximum duration in seconds that a task can remain in requested status before timing out."
}, {
  "path" : "requester",
  "name" : "requester",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Entity that requested the task."
}, {
  "path" : "requester.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Human-readable display name of the requester."
}, {
  "path" : "requester.id",
  "name" : "id",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Identifier of the requester."
}, {
  "path" : "requester.resourceType",
  "name" : "resourceType",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of resource that made the request."
}, {
  "path" : "requester.rule",
  "name" : "rule",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Rule that authorized the request."
}, {
  "path" : "requester.service",
  "name" : "service",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Service that initiated the request."
}, {
  "path" : "result",
  "name" : "result",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Result data produced by successful task execution."
}, {
  "path" : "retryCount",
  "name" : "retryCount",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "decimal",
  "desc" : "Number of times the task has been retried."
}, {
  "path" : "retryDelay",
  "name" : "retryDelay",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "decimal",
  "desc" : "Delay in seconds before retrying a failed task."
}, {
  "path" : "status",
  "name" : "status",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Current status of the task. \n\n**Allowed values**: `created` | `ready` | `requested` | `in-progress` | `done` | `waiting`"
}, {
  "path" : "workflow-definition",
  "name" : "workflow-definition",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Reference to the workflow definition for this task."
} ]
```


## AidboxTaskLog

```fhir-structure
[ {
  "path" : "action",
  "name" : "action",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Name of the performed action."
}, {
  "path" : "action-params",
  "name" : "action-params",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Parameters provided with the action."
}, {
  "path" : "re-scheduled",
  "name" : "re-scheduled",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Definition of a rescheduled action."
}, {
  "path" : "re-scheduled.action",
  "name" : "action",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Name of the rescheduled action."
}, {
  "path" : "re-scheduled.action-params",
  "name" : "action-params",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Parameters for the rescheduled action."
}, {
  "path" : "re-scheduled.at",
  "name" : "at",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "New specific time when the action should be executed."
}, {
  "path" : "re-scheduled.delay",
  "name" : "delay",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "decimal",
  "desc" : "New delay in seconds before executing the action."
}, {
  "path" : "re-scheduled.id",
  "name" : "id",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Unique identifier for the rescheduled action."
}, {
  "path" : "scheduled",
  "name" : "scheduled",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Definition of a scheduled action."
}, {
  "path" : "scheduled.action",
  "name" : "action",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Name of the scheduled action."
}, {
  "path" : "scheduled.action-params",
  "name" : "action-params",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Parameters for the scheduled action."
}, {
  "path" : "scheduled.at",
  "name" : "at",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Specific time when the action should be executed."
}, {
  "path" : "scheduled.delay",
  "name" : "delay",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "decimal",
  "desc" : "Delay in seconds before executing the action."
}, {
  "path" : "scheduled.id",
  "name" : "id",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Unique identifier for the scheduled action."
}, {
  "path" : "status-after",
  "name" : "status-after",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Task status after the action was performed."
}, {
  "path" : "status-before",
  "name" : "status-before",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Task status before the action was performed."
}, {
  "path" : "subject",
  "name" : "subject",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Resource that is the subject of the action."
}, {
  "path" : "subject.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Human-readable display name of the subject."
}, {
  "path" : "subject.id",
  "name" : "id",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Identifier of the subject."
}, {
  "path" : "subject.resourceType",
  "name" : "resourceType",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of resource that is the subject."
} ]
```


## AidboxTopicDestination

Configures where and how notifications triggered by a subscription topic should be routed. Connects topics to external systems like Kafka, RabbitMQ, Webhook and others.

```fhir-structure
[ {
  "path" : "content",
  "name" : "content",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "empty | id-only | full-resource. \n\n**Allowed values**: `empty` | `id-only` | `full-resource`"
}, {
  "path" : "kind",
  "name" : "kind",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Channel type for notifications."
}, {
  "path" : "status",
  "name" : "status",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "requested | active | error | off | entered-in-error."
}, {
  "path" : "topic",
  "name" : "topic",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "url",
  "desc" : "Reference to the AidboxSubscriptionTopic being subscribed to."
} ]
```


## AidboxTrigger

Aidbox trigger resource for executing SQL on resource create, update, or delete events.

```fhir-structure
[ {
  "path" : "action",
  "name" : "action",
  "lvl" : 0,
  "min" : 1,
  "max" : "*",
  "type" : "code",
  "desc" : ""
}, {
  "path" : "resource",
  "name" : "resource",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "sql",
  "name" : "sql",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
} ]
```


## AidboxWorkflow

```fhir-structure
[ {
  "path" : "definition",
  "name" : "definition",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Workflow definition identifier."
}, {
  "path" : "error",
  "name" : "error",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Error details if workflow failed."
}, {
  "path" : "execId",
  "name" : "execId",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Unique execution identifier."
}, {
  "path" : "executeAt",
  "name" : "executeAt",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Scheduled time for workflow execution."
}, {
  "path" : "label",
  "name" : "label",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Human-readable label for the workflow."
}, {
  "path" : "outcome",
  "name" : "outcome",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Final outcome of the workflow execution. \n\n**Allowed values**: `succeeded` | `failed` | `canceled`"
}, {
  "path" : "outcomeReason",
  "name" : "outcomeReason",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Detailed reason for the workflow outcome."
}, {
  "path" : "outcomeReason.data",
  "name" : "data",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Additional data related to the outcome."
}, {
  "path" : "outcomeReason.message",
  "name" : "message",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Human-readable explanation of the outcome."
}, {
  "path" : "outcomeReason.type",
  "name" : "type",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of outcome reason. \n\n**Allowed values**: `awf.workflow/failed-by-executor` | `awf.executor/unknown-error`"
}, {
  "path" : "params",
  "name" : "params",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Parameters required for workflow execution."
}, {
  "path" : "requester",
  "name" : "requester",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Entity that requested the workflow."
}, {
  "path" : "requester.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Human-readable display name of the requester."
}, {
  "path" : "requester.id",
  "name" : "id",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Identifier of the requester."
}, {
  "path" : "requester.resourceType",
  "name" : "resourceType",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of resource that made the request."
}, {
  "path" : "requester.rule",
  "name" : "rule",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Rule that authorized the request."
}, {
  "path" : "requester.service",
  "name" : "service",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Service that initiated the request."
}, {
  "path" : "result",
  "name" : "result",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Result data produced by successful workflow execution."
}, {
  "path" : "retryCount",
  "name" : "retryCount",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "decimal",
  "desc" : "Number of times the workflow has been retried."
}, {
  "path" : "status",
  "name" : "status",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Current status of the workflow. \n\n**Allowed values**: `created` | `in-progress` | `done`"
} ]
```


## App

Application definition resource for Aidbox.

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
  "path" : "endpoint.type",
  "name" : "type",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "code",
  "desc" : "Type of endpoint protocol. \n\n**Allowed values**: `http-rpc` | `ws-rpc` | `native`"
}, {
  "path" : "endpoint.url",
  "name" : "url",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "URL of the endpoint."
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
  "path" : "migrations.action",
  "name" : "action",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Action to be performed for the migration."
}, {
  "path" : "migrations.dateTime",
  "name" : "dateTime",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Timestamp for the migration."
}, {
  "path" : "migrations.id",
  "name" : "id",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Unique identifier for the migration."
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

Attribute definition resource for Aidbox entities.

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
  "min" : 1,
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
  "type" : "BackboneElement",
  "desc" : "Value set constraint for this attribute."
}, {
  "path" : "valueSet.id",
  "name" : "id",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Identifier of the value set."
}, {
  "path" : "valueSet.resourceType",
  "name" : "resourceType",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of the value set resource."
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


## AuthConfig

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
  "path" : "asidCookieMaxAge",
  "name" : "asidCookieMaxAge",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Maximum age of the ASID cookie in seconds. Default is 432000 (5 days)."
}, {
  "path" : "theme",
  "name" : "theme",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Theme configuration for auth page."
}, {
  "path" : "theme.brand",
  "name" : "brand",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Brand for auth page"
}, {
  "path" : "theme.forgotPasswordUrl",
  "name" : "forgotPasswordUrl",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "uri",
  "desc" : "URL to forgot password page"
}, {
  "path" : "theme.styleUrl",
  "name" : "styleUrl",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "uri",
  "desc" : "URL to external stylesheet"
}, {
  "path" : "theme.title",
  "name" : "title",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Title for auth page"
}, {
  "path" : "twoFactor",
  "name" : "twoFactor",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Two-factor authentication configuration."
}, {
  "path" : "twoFactor.issuerName",
  "name" : "issuerName",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Issuer name for OTP authenticator app."
}, {
  "path" : "twoFactor.validPastTokensCount",
  "name" : "validPastTokensCount",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Number of past tokens considered valid (useful with webhook since OTP lives ~30s)"
}, {
  "path" : "twoFactor.webhook",
  "name" : "webhook",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Webhook configuration for 2FA."
}, {
  "path" : "twoFactor.webhook.endpoint",
  "name" : "endpoint",
  "lvl" : 2,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "URL to webhook that supports POST method"
}, {
  "path" : "twoFactor.webhook.headers",
  "name" : "headers",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Map of HTTP header key-value pairs."
}, {
  "path" : "twoFactor.webhook.timeout",
  "name" : "timeout",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Timeout in milliseconds."
} ]
```


## AwsAccount

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
  "path" : "access-key-id",
  "name" : "access-key-id",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "AWS access key identifier for authentication."
}, {
  "path" : "host",
  "name" : "host",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "AWS host endpoint for the service."
}, {
  "path" : "path-style",
  "name" : "path-style",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether to use path-style addressing for S3 requests."
}, {
  "path" : "region",
  "name" : "region",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "AWS region where the resources are located."
}, {
  "path" : "secret-access-key",
  "name" : "secret-access-key",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "AWS secret access key for authentication."
}, {
  "path" : "use-ssl",
  "name" : "use-ssl",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether to use SSL for secure connections."
} ]
```


## AzureAccount

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
  "path" : "clientId",
  "name" : "clientId",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Azure AD application/client ID (required for User Delegation SAS). Available since 2508."
}, {
  "path" : "clientSecret",
  "name" : "clientSecret",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Azure AD client secret (required for User Delegation SAS). Available since 2508."
}, {
  "path" : "key",
  "name" : "key",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Azure storage account key for authentication (required for Account SAS)."
}, {
  "path" : "sasType",
  "name" : "sasType",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "SAS type: 'account' (default) or 'userDelegation'. Available since 2508."
}, {
  "path" : "tenantId",
  "name" : "tenantId",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Azure AD tenant ID (required for User Delegation SAS)."
} ]
```


## AzureContainer

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
  "path" : "account",
  "name" : "account",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to the Azure account credentials. \n\n**Allowed references**: AzureAccount"
}, {
  "path" : "container",
  "name" : "container",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Name of the Azure storage container."
}, {
  "path" : "extension",
  "name" : "extension",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "File extension for content stored in the container."
}, {
  "path" : "storage",
  "name" : "storage",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Azure storage account name."
} ]
```


## BatchValidationError

Validation error resource for batch validation runs.

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
  "min" : 1,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Reference to the resource that failed validation."
}, {
  "path" : "resource.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Human-readable display name for the resource."
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
  "path" : "run",
  "name" : "run",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Reference to the validation run that produced this error."
}, {
  "path" : "run.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Human-readable display name for the validation run."
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
} ]
```


## BatchValidationRun

Batch validation run resource for tracking validation processes.

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
  "min" : 1,
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
  "path" : "error.count",
  "name" : "count",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Number of resources that encountered errors."
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
  "type" : "BackboneElement",
  "desc" : "Information about the exported data files."
}, {
  "path" : "output.count",
  "name" : "count",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Number of resources in this output file."
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
  "path" : "params",
  "name" : "params",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Parameters specified for the bulk export request."
}, {
  "path" : "params.export-level",
  "name" : "export-level",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Level at which to perform the export (patient, group, or system). \n\n**Allowed values**: `patient` | `group` | `system`"
}, {
  "path" : "params.fhir?",
  "name" : "fhir?",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether to export in FHIR format."
}, {
  "path" : "params.group-id",
  "name" : "group-id",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "ID of the group to export data for, when export-level is 'group'."
}, {
  "path" : "params.gzip?",
  "name" : "gzip?",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether to compress the exported files using gzip."
}, {
  "path" : "params.output-file-ext",
  "name" : "output-file-ext",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "File extension for the exported files. \n\n**Allowed values**: `.ndjson` | `.ndjson.gz`"
}, {
  "path" : "params.output-format",
  "name" : "output-format",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Format of the exported data files. \n\n**Allowed values**: `application/fhir+ndjson` | `application/ndjson+fhir` | `application/ndjson` | `ndjson` | `application/fhir+ndjson+gzip` | `application/fhir+gzip+ndjson` | `application/ndjson+fhir+gzip` | `application/ndjson+gzip+fhir` | `application/gzip+fhir+ndjson` | `application/gzip+ndjson+fhir`"
}, {
  "path" : "params.patient-ids",
  "name" : "patient-ids",
  "lvl" : 1,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "List of patient IDs to export data for."
}, {
  "path" : "params.requester",
  "name" : "requester",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to the user or client that requested the export."
}, {
  "path" : "params.since",
  "name" : "since",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Export only resources modified after this date."
}, {
  "path" : "params.storage",
  "name" : "storage",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Storage configuration for exported files."
}, {
  "path" : "params.storage.account",
  "name" : "account",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to account with credentials for accessing the storage."
}, {
  "path" : "params.storage.bucket",
  "name" : "bucket",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Name of the storage bucket to use."
}, {
  "path" : "params.storage.type",
  "name" : "type",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of storage backend to use. \n\n**Allowed values**: `test-delay` | `test-cloud` | `aidbox` | `gcp` | `aws` | `azure`"
}, {
  "path" : "params.tenant",
  "name" : "tenant",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Tenant identifier for multi-tenant environments."
}, {
  "path" : "params.types",
  "name" : "types",
  "lvl" : 1,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "List of resource types to include in the export."
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
  "min" : 1,
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
  "path" : "inputs.duration",
  "name" : "duration",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Import duration time."
}, {
  "path" : "inputs.resourceType",
  "name" : "resourceType",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of resources contained in this input file."
}, {
  "path" : "inputs.status",
  "name" : "status",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Import progress status."
}, {
  "path" : "inputs.time",
  "name" : "time",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Time taken to process this input file in milliseconds."
}, {
  "path" : "inputs.total",
  "name" : "total",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Total number of imported resources."
}, {
  "path" : "inputs.ts",
  "name" : "ts",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Time when the import operation completed."
}, {
  "path" : "inputs.url",
  "name" : "url",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "URL of the imported file."
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


## Client

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
  "path" : "active",
  "name" : "active",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Indicates whether this client is active and can be used for authentication."
}, {
  "path" : "allowed-scopes",
  "name" : "allowed-scopes",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "Reference",
  "desc" : "References to specific Scope resources this client is allowed to request. \n\n**Allowed references**: Scope"
}, {
  "path" : "allowedIssuers",
  "name" : "allowedIssuers",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "List of authorized token issuers for this client."
}, {
  "path" : "allowed_origins",
  "name" : "allowed_origins",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "uri",
  "desc" : "Allowed Origins are URLs that will be allowed to make requests."
}, {
  "path" : "auth",
  "name" : "auth",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Authentication configuration for different OAuth flows."
}, {
  "path" : "auth.authorization_code",
  "name" : "authorization_code",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Configuration for the authorization code grant type."
}, {
  "path" : "auth.authorization_code.access_token_expiration",
  "name" : "access_token_expiration",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Expiration time for access tokens in seconds."
}, {
  "path" : "auth.authorization_code.audience",
  "name" : "audience",
  "lvl" : 2,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "Intended audience for issued tokens."
}, {
  "path" : "auth.authorization_code.client_assertion_types",
  "name" : "client_assertion_types",
  "lvl" : 2,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "Supported client assertion types. \n\n**Allowed values**: `urn:ietf:params:oauth:client-assertion-type:jwt-bearer`"
}, {
  "path" : "auth.authorization_code.default_identity_provider",
  "name" : "default_identity_provider",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Default IdentityProvider that will be used instead of Aidbox login. \n\n**Allowed references**: IdentityProvider"
}, {
  "path" : "auth.authorization_code.pkce",
  "name" : "pkce",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether PKCE (Proof Key for Code Exchange) is required."
}, {
  "path" : "auth.authorization_code.redirect_uri",
  "name" : "redirect_uri",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "url",
  "desc" : "Redirect URI for the authorization code flow."
}, {
  "path" : "auth.authorization_code.refresh_token",
  "name" : "refresh_token",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether to issue refresh tokens with this grant type."
}, {
  "path" : "auth.authorization_code.refresh_token_expiration",
  "name" : "refresh_token_expiration",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Expiration time for refresh tokens in seconds."
}, {
  "path" : "auth.authorization_code.secret_required",
  "name" : "secret_required",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether client secret is required for token exchange."
}, {
  "path" : "auth.authorization_code.token_format",
  "name" : "token_format",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Format of the access token. \n\n**Allowed values**: `jwt`"
}, {
  "path" : "auth.client_credentials",
  "name" : "client_credentials",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Configuration for the client credentials grant type."
}, {
  "path" : "auth.client_credentials.access_token_expiration",
  "name" : "access_token_expiration",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Expiration time for access tokens in seconds."
}, {
  "path" : "auth.client_credentials.audience",
  "name" : "audience",
  "lvl" : 2,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "Intended audience for issued tokens."
}, {
  "path" : "auth.client_credentials.client_assertion_types",
  "name" : "client_assertion_types",
  "lvl" : 2,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "Supported client assertion types. \n\n**Allowed values**: `urn:ietf:params:oauth:client-assertion-type:jwt-bearer`"
}, {
  "path" : "auth.client_credentials.refresh_token",
  "name" : "refresh_token",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether to issue refresh tokens with this grant type."
}, {
  "path" : "auth.client_credentials.refresh_token_expiration",
  "name" : "refresh_token_expiration",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Expiration time for refresh tokens in seconds."
}, {
  "path" : "auth.client_credentials.token_format",
  "name" : "token_format",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Format of the access token. \n\n**Allowed values**: `jwt`"
}, {
  "path" : "auth.implicit",
  "name" : "implicit",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Configuration for the implicit grant type."
}, {
  "path" : "auth.implicit.access_token_expiration",
  "name" : "access_token_expiration",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Expiration time for access tokens in seconds."
}, {
  "path" : "auth.implicit.audience",
  "name" : "audience",
  "lvl" : 2,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "Intended audience for issued tokens."
}, {
  "path" : "auth.implicit.redirect_uri",
  "name" : "redirect_uri",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "url",
  "desc" : "Redirect URI for the implicit flow."
}, {
  "path" : "auth.implicit.token_format",
  "name" : "token_format",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Format of the access token. \n\n**Allowed values**: `jwt`"
}, {
  "path" : "auth.password",
  "name" : "password",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Configuration for the password grant type."
}, {
  "path" : "auth.password.access_token_expiration",
  "name" : "access_token_expiration",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Expiration time for access tokens in seconds."
}, {
  "path" : "auth.password.audience",
  "name" : "audience",
  "lvl" : 2,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "Intended audience for issued tokens."
}, {
  "path" : "auth.password.redirect_uri",
  "name" : "redirect_uri",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "url",
  "desc" : "If present, turn on redirect protection"
}, {
  "path" : "auth.password.refresh_token",
  "name" : "refresh_token",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether to issue refresh tokens with this grant type."
}, {
  "path" : "auth.password.refresh_token_expiration",
  "name" : "refresh_token_expiration",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Expiration time for refresh tokens in seconds."
}, {
  "path" : "auth.password.secret_required",
  "name" : "secret_required",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether client secret is required for password grant."
}, {
  "path" : "auth.password.token_format",
  "name" : "token_format",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Format of the access token. \n\n**Allowed values**: `jwt`"
}, {
  "path" : "auth.token_exchange",
  "name" : "token_exchange",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Configuration for the token exchange grant type."
}, {
  "path" : "auth.token_exchange.access_token_expiration",
  "name" : "access_token_expiration",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Expiration time for access tokens in seconds."
}, {
  "path" : "auth.token_exchange.audience",
  "name" : "audience",
  "lvl" : 2,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "Intended audience for issued tokens."
}, {
  "path" : "auth.token_exchange.refresh_token",
  "name" : "refresh_token",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether to issue refresh tokens with this grant type."
}, {
  "path" : "auth.token_exchange.refresh_token_expiration",
  "name" : "refresh_token_expiration",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Expiration time for refresh tokens in seconds."
}, {
  "path" : "auth.token_exchange.token_format",
  "name" : "token_format",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Format of the access token. \n\n**Allowed values**: `jwt`"
}, {
  "path" : "description",
  "name" : "description",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A description of the client application for administrative purposes."
}, {
  "path" : "details",
  "name" : "details",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Additional client details or configuration options."
}, {
  "path" : "fhir-base-url",
  "name" : "fhir-base-url",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Base URL of the FHIR server this client interacts with."
}, {
  "path" : "first_party",
  "name" : "first_party",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Indicates whether this is a first-party client."
}, {
  "path" : "grant_types",
  "name" : "grant_types",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "OAuth 2.0 grant types this client is authorized to use. \n\n**Allowed values**: `basic` | `authorization_code` | `code` | `password` | `client_credentials` | `implicit` | `refresh_token` | `urn:ietf:params:oauth:grant-type:token-exchange`"
}, {
  "path" : "jwks",
  "name" : "jwks",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "JSON Web Key Set for client authentication and/or verification."
}, {
  "path" : "jwks.alg",
  "name" : "alg",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Algorithm used with this key. \n\n**Allowed values**: `RS384`"
}, {
  "path" : "jwks.e",
  "name" : "e",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Exponent value for RSA key."
}, {
  "path" : "jwks.kid",
  "name" : "kid",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Key ID that identifies this key."
}, {
  "path" : "jwks.kty",
  "name" : "kty",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Key type. \n\n**Allowed values**: `RSA`"
}, {
  "path" : "jwks.n",
  "name" : "n",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Modulus value for RSA key."
}, {
  "path" : "jwks.use",
  "name" : "use",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Key usage. \n\n**Allowed values**: `sig`"
}, {
  "path" : "jwks_uri",
  "name" : "jwks_uri",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "url",
  "desc" : "URI where the client's JSON Web Key Set can be retrieved."
}, {
  "path" : "name",
  "name" : "name",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Human-readable name of the client application."
}, {
  "path" : "scope",
  "name" : "scope",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "List of scopes this client is authorized to request."
}, {
  "path" : "scopes",
  "name" : "scopes",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "Detailed scope configurations with associated policies."
}, {
  "path" : "scopes.parameters",
  "name" : "parameters",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Parameters to be applied with the scope's policy."
}, {
  "path" : "scopes.policy",
  "name" : "policy",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to an AccessPolicy resource for this scope. \n\n**Allowed references**: AccessPolicy"
}, {
  "path" : "secret",
  "name" : "secret",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Hashed client secret for authentication."
}, {
  "path" : "smart",
  "name" : "smart",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "SMART on FHIR configuration for this client."
}, {
  "path" : "smart.description",
  "name" : "description",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Description of the SMART app."
}, {
  "path" : "smart.launch_uri",
  "name" : "launch_uri",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "URI to launch the SMART app."
}, {
  "path" : "smart.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Name of the SMART app."
}, {
  "path" : "trusted",
  "name" : "trusted",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Indicates whether this client is trusted and given special privileges."
}, {
  "path" : "type",
  "name" : "type",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The type of client application."
} ]
```


## Concept

Terminology concept resource for Aidbox.

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
  "type" : "Object",
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

Concept mapping rule resource for terminology translations.

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
  "type" : "BackboneElement",
  "desc" : "Element mapping definition."
}, {
  "path" : "element.target",
  "name" : "target",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
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
  "type" : "BackboneElement",
  "desc" : "Rules for handling unmapped concepts."
}, {
  "path" : "unmapped.mode",
  "name" : "mode",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Mode for handling unmapped concepts."
}, {
  "path" : "unmapped.url",
  "name" : "url",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "URL for unmapped value set."
} ]
```


## CustomType

A modifier extension for ElementDefinition that indicates the element uses a custom non-FHIR type. When value is 'any', compiles to {any: true} schema without additionalProperties wrapper.

```fhir-structure
[ {
  "path" : "extension",
  "name" : "extension",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "url",
  "name" : "url",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "value[x]",
  "name" : "value[x]",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "code",
  "desc" : "Custom type code"
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
  "path" : "restore.duration",
  "name" : "duration",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Duration of the index restoration in milliseconds."
}, {
  "path" : "restore.end",
  "name" : "end",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Time when the index restoration completed."
}, {
  "path" : "restore.start",
  "name" : "start",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Time when the index restoration started."
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


## Entity

Entity definition resource for custom Aidbox resource types.

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

Status tracking resource for flat file imports.

```fhir-structure
[ {
  "path" : "error",
  "name" : "error",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
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
  "type" : "BackboneElement",
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

FHIR Terminology Repository configuration resource.

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


## GcpServiceAccount

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
  "path" : "private-key",
  "name" : "private-key",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Private key for GCP service account authentication."
}, {
  "path" : "service-account-email",
  "name" : "service-account-email",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Email address of the GCP service account."
} ]
```


## Grant

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
  "path" : "client",
  "name" : "client",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to the client application being granted access. \n\n**Allowed references**: Client"
}, {
  "path" : "patient",
  "name" : "patient",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to the patient this grant is for (in SMART on FHIR scenarios). \n\n**Allowed references**: Patient"
}, {
  "path" : "provided-scope",
  "name" : "provided-scope",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "List of scopes that were actually granted by the user."
}, {
  "path" : "requested-scope",
  "name" : "requested-scope",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "List of scopes that were requested by the client."
}, {
  "path" : "scope",
  "name" : "scope",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Space-separated list of granted scopes."
}, {
  "path" : "start",
  "name" : "start",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Time when this grant was created."
}, {
  "path" : "user",
  "name" : "user",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to the user who granted the access. \n\n**Allowed references**: User"
} ]
```


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
  "type" : "BackboneElement",
  "desc" : "Extensions for the HL7v2 message processing configuration."
}, {
  "path" : "extensions.after",
  "name" : "after",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "After segment placement."
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
  "desc" : "Field key."
}, {
  "path" : "extensions.fields.name",
  "name" : "name",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Field name."
}, {
  "path" : "extensions.fields.type",
  "name" : "type",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Field type."
}, {
  "path" : "extensions.msh",
  "name" : "msh",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Message header information."
}, {
  "path" : "extensions.quantifier",
  "name" : "quantifier",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Occurrence quantifier. \n\n**Allowed values**: `*` | `?` | `+`"
}, {
  "path" : "extensions.segment",
  "name" : "segment",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "HL7v2 segment identifier."
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
  "type" : "BackboneElement",
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


## IdentityProvider

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
  "path" : "active",
  "name" : "active",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Indicates whether this identity provider is active and can be used for authentication."
}, {
  "path" : "authorize_endpoint",
  "name" : "authorize_endpoint",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The URL of the authorization endpoint."
}, {
  "path" : "base_url",
  "name" : "base_url",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "uri",
  "desc" : "Base URL for the identity provider."
}, {
  "path" : "client",
  "name" : "client",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Client configuration for this identity provider."
}, {
  "path" : "client.auth-method",
  "name" : "auth-method",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Client authentication method. \n\n**Allowed values**: `symmetric` | `asymmetric`"
}, {
  "path" : "client.certificate",
  "name" : "certificate",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Client certificate for authentication."
}, {
  "path" : "client.certificate-thumbprint",
  "name" : "certificate-thumbprint",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Certificate thumbprint."
}, {
  "path" : "client.creds-ts",
  "name" : "creds-ts",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Credentials timestamp."
}, {
  "path" : "client.id",
  "name" : "id",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Client identifier used for authentication with the identity provider."
}, {
  "path" : "client.private-key",
  "name" : "private-key",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Private key for asymmetric authentication."
}, {
  "path" : "client.redirect_uri",
  "name" : "redirect_uri",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "uri",
  "desc" : "URI where the provider will redirect after authentication."
}, {
  "path" : "client.secret",
  "name" : "secret",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Client secret for symmetric authentication."
}, {
  "path" : "introspection_endpoint",
  "name" : "introspection_endpoint",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The URL of the token introspection endpoint."
}, {
  "path" : "isEmailUniqueness",
  "name" : "isEmailUniqueness",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Indicates whether email uniqueness should be enforced for this provider."
}, {
  "path" : "isScim",
  "name" : "isScim",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Indicates whether this provider supports SCIM protocol."
}, {
  "path" : "jwks_uri",
  "name" : "jwks_uri",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "URI where the provider's JSON Web Key Set can be retrieved."
}, {
  "path" : "kid",
  "name" : "kid",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Key identifier used for token verification."
}, {
  "path" : "organizations",
  "name" : "organizations",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "Organizations associated with this identity provider."
}, {
  "path" : "registration_endpoint",
  "name" : "registration_endpoint",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The URL of the registration endpoint."
}, {
  "path" : "revocation_endpoint",
  "name" : "revocation_endpoint",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The URL of the token revocation endpoint."
}, {
  "path" : "scopes",
  "name" : "scopes",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "OAuth scopes that should be requested during authentication."
}, {
  "path" : "system",
  "name" : "system",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "System identifier for the identity provider."
}, {
  "path" : "team_id",
  "name" : "team_id",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Team ID (for Apple)."
}, {
  "path" : "title",
  "name" : "title",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A human-readable name for the identity provider."
}, {
  "path" : "toScim",
  "name" : "toScim",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Mapping rules for transforming identity provider data."
}, {
  "path" : "token_endpoint",
  "name" : "token_endpoint",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The URL of the token endpoint."
}, {
  "path" : "type",
  "name" : "type",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The type of identity provider. \n\n**Allowed values**: `aidbox` | `github` | `google` | `OIDC` | `OAuth` | `az-dev` | `yandex` | `okta` | `apple`"
}, {
  "path" : "userinfo-source",
  "name" : "userinfo-source",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Source of userinfo details. \n\n**Allowed values**: `id-token` | `userinfo-endpoint`"
}, {
  "path" : "userinfo_endpoint",
  "name" : "userinfo_endpoint",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The URL of the userinfo endpoint."
}, {
  "path" : "userinfo_header",
  "name" : "userinfo_header",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Header to be used when calling the userinfo endpoint."
} ]
```


## IndexCreationJob

Database index creation job tracking resource.

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
  "min" : 1,
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

Lambda function resource for hook-based code execution.

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
  "path" : "error.code",
  "name" : "code",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Error code or identifier."
}, {
  "path" : "error.source",
  "name" : "source",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Source of the error. \n\n**Allowed values**: `postgres` | `aws` | `aidbox`"
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
  "type" : "decimal",
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


## Mapping

Data transformation mapping resource for Aidbox.

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
  "type" : "BackboneElement",
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
  "type" : "BackboneElement",
  "desc" : "Human-readable text about the mapping."
}, {
  "path" : "text.div",
  "name" : "div",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "HTML representation of the text."
}, {
  "path" : "text.status",
  "name" : "status",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Status of the text."
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

Module definition resource for Aidbox.

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
  "path" : "meta.post-sql",
  "name" : "post-sql",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "SQL to execute after module installation."
}, {
  "path" : "meta.pre-sql",
  "name" : "pre-sql",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "SQL to execute before module installation."
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

Notebook resource for interactive documentation and code execution.

```fhir-structure
[ {
  "path" : "cells",
  "name" : "cells",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "Cells contained in the notebook."
}, {
  "path" : "cells.error",
  "name" : "error",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Error information if evaluation failed."
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
  "type" : "BackboneElement",
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
  "path" : "cells.id",
  "name" : "id",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : "Unique identifier for the cell."
}, {
  "path" : "cells.nb-title",
  "name" : "nb-title",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Title for the cell."
}, {
  "path" : "cells.result",
  "name" : "result",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Result of cell evaluation."
}, {
  "path" : "cells.type",
  "name" : "type",
  "lvl" : 1,
  "min" : 1,
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


## Notification

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
  "path" : "provider",
  "name" : "provider",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Notification provider."
}, {
  "path" : "providerData",
  "name" : "providerData",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Provider-specific data."
}, {
  "path" : "status",
  "name" : "status",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Status of the notification delivery (delivered or error). \n\n**Allowed values**: `delivered` | `error`"
} ]
```


## NotificationTemplate

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
  "path" : "subject",
  "name" : "subject",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Subject line for the notification template."
}, {
  "path" : "template",
  "name" : "template",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Template content used to generate the notification message."
} ]
```


## Operation

Custom operation definition resource for Aidbox.

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
  "min" : 1,
  "max" : 1,
  "type" : "code",
  "desc" : "Transformation engine to use."
}, {
  "path" : "transform.request.part",
  "name" : "part",
  "lvl" : 2,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Part of the request to transform. \n\n**Allowed values**: `body`"
}, {
  "path" : "transform.request.template",
  "name" : "template",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to a template for transformation."
} ]
```


## PGSequence

PostgreSQL sequence definition resource for Aidbox.

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


## Registration

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
  "path" : "params",
  "name" : "params",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : ""
}, {
  "path" : "resource",
  "name" : "resource",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Registration form data."
}, {
  "path" : "status",
  "name" : "status",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Status of the registration process. \n\n**Allowed values**: `activated` | `active`"
} ]
```


## Role

Role resource for assigning access roles to users.

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
  "path" : "context",
  "name" : "context",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Additional context data."
}, {
  "path" : "description",
  "name" : "description",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Text description of the role."
}, {
  "path" : "links",
  "name" : "links",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "You may list resources here, which can later be granted access for the user with this role via an AccessPolicy resource."
}, {
  "path" : "links.organization",
  "name" : "organization",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to Organization resource. \n\n**Allowed references**: Organization"
}, {
  "path" : "links.patient",
  "name" : "patient",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to Patient resource \n\n**Allowed references**: Patient"
}, {
  "path" : "links.person",
  "name" : "person",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to Person resource. \n\n**Allowed references**: Person"
}, {
  "path" : "links.practitioner",
  "name" : "practitioner",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to Practitioner resource. \n\n**Allowed references**: Practitioner"
}, {
  "path" : "links.practitionerRole",
  "name" : "practitionerRole",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to PractitionerRole resource. \n\n**Allowed references**: PractitionerRole"
}, {
  "path" : "links.relatedPerson",
  "name" : "relatedPerson",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to RelatedPerson resource. \n\n**Allowed references**: RelatedPerson"
}, {
  "path" : "name",
  "name" : "name",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Role name is a string that defines role. To assign the same role to multiple users, create multiple Role resources with the same \"name\". [Search param: name => type string]"
}, {
  "path" : "user",
  "name" : "user",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to a User resource for which the role will be applied. [Search param: user => type reference] \n\n**Allowed references**: User"
} ]
```


## SchedulerRuleStatus

```fhir-structure
[ {
  "path" : "allowedRetryCount",
  "name" : "allowedRetryCount",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Maximum number of retries allowed for this rule."
}, {
  "path" : "concurrencyLimit",
  "name" : "concurrencyLimit",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "decimal",
  "desc" : "Maximum number of concurrent executions allowed for this rule."
}, {
  "path" : "concurrencyPath",
  "name" : "concurrencyPath",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "Path to the field used for concurrency control."
}, {
  "path" : "definition",
  "name" : "definition",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Identifier for the scheduler rule definition."
}, {
  "path" : "executeAt",
  "name" : "executeAt",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Next scheduled execution time for this rule."
}, {
  "path" : "inProgressTimeout",
  "name" : "inProgressTimeout",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "decimal",
  "desc" : "Maximum duration in seconds that a task can remain in in-progress status before timing out."
}, {
  "path" : "label",
  "name" : "label",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Human-readable label for the scheduler rule."
}, {
  "path" : "lastSchedule",
  "name" : "lastSchedule",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Timestamp of the last schedule attempt."
}, {
  "path" : "lastScheduleReference",
  "name" : "lastScheduleReference",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Reference to the last scheduled task for this rule."
}, {
  "path" : "lastScheduleReference.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Human-readable display name of the last scheduled task."
}, {
  "path" : "lastScheduleReference.id",
  "name" : "id",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Identifier of the last scheduled task."
}, {
  "path" : "lastScheduleReference.resourceType",
  "name" : "resourceType",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of resource that was last scheduled."
}, {
  "path" : "lastScheduleStatus",
  "name" : "lastScheduleStatus",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Status of the last schedule attempt. \n\n**Allowed values**: `started` | `skipped`"
}, {
  "path" : "params",
  "name" : "params",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : "Parameters required for rule execution."
}, {
  "path" : "requestedToStartTimeout",
  "name" : "requestedToStartTimeout",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "decimal",
  "desc" : "Maximum duration in seconds that a task can remain in requested status before timing out."
}, {
  "path" : "retryDelay",
  "name" : "retryDelay",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "decimal",
  "desc" : "Delay in seconds before retrying a failed rule execution."
} ]
```


## Scope

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
  "desc" : "Scope definition additionally displayed on the consent screen."
}, {
  "path" : "scope",
  "name" : "scope",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "The value of the scope."
}, {
  "path" : "title",
  "name" : "title",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "A user-friendly name for the scope that appears on the consent screen."
} ]
```


## Search

Custom search parameter definition resource for Aidbox.

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
  "desc" : "Parse value as string, identifier, or reference. \n\n**Allowed values**: `token` | `reference`"
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
  "path" : "token-sql.both",
  "name" : "both",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "SQL template when both system and code are provided."
}, {
  "path" : "token-sql.no-system",
  "name" : "no-system",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "SQL template when no system is provided."
}, {
  "path" : "token-sql.only-code",
  "name" : "only-code",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "SQL template when only code is provided."
}, {
  "path" : "token-sql.only-system",
  "name" : "only-system",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "SQL template when only system is provided."
}, {
  "path" : "token-sql.text",
  "name" : "text",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "SQL template for text search."
}, {
  "path" : "token-sql.text-format",
  "name" : "text-format",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Format for text search."
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

Custom search query resource for Aidbox.

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
  "path" : "query.join",
  "name" : "join",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Join conditions for the query."
}, {
  "path" : "query.order-by",
  "name" : "order-by",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Column or expression to order results by."
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


## SecretReference

Contains the name of a secret defined in the vault config file. Used together with data-absent-reason=masked to indicate that the field value is resolved at runtime from a mounted secret file.

```fhir-structure
[ {
  "path" : "extension",
  "name" : "extension",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "url",
  "name" : "url",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "value[x]",
  "name" : "value[x]",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
} ]
```


## SeedImport

Seed data import resource for initial data loading.

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
  "type" : "BackboneElement",
  "desc" : "Resources to be imported."
}, {
  "path" : "resources.id",
  "name" : "id",
  "lvl" : 1,
  "min" : 1,
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
  "path" : "resources.resourceType",
  "name" : "resourceType",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of resource."
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


## Session

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
  "path" : "access_token",
  "name" : "access_token",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Access token hash associated with this session."
}, {
  "path" : "active",
  "name" : "active",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Indicates whether this session is currently active."
}, {
  "path" : "audience",
  "name" : "audience",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Intended audience for tokens issued in this session."
}, {
  "path" : "authorization_code",
  "name" : "authorization_code",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Authorization code used to obtain this session."
}, {
  "path" : "client",
  "name" : "client",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to the client application associated with this session. \n\n**Allowed references**: Client"
}, {
  "path" : "ctx",
  "name" : "ctx",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Session context data."
}, {
  "path" : "end",
  "name" : "end",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Time when the session ended or will end."
}, {
  "path" : "exp",
  "name" : "exp",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Expiration time for the access token (in seconds since epoch)."
}, {
  "path" : "iss",
  "name" : "iss",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Issuer of token for the current session."
}, {
  "path" : "jti",
  "name" : "jti",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "JWT ID."
}, {
  "path" : "on-behalf",
  "name" : "on-behalf",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to a user on whose behalf this session is operating. \n\n**Allowed references**: User"
}, {
  "path" : "parent",
  "name" : "parent",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to a parent session if this is a child session. \n\n**Allowed references**: Session"
}, {
  "path" : "patient",
  "name" : "patient",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to the patient associated with this session. \n\n**Allowed references**: Patient"
}, {
  "path" : "refresh_token",
  "name" : "refresh_token",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Refresh token hash associated with this session."
}, {
  "path" : "refresh_token_exp",
  "name" : "refresh_token_exp",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Expiration time for the refresh token (in seconds since epoch)."
}, {
  "path" : "scope",
  "name" : "scope",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "List of OAuth scopes authorized for this session."
}, {
  "path" : "start",
  "name" : "start",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Time when the session started."
}, {
  "path" : "type",
  "name" : "type",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of session (e.g., authorization_code, password, client_credentials)."
}, {
  "path" : "user",
  "name" : "user",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to the user associated with this session. \n\n**Allowed references**: User"
} ]
```


## SubsNotification

Subscription notification tracking resource.

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

Topic-based subscription resource for event notifications.

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
  "type" : "BackboneElement",
  "desc" : "Channel configuration for delivering notifications."
}, {
  "path" : "channel.endpoint",
  "name" : "endpoint",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "url",
  "desc" : "URL endpoint where notifications are sent."
}, {
  "path" : "channel.headers",
  "name" : "headers",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "HTTP headers to include with notifications."
}, {
  "path" : "channel.heartbeatPeriod",
  "name" : "heartbeatPeriod",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Period in milliseconds for sending heartbeat notifications."
}, {
  "path" : "channel.payload",
  "name" : "payload",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Content to be sent in the notification."
}, {
  "path" : "channel.payload.content",
  "name" : "content",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "code",
  "desc" : "Content type for payload. \n\n**Allowed values**: `id-only` | `full-resource`"
}, {
  "path" : "channel.payload.contentType",
  "name" : "contentType",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "code",
  "desc" : "MIME type of the payload. \n\n**Allowed values**: `json` | `fhir+json`"
}, {
  "path" : "channel.payload.context",
  "name" : "context",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Additional context for the payload."
}, {
  "path" : "channel.timeout",
  "name" : "timeout",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Timeout in milliseconds for notification delivery."
}, {
  "path" : "channel.type",
  "name" : "type",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "code",
  "desc" : "Type of channel for notifications. \n\n**Allowed values**: `rest-hook`"
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

Terminology bundle file tracking resource.

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


## TokenIntrospector

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
  "path" : "cache_ttl",
  "name" : "cache_ttl",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "integer",
  "desc" : "Cache TTL for token introspection results in seconds."
}, {
  "path" : "identity_provider",
  "name" : "identity_provider",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Link to Identity provider associated with the token introspector. \n\n**Allowed references**: IdentityProvider"
}, {
  "path" : "introspection_endpoint",
  "name" : "introspection_endpoint",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Remote introspection endpoint configuration."
}, {
  "path" : "introspection_endpoint.authorization",
  "name" : "authorization",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The authorization header value."
}, {
  "path" : "introspection_endpoint.url",
  "name" : "url",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The fully qualified URL of the remote introspection endpoint."
}, {
  "path" : "jwks_uri",
  "name" : "jwks_uri",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A URL pointing to a JSON Web Key Set (JWKS). When type is jwt the introspector retrieves public keys from this URI to validate token signatures."
}, {
  "path" : "jwt",
  "name" : "jwt",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Configuration for local JWT validation used when type is jwt."
}, {
  "path" : "jwt.iss",
  "name" : "iss",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The expected issuer (iss) claim value for JWTs. The TokenIntrospector ensures that tokens it validates come from this issuer."
}, {
  "path" : "jwt.keys",
  "name" : "keys",
  "lvl" : 1,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "The set of keys to use for validation."
}, {
  "path" : "jwt.keys.alg",
  "name" : "alg",
  "lvl" : 2,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "The algorithm to use for validation. \n\n**Allowed values**: `RS256` | `RS384` | `ES256` | `HS256`"
}, {
  "path" : "jwt.keys.format",
  "name" : "format",
  "lvl" : 2,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "The format of the key to use for validation. 'plain' for symmetric algs (HS256) and 'PEM' for all asymmetric algs \n\n**Allowed values**: `PEM` | `plain`"
}, {
  "path" : "jwt.keys.k",
  "name" : "k",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The symmetric key to use for validation."
}, {
  "path" : "jwt.keys.kty",
  "name" : "kty",
  "lvl" : 2,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "The key type to use for validation. \n\n**Allowed values**: `RSA` | `EC` | `OCT`"
}, {
  "path" : "jwt.keys.pub",
  "name" : "pub",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The asymmetric key to use for validation."
}, {
  "path" : "jwt.secret",
  "name" : "secret",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A shared secret key or other signing key material used to verify the JWT's signature."
}, {
  "path" : "type",
  "name" : "type",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Specifies the type of token to introspect. \n\n**Allowed values**: `opaque` | `jwt` | `aspxauth`"
} ]
```


## User

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
  "path" : "active",
  "name" : "active",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Deprecated. Use 'inactive' instead. Indicates the User's administrative status."
}, {
  "path" : "addresses",
  "name" : "addresses",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "A physical mailing address for this User."
}, {
  "path" : "addresses.country",
  "name" : "country",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Country name component."
}, {
  "path" : "addresses.formatted",
  "name" : "formatted",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Full address, formatted for display or mailing label."
}, {
  "path" : "addresses.locality",
  "name" : "locality",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "City or locality component."
}, {
  "path" : "addresses.postalCode",
  "name" : "postalCode",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Zip code or postal code."
}, {
  "path" : "addresses.region",
  "name" : "region",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "State or region component."
}, {
  "path" : "addresses.streetAddress",
  "name" : "streetAddress",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Street address component (may contain newlines)."
}, {
  "path" : "addresses.type",
  "name" : "type",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A label indicating the address type."
}, {
  "path" : "costCenter",
  "name" : "costCenter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Identifies the name of a cost center."
}, {
  "path" : "data",
  "name" : "data",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Arbitrary user-related data."
}, {
  "path" : "department",
  "name" : "department",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Identifies the name of a department."
}, {
  "path" : "displayName",
  "name" : "displayName",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The name of the User, suitable for display to end-users."
}, {
  "path" : "division",
  "name" : "division",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Identifies the name of a division."
}, {
  "path" : "email",
  "name" : "email",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Primary email for the user."
}, {
  "path" : "emails",
  "name" : "emails",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "Email addresses for the user."
}, {
  "path" : "emails.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A human-readable name for display purposes (READ-ONLY)."
}, {
  "path" : "emails.primary",
  "name" : "primary",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Indicates if this is the primary email."
}, {
  "path" : "emails.type",
  "name" : "type",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A label indicating the attribute's function, e.g. 'work', 'home'."
}, {
  "path" : "emails.value",
  "name" : "value",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "An individual email address (canonicalized)."
}, {
  "path" : "employeeNumber",
  "name" : "employeeNumber",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Numeric or alphanumeric identifier assigned to a person."
}, {
  "path" : "entitlements",
  "name" : "entitlements",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "A list of entitlements for the User that represent a thing the User has."
}, {
  "path" : "entitlements.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A human-readable name, primarily used for display purposes (READ-ONLY)."
}, {
  "path" : "entitlements.primary",
  "name" : "primary",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Indicates if this is the primary entitlement. Only one may be 'true'."
}, {
  "path" : "entitlements.type",
  "name" : "type",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A label indicating the attribute's function."
}, {
  "path" : "entitlements.value",
  "name" : "value",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The value of an entitlement."
}, {
  "path" : "fhirUser",
  "name" : "fhirUser",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "A reference to a related FHIR resource. \n\n**Allowed references**: Patient, Practitioner, PractitionerRole, Person, RelatedPerson"
}, {
  "path" : "gender",
  "name" : "gender",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The user's gender."
}, {
  "path" : "identifier",
  "name" : "identifier",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "Identifier",
  "desc" : "A list of identifiers for the user."
}, {
  "path" : "ims",
  "name" : "ims",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "Instant messaging addresses for the User."
}, {
  "path" : "ims.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A human-readable name, primarily for display (READ-ONLY)."
}, {
  "path" : "ims.primary",
  "name" : "primary",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Indicates if this is the primary IM. Only one may be 'true'."
}, {
  "path" : "ims.type",
  "name" : "type",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A label indicating the IM type, e.g. 'aim', 'gtalk'."
}, {
  "path" : "ims.value",
  "name" : "value",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Instant messaging address."
}, {
  "path" : "inactive",
  "name" : "inactive",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "A Boolean value indicating the User's administrative status."
}, {
  "path" : "link",
  "name" : "link",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "A collection of references or links associated with the user."
}, {
  "path" : "link.link",
  "name" : "link",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "A referenced resource link."
}, {
  "path" : "link.type",
  "name" : "type",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A label indicating the link's function."
}, {
  "path" : "locale",
  "name" : "locale",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Indicates the User's default location for localization."
}, {
  "path" : "manager",
  "name" : "manager",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Another User resource who is this User's manager. \n\n**Allowed references**: User"
}, {
  "path" : "name",
  "name" : "name",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "The components of the user's real name."
}, {
  "path" : "name.familyName",
  "name" : "familyName",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Family name (last name in Western languages)."
}, {
  "path" : "name.formatted",
  "name" : "formatted",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Full name, including titles and suffixes, formatted for display."
}, {
  "path" : "name.givenName",
  "name" : "givenName",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Given name (first name in Western languages)."
}, {
  "path" : "name.honorificPrefix",
  "name" : "honorificPrefix",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Honorific prefix (title), e.g. 'Ms.'."
}, {
  "path" : "name.honorificSuffix",
  "name" : "honorificSuffix",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Honorific suffix, e.g. 'III'."
}, {
  "path" : "name.middleName",
  "name" : "middleName",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The middle name(s) of the User."
}, {
  "path" : "organization",
  "name" : "organization",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Identifies the name of an organization. \n\n**Allowed references**: Organization"
}, {
  "path" : "password",
  "name" : "password",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "The User's cleartext password, used for initial or reset scenarios."
}, {
  "path" : "phoneNumber",
  "name" : "phoneNumber",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Primary phone number."
}, {
  "path" : "phoneNumbers",
  "name" : "phoneNumbers",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "Phone numbers for the User."
}, {
  "path" : "phoneNumbers.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A human-readable name for display purposes (READ-ONLY)."
}, {
  "path" : "phoneNumbers.primary",
  "name" : "primary",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Indicates if this is the primary phone number."
}, {
  "path" : "phoneNumbers.type",
  "name" : "type",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A label for the phone number's function."
}, {
  "path" : "phoneNumbers.value",
  "name" : "value",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The user's phone number."
}, {
  "path" : "photo",
  "name" : "photo",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "uri",
  "desc" : "Primary photo for the user."
}, {
  "path" : "photos",
  "name" : "photos",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "URLs of photos of the user."
}, {
  "path" : "photos.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A human-readable name, primarily used for display purposes (READ-ONLY)."
}, {
  "path" : "photos.primary",
  "name" : "primary",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Indicates if this is the primary photo. Only one may be 'true'."
}, {
  "path" : "photos.type",
  "name" : "type",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A label indicating 'photo' or 'thumbnail'."
}, {
  "path" : "photos.value",
  "name" : "value",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "uri",
  "desc" : "URL of a photo of the User."
}, {
  "path" : "preferredLanguage",
  "name" : "preferredLanguage",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The User's preferred written or spoken language."
}, {
  "path" : "profileUrl",
  "name" : "profileUrl",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "uri",
  "desc" : "A fully qualified URL pointing to a page representing the User's online profile."
}, {
  "path" : "roles",
  "name" : "roles",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "A list of roles for the User that collectively represent who the User is (e.g. 'Student', 'Faculty')."
}, {
  "path" : "roles.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A human-readable name, primarily used for display purposes (READ-ONLY)."
}, {
  "path" : "roles.primary",
  "name" : "primary",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Indicates if this is the primary role. Only one may be 'true'."
}, {
  "path" : "roles.type",
  "name" : "type",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A label indicating the attribute's function."
}, {
  "path" : "roles.value",
  "name" : "value",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The value of a role."
}, {
  "path" : "securityLabel",
  "name" : "securityLabel",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "List of security labels associated to the user."
}, {
  "path" : "securityLabel.code",
  "name" : "code",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Code value."
}, {
  "path" : "securityLabel.system",
  "name" : "system",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Code system."
}, {
  "path" : "timezone",
  "name" : "timezone",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The User's time zone in the 'Olson' format."
}, {
  "path" : "title",
  "name" : "title",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The user's title, e.g. 'Vice President'."
}, {
  "path" : "twoFactor",
  "name" : "twoFactor",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Two factor settings for user."
}, {
  "path" : "twoFactor.enabled",
  "name" : "enabled",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Defines whether two-factor auth is currently enabled."
}, {
  "path" : "twoFactor.secretKey",
  "name" : "secretKey",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "TOTP Secret key."
}, {
  "path" : "twoFactor.transport",
  "name" : "transport",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Transport of 2FA confirmation code (if used)."
}, {
  "path" : "userName",
  "name" : "userName",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Unique identifier for the User, typically used to directly authenticate."
}, {
  "path" : "userType",
  "name" : "userType",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Identifies the relationship between the organization and the user."
}, {
  "path" : "x509Certificates",
  "name" : "x509Certificates",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "A list of certificates issued to the User."
}, {
  "path" : "x509Certificates.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A human-readable name, primarily used for display purposes (READ-ONLY)."
}, {
  "path" : "x509Certificates.primary",
  "name" : "primary",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Indicates if this is the primary certificate. Only one may be 'true'."
}, {
  "path" : "x509Certificates.type",
  "name" : "type",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A label indicating the certificate's function."
}, {
  "path" : "x509Certificates.value",
  "name" : "value",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "base64Binary",
  "desc" : "The value of an X.509 certificate (base64)."
} ]
```


## ViewDefinition

```fhir-structure
[ {
  "path" : "constant",
  "name" : "constant",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "Constant that can be used in FHIRPath expressions"
}, {
  "path" : "constant.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Name of constant (referred to in FHIRPath as %[name])"
}, {
  "path" : "constant.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "base64Binary",
  "desc" : "Value of constant"
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
  "desc" : "FHIR version(s) of the resource for the ViewDefinition"
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
  "desc" : "Name of view definition (computer and database friendly)"
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
  "desc" : "FHIR resource for the ViewDefinition"
}, {
  "path" : "select",
  "name" : "select",
  "lvl" : 0,
  "min" : 1,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "A collection of columns and nested selects to include in the view."
}, {
  "path" : "select.column",
  "name" : "column",
  "lvl" : 1,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "A column to be produced in the resulting table."
}, {
  "path" : "select.column.collection",
  "name" : "collection",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Indicates whether the column may have multiple values."
}, {
  "path" : "select.column.description",
  "name" : "description",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "markdown",
  "desc" : "Description of the column"
}, {
  "path" : "select.column.name",
  "name" : "name",
  "lvl" : 2,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Column name produced in the output"
}, {
  "path" : "select.column.path",
  "name" : "path",
  "lvl" : 2,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "FHIRPath expression that creates a column and defines its content"
}, {
  "path" : "select.column.tag",
  "name" : "tag",
  "lvl" : 2,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "Additional metadata describing the column"
}, {
  "path" : "select.column.tag.name",
  "name" : "name",
  "lvl" : 3,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Name of tag"
}, {
  "path" : "select.column.tag.value",
  "name" : "value",
  "lvl" : 3,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Value of tag"
}, {
  "path" : "select.column.type",
  "name" : "type",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "uri",
  "desc" : "A FHIR StructureDefinition URI for the column's type."
}, {
  "path" : "select.forEach",
  "name" : "forEach",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A FHIRPath expression to retrieve the parent element(s) used in the containing select. The default is effectively `$this`."
}, {
  "path" : "select.forEachOrNull",
  "name" : "forEachOrNull",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Same as forEach, but will produce a row with null values if the collection is empty."
}, {
  "path" : "select.repeat",
  "name" : "repeat",
  "lvl" : 1,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : ""
}, {
  "path" : "select.select",
  "name" : "select",
  "lvl" : 1,
  "min" : 0,
  "max" : "*",
  "type" : "",
  "desc" : "Nested select relative to a parent expression."
}, {
  "path" : "select.unionAll",
  "name" : "unionAll",
  "lvl" : 1,
  "min" : 0,
  "max" : "*",
  "type" : "",
  "desc" : "Creates a union of all rows in the given selection structures."
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
  "desc" : "Name for this view definition (human friendly)"
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
  "desc" : "A series of zero or more FHIRPath constraints to filter resources for the view."
}, {
  "path" : "where.description",
  "name" : "description",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "A human-readable description of the above where constraint."
}, {
  "path" : "where.path",
  "name" : "path",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "A FHIRPath expression defining a filter condition"
} ]
```


## WebPushSubscription

```fhir-structure
[ {
  "path" : "app",
  "name" : "app",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Application identifier for the subscription."
}, {
  "path" : "subscription",
  "name" : "subscription",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Web Push API subscription details."
}, {
  "path" : "subscription.endpoint",
  "name" : "endpoint",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "URL to which the push notification service should send notifications."
}, {
  "path" : "subscription.expirationTime",
  "name" : "expirationTime",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "decimal",
  "desc" : "Time in seconds when the subscription will expire."
}, {
  "path" : "subscription.keys",
  "name" : "keys",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Cryptographic keys needed for the push subscription."
}, {
  "path" : "subscription.keys.auth",
  "name" : "auth",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Authentication key for the subscription."
}, {
  "path" : "subscription.keys.p256dh",
  "name" : "p256dh",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Public key for the subscription (P-256 Diffie-Hellman)."
}, {
  "path" : "user",
  "name" : "user",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Reference to the user who owns this subscription."
}, {
  "path" : "user.id",
  "name" : "id",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "User identifier."
}, {
  "path" : "user.resourceType",
  "name" : "resourceType",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Fixed value indicating this is a reference to a User resource."
} ]
```


## aidboxtopicdestination-amqp-1-0-at-least-once

AMQP 1.0 at-least-once delivery profile for AidboxTopicDestination.

```fhir-structure
[ {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "integer",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "boolean",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "integer",
  "desc" : ""
} ]
```


## aidboxtopicdestination-amqp-rabbitmq-0-9-1

RabbitMQ AMQP 0-9-1 best-effort delivery profile for AidboxTopicDestination.

```fhir-structure
[ {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "integer",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "boolean",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "boolean",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
} ]
```


## aidboxtopicdestination-amqp-rabbitmq-0-9-1-at-least-once

RabbitMQ AMQP 0-9-1 at-least-once delivery profile for AidboxTopicDestination.

```fhir-structure
[ {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "integer",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "boolean",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "integer",
  "desc" : ""
} ]
```


## aidboxtopicdestination-aws-eventbridge-at-least-once

AWS EventBridge at-least-once delivery profile for AidboxTopicDestination.

```fhir-structure
[ {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "integer",
  "desc" : ""
} ]
```


## aidboxtopicdestination-aws-eventbridge-best-effort

AWS EventBridge best-effort delivery profile for AidboxTopicDestination.

```fhir-structure
[ {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
} ]
```


## aidboxtopicdestination-aws-sns-at-least-once

AWS SNS at-least-once delivery profile for AidboxTopicDestination.

```fhir-structure
[ {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "integer",
  "desc" : ""
} ]
```


## aidboxtopicdestination-aws-sns-best-effort

AWS SNS best-effort delivery profile for AidboxTopicDestination.

```fhir-structure
[ {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
} ]
```


## aidboxtopicdestination-clickhouse

ClickHouse best-effort delivery profile for AidboxTopicDestination.

```fhir-structure
[ {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
} ]
```


## aidboxtopicdestination-clickhouse-at-least-once

ClickHouse at-least-once delivery profile for AidboxTopicDestination.

```fhir-structure
[ {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "unsignedInt",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "unsignedInt",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
} ]
```


## aidboxtopicdestination-fhir-native-topic-based-subscription

FHIR native topic-based subscription profile for AidboxTopicDestination.

```fhir-structure
[ {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Time in seconds for which to store events. During this period, events will be available with the $events operation. If not specified, events will be stored indefinitely."
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Number of parallel senders which will handle subscriptions for the topic. Default is 4."
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "\n\n**Allowed values**: `R4-backported` | `R5` | `R4B-backported`"
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "canonical",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "unsignedInt",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "unsignedInt",
  "desc" : ""
} ]
```


## aidboxtopicdestination-gcp-pubsub-at-least-once

GCP Pub/Sub at-least-once delivery profile for AidboxTopicDestination.

```fhir-structure
[ {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "unsignedInt",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "unsignedInt",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "unsignedInt",
  "desc" : ""
} ]
```


## aidboxtopicdestination-kafka-at-least-once

Kafka at-least-once delivery profile for AidboxTopicDestination.

```fhir-structure
[ {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "unsignedInt",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "unsignedInt",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "unsignedInt",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "unsignedInt",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "unsignedInt",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
} ]
```


## aidboxtopicdestination-kafka-at-least-once-mock

Kafka at-least-once mock delivery profile for AidboxTopicDestination.

```fhir-structure
[ {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "unsignedInt",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "unsignedInt",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "unsignedInt",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "unsignedInt",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "unsignedInt",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "boolean",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "boolean",
  "desc" : ""
} ]
```


## aidboxtopicdestination-kafka-best-effort

Kafka best-effort delivery profile for AidboxTopicDestination.

```fhir-structure
[ {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "unsignedInt",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "unsignedInt",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "unsignedInt",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "unsignedInt",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "unsignedInt",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
} ]
```


## aidboxtopicdestination-kafka-best-effort-mock

Kafka best-effort mock delivery profile for AidboxTopicDestination.

```fhir-structure
[ {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "unsignedInt",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "unsignedInt",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "unsignedInt",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "unsignedInt",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "unsignedInt",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "boolean",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "boolean",
  "desc" : ""
} ]
```


## aidboxtopicdestination-nats-core-best-effort

NATS Core best-effort delivery profile for AidboxTopicDestination.

```fhir-structure
[ {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
} ]
```


## aidboxtopicdestination-nats-jetstream-at-least-once

NATS JetStream at-least-once delivery profile for AidboxTopicDestination.

```fhir-structure
[ {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
} ]
```


## aidboxtopicdestination-webhook-at-least-once

Webhook at-least-once delivery profile for AidboxTopicDestination.

```fhir-structure
[ {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.part",
  "name" : "part",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "url",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "unsignedInt",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "integer",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "unsignedInt",
  "desc" : ""
}, {
  "path" : "eter.value[x]",
  "name" : "value[x]",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : ""
} ]
```


## fhir-package-install-parameters

Profile for FHIR package installation parameters.

```fhir-structure
[ {
  "path" : "parameter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "parameter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : "*",
  "type" : "",
  "desc" : ""
}, {
  "path" : "parameter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "parameter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "parameter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
} ]
```


## fhir-package-uninstall-parameters

Profile for FHIR package uninstallation parameters.

```fhir-structure
[ {
  "path" : "parameter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "parameter",
  "name" : "parameter",
  "lvl" : 0,
  "min" : 1,
  "max" : "*",
  "type" : "",
  "desc" : ""
}, {
  "path" : "parameter.name",
  "name" : "name",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
} ]
```


## legacy-fce-extension

```fhir-structure
[ {
  "path" : "url",
  "name" : "url",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "value[x]",
  "name" : "value[x]",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
} ]
```


## orgbac-mode

```fhir-structure
[ {
  "path" : "url",
  "name" : "url",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "value[x]",
  "name" : "value[x]",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
} ]
```


## orgbac-organization-reference

```fhir-structure
[ {
  "path" : "url",
  "name" : "url",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "value[x]",
  "name" : "value[x]",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "\n\n**Allowed references**: Organization"
} ]
```


## password

```fhir-structure
[ {
  "path" : "value",
  "name" : "value",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "http://hl7.org/fhirpath/System.String",
  "desc" : "Primitive value for password"
} ]
```


## search-parameter-code-extension

```fhir-structure
[ {
  "path" : "url",
  "name" : "url",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : ""
}, {
  "path" : "value[x]",
  "name" : "value[x]",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "code",
  "desc" : ""
} ]
```


## secret

```fhir-structure
[ {
  "path" : "value",
  "name" : "value",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "http://hl7.org/fhirpath/System.String",
  "desc" : "Primitive value for secret"
} ]
```


## sha256Hash

```fhir-structure
[ {
  "path" : "value",
  "name" : "value",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "http://hl7.org/fhirpath/System.String",
  "desc" : "Primitive value for sha256Hash"
} ]
```


## ui_history

UI history tracking resource for Aidbox console.

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

UI snippet resource for saved queries and commands in Aidbox console.

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
  "desc" : "Collection the snippet belongs to."
}, {
  "path" : "command",
  "name" : "command",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Command or query content."
}, {
  "path" : "title",
  "name" : "title",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Title of the snippet."
}, {
  "path" : "type",
  "name" : "type",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of snippet. \n\n**Allowed values**: `http` | `sql`"
}, {
  "path" : "user",
  "name" : "user",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to the user who created the snippet. \n\n**Allowed references**: User"
} ]
```

