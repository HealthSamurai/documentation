# Auditbox - Quick Start Guide

Auditbox is a FHIR-native, Elastic-backed Audit Record Repository that consolidates AuditEvent resources from multiple systems and enables secure, high-performance querying for compliance, security, and operational visibility.

- **Fast performance:** Auditbox can handle thousands of new audit events every second.
- **Saves space:** It stores audit data much more efficiently, using about 10 times less disk space.
- **Powerful search:** Find what you need with FHIR search and full-text search.
- **User-friendly dashboard:** The admin panel helps you quickly drill down to the most important events with just a few clicks.

Auditbox uses Elasticsearch behind the scenes, but you don't need any special setup or a large server. Even a basic installation is enough, since most of the work is writing new events (not searching), and only a few users will be reviewing events at a time.

## Requirements

- **Docker Desktop** (includes Docker Compose)
- 2 GB free memory
- Ports 3002, 9204, 8888

## Quick Start

### Step 1: Create project folder

```bash
mkdir auditbox
cd auditbox
```

### Step 2: Create docker-compose.yml file

Create a `docker-compose.yml` file with the following content:

```yaml
services:
  auditbox:
    image: healthsamurai/auditbox:alpha
    pull_policy: always
    ports:
      - 3002:3000
    environment:
      BINDING: 0.0.0.0
      ELASTIC_URI: http://elasticsearch:9200
      AUDITBOX_BASE_URL: http://localhost:3002
      IDP_AUTHORIZE_ENDPOINT: http://localhost:8888/realms/auditbox/protocol/openid-connect/auth
      IDP_TOKEN_ENDPOINT: http://keycloak:8888/realms/auditbox/protocol/openid-connect/token
      IDP_CLIENT_ID: auditbox
      IDP_CLIENT_SECRET: super-secret
    depends_on:
      elasticsearch:
        condition: service_healthy

  elasticsearch:
    image: docker.elastic.co/elasticsearch/elasticsearch:8.17.0
    environment:
      - node.name=elasticsearch
      - cluster.name=es-docker-cluster
      - discovery.type=single-node
      - xpack.security.enabled=false
      - bootstrap.memory_lock=true
      - ES_JAVA_OPTS=-Xms1g -Xmx1g
    healthcheck:
      test: "curl http://localhost:9200"
      interval: 1s
      timeout: 3s
      start_period: 60s
      retries: 20
    ports:
      - 9204:9200
    volumes:
      - elasticsearch-data:/usr/share/elasticsearch/data

  keycloak:
    image: quay.io/keycloak/keycloak:26.0.6
    ports:
      - 8888:8888
    environment:
      KC_HTTP_PORT: 8888
      KC_BOOTSTRAP_ADMIN_USERNAME: admin
      KC_BOOTSTRAP_ADMIN_PASSWORD: admin
    volumes:
      - ./keycloak.json:/opt/keycloak/data/import/keycloak.json
    command: start-dev --import-realm --http-port 8888

volumes:
  elasticsearch-data:
```

### Step 3: Create keycloak.json file

Create a `keycloak.json` file with the following content:

```json
{
  "id": "auditbox",
  "realm": "auditbox",
  "enabled": true,
  "clients": [
    {
      "clientId": "auditbox",
      "enabled": true,
      "clientAuthenticatorType": "client-secret",
      "secret": "super-secret",
      "redirectUris": [
        "http://localhost:3002/auth/callback/keycloak",
        "http://auditbox:3002/auth/callback/keycloak"
      ],
      "webOrigins": [
        "http://localhost:3002",
        "http://auditbox:3002"
      ],
      "standardFlowEnabled": true,
      "directAccessGrantsEnabled": true,
      "publicClient": false,
      "protocol": "openid-connect"
    }
  ],
  "users": [
    {
      "username": "user",
      "email": "user@example.com",
      "enabled": true,
      "emailVerified": true,
      "firstName": "User",
      "lastName": "Demo",
      "credentials": [
        {
          "type": "password",
          "value": "password",
          "temporary": false
        }
      ]
    }
  ]
}
```

### Step 4: Launch AuditBox

```bash
docker compose up
```

Wait 2-3 minutes for all services to load.

### Step 5: Open AuditBox

- **Main page**: http://localhost:3002/
- **Login**: `user@example.com`
- **Password**: `password`

## How to use

### Sending audit events

```bash
curl -X POST \
  -H "Content-Type: application/json" \
  -d '{
    "resourceType": "AuditEvent",
    "type": {
      "code": "110100",
      "system": "http://dicom.nema.org/resources/ontology/DCM"
    },
    "outcome": "0",
    "recorded": "2025-01-31T16:03:16Z",
    "source": {
      "observer": {
        "display": "My System",
        "type": "Device"
      }
    },
    "agent": [{
      "requestor": true,
      "who": {
        "reference": "Practitioner/123"
      }
    }],
    "action": "C",
    "entity": [{
      "type": {
        "system": "http://hl7.org/fhir/resource-types",
        "code": "Patient"
      },
      "what": {
        "reference": "Patient/456"
      }
    }]
  }' \
  http://localhost:3002/AuditEvent
```

### Using pre-generated seed data

If you want to use the same 1000 audit events that are used in the official demo, you can use the provided `seed.json` file. In your `auditbox` folder, create a `use-seed-data.sh` file:

```bash
#!/bin/bash
echo "Loading seed data..."

# Check if seed.json exists
if [ ! -f seed.json ]; then
    echo "❌ seed.json file not found. Please make sure it's in the same directory as this script."
    exit 1
fi

echo "Extracting and loading 1000 audit events from seed data..."

# Extract each audit event from the bundle and POST it to Auditbox
jq -c '.entry[].resource' seed.json | while read -r event; do
    curl -s -X POST \
        -H "Content-Type: application/json" \
        -d "$event" \
        http://localhost:3002/AuditEvent
done

echo "✅ Done! Loaded 1000 audit events from seed data."
echo "You can now explore them in the Auditbox interface at http://localhost:3002/"
```

### Step 6: Load test data

After Auditbox is running, you can load test data. Open a new terminal window and navigate to your `auditbox` folder:

```bash
cd auditbox
```

Load 1000 pre-generated events:
```bash
chmod +x use-seed-data.sh
./use-seed-data.sh
```

**Note**: The seed data script requires `jq` to be installed. If you don't have it, install it with:
- **macOS**: `brew install jq`
- **Ubuntu/Debian**: `sudo apt-get install jq`
- **CentOS/RHEL**: `sudo yum install jq`

The seed data contains realistic audit events with varied:
- Event types (DICOM codes and lifecycle events)
- Actions (Create, Read, Update, Delete)
- Outcomes (0, 4, 8, 12)
- Sources (different sites and observers)
- Agents (practitioners, devices, organizations)
- Entities (patients, conditions, observations)
- Timestamps (from 2011)

## Stopping

```bash
# Stop services (data will be preserved)
docker compose down

# Stop and remove all data
docker compose down -v
```

## Available services

| Service | URL | Description |
|---------|-----|-------------|
| AuditBox | http://localhost:3002/ | Main interface |
| API | http://localhost:3002/AuditEvent | REST API |
| Keycloak | http://localhost:8888/ | Authorization admin |

## Auditbox configuration

The app can be configured with the following environment variables:

- `ELASTIC_URI` (required)
- `CAPABILITY_STATEMENT_PATH` (default `capability_statement.edn`)
- `BINDING` (default `127.0.0.1`)
- `PORT` (default `3000`)
- `BALP_VERSION` (default `1.1.3`, #{"1.1.0" "1.1.1" "1.1.2" "1.1.3"})
- `AUDITBOX_BASE_URL` (required)
- `IDP_AUTHORIZE_ENDPOINT` (required)
- `IDP_TOKEN_ENDPOINT` (required)
- `IDP_CLIENT_ID` (required)
- `IDP_CLIENT_SECRET` (required)
- `AUDITBOX_API_AUTH_ENABLED` (required, #{true false})
- `AUDITBOX_ES_AUTH` (optional, format `<user>:<password>`). Elastic basic auth. Implemented and tested for the `superadmin` role in Elastic
- `AUDITBOX_LOG_LEVEL` (default `off`, #{"off" "info" "debug" "error"})
- `AUDITBOX_DATA_RETENTION_DAYS` (default `30`, must be >= 1) - number of days to retain audit events before archiving
- `AUDITBOX_SNAPSHOT_REPOSITORY_NAME` (default `default`) - name of Elasticsearch snapshot repository for backups
- `AUDITBOX_VERIFY_BACKUP_REPOSITORY` (default `false`, #{true false}) - enables backup repository verification at startup
