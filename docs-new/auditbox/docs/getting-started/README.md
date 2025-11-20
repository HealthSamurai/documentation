# Auditbox Overview

**One vault for every audit trail**

Auditbox is a FHIR-native, Elastic-backed Audit Record Repository that consolidates AuditEvent resources from multiple systems and enables secure, high-performance querying for compliance, security, and operational visibility.

## What is Auditbox?

Auditbox is designed to centralize healthcare audit logs from multiple sources into a single source of truth. It uses FHIR AuditEvent as the foundational data model, ensuring a consistent schema and lifecycle policies across all audit records.

## Key Features

### Standards-Based Audit Consolidation
- Centralize FHIR AuditEvent logs from multiple systems
- Provide a single source of truth for audit data
- Ensure consistent schema and lifecycle policies

### FHIR Ecosystem Integration
- Integrate via standard FHIR APIs
- Use AuditEvent resources
- Reduce mapping and boilerplate work
- Seamless integration with existing healthcare systems

### Comprehensive Audit Tracking
Capture complete audit information including:
- **Who**: Agents/Actors performing actions
- **What**: Event type, action, and affected entities
- **When**: Timestamps and event lifecycle
- **Where**: Event source and location
- **How**: Authorization context and outcomes

### Performance & Efficiency
- **Fast performance**: Handle thousands of new audit events every second
- **Space-efficient**: Stores audit data using about 10x less disk space
- **Powerful search**: Find what you need with FHIR search and full-text search
- **User-friendly dashboard**: Quickly drill down to the most important events

## Compliance-Ready Design

Auditbox aligns with major compliance standards:
- **HIPAA**: Healthcare data protection requirements
- **GDPR**: European data protection regulations
- **ASTM E2147**: Standard for audit log structure and content
- Supports long-term regulatory retention requirements

## Architecture

Auditbox uses Elasticsearch behind the scenes for high-performance storage and querying. Despite its powerful capabilities, it doesn't require any special setup or a large server. Even a basic installation is sufficient, since most of the work involves writing new events (not searching), and only a few users will be reviewing events at a time.

### Event Data Model

FHIR AuditEvent captures detailed metadata including:
- Agents and actors involved
- Event source and location
- Event type and action performed
- Affected entities and resources
- Timestamp and outcome
- Authorization context

## Benefits

- **Cost-effective log storage**: Efficient storage reduces infrastructure costs
- **Fast incident investigations**: Quick search and filtering capabilities
- **Compliance-ready**: Built with regulatory requirements in mind
- **Security tool integration**: Works seamlessly with security monitoring systems
- **Long-term retention**: Designed for regulatory retention requirements

## Who Uses Auditbox?

Auditbox is designed for:
- Healthcare organizations managing multiple systems
- Security teams investigating incidents
- Compliance managers ensuring regulatory adherence
- Digital health developers building FHIR-based applications

## Get Started

Ready to try Auditbox? Check out our guides:

- [Run Auditbox with Oneliner](run-with-oneliner.md) - Quick setup with pre-configured services
- [Configuration](../configuration/envs.md) - Configure Auditbox for your environment
