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



