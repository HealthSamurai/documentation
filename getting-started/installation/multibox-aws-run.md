# AWS Multibox Tutorial

## Create key-pair

```text
aws ec2 create-key-pair --region us-west-2 --key-name test-key-pair
```

## Create your cluster with the following command.

```text
eksctl create cluster \
--name <my-cluster> \
--version <1.20> \
--with-oidc \
--without-nodegroup
```

## Set kubectl context

```text
aws eks --region us-west-2  update-kubeconfig --name test-cluster
```

## Create your nodes with the following command.

```text
eksctl create nodegroup \
  --cluster test-cluster \
  --region us-west-2 \
  --name test-nodegroup \
  --node-type m5.large \
  --nodes 1 \
  --nodes-min 1 \
  --nodes-max 1 \
  --ssh-access \
  --ssh-public-key test-key-pair \
  --managed
```

## View the workloads running on your cluster.

```text
kubectl get pods --all-namespaces -o wide
```

