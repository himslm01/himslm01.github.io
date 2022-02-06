# Run Google's hello-world app securely on a default k3s deployment

## Warning

This does not work on arm/v7 or arm64 - it will not work on a Raspberry Pi

## Instalation

Make yourself a k3s cluster using version >= 1.21. It can have one node or many nodes, it doesn't matter. This will give you Træfik v2, which we can leverage to make sure the hello-world app is served securely.

Configure your local DNS so that a hostname points to the IP address of your k3s cluster.

Save the following to a file name `hello-app.yaml`.

Replace the values fo `tls.crt` and `tls.key` with your TLS certificate and key, and replace `host.example.com` with your hostnames.

    ---
    apiVersion: v1
    kind: Namespace
    metadata:
      name: hello-app

    ---
    apiVersion: apps/v1
    kind: Deployment
    metadata:
      name: hello-app
      namespace: hello-app
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
                - name: http
                  containerPort: 8080
                  protocol: TCP
              resources:
                requests:
                  cpu: 50m
                  memory: 64Mi
                limits:
                  cpu: 1000m
                  memory: 128Mi

    ---
    apiVersion: v1
    kind: Service
    metadata:
      name: hello-app
      namespace: hello-app
    spec:
      selector:
        app: hello-app
      ports:
        - name: http
          port: 80
          targetPort: http

    ---
    apiVersion: v1
    kind: Secret
    metadata:
      name: tls-secret
      namespace: hello-app
    data:
      tls.crt: LS0t...LS0K
      tls.key: LS0t...LS0K
    type: kubernetes.io/tls

    ---
    apiVersion: traefik.containo.us/v1alpha1
    kind: Middleware
    metadata:
      name: redirect-https
      namespace: hello-app
    spec:
      redirectScheme:
        scheme: https
        permanent: true

    ---
    apiVersion: networking.k8s.io/v1
    kind: Ingress
    metadata:
      name: hello-app
      namespace: hello-app
      annotations:
        kubernetes.io/ingress.class: traefik
        traefik.ingress.kubernetes.io/router.middlewares: hello-app-redirect-https@kubernetescrd
    spec:
      rules:
        - host: host.example.com
          http:
            paths:
              - path: /
                pathType: Prefix
                backend:
                  service:
                    name: hello-app
                    port:
                      name: http
      tls:
      - hosts:
        - host.example.com
        secretName: tls-secret

Assuming kubectl is already pointing at the right context for your k3s cluster, apply that to your k3s cluster:

    kubectl apply -f hello-app.yaml

This will:

- create a namespace called `hello-app`
- create a deployment in the `hello-app` namespace containing 1 replica of the `hello-app` container
- create a network service in the `hello-app` namespace making the `hello-app` container available internally to the cluster
- create a configuration secret holding your TLS certificate and key
- create a Træfif middleware forcing http to https upgrade
- create a network ingress forwarding all paths of your host to the `hello-app` service, and therefore onto the `hello-app` container.

Navigate to `http://<your-hostname>` or `https://<your-hostname>`. If you went to `http` then your browser will be redirected to `https` and you'll securely see the hello app.
