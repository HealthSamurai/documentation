---
description: This page explains what log message transformations are available in Aidbox
---

# Log transformations

## What is log transformation? <a href="#what-is-log-transformation" id="what-is-log-transformation"></a>

Transformation is the tool used to modify and/or filter log messages on the fly according to the rules defined in the Aidbox configuration.

### How transformation can be activated <a href="#how-transformation-can-be-activated" id="how-transformation-can-be-activated"></a>

To active transformations on an appender define the transformation and add it to the appender. In the example below there are two transformation applied to the `:qb/q` log messages and `aidbox.log/ignore` transformation applied to the `:w/resp` messages.

```clojure
{:ns     main
 :import #{aidbox
           aidbox.log
           config}

 db-q-transform
 {:zen/tags #{aidbox.log/transform}
  :transforms
   [{:filter {:path [:d] :op :min :value 100}}
    {:trim {:path [:sql] :length 50}}]}

 stdout-appender
 {:zen/tags #{aidbox/service}
  :engine   aidbox.log/stdout-appender
  :transforms {:qb/q db-q-transform
               :w/resp aidbox.log/ignore}}

 box
 {:zen/tags #{aidbox/system}
  :config   config/zen-config
  :services {:seed            config/admin-seed
             :stdout-appender stdout-appender}}}
```

## aidbox.log/ignore

To ignore the all the messages related to a certain type use built-in `aidbox.log/ignore` transformation. In the example below all the `:w/resp` (http response) messages are skipped.

```
 stdout-appender
 {:zen/tags #{aidbox/service}
  :engine   aidbox.log/stdout-appender
  :transforms {:w/resp aidbox.log/ignore}}
```

## :filter

Decides whether to omit a log message. It compares the threshold value with the specified path log value.

```clojure
 only-slow-sql-queries-allowed
 {:zen/tags #{aidbox.log/transform}
  :transforms
   [{:filter {:path [:d] :op :min :value 100}}]}

 stdout-appender
 {:zen/tags #{aidbox/service}
  :engine   aidbox.log/stdout-appender
  :transforms {:qb/q only-slow-sql-queries-allowed}}
```

This example transformation skips log messages where the `d` property in a message is lower than 100.

### **Parameters**

* `:op` defines the operation:
  * `:min` skips a message when threshold is lower than specified path value
  * `:max` skips a message when threshold is greater than specified path value
* `:value` is the threshold
* `:path` specifies the path in the message where the desired value is held

## :trim <a href="#trim" id="trim"></a>

Overwrites the given path value with the sub-string function.

```clojure
 sql-shorter-than-50-chars
 {:zen/tags #{aidbox.log/transform}
  :transforms
   [{:trim {:path [:sql] :length 50}}]}

 stdout-appender
 {:zen/tags #{aidbox/service}
  :engine   aidbox.log/stdout-appender
  :transforms {:qb/q sql-shorter-than-50-chars}}
```

This example transformation reduces the length of the `:sql` string if it is longer than 50 symbols.

### Parameters

* `:length` defines the maximum allowed string length
* `:path` specifies where the desired string should be taken from

## :dissoc

Removes the given keys set from the root of the log message.

```
 no-sql-params
 {:zen/tags #{aidbox.log/transform}
  :transforms
   [{:dissoc [:db_prm]}]}

 stdout-appender
 {:zen/tags #{aidbox/service}
  :engine   aidbox.log/stdout-appender
  :transforms {:qb/q no-sql-params}}
```

This transformation remove the key `:db_prm` and its values from the log message.

### Parameters

`:dissoc` itself is an array of prohibited root keys.

## :select-keys

Takes defined keys from the root of the log message.

```
 whitelisted-sql-keys
 {:zen/tags #{aidbox.log/transform}
  :transforms
   [{:select-keys [:ev :op :ts :sql :d]}]}

 stdout-appender
 {:zen/tags #{aidbox/service}
  :engine   aidbox.log/stdout-appender
  :transforms {:qb/q whitelisted-sql-keys}}
```

This transformation omits all the keys in a log messaged except: `:ev`, `:op`, `:sql` and `:d`.

### Parameters

`:select-keys` itself is an array of whitelisted root keys.
