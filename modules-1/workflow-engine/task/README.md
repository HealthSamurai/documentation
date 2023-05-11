# Task

## Introduction

**Tasks** are atomic actions **asynchronously** executed by Aidbox or by an external executor. Tasks can be used as stand-alone operations or as part of an [Aidbox Workflow](../workflow/). It allows async operations to be more reliable, to continue work after restarts, and handle errors correctly. A typical example of task usage is asynchronous sending email or transforming a resource.

## Aidbox predefined Tasks

Aidbox provides several predefined tasks for routine jobs that can be called via [User API](./#task-user-api) or [task-related services](../services.md). They are executed in aidbox runtime, and available from the box.

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

 ;; Result of the task, should be valid to the schema of the definition, only present for a task with :outcome "succeeded"
 :result          {}

 ;; Error of the task, should be valid to the schema of the definition, only present for a task with :outcome "failed"
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



#### Task Statuses and Outcomes

Below is a representation of a Task Instance life cycle.

<div align="center">

<figure><img src="../../../.gitbook/assets/image (23).png" alt="" width="375"><figcaption><p>Task lifecycle</p></figcaption></figure>

</div>

After the creation of tasks, their status will be changed by Task Service to `ready`, `waiting`, or `status`, depending on the `executeAt` field.

Tasks in the status `ready` could be pulled by executors. (Either internal or external)

If the task failed and retry attempts are available, its status will be changed back to `waiting` again.

Finally, the status of tasks is always changed to `done`, either by an executor, a user on cancel, or Task Service on timeout, with one of the outcomes: `succeeded` , `failed` or `canceled.`

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

`OLD` To add a custom task, a definition for it should be added to Aidbox Project, and the executor implemented with Executor API.

### Task Definition

The first step for implementing a new custom task is to describe its definition in [aidbox-zen-lang-project](../../../aidbox-configuration/aidbox-zen-lang-project/ "mention").&#x20;

Task Definition contains all the information necessary to define the behavior of a task instance.

Below is an example of Aidbox Project namespace with a new task definition.

```clojure
```

### Executor Implementation&#x20;

<figure><img src="../../../.gitbook/assets/Workflow &#x26; Task Runtime.png" alt=""><figcaption><p>Task Executor API</p></figcaption></figure>

[Task Executor API](task-executor-api.md) is designed to implement task executor in any programming language and use it in Aidbox via REST requests with [RPC calls](../../../api-1/rpc-api.md).

<details>

<summary>API methods</summary>

[`awf.task/poll`](task-executor-api.md#awf.task-poll) - Fetches a new task from the queue and moves its status from `ready` to `requested`, immediately returns an empty array if there are no tasks in the queue.

[`awf.task/long-poll`](task-executor-api.md#awf.task-long-poll) - Fetches a new task from the queue and moves its status from `ready` to `requested`, waits until timeout unless there will be a new task, and after that returns an empty array.&#x20;

[`awf.task/start`](task-executor-api.md#awf.task-start) - Changes a task status from `requested` to `in-progress` and start its execution.

[`awf.task/fail`](task-executor-api.md#awf.task-fail) - Changes a task status from `in-progress` to `done` with outcome `failed`.

[`awf.task/success`](task-executor-api.md#awf.task-success) - Changes a task status from `in-progress`  to `done` with outcome `succeeded`.

[`awf.task/notify`](task-executor-api.md#awf.task-notify) - Notifies task service that a task is still alive.

</details>







`NEW`&#x20;

## Task Implementation

To add custom tasks, the following steps will be needed.

1. Implement Task Executor in any programming language or use SDK
2. Specify Task Definition in Aidbox configuration project
3. Implement particular tasks  in any programming language&#x20;

### 1. Implement Task Executor

{% hint style="info" %}
We are now preparing Aidbox Workflow/Task SDK. By using it, you can probably skip this step if you use one of the following languages: **typescript**, **python**, or **.NET**.
{% endhint %}

Aidbox has its own Task Executor that handles task execution and communicates with Task Service.

To perform custom tasks, you must have your own executor service.&#x20;

The executor can be implemented in any programming language by using [Task Executor API](task-executor-api.md), according to the following diagram, which shows how Aidbox Task Executor works.

&#x20;

<figure><img src="../../../.gitbook/assets/Workflow &#x26; Task Runtime.png" alt=""><figcaption><p>Aidbox Task Executor</p></figcaption></figure>

At first, when a worker gets available, the executor needs to send a **`awf.task/long-poll`** API request to Task Service. This request changes the status of a task from `ready` to `requested`, if there are tasks in `ready`, and returns that task.

Then, the executor should send a **`awf.task/start`** request to change the task status to `in-progress`. After receiving the response, the executor runs the task you implement in step 3.

Finally, if the execution is successful, the executor must send a **`awf.task/success`** request, otherwise **`awf.task/fail`**. They both change the status of the task to `done` and the former sets the outcome to `succeeded`, the latter to `failed`.

For long tasks,  **`awf.task/notify`** requests should be sent during their execution to tell Task Service that the worker continues processing and it extends the inProgressTimeout value. Otherwise, Task Service may force those tasks to terminate automatically.

All [Task Executor API](task-executor-api.md) methods are listed below.

<details>

<summary>API methods</summary>

[`awf.task/poll`](task-executor-api.md#awf.task-poll) - Fetches a new task from the queue and changes its status from `ready` to `requested`, immediately returns an empty array if there are no tasks in the queue.

[`awf.task/long-poll`](task-executor-api.md#awf.task-long-poll) - Fetches a new task from the queue and changes its status from `ready` to `requested`, waits until timeout unless there will be a new task, and after that returns an empty array.&#x20;

[`awf.task/start`](task-executor-api.md#awf.task-start) - Changes the status of a task from `requested` to `in-progress` and start its execution.

[`awf.task/notify`](task-executor-api.md#awf.task-notify) - Notifies Task Service that a task is still alive.

[`awf.task/success`](task-executor-api.md#awf.task-success) - Changes the status of a task from `in-progress`  to `done` with outcome `succeeded`.

[`awf.task/fail`](task-executor-api.md#awf.task-fail) - Changes the status of a task from `in-progress` to `done` with outcome `failed`.

</details>



### 2. Specify Task Definition

You need to specify definitions of custom tasks in [aidbox-zen-lang-project](../../../aidbox-configuration/aidbox-zen-lang-project/ "mention") in order to run them with the executor.&#x20;

Task Definition contains all the information necessary to define the behavior of a task instance.

Below is an example of Aidbox Project namespace with a new task definition.

```clojure
{ns     my-tasks
 ;; To define tasks, "awf.task" namespace should be imported
 import #{awf.task}

 ;; Definition of Pool
 ;; `Pool` is a logical entity that provides a way of scoping tasks in Aidbox.
 ;; Each task and executor must be specified to belong to a pool in its definition.
 ;; After the creation of tasks, they will be registered to the specified pools,
 ;; grouped within a pool and waiting there in line,
 ;; from where executors registered those pools fetch tasks in turn.
 ;; The configuration of task instance behavior should be specified 
 ;; in the scope of a pool as follows
 my-tasks-pool
 {
  ;; The tag "awf.task/pool" must be set to specify that this is a pool definition
  :zen/tags #{awf.task/pool}

  ;; Time limit in ms during which tasks can be in the status "requested"
  ;; without switching to the status "started"
  :requestedToStartTimeout 300

  ;; Time limit in ms during which tasks can be in the status "in-progress"
  :inProgressTimeout 800

  ;; Allowed number of attempts for retry in case the execution fails
  :allowedRetryCount 1

  ;; Delay time in ms before each retry
  :retryDelay 700
 }


 ;; Task Definition
 ;; The name ("example-task") will be used to execute this task
 example-task
 {
  ;; The following tags must be set for task definitions
  :zen/tags #{awf.task/definition zen/schema}

  ;; Every task definition has a hash-map (dictionary-like) data structure
  :type zen/map

  ;; Must be registered to a pool
  :pool my-tasks-pool

  ;; You can override the pool configuration (see above) for certain tasks
  :allowedRetryCount 2

  ;; Define the schema of input, output, and error for task execution
  :keys {
         ;; Input parameters
         :params {
                  ;; Should be defined in the form of hash-map (dictionary)
                  :type zen/map

                  ;; Set the keys specified below as the mandatory fields if needed
                  :require #{:arg-required}

                  ;; Specify the names of keys with their types
                  :keys {:arg-required {:type zen/integer}
                         :arg-optional {:type zen/integer}}
                  }

         ;; The return value in case of errors
         :error {
                 ;; Should be defined in the form of hash-map (dictionary)
                 :type zen/map

                 ;; Set the keys specified below as the mandatory fields if needed
                 :require #{:my-message}

                 ;; Specify the names of keys with their types
                 :keys {:my-message {:type zen/string}}}

         ;; The return value when no errors
         :result {
                  ;; Should be defined in the form of hash-map (dictionary)
                  :type zen/map

                  ;; Set the keys specified below as the mandatory fields if needed
                  :require #{:my-result}

                  ;; Specify the names of keys with their types
                  :keys {:my-result {:type zen/number}}}}}}
```

### 3. Implement task

Once you have the executor and definitions of custom tasks, all you have to do is write the implementation of the tasks in the programming language you chose.

