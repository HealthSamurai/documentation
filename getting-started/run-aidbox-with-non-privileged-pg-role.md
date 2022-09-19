# Run Aidbox with non-privileged postgresql role

This quickstart guide explains how to run Aidbox using non-privileged
postgresql role.

### Prerequisites

Follow [Run Aidbox locally with
Docker](run-aidbox-locally-with-docker/README.md) until [Run Aidbox
locally with
Docker](run-aidbox-locally-with-docker/README.md#launch-aidbox)

### Run aidboxdb image

```shell
docker compose up aidbox-db -d
```

### Execute psql inside container

```shell
docker compose exec aidbox-db psql postgres
```

### Create role

```sql
CREATE USER aidbox WITH CREATEDB ENCRYPTED PASSWORD 'aidboxpass';
```

### Setup Aidbox to use new user

Modify `.env` file by replacing following vales. If you're using
existing database make sure `aidbox` role has `CREATE` privilege on
it. Otherwise Aidbox won't be able to install most of the extensions.

```shell
PGUSER=aidbox
PGPASSWORD=aidboxpass
PGDATABASE=aidbox
```

### Create extensions as superuser

Aidbox can install most of the required extensions (by having `CREATE`
privilege on database), but some require superuser. You can install
them manually by executing folowing commands in psql:

```sql
CREATE EXTENSION pg_stat_statements;
CREATE EXTENSION jsquery;
CREATE EXTENSION jsonknife;
```

### Next steps

Continue with [Run Aidbox locally with
Docker](run-aidbox-locally-with-docker/README.md)
