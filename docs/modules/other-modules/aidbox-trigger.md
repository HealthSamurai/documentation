# AidboxTrigger

{% hint style="info" %}
AidboxTrigger is available in FHIR schema mode and allows you to execute custom SQL statements automatically when FHIR resources are created, updated, or deleted.
{% endhint %}

**AidboxTrigger** is a powerful feature that enables automatic execution of SQL statements in response to FHIR resource operations. It acts as a database-level hook system that can maintain data consistency, implement custom business logic, or integrate with external systems through database operations.

## How AidboxTrigger works

AidboxTrigger works by registering SQL statements that are executed automatically after successful FHIR CRUD operations. The system:

1. Monitors `AidboxTrigger` resources in your Aidbox instance
2. Executes the configured SQL statements when matching FHIR operations occur
3. Supports parameterized queries using `{{id}}` template substitution
4. Ensures transactional consistency with automatic rollback on errors

## Configure AidboxTrigger

### Creating triggers

To set up an AidboxTrigger, create an `AidboxTrigger` resource with the following structure:

```json
{
  "resourceType": "AidboxTrigger",
  "action": ["create","update","delete"],
  "resource": "Patient",
  "sql": "INSERT INTO audit_log (resource_id, action) VALUES ({{id}}, 'created');"
}
```

#### Required fields

- `action` (string array): Array of operations that trigger the SQL execution
- `resource` (string): The FHIR resource type to monitor
- `sql` (string): The SQL statement to execute

#### Supported actions

<table><thead><tr><th width="120">Action</th><th width="300">Description</th><th>When Triggered</th></tr></thead><tbody><tr><td>create</td><td>Executes when a new FHIR resource is created</td><td>POST operations, Bundle entries with POST</td></tr><tr><td>update</td><td>Executes when an existing FHIR resource is updated</td><td>PUT and PATCH operations, Bundle entries with PUT/PATCH</td></tr><tr><td>delete</td><td>Executes when a FHIR resource is deleted</td><td>DELETE operations, Bundle entries with DELETE</td></tr></tbody></table>

## Examples

### Basic audit logging

Set up a queue table and triggers to log all Patient operations:

```sql
-- Create audit table
CREATE TABLE IF NOT EXISTS patient_audit (
  id text NOT NULL,
  resource_type text NOT NULL, 
  status text NOT NULL,
  action text NOT NULL,
  ts timestamp with time zone DEFAULT CURRENT_TIMESTAMP
);
```

Create triggers for all operations:

```http
# Create trigger
POST /fhir/AidboxTrigger
Content-Type: application/json

{
  "action": ["create","update","delete"],
  "sql": "INSERT INTO patient_audit (id, resource_type, status, action) VALUES ({{id}}, 'Patient', 'completed', 'create');",
  "resource": "Patient"
}
```

### Multiple {{id}} usage

You can use multiple `{{id}}` references in a single SQL statement:

```http
POST /fhir/AidboxTrigger
Content-Type: application/json

{
  "action": "create",
  "sql": "INSERT INTO patient_audit (id, resource_type, encounter_id, action) VALUES ({{id}}, 'Patient', (SELECT id FROM encounter where resource#>>'{subject,id}' = {{id}} LIMIT 1), 'create');",
  "resource": "Patient"
}
```

### SQL without parameters

Triggers don't require the `{{id}}` parameter:

```http
POST /fhir/AidboxTrigger  
Content-Type: application/json

{
  "action": "create",
  "sql": "INSERT INTO patient_stats (total_patients) VALUES (1) ON CONFLICT (id) DO UPDATE SET total_patients = patient_stats.total_patients + 1;",
  "resource": "Patient"  
}
```

## Error Handling and Rollback

### Automatic Rollback

AidboxTrigger ensures data consistency through automatic rollback:

- If any trigger SQL fails, the entire FHIR operation is rolled back
- The original FHIR resource operation returns a 500 error
- No partial state changes are persisted

```http
# Example: Invalid SQL causes rollback
POST /fhir/AidboxTrigger
Content-Type: application/json

{
  "action": "create", 
  "sql": "INVALID SQL STATEMENT",
  "resource": "Patient"
}

# Subsequent Patient creation will fail:
POST /fhir/Patient
# Returns: 500 Internal Server Error
# {
#   "issue": [{
#     "code": "exception",
#     "diagnostics": "Error occured during AidboxTrigger execution", 
#     "severity": "fatal"
#   }]
# }
```

### Bundle Operations

AidboxTrigger respects Bundle transaction semantics:

**Batch Bundles**: Partial failure is allowed - successful operations have their triggers executed, failed operations are skipped.

**Transaction Bundles**: Complete rollback on any failure - if any operation or trigger fails, all changes are rolled back.

```http
# Transaction bundle - all or nothing
POST /fhir
Content-Type: application/json

{
  "resourceType": "Bundle",
  "type": "transaction", 
  "entry": [
    {
      "request": {"method": "POST", "url": "/Patient"},
      "resource": {"id": "valid-patient"}
    },
    {
      "request": {"method": "POST", "url": "/Patient"}, 
      "resource": {"id": "invalid-patient", "name": "INVALID"}
    }
  ]
}
# If validation fails on invalid-patient, 
# no triggers execute and no resources are created
```

## Best Practices

### Performance Considerations

- Keep trigger SQL statements lightweight and fast
- Avoid complex queries that might slow down FHIR operations
- Consider using background processing for heavy operations
- Test your SQL statements thoroughly before deploying triggers
- Ensure trigger SQL follows least-privilege principles
- Validate that triggers don't expose sensitive data
