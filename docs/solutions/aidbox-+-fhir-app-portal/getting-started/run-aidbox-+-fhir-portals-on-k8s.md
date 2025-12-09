---
description: Deploy Aidbox and FHIR App Portal on Kubernetes with step-by-step configuration instructions.
---

# Run Aidbox + FHIR Portals on k8s

This guide provides instructions for deploying Aidbox and FHIR App Portal on Kubernetes using Helm charts.

## Overview

The SmartBox Helm chart allows you to deploy a complete FHIR server solution on Kubernetes, including:

* Aidbox FHIR server
* FHIR App Portal
* PostgreSQL database
* Required configurations and secrets

## Prerequisites

Before you begin, ensure you have:

* A Kubernetes cluster (version 1.19+)
* `kubectl` configured to communicate with your cluster
* `helm` (version 3.0+) installed
* Aidbox license key (for production deployments)

## Deployment Guide

The official Helm charts and comprehensive deployment guide are available in the HealthSamurai Helm Charts repository:

**📦 Repository:** [https://github.com/HealthSamurai/helm-charts/tree/main/smartbox](https://github.com/HealthSamurai/helm-charts/tree/main/smartbox)

The repository contains:

* **Helm chart manifests** - Production-ready Kubernetes manifests
* **values.yaml** - Configurable deployment parameters
* **README.md** - Detailed installation and configuration guide
* **Examples** - Sample configurations for different deployment scenarios

## Quick Start

1. **Clone the repository:**

```bash
git clone https://github.com/HealthSamurai/helm-charts.git
cd helm-charts/smartbox
```

2. **Review and customize the values:**

```bash
# Edit values.yaml with your configuration
vim values.yaml
```

3. **Install the chart:**

```bash
helm install smartbox . -f values.yaml
```

4. **Verify the deployment:**

```bash
kubectl get pods
kubectl get services
```

## Configuration

Refer to the [SmartBox Helm Chart documentation](https://github.com/HealthSamurai/helm-charts/tree/main/smartbox) for detailed configuration options, including:

* Custom resource limits
* Persistent volume configuration
* Ingress and TLS setup
* Environment-specific settings
* Backup and monitoring configuration