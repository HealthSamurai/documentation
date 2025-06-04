# Overview

Aidbox uses PostgreSQL as its database engine, leveraging its robust JSON capabilities, reliability, and performance for healthcare data storage.

## Version compatibility

* Minimum: PostgreSQL 12 (required for JSON path support). For older versions, use PostgreSQL with `jsonknife` extension
* Actively supported: Three most recent versions (currently 17, 16, 15)

## Deployment options

Compatible with all PostgreSQL deployments

* cloud-managed services (such as [AWS RDS](../deployment-and-maintenance/deploy-aidbox/run-aidbox-on-managed-postgresql.md#aurora-postgresql), [Google Cloud SQL](https://cloud.google.com/sql/postgresq), [Azure Database](../deployment-and-maintenance/deploy-aidbox/run-aidbox-on-managed-postgresql.md#azure-database-for-postgresql-flexible-server), etc)
* self-hosted installations&#x20;
* on-premises deployments

## Database Management

* **Initialization**: Aidbox automatically creates all database objects, including tables, starting indexes, and other required structures
* **Schema migrations**: Automatically handled during Aidbox version upgrades
* **Monitoring**:&#x20;
  * Aidbox provides [OpenTelemetry metrics for PostgreSQL monitoring](../modules/observability/metrics/technical-reference/opentelemetry-metrics.md#postgres)
* **Backup/Restore**: Use standard PostgreSQL methods:
  * Native PostgreSQL backup tools
  * Cloud provider solutions
  * [Kubernetes operators](aidboxdb-image/ha-aidboxdb.md)
  * Custom scripts

## AidboxDB Distribution

* [AidboxDB](aidboxdb-image/) is a PostgreSQL distribution based on the official PostgreSQL release&#x20;
* Includes Aidbox-specific extensions and the WAL-G backup tool
* Standard PostgreSQL installations remain fully supported

## What's next

1. Check [Database Schema](database-schema.md) for technical details on schema and query capabilities.
2. Required and optional [PostgreSQL Extensions](postgresql-extensions.md)
3. [AidboxDB](aidboxdb-image/)
