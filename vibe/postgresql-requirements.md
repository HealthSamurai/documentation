<!-- Merge into: database.md -->

# Which PostgreSQL can be used with Aidbox?

Aidbox requires [PostgreSQL](https://www.postgresql.org/) version 12 
or higher to leverage advanced JSONB features including JSON path support. 
Aidbox actively supports the three most recent PostgreSQL versions (currently 17, 16, and 15)
and is compatible with all deployment options including cloud-managed services like AWS RDS, Google Cloud SQL, and Azure Database, as well as self-hosted and on-premises installations. Aidbox automatically handles database initialization, schema migrations, and provides optional [AidboxDB](aidboxdb-image/README.md) - a PostgreSQL distribution with Aidbox-specific extensions.

See also: [Requirements](./requirements.md)
