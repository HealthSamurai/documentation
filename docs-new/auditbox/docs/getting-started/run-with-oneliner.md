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

Open **http://localhost:3000/** in your browser.

You'll see the Auditbox login screen. 

<img src="../../.gitbook/assets/auditbox/kc-enter.png" 
     width="50%"
     alt="Auditbox login screen">

Use the following credentials to sign in:

- **Username or email**: `user`
- **Password**: `password`

Click "Sign In" to access the Auditbox UI.

## What gets installed

The one-liner automatically sets up and runs five Docker services:

| Service | URL | Description |
|---------|-----|-------------|
| Auditbox | http://localhost:3000/ | Auditbox User Interface |
| API | http://localhost:3000/AuditEvent | FHIR REST API |
| Elasticsearch | http://localhost:9204/ | Elasticsearch |
| Keycloak | http://localhost:8888/ | Authorization admin |
| Kibana | http://localhost:5602/ | Analytics and visualization UI |

## Discover Auditbox features

Use the Auditbox interface to explore audit event management and search capabilities. Test data is automatically loaded, so you can start exploring right away.

## Stopping

```bash
# Stop services (data will be preserved)
docker compose down

# Stop and remove all data
docker compose down -v
```
