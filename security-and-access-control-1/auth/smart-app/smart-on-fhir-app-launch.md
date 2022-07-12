# SMART on FHIR App Launch

SMART on FHIR specifies authentication/authorization scheme for FHIR Applications. This scheme extends OAuth 2.0 and OpenID. To enable [SMART on FHIR](https://smarthealthit.org/) you need to create an [Aidbox project](../../../aidbox-configuration/aidbox-zen-lang-project/) and configure SMART API routes using the [Aidbox API Constructor](../../../aidbox-configuration/aidbox-api-constructor.md).

{% hint style="warning" %}
Currently SMART on FHIR support is in the alpha stage.
{% endhint %}

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
* `:smart.fhir/authorization-middleware` — Provider launch    &#x20;

## Create Resources

To use SMART on FHIR you need to create a few resources like Client and AccessPolicies

### Client

Let's create an [Aidbox Client](https://docs.aidbox.app/security-and-access-control-1/overview#client):

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

You likely want to add some [Access Policies](../../security/) to allow users see their data. Here we provide some examples for reference:

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

Aidbox provides the [RPC API](../../../api-1/rpc-api.md) method `aidbox.smart/get-launch-uri` to generate launch URI. The method accepts the following arguments:

* `user`: Reference to  `User` resource
* `iss`: Aidbox base URL
* `client`: Reference to `Client` resource

This RPC method generates SMART launch URL.

## Standalone Launch

![Standalone launch sequence](<../../../.gitbook/assets/image (101).png>)

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

![EHR Launch Sequence](<../../../.gitbook/assets/image (93) (1).png>)



Authorization code flow with SMART on FHIR EHR Launch:

* EHR redirects to the App Launch URI (which is [generated using Aidbox RPC](smart-on-fhir-app-launch.md#generate-launch-uri-for-ehr-launch-sequence))
* App requests SMART configuration from `base-url/.well-known/smart-configuration`
* App requests FHIR conformance statement from `base-url/metadata`
* App redirects to the EHR Authorization endpoint providing extra parameters:
  * `scope`: OAuth scopes including scopes defined by SMART on FHIR
  * `aud`: FHIR Server base url
* Authorization server checks asks user to grant access to resources requested by the App and redirects to the App with code
* App exchanges code for token using the token endpoint
* App uses the token for resource access

## Inferno tests

The SMART on FHIR sample in the [Aidbox Project Samples](https://github.com/Aidbox/aidbox-project-samples) is ready to pass the Inferno ONC Program and most of the Inferno Community SMART on FHIR tests. Follow the README in the Aidbox Project Samples repository to set up Aidbox for running these tests.



