# Getting started

The HL7v2 module is available as a Docker image with several versioning options:

* The `latest` tag represents the most recent development version, though it may contain unstable features
* The `stable` tag indicates a thoroughly tested version that has passed through multiple release cycles without known issues
* For specific versions, we use `vMAJOR.MINOR.PATCH` tags following [semantic versioning](https://semver.org/#semantic-versioning-200) conventions

## Deploy the Docker Application

Deploy the Docker image to your infrastructure using the following reference compose as a guide.

```yaml
name: hl7v2

services:
  app:
    container_name: app
    image: <private-registry>/hl7v2-module:latest
    restart: unless-stopped
    env_file:
      - .env
    environment:
      HL7v2_PORT: ${HL7v2_PORT}
      # ... other environment variables
    depends_on:
      aidbox:
        condition: service_healthy
    ports:
      - 8280:${HL7v2_PORT}
    networks:
      - hl7v2-module-network

  # ... other services, including Aidbox

networks:
  hl7v2-module-network: {}
```

#### Environment Variables

* `HL7v2_PORT` – HTTP port of the module
* `NREPL_PORT` – Reprl port for debugging
* `AIDBOX_BASE_URL` – Aidbox base URL (without trailing slash)
* `LOG_LEVEL` – Level of logs severity. Available values: trace, debug, info, warn, error, fatal, report, default is - **info.**
* `AIDBOX_CREDENTIALS` – Aidbox Client secret for module access; **base64-encoded** client name and secret from App resource definition: `base64("client_name:client_secret")`

## Create an Application

{% hint style="warning" %}
Resource definitions will be provided by our team directly.
{% endhint %}

Create the application using the provided resource definition. To do that, simply insert the resource definition into the App section in Aidbox (`/ui/console#/entities/App`).

**Key Notes:**

* Include your real **secret** in Client section (replace `PUT_SECRET_HERE`).
* Include your deployment host (replace `PUT_APP_ENDPOINT_URL_HERE`), but retain the `/rpc` part.

## Verify Setup with API Endpoints

After deployment, test the setup by calling these endpoints:

* _(directly)_ **`/`**: Access the OpenAPI/Swagger documentation to explore available APIs.
* _(directly)_ `/api/configuration`: Get all module configuration for debugging purposes.
* _(via aidbox)_ `/hl7v2/health`: Check the current version and retrieve health state.
