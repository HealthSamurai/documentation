# Task

## Task Introduction

**Tasks** are atomic actions **asynchronously** executed by Aidbox or by an external executor. Tasks can be used as stand-alone operations or as part of an [Aidbox Workflow](../workflow/). It allows async operations to be more reliable, to continue work after restarts, and handle errors correctly. A typical example of task usage is asynchronous sending email or transforming a resource.

## Aidbox predefined Tasks

Aidbox provides several predefined tasks for routine jobs that can be called via [User API](./#task-user-api) or [task-related services](../services.md).

<details>

<summary>Predefined tasks</summary>

* &#x20;**Special tasks:**
  * [**Decision task**](aidbox-predefined-tasks.md#decision-task) - a task used to implement an [Aidbox Workflow](../workflow/).
  * [**Subscription task**](aidbox-predefined-tasks.md#subscription-task) - a task that waits for the resource that meets specified criteria.

<!---->

* **Self-contained tasks:**
  * [**Import resource task**](aidbox-predefined-tasks.md#import-resource-task) - a task that allows loading  `.ndjson` files from AWS or GCP cloud into Aidbox

</details>

## Task Instance

When a new task is created  by [task-user-api.md](task-user-api.md "mention") or by [services.md](../services.md "mention") or [workflow](../workflow/ "mention"), new resource `AidboxTask` is created which stores task Params, Result, and Status, as well as some additional information regarding task execution. Bellow is an example of AidboxTask with fields explanation:

```clojure
{
 ;; resource or service who requested task execution
 :requester       {:resourceType "User" :id "admin"}

 ;; number of finished attempts to execute tasks
 :retryCount      1

 ;; UUID which is assigned to a task, whenever task is started
 ;; Executors should include it ina  requests
 :execId         "44bc2-aac6-49a1-91e7-7f9fa29afe21"

 ;; Current status of the Task.
 ;; Possible values are: "created" "waiting" "ready" "requested" "in-progress" "done"
 :status          "done"

 ;; Outcome of the task execution, only available when task status is "done"
 ;; Possible values are: "succeeded" "failed" "canceled"
 :outcome         "succeeded"

 ;; Only filled when outcome is "failed"
 :outcomeReason   {;; Type of "fail"
                   ;; Possible values are:
                   ;; "awf.task/failed-by-executor" - executor called action awf.task/fail, error is validated with definition schema
                   ;; "awf.executor/unknown-error" - unexpected error happened
                   ;; "awf.task/failed-due-to-in-progress-timeout"
                   :type "awf.task/failed-by-executor"
                   :message "Failed by executor"
                   :data {}}

 ;; Params of the task, as specified by caller, should be valid to the schema of the definition
 :params          {}

 ;; Result of the task, should be valid to the schema of the definition, only present for a task with :outcame "succeeded"
 :result          {}

 ;; Error of the task, should be valid to the schema of the definition, only present for a task with :outcame "failed"
 :error           {}

 ;; Id of the task, generated if not supplied. Should be unique across the system.
 :id  "ea82769d-e083-461c-8cb0-3427ed466f19"

 ;; Human or machine-readable description of task instance.
 :label "import-resources-aws-my-bucket"

 ;; Symbol of the task definition in Aidbox Project,
 ;; works as the unique name of the Task that resonates with its function
 :definition  "aidbox.bulk/import-resource-task"

 ;; Time when a task becomes "ready". Could be specified by the caller, or set by the engine on task retry.
 :executeAt "2023-04-24T09:30:51.562261Z"
 }
```



<figure><img src="../../../.gitbook/assets/image (23).png" alt="" width="375"><figcaption><p>Task lifecycle</p></figcaption></figure>

After creation task will become either `waiting` or `ready`. Executors will poll for a task in `ready` status, and execute them using [#task-executor-api](./#task-executor-api "mention").

Task instances could be created in multiple ways, for example by [#awf.task-create-and-execute](./#awf.task-create-and-execute "mention")RPC operation, or by [services.md](../services.md "mention").&#x20;

## Task User API

[Task User API](task-user-api.md) allows users to manually control Aidbox tasks by [RPC methods](../../../api-1/rpc-api.md).&#x20;

<details>

<summary>API methods</summary>

* [`awf.task/create-and-execute`](task-user-api.md#awf.task-create-and-execute) - Creates an instance of a defined task and makes it ready to be executed immediately or at a specified time.
* [`awf.task/status`](task-user-api.md#awf.task-status) - Returns the status of a task instance with the specified id.
* [`awf.task/cancel`](task-user-api.md#awf.task-cancel) - Cancels execution of a created task instance.
* [`awf.task/list`](task-user-api.md#awf.task-list) - Returns the list of all tasks.

</details>

## Task Implementation

[Task Executor API](task-executor-api.md) is designed to allow implement task executor in any programming language and use it in Aidbox via REST requests with [RPC calls](../../../api-1/rpc-api.md).

<details>

<summary>API methods</summary>

[`awf.task/poll`](task-executor-api.md#awf.task-poll) - Fetches a new task from the queue and moves its status from `ready` to `requested`, immediately returns an empty array if there are no tasks in the queue.

[`awf.task/long-poll`](task-executor-api.md#awf.task-long-poll) - Fetches a new task from the queue and moves its status from `ready` to `requested`, waits until timeout unless there will be a new task, and after that returns an empty array.&#x20;

[`awf.task/start`](task-executor-api.md#awf.task-start) - Moves a task status from `requested` to `in-progress` and start its execution.

[`awf.task/fail`](task-executor-api.md#awf.task-fail) - Moves a task status from `in-progress` to `done` with outcome `failed`.

[`awf.task/success`](task-executor-api.md#awf.task-success) - Move a task status from `in-progress`  to `done` with outcome `succeeded`.

[`awf.task/notify`](task-executor-api.md#awf.task-notify) - Notifies task service that a task is still alive.

</details>

<figure><img src="../../../.gitbook/assets/image (34).png" alt=""><figcaption><p>Task execution process</p></figcaption></figure>
