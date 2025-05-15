# Archive/Restore API

Archive/restore API was designed to upload unnecessary resources from Aidbox to AWS or GCP cloud and restore it back when it is needed.

Archive/restore API is represented by several [tasks](../../../deprecated/deprecated/zen-related/workflow-engine/task/README.md):

{% content-ref url="create-archive.md" %}
[create-archive.md](create-archive.md)
{% endcontent-ref %}

{% content-ref url="restore-archive.md" %}
[restore-archive.md](restore-archive.md)
{% endcontent-ref %}

{% content-ref url="delete-archive.md" %}
[delete-archive.md](delete-archive.md)
{% endcontent-ref %}

{% content-ref url="prune-archived-data.md" %}
[prune-archived-data.md](prune-archived-data.md)
{% endcontent-ref %}

You can also use [Scheduler service](../../../deprecated/deprecated/zen-related/workflow-engine/services.md#scheduler) to automatically run create-archive task at the scheduled time. Check this tutorial for more information:

{% content-ref url="../../../deprecated/deprecated/other/other-deprecated-tutorials/automatically-archive-auditevent-resources-in-gcp-storage-guide.md" %}
[automatically-archive-auditevent-resources-in-gcp-storage-guide.md](../../../deprecated/deprecated/other/other-deprecated-tutorials/automatically-archive-auditevent-resources-in-gcp-storage-guide.md)
{% endcontent-ref %}
