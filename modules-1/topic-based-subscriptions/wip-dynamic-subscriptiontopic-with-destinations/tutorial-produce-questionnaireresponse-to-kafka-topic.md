# Tutorial: produce QuestionnaireResponse to Kafka topic

### Prerequisites

* [Docker](https://www.docker.com/)
* Aidbox license. Get a self-hosted Aidbox license on the [Aidbox Portal](https://aidbox.app/).

### Step 1: Set Up the Environment

#### Clone GitHub repository

```bash
git clone https://github.com/Aidbox/app-examples
cd app-examples/aidbox-subscriptions-to-kafka
```

#### Set Up Aidbox

1.  Copy the `.env.tpl` file to `.env`:

    ```shell
    cp .env.tpl .env
    ```
2. Get a self-hosted Aidbox license on the [Aidbox Portal](https://aidbox.app/).
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

This resource describes the data source for the subscription but doesn't execute any activities from Aidbox.

#### Create TopicDestination Resource

Creating this resource establishes a connection to the Kafka server. When the system produces an event, it will be processed to the specified Kafka topic.

```json
POST /fhir/TopicDestination
content-type: application/json
accept: application/json

{
  "meta": {
    "profile": [
      "http://fhir.aidbox.app/StructureDefinition/TopicDestinationKafka"
    ]
  },
  "kind": "kafka",
  "id": "kafka-destination",
  "topic": "http://example.org/FHIR/R5/SubscriptionTopic/QuestionnaireResponse-topic",
  "parameter": [
    {
      "name": "kafkaTopic",
      "valueString": "aidbox-forms"
    },
    {
      "name": "bootstrap.servers",
      "valueString": "kafka:29092"
    }
  ]
}
```

### Step 3: Demonstration

#### Submit Form

Open the [list of forms](http://localhost:8888/ui/sdc#/), click `share` -> click `attach` -> copy the link -> open the link -> fill out the form, and submit it.

#### Check TopicDestination Status

Open the Aidbox [REST Console](http://localhost:8888/ui/console#/rest) and get the TopicDestination status:

```
GET /fhir/TopicDestination/kafka-destination/$status
```

#### Check Messages in Kafka UI

Open [Kafka UI](http://localhost:8080/) -> `Topics` -> `aidbox-forms` -> `messages` and review the `QuestionnaireResponse` that was created after submitting the form.
