---
title: "A quick guide how to enable 2FA for Aidbox-based solutions."
slug: "2fa-in-your-healthcare-application"
published: "2021-04-28"
author: "Anastasia Demina"
reading-time: "2 min read"
tags: ["aidbox", "fhir", "2FA"]
category: "System Design"
teaser: "Two-factor authentication (2FA) is a subset of multi-factor authentication (MFA) and an important security measure that adds a second layer of protection to your healthcare app. Read our guide and enable 2FA for Aidbox-based solutions."
image: "cover.png"
---

Security and data breaches are a major concern for every organization. Companies in the healthcare sector, however, are especially vulnerable: they are a top target for cybercriminals because the black market value of medical data is exceptionally high. When it comes to the financial impact of data breaches, healthcare suffers greater losses than any other industry. To protect companies from the dire consequences of data breaches, security specialists have developed a number of innovative technologies that make identity theft increasingly difficult, if not impossible. The most effective of these solutions is multi-factor authentication.

Two-factor authentication (2FA) is a subset of multi-factor authentication (MFA) and an important security measure that adds a second layer of protection in addition to the username and password combination.  

### 2FA with TOTP: Architecture overview

The most popular form of two-factor authentication uses a software-generated Time-based One-time Passcode (also known as a [TOTP](https://en.wikipedia.org/wiki/Time-based_One-time_Password_Algorithm)).   
  
First, users [download and install](https://authy.com/download/) a free 2FA app on their smartphone or desktop. They can then use the app with any site that supports this type of authentication. When signing in, the user first enters a username and password and then, when prompted, they enter the code shown on the app.  

![](image-1.png)

###  Enable 2FA for Aidbox-based solutions

Since [Aidbox](https://www.health-samurai.io/new-aidbox) is widely used for custom development of healthcare applications, it’s often the case that when you don’t follow the default sign up or login process, the custom frontend application uses business-specific user flow for sign up and login instead.

When adding 2FA feature support into Aidbox, we take care over your custom business logic, and you will still have full control of it inside your application. It doesn’t affect the flexibility you already have. Aidbox only validates the generated TOTP token when 2FA is enabled for a particular user.

You can try it out with DevBox using our [detailed guide complete with demo application](https://en.wikipedia.org/wiki/Time-based_One-time_Password_Algorithm).

### More questions about 2FA?

Have any feedback or suggestions after trying [Aidbox](https://www.health-samurai.io/new-aidbox) 2FA out? Join the discussion with the [Aidbox community](https://t.me/aidbox)!

Follow US

> Get started with the Aidbox [FHIR Server](https://www.health-samurai.io/aidbox) for data storage, integrations, healthcare analytics, and more, or [hire our team](https://www.health-samurai.io/services) to support your software development needs.

[![Aidbox FHIR Platform Free Trial](image-2.png)](https://www.health-samurai.io/aidbox)
