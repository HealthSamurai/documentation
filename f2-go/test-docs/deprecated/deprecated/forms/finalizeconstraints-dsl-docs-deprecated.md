---
hidden: true
---

# FinalizeConstraints DSL (Deprecated)

FinalizeConstraints is just like a document schema, but presumably more strict one. Internally, on Sign operation both (document and FinalizeConstraints) schemas will be used simultaneously to validate the data.

Easiest approach for making a `FinalizeConstraints` schema is to copy original document schema and remove unnecessary keys or use `aidbox.sdc/generate-form-constraints` RPC for that.

Proficient Zen user might specify only an additional subset of constraints (e.g. `require`/`minItems`) that are needed for Sign, besides of constraints already existing in Document schema.

In case `:profile` parameter will be omitted only a document schema will be used to validate data on Sign. Object example:

```
 VitalsFinalizeConstraints
 {:zen/tags #{zen/schema}
  :type zen/map
  :require #{:loinc-8310-5
             :loinc-85354-9
             :loinc-9279-1
             :loinc-39156-5
             :loinc-29463-7
             :loinc-59408-5
             :loinc-8302-2
             :loinc-8867-4}
  :keys
  {:loinc-8310-5 {:type zen/vector,
                  :minItems 1,
                  :every {:type zen/map,
                          :require #{:loinc-8310-5-value :datetime}
                          :keys {:loinc-8310-5-value {:require #{:value}
                                                      :type zen/map
                                                      :keys {:value {:type zen/number}}},
                                 :datetime {:type zen/datetime}}}},
   :loinc-85354-9 {:type zen/vector,
                   :minItems 1,
                   :every {:type zen/map,
                           :require #{:loinc-8480-6 :loinc-8462-4 :datetime}
                           :keys {:loinc-8480-6 {:type zen/map
                                                 :require #{:value}
                                                 :keys {:value {:type zen/number}}},
                                  :loinc-8462-4 {:type zen/map
                                                 :require #{:value}
                                                 :keys {:value {:type zen/number}}},
                                  :datetime {:type zen/datetime}}}},
   :loinc-9279-1 {:type zen/vector,
                  :minItems 1,
                  :every {:type zen/map,
                          :require #{:loinc-9279-1-value :datetime}
                          :keys {:loinc-9279-1-value {:type zen/map
                                                      :require #{:value}
                                                      :keys {:value {:type zen/number}}},
                                 :datetime {:type zen/datetime}}}}
   :loinc-39156-5 {:type zen/number},
   :loinc-29463-7 {:type zen/map
                   :require #{:value}
                   :keys {:value {:type zen/number}}},
   :loinc-59408-5 {:type zen/vector,
                   :minItems 1,
                   :every {:type zen/map,
                           :require #{:loin-59408-value :datetime}
                           :keys {:loinc-59408-5-value {:require #{:value}
                                                        :type zen/map
                                                        :keys {:value {:type zen/number}}
                                  :datetime {:type zen/datetime}}}}}
   :loinc-8302-2 {:require #{:value}
                  :keys {:value {:type zen/number}}
                  :type zen/map},
   :loinc-8867-4 {:type zen/vector,
                  :minItems 1,
                  :every {:type zen/map,
                          :require #{:loinc-8867-4-value :datetime}
                          :keys {:loinc-8867-4-value {:type zen/map
                                                      :require #{:value}
                                                      :keys {:value {:type zen/number}}}
                                 :datetime {:type zen/datetime}}}}}}
```

### \`\`
