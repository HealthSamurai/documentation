---
description: Aidbox gives you ability to run pgagent
---

# Configure pgagent

## Intro

In this tutorial, we will learn how to run [pgagent](https://www.pgadmin.org/docs/pgadmin4/development/pgagent.html) in[ aidboxdb](https://app.gitbook.com/@aidbox/s/project/~/drafts/-MgLXlXB3EoFwi0-IE3d/getting-started/installation/aidboxdb-image). In order for the pg agent to run together with aidboxdb, you just need to specify an additional environment variable. 

To start pgagent set environmental variable `ENABLE_PGAGENT` .to `true`

Here's an example of [how to specify env variables](https://docs.docker.com/compose/environment-variables/) in docker compose file.

```text
...
services:
  db:
    image: "healthsamurai/aidboxdb"
    environment:
      ENABLE_PGAGENT:    "true"
...

```

{% hint style="info" %}
[You can see the full list of environment variables for aidboxdb here](https://docs.aidbox.app/getting-started/installation/aidboxdb-image#optional-environment-variables)
{% endhint %}

Pgagent runs with aidboxdb container and will restart after container restarting. By default pgagent runs as the standard user `postgres`. in order to run pgagent as a different user you need.

## Run as a different user

1. [Create new user in db](https://www.postgresql.org/docs/8.0/sql-createuser.html)
2. [Give the postgres user all the necessary permissions](https://www.postgresql.org/docs/9.1/sql-grant.html). 
3. Provide user password by [environment variables](https://docs.aidbox.app/getting-started/installation/aidboxdb-image#optional-environment-variables) `PGAGENT_USER` and `PGAGENT_PASSWORD`

Example docker compose file with pgagent enabled running from another user.

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

Example [kubernetes deployment](https://kubernetes.io/docs/tasks/inject-data-application/define-environment-variable-container/) file with pgagent enabled running from another user.

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

How to check that pgagent is working? We can verify this by connecting to the aidbox container. You can connect to container by docker cli or kubectl if use use kubernetes.

* connect by docker cli - [https://docs.docker.com/engine/reference/commandline/exec/](https://docs.docker.com/engine/reference/commandline/exec/)
* connect by kubectl - [https://kubernetes.io/docs/tasks/debug-application-cluster/get-shell-running-container/](https://kubernetes.io/docs/tasks/debug-application-cluster/get-shell-running-container/)

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











