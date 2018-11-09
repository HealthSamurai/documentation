---
description: >-
  Access Policy is a JSON schema filtering REST requests. A request succeeds if
  at least one of the policies is valid for it.
---

# Access Control

## Simple Access Policy

You can test access policies with Postman.

Access the Auth Clients tab and create new client.

```yaml
resourceType: Client
id: postman
secret: secret
```

Access the Access Control tab and create new access policy.

```yaml
engine: json-schema
schema:
  required:
    - client
    - uri
    - request-method
  properties:
    uri:
      type: string
      pattern: '^/fhir/.*'
    client:
      required:
        - id
      properties:
        id:
          const: postman
    request-method:
      const: get
resourceType: AccessPolicy
id: policy-for-postman
```

Now, let's execute requests in Postman.

### Positive Test

```javascript
GET {{base}}/fhir/Patient
```

![](../.gitbook/assets/policy1%20%281%29.png)

### Negative Test

```javascript
POST {{base}}/fhir/Patient
```

![](../.gitbook/assets/policy2%20%281%29.png)

## Policy Debugging

Let's use the parameter  \_\_debug=policy in requests to see which JSON-schema validation returned true/false.

### Positive Test

```javascript
GET {{base}}/fhir/Patient
```

![](../.gitbook/assets/policy3%20%281%29.png)

### Negative Test

```javascript
POST {{base}}/fhir/Patient
```

![](../.gitbook/assets/policy4%20%281%29.png)

See the full documentation [Access Policies](../security/access-control.md).

{% page-ref page="../security/access-control.md" %}



