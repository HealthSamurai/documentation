# NodeJs SDK

See code example [here](https://github.com/Aidbox/aidbox-node-sdk/blob/master/example/example.js) 

Main function of SDK  - `start` receives context \(`ctx` below\), which describes environment variables, application info, registered subscriptions and operations. It consists of two main parts - environment section and manifest.

{% code-tabs %}
{% code-tabs-item title="ctx.js" %}
```javascript
var ctx = {
  env: {
    init_url: process.env.APP_INIT_URL,
    ....
  },
  manifest: {
    id: APP_ID,
    type: 'app'
    ...
  }
};
```
{% endcode-tabs-item %}
{% endcode-tabs %}

### Register Operation

Let's describe new operation - called "report", we will obtain count of Aidbox default resource - Attribute. Our handle will looks like this

{% code-tabs %}
{% code-tabs-item title="handler.js" %}
```javascript
const report = (ctx, msg) => 
  ctx
    .query('select count(*) FROM Attribute')
    .then(data => Promise.resolve({ count: data[0].count }));
```
{% endcode-tabs-item %}
{% endcode-tabs %}

Add it into manifest in operations section and specify `method` - http request method,  `path` - array with path request value, `handler` - link to handler 

### Register Subscription

We have an ability to subscribe to updates of resources. For example let's subscribe for `User` resource and print to console new entities.

{% code-tabs %}
{% code-tabs-item title="subscription.js" %}
```javascript
const userSub =(ctx, msg) => {
  console.log('my userSub handler\nctx:', ctx, '\nmsg:', msg);
}
```
{% endcode-tabs-item %}
{% endcode-tabs %}

Add register it into manifest in subscriptions section specifies entity and subscription handler

Our context variable with operation and subscription will looks like this

{% code-tabs %}
{% code-tabs-item title="ctx.js" %}
```javascript
var ctx = {
  env: {
    ...
  },
  manifest: {
    id: APP_ID,
    type: 'app',
    subscriptions: {
      User: {
        handler: userSub
      }
    },
    operations: {
      report: {
        method: 'GET',
        path: ["_report"],
        handler: report
      }
    }
  }
};
```
{% endcode-tabs-item %}
{% endcode-tabs %}

Then pass `ctx` into `start` method and have fun!

{% code-tabs %}
{% code-tabs-item title="index.js" %}
```javascript
const aidbox = require('aidbox-node-sdk');
const ctx = require('./src/ctx');

aidbox.start(ctx)
  .then(() => console.log('connected to server and started'))
  .catch(err => console.log(err.body));
```
{% endcode-tabs-item %}
{% endcode-tabs %}

