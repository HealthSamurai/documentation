# Serve static files

DevBox supports serving of static files. This feature works only in docker-compose environment.

Files will be saved in folder that is set in [FILE\_ROOT environment variable](https://github.com/Aidbox/devbox/search?l=YAML&q=FILE_ROOT).It should be absolute absolute path to writable folder.

### Upload

$upload form param request

```text
curl -X POST \
  'http://localhost:8888/$upload' \
  -H 'content-type: multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW' \
  -F test=@PATH_TO_FILE
```

Request will return [FHIR Attachment](https://www.hl7.org/fhir/datatypes.html#attachment) which will include file URL. Now file with duplicate name will overwrite earlier version. 





