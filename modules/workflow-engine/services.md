# Services

## Scheduler

The Scheduler is a task-related service. It provides the ability to execute tasks and workflows at defined time intervals. The scheduler is managed by rules that have to determine which and when the activity will be executed, whether it should be forbidden if the same activity is already in progress, and the time for which it should be executed before the execution time comes.

To use the scheduler, you need to describe your rules in zen format and list them in the scheduler service definition.

#### Rule params

<table data-full-width="true"><thead><tr><th width="230">Parameter</th><th width="91">Type</th><th width="105.33333333333331" data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>schedule</td><td>string</td><td>true</td><td>A string in <strong>cron</strong> format that describes how often an activity is executed.<br><em>Example: <code>*****</code> (Every minute)</em></td></tr><tr><td>execute-on-setup</td><td>boolean</td><td>false</td><td>If <code>true</code>, the task will always be started immediately on the first Aidbox start with the new rule. The rule will adhere to the schedule after this. <br><em>Default: <code>false</code></em></td></tr><tr><td>concurrency-policy</td><td>string</td><td>false</td><td>Prohibits the scheduler to start an activity if a similar activity already started has not yet ended when value is <code>forbid</code>. Allows this behavior if the value is <code>allow</code>.<br><em>Default: <code>forbid</code></em></td></tr><tr><td>start-deadline-seconds</td><td>number</td><td>false</td><td>The period of time in which the task can be started after the assigned time in seconds.<br><em>Default: <code>60</code></em></td></tr><tr><td>task-request</td><td>map</td><td>true</td><td><p>Definition of the task-request to be performed.</p><p><em>Exclusive with workflow-request.</em></p></td></tr><tr><td>workflow-request</td><td>map</td><td>true</td><td><p>Definition of the workflow-request to be performed.</p><p><em>Exclusive with task-request.</em></p></td></tr></tbody></table>

#### Rule definition

```clojure
 archive-every-day
 {:zen/tags #{awf.scheduler/rule}
  :schedule "10 14 * * *"
  :execute-on-setup true
  :concurrency-policy "forbid"
  :start-deadline-seconds 60
  :task-request {:definition aidbox.archive/create-archive
                 :params {:targetResourceType    "AuditEvent"
                          :history               false
                          :criteriaPaths         ["recorded"]
                          :retentionPeriod       {:value  1
                                                  :unit   "day"}
                          :storageBackend        "gcp"
                          :serviceAccount        {:id "my-account"
                                                  :resourceType "GcpServiceAccount"}
                          :bucket                "aidbox-archive-test"
                          :pruneArchivedData     false}}}
 scheduler-service
 {:zen/tags #{aidbox/service}
  :engine awf.scheduler/task-scheduler-service-engine
  :rules #{archive-every-minute}}
```

This rule describes how to run the audit-events archiving task every day at 14:10. For a full example of how to use the scheduler, see the tutorial[automatically-archive-auditevent-resources-in-gcp-storage-guide.md](../../tutorials/tutorials/automatically-archive-auditevent-resources-in-gcp-storage-guide.md "mention")

Another example of rule definition is automatically delete AidboxTask, AidboxTaskLog and AidboxWrokflow resources with `awf.task/clean-up-activities` task.

#### Cleanup rule definition

```clojure
  cleanup-tasks
  {:schedule "10 14 * * *"
   :concurrency-policy "forbid"
   :start-deadline-seconds 60
   :task-request {:definition awf.task/task-clean-up
                  :params {:rules [{:retentionPolicy {:unit "days" :value 1}}]}}}
  scheduler-service
  {:zen/tags #{aidbox/service}
   :engine awf.scheduler/task-scheduler-service-engine
   :rules #{cleanup-tasks}}
```

This rule describes how to run the cleanup task that deletes all AidboxTask, AidboxTaskLog, and AidboxWorkflow resources that are older than 1 day every day at 14:10.

## Subscription trigger

Subscription trigger is a service that allows you to subscribe to changes in the Aidbox database and perform tasks or workflows described in the rule when the conditions are met.

To use the subscription trigger, you need to describe your rules in zen format and list them in the subscription trigger service definition.

#### Subscription trigger rule DSL

Subscription trigger rules

Subscription trigger rules use their own DSL to describe subscriptions. A select-by part of the rule may include several expressions and will be considered fulfilled if all expressions are true.

In general every expression is divided into three parts with specified keys:

1. **:get-in** - describes the path in the modified resource where the left-hand operation argument will be taken. The value must be a vector and contain the path to the value in the nested structure.\
   &#xNAN;_&#x45;xample: `:get-in [:name :given]`_
2. **:comparator** - the logical operation performed on the arguments.\
   &#xNAN;_&#x50;ossible values: `:eq`, `:gt`, `:lt`, `:ge`, `:le`_
3. **:value** - the right-hand argument of the expression. Should be the same type with left argument.

#### Rule params

<table data-full-width="true"><thead><tr><th width="184">Parameter</th><th width="91">Type</th><th width="99.33333333333331" data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td>select-by</td><td>vector</td><td>true</td><td><p>Vector of instructions written in the <a href="services.md#subscription-trigger-rule-dsl">subscription trigger rule DSL</a>.</p><p><em>Example: <code>[{:get-in [:resourceType] :comparator :eq :value "</code></em><code>Encounter</code><em><code>"}]</code></em></p></td></tr><tr><td>task-request</td><td>map</td><td>true</td><td><p>Definition of the task-request to be performed.</p><p><em>Exclusive with workflow-request.</em></p></td></tr><tr><td>workflow-request</td><td>map</td><td>true</td><td><p>Definition of the workflow-request to be performed.</p><p><em>Exclusive with task-request.</em></p></td></tr></tbody></table>

#### Rule definition

{% code fullWidth="true" %}
```clojure
{ns     my-trigger
 import #{aidbox awf.subscription-trigger ingestion.core lisp}

 observation-bundle-mapping
 {:zen/tags #{lisp/mapping}
  :mapping   {:resourceType "Bundle"
              :type "transaction"
              :entry [{:resource {:resourceType "Encounter"
                                  :subject (get-in [:subject])
                                  :participant (for [p (get-in [:performer])]
                                                       {:individual {:resourceType (get p :resourceType)
                                                                     :id (get p :id)}})
                                  :status "planned"
                                  :class  {:display "Encounter"}}
                                  :request {:method "PUT"
                                            :url "/Encounter"}}]}}

 trigger-on-observation-registered
 {:zen/tags    #{awf.subscription-trigger/rule}
  :select-by    [{:get-in [:resourceType] :comparator :eq :value "Observation"}
                 {:get-in [:status] :comparator :eq :value "registered"}]
  :task-request {:definition ingestion.core/map-to-fhir-bundle-task
                 :params {;; mapping should be a string representation of schema symbol with namespace
                          :mapping "my-trigger/observation-bundle-mapping"
                          :context {:resourceId (get-in [:id])
                          :subject (get-in [:subject])
                          :performer (get-in [:performer])}
  :format "fhir"}}}

subscription-trigger-service
{:zen/tags #{aidbox/service}
 :engine   awf.subscription-trigger/subscription-trigger-service-engine
 :rules    #{trigger-on-observation-registred}}}
```
{% endcode %}

This example uses a subscription trigger with [ingestion/map-to-fhir-bundle-task](task/aidbox-predefined-tasks.md#ingestion-map-to-fhir-bundle-task). It means that every time an Observation resource is updated with status `registered`, an Encounter resource is created with the same `subject` and `performer` as the `participant`.
