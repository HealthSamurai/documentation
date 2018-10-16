# Custom Resources

Sometimes your data does not fit any existing FHIR resources. It is not always obvious, that your data can not be translated to FHIR \(because of some FHIR generalisations\). The "right" first step is to go to [FHIR community chat](http://health-samurai.info/a-cusres-to-zulip) and ask your specific question or contact health-samurai modelling team with your concern. If after this adventure you are sure - there is no such resource in FHIR or it will take to much time to wait for it - in aidbox you can define your own **Custom Resources.**

**Custom Resources** are defined exactly the same way as core FHIR resources, can refer existing resources, have uniform REST API for CRUD and Search and participate in transactions.

Let's imagine in our app we want to save User preferences like UI configuration or personalised Patient List filters. We expect you have [installed Aidbox.Dev](https://docs.aidbox.app/~/drafts/-LOrgfiiMwbxfp70_ZP0/primary/v/master/installation/setup-aidbox.dev) or already created your box in [Aidbox.Cloud](https://docs.aidbox.app/~/drafts/-LOrgfiiMwbxfp70_ZP0/primary/v/master/installation/use-aidbox.cloud). First of all we have to define new resource type by creating **Entity** resource.

​

```yaml
POST /Entity​

id: UserSetting
type: resource
isOpen: true
```

