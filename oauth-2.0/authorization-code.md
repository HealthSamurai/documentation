# Authorization Code Grant

[https://tools.ietf.org/html/rfc6749\#section-4.1](https://tools.ietf.org/html/rfc6749#section-4.1)

## Authorization Request

Request [https://tools.ietf.org/html/rfc6749\#section-4.1.1](https://tools.ietf.org/html/rfc6749#section-4.1.1)

```javascript
GET [base]/oauth2/authorize?response_type=code&client_id=web-app
```

Response:

```text
Location http://localhost:4200/?code=5b0c35d3-ae69-475f-b75c-68d7faf7e167&state=
```

`Code` is `5b0c35d3-ae69-475f-b75c-68d7faf7e167`  


## Access Token Request

Request [https://tools.ietf.org/html/rfc6749\#section-4.1.3](https://tools.ietf.org/html/rfc6749#section-4.1.3)

POST \[base\]/oauth2/token?grant\_type=authorization\_code&code=5b0c35d3-ae69-475f-b75c-68d7faf7e167&redirect\_uri=http://localhost:4200

```text
POST [base]/oauth2/token?grant_type=authorization_code&code=5b0c35d3-ae69-475f-b75c-68d7faf7e167&redirect_uri=http://localhost:4200
```

Response

```text
{
    "access_token": "d3ccee5e-e082-48de-93f3-aea032ed1806",
    "token_type": "bearer"
}
```

## Rest Access

```bash
$ curl -H "Authorization: Bearer d3ccee5e-e082-48de-93f3-aea032ed1806" http://localhost:8888/Patient
```

