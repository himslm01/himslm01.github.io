# Træfik in Kubernetes with manual certificate configuration

These examples assume that [Træfik proxy](https://traefik.io/traefik/) is installed and is fully functional, probably using the [install Traefik using the helm chart](https://doc.traefik.io/traefik/getting-started/install-traefik/#use-the-helm-chart) instructions. Do not just follow the [quick start with Kubernetes](https://doc.traefik.io/traefik/getting-started/quick-start-with-kubernetes/) instructions as that does not provide a fully functional Træfik install.

## Manually add a certificate as a secret

Using most TLS certificate authorities will result in having a **private key** file and a **certificate** file. When using the ACME protocol, for instance with LetsEncrypt, the process ends up with four files in a folder - the files `privkey.pem` and `fullchain.pem` are the two required files.

A Kubernetes **tls *Secret*** manifest file must be created and applied to the correct namespace in your cluster.

This command will create a Kubernetes tls *Secret*, but it **will not** replace a certificate when it already exists.

```bash
kubectl create secret tls ${NAME_OF_SECRET} \
    --namespace ${NAME_OF_NAMESPACE} \
    --key etc/live/lxiv.uk/privkey.pem \
    --cert etc/live/lxiv.uk/fullchain.pem
```

The `kubectl apply` command **will** replace a manifest which is already deployed, so a better form of `kubectl` command is to create the manifest locally, by using the `--dry-run=client -o yaml` command line switches, and pipe that manifest directly into a `kubectl apply` command.

For example - this will create a *Secret* named `whoami-tls` in the `default` namespace - assuming you correctly set the path to the `privkey.pem` and `fullchain.pem` files.

```bash
kubectl create secret tls whoami-tls \
    --namespace default \
    --key ${PATH_TO}/privkey.pem \
    --cert ${PATH_TO}/fullchain.pem \
    --dry-run=client -o yaml \
    | kubectl apply -f -
```

## Use the certificate

The following manifest will demonstrate the use of the certificate in a simple [who am I](https://github.com/traefik/whoami) application provided by Træfik, although any other simple HTTP demonstration app can be used as an alternative.

A *Deployment* of the `traefik/whoami` image is created, a *Service* is created exposing the deployment internally within the Kubernetes cluster, and Træfik *IngressRoute* is created to inform Træfik of the hostname and path on which to expose the web-application, and which *Secret* to use containing the private key and certificate for the TLS encryption.

```yaml
---
kind: Deployment
apiVersion: apps/v1
metadata:
  name: whoami
  labels:
    app: whoami

spec:
  replicas: 1
  selector:
    matchLabels:
      app: whoami
  template:
    metadata:
      labels:
        app: whoami
    spec:
      containers:
        - name: whoami
          image: traefik/whoami
          ports:
            - name: web
              containerPort: 80
          resources:
            limits:
              memory: "200Mi"
              cpu: 300m
            requests:
              memory: "100Mi"
              cpu: 1m

---
apiVersion: v1
kind: Service
metadata:
  name: whoami

spec:
  ports:
    - name: web
      port: 80
      targetPort: web

  selector:
    app: whoami

---
apiVersion: traefik.io/v1alpha1
kind: IngressRoute
metadata:
  name: whoami
spec:
  entryPoints:
    - websecure
  routes:
    - kind: Rule
      match: Host(`whoami.example.com`)
      services:
        - kind: Service
          name: whoami
          namespace: default
          port: web
          passHostHeader: true
  tls:
    secretName: whoami-tls
```

This manifest is available to download [here](whoami.yaml). It can be easily reused and adapted, for instance by applying a [Kubernetes kubectl kustomization](https://kubernetes.io/docs/tasks/manage-kubernetes-objects/kustomization/) file, which might look like this:

```yaml
namespace: default
resources:
  - whoami.yaml
patches:
  - target:
      group: traefik.io
      version: v1alpha1
      kind: IngressRoute
      name: whoami
    patch: |
      - op: replace
        path: /spec/routes/0/match
        value: Host(`your.host.name`) && PathPrefix(`/`)
```

Save that as `kustomization.yaml` into the same folder you saved `whoami.yaml`, replace `your.host.name` with the publicly available hostname in your TLS certificate, and apply the kustomization with this command:

```bash
kubectl kustomize . | kubectl apply -f -
```

The whoami application should be visible at [http://your.host.name](http://your.host.name) - assuming any network configuration to route or NAT to your Træfik serveice has been applied.

### *Ingress* vs *IngressRoute*

In my opinion, a worse alternative to using a Træfik specific [*IngressRoute*](https://doc.traefik.io/traefik/providers/kubernetes-crd/) is using the Kubernetes built-in [*Ingress*](https://kubernetes.io/docs/concepts/services-networking/ingress/).

The biggest problem with a Kubernetes *Ingress* is that the *Ingress* will apply routes to Træfik's service on both web (TCP port 80 - http) and websecure (TCP port 443 - https) *EntryPoints*. Which is unlike Træfik's *IngressRoute*, which binds to specific *EntryPoints*.

An example using an *Ingress* which mimics the *IngressRoute* configuration might look something like this:

```yaml
---
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: whoami

spec:
  tls:
    - hosts:
      - whoami.example.com
      secretName: whoami-tls
  rules:
  - host: whoami.example.com
    http:
      paths:
      - path: /
        pathType: Prefix
        backend:
          service:
            name: whoami
            port:
              name: web
```

Note that Kubernetes have stopped developing any changes to *Ingress* and are developing a new [Gateway API](https://kubernetes.io/docs/concepts/services-networking/gateway/) which Træfik will also support.
