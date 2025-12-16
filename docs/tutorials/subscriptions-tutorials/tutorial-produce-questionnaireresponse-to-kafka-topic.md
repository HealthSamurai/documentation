---
description: Stream FHIR QuestionnaireResponse data to Apache Kafka using Aidbox Forms and topic subscriptions.
---

# Tutorial: Produce QuestionnaireResponse to Kafka topic

## Topic-based Subscriptions to Kafka

[DEMO](https://github.com/Aidbox/examples/tree/main/aidbox-features/aidbox-subscriptions-to-kafka/README.md#demo) | [Documentation](./)

This example showcases [Aidbox SubscriptionTopic](./) producing data to Kafka.

Objectives:

1. Set up Aidbox and Kafka locally using Docker Compose.
2. Get **FHIR QuestionnaireResponse** via [Aidbox Forms](../../modules/aidbox-forms).
3. Learn how [AidboxSubscriptionTopic and AidboxTopicDestination](kafka-aidboxtopicdestination.md) work with Kafka to handle the collected data.

**Table of Contents**

* [Topic-based Subscriptions to Kafka](tutorial-produce-questionnaireresponse-to-kafka-topic.md#topic-based-subscriptions-to-kafka)
  * [Prerequisites](tutorial-produce-questionnaireresponse-to-kafka-topic.md#prerequisites)
  * [Step 1: Set Up the Environment](tutorial-produce-questionnaireresponse-to-kafka-topic.md#step-1-set-up-the-environment)
    * [Set Up Aidbox](tutorial-produce-questionnaireresponse-to-kafka-topic.md#set-up-aidbox)
    * [Run Aidbox, Kafka & Kafka UI](tutorial-produce-questionnaireresponse-to-kafka-topic.md#run-aidbox-kafka--kafka-ui)
  * [Step 2: Set Up Subscription and Destination](tutorial-produce-questionnaireresponse-to-kafka-topic.md#step-2-set-up-subscription-and-destination)
    * [Create AidboxSubscriptionTopic Resource](tutorial-produce-questionnaireresponse-to-kafka-topic.md#create-aidboxsubscriptiontopic-resource)
    * [Create AidboxTopicDestination Resource](tutorial-produce-questionnaireresponse-to-kafka-topic.md#create-aidboxtopicdestination-resource)
  * [Step 3: Demonstration](tutorial-produce-questionnaireresponse-to-kafka-topic.md#step-3-demonstration)
    * [Submit Form](tutorial-produce-questionnaireresponse-to-kafka-topic.md#submit-form)
    * [Check AidboxTopicDestination Status](tutorial-produce-questionnaireresponse-to-kafka-topic.md#check-aidboxtopicdestination-status)
    * [See Messages in Kafka UI](tutorial-produce-questionnaireresponse-to-kafka-topic.md#see-messages-in-kafka-ui)
  * [Example of Kubernetes Setup](tutorial-produce-questionnaireresponse-to-kafka-topic.md#example-of-kubernetes-setup)

### Prerequisites

* [Docker](https://www.docker.com/)
* Cloned repository: [Github: Aidbox/examples](https://github.com/Aidbox/examples/tree/main)
* Working directory: `aidbox-subscriptions-to-kafka`

### Step 1: Set Up the Environment

#### Set Up Aidbox

1.  Copy the `.env.tpl` file to `.env`:

    ```shell
    cp .env.tpl .env
    ```
2. Get a self-hosted Aidbox license from the [Aidbox Portal](https://aidbox.app/?utm_source=github\&utm_medium=readme\&utm_campaign=app-examples-repo).
3. Add the license key (`AIDBOX_LICENSE`) to the `.env` file.

#### Run Aidbox, Kafka & Kafka UI

```shell
docker compose up
```

* Aidbox is be available at [http://localhost:8888/](http://localhost:8888/)
  * Username: `admin`
  * Password: `password`
* Kafka UI is be available at [http://localhost:8080/](http://localhost:8080/)
* Kafka is available at `http://localhost:9092/` (no authorization required)

The Docker Compose file initializes the environment for both Kafka and Aidbox with the following configuration:

* Imports FHIR Questionnaire (see `init-aidbox` service).
* Creates a Kafka topic for `QuestionnaireResponse` (see `init-kafka` service).

### Step 2: Set Up Subscription and Destination

#### Create AidboxSubscriptionTopic Resource

To create a subscription on the `QuestionnaireResponse` resource that has a specific status, open Aidbox UI -> APIs -> REST Console and execute the following request:

```json
POST /fhir/AidboxSubscriptionTopic
content-type: application/json
accept: application/json

{
  "resourceType": "AidboxSubscriptionTopic",
  "url": "http://example.org/FHIR/R5/SubscriptionTopic/QuestionnaireResponse-topic",
  "status": "active",
  "trigger": [
    {
      "resource": "QuestionnaireResponse",
      "fhirPathCriteria": "status = 'completed' or status = 'amended'"
    }
  ]
}
```

##### Using `%current` and `%previous` in `fhirPathCriteria`

When working with resource updates, you can use special variables in your `fhirPathCriteria` to compare the current and previous state of a resource:

- `%current` - refers to the current state of the resource after an update
- `%previous` - refers to the previous state of the resource before an update

This allows for creating more specific triggers based on changes in resource values. For example:

```json
POST /fhir/AidboxSubscriptionTopic
content-type: application/json
accept: application/json

{
  "resourceType": "AidboxSubscriptionTopic",
  "url": "http://example.org/FHIR/R5/SubscriptionTopic/QuestionnaireResponse-status-change",
  "status": "active",
  "trigger": [
    {
      "resource": "QuestionnaireResponse",
      "supportedInteraction": ["update"],
      "fhirPathCriteria": "%previous.status = 'in-progress' and %current.status = 'completed'"
    }
  ]
}
```

On the create interaction `%previous` return `{}`. On the delete interaction `%current` return `{}`.

#### Create AidboxTopicDestination Resource

Creating this resource establishes a connection to the Kafka server. When the system produces an event, it will be processed to the specified Kafka topic.

```json
POST /fhir/AidboxTopicDestination
content-type: application/json
accept: application/json

{
  "meta": {
    "profile": [
      "http://aidbox.app/StructureDefinition/aidboxtopicdestination-kafka-at-least-once"
    ]
  },
  "kind": "kafka-at-least-once",
  "id": "kafka-destination",
  "topic": "http://example.org/FHIR/R5/SubscriptionTopic/QuestionnaireResponse-topic",
  "parameter": [
    {
      "name": "kafkaTopic",
      "valueString": "aidbox-forms"
    },
    {
      "name": "bootstrapServers",
      "valueString": "kafka:29092"
    }
  ]
}
```

### Step 3: Demonstration

#### Submit Form

Open the [list of forms](http://localhost:8888/ui/sdc#/), click `share` -> enable 'allow amend' -> click `attach` -> copy the link -> open the link -> fill out the form, and submit it.

#### Check AidboxTopicDestination Status

Open the Aidbox [REST Console](http://localhost:8888/ui/console#/rest) and get the AidboxTopicDestination status:

```
GET /fhir/AidboxTopicDestination/kafka-destination/$status
```

#### See Messages in Kafka UI

Open [Kafka UI](http://localhost:8080/) -> `Topics` -> `aidbox-forms` -> `messages` and review the `QuestionnaireResponse` that was created after submitting the form.

### Example of Kubernetes Setup

Also you can find example of k8s deployment:

* Configuration: [k8s.yaml](https://github.com/Aidbox/examples/tree/main/aidbox-features/aidbox-subscriptions-to-kafka/k8s.yaml)
* Also, you need to pass secrets for Aidbox and Database. See details: [Deploy Aidbox with Helm Charts](../../deployment-and-maintenance/deploy-aidbox/run-aidbox-in-kubernetes/deploy-aidbox-with-helm-charts.md). We recommend to use helm.
* Configuration resource examples: [k8s\_resources](https://github.com/Aidbox/examples/tree/main/aidbox-features/aidbox-subscriptions-to-kafka/k8s.yaml)

