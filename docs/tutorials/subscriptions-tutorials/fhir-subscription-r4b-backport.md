---
description: Step-by-step guide to setting up FHIR R4B Backport Topic-Based Subscriptions in Aidbox.
---

# FHIR R4B Backport Subscription Setup

{% hint style="info" %}
This functionality is available in Aidbox versions 2601 and later and requires [FHIR Schema](../../modules/profiling-and-validation/fhir-schema-validator/) validation engine to be enabled and FHIR R4B core package (`hl7.fhir.r4b.core#4.3.0`) to be installed.
{% endhint %}

For concepts, supported fields, and configuration details, see [FHIR Topic-Based Subscriptions](../../modules/topic-based-subscriptions/fhir-topic-based-subscriptions.md).

In this tutorial, we will set up Aidbox to implement the [DaVinci PAS SubscriptionTopic](https://build.fhir.org/ig/HL7/davinci-pas/SubscriptionTopic-PASSubscriptionTopic.json.html) (`http://hl7.org/fhir/us/davinci-pas/SubscriptionTopic/PASSubscriptionTopic`). This topic notifies subscribers when a Prior Authorization response (ClaimResponse) becomes available.

R4B Backport combines the R4 Backport subscription format (with extensions) with R5-style notification bundles (using `SubscriptionStatus` resource).

## Prerequisites

- Aidbox configured with FHIR R4B core package
- An HTTP endpoint to receive notifications (for testing, you can use services like [RequestCatcher](https://requestcatcher.com/) or [Webhook.site](https://webhook.site/))

## Step 1: Install the Subscriptions Backport Package

Install the FHIR Subscriptions Backport IG package for R4B:

{% tabs %}
{% tab title="Request" %}
```http
POST /fhir/$fhir-package-install
Content-Type: application/json

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "package",
      "valueString": "hl7.fhir.uv.subscriptions-backport.r4b@1.1.0"
    }
  ]
}
```
{% endtab %}
{% tab title="Response" %}
```json
{
  "resourceType": "Parameters",
  "parameter": [
    {"name": "result", "valueBoolean": true},
    {"name": "package", "part": [
      {"name": "name", "valueString": "hl7.fhir.uv.subscriptions-backport.r4b@1.1.0"},
      {"name": "installedCanonicals", "valueInteger": 23}
    ]}
  ]
}
```
{% endtab %}
{% endtabs %}

## Step 2: Create AidboxSubscriptionTopic

Create an `AidboxSubscriptionTopic` that implements the PAS SubscriptionTopic logic:

{% tabs %}
{% tab title="Request" %}
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
{% endtab %}
{% tab title="Response" %}
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
  "id": "c01fb7db-a306-402b-8234-cee3bb1e4cb5",
  "resourceType": "AidboxSubscriptionTopic",
  "meta": {
    "lastUpdated": "2026-01-09T13:15:31.201049Z",
    "versionId": "7"
  }
}
```
{% endtab %}
{% endtabs %}

### Key Elements

| Element | Description |
|---------|-------------|
| `url` | Your internal canonical URL for this topic (NOT the PAS topic URL). This is used to link with `AidboxTopicDestination`. |
| `trigger.fhirPathCriteria` | Defines what "result-available" means in your specific system. In this example, we trigger when ClaimResponse status becomes `active`. Adjust this expression to match your business logic. |
| `trigger.canFilterBy.filterParameter` | The name that subscribers use in their filter criteria. Must match the parameter name defined in the FHIR SubscriptionTopic (e.g., `orgIdentifier` from [PAS SubscriptionTopic](https://build.fhir.org/ig/HL7/davinci-pas/SubscriptionTopic-PASSubscriptionTopic.json.html)). |
| `trigger.canFilterBy.filterDefinitionFhirPathExpression` | FHIRPath expression to extract the filter value from the resource. Here `insurer` extracts the insurer reference from ClaimResponse. |
| `trigger.canFilterBy.comparator` | Comparison operators allowed for this filter. Currently only `eq` (equals) is supported. |

## Step 3: Create AidboxTopicDestination

Create an `AidboxTopicDestination` that binds your `AidboxSubscriptionTopic` to the FHIR SubscriptionTopic URL and enables FHIR Subscription support:

{% tabs %}
{% tab title="Request" %}
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
      "valueString": "R4B-backported"
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
{% endtab %}
{% tab title="Response" %}
```json
{
  "meta": {
    "profile": [
      "http://aidbox.app/StructureDefinition/aidboxtopicdestination-fhir-native-topic-based-subscription"
    ],
    "lastUpdated": "2026-01-09T13:15:58.234943Z",
    "versionId": "8"
  },
  "content": "full-resource",
  "topic": "http://example.org/SubscriptionTopic/pas-claim-response",
  "resourceType": "AidboxTopicDestination",
  "status": "active",
  "id": "18a7068f-97e4-4d68-8014-82be3479b998",
  "kind": "fhir-native-topic-based-subscription",
  "parameter": [
    {
      "name": "subscription-specification-version",
      "valueString": "R4B-backported"
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
{% endtab %}
{% endtabs %}

### Key Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| `topic` | - | URL of your `AidboxSubscriptionTopic` from Step 2. |
| `fhir-topic` * | valueCanonical | The canonical FHIR SubscriptionTopic URL that clients will use in their Subscription resources. |
| `subscription-specification-version` * | valueString | Set to `R4B-backported` for R4B Backport format. Uses R4 Backport subscription format with R5-style notification bundles. |
| `keep-events-for-period` | valueUnsignedInt | Event retention period in seconds for `$events` operation. If not specified, events are stored indefinitely. |
| `number-of-deliverer` | valueUnsignedInt | Number of parallel delivery workers (default: 4). |

\* required

## Step 4: Create Test Data

Create the Organization and Patient resources that will be referenced:

{% tabs %}
{% tab title="Request" %}
```http
PUT /fhir/Organization/org-1
Content-Type: application/json

{
  "resourceType": "Organization",
  "id": "org-1",
  "name": "Example Insurance Company"
}
```
{% endtab %}
{% tab title="Response" %}
```json
{
  "name": "Example Insurance Company",
  "id": "org-1",
  "resourceType": "Organization",
  "meta": {
    "lastUpdated": "2026-01-09T13:16:07.616907Z",
    "versionId": "9"
  }
}
```
{% endtab %}
{% endtabs %}

{% tabs %}
{% tab title="Request" %}
```http
PUT /fhir/Patient/example
Content-Type: application/json

{
  "resourceType": "Patient",
  "id": "example",
  "name": [{"family": "Test", "given": ["Patient"]}]
}
```
{% endtab %}
{% tab title="Response" %}
```json
{
  "name": [{"family": "Test", "given": ["Patient"]}],
  "id": "example",
  "resourceType": "Patient",
  "meta": {
    "lastUpdated": "2026-01-09T13:16:13.419968Z",
    "versionId": "11"
  }
}
```
{% endtab %}
{% endtabs %}

## Step 5: Create a FHIR Subscription

R4B Backport uses the same subscription format as R4 Backport (with extensions):

{% tabs %}
{% tab title="Request" %}
```http
POST /fhir/Subscription
Content-Type: application/json

{
  "resourceType": "Subscription",
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
        "valueString": "orgIdentifier=Organization/org-1"
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
{% endtab %}
{% tab title="Response" %}
```json
{
  "meta": {
    "profile": [
      "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-subscription"
    ],
    "lastUpdated": "2026-01-09T13:16:32.332542Z",
    "versionId": "12"
  },
  "criteria": "http://hl7.org/fhir/us/davinci-pas/SubscriptionTopic/PASSubscriptionTopic",
  "channel": {
    "type": "rest-hook",
    "header": ["Authorization: Bearer your-token"],
    "payload": "application/fhir+json",
    "_payload": {
      "extension": [
        {
          "url": "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-payload-content",
          "valueCode": "full-resource"
        }
      ]
    },
    "endpoint": "https://your-endpoint.example.com/webhook",
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
  },
  "_criteria": {
    "extension": [
      {
        "url": "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-filter-criteria",
        "valueString": "orgIdentifier=Organization/org-1"
      }
    ]
  },
  "resourceType": "Subscription",
  "reason": "Receive notifications about claim responses for my organization.",
  "status": "requested",
  "id": "pas-subscription-r4b"
}
```
{% endtab %}
{% endtabs %}

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
| `backport-heartbeat-period` | Interval in seconds for sending keep-alive notifications during inactivity. |
| `backport-timeout` | Maximum time in seconds the server will wait before failing a notification delivery attempt. |
| `backport-max-count` | Maximum number of events to include in a single notification bundle. |

## Step 6: Verify Handshake

After creating the subscription, Aidbox sends a handshake notification to your endpoint. If the handshake succeeds (HTTP 2xx response), the subscription status changes to `active`.

Check subscription status:

{% tabs %}
{% tab title="Request" %}
```http
GET /fhir/Subscription/pas-subscription-r4b
```
{% endtab %}
{% tab title="Response" %}
```json
{
  "meta": {
    "profile": [
      "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-subscription"
    ],
    "lastUpdated": "2026-01-09T13:16:32.457685Z",
    "versionId": "13"
  },
  "criteria": "http://hl7.org/fhir/us/davinci-pas/SubscriptionTopic/PASSubscriptionTopic",
  "channel": {
    "type": "rest-hook",
    "header": ["Authorization: Bearer your-token"],
    "payload": "application/fhir+json",
    "_payload": {
      "extension": [
        {
          "url": "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-payload-content",
          "valueCode": "full-resource"
        }
      ]
    },
    "endpoint": "https://your-endpoint.example.com/webhook",
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
  },
  "_criteria": {
    "extension": [
      {
        "url": "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-filter-criteria",
        "valueString": "orgIdentifier=Organization/org-1"
      }
    ]
  },
  "resourceType": "Subscription",
  "reason": "Receive notifications about claim responses for my organization.",
  "status": "active",
  "id": "pas-subscription-r4b"
}
```
{% endtab %}
{% endtabs %}

If handshake fails, the subscription status will be `error`.

## Step 7: Trigger Events

Create or update resources matching your topic criteria:

{% tabs %}
{% tab title="Request" %}
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
{% endtab %}
{% tab title="Response" %}
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
  "id": "4e455dd8-133c-4c5a-9cc4-b32c6abfba29",
  "resourceType": "ClaimResponse",
  "meta": {
    "lastUpdated": "2026-01-09T13:17:00.191070Z",
    "versionId": "13"
  }
}
```
{% endtab %}
{% endtabs %}

Your endpoint will receive a notification bundle containing the ClaimResponse.

## Using $status Operation

Check subscription status and event counts:

{% tabs %}
{% tab title="Request" %}
```http
GET /fhir/Subscription/pas-subscription-r4b/$status
```
{% endtab %}
{% tab title="Response" %}
```json
{
  "resourceType": "Bundle",
  "type": "history",
  "timestamp": "2026-01-09T13:17:15.305948Z",
  "entry": [
    {
      "fullUrl": "urn:uuid:22dc7587-2557-4113-a199-945da867a7f4",
      "request": {
        "method": "GET",
        "url": "http://localhost:8765/Subscription/pas-subscription-r4b/$status"
      },
      "response": {
        "status": "200"
      },
      "resource": {
        "resourceType": "SubscriptionStatus",
        "id": "22dc7587-2557-4113-a199-945da867a7f4",
        "type": "query-status",
        "topic": "http://hl7.org/fhir/us/davinci-pas/SubscriptionTopic/PASSubscriptionTopic",
        "status": "active",
        "subscription": {
          "reference": "http://localhost:8765/Subscription/pas-subscription-r4b"
        },
        "eventsSinceSubscriptionStart": "1"
      }
    }
  ]
}
```
{% endtab %}
{% endtabs %}

## Using $events Operation

Query past events for recovery:

{% tabs %}
{% tab title="Request" %}
```http
POST /fhir/Subscription/pas-subscription-r4b/$events
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
{% endtab %}
{% tab title="Response" %}
```json
{
  "resourceType": "Bundle",
  "type": "history",
  "timestamp": "2026-01-09T13:17:22.805048Z",
  "entry": [
    {
      "fullUrl": "urn:uuid:e266f73c-365c-48e7-8a4d-5553304fd46d",
      "request": {
        "method": "GET",
        "url": "http://localhost:8765/Subscription/pas-subscription-r4b/$status"
      },
      "response": {
        "status": "200"
      },
      "resource": {
        "resourceType": "SubscriptionStatus",
        "id": "e266f73c-365c-48e7-8a4d-5553304fd46d",
        "type": "query-event",
        "topic": "http://hl7.org/fhir/us/davinci-pas/SubscriptionTopic/PASSubscriptionTopic",
        "status": "active",
        "subscription": {
          "reference": "http://localhost:8765/Subscription/pas-subscription-r4b"
        },
        "notificationEvent": [
          {
            "eventNumber": "1",
            "focus": {
              "reference": "http://localhost:8765/fhir/ClaimResponse/4e455dd8-133c-4c5a-9cc4-b32c6abfba29"
            },
            "timestamp": "2026-01-09T13:17:00.191070Z"
          }
        ],
        "eventsSinceSubscriptionStart": "1"
      }
    },
    {
      "fullUrl": "http://localhost:8765/fhir/ClaimResponse/4e455dd8-133c-4c5a-9cc4-b32c6abfba29",
      "resource": {
        "patient": {
          "reference": "Patient/example"
        },
        "meta": {
          "versionId": "13",
          "lastUpdated": "2026-01-09T13:17:00.191070Z"
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
        "id": "4e455dd8-133c-4c5a-9cc4-b32c6abfba29"
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
{% endtab %}
{% endtabs %}

## Notification Bundle Examples

R4B Backport notification bundles use `SubscriptionStatus` resource (like R5) but with Bundle type `history`.

### Handshake Notification

Sent immediately after subscription creation to verify the endpoint:

```json
{
  "resourceType": "Bundle",
  "type": "history",
  "timestamp": "2026-01-09T13:16:32.457685Z",
  "entry": [
    {
      "fullUrl": "urn:uuid:9cfe9aa4-a49b-4a96-b434-25879eaaff47",
      "request": {
        "method": "GET",
        "url": "http://localhost:8765/Subscription/pas-subscription-r4b/$status"
      },
      "response": {
        "status": "200"
      },
      "resource": {
        "resourceType": "SubscriptionStatus",
        "id": "9cfe9aa4-a49b-4a96-b434-25879eaaff47",
        "type": "handshake",
        "topic": "http://hl7.org/fhir/us/davinci-pas/SubscriptionTopic/PASSubscriptionTopic",
        "status": "requested",
        "subscription": {
          "reference": "http://localhost:8765/Subscription/pas-subscription-r4b"
        },
        "eventsSinceSubscriptionStart": "0"
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
  "type": "history",
  "timestamp": "2026-01-09T13:17:00.691233Z",
  "entry": [
    {
      "fullUrl": "urn:uuid:31d18835-c7f3-4e2f-920a-6654ea2ea0f6",
      "request": {
        "method": "GET",
        "url": "http://localhost:8765/Subscription/pas-subscription-r4b/$status"
      },
      "response": {
        "status": "200"
      },
      "resource": {
        "resourceType": "SubscriptionStatus",
        "id": "31d18835-c7f3-4e2f-920a-6654ea2ea0f6",
        "type": "event-notification",
        "topic": "http://hl7.org/fhir/us/davinci-pas/SubscriptionTopic/PASSubscriptionTopic",
        "status": "active",
        "subscription": {
          "reference": "http://localhost:8765/Subscription/pas-subscription-r4b"
        },
        "notificationEvent": [
          {
            "eventNumber": "1",
            "focus": {
              "reference": "http://localhost:8765/fhir/ClaimResponse/4e455dd8-133c-4c5a-9cc4-b32c6abfba29"
            },
            "timestamp": "2026-01-09T13:17:00.191070Z"
          }
        ],
        "eventsSinceSubscriptionStart": "1"
      }
    },
    {
      "fullUrl": "http://localhost:8765/fhir/ClaimResponse/4e455dd8-133c-4c5a-9cc4-b32c6abfba29",
      "resource": {
        "patient": {
          "reference": "Patient/example"
        },
        "meta": {
          "versionId": "13",
          "lastUpdated": "2026-01-09T13:17:00.191070Z"
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
        "id": "4e455dd8-133c-4c5a-9cc4-b32c6abfba29"
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

Sent periodically during inactivity based on `backport-heartbeat-period`:

```json
{
  "resourceType": "Bundle",
  "type": "history",
  "timestamp": "2026-01-09T13:18:00.697371Z",
  "entry": [
    {
      "fullUrl": "urn:uuid:5174f13e-6845-4fa0-b509-21a1c75b96b0",
      "request": {
        "method": "GET",
        "url": "http://localhost:8765/Subscription/pas-subscription-r4b/$status"
      },
      "response": {
        "status": "200"
      },
      "resource": {
        "resourceType": "SubscriptionStatus",
        "id": "5174f13e-6845-4fa0-b509-21a1c75b96b0",
        "type": "heartbeat",
        "topic": "http://hl7.org/fhir/us/davinci-pas/SubscriptionTopic/PASSubscriptionTopic",
        "status": "active",
        "subscription": {
          "reference": "http://localhost:8765/Subscription/pas-subscription-r4b"
        },
        "eventsSinceSubscriptionStart": "1"
      }
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
- [FHIR R5 Subscription Setup](fhir-subscription-r5.md) - Tutorial for native R5 format
- [FHIR R4 Backport Subscription Setup](fhir-subscription-r4-backport.md) - Tutorial for R4 Backport format
- [Subscriptions Backport IG](https://build.fhir.org/ig/HL7/fhir-subscription-backport-ig/) - Official specification
