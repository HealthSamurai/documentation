# Task API

**Task API** is service for executing and monitoring expensive asynchronous  operations which in the context of this API will be called **tasks.**&#x20;

API consists of **two** services:&#x20;

* **Task-service** is a service that controls whole task life-cycle (start, fail, retry, etc.)
* **Executor-service** is a service that execute tasks defined as part of Aidbox (for example: [Archive/Restore API](archive-restore-api/) tasks).

### Start using Task API

To start using Task API you should define a few services in your Aidbox service configuration:&#x20;

1.  Include **task** and **executor** services in your Aidbox service configuration.

    ```clojure
    {ns     aidbox-with-task
     import #{aidbox awf.task awf.executor}

     box
     {:zen/tags #{aidbox/system}
      :services {:task-service awf.task/task-service
                 :aidbox-long-pool-executor-service awf.executor/aidbox-long-pool-executor-service}}
     }
    ```
2. Restart Aidbox.

### Task UI

If all services running correctly you would see Tasks icon on navigation bar and use built-in Aidbox Tasks UI:

<figure><img src="../../.gitbook/assets/image (5) (1).png" alt=""><figcaption><p>Tasks UI</p></figcaption></figure>

<figure><img src="../../.gitbook/assets/image (7) (1).png" alt=""><figcaption><p>Expanded task</p></figcaption></figure>
