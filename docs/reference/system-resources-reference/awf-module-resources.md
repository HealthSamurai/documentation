# AWF Module Resources

Resources for configuration and management of Aidbox Workflows.

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
  "type" : "number",
  "desc" : "Maximum number of concurrent tasks allowed."
}, {
  "path" : "concurrencyPath",
  "name" : "concurrencyPath",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "keyword",
  "desc" : ""
}, {
  "path" : "definition",
  "name" : "definition",
  "lvl" : 0,
  "min" : 0,
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
  "type" : "number",
  "desc" : "Maximum duration in seconds that a task can remain in in-progress status before timing out."
}, {
  "path" : "label",
  "name" : "label",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
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
  "type" : "",
  "desc" : "Detailed reason for the task outcome."
}, {
  "path" : "outcomeReason.type",
  "name" : "type",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of outcome reason. \n\n**Allowed values**: `awf.task/failed-due-to-in-progress-timeout` | `awf.task/failed-by-executor` | `awf.executor/unknown-error`"
}, {
  "path" : "outcomeReason.message",
  "name" : "message",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Human-readable explanation of the outcome."
}, {
  "path" : "outcomeReason.data",
  "name" : "data",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Additional data related to the outcome."
}, {
  "path" : "params",
  "name" : "params",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Parameters required for task execution."
}, {
  "path" : "requestedToStartTimeout",
  "name" : "requestedToStartTimeout",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "number",
  "desc" : "Maximum duration in seconds that a task can remain in requested status before timing out."
}, {
  "path" : "requester",
  "name" : "requester",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Entity that requested the task."
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
  "path" : "requester.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Human-readable display name of the requester."
}, {
  "path" : "requester.service",
  "name" : "service",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Service that initiated the request."
}, {
  "path" : "requester.rule",
  "name" : "rule",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Rule that authorized the request."
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
  "type" : "number",
  "desc" : "Number of times the task has been retried."
}, {
  "path" : "retryDelay",
  "name" : "retryDelay",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "number",
  "desc" : "Delay in seconds before retrying a failed task."
}, {
  "path" : "status",
  "name" : "status",
  "lvl" : 0,
  "min" : 0,
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
  "type" : "",
  "desc" : "Definition of a rescheduled action."
}, {
  "path" : "re-scheduled.action",
  "name" : "action",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Name of the rescheduled action."
}, {
  "path" : "re-scheduled.id",
  "name" : "id",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Unique identifier for the rescheduled action."
}, {
  "path" : "re-scheduled.delay",
  "name" : "delay",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "number",
  "desc" : "New delay in seconds before executing the action."
}, {
  "path" : "re-scheduled.at",
  "name" : "at",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "New specific time when the action should be executed."
}, {
  "path" : "re-scheduled.action-params",
  "name" : "action-params",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Parameters for the rescheduled action."
}, {
  "path" : "scheduled",
  "name" : "scheduled",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Definition of a scheduled action."
}, {
  "path" : "scheduled.action",
  "name" : "action",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Name of the scheduled action."
}, {
  "path" : "scheduled.id",
  "name" : "id",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Unique identifier for the scheduled action."
}, {
  "path" : "scheduled.delay",
  "name" : "delay",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "number",
  "desc" : "Delay in seconds before executing the action."
}, {
  "path" : "scheduled.at",
  "name" : "at",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Specific time when the action should be executed."
}, {
  "path" : "scheduled.action-params",
  "name" : "action-params",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Object",
  "desc" : "Parameters for the scheduled action."
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
  "type" : "",
  "desc" : "Resource that is the subject of the action."
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
}, {
  "path" : "subject.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Human-readable display name of the subject."
} ]
```


## AidboxWorkflow

```fhir-structure
[ {
  "path" : "definition",
  "name" : "definition",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : ""
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
  "type" : "",
  "desc" : "Detailed reason for the workflow outcome."
}, {
  "path" : "outcomeReason.type",
  "name" : "type",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Type of outcome reason. \n\n**Allowed values**: `awf.workflow/failed-by-executor` | `awf.executor/unknown-error`"
}, {
  "path" : "outcomeReason.message",
  "name" : "message",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Human-readable explanation of the outcome."
}, {
  "path" : "outcomeReason.data",
  "name" : "data",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Additional data related to the outcome."
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
  "type" : "",
  "desc" : "Entity that requested the workflow."
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
  "path" : "requester.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Human-readable display name of the requester."
}, {
  "path" : "requester.service",
  "name" : "service",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Service that initiated the request."
}, {
  "path" : "requester.rule",
  "name" : "rule",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Rule that authorized the request."
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
  "type" : "number",
  "desc" : "Number of times the workflow has been retried."
}, {
  "path" : "status",
  "name" : "status",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Current status of the workflow. \n\n**Allowed values**: `created` | `in-progress` | `done`"
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
  "type" : "number",
  "desc" : "Maximum number of concurrent executions allowed for this rule."
}, {
  "path" : "concurrencyPath",
  "name" : "concurrencyPath",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "keyword",
  "desc" : "Path to the field used for concurrency control."
}, {
  "path" : "definition",
  "name" : "definition",
  "lvl" : 0,
  "min" : 0,
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
  "path" : "id",
  "name" : "id",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Unique identifier for this scheduler rule status."
}, {
  "path" : "inProgressTimeout",
  "name" : "inProgressTimeout",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "number",
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
  "type" : "",
  "desc" : "Reference to the last scheduled task for this rule."
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
  "path" : "lastScheduleReference.display",
  "name" : "display",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Human-readable display name of the last scheduled task."
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
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "Parameters required for rule execution."
}, {
  "path" : "requestedToStartTimeout",
  "name" : "requestedToStartTimeout",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "number",
  "desc" : "Maximum duration in seconds that a task can remain in requested status before timing out."
}, {
  "path" : "retryDelay",
  "name" : "retryDelay",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "number",
  "desc" : "Delay in seconds before retrying a failed rule execution."
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
  "desc" : ""
}, {
  "path" : "subscription",
  "name" : "subscription",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
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
  "type" : "number",
  "desc" : "Time in seconds when the subscription will expire."
}, {
  "path" : "subscription.keys",
  "name" : "keys",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "",
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
  "type" : "",
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

