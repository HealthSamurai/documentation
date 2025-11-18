# How To Setup Archiving To Minio

This guide explains how to configure Auditbox with MinIO archiving functionality. This enables automatic backup of audit events to S3-compatible storage for long-term retention and compliance.

## Prerequisites

- Elasticsearch 8.17 cluster
- MinIO server with a bucket created
- MinIO access key ID and secret access key

## Step 1: Configure Elasticsearch S3 Credentials

First, you need to add S3 credentials to the Elasticsearch keystore to enable snapshot operations.

### Add S3 Credentials to Keystore

```bash
# Add AWS access key
echo "your-access-key-id" | bin/elasticsearch-keystore add s3.client.default.access_key --stdin

# Add AWS secret key
echo "your-secret-access-key" | bin/elasticsearch-keystore add s3.client.default.secret_key --stdin
```

### Reload Secure Settings

After adding credentials to the keystore, you need to reload the secure settings in Elasticsearch for the changes to take effect:

```bash
curl -X POST "localhost:9200/_nodes/reload_secure_settings" -H 'Content-Type: application/json'
```

## Step 2: Configure Auditbox Environment Variables

Enable MinIO archiving in Auditbox by setting the following environment variables:

```yaml
environment:
  # Enable archiving to MinIO
  AUDITBOX_ARCHIVE_S3_ENABLED: true

  # Data retention period (days before archiving)
  AUDITBOX_DATA_RETENTION_DAYS: 90

  # Elasticsearch snapshot repository name
  AUDITBOX_SNAPSHOT_REPOSITORY_NAME: auditbox_repo

  # S3 bucket name
  AUDITBOX_S3_BUCKET_NAME: backup_bucket

  # S3 endpoint (use https://s3.amazonaws.com for AWS S3)
  AUDITBOX_S3_ENDPOINT: http://minio:9000
```

## Step 3: Start Auditbox

After configuring both Elasticsearch and Auditbox environment variables, start your Auditbox instance. The archiving will be automatically enabled and audit events older than the retention period will be archived to S3.

## Troubleshooting

### Common Issues

1. **Repository creation fails**
   - Verify S3 credentials are correctly added to keystore
   - Check S3 bucket permissions and endpoint URL

2. **Connection timeout**
   - Verify S3 endpoint is accessible from Elasticsearch
   - Check network connectivity and firewall rules