---
description: Block user authentication in Aidbox by setting the inactive property to prevent login access.
---

# Prohibit user to login

{% hint style="info" %}
`User` resource has `inactive` property. When it equals to `true` the user cannot login. Login attempts return `400` with `"error": "invalid_grant"` and `"error_description": "User is inactive"`.
{% endhint %}

### Using the API

#### Lock a user account

```http
PATCH /User/my-user
Content-Type: application/json

{
  "inactive": true
}
```

#### Unlock a user account

```http
PATCH /User/my-user
Content-Type: application/json

{
  "inactive": false
}
```

{% hint style="warning" %}
Aidbox does not automatically lock accounts after failed login attempts. Setting `inactive` to `true` is the only lockout mechanism.
{% endhint %}

### Using the UI

1. Click the `Users` link in the left sidebar
2. Filter users list and click the desired user
3. Add `inactive` property value `true`
4. Press the `Save` button
