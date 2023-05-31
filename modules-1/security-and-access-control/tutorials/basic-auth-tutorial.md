# Authentication Tutorial

## Basic Auth

The simplest way to interact with Aidbox API from your confidential (backend) code is a Basic Auth

To start you have to register in Aidbox Auth Client with secret and basic flow enabled in this form:

![Auth Client form](<../../../.gitbook/assets/image (79).png>)

![PUT Client/basic](<../../../.gitbook/assets/image (50).png>)

Let's create AccessPolicy for this Client

![PUT AcessPolicy/basic-policy](<../../../.gitbook/assets/image (51).png>)

![GET /Patient](<../../../.gitbook/assets/image (52) (1).png>)

## Client Credentials Flow

Client get access by OAuth 2.0 Client Credentials flow

To start you have to register in Aidbox OAuth Client with secret and client\_credentials flow enabled

![OAuth Client form](<../../../.gitbook/assets/image (53) (1).png>)

Before you start - create a Client

![](<../../../.gitbook/assets/image (54).png>)

And policy for this client

![](<../../../.gitbook/assets/image (55).png>)

![](<../../../.gitbook/assets/image (56).png>)

#### When you have a token!

![](<../../../.gitbook/assets/image (57).png>)

Now using this access token we can query Aidbox

![](<../../../.gitbook/assets/image (58).png>)

## Resource Owner Flow

This flow works for mobile and desktop apps.

Before you start - create a Client and User

![](<../../../.gitbook/assets/image (59).png>)

![](<../../../.gitbook/assets/image (60).png>)

![](<../../../.gitbook/assets/image (62).png>)

#### When you have a token!

![](<../../../.gitbook/assets/image (63).png>)

Now using this access token we can query Aidbox

![](<../../../.gitbook/assets/image (64).png>)

## Authorization Code Flown

This flow works for web and single page apps

Before you start - create a Client and User

![](<../../../.gitbook/assets/image (65).png>)

![](<../../../.gitbook/assets/image (66).png>)

Now we have to go to authorize endpoint to get code

`/auth/authorize?client_id=web-app&response_type=code`

![](<../../../.gitbook/assets/image (67).png>)

#### When you have a token!

![](<../../../.gitbook/assets/image (63).png>)

Now using this access token we can query Aidbox

![](<../../../.gitbook/assets/image (64).png>)

## Implicit Flow

This flow works for single page apps. Before you start - create a Client

![](<../../../.gitbook/assets/image (68).png>)

Now we have to go to authorize endpoint to get code

`/auth/authorize?client_id=web-app&response_type=code`

#### When you have a token!

![](<../../../.gitbook/assets/image (63).png>)

Now using this access token we can query Aidbox

![](<../../../.gitbook/assets/image (64).png>)

## JWT Auth

If you use external server, which provides you with JWT access token - you can configure Aidbox to trust this JWT token and act as Resource Server

_You can use for example this_ [_app to generate test JWT_](http://jwtbuilder.jamiekurtz.com/)\_\_

First of all you have to register TokenIntrospector in Aidbox

![](<../../../.gitbook/assets/image (69).png>)

![](<../../../.gitbook/assets/image (70).png>)

## SMART on FHIR Flow

![](<../../../.gitbook/assets/image (73).png>)

#### When you have a token!

![](<../../../.gitbook/assets/image (63).png>)

Now using this access token we can query Aidbox

![](<../../../.gitbook/assets/image (64).png>)
