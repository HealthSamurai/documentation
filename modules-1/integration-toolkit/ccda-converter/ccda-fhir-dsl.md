---
description: This page describes how to customize the conversion results.
---

# How to customize conversion rules

The converter is a rule-based service so it allows users to define conversion rules using a special Domain-Specific Language (DSL), enabling customization and mapping of data elements between C-CDA and FHIR standards.

{% hint style="warning" %}
This page is currently under construction.
{% endhint %}

{% hint style="info" %}
This part of functionality requires knowledge of zen language.
{% endhint %}

## Setup a service with the service configuration

Here is basic information on configuring zen-project to enable the converter functionality.

[More details about zen-configuration](https://docs.aidbox.app/aidbox-configuration/aidbox-zen-lang-project/aidbox-configuration-project-structure).

## Default rules

Current conversion rules are stored in `.edn` files in the zen project.

The `core.edn` file describes a top-level configuration of the service:

```
default-config
 {:docdefs
  {:discharge-summary aidbox.ccda.docdefs/discharge-summary}

  :section-aliases
  {:allergies  ["AllergiesandIntolerancesSectioner"
                "AllergiesandIntolerancesSectioneo"]

   :encounters ["EncountersSectionentriesrequiredV3"
                "EncountersSectionentriesoptionalV3"]

    ...
  }

  :section-rules
  {"VitalSignsSectionentriesrequired"    {:entries aidbox.ccda.rules.vital-signs/rules
                                          :narrative aidbox.ccda.rules.vital-signs/narrative}
   "VitalSignsSectionentriesoptional"    {:entries aidbox.ccda.rules.vital-signs/rules
                                          :narrative aidbox.ccda.rules.vital-signs/narrative}
   "GoalsSection"                        {:entries aidbox.ccda.rules.goals/rules}
   "EncountersSectionentriesrequiredV3"  {:entries aidbox.ccda.rules.encounters/rules}
   "EncountersSectionentriesoptionalV3"  {:entries aidbox.ccda.rules.encounters/rules}

    ...

    }}
```

The `:docdefs` key defines document templates that will be explaned further.

The `:section-aliases` key defines the aliases for sections.

The `:section-rules` key defines the default conversion rules.

Each key inside `:section-rules` references mappings for the section entries and the narrative part of C-CDA document.

For example, this entry

```
  {"VitalSignsSectionentriesrequired"    {:entries aidbox.ccda.rules.vital-signs/rules
                                          :narrative aidbox.ccda.rules.vital-signs/narrative}
```

is related to Vital signs section mapping.

The reference `:entries aidbox.ccda.rules.vital-signs/rules` points to the file with corresponding mappings:

```
{ns aidbox.ccda.rules.vital-signs
 import #{aidbox.ccda.rules.shared.observation
          aidbox.ccda.rules.shared.narrative-fns
          aidbox.ccda.core}

 narrative
 {:zen/tags #{aidbox.ccda.core/rules}

  :rules
  [{:cda [:text :table :thead :tr]
    :const {:td ["Vital sign" "Timing" "Value and units"]}}

   {:cda [:text :table :styleCode]
    :const {:border "1" :width "100%"}}

   ...

   {:const "Narrative block"
    :cda [:text :cda/type]}]}

 rules
 {:zen/tags #{aidbox.ccda.core/rules}
  :rules
  [{:cda [:entry.VitalSignsOrganizerV3 :* :component :*
          "VitalSignObservationV2"]
    :apply-rules aidbox.ccda.rules.shared.observation/rules
    :fhir [:entry :**]}

    ...

    }
```

The rules syntax will be explained in the next part.

## Rules DSL syntax

Here is a description of the rules syntax.

Each rule is a map with at least two keys:

```
{:cda [...]
 :fhir [...]}
```

Where `[...]` - is a path in the intermediate tree of an input document. Detailed explanation of the intermediate tree will be provided later.

{% hint style="info" %}
Use the endpoint `/v2/ccda/to-fhir?intermediate=true` to get the intermediate tree for input FHIR-bundle and `/v2/ccda/to-ccda?intermediate=true` to get the one for input C-CDA document.
{% endhint %}

The path is a combination of nodes of the intermediate tree that leeds to the value of some particulare field.

For example, the path

```
[:entry.VitalSignsOrganizerV3 :* :component :* "VitalSignObservationV2" :value]
```

means get a value of the "VitalSignObservationV2" by path in the tree:

```
- :entry.VitalSignsOrganizerV3
  - [
      :component
      - [
          ...
          "VitalSignObservationV2"
            - value
          ...
          ... (some other components/fields)
          ...
      ]

      ...
    ]
```

Symbol `:*` means "for each" expression so `[:component :*]` means "for each element in :component vector".

The table below describes all kind of special symbols:

| Symbol                        | Meaning                                                                                               | Example                                            |
| ----------------------------- | ----------------------------------------------------------------------------------------------------- | -------------------------------------------------- |
| :\*                           | for each                                                                                              | `[:component :*]`                                  |
| 0-N number                    | get N-th element                                                                                      | `[:component 0]`                                   |
| {:resourceType "Observation"} | filtering expression, select all maps where there is a key `:resourceType` with value `"Observation"` | `[:entry.Organizer {:resourceType "Observation"}]` |

## Overriding existing rules

Aidbox users can define conversion rules for specific sections.&#x20;

Existing CCDA rules are displayed in `aidbox/ccda/rules` folder of zen-ui .&#x20;

Let it be Vital Signs section narrative. It is available via `#aidbox.ccda.rules.vital-signs/narrative`. We do not want to display just the most popular observations, but we want to see only those observations in the dataset, without any placeholders.

We create `.yaml` file where we say:

```
aidbox.ccda.rules.vital-signs/narrative:
  replace:
    - cda: [text 0]
      const:
        type: table
        header:
          - Vital sign
          - Date
          - Value and units
    - fhir: [entry *]
      cda: [text 0 rows]
      apply-rules:
        - fhir: [code]
          cda: [0]
        - fhir: [effective]
          cda: [1]
        - fhir: [value]
          cda: [2]
```

`aidbox.ccda.rules.vital-signs/narrative` - means that we are going to override the narrative in Vital Signs namespace `replace` - this instruction means that we substitute all conversion rules to the given in `.yaml` file

If you need to substitute just one rule and leave all the rest there is `select` instruction:

```
aidbox.ccda.rules.encounters/narrative:
  select:
    - selector:
        cda:  [text 0 cols 1]
        fhir: [entry * type 0]
      override:
        cda:  [text 0 cols 1]
        fhir: [entry * class]
```

Code here says that you:

* find the rule in `aidbox.ccda.rules.encounters/narrative` that is equal to the rule declared in `selector`
* substitue the rule that was found to the rule that was declared in `override`

You can see the example of this config [here](https://github.com/Aidbox/aidbox-project-template/tree/aidbox-ccda-custom-rules) .

{% hint style="warning" %}
The work with Override DSL is in progress.
{% endhint %}

## Custom codemaps 

{% hint style="info" %}
Codemap is mapping between OID and URI based codesystems. 
{% endhint %}

If default set of codemaps is not suitable for you for some reason - you may declare your own custom codemaps and 
they will override default ones.

Codemap is basically a hash-map that we use to perform: 
- URI to OID translation for FHIR to C-CDA conversion 
- OID to URI translation for C-CDA to FHIR conversion 

This is URI to OID codemap, URI is a key, OID is a values: 

```
{"http://terminology.hl7.org/CodeSystem/hsloc"    "2.16.840.1.113883.6.259",
 "http://hl7.org/fhir/ValueSet/provider-taxonomy" "2.16.840.1.113883.6.101"}
```

You can see example of codemap [here](https://github.com/Aidbox/aidbox-project-template/blob/aidbox-ccda-custom-rules/custom-rules/custom-codemaps.yaml), it is just a YAML file: 

```
to-ccda:
  - urn: http://hl7.org/fhir/sid/icd-10-cm
    oid: 2.16.840.1.113883.6.90
```

Please take into account that it also requires following steps: 
- CCDA_CUSTOM_CODEMAPS variable is set to path where custom codemaps are located
- path where custom codemaps are located is added to Kubernetes volumes (e.g. [example](https://github.com/Aidbox/aidbox-project-template/blob/aidbox-ccda-custom-rules/docker-compose.yaml) ) 

## DSL description
