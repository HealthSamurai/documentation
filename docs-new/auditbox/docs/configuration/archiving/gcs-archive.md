# How To Setup Archiving To GCS

This guide explains how to configure Auditbox with Google Cloud Storage (GCS) archiving functionality. This enables automatic backup of audit events to GCS for long-term retention and compliance.

## Prerequisites

- Elasticsearch 8.17 cluster
- Google Cloud Storage bucket
- Google Cloud service account with appropriate permissions
- Google Cloud credentials (service account key)

## Step 1: Set up Google Cloud Service Account

First, create a service account in Google Cloud Console and grant it the necessary permissions for GCS access.

### Create Service Account

1. Go to the [Google Cloud Console](https://console.cloud.google.com/)
2. Navigate to "IAM & Admin" > "Service Accounts"
3. Click "Create Service Account"
4. Give it a name (e.g., "auditbox-archive-sa")
5. Grant the following roles:
   - `roles/storage.objectAdmin` for the target bucket
   - `roles/storage.legacyBucketReader` for the target bucket

### Generate Service Account Key

1. Select the service account you just created
2. Go to the "Keys" tab
3. Click "Add Key" > "Create new key"
4. Choose JSON format and download the key file

## Step 2: Configure Elasticsearch GCS Credentials

You need to add GCS credentials to the Elasticsearch keystore to enable snapshot operations.

### Add GCS Credentials to Keystore


```bash
# Create temp file with service account
cat > /tmp/gcs-credentials.json << 'EOF'
{
  "type": "service_account",
  ...
}
EOF
```

```bash
# Add Google Cloud service account key file path
bin/elasticsearch-keystore add-file gcs.client.default.credentials_file /tmp/gcs-credentials.json
```

```bash
# remove temp file
rm /tmp/gcs-credentials.json
```

### Reload Secure Settings

After adding credentials to the keystore, you need to reload the secure settings in Elasticsearch for the changes to take effect:

```bash
curl -X POST "localhost:9200/_nodes/reload_secure_settings" -H 'Content-Type: application/json'
```

## Step 3: Configure Auditbox Environment Variables

Enable GCS archiving in Auditbox by setting the following environment variables:

```yaml
environment:
  # Enable GCS archiving
  AUDITBOX_ARCHIVE_S3_ENABLED: true

  # Data retention period (days before archiving)
  AUDITBOX_DATA_RETENTION_DAYS: 90

  AUDITBOX_S3_TYPE: gcs
  AUDITBOX_S3_BUCKET_NAME: "auditbox-bucket"

  # Elasticsearch snapshot repository name
  AUDITBOX_SNAPSHOT_REPOSITORY_NAME: gcs-repo
```

## Step 4: Start Auditbox

After configuring both Elasticsearch and Auditbox environment variables, start your Auditbox instance. The archiving will be automatically enabled and audit events older than the retention period will be archived to GCS.

## Troubleshooting

### Common Issues

1. **Repository creation fails**
   - Verify GCS credentials file is correctly mounted and accessible
   - Check service account has proper permissions on the GCS bucket
   - Ensure the credentials JSON is valid

2. **Authentication errors**
   - Verify the service account key file path is correct
   - Check that the file is readable by Elasticsearch process
   - Confirm the service account hasn't been deleted or disabled

3. **Connection timeout**
   - Verify GCS bucket exists and is accessible
   - Check network connectivity and firewall rules
   - Ensure Google Cloud APIs are enabled for your project

4. **Permission denied**
   - Verify the service account has `storage.objectAdmin` role
   - Check bucket permissions and IAM policies
   - Ensure the bucket is not set to uniform bucket-level access if using ACLs