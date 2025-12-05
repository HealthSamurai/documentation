---
description: Manage FHIR Questionnaire versions using canonical URLs, version numbers, and status lifecycle (draft, active, retired).
---

# Versioning

FHIR Questionnaire is a definition resource - it can be used for a long period of time (years), and it should support a way for storing changes and enhancements that required by new circumstances. In the same time it must to do that in a safe way - not to break users of the old Questionnaires. FHIR Specification has an answer for this - Canonical Resource versioning.

## Canonical Resource versioning

Questionnaire has an `url`, `version` and `status` properties. `url` and `version` are used as `Questionnaire` uniq identifier - they should be unique globally.

> You should pay attention to make `url` globally unique - to not collide with Questionnaires produced by other organizations.

Different Questionnaire versions share the same `url`.

Questionnaire `status` helps determine it's usage.

FHIR Questionnaire statuses: `draft`, `active`, `retired` and `unknown` (`unknown` should not be used by user)

* `draft` - used when you developing a form
* `active` - used for forms in production - you should be carefull, and do not change these forms (small changes are ok).
* `retired`- used for production retired forms - you also should be carefull about these, they are used for historical reasons

There are main rules - which helps you to stay the right way:

* Questionnaire with the same logical meaning share the same `url`.
* New Questionnare versions should be stored as new (separate) FHIR resources - which share `url`, but differ with `version`.
* `version` property can be empty - but it's not recommended to have more than one form with same `url` but without `version`.
* when form is ready for production use - you should make status = `active`
* Only small changes (like fixing typos) are allowed in `Questionnaire`s in status `active` and `retired`.

## UI Form Builder versioning support

UI Builder gives you an ability to change _version_ related properties and helps you to not screw up by warning your when you done something unsafe.

It warns you when:

* you changed the Questionnaire ID, but a Questionnaire with that ID already exists.
* there is other questionaire with same URL+VERSION - preventing you from doing unconsistency
* you trying to move Questionnaire into status 'active' without URL specified.
* you trying to edit Questionnaire in status 'active' or 'retired'
* you trying to move Questionnaire into status 'draft' from 'active' or 'retired'.
