# Aidbox SQL functions

{% hint style="info" %}
This page is in progress. Please [contact us](broken-reference) if you need more SQL functions to be documented.
{% endhint %}





### knife\_extract

```sql
knife_extract(data::jsonb, paths::jsonb) -> jsonb[] 
```

Extract elements from jsonb `data` given jsonb array of paths `paths`.&#x20;

Path is a jsonb array each element of which is either path element or filter.

Path element is a jsonb string. It specifies keyword to follow in current map.

Filter is a jsonb object. Only objects which contain filter objects are left in current array. &#x20;

`knife_extract` iterates over each array while following path and flattens results.

Example:

```sql
select knife_extract('{"telecom": [{"system": "phone", "value": "abc"}, {"system": "email", "value": "def"}]}', '[["telecom", {"system": "email"}]]');
                  knife_extract                  
-------------------------------------------------
 {"{\"value\": \"def\", \"system\": \"email\"}"}
(1 row)
```

### knife\_extract\_text

```sql
knife_extract_text(data::jsonb, paths::jsonb) -> text[] 
```

Extract strings from jsonb `data` given jsonb array of paths `paths`.&#x20;

Like `knife_extract` but return only strings.

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

```sql
select knife_extract_text(
  '{"telecom": [{"system": "phone", "value": "abc"}, {"system": "email", "value": "def"}]}',
  '[["telecom", {"system": "email"}, "value"]]'
  );
 knife_extract_text 
--------------------
 {def}
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

