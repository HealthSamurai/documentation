---
description: Upload and retrieve files from Google Cloud Storage using Workload Identity or service account credentials.
---

# GCP Cloud Storage

Storing unstructured data like text files, documents, or backups directly in your database creates bloat and performance issues. Aidbox integrates with Google Cloud Storage to handle these files efficiently, generating secure signed URLs that let clients upload and download directly from GCS without routing data through your server.

## Authentication methods

Aidbox provides two ways to authenticate with Google Cloud Storage. The Workload Identity approach is recommended for production deployments.

### Workload Identity (recommended, since 2510)

[Workload Identity](https://cloud.google.com/kubernetes-engine/docs/concepts/workload-identity) is a Google Cloud security feature that allows applications running in GKE or Cloud Run to access GCP services without managing service account keys. Instead of storing credentials, your workload assumes an identity configured at the infrastructure level.

This approach offers several advantages:

- **No credential management** - keys never leave GCP's infrastructure
- **Simpler configuration** - credentials are ambient in the runtime environment
- **Better security posture** - eliminates the risk of leaked service account keys
- **Automatic rotation** - tokens are refreshed automatically by GCP

The Workload Identity endpoints don't require creating `GcpServiceAccount` resources in Aidbox. They work automatically when your GCP environment is properly configured.

### Service account keys (legacy)

The original approach uses explicit service account credentials stored in Aidbox. While still supported, this method requires managing and securing private keys, making it less suitable for production environments.

## Workload Identity API

These endpoints require [Workload Identity](https://cloud.google.com/iam/docs/workload-identity-federation) to be configured in your GCP environment.

### Setup requirements

For Workload Identity endpoints to work, you need to configure your GCP environment to allow Aidbox to authenticate as a service account without explicit credentials.

#### Enable Workload Identity

For GKE clusters, enable Workload Identity on the cluster with a workload pool configured for your project.

For Cloud Run, Workload Identity is enabled by default. Your service automatically gets a service account identity.

#### Create a GCP service account

Create a dedicated GCP service account for Cloud Storage operations. This service account will be used by Aidbox to access Cloud Storage buckets.

#### Grant Cloud Storage permissions

Assign the `roles/storage.objectAdmin` role to the service account on your Cloud Storage bucket. This role includes all necessary permissions for file operations.

Alternatively, for more restrictive access, grant only the specific permissions needed:

- `storage.buckets.get` - required for all operations
- `storage.objects.get` - required for download URLs
- `storage.objects.create` - required for upload URLs
- `storage.objects.delete` - required for delete URLs

#### Grant URL signing permissions

The service account needs permission to sign URLs on behalf of itself. Assign the `roles/iam.serviceAccountTokenCreator` role to the service account, with the service account as both the principal and the resource.

This permission allows the service account to generate signed URLs.

#### Bind service accounts

Configure your workload to use the GCP service account identity:

- For GKE: bind your Kubernetes service account to the GCP service account using the `roles/iam.workloadIdentityUser` role and annotate the Kubernetes service account
- For Cloud Run: configure your service to run with the GCP service account

#### Verify the setup

After configuration, Aidbox will automatically use the ambient credentials. Test by making a request to generate a signed URL:

```
POST /gcp/storage/<bucket-name>

filename: test.txt
```

If you receive a signed URL in the response, Workload Identity is configured correctly.

See also:
- [Workload Identity setup guide](https://cloud.google.com/kubernetes-engine/docs/how-to/workload-identity)
- [Cloud Storage IAM permissions](https://cloud.google.com/storage/docs/access-control/iam-permissions)
- [Signed URL documentation](https://cloud.google.com/storage/docs/access-control/signed-urls)

### Generate upload URL

Request a signed URL for uploading a file to Cloud Storage.

```
POST /gcp/storage/<bucket-id>

filename: documents/consent.txt
```

Response contains a signed URL that you can use to upload your file:

```
status: 200
url: https://storage.googleapis.com/my-bucket/documents/consent.txt?X-Goog-Algorithm=...
```

After receiving the signed URL, upload your file directly to Google Cloud Storage using a PUT request with the file content in the body.

Example workflow:

1. Request upload URL from Aidbox:

```
POST /gcp/storage/my-bucket
Body: {"filename": "reports/results.txt"}
```

2. Upload file to the signed URL:

```
PUT <signed-url>
Body: <file-content>
```

### Generate download URL

Request a signed URL for downloading a file from Cloud Storage.

```
GET /gcp/storage/<bucket-id>/<file-path>
```

Example:

```
GET /gcp/storage/my-bucket/documents/consent.txt
```

Response:

```
status: 200
url: https://storage.googleapis.com/my-bucket/documents/consent.txt?X-Goog-Algorithm=...
```

The file path can include multiple directory levels (e.g., `documents/2024/january/report.txt`).

### Generate delete URL

Request a signed URL for deleting a file from Cloud Storage.

```
DELETE /gcp/storage/<bucket-id>/<file-path>
```

Example:

```
DELETE /gcp/storage/my-bucket/temp/backup.txt
```

Response:

```
status: 200
url: https://storage.googleapis.com/my-bucket/temp/backup.txt?X-Goog-Algorithm=...
```

### Configuration options

All Workload Identity endpoints accept an optional `expiration` query parameter that sets the signed URL lifetime in seconds. If not specified, URLs expire after 7 days (604800 seconds).

```
POST /gcp/storage/my-bucket?expiration=3600

filename: data.txt
```

The URL will expire in 1 hour (3600 seconds).

Shorter expiration times improve security but require clients to use the URL quickly. For long-running uploads or downloads, use longer expiration periods.

## Service Account API (legacy)

This approach uses explicit service account credentials stored in Aidbox. It remains available for backward compatibility but is not recommended for new deployments.

### Create GcpServiceAccount

Create a resource containing service account credentials with read/write access to Cloud Storage.

```
PUT /GcpServiceAccount

id: my-account
service-account-email: storage-access@myproject.iam.gserviceaccount.com
private-key: "-----BEGIN PRIVATE KEY-----\n...\n-----END PRIVATE KEY-----"
```

The private key is sensitive data. Store it securely and rotate it periodically.

### Generate upload URL

```
POST /gcp/storage/<service-account-id>/<bucket-id>

filename: documents/report.txt
```

Response:

```
status: 200
url: <signed-url>
```

### Generate download URL

```
GET /gcp/storage/<service-account-id>/<bucket-id>/<file-path>
```

Example:

```
GET /gcp/storage/my-account/my-bucket/documents/report.txt
```

Response:

```
status: 200
url: <signed-url>
```

### Configuration options

The service account endpoints also support the `expiration` query parameter to control URL lifetime.

```
POST /gcp/storage/my-account/my-bucket?expiration=1800

filename: data.txt
```

The URL expires in 30 minutes (1800 seconds).

## References

- [Google Cloud Storage documentation](https://cloud.google.com/storage/docs)
- [Best practices for using Workload Identity](https://cloud.google.com/iam/docs/best-practices-for-using-workload-identity)
