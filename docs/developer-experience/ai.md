---
description: Learn how to integrate AI assistants with Aidbox through MCP Server and AI Prompts for enhanced development workflows.
---

# Working with AI

Aidbox provides comprehensive AI integration capabilities that enable developers to leverage large language models (LLMs) for healthcare application development. Aidbox offers two primary AI integration methods: the [Model Context Protocol (MCP)](https://modelcontextprotocol.io/) Server for direct AI assistant interaction with FHIR resources, and specialized AI Prompts for generating complex configurations like Access Policies and Search Parameters. 

## MCP Server

The Aidbox MCP Server enables direct integration between AI assistants and your FHIR server through the standardized Model Context Protocol. This powerful feature allows LLMs like Claude, ChatGPT, and other AI tools to perform FHIR operations directly within your conversational workflow, making healthcare data management more intuitive and efficient.

### Key Capabilities

The MCP Server provides a comprehensive set of FHIR operations that AI assistants can execute:

- **Resource Management**: Create, read, update, and delete FHIR resources through natural language commands
- **Advanced Search**: Perform complex FHIR searches with intelligent query building
- **Conditional Operations**: Execute conditional updates and patches based on specific criteria
- **Real-time Interaction**: Work with live healthcare data through Server-Sent Events (SSE) protocol

### Supported AI Platforms

The MCP Server works seamlessly with popular AI development tools:

- **Claude Desktop**: Direct integration for healthcare application development
- **Cursor Editor**: In-IDE FHIR resource management during coding
- **ChatGPT Desktop**: Natural language FHIR operations
- **Custom Applications**: Any MCP-compatible LLM through standardized endpoints

### Security and Access Control

Aidbox MCP Server implements robust security measures through Access Policies, ensuring that AI interactions with healthcare data remain secure and compliant. You can configure either public endpoints for development or token-based authentication for production environments.

See also:
- [Aidbox MCP Server](../modules/other-modules/mcp.md)
- [Model Context Protocol Specification](https://modelcontextprotocol.io/)

[MCP Server](../modules/other-modules/mcp.md)

## AI Prompts

AI Prompts provide pre-configured documentation bundles that enable LLMs to generate complex Aidbox configurations with expert-level accuracy. These specialized prompts combine comprehensive documentation, examples, and best practices to help AI assistants create sophisticated Access Policies and Search Parameters without requiring deep expertise in FHIR or Aidbox internals.

### Available Prompt Types

Aidbox currently provides AI prompts for two critical configuration areas:

- **Access Policy Generation**: Create sophisticated authorization rules that control access to FHIR resources based on user roles, resource types, and complex business logic
- **Search Parameter Creation**: Define custom search capabilities for FHIR resources, enabling advanced querying and filtering based on specific healthcare use cases

### How AI Prompts Work

AI Prompts operate by bundling relevant documentation into comprehensive reference materials that you can include in your AI assistant conversations. Each prompt combines:

1. **Core Concepts**: Fundamental principles and architecture overview
2. **Practical Examples**: Real-world implementation patterns and use cases
3. **Best Practices**: Security considerations, performance guidelines, and recommended approaches

### Integration with Development Tools

These prompts work seamlessly with popular AI-powered development environments:

- **GitHub Copilot**: Use `#<filename>` to include prompt documentation
- **Cursor**: Reference prompts with `@Files` for context-aware assistance
- **Zed Editor**: Include prompts using `/file` command
- **Claude Code**: Reference documentation with `@<filename>` notation


See also:
- [AI Prompts Tutorial](../tutorials/other-tutorials/ai-prompts.md)
