# Set up Aidbox with Postman

This tutorial will guide you through the process of setting up and using Postman with Aidbox for API testing and development.

## Prerequisites

* Aidbox instance up and running
* Postman installed on your computer
* Basic understanding of REST APIs and FHIR

## Step 1: Create a Client in Aidbox

First, you need to create a client in Aidbox that will be used for authentication. You can do this through the Aidbox UI or by making a direct API call.

### Using JSON in `IAM/Client/New` tab

```json
{
  "resourceType": "Client",
  "id": "postman",
  "secret": "secret",
  "grant_types": ["basic"]
}
```

### Using REST Console

```http
PUT /Client/postman
Content-Type: application/json

{
  "resourceType": "Client",
  "id": "postman",
  "secret": "secret",
  "grant_types": ["basic"]
}
```

## Step 2: Set up Postman Environment

1. Open Postman and create a new environment
2. Add the following variables:
   * `base_url`: Your Aidbox instance URL (e.g., `http://localhost:8080`)
   * `client_id`: `postman`
   * `client_secret`: `secret`

## Step 3: Create Access Policy

Create an access policy to define what operations your Postman client can perform. While Aidbox supports multiple engines (`matcho`, `json-schema`, and others), we'll focus on the `matcho` engine as it provides a more concise and readable syntax. Here are examples of common access policies:

### Full CRUD Access

This policy allows all CRUD operations (Create, Read, Update, Delete) on all FHIR resources.\
\
**Using JSON in `IAM/AccessPolicy/New` tab**

```json
{
  "resourceType": "AccessPolicy",
  "id": "policy-for-postman-crud",
  "engine": "matcho",
  "matcho": {
    "uri": "#^/fhir/.*",
    "client": {
      "id": "postman"
    },
    "request-method": {
      "$enum": ["get", "post", "put", "delete", "patch"]
    }
  }
}
```

**REST Console**

```http
PUT /AccessPolicy/policy-for-postman-crud
Content-Type: application/json

{
  "resourceType": "AccessPolicy",
  "id": "policy-for-postman-crud",
  "engine": "matcho",
  "matcho": {
    "uri": "#^/fhir/.*",
    "client": {
      "id": "postman"
    },
    "request-method": {
      "$enum": ["get", "post", "put", "delete", "patch"]
    }
  }
}
```

### Resource-Specific Access

This policy allows CRUD operations only on Patient resources.\
\
**Using JSON in `IAM/AccessPolicy/New` tab**

```json
{
  "resourceType": "AccessPolicy",
  "id": "policy-for-postman-patient",
  "engine": "matcho",
  "matcho": {
    "uri": "#^/fhir/Patient.*",
    "client": {
      "id": "postman"
    },
    "request-method": {
      "$enum": ["get", "post", "put", "delete", "patch"]
    }
  }
}
```

**REST Console**

```http
PUT /AccessPolicy/policy-for-postman-patient
Content-Type: application/json

{
  "resourceType": "AccessPolicy",
  "id": "policy-for-postman-patient",
  "engine": "matcho",
  "matcho": {
    "uri": "#^/fhir/Patient.*",
    "client": {
      "id": "postman"
    },
    "request-method": {
      "$enum": ["get", "post", "put", "delete", "patch"]
    }
  }
}
```

### Read-Only Access

This policy allows only GET requests to all FHIR resources.\
\
**Using JSON in `IAM/AccessPolicy/New` tab**

```json
{
  "resourceType": "AccessPolicy",
  "id": "policy-for-postman-read",
  "engine": "matcho",
  "matcho": {
    "uri": "#^/fhir/.*",
    "client": {
      "id": "postman"
    },
    "request-method": "get"
  }
}
```

**REST Console**

```http
PUT /AccessPolicy/policy-for-postman-read
Content-Type: application/json

{
  "resourceType": "AccessPolicy",
  "id": "policy-for-postman-read",
  "engine": "matcho",
  "matcho": {
    "uri": "#^/fhir/.*",
    "client": {
      "id": "postman"
    },
    "request-method": "get"
  }
}
```

### Write-Only Access

This policy allows only write operations (POST, PUT, PATCH) to all FHIR resources.\
\
**Using JSON in `IAM/AccessPolicy/New` tab**

```json
{
  "resourceType": "AccessPolicy",
  "id": "policy-for-postman-write",
  "engine": "matcho",
  "matcho": {
    "uri": "#^/fhir/.*",
    "client": {
      "id": "postman"
    },
    "request-method": {
      "$enum": ["post", "put", "patch"]
    }
  }
}
```

**REST Console**

```http
PUT /AccessPolicy/policy-for-postman-write
Content-Type: application/json

{
  "resourceType": "AccessPolicy",
  "id": "policy-for-postman-write",
  "engine": "matcho",
  "matcho": {
    "uri": "#^/fhir/.*",
    "client": {
      "id": "postman"
    },
    "request-method": {
      "$enum": ["post", "put", "patch"]
    }
  }
}
```

## Step 4: Test the Setup

1. Create a new request in Postman
2. Set the request method to GET
3. Set the URL to `{{base_url}}/fhir/Patient`
4. Add the following headers:
   * `Content-Type: application/json`
   * `Authorization: Basic {{base64(client_id:client_secret)}}`

To generate the Basic Auth header value, use Postman's built-in Authorization tab to handle this automatically:

1. Select "Basic Auth" type
2. Enter your `client_id` as the username
3. Enter your `client_secret` as the password

## Debugging Tips

1. Check the response headers for additional information about the request processing
2. If you get a 401 Unauthorized error:
   * Verify your client credentials
   * Check that your access policy is correctly configured
   * Ensure the Basic Auth header is properly formatted

### Additional Resources

{% content-ref url="../../modules/access-control/" %}
[access-control](../../modules/access-control/)
{% endcontent-ref %}

{% content-ref url="../../modules/access-control/authorization/access-policies.md" %}
[access-policies.md](../../modules/access-control/authorization/access-policies.md)
{% endcontent-ref %}

{% content-ref url="../security-access-control-tutorials/accesspolicy-best-practices.md" %}
[accesspolicy-best-practices.md](../security-access-control-tutorials/accesspolicy-best-practices.md)
{% endcontent-ref %}
