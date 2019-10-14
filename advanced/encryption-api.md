# Encryption API

Aidbox can encrypt attributes with type `secret`.

### Demo

Register custom resource with secret attribute:

```yaml
POST /App

id: cards
type: app
apiVersion: 1
entities:
  MyCard:
    attrs:
      name: {type: string}
      number: {type: secret}
```

Create an instance of resource:

```yaml
POST /MyCard

id: c1
number: x777888
name: Nikolai

# 200
id: c1
name: Nikolai
number: $58B5........5766D
```

Read instance with encrypted attr:

```yaml
GET /MyCard/c1

# 200
id: c1
name: Nikolai
number: $58B5........5766D
```

Decrypt instance:

```yaml
GET /$decrypt/MyCard/c1

id: c1
name: Nikolai
number: x777888
```

