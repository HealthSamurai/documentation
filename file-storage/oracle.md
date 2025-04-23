# Oracle Cloud Storage

Oracle Cloud Storage is used to store arbitrary unstructured data like images, files, backups, etc. Currently Aidbox doesn't offer integration with Oracle Cloud Storage but you can use pre-authenticated requests to load data into Aidbox. All examples from this tutorial are executable in the [Aidbox REST console](../../../overview/aidbox-ui/rest-console-1.md).

## Create pre-authenticated request in Oracle Cloud Storage

You can create pre-authenticated request by following [this tutorial](https://docs.oracle.com/en-us/iaas/Content/Object/Tasks/usingpreauthenticatedrequests.htm)

## Use pre-authenticated request in `$import`

You can use your pre-authenticated request in `$import` like this:

```yaml
POST /fhir/$import
Accept: text/yaml
Content-Type: text/yaml

id: oracle-test
inputFormat: application/fhir+ndjson
contentEncoding: gzip
mode: bulk
inputs:
- resourceType: Patient
  url: <pre-authenticated-request>
```
