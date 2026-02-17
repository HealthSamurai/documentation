---
description: >-
  Mount secrets from Azure Key Vault into Aidbox pods using the Azure Key Vault
  CSI Provider.
---

# Azure Key Vault

This guide covers mounting secrets from [Azure Key Vault](https://azure.microsoft.com/en-us/products/key-vault) into Aidbox using the Azure Key Vault CSI Provider. For the general concept and CSI Driver installation, see [External secret stores](./).

## Prerequisites

* Secrets Store CSI Driver installed ([instructions](./#install-the-secrets-store-csi-driver))
* An existing Azure Key Vault with a secret named `client-secret`
* A Service Principal with `Key Vault Secrets User` role on the Key Vault

## Install the Azure Key Vault Provider

```bash
helm repo add csi-secrets-store-provider-azure \
  https://azure.github.io/secrets-store-csi-driver-provider-azure/charts

helm install azure-kv csi-secrets-store-provider-azure/csi-secrets-store-provider-azure \
  --namespace kube-system \
  --set secrets-store-csi-driver.install=false
```

Wait for pods to be ready:

```bash
kubectl wait --for=condition=ready pod \
  -l app=csi-secrets-store-provider-azure \
  --namespace kube-system \
  --timeout=120s
```

## Configure access

Create a Kubernetes Secret with Service Principal credentials:

```bash
kubectl create secret generic azure-kv-creds \
  --from-literal=clientid="<your-client-id>" \
  --from-literal=clientsecret="<your-client-secret>"
```

{% hint style="warning" %}
In production, consider using [Workload Identity](https://learn.microsoft.com/en-us/azure/aks/workload-identity-overview) instead of Service Principal credentials stored in a Kubernetes Secret.
{% endhint %}

## Kubernetes resources

### SecretProviderClass

{% code title="secret-provider-class.yaml" lineNumbers="true" %}
```yaml
apiVersion: secrets-store.csi.x-k8s.io/v1
kind: SecretProviderClass
metadata:
  name: aidbox-azure-secrets
spec:
  provider: azure
  parameters:
    keyvaultName: "<your-keyvault-name>"
    tenantId: "<your-tenant-id>"
    cloudName: "AzurePublicCloud"
    objects: |
      array:
        - |
          objectName: client-secret
          objectType: secret
```
{% endcode %}

The `objects` section lists what to fetch from Azure Key Vault and mount as files.

### Aidbox Deployment

Add the CSI volume and configure Aidbox to read secrets from mounted files:

{% code title="aidbox.yaml (volume fragment)" lineNumbers="true" %}
```yaml
spec:
  template:
    spec:
      containers:
        - name: aidbox
          env:
            - name: AIDBOX_SECRET_FILES_ENABLED
              value: "true"
            - name: AIDBOX_SECRET_FILES_DIRS
              value: "/run/azure-secrets"
          volumeMounts:
            - name: azure-secrets
              mountPath: "/run/azure-secrets"
              readOnly: true
      volumes:
        - name: azure-secrets
          csi:
            driver: secrets-store.csi.k8s.io
            readOnly: true
            volumeAttributes:
              secretProviderClass: "aidbox-azure-secrets"
            nodePublishSecretRef:
              name: azure-kv-creds
```
{% endcode %}

The `nodePublishSecretRef` tells the CSI Driver to use the `azure-kv-creds` Kubernetes Secret when authenticating with Azure Key Vault.

## Verify

Verify that the secret file is mounted:

```bash
kubectl exec deploy/aidbox -- cat /run/azure-secrets/client-secret
```

## Secret rotation

Update the secret in Azure Key Vault:

```bash
az keyvault secret set \
  --vault-name <your-keyvault-name> \
  --name client-secret \
  --value '<new-secret-value>'
```

Wait for the CSI Driver to pick up the change (based on `rotationPollInterval`):

```bash
sleep 35
kubectl exec deploy/aidbox -- cat /run/azure-secrets/client-secret
```

Aidbox detects the file modification and uses the new value on the next request — no restart required.
