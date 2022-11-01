---
description: This article explains how Clients for Inferno can be created
---

# Adding Clients for Inferno tests

A `Client` can be added with predefined secret and without. Sometimes it's important to add a `Client` resource without predefined `id` and `secret`.

## Client with predefined secret and id

To `create` or `update` a Client with predefined secret and client id.

```http
PUT /Client/inferno-confidential-patient-smart-app
Content-Type: text/yaml

secret: inferno-confidential-patient-smart-app-secret
type: patient-facing-smart-app
active: true
grant_types:
- authorization_code
auth:
  authorization_code:
    pkce: false
    redirect_uri: 'https://inferno.healthit.gov/suites/custom/smart/redirect'
    refresh_token: true
    secret_required: true
    access_token_expiration: 300
smart:
  launch_uri: 'https://inferno.healthit.gov/suites/custom/smart/launch'
```

* `id` of the Client is defined within the uri. It's a `inferno-confidential-patient-smart-app` value
* `secret` is defined in the request body. The secret value is `inferno-confidential-patient-smart-app-secret`

## Client without predefined secret and id (confidential app)

To `create` a Client without predefined secret and client id use the `smartbox.portal.developer.rpc/save-developer-application` RPC method.

{% tabs %}
{% tab title="Request" %}
```http
POST /rpc
Content-Type: text/yaml

method: smartbox.portal.developer.rpc/save-developer-application
params:
  application-name: some-app-name
  description: some-app-name
  confidentiality: confidential
  redirect-url: http://redirect
  launch-url: http://launch
  logo-url: http://logo
  org-name: org-name
  org-url: http://org
  policy-url: http://policy
  tos-url: http://tos
```
{% endtab %}

{% tab title="Response" %}
```http
status: 200

result:
  id: b2f66062-4beb-43c8-a32c-aa072134185c # generated id of the Created client
  message: Application saved successfully
```
{% endtab %}
{% endtabs %}

### Request parameters

* `application-name` is the application name
* `description` is the application description
* `confidentiality` is a set of two options: `confidential` and `public`. In the example above the `confidential` is chosen
* `redirect-url` is the redirect uri of the application
* `launch-uri` is the launch uri pf the application
* `logo-url` is the url of the application logo file
* `org-name` is the name of the company application belongs to
* `org-url` is the url of the compamy's website
* `policy-url` is the link to the application policy page
* `tos-url` os the link to the application term of services page

### How to get Client secret

Use `id` we received oт the previous step to request Client details. The `secret` contains generated value.

{% tabs %}
{% tab title="Request" %}
```http
GET /Client/b2f66062-4beb-43c8-a32c-aa072134185c
Content-Type: text/yaml
```
{% endtab %}

{% tab title="Response" %}
<pre class="language-yaml"><code class="lang-yaml">description: some-app-name
name: some-app-name
type: patient-facing-smart-app
grant_types:
  - authorization_code
  - basic
resourceType: Client
auth:
  authorization_code:
    redirect_uri: http://redirect
    refresh_token: true
    secret_required: true
    access_token_expiration: 300
<strong>secret: M2JmODllMjItNzBiNC00MzM5LTkyMDktMDAxMzM4MGM5OTk2
</strong>details:
  user:
    name: {}
  user-id: admin
  logo-url: http://logo
  organization:
    url: http://org
    name: org-name
  service-terms: http://tos
  privacy-policy-url: http://policy
active: true
id: >-
  b2f66062-4beb-43c8-a32c-aa072134185c
smart:
  launch_uri: http://launch</code></pre>
{% endtab %}
{% endtabs %}
