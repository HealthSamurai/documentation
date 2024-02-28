---
description: This guide explains how security label access control can be enabled
---

# How to enable security labels access control

## Prerequisites

### Docker and Docker Compose

You should have Docker and Docker Compose installed before go further. To get it installed follow the [instructions](https://docs.docker.com/engine/install/).&#x20;

### Aidbox license

To get the Aidbox License:

1. Go the Aidbox user portal [https://aidbox.app](https://aidbox.app/)
2. Login to the portal
3. Create new **self-hosted** Aidbox License or use the license that you already have

## Create Aidbox project

To create sample project run command below.

```shell
git clone \
  --branch=without-zen \
  --depth=1 \
  https://github.com/Aidbox/aidbox-project-template.git \
  aidbox-project && \
  cd aidbox-project && \
  rm -rf .git
```

{% hint style="info" %}
See more details related the [running Aidbox locally](broken-reference)
{% endhint %}

### Apply the license

Populate the `.env` file with the Aidbox License.&#x20;

{% code title=".env" %}
```ini
AIDBOX_LICENSE=YOUR_AIDBOX_LICENSE_KEY
...
```
{% endcode %}

## Enable security labels access control

Populate the `.env` file with the security labels ENVs.&#x20;

{% code title=".env" %}
```ini
# if true, security label feature is enabled
BOX_FEATURES_SECURITY__LABELS_ENABLE=true

# if true, removes security labels from the resource
BOX_FEATURES_SECURITY__LABELS_STRIP__LABELS=true
...
```
{% endcode %}

## Start Aidbox with Docker Compose

To start Aidbox run the command in the `aidbox-project` directory.

```bash
docker compose up --force-recreate
```

When Aidbox starts, navigate to the [http://localhost:8888](http://localhost:8888/) and sign in to the Aidbox UI using the credentials `admin` / `password`.

## Ensure the security labels access control works

### Create TokenIntrospector

To make Aidbox trust `JWT` issued by external server token introspection is used.

```yaml
PUT /TokenIntrospector/security-labels-demo
content-type: text/yaml

resourceType: TokenIntrospector
id: security-labels-demo-client
type: jwt
jwt:
  iss: https://auth.example.com
  secret: secret
```

{% hint style="info" %}
Currently we use a common secret to make the introspector works. In production installations it's better to switch to `jwks_uri` instead.
{% endhint %}

### Create AccessPolicy

This access policy allows `FhirRead` and `FhirSearch` operations for requesters having JWT with `iss` claim value `https://auth.example.com`.

```yaml
PUT /AccessPolicy/as-security-labels-demo-client-do-read-search
content-type: text/yaml

resourceType: AccessPolicy
id: as-security-labels-demo-client-do-read-search
link:
- resourceType: Operation
  id: FhirRead
- resourceType: Operation
  id: FhirSearch
engine: matcho
matcho:
  jwt:
    iss: https://auth.example.com
```
