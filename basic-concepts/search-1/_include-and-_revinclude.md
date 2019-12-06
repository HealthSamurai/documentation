---
description: Include associated resources
---

# \_include & \_revinclude

Client can add related resources to search result using **include,** **revinclude**  and **with** parameters.  In ORM frameworks this feature sometimes is called "associations eager loading". This technique can save additional roundtrips from client to server and potential N+1 problem.

For example you may want to get encounters with patients:

```yaml
GET /Encounter?_include=Encounter:subject:Patient
GET /Encounter?_with=subject{Patient}
```

Or patients with conditions:

```yaml
GET /Patient?_revinclude=Condition:patient:Patient
GET /Patient?_with=Condition.patient
```

We have an ability to include linked entities into result. For example, we want to get all encounters and patients related to them. Structure of the request will be: `_include=<reference search parameter> or _include=<Resource>:<referencesearch parameter> or _include=*`

```javascript
GET /Encounter?_include=subject
```

```javascript
GET /Encounter?_include=Encounter:subject
```

Or for specific **id:**

```javascript
GET /Encounter?_id=enc1&_include=Encounter:subject
```

You can get all resources referenced from result of your search using `*` as parameter value:

```javascript
GET /Encounter?_include=*
```

Reverse include  is specified as a `_revinclude`

```javascript
GET /Patient?_id=patient1&_revinclude=Encounter:subject
```

We have additional modifier \(for \_include __and __\_revinclude\) `:logical` for search by identifier:

```javascript
GET /Patient?_id=patient1&_revinclude:logical=Encounter:subject
```

Ascending order is used by default but we can change this behavior. To sort by descending order, add`-` before a parameter or specify `:asc/:desc` after search parameter.

We have an access to attributes of a resource through `.` 

```javascript
GET /Organization?_sort=-name
```

It is better described by resulting SQL:

```sql
SELECT "patient".* 
FROM "patient" 
WHERE ("patient".resource#>>'{name,0,given}'in ('Nikolai'));
```

```javascript
GET /Organization?_sort:desc=name
```

As we know, not all attributes can be used as search parameters of a resource but in **\_sort** we have an ability to use them via `.` notation.

For example, with the following request we will receive an error:

{% tabs %}
{% tab title="Request" %}
```javascript
GET /Encounter?_sort=-id
```
{% endtab %}

{% tab title="Response" %}
```yaml
# Status: 500

resourceType: OperationOutcome
...
No search parameter for Encounter.id
```
{% endtab %}
{% endtabs %}

We can avoid it with such type of request:

{% tabs %}
{% tab title="Request" %}
```javascript
GET /Encounter?_sort=-.id
```
{% endtab %}

{% tab title="Response" %}
```yaml
# Status 200

resourceType: Bundle
entry:
...
```
{% endtab %}
{% endtabs %}

Also, we can use several fields for sorting, for this we need to add them through `,` . Priority will be determined from left to right.

```javascript
GET /Encounter?_sort=status,-.id
```

In the example above, we search for all encounters and sort them by the status parameter in cases of equality, the sorting will occur by the id field in the reverse order.

