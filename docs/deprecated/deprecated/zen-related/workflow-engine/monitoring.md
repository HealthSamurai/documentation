# Monitoring

{% hint style="warning" %}
Workflow engine is configured by zen. We do not support it and do not recommend to use it anymore. Please, use any other workflow engine e.g. [Temporal](https://temporal.io/).

Since the 2405 release, using Aidbox in FHIR schema validation engine is recommended, which is incompatible with zen or Entity/Attribute options.

[Setup Aidbox with FHIR Schema validation engine](broken-reference)
{% endhint %}

Inside the Aidbox Console a number of panels are provided to monitor the state of Workflows, Tasks, and Services:

## Task UI

Task UI list displays the list of tasks in the system.

<div data-full-width="true"><figure><img src="../../../../../.gitbook/assets/0cdc9217-a4fe-46ce-acde-bb4e7151803c.png" alt=""><figcaption><p>Task list</p></figcaption></figure></div>

Task detailed view allows you to check the results, params, log of task instance transitions through statuses, and cancel tasks manually.

<div data-full-width="true"><figure><img src="../../../../../.gitbook/assets/229666e5-9114-4bb2-90ec-fe8f2890e48d.png" alt=""><figcaption><p>Task detailed view</p></figcaption></figure></div>

## Workflow UI

The workflow list displays all started workflows in the system.

<div data-full-width="true"><figure><img src="e830137a-d778-4c26-9a67-4ca08d66002e.png" alt=""><figcaption><p>Workflow list</p></figcaption></figure></div>

The workflow details view allows you to check the results and parameters of the selected workflow instance and all activities launched by it.

<div data-full-width="true"><figure><img src="76040283-821c-4bca-ad9d-5fa3e1d3f826.png" alt=""><figcaption><p>Workflow result</p></figcaption></figure></div>

<div data-full-width="true"><figure><img src="../../../../../.gitbook/assets/6b96e391-a3ca-49b2-98a6-d0ba82ed94ef.png" alt=""><figcaption><p>Workflow params</p></figcaption></figure></div>

From the Activity tab, you can review all tasks and workflows launched by the selected workflow. You can also click the activity definition to go to the [Task](monitoring.md#task-ui) or [Workflow](monitoring.md#workflow-ui) UI.

<div data-full-width="true"><figure><img src="../../../../../.gitbook/assets/0d6743df-3cbc-470e-8284-893ade68d77a.png" alt=""><figcaption><p>Started activities</p></figcaption></figure></div>

## Scheduler UI

Scheduler rule list displays the list of all rules defined in Aidbox Project.

<div data-full-width="true"><figure><img src="af3b2b10-893f-4c31-943a-06a5eeb1a8bc.png" alt=""><figcaption><p>Scheduler rule list</p></figcaption></figure></div>

The detailed rule view displays the definition of the selected rule and other information about its execution.

<div data-full-width="true"><figure><img src="../../../../../.gitbook/assets/f6380fb8-cd66-4de8-8ea9-6a123e1eeaa6.png" alt=""><figcaption><p>Scheduler definition</p></figcaption></figure></div>

From the Activity tab, you can review all tasks and workflows launched by the rule. You can also click the activity definition to go to the [Task](monitoring.md#task-ui) or [Workflow](monitoring.md#workflow-ui) UI.

<div data-full-width="true"><figure><img src="../../../../../.gitbook/assets/7ee65051-102f-43cb-b73c-11ca4561da14.png" alt=""><figcaption><p>Scheduler activities</p></figcaption></figure></div>
