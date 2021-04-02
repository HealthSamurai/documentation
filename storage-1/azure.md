# Azure Blob Storage

Azure Blob Storage is used to store arbitrary unstructured data like images, files, backups, etc. Aidbox offers integration with Blob Storage to simplify upload and retrieval of data. You can read more on Blob Storage internals [here](https://docs.microsoft.com/en-gb/azure/storage/blobs/storage-blobs-introduction). All examples from this tutorial are executable in Aidbox REST console.

### Set up Azure storage account

First of all, we have to create AzureAccount resource with **id** = account name and **key** = secret key of your account. Your account name and keys can be found under "Access keys" section in Azure Storage account settings.

```yaml
POST /AzureAccount

id: aidbox
key: <..................>
```

### Register AzureContainer

Go to Azure console and create container, for example, "avatars". Now we can create an **AzureContainer** resource:

```yaml
POST /AzureContainer

id: avatars
account: {id: aidbox, resourceType: AzureAccount}
storage: aidbox
container: avatars
```

### Get Shared Access Signature \(SAS\) to upload file

When configuration is complete, you can request a temporary URL to upload blobs. By default, such URL expires in 30 minutes. You can provide a blob name or just the extension \(name will be generated\).

```yaml
POST /azure/storage/avatars

blob: pt-1.png
# extension: png

---
status: 200
url:  https://aidbox.blob.core.windows.net/avatars/pt-1.png?sr=............
```

Configure CORS in azure if you want to send data from browser:

![](../.gitbook/assets/image%20%283%29.png)

Now you can upload file from your UI using signed URL provided by Aidbox:

```javascript
//onChange input[type=file]
var file = inputEvent.file.originFileObj;
fetch("<signed-url>", { 
   method: 'PUT', 
   body: file, 
   headers: {'x-ms-blob-type': 'BlockBlob'}
 }).then(...)
```

### Get SAS to read file

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

For example, you can use a trick with redirect to render image:

```markup
<img src="/azure/storage/avatar/pt-1.png?redirect=true"/>
```

