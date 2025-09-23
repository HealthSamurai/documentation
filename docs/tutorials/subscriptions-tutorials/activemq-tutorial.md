# AidboxTopicSubscription ActiveMQ tutorial

## Objectives

* Learn how to integrate [AidboxTopicSubscriptions](../../modules/topic-based-subscriptions/aidbox-topic-based-subscriptions.md) with ActiveMQ using AMQP 1.0 protocol

## Before you begin

* Make sure your Aidbox version is newer than 2504
* Setup the local Aidbox instance using getting started [guide](../../getting-started/run-aidbox-locally.md)

## What is ActiveMQ?

[Apache ActiveMQ](https://activemq.apache.org/) is a popular open source messaging and Integration Patterns server. ActiveMQ comes in two flavors:

* **ActiveMQ Classic**: The long-established broker with full JMS support
* **ActiveMQ Artemis**: The next generation broker with improved performance and AMQP 1.0 native support

Both versions support AMQP 1.0 protocol, making them compatible with Aidbox's `amqp-1-0` topic destination kind. This tutorial focuses on ActiveMQ Artemis, but the configuration works similarly for ActiveMQ Classic.

## Setting up

1. Add ActiveMQ Artemis service to **docker-compose.yaml** from getting started [guide](../../getting-started/run-aidbox-locally.md):

   ```yaml
     activemq-artemis:
       image: apache/activemq-artemis:latest-alpine
       environment:
         ARTEMIS_USER: admin
         ARTEMIS_PASSWORD: admin
         ANONYMOUS_LOGIN: "false"
         EXTRA_ARGS: "--http-host 0.0.0.0 --relax-jolokia"
         # Enable auto-creation of addresses and queues
         AMQ_EXTRA_ARGS: "--auto-create --default-queue-routing-type ANYCAST"
       ports:
         - "61616:61616"  # Core messaging port
         - "5672:5672"     # AMQP port
         - "8161:8161"     # Web Console port
   ```

   For ActiveMQ Classic (alternative option):
   ```yaml
     activemq-classic:
       image: apache/activemq-classic:latest
       environment:
         ACTIVEMQ_OPTS: "-Djetty.host=0.0.0.0"
         ACTIVEMQ_BROKER_NAME: activemq
         ACTIVEMQ_ADMIN_LOGIN: admin
         ACTIVEMQ_ADMIN_PASSWORD: admin
       ports:
         - "61616:61616"  # OpenWire port
         - "5672:5672"     # AMQP port
         - "8161:8161"     # Web Console port
   ```

2. Download **.jar** AMQP module file from [our bucket](https://console.cloud.google.com/storage/browser/aidbox-modules) and place it next to **docker-compose.yaml**.

   ```sh
   curl -O https://storage.googleapis.com/aidbox-modules/topic-destination-amqp/topic-destination-amqp-2509.4.jar
   ```

3. Add jar module file to Aidbox:
   ```yaml
       volumes:
       # module jar to turn on AMQP support
       - ./topic-destination-amqp-2509.4.jar:/topic-destination-amqp-2509.4.jar
   ```

   Set envs to import it on start:
   ```yaml
      BOX_MODULE_JAR: "/topic-destination-amqp-2509.4.jar"
      BOX_MODULE_LOAD: io.healthsamurai.topic-destination.amqp.core
   ```

4. Start the services.

   ```sh
   docker compose up
   ```

   Now, in AidboxUI, go to **FHIR Packages -> io.healthsamurai.topic** and make sure that AMQP profiles are present.

## Basic usage with AMQP 1.0

1. ActiveMQ Artemis is configured to auto-create queues when first accessed, so manual queue creation is optional.

   If you want to verify the Web Console is working, access it at http://localhost:8161 (login: admin/admin).
   - Look for **Artemis** in the tree menu
   - Navigate to **addresses** to see existing queues
   - Note: The address `patient-events` and queue `patient-queue` will be auto-created when we send the first message

2. Go to AidboxUI and create a topic that triggers if `Patient.name` exists.

   ```
   POST /fhir/AidboxSubscriptionTopic
   content-type: application/json
   accept: application/json

   {
     "resourceType": "AidboxSubscriptionTopic",
     "url": "patient-topic",
     "status": "active",
     "trigger": [
       {
         "resource": "Patient",
         "fhirPathCriteria": "name.exists()"
       }
     ]
   }
   ```

3. Create **AidboxTopicDestination** with AMQP 1.0 profile.

   ```
   POST /fhir/AidboxTopicDestination
   content-type: application/json
   accept: application/json

   {
     "id": "activemq-destination",
     "resourceType": "AidboxTopicDestination",
     "meta": {
       "profile": [
         "http://aidbox.app/StructureDefinition/aidboxtopicdestination-amqp-1-0-at-least-once"
       ]
     },
     "kind": "amqp-1-0-at-least-once",
     "topic": "patient-topic",
     "parameter": [
       {
         "name": "host",
         "valueString": "activemq-artemis"
       },
       {
         "name": "address",
         "valueString": "patient-events::patient-queue"
       },
       {
         "name": "username",
         "valueString": "admin"
       },
       {
         "name": "password",
         "valueString": "admin"
       }
     ]
   }
   ```

## Testing the integration

1. Create a patient with a name.

   ```
   POST /fhir/Patient

   name:
   - family: smith
   ```

2. Check the message in ActiveMQ Web Console (http://localhost:8161, admin/admin):

   For Artemis:
   - Navigate to **Artemis -> Addresses**, check that **patient-events** exists
   - Click on **Queues** tab
   - You should see the message count increased in **patient-events** queue
   - Click **patient-events** to view messages

   For Classic:
   - Navigate to **Queues**
   - Click on `patient-events`
   - Click **Browse** to view messages

   The message will contain:
   ```
   {"topic":"patient-events::patient-queue","value":{"resourceType":"Bundle","type":"history","timestamp":"2025-09-23T10:33:03Z","entry":[{"resource":{"resourceType":"AidboxSubscriptionStatus","status":"active","type":"event-notification","notificationEvent":, + 614 more
   ```

### Connection pooling

For high-volume scenarios, configure connection settings:

```json
{
  "parameter": [
    {
      "name": "idleTimeout",
      "valueInteger": 120  // Keep connection alive for 2 minutes
    }
    // ... other parameters
  ]
}
```

## Monitoring

Check the status of your topic destination:

```
GET /fhir/AidboxTopicDestination/activemq-destination/$status
```

Response will include metrics:
```json
{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "messagesDelivered",
      "valueInteger": 1
    },
    {
      "name": "messagesInProcess",
      "valueInteger": 0
    },
    {
      "name": "messagesQueued",
      "valueInteger": 0
    },
    {
      "name": "messagesDeliveryAttempts",
      "valueInteger": 0
    },
    {
      "name": "status",
      "valueString": "active"
    },
    {
      "name": "startTimestamp",
      "valueString": "2025-05-05T09:54:29Z"
    }
  ]
}
```

## Troubleshooting

### Connection issues

If you see connection errors:
- Verify ActiveMQ is running: `docker compose ps`
- Check ActiveMQ logs: `docker compose logs activemq-artemis`
- Ensure the host parameter matches your ActiveMQ service name
- Verify AMQP port (5672) is exposed and not blocked

### Messages not delivered

If messages aren't appearing in queues:
- Check Topic Destination status: `GET /fhir/AidboxTopicDestination/{id}/$status`
- Verify address/queue exists in ActiveMQ
- Check address routing type (ANYCAST for queue, MULTICAST for topic)
- **For Artemis**: Ensure you're using FQQN format (`address::queue`) in the address parameter to guarantee sender and receiver use the same queue
- Ensure subscription topic is active and matches your resource criteria
- Review ActiveMQ logs for any errors
