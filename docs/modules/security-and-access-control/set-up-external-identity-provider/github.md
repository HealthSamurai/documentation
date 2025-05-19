---
description: This guide shows how to set-up GitHub identity provider with Aidbox
---

# GitHub

## Register an OAuth application on GitHub

Open your organization's page on GitHub then **Settings > Developer Settings > OAuth Apps > New Org OAuth App.**&#x20;

<figure><img src="../../../../.gitbook/assets/456cfed8-3dd6-464c-9428-3b3823dbd493.png" alt=""><figcaption><p>GitHub registration OAuth app form</p></figcaption></figure>

## Generate a new secret

<figure><img src="../../../../.gitbook/assets/a2e82a5a-716e-43bb-8f24-03d42d4100ee.png" alt=""><figcaption><p>OAuth application settings</p></figcaption></figure>

## Create an IdentityProvider in Aidbox

Using REST Console create an IdentityProvider config.

```yaml
PUT /IdentityProvider/github?_format=yaml&_pretty=true
content-type: text/yaml

type: github
active: true
system: https://github.com
scopes:
  - profile
  - openid
  - read:org
  - user
organizations:
  - <your-github-organization-name>
client:
  id: <client-id>
  secret: <client-secret>
```

## Login into Aidbox with GitHub

Go to your Aidbox base URL, you will be redirected to the login page - you should see **"Sign in with \<provider.title or .type>"** button. Press this button and log in with GitHub user into aidbox.

<figure><img src="../../../../.gitbook/assets/a9ee233e-fada-4230-8ccd-bedd9643a560.png" alt=""><figcaption><p>Aidbox login page</p></figcaption></figure>

This user will be logged into Aidbox Console, but without any permissions. Read more in [Access Control Section](../security/README.md) about permissions.
