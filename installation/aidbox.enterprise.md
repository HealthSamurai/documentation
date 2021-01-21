---
description: Installation instructions for Aidbox.Enterprise
---

# Aidbox.Enterprise

If you are a happy owner of Aidbox.Enterprise and want to deploy it to kubernetes cluster, follow these steps.

### Create credentials for db

Let's create a Secret resource with credentials for the database. The first step is to create an `env-file` like this:

{% code title="aidboxdb" %}
```bash
POSTGRES_USER=postgres
POSTGRES_PASSWORD=yoursecretpasswrod
```
{% endcode %}

Now, let's generate a Secret resource:

```bash
kubectl create secret generic --dry-run -o yaml aidboxdb \
  --from-env-file=aidboxdb > aidboxdb-secret.yaml
```

You will get a file like this which you can put under source control.

{% code title="db.yaml" %}
```yaml
apiVersion: v1
data:
  POSTGRES_PASSWORD: eW91cnNlY3JldHBhc3N3cm9k
  POSTGRES_USER: cG9zdGdyZXM=
kind: Secret
metadata:
  creationTimestamp: null
  name: aidboxdb
```
{% endcode %}

Create a resource in cluster:

```bash
kubectl apply -f aidboxdb-secre.yaml
kubectl get secrets
```

### Deploy aidbox/db

Now we are ready to deploy the database \(custom PostgreSQL build\):

{% code title="aidboxdb.yaml" %}
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
        envFrom:
        - secretRef: 
            name: aidboxdb
        volumeMounts:
          - mountPath: /data
            name: aidboxdb
            subPath: pgdata
      volumes:
        - name: aidboxdb
          persistentVolumeClaim:
            claimName: aidboxdb  
---
# headless service for aidboxdb
apiVersion: v1
kind: Service
metadata:
  name: aidboxdb
  labels:
    service: aidboxdb
spec:
  ports:
  - name: postgresql
    port: 5432
    protocol: TCP
    targetPort: 5432
  selector:
    app: aidboxdb
  sessionAffinity: ClientIP
  clusterIP: None
  type: ClusterIP
```
{% endcode %}

Check that database is up and running:

```bash
kubectl apply -f aidboxdb.yaml
kubectl get pods
kubectl logs -f aidboxdb-0
kubectl exec -it aidboxdb-0 psql postgres
psql:> \l
psql:> \q
```

### Deploy Aidbox.Enterprise

You need to have access to Aidbox.Enterprise docker image - **AIDBOX\_IMAGE** — something like `us.gcr.io/aidbox2-205511/aidbox-enterprise:0.4.6`

{% code title="aidbox.yaml" %}
```yaml
---
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  name: aidbox
  labels:
    system: aidbox
spec:
  replicas: 1
  template:
    metadata:
      labels:
        system: aidbox
    spec:
      containers:
      - name: aidbox
        image: "{{AIDBOX_IMAGE}}"
        imagePullPolicy: Always
        env:
        - name: AIDBOX_CLUSTER_SECRET
          value: "entsecret"
        - name: AIDBOX_CLUSTER_DOMAIN
          value: "fhir.my.io"
        - name: AIDBOX_BASE_URL
          value: "https://fhir.my.io"
        - name: AIDBOX_PORT
          value: "8080"
        - name: PGHOST
          value: aidboxdb
        - name: PGPORT
          value: '5432'
        - name: PGUSER
          value: 'postgres'
        - name: PGUSER
          valueFrom:
            secretKeyRef:
              name: aidboxdb
              key: POSTGRES_USER
        - name: PGPASSWORD
          valueFrom:
            secretKeyRef:
              name: aidboxdb
              key: POSTGRES_PASSWORD
        # if you want login with GITHUB or GOOGLE
        - name: GITHUB_CLIENT_ID
          value: <...>
        - name: GITHUB_CLIENT_SECRET
          value: <....>
        - name: GITHUB_ORGANIZATION
          value: <...>
         - name: GOOGLE_CLIENT_ID
          value: <...>
        - name: GOOGLE_CLIENT_SECRET
          value: <....>
        - name: GOOGLE_ORGANIZATION
          value: <...>
        ports:
        - containerPort: 8080
          protocol: TCP
        readinessProbe:
          httpGet:
            scheme: HTTP
            path: /__healthcheck
            port: 8080
          initialDelaySeconds: 20
          timeoutSeconds: 10
          periodSeconds: 10
          failureThreshold: 30
```
{% endcode %}

Now we can deploy it:

```bash
kubectl apply -f aidbox.yaml
kubectl get pods
kubectl logs -f aidbox-....
```

