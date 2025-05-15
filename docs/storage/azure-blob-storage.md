# Azure Blob Storage

Azure Blob Storage is used to store arbitrary unstructured data like images, files, backups, etc. Aidbox offers integration with Blob Storage to simplify upload and retrieval of data. You can read more on Blob Storage internals [here](https://docs.microsoft.com/en-gb/azure/storage/blobs/storage-blobs-introduction). All examples from this tutorial are executable in the [Aidbox REST console](../overview/aidbox-ui/rest-console.md).

### Set up Azure storage account

First of all, we have to create AzureAccount resource with **id** = account name and **key** = secret key of your account. Your account name and keys can be found under "Access keys" section in Azure Storage account settings.

{% tabs %}
{% tab title="Request" %}
#### Parameters

* `id` _(required)_: Azure storage Account name
* `key` _(required)_: Azure storage Account key

#### Example

```yaml
POST /AzureAccount

id: aidbox
key: long-base64-encoded-string
```
{% endtab %}
{% endtabs %}

### Register AzureContainer

Go to Azure console and create a container, for example, "avatars". Now we can create an **AzureContainer** resource:

{% tabs %}
{% tab title="Request" %}
#### Parameters

* `id` _(optional)_: id to reference this container in Aidbox requests
* `account` _(required)_: reference to `AzureAccount` resource
* `storage` _(required)_: Azure resource group name
* `container` _(required)_: Azure container name

#### Example

```yaml
POST /AzureContainer

id: avatars
account: {id: aidbox, resourceType: AzureAccount}
storage: aidbox
container: avatars
```
{% endtab %}
{% endtabs %}

### Get Shared Access Signature (SAS) to upload file

When the configuration is complete, you can request a temporary URL to upload blobs. By default, such URL expires in 30 minutes. You can provide a blob name or just the extension (name will be generated).

{% tabs %}
{% tab title="Request" %}
#### Body parameters

* `blob` _(required)_: file name
* `timeout` _(optional, default: 30)_: timeout in minutes

#### Example

```yaml
POST /azure/storage/avatars

blob: pt-1.png
```
{% endtab %}

{% tab title="Response" %}
#### Body

* `url`: signed url to upload file

#### Example

```yaml
url:  https://aidbox.blob.core.windows.net/avatars/pt-1.png?sr=signature
```
{% endtab %}
{% endtabs %}

Configure CORS in Azure if you want to send data from the browser:

![](<../.gitbook/assets/674bceea9c5842569072a413e2567815.png>)

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

### Get SAS to read a file

To read the uploaded file you can request a signed URL with:

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
  Location: <read-signed-url>
```

For example, you can use a trick with redirect to render an image:

```markup
<img src="/azure/storage/avatar/pt-1.png?redirect=true"/>
```
