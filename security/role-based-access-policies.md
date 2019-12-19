# Role-Based Access Policies

User can interact with system in some context. For example as a Patient. You need resource to store this context information \(i.e. patient\_id or practitioner\_id for example\). The simplest approach store this info in User resource, by extending it or using generic User.data element. This information will be available for AccessPolicies, because user resource is a part of request context. Here is example access policy for patient/user:

```yaml
---
resourceType: User
id: user-1
data:
  patient_id: pt-1

---  
resourceType: Policy
engine: matcho
matcho:
  uri: '#/Patient/.*'
  request-method: get
  params:
    # this is parameter. which is set into resource.id
    resource/id: .user.data.patient_id
```

The problem with this approach can appear, if you want to allow user update his User resource. Or for multi-tenant systems, where users can be part of multiple organisations or departments,  he/she can potentially interact with a system in different roles. You can imagine Outpatient EHR with multiple locations and physicians, who can work in some of this locations. Aidbox provides you with Role resource, which can keep this context information and integrated with Access Control engine in a sophisticated way. Let's say we have multi-organisation system with users, who can play different roles in different organisations.

With new Role resource is intended to help solve such problems. 

```yaml
---
desc: User role
attrs:
  name:
    type: string
    isRequired: true
    search: { name: name, type: string }
  description:
    type: string
  user:
    type: Reference
    isRequired: true
    refers: [ User ]
    search: { name: user, type: reference }
  links:
    attrs:
      patient:
        type: Reference
        refers: [ Patient ]
      practitionerRole:
        type: Reference
        refers: [ PractitionerRole ]
      practitioner:
        type: Reference
        refers: [ Practitioner ]
      organization:
        type: Reference
        refers: [ Organization ]
      person:
        type: Reference
        refers: [ Person ]
      relatedPerson:
        type: Reference
        refers: [ RelatedPerson ]
  context: { isOpen: true }

```

Role  links User and AccessPolicy with `roleName = Role.name`:

```yaml
---
# special role policy
PUT /AccessPolicy/practitioner-role

roleName: practitioner
engine: matcho
matcho:
  uri: '#/Practitioner/.*'
  params:
    # you can access role by .role
    resource/id: .role.links.practitioner.id

---
PUT /Role/pr-u-1

# should match roleName of AccessPolicy
name: practitioner
user: {id: user-1, resourceType: 'User'}
links:
  practitioner: {id: pr-1, resourceType: 'Practitioner'}
```

AccessPolicies with **.roleName** attribute evaluated only if User has roles such as `Role.name=policy.roleName` . Such AccessPolicy can access user role resource as `.role` for macho engine or `{{role...}}` for SQL and JSON engines. 

