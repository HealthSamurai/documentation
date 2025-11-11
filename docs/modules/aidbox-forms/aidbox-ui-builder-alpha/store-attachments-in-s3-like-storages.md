---
description: >-
  This article explains how to configure Aidbox Forms to store attachments in
  S3-Compatible storages
---

# Store attachments in S3-like storages

Aidbox Forms provides an ability to store files from attachment items in cloud storages like S3.

## 1. Setup storage account

Aidbox supports Amazon S3, Google Cloud Storage, Azure Container. Here is the guide on [how to setup this integration](../../../file-storage/aws-s3.md).

## 2. Update SDCConfig

Aidbox Forms supports two authentication methods: credential-based (using stored credentials) and workload identity (using managed identities).

### Option 1: Workload Identity (Recommended)

> Available since 2511

Workload identity is the most secure option as it eliminates the need to store credentials in Aidbox. It uses your cloud provider's native identity management.

**For GCP Workload Identity:**

```json
{
  "resourceType": "SDCConfig",
  "default": true,
  "storage": {
    "bucket": "sdc-files"
  }
}
```

**For Azure Workload Identity:**

```json
{
  "resourceType": "SDCConfig",
  "default": true,
  "storage": {
    "storageAccount": "mystorageaccount",
    "container": "sdc-files"
  }
}
```

**Prerequisites:**
- Configure workload identity for your Kubernetes cluster (GKE Workload Identity or AKS Managed Identity)
- Grant appropriate storage permissions to the managed identity
- See [GCP Cloud Storage](../../../file-storage/gcp-cloud-storage.md) or [Azure Blob Storage](../../../file-storage/azure-blob-storage.md) documentation for detailed setup instructions

### Option 2: Credential-Based Authentication

After you setup the integration, put the reference to storage credential resource into the SDCConfig resource and set the bucket.

**For AWS, GCP with service account, or Azure with SAS:**

```json
{
  "resourceType": "SDCConfig",
  "default": true,
  "storage": {
    "account": {
      "id": "test-account",
      "resourceType": "AwsAccount"
    },
    "bucket": "sdc-files"
  }
}
```

Supported `resourceType` values:
- `AwsAccount` - for Amazon S3
- `GcpServiceAccount` - for Google Cloud Storage with service account
- `AzureContainer` - for Azure Blob Storage with SAS

Now all files from attachment items will be stored in cloud storage.
