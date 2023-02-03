# How to migrate to Apline Linux

Since version `2301`, [healthsamurai/aidboxone](https://hub.docker.com/r/healthsamurai/aidboxone) and [healthsamurai/multibox](https://hub.docker.com/r/healthsamurai/multibox) images are based on Apline Linux and run from a non-root user. User name **aidbox** uid=1000

If you use aidboxone as a base image to build your own containers you need to make the following changes

* Use [apk](https://wiki.alpinelinux.org/wiki/Alpine\_Package\_Keeper) instead of `yum`
* Use `/bin/sh` instead of `/bin/bash`

If you use volumes, you will need to change the access rights

* change zen package cash dir ownership (when volume mount)&#x20;

```bash
chown -R $APPLICATION_USER /zen/package/dir
```

* if you need to connect to the aidbox pod in k8s by root you can use this repository [https://github.com/jordanwilson230/kubectl-plugins](https://github.com/jordanwilson230/kubectl-plugins)
