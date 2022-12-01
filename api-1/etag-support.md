# ETAG support

Read more about [How ETAG](https://developer.mozilla.org/en/docs/Web/HTTP/%D0%97%D0%B0%D0%B3%D0%BE%D0%BB%D0%BE%D0%B2%D0%BA%D0%B8/ETag) works!

### Search API & ETAG

Search response contains **etag** information in the **etag** header and in `meta.versionId`. You can cache result and use `If-None-Match` header or `_etag` parameter to get the latest data from the server or a response with `304` if nothing has been changed.

```yaml
GET /Patient
Accept: text/yaml

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
Accept: text/yaml
If-None-Match: 999

# resp
status: 304

# if etag is old you will get result
GET /Patient?_etag=99
Accept: text/yaml

# resp
status: 200
body:
  meta: {versionId: '999'}
```

### ETag Cache

All ETag values are cached to make ETag queries efficient. If you somehow made this cache invalid, you can reset Aidbox ETag cache by `DELETE /$etags-cache` or `DELETE /Patient/$etags-cache`.

{% hint style="warning" %}
Aidbox **ETags** mechanisms is based on **txid** column of resource table in database! If you update resources in database, don't forget to update the **txid** column and reset cache. `UPDATE resource SET txid = nextval('transaction_id_seq')`
{% endhint %}

### ETag Cache performance

To build a cache for a specific resourceType **ETag** Aidbox runs a query to get the `max` value of the `txid` column. To make this operation efficient, it's recommended to build an index on the `txid` column for tables where **ETag** will be used.

Use query:

```sql
CREATE INDEX IF NOT EXISTS <resourceType>_txid_btree ON <resourceType> using btree(txid);

CREATE INDEX IF NOT EXISTS <resourceType>_history_txid_btree ON <resourceType>_history using btree(txid);
```

{% hint style="info" %}
replace with table name, for example `CREATE INDEX IF NOT EXISTS patient_txid_btree ON patient using btree(txid); CREATE INDEX IF NOT EXISTS patient_history_txid_btree ON patient_history using btree(txid);`
{% endhint %}
