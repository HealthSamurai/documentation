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

To add a custom task:

1. Add the definition of the task to Aidbox Project, so WorkflowEngine knows about the new task.
2. &#x20;Implement tak logic using [Executor API](task-executor-api.md) either directly or through the SDK.

### 1. Specify Task Definition

The first step for implementing a new custom is to specify definitions of custom tasks in [aidbox-zen-lang-project](../../../aidbox-configuration/aidbox-zen-lang-project/ "mention") .&#x20;

Task Definition contains all the information necessary to define the behavior of a task instance.

Below is an example of the Aidbox Project namespace with a new task definition.

```clojure
{ns     my-tasks
 ;; For task definitions, "awf.task" namespace should be imported
 import #{awf.task}

 ;; The name of the task, which is referred to with the namespace as "my-tasks/example-task"
 example-task
 {
  ;; The following tags must be set for task definitions
  :zen/tags #{awf.task/definition zen/schema}

  ;; Every task definition is supposed to have a hash-map (dictionary-like) data structure
  :type zen/map

  ;; Time limit in milliseconds during which tasks can be in the status "requested"
  ;; In case of timeout, the status is changed to "ready"
  ;; The default value: 10000
  :requestedToStartTimeout 3000

  ;; Time limit in milliseconds during which tasks can be in the status "in-progress"
  ;; The default value: 120000
  :inProgressTimeout 5000

  ;; The maximum number of retries allowed
  ;; The default value: 2
  :allowedRetryCount 1

  ;; Delay time in milliseconds before each retry
  ;; The default value: 10000
  :retryDelay 2000


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



### 2. Implement Task

{% hint style="info" %}
We are now preparing Aidbox Workflow/Task SDK. By using it, you can probably simplify this step if you use one of the following languages: **typescript**, **python**, or **.NET**.
{% endhint %}

Once you have the task definitions above, your custom tasks can be implemented in any programming language by using [Task Executor API](task-executor-api.md), according to the following diagram.

&#x20;

<figure><img src="../../../.gitbook/assets/Workflow &#x26; Task Runtime.png" alt=""><figcaption><p>Aidbox Task Executor</p></figcaption></figure>

At first, a **`awf.task/long-poll`**  or **`awf.task/poll`** API request should be sent to Task Service to fetch a new task created by [task-user-api.md](task-user-api.md "mention") .&#x20;

Then, the executor should send an **`awf.task/start`** request to set the task status to `in-progress`. After receiving the response, the task you implement is supposed to be run.

Finally, an **`awf.task/success`** request must be sent, if the execution is successful, or an**`awf.task/fail`** request if not. They both change the status of the task to `done` and set the corresponding outcome value.

In case the task can run for a long time,  **`awf.task/notify`** requests need to be sent during their execution to tell Task Service that the worker continues processing and it extends the inProgressTimeout value.&#x20;

All [Task Executor API](task-executor-api.md) methods are listed below.

<details>

<summary>API methods</summary>

[`awf.task/poll`](task-executor-api.md#awf.task-poll) - Fetches a new task from the queue and changes its status from `ready` to `requested`, immediately returns an empty array if there are no tasks in the queue.

[`awf.task/long-poll`](task-executor-api.md#awf.task-long-poll) - Fetches a new task from the queue and changes its status from `ready` to `requested`. Waits for a timeout unless a new task is received. In case of timeout, returns an empty array.&#x20;

[`awf.task/start`](task-executor-api.md#awf.task-start) - Changes the status of a task from `requested` to `in-progress` and start its execution.

[`awf.task/notify`](task-executor-api.md#awf.task-notify) - Notifies Task Service that a task is still alive.

[`awf.task/success`](task-executor-api.md#awf.task-success) - Changes the status of a task from `in-progress`  to `done` with outcome `succeeded`.

[`awf.task/fail`](task-executor-api.md#awf.task-fail) - Changes the status of a task from `in-progress` to `done` with outcome `failed`.

</details>

