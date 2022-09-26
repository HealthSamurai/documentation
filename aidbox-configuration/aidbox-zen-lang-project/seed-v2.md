---
description: >-
  The article explains what seed v2 service is. The service comes into Aidbox
  with version 2209.
---

# Seed v2

**Seed v2 service keeps your resources synced** **with Aidbox** meaning that it loads declared resources into Aidbox at startup and deletes resources from Aidbox that were declared in Seed v2 and then removed. Sync semantics dramatically distinguishes Seed v2 from [Seed Import](seed-import.md) which implements upserting semantic and expects you make an explicit migration to delete undesirable resources from Aidbox.

Seed v2 was designed to make a reliable bridge between [aidbox-project on zen](./) and predecessor configuration way on meta-resources (e.g. AccessPolicy resources).&#x20;

The second reason is to bring interactive experience for meta-resources configuration which is incredibly useful while development.

Seed v2 service respects [Aidbox project reloading](rpc-api.md) as well. It allows you to synchronise resources without restarting Aidbox.

If you provide invalid resource within seed v2, Aidbox won't start, reporting an error. In case of reloading Aidbox project with an invalid resource, Aidbox will report an error in console.

{% hint style="info" %}
Seed v2 uses SeedImport custom resources to track saved resources within the service. Do not delete or modify them directly, otherwise Aidbox won't be able to detect resources gone from Aidbox project and won't delete them.
{% endhint %}

## Example

```clojure
{ns     importbox
 import #{aidbox
          zenbox}
 box
 {:zen/tags #{aidbox/system}
  :services {:my-resources my-seed}}

 my-seed
 {:zen/tags  #{aidbox/service}
  :engine    aidbox/seed-v2
  :resources
  {:AccessPolicy 
   {:my-access-policy
    {:engine "matcho" 
     :matcho {:uri "/public-url"}}}}}}
```

## How to migrate from Seed import to Seed v2

Seed import and Seed v2 may co-exist together. Loading resources from file and migrations can be defined only with Seed Import. If you wish Aidbox to enable resource synchronisation instead of just loading for inlined resources you may migrate that resources to Seed v2.

Let's say you have `AccessPolicy/my-policy-policy` declared within Seed import.

```clojure
{ns     importbox
 import #{aidbox
          zenbox}
 box
 {:zen/tags #{aidbox/system}
  :services {:my-seed my-seed}}

 my-seed
 {:zen/tags  #{aidbox/service}
  :engine    aidbox/seed
  resources [{:id "my-access-policy"
              :resourceType "AccessPolicy"
              :engine "allow"}]}}
```

In order to migrate to Seed v2, it is recommended to declare a migration in Seed import which will delete all resources, you wish to move to Seed v2.

```clojure
{ns     importbox
 import #{aidbox
          zenbox}
 box
 {:zen/tags #{aidbox/system}
  :services {:my-seed-v2 my-seed-v2
             :delete-resources-from-seed-import delete-resources-from-seed-import}}

 my-seed-v2
 {:zen/tags  #{aidbox/service}
  :engine    aidbox/seed-v2
  resources {:AccessPolicy {:my-access-policy {:engine "allow"}}}}

 delete-resources-from-seed-import
 {:zen/tags  #{aidbox/service}
  :engine    aidbox/seed
  :migrations [{:id "delete-resources-from-seed-import"
                :sql "delete from accesspolicy where id in ('my-access-plicy')"}]}}
```

Seed v2 starts after Seed import, so be assured that migration will be run before Seed v2 comes to play.

### Why is it important to define delete migration?&#x20;

Short answer is just to enable sync semantics for sure.&#x20;

Imagine you migrated to Seed v2 without the migration. You played with it on staging and see that synchronisation worked fine. Then you decide to remove `AccessPolicy/my-access-policy` or change the id, and you see that changes was applied on staging environment. But once you deployed the solution on production you may notice that `AccessPolicy/my-access-policy` is still there. The reason is that the resource hadn't been tracked by Seed v2 on production before and the service knows nothing about it. So if you want to have sync semantic for sure, it is recommended to declare a migration in Seed import which will delete all resources, you wish to move to Seed v2.

{% hint style="warning" %}
Be aware that Aidbox runs migration only once. If you modify applied migration, Aidbox will ignore it.
{% endhint %}
