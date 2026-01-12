---
description: Step-by-step guide to setting up FHIR R4 Backport Topic-Based Subscriptions in Aidbox.
---

# FHIR R4 Backport Subscription Setup

{% hint style="info" %}
This functionality is available in Aidbox versions 2512 and later and requires [FHIR Schema](../../modules/profiling-and-validation/fhir-schema-validator/) validation engine to be enabled. If you started Aidbox using the [Run Aidbox Locally](../../getting-started/run-aidbox-locally.md) guide, FHIR Schema is already enabled.
{% endhint %}

For concepts, supported fields, and configuration details, see [FHIR Topic-Based Subscriptions](../../modules/topic-based-subscriptions/fhir-topic-based-subscriptions.md).

In this tutorial, we will set up Aidbox to implement the [DaVinci PAS SubscriptionTopic](https://build.fhir.org/ig/HL7/davinci-pas/SubscriptionTopic-PASSubscriptionTopic.json.html) (`http://hl7.org/fhir/us/davinci-pas/SubscriptionTopic/PASSubscriptionTopic`). This topic notifies subscribers when a Prior Authorization response (ClaimResponse) becomes available.

## Prerequisites
- An HTTP endpoint to receive notifications (for testing, you can use services like [RequestCatcher](https://requestcatcher.com/) or [Webhook.site](https://webhook.site/))

## Step 1: Install the Subscriptions Backport Package

Install the FHIR Subscriptions Backport IG package. This package contains the Subscription profile that Aidbox uses to validate Subscription resources created by clients:

Request:

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

Response:

```json
{
  "resourceType": "Parameters",
  "parameter": [
    {"name": "result", "valueBoolean": true},
    {"name": "package", "part": [
      {"name": "name", "valueString": "hl7.fhir.uv.subscriptions-backport.r4@1.1.0"},
      {"name": "installedCanonicals", "valueInteger": 23}
    ]}
  ]
}
```

## Step 2: Create AidboxSubscriptionTopic

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
  "id": "d9c47c9f-c215-4192-80ed-0ee792598445",
  "resourceType": "AidboxSubscriptionTopic",
  "meta": {
    "lastUpdated": "2025-01-05T14:17:39.601439Z",
    "versionId": "4",
    "extension": [
      {
        "url": "ex:createdAt",
        "valueInstant": "2025-01-05T14:17:39.601439Z"
      }
    ]
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

## Step 3: Create AidboxTopicDestination

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

Response:

```json
{
  "topic": "http://example.org/SubscriptionTopic/pas-claim-response",
  "kind": "fhir-native-topic-based-subscription",
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
  ],
  "id": "b5f1b2a3-c4d5-4e6f-8a9b-0c1d2e3f4a5b",
  "resourceType": "AidboxTopicDestination",
  "meta": {
    "profile": [
      "http://aidbox.app/StructureDefinition/aidboxtopicdestination-fhir-native-topic-based-subscription"
    ],
    "lastUpdated": "2025-01-05T14:18:12.345678Z",
    "versionId": "1",
    "extension": [
      {
        "url": "ex:createdAt",
        "valueInstant": "2025-01-05T14:18:12.345678Z"
      }
    ]
  }
}
```

### Key Parameters

| Parameter | Type | Description |
|-----------|------|-------------|
| `topic` | - | URL of your `AidboxSubscriptionTopic` from Step 2. |
| `fhir-topic` * | valueCanonical | The canonical FHIR SubscriptionTopic URL that clients will use in their Subscription resources. |
| `subscription-specification-version` * | valueString | FHIR version of the Subscription spec. Determines the format of notification bundles and operation responses. Currently only `R4-backported` is supported. |
| `keep-events-for-period` | valueUnsignedInt | Event retention period in seconds for `$events` operation. If not specified, events are stored indefinitely. |
| `number-of-deliverer` | valueUnsignedInt | Number of parallel delivery workers (default: 4). |

\* required

## Step 4: Create Test Data

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
    "lastUpdated": "2025-01-05T14:19:00.123456Z",
    "versionId": "1",
    "extension": [
      {
        "url": "ex:createdAt",
        "valueInstant": "2025-01-05T14:19:00.123456Z"
      }
    ]
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
    "lastUpdated": "2025-01-05T14:19:05.234567Z",
    "versionId": "1",
    "extension": [
      {
        "url": "ex:createdAt",
        "valueInstant": "2025-01-05T14:19:05.234567Z"
      }
    ]
  }
}
```

## Step 5: Create a FHIR Subscription

Now clients can create standard FHIR Subscription resources:

Request:

```http
POST /fhir/Subscription
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

Response:

```json
{
  "meta": {
    "profile": [
      "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-subscription"
    ],
    "lastUpdated": "2025-01-05T14:20:00.567890Z",
    "versionId": "1",
    "extension": [
      {
        "url": "ex:createdAt",
        "valueInstant": "2025-01-05T14:20:00.567890Z"
      }
    ]
  },
  "reason": "Receive notifications about claim responses for my organization.",
  "channel": {
    "type": "rest-hook",
    "header": ["Authorization: Bearer your-token"],
    "payload": "application/fhir+json",
    "endpoint": "https://your-endpoint.example.com/webhook",
    "_payload": {
      "extension": [
        {
          "url": "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-payload-content",
          "valueCode": "full-resource"
        }
      ]
    },
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
  "status": "requested",
  "criteria": "http://hl7.org/fhir/us/davinci-pas/SubscriptionTopic/PASSubscriptionTopic",
  "_criteria": {
    "extension": [
      {
        "url": "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-filter-criteria",
        "valueString": "orgIdentifier=Organization/org-1"
      }
    ]
  },
  "id": "pas-subscription-1",
  "resourceType": "Subscription"
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
| `backport-heartbeat-period` | Interval in seconds for sending keep-alive notifications during inactivity. Helps maintain active connection. |
| `backport-timeout` | Maximum time in seconds the server will wait before failing a notification delivery attempt. |
| `backport-max-count` | Maximum number of events to include in a single notification bundle. Events are batched up to this limit before sending. |

## Step 6: Verify Handshake

After creating the subscription, Aidbox sends a handshake notification to your endpoint. If the handshake succeeds (HTTP 2xx response), the subscription status changes to `active`.

Check subscription status:

Request:

```http
GET /fhir/Subscription/pas-subscription-1
```

Response:

```json
{
  "meta": {
    "profile": [
      "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-subscription"
    ],
    "lastUpdated": "2025-01-05T14:20:01.234567Z",
    "versionId": "2",
    "extension": [
      {
        "url": "ex:createdAt",
        "valueInstant": "2025-01-05T14:20:00.567890Z"
      }
    ]
  },
  "reason": "Receive notifications about claim responses for my organization.",
  "channel": {
    "type": "rest-hook",
    "header": ["Authorization: Bearer your-token"],
    "payload": "application/fhir+json",
    "endpoint": "https://your-endpoint.example.com/webhook",
    "_payload": {
      "extension": [
        {
          "url": "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-payload-content",
          "valueCode": "full-resource"
        }
      ]
    },
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
  "status": "active",
  "criteria": "http://hl7.org/fhir/us/davinci-pas/SubscriptionTopic/PASSubscriptionTopic",
  "_criteria": {
    "extension": [
      {
        "url": "http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-filter-criteria",
        "valueString": "orgIdentifier=Organization/org-1"
      }
    ]
  },
  "id": "pas-subscription-1",
  "resourceType": "Subscription"
}
```

If handshake fails, the subscription status will be `error`.

## Step 7: Trigger Events

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
  "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "resourceType": "ClaimResponse",
  "meta": {
    "lastUpdated": "2025-01-05T14:21:00.123456Z",
    "versionId": "1",
    "extension": [
      {
        "url": "ex:createdAt",
        "valueInstant": "2025-01-05T14:21:00.123456Z"
      }
    ]
  }
}
```

Your endpoint will receive a notification bundle containing the ClaimResponse.

## Using $status Operation

Check subscription status and event counts:

Request:

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

Request:

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
            "valueCode": "query-event"
          },
          {
            "name": "events-since-subscription-start",
            "valueString": "1"
          }
        ]
      }
    },
    {
      "resource": {
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
        "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
        "resourceType": "ClaimResponse",
        "meta": {
          "lastUpdated": "2025-01-05T14:21:00.123456Z",
          "versionId": "1"
        }
      }
    }
  ]
}
```

## Limitations

- **No subscription updates**: Once created, subscriptions cannot be modified. Delete and recreate if changes are needed.
- **REST-hook only**: Currently only the [`rest-hook`](https://hl7.org/fhir/R4/valueset-subscription-channel-type.html) channel type is supported.
- **Content level**: Subscription's payload content cannot exceed the AidboxTopicDestination's `content` setting.

## See Also

- [FHIR Topic-Based Subscriptions](../../modules/topic-based-subscriptions/fhir-topic-based-subscriptions.md) - Overview and concepts
- [FHIR R5 Subscription Setup](fhir-subscription-r5.md) - Tutorial for native R5 format
- [FHIR R4B Backport Subscription Setup](fhir-subscription-r4b-backport.md) - Tutorial for R4B Backport format
- [Subscriptions Backport IG](https://build.fhir.org/ig/HL7/fhir-subscription-backport-ig/) - Official specification
