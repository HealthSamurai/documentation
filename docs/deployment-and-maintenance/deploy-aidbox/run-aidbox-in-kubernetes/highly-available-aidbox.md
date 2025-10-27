# Highly Available Aidbox

{% hint style="info" %}
Run parallel Aidbox replicas supported from **2208** version
{% endhint %}

### Concept

To provide increased High availability, the approach is to run two or more application instances. All incoming traffic is balanced between all running Aidbox instances. In case of failure of one of the instances, the network layer stops receiving incoming traffic to failed instance and distributes it to other available instances. The task of the orchestration system is to detect failure of one of the instances and restart it.

{% hint style="warning" %}
Attention: by default Aidbox generates both keypair and secret on every startup. This means that on every start all previously generated JWT will be invalid. In order to avoid such undesirable situation, you may pass RSA keypair and secret as Aidbox parameters.

It is required to pass RSA keypair and secret as Aidbox parameters if you have multiple replicas of the same Aidbox/Multibox instance. Check out this section in the docs on how to configure it properly:

[Set up RSA private/public keys and secret](../../../reference/all-settings.md#security.auth.keys.public "mention")
{% endhint %}

### Configuration

Let's take the Kubernetes example of a high availability Aidbox configuration (this example can also be applied to Multibox)

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
      topologySpreadConstraints:
      - maxSkew: 1
        topologyKey: topology.kubernetes.io/zone
        whenUnsatisfiable: ScheduleAnyway
        labelSelector:
          matchLabels:
            service: aidbox
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

#### Replicas

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

#### Startup probe

Startup probe - provide a way to defer the execution of liveness and readiness probes until a container indicates it’s able to handle them. Kubernetes won’t direct the other probe types to a container if it has a startup probe that hasn’t yet succeeded..

```yaml
startupProbe:
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

#### Pod topology

To improve fault tolerance in case of failure of one or more availability zones, you must specify — [Pod Topology Spread Constraints](https://kubernetes.io/docs/concepts/scheduling-eviction/topology-spread-constraints/)

```yaml
topologySpreadConstraints:
- maxSkew: 1
  topologyKey: topology.kubernetes.io/zone
  whenUnsatisfiable: ScheduleAnyway
  labelSelector:
    matchLabels:
      service: aidbox
```
