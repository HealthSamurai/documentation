# ETAG support

The `ETag` HTTP response header is a unique identifier assigned to a specific version of a resource on a server. When a resource, like a web page or an API endpoint, changes, its `ETag` also changes. This mechanism allows client-side caching and efficient revalidation. \
\
When a client requests a resource, it may include the `ETag` it has in an `If-None-Match` request header. If the server's current version of the resource has a matching `ETag`, it means the content hasn't changed, and the server can send back a `304 Not Modified` response without the actual resource data. \
\
This saves bandwidth and speeds up the loading process, as the client can use its cached version. When the `ETags` don't match, it indicates the resource has changed, prompting the server to send the new version of the resource with its updated `ETag`.

### ETag Cache

All ETag values are cached to make ETag queries efficient. If you somehow made this cache invalid, you can reset Aidbox ETag cache by `DELETE /$etags-cache` or `DELETE /Patient/$etags-cache`.

{% hint style="warning" %}
The Aidbox **ETags** mechanism is based on **txid** column of the resource table in the database! If you update resources in the database, don't forget to update the **txid** column and reset cache. `UPDATE resource SET txid = nextval('transaction_id_seq')`
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
