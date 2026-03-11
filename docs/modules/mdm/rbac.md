# RBAC configuration

> **Note:** RBAC in MPI is minimal and early-stage. It supports only two roles with no granular permissions. We are open to feedback and suggestions on how access control should evolve.

## How it works

MPI uses a simple **admin / basic** two-tier model. A user is either an **admin** (full access) or a **basic user** (patient search and duplicate matching only).

The role is determined by checking the user's `data.groups` array in Aidbox against the configured `MPI_ADMIN_ROLE` environment variable.

## Configuration

### 1. Environment variables

| Variable | Service | Purpose | Example |
|----------|---------|---------|---------|
| `MPI_ADMIN_ROLE` | Backend + Frontend | Group name that grants admin access | `SIT_EMPI_ADMIN_DEV` |
| `MPI_ENABLE_AUTHENTICATION` | Backend | Enable authentication (`true`/`false`) | `true` |
| `MPI_ENABLE_AUTHORIZATION` | Backend | Enable authorization (`true`/`false`) | `true` |
| `AUTH_DISABLED` | Frontend | Disable auth entirely, dev mode (`true`/`false`) | `false` |

### 2. Aidbox User setup

We use `data.groups` (not `data.roles`) because it maps naturally to **Active Directory / LDAP groups**. When Aidbox is connected to an external IdP (Azure AD, ADFS, Okta, etc.), AD group memberships are propagated into `data.groups` automatically — so adding a user to the AD group is enough, no manual Aidbox edits needed.

Add the role string to the `data.groups` array of the Aidbox User resource:

```json
{
  "resourceType": "User",
  "id": "my-user",
  "data": {
    "groups": [
      "SIT_EMPI_ADMIN_DEV"
    ]
  }
}
```

The value in `groups` must match `MPI_ADMIN_ROLE` exactly. If it doesn't match, the user is treated as a basic user.

> For Aidbox **Client** resources (service accounts), the check looks at `details.roles` instead of `data.groups`.

## What each role can see

| Feature | Admin | Basic user |
|---------|:-----:|:----------:|
| Patient search & details | Yes | Yes |
| Duplicate matching | Yes | Yes |
| Select matching model | Yes | No |
| Merges page | Yes | No |
| Non-duplicates page | Yes | No |
| Audit logs page | Yes | No |
| Unmerge operations | Yes | No |
| Aidbox Resource Browser link | Yes | Hidden |
| REST API (merge, unmerge, model CRUD, bulk match) | Yes | 403 Forbidden |

Basic users see only the **Patients** tab in the navigation. All other tabs are hidden.
 