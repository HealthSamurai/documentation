---
description: This article explains how AuditEvent Viewer works
---

# Research possible security issues

## AuditEvent Viewer

Simplifies research, diagnose and resolve possible security breaches. It is released in May v2305 Aidbox release.



<div data-full-width="false">

<figure><img src="../../../.gitbook/assets/audit-event-viewer-ui.png" alt=""><figcaption><p>AuditMessage Viewer UI</p></figcaption></figure>

</div>

## How to enable the Viewer

{% hint style="warning" %}
Aidbox produces AuditMessage resources when the features is active
{% endhint %}

### With Aidbox Configuration project

To enable the Viewer:

1. Find the file containing the `base-config` definition. It is tagged with the `aidbox.config/config` value
2. Add the definition of the feature

```clojure
features
{:zen/tags #{aidbox.config/features}
 :audit    {:enable-audit-messages true}}
```

3. Attach the `features` to the `base-config`

```clojure
 base-config
 {:zen/tags                #{aidbox.config/config}
  ...
  :features                features
  ...
  :aidbox-license          #env AIDBOX_LICENSE}
```
4. Restart Aidbox instance

### Without Aidbox Configuration project

To enable the Viewer define ENV variable `box_features_audit_enable__audit__messages` and restart Aidbox.

For example, `box_features_audit_enable__audit__messages=enabled`.

## How to get into the Viewer

To open the AuditEvent Viewer click the `Audit Events` menu item in the left sidebar of the Aidbox UI.

## W-questions

AuditMessage resources and their views are defined the way to answer the w-questions:

* what happened: event type, description and additional event details
* when: occurred date and time
* where: request origins and user-agent details
* who: user, client and sessions

## Audit event details

To see a certain AuditMessage details click it. Also there is a `raw` switcher. It allows see all the AuditMessage content in raw mode.

To filter AuditMessage resources enter query string into the search bar.

<figure><img src="../../../.gitbook/assets/audit-event-unfolded.png" alt=""><figcaption><p>Unfolded AuditMessage</p></figcaption></figure>

## Audit event types 

- `user-login`
- `user-logout` 
- `client-init-launch`
- `user-grant-access-client`
- `client-exchange-code-token`
- `client-refresh-access-token`
