# Deploy Production-ready Aidbox to Kubernetes

## Production-ready infrastructure

Key infrastructure elements:

* [Cluster configuration](deploy-aidbox-in-kubernetes.md#cluster-configuration-and-tooling) — Node pool and tooling
* [Database](deploy-aidbox-in-kubernetes.md#database) — Cloud or self-managed database
* [Aidbox](deploy-aidbox-in-kubernetes.md#aidbox) — Aidbox installation
* [Logging](deploy-aidbox-in-kubernetes.md#logging) — Сollect application and cluster logs
* [Monitoring](deploy-aidbox-in-kubernetes.md#monitoring) — Сollect, alert, and visualize cluster and application metrics
* [Security](deploy-aidbox-in-kubernetes.md#extra) — Vulnerability scanning and policy management

## Cluster configuration and tooling

Recommended Kubernetes cluster configuration:

* Small and medium workloads — 3 nodes X 4 VCPU 16 GB RAM
* Huge workloads — 3 nodes X 8 VCPU X 64 GB RAM

Toolkit required for development and deployment:

* [AWS](https://docs.aws.amazon.com/cli/latest/userguide/getting-started-install.html), [GCP](https://cloud.google.com/storage/docs/gsutil_install), [AZURE](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli) - Cloud provider CLI and SDK. Depends on your cloud provider:
* [Kubectl](https://kubernetes.io/docs/tasks/tools/) - connection and cluster management
* [Helm](https://helm.sh/) - Kubernetes package manager
* [Lens](https://k8slens.dev/) - Kubernetes IDE

**Optional** - Development and Delivery tooling:

* [Terraform](https://www.terraform.io/) - Infrastructure automation tool
* [Grafana tanka](https://grafana.com/oss/tanka/) - configuration utility for your Kubernetes
* [Argo CD](https://argoproj.github.io/) - GitOps delivery and management
* [Flux](https://fluxcd.io/) - set of continuous and progressive delivery solutions for Kubernetes

## Database

### Managed solution

Aidbox supports all popular managed Postgresql databases. Supported versions - 13 and higher. See more details in this article — [Run Aidbox on managed PostgreSQL](https://docs.aidbox.app/getting-started/run-aidbox-on-managed-postgresql).

* [AWS RDS Aurora](https://aws.amazon.com/ru/rds/aurora/)
* [GCP Cloud SQL for PostgreSQL](https://cloud.google.com/sql/postgresql)
* [Azure Database for PostgreSQL](https://docs.microsoft.com/en-us/azure/postgresql/single-server/overview)

### Self-managed solution

For a self-managed solution, we recommend using the [AidboxDB image](https://hub.docker.com/r/healthsamurai/aidboxdb). This image contains all required extensions, backup tools, and pre-build replication support. Read more information in the documentation — [AidboxDB](../../../storage-1/aidboxdb-image/).

{% hint style="info" %}
To streamline the deployment process, our DevOps engineers have prepared [Helm charts](https://github.com/Aidbox/helm-charts/tree/main/aidboxdb) that you may find helpful.&#x20;
{% endhint %}

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
  serviceName: db
  selector:
    matchLabels:
      service: db
  template:
    metadata:
      labels:
        service: db
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

Create a master database service

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

Replica installation contains all the same steps but requires additional configuration

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

For backups and WAL archiving we recommend a cloud-native solution [WAL-G](https://github.com/wal-g/wal-g). Full information about its configuration and usage is on this [documentation page](https://github.com/wal-g/wal-g/blob/master/docs/PostgreSQL.md).

* [Configure storage access](https://github.com/wal-g/wal-g/blob/6ec7680ef5cb66c938faf180c97b3378b701d685/docs/STORAGES.md) — WAL-G can store backups in S3, Google Cloud Storage, Azure, or a local file system.
* Recommended backup policy — Full backup every week, incremental backup every day.

### Alternative solutions

A set of tools to perform HA PostgreSQL with fail and switchover, automated backups.

* [Patroni](https://github.com/zalando/patroni) — A Template for PostgreSQL HA with ZooKeeper, ETCD or Consul.
* [Postgres operator](https://github.com/zalando/postgres-operator) — The Postgres Operator delivers an easy-to-run HA PostgreSQL clusters on Kubernetes.

## Aidbox

First, you must get an Aidbox license on the [Aidbox user portal](https://aidbox.app).

{% hint style="info" %}
You might want to use the [Helm charts](https://github.com/Aidbox/helm-charts/tree/main/aidbox) prepared by our DevOps engineers to make the deployment experience smoother.
{% endhint %}

Create ConfigMap with all required config and database connection

{% hint style="info" %}
This ConfigMap example uses our default Aidbox Configuration Project Template. It's recommended to clone this template and bind your Aidbox installation with it.
{% endhint %}

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
  BOX_PROJECT_GIT_URL: "https://github.com/Aidbox/aidbox-project-template.git"
  BOX_PROJECT_GIT_PROTOCOL: "https"
  BOX_PROJECT_GIT_TARGET__PATH: "/tmp/aidbox-project"
  BOX_PROJECT_GIT_CHECKOUT: "main"
  AIDBOX_ZEN_ENTRYPOINT: main/box
  AIDBOX_DEV_MODE: "false"
  AIDBOX_ZEN_DEV_MODE: "false"
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
  AIDBOX_ADMIN_PASSWORD: <admin_password>
  AIDBOX_CLIENT_SECRET: <root_client_password>
  AIDBOX_LICENSE: <JWT-LICENSE>    # JWT license from the Aidbox user portal
  PGUSER: <db_user>                # database username
  PGPASSWORD: <db_password>        # database password
  
  BOX_AUTH_KEYS_SECRET: <random_string_auth_secret>
  BOX_AUTH_KEYS_PRIVATE: <rsa_private_key> 
  BOX_AUTH_KEYS_PUBLIC: <rsa_public_key> 
  
  # or just use our samples for non-production installation
  # BOX_AUTH_KEYS_SECRET: "auth-key-secret"
  # BOX_AUTH_KEYS_PRIVATE: "-----BEGIN RSA PRIVATE KEY-----\nMIICXAIBAAKBgQCRLKv0n9HPsajw3wcDH1k5DUSPPdKjxqp8h4OZKiG3wGEFYXi9\nfxBbpkQXjxGEmORi8UR4aM41kX8dd4SdMRGS1VX2AMgLEAFq354MpGBPIeJyv00y\nqV6wW0HT58+Nh+xdridDFSHkkplJFjDuQbYjfQzbSNECA31ME/GI9rGomQIDAQAB\nAoGAEYGytFecCnjtC6wHiVK71JeTIZd12fJsj4MbhWpJYeJxCMAz+l0S7MxweGtU\nNFpoKz7XUBJqcJcMvlHSBA89ZDobp3HS0R8ZDcdxossNRio3Ix1bRG7Pxnhs3R/T\nsOxlrQSgnSbg1k6M5iVSZt1ptCwch+ZLG37tD3ZvdAN0LCECQQC0IFiPJJEPauUi\neKmW4oUgBvOUVA93EqnBiv9lzk7UxrPgusFqnY02qJouDNvXXso6+FM8u9DNxSvw\nHPIuqJvhAkEAzlNYaJzoInkCS5PYTGg2f1GqRih9WHj8NUukfgbO61xT9QscM6An\n+RF8dfshU2zuaQFLTBPWrS0Nk0ZOxLFjuQJAZ4gz/sqwyiDR5RdfuscmZ3s3ZClQ\n3ksO4ZzoIXcMnoY7e888PvCh6ynLvO5NKiRkrrJu/XiikrNjBtdMaH8nYQJADkCF\nl9xW0KLJPM0+oLCGKy9J8sSzO9xHl6rc9vOjcXCUQBX/YbWLbVH+5ett9uRMZ6Z2\nPBAWwSmeiXDO2hliyQJBAI/7Gtzf1Z2O5pDgNMLkKcyX4BqsHFKFSD5Btb/zReEq\nTsr6vTvzucjJcS8843vgyhIUDtW2cu7G9BGxSfsZNCw=\n-----END RSA PRIVATE KEY-----\n"
  # BOX_AUTH_KEYS_PUBLIC: "-----BEGIN PUBLIC KEY-----\nMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCRLKv0n9HPsajw3wcDH1k5DUSP\nPdKjxqp8h4OZKiG3wGEFYXi9fxBbpkQXjxGEmORi8UR4aM41kX8dd4SdMRGS1VX2\nAMgLEAFq354MpGBPIeJyv00yqV6wW0HT58+Nh+xdridDFSHkkplJFjDuQbYjfQzb\nSNECA31ME/GI9rGomQIDAQAB\n-----END PUBLIC KEY-----\n"
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

When Aidbox starts for the first time, resolving all the dependencies takes longer. If you encounter startupProbe failure, you might want to consider increasing the initialDelaySeconds and failureThreshold under the startupProbe spec in the config above.&#x20;

All additional information about HA Aidbox configuration can be found in this article — [HA Aidbox](https://docs.aidbox.app/getting-started/run-aidbox-in-kubernetes/high-available-aidbox).

To verify that Aidbox started correctly you can check the logs:

```bash
kubectl logs -f <aidbox-pod-name>
```

Create the Aidbox k8s service

{% code title="Aidbox service" lineNumbers="true" %}
```yaml
apiVersion: v1
kind: Service
metadata:
  name: aidbox
  namespace: prod
spec:
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8080
  selector:
    service: aidbox

```
{% endcode %}

## Ingress

A Cluster must have an [ingress controller](https://kubernetes.io/docs/concepts/services-networking/ingress-controllers/) Installed.

Our recommendation is to use the [Kubernetes Ingress NGINX Controller](https://github.com/kubernetes/ingress-nginx). As an alternative, you can use [Traefic](https://github.com/traefik/traefik/).

More additional information about Ingress in k8s can be found in this documentation — [Kubernetes Service Networking](https://kubernetes.io/docs/concepts/services-networking/ingress/)

### Ingress NGINX controller

Ingress-nginx — is an Ingress controller for Kubernetes using [NGINX](https://www.nginx.org/) as a reverse proxy and load balancer.

{% code title="Install Ingress NGINX" %}
```shell
helm upgrade \
  --install ingress-nginx ingress-nginx \
  --repo https://kubernetes.github.io/ingress-nginx \
  --namespace ingress-nginx --create-namespace
```
{% endcode %}

### CertManager

To provide a secure HTTPS connection you can use paid SSL certificates, issued for your domain, or use LetsEncrypt-issued certificates. In the case of using LetsEcrypt, we recommend [installing and configuring Cert Manager](https://cert-manager.io/docs/installation/helm/) Operator

{% code title="Install Cert Manager" %}
```bash
helm repo add jetstack https://charts.jetstack.io
helm repo update
helm install \
  cert-manager jetstack/cert-manager \
  --namespace cert-manager \
  --create-namespace \
  --version v1.10.0 \       # Or latest available version
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
If you use Multibox image and want to use cert manger — you should configure DNS01 authorization to provide wildcard certificates

[https://letsencrypt.org/docs/challenge-types/#dns-01-challenge](https://letsencrypt.org/docs/challenge-types/#dns-01-challenge)
{% endhint %}

### Ingress resource

Now you can create k8s `Ingress` for Aidbox deployment

{% code title="Ingress" lineNumbers="true" %}
```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: aidbox
  namespace: prod
  annotations:
    acme.cert-manager.io/http01-ingress-class: nginx
    cert-manager.io/cluster-issuer: letsencrypt
    kubernetes.io/ingress.class: nginx
spec:
  tls:
    - hosts:
        - my.box.url
      secretName: aidbox-tls
  rules:
    - host: my.box.url
      http:
        paths:
          - path: /
            pathType: ImplementationSpecific
            backend:
              service:
                name: aidbox
                port:
                  number: 80
```
{% endcode %}

Now you can test ingress

```bash
curl https://my.box.url
```

## Logging

General logging & audit information can be found in this article — [Logging & Audit](https://docs.aidbox.app/core-modules/logging-and-audit)

Aidbox supports integration with the following systems:

* ElasticSearch — [Elastic Logs and Monitoring Integration](https://docs.aidbox.app/core-modules/logging-and-audit/integrations/elastic-logs-and-monitoring-integration)
* Loki — [Grafana Loki Log management integration](https://docs.aidbox.app/core-modules/logging-and-audit/integrations/loki-integration)
* DataDog — [Datadog Log management integration](https://docs.aidbox.app/core-modules/logging-and-audit/integrations/aidbox-logs-and-datadog-integration)

### ElasticSearch integration

You can install ECK using the [official guide.](https://www.elastic.co/guide/en/cloud-on-k8s/master/k8s-installing-eck.html)

Configure Aidbox and ES integration

<pre class="language-yaml" data-title="Aidbox ConfigMap" data-line-numbers><code class="lang-yaml">apiVersion: v1
kind: Secret
metadata:
  name: aidbox
  namespace: prod
data:
<strong>  ...
</strong><strong>  AIDBOX_ES_URL = http://es-service.es-ns.svc.cluster.local
</strong>  AIDBOX_ES_AUTH = &#x3C;user>:&#x3C;password>
<strong>  ...
</strong></code></pre>

### DataDog integration

<pre class="language-yaml" data-title="Aidbox ConfigMap" data-line-numbers><code class="lang-yaml">apiVersion: v1
kind: Secret
metadata:
  name: aidbox
  namespace: prod
data:
<strong>  ...
</strong><strong>  AIDBOX_DD_API_KEY: &#x3C;Datadog API Key>
</strong><strong>  ...
</strong></code></pre>

## Monitoring

For monitoring our recommendation is to use the [Kube Prometheus stack](https://github.com/prometheus-community/helm-charts/tree/main/charts/kube-prometheus-stack)

```shell
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo update
helm install prometheus prometheus-community/kube-prometheus-stack
```

Create Aidbox metrics service

{% code title="" lineNumbers="true" %}
```yaml
apiVersion: v1
kind: Service
metadata:
  name: aidbox-metrics
  namespace: prod
  labels:
    operated: prometheus
spec:
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8765
  selector:
    service: aidbox
```
{% endcode %}

Create ServiceMonitor config for scrapping metrics data

{% code title="ServiceMonitor" lineNumbers="true" %}
```yaml
apiVersion: monitoring.coreos.com/v1
kind: ServiceMonitor
metadata:
  labels:
    app.kubernetes.io/component: metrics
    release: kube-prometheus
    serviceMonitorSelector: aidbox
  name: aidbox
  namespace: kube-prometheus
spec:
  endpoints:
    - honorLabels: true
      interval: 10s
      path: /metrics
      targetPort: 8765
    - honorLabels: true
      interval: 60s
      path: /metrics/minutes
      targetPort: 8765
    - honorLabels: true
      interval: 10m
      path: /metrics/hours
      targetPort: 8765
  namespaceSelector:
    any: true
  selector:
    matchLabels:
      operated: prometheus
```
{% endcode %}

Or you can directly specify the Prometheus scrapers configuration

```yaml
global:
  external_labels:
    monitor: 'aidbox'
scrape_configs:
  - job_name: aidbox
    scrape_interval: 5s
    metrics_path: /metrics
    static_configs:
      - targets: [ 'aidbox-metrics.prod.svc.cluster.local:8765' ]

  - job_name: aidbox-minutes
    scrape_interval: 30s
    metrics_path: /metrics/minutes
    static_configs:
      - targets: [ 'aidbox-metrics.prod.svc.cluster.local:8765' ]

  - job_name: aidbox-hours
    scrape_interval: 1m
    scrape_timeout: 30s                     
    metrics_path: /metrics/hours
    static_configs:
      - targets: [ 'aidbox-metrics.prod.svc.cluster.local:8765' ]
```

### Alternative solutions

* [VictoriaMetrics](https://victoriametrics.com/) — High-Performance Open Source Time Series Database.
* [Thanos](https://thanos.io/) — highly available Prometheus setup with long-term storage capabilities.
* [Grafana Mimir](https://grafana.com/oss/mimir/) — highly available, multi-tenant, long-term storage for Prometheus.

### Export the Aidbox Grafana dashboard

Aidbox metrics has integration with Grafana, which can generate dashboards and upload them to Grafana — [Grafana Integration](https://docs.aidbox.app/core-modules/monitoring/grafana-integration)

### Additional monitoring

System monitoring:

* [node exporter](https://github.com/prometheus/node_exporter) — Prometheus exporter for hardware and OS metrics exposed by \*NIX kernels
* [kube state metrics](https://github.com/kubernetes/kube-state-metrics) — is a simple service that listens to the Kubernetes API server and generates metrics about the state of the objects
* [cadvisor](https://kubernetes.io/docs/concepts/cluster-administration/system-metrics/) — container usage metrics

PostgreSQL monitoring:

* [pg\_exporter](https://github.com/prometheus-community/postgres_exporter) — Prometheus exporter for PostgreSQL server metrics

## Alerting

[Alerting rules](https://prometheus.io/docs/prometheus/latest/configuration/alerting_rules/) allow you to define alert conditions based on Prometheus expression language expressions and to send notifications about firing alerts to an external service.

### Alert rules

Alert for long-running HTTP queries with P99 > 5s in 5m interval

{% code lineNumbers="true" %}
```yaml
alert: SlowRequests
for: 5m
expr: histogram_quantile(0.99, sum (rate(aidbox_http_request_duration_seconds_bucket[5m])) by (le, route, instance)) > 5
labels: {severity: ticket}
annotations:
  title: Long HTTP query execution
  metric: '{{ $labels.route }}'
  value: '{{ $value | printf "%.2f" }}'
```
{% endcode %}

### Alert delivery

Alert manager template for Telegram

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

All other integrations you can find on the [AlertManager documentation page.](https://prometheus.io/docs/alerting/latest/configuration/)

### Additional tools

* Embedded Grafana alerts
* Grafana OnCall

## Security

Vulnerability and security scanners:

* [Trivy operator](https://github.com/aquasecurity/trivy-operator) — Kubernetes-native security toolkit.
* [Trivy operator Lens extension](https://github.com/aquasecurity/trivy-operator-lens-extension) — UI extension for Lens which provides visibility into Trivy reports

Kubernetes Policy Management:

* [Kyverno](https://kyverno.io/) OR [Gatekeeper](https://github.com/open-policy-agent/gatekeeper) — Kubernetes policy management

Advanced:

* [Datree](https://www.datree.io/) — k8s resources linter
