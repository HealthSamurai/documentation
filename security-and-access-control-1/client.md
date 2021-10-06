# Client

To provide programmatic access to Aidbox you have to register a client - Client resource.

Client resource must have `grant_types` attribute defining authentification scheme for this Client. 

Other required attributes are determined based on the values of this attribute `grant_types` is an array of strings, possible values are:

* basic
* client\_credentials
* password
* implicit
* authorization\_code
* code

{% hint style="info" %}
You can find different authorization flow examples in the Auth Sandbox in the Aidbox ui
{% endhint %}



