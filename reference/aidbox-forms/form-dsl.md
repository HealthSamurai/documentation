# Form DSL

Form DSL used just to bind all DSLs to one item.

```
 VitalsForm
 {:zen/tags #{aidbox.sdc/Form}
  ;; form title
  :title    "Vitals Signs"
  ;; just text version of version
  :version  "1.0.0"
  ;; bind to Document
  :document VitalsDocument
  ;; bind to Layout
  :layout   VitalsLayout
  ;; bind to Launch
  :launch   VitalsLaunch
  ;; bind to Finalize
  :finalize VitalsFinalize}
```

Only `:document` layer is required.
Other layers you add only if you need special logic there.










