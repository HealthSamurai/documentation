# API Overview

## Data Management

Healthcare systems must handle complex data workflows while maintaining integrity and meeting regulatory requirements. Aidbox's data management APIs implement the [FHIR HTTP API](https://www.hl7.org/fhir/http.html) specification with extensions designed for production healthcare environments.

### CRUD

At the heart of any healthcare system lies the ability to create, read, update, and delete clinical resources - the fundamental CRUD operations that power everything from patient registration to medication orders. Aidbox implements these operations following the FHIR RESTful API specification, where each resource type gets its own endpoint and standard HTTP methods provide predictable behavior across all resource types. When a nurse creates a new patient record, updates vital signs, or a physician reviews medical history, they're using these CRUD APIs behind the scenes, with Aidbox ensuring each operation maintains data consistency through PostgreSQL's ACID transactions.

The RESTful design means developers work with familiar HTTP patterns: POST to create resources, GET to retrieve them, PUT to update, and DELETE to remove. Each operation returns appropriate HTTP status codes and follows FHIR's versioning strategy through ETags and the \_history endpoint. For instance, creating a Patient resource returns a 201 status with the Location header pointing to the newly created resource.

See also:

* [CRUD Operations](rest-api/crud/)
* [FHIR HTTP API](https://www.hl7.org/fhir/http.html)

### Validation

Healthcare data validation ensures that clinical information conforms to predefined constraints and business rules. Aidbox uses [FHIR Schema](../modules/profiling-and-validation/fhir-schema-validator/) validation engine -  an engine that uses FHIR Schema format internally. FHIR Schema is a developer-friendly format that simplifies FHIR StructureDefinitions into intuitive, JSON Schema-like representations. FHIR Schema validation engine provides enhanced performance, supports advanced features like FHIRPath invariants and slicing, and offers clearer error messages compared to traditional validation approaches.

Aidbox provides two types of validation:

* **Automatic validation**: Occurs during create/update operations to prevent invalid data from entering the system
* **Explicit validation**: The `$validate` operation checks resources without persisting them, useful for testing and form validation

For example, validating a Patient resource checks required fields like gender codes, date formats, and identifier systems:

```json
{
  "resourceType": "Patient",
  "birthDate": "2024-13-45",  // Invalid date
  "gender": "M"                // Invalid code (should be 'male')
}
```

See also:

* [Profiling and Validation](../docs/modules/profiling-and-validation/)
* [FHIR Schema Validator](../modules/profiling-and-validation/fhir-schema-validator/)

### History

In healthcare, knowing not just what data looks like now, but how it changed over time, is crucial for understanding patient journeys and tracking resource evolution. Aidbox automatically tracks every change to every resource, maintaining version history that captures what changed and when. This history API enables clinical workflows like reviewing how a patient's condition evolved, understanding medication adjustments over time, or retrieving previous versions of resources.

The history mechanism works at two levels: instance history tracks changes to individual resources, and type history shows all changes across a resource type. Each history entry includes the complete resource state at that point in time, the HTTP method used (POST, PUT, DELETE), version identifiers, and timestamps. For example, `GET /fhir/Patient/123/_history` retrieves all versions of a specific patient, while `GET /fhir/Patient/_history?_since=2024-01-01` shows all patient changes since a specific date.

See also:

* [History](rest-api/history.md)
* [FHIR History](https://www.hl7.org/fhir/http.html#history)

### Bundle

Real-world healthcare operations rarely involve single, isolated data changes. Admitting a patient might require creating a Patient resource, an Encounter, multiple Observations, and updating various other records - all of which must succeed or fail together. FHIR Bundles solve this by packaging multiple operations into a single atomic transaction. Aidbox's Bundle API processes these multi-operation requests within a single PostgreSQL transaction, ensuring data consistency even in complex workflows.

Bundles support different processing modes for different use cases. Transaction bundles ensure all-or-nothing processing where every operation must succeed or the entire bundle rolls back - critical for maintaining referential integrity. Batch bundles process operations independently, continuing even if individual operations fail - useful for bulk updates where partial success is acceptable. Message bundles carry event notifications with guaranteed delivery semantics, while document bundles package complete clinical documents like discharge summaries with all their referenced resources.

Aidbox implements bundle processing exactly as specified in the [FHIR Bundle specification](https://www.hl7.org/fhir/bundle.html), including automatic reference resolution for temporary identifiers, conditional operations, and detailed operation outcomes. The implementation follows FHIR's processing rules precisely, ensuring full compatibility with other FHIR-compliant systems while leveraging PostgreSQL's transaction capabilities for optimal performance.

See also:

* [Bundle](rest-api/bundle.md)
* [Batch/Transaction](batch-transaction.md)
* [FHIR Bundle](https://www.hl7.org/fhir/bundle.html)
* [Transaction Processing](https://www.hl7.org/fhir/http.html#transaction)

### Bulk Import

Healthcare data migrations and integrations often involve millions of records from various scenarios:

* Initial system deployments requiring complete data migration
* Regular data synchronization with external systems
* One-time imports from legacy systems or data warehouses
* Bulk loading of research datasets or population health data

Traditional RESTful APIs, while perfect for real-time operations, struggle with such volumes. Aidbox's bulk import APIs provide two complementary approaches: `$load` for synchronous imports when you need immediate confirmation, and `$import` for asynchronous processing of massive datasets.

The choice between `$load` and `$import` depends on your specific use case. The `$load` operation processes data synchronously, making it ideal for smaller datasets (up to hundreds of thousands of records) where you need immediate feedback about success or failure. It streams NDJSON data directly into PostgreSQL using [COPY](https://www.postgresql.org/docs/current/sql-copy.html) commands, providing real-time progress and error reporting. The `$import` operation handles truly massive datasets asynchronously, accepting data via URLs or direct upload, queuing the import job, and processing millions of records in the background while your application continues other work.

See also:

* [`$load`](bulk-api/load-and-fhir-load.md)
* [`$import`](bulk-api/import-and-fhir-import.md)

### Other APIs

Beyond the core data management operations, Aidbox provides specialized APIs that address specific healthcare challenges. The Encryption API enables field-level encryption for sensitive data like Social Security numbers or psychiatric notes, ensuring data remains protected even if database backups are compromised. The Sequence API generates guaranteed-unique identifiers crucial for medical record numbers and order IDs, using PostgreSQL sequences to ensure uniqueness even under high concurrency. Batch Upsert combines insert and update operations, perfect for synchronizing data from external systems where you don't know if records already exist.

See also:

* [Encryption API](other/encryption-api.md)
* [Sequence API](other/sequence-api.md)
* [Batch Upsert](other/batch-upsert.md)

## Data Querying

Healthcare applications need to retrieve and analyze patient data efficiently, from simple lookups to complex analytical queries. Aidbox provides multiple querying approaches that balance FHIR compliance with the performance demands of production healthcare systems, allowing developers to choose the right tool for each specific use case.

### FHIR Search

When building healthcare applications, you often need to find patients by name, retrieve recent lab results, or search for medications prescribed within a date range. FHIR Search provides a standardized way to query resources using familiar REST patterns, with Aidbox implementing the complete [FHIR Search specification](https://www.hl7.org/fhir/search.html) including all standard search parameters, modifiers, and chaining capabilities.

The search API follows RESTful conventions where each resource type has its own search endpoint. For example, `GET /Patient?name=smith&gender=male` finds all male patients with "smith" in their name, while `GET /Observation?patient=Patient/123&date=gt2024-01-01` retrieves all observations for a specific patient after January 1st, 2024. Search results include pagination controls, total counts, and can be sorted by any searchable parameter.

See also:

* [FHIR Search](rest-api/fhir-search/)
* [FHIR Search Specification](https://www.hl7.org/fhir/search.html)

### Aidbox Search

While FHIR Search provides comprehensive querying capabilities, healthcare applications sometimes need specialized filtering approaches that extend beyond the standard FHIR specification. Aidbox provides several advanced search approaches that offer the flexibility needed for complex clinical workflows.

[**Search Resources**](rest-api/aidbox-search.md#search-resource) allow you to define custom search parameters or override existing FHIR SearchParameters with SQL-based implementations for performance optimization and complex custom searches.

[**AidboxQuery**](rest-api/aidbox-search.md#aidboxquery) provides a general SQL-based search approach with a DSL to build complex queries through a dedicated endpoint, perfect for generating reports and implementing specialized search logic.

[**Dot expressions**](rest-api/aidbox-search.md#dot-expressions) enable search without defining SearchParameters by providing direct access to JSON paths in FHIR resources with optional PostgreSQL type coercion and operators.

[**$lookup**](rest-api/aidbox-search.md#lookup) provides efficient prefix search for resources by multiple key attributes, designed specifically for type-ahead dropdown scenarios in user interfaces with optimized performance for millions of records.

Aidbox also provides special search parameters like `_explain` for query execution plan analysis, `_timeout` for controlling query timeouts, and `_search-language` for multi-language search support.

See also:

* [Aidbox Search](rest-api/aidbox-search.md)

### SQL-on-FHIR

Healthcare analytics and reporting often require complex queries that go beyond what FHIR search can express - aggregating data across multiple resources, performing statistical analysis, or generating custom reports. Aidbox implements the [SQL-on-FHIR specification](https://build.fhir.org/ig/HL7/sql-on-fhir-v2/), which bridges this gap by allowing you to write SQL queries directly against FHIR data, treating resources as relational tables while maintaining FHIR's semantic structure.

The SQL-on-FHIR implementation provides flat views of FHIR resources that can be queried using standard SQL, making it possible to write complex analytical queries using familiar SQL syntax. For example, you can join Patient and Observation resources to analyze lab trends, aggregate medication data across populations, or generate custom reports that combine data from multiple resource types. This approach enables seamless integration with existing analytical tools and BI platforms that expect SQL interfaces.

Aidbox uses [ViewDefinition](https://build.fhir.org/ig/FHIR/sql-on-fhir-v2/StructureDefinition-ViewDefinition.html) resources to define these flat views, automatically creating and maintaining them to ensure they stay synchronized with the underlying FHIR data while providing the performance benefits of PostgreSQL's query optimizer.

See also:

* [SQL-on-FHIR](../modules/sql-on-fhir/)
* [SQL-on-FHIR Specification](https://build.fhir.org/ig/FHIR/sql-on-fhir-v2/)

### SQL APIs

Sometimes you need direct access to the underlying database for custom queries, bulk operations, or integration with existing analytics tools. Aidbox's SQL APIs provide low-level access to PostgreSQL, allowing you to execute custom SQL queries while maintaining proper access controls and audit trails.

The SQL endpoints support parameterized queries to prevent SQL injection, provide result streaming for large datasets, and include proper error handling and logging. This direct SQL access is particularly useful for data migration scripts, custom analytics pipelines, or integration with external reporting tools that expect SQL interfaces.

See also:

* [SQL APIs](rest-api/other/sql-endpoints.md)

### GraphQL

Modern healthcare applications often need to retrieve complex, nested data structures - a patient's complete medical history including encounters, observations, medications, and procedures, all in a single request. GraphQL provides a flexible query language that allows clients to specify exactly what data they need, reducing over-fetching and under-fetching while providing a single endpoint for all data access.

Aidbox's GraphQL API is based on the [FHIR GraphQL specification](https://build.fhir.org/graphql.html) and maps FHIR resources to GraphQL types, allowing you to query FHIR data using GraphQL syntax. For example, a single GraphQL query can retrieve a patient with all their encounters, observations, and medications, with the exact fields and relationships you specify. This approach is particularly valuable for mobile applications and single-page applications that need to minimize network requests.

The GraphQL implementation includes features like field-level authorization, query complexity analysis to prevent expensive queries, and integration with Aidbox's caching layer for improved performance. Beyond the standard FHIR GraphQL specification, Aidbox adds additional features such as `_include` and `_revinclude` parameters for efficient resource traversal, enabling you to fetch related resources in a single query. The API maintains FHIR semantics while providing the flexibility and efficiency benefits of GraphQL, with additional Aidbox-specific extensions for enhanced functionality.

See also:

* [GraphQL](graphql-api.md)

### $everything

Clinical workflows often require a complete view of a patient's data - all their encounters, observations, medications, procedures, and other resources in one comprehensive dataset. The `$everything` operation provides this capability by retrieving all resources related to a specific patient or other entity, creating a complete clinical summary that's essential for care coordination and patient handoffs.

The operation follows the [FHIR $everything specification](https://www.hl7.org/fhir/operation-patient-everything.html), which defines how to retrieve all resources related to a patient.

`$everything` is particularly useful for generating patient summaries, supporting care transitions, and providing complete datasets for analytics or research purposes. The operation maintains referential integrity by including all related resources, ensuring that the complete clinical picture is preserved.

See also:

* [$everything](rest-api/everything-on-patient.md)
* [FHIR $everything Operation](https://www.hl7.org/fhir/operation-patient-everything.html)

### Other APIs

Beyond the core querying capabilities, Aidbox provides specialized APIs for specific healthcare use cases. The `$document` operation generates FHIR Documents - structured clinical summaries that package complete patient information for care coordination and legal requirements. The `$lastn` operation retrieves the most recent N observations for a patient, useful for trending analysis and clinical decision support.

See also:

* [$document](rest-api/other/document.md)
* [$lastn](rest-api/other/observation-lastn.md)

## Data Feed And Export

Healthcare systems need to provide real-time data feeds for clinical decision support, enable data synchronization across multiple systems, and support bulk export for analytics, research, and regulatory compliance. Aidbox provides comprehensive APIs for data streaming, change tracking, and bulk export that address the complex data flow requirements of modern healthcare environments.

### Subscriptions

Real-time clinical decision support and care coordination require immediate notification when patient data changes - a new lab result arrives, a medication is prescribed, or a care plan is updated. Aidbox implements topic-based subscriptions modeled after the [FHIR R6 SubscriptionTopic](https://build.fhir.org/subscriptiontopic.html), providing dynamic subscriptions to changes in FHIR resources through multiple channels including webhooks, Kafka, and GCP Pub/Sub, etc.

The subscription system uses `AidboxSubscriptionTopic` resources to define event triggers with FHIRPath criteria, allowing precise filtering of notifications. For example, a clinical decision support system can subscribe to completed QuestionnaireResponse resources, receiving immediate notifications when lab results are finalized or when medication orders are created for patients with specific conditions.

Aidbox supports multiple delivery channels with guaranteed delivery options, automatic retry logic for failed notifications. The implementation provides flexible payload options from full resources to minimal notifications, optimizing for different use cases and network constraints.

See also:

* [Subscriptions](../modules/topic-based-subscriptions/)

### Bulk Export

Healthcare analytics, research, and regulatory reporting often require complete datasets that span millions of records across multiple resource types. Traditional REST APIs struggle with such volumes, leading to timeouts, memory issues, and unreliable data extraction. Aidbox's bulk export APIs provide efficient, reliable mechanisms for extracting large datasets while maintaining data integrity and supporting incremental exports.

The bulk export system supports NDJSON format for streaming processing, with optional gzip compression for efficient storage and transfer. Export jobs can be configured to filter data by resource types, date ranges, and custom criteria, allowing you to extract only the data you need. The system provides real-time progress tracking, detailed error reporting, and automatic retry logic for failed exports.

Aidbox implements the [FHIR Bulk Data Export specification](https://hl7.org/fhir/uv/bulkdata/export.html), which defines standardized endpoints for bulk export operations. The implementation includes support for group-based exports, patient-specific exports, and system-wide exports, with proper authentication and authorization controls to ensure data security and privacy compliance.

See also:

* [Bulk Export](bulk-api/export.md)
* [FHIR Bulk Data Export](https://hl7.org/fhir/uv/bulkdata/export.html)

### Dump APIs

Sometimes you need to stream data directly from the database for real-time processing, custom analytics pipelines, or integration with external tools that expect streaming data formats. Aidbox's dump APIs provide efficient streaming access to FHIR data without the overhead of bulk export job management, making them ideal for real-time data processing and custom analytics workflows.

The `$dump` operation streams FHIR resources of a specific type in real-time using [Chunked Transfer Encoding](https://en.wikipedia.org/wiki/Chunked_transfer_encoding) with NDJSON format, providing immediate access to data as it's processed. This approach is particularly valuable for data pipelines that need to process large datasets incrementally, real-time analytics that require fresh data, or integration scenarios where you need to stream data to external systems without storing intermediate files.

The `$dump-sql` operation extends this capability by allowing you to stream the results of custom SQL queries in FHIR format or CSV format. This provides maximum flexibility for complex data extraction scenarios, enabling you to join multiple resource types, apply custom filtering logic, or transform data during the export process. The operation supports parameterized queries for security and provides streaming results to handle large datasets efficiently.

The `$dump-csv` operation provides direct CSV export for specific resource types, making it easy to integrate with spreadsheet applications, business intelligence tools, or custom analytics platforms that expect CSV input. This operation is optimized for performance and includes proper handling of nested FHIR data structures in flat CSV format.

See also:

* [$dump](bulk-api/dump.md)
* [$dump-sql](bulk-api/dump-sql.md)
* [$dump-csv](bulk-api/dump-csv.md)

### Archive / Restore

Healthcare organizations face increasing pressure to manage data retention, comply with regulatory requirements, and optimize storage costs while maintaining access to historical data for research and legal purposes. The Archive/Restore API provides a comprehensive solution for long-term data management, allowing you to archive resources to cost-effective cloud storage while maintaining the ability to restore them when needed.

The archive system uses task-based operations to upload resources to AWS or GCP cloud storage in compressed NDJSON format. You can archive resources based on time periods or retention policies, with options to automatically prune archived data from the database. The system supports both manual archiving and automated scheduling for compliance workflows.

Aidbox's implementation provides flexible archiving policies with support for selective restoration, complete data deletion from cloud storage, and comprehensive audit trails. The restore process is designed to be fast and reliable, with safeguards to prevent data duplication when restoring archived resources back to the database.

See also:

* [Archive / Restore](other/archive-restore-api/)

## Other APIs

Utility and system endpoints for health checks, versioning, and data transformation.

*   #### [$to-format](rest-api/other/to-format.md)

    Conversion between Aidbox and FHIR formats using `POST /$to-format/fhir` and `POST /$to-format/aidbox` endpoints.
*   #### [$matcho](rest-api/other/matcho.md)

    Endpoint for testing the Matcho engine, which is used in [AccessPolicies](../access-control/authorization/#access-policies).
*   #### [Health check](rest-api/other/health-check.md)

    Check the health and status of the Aidbox instance.
*   #### [Version](rest-api/other/aidbox-version.md)

    Retrieve the current version and build information of Aidbox
