---
description: >-
  This page describes how to deploy C-CDA <-> FHIR service.
---

# Deployment configuration

There is the configuration project template prepared for the service deployment:

```
git clone --depth=1 --branch=aidbox-ccda-service https://github.com/Aidbox/aidbox-project-template.git aidbox-project && cd aidbox-project && rm -rf .git && mkdir zen-packages
```

For detailed instructions on deploying the service based on the configuration project template please refer to the [deployment guide](https://docs.aidbox.app/getting-started/run-aidbox-locally-with-docker)

Also there is a page about [project configuration structure](https://docs.aidbox.app/aidbox-configuration/aidbox-zen-lang-project/aidbox-configuration-project-structure).

The next step is starting of docker-containers:
```
docker compose up --force-recreate --remove-orphans
```

The converter endpoints will be available at http://localhost:8888.

Please refer [this page](https://docs.aidbox.app/modules-1/ccda-converter) for more information about converter API.