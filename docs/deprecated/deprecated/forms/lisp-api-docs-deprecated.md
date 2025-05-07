---
hidden: true
---

# Lisp API (docs deprecated)

* [lisp/get-specification](lisp-api-docs-deprecated.md#lisp-get-specification) - get the specification which is distributed in form of a test suite
* [lisp/eval-lisp](lisp-api-docs-deprecated.md#lisp-eval-lisp) - evaluate lisp expression within the provided execution context (`data` or `resource`).

### lisp/get-specification

Params:

| Param   | Description                                            | Type          | required? | default |
| ------- | ------------------------------------------------------ | ------------- | --------- | ------- |
| runtime | Filter \[client or server]-only tests or get all tests | zenbox/string | no        | client  |

Request:

```
POST /rpc?

method: lisp/get-specification
params:
    runtime: server
```

Response:

```
result:
  - spec-edn: ... "complete zen specification in ZEN format"
    fn-spec: ... "zen specification of a test function"
    test-suite: ... "complete test suite with AST expressions in JSON format"
```

Server responds with `HTTP 422 Unprocessable Entity` if wrong params are provided.

### lisp/eval-lisp

{% hint style="info" %}
Some expressions are runtime specific. See language specification for more details.
{% endhint %}

Params:

| Param    | Description                            | Type            | required? |
| -------- | -------------------------------------- | --------------- | --------- |
| expr     | Lisp expression                        | zen/string      | yes       |
| resource | Resource the lisp expression refers to | zenbox/Resource | no        |
| data     | Data the lisp expression refers to     | zen/map         | no        |

The lisp expression `expr` can be used to retrieve data directly from the provided `data` map.

Request:

```
POST /rpc?

method: lisp/eval-lisp
params:
    expr: (str "Hello " (get-in [:name 0 :given 0]))
    data:
        name:
            - given:
                - John
```

Response:

```
result: Hello John
```

Or the expression `expr` can be used for example to retrieve data via `sql` function for a given `resource`.

> The `sql` functions are specified for `backend` lisp runtime only.

Request:

```
POST /rpc?

method: lisp/eval-lisp
params:
    expr: (sql {:select :* :from :patient})
    resource:
        id: p1
        resourceType: Patient
```

Response:

```
result:
  address: ...
  name:
    - given:
        - Morgan
      family: James
  birthDate: '1952-01-01'
  resourceType: Patient
  id: p1
  identifier: ...
  gender: male
  maritalStatus: ...
```

The expression `expr` can also be used to grab data from a particular Resource.

Request:

```
POST /rpc?

method: lisp/eval-lisp
params:
    expr: (str "Hello Mr. " (get-in [:name 0 :given 0]))
    resource:
        id: p1
        resourceType: Patient
```

Response:

```
result: Hello Mr. Morgan
```

Server responds with `HTTP 422 Unprocessable Entity` if wrong params provided or with `HTTP 500 Internal Server Error` if wrong `resourceType` provided.

\\
