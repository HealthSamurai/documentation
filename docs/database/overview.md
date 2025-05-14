# Overview

Aidbox requires a PostgreSQL database for operation. The minimum supported PostgreSQL version is 12, which is required for JSON path support functionality (for older versions, you should use `jsonknife` extension). Aidbox actively maintains compatibility with the three most recent PostgreSQL versions (currently 17, 16, and 15).

PostgreSQL can be deployed in any environment: cloud-managed services (such as [AWS RDS](../deployment-and-maintenance/deploy-aidbox/run-aidbox-on-managed-postgresql.md#aurora-postgresql), Google Cloud SQL, [Azure Database](../deployment-and-maintenance/deploy-aidbox/run-aidbox-on-managed-postgresql.md#azure-database-for-postgresql-flexible-server)), self-hosted installations, or on-premises deployments. Aidbox is compatible with any of these deployment options.

During initialization, Aidbox automatically creates all necessary database objects, including tables, indexes, and other required structures. Schema migrations are also handled automatically by Aidbox during version upgrades.

Database backup and restore operations are not managed by Aidbox. These operations should be implemented using standard database management approaches:

* Native PostgreSQL backup tools
* Cloud provider backup solutions
* [Kubernetes operators](aidboxdb-image/ha-aidboxdb.md)
* Custom backup scripts

Health Samurai provides [AidboxDB](aidboxdb-image/) - a PostgreSQL distribution based on the official PostgreSQL release. AidboxDB includes additional database extensions for specific Aidbox features, and wal-g backup tool. This distribution is optional and standard PostgreSQL installations are supported.

For technical details about database schema and query capabilities, refer to the [Database Schema](database-schema.md) documentation.
