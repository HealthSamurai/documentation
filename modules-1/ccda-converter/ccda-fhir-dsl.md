---
description: >-
  This page describes how to customize the conversion results.
---

# Overview

The converter is a rule-based service so it allows users to define conversion rules using a special Domain-Specific Language (DSL), enabling customization and mapping of data elements between C-CDA and FHIR standards.

{% hint style="warning" %}
We are still working on this page, sorry for the inconvenience.
{% endhint %}

{% hint style="info" %}
This part of functionality requires knowledge of zen language.
{% endhint %}

[More details about zen-configuration](https://docs.aidbox.app/aidbox-configuration/aidbox-zen-lang-project/aidbox-configuration-project-structure).

# Mapping rules

## Service configuration

Here is basic information how to configure zen-project to enable the converter functionality.

## Default rules
Current conversion rules are stored in `.edn` files in the zen project.

The core.edn file describes a top-level configuration of the service:

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

The table above describes all kind of special symbols:

| Symbol                            | Meaning                | Example                                             |
|-----------------------------------|------------------------|-----------------------------------------------------|
|  :*                               | for each               | `[:component :*]`                                   |
|  0-N number                       | get N-th element       | `[:component 0]`                                    |
|  {:resourceType "Observation"}    | filtering expression, select all maps where there is a key `:resourceType` with value `"Observation"` | `[:entry.Organizer {:resourceType "Observation"}]` |

## Defining new rules



## DSL description
