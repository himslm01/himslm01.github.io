# Harvester

Harvester is an open-source virtualisation platform created by Suse/Rancher. Based on a Kubernetes cluster, Harvester uses kubevert to create virtual machines, and also uses many Suse/Rancher components.

Harvester includes a full-featured web based user interface, but since it is built Kubernetes, the operations can also be performed and monitored using traditional Kubernetes tools.

## Shell access to Harvester

The default `ssh` username on a Harvester cluster is `rancher` and you will have set the password and/or added SSH keys when you installed the cluster.

```console
ssh rancher@harvester-cluster.home
```

## Connect to a Harvester context

To use `kubectl` or `Kubernetes Lens` a context can be created in your ${HOME}.kube/config (or wherever your $KUBECONFIG environment points). The usual context details can be found in the file `/etc/rancher/rke2/rke2.yaml` on your Harvester cluster.

```console
sudo cat /etc/rancher/rke2/rke2.yaml
```

## Set the User Interface TLS certificate

The web UI for Harvester is always transferred securely by TLS, but by default the certificates are self-signed. Harvester provides a Custom Resource Definition to allow you to supply certificates and keys.

### Get the current TLS certificate

By default, there will be no certificate to the result will be empty.

Set the following environment variables appropriately:

- CONTEXT - the name you have given to your Harvester context

```consol
kubectl get settings.harvesterhci.io ssl-certificates \
 -o yaml \
 --context ${CONTEXT}
```

### Set a Lets Encrypt TLS certificate

I have a wild-card TLS certificate which I manually collect from Lets Encrypt every couple of months. I get the certificate manually using **certbot** because the certificate is used in several places, so I have a central script which gets the updated certificate and distributes it to the places its used.

The following script can be used to update thebHarvester UI certificate directly from **certbot**'s store.

Set the following environment variables appropriately:

- CERTBOT_HOME - certbot's home folder
- DOMAIN - the domain (as known by certbot) for the certificate
- CONTEXT - the name you have given to your Harvester context

```bash
#!/bin/bash
set -e

publicCertificate=$(awk 'NF {sub(/\r/, ""); printf "%s\\n",$0;}' ${CERTBOT_HOME}/etc/live/${DOMAIN}/fullchain.pem)
privateKey=$(awk 'NF {sub(/\r/, ""); printf "%s\\n",$0;}' ${CERTBOT_HOME}/etc/live/${DOMAIN}/privkey.pem)

cat << EOF | kubectl apply -f - --context ${CONTEXT}
apiVersion: harvesterhci.io/v1beta1
kind: Setting
metadata:
  name: ssl-certificates
value: >-
  {
    "publicCertificate":"${publicCertificate}",
    "privateKey":"${privateKey}"
  }
EOF
```
