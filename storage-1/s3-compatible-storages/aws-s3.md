# AWS S3

AWS S3 is used to store arbitrary unstructured data like images, files, backups, etc. Aidbox offers integration with S3 to simplify upload and retrieval of data. All examples from this tutorial are executable in Aidbox REST console.

## Setup AwsAccount

Create an instance of AwsAccount that contains access credentials and region settings.

```
PUT /AwsAccount

id: my-account
access-key-id: AKIAIOSFODNN7EXAMPLE
secret-access-key: wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
region: us-east-1
```

## Get signed URL to upload the file

Provide AwsAccount id as well as the bucket name and get back the signed URL that you can use to upload the data. By default, the link is valid for 24 hours.

```
POST /aws/storage/my-account/aidboxtestbucket

filename: example.txt

# status: 200
# body:
#  url: <signed-url>  
```

You should use PUT request method with signed URL and provide content data in the request body.

```
curl -X PUT '<signed-url>' -d 'content'
```

## Get a signed URL to retrieve the file

```
GET /aws/storage/my-account/aidboxtestbucket/example.txt

# status: 200
# body:
#  url: <signed-url>
```

You should use GET request method with signed URL:

```
curl -X GET '<signed-url>' -d 'content'
```

## Get a signed URL to delete the file

It is also possible to delete the file:

```
DELETE /aws/storage/my-account/aidboxtestbucket/example.txt

# status: 200
# body:
#  url: <signed-url>
```

#### Configuration options

You can provide an expiration query parameter which sets X-Amz-Expires query param of signed URL. Expiration time is measured in seconds, e.g. for 12 hours expiration you should provide 43200.

```
GET /aws/storage/my-account/aidboxtestbucket/example.txt?expiration=43200
```

If your implementation requires additional configuration parameters, reach out to us through [Aidbox Users](https://t.me/aidbox) community or private support chat.
