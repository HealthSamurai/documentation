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
 id: "c25449fdaf4645feabcc88ef87d1b150"
 secret: "bc429e0cf75249c9be9b43efbb953b2f"
```

