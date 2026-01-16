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


## 2. Prepare the local folder and put tar archives with FHIR packages there

Please note that it's not mandatory to download terminology package, but it's recommended. Learn more about HL7 Terminology packages [here](https://terminology.hl7.org/index.html).


```bash
mkdir -p fhir-packages
curl -L "https://fs.get-ig.org/-/hl7.fhir.r4.core-4.0.1.tgz" -o "./fhir-packages/hl7.fhir.r4.core#4.0.1.tgz"
# It's not mandatory to download terminology package, but it's recommended
curl -L "https://fs.get-ig.org/-/hl7.terminology.r4-7.0.1.tgz" -o "./fhir-packages/hl7.terminology.r4#7.0.1.tgz"
# dependency package for terminology package
curl -L "https://fs.get-ig.org/-/hl7.fhir.uv.extensions.r4-5.2.0.tgz" -o "./fhir-packages/hl7.fhir.uv.extensions.r4#5.2.0.tgz"
```

## 3. Configure docker compose and init bundle

Create init-bundle.json file for loading terminology package (note that loading fhir core package is already configured in docker compose file using `BOX_BOOTSTRAP_FHIR_PACKAGES` environment variable).

```json
{
  "type": "transaction",
  "resourceType": "Bundle",
  "entry": [
    {
      "request": {
        "method": "POST",
        "url": "AidboxMigration",
        "ifNoneExist": "id=hl7-terminology-7.0.1"
      },
      "resource": {
        "action": "far-migration-fhir-package-install",
        "status": "to-run",
        "params": {
          "parameter": [
            {
              "name": "package",
              "valueString": "hl7.terminology.r4@7.0.1"
            }
          ],
          "resourceType": "Parameters"
        },
        "resourceType": "AidboxMigration",
        "id": "hl7-terminology-7.0.1"
      }
    }
  ]
}
```

Add the following volumes and environment variables to the docker compose file:

```yaml
aidbox:
  image: docker.io/healthsamurai/aidboxone:edge
  volumes:
    - ./fhir-packages:/srv/fhir-packages
    - ./init-bundle.json:/tmp/init-bundle.json
  environment:
    BOX_INIT_BUNDLE: file:///tmp/init-bundle.json
    # Uncomment the following line if you need to make sure external registry is not used
    # BOX_FHIR_NPM_PACKAGE_REGISTRY: https://broken.org
```

## 4. Start and initialize Aidbox

Start Aidbox with the following command:

```bash
docker compose up -d
```

Navigate to the [Aidbox UI](http://localhost:8080/), initialize Aidbox by following the instructions in the [Getting Started](../../getting-started/run-aidbox-locally.md) guide and navigate to the "FAR" tab and check that the packages are loaded.