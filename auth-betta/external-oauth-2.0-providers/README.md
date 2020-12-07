# External Oauth 2.0 Providers

In order to add external OAuth 2.0 Provider integration, you have to create a resource called IdentityProvider. It will be used by auth module to generate redirect links and make API calls to retrieve access token, user data, etc. All examples in this tutorial are executable in Aidbox REST Console.

```text
POST /IdentityProvider

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

<table>
  <thead>
    <tr>
      <th style="text-align:left">attribute</th>
      <th style="text-align:left">description</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td style="text-align:left">system</td>
      <td style="text-align:left">adds identifier for the created user with this system</td>
    </tr>
    <tr>
      <td style="text-align:left">authorize_endpoint</td>
      <td style="text-align:left">OAuth Provider authorization endpoint</td>
    </tr>
    <tr>
      <td style="text-align:left">token_endpoint</td>
      <td style="text-align:left">OAuth Provider access token endpoint</td>
    </tr>
    <tr>
      <td style="text-align:left">userinfo_endpoint</td>
      <td style="text-align:left">OAuth Provider user profile endpoint</td>
    </tr>
    <tr>
      <td style="text-align:left">userinfo_header</td>
      <td style="text-align:left">
        <p>Some providers require different prefix then &quot;Bearer&quot; for Authorization
          header in user info request. Fox example, if set to &quot;OAuth&quot; results
          in:</p>
        <p>GET /&lt;userinfo_endpoint&gt; with Authorization: Oauth &lt;access token&gt;</p>
      </td>
    </tr>
    <tr>
      <td style="text-align:left">scopes</td>
      <td style="text-align:left">array of scopes for which you request access from user</td>
    </tr>
    <tr>
      <td style="text-align:left">client.id</td>
      <td style="text-align:left">id of the client you registered in OAuth Provider API</td>
    </tr>
    <tr>
      <td style="text-align:left">client.secret</td>
      <td style="text-align:left">secret of the client you registered in OAuth Provider API</td>
    </tr>
  </tbody>
</table>

