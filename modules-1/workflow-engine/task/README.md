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

## &#x20;Task Instance

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
