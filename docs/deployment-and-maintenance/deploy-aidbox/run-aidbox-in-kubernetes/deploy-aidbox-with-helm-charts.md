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
  PGHOST: aidbox-db-rw.aidbox.svc.cluster.local
  PGDATABASE: aidbox
  PGUSER: postgres
  PGPASSWORD: <your-postgres-password>
  AIDBOX_CLIENT_ID: <your-aidbox-client-id>
  AIDBOX_CLIENT_SECRET: <your-aidbox-client-password>
  AIDBOX_ADMIN_ID: <your-aidbox-admin-id>
  AIDBOX_ADMIN_PASSWORD: <your-aidbox-admin-password>
  AIDBOX_LICENSE: <aidbox-license>
  AIDBOX_FHIR_VERSION: 4.0.1
  AIDBOX_FHIR_SCHEMA_VALIDATION: true
  AIDBOX_FHIR_PACKAGES: hl7.fhir.r4.core#4.0.1
  AIDBOX_PORT: 8888
  AIDBOX_COMPLIANCE: enabled
  
ingress:
  enabled: true
  className: nginx
  annotations:
    acme.cert-manager.io/http01-ingress-class: nginx
    cert-manager.io/cluster-issuer: letsencrypt
```

All Aidbox helm config values are [here](https://github.com/HealthSamurai/helm-charts/tree/main/aidbox).

### 3. Apply config

```bash
helm upgrade --install aidbox healthsamurai/aidbox \
  --namespace aidbox --create-namespace \
  --values /path/to/aidbox-config.yaml
```

It will install the Aidbox in the `aidbox` namespace, creating that namespace if it doesn't already exist.
