---
description: Aidbox topic-based subscription resources for real-time event notifications to external systems.
---

# Subscriptions Module Resources

Resources for configuration and management of Aidbox topic-based subscriptions.

 ## AidboxSubscriptionStatus

The status of an AidboxTopicDestination during notifications.

```fhir-structure
[ {
  "path" : "error",
  "name" : "error",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "CodeableConcept",
  "desc" : "List of errors on the subscription."
}, {
  "path" : "eventsSinceSubscriptionStart",
  "name" : "eventsSinceSubscriptionStart",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "decimal",
  "desc" : "Events since the AidboxTopicDestination was created."
}, {
  "path" : "notificationEvent",
  "name" : "notificationEvent",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "Detailed information about any events relevant to this notification."
}, {
  "path" : "notificationEvent.focus",
  "name" : "focus",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to the primary resource or information of this event. \n\n**Allowed references**: Resource"
}, {
  "path" : "notificationEvent.timestamp",
  "name" : "timestamp",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "instant",
  "desc" : "The instant this event occurred."
}, {
  "path" : "notificationEvent.eventNumber",
  "name" : "eventNumber",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "decimal",
  "desc" : "Sequencing index of this event."
}, {
  "path" : "notificationEvent.additionalContext",
  "name" : "additionalContext",
  "lvl" : 1,
  "min" : 0,
  "max" : "*",
  "type" : "Reference",
  "desc" : "References related to the focus resource and/or context of this event. \n\n**Allowed references**: Resource"
}, {
  "path" : "status",
  "name" : "status",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "code",
  "desc" : "requested | active | error | off | entered-in-error. \n\n**Allowed values**: `requested` | `active` | `error` | `off` | `entered-in-error`"
}, {
  "path" : "topic",
  "name" : "topic",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "canonical",
  "desc" : "Reference to the AidboxSubscriptionTopic this notification relates to."
}, {
  "path" : "topic-destination",
  "name" : "topic-destination",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "Reference",
  "desc" : "Reference to the AidboxTopicDestination responsible for this notification. \n\n**Allowed references**: AidboxTopicDestination"
}, {
  "path" : "type",
  "name" : "type",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "handshake | heartbeat | event-notification | query-status | query-event. \n\n**Allowed values**: `handshake` | `heartbeat` | `event-notification` | `query-status` | `query-event`"
} ]
```


## AidboxSubscriptionTopic

Defines the data sources and events that clients can subscribe to. Acts as a configuration that establishes what events matter by specifying which resources and conditions warrant notifications.

```fhir-structure
[ {
  "path" : "approvalDate",
  "name" : "approvalDate",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "date",
  "desc" : "When AidboxSubscriptionTopic is/was approved by publisher."
}, {
  "path" : "contact",
  "name" : "contact",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "ContactDetail",
  "desc" : "Contact details for the publisher."
}, {
  "path" : "copyright",
  "name" : "copyright",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "markdown",
  "desc" : "Use and/or publishing restrictions."
}, {
  "path" : "copyrightLabel",
  "name" : "copyrightLabel",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Copyright holder and year(s)."
}, {
  "path" : "date",
  "name" : "date",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "dateTime",
  "desc" : "Date status first applied."
}, {
  "path" : "derivedFrom",
  "name" : "derivedFrom",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "canonical",
  "desc" : "Based on FHIR protocol or definition."
}, {
  "path" : "description",
  "name" : "description",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "markdown",
  "desc" : "Natural language description of the AidboxSubscriptionTopic."
}, {
  "path" : "effectivePeriod",
  "name" : "effectivePeriod",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Period",
  "desc" : "The effective date range for the AidboxSubscriptionTopic."
}, {
  "path" : "experimental",
  "name" : "experimental",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "If for testing purposes, not real usage."
}, {
  "path" : "identifier",
  "name" : "identifier",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "Identifier",
  "desc" : "Business identifier for subscription topic."
}, {
  "path" : "jurisdiction",
  "name" : "jurisdiction",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "CodeableConcept",
  "desc" : "Intended jurisdiction of the AidboxSubscriptionTopic (if applicable)."
}, {
  "path" : "lastReviewDate",
  "name" : "lastReviewDate",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "date",
  "desc" : "Date the AidboxSubscriptionTopic was last reviewed by the publisher."
}, {
  "path" : "name",
  "name" : "name",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Name for this subscription topic (computer friendly)."
}, {
  "path" : "publisher",
  "name" : "publisher",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "The name of the individual or organization that published the AidboxSubscriptionTopic."
}, {
  "path" : "purpose",
  "name" : "purpose",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "markdown",
  "desc" : "Why this AidboxSubscriptionTopic is defined."
}, {
  "path" : "status",
  "name" : "status",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "code",
  "desc" : "draft | active | retired | unknown. \n\n**Allowed values**: `draft` | `active` | `retired` | `unknown`"
}, {
  "path" : "title",
  "name" : "title",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Name for this subscription topic (human friendly)."
}, {
  "path" : "trigger",
  "name" : "trigger",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "Definition of a resource-based trigger for the subscription topic."
}, {
  "path" : "trigger.event",
  "name" : "event",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "CodeableConcept",
  "desc" : "Event which can trigger a notification from the AidboxSubscriptionTopic."
}, {
  "path" : "trigger.resource",
  "name" : "resource",
  "lvl" : 1,
  "min" : 1,
  "max" : 1,
  "type" : "uri",
  "desc" : "Data Type or Resource (reference to definition) for this trigger definition."
}, {
  "path" : "trigger.canFilterBy",
  "name" : "canFilterBy",
  "lvl" : 1,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "Properties by which a AidboxTopicDestination can filter notifications from the AidboxSubscriptionTopic."
}, {
  "path" : "trigger.canFilterBy.modifier",
  "name" : "modifier",
  "lvl" : 2,
  "min" : 0,
  "max" : "*",
  "type" : "code",
  "desc" : "missing | exact | contains | not | text | in | not-in | below | above | type | identifier | of-type | code-text | text-advanced | iterate."
}, {
  "path" : "trigger.canFilterBy.resource",
  "name" : "resource",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "uri",
  "desc" : "URL of the triggering Resource that this filter applies to."
}, {
  "path" : "trigger.canFilterBy.comparator",
  "name" : "comparator",
  "lvl" : 2,
  "min" : 0,
  "max" : "*",
  "type" : "code",
  "desc" : "eq | ne | gt | lt | ge | le | sa | eb | ap."
}, {
  "path" : "trigger.canFilterBy.description",
  "name" : "description",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "markdown",
  "desc" : "Description of this filter parameter."
}, {
  "path" : "trigger.canFilterBy.filterParameter",
  "name" : "filterParameter",
  "lvl" : 2,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Human-readable and computation-friendly name for a filter parameter usable by subscriptions on this topic."
}, {
  "path" : "trigger.canFilterBy.filterDefinition",
  "name" : "filterDefinition",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "uri",
  "desc" : "Canonical URL for a filterParameter definition."
}, {
  "path" : "trigger.description",
  "name" : "description",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "markdown",
  "desc" : "Text representation of the resource trigger."
}, {
  "path" : "trigger.queryCriteria",
  "name" : "queryCriteria",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "BackboneElement",
  "desc" : "Query based trigger rule."
}, {
  "path" : "trigger.queryCriteria.current",
  "name" : "current",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Rule applied to current resource state."
}, {
  "path" : "trigger.queryCriteria.previous",
  "name" : "previous",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Rule applied to previous resource state."
}, {
  "path" : "trigger.queryCriteria.requireBoth",
  "name" : "requireBoth",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "boolean",
  "desc" : "Both must be true flag."
}, {
  "path" : "trigger.queryCriteria.resultForCreate",
  "name" : "resultForCreate",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "code",
  "desc" : "test-passes | test-fails."
}, {
  "path" : "trigger.queryCriteria.resultForDelete",
  "name" : "resultForDelete",
  "lvl" : 2,
  "min" : 0,
  "max" : 1,
  "type" : "code",
  "desc" : "test-passes | test-fails."
}, {
  "path" : "trigger.fhirPathCriteria",
  "name" : "fhirPathCriteria",
  "lvl" : 1,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "FHIRPath based trigger rule."
}, {
  "path" : "trigger.notificationShape",
  "name" : "notificationShape",
  "lvl" : 1,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "Properties for describing the shape of notifications generated by this topic."
}, {
  "path" : "trigger.notificationShape.include",
  "name" : "include",
  "lvl" : 2,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "Include directives, rooted in the resource for this shape."
}, {
  "path" : "trigger.notificationShape.resource",
  "name" : "resource",
  "lvl" : 2,
  "min" : 1,
  "max" : 1,
  "type" : "uri",
  "desc" : "URL of the Resource that is the focus (main) resource in a notification shape."
}, {
  "path" : "trigger.notificationShape.revInclude",
  "name" : "revInclude",
  "lvl" : 2,
  "min" : 0,
  "max" : "*",
  "type" : "string",
  "desc" : "Reverse include directives, rooted in the resource for this shape."
}, {
  "path" : "trigger.notificationShape.relatedQuery",
  "name" : "relatedQuery",
  "lvl" : 2,
  "min" : 0,
  "max" : "*",
  "type" : "BackboneElement",
  "desc" : "Related queries for notification shaping."
}, {
  "path" : "trigger.notificationShape.relatedQuery.query",
  "name" : "query",
  "lvl" : 3,
  "min" : 1,
  "max" : 1,
  "type" : "string",
  "desc" : "Query string to execute."
}, {
  "path" : "trigger.notificationShape.relatedQuery.queryType",
  "name" : "queryType",
  "lvl" : 3,
  "min" : 0,
  "max" : 1,
  "type" : "Coding",
  "desc" : "Type of query language."
}, {
  "path" : "trigger.supportedInteraction",
  "name" : "supportedInteraction",
  "lvl" : 1,
  "min" : 0,
  "max" : "*",
  "type" : "code",
  "desc" : "create | update | delete. \n\n**Allowed values**: `create` | `update` | `delete`"
}, {
  "path" : "url",
  "name" : "url",
  "lvl" : 0,
  "min" : 1,
  "max" : 1,
  "type" : "uri",
  "desc" : "Canonical identifier for this subscription topic, represented as an absolute URI (globally unique)."
}, {
  "path" : "useContext",
  "name" : "useContext",
  "lvl" : 0,
  "min" : 0,
  "max" : "*",
  "type" : "UsageContext",
  "desc" : "Content intends to support these contexts."
}, {
  "path" : "version",
  "name" : "version",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Business version of the subscription topic."
}, {
  "path" : "versionAlgorithm",
  "name" : "versionAlgorithm",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "",
  "desc" : "How to compare versions."
}, {
  "path" : "versionAlgorithmCoding",
  "name" : "versionAlgorithmCoding",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "Coding",
  "desc" : "How to compare versions."
}, {
  "path" : "versionAlgorithmString",
  "name" : "versionAlgorithmString",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "How to compare versions."
} ]
```


## AidboxTopicDestination

Configures where and how notifications triggered by a subscription topic should be routed. Connects topics to external systems like Kafka, RabbitMQ, Webhook and others.

```fhir-structure
[ {
  "path" : "content",
  "name" : "content",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "empty | id-only | full-resource. \n\n**Allowed values**: `empty` | `id-only` | `full-resource`"
}, {
  "path" : "kind",
  "name" : "kind",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "Channel type for notifications."
}, {
  "path" : "status",
  "name" : "status",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "string",
  "desc" : "requested | active | error | off | entered-in-error."
}, {
  "path" : "topic",
  "name" : "topic",
  "lvl" : 0,
  "min" : 0,
  "max" : 1,
  "type" : "url",
  "desc" : "Reference to the AidboxSubscriptionTopic being subscribed to."
} ]
```

