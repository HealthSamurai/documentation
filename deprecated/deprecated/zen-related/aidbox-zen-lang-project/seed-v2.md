---
description: >-
  The article explains what seed v2 service is. The service comes into Aidbox
  with version 2209.
---

# Seed v2

{% hint style="warning" %}
Since the 2405 release, using Aidbox in FHIR schema validation engine is recommended, which is incompatible with zen or Entity/Attribute options.

Please, use [Init Bundle](../../../../configuration/init-bundle.md) instead.

[Setup Aidbox with FHIR Schema validation engine](https://docs.aidbox.app/modules-1/profiling-and-validation/fhir-schema-validator/setup)
{% endhint %}

**Seed v2 service keeps your resources synced** **with Aidbox** meaning that it loads declared resources into Aidbox at startup and deletes resources from Aidbox that were declared in Seed v2 and then removed. Sync semantics dramatically distinguishes Seed v2 from [Seed Import](seed-import.md) which implements upserting semantics and expects you to make an explicit migration to delete undesirable resources from Aidbox.

{% hint style="warning" %}
Seed v2 is a stable feature, but it has a couple of known issues:

* Seed v2 allows you to define resources with the same id, but it shouldn't as it may lead to unexpected behaviour.
* For now, the behavior of Seed v2 is undefined in [Highly Available mode](../../../../deployment-and-maintenance/deploy-aidbox/run-aidbox-in-kubernetes/high-available-aidbox.md).

Please, let us know if you run into any issues with the service.
{% endhint %}

Seed v2 was designed to make a reliable bridge between [aidbox-project on zen](./) and the predecessor configuration way on meta-resources (e.g. AccessPolicy resources).

The second reason is to bring an interactive experience for meta-resources configuration which is incredibly useful during development.

Seed v2 service respects [Aidbox project reloading](../../../../reference/environment-variables/optional-environment-variables.md#aidbox_zen_dev_mode) as well. It allows you to synchronize resources without restarting Aidbox.

If you provide an invalid resource within seed v2, Aidbox won't start, reporting an error. In case of reloading Aidbox project with an invalid resource, Aidbox will report an error in the console.

{% hint style="info" %}
Seed v2 uses SeedImport custom resources to track saved resources within the service. Do not delete or modify them directly, otherwise, Aidbox won't be able to detect resources gone from Aidbox project and won't delete them.
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

## Multiple resources example

```clojure
{ns system
 import #{aidbox
          zenbox}

 box
 {:zen/tags #{aidbox/system}
  :services {:seed-policy seed-policy}}

 seed-policy
 {:zen/tags #{aidbox/service}
  :engine   aidbox/seed-v2
  :resources
  {:Client
   {:myapp
    {:secret      #env AIDBOX_AUTH_CLIENT_SECRET
     :grant_types ["password"]
     :auth
     {:password
      {:secret_required         true
       :access_token_expiration 86400}}}}
   :AccessPolicy
   {:myapp-access-policy
    {:engine "matcho"
     :matcho {:client         {:id "myapp"}
              :uri            "#/Patient/.*"
              :request-method "get"}}}}}}
```

## How to migrate from Seed import to Seed v2

Seed import and Seed v2 may co-exist together. Loading resources from file and migrations can be defined only with Seed Import. If you wish Aidbox to enable resource synchronization instead of just loading for inlined resources you may migrate those resources to Seed v2.

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

To migrate to Seed v2, it is recommended to declare a migration in Seed import which will delete all resources, you wish to move to Seed v2.

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

Seed v2 starts after Seed import, so be assured that migration will be run before Seed v2 comes into play.

### Why is it important to define delete migration?

The short answer is just to enable sync semantics for sure.

Imagine you migrated to Seed v2 without the migration. You played with it on staging and see that synchronization worked fine. Then you decide to remove `AccessPolicy/my-access-policy` or change the id, and you see that changes were applied on the staging environment. But once you deploy the solution on production you may notice that `AccessPolicy/my-access-policy` is still there. The reason is that the resource hadn't been tracked by Seed v2 on production before and the service knows nothing about it. So if you want to have sync semantics for sure, it is recommended to declare a migration in Seed import which will delete all resources, you wish to move to Seed v2.

{% hint style="warning" %}
Be aware that Aidbox runs migration only once. If you modify the applied migration, Aidbox will ignore it.
{% endhint %}
