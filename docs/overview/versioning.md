---
description: >-
  This page introduces the release cycle and versioning approach used in Aidbox
  development
---

# Versioning

Aidbox docker images are available on [Docker Hub](https://hub.docker.com/u/healthsamurai).

## Version numbering

Starting from October 2024 Aidbox follows a MAJOR.MINOR version scheme:

* MAJOR version: Formatted as YYMM (e.g., 2409 for September 2024)
* MINOR version: An increasing number starting from zero

Each update increments the MINOR version. For example:

* New release: 2409.0
* Bugfix: 2409.1
* Bugfix 2: 2409.2
* New release: 2410.0
* Bugfix for 2409: 2409.3
* Bugfix for 2410: 2410.1

Images built for full versions (MAJOR.MINOR) remain unchanged. You can specify the full version instead of the Docker manifest SHA to use a specific build.

### Release Channels

Aidbox provides several release channels, each updated with changes and new builds:

### Edge

The most recent Aidbox build. We highly recommend using the edge channel in staging and dev environments to get access to new functionality and detect potential issues as soon as possible. Every commit into the Aidbox code base after successful CI is published to the edge channel. The edge channel may have issues and/or regressions.

### Latest

The **latest** channel is identical to the last minor version of the last release.

**Latest** releases contain new functionality, bugfixes, and optimizations and passed all available QA and review processes by the Health Samurai team. If you're actively developing we recommend using **latest**. These releases are updated monthly.

### Stable

The **stable** channel is identical to the last minor version of the previous release.

**Stable** releases are ready for production. The current **stable** is the previous **latest** release. These releases are updated monthly.

### MAJOR-rc

This channel contains the last changes to the MAJOR release, which are undergoing internal QA processes. After these processes are finished, a new full version is released.

### MAJOR

For every release, a tag in the format YYMM (e.g., 2409 for September 2024) is created to allow users to check out a specific Aidbox version. This tag is automatically updated with each minor version and corresponds to the last minor version for the major version.

By using these channels and understanding the version numbering system, you can choose the most appropriate Aidbox build for your development, staging, and production environments.

### Long-term support releases

Starting in January 2022 we are introducing long-term support releases. The Aidbox team will backport security and critical bug fixes to LTS releases throughout a two-year support window.

Aidbox LTS releases are scheduled to be published twice a year. If you are not interested in getting new features on a monthly basis but want to get critical bug fixes, please consider switching to LTS releases.

<table><thead><tr><th width="162">Aidbox version</th><th width="138">Support</th><th>Availability starts</th><th>End of life</th></tr></thead><tbody><tr><td><a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2507/images/sha256-04f563a36f7e87796b616de64167d27681078b12a4fcc74667f3cb69ca30f1d3">2512</a></td><td>✔</td><td>2025-12</td><td>2027-12</td></tr><tr><td><a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2507/images/sha256-04f563a36f7e87796b616de64167d27681078b12a4fcc74667f3cb69ca30f1d3">2507</a></td><td>✔</td><td>2025-07</td><td>2027-07</td></tr><tr><td><a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2410/images/sha256-3da5b5908521774363ff1ba9ba25860bd56a3fe8d03fa7f41ebbd02b6820aea7?context=repo">2410</a></td><td>✔</td><td>2024-10</td><td>2026-10</td></tr><tr><td><a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2402-lts/images/sha256-aee5549dd5b750dc06b867171e7c078349574b8062b7eb51e0641a86ef6dabab?context=explore">2402-lts</a></td><td>✔</td><td>2024-04</td><td>2026-04</td></tr><tr><td><a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2306-lts/images/sha256-8fb6efd524d88c954a8f49ef0340ea6253bfe8dc09e796d8fa15820ee761fe21?context=explore">2306-lts</a></td><td>—</td><td>2023-08</td><td>2025-08</td></tr><tr><td><a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2302-lts/images/sha256-f8eef5dc5b72486fedd221a926f550e5b03a081135ab2b86e792db24fe150629?context=explore">2302-lts</a></td><td>—</td><td>2023-04</td><td>2025-04</td></tr><tr><td><a href="https://hub.docker.com/layers/aidboxone/healthsamurai/aidboxone/2206-lts/images/sha256-33ce2578c544b427408f2fc1e7526edc75f8d1df47dcb81d92f384fdb4b6b626?context=explore">2206-lts</a></td><td>—</td><td>2022-08</td><td>2023-08</td></tr><tr><td><a href="https://hub.docker.com/layers/aidboxone/healthsamurai/aidboxone/2202-lts/images/sha256-db99626a3ef739dc76a20f75eee7bf2ca4476548c1b89a1fe8a2993d4d02cf41?context=explore">2202-lts</a></td><td>—</td><td>2022-04</td><td>2023-04</td></tr></tbody></table>

