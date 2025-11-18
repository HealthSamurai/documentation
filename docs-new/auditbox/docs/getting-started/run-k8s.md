# Run Auditbox with Helm Charts

Our Helm charts streamline the deployment process, enabling easy configuration and customization while ensuring a seamless deployment experience within Kubernetes clusters. Whether you're a healthcare institution, developer, or DevOps engineer, these Helm charts provide a straightforward path to deploying Auditbox in your Kubernetes environment.

## Elasticsearch

Auditbox uses Elasticsearch as its data storage backend. You need to have an Elasticsearch cluster running in your Kubernetes environment. You can deploy Elasticsearch using the official Elasticsearch Helm chart or any other method that suits your infrastructure.

## Auditbox

### Add auditbox helm repo

```bash
helm repo add auditbox https://aidbox.github.io/helm-charts
helm repo update
```

### Prepare Auditbox config

```yaml
image:
  repository: healthsamurai/auditbox
  tag: "alpha"

config:
  ELASTIC_URI: http://elasticsearch.elasticsearch.svc.cluster.local:9200
  AUDITBOX_BASE_URL: https://<your-auditbox-host>
  IDP_AUTHORIZE_ENDPOINT: https://<your-keycloak-host>/realms/auditbox/protocol/openid-connect/auth
  IDP_TOKEN_ENDPOINT: http://keycloak.keycloak.svc.cluster.local:8080/realms/auditbox/protocol/openid-connect/token
  IDP_CLIENT_ID: auditbox
  IDP_CLIENT_SECRET: <your-idp-client-secret>
  AUDITBOX_API_AUTH_ENABLED: true
  BINDING: 0.0.0.0
  PORT: 3000
  AUDITBOX_LOG_LEVEL: info
```

All Auditbox helm config values are [here](https://github.com/Aidbox/helm-charts/tree/main/auditbox#values).

### Apply config

```bash
helm upgrade --install auditbox auditbox/auditbox \
  --namespace auditbox --create-namespace \
  --values /path/to/auditbox-config.yaml
```

It will install the Auditbox in the `auditbox` namespace, creating that namespace if it doesn't already exist.
