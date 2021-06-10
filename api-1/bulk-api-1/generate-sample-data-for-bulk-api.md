# Generate sample data for Bulk API

You can use sample data for executing Bulk API endpoints from related documentation sections.

Sample data contains:

* FHIR citizenship extension
* 3 patients one of which uses the citizenship extension
* 3 appointments with references to patients

{% hint style="success" %}
You can quickly copy sample requests using the icon in the top right corner of code blocks and execute them in the Aidbox REST console. 
{% endhint %}

{% tabs %}
{% tab title="Create Sample data" %}
```yaml
POST /

resourceType: bundle
type: transaction
entry:

- request:
    method: PUT
    url: "/Attribute/Patient.citizenship"
  resource:
    resourceType: Attribute
    description: "The patient's legal status as citizen of a country."
    resource:
      id: Patient
      resourceType: Entity
    path: [citizenship]
    id: Patient.citizenship
    isCollection: true
    extensionUrl: "http://hl7.org/fhir/StructureDefinition/patient-citizenship"
- request:
    method: PUT
    url: "/Attribute/Patient.citizenship.code"
  resource:
    resourceType: Attribute
    description: "Nation code representing the citizenship of patient."
    resource:
      id: Patient
      resourceType: Entity
    path: [citizenship, code]
    id: Patient.citizenship.code
    type: {id: CodeableConcept, resourceType: Entity}
    extensionUrl: "code"

- request:
    method: POST
    url: "/Patient"
  resource:
    id: pt-1
    name:
      - given: ["Alice"]
- request:
    method: POST
    url: "/Patient"
  resource:
    id: pt-2
    name:
      - given: ["Bob"]
- request:
    method: POST
    url: "/Patient"
  resource:
    id: pt-3
    name:
      - given: ["Charles"]
    citizenship:
      - code: {text: "ru"}

- request:
    method: POST
    url: "/Appointment"
  resource:
    id: ap-1
    status: fulfilled
    participant:
      - status: accepted
        actor: 
          resourceType: Patient
          id: pt-1
- request:
    method: POST
    url: "/Appointment"
  resource:
    id: ap-2
    status: booked
    participant:
      - status: accepted
        actor: 
          resourceType: Patient
          id: pt-1
- request:
    method: POST
    url: "/Appointment"
  resource:
    id: ap-3
    status: fulfilled
    participant:
      - status: accepted
        actor: 
          resourceType: Patient
          id: pt-2
```
{% endtab %}
{% endtabs %}

