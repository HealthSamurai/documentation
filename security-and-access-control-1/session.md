# Session

For each user login Aidbox creates Session resource

### Manage Sessions

{% code title="Get last 10 sessions" %}
```sql
select cts, resource#>>'{user,id}'
from session
order by cts desc
limit 10
```
{% endcode %}

### Clear Sessions

