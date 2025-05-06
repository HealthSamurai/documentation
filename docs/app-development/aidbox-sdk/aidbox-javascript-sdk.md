# Aidbox JavaScript SDK

### Installation

#### Prerequisites for Zen CLI

Before proceeding with the installation process, it's important to ensure that your system meets the necessary prerequisites. Failure to install the prerequisites may result in errors or unexpected behavior during the installation process.

Please check the documentation or installation guide for the software or package you are trying to install to determine the specific prerequisites:

Unix-like system with `bash`, `tar` and `java` installed

#### Step 1: Install Zen CLI

To generate SDK by your `zen-project` config you have to install [zen-cli](https://www.npmjs.com/package/@aidbox/zen-cli). This command will install the latest version of the zen-cli on your system. The -g flag tells npm to install the package globally, making it available to all projects on your system.

```
npm install -g @aidbox/zen-cli
```

#### Step 2: Install Zen dependencies

In the case you do not have `zen-project` configured - follow [the documentation](https://github.com/zen-lang/zen).

After configuration of zen-package you have to install dependencies (e.g hl7-fhir-r4-core) by typing the following into a terminal window

```
zen-cli pull-deps
```

This command will download FHIR Definitions All the value sets, profiles, etc. defined as part of the FHIR specification, and the included implementation guides depends on added packages.

#### Step 3: Generate SDK package

In terminal move to your `zen-project` folder and generate SDK by run the following

```
zen-cli get-sdk
```

After running the zen-cli get-sdk command, you should be able to find the aidbox-javascript-sdk.tgz npm package in the root directory of your project. This archive is generated based on your zen-project and includes all types of resources.

#### Step 4: Add SDK package to package.json

Then, when you get your SDK move this archive into your project and add SDK as dependency in `package.json`

```
"dependencies": {
  ...
  "aidbox-sdk": "file:<PATH_TO_GENERATED_ARCHIVE_SDK>"
}
```

Install dependencies

```
npm install
```

**Important**: Set of available features and typescript types are unique and depend on `zen-project` configuration, selected FHIR version, custom schemas and operations, it's important to include package as part of your Git repository.

### How to use

**Important:** Before we start we have to make sure that aidbox client [is configured](https://docs.aidbox.app/tutorials/security-and-access-control/basic-auth-tutorial#basic-auth) and your [access policies](https://docs.aidbox.app/security-and-access-control-1/security/access-control) provide granular access to resources you're trying to reach.

```javascript
import { Client } from "aidbox-sdk"

export const aidbox = new Client("<HOST_URL>", {
    username: "<CLIENT_NAME>",
    password: "<CLIENT_SECRET>"
})
```

This code creates a new instance of the SDK from the aidbox-sdk package, and sets it up to connect to an Aidbox server running on `<HOST_URL>`. You would need to replace `<CLIENT_NAME>` and `<CLIENT_SECRET>` with the actual client ID and client secret that you configured earlier.

By using aidbox-sdk in your project, you can easily interact with an Aidbox server and perform actions like reading and writing resources, searching for resources, and more.

Then you can use aidboxClient in wherever you want

```javascript
import { aidbox } from "../aidbox-client"

async function getPatients() {
    return aidbox.getResources("Patient")
}
```

### API

#### getResources

getResources method accepts the name of the resource and is the basis for the subsequent complication of the request

**where**

Method where add additional parameters for searching

For example, you want to find all patients with name John, so

```
where("name", "John")
```

Or, you want to find all patients with name John or Steve

```
where("name", ["John", "Steve"])
```

Also, method where support prefixes for numbers and date, just pass it as third parameter

```
where("birthDate", "2014-06-30", "gt")
```

**Sort**

Method sort add additional parameters for sorting

For example, you want to display the oldest patients

```
sort("birthDate", "acs")
```

And also, you want to sort this data by patients name

```
sort("birthDate", "acs").sort("name", "acs")
```

**Count**

Method count used for make limit the number of resource returned by request

```
count(10)
```

**Summary**

To request only a portion of the resources from a server, you can use the summary function. This function allows you to specify the type of summary you want to receive, such as **true, false, text, data, or count**.

By default, when you make a request to a server, it will return all resource. However, if you only need a specific part of the resources, you can use the summary function to request a condensed summary of the data.

The summary function accepts several types of summaries, each of which provides a different level of detail about the data. For example:

If you set the summary parameter to true, the server will return a limited subset of elements from the resource. This subset SHOULD consist solely of all supported elements that are marked as "summary" in the base definition of the resource

If you set the summary parameter to false, the server will return all parts of the resource.

If you set the summary parameter to text, the server will return only the text, id, meta, and top-level mandatory elements.

If you set the summary parameter to data, the server will return resources without the text element.

If you set the summary parameter to count, the server will return just return a count of the matching resources, without returning the actual matches.

```
summary("data")
```

**Elements**

If you need to retrieve specific elements of a resource from a server, you can use the elements function. This function allows you to specify which parts of the resource you are interested in, and can help to reduce the amount of data returned by the server.

By using the elements function, you can customize your requests to retrieve only the data that you need, rather than requesting the entire dataset. This can be particularly useful when dealing with large datasets or when you only need a small subset of the available data.

To use the elements function, simply pass in the elements you want to retrieve as arguments. For example, if you only need the name and address fields from a resource, you can make a request using the following syntax:

```
elements(["name", "address"])
```

This request will return only the name and address fields for each resource, rather than the entire dataset. By reducing the amount of data returned, you can help to streamline your requests and improve the performance of your application.

#### getResource

The getResource function is a tool for retrieving a single resource by its ID.

To use the getResource function, you must pass in the resource type and ID as arguments. For example, if you want to retrieve a patient resource with the ID some-id, you can make a request using the following syntax:

```
getResource("Patient", "id")
```

#### patchResource

The patchResource function is used to update a specific resource identified by its id with a partial set of data provided in the third parameter. This function allows for partial updates of a resource without having to send the entire resource object.

The first parameter specifies the name of the resource to be updated. The second parameter identifies the specific resource to be updated.

#### deleteResource

The deleteResource function is used to delete a specific resource identified by its id.

```
deleteResource("Patient", "c58f29eb-f28d-67c1-0400-9af3aba3d58c")
```

### createSubscription

Aidbox subscription is a way to subscribe and get notifications about updating resources on the server. See our [subscription sample](https://github.com/Aidbox/aidbox-sdk-js/tree/main/subscription-sample) for more details.

```javascript
await client.createSubscription({
  id: "patient-created",
  status: "active",
  trigger: { Patient: { event: ["create"] } },
  channel: { endpoint: `${process.env.NODE_APP_URL}/patient-created` },
});
```

### sendLog

Aidbox has the ability to extend its logs. There is an [endpoint](https://docs.aidbox.app/core-modules/logging-and-audit/extending-access-logs#usdloggy-endpoint) that accepts logs with the defined structure from your application. These logs are ingested into the elastic log.

```javascript
await client.sendLog({
  type: "ui",
  message: { event: "YOUR_EVENT", id: "id", ... },
  v: "APP_VERSION",
  fx: "fetchUsers"
})
```

### Bundle request

SDK has helpers to prepare data for bundle request.

```javascript
client.transformToBundle(
  [
    {
      resourceType: "Patient",
      name: [
        {
          given: [""],
          fimily: "",
        },
      ],
    },
  ],
  "POST"
);
```

Bundle requests could be a [transaction or batch](https://docs.aidbox.app/api-1/fhir-api/bundle#post-endpoint) type. SDK uses the "transaction" type by default but you can change it by providing it in the second parameter.

```javascript
const data = ArrayOfPatients.map(client.bundleEntryPost);

await client.bundleRequest(data, "batch");
```

## Task API

Queues are a valuable tool for achieving reliable, asynchronous, scalable, and retry-safe code execution. By using queues, we can ensure that tasks or messages are processed reliably, even in the face of failures or system disruptions. The asynchronous nature of queues allows tasks to be processed independently, enabling parallelism and reducing wait times.

```javascript
import { Engine } from "aidbox-javascript-sdk";

const client = new Engine({
  url: "http://localhost:8888",
  username: "test", password: "secret"
});
```

### Definition

First of all we have to create task's schema with settings that apply restrictions on input and output arguments for entity like this one:

```clojure
 SendMessage
 {:zen/tags #{awf.task/definition zen/schema}
  :type zen/map
  :require #{:params :result}
  :keys {:params {:type zen/map
                  :require #{:phone :message}
                  :keys {:phone {:type zen/string}
                         :message {:type zen/string}}}
         :result {:type zen/map
                  :require #{:status}
                  :keys {:status {:type zen/string}}}}}
```

{% hint style="info" %}
We have to regenerate SDK package each time we made changes into configuration project. [More information about task definition](https://docs.aidbox.app/modules-1/workflow-engine/task#1.-specify-task-definition)
{% endhint %}

### Implementation

Next step is creating business logic that will be considered as a worker, the worker will be handling each task, we can

```javascript
client.task.implement("SendMessage", async ({ params, status }) => {
  const { message, phone } = params;
  
  try {
    await fetch('https://message-sending-server.com/', {
      method: 'POST',
      body: JSON.stringify({ message, phone }),
    });
    
    return { status: 'success' }
  } catch(error) {
    return { status: 'failure' } 
  }
}, { batchSize: 5 })
```

### Execution

The way to execute a single task with unique context

```javascript
await client.task.execute("SendMessage", { phone: "+1234567890", message: "Hi!" })
```

## Workflow Engine

Aidbox provides Workflow Engine module, so you're able to define your own sequence of async tasks on top of Task API. In many business processes, certain tasks depend on the completion of other tasks. Defining task dependencies through workflow implementation allows the developer to control the order in which tasks are executed.

```javascript
import { Engine } from "aidbox-javascript-sdk";

const client = new Engine({
  url: "http://localhost:8888",
  username: "test", password: "secret"
});
```

### Definition

```clojure
 CheckOutWorkflow
 {:zen/tags #{awf.workflow/definition zen/schema}
  :type zen/map
  :pool decision-pool
  :require #{:params :result}
  :keys {:params {:type zen/map
                  :require #{:clientId}
                  :keys {:clientId {:type zen/string}}}
         :result {:type zen/map
                  :keys {:messageId {:type zen/string}}}
         :error {:type zen/map
                 :keys {:message {:type zen/string}}}}}
```

{% hint style="info" %}
We have to regenerate SDK package each time we made changes into configuration project. [More information about workflow definition](https://docs.aidbox.app/modules-1/workflow-engine/workflow#1.-specify-workflow-definition)
{% endhint %}

### Implementation

```javascript
await client.workflow.implement("CheckOutWorkflow", async ({ params }, { execute, complete, fail }) => {
  if (params.event === 'awf.workflow.event/workflow-init') {
    const response = await fetch("https://server.com/get-client")
    const { phone } = response.json()
    return [execute({ definition: "SendMessage", params: { phone, message: "Hi!" } })]
  }

  try {
    const response = await fetch("https://workflow-state.com")
    const data = response.json()

    if (params.event === 'awf.workflow.event/task-completed' && data.step === 1) {
      return [execute({ definition: "UpdateInformation", params: {} })]
    }

    if (params.event === 'awf.workflow.event/task-completed' && data.step === 2) {
      return [complete({ })]
    }
  } catch(error) {
    return [fail({ error })]
  }

  return []
})
```

### Execution

```javascript
await client.workflow.execute("CheckOutWorkflow", { clientId: "" })
```
