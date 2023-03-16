---
description: The article is in progress.
---

# Relationship-based access control in Aidbox

Throughout this tutorial, we’ll walk you through the implementation of basic relationship-based access control model in Aidbox. We’ll assume you have Aidbox up & running already [locally](https://docs.aidbox.app/getting-started/run-aidbox-locally-with-docker) or [in cloud](https://docs.aidbox.app/getting-started/run-aidbox-in-aidbox-sandbox).

{% hint style="info" %}
**Where to get help:**

If you’re having trouble going through this tutorial, please head over to [our Aidbox community chat](https://t.me/aidbox).
{% endhint %}

In this tutorial we will

* model authorization for our sample application called _Research Study Repository_
* implement that authorization model in Aidbox with AccessPolicy engine.

## Authorization model

We will model access for an example application called _Research study repository_. The system is going to give researchers an access to research studies and related patient records.

Our security policy says:

> _User has access to all studies they are collaborator of and to all patient records within those studies._

Authorization model that best suits our task is [relationship-based access control](https://en.wikipedia.org/wiki/Relationship-based\_access\_control) (ReBAC). It states that subject's permission to access a resource is defined by the presence of relationships between those subjects and resources.

We will focus only on read access, expecting that all data is uploaded. We will upload prepared sample data later on implementation section.

But before we dive into defining authorization, let's discuss our data model and UI we are going to provide our users. Good data model and UI leads to easier authorization.

### Data model & UI

The core entity of our application will be a research study. There is [a ResearchStudy resoruce in FHIR](https://www.hl7.org/fhir/researchstudy.html), which describes _'a process where a researcher or organization plans and then executes a series of steps intended to increase the field of healthcare-related knowledge'_.

ResearchStudy references to Group of patients invloved in the research with [`ResearchStudy.enrollment`](https://www.hl7.org/fhir/researchstudy-definitions.html#ResearchStudy.enrollment) element. Patient record is represented by two resources Patient and Observation.

ResearchStudy doesn't have references to collaborators. So, we will introduce one and make a linkage with Aidbox User.

<figure><img src="https://hedge.aidbox.app/uploads/upload_cdfe68d6da6d9812277a9629e55dee82.png" alt=""><figcaption><p> <em>Data model of Research study repository application</em></p></figcaption></figure>

As ResearchStudy resource is a core of our model, it's resonable to make the list of available studies a starting point on UI. So we may imagine user's flow within UI.

* Researcher enters the system and see the list of studies, they involved into as a collaborator.
* They can drill down to a study to see details and search over related patients and observations.

<figure><img src="https://hedge.aidbox.app/uploads/upload_20969235a7ad2e87386acd7cbe18c9f1.png" alt=""><figcaption><p><em>UI pages &#x26; FHIR requests for Research study repository application</em></p></figcaption></figure>

Once we defined our data model, UI pages and FHIR requests, we may start implementing this.

## Implementation

As we mentioned earlier, FHIR ResearchStudy doesn't have references to collaborators. It's good to start with enabling this reference.

### Add reference to collaborators

There are many ways you can customize your data model with Aidbox. We will do this by creating an Attribute resource.

There is an HTTP request below for creating `Attribute/ResearchStudy.collaborator`. You can perform this request in [Aidbox REST Console](https://docs.aidbox.app/overview/aidbox-ui/rest-console-1).

```yaml
PUT /Attribute/ResearchStudy.collaborator
Content-Type: text/yaml
Accept: text/yaml

description: List of study collaborators
resource: {id: ResearchStudy, resourceType: Entity}
path: [collaborator]
type: {id: Reference, resourceType: Entity}
isCollection: true
refers: [User]
extensionUrl: urn:extension:researchStudyMember
```

{% hint style="warning" %}
If you use zen profiles, Attribute resources will be disabled. Thus, you will need to define your attributes in [zen](https://docs.aidbox.app/profiling-and-validation/profiling-with-zen-lang/write-a-custom-zen-profile).
{% endhint %}

### Upload sample data

Aidbox is ready to store our data, and we prepared data samples, so we could test our access policies. You can use the request below to upload sample data.

```
POST /$load

source: ...
```

The picture below, demonstrates the key data we uploaded. Jane user has access to 'Smoking and cancer research', and both users have access to 'Diet and diabetes research'.

<figure><img src="../../.gitbook/assets/Screenshot 2023-03-15 at 17.36.05.png" alt=""><figcaption><p><em>Sample data for research study repository application</em></p></figcaption></figure>

## Write access policies

Now, we are ready to define available enpoints and write AccessPolicy for them.

### List of studies

The endpoint to fetch all user's research studies is

```
GET /ResearchStudy?collaborator=<user-id>
```

FHIR doesn't have search parameter `collaborator`. Aidbox allows you to define one with SearchParameter resource.

```yaml
PUT /SearchParameter/ResearchStudy.collaborator

name: collaborator
type: reference
resource: {id: ResearchStudy, resourceType: Entity}
expression:
- [collaborator]
```

AccessPolicy:

```yaml
PUT /AccessPolicy/user-can-search-their-research-studies

description: User can search for research studies, they collaborate on
engine: matcho
matcho:
  user:
    id: 'present?'
  request-method: get
  uri: /ResearchStudy
  params:
    collaborator: .user.id
    _with: 'nil?'
    _include: 'nil?'
    _revinclude: 'nil?'
```

{% hint style="info" %}
**Why did we explicitly exclude `_include`, `_revinclude` and `_with` parameters?**

matcho engine compares incoming request with defined pattern, if the key is not specified in pattern, it will be ignore while checking. `_include`, `_revinclude` and `_with` parameters expands the list of returning data with related resources. As far we want to leave only ResearchStudy resources, we excluded them explicitly.
{% endhint %}

Let's check it

{% tabs %}
{% tab title="Jane searches for her studies" %}
```yaml
GET /ResearchStudy?collaborator=jane
Authorization: Bearer janes-access-token

# 200 OK
```
{% endtab %}

{% tab title="Jane searches for all studies" %}
```yaml
GET /ResearchStudy
Authorization: Bearer janes-access-token

# 403 Forbidden
```
{% endtab %}

{% tab title="Jane searches for Oscar's studies" %}
```yaml
GET /ResearchStudy?collaborator=oscar
Authorization: Bearer janes-access-token

# 403 Forbidden
```
{% endtab %}
{% endtabs %}

We have secured endpoint for fetching list of studies. Note, that all [search parameters for ResearchStudy](https://www.hl7.org/fhir/researchstudy.html#search) is also available.

### Read study details

The endpoint to fetch research study details is

```
GET /ResearchStudy/<research-study-id>
```

It's not possible find out if current user is a collaborator on this study or not by only research study id . Fortunately, Aidbox AccessPolicy supports sql engine, which allows you to make your authorization decisions based on data you have.

```yaml
PUT /AccessPolicy/user-can-read-their-research-study

description: User can research study, they collaborate on
engine: complex
and:
- engine: matcho
  matcho:
    request-method: get
    uri: "#/ResearchStudy/.+"
- engine: sql
  sql:
    query: |-
      SELECT true
      FROM "researchstudy"
      WHERE 
        id = {{route-params.id}}
        and "researchstudy".resource @> jsonb_build_object('collaborator', jsonb_build_array(jsonb_build_object('id', {{user.id}}::text)))
      limit 1

# 201 Created, 200 OK
```

Let's check it.

{% tabs %}
{% tab title="Jane reads smoking research" %}
```yaml
GET /ResearchStudy/smoking-research
Authorization: Bearer janes-access-token

# 200 OK
```
{% endtab %}

{% tab title="Jane reads diet research" %}
```yaml
GET /ResearchStudy/diet-research
Authorization: Bearer janes-access-token

# 403 Forbidden
```
{% endtab %}

{% tab title="Oscar reads diet research" %}
```yaml
GET /ResearchStudy/diet-research
Authorization: Bearer oscars-access-token

# 200 Forbidden
```
{% endtab %}
{% endtabs %}

We have secured one more endpoint. There are only two left.

### Search for patients

The endpoint to fetch all patients by group is

```
GET /Patient?_has:Group:member:_id=<group-id>
```

You may have a lot of questions to this request.

* what does mean `_has:Group:member:_id` and
* where do we know group id if we don't have access to Group resource?

#### What does mean `_has:Group:member:_id`?

The `_has` parameter is a one of standard search parameters in FHIR, called [reverse chaining](https://www.hl7.org/fhir/search.html#has). FHIR specification says:

> The `_has` parameter provides limited support for reverse chaining - that is, selecting resources based on the properties of resources that refer to them...

The `_has` parameter always goes with [modifiers](https://www.hl7.org/fhir/search.html#modifiers), which specify the search parameter. Let's get back and read the request we have.

```
GET /Patient?_has:Group:member:_id=<group-id>
```

This requests the server to return Patient resources, where the patient resource is referred to by at least one Group with id \<group-id>, and where the Group refers to the patient resource in the member search parameter.

#### Where do we know group id if we don't have access to Group resource?

Technically we don't need to have access to Group resource, we need only to know group id. And group id is available from ResearchStudy resource, we already have access to.

Thus, we may conclude the request is suitable for our needs. the AccessPolicy should check  existence of ResearchStudy with that \<group-id> and user-id in collaborators.

```yaml
PUT /AccessPolicy/user-can-access-patient-related-research-study-group

engine: complex
and:
- engine: matcho
  matcho:
    request-method: get
    uri: /Patient
    params: {'_has:Group:member:_id': 'present?'}
- engine: sql
  sql:
    query: |-
      SELECT true
      FROM "researchstudy"
      WHERE "researchstudy".resource @>
      jsonb_build_object('member', jsonb_build_array(jsonb_build_object('id', {{user.id}}::text)),
                         'enrollment', jsonb_build_array(jsonb_build_object('id', {{params._has:Group:member:_id}}::text)))
      limit 1
```

Let's check it.

{% tabs %}
{% tab title="First Tab" %}
```
...
```
{% endtab %}

{% tab title="Second Tab" %}

{% endtab %}
{% endtabs %}

### Search for observations

There is no mechanisms to query all Observation resources, related to a group of patients.&#x20;

```
GET /Observation?group=<group-id>
```

But FHIR R5 has:

```
GET /Observation?patient._has:Group:member:_id=group-1
```

They call it `_has` with chained-searches. Aidbox doesn't support it out of the box, but we may support for our specific case with Aidbox SearchParameter

The \_has parameter can also be used in chained-searches, for example https://build.fhir.org/search.html#has

We got authorized request for Observation resources, and all FHIR search parameters for Observation resoruce is also available. https://www.hl7.org/fhir/observation.html#search

```yaml
PUT /Search/Observation.group

name: group
where: '{{table}}.resource#>>''{subject,id}'' in (select member#>>''{entity,id}'' from "group", jsonb_array_elements(resource#>''{member}'') member where id = {{param}})'
resource: {id: Observation, resourceType: Entity}
```

```yaml
PUT /AccessPolicy/user-can-access-observation-related-research-study-group

engine: complex
and:
- engine: matcho
  matcho:
    request-method: get
    uri: /Observation
    params: {group: 'present?'}
- engine: sql
  sql:
    query: |-
      SELECT true
      FROM "researchstudy"
      WHERE "researchstudy".resource @>
      jsonb_build_object('member', jsonb_build_array(jsonb_build_object('id', {{user.id}}::text)),
                         'enrollment', jsonb_build_array(jsonb_build_object('id', {{params.group}}::text)))
      limit 1
```

## Conclusion

We implemented security policy, described in ReBAC model and gave researchers an access to secured FHIR endpoints. Now researcher may see

* list of studies,
* study details,
* list of patient,
* list of observations.

## Talk to a Health Samurai Engineer

If you'd like to learn more about using Aidbox or have any questions about this guide, [connect with us on Telegram](https://t.me/aidbox). We're happy to help.
