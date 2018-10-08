# NodeJs SDK

```javascript
kjj// npm install --add aidbox
const aidbox = require('aidbox');

const report = (ctx, request) => {
  console.log("Request:", request);
  ctx.query("SELECT * FROM payment").then((data)=> {
    ctx.response({body: data});
  });
};

const validate = (ctx, payment) => {
 
};

const on_change = (ctx, payment) => {
 
};

aidbox.manifest({
  addon: 
    {name: 'payments.mydomain.app',
     version: 2},
  resources: {
    Payment: {
      props: {
        amount: {type: 'number'},
        patient: {type: 'Reference'}
      }
    }
  },
  operations: {
    PaymentList: {
      request: ['get', 'Payment', '$report']
    }
  },
  validators: {
    Payment: validate
  },
  subscriptions: {
    Payment: {
      handler: on_change
    }
  }
});

aidbox.start();
```

