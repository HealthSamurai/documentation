---
description: Aidbox gives you ability to run pgagent
---

# Configure pgagent

Supported only on `aidboxdb:13.2`

To start pgagent specify [environmental variable](https://app.gitbook.com/@aidbox/s/project/~/drafts/-MgL2PuDexdL_3OW-Bpq/getting-started/installation/aidboxdb-image#optional-environment-variables) in deploy configuration `ENABLE_PGAGENT: "true”` Here's an example of k8s configuration:

```text
kind: Deployment
...
spec:
  containers:
      env:
        # Define the environment variable
        - name: ENABLE_PGAGENT 
          value: true
...
```

Pgagent runs with aidbox-master container and will restart after pod restarting.

When starting pod in k8s, pgagent runs as the standard user `postgres`. In order to start pgagent as a different user, you need to specify additional environment variables

```text
PGAGENT_USER:      "another user"
PGAGENT_PASSWORD:  "password"
```

An example of k8s config

```text
kind: Deployment
...
spec:
  containers:
      env:
        # Define the environment variable
        - name: ENABLE_PGAGENT 
          value: true
        - name: PGAGENT_USER 
          value: "another user" 
        - name: PGAGENT_PASSWORD 
          value: "password" #use secret
```

Now pgagent running as different user role in container.

If you use devbox, specify env variables in docker compose file

```text
version: '3.1'
services:
  db:
    image: "healthsamurai/aidboxdb"
    ports:
      - "5432:5432"
    volumes:
    - "./pgdata:/data"
    environment:
      POSTGRES_USER: "postgres"
      POSTGRES_PASSWORD: "postgres"
      POSTGRES_DB: "postgres"
      ENABLE_PGAGENT: "true"
```

Troubleshoot

> If job pgagent running on different user, please don't forget to grant access to new user to required tables.

