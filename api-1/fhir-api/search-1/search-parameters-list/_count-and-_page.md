---
description: Paging search results
---

# \_count & \_page

Search results can contain many records, for more convenient work we can use pagination. Available parameters are: **\_count** — total records on the page, **page** — specific page, output Bundle includes link section with **first**, **self**, **next**, **previous,** and **last** page constructed URLs.

## Default `count` value

Configure default value for count search parameter with&#x20;

<pre class="language-yaml"><code class="lang-yaml"><strong>BOX_SEARCH_DEFAULT__PARAMS_COUNT=&#x3C;number>
</strong></code></pre>

The default value is 100.&#x20;

{% hint style="warning" %}
Be careful! Too high value of \_count parameter can lead to the OutOfMemoryError due to the memory requirements to load many values.
{% endhint %}

### Example

```yaml
GET /Patient?_count=10&_page=3

# 200
resourceType: Bundle
type: searchset
entry:
- resource:
    ...
total: 206
link:
- {relation: first, url: '/Patient?_count=10&_page=1'}
- {relation: self, url: '/Patient?_count=10&_page=3'}
- {relation: next, url: '/Patient?_count=10&_page=4'}
- {relation: previous, url: '/Patient?_count=10&_page=2'}
- {relation: last, url: '/Patient?_count=10&_page=21'}
```
