---
description: Efficient lookup for resources by key attributes
---

# $lookup

There are scenarios when you want to quickly lookup patients or practitioners with prefix search by multiple key elements like family name, date of birth and identifier. Prefix search means you want to say in query string `jo do 79` and find `John Doe with 1979` birthdate. Sometimes there are millions of patients in your database and you want to do it efficiently to show type-ahead dropdown choices in your UI.

$lookup operations is especially designed to be efficient implementation for this case.

There is no way to implement efficient multidimensional prefix search with ranking and sorting in  relational database. $lookup based on specific assumptions to find the right trade-off: if search returns more then `count` \(by default 50\) results, we consider that search failed and result can have some anomalies - for example not complete sorting.

Here is how it works:

First of all you have to describe priority groups of attributes with **by** parameter.  Groups are separated by `;` and inside group you specify list of paths separated by `,`.  Each path expression consist of dot separated elements and indexes \(filters support is coming soon\) and should end with primitive type \(examples: `name.given` or `identifier.value`\).

Result will be sorted with order of priority groups. For example if you want to rate first matches of name, identifier and birth of data, and second matches in address and telecom you will use following expression:`name.family,name.given,identifier.value,birthDate;address.state,address.city,address.line,telecom.value`

Let's say you are searching `joh 1979 ny` - aidbox will initially search in first priority group by expression like this: 

```text
expr = extract_space_separated(resource, paths) 
where expr ilike ' % joh' AND  expr ilike '% 1979'
limit 50
```

If this query returns  50 records, aidbox will respond with this records. In case query returns less than 

```yaml
GET /Patient/$lookup?\
  by=name.family,name.given,birthDate,identifier.value;address.city,address.line&\
  sort=name.family,name.given&\
  q=Joh+Do+1980&\
  count=50&\
  limit=200
```

### Parameters

#### by

#### sort

#### q

#### count & limit

#### mode

