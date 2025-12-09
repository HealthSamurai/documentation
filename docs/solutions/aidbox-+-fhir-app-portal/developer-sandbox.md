---
description: >-
  This tutorial will guide you through FHIR App Portal exploration of developer
  SMART application flow
---

# Developer Sandbox Portal Flow Description

## Get and deploy Growth Chart

Get Growth Chart and start it. In this guild we'll perform Test Launch against this SMART App.

```bash
git clone git@github.com:smart-on-fhir/growth-chart-app.git
cd growth-chart-app
npm install
npm start
```

## Register new developer

* open the developer sandbox portal [http://localhost:8096/](http://localhost:8096/) and click `Sign Up` button.
* submit developer registration form.
* you'll recieve confirmation email, open it and click `Confirm Email Address`
* you'll be redirected to sign up confirmation page, type in password and confirm
* you'll be redirected back to developer sandbox portal home page, click `Sign In` button
* from now on you're able to register apps

{% hint style="info" %}
Alternatively you can sign in as admin with `admin` username and `AIDBOX_ADMIN_PASSWORD` password.
{% endhint %}

## Create a SMART app in developer sandbox

* click `Register your app` and you'll be redirected to developer dashboard
* register SMART App: click on `Register New` button and fill the new app form with the following values:
  * App name: Growth Chart
  * Confidentiality: public
  * Redirect URL: [http://localhost:9000/](http://localhost:9000/)
  * Launch URL: [http://localhost:9000/launch.html](http://localhost:9000/launch.html)
* click `Create App` button
* you'll be redirected to draft page of created app, here you can: review it before submission, edit app, delete app, submit app for review or perform `Test Launch`
* for test launch of app click `Test Launch` button: this action will redirect you to launch URL you've set in settings of app with required launch context (for test launch context `fhirUser.reference=Patient/test-pt-1` is used from your Developer User)
* for submition of app click `Submit for Review` button: this action will mark app as `Under Review` and would be accessible by Admins from Admin Portal for review process

## Configure and run Growth Chart App

* after Growth Chart is registered copy its `Client ID`.
* open the file `growth-chart-app/launch.html` and fill the `client_id` property. Then save changes to the file.
* open developer sandbox portal back and click `Test Launch` button

TODO rewrite from now on:

### Approve SMART App Publishing Request

Go back to admin portal on [http://localhost:8888](http://localhost:8888). You will see list of SMART App waiting for review.

* Open the review request, made on the previous step,
* click the Approve button.

Now the smart app is available for your patients

## Enable FHIR API for tenant

### Register a tenant

In order to register a tenant you need to create Tenant resource in Aidbox.

1. Open admin portal.
2. Go to tenants page.
3. Create new tenant named My Clinic (id will be `my-clinic`).

Once you created tenant, you enabled FHIR API for patient, practitioners and bulk clients. Patient portal is related to the tenant as well. The approved smart app is available for patient in that tenant.

### Populate test data

1. Go to Aidbox REST Console. You may open it from admin portal
2.  Run the following import:

    ```yaml
    POST /$load
    Content-Type: text/yaml

    source: 'https://storage.googleapis.com/aidbox-public/smartbox/rows.ndjson.gz'
    merge:
      meta:
        tenant:
          id: my-clinic
          resourceType: Tenant
    ```

Once you saw 200 OK, Patient resource (id=test-pt-1) and corresponding resources had been uploaded into Aidbox. New we can create a User which has access to that data.

### Create User resource

In order to enroll your patient, you need to create User resource. Open Aidbox REST Console and run the following command:

```yaml
POST /User

email: example@mail.com
name:
  givenName: Amy
  familyName: Shaw
active: true
fhirUser:
  id: test-pt-1
  resourceType: Patient
roles:
- type: patient
password: password
meta:
  tenant:
    id: my-clinic
    resourceType: Tenant
```

### Sign in as a `User`

Go to My Clinic's patient portal and login as the user, created above with `example@mail.com` login and `password` password. Launch smart app and provide requested consent.

## That's it

In this tutorial we learned how to install Smartbox and to get your first SMART app approved.
