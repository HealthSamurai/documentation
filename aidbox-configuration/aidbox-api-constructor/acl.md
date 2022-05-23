---
description: Access control lists with API constructor
---

# ACL

`aidbox.rest.acl` provides a set of operations supporting SQL `:filter`s to be added on each request. `:filter`s can be used to restrict access to resources

#### Examples

* [Aidbox project with API constructor ACL](https://github.com/Aidbox/aidbox-project-samples/tree/main/aidbox-project-samples/acl)
* [Aidbox project with API constructor multitenancy on ACL](https://github.com/Aidbox/aidbox-project-samples/tree/main/aidbox-project-samples/multitenancy)

{% hint style="info" %}
Code examples on this page are taken from the ACL and the multitenancy example projects
{% endhint %}

## Overview

### Available `aidbox.rest/op-engine`s

Expects the same as regular FHIR API engines and also a `:filter`

* `aidbox.rest.acl/search`
* `aidbox.rest.acl/create`
* `aidbox.rest.acl/read`
* `aidbox.rest.acl/update`
* `aidbox.rest.acl/delete`

#### Example

```clojure
 search-observation
 {:zen/tags #{aidbox.rest/op}
  :engine   aidbox.rest.acl/search
  :resource "Observation"
  :format   "fhir"
  :filter   observation-filter}
```

### Filter

An ACL operation requires `:filter` to be specified. A `filter` requires to define `:expression` which will be added to a SQL formed by the operation. `:expression` is made of `templates` joined with `:and` or `:or` operators. A `filter` optionally accepts `:filter-table`

#### Example

```clojure
 observation-filter
 {:zen/tags     #{aidbox.rest.acl/filter}
  :filter-table acl-box.acl/acl-table
  :expression   [:and acl-box.acl/user-condition
                 acl-box.acl/subject-conditon]}
```

### Filter table

Filter table defines SQL table to be joined or searched in with SQL templates.

#### Example

```clojure
 acl-table
 {:zen/tags #{aidbox.rest.acl/filter-table}
  :schema   "public" ;; Custom resource table acl-box.custom-resources/PatientAccess
  :name     "patientaccess"}
```

### Template

Defines SQL template string. Accepts params. In the template string you can refer to variables with `{{<var>}}` syntax. Available variables:

* &#x20;`params` can be referred with `{{params.<path>.<to>.<param>}}` syntax.
* &#x20;`{{filter-table}}`  is the `:filter-table` added to the `filter`
* `{{target-resource}}` is the jsonb of a resource being checked
* `{{target-id}}` is the id of the resource

#### Example

```clojure
 user-condition
 {:zen/tags #{aidbox.rest.acl/sql-template}
  :params   {:user-id user-id-param}
  :template "{{filter-table}}.resource#>>'{user, id}' = {{params.user-id}}"}

 subject-conditon
 {:zen/tags #{aidbox.rest.acl/sql-template}
  :template "{{target-resource}}#>>'{subject, id}' = {{filter-table}}.resource#>>'{patient, id}'"}
  
 id-conditon
 {:zen/tags #{aidbox.rest.acl/sql-template}
  :template "{{target-id}} = {{filter-table}}.resource#>>'{patient, id}'"}
```

### Parameter

Defines a path in a request where to get data. The data can be used in a SQL template

#### Example

```clojure
 user-id-param
 {:zen/tags      #{aidbox.rest.acl/request-param}
  :source-schema {:type zen/string}
  :path          [:user :id]}
```
