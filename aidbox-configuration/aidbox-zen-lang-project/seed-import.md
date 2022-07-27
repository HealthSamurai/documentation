# Seed Import

You can declare a set of resources in Aidbox project and get them loaded in one or many Aidboxes on start. To do this you need to describe the seed service in the system entrypoint.

### Example

```clojure
{ns     importbox
 import #{aidbox
          zenbox}

 seed
 {:zen/tags  #{aidbox/service}
  :engine    aidbox/seed
  :files     ["patients.ndjson.gz"]
  :resources [{:id "rpt-1" :resourceType "Patient"}
              {:id "rpt-2" :resourceType "Patient"}]
  :migrations [{:id "mig-1" :sql "create table mytable (id text)"}
               {:id "mig-2" :sql "insert into mytable values ('hello')"}]}

 importbox
 {:zen/tags #{aidbox/system}
  :zen/desc "Import box for test"
  :services {:seed seed}}}
```

In this example `importbox/importbox` is the system entrypoint defined in the **`AIDBOX_ZEN_ENTRYPOINT`** variable, the files described in the `:files` field are located inside the zen project defined in the variable **`AIDBOX_ZEN_PATHS`**.

### Seed Service

`:files` - which `ndjson.gz` files will be imported at system startup. These files must be located inside the zen project described in the variable **`AIDBOX_ZEN_PATHS`**.

`:resources` - in-place resources definitions that will be imported at system startup.

`:migrations` — vector of migrations. Each migration is a map containing `id` and `sql` keys.

{% hint style="warning" %}
``
{% endhint %}
