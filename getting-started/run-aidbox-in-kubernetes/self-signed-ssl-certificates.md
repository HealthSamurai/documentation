---
description: This page explains how to make Aidbox respect self-signed SSL certificates
---

# Self-signed SSL certificates

## Problem

Sometimes it is necessary to make Aidbox respect self-signed certificates. For example, an identity provider may use a custom self-signed certificate and Aidbox receives SSL signature errors when sending HTTP requests to it.

## Solution

To make Aidbox respect these certificates, you need to load the root certificates into the Aidbox docker image and make Java (JRE) respect them as well.

## How to install custom SSL into Aidbox docker image

To install self-signed SSLs into Adibox docker image, you show build your own one. That image should

1. Inherit the required Aidbox docker image
2. Load necessary certificates to the image
3. Install the certs into operation system and Java runtime

{% code title="Dockerfile" lineNumbers="true" %}
```docker
# ingeriting from Aidbox docker image
FROM healthsamurai/aidboxone:<YOUR_AIDBOX_VERSION_HERE>

# switching to the root user
USER root 

# coping certificate into the docker filesystem
COPY root-ca-custom.pem /etc/ssl/certs/root-ca-custom.pem

# installing cert
RUN update-ca-certificates

# making Java respect loaded certificate
RUN keytool -import -storepass storepass_password -noprompt -alias root-ca-custom -cacerts -trustcacerts -file /etc/ssl/certs/root-ca-custom.pem

# switching back to the aidbox user
USER aidbox
```
{% endcode %}

## Build docker image

To build your custom Aidbox docker image use the docker [build command](https://docs.docker.com/engine/reference/commandline/build/).

For example, the build command could look like this

```bash
docker build -t aidbox-with-certs:latest .
```
