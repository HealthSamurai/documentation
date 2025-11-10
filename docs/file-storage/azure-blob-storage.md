---
description: Store files in Azure Blob Storage using Shared Access Signatures (SAS) or workload identity for secure, temporary access to blobs.
---

# Azure Blob Storage

Azure Blob Storage stores arbitrary unstructured data like images, files, and backups. Aidbox integrates with Blob Storage through [Shared Access Signatures (SAS)](https://learn.microsoft.com/en-us/rest/api/storageservices/delegate-access-with-shared-access-signature), which provide temporary, secure URLs for uploading and retrieving data.

Aidbox supports three authentication methods for generating SAS tokens:

* **Workload identity** (since 2511) - uses [Azure AD workload identity](https://learn.microsoft.com/en-us/azure/active-directory/workload-identities/workload-identities-overview) with managed identities, no credentials stored in Aidbox
* **User delegation SAS** (since 2508) - uses Azure Application credentials (client ID and secret) to generate SAS tokens
* **Account SAS** - uses storage account keys directly

Workload identity is the most secure option, as it eliminates the need to manage credentials. It works with any environment that supports [OpenID Connect (OIDC)](https://openid.net/developers/how-connect-works/) federation, including Kubernetes, GitHub Actions, and other CI/CD platforms.

## Workload identity

Workload identity allows Aidbox to access Azure Blob Storage using managed identities instead of storing credentials. This approach uses [OpenID Connect (OIDC)](https://openid.net/developers/how-connect-works/) to exchange identity tokens from your platform (Kubernetes, GitHub Actions, etc.) for Azure AD tokens.

### Prerequisites

Configure Azure workload identity for your environment:

1. **Create user-assigned managed identity** in Azure. See [Create a user-assigned managed identity](https://learn.microsoft.com/en-us/entra/identity/managed-identities-azure-resources/how-manage-user-assigned-managed-identities).

2. **Configure federated credential** to establish trust between your OIDC identity provider (like Kubernetes or GitHub Actions) and the managed identity. This allows the identity provider to request Azure tokens on behalf of Aidbox. See [Configure federated identity credentials](https://learn.microsoft.com/en-us/entra/workload-id/workload-identity-federation-create-trust).

3. **Assign storage roles** to the managed identity on your storage account:
   - `Storage Blob Data Contributor` - grants read, write, and delete permissions for blob data
   - `Storage Blob Delegator` - allows generating user delegation SAS tokens

   See [Assign Azure roles](https://learn.microsoft.com/en-us/azure/role-based-access-control/role-assignments-portal).

4. **Deploy Aidbox with OIDC token access**. Your platform must provide OIDC tokens to Aidbox. For Kubernetes with AKS, create a ServiceAccount with the managed identity client ID annotation and label your pod with `azure.workload.identity/use: "true"`. See [Use workload identity with AKS](https://learn.microsoft.com/en-us/azure/aks/workload-identity-deploy-cluster).

After deployment, Aidbox automatically detects the managed identity through [DefaultAzureCredential](https://learn.microsoft.com/en-us/dotnet/api/azure.identity.defaultazurecredential) and uses it to generate SAS tokens. No additional configuration resources like `AzureAccount` or `AzureContainer` are needed.

### Generate SAS URLs

With workload identity configured, request SAS URLs by specifying the storage account and container as query parameters.

Get write URL:

```http
POST /azure/workload-identity/<container-name>?storage-account=<account-name>
Content-Type: application/json

{
  "filename": "document.pdf"
}
```

Response:

```json
{
  "url": "https://<account>.blob.core.windows.net/<container>/document.pdf?sv=...&sig=..."
}
```

Get read URL:

```http
GET /azure/workload-identity/<container-name>/<blob-path>?storage-account=<account-name>
```

Get delete URL:

```http
DELETE /azure/workload-identity/<container-name>/<blob-path>?storage-account=<account-name>
```

The generated URLs are valid for 30 minutes by default. Add `expiration=<seconds>` query parameter to customize the duration.

See also:
* [Azure AD workload identities overview](https://learn.microsoft.com/en-us/azure/active-directory/workload-identities/workload-identities-overview)
* [Workload identity federation](https://learn.microsoft.com/en-us/entra/workload-id/workload-identity-federation)
* [Configure workload identity on AKS](https://learn.microsoft.com/en-us/azure/aks/workload-identity-deploy-cluster) (Kubernetes example)
* [DefaultAzureCredential class](https://learn.microsoft.com/en-us/dotnet/api/azure.identity.defaultazurecredential)

## User delegation SAS

User delegation SAS generates signed URLs using Azure Application credentials. This method requires storing client ID and secret in Aidbox but provides better security than account keys.

### Prerequisites

Configure Azure AD application and Aidbox resources:

1. **Register application in Azure AD** to get tenant ID, client ID, and client secret. See [Register an application in Microsoft Entra ID](https://learn.microsoft.com/en-us/entra/identity-platform/quickstart-register-app).

2. **Create AzureAccount resource** in Aidbox with application credentials:

```http
POST /AzureAccount
Content-Type: application/json

{
  "id": "my-azure-account",
  "tenantId": "<tenant-id>",
  "clientId": "<client-id>",
  "clientSecret": "<client-secret>",
  "sasType": "userDelegation"
}
```

3. **Create AzureContainer resource** to link Aidbox to your storage container:

```http
POST /AzureContainer
Content-Type: application/json

{
  "resourceType": "AzureContainer",
  "id": "my-container",
  "account": {
    "id": "my-azure-account",
    "resourceType": "AzureAccount"
  },
  "storage": "<storage-account-name>",
  "container": "<container-name>"
}
```

4. **Assign storage roles** to the application:
   - `Storage Blob Delegator` - allows generating user delegation SAS tokens
   - `Storage Blob Data Contributor` - grants read, write, and delete permissions for blob data

   See [Assign Azure roles](https://learn.microsoft.com/en-us/azure/role-based-access-control/role-assignments-portal).

### Generate SAS URLs

Request SAS URLs using the AzureContainer resource ID.

Get write URL:

```http
POST /azure/storage/<container-id>

{
  "blob": "document.pdf"
}
```

Get read URL:

```http
GET /azure/storage/<container-id>/<blob-path>
```

Get delete URL:

```http
DELETE /azure/storage/<container-id>/<blob-path>
```

## Account SAS

Account SAS generates signed URLs using storage account keys. This is the simplest method but less secure since it requires storing the account key in Aidbox. Not recommended for production use.

### Prerequisites

Configure storage account keys and Aidbox resources:

1. **Get storage account key** from Azure Portal under "Access keys" section. See [Manage storage account access keys](https://learn.microsoft.com/en-us/azure/storage/common/storage-account-keys-manage).

2. **Create AzureAccount resource** with account name and key:

```http
POST /AzureAccount
Content-Type: application/json

{
  "id": "my-storage-account",
  "key": "<storage-account-key>"
}
```

3. **Create AzureContainer resource** to link Aidbox to your storage container:

```http
POST /AzureContainer
Content-Type: application/json

{
  "resourceType": "AzureContainer",
  "id": "my-container",
  "account": {
    "id": "my-storage-account",
    "resourceType": "AzureAccount"
  },
  "storage": "<storage-account-name>",
  "container": "<container-name>"
}
```

### Generate SAS URLs

Request SAS URLs using the AzureContainer resource ID.

Get write URL:

```http
POST /azure/storage/<container-id>

{
  "blob": "document.pdf"
}
```

Get read URL:

```http
GET /azure/storage/<container-id>/<blob-path>
```

Get delete URL:

```http
DELETE /azure/storage/<container-id>/<blob-path>
```
