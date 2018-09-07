# Install

Devbox can be used on local machine via docker compose or deployed to kubernetes cluster via helm

You must obtain developer token in our [License server](https://license-ui.aidbox.app/)

## docker compose

See details in [https://github.com/Aidbox/devbox/blob/master/README.md](https://github.com/Aidbox/devbox/blob/master/README.md)

## Kubernetes cluster

Installation managed by [https://helm.sh](https://helm.sh/) See installation instructions [https://github.com/helm/helm\#install](https://github.com/helm/helm#install)

After installation you should add devbox repository to your local machine

```text
helm repo add aidbox https://aidbox.github.io/devbox
```

After that you are ready to install devbox. You can provide different values to release using -f option/ Example values file - [https://github.com/Aidbox/devbox/blob/master/helm/devbox/values.yaml](https://github.com/Aidbox/devbox/blob/master/helm/devbox/values.yaml)

```text
helm upgrade --namespace {{target_ns}} -i {{installation_name}} -f values.yaml aidbox/devbox
```

After that devbox can be accessed as service {{installation\_name}}-devbox or via kubectl port-forward.

