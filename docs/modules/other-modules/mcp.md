# MCP

{% hint style="warning" %}
The Aidbox MCP module is available starting from version 2505 and is currently in the alpha stage.
{% endhint %}

**MCP server** is a lightweight service that exposes tools and data sources through standardized MCP endpoints. It lets any MCP‑enabled Large Language Model securely discover and invoke those resources, acting as a universal bridge between the model and the outside world.

## Aidbox MCP Server

Aidbox MCP server works through Server-Sent Events (SSE) protocol and provides two endpoints:

* `<aidbox-base-url>/mcp` - to connect the server and discover tools
* `<aidbox-base-url>/mcp/<mcp-client-id>/messages` - to send messages to the server

### Tools

Aidbox provides a set of MCP tools to cover FHIR CRUDS operations.

<table><thead><tr><th width="198.7421875">Tool Name</th><th>Properties</th><th>Description</th></tr></thead><tbody><tr><td>read-fhir-resource</td><td>- resourceType (string, required)<br>- id (string, required)</td><td>Read an individual FHIR resource</td></tr><tr><td>create-fhir-resource</td><td>- resourceType (string, required)<br>- resource (JSON object, required)<br>- headers (JSON object)</td><td>Create a new FHIR resource</td></tr><tr><td>update-fhir-resource</td><td>- resourceType (string, required)<br>- id (string, required)<br>- resource (JSON object, required)</td><td>Update an existing FHIR resource</td></tr><tr><td>conditional-update-fhir-resource</td><td>- resourceType (string, required)<br>- resource (JSON object, required)<br>- query (string)<br>- headers (JSON object)</td><td>Conditional update an existing FHIR resource</td></tr><tr><td>conditional-patch-fhir-resource</td><td>- resourceType (string, required)<br>- resource (JSON object, required)<br>- query (string)<br>- headers (JSON object)</td><td>Conditional patch an existing FHIR resource</td></tr><tr><td>patch-fhir-resource</td><td>- resourceType (string, required)<br>- id (string, required)<br>- resource (JSON object, required)</td><td>Patch an existing FHIR resource</td></tr><tr><td>delete-fhir-resource</td><td>- resourceType (string, required)<br>- id (string, required)</td><td>Delete an existing FHIR resource</td></tr><tr><td>search-fhir-resources</td><td>- resourceType (string, required)<br>- query (string, required)</td><td>Search an existing FHIR resources</td></tr><tr><td>validate-fhir-resource<br><em>* available since 2509</em></td><td>- resourceType (string, required)<br>- resource (JSON object, required)<br>- mode (string - create|update|delete|patch, required)</td><td>Validate FHIR resource</td></tr></tbody></table>

## Configure Aidbox MCP server

### Runme command

The easiest way to run Aidbox with MCP is use the runme command:

```
curl -JO https://aidbox.app/runme/mcp && docker compose up
```

You will get Aidbox with enabled MCP server and created `AccessPolicy` for it.

### Already existed Aidbox

If you have already configured Aidbox to enable the MCP server:

1. Set [`module.mcp.server-enabled` setting](../../reference/all-settings.md#module.mcp.server-enabled) to `true`
2. Set up Access Control for MCP endpoints via `AccessPolicy`

#### Option 1. Public MCP Endpoint

{% hint style="warning" %}
The easiest but unsafe way to test MCP Server. Recommended for local development tests.
{% endhint %}

Aidbox MCP endpoints are not public, so you need to set up Access Control for these endpoints.\
The easiest way (but not the safest) is to create allow `AccessPolicy` for mcp operations:

```http
PUT /AccessPolicy/allow-mcp-endpoints
content-type: application/json
accept: application/json

{
  "resourceType": "AccessPolicy",
  "id": "allow-mcp-endpoints",
  "link": [
    {
      "id": "mcp",
      "resourceType": "Operation"
    },
    {
      "id": "mcp-sse",
      "resourceType": "Operation"
    },
    {
      "id": "mcp-client-messages",
      "resourceType": "Operation"
    }
  ],
  "engine": "allow"
}
```

This means that Aidbox MCP endpoints become public and anybody has access to them.

#### Option 2. Restricted MCP Endpoint

The second way (safer one) is to create `Client`, `AccessPolcy`, get a token and use this token to connect to Aidbox MCP server.\
Create `Client` resource

```http
PUT /Client/mcp-client
content-type: application/json
accept: application/json

{
 "id": "mcp-client",
 "secret": "verysecret", // change secret to more reliable one
 "grant_types": ["client_credentials"]
}
```

Create AccessPolicy resource:

```http
PUT /AccessPolicy/allow-mcp-endpoints
content-type: application/json
accept: application/json

{
  "resourceType": "AccessPolicy",
  "id": "mcp-endpoints",
  "engine": "matcho",
  "matcho": {
    "client": {
      "id": "mcp-client"
    },
    "operation": {
      "$one-of": [
        {
          "resourceType": "Operation",
          "id": "mcp"
        },
        {
          "resourceType": "Operation",
          "id": "mcp-sse"
        },
        {
          "resourceType": "Operation",
          "id": "mcp-client-messages"
        }
      ]
    }
  }
}
```

Get token:

```http
POST /auth/token
content-type: application/json
accept: application/json

{
 "client_id": "mcp-client",
 "client_secret": "verysecret", // put here your client secret
 "grant_type": "client_credentials"
}
```

Save a token from the response to connect to MCP server.

## Connect to MCP server

### Using LLM agents

Aidbox MCP server config:

```shell-session
$ npx -y supergateway --sse <your-box-base-url>/sse
```

```json
{
  "mcpServers": {
    "aidbox": {
      "command": "npx",
      "args": [
        "-y",
        "supergateway",
        "--sse",
        "<your-box-base-url>/sse",
        "--oauth2Bearer", // add this only if you created a client and got a token
        "<your-aidbox-token>" // add this only if you created a client and got a token
      ]
    }
  }
}
```

*   For Claude Code, run:

    ```
    claude mcp add aidbox-mcp -- npx -y supergateway --sse http://localhost:8080/sse
    ```
* For the `Cursor` editor add this config to your project folder `.cursor/mcp.json` and make sure that `Settings` -> `Cursor Settings` -> `MCP` is enabled.
* For the LLM Desktop applications, such `Claude Desktop`, `ChatGPT` etc. go to the `Settings` and set the config. For example, in `Claude` desktop app go to `Settings` -> `Developer` -> `Edit Config`.

Now you can ask your LLM agent to Create, Read, Update or Delete FHIR resources in Aidbox.

{% hint style="warning" %}
You need to uninstall all node versions below 18 if you use Claude Desktop. \\

```
nvm uninstall v16
nvm uninstall ... another version below 18
nvm cache clear
```
{% endhint %}

### Using MCP Inspector

MCP Inspector is a tool that helps you to discover and test MCP tools. It is a web application that allows you to connect to the Aidbox MCP server and explore its capabilities.

1. Run MCP Inspector

```bash
npx @modelcontextprotocol/inspector
```

Open the inspector in the browser:

```bash
http://localhost:6274
```

2. Connect to Aidbox MCP server

Select `SSE` in `Transport Type` dropdown. And set URL to `<your-aidbox-base-url>/mcp`.

3. Add your Aidbox token to `Authentication` -> `Bearer Token` (only if you created Aidbox Client and got the token).
4. Click `Connect` button.

Now you can discover tools and use them.

<figure><img src="../../.gitbook/assets/2a76abd4-8667-4151-a6d4-59abdd785c1f.png" alt=""><figcaption></figcaption></figure>
