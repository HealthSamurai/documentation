---
description: Search by date in Aidbox
---

# Date search

&#x20;As mentioned in [FHIR Search specification](https://www.hl7.org/fhir/search.html#date), Date parameters may be used with the `date`, `dateTime`, `instant`, `Period`, or `Timing` data types. Every date in search matches `yyyy-mm-ddThh:mm:ss[Z|(+|-)hh:mm]` format.

[Prefixes](https://www.hl7.org/fhir/search.html#prefix) for date search supported:

* `eq` - equal (default)
* `ne` - non-equal
* `lt` - less than
* `le` - less or equal
* `gt` - greater than
* `ge` - greater or equal
* `sa` - starts after
* `eb` - ends before

By default, search by date in Aidbox does not confirm to FHIR spec due historical reasons. We highly recommend to use FHIR compliant date search. Turn it on with:

```
BOX_SEARCH_FHIR__COMPARISONS=true
```

### FHIR Ranges

All date comparisons in FHIR are range-based. Range consists of lower bound and upper bound.&#x20;

FHIR dateTIme value can be incomplete datetime. E.g. `2020` is a FHIR dateTime but it is an incomplete datetime, since it doesn't specify month, day, etc.

Lower bound of the FHIR dateTime is the earliest complete datetime, which matches the FHIR dateTime value. For `2020` the lower bound is `2020-01-01T00:00:00Z`.

Vice versa the upper bound of the FHIR dateTime is the latest complete datetime.

For FHIR Period lower bound is the lower bound of the Period.start, similarly for the upper bound. Missing start or end in Period is treated as infinity.

#### Search using ranges

Searching by date is done by comparing _search_ range and _resource_ (target) range.&#x20;

Search range is the range of the search value.&#x20;

For example, searching `GET /fhir/Patient?birthdate=2020` means that the search range is `2020-01-01T00:00:00Z`—`2020-12-31T23:59:59Z`.

Resource range is the range of the target resource value.&#x20;

For example, if Patient resource contains birthDate `2020-01-01`, the resource range is `2020-01-01T00:00:00Z`—`2020-01-01T23:59:59Z`.

### Aidbox date search operators

Let $$(s, S)$$​ be the search range; $$(r, R)$$​ be the resource range.&#x20;

Description of Aidbox search operators.

* `eq` - equal (default). Formula: $$(s, S) \cap (r, R)$$
* `ne` - not equal. Formula: $$(s, S) \cap  (r, R) = \{\}$$​
* `lt` - less than. Formula: $$r < S$$
* `le` - less than or equal. Formula: $$r \le S$$
* `gt` - greater than. Formula: $$R > s$$​
* `ge` - greater than or equal. Formula: $$R \ge s$$

### FHIR date search operators

Let $$(s, S)$$ be the search range; $$(r, R)$$​ be the resource range.

Description of FHIR search operators

* `eq` - equal. Formula: $$(s, S) \supset (r, R)$$​
* `ne` - not equal. Formula: $$(s, S) \not\supset (r, R)$$​
* `lt` - less than. Formula: $$r \le s$$​
* `le` - less than or equal. Formula: $$r \le S$$
* `gt` - greater than. Formula: $$R > S$$
* `ge` - greater than or equal. Formula: $$R \ge s$$​
* `sa` - starts after. Formula: $$r \ge S$$​
* `eb` - ends before. Formula: $$R \le s$$

