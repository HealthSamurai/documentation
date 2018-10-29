# Serve Static Files

Aidbox.Dev supports serving of static files. This feature works only in a docker-compose environment.

Files will be saved in a folder that is set in the [FILE\_ROOT environment variable](https://github.com/Aidbox/devbox/search?l=YAML&q=FILE_ROOT). It should be an absolute path to a writable folder.

### Upload

$upload form param request:

```text
curl -X POST \
  'http://localhost:8888/$upload' \
  -H 'content-type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW' \
  -F test=@PATH_TO_FILE
```

The request will return a [FHIR Attachment](https://www.hl7.org/fhir/datatypes.html#attachment) which will include a file URL. Now a file with a duplicate name will overwrite its earlier version. 





