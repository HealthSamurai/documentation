---
description: Fill out Aidbox Forms offline with local caching, auto-save, and submission when internet connection is restored.
---

# Offline forms

## Overview

Aidbox Forms supports offline functionality for filling out forms. This means you can access and fill out the form even when you are not connected to the internet. However, to take advantage of this feature, there are some important steps and conditions to keep in mind. This document will guide you through the process of using the offline functionality effectively.

## Getting Started

### Initial Setup

1. **Open the Page Online**:
   * Before you can use the form offline, you need to open [shared form](form-sharing.md) at least once while connected to the internet. This step is crucial as it allows your browser to fully cache the page and download all necessary files to your device.

### Using the Form Offline

2. **Filling Out the Form**:
   * Once the page is cached, you can open and fill out the form at any time, even if you are not connected to Wi-Fi or mobile data. All changes you make to the form will be saved locally on your device.

### Submitting the Form

3. **Internet Connection Required for Submission**:
   * While you can fill out the form offline, submitting the form requires an internet connection. If you attempt to submit the form while offline, you will see an error message: "Internet connection required".
4. **Submitting When Back Online**:
   * To successfully submit your form, ensure you are connected to the internet and then click the submit button again. Your form data will be sent to the server as intended.

## Important Precautions

### Multiple Devices

* **Changes Are Not Shared**:
  * If you fill out the form from multiple devices, be aware that changes made on one device are not shared or propagated to other devices. Each device operates independently with its local copy of the form data.
* **First Submission Only**:
  * Only the first submission of the form will be accepted. Any subsequent attempts to submit the form from another device will result in an error, as the data from the first submission will already be recorded in the system. This means that only the data from the first successfully submitted form will be considered, and any additional submissions will be rendered invalid.

## Troubleshooting

### Common Issues

* **Form Not Accessible Offline**:
  * Ensure that you have opened the page while connected to the internet at least once to allow for proper caching.
* **Error Message on Submission**:
  * If you see the "Internet connection required" message, check your internet connection and try submitting the form again once you are online.

### Tips for a Smooth Experience

* **Regular Updates**:
  * Periodically open the page while online to ensure that you have the latest version and all necessary updates.
* **Check Connectivity**:
  * Before submitting the form, verify that your device is connected to the internet to avoid any issues.
* **Single Device Usage**:
  * To avoid issues with multiple submissions, use a single device to fill out and submit the form. This will ensure that your data is accurately recorded and submitted without conflicts.

## Offline renderer integration

> Note: This example is intended as a reference implementation, not a production-ready offline engine. 
> Production apps typically require stronger persistence and security/encryption.

### Example: Offline renderer integration (reference implementation)
In addition to the offline capabilities of Aidbox Forms, our Renderer delivered as a web component (<aidbox-form-renderer>) can also operate in offline mode. This is enabled via the component’s fetch proxy mechanism, where all HTTP requests are intercepted by your custom handler (enable-fetch-proxy + onFetch).

Below is a reference example showing one practical way to implement caching, autosave, and queued synchronization after connectivity is restored.

If you want to see a detailed practical integration pattern (caching + autosave + queued sync), check the Offline Mode Example: [Aidbox Forms Renderer Offline Mode](https://github.com/Aidbox/examples/tree/main/aidbox-forms/aidbox-forms-renderer-offline-mode). 
 
### What this example demonstrates
- Rendering Aidbox Forms with the web component (`aidbox-forms-renderer`)
- A simple offline layer that caches required resources locally and keeps an operation queue while offline
- Syncing saved drafts and submit actions to Aidbox once connectivity is restored.

### Where it can be used
- This is a lightweight reference for teams building mobile/field applications (e.g., caregivers completing visit documentation) where users must continue charting without internet. 
- It can be used as a starting point for an offline subsystem in a real app
(the demo uses `localStorage`; production apps typically replace this with IndexedDB/SQLite/secure storage)

## FAQs

**Q: Can I use the form on multiple devices?** A: Yes, but changes made on one device are not shared with other devices. Only the first submission will be accepted, so it's best to use a single device to avoid conflicts.

**Q: Will my data be lost if I close the page while offline?** A: No, any changes you make while offline are saved locally on your device and will be retained until you are able to submit the form online.

**Q: How often should I open the page online?** A: It's a good practice to open the page online occasionally to ensure you have the latest updates and files.
