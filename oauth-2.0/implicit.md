# Implicit

### Prepare

Create Client resource

{% tabs %}
{% tab title="Request" %}
```javascript
POST [base]/Client
```
{% endtab %}

{% tab title="Response" %}

{% endtab %}
{% endtabs %}

Create User resource

### Authentication request

```javascript
GET [base]/oauth2/authorize
    response_type=token id_token
    &scope=openid profile
    &client_id=oauth-client
    &redirect_uri=http://my.super.app
```







