# ETAG support

Read more about [How ETAG](https://developer.mozilla.org/ru/docs/Web/HTTP/%D0%97%D0%B0%D0%B3%D0%BE%D0%BB%D0%BE%D0%B2%D0%BA%D0%B8/ETag) works!

### Search API & ETAG

Search response contains **etag** information in  **etag** header and in `meta.versionId`. You can cache result and use `If-None-Match` header or `_etag` parameter to get newest data from server or response with status `304` if nothing changed.

```yaml
GET /Patient

# resp
status: 200
headers:
  ETag: '999'
body:
  meta: {versionId: '999'}
  ....

# you can cache response and versionId

# get 
GET /Patient?_etag=999 # laset versionId
If-None-Match: 999

# resp
status: 304

# if etag is old you will get result
GET /Patient?_etag=99

# resp
status: 200
body:
  meta: {versionId: '999'}

```

### ETag Cache

All etag values are cached to make etag queries efficient. If you somehow made this cache invalid - you can reset Aidbox etag cache by `DELETE /$etag-cache` or `DELETE /Patient/$etag-cache`.

{% hint style="warning" %}
Aidbox **etags** machine is based on **txid** column in database! If you update resources in database do not forget update **txid** column and reset cache. `UPDATE resource SET txid = nextval('transaction_id_seq')`
{% endhint %}

