---
description: Aidbox gives you ability to run pgagent
---

# Configure pgagent

Supported only on `aidboxdb:13.2`

To start [pgagent](https://www.pgadmin.org/docs/pgadmin4/development/pgagent.html) specify [environmental variable](https://app.gitbook.com/@aidbox/s/project/~/drafts/-MgL2PuDexdL_3OW-Bpq/getting-started/installation/aidboxdb-image#optional-environment-variables) in deploy configuration `ENABLE_PGAGENT: "true”` Here's an example of k8s configuration:

```text
kind: Deployment
...
spec:
  containers:
    - name: db
      image: healthsamurai/aidboxdb:13.2
      env:
        # Define the environment variable
        - name: ENABLE_PGAGENT 
          value: true
...
```

Pgagent runs with aidbox-master container and will restart after pod restarting.

When starting pod in k8s, pgagent runs as the standard user `postgres`. In order to start pgagent as a different user, you need to specify additional environment variables

> You need to create new user if user doesn't exist

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
    - name: db
      image: healthsamurai/aidboxdb:13.2
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

We can verify this by connecting to the postgres-master container. If you are using k8 the command will look like this

```text
kubectl exec -it <pod-name>  --namespace prod -- bash 
```

And check processes

```text
ps aux
```

You will see pgagent process and connection string with user, dbname, password

Also you can view the pgagent logs

```text
cat /tmp/pgagent.log
```

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

Troubleshooting

> If job pgagent running on different user, please don't forget to grant access to new user to required tables.
>
> if your environment variables are described in configmap and you have not changed deployment do not forget to restart deployment after changing environment variables for the change to take effect



