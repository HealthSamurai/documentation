# Number search

Searching on a simple numerical value in a resource.

## Difference with FHIR

Aidbox does not involves an implicit range while searching on decimals. Aidbox always searches for exact number.&#x20;

| Type                 | Meaning                                         |
| -------------------- | ----------------------------------------------- |
| `[parameter]=100`    | Values that equal 100                           |
| `[parameter]=100.00` | Values that equal 100                           |
| `[parameter]=1e2`    | Values that equal 100                           |
| `[parameter]=lt100`  | Values that are less than exactly 100           |
| `[parameter]=le100`  | Values that are less or equal to exactly 100    |
| `[parameter]=gt100`  | Values that are greater than exactly 100        |
| `[parameter]=ge100`  | Values that are greater or equal to exactly 100 |
| `[parameter]=ne100`  | Values that are not equal to exactly 100        |

## Supported modifiers

* eq
* gt
* ge
* lt
* le
* ne
