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

1. **Create storage account and container** in Azure if you don't have one already.

2. **Create user-assigned managed identity** in Azure. See [Create a user-assigned managed identity](https://learn.microsoft.com/en-us/entra/identity/managed-identities-azure-resources/how-manage-user-assigned-managed-identities).

3. **Configure federated credential** to establish trust between your OIDC identity provider (like Kubernetes or GitHub Actions) and the managed identity. This allows the identity provider to request Azure tokens on behalf of Aidbox. See [Configure federated identity credentials](https://learn.microsoft.com/en-us/entra/workload-id/workload-identity-federation-create-trust).

4. **Assign storage roles** - Grant the managed identity access to your storage account by assigning these roles:
   - `Storage Blob Data Contributor` - for read, write, and delete permissions
   - `Storage Blob Delegator` - for generating user delegation SAS tokens

   Assign both roles to the managed identity (from step 2) on your storage account. See [Assign Azure roles](https://learn.microsoft.com/en-us/azure/role-based-access-control/role-assignments-portal).

5. **Deploy Aidbox with OIDC token access**. Your platform must provide OIDC tokens to Aidbox. For Kubernetes with AKS:

   Create a ServiceAccount with the managed identity client ID annotation:

   ```yaml
   apiVersion: v1
   kind: ServiceAccount
   metadata:
     name: aidbox-sa
     namespace: aidbox
     annotations:
       azure.workload.identity/client-id: "<managed-identity-client-id>"
   ```

   Label your pod with `azure.workload.identity/use: "true"`:

   ```yaml
   apiVersion: v1
   kind: Pod
   metadata:
     name: aidbox
     namespace: aidbox
     labels:
       azure.workload.identity/use: "true"
   spec:
     serviceAccountName: aidbox-sa
     containers:
     - name: aidbox
       image: healthsamurai/aidboxone:edge
   ```

   The `<managed-identity-client-id>` is the client ID from the user-assigned managed identity created in step 2. See [Use workload identity with AKS](https://learn.microsoft.com/en-us/azure/aks/workload-identity-deploy-cluster).

After deployment, Aidbox automatically detects the managed identity through [DefaultAzureCredential](https://learn.microsoft.com/en-us/dotnet/api/azure.identity.defaultazurecredential) and uses it to generate SAS tokens. No additional configuration resources like `AzureAccount` or `AzureContainer` are needed.

### Generate SAS URLs

With workload identity configured, request SAS URLs by specifying the storage account and container in the URL path.

Get write URL:

```http
POST /azure/workload-identity/storage/<storage-account>/<container-name>
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
GET /azure/workload-identity/storage/<storage-account>/<container-name>/<blob-path>
```

Get delete URL:

```http
DELETE /azure/workload-identity/storage/<storage-account>/<container-name>/<blob-path>
```

### Configuration

**Query parameters:**

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `expiration` | integer | 1800 | URL validity duration in seconds (30 minutes default) |
| `redirect` | boolean | false | Return HTTP 302 redirect instead of JSON. Useful for `<img>` tags |

**Request body (POST only):**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `filename` | string | No | Blob name. If not specified, random UUID with optional extension is generated |

See also:
* [Azure AD workload identities overview](https://learn.microsoft.com/en-us/azure/active-directory/workload-identities/workload-identities-overview)
* [Workload identity federation](https://learn.microsoft.com/en-us/entra/workload-id/workload-identity-federation)
* [Configure workload identity on AKS](https://learn.microsoft.com/en-us/azure/aks/workload-identity-deploy-cluster) (Kubernetes example)
* [DefaultAzureCredential class](https://learn.microsoft.com/en-us/dotnet/api/azure.identity.defaultazurecredential)

## User delegation SAS

User delegation SAS generates signed URLs using Azure Application credentials. This method requires storing client ID and secret in Aidbox but provides better security than account keys.

### Prerequisites

Configure Azure AD application and Aidbox resources:

1. **Create storage account and container** in Azure if you don't have one already.

2. **Register application in Azure AD** to get tenant ID, client ID, and client secret. See [Register an application in Microsoft Entra ID](https://learn.microsoft.com/en-us/entra/identity-platform/quickstart-register-app).

3. **Create [AzureAccount](../reference/system-resources-reference/cloud-module-resources.md#azureaccount) resource** in Aidbox with application credentials:

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

4. **Create [AzureContainer](../reference/system-resources-reference/cloud-module-resources.md#azurecontainer) resource** to link Aidbox to your storage container:

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

5. **Assign storage roles** to the application:
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

### Configuration

**Query parameters:**

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `expiration` | integer | 1800 | URL validity duration in seconds (30 minutes default) |
| `redirect` | boolean | false | Return HTTP 302 redirect instead of JSON. Useful for `<img>` tags |

**Request body (POST only):**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `blob` | string | No | Blob name. If not specified, random UUID with optional extension from [AzureContainer](../reference/system-resources-reference/cloud-module-resources#azurecontainer) is generated |

See [AzureAccount](../reference/system-resources-reference/cloud-module-resources#azureaccount) and [AzureContainer](../reference/system-resources-reference/cloud-module-resources#azurecontainer) resource reference for configuration details.

## Account SAS

Account SAS generates signed URLs using storage account keys. This is the simplest method but less secure since it requires storing the account key in Aidbox. Not recommended for production use.

### Prerequisites

Configure storage account keys and Aidbox resources:

1. **Create storage account and container** in Azure if you don't have one already.

2. **Get storage account key** from Azure Portal under "Access keys" section. See [Manage storage account access keys](https://learn.microsoft.com/en-us/azure/storage/common/storage-account-keys-manage).

3. **Create [AzureAccount](../reference/system-resources-reference/cloud-module-resources#azureaccount) resource** with account name and key:

```http
POST /AzureAccount
Content-Type: application/json

{
  "id": "my-storage-account",
  "key": "<storage-account-key>"
}
```

4. **Create [AzureContainer](../reference/system-resources-reference/cloud-module-resources#azurecontainer) resource** to link Aidbox to your storage container:

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

### Configuration

**Query parameters:**

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `expiration` | integer | 1800 | URL validity duration in seconds (30 minutes default) |
| `redirect` | boolean | false | Return HTTP 302 redirect instead of JSON. Useful for `<img>` tags |

**Request body (POST only):**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `blob` | string | No | Blob name. If not specified, random UUID with optional extension from [AzureContainer](../reference/system-resources-reference/cloud-module-resources#azurecontainer) is generated |

See [AzureAccount](../reference/system-resources-reference/cloud-module-resources#azureaccount) and [AzureContainer](../reference/system-resources-reference/cloud-module-resources#azurecontainer) resource reference for configuration details.

## Using SAS URLs

Once you have obtained a SAS URL from any of the methods above, you can use it directly with standard HTTP clients.

### Upload file

Upload a file using the write URL:

```bash
curl -X PUT "<signed-url>" \
  -H "x-ms-blob-type: BlockBlob" \
  -H "Content-Type: text/plain" \
  -d "hello"
```

For binary files, use `--data-binary` with a file:

```bash
curl -X PUT "<signed-url>" \
  -H "x-ms-blob-type: BlockBlob" \
  -H "Content-Type: image/png" \
  --data-binary @image.png
```

### Download file

Download a file using the read URL:

```bash
curl -s "<signed-url>"
```

Save to file:

```bash
curl -s "<signed-url>" -o downloaded-file.txt
```

### Delete file

Delete a file using the delete URL:

```bash
curl -X DELETE "<signed-url>"
```

### Display image in HTML

**Option 1: Use signed URL directly**

Get the signed URL from Aidbox and use it in HTML:

```html
<img src="<signed-url>" alt="Image from Azure Blob Storage" />
```

This requires a two-step process: first get the URL from Aidbox API, then use it in your HTML.

**Option 2: Use redirect feature**

Add `?redirect=true` parameter to have Aidbox automatically redirect to the signed URL.

For User delegation SAS and Account SAS:

```bash
GET /azure/storage/<container-id>/<blob-path>?redirect=true
```

For Workload identity:

```bash
GET /azure/workload-identity/storage/<storage-account>/<container>/<blob-path>?redirect=true
```

Both return HTTP 302 redirect with `Location` header containing the signed URL.

This allows you to directly reference the Aidbox endpoint in HTML:

```html
<!-- User delegation SAS / Account SAS -->
<img src="/azure/storage/<container-id>/<blob-path>?redirect=true" alt="Image" />

<!-- Workload identity -->
<img src="/azure/workload-identity/storage/<storage-account>/<container>/<blob-path>?redirect=true" alt="Image" />
```
