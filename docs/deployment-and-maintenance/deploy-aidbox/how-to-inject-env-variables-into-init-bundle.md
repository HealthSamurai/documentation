# How to inject env variables into Init Bundle

Aidbox provides [Init Bundle](../../configuration/init-bundle.md) functionality to create configuration resources on its start. However, Aidbox load Init Bundle content as is, so it is not possible to inject secrets or other environment variables in it.

To overcome this limitation, we can use third-party reliable tools like `envsubst` and `sed` to inject secrets into Init Bundle. You can add following instruction as a step to your CI pipeline:

Let's say we have Aidbox [TokenIntrospector](../../tutorials/security-access-control-tutorials/set-up-token-introspection.md) resource in our Init Bundle and we have to set different values for `iss` and `jwks_uri` parameters on different environments. We need to prepare `init-bundle.json.template` file:

```json
{
  "type": "batch",
  "entry": [
    {
      "request": {
        "method": "PUT",
        "url": "/IdentityProvider/keycloak"
      },
      "resource": 
      {
        "resourceType": "TokenIntrospector",
        "id": "external-auth-server",
        "type": "jwt",
        "jwt": {
          "iss": "${AUTH_SERVER_URL}"
        },
        "jwks_uri": "${AUTH_SERVER_URL}/.well-known/jwks.json"
      }
    }
  ]
}
```

Please note that we use `${AUTH_SERVER_URL}` placeholder for the `jwt.iss` and `jwks_uri` parameters. This placeholder will be replaced with the actual value on CI.

## Dynamically template init bundle in Docker Compose

[See this example](https://github.com/Aidbox/examples/tree/main/init-bundle-env-template). This example demonstrates how to dynamically template Aidbox init bundles using environment variables in Docker Compose by overriding the container entrypoint.

## Using envsubst

`envsubst` is a tiny GNU gettext utility that scans a text file for shell-style variables like `${VAR}` and replaces them with the values currently set in the process environment.

1. Export all environment variables that you need to inject into Init Bundle:

```bash
export AUTH_SERVER_URL=https://auth.example.com
```

2. Run `envsubst` command to inject environment variables into `init-bundle.json.template` file:

```bash
envsubst < init-bundle.json.template > init-bundle.json
```

3. Use `init-bundle.json` file in your CI pipeline.

`init-bundle.json` file content will be:

```json
{
  "type": "batch",
  "entry": [
    {
      "request": {
        "method": "PUT",
        "url": "/IdentityProvider/keycloak"
      },
      "resource": {
        "resourceType": "TokenIntrospector",
        "id": "external-auth-server",
        "type": "jwt",
        "jwt": {
          "iss": "https://auth.example.com"
        },
        "jwks_uri": "https://auth.example.com/.well-known/jwks.json"
      }
    }
  ]
}
```

Aidbox envs:

```
...
BOX_INIT_BUNDLE: file:///init-bundle.json
...
```

## Using sed

`sed` – the stream editor that performs scripted text transformations (substitution, insertion, deletion, etc.)

1. Export all environment variables that you need to inject into Init Bundle:

```bash
export AUTH_SERVER_URL=https://auth.example.com
```

2. Run `sed` command to inject environment variables into `init-bundle.json.template` file:

```bash
sed -e "s|\${AUTH_SERVER_URL}|$AUTH_SERVER_URL|g" \
    init-bundle.json.template > init-bundle.json
```

3. Use `init-bundle.json` file in your CI pipeline.

`init-bundle.json` file content will be:

```json
{
  "type": "batch",
  "entry": [
    {
      "request": {
        "method": "PUT",
        "url": "/IdentityProvider/keycloak"
      },
      "resource": {
        "resourceType": "TokenIntrospector",
        "id": "external-auth-server",
        "type": "jwt",
        "jwt": {
          "iss": "https://auth.example.com"
        },
        "jwks_uri": "https://auth.example.com/.well-known/jwks.json"
      }
    }
  ]
}
```

Aidbox envs:

```
...
BOX_INIT_BUNDLE: file:///init-bundle.json
...
```

## CI step example

GitHub Actions step example with `envsubst` command:

```yaml
- name: Fill init bundle template
  run: |
    echo "Populating auth server value"
    envsubst < init-bundle.json.template > init-bundle.json
  env:
    AUTH_SERVER_URL: ${{ secrets.AUTH_SERVER_URL }}
```
