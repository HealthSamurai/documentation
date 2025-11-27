---
title: "How do we know where settings come from and how to prioritize them?"
slug: "prioritize-settings"
published: "2025-04-11"
author: "Artyom Bologov"
reading-time: "2 min "
tags: []
category: "FHIR"
teaser: "Source is an intuitive way to think about settings. They come from somewhere, be it a single place or many places."
image: "cover.png"
---

Source is an intuitive way to think about settings. They come from somewhere, be it a single place or many places. Environment variables, database contents, server-specific configuration—all of these are settings sources. We specifically added a concept of sources to Aidbox to handle these ideas.

Sources store and fetch values in their own specific ways. Environment variables are read-only, for example. And the database is easy to modify at any moment, ensuring settings apply right when you change them. With this source-specific behavior we can be sure that the system is both reliable (it’s impossible to break something fundamental) and flexible (no need to restart Aidbox every time you change a setting.)

To handle the variety of sources we have, these sources have priorities. Environment variables defined on Aidbox [FHIR server startup](https://www.health-samurai.io/aidbox) override settings stored in the database or set in legacy Zen config. We consider this order intuitive enough: if the user wants to override some setting, they have an easy way to do so, like providing environment variables.

Settings might come from multiple sources and have different priorities. Source priorities handle the clashes, and our Settings screen shows where these settings are coming from. With that, we maintain a transparent system that works in an intuitive way, showing you the sources for changed settings and allowing you to set and override them.
