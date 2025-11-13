# Run Auditbox Locally

Get Auditbox running on your local machine in a few minutes using our pre-configured one-liner script.

## Prerequisites

{% hint style="warning" %}
<img src="../../.gitbook/assets/auditbox/docker.png" 
     alt="docker whale image" 
     data-size="original">

Please **make sure** that both [Docker & Docker Compose](https://docs.docker.com/engine/install/) are installed.
{% endhint %}

## Quick Start

### Step 1: Create a directory and run

```bash
mkdir auditbox && cd auditbox
curl -JO https://aidbox.app/runme/auditbox && docker compose up
```

Wait about 2 minutes for all services to load.

### Step 2: Sign in to Auditbox

Open **http://localhost:3000/** in your browser. You'll see the Auditbox login screen. 

<img src="../../.gitbook/assets/auditbox/ui_login.png"
     width="50%"
     alt="AuditBox login screen">

{% hint style="info" %}
Auditbox uses Keycloak as the identity provider, but you can configure any other IDP that supports OpenID Connect.
{% endhint %}

Click "Sign in with Keycloak" and you'll see the Keycloak login screen.

<img src="../../.gitbook/assets/auditbox/kc-enter.png"
     width="50%"
     alt="Keycloak login screen">

Use the following credentials to sign in:

- **Username or email**: `user`
- **Password**: `password`

Click "Sign In" and you will see the Auditbox UI.

<img src="../../.gitbook/assets/auditbox/ui_main.png"
     width="100%"
     alt="AuditBox UI screen">

## Discover Auditbox features

Use the Auditbox interface to explore audit event management and search capabilities. Test data is automatically loaded.

## What gets installed

The one-liner automatically sets up and runs four Docker services:

| Service | URL | Description |
|---------|-----|-------------|
| Auditbox | http://localhost:3000/ | Auditbox User Interface |
| API | http://localhost:3000/AuditEvent | FHIR REST API |
| Elasticsearch 8.17.0 | http://localhost:9204/ | Elasticsearch |
| Keycloak 26.0.6 | http://localhost:8888/ | Authorization admin |

## Stopping

```bash
# Stop services (data will be preserved)
docker compose down

# Stop and remove all data
docker compose down -v
```
