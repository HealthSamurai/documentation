# Authentication Tutorial

## Basic Auth

The simplest way to interact with Aidbox API from your confidential \(backend\) code is a Basic Auth

To start you have to register in Aidbox Auth Client with secret and basic flow enabled in this form:

![Auth Client form](../../.gitbook/assets/image%20%2879%29.png)

![PUT Client/basic](../../.gitbook/assets/image%20%2872%29%20%281%29.png)

Let's create AccessPolicy for this Client

![PUT AcessPolicy/basic-policy](../../.gitbook/assets/image%20%2862%29.png)

![GET /Patient](../../.gitbook/assets/image%20%2870%29.png)

## Client Credentials Flow

Client get get access by OAuth 2.0 Client Credentials flow

To start you have to register in Aidbox OAuth Client with secret and client\_credentials flow enabled

![OAuth Client form](../../.gitbook/assets/image%20%2875%29.png)

Before you start - create a Client

![](../../.gitbook/assets/image%20%2863%29.png)

And policy for this client

![](../../.gitbook/assets/image%20%2867%29.png)

![](../../.gitbook/assets/image%20%2876%29.png)

#### When you have a token!

![](../../.gitbook/assets/image%20%2882%29.png)

Now using this access token we can query Aidbox

![](../../.gitbook/assets/image%20%2886%29.png)

## Resource Owner Flow

This flow works for mobile and desktop apps.

Before you start - create a Client and User

![](../../.gitbook/assets/image%20%2877%29.png)

![](../../.gitbook/assets/image%20%2859%29.png)

![](../../.gitbook/assets/image%20%2888%29.png)

#### When you have a token!

![](../../.gitbook/assets/image%20%2868%29.png)

Now using this access token we can query Aidbox

![](../../.gitbook/assets/image%20%2864%29.png)

## Authorization Code Flown

This flow works for web and single page apps

Before you start - create a Client and User

![](../../.gitbook/assets/image%20%2861%29.png)

![](../../.gitbook/assets/image%20%2860%29.png)

Now we have to go to authorize endpoint to get code

 ``[`/auth/authorize?client_id=web-app&response_type=code`](javascript:void%280%29)\`\`

![](../../.gitbook/assets/image%20%2883%29.png)

#### When you have a token!

![](../../.gitbook/assets/image%20%2868%29.png)

Now using this access token we can query Aidbox

![](../../.gitbook/assets/image%20%2864%29.png)

## Implicit Flow

This flow works for single page apps. Before you start - create a Client

![](../../.gitbook/assets/image%20%2884%29.png)

Now we have to go to authorize endpoint to get code

 ``[`/auth/authorize?client_id=web-app&response_type=code`](javascript:void%280%29)\`\`

#### When you have a token!

![](../../.gitbook/assets/image%20%2868%29.png)

Now using this access token we can query Aidbox

![](../../.gitbook/assets/image%20%2864%29.png)

## JWT Auth

If you use external server, which provides you with JWT access token - you can configure Aidbox to trust this JWT token and act as Resource Server

 _You can use for example this_ [_app to generate test JWT_](http://jwtbuilder.jamiekurtz.com/)\_\_

First of all you have to register TokenIntrospector in Aidbox

![](../../.gitbook/assets/image%20%2869%29.png)

![](../../.gitbook/assets/image%20%2885%29.png)

## SMART on FHIR Flow

![](../../.gitbook/assets/image%20%2878%29.png)

#### When you have a token!

![](../../.gitbook/assets/image%20%2868%29.png)

Now using this access token we can query Aidbox

![](../../.gitbook/assets/image%20%2864%29.png)

