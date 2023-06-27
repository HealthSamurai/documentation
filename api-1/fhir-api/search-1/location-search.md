# Location search

[Location search](https://www.hl7.org/fhir/location.html#search) is used to search locations by coordinates using the WGS84 datum.

{% hint style="warning" %}
Geopositional search is supported in Aidbox only with Aidboxdb 15.3 and later.
{% endhint %}

## Supported search parameters

<table><thead><tr><th width="280">Search parameter</th><th width="138.33333333333331" data-type="checkbox">Supported?</th><th>Description</th></tr></thead><tbody><tr><td>near</td><td>true</td><td><em>km</em> and <em>[mi_us]</em> units are supported</td></tr><tr><td>contains</td><td>false</td><td></td></tr></tbody></table>

## Near Search Parameter

Search for locations near the specified geo-coded position.

<pre><code><strong>GET [base]/Location?near=&#x3C;latitude>|&#x3C;longitude>|[distance]|[units]
</strong></code></pre>

Latitude and longitude are required.

If the units are omitted, then kms are assumed.

If the distance is also omitted, then Aidbox treat "near" as 3 km.&#x20;

### Examples

**Get locations within 11.2 kms of the some geo-coded position:**&#x20;

```http
#                   latitude |longitude|distance|units
GET /Location?near=-83.674810|42.266500|11.20|km
```

Response:

```yaml
resourceType: Bundle
total: 1
entry:
  - resource:
      position:
        latitude: -83.69481
        longitude: 42.2565
      id: >- loc-1
      resourceType: Location
```

**Search in miles:**

```
GET /Location?near=-83.674810|42.266500|11.20|[mi_us]
```

**Search without distance and units**

```
# distance = 3, units = km
GET /Location?near=-83.674810|42.266500
```
