# Implicit

### Authentication request

```javascript
GET [base]/oauth2/authorize
    ?response_type=token
    &client_id=web-app
    &redirect_uri=http://localhost:4200
```

### Response

```text
http://localhost:4200/#access_token=a4bd882e-2bc2-4664-95e5-27e55d453cba&token_type=bearer
```

`access_token` is `a4bd882e-2bc2-4664-95e5-27e55d453cba`

### Rest Access



```bash
$ curl -H "Authorization: Bearer a4bd882e-2bc2-4664-95e5-27e55d453cba" http://localhost:8888/Patient
```



