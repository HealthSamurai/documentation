# HA Aidbox

{% hint style="info" %}
Run parallel Aidbox replicas supported from **2208** version
{% endhint %}

### Concept&#x20;

To provide increased High availability, the approach is to run two or more application instances. All incoming traffic is balanced between all running Aidbox instances. In case of failure of one of the instances, the network layer stops receiving incoming traffic to failed instance and distributes it to other available instances. The task of the orchestration system is to detect failure of one of the instances and restart it.

### Configuration

Let's take the Kubernetes example of a hight availability Aidbox configuration (this example can also be applied to Multibox)

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: aidbox
  namespace: production
spec:
  replicas: 2
  selector:
    matchLabels:
      service: aidbox
  template:
    metadata:
      labels:
        service: aidbox
    spec:
      containers:
        - name: main
          image: healthsamurai/aidboxone:latest
          imagePullPolicy: Always
          ports:
            - containerPort: 8080
              protocol: TCP
          envFrom:
            - configMapRef:
                name: aidbox
            - secretRef:
                name: aidbox
          livenessProbe:
            httpGet:
              path: /health
              port: 8080
              scheme: HTTP
            initialDelaySeconds: 20
            timeoutSeconds: 5
            periodSeconds:  5
            successThreshold: 1
            failureThreshold: 4
          readinessProbe:
            httpGet:
              path: /health
              port: 8080
              scheme: HTTP
            initialDelaySeconds: 20
            timeoutSeconds: 5
            periodSeconds: 5
            successThreshold: 1
            failureThreshold: 2
```

#### Replicas&#x20;

First of all you should specify how many replicas you need

```yaml
...
  spec:
    replicas: 2
...
```

#### Readiness probe

Readiness probe - indicates that applications running and ready to receive traffic.

```yaml
readinessProbe:
  httpGet:
    path: /health
    port: 8080
    scheme: HTTP
  initialDelaySeconds: 20
  timeoutSeconds: 5
  periodSeconds:  5
  successThreshold: 1
  failureThreshold: 2
```

#### Liveness probe

Liveness probe - indicates whether the container is running. If the liveness probe fails, the kubelet kills the container, and the container is subjected to its restart policy.

```yaml
livenessProbe:
  httpGet:
    path: /health
    port: 8080
  scheme: HTTP
  initialDelaySeconds: 20
  timeoutSeconds: 5
  periodSeconds:  5
  successThreshold: 1
  failureThreshold: 4

```
