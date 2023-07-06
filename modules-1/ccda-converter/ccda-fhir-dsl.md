---
description: >-
  This page describes how to customize the conversion results.
---

# Overview

The converter is a rule-based service so it allows users to define conversion rules using a special Domain-Specific Language (DSL),
enabling customization and mapping of data elements between C-CDA and FHIR standards.

{% hint style="warning" %}
We are still working on this page, sorry for the inconvenience.
{% endhint %}

{% hint style="info" %}
This part of functionality requires knowledge of zen language.
{% endhint %}

[More details about zen-configuration](https://docs.aidbox.app/aidbox-configuration/aidbox-zen-lang-project/aidbox-configuration-project-structure).

# Mapping rules

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

The reference `:entries aidbox.ccda.rules.vital-signs/rules` points to the file with corresponding mappings.

## Defining new rules

## DSL description
