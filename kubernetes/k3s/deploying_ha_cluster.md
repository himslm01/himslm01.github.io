# Deploying an HA K3s Cluster

Using [Kube-Vip](https://kube-vip.io/) and [K3s](https://k3s.io/) together is an easy way to make a Highly Available Kubernetes cluster. This document describes how I achieved this, and includes my advice for automating the build and deploy process.

## Create config files

The best way to install K3s with Kube-vip is to make some config files up-front and deploy them with an automation system. I have used both cloud-config cloudinit files from a Terraform template and Ansible to deploy these config files. Implementing it all in cloud-config goes against the ethos of using cloud-config for configuring a host, so I advise creating these files using a software deployment tool like Ansible.

### Kernel sysctl settings

#### `/etc/sysctl.d/90-kubelet.conf`

Default Kernel settings advised for running Kubernetes.

```properties
vm.panic_on_oom=0
vm.overcommit_memory=1
kernel.panic=10
kernel.panic_on_oops=1
kernel.keys.root_maxbytes=25000000
```

### K3s config files

#### `/etc/rancher/k3s/config.yaml`

The majority of K3s' install configuration, leaving only a couple of node-based parameters which will be in the K3s install script.

```yaml
cluster-init: true
token: "SECRET_TOKEN"
write-kubeconfig-mode: "0644"
tls-san:
  - "${YOUR_VIP_ADDRESS}"
  - "${YOUR_VIP_HOSTNAME}"
# disable:
#   - "traefik"
```

#### `/etc/rancher/k3s/registries.yaml`

If you have a local Docker mirror:

```yaml
mirrors:
docker.io:
  endpoint:
    - "https://your.local.mirror"
```

### Kube-vip - for K3s HA

#### `/var/lib/rancher/k3s/server/manifests/kube-vip.yaml`

Get the RBAC manifests into a file called `kube-vip.yaml`:

```bash
curl -sL \
  -o kube-vip.yaml \
  "https://kube-vip.io/manifests/rbac.yaml"
```

Append a YAML separator to the end of `kube-vip.yaml`:

```bash
echo "---" >> kube-vip.yaml
```

Append a generated Kube-vip deployment config to the end of `kube-vip.yaml`:

```bash
docker run \
  --network host \
  --rm \
  ghcr.io/kube-vip/kube-vip:$(curl -sL https://api.github.com/repos/kube-vip/kube-vip/releases | jq -r ".[0].name") \
    manifest daemonset \
    --address ${YOUR_VIP_ADDRESS} \
    --interface ${YOUR_VIP_NETWORK_INTERFACE} \
    --inCluster \
    --taint \
    --controlplane \
    --services \
    --arp \
    --leaderElection >> kube-vip.yaml
```

Edit `kube-vip.yaml` to replace:

```yalm
          imagePullPolicy: Always
```

with:

```yalm
          imagePullPolicy: IfNotPresent
```

And finally deploy that file into `/var/lib/rancher/k3s/server/manifests/kube-vip.yaml`.

### Kube-vip cloud-controller for service LoadBalancer

#### `/var/lib/rancher/k3s/server/manifests/kube-vip-cloud-controller.yaml`

Get the Kube-vip cloud-controller manifests into a file called `kube-vip-cloud-controller.yaml`:

```bash
curl -sL \
  -o cloudinit-4_kube-vip-cloud-controller.yaml \
  "https://raw.githubusercontent.com/kube-vip/kube-vip-cloud-provider/main/manifest/kube-vip-cloud-controller.yaml"

```

Edit `kube-vip-cloud-controller.yaml` to replace:

```yalm
          imagePullPolicy: Always
```

with:

```yalm
          imagePullPolicy: IfNotPresent
```

And finally deploy that file into `/var/lib/rancher/k3s/server/manifests/kube-vip-cloud-controller.yaml`.
