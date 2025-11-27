---
title: "Implementing Role-Based Access Control for FHIR Resources with Keycloak and SMART on FHIR V2"
slug: "implementing-role-based-access-control-for-fhir-resources-with-keycloak-and-smart-on-fhir-v2"
published: "2025-10-14"
author: "Aleksandr Kislitsyn"
reading-time: "7 minutes"
tags: []
category: "Integrations"
teaser: "Fine-grained FHIR access control doesn’t have to be complex — see how we made it simple with Keycloak, SMART on FHIR, and Aidbox."
image: "cover.png"
---

Building healthcare applications on top of a FHIR server has become a common practice. FHIR servers provide standardized storage and APIs for clinical resources, allowing developers to focus on building features rather than managing healthcare data infrastructure. However, this introduces an important question: how do you ensure that different users see only the data they are allowed to see?

For example, a physician should have full access to patient observations — both laboratory results and vital signs. A lab technician, on the other hand, should only see finalized laboratory results relevant to their work. Implementing this kind of fine-grained access control traditionally requires complex application-level logic, custom authorization rules, and extensive testing.

There's a better way: a standards-based approach that leverages your existing identity provider and requires zero custom authorization code in your application.

In this post, we'll explore how to implement sophisticated role-based access control (RBAC) for FHIR resources using [Keycloak](https://www.keycloak.org/) (an open-source identity and access management solution) roles mapped to SMART on FHIR V2 scopes, with automatic enforcement by Aidbox. The best part? The same API endpoint returns different data based on who's asking — completely transparently.

## **The Problem: One Endpoint, Multiple Access Levels**

Imagine you're building a healthcare application on top of the FHIR server with two types of users:

**Dr. Sarah (Physician)**

- Needs to see all patient observations
- Reviews both laboratory results and vital signs
- Makes clinical decisions based on complete patient data

**Mike (Lab Technician)**

- Works only with finalized laboratory results
- Shouldn't see vital signs or preliminary lab data
- Needs access limited to their specific job function

Both users call the same API endpoint: GET /fhir/Observation. But they should see completely different results.

Traditionally, developers would solve this by adding:

- Custom middleware to check user roles
- Complex query filters in your application code
- Separate API endpoints or parameters for different user types
- Extensive unit tests for each authorization scenario
- Maintenance headaches when requirements change

This approach works, but it’s **hard to maintain** and **prone to errors** as requirements evolve. There's a better way.

## **The Solution: SMART Scopes Meet Keycloak Roles**

SMART on FHIR V2 introduces a powerful concept: [**scopes with query parameters**](https://build.fhir.org/ig/HL7/smart-app-launch/scopes-and-launch-context.html#finer-grained-resource-constraints-using-search-parameters). Instead of just anting broad access like "this user can read Observations," you can specify exact rules, such as:

```javascript
user/Observation.rs?category=laboratory&status=final
```

This scope means: "The user can read and search Observations, but only laboratory observations with final status."

By combining this with Keycloak's composite role system, we can:

1. **Define granular permissions** as basic Keycloak roles (each representing a SMART scope)
2. **Group them into job functions** using composite roles (like "physician" or "lab\_technician")
3. **Automatically resolve** composite roles into SMART scopes in the access token
4. **Let Aidbox enforce** the rules automatically — no custom code required

Here's how the flow works:

```javascript
User Login → Keycloak resolves roles → Token with SMART scopes →
Aidbox validates token → Automatic data filtering → Correct results
```

## **Implementation: Building the Role Structure**

### Step 1: Define Basic Roles (Granular SMART Scopes)

In Keycloak, we create basic roles that directly map to SMART on FHIR V2 scopes:

**Basic roles:**

- `user/Patient.rs` - Read and search patient data
- `user/Encounter.rs` - Read and search encounters
- `user/Observation.rs` - Read and search ALL observations
- `user/Observation.rs?category=laboratory&status=final` - Read and search ONLY finalized lab results

These basic roles are the building blocks. Each one represents a specific permission with optional query parameter restrictions.

### Step 2: Create Composite Roles (Job Functions)

Next, combine basic roles into composite roles that reflect real job functions:

**Physician Role** (full clinical access):

- `user/Patient.rs`
- `user/Encounter.rs`
- `user/Observation.rs`

**Lab Technician Role** (limited to lab results):

- `user/Patient.rs`
- `user/Observation.rs?category=laboratory&status=final`

Notice that the Lab Technician role uses the restricted Observation scope — preventing access to vital signs or draft results

### Step 3: Configure the Token Mapper

This is where the magic happens. Keycloak turns roles into access tokens. The token is a small, secure file (JSON Web Token — JWT) that travels with every API request and tells Aidbox what the user is allowed to do. We need Keycloak to:

1. Resolve composite roles (e.g., "physician") into their constituent basic roles
2. Include those basic roles in the token's `scope` claim as SMART scopes
3. Add the `atv: "2"` claim to indicate that there are SMART on FHIR scopes in the token to process

We accomplish this using a custom script-based protocol mapper. When a lab technician logs in, their access token looks like this:

```json
{
  "sub": "lab_technician",
  "scope": "user/Patient.rs 
            user/Observation.rs?category=laboratory&status=final",
  "atv": "2"
}
```

### Step 4: Configure Aidbox for Automatic Enforcement

Here's the most elegant part: Aidbox automatically enforces the access rules defined in the token. All you need is:****

1. **TokenIntrospector** - Verifies that the token is valid and issued by your Keycloak instance:

```javascript
{
  "resourceType": "TokenIntrospector",
  "jwt": {
    "iss": "http://localhost:8888/realms/master"
  },
  "type": "jwt",
  "jwks_uri": 
    "http://keycloak:8888/realms/master/protocol/openid-connect/certs",
  "id": "external-auth-server",
  "resourceType": "TokenIntrospector"
}
```

2. **AccessPolicy** — Allows requests with validated tokens:

```javascript
{
  "resourceType": "AccessPolicy",
  "id": "keycloak-access-policy",
  "engine": "matcho",
        "matcho": {
          "jwt": {
            "iss": "http://localhost:8888/realms/master"
          }
        }
}
```

That's it. When Aidbox receives a request with a token containing atv: "2" and SMART scopes, it automatically:

- Parses the scopes to determine allowed resource types
- Applies query parameter filters from the scopes
- Returns only data matching the scope constraints
- Rejects requests for unauthorized resource types

No custom authorization code. No complex access control logic. It just works.

## **Seeing It in Action**

Let's walk through both user scenarios to see how the same API call returns different results. Imagine you’ve set up your system with Aidbox connected to Keycloak, and both Dr. Sarah (the physician) and Mike (the lab technician) are using the same application.

In the system, there are two **Observation** resources:

- **Hemoglobin** — a laboratory observation with status “final”
- **Blood Pressure** — a vital signs observation

Both users will access the same endpoint: `GET /fhir/Observation`  
But Aidbox will return different responses depending on who’s making the request.

### **Test 1: Physician Access (Full Access)**

**Login credentials:**

```javascript
Username: physician
Password: password
```

**API call:**

```javascript
GET /fhir/Observation
Authorization: Bearer <physician_token>
```

**Token scopes:**

```javascript
user/Patient.rs user/Encounter.rs user/Observation.rs
```

**Result:** The physician sees **both observations**:

1. Hemoglobin (laboratory, final)
2. Blood Pressure (vital signs)

The scope `user/Observation.rs` grants unrestricted access to all Observation resources.

### **Test 2: Lab Technician Access (Restricted Access)**

**Login credentials:**

```javascript
Username: lab_technician
Password: password
```

**API call:**

```javascript
GET /fhir/Observation
Authorization: Bearer <lab_technician_token>
```

**Token scopes:**

```javascript
user/Patient.rs 
user/Observation.rs?category=laboratory&status=final
```

**Result:** The lab technician sees **only one observation**:

1. Hemoglobin (laboratory, final)

The Blood Pressure observation is filtered out because it doesn't match the scope requirements:

- It's category "vital-signs" (not "laboratory")
- The scope explicitly requires `category=laboratory&status=final`

Same endpoint, same code, different results based on who's asking. No custom filtering logic required.

## **Try It Yourself**

Want to see this in action? The complete [example](https://github.com/Aidbox/examples/tree/main/aidbox-features/smart-keycloak-roles) is ready to run with Docker.

**Prerequisites:** Docker and Docker Compose installed

**Quick start:**

1. **Clone the** [**repository**](https://github.com/Aidbox/examples) **and navigate to the example:**

```javascript
cd aidbox-features/smart-keycloak-roles
```

2. **Start all services:**

```javascript
docker compose up --build
```

3. **Initialize Aidbox:** Navigate to
   <http://localhost:8080>
   and complete the Aidbox initialization.
4. **Open the demo application:** Navigate to
   <http://localhost:3000>.
5. **Test both user roles:**
   - Login as `physician/password` — see all observations
   - Click “Start Over” and login as `lab_technician/password` — see only lab results

The demo application shows side-by-side what each user can access, making the access control differences immediately visible.

## **Key Takeaways**

This approach delivers several significant benefits:

**1. Zero Custom Authorization Code**   
Your application doesn't need to understand roles or implement filtering logic. Aidbox handles it automatically based on SMART scopes.

**2. Centralized Access Control**   
All role definitions live in your identity provider (Keycloak). Change a role's permissions in one place, and it applies everywhere.

**3. Standards-Based**   
Built on SMART on FHIR V2, an established healthcare interoperability standard. No proprietary solutions or vendor lock-in.

**4. Fine-Grained Control**   
Query parameter restrictions (?category=laboratory&status=final) enable precise access control without complex custom rules.

**5. Easy to Extend**   
Need a new role? Create a composite role in Keycloak combining the appropriate basic roles. No code changes required.

**Learn more:**

- [SMART on FHIR Scopes in Aidbox](https://www.health-samurai.io/docs/aidbox/access-control/authorization/smart-on-fhir/smart-scopes-for-limiting-access)
- [Keycloak Role-Based Access Control](https://www.keycloak.org/docs/latest/server_admin/#_composite-roles)
- [SMART App Launch Framework](https://hl7.org/fhir/smart-app-launch/)

## **Conclusion**

Fine-grained access control for FHIR resources doesn't have to be complicated. By combining SMART on FHIR V2 scopes, Keycloak's composite role system, and Aidbox's automatic scope enforcement, you can implement sophisticated RBAC without writing custom authorization code.

The result: safer applications, easier maintenance, and better alignment with healthcare interoperability standards.

Ready to implement role-based access control in your FHIR application? Start with the example repository and customize the roles for your specific use case.
