---
description: Full Text search by Narrative or Resource content
---

# \_text & \_content

[Full-text-search](https://en.wikipedia.org/wiki/Full-text_search) by resources is also at your disposal. It presents with the \_**text** _-_ search by narrative and _**\_**_**content** - search by remaining the resource content.

```javascript
GET /Patient?_text=John
```

```javascript
GET /Patient?_content=New-York
```

Search results can be sorted by rank with the **\_score** search-parameter value. More relevant results will be on top but in reversed order also supported through `-` prefix.

```javascript
GET /Patient?_content=baz&_sort=-_score
```

#### Expressions

Full-text search requests supports grouping and logical operations

```javascript
GET /Patient?_content=(NOT bar OR baz) AND foo
```

If you want to search by the phrase, just quote it

```javascript
GET /Patient?_content="Mad Max"
```

{% hint style="info" %}
Full-text search is a difficult query for the system. To improve performance, you can omit the number of entries in the results - use **\_total=none**. More information in [\_total \( \_countMethod \)](./#_total-_countmethod).
{% endhint %}

