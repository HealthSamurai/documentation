---
description: Configure Single Sign-On with Google, Okta, Auth0, or any OIDC provider using Aidbox IdentityProvider resource.
---

# SSO with External Identity Provider

## What is SSO?

**Single Sign-On (SSO)** lets users log in once and access multiple systems without having to log in again. It's like having one key that unlocks many doors.

## How SSO Works with Aidbox UI&#x20;

Aidbox can connect to external identity providers (IdPs) like Google, Auth0, Okta, or your organization's identity system. This means:

1. Users log in through the identity provider they already use
2. The identity provider confirms who they are
3. Aidbox trusts this confirmation and allows the User to log in

## How Aidbox Integrates with External Identity Providers

Aidbox uses a custom `IdentityProvider` resource to configure and manage connections to external identity systems. This resource provides a unified way to integrate with various identity protocols.

#### Aidbox IdentityProvider Resource

The `IdentityProvider` resource allows Aidbox to connect with external authentication systems by configuring:

* Connection parameters to the external system
* How to map external identities to Aidbox users
* Authentication flows and redirect handling

## Setting Up SSO in Aidbox

The general steps to set up SSO are:

1. Register Aidbox as an application with your external identity provider
2. Create an `IdentityProvider` resource in Aidbox with the connection details
3. Set up access policies to determine what authenticated users can do
4. Map external user identities to Aidbox users and roles

## User Experience

From the user's perspective, the login process is simple:

1. User clicks "Log In" in your Aidbox application
2. They're redirected to the identity provider's login page
3. After a successful login, they're sent back to Aidbox
4. Aidbox recognizes them and provides appropriate access



{% hint style="info" %}
IdentityProvider fields `client.secret`, `client.private-key`, and `client.certificate` support [external secrets](../../configuration/secret-files.md) — store sensitive values outside the database using vault config files.
{% endhint %}

## See Also

{% content-ref url="../../tutorials/security-access-control-tutorials/managing-admin-access-to-the-aidbox-ui-using-okta-groups.md" %}
[managing-admin-access-to-the-aidbox-ui-using-okta-groups.md](../../tutorials/security-access-control-tutorials/managing-admin-access-to-the-aidbox-ui-using-okta-groups.md)
{% endcontent-ref %}

{% content-ref url="../../tutorials/security-access-control-tutorials/keycloak.md" %}
[keycloak.md](../../tutorials/security-access-control-tutorials/keycloak.md)
{% endcontent-ref %}

{% content-ref url="../../tutorials/security-access-control-tutorials/github.md" %}
[github.md](../../tutorials/security-access-control-tutorials/github.md)
{% endcontent-ref %}

{% content-ref url="../../tutorials/security-access-control-tutorials/azure-ad.md" %}
[azure-ad.md](../../tutorials/security-access-control-tutorials/azure-ad.md)
{% endcontent-ref %}

{% content-ref url="../../tutorials/security-access-control-tutorials/azure-ad-with-certificate-authentication.md" %}
[azure-ad-with-certificate-authentication.md](../../tutorials/security-access-control-tutorials/azure-ad-with-certificate-authentication.md)
{% endcontent-ref %}

{% content-ref url="../../tutorials/security-access-control-tutorials/microsoft-ad-fs.md" %}
[microsoft-ad-fs.md](../../tutorials/security-access-control-tutorials/microsoft-ad-fs.md)
{% endcontent-ref %}

{% content-ref url="../../tutorials/security-access-control-tutorials/apple.md" %}
[apple.md](../../tutorials/security-access-control-tutorials/apple.md)
{% endcontent-ref %}

{% content-ref url="../../tutorials/security-access-control-tutorials/aidbox.md" %}
[aidbox.md](../../tutorials/security-access-control-tutorials/aidbox.md)
{% endcontent-ref %}
