---
description: >-
  In this tutorial you will learn how to enable Patient Access API (Smart on
  FHIR) in your Aidbox
---

# Patient Access API

Patient Access API enables Smart App to get Patient's healthcare data after Patient provided corresponding consent. Patient Access API is delivered as aidbox-project which can configured within any Aidbox instance.

In order to enable Patient Access API in your Aidbox instance you have to:

1. Set up [Aidbox](../getting-started/run-aidbox-locally-with-docker.md).
2.  Set up patient-access-smart-api sample project. You need to add next env variables and restart your Aidbox

    ```
    AIDBOX_ZEN_ENTRYPOINT=aidbox.patient-access-smart-api.sample
    AIDBOX_ZEN_PATHS=url:edn:https://raw.githubusercontent.com/Aidbox/aidbox-project-samples/main/aidbox-project-samples/smart-on-fhir/aidbox/patient-access-smart-api.edn,url:edn:https://raw.githubusercontent.com/Aidbox/aidbox-project-samples/main/aidbox-project-samples/smart-on-fhir/aidbox/patient-access-smart-api/sample.edn,url:zip:https://github.com/zen-lang/fhir/releases/download/0.2.14/hl7-fhir-r4-core.zip,url:zip:https://github.com/zen-lang/fhir/releases/download/0.2.14/hl7-fhir-us-core.zip
    ```

    {% hint style="info" %}
    aidbox-project is a configuration of Aidbox. It uses zen-lang. You can learn our single-patient-api project and a sample entrypoint. If you are not satisfied with current smart-api distribution you can copy the config and build you own one.
    {% endhint %}
3.  Create allowing access policy for "/smart" api.

    ```yaml
    id: patient-access-smart-api
    resourceType: AccessPolicy
    engine: matcho
    matcho:
      uri: '#/smart'
    ```

Now you have your Smart on FHIR server.

Patient Access API expects that you have User in your system bound with Patient resources. You can do this by specifying `fhirUser` for a User.

```yaml
resourceType: User
email: example@mail.com
password: password
fhirUser:
  id: <patient-id>
  resourceType: Patient
```

## Inferno testing

Here you can find steps to pass inferno tests.

1.  At first, you have to create Client for inferno

    ```yaml
    # Run this request in REST Console

    PUT /Client/inferno-client

    id: inferno-client
    resourceType: Client
    secret: inferno-secret
    auth:
      authorization_code:
        redirect_uri: https://inferno.healthit.gov/inferno/oauth2/static/redirect
        refresh_token: true
        secret_required: true
        access_token_expiration: 36000
    grant_types:
      - authorization_code
      - basic
    ```
2.  Upload synthetic data

    ```bash
    # Run this request in REST Console

    POST /fhir/$load

    source: https://storage.googleapis.com/aidbox-public/inferno/inferno-community-fixtures.ndjson.gz
    ```
3. You should have a user bound with Patient.
4.  You need to expose your localhost Aidbox to the public. You can use [ngrok](https://ngrok.com/) for instance.

    ```
    ngrok http 8888
    ```

    You will see something like this:

    <img src="../.gitbook/assets/image (100).png" alt="" data-size="original">

    Then copy forwading https connection and update base url env. In my case it's `AIDBOX_BASE_URL=https://4eb9-178-66-82-76.ngrok.io`

Now you are ready to run Inferno test.

{% hint style="info" %}
Pay attention that FHIR base url for Smart on FHIR is \[hostname]/smart
{% endhint %}

{% hint style="info" %}
After running Standalone Patient App test you should open \[host]/auth/grants and revoke the provided access for Inferno client
{% endhint %}

![Inferno starting page](<../.gitbook/assets/Screenshot 2021-10-18 at 12.24.59.png>)

![Standalone Patient App test](<../.gitbook/assets/Screenshot 2021-10-18 at 12.22.20.png>)

![Limited App test](<../.gitbook/assets/Screenshot 2021-10-18 at 12.22.06.png>)

{% hint style="info" %}
Other Inferno tests are related to Provider Access API. So, it's out of scope of Patient Access API which we are considering in current tutorial.
{% endhint %}
