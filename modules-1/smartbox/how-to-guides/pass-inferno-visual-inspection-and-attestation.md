---
description: >-
  This guide explains how to pass the Inferno Visual Inspection and Attestation
  sequence
---

# Pass Inferno Visual Inspection and Attestation

{% hint style="warning" %}
Currently this guide is in progress. It is going to be updated with all the attestations
{% endhint %}

## 9.10.01 Health IT Module demonstrated support for application registration for single patients

Provided by certification buddy `Tests steps` require demonstrating the process of application registration for single patients. You should switch to the `Yes` option.

{% hint style="info" %}
Smartbox supports [several ways](../background-information/adding-clients-for-inferno-tests.md) to register SMART applications
{% endhint %}

## 9.10.02 Health IT Module demonstrated support for application registration for multiple patients

Provided by certification buddy `Tests steps` require demonstrating the process of application registration for multiple patients. You should switch to the `Yes` option.

{% hint style="info" %}
Smartbox supports [several ways](../background-information/adding-clients-for-inferno-tests.md) to register SMART applications
{% endhint %}

## 9.10.03 Health IT Module demonstrated a graphical user interface for user to authorize FHIR resources

During the test sessions, Smartbox shows users the `Consent screen`. The `Consent screen` is the graphical user interface.

## 9.10.04 Health IT Module informed patient when "offline\_access" scope is being granted during authorization

During the test sessions, Smartbox shows users the `Consent screen`. There is the `Offline access` option on the `Consent screen`.

## 9.10.09 Health IT developer demonstrated the documentation is available at a publicly accessible URL

Smartbox has a documentation page. The address of the page is `https://example.com/documentation`

{% hint style="info" %}
Here `https://example.com` is the Smartbox domain
{% endhint %}

## 9.10.10 Health IT developer confirms the Health IT module does not cache the JWK Set received via a TLS-protected URL for longer than the cache-control header received by an application indicates

It is an attestation. You should state Smartbox never caches `JWK` sets it receives during the token validations.

## 9.10.11 Health IT developer demonstrates support for the Patient Demographics Suffix USCDI v1 element

To demonstrate supporting of the `Suffix`

1. Open the result of the `1.7.02 Access to Patient resources granted` test case
2. Press the `Details` button
3. Scroll down to the `name` array of the fetched `Patient` resource
4. See the `Suffix` property inside one `name` element

## 9.10.12 Health IT developer demonstrates support for the Patient Demographics Previous Name USCDI v1 element

To demonstrate supporting of the `Previous Name`

1. Open the result of the `1.7.02 Access to Patient resources granted` test case
2. Press the `Details` button
3. Scroll down to the `name` property of the fetched `Patient` resource
4. There are two items in the `name` array
   1. First `name` has `period.end` property. It means that `name` is the `previous` one
   2. Second `name` has no `period.end` property. That `name` is `current` one

## 9.10.14 Health IT developer demonstrates the public location of its base URLs

Smartbox generates the list of the base URLs. The address of the list is `https://example.com/service-base-urls`

{% hint style="info" %}
Here `https://example.com` is the Smartbox domain
{% endhint %}

## 9.10.15 TLS version 1.2 or above must be enforced

That behavior is not related to the Smartbox (Aidbox) settings. Setting the TLS versions and enforcement to the wanted versions is out of the Smartbox setup scope
