# Encryption API

Aidbox can encrypt attributes with the type `secret`.

### Configure Aidbox

Let's generate a key

```yaml
POST /$encrypt-key

# 200
# 00055DDDF7BB2A52C21651283F346C048C8470CAE2796A2A2346994A05DA760B
```

Copy this key into the box ENV variables and restart Aidbox:

```
AIDBOX_ENCRYPT_KEY="00055DDDF7BB2A52C21651283F346C048C8470CAE2796A2A2346994A05DA760B"
```

### Demo

Register a custom resource with the secret attribute:

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

Create an instance of the resource:

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

Read the instance with an encrypted attribute:

```yaml
GET /MyCard/c1

# 200
id: c1
name: Nikolai
number: $58B5........5766D
```

Decrypt the instance:

```yaml
GET /$decrypt/MyCard/c1

id: c1
name: Nikolai
number: x777888
```
