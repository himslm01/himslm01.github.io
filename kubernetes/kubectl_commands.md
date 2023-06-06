# Useful kubectl Commands

## Running

### Run an interactive process

```console
kubectl run -i -t --rm pod-name --image=registry.k8s.io/e2e-test-images/jessie-dnsutils:1.3 --context kube001 --namespace webcamimagedownloader
```

### Run an interactive process on a specific node

```console
kubectl run -i -t --rm pod-name --image=registry.k8s.io/e2e-test-images/jessie-dnsutils:1.3 --context kube001 --namespace webcamimagedownloader --overrides='{"spec": { "nodeSelector": {"kubernetes.io/hostname": "kube004"}}}'
```

## Cron jobs

### View all cron jobs

```console
kubectl get cronjobs --context kube001 --all-namespaces
```

### Disable a cron job

```console
kubectl patch cronjobs cron-job-name -p '{"spec" : {"suspend" : true }}' --context kube001 --namespace webcamimagedownloader
```

### Enable a disabled cron job

```console
kubectl patch cronjobs cron-job-name -p '{"spec" : {"suspend" : false }}' --context kube001 --namespace webcamimagedownloader
```

### Run a job manually based on a cron job

```console
kubectl create job --from=cronjob/webcamimagedownloader job-name --context kube001 --namespace webcamimagedownloader

# view the logs of the job
kubectl logs -f job/job-name --context kube001 --namespace webcamimagedownloader

# OR

# find the pod name the job was allocated
kubectl get pods --context kube001 --namespace webcamimagedownloader

# view the logs of the pod
kubectl logs -f job-name-qt9xf --context kube001 --namespace webcamimagedownloader
```
