---
description: Use babashka to manage Aidbox configuration in a simple way
---

# Run Aidbox locally with Babashka

Babashka allows to manage Aidbox configuration using one [edn](https://learnxinyminutes.com/docs/edn/) file.

### Get a license

Go to the [Aidbox portal](https://aidbox.app). Sign up and click the new license button. Choose product type "Aidbox" and hosting type "on premises".

You'll see your license in the "My Licenses" list. Click on your new license and copy credentials. It is a long string like

```
eyJhbGciOiJ...
```

This string is your license key.

### Install babashka

Follow the [babashka quickstart guide](https://github.com/babashka/babashka#quickstart) to install babashka

### Install Docker and Docker Compose

Follow the [official Docker guide](https://docs.docker.com/compose/install/#install-compose) to install Docker and Docker Compose

### Clone Aidbox.Dev repository

The [Aidbox.Dev repository](https://github.com/Aidbox/devbox) contains sample Aidbox configuration which allows you to quickly run Aidbox.

Clone the official Aidbox.Dev repository

```
git clone -b bb https://github.com/Aidbox/devbox.git
```

Go to the `devbox` directory

```
cd devbox
```

### Create a license file

Create file `license.edn` with content

```clojure
{:key "<your license key>"}
```

where `<your license key>` is your license key.

### Launch Aidbox

Start Aidbox with babashka

```
bb up
```

This command generates Docker Compose file and starts Aidbox using it. This can take a few minutes.

Babashka prints Aidbox URL and admin credentials when the start is finished

```
Aidbox started - open browser at  http://localhost:8888
login with  {:id admin, :password secret}
```

### Go to the Aidbox UI

Open [http://localhost:8888](http://localhost:8888). You'll see Aidbox login page. Sign in using login `admin` and password `secret`.

Now you are ready to use Aidbox.
