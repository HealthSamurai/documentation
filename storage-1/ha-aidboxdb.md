---
description: HA AidboxDB installation with Crunchy
---

# HA AidboxDB

High availability for PostgreSQL is complex because it requires multiple components to work seamlessly, can be time-consuming to set up and configure manually, and ongoing maintenance can be challenging.&#x20;

Using ready solutions like the Crunchy operator for Kubernetes simplifies the process and improves reliability. Crunchy and similar operators provide a tested and production-ready infrastructure that integrates well with PostgreSQL, as well as features like automatic failover, backups, restores, and upgrades, which can be complex to implement manually. Overall, using a ready solution like Crunchy can reduce complexity and free up time and resources to focus on other aspects of your application.

### Crunchy Operator

The [Crunchy Operator](https://access.crunchydata.com/documentation/postgres-operator/5.3.1/quickstart/) is an open-source Kubernetes operator that automates the management of PostgreSQL clusters. It provides a simple way to deploy, manage, and operate PostgreSQL clusters in a Kubernetes environment, making it easier to run and scale PostgreSQL workloads.

One of the key benefits of using the Crunchy operator is that it allows for high availability and fault tolerance for your PostgreSQL database. When running a PostgreSQL cluster with the Crunchy operator, you can specify the number of replicas to create, which helps ensure that your database is always available in case of a failure.

Here's how high availability works in terms of the Crunchy operator:

* The Crunchy operator deploys a primary PostgreSQL instance and one or more replicas.
* The primary instance is responsible for accepting read and write requests and replicating changes to the replicas.
* If the primary instance fails, one of the replicas is promoted to become the new primary instance.
* The Crunchy operator automatically reconfigures the remaining replicas to replicate from the new primary instance.
* This ensures that the cluster remains available even if one or more instances fail.

In addition to high availability, the Crunchy operator also provides other features such as backups and restores, scaling, rolling upgrades, and custom configurations using PostgreSQL custom resource definitions (CRDs).

### Install Crunchy

We recommend following official Crunchy [Quickstart](https://access.crunchydata.com/documentation/postgres-operator/v5/quickstart/) for how to install and get up and running with PGO. Here are some instructions to get Postgres up and running on Kubernetes:

1. [Fork the Postgres Operator examples repository](https://github.com/CrunchyData/postgres-operator-examples/fork) and clone it to your host machine.

```bash
YOUR_GITHUB_UN="<your GitHub username>"
git clone --depth 1 "git@github.com:${YOUR_GITHUB_UN}/postgres-operator-examples.git"
cd postgres-operator-examples
```

2. Install PGO using `kustomize`

```bash
kubectl apply -k kustomize/install/namespace
kubectl apply --server-side -k kustomize/install/default
```

3. Werify PGO

```bash
$ kubectl get pods -n postgres-operator
NAME                           READY   STATUS    RESTARTS   AGE
pgo-7b5d478777-7g6kc           1/1     Running   0          51m
pgo-upgrade-5b576ccfb5-m5qdc   1/1     Running   0          51m
```

### Create cluster

For creating new PostgreSQL cluster using PGO you should create CRD `PostgresCluster.` More detailed information about creating a PGO cluster you can found in [official documentation](https://access.crunchydata.com/documentation/postgres-operator/5.3.1/tutorial/create-cluster/).

1. Create `aidboxdb.yml` file with the following content

<pre class="language-yaml" data-title="aidboxdb.yml"><code class="lang-yaml"><strong>apiVersion: postgres-operator.crunchydata.com/v1beta1
</strong>kind: PostgresCluster
metadata:
  name: aidboxdb
  namespace: aidboxdb-db
spec:
  image: healthsamurai/aidboxdb:15.2.0-crunchy
  postgresVersion: 15
  port: 5432
  instances:
    - name: aidboxdb
      replicas: 2
      dataVolumeClaimSpec:
        accessModes:
          - ReadWriteOnce
        resources:
          requests:
            storage: 10Gi
  backups:
    pgbackrest:
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
          volume:
            volumeClaimSpec:
              accessModes:
                - ReadWriteOnce
              resources:
                requests:
                  storage: 10Gi
  users:
    - databases:
        - aidbox
      name: aidbox
      options: "SUPERUSER"
  patroni:
    dynamicConfiguration:
      postgresql:
        pg_hba:
          - host all all 0.0.0.0/0 md5
        parameters:
          listen_addresses : '*'
          shared_preload_libraries : 'pg_stat_statements'
          shared_buffers : '1GB'

</code></pre>

Important notes

* `image: healthsamurai/aidboxdb:15.2.0-crunchy`  - we recommend use our aidboxdb image build that is preconfigured for use in PGO
* `replicas: 2`  - in this configuration, we install 1 master and 1 replica
* `backup options` - in this sample, we use local PVC for storing backups. For configuring cloud storages like S3 or GCS you can [follow this instructions](https://access.crunchydata.com/documentation/postgres-operator/5.3.1/tutorial/backups/)
* `pg_hba: ["host all all 0.0.0.0/0 md5"]` - for this tutorial we allow non SSL connection

2. Create namespace and apply aidboxdb.yml resource

```bash
$ kubectl create ns aidboxdb-db
namespace/aidboxdb-db created                                                                                                      ⎈ kind-kind 11:21:57
$ kubectl apply -f aidboxdb.yaml
postgrescluster.postgres-operator.crunchydata.com/aidboxdb created
```

3. Verify PostgreSQL cluster

```bash
$ kubectl get pods -n aidboxdb-db
NAME                         READY   STATUS      RESTARTS   AGE
aidboxdb-aidboxdb-p2tm-0     4/4     Running     0          12m
aidboxdb-aidboxdb-tc58-0     4/4     Running     0          12m
aidboxdb-backup-qvk7-q7qmv   0/1     Completed   0          11m
aidboxdb-repo-host-0         2/2     Running     0          12m
```

### Connect to the cluster&#x20;

1. Get connection credentials. Crunchy operator store all connection information in related `Secret` resource. In our case it `aidboxdb-pguser-aidbox`. More detailed information you can be found in the[ connection tutorial](https://access.crunchydata.com/documentation/postgres-operator/5.3.1/tutorial/connect-cluster/).

```bash
$ kubectl describe secret aidboxdb-pguser-aidbox -n aidboxdb-db
Name:         aidboxdb-pguser-aidbox
Namespace:    aidboxdb-db
Annotations:  <none>

Type:  Opaque

Data
====
port:      4 bytes     # Database port 5432
host:      32 bytes    # Local K8S host name
user:      6 bytes     # User name
password:  24 bytes    # Password
dbname:    6 bytes     # database name
verifier:  133 bytes
jdbc-uri:  120 bytes
uri:       101 bytes
```

2. Now you can set up this parameter for the Aidbox database connection. Look at [Install Aidbox in Kubernetes](../getting-started/run-aidbox-in-kubernetes/) tutorial

{% code title="Aidbox ConfigMap" %}
```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: aidbox
  namespace: prod
data:
  ...
  PGDATABASE: aidbox
  PGHOST: < host value from aidboxdb-pguser-aidbox secret>
  PGPORT: '5432' 
```
{% endcode %}

{% code title="Aidbox Secret" %}
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: aidbox
  namespace: prod
data:
  ...
  PGPASSWORD: < password value from aidboxdb-pguser-aidbox secret>
  PGUSER:     < user value from aidboxdb-pguser-aidbox secret>
```
{% endcode %}

### Backup a cluster

You can specify a [schedule backup and retention policy](https://access.crunchydata.com/documentation/postgres-operator/5.3.1/tutorial/backup-management/) for cluster&#x20;

```yaml
spec:
  backups:
    pgbackrest:
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
          volume:
            volumeClaimSpec:
              accessModes:
                - ReadWriteOnce
              resources:
                requests:
                  storage: 10Gi
```

*   Define backup schedule. In this spec we define incremental backup from Monday to Saturday and take one full backup every Sunday at 1 AM

    ```yaml
    schedules:
      full: "0 1 * * 0"
      incremental: "0 1 * * 1-6"  
    ```
*   Define backup retention policy. In this spec we store all backups 30 days, after that period - delete them

    ```yaml
    global:
      repo1-retention-full: "30"
      repo1-retention-full-type: time  
    ```

### Create manual backup

For creating a manual full backup you should annotate `postgrescluster` resource

```bash
$ kubectl annotate -n aidboxdb-db postgrescluster aidboxdb --overwrite \
        postgres-operator.crunchydata.com/pgbackrest-backup="$(date)"
```

### Clone a Postgres Cluster <a href="#clone-a-postgres-cluster" id="clone-a-postgres-cluster"></a>

You can create a single copy of the existing cluster by creating a new one and specify `dataSource` parameter. In the next example, we  create a `stage` cluster that is a copy of `aidboxdb` cluster.

```yaml
apiVersion: postgres-operator.crunchydata.com/v1beta1
kind: PostgresCluster
metadata:
  name: stage
  namespace: aidboxdb-stage
spec:
  dataSource:
    postgresCluster:
      clusterName: aidboxdb
      clusterNamespace: aidboxdb-db
      repoName: repo1
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
      image: registry.developers.crunchydata.com/crunchydata/crunchy-pgbackrest:ubi8-2.41-4
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

Take care of `dataSource` parameter. In this section we specify the source that will be used for cloning.

```yaml
  dataSource:
    postgresCluster:
      clusterName: aidboxdb
      clusterNamespace: aidboxdb-db
      repoName: repo1
```

### Restore PITR

When you needed to restore a specific time version of the cluster, or you want to periodically restore you can specify `restore` option on backup config.

```yaml
apiVersion: postgres-operator.crunchydata.com/v1beta1
kind: PostgresCluster
metadata:
  name: stage-recovery
  namespace: aidboxdb-stage
spec:
  image: healthsamurai/aidboxdb:15.2.0-crunchy
  postgresVersion: 15
  dataSource:
    postgresCluster:
      clusterName: aidboxdb
      clusterNamespace: aidboxdb-db
      repoName: repo1
  instances:
    - dataVolumeClaimSpec:
        accessModes:
        - "ReadWriteOnce"
        resources:
          requests:
            storage: 1Gi
  backups:
    pgbackrest:
      image: registry.developers.crunchydata.com/crunchydata/crunchy-pgbackrest:ubi8-2.41-4
      restore:
        enabled: true
        repoName: repo1
        options:
        - --type=time
        - --target="2023-04-25 16:20:00-02"
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

Now you need trigger the recovery process

```bash
$ kubectl annotate -n aidboxdb-db  postgrescluster aidboxdb --overwrite \
        postgres-operator.crunchydata.com/pgbackrest-restore="$(date)"
```

