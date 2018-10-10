---
description: In this tutorial we will see how to define and use Custom Resources
---

# Custom Resources

If you data does not fit existing  FHIR resources - in aidbox you can define your Custom Resources.

You can create meta-resources one by one or use  manifest 

```yaml
resourceType: 'Manifest'
id: payments
resources: 
  Payment:
      properties:
          amount:  {type: 'numeric'}
          patient: {type: 'Reference'}
          datetime: {type: 'dateTime'}
          lines:
             collection: true
             properties:
                 item: {type: 'string'}
```



