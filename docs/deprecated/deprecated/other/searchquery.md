---
description: Managed SQL for Search API
---

# SearchQuery

With **SearchQuery** resource, you can define "managed" SQL for Search API with parameters, paging, sorting, and includes.

| Parameter name        | Description                                               |
| --------------------- | --------------------------------------------------------- |
| **\_count**           | A number of records returned per page                     |
| **\_page**            | Controls pagination                                       |
| **\_total**           | The maximum number of results returned by a search result |
| **\_timeout**         | Defines query timeout                                     |
| **join**              | Allows you to join related resources for search           |
| **order-by**          | Defines the ordering of the search results                |
| **includes**          | Allows you to predefine included resources                |
| **reverse**           | Includes resources that refer resources from your query   |
| **\_explain=analyze** | Helps to inspect the execution plan of a search query     |

{% hint style="info" %}
If you want to use arbitrary SQL (e.g. `LEFT JOIN`), consider [AidboxQuery](../../../api/rest-api/aidbox-search.md#aidboxquery).
{% endhint %}

### Prepare example data

We need some sample data to see the results of example queries. Let's create it.\
Copy the following snippet to the Aidbox `REST Console`.

{% tabs %}
{% tab title="Request (Aidbox format)" %}
```yaml
POST /

type: transaction
entry:
- resource:
    id: pr-1
    name:
    - given: [Ted]
      family: 'Scott'
  request:
    method: POST
    url: "/Practitioner"

- resource:
    id: pr-2
    name:
    - given: [Tommy]
      family: 'Peterson'
  request:
    method: POST
    url: "/Practitioner"

- resource:
    id: org1
    name: 'Test hospital1'
  request:
    method: POST
    url: "/Organization"

- resource:
    id: org2
    name: 'Test hospital2'
  request:
    method: POST
    url: "/Organization"

- resource:
    id: patient1
    name:
    - given: [Max]
      family: Johnson
    gender: male
    managingOrganization: {resourceType: Organization, id: org1, display: 'Test hospital1'}
    birthDate: '1960-10-10'
  request:
    method: POST
    url: "/Patient"

- resource:
    id: patient2
    name:
    - given: [Alex]
      family: Smith
    gender: male
    managingOrganization: {resourceType: Organization, id: org2, display: 'Test hospital2'}
    birthDate: '1990-01-01'
  request:
    method: POST
    url: "/Patient"

- resource:
    id: enc1
    status: planned
    subject:
      resourceType: Patient
      id: patient1
    class:
      code: abc
  request:
    method: POST
    url: "/Encounter"

- resource:
    id: enc2
    status: finished
    subject:
      resourceType: Patient
      id: patient1
    class:
      code: abc
  request:
    method: POST
    url: "/Encounter"

- resource:
    id: enc3
    status: planned
    subject:
      resourceType: Patient
      id: patient2
    class:
      code: abc
  request:
    method: POST
    url: "/Encounter"

- resource:
    id: apt1
    description: "Test appointment 1"
    start: '2020-12-10T09:00:00Z'
    end: '2020-12-10T11:00:00Z'
    status: booked
    participant: [{ actor: { resourceType: Patient, id: patient1}, status: accepted},{ actor: { resourceType: Practitioner, id: pr-1}, status: accepted}]
  request:
    method: POST
    url: "/Appointment"

- resource:
    id: apt2
    description: "Test appointment 2"
    start: '2021-04-10T09:00:00Z'
    end: '2021-04-10T11:00:00Z'
    status: booked
    participant: [{ actor: { resourceType: Patient, id: patient2}, status: accepted}, { actor: { resourceType: Practitioner, id: pr-2}, status: accepted}]
  request:
    method: POST
    url: "/Appointment"
```
{% endtab %}

{% tab title="Response (Aidbox format)" %}
```yaml
# Status: 200
resourceType: Bundle
type: transaction-response
id: '244'
entry:
  - resource:
      name:
        - given:
            - Ted
          family: Scott
      id: pr-1
      resourceType: Practitioner
      meta:
        lastUpdated: '2021-04-19T12:18:14.183626Z'
        createdAt: '2021-04-19T12:18:14.183626Z'
        versionId: '244'
    response:
      etag: '244'
      cache-control: no-cache
      last-modified: 'Mon, 19 Apr 2021 12:18:14 GMT'
      location: /Practitioner/pr-1/_history/244
      x-duration: 54
      x-request-id: c9481d21-a93e-4bbd-940e-d7221ad45110
      status: '201'
  - resource:
      name:
        - given:
            - Tommy
          family: Peterson
      id: pr-2
      resourceType: Practitioner
      meta:
        lastUpdated: '2021-04-19T12:18:14.183626Z'
        createdAt: '2021-04-19T12:18:14.183626Z'
        versionId: '244'
    response:
      etag: '244'
      cache-control: no-cache
      last-modified: 'Mon, 19 Apr 2021 12:18:14 GMT'
      location: /Practitioner/pr-2/_history/244
      x-duration: 14
      x-request-id: c9481d21-a93e-4bbd-940e-d7221ad45110
      status: '201'
  - resource:
      name: Test hospital1
      id: org1
      resourceType: Organization
      meta:
        lastUpdated: '2021-04-19T12:18:14.183626Z'
        createdAt: '2021-04-19T12:18:14.183626Z'
        versionId: '244'
    response:
      etag: '244'
      cache-control: no-cache
      last-modified: 'Mon, 19 Apr 2021 12:18:14 GMT'
      location: /Organization/org1/_history/244
      x-duration: 16
      x-request-id: c9481d21-a93e-4bbd-940e-d7221ad45110
      status: '201'
  - resource:
      name: Test hospital2
      id: org2
      resourceType: Organization
      meta:
        lastUpdated: '2021-04-19T12:18:14.183626Z'
        createdAt: '2021-04-19T12:18:14.183626Z'
        versionId: '244'
    response:
      etag: '244'
      cache-control: no-cache
      last-modified: 'Mon, 19 Apr 2021 12:18:14 GMT'
      location: /Organization/org2/_history/244
      x-duration: 11
      x-request-id: c9481d21-a93e-4bbd-940e-d7221ad45110
      status: '201'
  - resource:
      name:
        - given:
            - Max
          family: Johnson
      gender: male
      birthDate: '1960-10-10'
      managingOrganization:
        id: org1
        display: Test hospital1
        resourceType: Organization
      id: patient1
      resourceType: Patient
      meta:
        lastUpdated: '2021-04-19T12:18:14.183626Z'
        createdAt: '2021-04-19T12:18:14.183626Z'
        versionId: '244'
    response:
      etag: '244'
      cache-control: no-cache
      last-modified: 'Mon, 19 Apr 2021 12:18:14 GMT'
      location: /Patient/patient1/_history/244
      x-duration: 24
      x-request-id: c9481d21-a93e-4bbd-940e-d7221ad45110
      status: '201'
  - resource:
      name:
        - given:
            - Alex
          family: Smith
      gender: male
      birthDate: '1990-01-01'
      managingOrganization:
        id: org2
        display: Test hospital2
        resourceType: Organization
      id: patient2
      resourceType: Patient
      meta:
        lastUpdated: '2021-04-19T12:18:14.183626Z'
        createdAt: '2021-04-19T12:18:14.183626Z'
        versionId: '244'
    response:
      etag: '244'
      cache-control: no-cache
      last-modified: 'Mon, 19 Apr 2021 12:18:14 GMT'
      location: /Patient/patient2/_history/244
      x-duration: 11
      x-request-id: c9481d21-a93e-4bbd-940e-d7221ad45110
      status: '201'
  - resource:
      status: planned
      subject:
        id: patient1
        resourceType: Patient
      id: enc1
      resourceType: Encounter
      meta:
        lastUpdated: '2021-04-19T12:18:14.183626Z'
        createdAt: '2021-04-19T12:18:14.183626Z'
        versionId: '244'
    response:
      etag: '244'
      cache-control: no-cache
      last-modified: 'Mon, 19 Apr 2021 12:18:14 GMT'
      location: /Encounter/enc1/_history/244
      x-duration: 14
      x-request-id: c9481d21-a93e-4bbd-940e-d7221ad45110
      status: '201'
  - resource:
      status: finished
      subject:
        id: patient1
        resourceType: Patient
      id: enc2
      resourceType: Encounter
      meta:
        lastUpdated: '2021-04-19T12:18:14.183626Z'
        createdAt: '2021-04-19T12:18:14.183626Z'
        versionId: '244'
    response:
      etag: '244'
      cache-control: no-cache
      last-modified: 'Mon, 19 Apr 2021 12:18:14 GMT'
      location: /Encounter/enc2/_history/244
      x-duration: 9
      x-request-id: c9481d21-a93e-4bbd-940e-d7221ad45110
      status: '201'
  - resource:
      status: planned
      subject:
        id: patient2
        resourceType: Patient
      id: enc3
      resourceType: Encounter
      meta:
        lastUpdated: '2021-04-19T12:18:14.183626Z'
        createdAt: '2021-04-19T12:18:14.183626Z'
        versionId: '244'
    response:
      etag: '244'
      cache-control: no-cache
      last-modified: 'Mon, 19 Apr 2021 12:18:14 GMT'
      location: /Encounter/enc3/_history/244
      x-duration: 10
      x-request-id: c9481d21-a93e-4bbd-940e-d7221ad45110
      status: '201'
  - resource:
      end: '2020-12-10T11:00:00.000Z'
      start: '2020-12-10T09:00:00.000Z'
      status: booked
      description: Test appointment 1
      participant:
        - actor:
            id: patient1
            resourceType: Patient
          status: accepted
        - actor:
            id: pr-1
            resourceType: Practitioner
          status: accepted
      id: apt1
      resourceType: Appointment
      meta:
        lastUpdated: '2021-04-19T12:18:14.183626Z'
        createdAt: '2021-04-19T12:18:14.183626Z'
        versionId: '244'
    response:
      etag: '244'
      cache-control: no-cache
      last-modified: 'Mon, 19 Apr 2021 12:18:14 GMT'
      location: /Appointment/apt1/_history/244
      x-duration: 20
      x-request-id: c9481d21-a93e-4bbd-940e-d7221ad45110
      status: '201'
  - resource:
      end: '2021-04-10T11:00:00.000Z'
      start: '2021-04-10T09:00:00.000Z'
      status: booked
      description: Test appointment 2
      participant:
        - actor:
            id: patient2
            resourceType: Patient
          status: accepted
        - actor:
            id: pr-2
            resourceType: Practitioner
          status: accepted
      id: apt2
      resourceType: Appointment
      meta:
        lastUpdated: '2021-04-19T12:18:14.183626Z'
        createdAt: '2021-04-19T12:18:14.183626Z'
        versionId: '244'
    response:
      etag: '244'
      cache-control: no-cache
      last-modified: 'Mon, 19 Apr 2021 12:18:14 GMT'
      location: /Appointment/apt2/_history/244
      x-duration: 21
      x-request-id: c9481d21-a93e-4bbd-940e-d7221ad45110
      status: '201'
```
{% endtab %}
{% endtabs %}

We created 2 patients, 2 practitioners, 3 encounters, 2 appointments, 2 Managing organizations that are linked to each other.

### Define search query with filtering

Let's define the search query to search old patients by the partial match of the family name with the filtering by gender:

{% tabs %}
{% tab title="Aidbox format" %}
```yaml
PUT /SearchQuery/q-1

# attach this query to Patient resource type
resource: {id: 'Patient', resourceType: 'Entity'}
# give alias to patient table
as: pt
# enable total query
total: true 
# basic query
query:
  where: "(pt.resource->>'birthDate')::date < '1980-01-01'"
  order-by: pt.id desc
params:
   gender:
     type: string
     where: "pt.resource->>'gender' = {{params.gender}}"
   family:
     type: string
     format: '% ?%'
     where: |
       aidbox_text_search(knife_extract_text(pt.resource, $$[["name","family"]]$$)) 
       ilike {{params.family}}
```
{% endtab %}
{% endtabs %}

Now we can call this query with `/alpha/<resourceType>?query=<query-name>&params....`:

{% tabs %}
{% tab title="Aidbox format" %}
```yaml
GET /alpha/Patient?query=q-1&_page=1&_count=3&_total=none

# 200
resourceType: Bundle
type: searchset
entry: [...]
query-sql: |
  SELECT *
  FROM "patient" pt
  WHERE /* query */ (pt.resource->>'birthDate')::date < '1980-01-01'
  ORDER BY pt.id desc
  LIMIT 100
query-timeout: 60000
```
{% endtab %}
{% endtabs %}

You can use **count** and **page** parameters for paging and control total query (if enabled) with **total** parameter. Use **\_timeout** parameter to set query timeout.

If the parameter is provided, another query will be generated on the fly:

{% tabs %}
{% tab title="Aidbox format" %}
```yaml
GET /alpha/Patient?query=q-1&family=joh

# 200

resourceType: Bundle
type: searchset
entry: [...]
query-sql:
- | 
  SELECT *
  FROM \"patient\" pt
  WHERE /* query */ (pt.resource->>'birthDate')::date < '1980-01-01'
    AND /* family */ aidbox_text_search(knife_extract_text(pt.resource, $$[[\"name\",\"family\"]]$$)) 
    ilike ?\nORDER BY pt.id desc
    LIMIT 100"
- '% joh%'
```
{% endtab %}
{% endtabs %}

### Define search query with JOIN

Your parameters and basic query can use join attribute to join related resources for search:

{% tabs %}
{% tab title="Aidbox format" %}
```yaml
PUT /SearchQuery/q-2

resource: {id: 'Encounter', resourceType: 'Entity'}
as: enc
query:
  order-by: pt.id desc
params:
   pt:
     type: string
     format: '% ?%'
     join:
       pt: 
         table: patient
         by: "enc.resource#>>'{subject,id}' = pt.id"
     where: |
        aidbox_text_search(knife_extract_text(pt.resource, $$[["name","family"]]$$)) 
        ilike {{params.pt}}
```
{% endtab %}
{% endtabs %}

{% tabs %}
{% tab title="Aidbox format" %}
```yaml
GET /alpha/Encounter?query=q-2&pt=joh

# 200
resourceType: Bundle
type: searchset
entry: [...]
query-sql:
-  |
  SELECT *
  FROM \"encounter\" enc
  JOIN \"patient\" pt
    ON enc.resource#>>'{subject,id}' = pt.id
  WHERE /* pt */ aidbox_text_search(knife_extract_text(pt.resource, $$[[\"name\",\"family\"]]$$)) 
   ilike ?
  ORDER BY pt.id desc\nLIMIT 100"
- '% joh%'
```
{% endtab %}
{% endtabs %}

### Add order-by into parameters

Both `query` and `params` support `order-by`. `order-by` in query has the least precedence. `order-by` in params are added in top-down order. e.g. `order-by` in first search parameter has the most precedence.

Example: create search query

{% tabs %}
{% tab title="Aidbox format" %}
```yaml
PUT /SearchQuery/sq

as: ap
query:
  order-by: "ap.resource->>'start' ASC"
resource:
  id: 'Appointment'
  resourceType: 'Entity'
params:
  ord-dir:
    type: string
    format: '?'
    order-by: |
      CASE WHEN {{params.ord-dir}} = 'asc' THEN ap.resource->>'start' END ASC,
      CASE WHEN {{params.ord-dir}} = 'desc' THEN ap.resource->>'start' END DESC
```
{% endtab %}
{% endtabs %}

Example: use this search query

{% tabs %}
{% tab title="Aidbox format" %}
```yaml
GET /alpha/Appointment?query=sq&ord-dir=desc

#200

resourceType: Bundle
type: searchset
entry:
  - resource:
      start: '2021-04-02T16:02:50.996+03:00'
      # omitted
  - resource:
      start: '2021-02-02T16:02:50.997+03:00'
      # omitted
  - resource:
      start: '2020-02-02T16:02:50.997+03:00'
      # omitted
# omitted
```
{% endtab %}
{% endtabs %}

### Include related resources

You can predefine included resources for SearchQuery with **includes** property:

{% tabs %}
{% tab title="Aidbox format" %}
```yaml
PUT /SearchQuery/inc

resourceType: SearchQuery
resource: {id: Encounter, resourceType: Entity}
as: enc
total: true
includes:
  # name for include
  subject:
    # path to reference
    path: [subject]
    # ref to resource
    resource: {id: Patient, resourceType: Entity}
    # nested includes
    includes:
      organization:
        path: [managingOrganization]
        resource: {id: Organization, resourceType: Entity}
query: {order-by: enc.id}
limit: 40
```
{% endtab %}
{% endtabs %}

Use the created query:

{% tabs %}
{% tab title="Aidbox format" %}
```yaml
GET /alpha/Encounter?query=inc

#200

resourceType: Bundle
type: searchset
entry:
  - resource:
      status: planned
      subject:
        id: patient1
        resourceType: Patient
      id: enc1
      resourceType: Encounter
      meta:
        lastUpdated: '2021-04-19T12:18:14.183626Z'
        createdAt: '2021-04-19T12:18:14.183626Z'
        versionId: '244'
  - resource:
      status: finished
      subject:
        id: patient1
        resourceType: Patient
      id: enc2
      resourceType: Encounter
      meta:
        lastUpdated: '2021-04-19T12:18:14.183626Z'
        createdAt: '2021-04-19T12:18:14.183626Z'
        versionId: '244'
  - resource:
      status: planned
      subject:
        id: patient2
        resourceType: Patient
      id: enc3
      resourceType: Encounter
      meta:
        lastUpdated: '2021-04-19T12:18:14.183626Z'
        createdAt: '2021-04-19T12:18:14.183626Z'
        versionId: '244'
  - resource:
      name:
        - given:
            - Max
          family: Johnson
      gender: male
      birthDate: '1960-10-10'
      managingOrganization:
        id: org1
        display: Test hospital1
        resourceType: Organization
      id: patient1
      resourceType: Patient
      meta:
        lastUpdated: '2021-04-19T12:18:14.183626Z'
        createdAt: '2021-04-19T12:18:14.183626Z'
        versionId: '244'
  - resource:
      name:
        - given:
            - Alex
          family: Smith
      gender: male
      birthDate: '1990-01-01'
      managingOrganization:
        id: org2
        display: Test hospital2
        resourceType: Organization
      id: patient2
      resourceType: Patient
      meta:
        lastUpdated: '2021-04-19T12:18:14.183626Z'
        createdAt: '2021-04-19T12:18:14.183626Z'
        versionId: '244'
  - resource:
      name: Test hospital1
      id: org1
      resourceType: Organization
      meta:
        lastUpdated: '2021-04-19T12:18:14.183626Z'
        createdAt: '2021-04-19T12:18:14.183626Z'
        versionId: '244'
  - resource:
      name: Test hospital2
      id: org2
      resourceType: Organization
      meta:
        lastUpdated: '2021-04-19T12:18:14.183626Z'
        createdAt: '2021-04-19T12:18:14.183626Z'
        versionId: '244'
query-sql:
  - |-
    SELECT enc.*
    FROM "encounter" enc
    ORDER BY enc.id
    LIMIT 40
query-timeout: 60000
total: 3
total-query:
  - |-
    SELECT count(*)
    FROM "encounter" enc
```
{% endtab %}
{% endtabs %}

#### Reverse includes

To include resources that refer resources from your query, you can add **reverse**: true attribute:

{% tabs %}
{% tab title="Aidbox format" %}
```yaml
PUT /SearchQuery/revinc

resourceType: SearchQuery
resource: {id: Patient, resourceType: Entity}
as: pt
total: true
includes:
  encounters:
    # means that reference going from Encounter to patient
    reverse: true
    path: [subject]
    resource: {id: Encounter, resourceType: Entity}
    where: "resource->>'status' = 'finished'"
limit: 40
```
{% endtab %}
{% endtabs %}

Execute the created query

{% tabs %}
{% tab title="Aidbox format" %}
```yaml
GET /alpha/Encounter?query=revinc

#200

resourceType: Bundle
type: searchset
entry:
  - resource:
      name:
        - text: Alex
      gender: male
      address:
        - city: New-York
      telecom:
        - value: fhir
      birthDate: '1988-04-16'
      id: b0cab43b-ba3e-4192-9ee6-851fb15ebc5f
      resourceType: Patient
      meta:
        lastUpdated: '2021-04-16T14:01:51.973363Z'
        createdAt: '2021-04-16T11:43:36.524830Z'
        versionId: '143'
  - resource:
      name:
        - given:
            - Max
        - family: Smith
      gender: male
      address:
        - city: Hello
          line:
            - 123 Oxygen St
          state: NY
          district: World
          postalCode: '3212'
      telecom:
        - use: home
        - use: work
          rank: 1
          value: (32) 8934 1234
          system: phone
      birthDate: '1960-10-10'
      id: 6e690b70-c55d-4efc-89d4-38257d37a774
      resourceType: Patient
      meta:
        lastUpdated: '2021-04-19T09:35:48.183189Z'
        createdAt: '2021-04-19T09:35:48.183189Z'
        versionId: '163'
  - resource:
      name:
        - given:
            - Max
          family: Johnson
      gender: male
      birthDate: '1960-10-10'
      managingOrganization:
        id: org1
        display: Test hospital1
        resourceType: Organization
      id: patient1
      resourceType: Patient
      meta:
        lastUpdated: '2021-04-19T12:18:14.183626Z'
        createdAt: '2021-04-19T12:18:14.183626Z'
        versionId: '244'
  - resource:
      name:
        - given:
            - Alex
          family: Smith
      gender: male
      birthDate: '1990-01-01'
      managingOrganization:
        id: org2
        display: Test hospital2
        resourceType: Organization
      id: patient2
      resourceType: Patient
      meta:
        lastUpdated: '2021-04-19T12:18:14.183626Z'
        createdAt: '2021-04-19T12:18:14.183626Z'
        versionId: '244'
  - resource:
      status: finished
      subject:
        id: patient1
        resourceType: Patient
      id: enc2
      resourceType: Encounter
      meta:
        lastUpdated: '2021-04-19T12:18:14.183626Z'
        createdAt: '2021-04-19T12:18:14.183626Z'
        versionId: '244'
query-sql:
  - |-
    SELECT pt.*
    FROM "patient" pt
    LIMIT 40
query-timeout: 60000
total: 4
total-query:
  - |-
    SELECT count(*)
    FROM "patient" pt
```
{% endtab %}
{% endtabs %}

#### Path in includes

Path expression in includes is `json_knife` extension path, it consists of strings, integers, and objects. If the item is path string, it means get key in object (arrays are implicitly flattened). If key is integer, it is interpreted as index in array. If key is object, it is pattern to filter values in array with inclusion semantic (like PostgreSQL JSONB operator `@>`).

Here is an example of how to extract a patient (code: PART) from the appointment:

{% hint style="info" %}
The following example is prepared to be executed in the DB Console
{% endhint %}

{% tabs %}
{% tab title="Aidbox DB Console Request" %}
```yaml
select knife_extract(
  '{
     "resourceType" : "Appointment",
     "status" : "active",
     "participant" : [ {
       "type" : [ {
         "text" : "Patient",
         "coding" : [ {
           "code" : "PART"
         } ]
       } ],
       "actor" : {
         "id" : "patient2",
         "resourceType" : "Patient"
       },
       "status" : "active"
     }, {
       "type" : [ {
         "text" : "Admit",
         "coding" : [ {
           "code" : "ADM"
         } ]
       } ],
       "actor" : {
         "id" : "pr-2",
         "resourceType" : "Practitioner"
       },
       "status" : "active"
     } ]
   }',
   '[["participant", {"type": [{"coding": [{"code": "PART"}]}]}, "actor"]]'
)
```
{% endtab %}

{% tab title="Aidbox DB Console Response" %}
```
knife_extract
- '{"id": "patient2", "resourceType": "Patient"}'
```
{% endtab %}
{% endtabs %}

#### Parametrised includes

Include query can be parametrised if you define include inside params. You can use `where` key to add additional filter on included resources.

{% tabs %}
{% tab title="Aidbox format" %}
```yaml
PUT /SearchQuery/cond-incl

resource: {id: 'Patient', resourceType: 'Entity'}
as: pt
query:
  order-by: pt.id desc
params:
   obs-cat:
     type: string
     includes: 
        obs:
          reverse: true
          path: ["patient"]
          resource: {id: 'Observation', resourceType: 'Entity'}
          where: "resource#>>'{category,0,coding,0,code}' = {{params.category}}"
          
---

GET /alpha/Patient?query=cond-incl&category=labs
# will add filtered include

GET /alpha/Patient?query=cond-incl
# will skip include

```
{% endtab %}
{% endtabs %}

If you want to provide default include, define include with the same key on query level and in parameter. Parameter include will override the default in case parameter is provided in the request.

{% tabs %}
{% tab title="Aidbox format" %}
```yaml
PUT /SearchQuery/cond-incl

resource: {id: 'Patient', resourceType: 'Entity'}
as: pt
query:
  order-by: pt.id desc
includes:
  # default include with filter
  obs:
    reverse: true
    path: ["patient"]
    resource: {id: 'Observation', resourceType: 'Entity'}
    where: "resource#>>'{category,0,coding,0,code}' = 'default"

params:
   obs-cat:
     type: string
     # override default include
     includes: 
        obs:
          where: "resource#>>'{category,0,coding,0,code}' = {{params.category}}"
          
```
{% endtab %}
{% endtabs %}

### EXPLAIN ANALYZE

With the parameter `_explain=analyze` , you can inspect the execution plan of a search query:

{% tabs %}
{% tab title="Aidbox fromat" %}
```yaml
GET /alpha/Encounter?query=q-2&pt=joh&_explain=analyze

# 200

query: |-
  EXPLAIN ANALYZE SELECT *FROM \"encounter\" enc
  JOIN \"patient\" pt
    ON enc.resource#>>'{subject,id}' = pt.id
  WHERE /* pt */ aidbox_text_search(knife_extract_text(pt.resource, $$[[\"name\",\"family\"]]$$)) 
    ilike ?
    ORDER BY pt.id desc
    LIMIT 100"
params: ['% joh%']
explain: |-
  Limit  (cost=1382.90..1382.97 rows=28 width=882) (actual time=4.274..4.274 rows=0 loops=1)
    ->  Sort  (cost=1382.90..1382.97 rows=28 width=882) (actual time=4.272..4.272 rows=0 loops=1)
          Sort Key: pt.id DESC
          Sort Method: quicksort  Memory: 25kB
          ->  Hash Join  (cost=951.07..1382.23 rows=28 width=882) (actual time=4.247..4.248 rows=0 loops=1)
                Hash Cond: ((enc.resource #>> '{subject,id}'::text[]) = pt.id)
                ->  Seq Scan on encounter enc  (cost=0.00..421.60 rows=3460 width=839) (actual time=0.779..1.544 rows=3460 loops=1)
                ->  Hash  (cost=950.95..950.95 rows=10 width=38) (actual time=1.375..1.375 rows=1 loops=1)
                      Buckets: 1024  Batches: 1  Memory Usage: 9kB
                      ->  Seq Scan on patient pt  (cost=0.00..950.95 rows=10 width=38) (actual time=1.370..1.371 rows=1 loops=1)
                            Filter: (immutable_wrap_ws(immutable_unaccent(immutable_array_to_string(knife_extract_text(resource, '[["name", "family"]]'::jsonb), ' '::text))) ~~* '% joh%'::text)
                            Rows Removed by Filter: 1
  Planning Time: 9.345 ms
  Execution Time: 4.564 ms
total-query: "EXPLAIN ANALYZE SELECT count(*)\nFROM \"encounter\" enc\nJOIN \"patient\" pt\n  ON enc.resource#>>'{subject,id}' = pt.id\nWHERE /* pt */ aidbox_text_search(knife_extract_text(pt.resource, $$[[\"name\",\"family\"]]$$)) \nilike ?"
total-explain: |-
  Aggregate  (cost=1382.30..1382.31 rows=1 width=8) (actual time=3.257..3.257 rows=1 loops=1)
    ->  Hash Join  (cost=951.07..1382.23 rows=28 width=0) (actual time=3.254..3.254 rows=0 loops=1)
          Hash Cond: ((enc.resource #>> '{subject,id}'::text[]) = pt.id)
          ->  Seq Scan on encounter enc  (cost=0.00..421.60 rows=3460 width=772) (actual time=0.286..0.910 rows=3460 loops=1)
          ->  Hash  (cost=950.95..950.95 rows=10 width=5) (actual time=1.198..1.199 rows=1 loops=1)
                Buckets: 1024  Batches: 1  Memory Usage: 9kB
                ->  Seq Scan on patient pt  (cost=0.00..950.95 rows=10 width=5) (actual time=1.195..1.195 rows=1 loops=1)
                      Filter: (immutable_wrap_ws(immutable_unaccent(immutable_array_to_string(knife_extract_text(resource, '[["name", "family"]]'::jsonb), ' '::text))) ~~* '% joh%'::text)
                      Rows Removed by Filter: 1
  Planning Time: 6.716 ms
  Execution Time: 3.543 ms
```
{% endtab %}
{% endtabs %}

### Debug SearchQuery

You can debug SearchQuery with multiple parameters combinations without saving resource by `POST /SearchQuery/$debug`. You can simulate requests with different parameters by **tests** attribute. Aidbox will return results and explanation for each test:

{% tabs %}
{% tab title="Aidbox format" %}
```yaml
POST /SearchQuery/$debug

# explain all queries
explain: true
# timeout for query in ms
timeout: 2000
# test with requests
tests: 
  # name of request
  only-pid:
    # params for request
    params: {pid: 'patient1'}
  only-ts:
    params: {ts: '2019-01-01'}
  both:
    params: {pid: 'patient1', ts: 'ups'}
# SearchQuery defnition
query:
  resource: {id: Patient, resourceType: Entity}
  as: pt
  params:
    pid: {type: string, isRequired: true, where: 'pt.id = {{params.pid}}'}
    ts: {type: date, where: 'pt.tis >= {{params.date}}'}
  query: {order-by: pt.ts desc}
  limit: 40
  
  
  # 200
  
only-pid:
  params:
    pid: patient1
    _timeout: 2000
  result:
    resourceType: Bundle
    type: searchset
    entry:
      - resource:
          name:
            - given:
                - Max
              family: Johnson
          gender: male
          birthDate: '1960-10-10'
          managingOrganization:
            id: org1
            display: Test hospital1
            resourceType: Organization
          id: patient1
          resourceType: Patient
          meta:
            lastUpdated: '2021-04-19T12:18:14.183626Z'
            createdAt: '2021-04-19T12:18:14.183626Z'
            versionId: '244'
    query-timeout: 2000000
  explain:
    query: |-
      EXPLAIN ANALYZE SELECT * FROM "patient" pt
      WHERE /* pid */ pt.id = ?
      ORDER BY pt.ts desc
      LIMIT 40
    params:
      - patient1
    explain: >-
      Limit  (cost=8.18..8.18 rows=1 width=124) (actual time=0.089..0.236 rows=1
      loops=1)

        ->  Sort  (cost=8.18..8.18 rows=1 width=124) (actual time=0.074..0.101
      rows=1 loops=1)

              Sort Key: ts DESC

              Sort Method: quicksort  Memory: 25kB

              ->  Index Scan using patient_pkey on patient pt  (cost=0.15..8.17
      rows=1 width=124) (actual time=0.037..0.053 rows=1 loops=1)

                    Index Cond: (id = 'patient1'::text)

      Planning Time: 0.185 ms

      Execution Time: 0.302 ms
only-ts:
  status: error
  params:
    ts: '2019-01-01'
    _timeout: 2000
  errors:
    - details: Parameter pid is required
both:
  params:
    pid: patient1
    ts: ups
    _timeout: 2000
  result:
    status: error
    query:
      - |-
        SELECT pt.*
        FROM "patient" pt
        WHERE /* pid */ pt.id = ?
          AND /* ts */ pt.tis >= ?
        ORDER BY pt.ts desc
        LIMIT 40
      - patient1
      - null
    error: |-
      ERROR: column pt.tis does not exist
        Hint: Perhaps you meant to reference the column "pt.ts".
        Position: 73
```
{% endtab %}
{% endtabs %}
