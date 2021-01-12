# Patient Encounter notification Application

In this tutorial we will create simple application written on [Clojure](https://clojure.org/) programming language using [Aidbox Clojure SDK](https://github.com/Aidbox/aidbox-clojure-sdk) and Aidbox.cloud or [Aidbox.dev](https://www.health-samurai.io/aidbox) as a backend.

All source code available on [Github Aidbox/app-mailgun-clj](https://github.com/Aidbox/app-mailgun-clj)

### Problem statement

We have a Encounter included Patient, date and location of visit.

We want send Email to patient with notification of upcoming visit. In this email we want show Patient information, time and location of visit. These notifications should be sent the day before the visit.

### Architecture

Aidbox as backend which will store all Encounters. .....

Clojure application with Aidbox Clojure SDK as connector between our application and backend.

For sending Emails we will use [MailGun](https://www.mailgun.com/) service as a most easiest way to send emails instead SMPT.

![Architecture of Aidbox based Application](../.gitbook/assets/untitled-2.png)

### Get started

In the first you need install local Aidbox.dev. See full instruction how to [install Aidbox.dev](../installation/setup-aidbox.dev.md) and [how to use Rest API]()

In this sample application we use Clojure CLI. See [Getting Started](https://clojure.org/guides/getting_started) for details on how to install the tools.

### Enable Aidbox SDK

In this sample app we will use Clojure Cli - command line tools for running REPL and Clojure apps. In `deps.end` file need specify `aidbox-sdk` as reference to git.

{% code title="deps.edn" %}
```java
{:deps 
  {aidbox-sdk {:git/url "https://github.com/Aidbox/aidbox-clojure-sdk" 
               :sha "057ebd1a542bb17c7a910283ae942f56a89167f1"}}}
```
{% endcode %}

And then require `aidbox-sdk` in  the main `mailgun.core` file

{% code title="mailgun.core" %}
```java
(ns mailgun.core
  (:require [aidbox.sdk.core :as aidbox]))
```
{% endcode %}

### Connect your app with Aidbox

For connecting your application with Aidbox, you need call `aidbox/call` method send them information about location of Aidbox and your application.

{% code title="src/mailgun/core.clj" %}
```java
(def manifest
  {:id "mailgun-app"
   :type "app"
   :env {:box {;; host and port of Aidbox instance
               :host "localhost"
               :scheme "http"
               :port 8888}
         :app {;; host, port and client creds of your application
               ;;:host "docker.for.mac.localhost"  ;; for Mac Os
               :host "localhost"
               :scheme "http"
               :port 8989
               :id "root"
               :secret "secret"}}})

(defn -main [] (aidbox/start manifest))

```
{% endcode %}

#### How it works?

When you call `aidbox/start` method, `aidbox-sdk` try to connect to `Aidbox` with given `env.box.host` , `env.app.id` and `env.app.secret` credentials and then, if connection success, register your app in Aidbox. After that, `aidbox/sdk` create local web server running on `env.app.port` port, in our case is `8989`. 

### Email templating

### Sending email

### Aidbox Jobs



