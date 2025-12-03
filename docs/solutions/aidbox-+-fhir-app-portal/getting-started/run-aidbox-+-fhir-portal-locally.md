---
description: This tutorial will guide you through Aidbox + FHIR App Portal installation
---

# Run Aidbox + FHIR Portal locally

TODO add to each header link like this: ### Aidbox license

## Run Aidbox + FHIR Portal locally

### Prerequisites

#### Required Software

* **Docker** and **Docker Compose** - [Install Docker](https://docs.docker.com/get-docker/)
* **Docker Compose** v2.0 or later

#### Required Licenses

You need two Aidbox licenses you can get from the [Aidbox User Portal](https://aidbox.app):

1. **Admin Aidbox License** - For the admin/patient management system
2. **Developer Aidbox License** - For the developer sandbox environment

It is a JWT token that looks like:

```
eyJhbGciOiJ...
```

### **Required: Email Provider Configuration**

{% hint style="info" %}
In this guide `mailgun` is used to send email. FHIR App Portal also supports [different email providers](../../../modules/smartbox/how-to-guides/setup-email-provider.md) and [SMTP](../../../modules/smartbox/how-to-guides/setup-email-provider.md#how-to-set-up-smtp)
{% endhint %}

Email provider is used to communicate with users (developers, patients). It sends emails for email verification, resetting of a password and etc.

### Quick Start

#### **Step 1: Create Project Directory**

```bash
mkdir fhir-app-portal
cd fhir-app-portal
```

### **Step 2: Create docker-compose.yaml**

Create a `docker-compose.yaml` file and paste the following content:

<details>

<summary>Click to view docker-compose.yaml</summary>

```yaml
version: "3.7"

volumes:
  postgres_data: {}

services:
  postgres:
    image: postgres:17
    restart: unless-stopped
    volumes:
      - postgres_data:/var/lib/postgresql/data:delegated
    command:
      - postgres
      - -c
      - shared_preload_libraries=pg_stat_statements
    environment:
      POSTGRES_USER: ${POSTGRES_USER:-aidbox}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD:-password}
      POSTGRES_DB: ${POSTGRES_DB:-aidbox}
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U ${POSTGRES_USER:-aidbox} -d postgres"]
      interval: 10s
      timeout: 5s
      retries: 5

  aidbox-admin:
    image: healthsamurai/aidboxone:edge
    pull_policy: always
    restart: unless-stopped
    depends_on:
      postgres:
        condition: service_healthy
    ports:
      - "${AIDBOX_ADMIN_PORT:-8080}:8080"
    volumes:
      - ./initBundleAdmin.json:/tmp/initBundle.json:ro
    environment:
      BOX_ADMIN_PASSWORD: ${AIDBOX_ADMIN_PASSWORD:-password}
      BOX_ROOT_CLIENT_SECRET: ${ADMIN_CLIENT_SECRET:-secret}
      BOX_DB_HOST: postgres
      BOX_DB_PORT: "5432"
      BOX_DB_USER: ${POSTGRES_USER:-aidbox}
      BOX_DB_PASSWORD: ${POSTGRES_PASSWORD:-password}
      BOX_DB_DATABASE: ${ADMIN_DB_NAME:-aidbox-admin}
      BOX_WEB_PORT: 8080
      BOX_WEB_BASE_URL: ${ADMIN_BASE_URL:-http://localhost:8080}
      BOX_FHIR_COMPLIANT_MODE: "true"
      BOX_FHIR_CORRECT_AIDBOX_FORMAT: "true"
      BOX_FHIR_SCHEMA_VALIDATION: "true"
      BOX_FHIR_SEARCH_AUTHORIZE_INLINE_REQUESTS: "true"
      BOX_FHIR_SEARCH_CHAIN_SUBSELECT: "true"
      BOX_FHIR_SEARCH_COMPARISONS: "true"
      BOX_FHIR_CREATEDAT_URL: https://aidbox.app/ex/createdAt
      BOX_SEARCH_INCLUDE_CONFORMANT: "true"
      BOX_BOOTSTRAP_FHIR_PACKAGES: "hl7.fhir.r4.core#4.0.1:hl7.fhir.us.core#6.1.0"
      BOX_FHIR_TERMINOLOGY_ENGINE: hybrid
      BOX_FHIR_TERMINOLOGY_ENGINE_HYBRID_EXTERNAL_TX_SERVER: https://tx.health-samurai.io/fhir
      BOX_FHIR_TERMINOLOGY_SERVICE_BASE_URL: https://tx.health-samurai.io/fhir
      BOX_LICENSE: ${AIDBOX_ADMIN_LICENSE}
      BOX_SECURITY_AUDIT_LOG_ENABLED: "true"
      BOX_SECURITY_DEV_MODE: ${DEV_MODE:-true}
      BOX_SECURITY_ORGBAC_ENABLED: "true"
      BOX_SECURITY_AUTH_KEYS_SECRET: ${AUTH_KEYS_SECRET:-change-this-secret}
      BOX_MODULE_SDC_STRICT_ACCESS_CONTROL: "true"
      BOX_SETTINGS_MODE: read-write
      BOX_INIT_BUNDLE: "file:///tmp/initBundle.json"
      BOX_MODULE_PROVIDER_DEFAULT_TYPE: ${EMAIL_PROVIDER_TYPE:-mailgun}
      BOX_MODULE_PROVIDER_DEFAULT_URL: ${EMAIL_PROVIDER_URL}
      BOX_MODULE_PROVIDER_DEFAULT_USERNAME: ${EMAIL_PROVIDER_USERNAME:-api}
      BOX_MODULE_PROVIDER_DEFAULT_FROM: ${EMAIL_FROM:-noreply@example.com}
      BOX_MODULE_PROVIDER_DEFAULT_PASSWORD: ${EMAIL_PROVIDER_PASSWORD}
      BOX_COMPATIBILITY_VALIDATION_JSON__SCHEMA_REGEX: "#{:fhir-datetime}"
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/health"]
      interval: 10s
      timeout: 5s
      retries: 30
      start_period: 60s
  aidbox-dev:
    image: healthsamurai/aidboxone:edge
    pull_policy: always
    restart: unless-stopped
    depends_on:
      postgres:
        condition: service_healthy
    ports:
      - "${AIDBOX_DEV_PORT:-8090}:8090"
    volumes:
      - ./initBundleDeveloper.json:/tmp/initBundle.json:ro
    environment:
      BOX_ADMIN_PASSWORD: ${AIDBOX_ADMIN_PASSWORD:-password}
      BOX_ROOT_CLIENT_SECRET: ${DEV_CLIENT_SECRET:-secret}
      BOX_DB_HOST: postgres
      BOX_DB_PORT: "5432"
      BOX_DB_USER: ${POSTGRES_USER:-aidbox}
      BOX_DB_PASSWORD: ${POSTGRES_PASSWORD:-changeme}
      BOX_DB_DATABASE: ${DEV_DB_NAME:-aidbox-dev}
      BOX_WEB_PORT: 8090
      BOX_WEB_BASE_URL: ${DEV_BASE_URL:-http://localhost:8090}
      BOX_FHIR_COMPLIANT_MODE: "true"
      BOX_FHIR_CORRECT_AIDBOX_FORMAT: "true"
      BOX_FHIR_SCHEMA_VALIDATION: "true"
      BOX_FHIR_SEARCH_AUTHORIZE_INLINE_REQUESTS: "true"
      BOX_FHIR_SEARCH_CHAIN_SUBSELECT: "true"
      BOX_FHIR_SEARCH_COMPARISONS: "true"
      BOX_FHIR_CREATEDAT_URL: https://aidbox.app/ex/createdAt
      BOX_SEARCH_INCLUDE_CONFORMANT: "true"
      BOX_BOOTSTRAP_FHIR_PACKAGES: "hl7.fhir.r4.core#4.0.1:hl7.fhir.us.core#6.1.0"
      BOX_FHIR_TERMINOLOGY_ENGINE: hybrid
      BOX_FHIR_TERMINOLOGY_ENGINE_HYBRID_EXTERNAL_TX_SERVER: https://tx.health-samurai.io/fhir
      BOX_FHIR_TERMINOLOGY_SERVICE_BASE_URL: https://tx.health-samurai.io/fhir
      BOX_LICENSE: ${AIDBOX_DEV_LICENSE}
      BOX_SECURITY_AUDIT_LOG_ENABLED: "true"
      BOX_SECURITY_DEV_MODE: ${DEV_MODE:-true}
      BOX_MODULE_SDC_STRICT_ACCESS_CONTROL: "true"
      BOX_SETTINGS_MODE: read-write
      BOX_INIT_BUNDLE: "file:///tmp/initBundle.json"
      BOX_MODULE_PROVIDER_DEFAULT_URL: ${EMAIL_PROVIDER_URL}
      BOX_MODULE_PROVIDER_DEFAULT_USERNAME: ${EMAIL_PROVIDER_USERNAME:-api}
      BOX_MODULE_PROVIDER_DEFAULT_FROM: ${EMAIL_FROM:-noreply@example.com}
      BOX_MODULE_PROVIDER_DEFAULT_PASSWORD: ${EMAIL_PROVIDER_PASSWORD}
      BOX_COMPATIBILITY_VALIDATION_JSON__SCHEMA_REGEX: "#{:fhir-datetime}"
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8090/health"]
      interval: 10s
      timeout: 5s
      retries: 30
      start_period: 60s

  fhir-app-portal:
    image: healthsamurai/fhir-app-portal:edge
    pull_policy: always
    restart: unless-stopped
    depends_on:
      aidbox-admin:
        condition: service_healthy
      aidbox-dev:
        condition: service_healthy
    ports:
      - "${ADMIN_PORTAL_PORT:-8095}:8095"
      - "${DEV_PORTAL_PORT:-8096}:8096"
    environment:
      NODE_ENV: production
      PORTAL_BACKEND_PORT: ${PORTAL_BACKEND_PORT:-8081}
      SESSION_SECRET: ${SESSION_SECRET:-changeme-secure-session-secret}
      DEPLOYMENT_MODE: prod
      AIDBOX_DEV_URL: ${AIDBOX_DEV_URL:-http://aidbox-dev:8090}
      AIDBOX_ADMIN_URL: ${AIDBOX_ADMIN_URL:-http://aidbox-admin:8080}
      ADMIN_AIDBOX_PUBLIC_URL: ${ADMIN_AIDBOX_PUBLIC_URL:-http://localhost:8080}
      DEVELOPER_AIDBOX_PUBLIC_URL: ${DEVELOPER_AIDBOX_PUBLIC_URL:-http://localhost:8090}
      ADMIN_FRONTEND_URL: ${ADMIN_FRONTEND_URL:-http://localhost:8095}
      DEVELOPER_FRONTEND_URL: ${DEVELOPER_FRONTEND_URL:-http://localhost:8096}
      AIDBOX_DEV_PUBLIC_URL: ${AIDBOX_DEV_PUBLIC_URL:-http://localhost:8090}
      AIDBOX_ADMIN_PUBLIC_URL: ${AIDBOX_ADMIN_PUBLIC_URL:-http://localhost:8080}
      CORS_ORIGINS: ${CORS_ORIGINS:-http://localhost:8095,http://localhost:8096}
    healthcheck:
      test:
        [
          "CMD",
          "wget",
          "--quiet",
          "--tries=1",
          "--spider",
          "http://localhost:8095/health",
        ]
      interval: 10s
      timeout: 5s
      retries: 5
      start_period: 10s
```

</details>

### **Step 3: Create .env file**

Create `.env` file in the same folder alongside with `docker-compose.yaml` and copy environment variables:

<details>

<summary>Click to view .env file</summary>

```shell
# -----------------------------------------------------------------------------
# Licenses
# -----------------------------------------------------------------------------
# If license isn't provided then developer license will be applied automatically
# If you want apply another licence simply uncomment variable and set it
# AIDBOX_ADMIN_LICENSE=<YOUR-ADMIN-LICENCE-JWT-TOKEN>
# AIDBOX_DEV_LICENSE=<YOUR-SANDBOX-LICENCE-JWT-TOKEN>

# -----------------------------------------------------------------------------
# PostgreSQL Database Configuration
# -----------------------------------------------------------------------------
POSTGRES_USER=aidbox
POSTGRES_PASSWORD=password
POSTGRES_DB=aidbox

# Database names
ADMIN_DB_NAME=aidbox-admin
DEV_DB_NAME=aidbox-dev

# -----------------------------------------------------------------------------
# Admin Portal Configuration
# -----------------------------------------------------------------------------
AIDBOX_ADMIN_PORT=8080
ADMIN_BASE_URL=http://localhost:8080

# Admin credentials (used to login to Aidbox UI)
AIDBOX_ADMIN_PASSWORD=secure-admin-password
ADMIN_CLIENT_SECRET=secure-admin-client-secret

# -----------------------------------------------------------------------------
# Developer Sandbox Configuration
# -----------------------------------------------------------------------------
AIDBOX_DEV_PORT=8090
DEV_BASE_URL=http://localhost:8090

# Developer sandbox client secret
DEV_CLIENT_SECRET=secure-developer-client-secret

# -----------------------------------------------------------------------------
# FHIR App Portal Configuration
# -----------------------------------------------------------------------------
# Admin portal port
ADMIN_PORTAL_PORT=8095

# Developer portal port  
DEV_PORTAL_PORT=8096

# Backend API port
PORTAL_BACKEND_PORT=8081

# Aidbox internal URLs (for backend-to-Aidbox API calls)
AIDBOX_DEV_URL=http://aidbox-dev:8090
AIDBOX_ADMIN_URL=http://aidbox-admin:8080

# Aidbox public URLs (for browser redirects - accessible from user's machine)
AIDBOX_DEV_PUBLIC_URL=http://localhost:8090
AIDBOX_ADMIN_PUBLIC_URL=http://localhost:8080

ADMIN_AIDBOX_PUBLIC_URL=http://localhost:8080
DEVELOPER_AIDBOX_PUBLIC_URL=http://localhost:8090

# Aidbox UI URLs
ADMIN_FRONTEND_URL=http://localhost:8095
DEVELOPER_FRONTEND_URL=http://localhost:8096

# -----------------------------------------------------------------------------
# Email Provider Configuration (Mailgun Example)
# -----------------------------------------------------------------------------

EMAIL_PROVIDER_TYPE=your-email-provider
EMAIL_PROVIDER_URL=your-email-url
EMAIL_PROVIDER_USERNAME=your-email-username
EMAIL_PROVIDER_PASSWORD=your-email-password
EMAIL_FROM=your-email-from

# -----------------------------------------------------------------------------
# Security Configuration
# -----------------------------------------------------------------------------
# Change this to a secure random string for production
AUTH_KEYS_SECRET=change-this-secret
SESSION_SECRET=changeme-secure-session-secret

# Development mode (set to false for production)
DEV_MODE=true

# -----------------------------------------------------------------------------
# Cloud Storage Configuration (Optional - for Bulk Export)
# -----------------------------------------------------------------------------
# Uncomment and configure one of the following based on your cloud provider:

# --- Google Cloud Storage (GCP) ---
# BOX_BULK__STORAGE_BACKEND=gcp
# BOX_BULK__STORAGE_GCP_SERVICE__ACCOUNT=gcp-service-account-name
# BOX_BULK__STORAGE_GCP_SERVICE__ACCOUNT__EMAIL=your-service-account@project.iam.gserviceaccount.com
# BOX_BULK__STORAGE_GCP_SERVICE__ACCOUNT__PRIVATE__KEY=your-private-key-here
# BOX_BULK__STORAGE_GCP_BUCKET=your-gcp-bucket-name

# --- Amazon Web Services (AWS S3) ---
# BOX_BULK__STORAGE_BACKEND=aws
# BOX_BULK__STORAGE_AWS_ACCESS__KEY__ID=your-aws-access-key
# BOX_BULK__STORAGE_AWS_SECRET__ACCESS__KEY=your-aws-secret-key
# BOX_BULK__STORAGE_AWS_REGION=us-east-1
# BOX_BULK__STORAGE_AWS_BUCKET=your-s3-bucket-name

# --- Microsoft Azure Blob Storage ---
# BOX_BULK__STORAGE_BACKEND=azure
# BOX_BULK__STORAGE_AZURE_ACCOUNT__NAME=your-storage-account
# BOX_BULK__STORAGE_AZURE_ACCOUNT__KEY=your-account-key
# BOX_BULK__STORAGE_AZURE_CONTAINER=your-container-name
```

</details>

### **Step 4: Create initBundleDeveloper.json for Sandbox Aidbox**

Create `initBundleDeveloper.json` file in the same folder alongside with `docker-compose.yaml` and copy the content:

<details>

<summary>Click to view Sandbox Init Bundle file</summary>

```json
{
  "resourceType": "Bundle",
  "type": "transaction",
  "entry": [
    {
      "resource": {
        "resourceType": "FHIRSchema",
        "url": "http://aidbox.app/StructureDefinition/Client/created-by",
        "id": "client-created-by",
        "base": "Extension",
        "name": "client-created-by",
        "kind": "complex-type",
        "type": "Extension",
        "version": "0.0.1",
        "elements": {
          "url": {
            "fixed": "http://aidbox.app/StructureDefinition/Client/created-by"
          },
          "value": {
            "choices": ["valueReference"]
          },
          "valueReference": {
            "type": "Reference",
            "refers": ["User"],
            "choiceOf": "value"
          }
        },
        "derivation": "constraint"
      },
      "request": {
        "method": "PUT",
        "url": "FHIRSchema/client-created-by"
      }
    },
    {
      "resource": {
        "resourceType": "SearchParameter",
        "id": "Client.created-by",
        "url": "http://aidbox.app/StructureDefinition/Client/created-by",
        "name": "created-by",
        "status": "active",
        "code": "created-by",
        "base": ["Client"],
        "type": "token",
        "description": "Client created-by User",
        "expression": "Client.meta.extension.where(url='http://aidbox.app/StructureDefinition/Client/created-by').value.Reference.id"
      },
      "request": {
        "method": "PUT",
        "url": "SearchParameter/Client.created-by"
      }
    },
    {
      "resource": {
        "resourceType": "FHIRSchema",
        "url": "http://aidbox.app/StructureDefinition/Client/status",
        "id": "client-status",
        "base": "Extension",
        "name": "client-status",
        "kind": "complex-type",
        "type": "Extension",
        "version": "0.0.2",
        "elements": {
          "url": {
            "fixed": "http://aidbox.app/StructureDefinition/Client/status"
          },
          "value": {
            "choices": ["valueCode"]
          },
          "valueCode": {
            "type": "code",
            "choiceOf": "value",
            "constraints": {
              "enum-client-status": {
                "severity": "error",
                "expression": "%context.subsetOf('draft' | 'review' | 'active' | 'rejected')"
              }
            }
          }
        },
        "derivation": "constraint"
      },
      "request": {
        "method": "PUT",
        "url": "FHIRSchema/client-status"
      }
    },
    {
      "resource": {
        "resourceType": "SearchParameter",
        "id": "Client.status",
        "url": "http://aidbox.app/StructureDefinition/Client/status",
        "name": "status",
        "status": "active",
        "code": "status",
        "base": ["Client"],
        "type": "token",
        "description": "Client status (draft, review, active, rejected)",
        "expression": "Client.meta.extension.where(url='http://aidbox.app/StructureDefinition/Client/status').value.code"
      },
      "request": {
        "method": "PUT",
        "url": "SearchParameter/Client.status"
      }
    },
    {
      "resource": {
        "source": "https://storage.googleapis.com/aidbox-public/smartbox/all_data.ndjson.gz"
      },
      "request": {
        "method": "POST",
        "url": "/$load"
      }
    },
    {
      "resource": {
        "resourceType": "Client",
        "id": "smartbox-developer-portal",
        "first_party": true,
        "grant_types": ["code"],
        "scope": ["openid", "profile", "email"],
        "auth": {
          "authorization_code": {
            "redirect_uri": "http://localhost:8096/api/developer/auth/callback",
            "access_token_expiration": 3600,
            "token_format": "jwt",
            "pkce": true,
            "refresh_token": true,
            "refresh_token_expiration": 86400
          }
        }
      },
      "request": {
        "method": "PUT",
        "url": "Client/smartbox-developer-portal"
      }
    },
    {
      "resource": {
        "fhirUser": {
          "reference": "Patient/test-pt-1"
        }
      },
      "request": {
        "method": "PATCH",
        "url": "User/admin"
      }
    },
    {
      "request": {
        "method": "PUT",
        "url": "AccessPolicy/dev-can-get-launch-uri"
      },
      "resource": {
        "resourceType": "AccessPolicy",
        "id": "dev-can-get-launch-uri",
        "type": "rpc",
        "engine": "matcho-rpc",
        "rpc": {
          "aidbox.smart/get-launch-uri": {
            "user": {
              "fhirUser": {
                "resourceType": "Patient"
              }
            }
          }
        }
      }
    },
    {
      "request": {
        "method": "PUT",
        "url": "AccessPolicy/dev-smart-app-read"
      },
      "resource": {
        "resourceType": "AccessPolicy",
        "id": "dev-smart-app-read",
        "type": "rest",
        "engine": "matcho",
        "matcho": {
          "client": {
            "type": "smart-app"
          },
          "operation": {
            "id": {
              "$one-of": ["FhirSearch", "FhirRead"]
            }
          }
        }
      }
    },
    {
      "resource": {
        "resourceType": "Client",
        "id": "developer-api",
        "secret": "developer-api-secret-change-in-production",
        "grant_types": ["client_credentials"]
      },
      "request": {
        "method": "PUT",
        "url": "Client/developer-api"
      }
    },
    {
      "resource": {
        "resourceType": "AccessPolicy",
        "id": "developer-api",
        "engine": "matcho",
        "description": "Scoped access for developer API client (sessions, oauth state, orgs, users)",
        "matcho": {
          "client": {
            "id": "developer-api"
          },
          "$one-of": [
            {
              "params": {
                "resource/type": "Session"
              },
              "operation": {
                "id": {
                  "$one-of": [
                    "FhirCreate",
                    "FhirSearch",
                    "FhirRead",
                    "FhirUpdate",
                    "FhirPatch"
                  ]
                }
              }
            },
            {
              "params": {
                "resource/type": "Organization"
              },
              "operation": {
                "id": {
                  "$one-of": ["FhirSearch", "FhirRead"]
                }
              }
            },
            {
              "params": {
                "resource/type": "User"
              },
              "operation": {
                "id": "FhirRead"
              }
            }
          ]
        }
      },
      "request": {
        "method": "PUT",
        "url": "AccessPolicy/developer-api"
      }
    },
    {
      "request": {
        "method": "PUT",
        "url": "AccessPolicy/dev-client-search-own"
      },
      "resource": {
        "resourceType": "AccessPolicy",
        "id": "dev-client-search-own",
        "engine": "matcho",
        "description": "Developers can search only their own Client apps",
        "matcho": {
          "user": {
            "roles": {
              "$contains": {
                "type": "developer"
              }
            }
          },
          "operation": {
            "id": "FhirSearch"
          },
          "params": {
            "resource/type": "Client",
            "created-by": ".user.id"
          }
        }
      }
    },
    {
      "request": {
        "method": "PUT",
        "url": "AccessPolicy/dev-client-read-write-own"
      },
      "resource": {
        "resourceType": "AccessPolicy",
        "id": "dev-client-read-write-own",
        "engine": "sql",
        "sql": {
          "query": "select true from client where id = {{params.resource/id}}\n  and exists (\n  select 1\n  from jsonb_array_elements(resource->'meta'->'extension') ext\nwhere ext->>'url' = 'http://aidbox.app/StructureDefinition/Client/created-by'\n  and ext #>> '{value,Reference,id}' = {{user.id}});"
        }
      }
    },
    {
      "request": {
        "method": "PUT",
        "url": "AccessPolicy/dev-client-create-self-owned"
      },
      "resource": {
        "resourceType": "AccessPolicy",
        "id": "dev-client-create-self-owned",
        "engine": "matcho",
        "description": "Developers can create Client apps only when created-by points to themselves",
        "matcho": {
          "user": {
            "roles": {
              "$contains": {
                "type": "developer"
              }
            }
          },
          "operation": {
            "id": "FhirCreate"
          },
          "params": {
            "resource/type": "Client"
          },
          "resource": {
            "meta": {
              "extension": {
                "$contains": {
                  "url": "http://aidbox.app/StructureDefinition/Client/created-by",
                  "value": {
                    "Reference": {
                      "id": ".user.id"
                    }
                  }
                }
              }
            }
          }
        }
      }
    },
    {
      "request": {
        "method": "PUT",
        "url": "AccessPolicy/developer-user-read-self"
      },
      "resource": {
        "resourceType": "AccessPolicy",
        "id": "developer-user-read-self",
        "engine": "matcho",
        "description": "Developers can read only their own User resource",
        "matcho": {
          "user": {
            "roles": {
              "$contains": {
                "type": "developer"
              }
            }
          },
          "operation": {
            "id": "FhirRead"
          },
          "params": {
            "resource/type": "User",
            "resource/id": ".user.id"
          }
        }
      }
    },
    {
      "request": {
        "method": "PUT",
        "url": "AccessPolicy/admin-review-sandbox-clients"
      },
      "resource": {
        "resourceType": "AccessPolicy",
        "id": "admin-review-sandbox-clients",
        "engine": "matcho",
        "description": "Admins can review sandbox Clients (read/search only)",
        "matcho": {
          "user": {
            "roles": {
              "$contains": {
                "type": "admin"
              }
            }
          },
          "params": {
            "resource/type": "Client"
          },
          "operation": {
            "id": {
              "$one-of": ["FhirSearch", "FhirRead"]
            }
          }
        }
      }
    },
    {
      "request": {
        "method": "PUT",
        "url": "AccessPolicy/admin-role-access"
      },
      "resource": {
        "resourceType": "AccessPolicy",
        "id": "admin-role-access",
        "description": "Role-based access policy for administrators - allows full system access",
        "engine": "matcho",
        "matcho": {
          "user": {
            "roles": {
              "contains": {
                "type": "admin"
              }
            }
          }
        }
      }
    },
    {
      "request": {
        "method": "PUT",
        "url": "AccessPolicy/patient-role-access"
      },
      "resource": {
        "resourceType": "AccessPolicy",
        "id": "patient-role-access",
        "description": "Role-based access policy for patients - allows access to own data and consent management",
        "engine": "matcho",
        "matcho": {
          "user": {
            "roles": {
              "contains": {
                "type": "patient"
              }
            }
          },
          "operation": {
            "id": {
              "$one-of": [
                "FhirSearch",
                "FhirRead",
                "FhirPatch",
                "FhirCreate",
                "FhirUpdate"
              ]
            }
          },
          "$or": [
            {
              "params": {
                "resource/type": "Patient"
              },
              "operation": {
                "id": {
                  "$one-of": ["FhirRead"]
                }
              }
            },
            {
              "params": {
                "resource/type": "Consent"
              },
              "operation": {
                "id": {
                  "$one-of": [
                    "FhirSearch",
                    "FhirRead",
                    "FhirPatch",
                    "FhirCreate",
                    "FhirUpdate"
                  ]
                }
              }
            }
          ]
        }
      }
    },
    {
      "request": {
        "method": "PUT",
        "url": "NotificationTemplate/auth-signup-email"
      },
      "resource": {
        "resourceType": "NotificationTemplate",
        "template": "\n<h3>Confirm your email address </h3>\n<p>Hello {{params.data.name}}{{params.email}}{{params.username}}!</p>\n<p>We just need to verify that {{params.email}} {{params.username}} is your email address.</p>\n<p><a target=\"_blank\" href=\"{{params.base-url}}/auth/signup/confirm/{{id}}?redirect_to=aHR0cDovL2xvY2FsaG9zdDo4MDgxL2RldmVsb3Blci9hdXRoL3NpZ251cD9jb25maXJtUGFzc3dvcmQ9dHJ1ZQ==\">Confirm Email Address</a></p> \n<b>Didn't request this email?</b>\n<p>No worries! Your address may have been entered by mistake. If you ignore or delete this email, nothing further will happen.</p>\n<p>If you're having problems, please feel free to write to us at hello@health-samurai.io. We'll be glad to help.</p> "
      }
    },
    {
      "request": {
        "method": "PUT",
        "url": "TokenIntrospector/admin-aidbox"
      },
      "resource": {
        "resourceType": "TokenIntrospector",
        "id": "admin-aidbox",
        "type": "jwt",
        "jwt": {
          "iss": "http://localhost:8080"
        },
        "jwks_uri": "http://aidbox-admin:8080/.well-known/jwks.json"
      }
    },
    {
      "request": {
        "method": "PUT",
        "url": "AccessPolicy/allow-admin-tokens"
      },
      "resource": {
        "resourceType": "AccessPolicy",
        "id": "allow-admin-tokens",
        "description": "Allow requests authenticated with Admin Aidbox JWT tokens",
        "engine": "json-schema",
        "schema": {
          "required": ["jwt"],
          "properties": {
            "jwt": {
              "required": ["iss"],
              "properties": {
                "iss": {
                  "constant": "http://localhost:8080"
                }
              }
            }
          }
        }
      }
    },
    {
      "request": {
        "method": "PUT",
        "url": "AccessPolicy/dev-client-search-communication"
      },
      "resource": {
        "engine": "matcho",
        "matcho": {
          "user": {
            "roles": {
              "$contains": {
                "type": "developer"
              }
            }
          },
          "params": {
            "resource/type": "Communication",
            ".about.0.reference": "present?"
          },
          "operation": {
            "id": "FhirSearch"
          }
        },
        "description": "Developers can search communication for sertain app",
        "id": "dev-client-search-communication",
        "resourceType": "AccessPolicy"
      }
    },
    {
      "request": {
        "method": "PUT",
        "url": "AccessPolicy/smart-app-can-read-patient-api"
      },
      "resource": {
        "resourceType": "AccessPolicy",
        "id": "smart-app-can-read-patient-api",
        "engine": "matcho",
        "matcho": {
          "jwt": {
            "atv": 2,
            "scope": "present?",
            "context": {
              "patient": "present?"
            }
          },
          "client": {
            "type": "smart-app",
            "active": true
          }
        }
      }
    }
  ]
}
```

</details>

### **Step 5: Create Init Bundle for Admin Aidbox**

Create `initBundleAdmin.json` file in the same folder alongside with `docker-compose.yaml` and copy the content:

<details>

<summary>Click to view Admin Init Bundle file</summary>

```json
{
  "resourceType": "Bundle",
  "type": "transaction",
  "entry": [
    {
      "resource": {
        "resourceType": "FHIRSchema",
        "url": "http://aidbox.app/StructureDefinition/Client/created-by",
        "id": "client-created-by",
        "base": "Extension",
        "name": "client-created-by",
        "kind": "complex-type",
        "type": "Extension",
        "version": "0.0.1",
        "elements": {
          "url": {
            "fixed": "http://aidbox.app/StructureDefinition/Client/created-by"
          },
          "value": {
            "choices": ["valueReference"]
          },
          "valueReference": {
            "type": "Reference",
            "refers": ["User"],
            "choiceOf": "value"
          }
        },
        "derivation": "constraint"
      },
      "request": {
        "method": "PUT",
        "url": "FHIRSchema/client-created-by"
      }
    },
    {
      "resource": {
        "resourceType": "SearchParameter",
        "id": "Client.created-by",
        "url": "http://aidbox.app/StructureDefinition/Client/created-by",
        "name": "created-by",
        "status": "active",
        "code": "created-by",
        "base": ["Client"],
        "type": "token",
        "description": "Client created-by User",
        "expression": "Client.meta.extension.where(url='http://aidbox.app/StructureDefinition/Client/created-by').value.Reference.id"
      },
      "request": {
        "method": "PUT",
        "url": "SearchParameter/Client.created-by"
      }
    },
    {
      "resource": {
        "resourceType": "FHIRSchema",
        "url": "http://aidbox.app/StructureDefinition/Client/status",
        "id": "client-status",
        "base": "Extension",
        "name": "client-status",
        "kind": "complex-type",
        "type": "Extension",
        "version": "0.0.2",
        "elements": {
          "url": {
            "fixed": "http://aidbox.app/StructureDefinition/Client/status"
          },
          "value": {
            "choices": ["valueCode"]
          },
          "valueCode": {
            "type": "code",
            "choiceOf": "value",
            "constraints": {
              "enum-client-status": {
                "severity": "error",
                "expression": "%context.subsetOf('draft' | 'review' | 'active' | 'rejected')"
              }
            }
          }
        },
        "derivation": "constraint"
      },
      "request": {
        "method": "PUT",
        "url": "FHIRSchema/client-status"
      }
    },
    {
      "resource": {
        "resourceType": "SearchParameter",
        "id": "Client.status",
        "url": "http://aidbox.app/StructureDefinition/Client/status",
        "name": "status",
        "status": "active",
        "code": "status",
        "base": ["Client"],
        "type": "token",
        "description": "Client status (draft, review, active, rejected)",
        "expression": "Client.meta.extension.where(url='http://aidbox.app/StructureDefinition/Client/status').value.code"
      },
      "request": {
        "method": "PUT",
        "url": "SearchParameter/Client.status"
      }
    },
    {
      "resource": {
        "resourceType": "Client",
        "id": "smartbox-admin-portal",
        "first_party": true,
        "grant_types": ["code"],
        "scope": ["openid", "profile", "email"],
        "auth": {
          "authorization_code": {
            "redirect_uri": "http://localhost:8095/api/admin/auth/callback",
            "access_token_expiration": 3600,
            "token_format": "jwt",
            "pkce": true,
            "refresh_token": true,
            "refresh_token_expiration": 86400
          }
        }
      },
      "request": {
        "method": "PUT",
        "url": "Client/smartbox-admin-portal"
      }
    },
    {
      "resource": {
        "resourceType": "Client",
        "id": "admin-api",
        "secret": "admin-api-secret-change-in-production",
        "grant_types": ["client_credentials"]
      },
      "request": {
        "method": "PUT",
        "url": "Client/admin-api"
      }
    },
    {
      "resource": {
        "resourceType": "AccessPolicy",
        "id": "admin-api",
        "engine": "matcho",
        "description": "Scoped access for admin API client (sessions, orgs, clients, users, tos/privacy)",
        "matcho": {
          "client": { "id": "admin-api" },
          "$one-of": [
            {
              "params": { "resource/type": "Session" },
              "operation": {
                "id": {
                  "$one-of": [
                    "FhirCreate",
                    "FhirSearch",
                    "FhirRead",
                    "FhirUpdate",
                    "FhirPatch"
                  ]
                }
              }
            },
            {
              "params": { "resource/type": "Organization" },
              "operation": { "id": { "$one-of": ["FhirSearch", "FhirRead"] } }
            },
            {
              "params": { "resource/type": "Client" },
              "operation": { "id": { "$one-of": ["FhirSearch", "FhirRead"] } }
            },
            {
              "params": { "resource/type": "User" },
              "operation": { "id": "FhirRead" }
            },
            {
              "params": {
                "resource/type": "DocumentReference",
                "resource/id": {
                  "$one-of": ["smartbox-tos", "smartbox-privacy"]
                }
              },
              "operation": {
                "id": { "$one-of": ["FhirRead", "FhirUpdate", "FhirCreate"] }
              }
            }
          ]
        }
      },
      "request": {
        "method": "PUT",
        "url": "AccessPolicy/admin-api"
      }
    },
    {
      "resource": {
        "resourceType": "AccessPolicy",
        "id": "allow-read-settings",
        "engine": "matcho",
        "description": "Allow admin-api client to read Aidbox settings",
        "matcho": {
          "client": { "id": "admin-api" },
          "uri": {
            "$one-of": ["/api/v1/settings/introspect", "^/api/v1/settings/.*"]
          }
        }
      },
      "request": {
        "method": "PUT",
        "url": "AccessPolicy/allow-read-settings"
      }
    },
    {
      "resource": {
        "resourceType": "SearchParameter",
        "id": "Communication.about",
        "url": "http://smartbox.hs/sp/Communication-about",
        "name": "communication-about",
        "status": "active",
        "code": "about",
        "base": ["Communication"],
        "type": "reference",
        "description": "Search Communication by Communication.about (any reference, incl. Aidbox system resources)",
        "expression": "Communication.about"
      },
      "request": {
        "method": "PUT",
        "url": "SearchParameter/Communication.about"
      }
    },
    {
      "resource": {
        "resourceType": "SearchParameter",
        "id": "Session.client",
        "url": "http://smartbox.hs/sp/Session-client",
        "name": "session-client",
        "status": "active",
        "code": "client",
        "base": ["Session"],
        "type": "reference",
        "description": "Search Session by client reference",
        "expression": "Session.client",
        "target": ["Client"]
      },
      "request": {
        "method": "PUT",
        "url": "SearchParameter/Session.client"
      }
    },
    {
      "resource": {
        "roles": [
          {
            "type": "admin"
          }
        ]
      },
      "request": {
        "method": "PATCH",
        "url": "User/admin"
      }
    },
    {
      "request": {
        "method": "PUT",
        "url": "AccessPolicy/patient-can-get-launch-uri"
      },
      "resource": {
        "resourceType": "AccessPolicy",
        "id": "patient-can-get-launch-uri",
        "type": "rpc",
        "engine": "matcho-rpc",
        "rpc": {
          "aidbox.smart/get-launch-uri": {
            "user": {
              "fhirUser": {
                "resourceType": "Patient"
              }
            }
          }
        }
      }
    },
    {
      "request": {
        "method": "PUT",
        "url": "AccessPolicy/smart-app-can-read-patient-api"
      },
      "resource": {
        "resourceType": "AccessPolicy",
        "id": "smart-app-can-read-patient-api",
        "engine": "matcho",
        "matcho": {
          "jwt": {
            "atv": 2,
            "scope": "present?",
            "context": {
              "patient": "present?"
            }
          },
          "client": {
            "type": "smart-app",
            "active": true
          }
        }
      }
    },
    {
      "resource": {
        "resourceType": "DocumentReference",
        "id": "smartbox-tos",
        "status": "current",
        "type": {
          "text": "Terms of Service"
        },
        "category": [
          {
            "text": "legal"
          }
        ],
        "content": [
          {
            "attachment": {
              "contentType": "text/html"
            }
          }
        ]
      },
      "request": {
        "method": "PUT",
        "url": "DocumentReference/smartbox-tos"
      }
    },
    {
      "resource": {
        "resourceType": "DocumentReference",
        "id": "smartbox-privacy",
        "status": "current",
        "type": {
          "text": "Privacy Policy"
        },
        "category": [
          {
            "text": "legal"
          }
        ],
        "content": [
          {
            "attachment": {
              "contentType": "text/html"
            }
          }
        ]
      },
      "request": {
        "method": "PUT",
        "url": "DocumentReference/smartbox-privacy"
      }
    },
    {
      "resource": {
        "resourceType": "FHIRSchema",
        "url": "http://smartbox.hs/StructureDefinition/AwsAccount",
        "id": "AwsAccount",
        "name": "AwsAccount",
        "type": "AwsAccount",
        "kind": "resource",
        "derivation": "specialization",
        "elements": {
          "region": {
            "type": "string"
          },
          "access-key-id": {
            "type": "string"
          },
          "secret-access-key": {
            "type": "string"
          }
        }
      },
      "request": {
        "method": "PUT",
        "url": "FHIRSchema/AwsAccount"
      }
    },
    {
      "resource": {
        "resourceType": "FHIRSchema",
        "url": "http://smartbox.hs/StructureDefinition/GcpServiceAccount",
        "id": "GcpServiceAccount",
        "name": "GcpServiceAccount",
        "type": "GcpServiceAccount",
        "kind": "resource",
        "derivation": "specialization",
        "elements": {
          "private-key": {
            "type": "string"
          },
          "service-account-email": {
            "type": "string"
          }
        }
      },
      "request": {
        "method": "PUT",
        "url": "FHIRSchema/GcpServiceAccount"
      }
    }
  ]
}
```

</details>

### **Step 6: Launch Aidbox**

Run the following command:

```shell
docker compose up
```

### **Step 7: How to access services**

Now Aidboxes and FHIR App Portal are ready. They're accessible by the following URLs:

* **Admin/Patient Portal** ([http://localhost:8095](http://localhost:8095)) - Manage users, review apps, monitor the system as admin and also access App Gallery as patient
* **Developer Sandbox Portal** ([http://localhost:8096](http://localhost:8096)) - Register and test SMART on FHIR applications
* **Admin/Patient Aidbox Instance** - ([http://admin.localhost:8080](http://admin.localhost:8080)) - Separate FHIR server for Admin/Patient environment

      For Sign in as admin use `admin` username and `AIDBOX_ADMIN_PASSWORD` password
* **Sanbox Aidbox Instance** - ([http://localhost:8090](http://localhost:8090)) - Separate FHIR server for Sandbox (Developer) environment

      For sign in as admin use `admin` username and `AIDBOX_ADMIN_PASSWORD` password

### **Step 8: Manually upload Bundle**

Open Sandbox Aidbox Instance at [http://localhost:8090/ui/console#/rest](http://localhost:8090/ui/console#/rest) and run the following command in REST Console:


<details>

<summary>Click to view Bundle</summary>

```json
POST /
content-type: application/json
accept: application/json

{
  "resourceType": "Bundle",
  "type": "transaction",
  "entry": [
    {
      "fullUrl": "AccessPolicy/dev-client-create-self-owned",
      "resource": {
        "resourceType": "AccessPolicy",
        "id": "dev-client-create-self-owned",
        "engine": "matcho",
        "description": "Developers can create Client apps only when created-by points to themselves",
        "matcho": {
          "user": { "roles": { "$contains": { "type": "developer" } } },
          "operation": { "id": "FhirCreate" },
          "params": { "resource/type": "Client" },
          "resource": {
            "meta": {
              "extension": {
                "$contains": {
                  "url": "http://aidbox.app/StructureDefinition/Client/created-by",
                  "value": {
                    "Reference": {
                      "id": ".user.id"
                    }
                  }
                }
              }
            }
          }
        }
      },
      "request": {
        "method": "PUT",
        "url": "AccessPolicy/dev-client-create-self-owned"
      }
    }
  ]
}
```

</details>