# Matcho DSL reference

* Strings, numbers, and booleans are compared by value.
* If the pattern is a dictionary, search for its inclusion into a test subject. This check is nested and recursive.
  * Pattern: `{x: 1}`
    * matches: `{x: 1}`, `{x: 1, y: 2, ...}`
    * doesn't match: `{z: 1}`
  * Pattern `{a: {b: 5}`
    * matches: `{a: {b: 5, c: 6, ...}, d: 7}`
    * doesn't match: `{a: {c: 5}}`, `{b: {a: 5}}`
* If a pattern is an array, search for its elements in the order given.
  * Pattern `[1, 2]`
    * matches `[1, 2]`, `[1, 2, 3, …]`
    * doesn't match: `[2, 1]`

#### Regular expressions

If a string starts with `#` , it is treated as a regular expression.

* Pattern `{a: "#\\d+"}`
  * matches: `{a: "2345"}`
  * doesn't match: `{a: "abc"}`

#### Special string literals (postfixed with `?`)

* **present?** — matches non-`null` values
  * Pattern `{a: "present?"}`
    * matches: `{a: 5}`,`{a: {b: 6}}`
    * doesn't match: `{b: 5}`
* **nil?** — matches `null` values;
* **not-blank?** — matches a non-empty string.

#### Matching values from the request context

If a string starts with `.` , it is interpreted as a path in the provided request context.

* Pattern: `{params: {user_id: ".user.id"}}`
  * matches: `{user: {id: 1}, params: {user_id: 1}}` where `user.id == param.user_id`.

In this example, $matcho will evaluate true only if 'a' is equal to my-value from the context.

```
POST /$matcho

context: { my-value: 'value'}
matcho: {'a': '.my-value'}
resource: {'a': 'value'}
```

#### Special keys

* **$enum** — value must be equal to one of the items in the enumeration. Strings, numbers, and booleans are supported.
  * Pattern: `{request-method: {$enum: ["get", "post"]}}`
    * matches: `{request-method: "post"},` `{request-method: "get"}`
    * doesn't match: `{request-method: "put"}`
*   **$one-of —** value must match any of the patterns. Should be used in cases, when **$enum** can not be used.

    * Pattern `{a: {$one-of: [{b: "present?"}, {c: "present?"}]}}`
      * matches `{a: {c: 5}}`
      * doesn't match: `{a: {d: 5}`, `{a: {b: null}`

    Note that the `$one-of` key cannot be utilized concurrently with other keys.

    *   **Correct usage example.** Here, the `resource/type` key is correctly nested within each option specified by the `$one-of` key.

        ```yaml
        resourceType: AccessPolicy
        engine: matcho
        matcho:
          request-method: get
          params:
            $one-of:
              - name: present?
                resource/type: Patient
              - _id: present?
                resource/type: Patient
              - id: present?
                resource/type: Patient
        ```
    * **Incorrect usage example.**

    In this case, the `$one-of` key is improperly combined with the `resource/type` key within the same `params` block:

    ```yaml
    resourceType: AccessPolicy
    engine: matcho
    matcho:
      request-method: get
      params:
        resource/type: Patient
        $one-of:
          - name: present?
          - _id: present?
          - id: present?
    ```
*   **$reference** — parse `Reference` or string into [aidbox format](../api/rest-api/other/aidbox-and-fhir-formats.md#references). It is useful to map FHIR reference into resourceType and id like this:

    `{reference: "Patient/pid"}` => `{id: "pid", resourceType: "Patient"}`

Example: `{resource: {patient: {$reference: {id: '.user.data.patient_id'}}}`

* **$contains** — collection must contain at least one match.
  * Pattern: `{type: {$contains: {system: "loinc"}}`
    * matches `{type: [{system: "snomed"}, {system: "loinc"}]}`
* **$every** — each item in a collection must satisfy a pattern.
  * Pattern: `{col: {"$every": {foo: "bar"}}`
    * matches: `{col: [{foo: "bar"}, {foo: "bar", baz: "quux"}]}`
*   **$not** — negates a pattern.

    * Pattern: `{message: {$not: {status: private}}`
      * matches: `{message: {status: public}}`
      * doesn't match: `{message: {status: private}}`.

    **Be careful** when using **$not**, as it is possible to create policies that are too permissive.

{% hint style="warning" %}
Consider the following policy which uses `$not` key.

```yaml
sourceType: AccessPolicy
engine: matcho
matcho:
  request-method: delete
  uri: '#^/Patient.*$'
  user: {$not: {data: {role: guest}}}
```

While the original intent was to prevent Guest users from deleting Patient resources, this AccessPolicy allows them to perform DELETE /Patient/ for all other users, including unauthorized ones. In this case, it is better to explicitly list allowed roles with **$enum**.
{% endhint %}

* **$present-all** — checks that every element in $present-all is present in the original array, with no sort check. It can be combined with $length.
* **$length** — checks the length of the array.
