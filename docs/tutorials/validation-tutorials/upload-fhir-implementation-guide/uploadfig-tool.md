---
description: >-
  Step-by-step guide to upload an FHIR Implementation Guide into Aidbox using
  the open-source UploadFIG
---

# UploadFIG Tool

## 1. Setup Aidbox

Set up Aidbox with the FHIR Schema validation engine. Following the guide below:

* [Setup Aidbox with FHIR Schema validation engine](../../../modules/profiling-and-validation/fhir-schema-validator/setup-aidbox-with-fhir-schema-validation-engine.md)

## 2. Setup UploadFIG

To get started with the **UploadFIG**, you'll first need to install **Dotnet** and download **uploadfig**. Follow the commands below or refer to the original [UploadFIG Installation guide](https://github.com/brianpos/UploadFIG?tab=readme-ov-file#installation):

{% tabs %}
{% tab title="Mac OS" %}
```
$ brew install dotnet
$ dotnet tool install uploadfig --global
$ export PATH="$PATH:/Users/<USER_NAME>/.dotnet/tools"
```
{% endtab %}
{% endtabs %}

After installation, verify that the is correctly installed:

```
UploadFIG --version
```

These steps ensure that you have the necessary tools installed and configured to use the **UploadFIG** effectively.

## 3. Get Bearer Access Token

### 3.1 Exchange client id and client secret for Access Token

To obtain an Access Token, you need to exchange your client ID and client secret. Use the following command:

{% code title="Aidbox REST Console" fullWidth="false" %}
```yaml
POST /auth/token

client_id: root
client_secret: secret
grant_type: client_credentials
```
{% endcode %}

This command will generate an Access Token that you'll need for authentication.

### 3.2 Copy Access Token from output

After executing the command, you'll receive a response containing the Access Token. Copy the `access_token` for later use.

{% code title="Output" %}
```yaml
token_type: Bearer
need_patient_banner: true
access_token: ZjU1YjMyYTQtYWUzZi00NWU1LWFkYjctMWMxMGI4ZGYyMGVj # Access Token
```
{% endcode %}

## 4. Upload Implementation Guide

To upload the IG, you need to specify the base URL of your Aidbox instance, along with the name and version of the package you wish to upload.

Additionally, provide the Bearer token obtained from the previous section for authentication.

Use the following command with UploadFIG:

```bash
UploadFIG \
-d http://localhost:8765/fhir \
-pid hl7.fhir.us.davinci-pdex \
-pv 2.0.0 \
-df json \
-dh "Authorization:Bearer ZjU1YjMyYTQtYWUzZi00NWU1LWFkYjctMWMxMGI4ZGYyMGVj" \
--verbose
```

This command initiates the upload process, sending the specified IG package to your Aidbox instance

Learn about other methods for loading IGs here:

* [Upload FHIR Implementation Guide](./)
