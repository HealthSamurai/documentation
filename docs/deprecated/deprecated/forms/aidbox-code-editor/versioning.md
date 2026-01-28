---
description: Automatic versioning for Aidbox Forms and Workflows with database snapshots based on essential field changes.
---

# Versioning

When `:versioning` feature is enabled - all workflows and forms definitions will be snapshotted to DB.

Version is positive natural number.

> Started from version=1

Versioning is **automatic** and based on hashing essential fields of definitions. If some essential field of form/wf is changed - created a new version and snapshotted to DB.

* Workflow essential fields are:
  * title
  * order
  * items
  * form reference



* Form essential fields are:
  * title
  * inlined document definition
  * inlined layout definition with resolved sub-forms
  * inlined finalize definition.
  * inlined finalize-profile definition.

WF versions just store workflow contents and no information of form istelf, but form reference (zen symbol).

WF and Form snapshots weakly coupled. When WF is started and forms are launched - **documents** will be created and they contains **reference to particular form-version**.

> Weakly coupling of form<->wf versions adds ability to avoid version generation chain reaction.
