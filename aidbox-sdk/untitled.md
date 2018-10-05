# NodeJs SDK

```javascript
// npm install --add aidbox
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
  name: 'payments.mydomain.app',
  version: 2,
  Resources: {
    Payment: {
      props: {
        amount: {type: 'number'},
        patient: {type: 'Reference'}
      }
    }
  },
  Operations: {
    payment_list: {
      path: ['get', 'Payment', '$report'] ,
      handler: report
    }
  },
  Validators: {
    Payment: validate
  },
  Subscription: {
    Payment: {
      handler: on_change
    }
  }
});

aidbox.start();
```

