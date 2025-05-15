# Monitoring

{% hint style="warning" %}
Workflow engine is configured by zen. We do not support it and do not recommend to use it anymore. Please, use any other workflow engine e.g. [Temporal](https://temporal.io/).

Since the 2405 release, using Aidbox in FHIR schema validation engine is recommended, which is incompatible with zen or Entity/Attribute options.

[Setup Aidbox with FHIR Schema validation engine](broken-reference)
{% endhint %}

Inside the Aidbox Console a number of panels are provided to monitor the state of Workflows, Tasks, and Services:

## Task UI

Task UI list displays the list of tasks in the system.

<div data-full-width="true"><figure><img src="../../../../../.gitbook/assets/705022c71d074ea88c2deabf959caa5a.png" alt=""><figcaption><p>Task list</p></figcaption></figure></div>

Task detailed view allows you to check the results, params, log of task instance transitions through statuses, and cancel tasks manually.

<div data-full-width="true"><figure><img src="../../../../../.gitbook/assets/d4ae5b2822d94f7abfeae49cc0072b37.png" alt=""><figcaption><p>Task detailed view</p></figcaption></figure></div>

## Workflow UI

The workflow list displays all started workflows in the system.

<div data-full-width="true"><figure><img src="../../../../../.gitbook/assets/d4d29b054db540de9e875c54a5d8827a.png" alt=""><figcaption><p>Workflow list</p></figcaption></figure></div>

The workflow details view allows you to check the results and parameters of the selected workflow instance and all activities launched by it.

<div data-full-width="true"><figure><img src="../../../../../.gitbook/assets/94c3a88347804dbea40123db97e30564.png" alt=""><figcaption><p>Workflow result</p></figcaption></figure></div>

<div data-full-width="true"><figure><img src="../../../../../.gitbook/assets/dd99ee23116941cab49cbeba2a872c73.png" alt=""><figcaption><p>Workflow params</p></figcaption></figure></div>

From the Activity tab, you can review all tasks and workflows launched by the selected workflow. You can also click the activity definition to go to the [Task](monitoring.md#task-ui) or [Workflow](monitoring.md#workflow-ui) UI.

<div data-full-width="true"><figure><img src="../../../../../.gitbook/assets/c7ba3b8b402f4096a66b2773d108517f.png" alt=""><figcaption><p>Started activities</p></figcaption></figure></div>

## Scheduler UI

Scheduler rule list displays the list of all rules defined in Aidbox Project.

<div data-full-width="true"><figure><img src="../../../../../.gitbook/assets/2090a2e1a868497095329ede6dd8986e.png" alt=""><figcaption><p>Scheduler rule list</p></figcaption></figure></div>

The detailed rule view displays the definition of the selected rule and other information about its execution.

<div data-full-width="true"><figure><img src="../../../../../.gitbook/assets/9bb63ac84f164c59a79381073f5f1280.png" alt=""><figcaption><p>Scheduler definition</p></figcaption></figure></div>

From the Activity tab, you can review all tasks and workflows launched by the rule. You can also click the activity definition to go to the [Task](monitoring.md#task-ui) or [Workflow](monitoring.md#workflow-ui) UI.

<div data-full-width="true"><figure><img src="../../../../../.gitbook/assets/71441dc2320941ff9fc33434ec4c8d61.png" alt=""><figcaption><p>Scheduler activities</p></figcaption></figure></div>
