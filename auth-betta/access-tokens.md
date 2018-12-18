# Access Tokens

## Access Tokens

An Access Token is a credential that can be used by an application to access an API. Access Tokens can be either an opaque string or a JSON web token. They inform the API that the bearer of the token has been authorized to access the API and perform specific actions specified by the **scope** that has been granted.

Access Tokens should be used as a **Bearer** credential and transmitted in an HTTP **Authorization** header to the API.

## Set the Access Token Format

Aidbox generates Access Tokens in two formats:

* Opaque strings: When not using a custom API
* JSON Web Tokens \(JWTs\): When using a custom API

Set the format using the value of the **audience** parameter in the authorization request. The **audience** is a parameter set during authorization. 

### Get Access Token as an opaque string

If you do not want to use this token to access API outside Aidbox, skip the **audience** parameter  and Aidbox will make the Access Token an opaque string.

### Set Access Token format to a JWT

If you want to use Aidbox Access Tokens to access external \(non-Aidbox\) API. You can get Access Token in JWT format. JWTs contain three parts:

* **Header**: The header contains metadata about the type of token and the cryptographic algorithms used to secure its contents.
* **Claims**: The set of claims contains verifiable security statements such as the identity of the user and the permissions they are allowed.
* **Signature**: The signature is used to validate that the token is trustworthy and has not been tampered with.

Set the **audience** to your API Base URL to make the Access Token a JWT.

When the **audience** is set to a custom API and the **scope** parameter includes the `openid` value, then the generated Access Token will be a JWT valid for both retrieving the user's profile and for accessing the custom API.

Read more about Audience Information in [RFC](https://tools.ietf.org/id/draft-tschofenig-oauth-audience-00.html).

