# AidboxTopicSubscription RabbitMQ tutorial

## Objectives

* Learn how to integrate [AidboxTopicSubscriptions](../../modules/topic-based-subscriptions/aidbox-topic-based-subscriptions.md) with RabbitMQ

## Before you begin

* Make sure your Aidbox version is newer than 2504
* Setup the local Aidbox instance using getting started [guide](../../getting-started/run-aidbox-locally.md)

## What is RabbitMQ?

[RabbitMQ](https://www.rabbitmq.com/) is a reliable and mature messaging broker that implements the Advanced Message Queuing Protocol (AMQP). It provides robust message delivery, routing capabilities, and supports various messaging patterns including publish/subscribe, request/reply, and point-to-point communication.

In Aidbox, create [AidboxTopicDestination](../../modules/topic-based-subscriptions/aidbox-topic-based-subscriptions.md#aidboxtopicdestination) with `http://aidbox.app/StructureDefinition/aidboxtopicdestination-rabbitmq-core-best-effort` profile to integrate with RabbitMQ.

## Setting up

1. Add rabbitmq service to **docker-compose.yaml** from getting started [guide](../../getting-started/run-aidbox-locally.md):

   ```yaml
     rabbitmq:
       image: rabbitmq:3-management
       environment:
         RABBITMQ_DEFAULT_USER: admin
         RABBITMQ_DEFAULT_PASS: admin
       ports:
         - "5672:5672"
         - "15672:15672"
   ```

2. Download **.jar** RabbitMQ module file from [our bucket](https://console.cloud.google.com/storage/browser/aidbox-modules) and place it next to **docker-compose.yaml**.

   ```sh
   curl -O https://storage.googleapis.com/aidbox-modules/topic-destination-rabbitmq/topic-destination-rabbitmq-2509.1.jar
   ```


3. Add jar module file to Aidbox:
   ```yaml
       volumes:
       # module jar to turn on rabbitmq support
       - ./topic-destination-rabbitmq-2509.1.jar:/topic-destination-rabbitmq-2509.1.jar
   ```

   Set envs to import it on start:
   ```yaml
      BOX_MODULE_JAR: "/topic-destination-rabbitmq-2509.1.jar"
      BOX_MODULE_LOAD: io.healthsamurai.topic-destination.rabbitmq.core
   ```

4. Start the services.

   ```sh
   docker compose up
   ```

   Now, in AidboxUI, go to **FHIR Packages -> io.healthsamurai.topic** and make sure that RabbitMQ profile is present.

## Basic Usage

1. Access RabbitMQ Management UI at http://localhost:15672 (login: admin/admin).

2. Create a queue in RabbitMQ Management UI:
   - Go to **Queues and Streams** tab
   - Click **Add a new queue**
   - Name: `patient-events`
   - Type: Classic
   - Durability: Durable
   - Click **Add queue**

3. Go to AidboxUI and create a topic that triggers if `Patient.name` exists.

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

4. Create **AidboxTopicDestination** with RabbitMQ profile.

   ```
   POST /fhir/AidboxTopicDestination
   content-type: application/json
   accept: application/json

   {
     "id": "rabbitmq-basic",
     "resourceType": "AidboxTopicDestination",
     "meta": {
       "profile": [
         "http://aidbox.app/StructureDefinition/aidboxtopicdestination-rabbitmq-core-best-effort"
       ]
     },
     "kind": "rabbitmq-core-best-effort",
     "topic": "patient-topic",
     "parameter": [
       {
         "name": "host",
         "valueString": "rabbitmq"
       },
       {
         "name": "routingKey",
         "valueString": "patient-events"
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

5. Create a patient with a name.

   ```
   POST /fhir/Patient

   name:
   - family: smith
   ```

6. Check the message in RabbitMQ Management UI(http://localhost:15672):
   - Go to **Queues** tab
   - Click on `patient-events` queue
   - You should see 1 message in the queue
   - Click "Get messages" to view the content

   The message will contain:
   ```json
   {
     "topic": "patient-events",
     "value": {
       "resourceType": "Bundle",
       "type": "history",
       "timestamp": "2025-05-05T09:54:29Z",
       "entry": [
         {
           "resource": {
             "resourceType": "AidboxSubscriptionStatus",
             "status": "active",
             "type": "event-notification",
             "notificationEvent": [
               {
                 "eventNumber": 1,
                 "focus": {
                   "reference": "Patient/[patient-id]"
                 }
               }
             ],
             "topic": "patient-topic",
             "topic-destination": {
               "reference": "AidboxTopicDestination/rabbitmq-basic"
             }
           }
         },
         {
           "request": {
             "method": "POST",
             "url": "/fhir/Patient"
           },
           "fullUrl": "http://localhost:8080/fhir/Patient/[patient-id]",
           "resource": {
             "name": [{"family": "smith"}],
             "id": "[patient-id]",
             "resourceType": "Patient",
             "meta": {
               "lastUpdated": "2025-05-05T09:54:29.496342Z",
               "versionId": "1"
             }
           }
         }
       ]
     }
   }
   ```

## Monitoring

Check the status of your topic destination:

```
GET /fhir/AidboxTopicDestination/rabbitmq-basic/$status
```

Response will include metrics:
```json
{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "messagesDelivered",
      "valueInteger": 5
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
    }
  ]
}
```

## Configuration Parameters

The RabbitMQ topic destination supports the following parameters:

| Parameter | Required | Default | Description |
|-----------|----------|---------|-------------|
| `host` | Yes | - | RabbitMQ server hostname |
| `routingKey` | Yes | - | Routing key (queue name for default exchange) |
| `port` | No | 5672 | RabbitMQ server port |
| `vhost` | No | "/" | Virtual host |
| `exchange` | No | "" | Exchange name (empty for default exchange) |
| `username` | No | - | Username for authentication |
| `password` | No | - | Password for authentication |
| `connectionName` | No | "aidbox-{id}" | Connection name for monitoring |
| `ssl` | No | false | Enable SSL/TLS connection |
| `automaticallyRecover` | No | true | Enable automatic connection recovery |

## Troubleshooting

### Connection Issues

If you see connection errors:
- Verify RabbitMQ is running: `docker compose ps`
- Check RabbitMQ logs: `docker compose logs rabbitmq`
- Ensure the host parameter matches your RabbitMQ service name or IP

### Authentication Failures

If you see authentication errors:
- Verify username/password in AidboxTopicDestination parameters
- Check user permissions in RabbitMQ Management UI
- Default Docker setup uses admin/admin

### Messages Not Delivered

If messages aren't appearing in queues:
- Check Topic Destination status: `GET /fhir/AidboxTopicDestination/{id}/$status`
- Verify queue exists in RabbitMQ
- Check exchange bindings if using exchanges
- Ensure subscription topic is active and matches your resource criteria
