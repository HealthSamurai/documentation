---
description: >-
  This page describes how to deploy C-CDA <-> FHIR service.
---

# How to Deploy the Service

There is the configuration project template prepared for the service deployment:

```
git clone --depth=1 --branch=aidbox-ccda-service https://github.com/Aidbox/aidbox-project-template.git aidbox-project && cd aidbox-project && rm -rf .git && mkdir zen-packages
```

For detailed instructions on deploying the service based on the configuration project template please refer to the [deployment guide](../../../deployment-and-maintenance/deploy-aidbox/README.md)

The next step is starting of docker-containers:
```
docker compose up --force-recreate --remove-orphans
```

The converter endpoints will be available at http://localhost:8888.

Please refer [this page](./README.md) for more information about converter API.
