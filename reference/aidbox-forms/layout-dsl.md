# Layout DSL

{% hint style="danger" %}
Layout DSL is still under development and unstable
{% endhint %}

Layout DSL can be changed with layout-engine.
Default layout-engine described here - `aidbox.sdc/Hiccup`.

There are three types of layout nodes:

* [Container node](layout-dsl.md#container-node)
* [Input node](layout-dsl.md#input-node)
* [Subform](layout-dsl.md#subforms)

You can also set [Layout rules](layout-dsl.md#layout-rules) or [Customize node view](layout-dsl.md#customize-node-view).

## Container node

Container node is a node that contains several children elements.

Typical description of node:

```
{:type aidbox.sdc/fields
 :children [{...} {...}]
```

* children - an array of child elements

## Input node

Minimal example of input node:

```
{:bind [:blood-pressure]}
```

* bind - binding to SDCDocument field

### Input node type

By default Aidbox FormLayouts supports five types of inputs:

* text input
* number input
* choice
* quantity
* calculated field

Aidbox Forms has input-node inference logic based on special properties of the field schemas. 
But you also have ability to force some input types by specifying sdc-type in SDCDocument field


#### Text input

To define text input just define your field as `zen/string`:

```
SuperDocument
{...
 :type zen/map
 :keys {:field {:type zen/string}}
 ...
 }
```

And bind some node in layout to this field:

```
SuperDocumentLayout
{...
 :type zen/map
 :engine aidbox.sdc/Hiccup              ;; <-- default layout engine.
 :layout {:type aidbox.sdc/fields       ;; layout definition
          :children [{:bind [:field]}]}
 ...
 }
```

#### Number input

Same as text input but use `zen/number` in SDCDocument definition:

```
SuperDocument
{...
 :type zen/map
 :keys {:field {:type zen/number}}
 ...
 }

SuperDocumentLayout
{...
 :type zen/map
 :layout {:type aidbox.sdc/fields
          :children [{:bind [:field]}]}
 ...}
```

#### Choice & quantity input

Since choice and quantity types are usually represented as `zen/map` in SDCDocument we should help input-type inference engine to select correect input-type 
by specifing additional properties of node or specify input-type directly by special key: `sdc-type` in SDCDocument definition:

Example:

```
SuperDocument
{...
 :type zen/map
 :keys {
        ;; use direct input-style set
        :choice-field {:type zen/map
                       :confirms #{aidbox.sdc.fhir/coding}
                       :sdc-type aidbox.sdc/choice                     ;; <---- special type
                       :enum [{:value {:code "Option 1"}}              ;; in enum we declaring available options for select
                              {:value {:code "Option 2}}]}

        :body-temperature {:type zen/map
                           :keys {:value {:type zen/number}
                                  :unit {:type zen/string}}
                           :sdc-type aidbox.sdc/quantity               ;; <--- use special type
                           :units [{:name "kg"}                        ;; available units for this field
                                   {:name "lb"}]}

        ;; use inference

        :choice-field-inferred-1 {:type zen/map
                                  :enum [{:value {:code "Option 1"}}         ;; <--- declare :enum property (helps inference)
                                         {:value {:code "Option 2}}]}

        :choice-field-inferred-2 {:type zen/map
                                  :sdc/option :aidbox.sdc.options/valueset   ;; <--- declare :sdc/options (helps inference)
                                  :valueset "my-valueset-id"}
    
        :quantity-inferred-1 {:confirms #{aidbox.sdc.fhir/quantity}}         ;; <--- declare :confirms with aidbox.sdc.fhir/quantity (helps inference)

        :quantity-inferred-2 {:confirms #{aidbox.sdc.fhir/quantity}
                              :units [{:name "kg"}                           ;; <--- declare available units for this field (helps inference)
                                      {:name "lb"}]}
                                   
                                   
 ...
 }

SuperDocumentLayout
{...
 :type zen/map
 :layout {:type aidbox.sdc/fields
          :children [{:bind [:choice-field]}
                     {:bind [:quantity-field]}]}
 ...}
```

## Subforms

If you need to to group input fields or handle multiple readings of some field, you can use subforms.

```
{:type aidbox.sdc/subform
 :bind [:blood-pressure]
 :form-layout {:type aidbox.sdc/fields
               :children [{..} {..}]}
```

For example if need to store blood pressure in a map with two keys: _systolic_, _diastolic_ you can declare your document in such way:

```
BloodPressureDocument
{:zen/tags #{zen/schema aidbox.sdc/doc}
 :type zen/schema
 :type zen/map
 :confirms #{aidbox.sdc/Document}
 :keys {:blood-pressure {:type zen/map
                         :keys {:systolic {:type zen/number}
                                :diastolic {:type zen/number}}}}}
```

Then in form definition we can use subforms with their relative paths. We state that subforms binds to blood pressure map. Then all child elements in this subforms will be binded to fields in this `blood-pressure` map

```
BloodPressureLayout
{:zen/tags #{aidbox.sdc/Layout}
 :document BloodPresureDocument
 :layout {:type aidbox.sdc/subform
          :bind [:blood-pressure]
          :layout {:type aidbox.sdc/fields
                   :children [{:bind [:systolic]} ;; full path will be [:blood-pressure :systolic]
                              {:bind [:diastolic]}]}}}
```

### Collection subforms

If you have vector fields in your Document, you might need to use collection subforms to capture values.

Just declare your field as `zen/vector` in the document:

```
:blood-pressure {:type zen/vector
                 :every {:type zen/map
                         :keys {:systolic {:type zen/number}
                                :diastolic {:type zen/number}}}}
```

And in subform definition set `collection=true`

```
{:type aidbox.sdc/subform
 :bind [:blood-pressure]
 :collection true
 :layout {:type aidbox.sdc/fields
          :children [{:bind [:systolic]} ;; full path will be [:blood-pressure {nth} :systolic]
                     {:bind [:diastolic]}]}}
```

## Layout rules

For each node we can use two types of rules:

* `:sdc/disable-when` - disables node when calculating rule returns true (any non-nil value counts as true value)
* `:sdc/display-when` - displays node if calculating result returns true

Usage:

```
{:bind [:blood-pressure :position]
 :sdc/display-when (get-in [:blood-pressure :systolic])} ;; field will show only if :systolic field is filled
```

## Customize node view

If you want to show something different than usual select component, you can customize visual view of node using `:control` field.

```
{:bind [:loinc-80884-0] :control ScoreSelector}
```

Your DSL interperter should understand this `:control` field\


\


\
\
\
