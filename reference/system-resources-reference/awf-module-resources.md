# AWF Module Resources

Resources for configuration and management of Aidbox Workflows.

 ## Overview

AWF module includes the following resource types:

- AidboxTask
- AidboxTaskLog
- AidboxWorkflow
- SchedulerRuleStatus
- WebPushSubscription

## AidboxTask

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
<tr><td width="290">concurrencyLimit</td><td width="70">0..1</td><td width="150">number</td><td>Maximum number of concurrent tasks allowed.</td></tr>
<tr><td width="290">executeAt</td><td width="70">0..1</td><td width="150">dateTime</td><td>Scheduled time for task execution.</td></tr>
<tr><td width="290">requester</td><td width="70">0..1</td><td width="150"></td><td>Entity that requested the task.</td></tr>
<tr><td width="290">requester.<strong>id</strong></td><td width="70">0..1</td><td width="150">string</td><td>Identifier of the requester.</td></tr>
<tr><td width="290">requester.<strong>resourceType</strong></td><td width="70">0..1</td><td width="150">string</td><td>Type of resource that made the request.</td></tr>
<tr><td width="290">requester.<strong>display</strong></td><td width="70">0..1</td><td width="150">string</td><td>Human-readable display name of the requester.</td></tr>
<tr><td width="290">requester.<strong>service</strong></td><td width="70">0..1</td><td width="150">string</td><td>Service that initiated the request.</td></tr>
<tr><td width="290">requester.<strong>rule</strong></td><td width="70">0..1</td><td width="150">string</td><td>Rule that authorized the request.</td></tr>
<tr><td width="290">params</td><td width="70">0..1</td><td width="150"></td><td>Parameters required for task execution.</td></tr>
<tr><td width="290">workflow-definition</td><td width="70">0..1</td><td width="150">string</td><td>Reference to the workflow definition for this task.</td></tr>
<tr><td width="290">label</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">error</td><td width="70">0..1</td><td width="150"></td><td>Error details if task failed.</td></tr>
<tr><td width="290">retryCount</td><td width="70">0..1</td><td width="150">number</td><td>Number of times the task has been retried.</td></tr>
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td>Current status of the task. 

<strong>Allowed values</strong>: created | ready | requested | in-progress | done | waiting</td></tr>
<tr><td width="290">retryDelay</td><td width="70">0..1</td><td width="150">number</td><td>Delay in seconds before retrying a failed task.</td></tr>
<tr><td width="290">execId</td><td width="70">0..1</td><td width="150">string</td><td>Unique execution identifier.</td></tr>
<tr><td width="290">definition</td><td width="70">0..1</td><td width="150">string</td><td>Identifier for the task definition.</td></tr>
<tr><td width="290">allowedRetryCount</td><td width="70">0..1</td><td width="150">integer</td><td>Maximum number of retries allowed for this task.</td></tr>
<tr><td width="290">inProgressTimeout</td><td width="70">0..1</td><td width="150">number</td><td>Maximum duration in seconds that a task can remain in in-progress status before timing out.</td></tr>
<tr><td width="290">outcomeReason</td><td width="70">0..1</td><td width="150"></td><td>Detailed reason for the task outcome.</td></tr>
<tr><td width="290">outcomeReason.<strong>type</strong></td><td width="70">0..1</td><td width="150">string</td><td>Type of outcome reason. 

<strong>Allowed values</strong>: awf.task/failed-due-to-in-progress-timeout | awf.task/failed-by-executor | awf.executor/unknown-error</td></tr>
<tr><td width="290">outcomeReason.<strong>message</strong></td><td width="70">0..1</td><td width="150">string</td><td>Human-readable explanation of the outcome.</td></tr>
<tr><td width="290">outcomeReason.<strong>data</strong></td><td width="70">0..1</td><td width="150"></td><td>Additional data related to the outcome.</td></tr>
<tr><td width="290">concurrencyPath</td><td width="70">0..*</td><td width="150">keyword</td><td></td></tr>
<tr><td width="290">requestedToStartTimeout</td><td width="70">0..1</td><td width="150">number</td><td>Maximum duration in seconds that a task can remain in requested status before timing out.</td></tr>
<tr><td width="290">outcome</td><td width="70">0..1</td><td width="150">string</td><td>Final outcome of the task execution. 

<strong>Allowed values</strong>: succeeded | failed | canceled</td></tr>
<tr><td width="290">result</td><td width="70">0..1</td><td width="150"></td><td>Result data produced by successful task execution.</td></tr></tbody>
</table>


## AidboxTaskLog

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
<tr><td width="290">status-before</td><td width="70">0..1</td><td width="150">string</td><td>Task status before the action was performed.</td></tr>
<tr><td width="290">status-after</td><td width="70">0..1</td><td width="150">string</td><td>Task status after the action was performed.</td></tr>
<tr><td width="290">action-params</td><td width="70">0..1</td><td width="150">Object</td><td>Parameters provided with the action.</td></tr>
<tr><td width="290">scheduled</td><td width="70">0..1</td><td width="150"></td><td>Definition of a scheduled action.</td></tr>
<tr><td width="290">scheduled.<strong>action</strong></td><td width="70">0..1</td><td width="150">string</td><td>Name of the scheduled action.</td></tr>
<tr><td width="290">scheduled.<strong>id</strong></td><td width="70">0..1</td><td width="150">string</td><td>Unique identifier for the scheduled action.</td></tr>
<tr><td width="290">scheduled.<strong>delay</strong></td><td width="70">0..1</td><td width="150">number</td><td>Delay in seconds before executing the action.</td></tr>
<tr><td width="290">scheduled.<strong>at</strong></td><td width="70">0..1</td><td width="150">dateTime</td><td>Specific time when the action should be executed.</td></tr>
<tr><td width="290">scheduled.<strong>action-params</strong></td><td width="70">0..1</td><td width="150">Object</td><td>Parameters for the scheduled action.</td></tr>
<tr><td width="290">action</td><td width="70">0..1</td><td width="150">string</td><td>Name of the performed action.</td></tr>
<tr><td width="290">subject</td><td width="70">0..1</td><td width="150"></td><td>Resource that is the subject of the action.</td></tr>
<tr><td width="290">subject.<strong>id</strong></td><td width="70">0..1</td><td width="150">string</td><td>Identifier of the subject.</td></tr>
<tr><td width="290">subject.<strong>resourceType</strong></td><td width="70">0..1</td><td width="150">string</td><td>Type of resource that is the subject.</td></tr>
<tr><td width="290">subject.<strong>display</strong></td><td width="70">0..1</td><td width="150">string</td><td>Human-readable display name of the subject.</td></tr>
<tr><td width="290">re-scheduled</td><td width="70">0..1</td><td width="150"></td><td>Definition of a rescheduled action.</td></tr>
<tr><td width="290">re-scheduled.<strong>action</strong></td><td width="70">0..1</td><td width="150">string</td><td>Name of the rescheduled action.</td></tr>
<tr><td width="290">re-scheduled.<strong>id</strong></td><td width="70">0..1</td><td width="150">string</td><td>Unique identifier for the rescheduled action.</td></tr>
<tr><td width="290">re-scheduled.<strong>delay</strong></td><td width="70">0..1</td><td width="150">number</td><td>New delay in seconds before executing the action.</td></tr>
<tr><td width="290">re-scheduled.<strong>at</strong></td><td width="70">0..1</td><td width="150">dateTime</td><td>New specific time when the action should be executed.</td></tr>
<tr><td width="290">re-scheduled.<strong>action-params</strong></td><td width="70">0..1</td><td width="150">Object</td><td>Parameters for the rescheduled action.</td></tr></tbody>
</table>


## AidboxWorkflow

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
<tr><td width="290">executeAt</td><td width="70">0..1</td><td width="150">string</td><td>Scheduled time for workflow execution.</td></tr>
<tr><td width="290">requester</td><td width="70">0..1</td><td width="150"></td><td>Entity that requested the workflow.</td></tr>
<tr><td width="290">requester.<strong>id</strong></td><td width="70">0..1</td><td width="150">string</td><td>Identifier of the requester.</td></tr>
<tr><td width="290">requester.<strong>resourceType</strong></td><td width="70">0..1</td><td width="150">string</td><td>Type of resource that made the request.</td></tr>
<tr><td width="290">requester.<strong>display</strong></td><td width="70">0..1</td><td width="150">string</td><td>Human-readable display name of the requester.</td></tr>
<tr><td width="290">requester.<strong>service</strong></td><td width="70">0..1</td><td width="150">string</td><td>Service that initiated the request.</td></tr>
<tr><td width="290">requester.<strong>rule</strong></td><td width="70">0..1</td><td width="150">string</td><td>Rule that authorized the request.</td></tr>
<tr><td width="290">params</td><td width="70">0..1</td><td width="150"></td><td>Parameters required for workflow execution.</td></tr>
<tr><td width="290">label</td><td width="70">0..1</td><td width="150">string</td><td>Human-readable label for the workflow.</td></tr>
<tr><td width="290">error</td><td width="70">0..1</td><td width="150"></td><td>Error details if workflow failed.</td></tr>
<tr><td width="290">retryCount</td><td width="70">0..1</td><td width="150">number</td><td>Number of times the workflow has been retried.</td></tr>
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td>Current status of the workflow. 

<strong>Allowed values</strong>: created | in-progress | done</td></tr>
<tr><td width="290">execId</td><td width="70">0..1</td><td width="150">string</td><td>Unique execution identifier.</td></tr>
<tr><td width="290">definition</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">outcomeReason</td><td width="70">0..1</td><td width="150"></td><td>Detailed reason for the workflow outcome.</td></tr>
<tr><td width="290">outcomeReason.<strong>type</strong></td><td width="70">0..1</td><td width="150">string</td><td>Type of outcome reason. 

<strong>Allowed values</strong>: awf.workflow/failed-by-executor | awf.executor/unknown-error</td></tr>
<tr><td width="290">outcomeReason.<strong>message</strong></td><td width="70">0..1</td><td width="150">string</td><td>Human-readable explanation of the outcome.</td></tr>
<tr><td width="290">outcomeReason.<strong>data</strong></td><td width="70">0..1</td><td width="150"></td><td>Additional data related to the outcome.</td></tr>
<tr><td width="290">outcome</td><td width="70">0..1</td><td width="150">string</td><td>Final outcome of the workflow execution. 

<strong>Allowed values</strong>: succeeded | failed | canceled</td></tr>
<tr><td width="290">result</td><td width="70">0..1</td><td width="150"></td><td>Result data produced by successful workflow execution.</td></tr></tbody>
</table>


## SchedulerRuleStatus

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
<tr><td width="290">concurrencyLimit</td><td width="70">0..1</td><td width="150">number</td><td>Maximum number of concurrent executions allowed for this rule.</td></tr>
<tr><td width="290">executeAt</td><td width="70">0..1</td><td width="150">dateTime</td><td>Next scheduled execution time for this rule.</td></tr>
<tr><td width="290">params</td><td width="70">0..1</td><td width="150"></td><td>Parameters required for rule execution.</td></tr>
<tr><td width="290">label</td><td width="70">0..1</td><td width="150">string</td><td>Human-readable label for the scheduler rule.</td></tr>
<tr><td width="290">lastScheduleReference</td><td width="70">0..1</td><td width="150"></td><td>Reference to the last scheduled task for this rule.</td></tr>
<tr><td width="290">lastScheduleReference.<strong>id</strong></td><td width="70">0..1</td><td width="150">string</td><td>Identifier of the last scheduled task.</td></tr>
<tr><td width="290">lastScheduleReference.<strong>resourceType</strong></td><td width="70">0..1</td><td width="150">string</td><td>Type of resource that was last scheduled.</td></tr>
<tr><td width="290">lastScheduleReference.<strong>display</strong></td><td width="70">0..1</td><td width="150">string</td><td>Human-readable display name of the last scheduled task.</td></tr>
<tr><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td>Unique identifier for this scheduler rule status.</td></tr>
<tr><td width="290">lastScheduleStatus</td><td width="70">0..1</td><td width="150">string</td><td>Status of the last schedule attempt. 

<strong>Allowed values</strong>: started | skipped</td></tr>
<tr><td width="290">retryDelay</td><td width="70">0..1</td><td width="150">number</td><td>Delay in seconds before retrying a failed rule execution.</td></tr>
<tr><td width="290">definition</td><td width="70">0..1</td><td width="150">string</td><td>Identifier for the scheduler rule definition.</td></tr>
<tr><td width="290">allowedRetryCount</td><td width="70">0..1</td><td width="150">integer</td><td>Maximum number of retries allowed for this rule.</td></tr>
<tr><td width="290">inProgressTimeout</td><td width="70">0..1</td><td width="150">number</td><td>Maximum duration in seconds that a task can remain in in-progress status before timing out.</td></tr>
<tr><td width="290">concurrencyPath</td><td width="70">0..*</td><td width="150">keyword</td><td>Path to the field used for concurrency control.</td></tr>
<tr><td width="290">lastSchedule</td><td width="70">0..1</td><td width="150">dateTime</td><td>Timestamp of the last schedule attempt.</td></tr>
<tr><td width="290">requestedToStartTimeout</td><td width="70">0..1</td><td width="150">number</td><td>Maximum duration in seconds that a task can remain in requested status before timing out.</td></tr></tbody>
</table>


## WebPushSubscription

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
<tr><td width="290">subscription</td><td width="70">0..1</td><td width="150"></td><td>Web Push API subscription details.</td></tr>
<tr><td width="290">subscription.<strong>endpoint</strong></td><td width="70">0..1</td><td width="150">string</td><td>URL to which the push notification service should send notifications.</td></tr>
<tr><td width="290">subscription.<strong>expirationTime</strong></td><td width="70">0..1</td><td width="150">number</td><td>Time in seconds when the subscription will expire.</td></tr>
<tr><td width="290">subscription.<strong>keys</strong></td><td width="70">0..1</td><td width="150"></td><td>Cryptographic keys needed for the push subscription.</td></tr>
<tr><td width="290">subscription.<strong>keys</strong>.<strong>auth</strong></td><td width="70">0..1</td><td width="150">string</td><td>Authentication key for the subscription.</td></tr>
<tr><td width="290">subscription.<strong>keys</strong>.<strong>p256dh</strong></td><td width="70">0..1</td><td width="150">string</td><td>Public key for the subscription (P-256 Diffie-Hellman).</td></tr>
<tr><td width="290">user</td><td width="70">0..1</td><td width="150"></td><td>Reference to the user who owns this subscription.</td></tr>
<tr><td width="290">user.<strong>id</strong></td><td width="70">0..1</td><td width="150">string</td><td>User identifier.</td></tr>
<tr><td width="290">user.<strong>resourceType</strong></td><td width="70">0..1</td><td width="150"></td><td>Fixed value indicating this is a reference to a User resource.</td></tr>
<tr><td width="290">app</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
</table>

