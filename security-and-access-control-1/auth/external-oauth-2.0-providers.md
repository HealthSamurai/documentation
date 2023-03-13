# External Oauth 2.0 Providers

In order to add external OAuth 2.0 Provider integration, you have to create a resource called IdentityProvider. It will be used by the auth module to generate redirect links and make API calls to the provider to retrieve access token, user data, etc. All examples in this tutorial are executable in Aidbox REST Console.

```yaml
POST /IdentityProvider
Accept: text/yaml
Content-Type: text/yaml

resourceType: IdentityProvider
system: https://google.com
active: true
id: <provider-id>
authorize_endpoint: https://accounts.google.com/o/oauth2/v2/auth
token_endpoint: https://www.googleapis.com/oauth2/v4/token
userinfo_endpoint: https://www.googleapis.com/oauth2/v1/userinfo
scopes:
 - https://www.googleapis.com/auth/userinfo.profile
 - https://www.googleapis.com/auth/userinfo.email
client:
 id: <your auth client id>
 secret: <your auth client secret>
```

| attribute           | description                                                                                                                                                                                                                              |
| ------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| system              | adds identifier for the created user with this system                                                                                                                                                                                    |
| authorize\_endpoint | OAuth Provider authorization endpoint                                                                                                                                                                                                    |
| token\_endpoint     | OAuth Provider access token endpoint                                                                                                                                                                                                     |
| userinfo\_endpoint  | OAuth Provider user profile endpoint                                                                                                                                                                                                     |
| userinfo\_header    | <p>Some providers require different prefix then "Bearer" for Authorization header in user info request. Fox example, if set to "OAuth" results in:</p><p>GET /&#x3C;userinfo_endpoint> with Authorization: Oauth &#x3C;access token></p> |
| scopes              | array of scopes for which you request access from user                                                                                                                                                                                   |
| client.id           | id of the client you registered in OAuth Provider API                                                                                                                                                                                    |
| client.secret       | secret of the client you registered in OAuth Provider API                                                                                                                                                                                |

Next, we have to create Client resource which will receive access token from Aidbox backend later on and use Aidbox API on behalf of the user. We enable the authorization\_code flow for the application and provide the redirect\_uri.&#x20;

```yaml
POST /Client
Accept: text/yaml
Content-Type: text/yaml

id: my-client
grant_types: ["authorization_code"]
first_party: true
auth:
 authorization_code:
  redirect_uri: <your app redirect uri>
```

You will also need to register /auth/callback/\<provider-id> as callback URI in your OAuth provider client application configuration.&#x20;

To initiate authorization, redirect the user to the endpoint /auth/redirect/\<provider-id>. You should provide at least two query parameters client\_id and response\_type. The following API interactions happen as a result:

```yaml
GET /auth/redirect/google?client_id=my-client&response_type=code
# your application entrypoint
# redirects to

GET https://accounts.google.com/o/oauth2/v2/auth?...
# user enters his credentials, allows aidbox access to his profile data
# provider redirects to

GET /auth/callback/google?...
# aidbox receives temporary token, exchanges it on access token by calling

GET https://www.googleapis.com/oauth2/v4/token?...

# using access token, aidbox calls user data endpoint
GET https://www.googleapis.com/oauth2/v1/userinfo
# then it creates User resource and continues with the flow specified
# in response_type query param

GET <your app redirect uri>?code=...

```

By default, everything that is returned by provider's userinfo endpoint gets stored into User.data. You can also configure mapping to other User attributes by adding 'toScim' object into IdentityProvider.

```yaml
PUT /IdentityProvider/<provider-id>
Accept: text/yaml
Content-Type: text/yaml

toScim:
 default_email:
  - email
 first_name:
  - name
  - givenName
 last_name:
  - name
  - givenName
```

Each key here refers to the key in the userinfo response object, while value is an array that specifies path in User resource.
