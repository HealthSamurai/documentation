# Custom Resources

During application development, you may encounter data that does not conform to FHIR standards or cannot be modeled with existing FHIR resources. Despite this, you may still actively use this data in your business logic layer. Naturally, you would want to have the same interface for interacting with these resources as you do with regular FHIR resources.

In this case, you have the following options:

#### FHIR Basic Resource

The FHIR Basic resource is designed to handle concepts that are not yet defined within the FHIR framework. It primarily provides metadata about the resource, with all other data elements expressed using FHIR extensions.

To ensure full interoperability, using the Basic resource imposes certain responsibilities:

* **Maintain Extensions:** You need to manage and update the extensions used in your Basic resource, bump versions in extensions urls/profiles.
* **Publishing Guides:** Consider publishing an ImplementationGuide or a FHIR NPM package with your extensions. This ensures that other FHIR servers can interpret your Basic resource correctly.

#### An additional storage & runtime specific for your custom resources

Using this approach, you create your own storage and runtime for custom resources. While this provides more control and flexibility, it also requires you to implement custom logic for Authorization, API, CRUD operations, search, validation, logging, etc.

#### Define custom resources using FHIR abstractions

Use FHIR StructureDefinition or FHIR Schema to define a custom resource. Although resources defined this way will not be interoperable across different FHIR servers, within the bounds of your application, you can use all available utilities and the regular interface for interactions with these resources. This includes validation, search parameters, CRUD operations, and more.

\
Aidbox provides the following mechanisms to create custom resource types using FHIR abstractions:

{% content-ref url="custom-resources-using-fhirschema.md" %}
[custom-resources-using-fhirschema.md](custom-resources-using-fhirschema.md)
{% endcontent-ref %}

{% content-ref url="custom-resources-on-fhir-logical-model.md" %}
[custom-resources-on-fhir-logical-model.md](custom-resources-on-fhir-logical-model.md)
{% endcontent-ref %}

{% hint style="danger" %}
Entity & Attributes and Zen Schema are planned to be retired and will be replaced by FHIR Schema. Here’s a migration guide to help you transition your custom resources defined via Entity & Attributes / Zen Schema.
{% endhint %}

{% content-ref url="../../deprecated/deprecated/zen-related/aidbox-zen-lang-project/custom-resources-using-aidbox-project.md" %}
[custom-resources-using-aidbox-project.md](../../deprecated/deprecated/zen-related/aidbox-zen-lang-project/custom-resources-using-aidbox-project.md)
{% endcontent-ref %}

{% content-ref url="../../deprecated/deprecated/entity-attribute/getting-started-with-custom-resources.md" %}
[getting-started-with-custom-resources.md](../../deprecated/deprecated/entity-attribute/getting-started-with-custom-resources.md)
{% endcontent-ref %}

{% content-ref url="migrate-to-fhirschema/" %}
[migrate-to-fhirschema](migrate-to-fhirschema/)
{% endcontent-ref %}
