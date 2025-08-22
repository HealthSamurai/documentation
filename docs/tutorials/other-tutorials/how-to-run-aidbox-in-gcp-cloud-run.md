# How to run Aidbox in GCP Cloud Run

## Objectives <a href="#objectives" id="objectives"></a>

* Run the Aidbox container in the GCP [Cloud Run](https://cloud.google.com/run?hl=en) environment using [Cloud SQL ](https://cloud.google.com/sql?hl=en)as a database.

## Before you begin <a href="#before-you-begin" id="before-you-begin"></a>

* You must have an active Google Cloud account. More information [here](https://cloud.google.com/docs/get-started).

## Create a Managed PostgreSQL Database

1.  Log in to Google Cloud Console, go to SQL Service, and create a new Cloud SQL instance.

    <figure><img src="../../../.gitbook/assets/96f8316d-950a-484c-aa89-a225f78c046e.png" alt=""><figcaption></figcaption></figure>
2.  Choose PostgreSQL for the database engine

    <figure><img src="../../../.gitbook/assets/47976888-10c5-40fb-b571-5efb4d934433.png" alt=""><figcaption></figcaption></figure>
3.  Choose Enterprise Cloud SQL Edition and **"Sandbox"** edition preset

    <figure><img src="../../../.gitbook/assets/9811df93-3a7b-45f1-928c-297a7a06b8fb.png" alt=""><figcaption></figcaption></figure>
4.  Enter the required parameters

    <figure><img src="../../../.gitbook/assets/ed9a9a48-423f-412a-a330-2e51a0bdbaac.png" alt=""><figcaption></figcaption></figure>

    \
    5\. Configure the private IP access to the database instance.

{% hint style="info" %}
Restricting database access at the network level is a recommended security best practice. In this tutorial, we take a simple approach by allowing access from the **default** VPC network and configuring Cloud Run to use the same network for outbound connections. For production environments, we strongly encourage implementing more granular and restrictive network policies tailored to your security needs.
{% endhint %}

In **the "Customize your Instance -> Connections"** section, enable the "**Private IP"** option and disable the **"Public IP"** option.

<figure><img src="../../../.gitbook/assets/13580b5c-aa49-4d00-9001-bbc9d2a6878e.png" alt=""><figcaption></figcaption></figure>

\
6\. Click the **"Create Instance"** button and wait until the instance is created.

<figure><img src="../../../.gitbook/assets/fddf7164-aa65-4fb8-a608-690abdf68733.png" alt=""><figcaption></figcaption></figure>

## Create a database and a user for the Aidbox service

1.  Open **Cloud SQL Studio**.

    <figure><img src="../../../.gitbook/assets/7875abc8-9a2d-4f11-8412-d96f1bd0617e.png" alt=""><figcaption></figcaption></figure>
2.  Connect to the **postgres** database with the username **postgres** and the password you've chosen when you created the database.

    <figure><img src="../../../.gitbook/assets/0abe38f1-9e9d-4c41-99ca-74550914d9aa.png" alt=""><figcaption></figcaption></figure>
3.  Create the database and user for the Aidbox.\
    \
    Run the following statements one by one in Cloud SQL Studio Editor

    ```sql
    -- 1. Create the user (and a role) for Aidbox
    CREATE USER aidbox WITH PASSWORD '<your password for aidbox user>';
    -- 2. Grant the role aidbox to postgres, because postgres is not a superuser
    -- in Google Cloud SQL. 
    -- It's required to execute the following ALTER DATABASE statement
    GRANT aidbox TO postgres;
    -- 3. Create the database for Aidbox owned by the aidbox user
    CREATE DATABASE aidbox OWNER aidbox;
    ```

{% hint style="info" %}
During initialization, Aidbox creates certain database extensions. You can find more details [here](../../database/postgresql-extensions.md). If you prefer not to grant the Aidbox user the permissions required for this, you can pre-create the necessary extensions using a privileged user and disable automatic extension creation via [settings](../../reference/settings/database.md#db.install-pg-extensions). This is the approach we follow in the current tutorial.
{% endhint %}

4. Connect to the database `aidbox` , that you've created using user **postgres**, and create the extension.

```sql
CREATE EXTENSION pg_stat_statements;
```

## Create the service for Aidbox in Cloud Run

1.  Navigate to the **"Cloud Run"** section in the Google Cloud Console and create a new service from the container .

    <figure><img src="../../../.gitbook/assets/b24b29aa-eaec-4ecd-9ef2-f743205a665a.png" alt=""><figcaption></figcaption></figure>
2.  Enter `healthsamurai/aidboxone:edge` as a container image URL and add a service name.

    <figure><img src="../../../.gitbook/assets/78c85d4b-2c41-4faa-9a4f-d5d49bdd1b60.png" alt=""><figcaption></figcaption></figure>
3.  Disable authentication.

    <figure><img src="../../../.gitbook/assets/78a68216-130b-43e5-b497-1aa70c9f03ad.png" alt=""><figcaption></figcaption></figure>
4.  Select "**Instance-based**" billing, set Auto scaling to 1 and leave other properties by default.\


    <figure><img src="../../.gitbook/assets/image (7).png" alt=""><figcaption></figcaption></figure>
5.  Configure Memory and CPU Limits

    <figure><img src="../../../.gitbook/assets/28d61f89-6b9a-426f-9302-5969713bf640.png" alt=""><figcaption></figcaption></figure>
6.  Enable **"Second generation"** execution environment for better performance

    <figure><img src="../../../.gitbook/assets/9aef845b-9f25-4dc1-aaa6-275db56cf35f.png" alt=""><figcaption></figcaption></figure>
7.  In the container properties, add environment variables.

    <figure><img src="../../../.gitbook/assets/34052075-100c-4afc-8a70-d882f172f503.png" alt=""><figcaption></figcaption></figure>

    Add the following environment variables:\


    ```yaml
    - name: BOX_SECURITY_AUDIT_LOG_ENABLED
      value: true
    - name: BOX_FHIR_SCHEMA_VALIDATION
      value: true
    - name: BOX_DB_PORT
      value: 5432
    - name: BOX_WEB_PORT
      value: 8080
    - name: BOX_SECURITY_DEV_MODE
      value: true
    - name: BOX_BOOTSTRAP_FHIR_PACKAGES
      value: hl7.fhir.r4.core#4.0.1
    - name: BOX_DB_DATABASE
      value: aidbox
    - name: BOX_FHIR_CREATEDAT_URL
      value: https://aidbox.app/ex/createdAt
    - name: BOX_ROOT_CLIENT_SECRET
      value: <put the default API client secret here>
    - name: BOX_FHIR_CORRECT_AIDBOX_FORMAT
      value: true
    - name: BOX_SETTINGS_MODE
      value: read-write
    - name: BOX_DB_USER
      value: aidbox
    - name: BOX_FHIR_COMPLIANT_MODE
      value: true
    - name: BOX_FHIR_SEARCH_COMPARISONS
      value: true
    - name: BOX_COMPATIBILITY_VALIDATION_JSON__SCHEMA_REGEX
      value: #{:fhir-datetime}
    - name: BOX_FHIR_TERMINOLOGY_SERVICE_BASE_URL
      value: https://tx.health-samurai.io/fhir
    - name: BOX_DB_HOST
      value: <put your PostgreSQL internal IP here>
    - name: BOX_FHIR_SEARCH_AUTHORIZE_INLINE_REQUESTS
      value: true
    # See https://cloud.google.com/run/docs/triggering/https-request#deterministic  
    # for more info about deterministic URLs
    - name: BOX_WEB_BASE_URL
      value: <put your deterministic URL here>
    - name: BOX_DB_PASSWORD
      value: <put the password of aidbox database user here>
    - name: BOX_ADMIN_PASSWORD
      value: <put the password of aidbox built-in admin user here>
    - name: BOX_SEARCH_INCLUDE_CONFORMANT
      value: true
    - name: BOX_DB_INSTALL_PG_EXTENSIONS
      value: false
    - name: BOX_DB_MAINTENANCE_DATABASE
      value: aidbox
    - name: JAVA_OPTS
      value: -XX:MaxRAMPercentage=75 -XshowSettings:vm  
    ```

    \
    See more about recommended Aidbox environment variables [here](../../configuration/configure-aidbox-and-multibox.md).

    <figure><img src="../../../.gitbook/assets/bc06953f-ef76-4c90-ac15-59f1f56d3794.png" alt=""><figcaption></figcaption></figure>
8.  At the Networking tab, configure the outbound traffic to go to the **default** subnet.\


    <figure><img src="../../../.gitbook/assets/b89e5183-91ac-4506-944f-376e4cc90d49.png" alt=""><figcaption></figcaption></figure>
9. Click the "**Create**" button and wait for the service to deploy successfully.

<figure><img src="../../../.gitbook/assets/c72b5c74-925a-4c5b-ab30-f8e33f230640.png" alt=""><figcaption></figcaption></figure>

10. Access the service via the URL.

<figure><img src="../../../.gitbook/assets/f2713806-c5b6-4fb7-9494-dd2c5d59c5c0.png" alt=""><figcaption></figcaption></figure>

11. [Activate](../../getting-started/run-aidbox-locally.md#id-4.-activate-your-aidbox-instance) the Aidbox instance.

## What's next

See more about different options for running Aidbox:

* [Deploy Aidbox with Helm charts](../../deployment-and-maintenance/deploy-aidbox/run-aidbox-in-kubernetes/deploy-aidbox-with-helm-charts.md)
* [Run Aidbox locally](../../getting-started/run-aidbox-locally.md)
