# Testing with Stresty

## requires to have your data publicly available. Power BI needs access to FHIR read operation `GET /fhir/<resourceType>` .Configure Your Box

Create a box here [https://aidbox.app/static/aidbox.html](https://aidbox.app/static/aidbox.html). See the [tutorial](../getting-started/run-aidbox-in-aidbox-sandbox.md) on how to create a new box.

Access the [REST Console](../overview/aidbox-ui/rest-console-1.md) of the created box. Execute the following requests. Their meaning you can read in the [tutorial](../modules-1/security-and-access-control/auth/basic-auth.md#register-client).

{% code title="Create a client:" %}
```yaml
POST /Client

id: basic
secret: secret
grant_types: ['basic']
```
{% endcode %}

{% code title="Create an access policy for your client:" %}
```yaml
POST /AccessPolicy

id: api-clients
engine: allow # which means it has permisions for everything
description: Root access to specific clients
link:
  # link policy with client
  - resourceType: Client
    id: basic # client.id
```
{% endcode %}

Create a folder on your disk for the stresty files. Go to the folder in a command line console. Execute the following command there to download _stresty.jar_:

```bash
curl -L -o stresty.jar https://github.com/Aidbox/stresty/releases/latest/download/stresty.jar
```

Create the following file in the same folder. Name it _test.yaml_.

{% code title="test.yaml" %}
```yaml
desc: Create & Read Patient

steps:
- id: clean
  desc: Clear all patients
  POST: /$sql
  body: 'TRUNCATE patient'
  match:
    status: 200
- id: create
  desc: Create test patient
  POST: /Patient
  body:
    id: pt-1
    name: [{given: ['Ivan'], family: 'Pupkin'}]
  match:
    status: 201
    body:
      id: pt-1
      name: [{given: ['Ivan']}] # Checks only given name
- id: read
  desc: Read our patient
  GET: /Patient/pt-1
  match:
    status: 200
    body:
      id: pt-1
      name: [{given: ['Ivan'], family: 'Pupkin'}]
- id: search-by-id
  GET: /Patient?_id=pt-1
  match:
    status: 200
    body:
      entry:
      - resource: {resourceType: 'Patient', id: 'pt-1'}
- id: update
  desc: Update our patient
  PUT: /Patient/pt-1
  body:
    name: [{given: ['Petr'], family: 'Pupkin'}]
  match:
    status: 200
    body:
      name: [{given: ['Petr'], family: 'Pupkin'}]
```
{% endcode %}

In the same folder, execute the following commands in the console.

### 1

{% hint style="warning" %}
Replace \<your\_box\_name> with the name of your box.
{% endhint %}

```bash
export AIDBOX_URL=http://<your_box_name>.aidbox.app
```

### 2

```
export AIDBOX_AUTH_TYPE=Basic
```

### 3

```
export AIDBOX_CLIENT_ID=basic
```

### 4

```
export AIDBOX_CLIENT_SECRET=secret
```

### 5

Now you are ready to run the test script with stresty:

```bash
java -jar stresty.jar test.yaml
```

The output will be the following:

```bash
$ java -jar stresty.jar test.yaml
Args: [test.yaml]
Configuration:
{:verbosity 0,
 :interactive false,
 :base-url "https://strestytests.edge.aidbox.app",
 :client-id "basic",
 :client-secret "secret",
 :authorization-type "Basic"}


run test case test.yaml
passed test.yaml

Test results: 5 passed,
              0 failed,
              0 skipped.
```

You can check your box, it now has a patient. Access your [REST Console](../overview/aidbox-ui/rest-console-1.md) and make the request:

{% tabs %}
{% tab title="Request" %}
{% code title="Execute in the box REST Console:" %}
```bash
GET  /Patient
```
{% endcode %}
{% endtab %}

{% tab title="Response" %}
{% code title="Status: 200" %}
```
query-time: 20
meta: {versionId: '10'}
type: searchset
resourceType: Bundle
total: 1
link:
- {relation: first, url: '/Patient?page=1'}
- {relation: self, url: '/Patient?page=1'}
query-timeout: 60000
entry:
- resource:
    name:
    - given: [Petr]
      family: Pupkin
    id: pt-1
    resourceType: Patient
    meta: {lastUpdated: '2020-02-17T19:33:13.568499Z', 
    createdAt: '2020-02-17T19:33:13.063733Z', versionId: '9'}
  fullUrl: https://strestytests.edge.aidbox.app/Patient/pt-1
query-sql: ['SELECT "patient".* FROM "patient" LIMIT ? OFFSET ? ', 100, 0]
```
{% endcode %}
{% endtab %}
{% endtabs %}

Read more about stresty, predicates and regexps [here](https://github.com/Aidbox/stresty).
