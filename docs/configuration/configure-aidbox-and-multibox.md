# Configure Aidbox and Multibox

[Aidbox](https://hub.docker.com/r/healthsamurai/aidboxone) is a full-featured single instance of the Aidbox FHIR server. If you are interested in multi-tenant Aidbox, consider using [Multibox](https://hub.docker.com/r/healthsamurai/multibox) distribution.

All distributions can be used both on [healthsamurai/aidboxdb](https://hub.docker.com/r/healthsamurai/aidboxdb) PostgreSQL distribution or managed PostgreSQLs.\
\
Basic Aidbox installation consists of two components: the backend and the database. Both are released as docker images and can be pulled from HealthSamurai [docker hub](https://hub.docker.com/u/healthsamurai). For each type of Aidbox license an individual backend image is available — either [Aidbox](https://hub.docker.com/r/healthsamurai/aidboxone) or [Multibox](https://hub.docker.com/r/healthsamurai/multibox).

[The database AidboxDB image](https://hub.docker.com/r/healthsamurai/aidboxdb) is the custom build of the open source PostgreSQL database. It contains a number of extensions that are primarily used for search performance optimization. AidboxDB officially supports the latest minor releases of all major PostgreSQL versions starting from 11. Note that the database image is the same for all Aidbox backend license types.

## Recommended environment variables

```yaml
# Environment variables for Postgres
POSTGRES_PORT: '5432'
POSTGRES_DB: aidbox
POSTGRES_USER: aidbox
POSTGRES_PASSWORD: <pgpassword>

# Environment variables for Aidbox
BOX_DB_HOST: aidbox_db
BOX_DB_PORT: '5432'
BOX_DB_USER: aidbox
BOX_DB_PASSWORD: <pgpassword>
BOX_DB_DATABASE: aidbox

BOX_WEB_PORT: 8080
BOX_ROOT_CLIENT_SECRET: <secret>
BOX_ADMIN_PASSWORD: <password>

AIDBOX_LICENSE: <license jwt>
BOX_BOOTSTRAP_FHIR_PACKAGES: "hl7.fhir.r4.core#4.0.1"
BOX_FHIR_TERMINOLOGY_SERVICE_BASE_URL: https://tx.health-samurai.io/fhir
BOX_FHIR_SCHEMA_VALIDATION: true
BOX_FHIR_CREATEDAT_URL: https://aidbox.app/ex/createdAt
BOX_FHIR_CORRECT_AIDBOX_FORMAT: true
BOX_SETTINGS_MODE: read-write
BOX_FHIR_COMPLIANT_MODE: true
BOX_FHIR_SEARCH_COMPARISONS: true
BOX_FHIR_JSON_SCHEMA_DATETIME_REGEX: '#{:fhir-datetime}'
BOX_FHIR_SEARCH_INCLUDE_CONFORMANT: true
BOX_FHIR_SEARCH_AUTHORIZE_INLINE_REQUESTS: true
BOX_WEB_BASE_URL: http://localhost:8080
BOX_SECURITY_AUDIT_LOG_ENABLED: true
```

## Configure performance

By default, Aidbox and Multibox runs with 8 web workers and 8 DB connection pool size. That means that Aidbox can process at the same time 8 concurrent connections.

A good practice is stayed pool size the same as CPU count of your database. For example, if your database has 16 CPU cores, you can set `BOX_DB_POOL_MAXIMUM__POOL__SIZE=16`. Box web workers count is dependent on your load profile. For example, if you have a lot of fast read queries, you can set `BOX_WEB_THREAD` equal x2 or x3 of your DB pool size (32 or 48). Or if you have a lot of batch insert queries, we recommend stay web workers count as the same DB pool size.

You can configure this parameter using following environment variables.

```bash
BOX_DB_POOL_MAXIMUM_POOL_SIZE=8
BOX_WEB_THREAD=8
```

Aidbox usually needs 2-4 Gb of memory:

```
JAVA_OPTS="-Xms4096m -Xmx4096m"
```

## References

Refer to the following pages for the specific image description and the list of available configuration options.

{% content-ref url="../database/aidboxdb-image/" %}
[aidboxdb-image](../database/aidboxdb-image/)
{% endcontent-ref %}

{% content-ref url="../reference/environment-variables/" %}
[environment-variables](../reference/environment-variables/)
{% endcontent-ref %}

You can also see the environment variables in the **Settings page** in **AidboxUI**.

If you are looking for the latest versions of the docker images or general release cycle explanation go to the Versioning page.

{% content-ref url="../overview/versioning.md" %}
[versioning.md](../overview/versioning.md)
{% endcontent-ref %}

See also deployment documentation:

{% content-ref url="../deployment-and-maintenance/deploy-aidbox/" %}
[deploy-aidbox](../deployment-and-maintenance/deploy-aidbox/)
{% endcontent-ref %}
