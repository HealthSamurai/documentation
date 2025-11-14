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

## IAM permissions

The IAM user needs the following permissions on the S3 bucket:

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "s3:PutObject",
        "s3:GetObject",
        "s3:DeleteObject",
        "s3:ListBucket"
      ],
      "Resource": [
        "arn:aws:s3:::your-bucket-name",
        "arn:aws:s3:::your-bucket-name/*"
      ]
    }
  ]
}
```

Required actions:
- `s3:PutObject` - Upload files to the bucket
- `s3:GetObject` - Generate signed URLs for file retrieval
- `s3:DeleteObject` - Delete files from the bucket
- `s3:ListBucket` - List bucket contents

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

## Configuration options

| path                             | type    | description                                                                                                                     |
| -------------------------------- | ------- | ------------------------------------------------------------------------------------------------------------------------------- |
| AwsAccount.**access-key-id**     | string  | The access key ID used for authentication with AWS S3 or an S3-compatible service                                               |
| AwsAccount.**secret-access-key** | string  | The secret key paired with the access key ID for authentication                                                                 |
| AwsAccount.**region**            | string  | Specifies the geographic region where the cloud service operates (for example, `us-east-1` for AWS S3).                              |
| AwsAccount.**host**              | string  | The endpoint or base URL of the storage service, required for non-AWS S3 providers. Default is `s3.amazonaws.com`               |
| AwsAccount.**use-ssl**           | boolean | Use HTTPS or HTTP. Default is `false`                                                                                           |
| AwsAccount.**path-style**        | boolean | Use `<protocol>://<host>/<bucket-id>/<filename>` URL instead of `<protocol>://<bucket-name>.<host>/<file>`. Default is `false`. |

## Expiration query parameter

You can provide an expiration query parameter which sets X-Amz-Expires query param of signed URL. Expiration time is measured in seconds, fox example, for 12 hours expiration you should provide 43200.

```
GET /aws/storage/my-account/aidboxtestbucket/example.txt?expiration=43200
```

If your implementation requires additional configuration parameters, reach out to us through [Aidbox Users](https://t.me/aidbox) community or private support chat.

## How to use compatible S3 storages

Some storages, like [MinIO](https://github.com/minio/minio) or [Garage](https://garagehq.deuxfleurs.fr/) have compatible with Amazon S3 cloud storage service API. Here's an example of AwsAccount to use with local MinIO:

```
PUT /AwsAccount/my-minio

access-key-id: <access-key-id>
secret-access-key: <secret-access-key>
region: us-east-1
host: 127.0.0.1:9000
path-style: true
use-ssl: false
```
