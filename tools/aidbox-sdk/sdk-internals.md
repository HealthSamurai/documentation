# SDK internals

{% hint style="warning" %}
This page is for SDK developers
{% endhint %}

You can extend aidbox with Apps. App can work with one box or many boxes. App is a REST service, which talks with [Aidbox](https://www.health-samurai.io/aidbox) through simple REST protocol. App can work with one box or many boxes. Protocol details are abstracted by SDK.

### Initialisation workflow

First of all, App should register itself in a box.

#### Single box app

For single box, app has credentials to a specific box:

1. At app start it sends **init** message to box with his endpoint url and app secret
2. Box sends **init** message to app with box coordinates \(box url, client\_id, client\_secret\), in response App upload Manifest into box

#### Aidbox app

App registered by hands in aidbox with specific client id and secret as well app secret

1. When app has started, it sends **init** message into aidbox init uri
2. Aidbox checks for which boxes this app is registered
3. For each box it calls "Single box app: phase 2".



