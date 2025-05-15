# Archive/Restore API

Archive/restore API was designed to upload unnecessary resources from Aidbox to AWS or GCP cloud and restore it back when it is needed.

Archive/restore API is represented by several [tasks](../../../deprecated/deprecated/zen-related/workflow-engine/task/README.md):

* [create-archive](create-archive.md)

* [restore-archive](restore-archive.md)

* [delete-archive](delete-archive.md)

* [prune-archived-data](prune-archived-data.md)

You can also use [Scheduler service](../../../deprecated/deprecated/zen-related/workflow-engine/services.md#scheduler) to automatically run create-archive task at the scheduled time. Check this tutorial for more information:

* [Automatically archive AuditEvent resources in GCP storage guide](../../../deprecated/deprecated/other/other-deprecated-tutorials/automatically-archive-auditevent-resources-in-gcp-storage-guide.md)
