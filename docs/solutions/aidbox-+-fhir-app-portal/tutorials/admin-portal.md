---
description: >-
  Administrator guide to reviewing, approving, and managing SMART on FHIR
  applications in the Admin Portal.
---

# Admin Portal

The Admin Portal is a private console where administrators review, approve, and manage SMART on FHIR applications.

## Overview

Administrators can:

* **Review Apps** — Evaluate submitted apps and approve or decline them
* **Manage Active Apps** — Monitor apps, view feedback, check sessions, disable if needed
* **Manage Organizations** — Create and configure organizations

## Review Submitted Apps

When developers submit apps, they appear in the **Under Review** list.

### Review Checklist

Before approving an app, verify:

* **Scopes** — Are the requested scopes appropriate and safe?
* **Redirect URLs** — Are all OAuth URLs valid?
* **Required Links** — Privacy policy, terms of service, homepage
* **App Details** — Description, icon, contact information

### Approve or Decline

1. Open the app from the review list
2. Check all required information
3. Click **Approve** or **Decline**

If declining, provide a reason so the developer can fix the issues and resubmit.

## Manage Active Apps

Active apps are available to patients in the App Gallery.

### Monitoring

For each active app you can:

* **View Feedback** — See patient comments and issues
* **Check Sessions** — Review session logs and usage
* **Disable App** — Temporarily disable if there's a problem

### Disabling an App

1. Open the app details
2. Click **Disable**
3. Enter a reason

The app will be removed from the App Gallery until re-enabled.

## Manage Organizations

Organizations represent healthcare providers or tenants using the FHIR API.

### Create an Organization

1. Go to **Organizations**
2. Click **Create New**
3. Enter organization details
4. Save

Each organization gets its own FHIR endpoint and can have its own users and patients.

## App Status

| Status           | Description                           |
| ---------------- | ------------------------------------- |
| **Under Review** | Submitted, waiting for admin decision |
| **Active**       | Approved and live in App Gallery      |
| **Declined**     | Rejected with reason provided         |
| **Disabled**     | Temporarily removed from gallery      |
