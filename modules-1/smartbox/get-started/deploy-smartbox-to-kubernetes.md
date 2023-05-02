---
description: The guide covers only Smartbox mandatory components deployment in k8s
---

# Deploy Smartbox with Kubernetes

This guide shows how to deploy Smartbox in minimal configuration. One instance of PostgreSQL and a two instances of Aidbox (Sandbox and Portal).

To have a production-ready deployment there also should be:

* Database and Aidbox replicas
* Backups and restoring
* Logging and rotations
* Monitoring and Alerting
* Expose Smartbox to the Internet
* Issuing SSL certificates

{% hint style="warning" %}
This guide does not define exposing Smartbox to the Internet
{% endhint %}

## Prerequisites

* [Kubernetes](https://kubernetes.io/) cluster is set up and running
* `kubectl` utility is installed
* Two Aidbox [licenses](../../../overview/aidbox-user-portal/licenses.md) are obtained
* Email provider [credentials](../../../integrations/email-providers/) are obtained
* GCP connect [credentials](../../../storage-1/gcp-cloud-storage.md) are obtained

## Prebuilt k8s configuration

1. Download the file
2. Populate the [missed ENVs](deploy-smartbox-to-kubernetes.md#smartbox-envs)
3. Run the command `kubectl apply -f smartbox.yaml`

{% file src="../../../.gitbook/assets/smartbox.yaml" %}

{% hint style="info" %}
The `smartbox.yaml` is the k8s compiled templates configuration. The configuration components contained in the file are defined [further in this guide](deploy-smartbox-to-kubernetes.md#resources-templates)
{% endhint %}

## Smartbox mandatory ENVs

### Common for Portal & Sandbox

* PGUSER
* PGPASSWORD
* BOX\_PROVIDER\_DEFAULT\_**\*** values. See the [documentation](../how-to-guides/setup-email-provider.md)

### Sandbox specific

* PGDATABASE: sandbox
* AIDBOX\_LICENSE
* AIDBOX\_ADMIN\_ID
* AIDBOX\_ADMIN\_PASSWORD
* AIDBOX\_BASE\_URL: http://sandbox
* AIDBOX\_ZEN\_ENTRYPOINT: 'smartbox.dev-portal/box'
* AIDBOX\_CLIENT\_ID: sandbox-client
* AIDBOX\_CLIENT\_SECRET: sandbox-secret
* BOX\_AUTH\_LOGIN\_\_REDIRECT: "/"

### Portal specific

* PGDATABASE: smartbox
* AIDBOX\_LICENSE
* AIDBOX\_ADMIN\_ID
* AIDBOX\_ADMIN\_PASSWORD
* AIDBOX\_BASE\_URL: http://smartbox
* AIDBOX\_CLIENT\_ID: portal-client
* AIDBOX\_CLIENT\_SECRET: portal-secret
* BOX\_SMARTBOX\_SANDBOX\_\_URL: http://sandbox
* BOX\_SMARTBOX\_SANDBOX\_\_ADMIN: admin
* BOX\_BULK\_\_STORAGE\_GCP\_\* values. See the [documentation](../../../api-1/bulk-api-1/usdexport.md#gcp)

{% hint style="warning" %}
BOX\_SMARTBOX\_SANDBOX\_\_BASIC is deprecated. Use BOX\_SMARTBOX\_SANDBOX\_\_ADMIN instead
{% endhint %}

{% hint style="info" %}
All the available environment variables are defined [here](../../../reference/configuration/environment-variables/)
{% endhint %}

##

## Components templates

### Database (PostgreSQL)

Smartbox (as an Aidbox configuration) requires an instance of running PostgreSQL. There should be two databases on a PostgreSQL cluster:

* First is for `Sandbox` instance
* Second is for `Portal` instance

#### Volume

{% code title="Volume" %}
```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: aidboxdb-data
  namespace: smartbox
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 50Gi
```
{% endcode %}

#### ENVs

{% code title="ConfigMap - ENVs" %}
```yaml
kind: ConfigMap
metadata:
  name: aidboxdb-envs
  namespace: smartbox
apiVersion: v1
data:
  POSTGRES_DB: postgres
  PGDATA: /data/pg
```
{% endcode %}

{% code title="Secrets - ENVs" %}
```yaml
kind: Secret
apiVersion: v1
metadata:
  name: aidboxdb-envs
  namespace: smartbox
data:
  POSTGRES_USER: cG9zdGdyZXM=      # base64 encoded string postgres
  POSTGRES_PASSWORD: cG9zdGdyZXM=  # base64 encoded string postgres
```
{% endcode %}

#### Config

{% code title="ConfigMap" %}
```yaml
kind: ConfigMap
apiVersion: v1
metadata:
  name: aidboxdb-config
  namespace: smartbox
data:
  postgres.conf: |-
    listen_addresses = '*'
    max_replication_slots = 30
    max_wal_senders = 30
    max_wal_size = '1GB'
    max_worker_processes = 128
    pg_stat_statements.max = 500
    pg_stat_statements.save = false
    pg_stat_statements.track = top
    pg_stat_statements.track_utility = true
    shared_buffers = '1GB'
    shared_preload_libraries = 'pg_stat_statements'
    synchronous_commit = off
    track_io_timing = on
    wal_level = logical
    wal_log_hints = on
```
{% endcode %}

#### StatefulSet

{% code title="StatefulSet" %}
```yaml
kind: StatefulSet
apiVersion: apps/v1
metadata:
  name: aidboxdb
  namespace: smartbox
spec:
  replicas: 1
  selector:
    matchLabels:
      service: aidboxdb
  serviceName: aidboxdb
  template:
    metadata:
      labels:
        service: aidboxdb
    spec:
      containers:
      - name: main
        imagePullPolicy: Always
        image: healthsamurai/aidboxdb:14.5
        volumeMounts:
        - name: db-data
          mountPath: /data
          subPath: pg
        - name: aidboxdb-config
          mountPath: /etc/configs
        - name: db-dshm
          mountPath: /dev/shm
        readinessProbe:
          exec:
            command:
            - bash
            - -c
            - psql -c 'SELECT 1'
          initialDelaySeconds: 10
          timeoutSeconds: 2
        envFrom:
        - configMapRef:
            name: aidboxdb-envs
        - secretRef:
            name: aidboxdb-envs
        ports:
        - containerPort: 5432
          protocol: TCP
        resources:
          requests:
            memory: 1Gi
      volumes:
      - name: db-data
        persistentVolumeClaim:
          claimName: aidboxdb-data
      - name: aidboxdb-config
        configMap:
          name: aidboxdb-config
      - name: db-dshm
        emptyDir:
          medium: Memory
```
{% endcode %}

#### Service

{% code title="Service" %}
```yaml
kind: Service
apiVersion: v1
metadata:
  name: aidboxdb
  namespace: smartbox
spec:
  selector:
    service: aidboxdb
  ports:
  - protocol: TCP
    targetPort: 5432
    port: 5432
```
{% endcode %}

### Sandbox

#### ENVs

```yaml
kind: ConfigMap
apiVersion: v1
metadata:
  name: sandbox
  namespace: smartbox
data:
  BOX_ID: aidboxone
  AIDBOX_ZEN_ENTRYPOINT: 'smartbox.dev-portal/box'
  BOX_AUTH_LOGIN__REDIRECT: "/"
  PGHOST: aidboxdb
  PGDATABASE: sandbox
  AIDBOX_STDOUT_PRETTY: 'true'
  AIDBOX_PORT: '8080'
  AIDBOX_BASE_URL: 'http://sandbox'
  PGPORT: '5432'
  AIDBOX_FHIR_VERSION: 4.0.1
  BOX_PROVIDER_DEFAULT_TYPE: mailgun

```

{% code title="Secret" %}
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: sandbox
  namespace: smartbox
type: Opaque
data:
  PGUSER: cG9zdGdyZXM=                  # base64 encoded postgres
  PGPASSWORD: cG9zdGdyZXM=              # base64 encoded postgres
  AIDBOX_ADMIN_ID: YWRtaW4=             # base64 encoded admin
  AIDBOX_ADMIN_PASSWORD: cGFzc3dvcmQ=   # base64 encoded password
  AIDBOX_CLIENT_ID: cm9vdA==            # base64 encoded root
  AIDBOX_CLIENT_SECRET: c2VjcmV0        # base64 encoded secret

  AIDBOX_LICENSE:                       # your base64 encoded lincense

  # your base64 encoded email provider secrets
  BOX_PROVIDER_DEFAULT_URL:
  BOX_PROVIDER_DEFAULT_FROM:
  BOX_PROVIDER_DEFAULT_USERNAME:
  BOX_PROVIDER_DEFAULT_PASSWORD:
```
{% endcode %}

#### Service

```yaml
kind: Service
apiVersion: v1
metadata:
  name: sandbox
  namespace: smartbox
spec:
  selector:
    service: sandbox
  ports:
  - protocol: TCP
    targetPort: 8080
    port: 80
```

#### Deployment

```yaml
kind: Deployment
apiVersion: apps/v1
metadata:
  name: sandbox
  namespace: smartbox
spec:
  replicas: 1
  selector:
    matchLabels:
      service: sandbox
  template:
    metadata:
      labels:
        service: sandbox
    spec:
      containers:
      - readinessProbe:
          httpGet:
            scheme: HTTP
            path: /health
            port: 8080
          initialDelaySeconds: 20
          timeoutSeconds: 10
          periodSeconds: 10
          failureThreshold: 6
        envFrom:
        - configMapRef:
            name: sandbox
        - secretRef:
            name: sandbox
        name: main
        ports:
        - containerPort: 8080
          protocol: TCP
        livenessProbe:
          httpGet:
            scheme: HTTP
            path: /health
            port: 8080
          initialDelaySeconds: 20
          timeoutSeconds: 10
          periodSeconds: 10
          failureThreshold: 12
        imagePullPolicy: Always
        image: healthsamurai/smartbox:edge
```

### Portal

#### ENVs

{% code title="ConfigMap" %}
```yaml
kind: ConfigMap
apiVersion: v1
metadata:
  name: smartbox
  namespace: smartbox
data:
  BOX_INSTANCE_NAME: smartbox
  BOX_ID: aidboxone
  AIDBOX_ZEN_ENTRYPOINT: 'smartbox.portal/box'
  BOX_AUTH_LOGIN__REDIRECT: "/admin/portal"
  BOX_SMARTBOX_SANDBOX__URL: "http://sandbox"
  PGHOST: aidboxdb
  PGDATABASE: smartbox
  PGPORT: '5432'
  AIDBOX_STDOUT_PRETTY: 'true'
  AIDBOX_PORT: '8080'
  AIDBOX_FHIR_VERSION: 4.0.1
  AIDBOX_BASE_URL: 'http://smartbox'
  BOX_PROVIDER_DEFAULT_TYPE: mailgun
  BOX_BULK__STORAGE_BACKEND: gcp
  BOX_BULK__STORAGE_GCP_SERVICE__ACCOUNT: gcp-ac
```
{% endcode %}

{% code title="Secret" %}
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: smartbox
  namespace: smartbox
type: Opaque
data:
  PGUSER: cG9zdGdyZXM=                  # base64 encoded postgres
  PGPASSWORD: cG9zdGdyZXM=              # base64 encoded postgres
  AIDBOX_ADMIN_ID: YWRtaW4=             # base64 encoded admin
  AIDBOX_ADMIN_PASSWORD: cGFzc3dvcmQ=   # base64 encoded password
  AIDBOX_CLIENT_ID: cm9vdA==            # base64 encoded root
  AIDBOX_CLIENT_SECRET: c2VjcmV0        # base64 encoded secret
  
  BOX_SMARTBOX_SANDBOX__ADMIN: YWRtaW4= # base64 encoded admin
  
  AIDBOX_LICENSE:                       # your base64 encoded lincense
  
  # your base64 encoded email provider secrets
  BOX_PROVIDER_DEFAULT_URL:
  BOX_PROVIDER_DEFAULT_FROM:
  BOX_PROVIDER_DEFAULT_USERNAME:
  BOX_PROVIDER_DEFAULT_PASSWORD:

  # your base64 encoded GCP storage secrets
  BOX_BULK__STORAGE_GCP_SERVICE__ACCOUNT__EMAIL:
  BOX_BULK__STORAGE_GCP_SERVICE__ACCOUNT__PRIVATE__KEY:
  BOX_BULK__STORAGE_GCP_BUCKET:
```
{% endcode %}

#### Service

{% code title="Service" %}
```yaml
kind: Service
apiVersion: v1
metadata:
  name: smartbox
  namespace: smartbox
spec:
  selector:
    service: smartbox
  ports:
  - protocol: TCP
    targetPort: 8080
    port: 80
```
{% endcode %}

#### Deployment

```yaml
kind: Deployment
apiVersion: apps/v1
metadata:
  name: smartbox
  namespace: smartbox
spec:
  replicas: 1
  selector:
    matchLabels:
      service: smartbox
  template:
    metadata:
      labels:
        service: smartbox
    spec:
      containers:
      - readinessProbe:
          httpGet:
            scheme: HTTP
            path: /health
            port: 8080
          initialDelaySeconds: 20
          timeoutSeconds: 10
          periodSeconds: 10
          failureThreshold: 6
        envFrom:
        - configMapRef:
            name: smartbox
        - secretRef:
            name: smartbox
        name: main
        ports:
        - containerPort: 8080
          protocol: TCP
        livenessProbe:
          httpGet:
            scheme: HTTP
            path: /health
            port: 8080
          initialDelaySeconds: 20
          timeoutSeconds: 10
          periodSeconds: 10
          failureThreshold: 12
        imagePullPolicy: Always
        image: healthsamurai/smartbox:edge
```

## Prepare a configuration file

To get a k8s configuration file:

1. Populate the templates above
2. Combine all the templates to the `.yaml` file separating the templates with `---` lines

The beginning of the file should look like.

{% code title="smartbox.yaml" %}
```yaml
---
kind: Namespace
apiVersion: v1
metadata:
  name: smartbox
---
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: aidboxdb-data
  namespace: smartbox
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 10Gi
---
# ... other file content
```
{% endcode %}

## Deploy Smartbox to your cluster

To deploy Smartbox run the command.

```
kubectl apply -f smartbox.yaml
```

The result should look like this.

```shell
namespace/smartbox created
persistentvolumeclaim/aidboxdb-data created
configmap/aidboxdb-envs created
secret/aidboxdb-envs created
configmap/aidboxdb-config created
statefulset.apps/aidboxdb created
service/aidboxdb created
configmap/sandbox created
secret/sandbox created
service/sandbox created
deployment.apps/sandbox created
configmap/smartbox created
secret/smartbox created
service/smartbox created
deployment.apps/smartbox created
```

To check if everything is working fine run the command.

```shell
kubectl get pods -n smartbox
```

There should be 3 running pods.

```
NAME                       READY   STATUS    RESTARTS      AGE
aidboxdb-0                 1/1     Running   1 (31s ago)   99m
sandbox-759d6b46fc-qwzwd   0/1     Running   1 (31s ago)   9m56s
smartbox-979b6dfbb-2bhkn   0/1     Running   1 (31s ago)   9m56s
```
