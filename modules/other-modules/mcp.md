# MCP

{% hint style="warning" %}
The Aidbox MCP module is available starting from version 2505 and is currently in the alpha stage.
{% endhint %}

**MCP server** is a lightweight service that exposes tools and data sources through standardized MCP endpoints. It lets any MCP‑enabled Large Language Model securely discover and invoke those resources, acting as a universal bridge between the model and the outside world.

## Aidbox MCP Server

Aidbox MCP server works through Server-Sent Events (SSE) protocol and provides two endpoints:

* `<aidbox-base-url>/mcp` - to connect server and discover tools
* `<aidbox-base-url>/mcp/<mcp-client-id>/messages` - to send messages to the server

### Tools

Aidbox provides a set of MCP tools to cover FHIR CRUDS operations.

<table><thead><tr><th width="198.7421875">Tool Name</th><th>Properties</th><th>Description</th></tr></thead><tbody><tr><td>read-fhir-resource</td><td>- resourceType (string, required)<br>- id (string, required)</td><td>Read an individual FHIR resource</td></tr><tr><td>create-fhir-resource</td><td>- resourceType (string, required)<br>- body (JSON object, required)</td><td>Create a new FHIR resource</td></tr><tr><td>update-fhir-resource</td><td>- resourceType (string, required)<br>- id (string, required)<br>- body (JSON object, required)</td><td>Update an existing FHIR resource</td></tr><tr><td>delete-fhir-resource</td><td>- resourceType (string, required)<br>- id (string, required)</td><td>Delete an existing FHIR resource</td></tr><tr><td>search-fhir-resources</td><td>- resourceType (string, required)<br>- query (string, required)</td><td>Search existing FHIR resources</td></tr></tbody></table>

## Connect to Aidbox MCP server

To enable MCP sever in Aidbox set environment variable `BOX_MODULE_MCP_SERVER_ENABLED` to `true`.

### Using MCP Inspector

MCP Inspector is a tool that helps you to discover and test MCP tools. It is a web application that allows you to connect to Aidbox MCP server and explore its capabilities.

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

3. Click `Connect` button.

Now you can discover tools and use them.

<figure><img src="../../.gitbook/assets/Screenshot 2025-05-07 at 14.24.30.png" alt=""><figcaption></figcaption></figure>

### Using LLM agents

Aidbox MCP server config:

```json
{
  "mcpServers": {
    "aidbox": {
      "command": "npx",
      "args": [
        "-y",
        "@latitude-data/supergateway",
        "--sse",
        "<your-box-base-url>/mcp"
      ]
    }
  }
}
```

* For the `Cursor` editor add this config to your project folder `.cursor/mcp.json`.
* For the LLM Desktop applications such `Claude`, `ChatGPT` etc. go to the `Settings` and set the config. For example in `Claude` desktop app go to `Settings` -> `Developer` -> `Edit Config`.

Now you can ask your LLM agent to Create, Read, Update or Delete FHIR resources in Aidbox.
