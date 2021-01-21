---
description: Filter by the modification date
---

# \_lastUpdated & \_createdAt

Search by the last modification time of the resource   `meta.lastUpdated`\(note: `ts` column in database\) or by the creation time `meta.createdAt`\(note: `cts` column in the database\) 

```http
GET /Patient?_lastUpdated=2019-01-01
```

```text
GET /Patient?_createdAt=2019-01-01
```

Value can be partial ISO date:

* only year  - `2019`
* year and month - `2019-03`
* date - `2019-03-05`
* hours - `2019-03-05T12`
* mins - `2019-03-05T12:30`
* secs - `2019-03-05T12:30:30`
* micro-secs - `2019-03-05T12:30:30.333444`
* with timezone -  `2019-03-05T12:30:30Z or 2019-03-05T12:30:30+03`

{% hint style="warning" %}
Do not forget url encode value of the expression in your code!  Browser and Aidbox REST console will do some of encoding for you.

`2019-03-05T12:30:30+03 =>2019-03-05T12%3A30%3A30%2B03`
{% endhint %}

If you use `=` operator, Aidbox rounds the query date to the max and min value and searches in between this range:

```sql
ts <= max_date_bound('2019-01-01') AND ts >= min_date_bound('2019-01-01')
```

You can use operators `lt,le,gt,ge` as the prefix of **value** to make Aidbox generate inequality queries:

```text
_lastUpdated=lt2019-01  => ts < max_date_bound('2019-01')
_lastUpdated=ge2019-01  => ts >= min_date_bound('2019-01')

```

{% hint style="info" %}
Aidbox uses the PostgreSQL precision for **lastUpdated/ts** and **createdAt/cts** - it's usually micro-seconds
{% endhint %}

