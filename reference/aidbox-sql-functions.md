# Aidbox SQL functions

{% hint style="info" %}
This page is incomplete.
{% endhint %}

### knife\_extract\_text

```sql
knife_extract_text(data::jsonb, paths::jsonb) -> text[] 
```

Extract strings from jsonb `data` given jsonb array of paths `paths`.&#x20;

Path is jsonb array of jsonb object keys.&#x20;

knife\_extract\_text iterates over each array.

Example:

```sql
SELECT knife_extract_text(
  '{"a": {"b": [{"c": "l", "d": "o"}, {"c": 1, "d": ["b", "o", "k"]}]}}',
  '[["a","b","c"],["a","b","d"]]'
  );
 knife_extract_text 
--------------------
 {l,o,b,o,k}
(1 row)
```





### aidbox\_text\_search

```
aidbox_text_search(texts::text[]) -> text 
```

Concatenates strings `texts` with spaces, removes accents, adds leading and trailing spaces. &#x20;

Example:

```sql
SELECT aidbox_text_search('{abc,def}');
 ?column? 
----------
 abc def
(1 row)
```

```sql
SELECT aidbox_text_search('{abc,def}') ILIKE '% ab%';
 ?column? 
----------
 t
(1 row)
```

