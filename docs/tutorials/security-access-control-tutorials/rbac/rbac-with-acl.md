---
description: Implement role-based access control with ACL in Aidbox to restrict user access based on organization membership.
---

# RBAC with ACL

{% hint style="info" %}
This guide is based on the [Access control list (ACL)](../../../deprecated/deprecated/zen-related/access-control-lists-docs-acl.md)
{% endhint %}

## The goal

When a user with the role `OrganizationAdmin` performs read request for some user like `GET /User/<user-id>`, Aidbox checks if `organization.id` of the requester and the desired user has the same organization reference. If the organization is the same, Aidbox allows otherwise restricts access.

## Init Aidbox configuration project

To set new [Adibox configuration project](../../../deprecated/deprecated/zen-related/aidbox-zen-lang-project/)

{% hint style="info" %}
It is important to syncronize directory and file name to the `:ns` parameter of the configuration
{% endhint %}

Create an empty directory `acl`

```bash
mkdir acl 
```

Create file `system.edn` in new folder

```bash
cd acl && touch system.edn
```

## Populate configuration file

{% hint style="warning" %}
In the following configuration project user and client credentials are written as plain text to simplify the topic. In real life scenarios it is important to define credentials with ENVs
{% endhint %}

```edn
{:ns     acl.system
 :import #{aidbox
           aidbox.config
           aidbox.rest
           aidbox.rest.acl}

 search-config
 {:zen/tags         #{aidbox.config/search}
  :zen-fhir         :disable
  :resource-compat  false
  :engine           :knife
  :fhir-comparisons true
  :default-params   {:timeout 30
                     :total   "none"
                     :count   100}
  :chain            {:subselect true}}

 compatibility-config
 {:zen/tags   #{aidbox.config/compatibility}
  :validation {:json-schema {:regex #{:fhir-datetime}}}
  :auth       {:pkce {:code-challenge {:s256 {:conformant true}}}}}

 zen-config
 {:zen/tags                #{aidbox.config/config}
  :search                  search-config
  :compatibility           compatibility-config

  :fhir-version            "4.0.1"
  :compliant-mode-enabled? true
  :override-createdat-url  "http://fhir.aidbox.app/extension/createdat"
  :correct-aidbox-format   true
  :disable-legacy-seed     true}

 seed-data
 {:zen/tags #{aidbox/service}
  :engine   aidbox/seed-v2
  :resources
  {:Client {:root {:secret      "secret"
                   :first_party true
                   :grant_types ["client_credentials" "basic"]}

            :postman {:secret      "secret"
                      :grant_types ["password" "basic"]}}

   :Organization {:org-1 {:resourceType "Organization" :id "org-1"}
                  :org-2 {:resourceType "Organization" :id "org-2"}}

   :User {:admin {:password "password"}

          :admin-org-1 {:password "password" :organization {:resourceType "Organization" :id "org-1"}}

          :user-org-1 {:organization {:resourceType "Organization" :id "org-1"}}

          :user-org-2 {:organization {:resourceType "Organization" :id "org-2"}}}

   :AccessPolicy {:admin-seed-policy
                  {:engine "allow"
                   :link   [{:resourceType "User" :id "admin"}
                            {:resourceType "Client" :id "root"}]}

                  :org-admin-can-read-its-users
                  {:engine "matcho"
                   :roleName "OrganizationAdmin"
                   :matcho {:uri "#/User.+" :request-method "get"}}}

   :Role {:OrganizationAdmin {:name "OrganizationAdmin"
                              :user {:resourceType "User" :id "admin-org-1"}}}}}

 ;; define additional filtering rules based

 ;; org-id-param value is taken from the requester user resource accroding to the :path
 org-id-param
 {:zen/tags      #{aidbox.rest.acl/request-param}
  :source-schema {:type zen/string}
  :path          [:user :organization :id]}

 ;; defined SQL statement injecting the org-id-param value 
 org-condition
 {:zen/tags #{aidbox.rest.acl/sql-template}
  :params   {:org-id org-id-param}
  :template "{{target-resource}}#>>'{organization,id}'={{params.org-id}}"}

 ;; attach injected SQL to the filter 
 user-filter
 {:zen/tags   #{aidbox.rest.acl/filter}
  :expression org-condition}

 ;; create user-read operation & apply user-filer
 user-read
 {:zen/tags #{aidbox.rest/op}
  :engine   aidbox.rest.acl/read
  :resource "User"
  :format   "aidbox"
  :filter   user-filter}

 ;; rebing standard User read operation
 user-api
 {:zen/tags #{aidbox.rest/api}
  "User"    {[:id] {:GET user-read}}}

 ;; attach api to the server
 server
 {:zen/tags #{aidbox/service}
  :engine   aidbox/http
  :apis     #{user-api}}

 box
 {:zen/tags #{aidbox/system}
  :config   zen-config
  :services {:seed seed-data
             :http server}}}
```

## Check configuration works

Use you favorite REST client

### Get access token for `admin-org-1` user

{% tabs %}
{% tab title="request access_token for user admin-org-1" %}
```yaml
POST [base-url]/auth/token
Content-Type: text/yaml

{
  "client_id": "postman",
  "client_secret": "secret",
  "username": "admin-org-1",
  "password": "password",
  "grant_type": "password"
}
```
{% endtab %}

{% tab title="response status: 200" %}
```yaml
token_type: Bearer
userinfo:
    organization: { id: org-1, resourceType: Organization }
    id: admin-org-1
    resourceType: User
    sub: admin-org-1
need_patient_banner: true
access_token: MW...Ex
```
{% endtab %}
{% endtabs %}

### Read user belonging to the requester organization

{% tabs %}
{% tab title="fetch allowed user data" %}
```yaml
GET [base-url]/User/user-org-1
Content-Type: text/yaml
authorization: "Bearer MW...Ex"
```
{% endtab %}

{% tab title="response status: 200" %}
```yaml
organization: { id: org-1, resourceType: Organization }
id: user-org-1
resourceType: User
```
{% endtab %}
{% endtabs %}

### Read attempt user from the different organization

{% tabs %}
{% tab title="fetch allowed user data" %}
```yaml
GET [base-url]/User/user-org-2
Content-Type: text/yaml
authorization: "Bearer MW...Ex"
```
{% endtab %}

{% tab title="response status: 404" %}
```yaml
resourceType: OperationOutcome
id: deleted
text:
  status: generated
  div: "Resource User/user-org-2 not found"
issue:
  - severity: fatal
    code: deleted
    diagnostics: "Resource User/user-org-2 not found"
```
{% endtab %}
{% endtabs %}
