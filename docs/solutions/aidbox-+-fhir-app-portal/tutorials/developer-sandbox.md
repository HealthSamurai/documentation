---
description: >-
  Developer guide to registering, testing, and submitting SMART on FHIR
  applications in the Developer Sandbox Portal.
---

# Developer Sandbox

The Developer Sandbox Portal is a public-facing website where SMART on FHIR app developers can register, test, and manage their applications.

## Overview

The Developer Sandbox enables:

* **Developer Registration** — Create an account to register apps
* **App Registration** — Register SMART Apps with OAuth settings
* **Sandbox Testing** — Test your app with sample patient data
* **App Submission** — Submit apps for admin review

## Register as a Developer

1. Open the Developer Portal and click **Sign Up**
2. Fill out the registration form
3. Check your email and click **Confirm Email Address**
4. Set your password on the confirmation page
5. Click **Sign In** to access your dashboard

> **Tip**: You can also sign in as admin using the `admin` username and `AIDBOX_ADMIN_PASSWORD`.

## Register a SMART App

1. From the dashboard, click **Register New**
2. Fill in the app details:
   * **App name** — Your application name
   * **Confidentiality** — Public or Confidential
   * **Redirect URL** — Where users return after authorization
   * **Launch URL** — Your app's SMART launch endpoint
3. Click **Create App**

You'll be redirected to your app's draft page where you can review, edit, or delete the app.

## Test Your App

Before submitting for review, test your app in the sandbox:

1. Copy the **Client ID** from your app's page
2. Configure your app to use this Client ID
3. Click **Test Launch** in the portal

The test launch uses sample patient data (`Patient/test-pt-1`) and redirects to your launch URL with the required SMART context.

## Submit for Review

When your app is ready:

1. Open your app's draft page
2. Click **Submit for Review**
3. Your app status changes to **Under Review**

Administrators will review your app from the Admin Portal and either approve or reject it.

## App Status

| Status           | Description                        |
| ---------------- | ---------------------------------- |
| **Draft**        | Not yet submitted                  |
| **Under Review** | Waiting for admin decision         |
| **Active**       | Approved and available to patients |
| **Rejected**     | Did not meet requirements          |

## Example: Growth Chart App

Here's how to test with the Growth Chart demo app:

### 1. Get the app

```bash
git clone git@github.com:smart-on-fhir/growth-chart-app.git
cd growth-chart-app
npm install
npm start
```

### 2. Register in the portal

Register a new app with:

* **App name**: Growth Chart
* **Confidentiality**: Public
* **Redirect URL**: `http://localhost:9000/`
* **Launch URL**: `http://localhost:9000/launch.html`

### 3. Configure and launch

1. Copy the **Client ID** from the portal
2. Open `growth-chart-app/launch.html` and set the `client_id` value
3. Save the file
4. Click **Test Launch** in the portal

The app will launch with test patient data.
