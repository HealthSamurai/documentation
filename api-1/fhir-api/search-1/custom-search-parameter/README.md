# Custom Search Parameter

Aidbox has multiple options to provide custom searches.

## [custom-search.md](../custom-search.md "mention")

Use AidboxQuery if you want to search writing your own SQL.

## Define SearchParameter resource

You can define custom search params by just creating SearchParameter resource. Let's say you want to search patient by city:

```yaml
PUT /SearchParameter/Patient.city
​
name: city
type: token
resource: {id: Patient, resourceType: Entity}
expression: [[address, city]]
```

Now let's test new search parameter

```yaml
GET /Patient?city=New-York
​
# resourceType: Bundle
type: searchset
entry: [...]
total: 10
query-sql: 
- | 
  SELECT "patient".* FROM "patient" 
  WHERE ("patient".resource @> '{"address":[{"city":"NY"}]}')
   LIMIT 100 OFFSET 0
```

#### Define custom SearchParameter with extension

If you have defined [first-class extension](../../../../modules-1/first-class-extensions.md), you have to use Aidbox format for the SearchParameter expression. If you use FHIR format, you don't need to create Attribute and the `expression` path should be in FHIR format.

{% tabs %}
{% tab title="First-class extension" %}
```yaml
PUT /Attribute/ServiceRequest.precondition

resourceType: Attribute
description: "The condition or state of the patient, prior or during the diagnostic procedure or test, for example, fasting, at-rest, or post-operative. This captures circumstances that may influence the measured value and have bearing on the interpretation of the result."
resource: {id: ServiceRequest, resourceType: Entity}
path: [precondition]
id: ServiceRequest.precondition
type: {id: CodeableConcept, resourceType: Entity}
isCollection: true
extensionUrl: "http://hl7.org/fhir/StructureDefinition/servicerequest-precondition"
```
{% endtab %}

{% tab title="SearchParameter (First-class extension)" %}
```yaml
PUT /SearchParameter/ServiceRequest.precondition

name: precondition
type: token
resource: {id: ServiceRequest, resourceType: Entity}
expression: [[precondition, coding]]
```
{% endtab %}

{% tab title="SearchParameter (FHIR extension)" %}
```yaml
PUT /SearchParameter/ServiceRequest.precondition

name: precondition
type: token
resource: {id: ServiceRequest, resourceType: Entity}
expression:
- [extension, {url: 'http://hl7.org/fhir/StructureDefinition/servicerequest-precondition'}, 
value, CodeableConcept, coding, code]

```
{% endtab %}
{% endtabs %}

If you use [Zen IG](../../../../aidbox-configuration/zen-configuration.md) then first-class extensions are generated from zen-schemas. You have to use Aidbox format for the custom SearchParameter `expression` (check tab #3 in the example above).

## Custom SearchParameter with Zen

Most of the Search Parameters from IG work with Zen by default, also you can make a new one.

Assuming you already know how to use [configuration projects](../../../../aidbox-configuration/aidbox-zen-lang-project/setting-up-a-configuration-project.md), let's learn how to create zen search parameter by example:

```clojure
{ns main
 import #{aidbox.search-parameter.v1
          aidbox
          aidbox.repository.v1}

 zen-config
 {...}

 my-parameter
 {:zen/tags #{aidbox.search-parameter.v1/search-parameter}
  :name "brthdt"
  :type :date
  :resource {:resourceType "Entity" :id "Patient"}
  :expression [["birthDate"]]}

 patient-repository
 {:zen/tags #{aidbox.repository.v1/repository}
  :resourceType "Patient"
  :extra-parameter-sources :all ; allow to use SearchParameters from outside of repo
  :search-parameters #{my-parameter}}

 repositories
 {:zen/tags #{aidbox/service}
  :engine aidbox.repository.v1/engine
  :repositories #{patient-repository}
  :load-default true}

 box {:zen/tags #{aidbox/system}
      :config   zen-config
      :services
      {:repositories repositories}}}
```

First we import `aidbox.search-parameter.v1` and `aidbox.repository.v1` namespaces from edn files. These are zen-namespaces we need to make an `aidbox/service` which name is `repositories`.

This service is our concept of wrapping resourceType-specific entities, as search parameters, indexes, and more, into one entity, called **repository**. We will add indexes for search parameters soon.

We have one repository for Patient resourceType: `patient-repository`. It contains `:search-parameters` key with new SearchParameter `my-parameter`. &#x20;

SearchParameter must contain:

* type: [FHIR Search Parameter types](../#search-parameters)
* resource, containing resourceType and id
* [jsonknife](./#jsonpath-vs-jsonknife) expression containing path in the resource to search for
* name to use in search

After your Aidbox loads the service, you can use new search parameter:

```yaml
GET /Patient?brthd=lt2023
```

{% hint style="info" %}
You can always look into the definition of Aidbox-specific namespaces in [Profiles page](../../../../profiling-and-validation/profiling-with-zen-lang/extend-an-ig-with-a-custom-zen-profile.md#check-if-your-profile-is-loaded)
{% endhint %}

Formal Zen SearchParameters description:

{% content-ref url="zen-search-parameters.md" %}
[zen-search-parameters.md](zen-search-parameters.md)
{% endcontent-ref %}

### Composite search parameter

Read [Composite Search Parameters](../composite-search-parameters.md) first.

Composite search parameter must contain additional key: components. It must be a nested array in following structure:

```
[ <paths-to-search-for-value1> <paths-to-search-for-value2> ...]
```

In this example we create SearchParameter with name `composite-string-date` which will look for two parts: one is string, the other is date.&#x20;

```
 {ns ...
 import #{...
          aidbox.search-parameter.draft
          ...}

 our-param-composite-string-date
 {:zen/tags #{aidbox.search-parameter.draft/composite-search-parameter}
  :name "composite-string-date"
  :type :composite
  :resource {:resourceType "Entity" :id "SomeType"}
  :expression [[]]
  :components [[["name" "given"] ["name" "family"]] [["mydate"]]]
  :search-types [:string :date]}
```

This request

```
GET /SomeType?composite-string-date=somename$2023-08-01
```

will search `somename` in `name.given` and `name.family`, `2023-08-01` in `mydate` in SomeType resources.
