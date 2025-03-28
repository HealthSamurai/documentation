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

<table><thead><tr><th width="162">Aidbox version</th><th width="138">Support</th><th>Availability starts</th><th>End of life</th></tr></thead><tbody><tr><td><a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2410/images/sha256-3da5b5908521774363ff1ba9ba25860bd56a3fe8d03fa7f41ebbd02b6820aea7?context=repo">2410</a></td><td>✔</td><td>2024-10</td><td>2026-10</td></tr><tr><td><a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2402-lts/images/sha256-aee5549dd5b750dc06b867171e7c078349574b8062b7eb51e0641a86ef6dabab?context=explore">2402-lts</a></td><td>✔</td><td>2024-04</td><td>2026-04</td></tr><tr><td><a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2306-lts/images/sha256-8fb6efd524d88c954a8f49ef0340ea6253bfe8dc09e796d8fa15820ee761fe21?context=explore">2306-lts</a></td><td>✔</td><td>2023-08</td><td>2025-08</td></tr><tr><td><a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2302-lts/images/sha256-f8eef5dc5b72486fedd221a926f550e5b03a081135ab2b86e792db24fe150629?context=explore">2302-lts</a></td><td>✔</td><td>2023-04</td><td>2025-04</td></tr><tr><td><a href="https://hub.docker.com/layers/aidboxone/healthsamurai/aidboxone/2206-lts/images/sha256-33ce2578c544b427408f2fc1e7526edc75f8d1df47dcb81d92f384fdb4b6b626?context=explore">2206-lts</a></td><td>—</td><td>2022-08</td><td>2023-08</td></tr><tr><td><a href="https://hub.docker.com/layers/aidboxone/healthsamurai/aidboxone/2202-lts/images/sha256-db99626a3ef739dc76a20f75eee7bf2ca4476548c1b89a1fe8a2993d4d02cf41?context=explore">2202-lts</a></td><td>—</td><td>2022-04</td><td>2023-04</td></tr></tbody></table>

#### LTS changelog

<table><thead><tr><th width="209">Aidbox version</th><th width="139.33333333333331">Date</th><th>Reason</th></tr></thead><tbody><tr><td><a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2302-lts/images/sha256-7d9e144e2f119194050209bb4134e0618ab16ffcd06d61d6718181fb7aab998f?context=explore">2302-lts</a>, <a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2306-lts/images/sha256-feac91222ede2cc78c30ffb20c0b119b05a10a537f5e674c400ac2ee6e8c2691?context=explore">2306-lts</a>, <a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2402-lts/images/sha256-8a908d1ff95e1394368253efb1ea1c1033e45db76b3adfe7bf238814fa18b775?context=explore">2402-lts</a></td><td>2024-10-18</td><td>Fixed critical security issues</td></tr><tr><td><a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2302-lts/images/sha256-9ec3aff797509353d751199999377b8b3c3712141887d4a39be693c849392ea0?context=explore">2302-lts</a>, <a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2306-lts/images/sha256-f4ba431828ca26026e8537511bab2337d66a53004648c3d91e25a2f857d2fddc?context=explore">2306-lts</a>, <a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2402-lts/images/sha256-ed24e61dd5ed4e6ac17032405f041583931695b0dfb0a28de8369e0a3e07e6fa?context=explore">2402-lts</a></td><td>2024-10-03</td><td>Fixed critical vulnerabilities</td></tr><tr><td><a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2402-lts/images/sha256-aee5549dd5b750dc06b867171e7c078349574b8062b7eb51e0641a86ef6dabab?context=explore">2402-lts</a></td><td>2024-09-25</td><td>Fixed critical vulnerabilities</td></tr><tr><td><a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2302-lts/images/sha256-16445e8f898b1fce4a234a4afa349892cc17905d43c179855f79e9d109f87c9b?context=explore">2302-lts</a>, <a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2306-lts/images/sha256-ca0df0fbcba3e47abb0591f164cc2367679036d71807edf5f4c70b8c20ca6235?context=explore">2306-lts</a></td><td>2024-09-18</td><td>Fixed critical vulnerabilities</td></tr><tr><td><a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2306-lts/images/sha256-bde2050be4e275434e7e02fb1ae887f5370fc412d399326cf04b8f1febda8324?context=explore">2306-lts</a>, <a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2302-lts/images/sha256-16445e8f898b1fce4a234a4afa349892cc17905d43c179855f79e9d109f87c9b?context=explore">2302-lts</a></td><td>2024-09-06</td><td>Fixed critical vulnerabilities</td></tr><tr><td><a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2306-lts/images/sha256-bde2050be4e275434e7e02fb1ae887f5370fc412d399326cf04b8f1febda8324?context=explore">2306-lts</a>, <a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2302-lts/images/sha256-16445e8f898b1fce4a234a4afa349892cc17905d43c179855f79e9d109f87c9b?context=explore">2302-lts</a></td><td>2024-01-18</td><td>Fixed recursion in transaction bundles. Fixed <code>missing</code> search modifier.</td></tr><tr><td><a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2306-lts/images/sha256-bde2050be4e275434e7e02fb1ae887f5370fc412d399326cf04b8f1febda8324?context=explore">2306-lts</a>, <a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2302-lts/images/sha256-16445e8f898b1fce4a234a4afa349892cc17905d43c179855f79e9d109f87c9b?context=explore">2302-lts</a></td><td>2024-01-16</td><td>Supported JWT without kid signature check</td></tr><tr><td><a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2206-lts/images/sha256-7b7a385efea8518fb0f21bb7efb9bffcf9a0cec852474bda04887972733e1bb2?tab=layers">2206-lts</a>, <a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2302-lts/images/sha256-a8e377fb849c1cb79326e060ce45016b6d9e427d35823f66d23b7322772b10ac?context=explore">2302-lts</a></td><td>2023-11-01</td><td>Updated dependencies</td></tr><tr><td><a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2302-lts/images/sha256-3629714c3e5a177b1ea344ea58f3e47f8b1447a9cd48a51c32f640cb3624b8a1?context=explore">2302-lts</a></td><td>2023-09-11</td><td>Updated dependencies</td></tr><tr><td><a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2302-lts/images/sha256-f8eef5dc5b72486fedd221a926f550e5b03a081135ab2b86e792db24fe150629?context=explore">2302-lts</a></td><td>2023-07-12</td><td>Fixed security issue</td></tr><tr><td><a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2206-lts/images/sha256-7b7a385efea8518fb0f21bb7efb9bffcf9a0cec852474bda04887972733e1bb2?tab=layers">2206-lts</a></td><td>2023-06-29</td><td>Fixed critical vulnerabilities</td></tr><tr><td><a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2302-lts/images/sha256-f8eef5dc5b72486fedd221a926f550e5b03a081135ab2b86e792db24fe150629?context=explore">2302-lts</a></td><td>2023-06-07</td><td>Fixed security issue</td></tr><tr><td><a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2302-lts/images/sha256-a8e377fb849c1cb79326e060ce45016b6d9e427d35823f66d23b7322772b10ac?context=explore">2302-lts</a></td><td>2023-05-29</td><td>Fixed critical vulnerabilities</td></tr><tr><td><a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2302-lts/images/sha256-a8e377fb849c1cb79326e060ce45016b6d9e427d35823f66d23b7322772b10ac?context=explore">2302-lts</a></td><td>2023-04-28</td><td>Fixed security issue</td></tr><tr><td><a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2202-lts/images/sha256-8a6354d41d48ffdb00ab96a4b81354fc9fa2e7d7f388121304e58e05305f4a70?tab=layers">2202-lts</a>, <a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2206-lts/images/sha256-7b7a385efea8518fb0f21bb7efb9bffcf9a0cec852474bda04887972733e1bb2?tab=layers">2206-lts</a></td><td>2023-04-05</td><td>Fixed critical vulnerabilities</td></tr><tr><td><a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2202-lts/images/sha256-8a6354d41d48ffdb00ab96a4b81354fc9fa2e7d7f388121304e58e05305f4a70?tab=layers">2202-lts</a>, <a href="https://hub.docker.com/layers/healthsamurai/aidboxone/2206-lts/images/sha256-7b7a385efea8518fb0f21bb7efb9bffcf9a0cec852474bda04887972733e1bb2?tab=layers">2206-lts</a></td><td>2023-02-03</td><td>Fixed critical vulnerabilities</td></tr><tr><td><a href="https://hub.docker.com/layers/aidboxone/healthsamurai/aidboxone/2202-lts/images/sha256-db99626a3ef739dc76a20f75eee7bf2ca4476548c1b89a1fe8a2993d4d02cf41?context=explore">2202-lts</a>, <a href="https://hub.docker.com/layers/aidboxone/healthsamurai/aidboxone/2206-lts/images/sha256-33ce2578c544b427408f2fc1e7526edc75f8d1df47dcb81d92f384fdb4b6b626?context=explore">2206-lts</a></td><td>2022-11-03</td><td>Fixed security issues in Search API.</td></tr><tr><td><a href="https://hub.docker.com/layers/aidboxone/healthsamurai/aidboxone/2202-lts/images/sha256-db99626a3ef739dc76a20f75eee7bf2ca4476548c1b89a1fe8a2993d4d02cf41?context=explore">2202-lts</a>, <a href="https://hub.docker.com/layers/aidboxone/healthsamurai/aidboxone/2206-lts/images/sha256-33ce2578c544b427408f2fc1e7526edc75f8d1df47dcb81d92f384fdb4b6b626?context=explore">2206-lts</a></td><td>2022-10-10</td><td>Fixed critical issue on subscriptions in DB transaction that can break DB connection.</td></tr></tbody></table>
