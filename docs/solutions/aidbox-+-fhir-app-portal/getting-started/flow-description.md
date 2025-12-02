---
description: >-
  This tutorial will guide you through FHIR App Portal exploration of
  developer, admin and patient SMART application flows
---

TODO add to each header link like this: ### Aidbox license<a href="#license" id="license"></a>
# Flow Description

## Admin portal

Open the admin portal [http://localhost:8888/](http://localhost:8888/) and login using credentials from the .env file `AIDBOX_ADMIN_ID` and `AIDBOX_ADMIN_PASSWORD`.

On the admin portal you can manage apps, patients and other admins.

### Register developer

Submit developer registration form

* Open Developer Sandbox on [http://localhost:9999](http://localhost:9999)
* Click the Sign Up button
* Register a new developer

Once you submitted the developer registration form, you should receive an email with the verification link.

* Follow the link to confirm your email address.
* You will be redirected on creation password form
* Create a password, submit it.

Now you can Sign In as developer to the Developer Sandbox.

## Create a SMART app in developer sandbox

### Get and deploy Growth Chart

To get and the Growth Chart downloaded and start it

```bash
git clone git@github.com:smart-on-fhir/growth-chart-app.git
cd growth-chart-app
npm install
npm start
```

Register a SMART App

Once you launched the Growth Chart app, you can register it in the Sandbox.

* Click the Create app button
* Populate the form:
  * App name: Growth Chart
  * Confidentiality: public
  * Redirect URL: [http://localhost:9000/](http://localhost:9000/)
  * Launch URL: [http://localhost:9000/launch.html](http://localhost:9000/launch.html)
* Submit the new app form
* Click the Submit for Review button to send the application to review

After Growth Chart is registered copy its `Client ID`.

### Update Growth Chart `client_id`

Open the file `growth-chart-app/launch.html` and fill the `client_id` property. Then save changes to the file.

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
