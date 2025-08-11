# Getting Started

The ePrescription module is available as a Docker image with several versioning options:

* The `latest` tag represents the most recent development version, though it may contain unstable features
* The `stable` tag indicates a thoroughly tested version that has passed through multiple release cycles without known issues
* For specific versions, we use `vMAJOR.MINOR.PATCH` tags following [semantic versioning](https://semver.org/#semantic-versioning-200) conventions

## Deploy the Docker Application

Deploy the Docker image to your infrastructure using the following reference compose as a guide.

```yaml
name: e-prescriptions

services:
  app:
    container_name: app
    image: <private-registry>/e-prescription:latest
    restart: unless-stopped
    secrets:
      - surescripts-authority
      - surescripts-cert
      - surescripts-private
    env_file:
      - .env
    environment:
      E_PRESCRIPTION_PORT: ${E_PRESCRIPTION_PORT}
      # ... other environment variables
    depends_on:
      aidbox:
        condition: service_healthy
    ports:
      - 8280:${E_PRESCRIPTION_PORT}
    networks:
      - e-prescription-network
    volumes:
      - ./data:/data

  # ... other services, including Aidbox

secrets:
  surescripts-authority:
    file: ./.secret/surescripts-authority.pem
  surescripts-cert:
    file: ./.secret/surescripts-cert.pem
  surescripts-private:
    file: ./.secret/surescripts-private.key

networks:
  e-prescription-network: {}
```

{% hint style="warning" %}
If you're not sure about the Surescripts secrets, please read this guide: [How to prepare authentication files.](./tutorials/how-to-test-callback.md)
{% endhint %}

{% hint style="warning" %}
For more information about available configuration options, please go to the [environment variables reference](./environment-variables.md).
{% endhint %}

### Volumes

The module can function without volume mounts, but we recommend mounting a volume to `/data` to enable directory backup functionality and ensure data persistence.

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
* _(via aidbox)_ `/e-prescription/health`: Check the current version and retrieve health state.
