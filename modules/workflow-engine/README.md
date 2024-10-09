---
description: A workflow engine designed to orchestrate and automate healthcare processes
---

# Workflow Engine

## Workflow Engine Components Overview

The Aidbox workflow engine is a core module of the Aidbox FHIR backend that allows you to define and execute custom business logic based on the data state and events in Aidbox.

Having a workflow engine tightly integrated with a FHIR server offers numerous benefits for healthcare organizations seeking to optimize their processes and improve patient care. Here are some key advantages:

1. Seamless data access and manipulation: With direct access to FHIR resources and operations, workflows can easily read, create, update, and delete healthcare data without the need for complex data integration or mapping.
2. Real-time event processing: The event-driven architecture allows the workflow engine to respond immediately to changes in FHIR data, enabling rapid adaptation and reaction to various healthcare scenarios.
3. Simplified maintenance: Reduces operational complexity and resource requirements by consolidating workflow management and FHIR data processing within a single server.

Workflow Engine allows implementing both simple scenarios using [predefined tasks](task/#predefined-tasks) and services with Aidbox Configuration or building applications of any complexity using provided API.

## Basic Concepts <a href="#basic-concepts" id="basic-concepts"></a>

<figure><img src="../../.gitbook/assets/image (22) (2).png" alt=""><figcaption><p>Workflow Engine Components</p></figcaption></figure>

### Task and Workflow

[task](task/ "mention") and [workflow](workflow/ "mention") are two basic concepts of Workflow Engine. Both are managed through Aidbox API.

Tasks are the simplest building block responsible for performing business actions. Aidbox provides a number of tasks out of the box. Additional tasks can be implemented outside of Aidbox in any programming language using an [SDK](../../app-development/aidbox-sdk/aidbox-javascript-sdk.md#task-api) or API directly.

For more complex application when a simple granular task is not enough, it is necessary to build a workflow. Workflow is a series of tasks that must be completed in a specific order and within a defined time frame to achieve a specific outcome.

### Task Executor

Task Executor is responsible for executing the logic behind a task. For [predefined tasks](task/aidbox-predefined-tasks.md), they are run within Aidbox, and custom task executors could be [implemented](task/#task-implementation-1) in any programming language using an [SDK](../../app-development/aidbox-sdk/aidbox-javascript-sdk.md#task-api) or API directly.

### Workflow Executor

Workflow Executor is responsible for making decisions about the next steps in a workflow based on predefined rules, conditions, or logic. Workflow logic could be implemented using [Workflow Engine SDK](../../app-development/aidbox-sdk/aidbox-javascript-sdk.md#workflow-engine)

### [Services](services.md)

Workflow Engine Services are responsible for starting tasks or workflow.

**Subscription Trigger Service**  - allows starting task or workflow, when a certain event happens in aidbox.

_For example:_ whenever an Observation resource with a value of blood pressure above some threshold is created in Aidbox - Subscription Trigger will start a Task that creates Appointment resource with reference for Observation in the \`reason\` field.

**Scheduler Service** - allows starting regular scheduled tasks or workflow.

### [Monitoring Console](monitoring.md)

To monitor the current state of tasks, workflow, and services Workflow Engine provides UI inside the Aidbox console. It's also possible to use API to implement your own user interface.&#x20;

### Tutorials

* [resource-generation-with-map-to-fhir-bundle-task-and-subscription-triggers.md](../../tutorials/tutorials/resource-generation-with-map-to-fhir-bundle-task-and-subscription-triggers.md "mention") - How to create an encounter when a specific observation is created.

{% content-ref url="task/" %}
[task](task/)
{% endcontent-ref %}

{% content-ref url="workflow/" %}
[workflow](workflow/)
{% endcontent-ref %}

{% content-ref url="services.md" %}
[services.md](services.md)
{% endcontent-ref %}

{% content-ref url="monitoring.md" %}
[monitoring.md](monitoring.md)
{% endcontent-ref %}
