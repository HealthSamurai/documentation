---
title: "How to inform Aidbox users about bad practice in their settings and prevent mistakes?"
slug: "users-bad-practice"
published: "2025-04-11"
author: "Artyom Bologov"
reading-time: "2 min "
tags: []
category: "Other"
teaser: "Our settings have a set of properties attached to them, and some of these properties exist exactly to prevent common mistakes that may lead to Aidbox crash or unwanted behavior."
image: "cover.png"
---

Our settings have a set of properties attached to them, and some of these properties exist exactly to prevent common mistakes that may lead to Aidbox [FHIR server](https://www.health-samurai.io/aidbox) crash or unwanted behavior.

First, some settings are not editable. Usually, such settings are only changeable via environment variables. By limiting the changes to environment variables, we ensure that the decision to change the non-editable setting is a conscious one.

Second, some settings only apply after restart, and Aidbox UI informs the user about that, allowing them to roll back to a previous value of the setting in case they change their mind. We’re also being transparent about Aidbox needing a restart so that you don’t have to guess whether a setting is applied or not and when.

And finally, all settings have a type. It is impossible to put a string into a number setting, or put a number into a boolean one. With types, we eliminate a whole group of mistakes with changing the setting to a value of improper format.

Having this solid basis, we can extend these properties with arbitrary checks in the future releases. With these, you have an understanding of what happens in the settings and how to use them effectively.
