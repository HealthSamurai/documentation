# MDM

Aidbox MDM module allows to detect and resolve duplicate records and improves the integrity of your data. Data can be of any resource type.

The MDM module utilizes probabilistic (score-based or Fellegi-Sunter) method. It is more flexible and can provide better results than rule-based approaches, but at the cost of simplicity.

## Train MDM model

The first step to set up Aidbox MDM module is to train model on your data. Model specifies how to find duplicates in the data.

{% content-ref url="train-model-zen.md" %}
[train-model-zen.md](train-model-zen.md)
{% endcontent-ref %}

## Configure MDM module

Configure Aidbox MDM module to use your model

{% content-ref url="configure-mdm-module.md" %}
[configure-mdm-module.md](configure-mdm-module.md)
{% endcontent-ref %}

## Find duplicates

Use `$match` operation to find duplicates

{% content-ref url="find-duplicates.md" %}
[find-duplicates.md](find-duplicates.md)
{% endcontent-ref %}

## How it works

Learn more about mathematics behind probabilistic matching

{% content-ref url="mathematical-details.md" %}
[mathematical-details.md](mathematical-details.md)
{% endcontent-ref %}

## References

### RPC methods

* [aidbox.mdm/update-mdm-tables](../../reference/rpc-reference/aidbox/mdm/aidbox.mdm-update-mdm-tables.md)
* [aidbox.mdm/match](../../reference/rpc-reference/aidbox/mdm/aidbox.mdm-match.md)
