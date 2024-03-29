# Træfik in Kubernetes with manual certificate configuration

These examples assume that [Træfik proxy](https://traefik.io/traefik/) is installed and is fully functional, probably using the [install Traefik using the helm chart](https://doc.traefik.io/traefik/getting-started/install-traefik/#use-the-helm-chart) instructions. Do not just follow the [quick start with Kubernetes](https://doc.traefik.io/traefik/getting-started/quick-start-with-kubernetes/) instructions as that does not provide a fully functional Træfik install.

## Manually add a certificate as a *Secret*

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

## Deploy "who am I" app to use the certificate

The following manifest will demonstrate the use of the certificate with a simple [who am I](https://github.com/traefik/whoami) application provided by Træfik, although any other simple HTTP demonstration app can be used as an alternative.

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
      enableServiceLinks: false

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

This manifest is available to download [here](whoami/whoami.yaml). It can be easily reused and adapted, for instance by applying a [Kubernetes kubectl kustomization](https://kubernetes.io/docs/tasks/manage-kubernetes-objects/kustomization/) file, which might look like this:

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

The whoami application should be visible at `https://your.host.name` - assuming any DNS configuration, and network configuration to route or NAT to your Træfik *Service* has been applied.

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

## Deploy Google's "hello world" app to use the certificate

As above, the following manifest will demonstrate the use of the certificate with a simple [hello world](https://github.com/GoogleCloudPlatform/kubernetes-engine-samples/tree/main/quickstarts/hello-app) web application provided by Google.

A *Deployment* of the `gcr.io/google-samples/hello-app` image is created, a *Service* is created exposing the deployment internally within the Kubernetes cluster, and two Træfik *IngressRoute*s are created to inform Træfik of the hostname and path on which to expose the web-application - one for http and one for https. The *Secret* is used the https *IngressRoute* for the TLS encryption, and a Træfik *Middleware* is used to redirect all http connections to https.

### Warning

The Google hello-app does not work on arm/v7 or arm64 - it will not work on a Raspberry Pi.

```yaml
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: hello-app
  labels:
    app: hello-app

spec:
  replicas: 1
  selector:
    matchLabels:
      app: hello-app
  template:
    metadata:
      labels:
        app: hello-app
    spec:
      containers:
        - name: hello-app
          image: "gcr.io/google-samples/hello-app:1.0"
          ports:
            - name: web
              containerPort: 8080
              protocol: TCP
          resources:
            requests:
              cpu: 50m
              memory: 64Mi
            limits:
              cpu: 1000m
              memory: 128Mi
      enableServiceLinks: false

---
apiVersion: v1
kind: Service
metadata:
  name: hello-app

spec:
  selector:
    app: hello-app
  ports:
    - name: web
      port: 80
      targetPort: web

---
apiVersion: traefik.io/v1alpha1
kind: IngressRoute
metadata:
  name: hello-app-web
spec:
  entryPoints:
    - web
  routes:
    - kind: Rule
      match: Host(`hello.example.com`) && PathPrefix(`/`)
      middlewares:
        - name: "hello-app-redirect"
      services:
        # the service will be never called because of the redirect middleware
        - kind: Service
          name: hello-app
          namespace: default
          port: web
          passHostHeader: true

---
apiVersion: traefik.io/v1alpha1
kind: IngressRoute
metadata:
  name: hello-app-websecure
spec:
  entryPoints:
    - websecure
  routes:
    - kind: Rule
      match: Host(`hello.example.com`) && PathPrefix(`/`)
      middlewares:
        - name: "hello-app-redirect"
      services:
        - kind: Service
          name: hello-app
          namespace: default
          port: web
          passHostHeader: true
  tls:
    secretName: hello-app-tls

---
apiVersion: traefik.io/v1alpha1
kind: Middleware
metadata:
  name: hello-app-redirect

spec:
  redirectScheme:
    scheme: https
    permanent: true
```

This manifest is available to download [here](hello-app/hello-app.yaml). As above, it can be easily reused and adapted by applying a Kubernetes kubectl kustomization like this.

```yaml
namespace: default
resources:
  - hello-app.yaml
patches:
  - target:
      group: traefik.io
      version: v1alpha1
      kind: IngressRoute
      name: hello-app-web
    patch: |
      - op: replace
        path: /spec/routes/0/match
        value: Host(`your.host.name`) && PathPrefix(`/`)
      - op: replace
        path: /spec/routes/0/services/0/namespace
        value: default
  - target:
      group: traefik.io
      version: v1alpha1
      kind: IngressRoute
      name: hello-app-websecure
    patch: |
      - op: replace
        path: /spec/routes/0/match
        value: Host(`your.host.name`) && PathPrefix(`/`)
      - op: replace
        path: /spec/routes/0/services/0/namespace
        value: default
```

The hello-app application should be visible at  to `http://<your.host.name>` or `https://<your.host.name>`. If you went to `http` then your browser will be redirected to `https` and you'll securely see the hello app.
