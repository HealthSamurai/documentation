# Crunchy Operator (/pgbackrest)

PGO Crunchy Operator uses pgBackRest for managing backups.&#x20;

[pgBackRest](https://pgbackrest.org/) - is a backup and restore solution for PostgreSQL databases that offers several features, such as parallel backup and restore, compression, full, differential, and incremental backups, backup rotation and archive expiration, backup integrity and etc. It supports multiple repositories, which can be located locally or remotely via TLS/SSH, or be cloud provided storage as  S3/GCS/Azure.

### Backup configuration

Backup configuration is done through the [`spec.backups.pgbackrest`](https://access.crunchydata.com/documentation/postgres-operator/5.3.1/references/crd/#postgresclusterspecbackupspgbackrest) parameter. See the example below.

{% code title="aidboxdb.yaml" %}
```yaml
spec:
  backups:
    pgbackrest:
      repos:
        - name: repo1                   # repo
          schedules:
            full: "0 1 * * 0"           # Full backup once a week on Sunday at 1 AM
            incremental: "0 1 * * 1-6"  # incremental - from Monday to Saturday at 1 AM   
          gcs:
            bucket: "<BUCKET_NAME>"     # GCS bucket name 
      configuration:
        - secret:
            name: pgo-gcs-creds         # GCS credentials
        - configMap:
            name: pgbackrest-config     # pgbackrest config
      global:
        repo1-path: /backup/aidboxdb    # Backup path in bucket
        repo1-retention-full-type: time # Retention policy  
        repo1-retention-full: "30"      # Delete backups after 30 days
      manual:
        repoName: repo1
        options:                        # Manual backup configuration
          - '--type=full'
          - '--compress-level=6'
          - '--start-fast=y'
          - '--process-max=20'
          - '--log-level-console=info'
```
{% endcode %}

And create additional configs and secrets

```yaml
---
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
---
apiVersion: v1
kind: ConfigMap
metadata:
  name: pgbackrest-config
  namespace: aidboxdb-db
data:
  db.conf: |-
    [global]
    compress-level=6
    start-fast=y
    process-max=20
```

#### Repositories

`repos:` - Defines a pgBackRest repository. This allows you to configure where and how your backups and WAL archive are stored. You can keep backups in up to four (4) different locations.

Supported 4 locations (see full [Backup Configuration](https://access.crunchydata.com/documentation/postgres-operator/5.3.1/tutorial/backups/) instructions):

* `azure` - For use with Azure Blob Storage.&#x20;
* `gcs` - For use with Google Cloud Storage (GCS).&#x20;
* `s3` - For use with Amazon S3 or any S3-compatible storage system such as MinIO.&#x20;
* `volume` For use with a Kubernetes Persistent Volume.

GCS configuration example:

1. Specify GCS bucket and secret with credentials

```yaml
spec:
  backups:
    pgbackrest:
      repos:
        - name: repo1        
          gcs:
            bucket: "<BUCKET_NAME>" 
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

In this spec, we define incremental backup from Monday to Saturday and take one full backup every Sunday at 1 AM:

```yaml
spec:
  backups:
    pgbackrest:
      repos:
        - name: repo1
          schedules:
            full: "0 1 * * 0"           # Full backup once a week on Sunday at 1AM
            incremental: "0 1 * * 1-6"  # incremental - from Monday to Saturday at 1AM
```

#### Backup retention

Define backup retention policy. In this spec we store all backups for 30 days, after that period - delete them:

```yaml
spec:
  backups:
    pgbackrest:
      global:
        repo1-path: /backup/aidboxdb    # Backup path in bucket
        repo1-retention-full-type: time # Retention policy  
        repo1-retention-full: "30"      # Delete backups after 30 days
```

### Create backup

At certain instances, you may find it necessary to perform a singular backup, especially before making significant modifications or updates to an application. To do so, you must first configure the `spec.backups.pgbackrest.manual` section, which includes details about the type of backup desired and any additional pgBackRest configuration settings required:

```yaml
spec:
  backups:
    pgbackrest:
      manual:
        repoName: repo1
        options:                  # Manual backup configuration
          - '--type=full'         # Take full backup
          - '--compress-level=6'  # Compress GZ
          - '--start-fast=y'      # Do no wait checkpoint
          - '--process-max=20'    # Max processes to use for compressing and transfer
```

For creating a manual backup you should annotate `postgrescluster` resource with `postgres-operator.crunchydata.com/pgbackrest-backup` annotation:

```bash
$ kubectl annotate -n aidboxdb-db postgrescluster aidboxdb --overwrite \
          postgres-operator.crunchydata.com/pgbackrest-backup="$(date)"
```

### Recovery

Sometimes you need to recover your database or clone your production database to the stage environment. Generally in the recovery process, we can define two types of recovery: clone the existing cluster to another environment, PITR - recovery database at a specific point in time.

#### Clone

To create a new clone of the existing PG cluster you should specify `dataSource` parameter for the new cluster. In the sample below we create `stage` cluster as a copy of `aidboxdb` cluster in `aidboxdb-db` namespace.

```yaml
apiVersion: postgres-operator.crunchydata.com/v1beta1
kind: PostgresCluster
metadata:
  name: stage
  namespace: stage
spec:
  dataSource:
    postgresCluster:
      clusterName: aidboxdb
      repoName: repo1
      clusterNamespace: aidboxdb-db
  image: healthsamurai/aidboxdb:15.2.0-crunchy
  postgresVersion: 15
  instances:
    - dataVolumeClaimSpec:
        accessModes:
        - "ReadWriteOnce"
        resources:
          requests:
            storage: 1Gi
  backups:
    pgbackrest:
      repos:
      - name: repo1
        volume:
          volumeClaimSpec:
            accessModes:
            - "ReadWriteOnce"
            resources:
              requests:
                storage: 1Gi
```

#### PITR

When you need recovery to a specific point in time you should add recovery options to the new cluster configuration.

```yaml
apiVersion: postgres-operator.crunchydata.com/v1beta1
kind: PostgresCluster
metadata:
  name: stage-pitr
  namespace: stage-pitr
spec:
  dataSource:
    postgresCluster:
      clusterName: aidboxdb
      repoName: repo1
      clusterNamespace: aidboxdb-db
      options:
      - --type=time
      - --target="2023-04-09 10:00:00-04"
  image: healthsamurai/aidboxdb:15.2.0-crunchy
  postgresVersion: 15
  instances:
    - dataVolumeClaimSpec:
        accessModes:
        - "ReadWriteOnce"
        resources:
          requests:
            storage: 1Gi
  backups:
    pgbackrest:
      repos:
      - name: repo1
        volume:
          volumeClaimSpec:
            accessModes:
            - "ReadWriteOnce"
            resources:
              requests:
                storage: 1Gi
```

Look at `dataSource` . In this section, you can specify the type of recovery and target.

```yaml
spec:
  dataSource:
    postgresCluster:
      clusterName: aidboxdb
      clusterNamespace: aidboxdb-db
      repoName: repo1
      options:
      - --type=time
      - --target="2023-04-09 10:00:00-04"
```

### Inspect backup

You can list of backups via direct exec `pgbackrest info` command on database image

```bash
$ export NS=aidboxdb-db
$ kubectl exec  -n $NS \
  $(kubectl get pod -n $NS -l "postgres-operator.crunchydata.com/data=postgres" -o jsonpath='{.items[0].metadata.name}') \
  -- bash -c 'pgbackrest info'
```

For verifying existing backups you can run `pgbackrest verify` command&#x20;

```bash
$ export NS=aidboxdb-db
$ kubectl exec  -n $NS \
  $(kubectl get pod -n $NS -l "postgres-operator.crunchydata.com/data=postgres" -o jsonpath='{.items[0].metadata.name}') \
  -- bash -c 'pgbackrest --stanza=db --log-level-console=info verify'
```

