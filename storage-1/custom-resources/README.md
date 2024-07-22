# Custom Resources

Sometimes your data couldn't be modeled using resources provided by FHIR Core specification. FHIR proposes to use Basic resource and create your own extensions and attach them to this Basic resource. Too many extensions may be overwhelming.\
\
Aidbox provides following mechanisms to create custom resource types:

{% content-ref url="custom-resources-using-fhirschema.md" %}
[custom-resources-using-fhirschema.md](custom-resources-using-fhirschema.md)
{% endcontent-ref %}

{% content-ref url="custom-resources-on-fhir-logical-model.md" %}
[custom-resources-on-fhir-logical-model.md](custom-resources-on-fhir-logical-model.md)
{% endcontent-ref %}

{% content-ref url="custom-resources-using-aidbox-project.md" %}
[custom-resources-using-aidbox-project.md](custom-resources-using-aidbox-project.md)
{% endcontent-ref %}

{% content-ref url="getting-started-with-custom-resources.md" %}
[getting-started-with-custom-resources.md](getting-started-with-custom-resources.md)
{% endcontent-ref %}

{% hint style="danger" %}
Entity & Attributes and Zen Schema are planned to be retired and will be replaced by FHIR Schema. Here’s a migration guide to help you transition your custom resources defined via Entity & Attributes / Zen Schema.
{% endhint %}

{% content-ref url="migrate-to-fhirschema/" %}
[migrate-to-fhirschema](migrate-to-fhirschema/)
{% endcontent-ref %}
