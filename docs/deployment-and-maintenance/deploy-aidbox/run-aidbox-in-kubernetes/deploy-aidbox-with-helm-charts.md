# Deploy Aidbox with Helm Charts

Our Helm charts streamline the deployment process, enabling easy configuration and customization while ensuring a seamless deployment experience within Kubernetes clusters. Whether you're a healthcare institution, developer, or DevOps engineer, these Helm charts provide a straightforward path to deploying Aidbox in your Kubernetes environment.

Before deployment please read about infrastructure prerequisites.

## Database

[AidboxDB](../../../reference/environment-variables/aidboxdb-environment-variables.md) is a specialized version of the open-source PostgreSQL database, tailored for use as the data storage backend for Aidbox.

1. #### **Add aidbox helm repo**

```bash
helm repo add aidbox https://aidbox.github.io/helm-charts
```

2. #### Prepare database config

```yaml
config: |-
  listen_addresses = '*'
  shared_buffers = '2GB'
  max_wal_size = '4GB'
  pg_stat_statements.max = 500
  pg_stat_statements.save = false
  pg_stat_statements.track = top
  pg_stat_statements.track_utility = true
  shared_preload_libraries = 'pg_stat_statements'
  track_io_timing = on
  wal_level = logical
  wal_log_hints = on
  archive_command = 'wal-g wal-push %p'
  restore_command = 'wal-g wal-fetch %f %p'

env:
  PGDATA: /data/pg
  POSTGRES_DB: postgres
  POSTGRES_PASSWORD: <your-postgres-password>

image.repository: healthsamurai/aidboxdb
image.tag: "16.1"
storage:
  size: "10Gi"
  className: <your-storage-className>
```

All AidboxDB helm config values are [here](https://github.com/Aidbox/helm-charts/tree/main/aidboxdb#values).

3. #### Apply config

```bash
helm upgrade --install aidboxdb aidbox/aidboxdb \
  --namespace postgres --create-namespace \
  --values /path/to/db-config.yaml
```

## Aidbox

First, you must get an Aidbox license on the [Aidbox user portal.](https://aidbox.app/)

1. #### Prepare Aidbox config

```yaml
host: <your-aidbox-host>
protocol: https

config:
  BOX_DB_HOST: aidboxdb.ips.svc.cluster.local
  BOX_DB_DATABASE: postgres
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

All AidboxDB helm config values are [here](https://github.com/Aidbox/helm-charts/tree/main/aidbox#values).

2. #### Apply config

```bash
helm upgrade --install aidbox aidbox/aidbox \
  --namespace aidbox --create-namespace \
  --values /path/to/aidbox-config.yaml
```

It will install the Aidbox in the `aidbox` namespace, creating that namespace if it doesn't already exist.
