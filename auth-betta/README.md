# Auth

[Aidbox](https://www.health-samurai.io/aidbox) is coming with built-in auth module with out of the box:

* OAuth 2.0 and OpenID Connect Auth server implementation
* SignUp/SignIn workflows with email or phone number
* SignIn using user password or external Auth Providers \(like Google, Facebook, Auth0 etc\)
* SCIM User and Group management module
* Aidbox as Resource Server - access by JWT

### How to choose your flow?

#### API Consumer

If you have backend service or periodical job, which wants to interact with Aidbox API - you can use session-less [Basic Auth](basic-auth.md), [Client Credentials Grant](client-credentials.md) \(OAuth\) or [Access by JWT](access-by-jwt.md).

#### Web Application

If you want to work with Aidbox from web application or implement Single Sign On using Aidbox as Identity Server consider [Authorization Code Grant](authorization-code.md) or [Access by JWT](access-by-jwt.md)

#### Single Page Application

For SPA you can use [Implicit Grant](implicit.md), [Authorization Code Grant](authorization-code.md) without secret or [Access by JWT](access-by-jwt.md).

#### Mobile and Desktop Application

For mobile and desktop application simplest option is [Resource Owner Credentials](resource-owner-password.md)

### Auth Sandbox

Aidbox is coming with Auth Sandbox - small app, which helps you configure and test all features of Auth module in Aidbox.  Click link Auth Sandbox in Aidbox UI navigation menu:

![](../.gitbook/assets/image.png)



