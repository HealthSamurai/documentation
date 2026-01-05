---
description: FHIR Topic-Based Subscriptions support for standard FHIR R4/R5 subscription interoperability.
---

# FHIR Topic-Based Subscriptions

{% hint style="info" %}
This functionality is available in Aidbox versions 2512 and later and requires [FHIR Schema](../profiling-and-validation/fhir-schema-validator/) validation engine to be [enabled](../profiling-and-validation/fhir-schema-validator/).
{% endhint %}

## Overview

Aidbox provides full support for the [FHIR Topic-Based Subscriptions Framework](https://build.fhir.org/subscriptions.html) according to the specification. This allows external systems to create and manage FHIR `Subscription` resources to receive notifications about data changes.

### When to use FHIR Subscriptions

Use FHIR Topic-Based Subscriptions when you:

- Need conformance to the FHIR subscription specification
- Need to provide the ability for external systems or customers to create their own subscriptions
- Expect many subscribers (tens to thousands) with different filters to the same topic

### When to use Aidbox Destinations instead

For simple subscriptions used for internal integrations, [Aidbox Topic Destinations](aidbox-topic-based-subscriptions.md) (Kafka, Webhook, GCP Pub/Sub, etc.) may work better as they require less setup and are managed server-side.

## Key Components

FHIR Topic-Based Subscriptions build on the [Aidbox Topic-Based Subscriptions](aidbox-topic-based-subscriptions.md) infrastructure, using the same core resources with additional configuration for FHIR-standard subscription support.

When you need to support a specific FHIR SubscriptionTopic in your installation (e.g., `http://hl7.org/fhir/us/davinci-pas/SubscriptionTopic/PASSubscriptionTopic`):

1. **Create an `AidboxSubscriptionTopic`** that implements the topic's triggers and filters
2. **Create an `AidboxTopicDestination`** that binds it to the canonical FHIR SubscriptionTopic URL

Once configured, clients can create standard FHIR `Subscription` resources referencing the topic URL.

### AidboxSubscriptionTopic

[`AidboxSubscriptionTopic`](aidbox-topic-based-subscriptions.md#aidboxsubscriptiontopic) is a custom Aidbox resource that models the FHIR [SubscriptionTopic](https://build.fhir.org/subscriptiontopic.html) resource.

**Why a custom resource?** FHIR SubscriptionTopic is not always fully computable — it often uses codes to describe event triggers that require human interpretation. Aidbox uses FHIRPath expressions to make triggers computable.

For example, the [DaVinci PAS SubscriptionTopic](https://build.fhir.org/ig/HL7/davinci-pas/SubscriptionTopic-PASSubscriptionTopic.json.html) defines an event trigger using a code:

```json
"eventTrigger": [{
  "description": "When a new result is made ready by the intermediary system",
  "event": {
    "coding": [{
      "system": "http://hl7.org/fhir/us/davinci-pas/CodeSystem/PASTempCodes",
      "code": "result-available"
    }]
  },
  "resource": "http://hl7.org/fhir/StructureDefinition/ClaimResponse"
}]
```

In Aidbox, you would model this with a computable FHIRPath expression:

```json
{
  "resourceType": "AidboxSubscriptionTopic",
  "url": "http://hl7.org/fhir/us/davinci-pas/SubscriptionTopic/PASSubscriptionTopic",
  "status": "active",
  "trigger": [{
    "resource": "ClaimResponse",
    "fhirPathCriteria": "status = 'active'"
  }]
}
```

{% hint style="info" %}
What "result-available" means may vary from installation to installation. The FHIRPath expression should match your specific business logic.
{% endhint %}

### canFilterBy

The `canFilterBy` element in `AidboxSubscriptionTopic` defines what filter parameters subscribers can use when creating their subscriptions. This allows subscribers to receive only events that match their specific criteria.

| Field | Description |
|-------|-------------|
| `filterParameter` | Name of the filter parameter that subscribers will use |
| `filterDefinitionFhirPathExpression` | FHIRPath expression to extract the filter value from the resource |
| `resource` | Resource type this filter applies to |
| `comparator` | Supported comparators (e.g., `["eq"]`) |
| `description` | Human-readable description of the filter |

```json
{
  "resourceType": "AidboxSubscriptionTopic",
  "url": "http://example.org/SubscriptionTopic/claim-response-topic",
  "status": "active",
  "trigger": [{
    "resource": "ClaimResponse",
    "fhirPathCriteria": "status = 'active'"
  }],
  "canFilterBy": [{
    "description": "Filter by insurer organization",
    "resource": "ClaimResponse",
    "filterParameter": "insurer",
    "filterDefinitionFhirPathExpression": "insurer",
    "comparator": ["eq"]
  }]
}
```

Subscribers can then specify filter criteria in their `Subscription` resource to receive only events matching their filters (e.g., only ClaimResponses for a specific insurer).

### AidboxTopicDestination

[`AidboxTopicDestination`](aidbox-topic-based-subscriptions.md#aidboxtopicdestination) enables an `AidboxSubscriptionTopic` to be available for FHIR Subscriptions. It acts as a bridge between the topic definition and the FHIR Subscription API.

For FHIR Subscriptions, use the `fhir-native-topic-based-subscription` kind with the following parameters:

| Parameter | Type | Description |
|-----------|------|-------------|
| `fhir-topic` | valueCanonical | Canonical URL of the FHIR SubscriptionTopic this destination implements |
| `subscription-specification-version` | valueString | FHIR version of the Subscription spec to support (see note below) |
| `keep-events-for-period` | valueUnsignedInt | How long events remain available for the `$events` operation (in seconds) |
| `number-of-deliverer` | valueUnsignedInt | Number of parallel delivery workers |

{% hint style="info" %}
Currently only `R4-backported` specification version is supported. [Contact us](https://www.health-samurai.io/contacts) if you need support for other versions (R5, R6).
{% endhint %}

```json
{
  "resourceType": "AidboxTopicDestination",
  "meta": {
    "profile": ["http://aidbox.app/StructureDefinition/aidboxtopicdestination-fhir-native-topic-based-subscription"]
  },
  "kind": "fhir-native-topic-based-subscription",
  "topic": "http://example.org/SubscriptionTopic/claim-response-topic",
  "parameter": [
    {
      "name": "fhir-topic",
      "valueUrl": "http://hl7.org/fhir/us/davinci-pas/SubscriptionTopic/PASSubscriptionTopic"
    },
    {
      "name": "subscription-specification-version",
      "valueString": "R4-backported"
    },
    {
      "name": "keep-events-for-period",
      "valueInteger": 86400
    },
    {
      "name": "number-of-deliverer",
      "valueInteger": 2
    }
  ]
}
```

### FHIR Subscription

Once the topic and destination are configured, clients can create standard FHIR `Subscription` resources to subscribe to events.

{% hint style="warning" %}
Only `rest-hook` channel type is supported. Other channel types (`websocket`, `email`, `message`) are not available.
{% endhint %}

For the R4 Backport profile structure and parameters, see the [Backport Subscription Profile](https://build.fhir.org/ig/HL7/fhir-subscription-backport-ig/StructureDefinition-backport-subscription.html).

## Subscription Lifecycle

1. **Requested**: Client creates a Subscription resource with `status: requested`. The subscription is not yet active.

2. **Handshake**: Server immediately sends a handshake notification to the configured endpoint to validate it can receive notifications. The handshake bundle contains a `SubscriptionStatus` with `type: handshake`.

3. **Active**: If the endpoint responds with HTTP 2xx, the subscription status changes to `active` and starts receiving events. If handshake fails, status changes to `error`.

4. **Event notifications**: When resources matching the topic trigger criteria are created/updated/deleted, notification bundles are sent to the endpoint containing the `SubscriptionStatus` and triggered resources. Events are batched up to `max-count` before sending, or sent earlier if heartbeat period is reached.

5. **Heartbeat**: If configured via `backport-heartbeat-period`, periodic empty notifications are sent during inactivity to confirm the connection is alive.

6. **Expiration/Off**: Subscription stops receiving events when:
   - The `end` date is reached
   - The subscription is deleted

## Operations

Only instance-level operations are supported (not the batch/server-level variants).

### $status

Returns the current status of a subscription including event counts.

**URL:** `GET|POST [base]/Subscription/[id]/$status`

**Parameters:** None

**Response:** Bundle with `SubscriptionStatus` containing:
- `status` - current subscription status (`active`, `error`)
- `events-since-subscription-start` - total event count
- `error` - error details if delivery failed

See: [Subscription Status Operation](https://build.fhir.org/ig/HL7/fhir-subscription-backport-ig/OperationDefinition-backport-subscription-status.html)

### $events

Allows clients to query past events for recovery purposes.

**URL:** `POST [base]/Subscription/[id]/$events`

**Parameters:**

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `eventsSinceNumber` | string | `eventsUntilNumber - 100` | Start of event range (inclusive) |
| `eventsUntilNumber` | string | Last event number | End of event range (inclusive) |
| `content` | code | Subscription's content level | `full-resource`, `id-only`, or `empty` |

**Defaults:** If no parameters provided, returns up to 100 most recent events.

See: [Subscription Events Operation](https://build.fhir.org/ig/HL7/fhir-subscription-backport-ig/OperationDefinition-backport-subscription-events.html)

## Notification Bundle

The notification bundle format follows the selected `subscription-specification-version`.

For R4-backported subscriptions, see:
- [Notification Bundle Example](https://build.fhir.org/ig/HL7/fhir-subscription-backport-ig/Bundle-r4-notification-full-resource.json.html)
- [All Backport IG Artifacts](https://build.fhir.org/ig/HL7/fhir-subscription-backport-ig/artifacts.html)

## Setup Tutorial

See [FHIR R4 Backport Subscription Setup](../../tutorials/subscriptions-tutorials/fhir-subscription-r4-backport.md) for a step-by-step guide.
