# Deploying an HA K3s Cluster

Using [Kube-Vip](https://kube-vip.io/) and [K3s](https://k3s.io/) together is an easy way to make a Highly Available Kubernetes cluster. This document describes how I achieved this, and includes my advice for automating the build and deploy process.

## Create configuration files

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
disable:
  - "servicelb"
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

## Installing K3s

Having created the K3s configuration files, as above, I use the standard [K3s install script](https://docs.k3s.io/quick-start) with some further overriding parameters to install K3s.

### The first node in a cluster

The first node in a cluster will be a server node. It needs to boot-strap the cluster and will startup kube-vip to make the HA control-plane available for all future server and agent nodes in the cluster to communicate with.

Because the file `/etc/rancher/k3s/config.yaml` has been created containing all of the default parameters, all we have to do is to tell the installer that this is a server node.

```bash
curl -sfL https://get.k3s.io | INSTALL_K3S_EXEC="server" sh -s -
```

### Wait for the HA control-plane to be available

You must wait for this K3s service to be up, kube-vip to be installed and advertising the control-plane on TCP port 6443 of the `${YOUR_VIP_ADDRESS}`, and the DNS for `${YOUR_VIP_HOSTNAME}` to be available before you move on to install any other nodes.

```bash
while ! curl --insecure --max-time 2 https://${YOUR_VIP_HOSTNAME}:6443; do sleep 2; done
```

### All future server nodes

The installation of all future server (and agent) nodes needs to refer to the HA control-plane of this cluster. We do that by including the `--server` parameter in the k3s install script.

**Note that if you need to replace the first node in the cluster you will need to use this command, because the cluster has already been boot-strapped and the HA control-plane is already available.**

```bash
curl -sfL https://get.k3s.io | INSTALL_K3S_EXEC="server --server https://${YOUR_VIP_HOSTNAME}:6443" sh -s -
```

## Automatic deployment

### Deploying using Terraform

I have deployed an HA cluster of three server nodes, each of which can also run workloads, using Terraform. I chose to deploy 3 Ubuntu 22.04 server VMs, and included all of the configuration files and the scripts to install K3s in [cloud-init ](https://cloud-init.io/) cloud config files.

Installing this way has the disadvantage that you must deploy the first VM, with the first K3s server node, on its own. You must wait until the HA control-plane is available on that VM before you can start to deploy the other VMs with the other server (or agent) nodes.

This will work well for many Infrastructure as a Service environments, but for others I believe it may be better to deploy the VMs Terraform use another automation tool to deploy K3s.

### Deploying using Terraform and Ansible

I would suggest using Terraform and cloud-init to set the IP addresses and hostnames, deploy the sysctl configuration, create standard users with their SSH keys, and install dependent packages - like `nfs-common` and your VM provider's guest tools.

When the nodes are available I suggest deploying the K3s cluster onto those nodes using Ansible.
