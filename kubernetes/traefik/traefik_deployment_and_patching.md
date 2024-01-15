# Træfik deployment and patching

My personal Kubernetes clusters all use the [K3s](https://k3s.io/) distribution of Kubernetes, which includes a fully installed and working Træfik - configured to be ready to serve HTTP, HTTPS, TCP and UDP services.

## Install Træfik using Helm

In my personal opinion, the best way to install Træfik into a cluster is to use the [Helm charts provided by Træfik](https://doc.traefik.io/traefik/getting-started/install-traefik/#use-the-helm-chart) instructions. Do not just follow the [quick start with Kubernetes](https://doc.traefik.io/traefik/getting-started/quick-start-with-kubernetes/) instructions as that does not provide a fully functional Træfik install.


```console
$ helm repo add traefik https://helm.traefik.io/traefik
$ helm repo update
$ helm install
```

Note: if you do not have Helm installed but you do have Docker installed, this is a handy Bash alias:

```bash
alias helm="docker run -it --rm -w $PWD -v $PWD:$PWD -v $HOME/.kube/config:/root/.kube/config -v $HOME/.helm:/root/.helm -v $HOME/.config/helm:/root/.config/helm -v $HOME/.cache/helm:/root/.cache/helm alpine/helm:3.13.3"
```

## Giving the Træfik *Service* an External IP address

The Træfik *Service* is of type *LoadBalancer* so the *Service* needs a cloud controller to assign it an IP address. In my homelab I use Kube-vip, and Kube-vip's cloud controller can directly assign an IP address when the *spec.loadBalancerIP* is set.

### Add a loadBalancer IP

```bash
kubectl patch svc traefik \
  --namespace=kube-system \
  -p '{"spec": {"loadBalancerIP": "10.11.12.13"}}'
```

### Add a loadBalancer IP add add the Træfik dashboard

```bash
kubectl patch svc traefik  \
  --namespace=kube-system \
  -p '{"spec":{"loadBalancerIP": "10.11.12.13","ports": [{"port": 9000,"protocol":"TCP","name":"traefik", "targetPort":"traefik"}]}}'
```

## Using Local externalTrafficPolicy with the Træfik *Service*

Where a load balancer is external to the Kubernetes Cluster, like in many cloud services, Træfik may not be presented with the public IP address of the original client. Depending on the load balancer and the protocols involved, the public IP address of the original client may be passed in an HTTP header.

When the load balancer is within the Kubernetes cluster, like when using Kube-vip or MetalLB, the Træfik *Service* can be configured so that the public IP address of the client should be available to Træfik, and Træfik can make that IP address available in HTTP headers to the applications it routes to.

The trick to make Træfik see the public IP address of the original client is to set the *externalTrafficPolicy* to the value *Local*.

```bash
kubectl patch svc traefik \
  --namespace=kube-system \
  -p '{"spec":{"externalTrafficPolicy":"Local"}}'
```

See the [Kubernetes documentation](https://kubernetes.io/docs/tasks/access-application-cluster/create-external-load-balancer/#preserving-the-client-source-ip) for more information and caveats.
