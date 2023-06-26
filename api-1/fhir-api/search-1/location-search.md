# Location search

[Location search](https://www.hl7.org/fhir/location.html#search) is used to search locations by coordinates using the WGS84 datum.

{% hint style="warning" %}
Geopositional search is supported in Aidbox only with Aidboxdb 15.3 and later.
{% endhint %}

### Supported search parameters

<table><thead><tr><th width="280">Search parameter</th><th width="138.33333333333331" data-type="checkbox">Supported?</th><th>Description</th></tr></thead><tbody><tr><td>near</td><td>true</td><td><em>km</em> and <em>[mi_us]</em> units are supported</td></tr><tr><td>contains</td><td>false</td><td></td></tr></tbody></table>

### Example

Get locations within 11.2 kms of the current geo-coded position:&#x20;

```
#                   latitude |longitude|distance|units
GET /Location?near=-83.694810|42.256500|11.20|km
```
