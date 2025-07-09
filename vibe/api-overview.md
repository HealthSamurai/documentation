# API Overview

## Data Management

Healthcare systems must handle complex data workflows while maintaining integrity and meeting regulatory requirements. Aidbox's data management APIs implement the [FHIR HTTP API](https://www.hl7.org/fhir/http.html) specification with extensions designed for production healthcare environments.

### CRUD

At the heart of any healthcare system lies the ability to create, read, update, and delete clinical resources - the fundamental CRUD operations that power everything from patient registration to medication orders. Aidbox implements these operations following the FHIR RESTful API specification, where each resource type gets its own endpoint and standard HTTP methods provide predictable behavior across all resource types. When a nurse creates a new patient record, updates vital signs, or a physician reviews medical history, they're using these CRUD APIs behind the scenes, with Aidbox ensuring each operation maintains data consistency through PostgreSQL's ACID transactions.

The RESTful design means developers work with familiar HTTP patterns: POST to create resources, GET to retrieve them, PUT to update, and DELETE to remove. Each operation returns appropriate HTTP status codes and follows FHIR's versioning strategy through ETags and the _history endpoint. For instance, creating a Patient resource returns a 201 status with the Location header pointing to the newly created resource.

See also:
* [CRUD Operations](rest-api/crud/)
* [FHIR HTTP API](https://www.hl7.org/fhir/http.html)

### Validation

Healthcare data validation ensures that clinical information conforms to predefined constraints and business rules. Aidbox uses [FHIR Schema validation](../docs/modules/profiling-and-validation/fhir-schema-validator/) - a developer-friendly format that simplifies FHIR StructureDefinitions into intuitive, JSON Schema-like representations. FHIR Schema provides enhanced performance, supports advanced features like FHIRPath invariants and slicing, and offers clearer error messages compared to traditional validation approaches.

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
* [Profiling and Validation](../docs/modules/profiling-and-validation/README.md)
* [FHIR Schema Validator](../docs/modules/profiling-and-validation/fhir-schema-validator/)

### History

In healthcare, knowing not just what data looks like now, but how it changed over time, is crucial for understanding patient journeys and tracking resource evolution. Aidbox automatically tracks every change to every resource, maintaining version history that captures what changed and when. This history API enables clinical workflows like reviewing how a patient's condition evolved, understanding medication adjustments over time, or retrieving previous versions of resources.

The history mechanism works at two levels: instance history tracks changes to individual resources, and type history shows all changes across a resource type. Each history entry includes the complete resource state at that point in time, the HTTP method used (POST, PUT, DELETE), version identifiers, and timestamps. For example, `GET /Patient/123/_history` retrieves all versions of a specific patient, while `GET /Patient/_history?_since=2024-01-01` shows all patient changes since a specific date.

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