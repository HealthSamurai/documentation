# Glossary

## BASE URL

In documentation you can find mention of base url or `[base]`. Sometimes it appears in examples:

```
GET [base]/[type]/[id]{?_format=[mime-type]}
```

Basically, it's an address of the box \(an instance of a FHIR server\), but sometimes it's not presented \(for example in REST Console\):

{% tabs %}
{% tab title="Request" %}
```text
GET /Patient
```
{% endtab %}
{% endtabs %}

But it still presented implicitly and full url will be something like `https://<YOUR-BOX>.aidbox.app/Patient`

You may think that base url is equal to value of [Host http header](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Host). 

