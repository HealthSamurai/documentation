---
description: The article explains, how Aidbox treats access tokens
---

# How Aidbox processes tokens

### Token types

There are two types of token Aidbox supports: 

- `opaque` strings are treaded as is. Aidbox does not parse such tokens and not validate them

- `JWT`s could be parsed and their signature could be checked as well 

### Parsing and validating `JWT`

Aidbox parses JWT tokens every time it receives it. Main steps in parsing and validating JWT strings are:

- to check if token string looks like JWT
- to parse string to JWT object
- to check JWT if token is not expired
- to check JWT signature

### Processing access token

`access_token` and `refresh_token` issued by Aidbox are just opaque strings. So Aidbox does not process them.
