---
description: This article describes how to manage Aidbox licenses
---

# Licenses

## Create license

### Create self-hosted license

1. On the main navigation sidebar of the [portal](https://aidbox.app/ui/portal#/profile/settings), click on the _**project name**_
2. On the menu that opens, click _**Assets**_
3. In the upper right corner of the page, click _**New Aidbox**_
4. Select the license type: _**Dev**_ or _**Prod**_
5. Enter the Aidbox instance _**License name**_
6. Select Goal: _**Development, Research,**_ or _**Evaluation**_
7. Specify _Hosting_ as **Self-hosted**
8. Click _**Create**_

[This quickstart guide](../../getting-started/run-aidbox-locally.md) explains how to run Aidbox locally using docker compose

### Create a hosted Aidbox instance

1. On the main navigation sidebar of the [portal](https://aidbox.app/ui/portal#/profile/settings), click on the _**project name**_
2. On the menu that opens, click _**Assets**_
3. In the upper right corner of the page, click _**New Aidbox**_
4. Select the license type: _**Dev**_ or _**Prod**_
5. Enter the Aidbox instance _**License name**_
6. Select Goal: _**Development, Research,**_ or _**Evaluation**_
7. Specify _Hosting_ as **Sandbox**
8. Specify _Aidbox version_
   * Latest
   * Edge
9. Specify **FHIR Version (**_we recommend using **4.0.1** by default_**)**
10. Click _**Create**_

### Create AWS license

{% content-ref url="../../getting-started/run-aidbox-on-aws.md" %}
[run-aidbox-on-aws.md](../../getting-started/run-aidbox-on-aws.md)
{% endcontent-ref %}

## Delete license

1. On the main navigation sidebar of the [portal](https://aidbox.app/ui/portal#/profile/settings), click on the _**project name**_
2. On the menu that opens, click _**Assets**_
3. Click on a license in the list
4. Information about the selected license will appear on the right side of the screen. Click _**Delete this License**_
5. In the confirmation window, type the license name and click _**Delete this License**_

## Manage Aidbox licenses via API

Licences API on Aidbox portal allows you to issue Aidbox licenses on-premise installation (self-hosted) and automate the process of deploying new Aidbox instances locally or in your infrastructure.

To run queries present below, you can use some tool to run http requests (e.g. Postman).

### Issue token

To access the API, you have to issue a token, bound with the project, through the Aidbox [portal](https://aidbox.app/):

1. On the main navigation sidebar, click on the _**project name**_.
2. On the menu that opens, click _**Settings**_.
3. Click button _**Issue Token**._

### Create license

```yaml
POST https://aidbox.app/rpc
content-type: text/yaml
accept: text/yaml

method: portal.portal/issue-license
params: 
  token: <your-token>
  name: <license-name>
  product: aidbox 
  # production | development | ci
  type: production
```

### Delete license

There are two options for deleting a license:

by **license id**

```yaml
POST https://aidbox.app/rpc
content-type: text/yaml
accept: text/yaml

method: portal.portal/remove-license
params: 
  token: <your-token>
  id: <license-id>
```

by **license string**

```yaml
POST https://aidbox.app/rpc
content-type: text/yaml
accept: text/yaml

method: portal.portal/remove-license
params: 
  token: <your-token>
  license: <license-string>
```

### Get license

Retrieve license:

by **license id**

```yaml
POST https://aidbox.app/rpc
content-type: text/yaml
accept: text/yaml

method: portal.portal/get-license
params: 
  token: <your-token>
  id: <license-id>
```

by **license string**

```yaml
POST https://aidbox.app/rpc
content-type: text/yaml
accept: text/yaml

method: portal.portal/get-license
params: 
  token: <your-token>
  license: <license-string>
```

### Get list of licenses

Retrieve all licenses associated with a project.

```yaml
POST https://aidbox.app/rpc
content-type: text/yaml
accept: text/yaml

method: portal.portal/get-licenses
params: 
  token: <your-token>
```

## References

### Licensing and Support

This page covers types of Aidbox licenses and describes Aidbox Support tiers.

### Run Aidbox locally

This [quickstart guide](../../getting-started/run-aidbox-locally.md) explains how to run Aidbox locally using docker compose

### Aidbox as a SaaS on AWS

{% content-ref url="../../getting-started/run-aidbox-on-aws.md" %}
[run-aidbox-on-aws.md](../../getting-started/run-aidbox-on-aws.md)
{% endcontent-ref %}
