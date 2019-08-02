---
description: Installation instructions for Aidbox.Enterprise
---

# Aidbox.Enterprise

If you own  Aidbox.Enterprise and want to deploy it to kubernetes:

### Deploy aidbox/db

```yaml
---
# create volume for database
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: aidboxdb
spec:
  accessModes:
  - ReadWriteOnce
  resources:
    requests:
      storage: 100Gi

---
# deploy db as statefulset
apiVersion: apps/v1
kind: StatefulSet
metadata:
  name: aidboxdb
  labels:
    app: aidboxdb
spec:
  replicas: 1
  serviceName: aidboxdb
  selector:
    matchLabels:
      app: aidboxdb
  template:
    metadata:
      labels:
        app: aidboxdb
    spec:
      containers:
      - image: aidbox/db:11.4.0
        imagePullPolicy: Always
        name: postgres
        ports:
        - containerPort: 5432
          protocol: TCP
        env:
        - name: POSTGRES_USER
          value: <pg-user>
        - name: POSTGRES_PASSWORD
          value: <pg-pass>
        volumeMounts:
          - mountPath: /data
            name: aidboxdb
            subPath: pgdata
      volumes:
        - name: aidboxdb
          persistentVolumeClaim:
            claimName: aidboxdb
```

