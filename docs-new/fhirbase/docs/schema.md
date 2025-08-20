# Schema

Fhirbase schema is regular and uniform. Each resource is saved in a table with the lower case name of the resourceType. For example, the **Patient** resource is saved in the **patient** table. All resource tables has similar structure:

```
CREATE TABLE "patient" (
  id text PRIMARY KEY,               // id of resource
  txid bigint not null,              // version id and logical transaction id
  ts timestamptz DEFAULT NOW(),      // last updated time
  resource_type text,                // resource type
  status resource_status not null,   // resource status
  resource jsonb not null            // resource body
);
```

* `id` is an resource ID
* `txid` is the versionId for the resource and at the same time the sequential id of logical transaction, which can be used for implementing integrations, ETags, reactive APIs, etc.
* `ts` stores a timestamp of last resource update
* `status` is enumeration with possible values: `create`, `updated`, `deleted`, `recreated`
* `resource` stores JSON representation of a resource in [slightly altered format](json-format).

Previous versions of resources are saved in the history table:

```
CREATE TABLE "patient_history" (
  id text, 
  txid bigint not null, 
  ts timestamptz DEFAULT current_timestamp,
  resource_type text default 'Media', 
  status resource_status not null,  
  resource jsonb not null, 
  PRIMARY KEY (id, txid)
);
```

History tables are postfixed with "\_history" and have same structure as primary tables. On resource update Fhirbase moves the current version of the resource from the primary table to the history table.

Sometimes you change many resources in one transaction and/or want to store meta-information about transactions. Fhirbase has a special **transaction** table to store transaction info and the `id` sequence for transaction is used as versionId (`txid`) in resources, which were touched in this transaction.

```
CREATE TABLE "transaction" ( 
  id serial primary key, 
  ts timestamptz DEFAULT current_timestamp,
  resource jsonb
);
```

The schematic transaction workflow usually looks like this:

* Create a new record in the transaction table or get next transaction ID from transaction ID sequence - `txid`
* For all resources which will be changed in the transaction, set `txid` to the value obtained in previous step
* Save changed resources

**txid** and record in transaction table can be used to track back changes. **txid** can also be used as the globally increasing version id for resources - a logical clock which may be helpful for caches and reactive engines implementation.\
