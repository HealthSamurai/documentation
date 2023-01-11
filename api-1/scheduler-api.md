# Scheduler API

**Scheduler API** is a service designed to schedule asynchronous operations. Operations defines as scheduler rules with cron string and concurrency-policy.

{% hint style="info" %}
Scheduler API operates with tasks provided by [Task API](task-api.md) so you need to include it your Aidbox service configuration before using Scheduler API.
{% endhint %}

### Rule example

<table><thead><tr><th>Properties</th><th data-type="checkbox">Required</th><th>Definition</th></tr></thead><tbody><tr><td>schedule</td><td>true</td><td>Cron string that defines period of executing task.</td></tr><tr><td>concurrency-policy</td><td>false</td><td>Can be "forbid" or "allow". When value is <strong>"forbid"</strong> new task would not be started while previous scheduled same task is still running. When value is <strong>"allow"</strong> new task starts anyway. Default value is "forbid".</td></tr><tr><td>start-deadline-seconds</td><td>false</td><td>Period of time in <strong>seconds</strong> when task is still available to start if it misses its scheduled time.<br>Default value is 60.</td></tr><tr><td>task-request</td><td>true</td><td>Definition of the task. Check out <a href="../tutorials/automatically-archive-auditevent-resources-in-gcp-storage-guide.md">regular archive creating guide</a> for more examples.</td></tr></tbody></table>

```clojure
sample-every-day-rule
 {:zen/tags #{awf.scheduler/rule}
  :schedule "10 14 * * *" 
  :concurrency-policy "forbid"
  :start-deadline-seconds 60
  :task-request { task-definition }
  }
```

### Start using Scheduler API

To start using Scheduler API you should define scheduler-service and add it into your Aidbox service configuration:

1.  Define your scheduler services. Include created rules in scheduler-service `:rules` property.

    ```clojure
    scheduler-service
     {:zen/tags #{aidbox/service}
      :engine   awf.scheduler/task-scheduler-service-engine
      :rules    #{sample-every-day-rule}}
    ```
2.  Include task, executor and scheduler services in your Aidbox service configuration if they are not already included in your configuration.

    ```clojure
    box
     {:zen/tags #{aidbox/system}
      :services {:task-service awf.task/task-service
                 :scheduler-service scheduler-service
                 :aidbox-long-pool-executor-service awf.executor/aidbox-long-pool-executor-service}}
    ```
3. Restart Aidbox.

If all services running correctly you should see Scheduler icon on navigation bar:

![](<../.gitbook/assets/image (1).png>)
