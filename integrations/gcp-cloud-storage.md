# GCP Cloud Storage

Cloud Storage is used to store arbitrary unstructured data like images, files, backups, etc. Aidbox offers integration with Cloud Storage to simplify upload and retrieval of data. All examples from this tutorial are executable in Aidbox REST console.

#### Create GcpServiceAccount

This resource contains credentials for Service Account that has write/read access to Cloud Storage.

```text
PUT /GcpServiceAccount

id: my-account
service-account-email: sign-url-test@mytestproject.iam.gserviceaccount.com
private-key: "..."
```

#### Get URL for file upload

In order to get URL for file upload you should provide GcpServiceAccount id, bucket name to upload to and a filename. To upload data to the bucket use PUT request with signed URL and provide file content in request body.

```text
POST /gcp/storage/my-account/my-bucket

filename: sample.txt

# status: 200
# body:
#  url: <signed-url> 
```

#### Get URL for file download

```text
GET /gcp/storage/my-account/my-bucket/sample.txt

# status: 200
# body:
# url: <signed-url>
```

#### Configuration options

You can provide "expiration" query param for both POST & GET queries. It corresponds to X-Goog-Expires query param which sets URL expiration time in seconds.

