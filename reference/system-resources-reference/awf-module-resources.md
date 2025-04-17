# AWF Module Resources

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
<tr><td width="290">concurrencyLimit</td><td width="70">0..1</td><td width="150">number</td><td></td></tr>
<tr><td width="290">executeAt</td><td width="70">0..1</td><td width="150">dateTime</td><td></td></tr>
<tr><td width="290">requester</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">requester.id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">requester.resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">requester.display</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">requester.service</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">requester.rule</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">params</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">workflow-definition</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">label</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">error</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">retryCount</td><td width="70">0..1</td><td width="150">number</td><td></td></tr>
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td><b>Allowed values</b>: created | ready | requested | in-progress | done | waiting</td></tr>
<tr><td width="290">retryDelay</td><td width="70">0..1</td><td width="150">number</td><td></td></tr>
<tr><td width="290">execId</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">definition</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">allowedRetryCount</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">meta</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">inProgressTimeout</td><td width="70">0..1</td><td width="150">number</td><td></td></tr>
<tr><td width="290">outcomeReason</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">outcomeReason.type</td><td width="70">0..1</td><td width="150">string</td><td><b>Allowed values</b>: awf.task/failed-due-to-in-progress-timeout | awf.task/failed-by-executor | awf.executor/unknown-error</td></tr>
<tr><td width="290">outcomeReason.message</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">outcomeReason.data</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">concurrencyPath</td><td width="70">0..*</td><td width="150">keyword</td><td></td></tr>
<tr><td width="290">requestedToStartTimeout</td><td width="70">0..1</td><td width="150">number</td><td></td></tr>
<tr><td width="290">outcome</td><td width="70">0..1</td><td width="150">string</td><td><b>Allowed values</b>: succeeded | failed | canceled</td></tr>
<tr><td width="290">result</td><td width="70">0..1</td><td width="150"></td><td></td></tr></tbody>
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
<tr><td width="290">status-after</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">status-before</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">scheduled</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">scheduled.action</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">scheduled.id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">scheduled.delay</td><td width="70">0..1</td><td width="150">number</td><td></td></tr>
<tr><td width="290">scheduled.at</td><td width="70">0..1</td><td width="150">dateTime</td><td></td></tr>
<tr><td width="290">scheduled.action-params</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">action</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">subject</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">subject.id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">subject.resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">subject.display</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">action-params</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">meta</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">re-scheduled</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">re-scheduled.action</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">re-scheduled.id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">re-scheduled.delay</td><td width="70">0..1</td><td width="150">number</td><td></td></tr>
<tr><td width="290">re-scheduled.at</td><td width="70">0..1</td><td width="150">dateTime</td><td></td></tr>
<tr><td width="290">re-scheduled.action-params</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr></tbody>
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
<tr><td width="290">executeAt</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">requester</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">requester.id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">requester.resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">requester.display</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">requester.service</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">requester.rule</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">params</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">label</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">error</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">retryCount</td><td width="70">0..1</td><td width="150">number</td><td></td></tr>
<tr><td width="290">status</td><td width="70">0..1</td><td width="150">string</td><td><b>Allowed values</b>: created | in-progress | done</td></tr>
<tr><td width="290">execId</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">definition</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">meta</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">outcomeReason</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">outcomeReason.type</td><td width="70">0..1</td><td width="150">string</td><td><b>Allowed values</b>: awf.workflow/failed-by-executor | awf.executor/unknown-error</td></tr>
<tr><td width="290">outcomeReason.message</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">outcomeReason.data</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">outcome</td><td width="70">0..1</td><td width="150">string</td><td><b>Allowed values</b>: succeeded | failed | canceled</td></tr>
<tr><td width="290">result</td><td width="70">0..1</td><td width="150"></td><td></td></tr></tbody>
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
<tr><td width="290">concurrencyLimit</td><td width="70">0..1</td><td width="150">number</td><td></td></tr>
<tr><td width="290">executeAt</td><td width="70">0..1</td><td width="150">dateTime</td><td></td></tr>
<tr><td width="290">params</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">label</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">lastScheduleReference</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">lastScheduleReference.id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">lastScheduleReference.resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">lastScheduleReference.display</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">lastScheduleStatus</td><td width="70">0..1</td><td width="150">string</td><td><b>Allowed values</b>: started | skipped</td></tr>
<tr><td width="290">retryDelay</td><td width="70">0..1</td><td width="150">number</td><td></td></tr>
<tr><td width="290">definition</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">allowedRetryCount</td><td width="70">0..1</td><td width="150">integer</td><td></td></tr>
<tr><td width="290">meta</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">inProgressTimeout</td><td width="70">0..1</td><td width="150">number</td><td></td></tr>
<tr><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">concurrencyPath</td><td width="70">0..*</td><td width="150">keyword</td><td></td></tr>
<tr><td width="290">lastSchedule</td><td width="70">0..1</td><td width="150">dateTime</td><td></td></tr>
<tr><td width="290">requestedToStartTimeout</td><td width="70">0..1</td><td width="150">number</td><td></td></tr></tbody>
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
<tr><td width="290">resourceType</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">meta</td><td width="70">0..1</td><td width="150">Object</td><td></td></tr>
<tr><td width="290">subscription</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">subscription.endpoint</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">subscription.expirationTime</td><td width="70">0..1</td><td width="150">number</td><td></td></tr>
<tr><td width="290">subscription.keys</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">subscription.keys.auth</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">subscription.keys.p256dh</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">user</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">user.id</td><td width="70">0..1</td><td width="150">string</td><td></td></tr>
<tr><td width="290">user.resourceType</td><td width="70">0..1</td><td width="150"></td><td></td></tr>
<tr><td width="290">app</td><td width="70">0..1</td><td width="150">string</td><td></td></tr></tbody>
</table>

