---
description: This guide will help you migrate from using the license type issued on the old License server to the new version which you can get on the Aidbox User Portal.
---

# License migration guide

### Register on the Aidbox User Portal 
The new [User Portal](aidbox.app) allows you to create Aidbox licenses and manage all the licenses for your projects in one place. To sign up just go to aidbox.app and follow the instructions.

### Create a project
[Projects](../../overview/aidbox-user-portal/projects.md) allow you to organize your licenses in groups and invite your team members to these groups. Click **+New project** on the main sidebar to create a project. You can [invite your team members](../../overview/aidbox-user-portal/members.md) in the Members tab and edit your project in the Settings tab.

### Issue a new license
You can [issue and manage your licenses](../../overview/aidbox-user-portal/licenses.md) in the Licenses tab in your project. To create a new license click on **New license** and specify which one you need.
There are several types of licenses available for you. 
    * **Standard licenses** are licenses for Production or Staging environments. 
    Default expiration of these licenses is 2 weeks but Health Samurai team will extend the expiration according to the agreement. 
    * **Development licenses** are available for your local development. 
    They don’t have an expiration but do impose a database size limit and cannot be used for PHI data. As a contract client you can issue these free of charge.
	For more information please refer to [this doc](../../overview/aidbox-user-portal/licenses.md) or contact your account manager. 

### Make changes to your Aidbox configuration.
The difference between the old license and a new license is the way they are initialized in the configuration file. Whereas old ones have a _license id_ and a _license key_, the new one only has a _license key_. 

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

