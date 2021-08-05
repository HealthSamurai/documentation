---
description: Aidbox gives you ability to run pgagent
---

# Configure pgagent

## Intro

In this tutorial, we will learn how to run [pgagent](https://www.pgadmin.org/docs/pgadmin4/development/pgagent.html) in[ aidboxdb](https://app.gitbook.com/@aidbox/s/project/~/drafts/-MgLXlXB3EoFwi0-IE3d/getting-started/installation/aidboxdb-image).

Supported only on `aidboxdb:13.2`

To start [pgagent](https://www.pgadmin.org/docs/pgadmin4/development/pgagent.html) specify [environmental variable](https://app.gitbook.com/@aidbox/s/project/~/drafts/-MgL2PuDexdL_3OW-Bpq/getting-started/installation/aidboxdb-image#optional-environment-variables) in deploy configuration `ENABLE_PGAGENT: "true”` Here's an example of [docker compose configuration](https://docs.docker.com/compose/environment-variables/):

```text
...
services:
  db:
    image: "healthsamurai/aidboxdb"
    environment:
      ENABLE_PGAGENT:    "true"
...

```

Pgagent runs with aidboxdb container and will restart after container restarting. By default pgagent runs as the standard user `postgres`. in order to run pgagent as a different user you need.

## Run as a different user

1. [Create new user in db](https://www.postgresql.org/docs/8.0/sql-createuser.html)
2. [Give the postgres user all the necessary permissions](https://www.postgresql.org/docs/9.1/sql-grant.html). 
3. Provide user password by [environment variables](https://docs.aidbox.app/getting-started/installation/aidboxdb-image#optional-environment-variables) `PGAGENT_USER` and `PGAGENT_PASSWORD`

### Example configurations

* Docker compose

```text
...
services:
  db:
    image: "healthsamurai/aidboxdb"
    environment:
      ENABLE_PGAGENT:    "true"
      PGAGENT_USER:      "postgres"
      PGAGENT_PASSWORD:  "postgres"

...

```

* Kubernetes deployment

```text
...
kind: Deployment
spec:
  containers:
    - name: db
      image: "healthsamurai/aidboxdb"
      env:
        # Define the environment variable
        - name: ENABLE_PGAGENT 
          value: "true"
        - name: PGAGENT_USER 
          value: "postgres"
        - name: PGAGENT_PASSWORD 
          value: "postgres"
                                     
```

> If your configuration in configmap and you don't change deployment, don't forget restart pod to apply new configuration

Now pgagent running as different user role in container!

## Debug

How to check that pgagent is working? We can verify this by connecting to the aidbox container. You can connect to container by[ docker CLI](https://docs.docker.com/engine/reference/commandline/exec/) or [kubectl if use use kubernetes](https://kubernetes.io/docs/tasks/debug-application-cluster/get-shell-running-container/).

By default the logs can be viewed in the file `/tmp/pgagent.log`

> You can override the path for logging and the level of logs using [environment variables](https://docs.aidbox.app/getting-started/installation/aidboxdb-image#optional-environment-variables)

```text
cat /tmp/pgagent.log
```

Check processes

```text
ps aux
```

You will see pgagent process and connection string with user, dbname, password.

{% page-ref page="working-wih-pgagent.md" %}











