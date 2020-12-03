---
description: 'Integration with Yandex as OAuth 2.0 provider that conforms to [link]'
---

# Yandex

Register your application on yandex Oauth server

Create IdentityProvider resource

```text
POST /IdentityProvider

resourceType: "IdentityProvider"
type: "yandex"
id: "yandex"
authorize_endpoint: "https://oauth.yandex.ru/authorize"
scopes: 
 - "login:birthday" 
 - "login:email" 
 - "login:info" 
 - "login:avatar"
client:
 id: <your client id>
 secret: <your client secret>
```

