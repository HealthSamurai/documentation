# Search \(TODO\)

### Overview

### Search Parameters

### Chained Parameters

### \_include & \_revinclude

### Dot parameters extension 

```http
GET <base-url>/Patient?.name.0.given=Nikolai&.birthDate::timestamptz$lt=2011
```

### \_sort

### \_total \( \_countMethod \) 

By default for all search requests aidbox return total number in result, which represent how many resources match criteria. But to do this we run second query for count, which takes some time and eats resources. To get response faster on big amount of data you can change this behaviour using **\_total** parameter. **\_total** parameter can have following values:

* `none` - do not run count query 
* `estimated` - roughly estimate number of results
* `accurate` - run accurate count

See discussion in zulip - [https://chat.fhir.org/\#narrow/stream/4-implementers/topic/Gender.20discrimination](https://chat.fhir.org/#narrow/stream/4-implementers/topic/Gender.20discrimination)

