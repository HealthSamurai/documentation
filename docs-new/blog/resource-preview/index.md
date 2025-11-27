---
title: "Resource Preview"
slug: "resource-preview"
published: "2025-04-11"
author: "Artyom Bologov"
reading-time: "2 min"
tags: []
category: "System Design"
teaser: "Aidbox has a Resource Browser that allows to look over the available resources and their data. But sometimes it’s necessary to focus on just one resource and act on it. That’s where Resource Preview comes in!"
image: "cover.png"
---

## Resource Preview

[Aidbox FHIR Server](https://www.health-samurai.io/aidbox) has a Resource Browser that allows to look over the available resources and their data. But sometimes it’s necessary to focus on just one resource and act on it. That’s where Resource Preview comes in!

Resource Preview sidebar appears when you click on an individual resource in the [Resource Browser](https://www.health-samurai.io/articles/new-resource-browser) table.

One screen in this Resource Preview is the list of properties. You can collapse and expand individual properties if you need more details regarding some of them. It’s a reasonable alternative to the old JSON display and is uncluttered enough to get an understanding of a particular resource.

But resources are also editable! The Edit tab of Resource Preview has an editor that lists the data from the main Preview tab as JSON that you can edit, save, and delete. This can help you if there’s malformed data in the dataset you loaded from elsewhere or if you want to manually alter some of the resources you have.

Overall, Resource Preview is a good solution for focused resource overview and editing, with no need to exit Aidbox or resort to outside systems for data modification.
