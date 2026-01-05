---
description: Step-by-step guide to setting up FHIR R4 Backport Topic-Based Subscriptions in Aidbox.
---

# FHIR R4 Backport Subscription Setup

{% hint style="info" %}
This functionality is available in Aidbox versions 2512 and later and requires [FHIR Schema](../../modules/profiling-and-validation/fhir-schema-validator/) validation engine to be enabled. If you started Aidbox using the [Run Aidbox Locally](../../getting-started/run-aidbox-locally.md) guide, FHIR Schema is already enabled.
{% endhint %}

In this tutorial, we will set up Aidbox to implement the [DaVinci PAS SubscriptionTopic](https://build.fhir.org/ig/HL7/davinci-pas/SubscriptionTopic-PASSubscriptionTopic.json.html) (`http://hl7.org/fhir/us/davinci-pas/SubscriptionTopic/PASSubscriptionTopic`). This topic notifies subscribers when a Prior Authorization response (ClaimResponse) becomes available.

## Prerequisites
- An HTTP endpoint to receive notifications (for testing, you can use services like [RequestCatcher](https://requestcatcher.com/) or [Webhook.site](https://webhook.site/))

## Step 1: Install the Subscriptions Backport Package

Install the FHIR Subscriptions Backport IG package. This package contains the Subscription profile that Aidbox uses to validate Subscription resources created by clients:

```http
POST /fhir/$fhir-package-install
Content-Type: application/json

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "package",
      "valueString": "hl7.fhir.uv.subscriptions-backport.r4@1.1.0"
    }
  ]
}
```

## Step 2: Create AidboxSubscriptionTopic

Create an `AidboxSubscriptionTopic` that implements the PAS SubscriptionTopic logic:

```http
POST /fhir/AidboxSubscriptionTopic
Content-Type: application/json

{
  "resourceType": "AidboxSubscriptionTopic",
  "url": "http://example.org/SubscriptionTopic/pas-claim-response",
  "status": "active",
  "trigger": [
    {
      "resource": "ClaimResponse",
      "fhirPathCriteria": "status = 'active'",
      "canFilterBy": [
        {
          "description": "Filter by insurer organization",
          "filterParameter": "insurer",
          "filterDefinitionFhirPathExpression": "insurer",
          "comparator": ["eq"]
        }
      ]
    }
  ]
}
```

### Key Elements

| Element | Description |
|---------|-------------|
| `url` | Your internal canonical URL for this topic (NOT the PAS topic URL). This is used to link with `AidboxTopicDestination`. |
| `trigger.fhirPathCriteria` | Defines what "result-available" means in your specific system. In this example, we trigger when ClaimResponse status becomes `active`. Adjust this expression to match your business logic. |
| `trigger.canFilterBy.filterDefinitionFhirPathExpression` | FHIRPath expression to extract the filter value. Here `insurer` extracts the insurer reference. Adjust based on how organization identifiers work in your system. |

## Step 3: Create AidboxTopicDestination

Create an `AidboxTopicDestination` that binds your `AidboxSubscriptionTopic` to the FHIR SubscriptionTopic URL and enables FHIR Subscription support:

```http
POST /fhir/AidboxTopicDestination
Content-Type: application/json

{
  "resourceType": "AidboxTopicDestination",
  "meta": {
    "profile": [
      "http://aidbox.app/StructureDefinition/aidboxtopicdestination-fhir-native-topic-based-subscription"
    ]
  },
  "kind": "fhir-native-topic-based-subscription",
  "topic": "http://example.org/SubscriptionTopic/pas-claim-response",
  "status": "active",
  "content": "full-resource",
  "parameter": [
    {
      "name": "subscription-specification-version",
      "valueString": "R4-backported"
    },
    {
      "name": "fhir-topic",
      "valueCanonical": "http://hl7.org/fhir/us/davinci-pas/SubscriptionTopic/PASSubscriptionTopic"
    },
    {
      "name": "number-of-deliverer",
      "valueUnsignedInt": 2
    },
    {
      "name": "keep-events-for-period",
      "valueUnsignedInt": 86400
    }
  ]
}
```

### Key Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| `topic` | - | URL of your `AidboxSubscriptionTopic` from Step 2. |
| `fhir-topic` * | valueCanonical | The canonical FHIR SubscriptionTopic URL that clients will use in their Subscription resources. |
| `subscription-specification-version` * | valueString | FHIR spec version: `R4-backported`. |
| `keep-events-for-period` | valueUnsignedInt | Event retention period in seconds for `$events` operation. If omitted, events are not cleaned up. |
| `number-of-deliverer` | valueUnsignedInt | Number of parallel delivery workers (default: 1). |

\* required

## Step 4: Create Test Data

Create an Organization resource that will be used for filtering:

```http
PUT /fhir/Organization/org-1
Content-Type: application/json

{
  "resourceType": "Organization",
  "id": "org-1",
  "name": "Example Insurance Company"
}
```

## Step 5: Create a FHIR Subscription

Now clients can create standard FHIR Subscription resources:

```http
PUT /fhir/Subscription/pas-subscription-1
Content-Type: application/json

{
  "resourceType": "Subscription",
  "id": "pas-subscription-1",
  "meta": {
    "profile": [
      "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-subscription"
    ]
  },
  "status": "requested",
  "reason": "Receive notifications about claim responses for my organization.",
  "criteria": "http://hl7.org/fhir/us/davinci-pas/SubscriptionTopic/PASSubscriptionTopic",
  "_criteria": {
    "extension": [
      {
        "url": "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-filter-criteria",
        "valueString": "insurer=Organization/org-1"
      }
    ]
  },
  "channel": {
    "type": "rest-hook",
    "endpoint": "https://your-endpoint.example.com/webhook",
    "payload": "application/fhir+json",
    "_payload": {
      "extension": [
        {
          "url": "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-payload-content",
          "valueCode": "full-resource"
        }
      ]
    },
    "header": [
      "Authorization: Bearer your-token"
    ],
    "extension": [
      {
        "url": "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-heartbeat-period",
        "valueUnsignedInt": 60
      },
      {
        "url": "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-timeout",
        "valueUnsignedInt": 30
      },
      {
        "url": "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-max-count",
        "valuePositiveInt": 10
      }
    ]
  }
}
```

### Subscription Elements

| Element | Description |
|---------|-------------|
| `criteria` | Canonical URL of the SubscriptionTopic (must match `fhir-topic` in AidboxTopicDestination) |
| `_criteria.extension` | Filter criteria using parameters defined in `canFilterBy` |
| `channel.type` | Channel type: `rest-hook` |
| `channel.endpoint` | URL to receive notifications |
| `channel.payload` | MIME type for notifications |
| `channel._payload.extension` | Payload content: `full-resource`, `id-only`, or `empty` |
| `channel.header` | Custom HTTP headers for notifications |
| `channel.extension` | Channel settings: heartbeat period, timeout, max count |

## Step 6: Verify Handshake

After creating the subscription, Aidbox sends a handshake notification to your endpoint. If the handshake succeeds (HTTP 2xx response), the subscription status changes to `active`.

Check subscription status:

```http
GET /fhir/Subscription/pas-subscription-1
```

If handshake fails, the subscription status will be `error`.

## Step 7: Trigger Events

Create or update resources matching your topic criteria:

```http
POST /fhir/ClaimResponse
Content-Type: application/json

{
  "resourceType": "ClaimResponse",
  "status": "active",
  "type": {
    "coding": [
      {
        "system": "http://terminology.hl7.org/CodeSystem/claim-type",
        "code": "professional"
      }
    ]
  },
  "use": "preauthorization",
  "patient": {
    "reference": "Patient/example"
  },
  "created": "2024-01-01T00:00:00Z",
  "insurer": {
    "reference": "Organization/org-1"
  },
  "outcome": "complete"
}
```

Your endpoint will receive a notification bundle containing the ClaimResponse.

## Using $status Operation

Check subscription status and event counts:

```http
GET /fhir/Subscription/pas-subscription-1/$status
```

Response:

```json
{
  "resourceType": "Bundle",
  "type": "history",
  "entry": [
    {
      "resource": {
        "resourceType": "Parameters",
        "parameter": [
          {
            "name": "subscription",
            "valueReference": {
              "reference": "Subscription/pas-subscription-1"
            }
          },
          {
            "name": "status",
            "valueCode": "active"
          },
          {
            "name": "type",
            "valueCode": "query-status"
          },
          {
            "name": "events-since-subscription-start",
            "valueString": "5"
          }
        ]
      }
    }
  ]
}
```

## Using $events Operation

Query past events for recovery:

```http
POST /fhir/Subscription/pas-subscription-1/$events
Content-Type: application/json

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "eventsSinceNumber",
      "valueString": "1"
    },
    {
      "name": "eventsUntilNumber",
      "valueString": "10"
    },
    {
      "name": "content",
      "valueCode": "full-resource"
    }
  ]
}
```

## Limitations

- **No subscription updates**: Once created, subscriptions cannot be modified. Delete and recreate if changes are needed.
- **REST-hook only**: Currently only the `rest-hook` channel type is supported.
- **Content level**: Subscription's payload content cannot exceed the AidboxTopicDestination's `content` setting.

## See Also

- [FHIR Topic-Based Subscriptions](../../modules/topic-based-subscriptions/fhir-topic-based-subscriptions.md) - Overview and concepts
- [Subscriptions Backport IG](https://build.fhir.org/ig/HL7/fhir-subscription-backport-ig/) - Official specification
