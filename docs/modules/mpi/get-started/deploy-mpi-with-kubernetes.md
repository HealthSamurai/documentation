---
description: >-
  This page provides instructions for deploying the Aidbox MPI module in
  Kubernetes using Helm charts.
---

# Deploy MPI with Kubernetes

The MPI module can be deployed using Helm Charts.

### Prerequisites

Before installing the MPI module, ensure you have:

* **Kubernetes Cluster** – running and able to pull images
* **Helm 3** – Helm CLI tool installed (version 3.x or later)
* **Service Account** – with access to the private Google Cloud registry containing the MPI image
* **Aidbox** – Aidbox and AidboxDB instances available

### Installation

For detailed deployment instructions, use this guide:

{% embed url="https://github.com/Aidbox/helm-charts/tree/main/mpi" %}

### Next step

**Create a matching model** – after deploying MPI, you need to create a matching model to enable record linkage. See [Configure MPI Module](configure-mpi-module.md) for details.
