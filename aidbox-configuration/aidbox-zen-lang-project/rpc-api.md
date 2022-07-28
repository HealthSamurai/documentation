# Reload Aidbox project configuration

`aidbox.zen` provides a set of operations for working with Aidbox projects.

Sometimes you want to reload zen namespaces. There is `AIDBOX_ZEN_DEV_MODE` variable to enable the hot reloading of the aidbox project.

```shell
AIDBOX_ZEN_DEV_MODE=true
```

If you don't want to enable it, but you need to reload namespaces without restarting Aidbox, you can use _the reload button_ in [Profile tab.](../zen-configuration.md)

If you don't use Aidbox UI, there's [`aidbox.zen/reload-namespaces`](../../reference/configuration/aidbox-project.md#aidbox.zen-reload-namespaces) RPC method.&#x20;

```yaml
POST /rpc

method: aidbox.zen/reload-namespaces
params: {}
```
