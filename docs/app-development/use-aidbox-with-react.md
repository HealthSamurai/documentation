---
description: >-
  Learn how to launch the sample app, add some sample data and query the data
  from a React app.
---

# Use Aidbox with React

## Intro

This quickstart guide demonstrates how to launch a PHR sample app on the Aidbox platform. The sample app has been developed using the official JavaScript SDK, incorporating Aidbox's best practices.

The guide helps you to explore the following Aidbox features:

* [REST Console](../overview/aidbox-ui/rest-console.md): execute and debug HTTP requests directly within Aidbox.
* [Access Control](broken-reference): manage fine-grained data access through the API.
* [Data Import endpoint](../api/bulk-api/import-and-fhir-import.md): Efficiently load FHIR data into Aidbox.
* [JavaScript SDK](aidbox-sdk/aidbox-javascript-sdk.md): Streamline JS app development on Aidbox effortlessly.

## 1. Run Aidbox and set up Access Control

Create your own Aidbox instance in [the Aidbox User Portal.](http://aidbox.app/)

Configure Access Control within the Aidbox UI using the integrated REST console (**APIs -> REST Console**). Utilize the provided code snippets to create the Client and Access Policy for the sample app.

#### **Register Client for the sample app:**

```http
POST /Client
content-type: text/yaml
accept: text/yaml

id: <client-name>
secret: <client-secret>
grant_types: ["basic"]
```

#### Add Access Policy for the sample app:

```http
POST /AccessPolicy
content-type: text/yaml
accept: text/yaml

link: [{ id: <client-name>, resourceType: Client }]
engine: allow
```

{% hint style="info" %}
By default, Aidbox has a policy that locks down access to all data. You need to enable data sharing by adding Access Policies for Clients. Aidbox supports different ways to describe these policies, like JSON Schema, SQL, or Macho DSL. [Learn more](broken-reference).
{% endhint %}

## **2. Import the sample data into Aidbox**

Populate your Aidbox instance with some synthetic data from Synthea using the Import endpoint and the handy REST console. Just run the following request to create the data:

```http
POST /fhir/$import
content-type: text/yaml
accept: text/yaml

id: synthea
contentEncoding: 'gzip'
inputs:
  - resourceType: Encounter
    url: https://storage.googleapis.com/aidbox-public/synthea/100/Encounter.ndjson.gz
  - resourceType: Organization
    url: https://storage.googleapis.com/aidbox-public/synthea/100/Organization.ndjson.gz
  - resourceType: Patient
    url: https://storage.googleapis.com/aidbox-public/synthea/100/Patient.ndjson.gz
  - resourceType: Condition
    url: https://storage.googleapis.com/aidbox-public/synthea/100/Condition.ndjson.gz
  - resourceType: Immunization
    url: https://storage.googleapis.com/aidbox-public/synthea/100/Immunization.ndjson.gz
  - resourceType: Observation
    url: https://storage.googleapis.com/aidbox-public/synthea/100/Observation.ndjson.gz
```

{% hint style="info" %}
The [**$import endpoint**](../api/bulk-api/import-and-fhir-import.md) is your go-to tool for loading FHIR data asynchronously into Aidbox. It effortlessly loads data from a public link specified in your HTTP request without performing any validation. Learn more.
{% endhint %}

## 3. Clone and Connect the sample app to Aidbox

Time to get your hands on the sample app! Simply clone it from GitHub at this link: [https://github.com/Aidbox/aidbox-sdk-js/tree/main/personal-health-record](https://github.com/Aidbox/aidbox-sdk-js/tree/main/personal-health-record)

```bash
git clone https://github.com/Aidbox/aidbox-sdk-js.git
```

Next up, let's configure the web app environment variables. Grab the **.env.tpl** file, make a copy called **.env**, and modify the following variables according to the configuration you set up in Step 1:

```bash
cd aidbox-sdk-js/personal-health-record/
```

{% code title=".env" %}
```json
VITE_APP_CLIENT=client-name - Client name
VITE_APP_SECRET=secret - Client secret
VITE_APP_AIDBOX_URL=https://your-app-name.aidbox.app/ - 
```
{% endcode %}

## 4. Start the sample app

Start the app and navigate to [http://localhost:5173](http://localhost:5173/) using your favorite browser.

```bash
npm install
npm run dev
```

**Congratulations!** You now have a fully functional application that is ready to be customized according to your needs. Explore the personal health record app sample from a technical perspective and uncover the wonders of working with Aidbox through the SDK.

## Next Steps

* Explore the [Aidbox JavaScript SDK](aidbox-sdk/aidbox-javascript-sdk.md) for advanced features.
* Unlock additional capabilities of [Aidbox UI](../overview/aidbox-ui/).
* Dive into the built-in [Access Control module](broken-reference).
* Play around with the [$import endpoint](../api/bulk-api/import-and-fhir-import.md) to load FHIR data.
