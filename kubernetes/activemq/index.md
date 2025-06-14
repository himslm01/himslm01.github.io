# Running an HA ActiveMQ in Kubernetes

## Use case

**As a** software developer with a Kubernetes cluster and some NFS storage,
**I want** to run a Highly Available classic ActiveMQ cluster,
**so that** I can develop and test against STOMP, MQTT and other topic & queue based protocols.

## Proposed Solution

Since have a NAS with shared NFS storage I opted for the shared file-system "master/slave" (as they continue to call it) Classic ActiveMQ configuration, [as documented here](https://activemq.apache.org/shared-file-system-master-slave).

I can make a Kubernetes *Deployment* requesting two (or more) classic ActiveMQ server instances to be run in *Pods*, mounting the NFS storage using *Persistent Volumes*.

## Problems to overcome

There is a wrinkle with Kubernetes Services when running that system.

Kubernetes *Services* are designed to load balance across multiple identical copies of a running application.

In an ActiveMQ cluster running in this configuration there will be a *leader* ActiveMQ server, and one (or more) *standby* ActiveMQ servers. Only the *leader* server binds to the network, listens for connections, and processes the messages on the topics/queues. The *standby* servers wait for the *leader* to fail, and then one will take over as *leader*.

Were a Kubernetes *Deployment* be deployed to create an ActiveMQ cluster in this configuration, with two ActiveMQ *Pods*, the Kubernetes Service would load balance across all of the *Pods*.

This is of no use with the *leader* + *standby* system.

Only one of the *Pods*, the one with the *leader* ActiveMQ instance, would be listening for network connections. 50% of the time the service would be connecting a client to the *standby* *Pod*, which is not listening for network connections.

In [this GitHub repository](https://github.com/himslm01/activemq-kubernetes) I demonstrate a method of making Kubernetes *Services* only route traffic to the *Pod* running the *leader* instance of ActiveMQ.
