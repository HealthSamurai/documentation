# Patient Encounter notification Application

In this tutorial we will create simple application written on [Clojure](https://clojure.org/) programming language using [Aidbox Clojure SDK](https://github.com/Aidbox/aidbox-clojure-sdk) and Aidbox.cloud or Aidbox.dev as a backend.

### Problem statement

We have a Encounter included Patient, date and location of visit.

We want send Email to patient with notification of upcoming visit. In this email we want show Patient information, time and location of visit. These notifications should be sent the day before the visit.

### Architecture

Aidbox as backend which will store all Encounters. .....

Clojure application with Aidbox Clojure SDK as connector between our application and backend.

For sending Emails we will use [MailGun](https://www.mailgun.com/) service as a most easiest way to send emails instead SMPT.





