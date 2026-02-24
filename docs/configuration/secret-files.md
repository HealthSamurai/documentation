---
description: >-
  Store sensitive values outside the database using a vault config file
  with named secret references and automatic rotation support.
---

# External Secrets

{% hint style="info" %}
This functionality is available starting from version 2602.
{% endhint %}

Aidbox can reference secrets stored as files on the filesystem instead of keeping them in the database. A JSON vault config file maps named secrets to file paths and controls which resources can access each secret. The actual values are never persisted or returned through the API.

## How it works

```mermaid
flowchart LR
    File(/run/secrets/client-secret):::blue2 -->|read at runtime| Aidbox(Aidbox):::violet2
    Config(vault-config.json):::blue2 -->|maps name → path + scope| Aidbox
    Aidbox -->|stores reference name| DB(_secret extension):::neutral2
```

1. A secret value is placed as a file on the filesystem (via Kubernetes Secrets, CSI Driver, Docker volumes, or any other mechanism)
2. A vault config JSON file maps named secrets to file paths and declares which resources may access them
3. A resource field uses the FHIR `data-absent-reason` / `secret-reference` extension pattern to reference a named secret
4. At runtime, Aidbox looks up the secret name in the vault config, verifies the requesting resource is in scope, and reads the file
5. Reading the resource back returns the extension with the secret name, never the secret value

## Configuration

| Environment variable | Description                                                                                   | Default                  |
|----------------------|-----------------------------------------------------------------------------------------------|--------------------------|
| `BOX_VAULT_CONFIG`   | Path to the vault config JSON file that maps named secrets to file paths and resource scopes. | empty (feature disabled) |

See [Aidbox Settings Reference](../reference/all-settings.md) for the full list of environment variables.

{% hint style="warning" %}
This setting requires a restart to take effect. The config file itself is re-read automatically when modified — no restart needed for config changes.
{% endhint %}

### Vault config file format

{% code title="vault-config.json" %}
```json
{
  "secret": {
    "client-secret": {
      "path": "/run/secrets/client-secret",
      "scope": {"resource_type": "Client", "id": "my-client"}
    },
    "kafka-jaas": {
      "path": "/run/secrets/kafka-jaas",
      "scope": {"resource_type": "AidboxTopicDestination", "id": "kafka-dest-1"}
    },
    "jwt-key": {
      "path": "/run/secrets/jwt-key",
      "scope": {"resource_type": "TokenIntrospector"}
    }
  }
}
```
{% endcode %}

Each entry under `"secret"` maps a secret name to:

<table><thead><tr><th width="100">Field</th><th>Description</th></tr></thead><tbody><tr><td><code>path</code></td><td>Absolute path to the file containing the secret value</td></tr><tr><td><code>scope</code></td><td>Object identifying the resource allowed to access this secret. Use <code>resource_type</code> and <code>id</code> to restrict to a specific instance (e.g. <code>{"resource_type": "Client", "id": "my-client"}</code>), or <code>resource_type</code> alone to allow any instance of that type (e.g. <code>{"resource_type": "TokenIntrospector"}</code>)</td></tr></tbody></table>

## Extension pattern

The resource uses FHIR primitive extensions on the secret field. The extension element carries two entries:

* `data-absent-reason` with value `masked` — indicates the field value is intentionally absent
* `secret-reference` with the secret name from the vault config — tells Aidbox which secret to resolve at runtime

### Example: Client secret

{% code title="PUT /fhir/Client/my-client" %}
```json
{
  "resourceType": "Client",
  "id": "my-client",
  "_secret": {
    "extension": [
      {
        "url": "http://hl7.org/fhir/StructureDefinition/data-absent-reason",
        "valueCode": "masked"
      },
      {
        "url": "http://health-samurai.io/fhir/secret-reference",
        "valueString": "client-secret"
      }
    ]
  },
  "grant_types": ["client_credentials", "basic"]
}
```
{% endcode %}

Reading the Client back returns the extension, not the resolved value:

{% code title="GET /fhir/Client/my-client" %}
```json
{
  "resourceType": "Client",
  "id": "my-client",
  "_secret": {
    "extension": [
      {
        "url": "http://hl7.org/fhir/StructureDefinition/data-absent-reason",
        "valueCode": "masked"
      },
      {
        "url": "http://health-samurai.io/fhir/secret-reference",
        "valueString": "client-secret"
      }
    ]
  },
  "grant_types": ["client_credentials", "basic"]
}
```
{% endcode %}

## Scope enforcement

Aidbox verifies that the resource requesting a secret is listed in the secret's `scope` array. If the requesting resource is not in scope, Aidbox returns an error.

## Secret rotation

Aidbox caches file contents with a short TTL and validates against file modification time. Updated files take effect automatically — no restart required.

This works with any mechanism that updates files in place — Kubernetes Secrets, CSI drivers, configuration management tools, or manual updates.

## Supported resources

The following fields support secret references via the extension pattern:

| Resource                   | Field                                  | Description                                      |
|----------------------------|----------------------------------------|--------------------------------------------------|
| **Client**                 | `secret`                               | Client secret for authentication                 |
| **IdentityProvider**       | `client.secret`                        | Client secret for symmetric authentication       |
| **IdentityProvider**       | `client.private-key`                   | Private key for asymmetric authentication        |
| **IdentityProvider**       | `client.certificate`                   | Certificate for asymmetric authentication        |
| **TokenIntrospector**      | `jwt.secret`                           | Shared secret key for JWT verification           |
| **TokenIntrospector**      | `jwt.keys.k`                           | Symmetric key for validation                     |
| **TokenIntrospector**      | `introspection_endpoint.authorization` | Authorization header value                       |
| **AidboxTopicDestination** | `parameter.saslJaasConfig`             | SASL JAAS configuration for Kafka authentication |
| **AidboxTopicDestination** | `parameter.sslKeystoreKey`             | SSL keystore private key for Kafka connection    |

## Delivering secrets to the filesystem

The external secrets feature is agnostic to how files are placed on the filesystem. Common approaches:

| Method                                                                          | Description                                                                                                                                                                                                                                                |
|---------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [Kubernetes Secrets](https://kubernetes.io/docs/concepts/configuration/secret/) | Mounted as volumes in pods                                                                                                                                                                                                                                 |
| [Secrets Store CSI Driver](https://secrets-store-csi-driver.sigs.k8s.io/)       | Mounts secrets from external vaults with automatic rotation. See the [Azure Key Vault](../tutorials/other-tutorials/azure-key-vault-external-secrets.md) and [HashiCorp Vault](../tutorials/other-tutorials/hashicorp-vault-external-secrets.md) tutorials |
| [Docker Secrets](https://docs.docker.com/engine/swarm/secrets/)                 | Available at `/run/secrets/` in swarm mode                                                                                                                                                                                                                 |
| Docker volumes                                                                  | Bind-mount a host directory containing secret files                                                                                                                                                                                                        |
