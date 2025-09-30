# Running an HA ActiveMQ Classic in Kubernetes

## Use case

**As a** software developer with a Kubernetes cluster and some NFS storage,  
**I want** to run a Highly Available ActiveMQ Classic cluster,  
**so that** I can develop and test against STOMP, MQTT and other topic & queue based protocols.

## Proposed Solution

Since have a NAS with shared NFS storage I opted for the shared file-system "master/slave" (as they continue to call it) Classic ActiveMQ configuration, [as documented here](https://activemq.apache.org/shared-file-system-master-slave).

I can make a Kubernetes *Deployment* requesting two (or more) classic ActiveMQ server instances to be run in *Pods*, mounting the NFS storage using *Persistent Volumes*.

## High Availability

The High Availability system used in this solution is ActiveMQ Classic's built-in [Shared File System Master Slave](https://activemq.apache.org/components/classic/documentation/shared-file-system-master-slave) (their words).

When multiple ActiveMQ Classic server nodes use a shared file system, such as NFS v4, to store their persistent data, the first server node will grab an exclusive lock on the filesystem and it will become the active server node. At that point the currently active server node will open it's network connections and ActiveMQ clients will be able to connect to this server node. All the other ActiveMQ Classic server nodes will be waiting for the exclusive lock to be available and will not present open network connections.

When the currently active server node dies it will release the exclusive lock and another waiting server node will grab the exclusive lock and will turn into the currently active server node.

In traditional static ActiveMQ systems the ActiveMQ clients will be given a connection string including all of the ActiveMQ Classic server nodes. The client will try to connect to all of the server nodes in turn. Since only one of the ActiveMQ Classic server nodes will have open network connections the ActiveMQ clients will connect to the currently active server node. When the currently active server node dies the clients will try once again to connect to all of the server nodes in turn and will connect to the new currently active server node.

![Shared File System Master Slave diagram](activemq-shared_file_system_master_slave.svg)

In a dynamic ActiveMQ system running in Kubernetes the ActiveMQ clients cannot know a list of ActiveMQ Classic servers. Instead the ActiveMQ clients are given one Kubernetes Service to connect to.

### Problem

The Kubernetes Service cannot be left to load-balance across all of the ActiveMQ Pods because only one of them will be presenting open network connections.

![Why ActiveMQ Kubernetes won't work diagram](activemq-kubernetes_fail.svg)

The Pods which are not currently active are not listening for connections. Every load-balanced client connection directed to Pods in that state will result in a failed connection.

The Kubernetes Service must be guided to only forward connection requests from the ActiveMQ clients to the Pod running the currently active server node. This is achieved using labels on the Pods.

### Solution

Initially all of the ActiveMQ Classic Pods will have labels of `app=activemq` and `leader=no`.

One of the ActiveMQ Classic servers will have created the exclusive lock on the filesystem and it will become the active server node.

The ActiveMQ Kubernetes Service definition, which guides client connections to servers running in Pods, has a selector which is looking for Pods with the labels of `app=activemq` and `leader=yes`.

So initially no ActiveMQ Classic Pod matches the requirements for the Kubernetes Service to use.

The ActiveMQ Pods consist of two containers.

* one container runs the ActiveMQ Classic server
* a second container runs [this Bash script](https://github.com/himslm01/activemq-kubernetes/blob/main/src/activemq-readiness/wait_until_ready.sh) which constantly tries to connect to the local ActiveMQ Classic server in the Pod

When the script can connect to the ActiveMQ Classic server in its pod, which can only happen when the local ActiveMQ Classic server is the currently active server node, the script changes a label on its pod to be `leader=yes`.

The Kubernetes Service updates the Kubernetes EndPoint, so that Kubernetes can route ActiveMQ client connections to the Pod where the ActiveMQ Classic is running in active server mode.

![working ActiveMQ Kubernetes diagram](activemq-kubernetes.svg)

When the currently active ActiveMQ Classic server node dies, the Pod it was running in will be deleted, meaning once again there is no ActiveMQ Classic Pod matches the requirements for the Kubernetes Service to use.

Kubernetes will start up a new Pod with labels of `app=activemq` and `leader=no`.

One of the waiting ActiveMQ Classic servers will grab the exclusive lock on the filesystem and it will become the active server node. The Bash script in that Pod will update its Pod label from `leader=no` to `leader=yes`.

The Kubernetes Service will once again be able route ActiveMQ client connections to that new currently active server in its Pod.

This process typically takes a few seconds. The ActiveMQ clients should be able to withstand this change-over process.

## Implementation

In [this GitHub repository](https://github.com/himslm01/activemq-kubernetes) I demonstrate:

* building Open Container Initiative images  (otherwise known as Docker images) for ActiveMQ Classic and the side-car Bash script monitoring the state of the ActiveMQ Classic server
* Deploying the system to a Kubernetes Cluster to create a Highly Available ActiveMQ Classic system
