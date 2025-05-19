# Monitoring

{% hint style="warning" %}
Workflow engine is configured by zen. We do not support it and do not recommend to use it anymore. Please, use any other workflow engine e.g. [Temporal](https://temporal.io/).

Since the 2405 release, using Aidbox in FHIR schema validation engine is recommended, which is incompatible with zen or Entity/Attribute options.

[Setup Aidbox with FHIR Schema validation engine](broken-reference)
{% endhint %}

Inside the Aidbox Console a number of panels are provided to monitor the state of Workflows, Tasks, and Services:

## Task UI

Task UI list displays the list of tasks in the system.

<div data-full-width="true"><figure><img src="../../../../../.gitbook/assets/0a8cff89-2b1d-49cc-ad90-a99d2fa8873a.png" alt=""><figcaption><p>Task list</p></figcaption></figure></div>

Task detailed view allows you to check the results, params, log of task instance transitions through statuses, and cancel tasks manually.

<div data-full-width="true"><figure><img src="../../../../../.gitbook/assets/0db613ca-dc0b-4649-bb31-846d40e6fb34.png" alt=""><figcaption><p>Task detailed view</p></figcaption></figure></div>

## Workflow UI

The workflow list displays all started workflows in the system.

<div data-full-width="true"><figure><img src="../../../../../.gitbook/assets/d020f06d-0733-4e5f-8aa2-59c8495ba7cd.png" alt=""><figcaption><p>Workflow list</p></figcaption></figure></div>

The workflow details view allows you to check the results and parameters of the selected workflow instance and all activities launched by it.

<div data-full-width="true"><figure><img src="../../../../../.gitbook/assets/f92d7fd8-8864-45e9-8d7c-f844c4085238.png" alt=""><figcaption><p>Workflow result</p></figcaption></figure></div>

<div data-full-width="true"><figure><img src="../../../../../.gitbook/assets/1c70ba18-54e9-4866-b6f0-83cebc05a4ee.png" alt=""><figcaption><p>Workflow params</p></figcaption></figure></div>

From the Activity tab, you can review all tasks and workflows launched by the selected workflow. You can also click the activity definition to go to the [Task](monitoring.md#task-ui) or [Workflow](monitoring.md#workflow-ui) UI.

<div data-full-width="true"><figure><img src="../../../../../.gitbook/assets/c4233392-34f7-4b35-8783-ba35d65ba9bd.png" alt=""><figcaption><p>Started activities</p></figcaption></figure></div>

## Scheduler UI

Scheduler rule list displays the list of all rules defined in Aidbox Project.

<div data-full-width="true"><figure><img src="../../../../../.gitbook/assets/47604651-edeb-41b4-95fa-4304b260bcb7.png" alt=""><figcaption><p>Scheduler rule list</p></figcaption></figure></div>

The detailed rule view displays the definition of the selected rule and other information about its execution.

<div data-full-width="true"><figure><img src="../../../../../.gitbook/assets/4de1ce75-1ccd-4079-9395-7117d50241ff.png" alt=""><figcaption><p>Scheduler definition</p></figcaption></figure></div>

From the Activity tab, you can review all tasks and workflows launched by the rule. You can also click the activity definition to go to the [Task](monitoring.md#task-ui) or [Workflow](monitoring.md#workflow-ui) UI.

<div data-full-width="true"><figure><img src="../../../../../.gitbook/assets/19b06498-d5b6-42bc-ad3a-09d80d557296.png" alt=""><figcaption><p>Scheduler activities</p></figcaption></figure></div>
