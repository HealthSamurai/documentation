---
description: This guide shows how to get set up SMART on FHIR in Aidbox
---

# Set up SMART on FHIR in Aidbox

SMART on FHIR specifies authentication/authorization scheme for FHIR Applications. This scheme extends OAuth 2.0 and OpenID. To enable [SMART on FHIR](https://smarthealthit.org/) you need to create an [Aidbox project](../../../zen-related/aidbox-zen-lang-project/) and configure SMART API routes using the [Aidbox API Constructor](../../../zen-related/api-constructor-docs-beta.md).

{% hint style="info" %}
You can use the configuration provided in the [Aidbox project samples](https://github.com/Aidbox/aidbox-project-samples) repo.
{% endhint %}

## Set Up Aidbox Project

Clone the [Aidbox project samples](https://github.com/Aidbox/aidbox-project-samples) repo:

```shell-session
$ git clone https://github.com/Aidbox/aidbox-project-samples.git
```

In the `aidbox-project-samples/smart-on-fhir/core.edn` file you can see example of the API constructor configuration.

Currently only two middlewares for SMART on FHIR authorization are implemented:

* `:smart.fhir/single-patient-auth-middleware` — Patient launch
* `:smart.fhir/authorization-middleware` — Provider launch

## Create Resources

To use SMART on FHIR you need to create a few resources like Client and AccessPolicies

### Client

Let's create an [Aidbox Client](broken-reference):

```yaml
POST /Client
Content-Type: text/yaml
Accept: text/yaml

resourceType: Client
id: smart-client
secret: some-secret
auth:
  authorization_code:
    redirect_uri: https://your/redirect/uri
    refresh_token: true
    secret_required: true
    access_token_expiration: 36000
smart:
  launch_uri: https://your/launch/uri
grant_types:
  - authorization_code
```

The `launch_uri` parameter here specifies the launch URI for the EHR-based SMART App launch.

### Access Policies

You likely want to add some [Access Policies](../../../../../modules/security-and-access-control/security/) to allow users see their data. Here we provide some examples for reference:

```yaml
PUT /
Content-Type: text/yaml
Accept: text/yaml

# Allow patients to search resources linked with them
- resourceType: AccessPolicy
  engine: matcho
  matcho:
    uri: '#/smart/.*'
    params:
      patient: .role.links.patient.id
  roleName: patient
  id: patient-smart

# Allow patients to read their info
- resourceType: AccessPolicy
  engine: matcho
  matcho:
    uri: '#/smart/Patient/.*'
    params:
      id: .role.links.patient.id
  roleName: patient
  id: smart-patient-read

# Allow patients to read their info
- resourceType: AccessPolicy
  engine: matcho
  matcho:
    uri: '#/smart/Patient'
    params:
      _id: .role.links.patient.id
  roleName: patient
  id: smart-patient-search-self
```

## Generate Launch URI for EHR Launch Sequence

To generate SMART App launch URI use [RPC API](../../../../../api/other/rpc-api.md) method `aidbox.smart/get-launch-uri`. The method accepts the following arguments:

* `user`: id of the `User` resource
* `iss`: Aidbox base URL
* `client`: id of the `Client` resource
* `ctx`: additional launch contextо
  * `patient`: id of `Patient` resource
  * `encounter`: id of `Encounter` resource
  * `fhirContext`: array of objects referring to any resource type other than `Patient` or `Encounter` (see [more details](https://build.fhir.org/ig/HL7/smart-app-launch/scopes-and-launch-context.html#fhir-context))

## Standalone Launch

![Standalone launch sequence](../../../../../.gitbook/assets/image%20\(101\).png)

Authorization code flow with SMART on FHIR Standalone Launch:

* App requests SMART configuration from `base-url/.well-known/smart-configuration`
* App requests FHIR conformance statement from `base-url/metadata`
* App redirects to the EHR Authorization endpoint providing extra parameters:
  * `scope`: OAuth scopes including scopes defined by SMART on FHIR
  * `aud`: FHIR Server base url
* Authorization server checks asks user to grant access to resources requested by the App and redirects to the App with code
* App exchanges code for token using the token endpoint
* App uses the token for resource access

## EHR Launch

![EHR Launch Sequence](../../../../../.gitbook/assets/image%20\(93\)%20\(1\)%20\(1\).png)

Authorization code flow with SMART on FHIR EHR Launch:

* EHR redirects to the App Launch URI (which is [generated using Aidbox RPC](set-up-smart-on-fhir-in-aidbox.md#generate-launch-uri-for-ehr-launch-sequence))
* App requests SMART configuration from `base-url/.well-known/smart-configuration`
* App requests FHIR conformance statement from `base-url/metadata`
* App redirects to the EHR Authorization endpoint providing extra parameters:
  * `scope`: OAuth scopes including scopes defined by SMART on FHIR
  * `aud`: FHIR Server base url
* Authorization server checks asks user to grant access to resources requested by the App and redirects to the App with `code`
* App exchanges `code` for `token` using the `token` endpoint
* App uses the `token` for resource access

{% hint style="info" %}
The result of the exchanging of the `code` for `token` is a JSON object containing at least:

* `access_token`. The access token issued by the authorization server
* `token_type`. Fixed value: `Bearer`
* `scope`. Scope of access authorized

More details can be found [here](https://www.hl7.org/fhir/smart-app-launch/app-launch.html#response-5)
{% endhint %}

## Inferno tests

The SMART on FHIR sample in the [Aidbox Project Samples](https://github.com/Aidbox/aidbox-project-samples) is ready to pass the Inferno ONC Program and most of the Inferno Community SMART on FHIR tests. Follow the README in the Aidbox Project Samples repository to set up Aidbox for running these tests.
