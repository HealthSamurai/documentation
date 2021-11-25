---
description: Create a simple profile with zen
---

# 🎓 Write a custom zen profile

{% hint style="info" %}
This article is work-in-progress. Please [contact us](../contact-us.md) if you want to get details on how to create a custom zen profile.
{% endhint %}

Core zen namespace which is an entrypoint should include a zen schema with an `aidbox/profile` tag specified. Schemas tagged with `aidbox/profile` should conform this schema:

```
{:zen/tags #{zen/tag zen/schema}
 :type     zen/map
 :keys     {:resourceType       {:type zen/string}
            :profile-definition {:type zen/string}
            :format             {:type zen/keyword
                                 :enum [{:value :aidbox}
                                        {:value :fhir}]}
            :severity           {:type zen/string
                                 :enum [{:value "required"}
                                        {:value "supported"}]}}}
```

| Keyword                      | Explanation                                                                                                                                                  |
| ---------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **`:type`**                  | The full list of zen supported types is [here](https://zen-lang.github.io/tags/zen/schema.html)                                                              |
| **`:resourceType`**          | The profile is applied for resources of this type                                                                                                            |
| **`:profile-definition`**    | Is the string which should be referenced in the [`Resource.meta.profile[]`](https://www.hl7.org/fhir/resource.html#Meta) for `supported` profiles validation |
| **`:format`**                | [format](../modules-1/fhir-resources/aidbox-and-fhir-formats.md) of the data. Default is `:aidbox`                                                           |
| **`:format :aidbox`**        | schema is designed to validate data in the aidbox format                                                                                                     |
| **`:format :fhir`**          | schema is designed to validate data in the FHIR format                                                                                                       |
| **`:severity`**              | Is related to [FHIR profile usage](http://hl7.org/fhir/profiling.html#profile-uses)                                                                          |
| **`:severity "required"`**   | The profile is applied to validate all resources of such type                                                                                                |
| **`:severity "supported"`**  | The profile is applied only when referenced in `Resource.meta.profile[]`                                                                                     |
| **`:validation-type :open`** | Optional.                                                                                                                                                    |

### API

| Method                  | Description                                                                                                                                                                                                                                                                                                                  |
| ----------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `GET /$zen-ctx`         | Returns zen ctx. Useful for debug                                                                                                                                                                                                                                                                                            |
| `GET /$zen-errors`      | Returns :errors key of zen ctx                                                                                                                                                                                                                                                                                               |
| `GET /$reload-zen-deps` | Reloads deps specified in `AIDBOX_ZEN_DEPS` variable                                                                                                                                                                                                                                                                         |
| `GET /$zen-get`         | <p>Search parameters:</p><p><code>tag</code> returns symbols tagged with this tag</p><p><code>symbol</code> returns symbol definition<br>Required to have either <code>tag</code> or <code>symbol</code> parameter (not both)</p><p><code>inline</code> when <code>true</code> substitutes symbols with their definition</p> |

### A basic step by step guide

Let's define your own basic profile for the Devbox.

For an instance, for any Patient in the system, the gender property is allowed to be male, female, other or unknown. All other values are not valid.

* Create a file called zen-test-ns.edn with the following content

```typescript
{ns zen-test-ns
 import #{aidbox}

 patient
 {:zen/tags        #{zen/schema aidbox/profile}
  :type            zen/map
  :resourceType    "Patient"
  :severity        "required"
  :validation-type :open
  :keys            {:gender {:type zen/string
                             :enum [{:value "male"}
                                    {:value "female"}
                                    {:value "other"}
                                    {:value "unknown"}]}}}}
```

* Create a zip archive with that file, with the name zen-test-ns.zip
* Make the file available for downloading by a public URL (without some authentication   required). For example, upload it to some cloud storage.&#x20;
* Update your .env with `AIDBOX_ZEN_DEPS`
* If you have already running Devbox container, you can just reload Aidbox deps by executing a request `GET /$reload-zen-deps` in the Aidbox Rest Console. Otherwise, just run your Aidbox container as usual.

Let's see how does the defined profile work.

Open the Aidbox Rest Console. Try to create a patient with gender "foo".

{% tabs %}
{% tab title="Request" %}
```javascript
POST /Patient

gender: foo
meta: 
  profile: ["myprofile-definition-url"]
```
{% endtab %}

{% tab title="Response" %}
```javascript
#Status: 422

resourceType: OperationOutcome
text:
  status: generated
  div: Invalid resource
issue:
  - severity: fatal
    code: invalid
    expression:
      - Patient.gender
    diagnostics: "Expected 'foo' in #{\"male\" \"female\" \"unknown\" \"other\"}"
```
{% endtab %}
{% endtabs %}

As you can see, the patient is not created and there is an explanation, that the expected value should be among the defined list of values.

Now try to create a patient with gender "male".

{% tabs %}
{% tab title="Request" %}
```javascript
POST /Patient

gender: male
meta: 
  profile: ["myprofile-definition-url"]
```
{% endtab %}

{% tab title="Response" %}
```javascript
#Status: 201

meta:
  profile:
    - myprofile-definition-url
  lastUpdated: '2021-04-22T09:31:33.483398Z'
  createdAt: '2021-04-22T09:31:33.483398Z'
  versionId: '286'
gender: male
id: bdaa680f-2a07-49dd-9131-0882c753bd16
resourceType: Patient
```
{% endtab %}
{% endtabs %}

Finally, the validation passed and the patient is created.
