---
description: >-
  This guide will help you migrate from the licenses issued on the legacy
  License server to the new version which you can get on the Aidbox User Portal.
---

# Migrate from legacy licence portal to Aidbox portal

#### Register on the Aidbox User Portal

The Aidbox User Portal allows you to create Aidbox licenses and manage all the licenses for your projects in one place. To sign up go to aidbox.app and follow the instructions.

#### Create a project

* [Projects](../../overview/aidbox-user-portal/projects.md)

#### Get a new license

* [Licenses](../../overview/aidbox-user-portal/licenses.md)

* **Production licenses** are licenses for Production or Staging environments. The default expiration of these licenses is 2 weeks but Health Samurai team will extend the expiration according to the agreement.
* **Development licenses** are available for your local development. They don’t have an expiration but do impose a database size limit and cannot be used for PHI data.&#x20;

#### Update your Aidbox configuration with your license key

The difference between the legacy and new licenses is how they are initialized in the configuration file. Whereas legacy ones have a _license id_ and a _license key_, the new ones only have a _license key_.

Follow these steps to migrate to the new license:

* On the User Portal choose a license you want to use and copy its credentials by clicking on the **License key** string.
* Stop your Aidbox instance
* Remove the following section from the .env file:

```shell
AIDBOX_LICENSE_ID=<your-license-id>
AIDBOX_LICENSE_KEY=<your-license-key>
```

* Add this line with the key of the new license you copied from the portal:

```shell
AIDBOX_LICENSE=<your-license-key>
```

* Upgrade and start your Aidbox instance
