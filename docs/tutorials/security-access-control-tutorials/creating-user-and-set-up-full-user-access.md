# Creating user & set up full user access

### Creating user resource

Aidbox `user` resources conforms to following [schema](https://docs.aidbox.app/security-and-access-control-1/overview#user). You can [create](https://docs.aidbox.app/api-1/api/crud-1/fhir-and-aidbox-crud) new `user` as a regular resource.

#### User resource example

{% tabs %}
{% tab title="Request" %}
```yaml
PUT /User/exampleuser
content-type: text/yaml
accept: text/yaml

email: test@example.com
password: '123456'
```
{% endtab %}

{% tab title="Response" %}
```yaml
email: test@example.com
password: >-
  $s0$f0801$HhKy93HnLn0qSmsS2JCZnQ==$P0oAp6yx6k8bCbeFN2UT9UfjKROuggn7deLbX2Mpm2M=
id: >-
  exampleuser
resourceType: User
```
{% endtab %}
{% endtabs %}

{% hint style="info" %}
Aidbox stores only hash of submitted password in resource
{% endhint %}

Now user can use this email & password to log-in into Aidbox console

### Setting up full user access

For this purpose you need to create `AccessPolicy` resource that grants full user access.

#### AccessPolicy resource example

{% tabs %}
{% tab title="Request" %}
```yaml
PUT /AccessPolicy/allow-all
content-type: text/yaml
accept: text/yaml

engine: allow
link:
  - resourceType: User
    id: exampleuser
```
{% endtab %}

{% tab title="Response" %}
```yaml
link:
  - id: >-
      exampleuser
    resourceType: User
engine: allow
id: >-
  allow-all
resourceType: AccessPolicy
```
{% endtab %}
{% endtabs %}

{% hint style="warning" %}
Note that `allow-all` AccessPolicy created in example uses engine allow, which grants **full** access to specified user.

If you want to set up user access more granularly, please refer to [AccessPolicy documentation](../../modules/security-and-access-control/security/access-control.md)
{% endhint %}
