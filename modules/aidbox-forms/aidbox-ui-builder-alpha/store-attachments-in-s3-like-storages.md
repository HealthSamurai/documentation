---
description: >-
  This article explains how to configure Aidbox Forms to store attachments in
  S3-Compatible storages
---

# Store attachments in S3-like storages

Aidbox Forms provides an ability to store files from attachment items in cloud storages like S3.

## 1. Setup storage account

Aidbox supports Amazon S3, Google Cloud Storage, Azure Container. Here is the guide on [how to setup this integration](broken-reference).

## 2. Update SDCConfig

After you setup the integration put the reference to storage credential resource into the SDCConfig resource and set the bucket.

```json
{"resourceType": "SDCConfig",
 "default": true,
 "storage": {
   "account": {
     "id": "test-account",
     "resourceType": "AwsAccount"
   },
   "bucket": "sdc-files"
 }
}
```

Now all files from attachment items will be stored in cloud storage.
