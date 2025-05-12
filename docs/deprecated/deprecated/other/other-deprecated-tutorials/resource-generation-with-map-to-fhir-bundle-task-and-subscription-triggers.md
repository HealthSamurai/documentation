# Resource generation with map-to-fhir-bundle-task and subscription triggers

{% hint style="warning" %}
Since the 2405 release, using Aidbox in FHIR schema validation engine is recommended, which is incompatible with zen or Entity/Attribute options.

[Setup Aidbox with FHIR Schema validation engine](broken-reference)
{% endhint %}

In this tutorial, we will create a FHIR resource in response to another resource creation using `map-to-fhir-bundle-task` and subscription trigger.

### Define Aidbox Project configuration

In our configuration, we will need the [**subscription trigger service**](../../zen-related/workflow-engine/services.md#subscription-trigger) with **trigger rule**, and [**lisp/mapping**](../../../../modules/integration-toolkit/hl7-v2-integration/mappings-with-lisp-mapping.md).

{% code fullWidth="true" %}
```clojure
{ns     my-subscription-trigger
 import #{aidbox awf.task awf.subscription-trigger ingestion.core lisp}

 ;; Mapping to transform income data (passed from task :context) to fhir Bundle
 ;; get-in and get expressions use the :context task parameter as a place to get data from
 observation-bundle-mapping
 {:zen/tags #{lisp/mapping}
  :mapping   {:resourceType "Bundle"
              :type "transaction"
              :entry [{:resource {:resourceType "Encounter"
                                  :subject (get-in [:subject])
                                  :participant (for [p (get-in [:performer])]
                                                 {:individual {:resourceType (get p :resourceType)
                                                               :id (get p :id)}})
                                  :status "planned"
                                  :class  {:display "Encounter"}}
                       :request {:method "PUT"
                                 :url "/Encounter"}}]}}

 trigger-on-observation-registered
 {:zen/tags    #{awf.subscription-trigger/rule}
 
  ;; When creating an Observation with status 'registered', the rule would be triggered.
  :select-by    [{:get-in [:resourceType] :comparator :eq :value "Observation"}
                 {:get-in [:status] :comparator :eq :value "registered"}]
                 
  ;; When triggering a rule, the map-to-fhir-bundle-task would be executed 
  :task-request {:definition ingestion.core/map-to-fhir-bundle-task
                 :params {
                          ;; Should be a string representation of the mapping definition symbol with the namespace.
                          :mapping "my-subscription-trigger/observation-bundle-mapping"
                          
                          ;; Acts like lisp/expr to get data from the resource that triggered the rule.
                          :context {:resourceId (get-in [:id])
                                    :subject (get-in [:subject])
                                    :performer (get-in [:performer])}
                            
                          ;; Bundle format.        
                          :format "fhir"}}}

 my-subscription-trigger-service
 {:zen/tags #{aidbox/service}
  :engine  awf.subscription-trigger/subscription-trigger-service-engine
  
  ;; Should contain defined rules.
  :rules #{trigger-on-observation-registered}}

 my-box
 {:zen/tags #{aidbox/system}
  :services {:task-service awf.task/task-service
             :executor-service aidbox/aidbox-long-pool-executor-service
             :subscription-trigger-service my-subscription-trigger-service}}}
```
{% endcode %}

Now a new Observation resource is created with the status `registered`, and the Encounter resource with the same `subject` and `performer` as a `participant` is created with the status Planned.
