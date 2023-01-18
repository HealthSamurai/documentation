---
description: >-
  This article describes how to manage Aidbox licenses in a project and explains
  the differences between license types.
---

# Licenses

## How-tos

### Create license

1. On the main navigation sidebar, click on the _project name_
2. On the menu that opens, click _**Licenses**_
3. In the upper right corner of the page, click _**+ License**_
4. Specify _Product type_
   * Aidbox
   * Multibox
5. Specify _License type_
   * Standard
   * Development (available if you have a contract for a standard license)
   * CI/CD (available if you have a contract for a standard license)
6. Enter the _Name_ of the license
7. Specify _Hosting_ method
   * Self-hosted
   * AWS
   * Sandbox
     1. Choose _Configuration project_ if needed
     2. Specify _Zone_, if you didn't choose configuration project
     3. Enter the _Instance URL_
     4. Specify _FHIR Version_
8. Click _**Create**_

### Delete license

1. On the main navigation sidebar, click on the _project name_
2. On the menu that opens, click _**Licenses**_
3. Click on a license in the list
4. Information about the selected license will appear on the right side of the screen. Click _**Delete**_
5. In the confirmation window, click _**Ok**_

## Scenarios for different license types

Currently we offer 2 product types: Aidbox and Multibox. Aidbox offers 1 instance, Multibox offers an unlimited number of instances.&#x20;

Consider the following scenarios that demonstrate the difference between license types at Aidbox/Multibox and their limitations of using.

### Standard license

A standard license allows running Aidbox locally or in Aidbox Sandbox for 14 days and then can be extended by the Health Samurai team.This license will be valid for 14 days. If your trial period expires, while you are installing your box, you’ll receive an error message:

`License expires in 2 days. Please contact us for extended license.`

### Development license

Development license is available for contract clients. This license imposes a 2.0 GiB database size limit. Development license allows running 2 instances in parallel. When you reach your database size limit, you’ll receive 3 types of messages - warning, error message and blocking message:&#x20;

`Your database (name of your database) size 1.9 GiB is about to exceed the license limit 2.0 GiB, in that case your process is going to shutdown. Please review database size or contact us for further information.`

Error message is: `Your database (name of your database) size is 2.7 GiB and it's above license limit 2.0 GiB. Please review database size or contact us for further information.`

Blocking message is: `Your database (name of your database) size is 33.7 GiB and it's above license limit 2.0 GiB, your process is going to shutdown in 60 minutes. Please review database size or contact us for further information.`

### CI/CD license

CI/CD license also available for contract clients. A CI/CD license allows you to run multiple instances in parallel, but not more than for 72 hours. After 72 hours license will expire (this is TTL of a license), you need to take a 3 hour break, before you continue your multiple instances. You’ll get a message, before your runtime expires:&#x20;

`After 2 hours your CI/CD license will exceed allowed limit of runtime working (72 hours). After that your license will be blocked for 3 hours. Please reboot system or contact us for further information.`

## References

### Licensing and Support

This page covers types of Aidbox licenses and describes Aidbox Support tiers.

{% content-ref url="../../getting-started/editions-and-pricing.md" %}
[editions-and-pricing.md](../../getting-started/editions-and-pricing.md)
{% endcontent-ref %}

### Run Aidbox locally

This quickstart guide explains how to run Aidbox locally using docker compose

{% content-ref url="../../getting-started/run-aidbox-locally-with-docker/" %}
[run-aidbox-locally-with-docker](../../getting-started/run-aidbox-locally-with-docker/)
{% endcontent-ref %}

### Run Multibox locally

This quickstart guide explains how to run Multibox locally using docker compose.

{% content-ref url="../../getting-started/run-multibox-locally-with-docker.md" %}
[run-multibox-locally-with-docker.md](../../getting-started/run-multibox-locally-with-docker.md)
{% endcontent-ref %}

### Aidbox as a SaaS on AWS

{% content-ref url="../../getting-started/run-aidbox-as-a-saas/aidbox-as-a-saas-on-aws.md" %}
[aidbox-as-a-saas-on-aws.md](../../getting-started/run-aidbox-as-a-saas/aidbox-as-a-saas-on-aws.md)
{% endcontent-ref %}
