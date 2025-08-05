# Uploading IG Terminology Content to External FHIR Terminology Server

In this tutorial, we will guide you through the steps to populate an external FHIR terminology server with IG terminology content, including ValueSets, CodeSystems, and more.

## 1. Install UploadFIG utility

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

## 2. Upload IG Terminology Content Using UploadFIG

To upload IG terminology content, execute the following command:

```shell
UploadFIG -pid <package-id> \
-pv <package-version> \
-r ValueSet \
-r CodeSystem \
-d <fhir-server-url> \
--verbose \
--includeReferencedDependencies
```

**Command Breakdown:**

* `-pid` — The package ID (e.g., hl7.fhir.r4.core, hl7.fhir.us.core, hl7.fhir.us.mcode, etc.).
* `-pv` — The package version (e.g., 4.0.1, 6.1.0, 3.0.0-ballot, etc.).
* `-r` — The resource types to load. For IG terminology, specify `ValueSet` and `CodeSystem`.
* `-d` — The FHIR server URL.
* `--verbose` — Enables verbose output.
* `--includeReferencedDependencies` — Loads any referenced CodeSystem or ValueSet from other packages.

If your IG is located on a file system, you can use the `-s` option with the path to your IG instead of the `-pid` option.

**Examples**

**Uploading US Core Terminologies by Package ID:**

```shell
UploadFIG -pid hl7.fhir.us.core \
-pv 6.1.0 \
-r ValueSet \
-r CodeSystem \ 
-d https://r4.ontoserver.csiro.au/fhir \
--verbose \
--includeReferencedDependencies
```

**Uploading mCode Terminologies from a Local File System:**

First, download mCode version 3.0.0 to your machine:

```shell
curl -sLo mcode.tgz https://packages2.fhir.org/packages/hl7.fhir.us.mcode/3.0.0
```

Then, upload the terminologies:

```shell
UploadFIG -s mcode.tgz \
-r ValueSet \
-r CodeSystem \
-d https://r4.ontoserver.csiro.au/fhir \
--verbose \
--includeReferencedDependencies
```
