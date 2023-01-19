# Technical details

Aidbox MDM is enabled with `aidbox.mdm/splink-engine` service. Only one MDM service can be enabled.

## Service symbol

An MDM service essentially is a collection of links to model symbols.

```
 patient-mdm
 {:zen/tags #{aidbox/service}
  :engine aidbox.mdm/splink-engine
  :models #{patient-mdm-model}}
```

The MDM service symbol structure:

* must be tagged with `aidbox/service` tag
* must have field `:engine` with value `aidbox.mdm/splink-engine`&#x20;
* must have field `:models` which is a set of symbols tagged with `aidbox.mdm/model` tag.

## Model symbol

Consider this example with comments

```
{:zen/tags #{aidbox.mdm/model} ; fixed value

 ;; resource-type specifies the type of resources
 ;; for which the model is built
 :resource-type "Patient" ; required, string

 ;; List of fields, declared in the :fields section of model.
 ;; MDM will take into account frequencies of values for these fields.
 :use-frequencies-for #{:first_name} ; optional, set of keywords

 ;; If we take two resources at random, how likely is the
 ;; true match of these two resources
 :random-match-prob 0.003 ; number, required

 ;; Compare a pair of resources only when it satisfyes
 ;; one of the blocking conditions.
 ;; Each element is a part of SQL query.
 :blocking-conds ["l.first_name = r.first_name"] ; vector of strings, required.

 ;; Extract fields from resources.
 ;; Each key is an arbitary keyword.
 ;; Each value is a path.
 :fields {:first_name [:name 0 :given 0]}

 ;; Specification of comparisons and scores
 ;; Each key is a keyword, declared in the :fields property
 ;; of the model.
 ;; Each value is a vector, structure of which is described below.
 :comparisons
 {:first_name
  [;; First value is always for the NULL condition.
   ;; It should have both probabilities set to 1.
   {:u-prob 1.0, :m-prob 1.0,
    :cond "\"first_name_l\" IS NULL OR \"first_name_r\" IS NULL"}
   ;; Second value is always for the ELSE conditions,
   ;; i.e. when no other conditions are met.
   {:cond :else
    :u-prob 0.99, :m-prob 0.5}
   ;; These conditions should be in order from the most strict
   ;; to the most loose.
   {:cond "first_name_l = first_name_r"
    :u-prob 0.01, :m-prob 0.5}
   {:cond "lev(first_name_l, first_name_r) <= 1"
    :m-prob 0.15, :u-prob 0.001}]}}
```

### Property `:fields`

This property defines the denormalization into MDM tables. MDM model can only use fields defined here.

Each key is the name of the field to be defined.

Each value is a path. The path should extract primitive value resource. Each extracted value will be converted to string.

#### Path syntax

A path is a vector.

_Keyword_

Keyword value specifies field name to follow. For example, path

```
[:a :b]
```

Will extract value

```
{:c {:d "e"}}
```

in object

```
{:a {:b {:c {:d "e"}}}}
```

_Integer_

Integer value specifies the index in array to follow. For example, path

```
[:a 1 :b]
```

will extract value

```
"y"
```

in object

```
{:a [{:b "x"}
     {:b "y"}
     {:b "z"}]}
```

_Map_

Map value can have only on key-value pair. It specifies Aidbox to filter vector leaving only objects, which have the specified key with the specified value. For example, path

```
[:a {:q "w"}]
```

will extract value

```
[{:q "w" :b "x"} {:q "w" :b "z"}]
```

in object

```
{:a [{:q "w", :b "x"}
     {:q "q", :b "y"}
     {:q "w", :b "z"}]}
```

### Property `:blocking-conds`

Blocking reduces number of comparisons. A pair of resources is compared only if it

Choose blocking conditions to be

* loose enough so every pair of matching resources falls into some block
* tight enough for performance to be acceptable

Blocking conditions are ORed. Essentially blocking is a fast and rough estimation of mathing resources. This estimation is refined by slower probabilistic algorithm to produce actual result.

These conditions are inserted in the WHERE part of generated queries as is.

### Property `:use-frequencies-for`

This property specifies field for which frequencies of values need to be taken into account.

For example, surname "Smith-Johnson-Williams" is much rarer than the surname "Smith", so a pair of resources having the former surname is more likely to be a match than a pair of resource having the later one.

### Property :comparisons

This property specifies comparison categories. The property is a map in which keys are field names and values are comparison definition vectors.

Each comparison is defined using a map having three key-value pairs:

* `:cond` -- SQL condition for the pair to fall into the category
* `:m-prob` -- m-probability of the comparison category
* `:u-prob` -- u-probability of the comparison category
* `:use-frequncies` -- optional boolean parameter. Enables frequency adjustment for this comparison. It is applicable only to exact equality comparison.

See [mathematical-details.md](mathematical-details.md "mention") article to learn about m- and u-probabilities.

SQL condition is inserted into WHERE part of generated queries.

Comparison definition vector is a vector with at least three comparison definitions.

1. NULL case is the case when the value in one of the resources of a pair is absent. You need to specify condition manually and set both probabilities to 1.
2. ELSE case is the case when the pair doesn't fall into any of the categories defined. You need to set condition value to be `:else` keyword.
3. All other items are comparison definitions. They should be in the order of decreasing strictness. E.g. "exact equality" comparison should be before "levenshtein distance < 2" category.

In SQL conditions field names have suffixes: `_l` suffix is added for the first record of a pair, and `_r` suffix is added for the second record of a pair.

### Property `:random-math-prob`

This property defines probability that two records picked at random will match. See [mathematical-details.md](mathematical-details.md "mention") for details.

## Auto updates of MDM tables

When an MDM model is enabled, Aidbox automatically updates the de-normalization table associated with the model. But you need to manually call the [`aidbox.mdm/update-mdm-tables` RPC](../reference/rpc-reference/aidbox/mdm/aidbox.mdm-update-mdm-tables.md) method once in a while to keep frequency data in sync.

Auto-update is triggered after any operation modifying resource. It updates one row of de-normalization table.

&#x20;
