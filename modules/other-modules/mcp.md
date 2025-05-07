# MCP

{% hint style="warning" %}
The Aidbox MCP module is available starting from version 2505 and is currently in the alpha stage.
{% endhint %}

*MCP server* is a lightweight service that exposes tools and data sources through standardized MCP endpoints. It lets any MCP‑enabled Large Language Model securely discover and invoke those resources, acting as a universal bridge between the model and the outside world.


## Aidbox MCP Server

Aidbox MCP server works through Server-Sent Events (SSE) protocol and provides two endpoints:

- `<aidbox-base-url>/mcp` - to connect server and discover tools
- `<aidbox-base-url>/mcp/<mcp-client-id>/messages` - to send messages to the server

### Tools

Aidbox provides a set of MCP tools to cover FHIR CRUD operations.

| Tool Name | Properties | Description |
|-----------|------------|-------------|
| read-fhir-resource | - resourceType (string, required)<br>- id (string, required) | Read an individual FHIR resource |
| create-fhir-resource | - resourceType (string, required)<br>- body (JSON object, required) | Create a new FHIR resource |
| update-fhir-resource | - resourceType (string, required)<br>- id (string, required)<br>- body (JSON object, required) | Update an existing FHIR resource |
| delete-fhir-resource | - resourceType (string, required)<br>- id (string, required) | Delete an existing FHIR resource |


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

Select `SSE` in `Transport Type` dropdown. Ans set URL to `<your-aidbox-base-url>/mcp`.

3. Click Connect button.

Now you can discover tools and use them.

### Using LLM agents

Aidbox MCP server config:

``` json
{
  "mcpServers": {
    "aidbox": {
      "command": "npx",
      "args": [
        "-y",
        "@latitude-data/supergateway",
        "--sse",
        "http://localhost:8765/mcp"
      ]
    }
  }
}
```

- For the `Cursor` editor add this config to your project folder `.cursor/mcp.json`.
- For the LLM Desktop applications such `Claude`, `ChatGPT` etc. go to the `Settings` and set the config. For example in `Claude` desktop app go to `Settings` -> `Developer` -> `Edit Config`.

Now you can ask your LLM agent to Create, Read, Update or Delete FHIR resources.
