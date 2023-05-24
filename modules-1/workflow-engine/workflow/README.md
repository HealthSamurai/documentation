# Workflow

## Introduction

Workflow allows orchestrating a series of [tasks](../task/). Workflow in Aidbox is implemented through a special [decision](../task/aidbox-predefined-tasks.md#awf.workflow-decision-task) task, an instance of which is created on every event of workflow, thus a logic behind workflow could be implemented as an executor for this task.

We plan to add SDK for different programming languages to allow the implementation of workflow as a code on the client side. Also, we plan to introduce DSL to describe simple workflow inside Aidbox Configuration Project.&#x20;

## Workflow instance

When a new workflow is created  by [task-user-api.md](task-user-api.md "mention") or by [services.md](../services.md "mention"), new resource `AidboxWorkflow` is created which stores workflow Params, Result, and Status, as well as some additional information regarding workflow execution. Bellow is an example of AidboxWorkflow with fields explanation:

```clojure
{
 ;; resource or service who requested workflow execution
 :requester       {:resourceType "User" :id "admin"}

 ;; number of finished attempts to execute workflow
 :retryCount      1

 ;; UUID which is assigned to a workflow, whenever workflow is started
 ;; Executors should include it in the requests
 :execId         "44bc2-aac6-49a1-91e7-7f9fa29afe21"

 ;; Current status of the workflow.
 ;; Possible values are: "created" "in-progress" "done"
 :status          "done"

 ;; Outcome of the workflow execution, only available when workflow status is "done"
 ;; Possible values are: "succeeded" "failed"
 :outcome         "succeeded"

 ;; Only filled when outcome is "failed"
 :outcomeReason   {;; Type of "fail"
                   ;; Possible values are:
                   ;; "awf.workflow/failed-by-executor" - executor called action awf.workflow/fail, error is validated with definition schema
                   ;; "awf.executor/unknown-error" - unexpected error happened
                   :type "awf.workflow/failed-by-executor"
                   :message "Failed by executor"
                   :data {}}

 ;; Params of the workflow, as specified by caller, should be valid to the schema of the definition
 :params          {}

 ;; Result of the workflow, should be valid to the schema of the definition, only present for a task with :outcome "succeeded"
 :result          {}

 ;; Error of the workflow, should be valid to the schema of the definition, only present for a task with :outcome "failed"
 :error           {}

 ;; Id of the workflow, generated if not supplied. Should be unique across the system.
 :id  "ea82769d-e083-461c-8cb0-3427ed466f19"

 ;; Symbol of the workflow definition in Aidbox Project,
 ;; works as the unique name of the workflow that resonates with its function
 :definition  "aidbox.bulk/import-resources-workflow"

 ;; Time when workflow starts. Could be specified by the caller, or set by the engine on task retry.
 :executeAt "2023-04-24T09:30:51.562261Z"
 }
```

###

### Workflow statuses and outcomes

Below is a representation of a Workflow Instance life cycle.

<figure><img src="../../../.gitbook/assets/image (17).png" alt="" width="563"><figcaption><p>Workflow instance lifecycle</p></figcaption></figure>

After the workflow is created, the decision task with the same definition is created to move the workflow to the `in-progress` state and to execute the workflow body until the new internal workflow activity is started. Then the decision task is completed with a successful outcome.&#x20;

When the started activity is executed, the new decision task should execute the workflow body again to start the next activities. This process is repeated until the last activity is finished or some activity is finished with an error. After that, the workflow will change to the status `done` with one of the possible outcomes.

##

## Workflow User API

[Workflow User API](task-user-api.md) allows users to manually control Aidbox workflows by [RPC methods](../../../api-1/rpc-api.md).&#x20;

<details>

<summary>API methods</summary>

* [`awf.workflow/create-and-execute`](task-user-api.md#awf.workflow-create-and-execute) - Creates an instance of a defined workflow and makes it ready to be executed immediately or at a specified time.
* [`awf.workflow/status`](task-user-api.md#awf.workflow-status) - Returns the status of a workflow instance with the specified id.
* [`awf.workflow/list`](task-user-api.md#awf.workflow-list) - Returns the list of all workflows.

</details>



## Workflow Implementation

To add a custom workflow:

1. Add the definition of the workflow to Aidbox Project, so Task Service knows about the new task.
2. &#x20;Implement decision tasks logic using [Executor API](../task/task-executor-api.md) either directly or through the SDK.

### 1. Specify Workflow Definition

The first step for implementing a new custom workflow is to specify its definition in [aidbox-zen-lang-project](../../../aidbox-configuration/aidbox-zen-lang-project/ "mention") .&#x20;

Workflow Definition contains all the information necessary to define the behavior of a workflow instance.

Below is an example of the Aidbox Project namespace with a new workflow definition.

```clojure
{ns     my-workflows
 ;; For workflow definitions, "awf.workflow" namespace should be imported
 import #{awf.workflow}

 ;; The name of the workflow, which is referred to with the namespace as "my-workflows/example-workflow"
 example-workflow
 {
  ;; The following tags must be set for workflow definitions
  :zen/tags #{awf.workflow/definition zen/schema}

  ;; Every workflow definition is supposed to have a hash-map (dictionary-like) data structure
  :type zen/map

  ;; Define the schema of input, output, and error for workfow execution
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

### 2. Implement Workflow

{% hint style="info" %}
We are now preparing Aidbox Workflow/Task SDK. By using it, you can probably simplify this step if you use one of the following languages: **Typescript**, **Python**, or **.NET**.
{% endhint %}

Once you have the workflow definition above, your custom workflow can be implemented in any programming language by using [Task Executor API](../task/task-executor-api.md) and decision tasks. The Decision Executor should be separated from the Task Executor and handle only decision tasks. The process of polling for new tasks is the same as for the Task Executor and is simply simplified in the diagram as the "New Task Handshake". Also, all normal task executions are shown as external signals on the diagram and must be implemented according to the [Task Implementation Guide](../task/#2.-implement-task).

<figure><img src="../../../.gitbook/assets/image (9).png" alt=""><figcaption><p>Workflow Execution Process</p></figcaption></figure>

The decision task is created after the workflow is started. The decision task should contain one of the [defined events](../task/aidbox-predefined-tasks.md#event-types) to mark the purpose of the decision task and return one or more [defined actions](../task/aidbox-predefined-tasks.md#action-types) to inform the executor of the current status of the workflow and to schedule new tasks.

According to the above diagram, the workflow should execute **Task-1** and **Task-2** synchronously. In this case, the first execution of the decision task should return **`awf.workflow.action/schedule-task`** with the **Task-1** definition to create a new task in the Task Service.&#x20;

When **Task-1** is completed, the new decision task will be created, but this time it should execute the next part of the workflow to schedule new tasks. This process should be repeated until all tasks are completed. The last decision task should return the result of the workflow with the **`awf.workflow.action/complete-workflow`** action.&#x20;

You can review all available decision task actions in the [decision task documentation](../task/aidbox-predefined-tasks.md#decision-task).

Despite the decision task behavior described above, Decisions Executor could be implemented as a Task Executor using the Task Executor API.

All [Task Executor API](../task/task-executor-api.md) methods are listed below.

<details>

<summary>API methods</summary>

[`awf.task/poll`](../task/task-executor-api.md#awf.task-poll) - Fetches a new task from the queue and changes its status from `ready` to `requested`, immediately returns an empty array if there are no tasks in the queue.

[`awf.task/long-poll`](../task/task-executor-api.md#awf.task-long-poll) - Fetches a new task from the queue and changes its status from `ready` to `requested`. Waits for a timeout unless a new task is received. In case of timeout, returns an empty array.&#x20;

[`awf.task/start`](../task/task-executor-api.md#awf.task-start) - Changes the status of a task from `requested` to `in-progress` and start its execution.

[`awf.task/notify`](../task/task-executor-api.md#awf.task-notify) - Notifies Task Service that a task is still alive.

[`awf.task/success`](../task/task-executor-api.md#awf.task-success) - Changes the status of a task from `in-progress`  to `done` with outcome `succeeded`.

[`awf.task/fail`](../task/task-executor-api.md#awf.task-fail) - Changes the status of a task from `in-progress` to `done` with outcome `failed`.

</details>
