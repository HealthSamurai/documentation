---
description: This article explains how Clients for Inferno can be created
---

# Adding Clients for Inferno tests

A `Client` can be added with predefined secret and without. Sometimes it's important to add a `Client` resource without predefined `id` and `secret`.

## Client with predefined secret and id

To `create` or `update` a Client with predefined `secret` and `id`.

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

## Client without predefined secret and id (patient confidential app)

To `create` a Client without predefined secret and client id use the `smartbox.portal.developer.rpc/save-developer-application` RPC method.

{% tabs %}
{% tab title="Request" %}
```http
POST /rpc
Content-Type: text/yaml

method: smartbox.portal.developer.rpc/save-developer-application
params:
  confidentiality:
    value: confidential
  name: some-app-name
  description: some-app-name
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

* `confidentiality` is a set of two options: `confidential` and `public`. In the example above the `confidential` is chosen
* `application-name` is the application name
* `description` is the application description
* `redirect-url` is the redirect uri of the application
* `launch-uri` is the launch uri pf the application
* `logo-url` is the url of the application logo file
* `org-name` is the name of the company application belongs to
* `org-url` is the url of the compamy's website
* `policy-url` is the link to the application policy page
* `tos-url` is the link to the application term of services page

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
  launch_uri: http://launch
</code></pre>
{% endtab %}
{% endtabs %}

## Client without predefined id (patient public app)

To `create` a Client without predefined `id` use the `smartbox.portal.developer.rpc/save-developer-application` RPC method.

{% tabs %}
{% tab title="Request" %}
```http
POST /rpc
Content-Type: text/yaml

method: smartbox.portal.developer.rpc/save-developer-application
params:
  confidentiality:
    value: public
  name: public-app-name
  description: public-app-name
  redirect-url: http://public.redirect
  launch-url: http://public.launch
  logo-url: http://public.logo
  org-name: public-org-name
  org-url: http://public.org
  policy-url: http://public.policy
  tos-url: http://public.tos
```
{% endtab %}

{% tab title="Response" %}
```http
status: 200

result:
  id: 068c221b-65e5-496c-82d0-53874d3d8714
  message: Application saved successfully
```
{% endtab %}
{% endtabs %}

### Request parameters

* `confidentiality` is a set of two options: `confidential` and `public`. In the example above the `public` is chosen
* `application-name` is the application name
* `description` is the application description
* `redirect-url` is the redirect uri of the application
* `launch-uri` is the launch uri pf the application
* `logo-url` is the url of the application logo file
* `org-name` is the name of the company application belongs to
* `org-url` is the url of the compamy's website
* `policy-url` is the link to the application policy page
* `tos-url` is the link to the application term of services page

## Client for provider usage

Provider SMART App must have a tenant link (see [this](multitenancy-approach.md) and [that](what-is-tenant.md) articles). To create a Client for provider use the following request.

{% tabs %}
{% tab title="Request" %}
```http
POST  /Client
Content-Type: text/yaml

type: provider-facing-smart-app 
name: provider-app-name
description: provider-app-description
grant_types:
   - authorization_code
resourceType: Client
smart:
  launch_uri: http://launch
auth:
  authorization_code:
    pkce: true
    redirect_uri: http://redirect
    refresh_token: true
    secret_required: false
    access_token_expiration: 300
details:
  logo-uri: http://logo
meta:
  tenant: my-clinic
```
{% endtab %}

{% tab title="Response" %}
```http
status: 201

description: provider-app-description
meta:
  _tenant: my-clinic
name: provider-app-name
type: provider-facing-smart-app
grant_types:
  - authorization_code
resourceType: Client
auth:
  authorization_code:
    pkce: true
    redirect_uri: http://redirect
    refresh_token: true
    secret_required: false
    access_token_expiration: 300
details:
  logo-uri: http://logo
id: dfd4d79b-b3e3-4b66-a890-3059fc81f37f
smart:
  launch_uri: http://launch
```
{% endtab %}
{% endtabs %}

{% hint style="info" %}
Notice `meta.tenant` in the request containing the id of the tenant

Formerly used `meta._tenant`
{% endhint %}

## Client for Bulk API

Bulk API Client must have a tenant link (see [this](multitenancy-approach.md) and [that](what-is-tenant.md) articles). To create a Bulk API Client use the following request.

{% tabs %}
{% tab title="Request" %}
```http
POST /Client

name: bulk-app-name
jwks_uri: http://jwks-uri
auth:
  client_credentials:
    client_assertion_types:
      - urn:ietf:params:oauth:client-assertion-type:jwt-bearer
    access_token_expiration: 300
type: bulk-api-client
scope:
  - system/*.read
grant_types:
  - client_credentials
meta:
  _tenant: my-clinic
```
{% endtab %}

{% tab title="Response" %}
```http
Status: 201

meta:
  _tenant: my-clinic
name: bulk-app-name
type: bulk-api-client
grant_types:
  - client_credentials
resourceType: Client
scope:
  - system/*.read
auth:
  client_credentials:
    client_assertion_types:
      - urn:ietf:params:oauth:client-assertion-type:jwt-bearer
    access_token_expiration: 300
id: bb88c359-e080-4bc8-9418-47536f1c49f5
jwks_uri: http://jwks-uri
```
{% endtab %}
{% endtabs %}

{% hint style="info" %}
Notice `meta._tenant` in the request containing the id of the tenant
{% endhint %}
