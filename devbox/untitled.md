# Install

helm repo add aidbox https://aidbox.github.io/devbox  


helm upgrade --namespace {{target\_ns}} -i myapp -f values.yaml aidbox/devbox

GitRepo  [https://github.com/Aidbox/devbox](https://github.com/Aidbox/devbox)

**Technical requirement**

docker , docker-compose, git, 2Gb ram

```text
git clone https://github.com/Aidbox/devbox
```

Аor access to DevBox please contact at **hello@health-samurai.io**

```text
docker login
```

restart.sh opts [https://docs.docker.com/compose/reference/up/](https://docs.docker.com/compose/reference/up/)

```text
restart.sh -d
```



