---
description: >-
  babashka (bb) is a Clojure scripting tool allowing to describe complex
  configurations simply
---

# 🎓 Devbox with bb

## Pre-requirements

Your system must have these programs installed:

* docker-compose
* [babashka](https://book.babashka.org/#\_installation)

## Use Aidbox with `bb`

Clone devbox repo and switch to the `bb` branch:

```bash
git clone https://github.com/Aidbox/devbox.git
git checkout bb
```

Create a `license.edn` file with following content:

```
{:id "your license id", :key "your license key"}
```

{% hint style="info" %}
See the [Devbox guide](setup-aidbox.dev.md#get-your-license) to see how to obtain license
{% endhint %}

Start Devbox and all additional services with

```bash
bb up
```

`bb up` generates `docker-compose.yml` and does `docker compose up`

{% hint style="info" %}
Execute `bb tasks` to list all available automatizations
{% endhint %}

When `bb up` is done, Devbox url and admin credentials will be printed

```
Aidbox started - open browser at  http://localhost:8888
login with  {:id admin, :password secret}
```
