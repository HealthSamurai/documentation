# Run Aidbox on managed postgresql

This quickstart guide explains how to run Aidbox on managed PostgreSQL
instance.


### Aurora PostgreSQL
#### Prerequisites

- aws CLI
- psql

#### Connect to db cluster

Follow [AWS
documentation](https://docs.aws.amazon.com/AmazonRDS/latest/AuroraUserGuide/UsingWithRDS.IAMDBAuth.Connecting.AWSCLI.PostgreSQL.html)
to connect to cluster using aws-cli and psql

#### Create role

Execute following sql in psql

```sql
CREATE USER aidbox WITH CREATEDB ENCRYPTED PASSWORD 'aidboxpass';
```

### Azure Database for PostgreSQL flexible server
#### Prerequisites

- azure CLI

#### Create Role

Follow [Azure
Documentation](https://learn.microsoft.com/en-us/azure/postgresql/flexible-server/connect-azure-cli)
and execute following SQL to create role:

```sql
CREATE USER aidbox WITH CREATEDB ENCRYPTED PASSWORD 'aidboxpass';
```

### Setup Aidbox to use new user

{% hint style="warning" %}
You may encounter `permission denied` error when creating
extensions. Just connect to PostgreSQL database using user that can
create extension (usually admin user created with a server) and create
failed extension manually.
{% endhint %}

Setup following environment variables. If you're using existing
`PGDATABASE` make sure `aidbox` role has `CREATE` privilege on
it. Otherwise Aidbox won't be able to install most of the extensions.

```shell
PGUSER=aidbox
PGPASSWORD=aidboxpass
PGDATABASE=aidbox
```
