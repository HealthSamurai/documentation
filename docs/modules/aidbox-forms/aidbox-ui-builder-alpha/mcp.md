---
description: Connect external MCP-capable agents directly to the Formbox Builder MCP endpoint.
---

# MCP

Formbox exposes an MCP endpoint so external agents can work with Formbox Builder directly. To connect an agent, use the `/sdc/mcp` endpoint and send a bearer token in the `Authorization` header.

## Endpoint

Formbox exposes one MCP endpoint:

```text
<formbox-base-url>/sdc/mcp
```

## Authentication

The authentication prerequisites are the same as for other Formbox MCP integrations: create a client, allow the required operations with an `AccessPolicy`, then request a bearer token.

### 1. Create a client

```http
POST /Client
content-type: application/json

{
  "resourceType": "Client",
  "id": "formbox-mcp-client",
  "secret": "verysecret",
  "grant_types": ["client_credentials"]
}
```

### 2. Create an AccessPolicy

This policy allows only the Formbox MCP operations used by this endpoint.

```http
POST /AccessPolicy
content-type: application/json

{
  "resourceType": "AccessPolicy",
  "id": "formbox-mcp-access",
  "engine": "matcho",
  "matcho": {
    "client": {
      "id": "formbox-mcp-client"
    },
    "operation": {
      "$one-of": [
        { "resourceType": "Operation", "id": "sdc-mcp" },
        { "resourceType": "Operation", "id": "sdc-mcp-stream" },
        { "resourceType": "Operation", "id": "sdc-mcp-delete" }
      ]
    }
  }
}
```

### 3. Get a token

```http
POST /auth/token
content-type: application/json
accept: application/json

{
  "client_id": "formbox-mcp-client",
  "client_secret": "verysecret",
  "grant_type": "client_credentials"
}
```

Save the `access_token` from the response. You will use it as a bearer token when connecting your agent.

## Connect an agent

Add the Formbox MCP server to your agent configuration and send the bearer token in the `Authorization` header.

{% tabs %}
{% tab title="Codex" %}
Add this to `~/.codex/config.toml`:

```toml
[mcp_servers.formbox]
url = "<formbox-base-url>/sdc/mcp"
http_headers = { Authorization = "Bearer <your-access-token>" }
```
{% endtab %}

{% tab title="Claude Code" %}
Run:

```sh
claude mcp add --transport http --scope user \
  --header "Authorization: Bearer <your-access-token>" \
  formbox <formbox-base-url>/sdc/mcp
```
{% endtab %}

{% tab title="Cursor" %}
Add this to `.cursor/mcp.json` in your project root, or `~/.cursor/mcp.json` for a global configuration:

```json
{
  "mcpServers": {
    "formbox": {
      "url": "<formbox-base-url>/sdc/mcp",
      "headers": {
        "Authorization": "Bearer <your-access-token>"
      }
    }
  }
}
```
{% endtab %}
{% endtabs %}

Replace `<formbox-base-url>` with your Formbox base URL and `<your-access-token>` with the token returned by `/auth/token`.

## Working with Formbox through MCP

Once the MCP server is configured, the interaction usually looks like this:

{% stepper %}
{% step %}
**User:** "Connect to Formbox and show me opened questionnaires."\
**Agent:** Lists the Builder tabs currently available for selection.
{% endstep %}

{% step %}
**User:** "Use PHQ-9 draft."\
**Agent:** Requests access to that Builder tab and asks for approval in the browser.
{% endstep %}

{% step %}
**User:** "Approved."\
**Agent:** Binds to the selected Builder tab and is ready to work with that questionnaire.
{% endstep %}

{% step %}
**User:** "Add the remaining PHQ-9 questions, reuse the same answer options, and add a calculated total score at the end."\
**Agent:** Updates the questionnaire structure in the selected Builder tab.
{% endstep %}

{% step %}
**User:** "Make all PHQ-9 questions required and verify the calculation."\
**Agent:** Applies the validation changes and checks that the calculation works.
{% endstep %}
{% endstepper %}

After the session is selected, the agent works with the same live Builder tab you see in the browser, so you can watch the changes in real time and continue refining the questionnaire through the conversation.
