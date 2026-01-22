---
description: >-
  In this tutorial, you will learn how to configure Aidbox to use local filesystem as artifact registry for initialization process.
---

# How to configure Aidbox to use local filesystem as artifact registry for initialization

To load FHIR packages from the local filesystem instead of fetching them from a remote NPM registry, mount a directory containing the packages to `/srv/aidbox-fhir-packages` in the Aidbox container. 

Packages must be in `.tgz` format (gzipped tarball) and follow the naming convention {package-name}#{version}.tgz. For example, the package hl7.fhir.r4.core version 4.0.1 should be named hl7.fhir.r4.core#4.0.1.tgz. 

When Aidbox loads a package, it will first check this local directory before attempting to download from the configured registry. This is useful for air-gapped environments, faster startup times, or when the remote registry is unavailable.

## 1. Generate a docker compose from runme script

Run the following command to generate a docker compose file:

```bash
mkdir -p aidbox && cd aidbox
curl -JO https://aidbox.app/runme
```


## 2. Prepare the local folder and put tar archive with FHIR package `hl7.fhir.r4.core` there


```bash
mkdir -p fhir-packages
curl -L "https://fs.get-ig.org/-/hl7.fhir.r4.core-4.0.1.tgz" -o "./fhir-packages/hl7.fhir.r4.core#4.0.1.tgz"
```

## 3. Configure docker compose and init bundle

Add the following volume and environment variable to the docker compose file:

```yaml
aidbox:
  image: docker.io/healthsamurai/aidboxone:edge
  volumes:
    - ./fhir-packages:/srv/fhir-packages
  environment:
    # Uncomment the following line if you need to make sure external registry is not used
    # BOX_FHIR_NPM_PACKAGE_REGISTRY: https://broken.org
```

## 4. Start and initialize Aidbox

Start Aidbox with the following command:

```bash
docker compose up -d
```

Navigate to the [Aidbox UI](http://localhost:8080/), initialize Aidbox by following the instructions in the [Getting Started](../../getting-started/run-aidbox-locally.md) guide and navigate to the "FAR" tab and check that the fhir r4 core package is loaded.

## See also:

{% content-ref url="../../artifact-registry/artifact-registry-overview.md#loading-packages-from-local-filesystem" %}
[Loading packages from local filesystem](../../artifact-registry/artifact-registry-overview.md#loading-packages-from-local-filesystem)
{% endcontent-ref %}