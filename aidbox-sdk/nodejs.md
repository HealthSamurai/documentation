# NodeJs SDK

See code example [here](https://github.com/Aidbox/aidbox-node-sdk/blob/master/example/example.js) 

Main function of SDK  - `start` receives context \(`ctx` below\), which describes environment variables, application info, registered subscriptions and operations. It consists of two main parts - environment section and manifest.

{% code title="ctx.js" %}
```javascript
var ctx = {
  env: {
    initUrl: process.env.APP_INIT_URL,
    initClientID: process.env.APP_INIT_CLIENT_ID,
    initClientSecret: process.env.APP_INIT_CLIENT_SECRET,

    appID: APP_ID,
    appUrl: process.env.APP_URL,
    appPort: process.env.APP_PORT,
    appSecret: process.env.APP_SECRET,
  },
  manifest: {
    id: APP_ID,
    type: 'app'
    ...
  }
};
```
{% endcode %}

### Register Operation



Operation response must return an object containing at least a resource property. The status and headers fields are optional. Let's describe new operation - called "report", we will obtain count of Aidbox default resource - Attribute. Our handle will looks like this

{% code title="handler.js" %}
```javascript
const report = (ctx, msg) => 
  ctx
    .query('select count(*) FROM Attribute')
    .then(data => Promise.resolve({
                            resource: {
                              count: data[0].result[0].count 
                            }}));
                            
                            
const reportExtended = (ctx, msg) => 
  ctx
    .query('select count(*) FROM Attribute')
    .then(data => {
      return Promise.resolve({
        resource: {
          count: data[0].result[0].count,
        },
        status: 200,
        headers: {
          "x-additional": "attribute counr",
          "x-extend": "extended report"
        },
      });
  });
```
{% endcode %}

Add it into manifest in operations section and specify `method` - http request method,  `path` - array with path request value, `handler` - link to handler 

### Register Subscription

We have an ability to subscribe to updates of resources. For example let's subscribe for `User` resource and print to console new entities.

{% code title="subscription.js" %}
```javascript
const userSub =(ctx, msg) => {
  console.log('my userSub handler\nctx:', ctx, '\nmsg:', msg);
}
```
{% endcode %}

Add register it into manifest in subscriptions section specifies entity and subscription handler

Our context variable with operation and subscription will looks like this

{% code title="ctx.js" %}
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
      reportExtended: {
        method: 'GET',
        path: ["_report-extended"],
        handler: reportExtended
      }
    }
  }
};
```
{% endcode %}

Then pass `ctx` into `start` method and have fun!

{% code title="index.js" %}
```javascript
const aidbox = require('aidbox-node-sdk');
const ctx = require('./src/ctx');

aidbox.start(ctx)
  .then(() => console.log('connected to server and started'))
  .catch(err => console.log(err.body));
```
{% endcode %}

