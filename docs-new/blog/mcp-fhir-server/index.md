---
title: "Interacting with FHIR Server from LLM using MCP protocol"
slug: "mcp-fhir-server"
published: "2025-05-09"
author: "Aleksandr Kislitsyn"
reading-time: "7 min"
tags: []
category: "FHIR"
teaser: "We recently added experimental support for the MCP Server in Aidbox. Let's explore how to set it up and use it to interact with FHIR resources through an LLM interface."
image: "cover.png"
---

## The Setup MCP FHIR Server

### 1. Launch a New Aidbox Instance

Start by running the following command in your terminal:

```javascript
curl -JO https://aidbox.app/runme/mcp && docker compose up
```

This command will download the Docker Compose configuration file for the new Aidbox instance with the MCP server enabled and then launch the services.

### 2. Activate your Aidbox instance

Open your browser and go to `<http://localhost:8080>` to activate your Aidbox instance:

![MCP FHIR Server get instance](image-1.png)

Issuing a development license is free – just register in the [Aidbox user portal](https://aidbox.app/ui/portal#/signin) and create a new development license.

### 3. Connect Your LLM Desktop Application to Use Aidbox MCP Server

To use Aidbox's MCP Server with your local LLM desktop application – such as Claude Desktop – follow the steps below:

##### **Prerequisites**

Before getting started, make sure you have:

- [Node.js v18](https://nodejs.org/en/download) or higher installed.
- Uninstalled all [Node.js versions below v18](https://github.com/supercorp-ai/supergateway/issues/19), as Claude Desktop may not function correctly with older versions.

##### **Configuration Steps for Claude Desktop**

1. Open Settings → Developer → Edit Config
2. Update the claude\_desktop\_config.json file with the following snippet:

```javascript
{
  "mcpServers": {
    "aidbox": {
      "command": "npx",
      "args": [
        "-y",
        "@latitude-data/supergateway",
        "--sse",
        "http://localhost:8080/sse"
      ]
    }
  }
}
```

For detailed information about Aidbox's MCP server and how to configure different LLM applications, please visit: <https://docs.aidbox.app/modules/other-modules/mcp>

## Let's See It in Action

Once everything is configured, you’re ready to interact with your FHIR server using **Claude Desktop** powered by the MCP protocol.

### Creating a Basic Patient Resource

We’ll start by creating a basic Patient resource:

![create patient in MCP FHIR Server](image-2.png)

You’ll be prompted to allow Claude to use the external tool:

![cloud for MCP FHIR Server](image-3.png)

That's it! The Patient resource has been created:

![MCP FHIR Server result](image-4.png)

Claude used the `create-fhir-resource` tool to interact with the [FHIR Server](https://www.health-samurai.io/fhir-server) and create the Patient resource. To verify it, open the Aidbox Console UI (<http://localhost:8080>), navigate to the Resource Browser, and confirm that the Patient was successfully created:

![resource for MCP FHIR Server](image-5.png)

### Creating Related Resources

Let’s go a step further and create a couple of related resources:

![creating resource for MCP FHIR Server](image-6.png)

Here’s what’s interesting: while creating the Task resource, Claude received feedback from Aidbox, analyzed the root cause of any issues, and managed to fix them automatically.

### Creating a Profile-Conformant Resource

Now, let’s create a resource that conforms to a specific FHIR profile:

![create profile for MCP FHIR Server](image-7.png)

## Try It Yourself

Now it’s your turn! Setting up a development instance of [Aidbox](https://aidbox.app/) is completely free. Try integrating your own LLM application and start exploring how AI can streamline your FHIR workflows.
