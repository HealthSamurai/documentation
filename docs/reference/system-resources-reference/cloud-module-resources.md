---
description: Aidbox Cloud module resources for integration with cloud providers like AWS, GCP, and Azure.
---

# Cloud Module Resources

Resources for configuration and management Aidbox integration with cloud providers.

 ## AwsAccount

```fhir-structure
[ {
  "path" : "_source",
  "name" : "_source",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "System Property. DO NOT USE IT."
}, {
  "path" : "access-key-id",
  "name" : "access-key-id",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "AWS access key identifier for authentication."
}, {
  "path" : "host",
  "name" : "host",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "AWS host endpoint for the service."
}, {
  "path" : "path-style",
  "name" : "path-style",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether to use path-style addressing for S3 requests."
}, {
  "path" : "region",
  "name" : "region",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "AWS region where the resources are located."
}, {
  "path" : "secret-access-key",
  "name" : "secret-access-key",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "AWS secret access key for authentication."
}, {
  "path" : "use-ssl",
  "name" : "use-ssl",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Whether to use SSL for secure connections."
} ]
```


## AzureAccount

```fhir-structure
[ {
  "path" : "_source",
  "name" : "_source",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "System Property. DO NOT USE IT."
}, {
  "path" : "clientId",
  "name" : "clientId",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Azure AD application/client ID (required for User Delegation SAS). Available since 2508."
}, {
  "path" : "clientSecret",
  "name" : "clientSecret",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Azure AD client secret (required for User Delegation SAS). Available since 2508."
}, {
  "path" : "key",
  "name" : "key",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Azure storage account key for authentication (required for Account SAS)."
}, {
  "path" : "sasType",
  "name" : "sasType",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "SAS type: 'account' (default) or 'userDelegation'. Available since 2508."
}, {
  "path" : "tenantId",
  "name" : "tenantId",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Azure AD tenant ID (required for User Delegation SAS)."
} ]
```


## AzureContainer

```fhir-structure
[ {
  "path" : "_source",
  "name" : "_source",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "System Property. DO NOT USE IT."
}, {
  "path" : "account",
  "name" : "account",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to the Azure account credentials. \n\n**Allowed references**: AzureAccount"
}, {
  "path" : "container",
  "name" : "container",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Name of the Azure storage container."
}, {
  "path" : "extension",
  "name" : "extension",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "File extension for content stored in the container."
}, {
  "path" : "storage",
  "name" : "storage",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Azure storage account name."
} ]
```


## GcpServiceAccount

```fhir-structure
[ {
  "path" : "_source",
  "name" : "_source",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "System Property. DO NOT USE IT."
}, {
  "path" : "private-key",
  "name" : "private-key",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Private key for GCP service account authentication."
}, {
  "path" : "service-account-email",
  "name" : "service-account-email",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Email address of the GCP service account."
} ]
```

