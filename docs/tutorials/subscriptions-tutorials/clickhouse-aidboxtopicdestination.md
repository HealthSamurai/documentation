# ClickHouse AidboxTopicDestination

{% hint style="info" %}
This functionality is available starting from version 2507 and requires [FHIR Schema](../../modules/profiling-and-validation/fhir-schema-validator/) validation engine to be [enabled](../../modules/profiling-and-validation/fhir-schema-validator/).
{% endhint %}

## Overview

The ClickHouse Topic Destination module provides integration between Aidbox's topic-based subscriptions and ClickHouse analytical database. It enables real-time export of FHIR resources from Aidbox to ClickHouse in a flattened format using [ViewDefinitions](../../modules/sql-on-fhir/defining-flat-views-with-view-definitions.md) and SQL-on-FHIR technology.

This module is designed for analytical workloads where you need to export FHIR data in a structured, queryable format suitable for business intelligence, reporting, and data analysis.

## Installation

To use the ClickHouse TopicDestination module, you need to install and configure it in your Aidbox instance:

### 1. Configure Deployment

#### Docker Compose

For Docker Compose deployments, first download the module JAR file locally:

```sh
curl -O https://storage.googleapis.com/aidbox-modules/topic-destination-clickhouse/topic-destination-clickhouse-2507.jar
```

Then update your **docker-compose.yaml** to include the ClickHouse module:

```yaml
  aidbox:
    extra_hosts:
    # connect Aidbox container and ClickHouse from localhost
    - "host.docker.internal:host-gateway"
    volumes:
    # module jar to turn on ClickHouse support
    - ./topic-destination-clickhouse-2507.jar:/topic-destination-clickhouse-2507.jar
    # ... other volumes ...
    environment:
      BOX_MODULE_LOAD: io.healthsamurai.topic-destination.clickhouse.core
      BOX_MODULE_JAR: "/topic-destination-clickhouse-2507.jar"
      # Enable FHIR Schema validation (required for ClickHouse module)
      BOX_FHIR_SCHEMA_VALIDATION: true
      # ... other environment variables ...
```

#### Kubernetes

For Kubernetes deployments, the module is downloaded automatically using an init container. Update your Aidbox deployment manifest:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: aidbox
spec:
  template:
    spec:
      initContainers:
      - name: download-clickhouse-module
        image: debian:bookworm-slim
        imagePullPolicy: Always
        command:
        - sh
        - -c
        - |
          apt-get -y update
          apt-get -y install curl
          curl -L -o /modules/topic-destination-clickhouse-2507.1.jar \
            https://storage.googleapis.com/aidbox-modules/topic-destination-clickhouse/topic-destination-clickhouse-2507.1.jar
          chmod 644 /modules/topic-destination-clickhouse-2507.1.jar
        volumeMounts:
        - mountPath: /modules
          name: modules-volume
      containers:
      - name: aidbox
        image: healthsamurai/aidboxone:edge
        env:
        - name: BOX_MODULE_LOAD
          value: "io.healthsamurai.topic-destination.clickhouse.core"
        - name: BOX_MODULE_JAR
          value: "/modules/topic-destination-clickhouse-2507.1.jar"
        - name: BOX_FHIR_SCHEMA_VALIDATION
          value: "true"
        # ... other environment variables ...
        volumeMounts:
        - name: modules-volume
          mountPath: /modules
        # ... other volume mounts ...
      volumes:
      - name: modules-volume
        emptyDir: {}
      # ... other volumes ...
```

### 2. Verify Installation

In AidboxUI, go to **FHIR Packages -> io.healthsamurai.topic** and verify that ClickHouse profiles are present:

- `http://aidbox.app/StructureDefinition/aidboxtopicdestination-clickhouse`
- `http://aidbox.app/StructureDefinition/aidboxtopicdestination-clickhouse-at-least-once`

## Key Features

- **Real-time data export**: Automatically exports FHIR resources to ClickHouse as they are created, updated, or deleted
- **Data flattening**: Uses ViewDefinitions to transform complex FHIR resources into flat, analytical-friendly tables
- **Two delivery modes**: Best effort and at-least-once delivery guarantees
- **Batch processing**: Configurable batch sizes and intervals for optimal performance
- **Initial export**: Automatically exports existing data when setting up a new destination
- **Monitoring**: Built-in metrics and status reporting
- **Flexible configuration**: Support for custom ClickHouse configurations and table schemas

## Architecture

The module consists of two main components:

### 1. Best Effort Delivery (`clickhouse-best-effort`)

- Immediate delivery of individual messages
- No persistent queue
- Suitable for non-critical analytics where some data loss is acceptable
- Lower latency and resource usage

### 2. At-Least-Once Delivery (`clickhouse-at-least-once`)

- Persistent message queue with guaranteed delivery
- Batch processing capabilities
- Configurable retry logic
- Suitable for critical analytics where data completeness is essential

## Configuration

### Common Parameters

Both delivery modes support the following parameters:

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `url` | string | Yes | ClickHouse server URL |
| `database` | string | Yes | ClickHouse database name |
| `user` | string | Yes | ClickHouse username |
| `password` | string | Yes | ClickHouse password |
| `viewDefinition` | string | Yes | Name of the ViewDefinition resource to use for data transformation |
| `destinationTable` | string | Yes | Target table name in ClickHouse |

### At-Least-Once Specific Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `batchSize` | unsignedInt | Yes | Number of messages to batch together (default: 10) |
| `sendIntervalMs` | unsignedInt | Yes | Time interval between sends in milliseconds |

## Usage Examples

To use ClickHouse as a destination for your FHIR data, you need to:

1. Create an `AidboxSubscriptionTopic` to define what data to capture
2. Create a `ViewDefinition` to transform the data
3. Materialize the ViewDefinition as a database view using `$materialize`
4. Configure an `AidboxTopicDestination` to send that data to ClickHouse

### Complete Example: Patient Data Export

#### Step 1: Create Subscription Topic

First, create a topic that captures patient changes:

```yaml
POST /fhir/AidboxSubscriptionTopic
Content-Type: application/json

{
  "resourceType": "AidboxSubscriptionTopic",
  "url": "http://example.org/subscriptions/patient-updates",
  "status": "active",
  "trigger": [
    {
      "resource": "Patient",
      "supportedInteraction": ["create", "update", "delete"],
      "fhirPathCriteria": "active = true"
    }
  ]
}
```

**Topic Parameters:**

- **url**: Unique identifier for the topic, referenced by destinations
- **status**: Must be "active" for the topic to process events
- **trigger.resource**: FHIR resource type to monitor
- **trigger.supportedInteraction**: Which operations to track
- **trigger.fhirPathCriteria**: Optional FHIRPath expression to filter resources

#### Step 2: Create ViewDefinition

Create a ViewDefinition to transform Patient resources into a flat structure:

```yaml
POST /fhir/ViewDefinition
Content-Type: application/json

{
  "resourceType": "ViewDefinition",
  "id": "patient_flat_view",
  "name": "patient_flat_view",
  "resource": "Patient",
  "status": "active",
  "select": [
    {
      "column": [
        {"name": "id", "path": "id"},
        {"name": "gender", "path": "gender"},
        {"name": "birth_date", "path": "birthDate"}
      ]
    },
    {
      "forEach": "name.where(use = 'official').first()",
      "column": [
        {"name": "family_name", "path": "family"},
        {"name": "given_name", "path": "given.join(' ')"}
      ]
    }
  ]
}
```

#### Step 3: Materialize ViewDefinition as View

Materialize the ViewDefinition as a database view (required for ClickHouse integration):

```yaml
POST /fhir/ViewDefinition/patient_flat_view/$materialize
Content-Type: application/json

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "type",
      "valueCode": "view"
    }
  ]
}
```

This creates a database view that the ClickHouse module will use to transform data.

#### Step 4: Configure Best Effort Destination

```yaml
resourceType: AidboxTopicDestination
id: patient-clickhouse-best-effort
topic: "http://example.org/subscriptions/patient-updates"
kind: clickhouse
parameter:
  - name: url
    valueString: "http://clickhouse:8123"
  - name: database
    valueString: "analytics"
  - name: user
    valueString: "default"
  - name: password
    valueString: "password"
  - name: viewDefinition
    valueString: "patient_flat_view"
  - name: destinationTable
    valueString: "patients"
meta:
  profile: 
    - "http://aidbox.app/StructureDefinition/aidboxtopicdestination-clickhouse"
```

### Alternative: At-Least-Once Configuration

For guaranteed delivery with batch processing:

```yaml
resourceType: AidboxTopicDestination
id: patient-clickhouse-reliable
topic: "http://example.org/subscriptions/patient-updates"
kind: clickhouse-at-least-once
parameter:
  - name: url
    valueString: "http://clickhouse:8123"
  - name: database
    valueString: "analytics"
  - name: user
    valueString: "default"
  - name: password
    valueString: "password"
  - name: viewDefinition
    valueString: "patient_flat_view"
  - name: destinationTable
    valueString: "patients"
  - name: batchSize
    valueUnsignedInt: 50
  - name: sendIntervalMs
    valueUnsignedInt: 5000
meta:
  profile:
    - "http://aidbox.app/StructureDefinition/aidboxtopicdestination-clickhouse-at-least-once"
```

### Advanced Topic Filtering

You can create more sophisticated topics using FHIRPath variables to detect specific state transitions:

```yaml
POST /fhir/AidboxSubscriptionTopic
Content-Type: application/json

{
  "resourceType": "AidboxSubscriptionTopic",
  "url": "http://example.org/subscriptions/encounter-completed",
  "status": "active",
  "trigger": [
    {
      "resource": "Encounter",
      "supportedInteraction": ["update"],
      "fhirPathCriteria": "%previous.status = 'in-progress' and %current.status = 'finished'"
    }
  ]
}
```

**Special Variables:**

- `%current`: Current state of the resource after an update
- `%previous`: Previous state of the resource before an update
- On create: `%previous` returns `{}`
- On delete: `%current` returns `{}`

## ViewDefinition Integration

The module uses [ViewDefinitions](../../modules/sql-on-fhir/defining-flat-views-with-view-definitions.md) to transform FHIR resources into flat, analytical structures.

{% hint style="info" %}
**Important**: For ClickHouse integration to work properly, the ViewDefinition must be materialized as a **view** (not as a table or materialized view). Use the [`$materialize`](../../modules/sql-on-fhir/operation-materialize.md) operation with `type: "view"` to create the necessary database view before configuring your ClickHouse destination.
{% endhint %}

### Example ViewDefinition

```yaml
resourceType: ViewDefinition
id: patient_flat_view
name: patient_flat_view
resource: Patient
status: active
select:
  - column:
      - name: id
        path: id
        description: Patient ID
      - name: gender
        path: gender
        description: Patient gender
      - name: birth_date
        path: birthDate
        description: Date of birth
  - forEach: "name.where(use = 'official').first()"
    column:
      - name: family_name
        path: family
        description: Family name
      - name: given_name
        path: given.join(' ')
        description: Given names joined
```

This ViewDefinition will flatten Patient resources into a table with columns: `id`, `gender`, `birth_date`, `family_name`, `given_name`.

### Materializing the ViewDefinition

After creating the ViewDefinition, you must materialize it as a database view using the `$materialize` operation:

```yaml
POST /fhir/ViewDefinition/patient_flat_view/$materialize
Content-Type: application/json

{
  "resourceType": "Parameters",
  "parameter": [
    {
      "name": "type",
      "valueCode": "view"
    }
  ]
}
```

This creates a database view in the `sof` schema (by default) that the ClickHouse module will use to transform data. The view must be created before configuring your ClickHouse destination.

## Data Transformation

The module automatically:

1. **Applies ViewDefinition**: Transforms FHIR resources using the specified ViewDefinition
2. **Adds deletion flag**: Includes `is_deleted` field (1 for DELETE operations, 0 for CREATE/UPDATE)

## ClickHouse Table Requirements

Your ClickHouse table should match the structure defined in your ViewDefinition:

```sql
CREATE TABLE analytics.patients (
    id String,
    gender String,
    birth_date Date,
    family_name String,
    given_name String,
    is_deleted UInt8
) ENGINE = MergeTree()
ORDER BY id;
```

## Monitoring and Metrics

Both delivery modes provide:

- **Status reporting**: Current state of the destination (active, failed, etc.)
- **Message metrics**: Count of sent, failed, and queued messages
- **Initial export status**: Progress of historical data export
- **Error tracking**: Detailed error messages and stack traces

### Status API

```http
GET /AidboxTopicDestination/{id}/$status
```

Returns current status for the destination.

## Error Handling

### Best Effort Mode

- Errors are logged but don't block processing
- Failed messages are not retried
- Suitable for non-critical analytics

### At-Least-Once Mode

- Failed messages remain in queue for retry
- Configurable retry intervals
- Detailed error logging and reporting
- Automatic recovery from transient failures

## Initial Export

When a new destination is created, the module automatically:

1. Exports existing data matching the subscription filter
2. Applies the ViewDefinition to historical data
3. Populates the ClickHouse table with existing records
4. Switches to real-time processing mode

## Performance Considerations

### Best Effort Mode

- Lower latency (immediate processing)
- Lower memory usage
- Suitable for real-time dashboards

### At-Least-Once Mode

- Higher throughput with batching
- Configurable batch sizes for optimal performance
- Better for bulk analytics and reporting

## Troubleshooting

### Common Issues

1. **Connection failures**: Verify ClickHouse URL, credentials, and network connectivity
2. **Table schema mismatches**: Ensure ClickHouse table structure matches ViewDefinition output
3. **ViewDefinition errors**: Validate ViewDefinition syntax and resource compatibility
4. **Timestamp format issues**: The module automatically handles ClickHouse timestamp requirements

### Debug Tips

- Check AidboxTopicDestination status endpoint for detailed error messages
- Verify ViewDefinition works correctly using Aidbox UI
- Test ClickHouse connection independently
- Monitor ClickHouse logs for ingestion errors

## Related Documentation

- [ViewDefinitions](../../modules/sql-on-fhir/defining-flat-views-with-view-definitions.md)
- [Topic-based Subscriptions](../../modules/topic-based-subscriptions/README.md)
