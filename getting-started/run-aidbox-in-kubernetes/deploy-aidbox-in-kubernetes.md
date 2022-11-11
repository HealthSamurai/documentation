---
description: Work In Progress
---

# Deploy Aidbox to Kubernetes

## Production-ready infrastructure&#x20;

Key infrastructure elements:

* [Cluster configuration ](deploy-aidbox-in-kubernetes.md#cluster-configuration-and-tooling) - Node pool and tooling
* [Database](deploy-aidbox-in-kubernetes.md#database) - Cloud or self managed database
* [Aidbox](deploy-aidbox-in-kubernetes.md#aidbox) - Aidbox installation
* [Logging](deploy-aidbox-in-kubernetes.md#logging) - Сollect application and cluster logs
* [Monitoring](deploy-aidbox-in-kubernetes.md#monitoring) - Сollect, alert, and visualize cluster and application metrics
* [Security](deploy-aidbox-in-kubernetes.md#extra) - Vulnerability scanning and policy management

## Cluster configuration and tooling

Recommended Kubernetes cluster configuration:

* Small and medium workloads - 3 nodes X 4 VCPU 16 GB RAM
* Huge workloads - 3 nodes X 8 VCPU X 64 GB RAM

Toolkit required for development and deployment:

* [AWS](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html), [GCP](https://cloud.google.com/storage/docs/gsutil\_install), [AZURE](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli) - Cloud provider CLI and SDK. Depends on your cloud provider:&#x20;
* [Kubectl](https://kubernetes.io/docs/tasks/tools/) - connection and cluster management&#x20;
* [Helm](https://helm.sh/) - Kubernetes package manager&#x20;
* [Lens](https://k8slens.dev/) - Kubernetes IDE

**Optional** - Development and Delivery tooling:

* [Terraform](https://www.terraform.io/) - Infrastructure automation tool
* [Grafana tanka](https://grafana.com/oss/tanka/) - configuration utility for your Kubernetes
* [Argo CD](https://argoproj.github.io/) - GitOps delivery and management
* [Flux](https://fluxcd.io/) - set of continuous and progressive delivery solutions for Kubernetes

## Database

### Managed solution

Aidbox supports all popular managed Postgresql databases. Supported versions - 13 and higher. See more details in this article — [Run Aidbox on managed PostgreSQL](https://docs.aidbox.app/getting-started/run-aidbox-on-managed-postgresql).

* [AWS RDS Aurora ](https://aws.amazon.com/ru/rds/aurora/)
* [GCP Cloud SQL for PostgreSQL](https://cloud.google.com/sql/postgresql)
* [Azure Database for PostgreSQL](https://docs.microsoft.com/en-us/azure/postgresql/single-server/overview)

### Self-managed solution

For a self-managed solution, we recommend use  [AidboxDB image](https://hub.docker.com/r/healthsamurai/aidboxdb) . This image contains all required extensions, backup tool, and pre-build replication support. Read more information in the documentation — [AidboxDB](../../storage-1/aidboxdb-image.md).

First step — create volume

{% code title="Persistent Volume" lineNumbers="true" %}
```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: db-master-data
  namespace: prod
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 300Gi
  # depend on your cloud provider. Use SSD volumes
  storageClassName: managed-premium
```
{% endcode %}

Next - create all required configs, like `postgresql.conf`, required container parameters and credentials.

{% code title="postgresql.conf" lineNumbers="true" %}
```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: db-pg-config
  namespace: prod
data:
  postgres.conf: |-
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
```
{% endcode %}

{% code title="db-config Configmap" lineNumbers="true" %}
```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: db-config
  namespace: prod
data:
  PGDATA: /data/pg
  POSTGRES_DB: postgres
```
{% endcode %}

{% code title="db-secret Secret" lineNumbers="true" %}
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: db-secret
  namespace: prod
type: Opaque
data:
  POSTGRES_PASSWORD: cG9zdGdyZXM=
  POSTGRES_USER: cG9zdGdyZXM=
```
{% endcode %}

Now we can create a database `StatefulSet`

{% code title="Db Master StatefulSet" lineNumbers="true" %}
```yaml
apiVersion: apps/v1
kind: StatefulSet
metadata:
  name: prod-db-master
  namespace: prod
spec:
  replicas: 1
  serviceName: prod-db
  selector:
    matchLabels:
      service: prod-db
  template:
    metadata:
      labels:
        service: prod-db
    spec:
      volumes:
        - name: db-pg-config
          configMap:
            name: db-pg-config
            defaultMode: 420
        - name: db-dshm
          emptyDir:
            medium: Memory
        - name: db-data
          persistentVolumeClaim:
            claimName: db-master-data
      containers:
        - name: main
          image: healthsamurai/aidboxdb:14.2
          ports:
            - containerPort: 5432
              protocol: TCP
          envFrom:
            - configMapRef:
                name: db-config
            - secretRef:
                name: db-secret
          volumeMounts:
            - name: db-pg-config
              mountPath: /etc/configs
            - name: db-dshm
              mountPath: /dev/shm
            - name: db-data
              mountPath: /data
              subPath: pg
```
{% endcode %}

Create master database service

{% code title="Database Service" lineNumbers="true" %}
```yaml
apiVersion: v1
kind: Service
metadata:
  name: db
  namespace: prod
spec:
  ports:
    - protocol: TCP
      port: 5432
      targetPort: 5432
  selector:
    service: db

```
{% endcode %}

Replica installation contains all the same steps but required additional configuration

{% code title="Replica DB config" lineNumbers="true" %}
```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: db-replica
  namespace: prod
data:
  PG_ROLE: replica
  PG_MASTER_HOST: db-master
  PG_REPLICA: streaming_replica_streaming
  PGDATA: /data/pg
  POSTGRES_DB: postgres
```
{% endcode %}

For backups and WAL archivation we are recommended cloud-native solution [WAL-G](https://github.com/wal-g/wal-g). You can find full information about configuration and usage on [documentation page](https://github.com/wal-g/wal-g/blob/master/docs/PostgreSQL.md).

* [Configure storage access](https://github.com/wal-g/wal-g/blob/6ec7680ef5cb66c938faf180c97b3378b701d685/docs/STORAGES.md) — WAL-G can store backups in S3, Google Cloud Storage, Azure, or a local file system.
* Recommended backup policy — Full backup every week, incremental backup every day.

### Alternative solutions

A set of tools to perform HA PostgreSQL with fail and switchover, automated backups.

* [Patroni](https://github.com/zalando/patroni) — A Template for PostgreSQL HA with ZooKeeper, ETCD or Consul.
* [Postgres operator](https://github.com/zalando/postgres-operator) — The Postgres Operator delivers an easy to run HA PostgreSQL clusters on Kubernetes.

## Aidbox

Create ConfigMap with all required config and database connection&#x20;

{% code title="Aidbox ConfigMap" lineNumbers="true" %}
```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: aidbox
  namespace: prod
data:
  AIDBOX_BASE_URL: https://my.box.url
  AIDBOX_BOX_ID: aidbox
  AIDBOX_FHIR_VERSION: 4.0.1
  AIDBOX_PORT: '8080'
  AIDBOX_STDOUT_PRETTY: all
  BOX_INSTANCE_NAME: aidbox
  BOX_METRICS_PORT: '8765'
  PGDATABASE: aidbox
  PGHOST: db.prod.svc.cluster.local   # database address
  PGPORT: '5432'                      # database port
```
{% endcode %}

{% code title="Aidbox Secret" lineNumbers="true" %}
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: aidbox
  namespace: prod
data:
  AIDBOX_ADMIN_ID: <admin_login>
  AIDBOX_ADMIN_PASSWORD: <admin_password>
  AIDBOX_CLIENT_ID: <root_client_id>
  AIDBOX_CLIENT_SECRET: <root_client_password>
  AIDBOX_LICENSE: <JWT-LICENSE>
  PGPASSWORD: <db_password>        # database password
  PGUSER: <db_user>                # database username
```
{% endcode %}

Aidbox Deployment

{% code title="Aidbox Deployment" lineNumbers="true" %}
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: aidbox
  namespace: prod
spec:
  replicas: 2
  selector:
    matchLabels:
      service: aidbox
  template:
    metadata:
      labels:
        service: aidbox
    spec:
      containers:
        - name: main
          image: healthsamurai/aidboxone:latest
          ports:
            - containerPort: 8080
              protocol: TCP
            - containerPort: 8765
              protocol: TCP
          envFrom:
            - configMapRef:
                name: aidbox
            - secretRef:
                name: aidbox
          livenessProbe:
            httpGet:
              path: /health
              port: 8080
              scheme: HTTP
            initialDelaySeconds: 20
            timeoutSeconds: 10
            periodSeconds: 10
            successThreshold: 1
            failureThreshold: 12
          readinessProbe:
            httpGet:
              path: /health
              port: 8080
              scheme: HTTP
            initialDelaySeconds: 20
            timeoutSeconds: 10
            periodSeconds: 10
            successThreshold: 1
            failureThreshold: 6
          startupProbe:
            httpGet:
                path: /health
                port: 8080
            scheme: HTTP
            initialDelaySeconds: 20
            timeoutSeconds: 5
            periodSeconds:  5
            successThreshold: 1
            failureThreshold: 4

```
{% endcode %}

All additional information about HA Aidbox configuration you can find in this article — [HA Aidbox](https://docs.aidbox.app/getting-started/run-aidbox-in-kubernetes/high-available-aidbox)&#x20;

To verify that Aidbox started correctly you can check the logs:

```bash
kubectl logs -f <aidbox-pod-name>
```

## \[TODO] Ingress

A Cluster must have an [ingress controller](https://kubernetes.io/docs/concepts/services-networking/ingress-controllers/) Installed. Our recommendation is use [kubernetes Ingress NGINX Controller](https://github.com/kubernetes/ingress-nginx). As alternative, you can use [Traefic](https://github.com/traefik/traefik/).

#### Install Ingress NGINX controller

{% code title="# Install Ingress NGINX" %}
```shell
helm repo add ....
helm install ....
```
{% endcode %}

#### CertManager

To provide secure HTTPS connection you can use paid SSL certificates, issued for your domain, or use LetsEncrypt issued certificates. In case of using LetsEcrypt we recommend [install and configure Cert Manager](https://cert-manager.io/docs/installation/helm/) Operator

{% code title="# Install Cert Manager" %}
```bash
helm repo add jetstack https://charts.jetstack.io
helm repo update
helm install \
  cert-manager jetstack/cert-manager \
  --namespace cert-manager \
  --create-namespace \
  --version v1.9.1 \
  --set installCRDs=true
```
{% endcode %}

Configure Cluster Issuer:

{% code title="" lineNumbers="true" %}
```yaml
apiVersion: cert-manager.io/v1
kind: ClusterIssuer
metadata:
  name: letsencrypt
spec:
  acme:
    email: hello@my-domain.com
    preferredChain: ''
    privateKeySecretRef:
      name: issuer-key
    server: https://acme-v02.api.letsencrypt.org/directory
    solvers:
      - http01:
          ingress:
            class: nginx  # Ingress class name
```
{% endcode %}

{% hint style="info" %}
If you use Multibox image and want to use cert manger - you should configure DNS01 authorization to provide wildcard certificates

[https://letsencrypt.org/docs/challenge-types/#dns-01-challenge](https://letsencrypt.org/docs/challenge-types/#dns-01-challenge)
{% endhint %}

## \[TODO] Logging

Supported log storages

* ElasticSearch
* Loki & Promtail
* DataDog

Recomendation&#x20;

Install Elastic & Kibana Configure Aidbox ES appender

## \[TODO] Monitoring

Recommendation:

* KubePrometheus HELM install command

Alternatives: Metrics

* VictoriaMetrics
* Thanos
* Mimir

Visualization

* Grafana (part of KubePrometheus)

Example&#x20;

Scrape aidbox metrics via service monitor&#x20;

Add service monitor file

## \[TODO] Alerting

#### Alert rules

{% code lineNumbers="true" %}
```yaml
alert: SlowRequests
for: 1m
expr: histogram_quantile(0.99, sum (rate(aidbox_http_request_duration_seconds_bucket[5m])) by (le, route, instance)) > 5
labels: {severity: ticket}
annotations:
  title: Long HTTP query execution
  metric: '{{ $labels.route }}'
  value: '{{ $value | printf "%.2f" }}'
```
{% endcode %}

Disk usage....

Unavailable Deployments, nodes, STS ...

#### Alert delivery

Alert manager template for telegram and slack

Telegram bot&#x20;

{% code lineNumbers="true" %}
```yaml
global:
  resolve_timeout: 5m
  telegram_api_url: 'https://api.telegram.org/'
route:
  group_by: [alertname instance]
  # Default receiver
  receiver: <my-ops-chat>
  routes:
  # Mute watchdog alert
  - receiver: empty
    match: {alertname: Watchdog}
receivers:
- name: empty
- name: <my-ops-chat>
  telegram_configs:
  - chat_id: <chat-id>
    api_url: https://api.telegram.org
    parse_mode: HTML
    message: |-
      <b>[{{ .CommonLabels.instance }}] {{ .CommonLabels.alertname }}</b>
      {{ .CommonAnnotations.title }}
      {{ range .Alerts }}{{ .Annotations.metric }}: {{ .Annotations.value }}
      {{ end }}
    bot_token: <bot-token>

```
{% endcode %}

Link how to delivery to the Slak ......

#### Alternatives

* Embedded Grafana alerts
* Grafana OnCall

## Security

Vulnerability and security scanners

* [Trivy operator](https://github.com/aquasecurity/trivy-operator) - Kubernetes-native security toolkit.
* [Trivy operator Lens extension](https://github.com/aquasecurity/trivy-operator-lens-extension) - UI extension for Lens which provides visibility into Trivy reports

Kubernetes Policy Management

* [Kyverno](https://kyverno.io/) OR [Gatekeeper](https://github.com/open-policy-agent/gatekeeper) - Kubernetes  policy management

Advanced

* [Datree](https://www.datree.io/) — k8s resources linter
