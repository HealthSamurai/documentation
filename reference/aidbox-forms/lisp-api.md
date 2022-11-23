# Lisp API

* [lisp/get-specification](lisp-api.md#lisp-get-specification) - get the specification which is distributed in form of a test suite

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
  - spec-edn: ... "complete zen specification in EDN format"
    fn-spec: ... "zen specification of a test function"
    test-suite: ... "complete test suite with AST expressions in JSON format"
```

Server responds with `HTTP 422 Unprocessable Entity` if wrong params are provided.
