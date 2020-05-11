---
description: Integrations with Azure cloud services.
---

# Azure

## Azure Storage Integration

First of all we have to create AzureAccount resource with **id** = account name and **key** = secret key of your account. Your account name and keys you can find under "Access keys" section in azure.

```yaml
POST /AzureAccount

id: aidbox
key: <..................>
```

![](../.gitbook/assets/image%20%286%29.png)

### Register AzureContainer

Go to azure console and create container "avatars". Now we can create an **AzureContainer** resource:

```yaml
POST /AzureContainer


id: avatars
account: {id: aidbox, resourceType: AzureAccount}
storage: aidbox
container: avatars
```

### Get SAS to upload file

You can provide blob name or just extension \(name will be generated\).

```yaml
POST /azure/storage/avatars

blob: pt-1.png
# ext: png

---
status: 200
url:  https://aidbox.blob.core.windows.net/avatars/pt-1.png?sr=............
```

Configure CORS in azure if you want to send data from browser:

![](../.gitbook/assets/image%20%283%29.png)

Now you can upload file from your UI:

```javascript
//onChange input[type=file]
var file = inputEvent.file.originFileObj;
fetch("<signed-url>", { 
   method: 'PUT', 
   body: file, 
   headers: {'x-ms-blob-type': 'BlockBlob'}
 }).then(...)
```

### Get SAS for read

To read uploaded file you can request signed url with:

```yaml
GET /azure/storage/avatar/pt-1.png

---
status: 200
url: <read-signed-url>

# or

GET /azure/storage/avatar/pt-1.png?redirect=true

---
status: 302
headers:
  Locaiton: <read-signed-url>
```

For example you can use trick with redirect to render image:

```markup
<img src="/azure/storage/avatar/pt-1.png?redirect=true"/>
```

