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
