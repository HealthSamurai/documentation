---
description: Production-ready Aidbox deployment on Kubernetes. Covers cluster setup, database, ingress, monitoring, logging, alerting, and security best practices.
---

# Deploy Production-ready Aidbox to Kubernetes

## Production-ready infrastructure

Key infrastructure elements:

* [Cluster configuration](deploy-production-ready-aidbox-to-kubernetes.md#cluster-configuration-and-tooling) — Node pool and tooling
* [Database](deploy-production-ready-aidbox-to-kubernetes.md#database) — Cloud or self-managed database
* [Aidbox](deploy-production-ready-aidbox-to-kubernetes.md#aidbox) — Aidbox installation
* [Logging](deploy-production-ready-aidbox-to-kubernetes.md#logging) — Сollect application and cluster logs
* [Monitoring](deploy-production-ready-aidbox-to-kubernetes.md#monitoring) — Сollect, alert, and visualize cluster and application metrics
* [Security](deploy-production-ready-aidbox-to-kubernetes.md#extra) — Vulnerability scanning and policy management

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

Aidbox supports all popular managed Postgresql databases. Supported versions - 13 and higher. See more details in this article — [Run Aidbox on managed PostgreSQL](../run-aidbox-on-managed-postgresql.md).

* [AWS RDS Aurora](https://aws.amazon.com/ru/rds/aurora/)
* [GCP Cloud SQL for PostgreSQL](https://cloud.google.com/sql/postgresql)
* [Azure Database for PostgreSQL](https://docs.microsoft.com/en-us/azure/postgresql/single-server/overview)

### Self-managed solution

For a self-managed solution in Kubernetes, we recommend using the [CloudNativePG operator](https://cloudnative-pg.io/). It provides high availability, automated failover, backups, and seamless PostgreSQL cluster management.

#### Install CloudNativePG operator

```bash
kubectl apply --server-side -f \
  https://raw.githubusercontent.com/cloudnative-pg/cloudnative-pg/main/releases/cnpg-1.28.0.yaml
```

#### Create PostgreSQL cluster

{% code title="postgres-secret.yaml" lineNumbers="true" %}
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: postgres
  namespace: prod
stringData:
  password: <your-password>
  username: postgres
type: kubernetes.io/basic-auth
```
{% endcode %}

{% code title="postgres-cluster.yaml" lineNumbers="true" %}
```yaml
apiVersion: postgresql.cnpg.io/v1
kind: Cluster
metadata:
  name: aidbox-db
  namespace: prod
spec:
  instances: 3
  bootstrap:
    initdb:
      database: aidbox
      owner: postgres
      secret:
        name: postgres
  postgresql:
    parameters:
      shared_buffers: '2GB'
      max_wal_size: '4GB'
      pg_stat_statements.max: '500'
      pg_stat_statements.track: 'all'
      shared_preload_libraries: 'pg_stat_statements'
  resources:
    requests:
      memory: 4Gi
      cpu: '2'
    limits:
      memory: 8Gi
  storage:
    size: 100Gi
    storageClass: managed-premium
```
{% endcode %}

CloudNativePG automatically creates services for connecting to the database:
- `aidbox-db-rw` — read-write service (primary)
- `aidbox-db-ro` — read-only service (replicas)
- `aidbox-db-r` — any instance

#### Configure backups

CloudNativePG supports backups to S3, Azure Blob Storage, and Google Cloud Storage. See [CloudNativePG backup documentation](https://cloudnative-pg.io/documentation/current/backup/) for details.

### Alternative solutions

* [Crunchy Postgres Operator](https://github.com/CrunchyData/postgres-operator) — Production-ready PostgreSQL on Kubernetes.

## Aidbox

First, you must get an Aidbox license on the [Aidbox user portal](https://aidbox.app).

{% hint style="info" %}
You might want to use the [Helm charts](https://github.com/HealthSamurai/helm-charts/tree/main/aidbox) prepared by our DevOps engineers to make the deployment experience smoother.
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
  AIDBOX_FHIR_PACKAGES: 'hl7.fhir.r4.core#4.0.1' # your packages
  AIDBOX_TERMINOLOGY_SERVICE_BASE_URL: 'https://tx.health-samurai.io/fhir'
  AIDBOX_BOX_ID: aidbox
  AIDBOX_PORT: '8080'
  AIDBOX_STDOUT_PRETTY: all
  BOX_INSTANCE_NAME: aidbox
  BOX_METRICS_PORT: '8765'
  PGDATABASE: aidbox
  PGHOST: db.prod.svc.cluster.local   # database address
  PGPORT: '5432'                      # database port
  AIDBOX_FHIR_SCHEMA_VALIDATION: 'true'
  AIDBOX_COMPLIANCE: 'enabled'
  AIDBOX_CORRECT_AIDBOX_FORMAT: 'true'
  AIDBOX_CREATED_AT_URL: 'https://aidbox.app/ex/createdAt'
  BOX_SEARCH_INCLUDE_CONFORMANT: 'true'
  BOX_SEARCH_FHIR__COMPARISONS: 'true'
  BOX_COMPATIBILITY_VALIDATION_JSON__SCHEMA_REGEX: '#{:fhir-datetime}'
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

When Aidbox starts for the first time, resolving all the dependencies takes longer. If you encounter startupProbe failure, you might want to consider increasing the initialDelaySeconds and failureThreshold under the startupProbe spec in the config above.

For running multiple Aidbox replicas, ensure all instances share the same RSA keys and secrets. See [Configure Aidbox](../../../configuration/configure-aidbox-and-multibox.md#set-up-rsa-private-public-keys-and-secret) for details.

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

General logging & audit information can be found in this article — [Logging & Audit](../../../modules/observability/logs/README.md)

Aidbox supports integration with the following systems:

* ElasticSearch — [Elastic Logs and Monitoring Integration](../../../modules/observability/logs/how-to-guides/elastic-logs-and-monitoring-integration.md)
* Loki — [Grafana Loki Log management integration](../../../modules/observability/logs/how-to-guides/loki-log-management-integration.md)
* DataDog — [Datadog Log management integration](../../../modules/observability/logs/how-to-guides/datadog-log-management-integration.md)

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

Aidbox metrics has integration with Grafana, which can generate dashboards and upload them to Grafana — [Grafana Integration](../../../modules/observability/metrics/monitoring/set-up-grafana-integration.md)

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
