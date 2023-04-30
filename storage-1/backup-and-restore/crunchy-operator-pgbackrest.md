# Crunchy Operator (pgBackRest)

PGO Crunchy Operator uses pgBackRest for managing backups. [pgBackRest](https://pgbackrest.org/) - is a backup and restore solution for PostgreSQL databases that offers several features, such as parallel backup and restore, compression, full, differential and incremental backups, backup rotation and archive expiration, backup integrity and etc. It supports multiple repositories, which can be located locally or remotely via TLS/SSH.&#x20;

### Backup configuration

Backup configuration is done through the [`spec.backups.pgbackrest`](https://access.crunchydata.com/documentation/postgres-operator/5.3.1/references/crd/#postgresclusterspecbackupspgbackrest) parameter. See example below.

```yaml
spec:
  backups:
    pgbackrest:
      repos:
        - name: repo1
          schedules:
            full: "0 1 * * 0"
            incremental: "0 1 * * 1-6"        
          gcs:
            bucket: "<YOUR_GCS_BUCKET_NAME>"      
      configuration:
        - secret:
            name: pgo-gcs-creds
      global:
        repo1-retention-full: "30"
        repo1-retention-full-type: time      
      manual:
        options:
          - '--type=full'
        repoName: repo1

```

#### Destination

`repos:` - Defines a pgBackRest repository. This allows you to configure where and how your backups and WAL archive are stored. You can keep backups in up to four (4) different locations.

Supported 4 locations (see full [Backup Configuration](https://access.crunchydata.com/documentation/postgres-operator/5.3.1/tutorial/backups/) instructions):

* `azure` - For use with Azure Blob Storage.&#x20;
* `gcs` - For use with Google Cloud Storage (GCS).&#x20;
* `s3` - For use with Amazon S3 or any S3 compatible storage system such as MinIO.&#x20;
* `volume` For use with a Kubernetes Persistent Volume.

GCS configuration example:

1. Specify GCS bucket and secret with credentials

```
spec:
  backups:
    pgbackrest:
      repos:
        - name: repo1        
          gcs:
            bucket: "<YOUR_GCS_BUCKET_NAME>" 
      configuration:
        - secret:
            name: pgo-gcs-creds
```

2. Create `secret` with GCS connection credentials

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: pgo-gcs-creds
  namespace: aidboxdb-db
dataString:
  gcs.conf: |-
    [global]
    repo1-gcs-key=/etc/pgbackrest/conf.d/gcs-key.json
  gcs-key.json: |-
    <GCP SA JSON access file>
```

#### Schedule

*   In this spec, we define incremental backup from Monday to Saturday and take one full backup every Sunday at 1 AM

    ```yaml
    schedules:
      full: "0 1 * * 0"
      incremental: "0 1 * * 1-6"  
    ```

#### Backup retention

*   Define backup retention policy. In this spec we store all backups 30 days, after that period - delete them

    ```yaml
      repo1-retention-full: "30"
      repo1-retention-full-type: time  
    ```

#### Create backup

For creating a manual full backup you should annotate `postgrescluster` resource

```bash
$ kubectl annotate -n aidboxdb-db postgrescluster aidboxdb --overwrite \
        postgres-operator.crunchydata.com/pgbackrest-backup="$(date)"
```

### Inspect Backups

### Recovery

#### Clone

#### PITR

