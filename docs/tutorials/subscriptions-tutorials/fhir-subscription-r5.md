---
description: Step-by-step guide to setting up FHIR R5 Topic-Based Subscriptions in Aidbox.
---

# FHIR R5 Subscription Setup

{% hint style="info" %}
This functionality is available in Aidbox versions 2601 and later and requires [FHIR Schema](../../modules/profiling-and-validation/fhir-schema-validator/) validation engine to be enabled and FHIR R5 core package (`hl7.fhir.r5.core#5.0.0`) to be installed.
{% endhint %}

For concepts, supported fields, and configuration details, see [FHIR Topic-Based Subscriptions](../../modules/topic-based-subscriptions/fhir-topic-based-subscriptions.md).

In this tutorial, we will set up Aidbox to implement the [DaVinci PAS SubscriptionTopic](https://build.fhir.org/ig/HL7/davinci-pas/SubscriptionTopic-PASSubscriptionTopic.json.html) (`http://hl7.org/fhir/us/davinci-pas/SubscriptionTopic/PASSubscriptionTopic`). This topic notifies subscribers when a Prior Authorization response (ClaimResponse) becomes available.

## Prerequisites

- Aidbox configured with FHIR R5 core package
- An HTTP endpoint to receive notifications (for testing, you can use services like [RequestCatcher](https://requestcatcher.com/) or [Webhook.site](https://webhook.site/))

## Step 1: Create AidboxSubscriptionTopic

Create an `AidboxSubscriptionTopic` that implements the PAS SubscriptionTopic logic:

Request:

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
          "filterParameter": "orgIdentifier",
          "filterDefinitionFhirPathExpression": "insurer",
          "comparator": ["eq"]
        }
      ]
    }
  ]
}
```

Response:

```json
{
  "url": "http://example.org/SubscriptionTopic/pas-claim-response",
  "status": "active",
  "trigger": [
    {
      "resource": "ClaimResponse",
      "canFilterBy": [
        {
          "comparator": ["eq"],
          "description": "Filter by insurer organization",
          "filterParameter": "orgIdentifier",
          "filterDefinitionFhirPathExpression": "insurer"
        }
      ],
      "fhirPathCriteria": "status = 'active'"
    }
  ],
  "id": "a1a1be63-defe-49a4-ba5d-101d4c88d3d6",
  "resourceType": "AidboxSubscriptionTopic",
  "meta": {
    "lastUpdated": "2026-01-09T12:47:51.891439Z",
    "versionId": "7"
  }
}
```

### Key Elements

| Element | Description |
|---------|-------------|
| `url` | Your internal canonical URL for this topic (NOT the PAS topic URL). This is used to link with `AidboxTopicDestination`. |
| `trigger.fhirPathCriteria` | Defines what "result-available" means in your specific system. In this example, we trigger when ClaimResponse status becomes `active`. Adjust this expression to match your business logic. |
| `trigger.canFilterBy.filterParameter` | The name that subscribers use in their filter criteria. Must match the parameter name defined in the FHIR SubscriptionTopic (e.g., `orgIdentifier` from [PAS SubscriptionTopic](https://build.fhir.org/ig/HL7/davinci-pas/SubscriptionTopic-PASSubscriptionTopic.json.html)). |
| `trigger.canFilterBy.filterDefinitionFhirPathExpression` | FHIRPath expression to extract the filter value from the resource. Here `insurer` extracts the insurer reference from ClaimResponse. |
| `trigger.canFilterBy.comparator` | Comparison operators allowed for this filter. Currently only `eq` (equals) is supported. |

## Step 2: Create AidboxTopicDestination

Create an `AidboxTopicDestination` that binds your `AidboxSubscriptionTopic` to the FHIR SubscriptionTopic URL and enables FHIR Subscription support:

Request:

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
      "valueString": "R5"
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

Response:

```json
{
  "meta": {
    "profile": [
      "http://aidbox.app/StructureDefinition/aidboxtopicdestination-fhir-native-topic-based-subscription"
    ],
    "lastUpdated": "2026-01-09T12:48:13.899936Z",
    "versionId": "8"
  },
  "content": "full-resource",
  "topic": "http://example.org/SubscriptionTopic/pas-claim-response",
  "resourceType": "AidboxTopicDestination",
  "status": "active",
  "id": "997b2cee-d6e8-4bf5-9e32-a356f2e7360e",
  "kind": "fhir-native-topic-based-subscription",
  "parameter": [
    {
      "name": "subscription-specification-version",
      "valueString": "R5"
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
| `topic` | - | URL of your `AidboxSubscriptionTopic` from Step 1. |
| `fhir-topic` * | valueCanonical | The canonical FHIR SubscriptionTopic URL that clients will use in their Subscription resources. |
| `subscription-specification-version` * | valueString | Set to `R5` for native FHIR R5 Subscription format. Determines the format of notification bundles and operation responses. |
| `keep-events-for-period` | valueUnsignedInt | Event retention period in seconds for `$events` operation. If not specified, events are stored indefinitely. |
| `number-of-deliverer` | valueUnsignedInt | Number of parallel delivery workers (default: 4). |

\* required

## Step 3: Create Test Data

Create the Organization and Patient resources that will be referenced:

Request:

```http
PUT /fhir/Organization/org-1
Content-Type: application/json

{
  "resourceType": "Organization",
  "id": "org-1",
  "name": "Example Insurance Company"
}
```

Response:

```json
{
  "name": "Example Insurance Company",
  "id": "org-1",
  "resourceType": "Organization",
  "meta": {
    "lastUpdated": "2026-01-09T12:48:20.075930Z",
    "versionId": "9"
  }
}
```

Request:

```http
PUT /fhir/Patient/example
Content-Type: application/json

{
  "resourceType": "Patient",
  "id": "example",
  "name": [{"family": "Test", "given": ["Patient"]}]
}
```

Response:

```json
{
  "name": [{"family": "Test", "given": ["Patient"]}],
  "id": "example",
  "resourceType": "Patient",
  "meta": {
    "lastUpdated": "2026-01-09T12:48:20.229755Z",
    "versionId": "10"
  }
}
```

## Step 4: Create a FHIR R5 Subscription

Now clients can create standard FHIR R5 Subscription resources. Unlike R4 Backport subscriptions, R5 uses native fields instead of extensions:

Request:

```http
POST /fhir/Subscription
Content-Type: application/json

{
  "resourceType": "Subscription",
  "status": "requested",
  "reason": "Receive notifications about claim responses for my organization.",
  "topic": "http://hl7.org/fhir/us/davinci-pas/SubscriptionTopic/PASSubscriptionTopic",
  "filterBy": [
    {
      "filterParameter": "orgIdentifier",
      "comparator": "eq",
      "value": "Organization/org-1"
    }
  ],
  "channelType": {
    "system": "http://terminology.hl7.org/CodeSystem/subscription-channel-type",
    "code": "rest-hook"
  },
  "endpoint": "https://your-endpoint.example.com/webhook",
  "heartbeatPeriod": 60,
  "timeout": 30,
  "contentType": "application/fhir+json",
  "content": "full-resource",
  "maxCount": 10,
  "parameter": [
    {
      "name": "Authorization",
      "value": "Bearer your-token"
    }
  ]
}
```

Response:

```json
{
  "maxCount": 10,
  "meta": {
    "lastUpdated": "2026-01-09T12:49:38.968637Z",
    "versionId": "13"
  },
  "content": "full-resource",
  "topic": "http://hl7.org/fhir/us/davinci-pas/SubscriptionTopic/PASSubscriptionTopic",
  "resourceType": "Subscription",
  "reason": "Receive notifications about claim responses for my organization.",
  "heartbeatPeriod": 60,
  "status": "requested",
  "id": "pas-subscription-r5",
  "contentType": "application/fhir+json",
  "timeout": 30,
  "filterBy": [
    {
      "value": "Organization/org-1",
      "comparator": "eq",
      "filterParameter": "orgIdentifier"
    }
  ],
  "endpoint": "https://your-endpoint.example.com/webhook",
  "parameter": [
    {
      "name": "Authorization",
      "value": "Bearer your-token"
    }
  ],
  "channelType": {
    "code": "rest-hook",
    "system": "http://terminology.hl7.org/CodeSystem/subscription-channel-type"
  }
}
```

### Subscription Elements

| Element | Description |
|---------|-------------|
| `topic` | Canonical URL of the SubscriptionTopic (must match `fhir-topic` in AidboxTopicDestination) |
| `filterBy` | Filter criteria using parameters defined in `canFilterBy`. Each filter has `filterParameter`, `comparator`, and `value`. |
| `channelType` | Channel type as a Coding. Only `rest-hook` is supported. |
| `endpoint` | URL to receive notifications |
| `contentType` | MIME type for notifications (e.g., `application/fhir+json`) |
| `content` | Payload detail: `full-resource`, `id-only`, or `empty` |
| `parameter` | Custom HTTP headers for notifications. Each parameter has `name` (header name) and `value` (header value). |
| `heartbeatPeriod` | Interval in seconds for sending keep-alive notifications during inactivity. |
| `timeout` | Maximum time in seconds the server will wait before failing a notification delivery attempt. |
| `maxCount` | Maximum number of events to include in a single notification bundle. |

## Step 5: Verify Handshake

After creating the subscription, Aidbox sends a handshake notification to your endpoint. If the handshake succeeds (HTTP 2xx response), the subscription status changes to `active`.

Check subscription status:

Request:

```http
GET /fhir/Subscription/pas-subscription-r5
```

Response:

```json
{
  "maxCount": 10,
  "meta": {
    "lastUpdated": "2026-01-09T12:50:06.398123Z",
    "versionId": "14"
  },
  "content": "full-resource",
  "topic": "http://hl7.org/fhir/us/davinci-pas/SubscriptionTopic/PASSubscriptionTopic",
  "resourceType": "Subscription",
  "reason": "Receive notifications about claim responses for my organization.",
  "heartbeatPeriod": 60,
  "status": "active",
  "id": "pas-subscription-r5",
  "contentType": "application/fhir+json",
  "timeout": 30,
  "filterBy": [
    {
      "value": "Organization/org-1",
      "comparator": "eq",
      "filterParameter": "orgIdentifier"
    }
  ],
  "endpoint": "https://your-endpoint.example.com/webhook",
  "parameter": [
    {
      "name": "Authorization",
      "value": "Bearer your-token"
    }
  ],
  "channelType": {
    "code": "rest-hook",
    "system": "http://terminology.hl7.org/CodeSystem/subscription-channel-type"
  }
}
```

If handshake fails, the subscription status will be `error`.

## Step 6: Trigger Events

Create or update resources matching your topic criteria:

Request:

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

Response:

```json
{
  "use": "preauthorization",
  "type": {
    "coding": [
      {
        "code": "professional",
        "system": "http://terminology.hl7.org/CodeSystem/claim-type"
      }
    ]
  },
  "status": "active",
  "created": "2024-01-01T00:00:00Z",
  "insurer": {
    "reference": "Organization/org-1"
  },
  "outcome": "complete",
  "patient": {
    "reference": "Patient/example"
  },
  "id": "9a13a867-a7da-4ba7-9c09-b4aa193098ad",
  "resourceType": "ClaimResponse",
  "meta": {
    "lastUpdated": "2026-01-09T12:50:30.650900Z",
    "versionId": "16"
  }
}
```

Your endpoint will receive a notification bundle containing the ClaimResponse.

## Using $status Operation

Check subscription status and event counts:

Request:

```http
GET /fhir/Subscription/pas-subscription-r5/$status
```

Response:

```json
{
  "resourceType": "Bundle",
  "type": "subscription-notification",
  "timestamp": "2026-01-09T12:51:00.894972Z",
  "entry": [
    {
      "fullUrl": "urn:uuid:f673700e-b619-4b9d-8515-3bfdeb3a51d4",
      "request": {
        "method": "GET",
        "url": "http://localhost:8765/Subscription/pas-subscription-r5/$status"
      },
      "response": {
        "status": "200"
      },
      "resource": {
        "resourceType": "SubscriptionStatus",
        "id": "f673700e-b619-4b9d-8515-3bfdeb3a51d4",
        "type": "query-status",
        "topic": "http://hl7.org/fhir/us/davinci-pas/SubscriptionTopic/PASSubscriptionTopic",
        "status": "active",
        "subscription": {
          "reference": "http://localhost:8765/Subscription/pas-subscription-r5"
        },
        "eventsSinceSubscriptionStart": 1
      }
    }
  ]
}
```

## Using $events Operation

Query past events for recovery:

Request:

```http
POST /fhir/Subscription/pas-subscription-r5/$events
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

Response:

```json
{
  "resourceType": "Bundle",
  "type": "subscription-notification",
  "timestamp": "2026-01-09T12:51:30.769005Z",
  "entry": [
    {
      "fullUrl": "urn:uuid:dbb5131e-fbee-4128-b274-9ee55c36faf9",
      "request": {
        "method": "GET",
        "url": "http://localhost:8765/Subscription/pas-subscription-r5/$status"
      },
      "response": {
        "status": "200"
      },
      "resource": {
        "resourceType": "SubscriptionStatus",
        "id": "dbb5131e-fbee-4128-b274-9ee55c36faf9",
        "type": "query-event",
        "topic": "http://hl7.org/fhir/us/davinci-pas/SubscriptionTopic/PASSubscriptionTopic",
        "status": "active",
        "subscription": {
          "reference": "http://localhost:8765/Subscription/pas-subscription-r5"
        },
        "notificationEvent": [
          {
            "eventNumber": 1,
            "focus": {
              "reference": "http://localhost:8765/fhir/ClaimResponse/9a13a867-a7da-4ba7-9c09-b4aa193098ad"
            },
            "timestamp": "2026-01-09T12:50:30.650900Z"
          }
        ],
        "eventsSinceSubscriptionStart": 1
      }
    },
    {
      "fullUrl": "http://localhost:8765/fhir/ClaimResponse/9a13a867-a7da-4ba7-9c09-b4aa193098ad",
      "resource": {
        "patient": {
          "reference": "Patient/example"
        },
        "meta": {
          "versionId": "16",
          "lastUpdated": "2026-01-09T12:50:30.650900Z"
        },
        "use": "preauthorization",
        "type": {
          "coding": [
            {
              "code": "professional",
              "system": "http://terminology.hl7.org/CodeSystem/claim-type"
            }
          ]
        },
        "created": "2024-01-01T00:00:00Z",
        "outcome": "complete",
        "resourceType": "ClaimResponse",
        "insurer": {
          "reference": "Organization/org-1"
        },
        "status": "active",
        "id": "9a13a867-a7da-4ba7-9c09-b4aa193098ad"
      },
      "request": {
        "method": "POST",
        "url": "http://localhost:8765/fhir/ClaimResponse"
      },
      "response": {
        "status": "201"
      }
    }
  ]
}
```

## Notification Bundle Examples

### Handshake Notification

Sent immediately after subscription creation to verify the endpoint:

```json
{
  "resourceType": "Bundle",
  "type": "subscription-notification",
  "timestamp": "2026-01-09T12:50:06.335764Z",
  "entry": [
    {
      "fullUrl": "urn:uuid:73d9ea0c-7667-4d48-8359-dd74a48b9063",
      "request": {
        "method": "GET",
        "url": "http://localhost:8765/Subscription/pas-subscription-r5/$status"
      },
      "response": {
        "status": "200"
      },
      "resource": {
        "resourceType": "SubscriptionStatus",
        "id": "73d9ea0c-7667-4d48-8359-dd74a48b9063",
        "type": "handshake",
        "topic": "http://hl7.org/fhir/us/davinci-pas/SubscriptionTopic/PASSubscriptionTopic",
        "status": "requested",
        "subscription": {
          "reference": "http://localhost:8765/Subscription/pas-subscription-r5"
        },
        "eventsSinceSubscriptionStart": 0
      }
    }
  ]
}
```

### Event Notification

Sent when a resource matches the topic trigger criteria:

```json
{
  "resourceType": "Bundle",
  "type": "subscription-notification",
  "timestamp": "2026-01-09T12:50:31.482976Z",
  "entry": [
    {
      "fullUrl": "urn:uuid:8342795b-65cd-4c28-8846-6caad72b3bdd",
      "request": {
        "method": "GET",
        "url": "http://localhost:8765/Subscription/pas-subscription-r5/$status"
      },
      "response": {
        "status": "200"
      },
      "resource": {
        "resourceType": "SubscriptionStatus",
        "id": "8342795b-65cd-4c28-8846-6caad72b3bdd",
        "type": "event-notification",
        "topic": "http://hl7.org/fhir/us/davinci-pas/SubscriptionTopic/PASSubscriptionTopic",
        "status": "active",
        "subscription": {
          "reference": "http://localhost:8765/Subscription/pas-subscription-r5"
        },
        "notificationEvent": [
          {
            "eventNumber": 1,
            "focus": {
              "reference": "http://localhost:8765/fhir/ClaimResponse/9a13a867-a7da-4ba7-9c09-b4aa193098ad"
            },
            "timestamp": "2026-01-09T12:50:30.650900Z"
          }
        ],
        "eventsSinceSubscriptionStart": 1
      }
    },
    {
      "fullUrl": "http://localhost:8765/fhir/ClaimResponse/9a13a867-a7da-4ba7-9c09-b4aa193098ad",
      "resource": {
        "patient": {
          "reference": "Patient/example"
        },
        "meta": {
          "versionId": "16",
          "lastUpdated": "2026-01-09T12:50:30.650900Z"
        },
        "use": "preauthorization",
        "type": {
          "coding": [
            {
              "code": "professional",
              "system": "http://terminology.hl7.org/CodeSystem/claim-type"
            }
          ]
        },
        "created": "2024-01-01T00:00:00Z",
        "outcome": "complete",
        "resourceType": "ClaimResponse",
        "insurer": {
          "reference": "Organization/org-1"
        },
        "status": "active",
        "id": "9a13a867-a7da-4ba7-9c09-b4aa193098ad"
      },
      "request": {
        "method": "POST",
        "url": "http://localhost:8765/fhir/ClaimResponse"
      },
      "response": {
        "status": "201"
      }
    }
  ]
}
```

### Heartbeat Notification

Sent periodically during inactivity based on `heartbeatPeriod`:

```json
{
  "resourceType": "Bundle",
  "type": "subscription-notification",
  "timestamp": "2026-01-09T12:51:31.483388Z",
  "entry": [
    {
      "fullUrl": "urn:uuid:094803cd-db7e-4b8d-b3e1-aca500068e8f",
      "request": {
        "method": "GET",
        "url": "http://localhost:8765/Subscription/pas-subscription-r5/$status"
      },
      "response": {
        "status": "200"
      },
      "resource": {
        "resourceType": "SubscriptionStatus",
        "id": "094803cd-db7e-4b8d-b3e1-aca500068e8f",
        "type": "heartbeat",
        "topic": "http://hl7.org/fhir/us/davinci-pas/SubscriptionTopic/PASSubscriptionTopic",
        "status": "active",
        "subscription": {
          "reference": "http://localhost:8765/Subscription/pas-subscription-r5"
        },
        "eventsSinceSubscriptionStart": 1
      }
    }
  ]
}
```

## Limitations

- **No subscription updates**: Once created, subscriptions cannot be modified. Delete and recreate if changes are needed.
- **REST-hook only**: Currently only the [`rest-hook`](https://hl7.org/fhir/R5/valueset-subscription-channel-type.html) channel type is supported.
- **Content level**: Subscription's payload content cannot exceed the AidboxTopicDestination's `content` setting.

## See Also

- [FHIR Topic-Based Subscriptions](../../modules/topic-based-subscriptions/fhir-topic-based-subscriptions.md) - Overview and concepts
- [FHIR R4B Backport Subscription Setup](fhir-subscription-r4b-backport.md) - Tutorial for R4B Backport format
- [FHIR R4 Backport Subscription Setup](fhir-subscription-r4-backport.md) - Tutorial for R4 Backport format
- [FHIR R5 Subscription](https://hl7.org/fhir/R5/subscription.html) - Official R5 specification
