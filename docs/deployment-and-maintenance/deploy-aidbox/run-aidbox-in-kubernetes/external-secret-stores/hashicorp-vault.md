---
description: >-
  Mount secrets from HashiCorp Vault into Aidbox pods using the Vault CSI
  Provider.
---

# HashiCorp Vault

This guide covers mounting secrets from [HashiCorp Vault](https://www.vaultproject.io/) into Aidbox using the Vault CSI Provider. For the general concept and CSI Driver installation, see [External secret stores](./).

## Prerequisites

* Secrets Store CSI Driver installed ([instructions](./#install-the-secrets-store-csi-driver))
* A running Vault instance (in-cluster or external)

## Install the Vault CSI Provider

Deploy Vault with the CSI Provider enabled:

```bash
helm repo add hashicorp https://helm.releases.hashicorp.com

helm install vault hashicorp/vault \
  --set "server.dev.enabled=true" \
  --set "csi.enabled=true"
```

Wait for pods to be ready:

```bash
kubectl wait --for=condition=ready pod -l app.kubernetes.io/name=vault --timeout=120s
kubectl wait --for=condition=ready pod -l app.kubernetes.io/name=vault-csi-provider --timeout=120s
```

{% hint style="warning" %}
This guide uses Vault in dev mode for simplicity. In production, deploy Vault with proper storage backend, TLS, and auto-unseal. See the [Vault documentation](https://developer.hashicorp.com/vault/docs).
{% endhint %}

## Configure Vault

### Store a secret

```bash
kubectl exec vault-0 -- vault kv put secret/aidbox/client \
  client-secret='<your-secret-value>'
```

### Enable Kubernetes authentication

```bash
kubectl exec vault-0 -- vault auth enable kubernetes

kubectl exec vault-0 -- sh -c 'vault write auth/kubernetes/config \
  kubernetes_host="https://$KUBERNETES_PORT_443_TCP_ADDR:443"'
```

### Create a policy and role

The policy grants read-only access to secrets under `secret/data/aidbox/*`. The role binds this policy to the `aidbox` ServiceAccount.

```bash
kubectl exec -i vault-0 -- vault policy write aidbox /dev/stdin <<'EOF'
path "secret/data/aidbox/*" {
  capabilities = ["read"]
}
EOF

kubectl exec vault-0 -- vault write auth/kubernetes/role/aidbox \
  bound_service_account_names=aidbox \
  bound_service_account_namespaces=default \
  policies=aidbox \
  ttl=1h
```

## Kubernetes resources

### ServiceAccount

The Vault CSI Provider authenticates via Kubernetes auth bound to this ServiceAccount:

{% code title="service-account.yaml" lineNumbers="true" %}
```yaml
apiVersion: v1
kind: ServiceAccount
metadata:
  name: aidbox
```
{% endcode %}

### SecretProviderClass

{% code title="secret-provider-class.yaml" lineNumbers="true" %}
```yaml
apiVersion: secrets-store.csi.x-k8s.io/v1
kind: SecretProviderClass
metadata:
  name: aidbox-vault-secrets
spec:
  provider: vault
  parameters:
    vaultAddress: "http://vault.default:8200"
    roleName: "aidbox"
    objects: |
      - objectName: "client-secret"
        secretPath: "secret/data/aidbox/client"
        secretKey: "client-secret"
```
{% endcode %}

The `objects` section lists what to fetch from Vault and mount as files.

### Aidbox Deployment

Add the CSI volume and configure Aidbox to read secrets from mounted files:

{% code title="aidbox.yaml (volume fragment)" lineNumbers="true" %}
```yaml
spec:
  template:
    spec:
      serviceAccountName: aidbox
      containers:
        - name: aidbox
          env:
            - name: AIDBOX_SECRET_FILES_ENABLED
              value: "true"
            - name: AIDBOX_SECRET_FILES_DIRS
              value: "/run/vault-secrets"
          volumeMounts:
            - name: vault-secrets
              mountPath: "/run/vault-secrets"
              readOnly: true
      volumes:
        - name: vault-secrets
          csi:
            driver: secrets-store.csi.k8s.io
            readOnly: true
            volumeAttributes:
              secretProviderClass: "aidbox-vault-secrets"
```
{% endcode %}

## Verify

Verify that the secret file is mounted:

```bash
kubectl exec deploy/aidbox -- cat /run/vault-secrets/client-secret
```

## Secret rotation

Update the secret in Vault:

```bash
kubectl exec vault-0 -- vault kv put secret/aidbox/client \
  client-secret='<new-secret-value>'
```

Wait for the CSI Driver to pick up the change (based on `rotationPollInterval`):

```bash
sleep 35
kubectl exec deploy/aidbox -- cat /run/vault-secrets/client-secret
```

Aidbox detects the file modification and uses the new value on the next request — no restart required.
