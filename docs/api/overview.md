# Overview

## Data Management

Aidbox provides robust APIs for managing FHIR and custom resources, including creation, validation, history tracking, and bulk operations.

*   ### [CRUD](rest-api/crud/)

    Create, read, update, and delete FHIR and custom resources using standard RESTful endpoints.
*   ### [Validation](rest-api/other/validate.md)

    Validate FHIR resources against profiles and constraints before storing or processing them.
*   ### [History](rest-api/history.md)

    Access the full change history of resources, supporting audit and rollback scenarios.
*   ### [Bundle](rest-api/bundle.md)

    Process multiple operations in a single HTTP request using FHIR Bundles for transactional or batch processing.
*   ### Bulk Import

    Import large datasets efficiently into Aidbox using optimized bulk endpoints.

    *   ### [$load](bulk-api/load-and-fhir-load.md)

        Synchronous API for loading data into Aidbox in _ndjson_ _gz_ format from an external web service or bucket. Data is not validated during the import process.
    *   ### [$import](bulk-api/import-and-fhir-import.md)

        Asynchronous API for loading data into Aidbox in _ndjson_ _gz_ format from an external web service or bucket. Data is not validated during the import process.
*   ### Other APIs

    Additional endpoints for resource management and system operations.

    *   #### [Encryption AP](other/encryption-api.md)I

        Encrypt and decrypt sensitive data using Aidbox's built-in encryption services.
    *   #### [Sequence API](other/sequence-api.md)

        Generate unique, sequential values for resource identifiers or other use cases.
    *   #### [Batch Upsert](other/batch-upsert.md)

        Insert or update multiple resources in a single request, optimizing for bulk data operations.

## Data Querying

Aidbox supports powerful querying capabilities, from standard FHIR search to advanced SQL and GraphQL endpoints.

*   ### [FHIR Search](rest-api/fhir-search/)

    Query resources using the standard FHIR search API with support for filters, sorting, and pagination.
*   ### [Aidbox Search](rest-api/aidbox-search.md)

    Use Aidbox-specific search extensions for advanced filtering and querying.
*   ### [SQL-on-FHIR](../modules/sql-on-fhir/)

    Run SQL queries directly against FHIR data for advanced analytics and reporting.
*   ### [SQL APIs](rest-api/other/sql-endpoints.md)

    Access low-level SQL endpoints for custom queries and data extraction.
*   ### [GraphQL](graphql-api.md)

    Query and manipulate FHIR resources using the flexible GraphQL API.
*   ### [$everything](rest-api/everything-on-patient.md)

    Retrieve all resources related to a patient or other entity using the $everything operation.
*   ### Other APIs

    Additional endpoints for querying and retrieving data.

    *   ### [$document](rest-api/other/document.md)

        Generate and retrieve FHIR Documents for clinical summaries and other use cases.
    *   ### [$lastn](rest-api/other/observation-lastn.md)

        Fetch the most recent N observations or other resources for a patient.

## Data Feed And Export

Aidbox provides APIs for real-time data feeds, change tracking, and bulk export of data.

*   ### [Subscriptions](../modules/topic-based-subscriptions/)

    Receive real-time notifications about resource changes using FHIR Subscriptions.
*   ### [Changes API](other/changes-api.md)

    Track and retrieve changes to resources over time for synchronization and auditing.
*   ### [Bulk Export](bulk-api/export.md)

    Export large volumes of FHIR data for backup, migration, or analytics.
*   ### Dump APIs

    A set of APIs that allow you to export FHIR Data as a stream of records in JSON or CSV format.

    *   ### [$dump](bulk-api/dump.md)

        Enables efficient retrieval and processing of a stream of FHIR resources of a specific type.
    *   ### [$dump-sql](bulk-api/dump-sql.md)

        Streams the results of SQL query in FHIR format or CSV format.
    *   ### [$dump-csv](bulk-api/dump-csv.md)

        Dumps the specific resource type to CSV
*   ### [Archive / Restore](other/archive-restore-api/)

    Archive resources for long-term storage and restore them when needed.



## Other APIs

Utility and system endpoints for health checks, versioning, and data transformation.

*   ### [$to-format](rest-api/other/to-format.md)

    Transform resources into different formats using the $to-format operation.
*   ### [$matcho](rest-api/other/matcho.md)

    Endpoint for testing the Matcho engine, which is used in [AccessPolicies](../access-control/authorization/#access-policies).
*   ### [Health check](rest-api/other/health-check.md)

    Check the health and status of the Aidbox instance.
*   ### [Version](rest-api/other/aidbox-version.md)

    Retrieve the current version and build information of Aidbox



### .
