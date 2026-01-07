---
description: Deploy Aidbox on Azure Container Apps with Azure Database for PostgreSQL Flexible Server.
---

# How to run Aidbox in Azure Container Apps

## Objectives

- Run the Aidbox container in [Azure Container Apps](https://learn.microsoft.com/en-us/azure/container-apps/) using [Azure Database for PostgreSQL Flexible Server](https://learn.microsoft.com/en-us/azure/postgresql/flexible-server/) as a database.

## Before you begin

- You must have an active Azure subscription.
- Install [Azure CLI](https://learn.microsoft.com/en-us/cli/azure/install-azure-cli) and sign in with `az login`.

## Set up environment variables

First, define the variables that will be used throughout this tutorial:

```bash
RESOURCE_GROUP="aidbox-rg"
LOCATION="westeurope"
VNET_NAME="aidbox-vnet"
PG_SUBNET="pg-subnet"
CONTAINER_SUBNET="container-subnet"
PG_SERVER="aidbox-pg-server"
PG_USER="aidbox"
PG_PASSWORD="<your-secure-password>"
PG_DATABASE="aidbox"
CONTAINER_ENV="aidbox-env"
CONTAINER_APP="aidbox-app"
```

{% hint style="warning" %}
Replace `<your-secure-password>` with a strong password. Azure requires passwords to be at least 8 characters with uppercase, lowercase, numbers, and special characters.
{% endhint %}

## Create a Resource Group (optional)

If you don't have an existing resource group, create one:

```bash
az group create \
  --name $RESOURCE_GROUP \
  --location $LOCATION
```

## Create a Virtual Network

Create a VNet with two subnets — one for PostgreSQL and one for Container Apps:

```bash
az network vnet create \
  --resource-group $RESOURCE_GROUP \
  --name $VNET_NAME \
  --location $LOCATION \
  --address-prefix 10.0.0.0/16

az network vnet subnet create \
  --resource-group $RESOURCE_GROUP \
  --vnet-name $VNET_NAME \
  --name $PG_SUBNET \
  --address-prefix 10.0.1.0/24 \
  --delegations Microsoft.DBforPostgreSQL/flexibleServers

az network vnet subnet create \
  --resource-group $RESOURCE_GROUP \
  --vnet-name $VNET_NAME \
  --name $CONTAINER_SUBNET \
  --address-prefix 10.0.2.0/23
```

{% hint style="info" %}
Container Apps requires a subnet with at least /23 CIDR block.
{% endhint %}

## Create a private DNS zone for PostgreSQL

```bash
az network private-dns zone create \
  --resource-group $RESOURCE_GROUP \
  --name "privatelink.postgres.database.azure.com"

az network private-dns link vnet create \
  --resource-group $RESOURCE_GROUP \
  --zone-name "privatelink.postgres.database.azure.com" \
  --name "postgres-dns-link" \
  --virtual-network $VNET_NAME \
  --registration-enabled false
```

## Create a PostgreSQL Flexible Server

Create the PostgreSQL server with private access (no public endpoint):

```bash
PG_SUBNET_ID=$(az network vnet subnet show \
  --resource-group $RESOURCE_GROUP \
  --vnet-name $VNET_NAME \
  --name $PG_SUBNET \
  --query id -o tsv)

az postgres flexible-server create \
  --resource-group $RESOURCE_GROUP \
  --name $PG_SERVER \
  --location $LOCATION \
  --admin-user $PG_USER \
  --admin-password "$PG_PASSWORD" \
  --sku-name Standard_B1ms \
  --tier Burstable \
  --storage-size 32 \
  --version 15 \
  --subnet $PG_SUBNET_ID \
  --private-dns-zone "privatelink.postgres.database.azure.com" \
  --yes
```

## Create the database

```bash
az postgres flexible-server db create \
  --resource-group $RESOURCE_GROUP \
  --server-name $PG_SERVER \
  --database-name $PG_DATABASE
```

## Configure PostgreSQL extensions

{% hint style="info" %}
During initialization, Aidbox creates certain database extensions. You can find more details [here](../../database/postgresql-extensions.md). Azure PostgreSQL requires extensions to be allowlisted before they can be created.
{% endhint %}

Allowlist the required extensions:

```bash
az postgres flexible-server parameter set \
  --resource-group $RESOURCE_GROUP \
  --server-name $PG_SERVER \
  --name azure.extensions \
  --value "PG_STAT_STATEMENTS,UNACCENT,PG_TRGM"
```

Create the extensions. Since the database is in a private network, use [Azure Cloud Shell](https://shell.azure.com) which has network access to Azure resources:

```bash
az postgres flexible-server execute \
  --name $PG_SERVER \
  --admin-user $PG_USER \
  --admin-password "$PG_PASSWORD" \
  --database-name $PG_DATABASE \
  --querytext "CREATE EXTENSION IF NOT EXISTS pg_stat_statements; \
               CREATE EXTENSION IF NOT EXISTS unaccent; \
               CREATE EXTENSION IF NOT EXISTS pg_trgm;"
```

## Create a Container Apps Environment

Create the environment with VNet integration:

```bash
CONTAINER_SUBNET_ID=$(az network vnet subnet show \
  --resource-group $RESOURCE_GROUP \
  --vnet-name $VNET_NAME \
  --name $CONTAINER_SUBNET \
  --query id -o tsv)

az containerapp env create \
  --name $CONTAINER_ENV \
  --resource-group $RESOURCE_GROUP \
  --location $LOCATION \
  --infrastructure-subnet-resource-id $CONTAINER_SUBNET_ID
```

## Deploy Aidbox Container App

Deploy Aidbox with the required environment variables:

```bash
az containerapp create \
  --name $CONTAINER_APP \
  --resource-group $RESOURCE_GROUP \
  --environment $CONTAINER_ENV \
  --image healthsamurai/aidboxone:latest \
  --target-port 8888 \
  --ingress external \
  --cpu 1.5 \
  --memory 3Gi \
  --min-replicas 1 \
  --max-replicas 1 \
  --env-vars \
    BOX_WEB_PORT=8888 \
    BOX_DB_HOST=${PG_SERVER}.postgres.database.azure.com \
    BOX_DB_PORT=5432 \
    BOX_DB_DATABASE=$PG_DATABASE \
    BOX_DB_USER=$PG_USER \
    BOX_DB_PASSWORD=$PG_PASSWORD \
    BOX_DB_INSTALL_PG_EXTENSIONS=false \
    BOX_ADMIN_PASSWORD=<your-admin-password> \
    BOX_ROOT_CLIENT_SECRET=<your-client-secret> \
    BOX_SECURITY_DEV_MODE=true \
    BOX_SECURITY_AUDIT_LOG_ENABLED=true \
    BOX_SETTINGS_MODE=read-write \
    BOX_BOOTSTRAP_FHIR_PACKAGES=hl7.fhir.r4.core#4.0.1 \
    BOX_FHIR_SCHEMA_VALIDATION=true \
    BOX_FHIR_COMPLIANT_MODE=true \
    BOX_FHIR_CORRECT_AIDBOX_FORMAT=true \
    BOX_FHIR_CREATEDAT_URL=https://aidbox.app/ex/createdAt \
    BOX_FHIR_SEARCH_COMPARISONS=true \
    BOX_FHIR_SEARCH_AUTHORIZE_INLINE_REQUESTS=true \
    BOX_SEARCH_INCLUDE_CONFORMANT=true \
    BOX_FHIR_TERMINOLOGY_SERVICE_BASE_URL=https://tx.health-samurai.io/fhir \
    "BOX_COMPATIBILITY_VALIDATION_JSON__SCHEMA_REGEX=#{:fhir-datetime}"
```

{% hint style="warning" %}
Replace `<your-admin-password>` and `<your-client-secret>` with secure values. These credentials will be used to access Aidbox.
{% endhint %}

See more about [recommended Aidbox environment variables](../../configuration/recommended-envs.md).

## Verify deployment

1. Check the container logs:

```bash
az containerapp logs show \
  --name $CONTAINER_APP \
  --resource-group $RESOURCE_GROUP \
  --tail 100
```

2. Get the application URL:

```bash
az containerapp show \
  --name $CONTAINER_APP \
  --resource-group $RESOURCE_GROUP \
  --query "properties.configuration.ingress.fqdn" \
  --output tsv
```

3. Open the URL in your browser and [activate](../../getting-started/run-aidbox-locally.md#id-4.-activate-your-aidbox-instance) your Aidbox instance.

## What's next

See more about different options for running Aidbox:

- [Deploy Aidbox with Helm charts](../../deployment-and-maintenance/deploy-aidbox/run-aidbox-in-kubernetes/deploy-aidbox-with-helm-charts.md)
- [Run Aidbox locally](../../getting-started/run-aidbox-locally.md)
