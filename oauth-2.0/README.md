---
description: >-
  This guide describes how to obtain an access token for specific type of an
  application
---

# OAuth 2.0

## Prerequisite

* Box instance \(in cloud or Aidbox.Dev\)
* Basic knowledge about [OAuth 2.0 Authorization Framework](https://oauth.net/2/)
* Configured [Users and Clients](users-and-clients.md)

## OAuth 2.0

All Aidbox products uses OAuth 2.0 authorization framework to provide secure access to FHIR API and supports each authorization flow described in [OAuth 2.0 Authorization Framework RFC](https://tools.ietf.org/html/rfc6749).  
  
The OAuth 2.0 authorization framework enables a third-party application to obtain limited access to an HTTP service, either on behalf of a resource owner by orchestrating an approval interaction between the resource owner and the HTTP service, or by allowing the third-party application to obtain access on its own behalf. More detailed information can be found in the [OAuth 2.0 Authorization Framework RFC](https://tools.ietf.org/html/rfc6749).

### Users and Clients

{% page-ref page="users-and-clients.md" %}

### Web Application with backend

{% page-ref page="authorization-code.md" %}

The Authorization Code grant is used by server-side applications that are capable of securely storing secrets, or by native applications. Since this is a redirection-based flow, the client must be capable of interacting with the resource owner's user-agent \(typically a web browser\) and capable of receiving incoming requests \(via redirection\) from the authorization server.

### Single Page Application

{% page-ref page="implicit.md" %}

The implicit grant type is used to obtain access tokens \(it does not support the issuance of refresh tokens\) and is optimized for public clients known to operate a particular redirection URI. These clients are typically implemented in a browser using a scripting language such as JavaScript.

### Native/Mobile app

{% page-ref page="resource-owner-credentials.md" %}

The resource owner password credentials grant type is suitable in cases where the resource owner has a trust relationship with the client, such as the device operating system or a highly privileged application. The authorization server should take special care when enabling this grant type and only allow it when other flows are not viable.

### Backend/Server application

{% page-ref page="client-credentials.md" %}

The client can request an access token using only its client credentials \(or other supported means of authentication\) when the client is requesting access to the protected resources under its control, or those of another resource owner that have been previously arranged with the authorization server \(the method of which is beyond the scope of this specification\).

 

