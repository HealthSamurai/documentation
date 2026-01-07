---
description: Upload and retrieve files from AWS S3 using default credentials or access keys.
---

# AWS S3

Aidbox integrates with AWS S3 to handle file storage, generating secure presigned URLs that let clients upload and download directly from S3.

## Authentication methods

Aidbox provides two ways to authenticate with AWS S3:

- **Default credentials** — uses AWS environment credentials (Pod Identity, Task Role, IRSA, etc.). Recommended for production deployments on AWS.
- **Access keys** — explicit credentials stored in AwsAccount. Required for S3-compatible services (MinIO, Garage) and legacy setups.

When `access-key-id` is present in AwsAccount, Aidbox uses explicit credentials. Otherwise, it uses the [default credentials provider chain](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/credentials-chain.html).

## AwsAccount configuration

Create an [AwsAccount](/reference/system-resources-reference/cloud-module-resources#awsaccount) resource to configure S3 access. The `region` field is required. When `access-key-id` is omitted, Aidbox uses the default credentials provider.

**Default credentials mode** (recommended for AWS):

```http
PUT /AwsAccount/my-aws

region: us-east-1
```

**Access keys mode** (for S3-compatible services or legacy):

```http
PUT /AwsAccount/my-aws

region: us-east-1
access-key-id: AKIAIOSFODNN7EXAMPLE
secret-access-key: wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
```

## API endpoints

All endpoints work identically regardless of authentication method.

### Generate upload URL

Request a presigned URL for uploading a file to S3.

```http
POST /aws/storage/<account-id>/<bucket-name>

filename: documents/report.pdf
```

Response:

```json
{
  "url": "https://your-bucket.s3.us-east-1.amazonaws.com/documents/report.pdf?X-Amz-Algorithm=..."
}
```

Upload the file directly to S3:

```bash
curl -X PUT '<presigned-url>' \
  -H "Content-Type: application/pdf" \
  --data-binary @report.pdf
```

### Generate download URL

```http
GET /aws/storage/<account-id>/<bucket-name>/<file-path>
```

Example:

```http
GET /aws/storage/my-aws/my-bucket/documents/report.pdf
```

The file path can include multiple directory levels (e.g., `documents/2024/january/report.pdf`).

### Generate delete URL

```http
DELETE /aws/storage/<account-id>/<bucket-name>/<file-path>
```

### URL expiration

All endpoints accept an optional `expiration` query parameter (seconds). Default: 86400 (24 hours).

```http
POST /aws/storage/my-aws/my-bucket?expiration=3600

filename: data.txt
```

## Default credentials setup

When AwsAccount has no `access-key-id`, Aidbox uses the AWS default credentials provider chain. This works across all AWS compute environments:

| Environment           | Credential Source                                                                                     |
| --------------------- | ----------------------------------------------------------------------------------------------------- |
| **EKS**               | [Pod Identity](https://docs.aws.amazon.com/eks/latest/userguide/pod-identities.html)                  |
| **ECS / Fargate**     | [Task IAM Role](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/task-iam-roles.html)      |
| **EC2**               | [Instance Profile](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/iam-roles-for-amazon-ec2.html) |
| **App Runner**        | [Instance Role](https://docs.aws.amazon.com/apprunner/latest/dg/security-iam.html)                    |
| **Lambda**            | [Execution Role](https://docs.aws.amazon.com/lambda/latest/dg/lambda-intro-execution-role.html)       |
| **Local development** | `~/.aws/credentials` or environment variables                                                         |

### IAM permissions

The IAM role needs S3 permissions for your bucket:

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": ["s3:PutObject", "s3:GetObject", "s3:DeleteObject"],
      "Resource": "arn:aws:s3:::your-bucket-name/*"
    }
  ]
}
```

### EKS Pod Identity

[Pod Identity](https://docs.aws.amazon.com/eks/latest/userguide/pod-identities.html) is the recommended approach for EKS.

**1. Install the Pod Identity Agent**

The agent runs on each node and provides temporary AWS credentials to pods based on their ServiceAccount.

```bash
aws eks create-addon \
  --cluster-name my-cluster \
  --addon-name eks-pod-identity-agent
```

**2. Create an IAM role**

Create an IAM role with S3 permissions and a trust policy for Pod Identity:

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": "pods.eks.amazonaws.com"
      },
      "Action": ["sts:AssumeRole", "sts:TagSession"]
    }
  ]
}
```

**3. Create a Pod Identity association**

Link the Kubernetes ServiceAccount to the IAM role. Pods using this ServiceAccount will receive the role's permissions.

```bash
aws eks create-pod-identity-association \
  --cluster-name my-cluster \
  --namespace aidbox \
  --service-account aidbox \
  --role-arn arn:aws:iam::111122223333:role/aidbox-s3-role
```

**4. Configure Aidbox ServiceAccount**

```yaml
apiVersion: v1
kind: ServiceAccount
metadata:
  name: aidbox
  namespace: aidbox
```

Reference it in your Deployment:

```yaml
spec:
  template:
    spec:
      serviceAccountName: aidbox
```

## Access keys setup

When using explicit access keys, create an IAM user with the [S3 permissions](#iam-permissions) for your bucket.

### Create AwsAccount with credentials

```http
PUT /AwsAccount/my-aws

region: us-east-1
access-key-id: AKIAIOSFODNN7EXAMPLE
secret-access-key: wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY
```

## S3-compatible services

Services like [MinIO](https://min.io/) or [Garage](https://garagehq.deuxfleurs.fr/) provide S3-compatible APIs. Use access keys with custom host settings:

```http
PUT /AwsAccount/my-minio

host: 127.0.0.1:9000
path-style: true
use-ssl: false
# ... other options
```

## See also

- [AWS S3 presigned URLs](https://docs.aws.amazon.com/AmazonS3/latest/userguide/using-presigned-url.html)
- [EKS Pod Identity](https://docs.aws.amazon.com/eks/latest/userguide/pod-identities.html)
- [Default credentials provider chain](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/credentials-chain.html)
