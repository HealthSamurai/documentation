# Crunchy Operator (pgBackRest)

PGO Crunchy Operator uses pgBackRest for managing backups. [pgBackRest](https://pgbackrest.org/) - is a backup and restore solution for PostgreSQL databases that offers several features, such as parallel backup and restore, compression, full, differential and incremental backups, backup rotation and archive expiration, backup integrity and etc. It supports multiple repositories, which can be located locally or remotely via TLS/SSH.&#x20;

Backup configuration is done through the [`spec.backups.pgbackrest`](https://access.crunchydata.com/documentation/postgres-operator/5.3.1/references/crd/#postgresclusterspecbackupspgbackrest) parametero

```yaml
spec:
  backups:
    pgbackrest:
      configuration:
        - secret:
          name: pgo-gcs-creds
      image: registry.developers.crunchydata.com/crunchydata/crunchy-pgbackrest:ubi8-2.41-4
      global:
        repo1-retention-full: "30"
        repo1-retention-full-type: time      
      manual:
        options:
          - '--type=full'
        repoName: repo1
      repos:
        - name: repo1
          schedules:
            full: "0 1 * * 0"
            incremental: "0 1 * * 1-6"        
          gcs:
            bucket: "<YOUR_GCS_BUCKET_NAME>"
```

Configure connection credentials

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: pgo-gcs-creds
  namespace: aidboxdb-db
data:
  gcs.conf: |-
    [global]
    repo1-gcs-key=/etc/pgbackrest/conf.d/gcs-key.json
  gcs-key.json: <GCP SA JSON access file>
```

`repos:` - define backup storage and schedules

*   In this spec, we define incremental backup from Monday to Saturday and take one full backup every Sunday at 1 AM

    ```yaml
    schedules:
      full: "0 1 * * 0"
      incremental: "0 1 * * 1-6"  
    ```

Google Cloud Storage configuration sample:

1. Edit [cluster configuration](https://access.crunchydata.com/documentation/postgres-operator/5.3.1/tutorial/backups/)

```
spec:
  configuration:
  - secret:
      name: pgo-gcs-creds
  backup:
    pgbackrest:
      repos:
        gcs:
          bucket: "<YOUR_GCS_BUCKET_NAME>"

```

2. Configure access

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: pgo-gcs-creds
  namespace: aidboxdb-db
data:
  gcs.conf: |-
    [global]
    repo1-gcs-key=/etc/pgbackrest/conf.d/gcs-key.json
  gcs-key.json: <GCP SA JSON access file>
```



`global:`  - define backup retention policy

*   Define backup retention policy. In this spec we store all backups 30 days, after that period - delete them

    ```yaml
      repo1-retention-full: "30"
      repo1-retention-full-type: time  
    ```

Take backup manually

For creating a manual full backup you should annotate `postgrescluster` resource

```bash
$ kubectl annotate -n aidboxdb-db postgrescluster aidboxdb --overwrite \
        postgres-operator.crunchydata.com/pgbackrest-backup="$(date)"
```
