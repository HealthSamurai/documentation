---
description: Deploy Aidbox and PostgreSQL to Kubernetes using Helm charts. Simplified configuration for database, ingress, SSL certificates, and FHIR packages.
---

# Deploy Aidbox with Helm Charts

Our Helm charts streamline the deployment process, enabling easy configuration and customization while ensuring a seamless deployment experience within Kubernetes clusters. Whether you're a healthcare institution, developer, or DevOps engineer, these Helm charts provide a straightforward path to deploying Aidbox in your Kubernetes environment.

Before deployment please read about infrastructure prerequisites.

## Database

Aidbox requires PostgreSQL as its data storage backend. See [PostgreSQL Requirements](../../../database/postgresql-requirements.md) for detailed requirements.

For self-managed PostgreSQL in Kubernetes, we recommend using the [CloudNativePG operator](https://cloudnative-pg.io/).

### 1. Install CloudNativePG operator

```bash
kubectl apply --server-side -f \
  https://raw.githubusercontent.com/cloudnative-pg/cloudnative-pg/main/releases/cnpg-1.28.0.yaml
```

### 2. Create PostgreSQL cluster

```yaml
# postgres-secret.yaml
apiVersion: v1
kind: Secret
metadata:
  name: postgres
  namespace: aidbox
stringData:
  password: <your-postgres-password>
  username: postgres
type: kubernetes.io/basic-auth
---
# postgres-cluster.yaml
apiVersion: postgresql.cnpg.io/v1
kind: Cluster
metadata:
  name: aidbox-db
  namespace: aidbox
spec:
  instances: 1
  bootstrap:
    initdb:
      database: aidbox
      owner: postgres
      secret:
        name: postgres
  postgresql:
    parameters:
      pg_stat_statements.max: "10000"
      pg_stat_statements.track: all
  resources:
    limits:
      memory: 2Gi
    requests:
      cpu: 100m
      memory: 1Gi
  storage:
    size: 10Gi
```

### 3. Apply config

```bash
kubectl create namespace aidbox
kubectl apply -f postgres-secret.yaml
kubectl apply -f postgres-cluster.yaml
```

CloudNativePG creates a service `aidbox-db-rw` for connecting to the primary instance.

## Aidbox

First, you must get an Aidbox license on the [Aidbox user portal.](https://aidbox.app/)

### 1. Add helm repo

```bash
helm repo add healthsamurai https://healthsamurai.github.io/helm-charts
```

### 2. Prepare Aidbox config

```yaml
host: <your-aidbox-host>
protocol: https

config:
  BOX_ADMIN_PASSWORD: <admin-password>
  BOX_BOOTSTRAP_FHIR_PACKAGES: "hl7.fhir.r4.core#4.0.1"
  BOX_COMPATIBILITY_VALIDATION_JSON__SCHEMA_REGEX: '#{:fhir-datetime}'
  BOX_DB_DATABASE: aidbox
  BOX_DB_HOST: aidbox-db-rw
  BOX_DB_PASSWORD: <pg-password>
  BOX_DB_PORT: '5432'
  BOX_DB_USER: postgres
  BOX_FHIR_BUNDLE_EXECUTION_VALIDATION_MODE: limited
  BOX_FHIR_COMPLIANT_MODE: 'true'
  BOX_FHIR_CORRECT_AIDBOX_FORMAT: 'true'
  BOX_FHIR_CREATEDAT_URL: https://aidbox.app/ex/createdAt
  BOX_FHIR_SCHEMA_VALIDATION: 'true'
  BOX_FHIR_SEARCH_AUTHORIZE_INLINE_REQUESTS: 'true'
  BOX_FHIR_SEARCH_CHAIN_SUBSELECT: 'true'
  BOX_FHIR_SEARCH_COMPARISONS: 'true'
  BOX_FHIR_TERMINOLOGY_ENGINE: hybrid
  BOX_FHIR_TERMINOLOGY_ENGINE_HYBRID_EXTERNAL_TX_SERVER: https://tx.health-samurai.io/fhir
  BOX_FHIR_TERMINOLOGY_SERVICE_BASE_URL: https://tx.health-samurai.io/fhir
  BOX_MODULE_SDC_STRICT_ACCESS_CONTROL: 'true'
  BOX_ROOT_CLIENT_SECRET: <client-secret>
  BOX_SEARCH_INCLUDE_CONFORMANT: 'true'
  BOX_SECURITY_AUDIT_LOG_ENABLED: 'true'
  BOX_SECURITY_DEV_MODE: 'true'
  BOX_SETTINGS_MODE: read-write
  BOX_WEB_BASE_URL: <base-url>
  BOX_WEB_PORT: 8888
  
ingress:
  enabled: true
  className: nginx
  annotations:
    acme.cert-manager.io/http01-ingress-class: nginx
    cert-manager.io/cluster-issuer: letsencrypt
```
You can find recomended variables [here](../../../configuration/recommended-envs.md)

All Aidbox helm config values are [here](https://github.com/HealthSamurai/helm-charts/tree/main/aidbox).

### 3. Apply config

```bash
helm upgrade --install aidbox healthsamurai/aidbox \
  --namespace aidbox --create-namespace \
  --values /path/to/aidbox-config.yaml
```

It will install the Aidbox in the `aidbox` namespace, creating that namespace if it doesn't already exist.
