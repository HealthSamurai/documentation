---
description: Multibox box life cycle RPC methods
---

# Multibox box manager API

{% hint style="info" %}
Please start [a discussion](https://github.com/Aidbox/Issues/discussions) or [contact](broken-reference) us if you have question, feedback or suggestions.
{% endhint %}

Multibox box manager API uses Aidbox JSON [RPC API](broken-reference).

{% hint style="info" %}
Multibox API is accessible on the box manager URL. Please use an external REST client to access your Multibox server.
{% endhint %}

## RPC methods available

### `multibox/fhir-versions`

List FHIR versions supported by the server. One of these values must be used in `multibox/create-box`.

<details>

<summary>Example</summary>

{% code title="Request" %}
```bash
curl "multibox.example.host/rpc"  
 -H "Content-Type: application/json"   
 -d '{ "method": "multibox/fhir-versions" }'
```
{% endcode %}

{% code title="Response" %}
```json
{
  "result": [
    "fhir-4.0.1",
    "fhir-4.0.0",
    "fhir-3.3.0",
    "fhir-3.2.0",
    "fhir-3.0.1",
    "fhir-1.8.0",
    "fhir-1.4.0",
    "fhir-1.1.0",
    "fhir-1.0.2"
  ]
}
```
{% endcode %}

</details>

### `multibox/create-box`

Create a new box for the current user.

{% tabs %}
{% tab title="Parameters" %}
<table><thead><tr><th width="177">Parameter name</th><th width="103.66666666666666" data-type="checkbox">Required</th><th>Description</th></tr></thead><tbody><tr><td><strong>id</strong></td><td>true</td><td><p>ID of the box to create</p><p>Must match <code>/[a-z][a-z0-9]{4,}/</code></p></td></tr><tr><td><strong>fhirVersion</strong></td><td>true</td><td>FHIR version. Value must be from the <code>multibox/versions</code> response</td></tr><tr><td><strong>description</strong></td><td>false</td><td>description of the box to create</td></tr><tr><td><strong>env</strong></td><td>false</td><td><p>object with environment variables in</p><p><code>lower-kebab-case</code> (not<code>UPPER_SNAKE_CASE</code>)</p></td></tr></tbody></table>
{% endtab %}

{% tab title="Response" %}
| Property name   | Value      | Description                      |
| --------------- | ---------- | -------------------------------- |
| **id**          | **string** | ID of the created box            |
| **description** | **string** | Box description                  |
| **fhirVersion** | **string** | FHIR version                     |
| **import**      | **object** |                                  |
| **env**         | **object** | Envs used with BOX               |
| **participant** | **object** | Contains requester information   |
{% endtab %}
{% endtabs %}

<details>

<summary>Example</summary>

{% code title="Request" %}
```bash
curl "multibox.example.host/rpc"
  -H "Content-Type: application/json"
  -H "Authorization: Basic <credential-hash>"
  -d '{
       "method": "multibox/create-box", 
       "params": {
         "id": "testid",
         "fhirVersion": "fhir-4.0.1",
         "description": "Test box",
         "env": { "aidbox-stdout-pretty": "fatal" } 
     }
   }'
```
{% endcode %}

{% code title="Reponse" %}
```json
{
  "result": {
    "env": {
      "aidbox-stdout-pretty": "fatal"
    },
    "import": {
      "fhir-4.0.1": {}
    },
    "description": "Test box",
    "fhirVersion": "fhir-4.0.1",
    "participant": [
      {
        "role": "owner",
        "user": {
          "id": "admin",
          "resourceType": "User"
        }
      }
    ],
    "id": "testid",
    "resourceType": "Box",
    "meta": {
      "lastUpdated": "2023-03-03T13:21:58.989147Z",
      "createdAt": "2023-03-03T13:21:58.989147Z",
      "versionId": "7"
    }
  }
}

```
{% endcode %}

</details>

#### Creating box with Aidbox configuration project

{% hint style="info" %}
You can find more information about Aidbox configuration project [here](broken-reference).
{% endhint %}

To create box with Aidbox configuration project you can use this environments:

| Environment variable name      | Description                                                                                      |
| ------------------------------ | ------------------------------------------------------------------------------------------------ |
| `box-project-git-url`          | Where to clone your project from. Aidbox substitutes it to `git clone <url>` command.            |
| `box-project-git-target--path` | Where to clone your project to and where to read it from. Default value is a directory in `/tmp` |
| `aidbox-zen-entrypoint`        | It is a namespaced symbol which is tagged with `aidbox/system` tag                               |

<details>

<summary>Example with <code>box-project-git-url</code></summary>

{% code title="Request" %}
```bash
curl "localhost:8888/rpc" \
  -H "Content-Type: application/json" \
  -H "Authorization: Basic <credential-hash>" \
  -d '{
       "method": "multibox/create-box", 
       "params": {
         "id": "myboxname",
         "fhirVersion": "fhir-4.0.1",
         "env": {"box-project-git-url": "https://github.com/Aidbox/aidbox-project-template.git",
		 "aidbox-zen-entrypoint": "main/box"} 
     }
   }'
```
{% endcode %}

{% code title="Response" %}
```json
{
  "result": {
    "env": {
      "box-project-git-url": "https://github.com/Aidbox/aidbox-project-template.git",
      "aidbox-zen-entrypoint": "main/box"
    },
    "import": {
      "fhir-4.0.1": {}
    },
    "fhirVersion": "fhir-4.0.1",
    "participant": [
      {
        "role": "owner",
        "user": {
          "id": "admin",
          "resourceType": "User"
        }
      }
    ],
    "id": "myboxname",
    "resourceType": "Box"
  }
}
```
{% endcode %}

</details>

<details>

<summary>Example with <code>box-project-git-target--path</code></summary>

{% code title="Request" %}
```bash
curl "localhost:8888/rpc" \
  -H "Content-Type: application/json" \
  -H "Authorization: Basic <credential-hash>" \
  -d '{
       "method": "multibox/create-box", 
       "params": {
         "id": "myboxname",
         "fhirVersion": "fhir-4.0.1",
         "env": {"box-project-git-target--path": "/myproject",
		 "aidbox-zen-entrypoint": "main/box"} 
     }
   }'

```
{% endcode %}

{% code title="Response" %}
```json
{
  "result": {
    "env": {
      "aidbox-zen-entrypoint": "main/box",
      "box-project-git-target--path": "/myproject"
    },
    "import": {
      "fhir-4.0.1": {}
    },
    "fhirVersion": "fhir-4.0.1",
    "participant": [
      {
        "role": "owner",
        "user": {
          "id": "admin",
          "resourceType": "User"
        }
      }
    ],
    "id": "myboxname",
    "resourceType": "Box"
  }
}
```
{% endcode %}

</details>



### `multibox/list-boxes`

List boxes available for the current user.

{% tabs %}
{% tab title="Parameters" %}
_Expects no parameters_
{% endtab %}

{% tab title="Response" %}
| Property name   | Type       | Description                               |
| --------------- | ---------- | ----------------------------------------- |
| **id**          | **string** | Box ID                                    |
| **fhirVersion** | **string** | FHIR version of the box                   |
| **constraints** | **object** | Contains max and current number of boxes  |
{% endtab %}
{% endtabs %}

<details>

<summary>Example</summary>

{% code title="Request" %}
```bash
curl "multibox.example.host/rpc"   
-H "Content-Type: application/json"   
-H "Authorization: Basic <credential-hash>" 
-d '{
       "method": "multibox/list-boxes"
    }'
```
{% endcode %}

{% code title="Resource" %}
```json
{
  "result": {
    "list": [
      {
        "id": "testid",
        "fhirVersion": "fhir-4.0.1"
      }
    ],
    "constraints": null
  }
}

```
{% endcode %}

</details>

### `multibox/get-box`

Get box information.

{% tabs %}
{% tab title="Parameters" %}
`id` (required): id of the box
{% endtab %}

{% tab title="Response" %}
| Property name | Type | Description |
| ------------- | ---- | ----------- |
|               |      |             |
|               |      |             |
|               |      |             |

`description` - box description \
`meta` - meta info about the box resource \
`fhirVersion` \
`box-url` \
`access-url` - link to get admin access \
`participant`- collection of user resources \
`resourceType` \
`env` - object with environment variables in lower-kebab-case (not in UPPER\_SNAKE\_CASE) \
`access-token` \
`id`
{% endtab %}

{% tab title="Error" %}
`message`- "You do not have access to this box"\
`message` - "No box with id - "\
`message` - "No user session"
{% endtab %}
{% endtabs %}

<details>

<summary>Example</summary>

{% code title="Response" %}
```json
{
  "result": {
    "description": "Test box",
    "meta": {
      "lastUpdated": "2023-03-03T13:21:58.989147Z",
      "createdAt": "2023-03-03T13:21:58.989147Z",
      "versionId": "7"
    },
    "fhirVersion": "fhir-4.0.1",
    "box-url": "http://testid.127.0.0.1.nip.io:8788",
    "access-url": "http://testid.127.0.0.1.nip.io:8788/__sudo?token=<token>&redirect-uri=/ui/console",
    "participant": [
      {
        "role": "owner",
        "user": {
          "id": "admin",
          "resourceType": "User"
        }
      }
    ],
    "resourceType": "Box",
    "env": {
      "aidbox_stdout_pretty": "fatal"
    },
    "access-token": <token>,
    "id": "testid",
    "import": {
      "fhir-4.0.1": {}
    }
  }
}

```
{% endcode %}

</details>

### `multibox/delete-box`

{% hint style="danger" %}
This operation will drop the box database. Can not be undone!
{% endhint %}

Delete a box

{% tabs %}
{% tab title="Parameters" %}
`id` (required): id of the box
{% endtab %}

{% tab title="Response" %}
`description` - box description \
`meta` - meta info about the box resource \
`fhirVersion` \
`box-url` \
`access-url` - link to get admin access \
`participant`- collection of user resources \
`resourceType` \
`env` - object with environment variables in lower-kebab-case (not in UPPER\_SNAKE\_CASE) \
`access-token` \
`id`
{% endtab %}

{% tab title="Error" %}
`message` - "Only owner can delete box."\
`message` - "Cannot delete box: \n \<details>"\
`message` - `FHIR OperationOutcome`
{% endtab %}
{% endtabs %}

### `multibox/drop-box-caches`

Drop cache in every box

{% tabs %}
{% tab title="Parameters" %}
_expects no parameters._
{% endtab %}

{% tab title="Response" %}
String: "ok".
{% endtab %}
{% endtabs %}
