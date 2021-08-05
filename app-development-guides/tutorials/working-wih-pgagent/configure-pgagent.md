---
description: Aidbox gives you ability to run pgagent
---

# Configure pgagent

To start please specify enviromental variable in deploy configuration `ENABLE_PGAGENT: "true”` Here's an example of k8s configuration:

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

Pgagent process will reboot after prod rebooting.

When starting pod in k8s, pgagent runs under the standard user postgres. In order to start pgagent under a different user, you need to specify additional environment variables

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
          value: "password" #разумеется пароли лучше предвать через ресурс secret а не напрямую
...
```

Now pgagent running under different user role in container.

Troubleshoot

> If  job pgagent running on different user, please don't forget to grant access to new user to required tables.

