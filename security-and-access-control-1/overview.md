---
description: This article explains who access control performs in Aidbox
---

# Overview

## How is HTTP request processed? <a href="#how-is-http-request-processed" id="how-is-http-request-processed"></a>

When an HTTP request reaches Aidbox, it goes through several steps. The request can be rejected at any step due to checks being performed on the current step.

Set of HTTP request steps:

1. Authentication
2. Resolving route
3. Authorization
4. Request processing

![](<../.gitbook/assets/image (1).png>)

### Authentication

Authentication (AuthN) is the process of verifying the identity of a user or program. The goal is to ensure that the requestor is who they claim to be.

If Aidbox is unable to authenticate the requestor, it may reject the request with an 400 error response.

### Resolving route

Aidbox evaluates the request and determines which handler to select. If no handler is found, it returns "404 Not found" response.

### Authorization

Authorization (AuthZ or access control) is the granting or denying access to a requestor. Access control is based on the [internal representation of the request](security/access-control.md#request-object-structure). Besides other internal request contains:

* requestor identity
* the handler

Aidbox applies access policies to determine if the request is allowed to invoke the selected handler. If at least one `AccessPolicy` allows the operation, handler processes the request. Otherwise, the server returns "401 Unauthorized" response.

### Process the request

Processing the request is useful work done by the handler. When process done, Aidbox returns the output to the requestor.

## HTTP request positive scenario

When:

* [x] Requestor authenticated, and
* [x] Route is resolved, and
* [x] Desired operation is authorized

Aidbox processes the request and returns the result to the requestor.

## How authentication works

From an authentication point of view, there are two groups of operations:

1. AuthN to open a new session
2. Useful work request authentication

### Authentication to open a new session

There are several flows to initiate a session:

* User login with password flow
* User login with external identity provider
* Client login with Resource Owner Grant

During those login flows Aidbox authenticate user and client (program) requesting the session open. Session itself is needed to authentication `useful` request.

### Useful requests authentication

Useful requests are performed by clients or programs. Clients add authorization details to each request they send to Aidbox. Clients auth flows supported by Aidbox:

1. Basic auth
2. Bearer JWT
3. Bearer opaque tokens
4. Cookie ASID

#### Basic Auth

[RFC 7617](https://datatracker.ietf.org/doc/html/rfc7617) defines the "Basic" HTTP authentication method. User ID and password pairs are encodes using base64 and then transmits them as credentials.

#### Authentication with Bearer opaque token

[Opaque token](https://tools.ietf.org/html/rfc7662) is a randomly generated string thus there is no meaningful information in the token.

How Aidbox processes opaque token to authenticate the requestor:

1. Aidbox sends request to the all auth servers it knows
2. Each request asks if the token is issued by an auth server
3. If the issuer is found, it returns the details related to the token
4. Aidbox enriches the request with the requester details

#### Authentication with Bearer JWT

[JSON Web Token (JWT)](https://jwt.io/) is a signed container for claims and other details. JWT payload could be checked on fly: JWT expiration, claims and signature.

How Aidbox processes JWT token to authenticate the requestor:

1. Aidbox unpacks the JWT
2. Aidbox checks JWT expiration
3. Aidbox checks JWT signature
4. Aidbox requests the token issuer
5. Aidbox enriches the request with the requester details

#### Authentication with user cookie

When user logs in (with Aidbox credentials or external identity provider), Aidbox creates a session and sets the session cookie to the browser. Since that time all the requests done by user browser are signed with the session cookie.

When Aidbox receives a request containing session cookie, Aidbox tries to fetch session related the cookie. If session is found, Aidbox authenticate request with the details storing in the session.

| AuthN method          | External request | Local session access                                 |
| --------------------- | ---------------- | ---------------------------------------------------- |
| Basic auth            |                  | No session access but credentials are stored locally |
| Opaque token          | Yes              |                                                      |
| External JWT          | Yes              |                                                      |
| JWT issued by Aidbox  |                  | Yes                                                  |
| Cookie authentication |                  | Yes                                                  |

## How access control performs

Authorization decides if a request can be processed by the desired handler. By the start of authorization the original HTTP request is augmented with the user or client identity and the desired handler (see the [request object structure](security/access-control.md#request-object-structure)).

Aidbox applies `AccessPolicy` resources the request one after the other. It does it until any policy grants the permission.

![](<../.gitbook/assets/image (17).png>)![](<../.gitbook/assets/image (4).png>)

Access policies work as `OR` logic gate. In the example below the third policy is skipped due to the second one granted the request in.

If there is no policy allowing the request, Aidbox rejects the request with the 401 (Unauthorized) response.

### What is a public route

The route is public is anyone can access it. To make root public, create an `AccessPolicy` allowing access to it without any restrictions.
