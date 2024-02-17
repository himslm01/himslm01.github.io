# Upgrading K3s from <1.22 to >= 1.22

## The problem

Træfik v1 is not compatible with Kubernetes v1.22.

K3s < v1.21 came with Træfik v1, k3s >= v1.21 comes with Træfik v2.

If you have not made any configuration changes to Træfik then you can let k3s replace v1 with v2 with about one minute of down time.

In this circumstance, to upgrade from <= v1.22 you can upgrade to k3s v1.21 - which will **not** replace Træfik, tell k3s to remove Træfik, allow k3s re-deploy Træfik, then upgrade to k3s v1.22.

## The solution

For instance - upgrade your cluster to k3s v1.21 (I'm only showing the command for a single node cluster here):

    curl -sfL https://get.k3s.io | INSTALL_K3S_CHANNEL=stable INSTALL_K3S_VERSION=v1.21.8+k3s2 sh -

Stop the k3s service and temporarily start k3s on the command line disabling Træfik:

    sudo systemctl stop k3s.service
    sudo /usr/local/bin/k3s server --disable=traefik

Watch for helm to run a job to uninstall Træfic pod(s), and watch them disappear:

Stop the temporary k3s running in the terminal:

    ctrl-C

Restart the k3s service, and upgrade k3s to v1.22:

    sudo systemctl start k3s.service
    curl -sfL https://get.k3s.io | INSTALL_K3S_CHANNEL=stable INSTALL_K3S_VERSION=v1.22.5+k3s1 sh -
